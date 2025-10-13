package com.gtocore.common.machine.monitor;

import com.gtocore.mixin.ae2.menu.CraftingStatusMenuAccessor;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.stacks.GenericStack;
import appeng.core.localization.ButtonToolTips;
import appeng.core.localization.GuiText;
import appeng.core.localization.Tooltips;
import appeng.crafting.execution.ElapsedTimeTracker;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.menu.me.crafting.CraftingStatusMenu;
import com.google.common.collect.ImmutableSet;
import com.hepdd.gtmthings.utils.FormatUtil;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MonitorAECPU extends AbstractAEInfoMonitor {

    private static final CraftingStatusMenu.CraftingCpuList EMPTY_CPU_LIST = new CraftingStatusMenu.CraftingCpuList(Collections.emptyList());
    private static final Comparator<CraftingStatusMenu.CraftingCpuListEntry> CPU_COMPARATOR = CraftingStatusMenuAccessor.getCPU_COMPARATOR();
    private static final Comparator<ICraftingCPU> RAW_CPU_COMPARATOR = Comparator
            .comparing((ICraftingCPU cpu) -> cpu.getName() == null)
            .thenComparing(cpu -> cpu.getName() == null ? "" : cpu.getName().getString())
            .thenComparing(Comparator.comparingLong(ICraftingCPU::getAvailableStorage).reversed())
            .thenComparing(Comparator.comparingInt(ICraftingCPU::getCoProcessors).reversed());

    /// 仅服务端
    @Nullable
    private ICraftingCPU selectedCpu = null;

    private CraftingStatusMenu.CraftingCpuList cpuList = EMPTY_CPU_LIST;
    @DescSynced
    @Persisted
    private int selectedCpuSerial = -1;

    public MonitorAECPU(MetaMachineBlockEntity holder) {
        super(holder);
    }

    public MonitorAECPU(Object o) {
        this((MetaMachineBlockEntity) o);
    }

    @DescSynced
    private CompoundTag cpuInfo = new CompoundTag();
    @DescSynced
    private Component cpuName = Component.empty();

    private ImmutableSet<ICraftingCPU> lastCpuSet = ImmutableSet.of();
    private final WeakHashMap<ICraftingCPU, Integer> cpuSerialMap = new WeakHashMap<>();

    private int nextCpuSerial = 1;

    @Override
    public void syncInfoFromServer() {
        var time = (int) Objects.requireNonNull(getLevel(), "Not on the server side").getGameTime();
        if (time - lastUpdateTime < 20) return; // Update every second
        if (!isOnline || getGridNode() == null) {
            state = State.NO_GRID;
            return;
        }
        updateCpus();
        lastUpdateTime = time;
        CompoundTag tag = new CompoundTag();
        tag.putInt("cpuCount", lastCpuSet.size());
        tag.putInt("busyCpuCount", (int) lastCpuSet.stream().filter(ICraftingCPU::isBusy).count());
        if ((selectedCpuSerial >= 0 && !trySelectCpu(selectedCpuSerial)) || !lastCpuSet.contains(selectedCpu)) {
            // If the selected CPU is not valid, reset it
            selectedCpu = null;
            selectedCpuSerial = -1;
        }
        if (selectedCpu != null) {
            cpuName = Objects.requireNonNullElse(selectedCpu.getName(), Component.empty());
            tag.putLong("availableStorage", selectedCpu.getAvailableStorage());
            tag.putLong("coProcessors", selectedCpu.getCoProcessors());
            tag.putInt("selectionMode", selectedCpu.getSelectionMode().ordinal());
            if (selectedCpu instanceof CraftingCPUCluster cpu && cpu.getJobStatus() != null) {
                var item = cpu.craftingLogic.getFinalJobOutput();
                if (item != null) {
                    tag.put("cpuCraftingItem", GenericStack.writeTag(item));
                    var elapsedTimeTracker = cpu.craftingLogic.getElapsedTimeTracker();
                    tag.put("elapsedTimeTracker", elapsedTimeTracker.writeToNBT());
                } else {
                    tag.put("cpuCraftingItem", ItemStack.EMPTY.serializeNBT());
                }
            }
            state = State.NORMAL;
        } else {
            state = State.NO_CONFIG;
        }
        this.cpuInfo = tag;
    }

    @Override
    @SuppressWarnings("all")
    public DisplayComponentList provideInformation() {
        var infoList = super.provideInformation();
        infoList.addIfAbsent(
                DisplayRegistry.AE_CPU_USAGE.id(),
                Component.translatable("gtocore.machine.monitor.ae.cpu.usage",
                        Component.literal(String.valueOf(cpuInfo.getInt("busyCpuCount")))
                                .withStyle(ChatFormatting.DARK_AQUA),
                        Component.literal(String.valueOf(cpuInfo.getInt("cpuCount")))
                                .withStyle(ChatFormatting.GOLD)
                                .withStyle(ChatFormatting.WHITE))
                        .getVisualOrderText());
        if (state != State.NORMAL) {
            return infoList;
        }
        var storage = cpuInfo.getLong("availableStorage");
        if (storage > 0) {
            infoList.addIfAbsent(
                    DisplayRegistry.AE_CPU_MONITORED.id(),
                    Component.translatable("gtocore.machine.monitor.ae.cpu.monitored",
                            selectedCpuSerial,
                            cpuName)
                            .getVisualOrderText());
            infoList.addIfAbsent(
                    DisplayRegistry.AE_CPU_CAPACITY.id(),
                    Component.translatable("gui.tooltips.ae2.CpuStatusStorage",
                            Component.literal(FormatUtil.formatNumber(cpuInfo.getLong("availableStorage"))).withStyle(ChatFormatting.AQUA))
                            .append("   ")
                            .append(cpuInfo.getInt("coProcessors") == 1 ?
                                    Component.translatable("gui.tooltips.ae2.CpuStatusCoProcessor", Component.literal(FormatUtil.formatNumber(cpuInfo.getInt("coProcessors"))).withStyle(ChatFormatting.AQUA)) :
                                    Component.translatable("gui.tooltips.ae2.CpuStatusCoProcessors", Component.literal(FormatUtil.formatNumber(cpuInfo.getInt("coProcessors"))).withStyle(ChatFormatting.AQUA)))
                            .getVisualOrderText());
            infoList.addIfAbsent(
                    DisplayRegistry.AE_CPU_MODE.id(),
                    switch (cpuInfo.getInt("selectionMode")) {
                        case 0 -> Component.translatable("gui.tooltips.ae2.CpuSelectionModeAny").withStyle(ChatFormatting.GREEN).getVisualOrderText();
                        case 1 -> Component.translatable("gui.tooltips.ae2.CpuSelectionModePlayersOnly").withStyle(ChatFormatting.AQUA).getVisualOrderText();
                        case 2 -> Component.translatable("gui.tooltips.ae2.CpuSelectionModeAutomationOnly").withStyle(ChatFormatting.YELLOW).getVisualOrderText();
                        default -> Component.literal("Unknown Mode").withStyle(ChatFormatting.RED).getVisualOrderText();
                    });
            if (cpuInfo.contains("elapsedTimeTracker")) {
                var item = GenericStack.readTag(cpuInfo.getCompound("cpuCraftingItem"));
                if (item != null) {
                    ElapsedTimeTracker elapsedTimeTracker = new ElapsedTimeTracker(cpuInfo.getCompound("elapsedTimeTracker"));
                    final long elapsedTime = elapsedTimeTracker.getElapsedTime();
                    final double remainingItems = elapsedTimeTracker.getRemainingItemCount();
                    final double startItems = elapsedTimeTracker.getStartItemCount();
                    final long eta = (long) (elapsedTime / Math.max(1d, startItems - remainingItems) * remainingItems);
                    String etaTimeText = String.format("%.02f%%", elapsedTimeTracker.getProgress() * 100);
                    if (eta > 0) {
                        final long etaInMilliseconds = TimeUnit.MILLISECONDS.convert(eta, TimeUnit.NANOSECONDS);
                        etaTimeText += " - " +
                                DurationFormatUtils.formatDuration(etaInMilliseconds,
                                        GuiText.ETAFormat.getLocal());
                    }
                    infoList.addIfAbsent(
                            DisplayRegistry.AE_CPU_CURRENT_CRAFTING.id(),
                            Component.translatable("gui.ae2.Crafting",
                                    item.what().getDisplayName()).withStyle(ChatFormatting.LIGHT_PURPLE)
                                    .append(Component.literal("(").withStyle(ChatFormatting.DARK_GRAY)
                                            .append(Tooltips.getAmountTooltip(ButtonToolTips.Amount, item))
                                            .append(")").withStyle(ChatFormatting.DARK_GRAY))
                                    .getVisualOrderText());
                    infoList.addIfAbsent(
                            DisplayRegistry.AE_CPU_CURRENT_PROGRESS.id(),
                            DisplayComponent.progressBar(DisplayRegistry.AE_CPU_CURRENT_PROGRESS.id(), elapsedTimeTracker.getProgress(), etaTimeText));
                }
            }
        }
        return infoList;
    }

    @Override
    public List<ResourceLocation> getAvailableRLs() {
        var rls = super.getAvailableRLs();
        rls.add(DisplayRegistry.AE_CPU_USAGE.id());
        rls.add(DisplayRegistry.AE_CPU_MONITORED.id());
        rls.add(DisplayRegistry.AE_CPU_CAPACITY.id());
        rls.add(DisplayRegistry.AE_CPU_MODE.id());
        rls.add(DisplayRegistry.AE_CPU_CURRENT_CRAFTING.id());
        rls.add(DisplayRegistry.AE_CPU_CURRENT_PROGRESS.id());
        return rls;
    }

    /// 仅服务端
    /// @see CraftingStatusMenu#broadcastChanges()
    private void updateCpus() {
        var network = getGridNode();
        if (network != null) {
            if (!lastCpuSet.equals(network.getGrid().getCraftingService().getCpus())) {
                cpuSerialMap.clear(); // 清除序列号映射
                nextCpuSerial = 1; // 重置序列号
                lastCpuSet = network.getGrid().getCraftingService().getCpus();
                cpuList = createCpuList();
            }
        } else {
            if (!lastCpuSet.isEmpty()) {
                cpuList = EMPTY_CPU_LIST;
                lastCpuSet = ImmutableSet.of();
            }
        }
    }

    @Override
    public Widget createUIWidget() {
        var superWidget = super.createUIWidget();
        var cpuListGui = new CPUListGui(198, 16, 50, 108);
        var panel = new ComponentPanelWidget(
                cpuListGui.getPositionX() + cpuListGui.getSizeWidth() / 2,
                cpuListGui.getPositionY() + 5 + cpuListGui.getSizeHeight(),
                List.of(Component.translatable("gtocore.machine.monitor.ae.cpu.list").withStyle(ChatFormatting.BLACK)))
                .setCenter(true)
                .setClientSideWidget();
        return new WidgetGroup(0, 0, superWidget.getSizeWidth() + 60, superWidget.getSizeHeight())
                .addWidget(superWidget)
                .addWidget(cpuListGui)
                .addWidget(panel);
    }

    /// @see CraftingStatusMenu#createCpuList()
    private CraftingStatusMenu.CraftingCpuList createCpuList() {
        var entries = new ArrayList<CraftingStatusMenu.CraftingCpuListEntry>(lastCpuSet.size());
        for (var cpu : lastCpuSet.stream().sorted(RAW_CPU_COMPARATOR).toList()) {
            var serial = getOrAssignCpuSerial(cpu);
            var status = cpu.getJobStatus();
            var progress = 0f;
            if (status != null && status.totalItems() > 0) {
                progress = (float) (status.progress() / (double) status.totalItems());
            }
            entries.add(new CraftingStatusMenu.CraftingCpuListEntry(
                    serial,
                    cpu.getAvailableStorage(),
                    cpu.getCoProcessors(),
                    cpu.getName(),
                    cpu.getSelectionMode(),
                    status != null ? status.crafting() : null,
                    progress,
                    status != null ? status.elapsedTimeNanos() : 0));
        }
        entries.sort(CPU_COMPARATOR);
        return new CraftingStatusMenu.CraftingCpuList(entries);
    }

    /// 保证每个CPU都有一个唯一的序列号
    /// @see CraftingStatusMenu#getOrAssignCpuSerial(ICraftingCPU)
    private int getOrAssignCpuSerial(ICraftingCPU cpu) {
        return cpuSerialMap.computeIfAbsent(cpu, ignored -> nextCpuSerial++);
    }

    /// 服务端用于更改选中的CPU
    private boolean trySelectCpu(int serial) {
        updateCpus();
        if (serial < 0) {
            selectedCpu = null;
            selectedCpuSerial = -1;
            return false;
        }
        for (var cpu : lastCpuSet) {
            if (getOrAssignCpuSerial(cpu) == serial) {
                selectedCpu = cpu;
                selectedCpuSerial = serial;
                break;
            }
        }
        return true;
    }

    private class CPUListGui extends WidgetGroup {

        private static final int PACKET_ID = 0x01;
        private DraggableScrollableWidgetGroup scrollArea;
        private final Int2ObjectOpenHashMap<SerialWidget> serialWidgets = new Int2ObjectOpenHashMap<>();

        private CPUListGui(int x, int y, int width, int height) {
            super(x, y, width, height);
            this.setSize(width, height);
            this.setBackground(GuiTextures.BACKGROUND_INVERSE);
            // if (!isRemote()) {
            // writeUpdateInfo(PACKET_ID, buf -> {
            // cpuList.writeToPacket(buf);
            // buf.writeInt(selectedCpuSerial);
            // });
            // }
            init();
        }

        /// 客户端只在打开GUI的时候才需要知道当前的cpu列表
        /// 利用gui的方式同步CPU列表到客户端
        @Override
        public void writeInitialData(FriendlyByteBuf buffer) {
            super.writeInitialData(buffer);
            cpuList.writeToPacket(buffer);
        }

        @Override
        public void readInitialData(FriendlyByteBuf buffer) {
            super.readInitialData(buffer);
            cpuList = new CraftingStatusMenu.CraftingCpuList(buffer);
            init();
        }

        @Override
        public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
            if (id == PACKET_ID) {
                cpuList = new CraftingStatusMenu.CraftingCpuList(buffer);
                selectedCpuSerial = buffer.readInt();
            } else {
                super.readUpdateInfo(id, buffer);
            }
        }

        private void init() {
            // 初始化GUI组件
            // 这里可以添加显示CPU列表的组件
            // 例如：创建一个滚动区域来显示cpuList中的每个CPU信息
            boolean firstInit = this.scrollArea == null;
            if (firstInit) {
                scrollArea = new DraggableScrollableWidgetGroup(4, 4, this.getSizeWidth() - 8, this.getSizeHeight() - 8);
                this.addWidget(scrollArea);
                scrollArea.setScrollable(true);
                scrollArea.setYBarStyle(GuiTextures.BACKGROUND, GuiTextures.BOX_OVERLAY);
            } else {
                scrollArea.clearAllWidgets();
            }
            int y = 2;
            for (var cpu : cpuList.cpus()) {
                var color = cpu.serial() == selectedCpuSerial ? ChatFormatting.GREEN : ChatFormatting.WHITE;
                var cpuWidgets = serialWidgets.computeIfAbsent(cpu.serial(), serial -> new SerialWidget(serial, new LabelWidget(), new ButtonWidget()));
                var cpuWidget = cpuWidgets.label();
                cpuWidget.setSelfPosition(5, y);
                cpuWidget.setClientSideWidget();
                cpuWidget.setHoverTooltips(createCpuTooltips(cpu));
                cpuWidget.setComponent(Component.literal("#" + cpu.serial()).withStyle(color));
                var button = cpuWidgets.button().setButtonTexture(GuiTextures.BUTTON);
                button.setSelfPosition(getSizeWidth() - 20, y);
                button.setSize(10, 10);
                button.setOnPressCallback((click) -> {
                    if (!isRemote() &&
                            trySelectCpu(cpu.serial())) {
                        init();
                        writeUpdateInfo(PACKET_ID, buf -> {
                            cpuList.writeToPacket(buf);
                            buf.writeInt(cpu.serial());
                        });
                    }
                });
                button.setHoverTooltips(
                        Component.translatable("gtocore.machine.monitor.ae.cpu.set.1"),
                        Component.translatable("gtocore.machine.monitor.ae.cpu.set.2"));
                scrollArea.acceptWidget(button);
                scrollArea.acceptWidget(cpuWidget);
                y += 10;
            }
        }

        private static MutableComponent getHashedComponent(CraftingStatusMenu.CraftingCpuListEntry cpu) {
            var cpuName = Component.literal("CPU #" + cpu.serial());
            if (cpu.name() != null) {
                cpuName = cpuName.append(" - ").append(cpu.name());
            }
            return cpuName;
        }

        private List<Component> createCpuTooltips(CraftingStatusMenu.CraftingCpuListEntry cpu) {
            List<Component> tooltips = new ArrayList<>();
            tooltips.add(getHashedComponent(cpu).withStyle(ChatFormatting.GOLD));
            tooltips.add(
                    Component.translatable("gui.tooltips.ae2.CpuStatusStorage",
                            Component.literal(FormatUtil.formatNumber(cpu.storage())).withStyle(ChatFormatting.AQUA)));
            tooltips.add(cpu.coProcessors() == 1 ?
                    Component.translatable("gui.tooltips.ae2.CpuStatusCoProcessor", Component.literal(FormatUtil.formatNumber(cpu.coProcessors())).withStyle(ChatFormatting.AQUA)) :
                    Component.translatable("gui.tooltips.ae2.CpuStatusCoProcessors", Component.literal(FormatUtil.formatNumber(cpu.coProcessors())).withStyle(ChatFormatting.AQUA)));
            tooltips.add(switch (cpu.mode()) {
                case PLAYER_ONLY -> Component.translatable("gui.tooltips.ae2.CpuSelectionModePlayersOnly").withStyle(ChatFormatting.AQUA);
                case ANY -> Component.translatable("gui.tooltips.ae2.CpuSelectionModeAny").withStyle(ChatFormatting.GREEN);
                case MACHINE_ONLY -> Component.translatable("gui.tooltips.ae2.CpuSelectionModeAutomationOnly").withStyle(ChatFormatting.YELLOW);
            });
            if (cpu.currentJob() != null) {
                tooltips.add(Component.translatable("gui.tooltips.ae2.CpuStatusCrafting",
                        cpu.currentJob().what().getDisplayName()));
            } else tooltips.add(Component.translatable("gtceu.multiblock.idling"));
            return tooltips;
        }

        private record SerialWidget(
                                    int serial,
                                    LabelWidget label,
                                    ButtonWidget button) {}
    }
}

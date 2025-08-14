package com.gtocore.common.machine.monitor;

import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEFluidList;
import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEItemList;
import com.gtocore.common.machine.multiblock.part.ae.widget.AEFluidConfigWidget;
import com.gtocore.common.machine.multiblock.part.ae.widget.AEItemConfigWidget;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.PowerSubstationMachine;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.stacks.AEKey;
import com.google.common.collect.ImmutableList;
import com.hepdd.gtmthings.api.misc.EnergyStat;
import com.hepdd.gtmthings.utils.FormatUtil;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class MonitorAEThroughput extends AbstractAEInfoMonitor {

    @DescSynced
    private CompoundTag displayingEntry = new CompoundTag();
    private final EnergyStat[] stats = new EnergyStat[2];
    private final long[] lastAmount = new long[] { 0, 0 };
    @Persisted
    private final AEItem aeItem = new AEItem();
    @Persisted
    private final AEFluid aeFluid = new AEFluid();
    @DescSynced
    private long[] currentAmount = new long[] { 0, 0 };
    @DescSynced
    private long[] lastMinuteStat = new long[] { 0, 0 };
    @DescSynced
    private long[] lastHourStat = new long[] { 0, 0 };
    @DescSynced
    private long[] lastDayStat = new long[] { 0, 0 };
    @DescSynced
    private long[] nowStat = new long[] { 0, 0 };
    private final CurrentGettable[] aeItemFluidGettables = new CurrentGettable[] { aeItem, aeFluid };

    public MonitorAEThroughput(MetaMachineBlockEntity holder) {
        super(holder);
    }

    public MonitorAEThroughput(Object o) {
        this((MetaMachineBlockEntity) o);
    }

    @Override
    public void syncInfoFromServer() {
        var time = (int) Objects.requireNonNull(getLevel(), "Not on the server side").getGameTime();
        if (time - lastUpdateTime < 40) return; // Update every 2 seconds
        lastUpdateTime = time;
        var grid = nodeHolder.getMainNode().getGrid();
        if (grid == null || !isOnline()) {
            state = State.NO_GRID;
            return;
        }
        var hasConfig = false;
        displayingEntry = new CompoundTag();
        for (int i = 0; i < 2; i++) {
            var current = aeItemFluidGettables[i].getCurrent();
            if (current == null) {
                // displayingName[i] = Component.empty();
                continue;
            }
            hasConfig = true;
            long amount = grid.getStorageService().getCachedInventory().get(current);
            var change = amount - lastAmount[i];
            if (stats[i] == null) {
                stats[i] = new EnergyStat(lastUpdateTime);
            }
            if (change != 0) {
                // No zero update otherwise ArithmeticException will be thrown
                stats[i].update(BigInteger.valueOf(change), lastUpdateTime);
            }
            lastAmount[i] = currentAmount[i];
            currentAmount[i] = amount;

            lastMinuteStat[i] = stats[i].getMinuteAvg().longValue();
            lastHourStat[i] = stats[i].getHourAvg().longValue();
            lastDayStat[i] = stats[i].getDayAvg().longValue();

            nowStat[i] = change;
            // displayingName[i] = current.getDisplayName();
            displayingEntry.put(String.valueOf(i), current.toTagGeneric());
        }
        state = hasConfig ? State.NORMAL : State.NO_CONFIG;
    }

    private static final ImmutableList<DisplayRegistry> ID_MAP = ImmutableList.of(
            DisplayRegistry.AE_STATUS_0,
            DisplayRegistry.AE_AMOUNT_0,
            DisplayRegistry.AE_STAT_MINUTE_0,
            DisplayRegistry.AE_STAT_HOUR_0,
            DisplayRegistry.AE_STAT_DAY_0,
            DisplayRegistry.AE_STAT_REMAINING_TIME_0,
            DisplayRegistry.AE_STATUS_1,
            DisplayRegistry.AE_AMOUNT_1,
            DisplayRegistry.AE_STAT_MINUTE_1,
            DisplayRegistry.AE_STAT_HOUR_1,
            DisplayRegistry.AE_STAT_DAY_1,
            DisplayRegistry.AE_STAT_REMAINING_TIME_1);

    @Override
    public List<ResourceLocation> getAvailableRLs() {
        var rls = super.getAvailableRLs();
        rls.add(DisplayRegistry.AE_STAT_TITLE.id());
        rls.addAll(ID_MAP.stream()
                .map(DisplayRegistry::id)
                .toList());
        return rls;
    }

    @Override
    public DisplayComponentList provideInformation() {
        var infoList = super.provideInformation();
        if (state == State.NORMAL) {
            for (int i = 0; i < 2; i++) {
                if (displayingEntry.contains(String.valueOf(i))) {
                    final var unit = i == 0 ? 80 : 80000; // Items are in units, fluids are in mB
                    var atomI = new AtomicInteger(i == 0 ? 0 : 6);
                    final BiFunction<Long, ChatFormatting, Component> formatter = getAmountFormatter(i, unit);
                    var itemId = ID_MAP.get(atomI.getAndIncrement()).id();
                    var itemKey = AEKey.fromTagGeneric(displayingEntry.getCompound(String.valueOf(i)));
                    if (itemKey != null) {
                        infoList.addIfAbsent(
                                itemId,
                                Component.translatable("gtocore.machine.monitor.ae.status." + i,
                                        itemKey.getDisplayName().copy().withStyle(ChatFormatting.AQUA)).getVisualOrderText());

                    }

                    infoList.addIfAbsent(
                            ID_MAP.get(atomI.getAndIncrement()).id(),
                            Component.translatable("gtocore.machine.monitor.ae.amount",
                                    Component.literal(FormatUtil.formatNumber(currentAmount[i] / unit * 80))
                                            .withStyle(ChatFormatting.GOLD)
                                            .append(Component.literal(i == 0 ? "" : "B").withStyle(ChatFormatting.GRAY))
                                            .append(Component.literal("(").withStyle(ChatFormatting.WHITE)
                                                    .append(formatter.apply(nowStat[i], ChatFormatting.DARK_PURPLE))
                                                    .append(Component.literal("/t").withStyle(ChatFormatting.GRAY))
                                                    .append(")").withStyle(ChatFormatting.WHITE)))
                                    .getVisualOrderText());
                    infoList.addIfAbsent(
                            DisplayRegistry.AE_STAT_TITLE.id(),
                            Component.translatable("gtocore.machine.monitor.ae.stat.title").getVisualOrderText());
                    infoList.addIfAbsent(
                            ID_MAP.get(atomI.getAndIncrement()).id(),
                            Component.translatable("gtocore.machine.monitor.ae.stat.minute",
                                    formatter.apply(lastMinuteStat[i], ChatFormatting.DARK_AQUA)).getVisualOrderText());
                    infoList.addIfAbsent(
                            ID_MAP.get(atomI.getAndIncrement()).id(),
                            Component.translatable("gtocore.machine.monitor.ae.stat.hour",
                                    formatter.apply(lastHourStat[i], ChatFormatting.YELLOW)).getVisualOrderText());
                    infoList.addIfAbsent(
                            ID_MAP.get(atomI.getAndIncrement()).id(),
                            Component.translatable("gtocore.machine.monitor.ae.stat.day",
                                    formatter.apply(lastDayStat[i], ChatFormatting.DARK_GREEN)).getVisualOrderText());
                    if (nowStat[i] < 0) {
                        var absSec = Math.abs(nowStat[i]) * 20;
                        infoList.addIfAbsent(
                                ID_MAP.get(atomI.getAndIncrement()).id(),
                                Component.translatable("gtocore.machine.monitor.ae.stat.remaining_time",
                                        PowerSubstationMachine.getTimeToFillDrainText(BigInteger.valueOf(currentAmount[i] / absSec)).withStyle(ChatFormatting.GRAY)).getVisualOrderText());
                    }
                }
            }
        }
        return infoList;
    }

    private @NotNull BiFunction<Long, ChatFormatting, Component> getAmountFormatter(int i, int unit) {
        return (num, color) -> {
            var sign = nowStat[i] >= 0 ? "+" : "-";
            if (num % unit != 0) sign += "~";
            return Component.literal(sign).withStyle(color)
                    .append(Component.literal(FormatUtil.formatNumber(Math.abs(num) / unit))
                            .withStyle(color))
                    .append(Component.literal(i == 0 ? "" : "B").withStyle(ChatFormatting.GRAY));
        };
    }

    @Override
    public Widget createUIWidget() {
        var superWidget = super.createUIWidget();
        var itemWidget = new AEItemConfigWidget(50, 186, aeItem);
        var fluidWidget = new AEFluidConfigWidget(132, 186, aeFluid);
        var panel = new ComponentPanelWidget(
                96 + fluidWidget.getSizeWidth(), 171,
                List.of(Component.translatable("gtocore.machine.monitor.ae.set_filter").withStyle(ChatFormatting.BLACK)))
                .setCenter(true)
                .setClientSideWidget();
        return (new WidgetGroup(0, 0, 200, 216)).addWidget(superWidget).addWidget(itemWidget).addWidget(fluidWidget).addWidget(panel);
    }

    private class AEItem extends ExportOnlyAEItemList implements CurrentGettable {

        public AEItem() {
            super(MonitorAEThroughput.this, 1);
        }

        public @Nullable AEKey getCurrent() {
            return getInventory()[0].getConfig() == null ? null : getInventory()[0].getConfig().what();
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            return ItemStack.EMPTY;
        }
    }

    private class AEFluid extends ExportOnlyAEFluidList implements CurrentGettable {

        public AEFluid() {
            super(MonitorAEThroughput.this, 1);
        }

        public @Nullable AEKey getCurrent() {
            return getInventory()[0].getConfig() == null ? null : getInventory()[0].getConfig().what();
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return FluidStack.EMPTY;
        }
    }

    private interface CurrentGettable {

        @Nullable
        AEKey getCurrent();
    }
}

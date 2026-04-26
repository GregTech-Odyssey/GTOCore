package com.gtocore.common.machine.monitor;

import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEFluidList;
import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEItemList;
import com.gtocore.common.machine.multiblock.part.ae.widget.AEFluidConfigWidget;
import com.gtocore.common.machine.multiblock.part.ae.widget.AEItemConfigWidget;

import com.gtolib.api.ae2.IExpandedStorageService;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.PowerSubstationMachine;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.AmountFormat;

import com.google.common.collect.ImmutableList;
import com.hepdd.gtmthings.api.misc.EnergyStat;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class MonitorAEThroughput extends AbstractAEInfoMonitor {

    private static final BigDecimal DISPLAY_SCALE = BigDecimal.valueOf(100);
    private static final DecimalFormat THROUGHPUT_FORMAT = new DecimalFormat("0.##");

    @DescSynced
    private CompoundTag displayingEntry = new CompoundTag();
    private final EnergyStat[] stats = new EnergyStat[2];
    private final long[] lastAmount = new long[] { 0, 0 };
    @Persisted
    private final AEItem aeItem = new AEItem();
    @Persisted
    private final AEFluid aeFluid = new AEFluid();
    @DescSynced
    private final long[] currentAmount = new long[] { 0, 0 };
    @DescSynced
    private final long[] lastMinuteStat = new long[] { 0, 0 };
    @DescSynced
    private final long[] lastHourStat = new long[] { 0, 0 };
    @DescSynced
    private final long[] lastDayStat = new long[] { 0, 0 };
    @DescSynced
    private final long[] nowStat = new long[] { 0, 0 };
    private final CurrentGettable[] aeItemFluidGettables = new CurrentGettable[] { aeItem, aeFluid };

    public MonitorAEThroughput(MetaMachineBlockEntity holder) {
        super(holder);
        aeItem.addChangedListener(() -> onFilterChanged(0));
        aeFluid.addChangedListener(() -> onFilterChanged(1));
    }

    public MonitorAEThroughput(Object o) {
        this((MetaMachineBlockEntity) o);
    }

    @Override
    public void syncInfoFromServer() {
        var time = (int) Objects.requireNonNull(getLevel(), "Not on the server side").getGameTime();
        if (lastUpdateTime == 0) {
            lastUpdateTime = time;
        }
        var elapsedTicks = time - lastUpdateTime;
        if (elapsedTicks < 40) return; // Update every 2 seconds
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
            long amount = IExpandedStorageService.of(grid.getStorageService()).getLazyKeyCounter().get(current);
            if (stats[i] == null) {
                stats[i] = new EnergyStat(time);
                stats[i].update(BigInteger.ZERO, time);
                lastAmount[i] = amount;
                currentAmount[i] = amount;
                lastMinuteStat[i] = 0;
                lastHourStat[i] = 0;
                lastDayStat[i] = 0;
                nowStat[i] = 0;
                displayingEntry.put(String.valueOf(i), current.toTagGeneric());
                continue;
            }
            var change = amount - lastAmount[i];
            stats[i].update(BigInteger.valueOf(change), time);
            for (int tick = 20; tick <= elapsedTicks; tick += 20) {
                stats[i].tick();
            }
            lastAmount[i] = amount;
            currentAmount[i] = amount;

            lastMinuteStat[i] = scaleStat(stats[i].minute.getAvgByTick());
            lastHourStat[i] = scaleStat(stats[i].hour.getAvgByTick());
            lastDayStat[i] = scaleStat(stats[i].day.getAvgByTick());

            nowStat[i] = elapsedTicks > 0 ? scaleStat(BigDecimal.valueOf(change)
                    .divide(BigDecimal.valueOf(elapsedTicks), 2, RoundingMode.HALF_UP)) : 0;
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
                    var atomI = new AtomicInteger(i == 0 ? 0 : 6);
                    var itemId = ID_MAP.get(atomI.getAndIncrement()).id();
                    var itemKey = AEKey.fromTagGeneric(displayingEntry.getCompound(String.valueOf(i)));
                    if (itemKey != null) {
                        final BiFunction<Long, ChatFormatting, MutableComponent> formatter = getAmountFormatter(itemKey);
                        infoList.addIfAbsent(
                                itemId,
                                Component.translatable("gtocore.machine.monitor.ae.status." + i,
                                        itemKey.getDisplayName().copy().withStyle(ChatFormatting.AQUA)).getVisualOrderText());
                        infoList.addIfAbsent(
                                ID_MAP.get(atomI.getAndIncrement()).id(),
                                Component.translatable("gtocore.machine.monitor.ae.amount",
                                        Component.literal(itemKey.formatAmount(currentAmount[i], AmountFormat.SLOT))
                                                .withStyle(ChatFormatting.GOLD)
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
                                        formatter.apply(lastMinuteStat[i], ChatFormatting.DARK_AQUA)
                                                .append(Component.literal("/t").withStyle(ChatFormatting.GRAY)))
                                        .getVisualOrderText());
                        infoList.addIfAbsent(
                                ID_MAP.get(atomI.getAndIncrement()).id(),
                                Component.translatable("gtocore.machine.monitor.ae.stat.hour",
                                        formatter.apply(lastHourStat[i], ChatFormatting.YELLOW)
                                                .append(Component.literal("/t").withStyle(ChatFormatting.GRAY)))
                                        .getVisualOrderText());
                        infoList.addIfAbsent(
                                ID_MAP.get(atomI.getAndIncrement()).id(),
                                Component.translatable("gtocore.machine.monitor.ae.stat.day",
                                        formatter.apply(lastDayStat[i], ChatFormatting.DARK_GREEN)
                                                .append(Component.literal("/t").withStyle(ChatFormatting.GRAY)))
                                        .getVisualOrderText());
                        if (nowStat[i] < 0) {
                            var absSec = Math.abs(nowStat[i]) / DISPLAY_SCALE.doubleValue() * 20;
                            infoList.addIfAbsent(
                                    ID_MAP.get(atomI.getAndIncrement()).id(),
                                    Component.translatable("gtocore.machine.monitor.ae.stat.remaining_time",
                                            PowerSubstationMachine.getTimeToFillDrainText(BigDecimal.valueOf(currentAmount[i])
                                                    .divide(BigDecimal.valueOf(absSec), 0, RoundingMode.CEILING)
                                                    .toBigInteger())
                                                    .withStyle(ChatFormatting.GRAY))
                                            .getVisualOrderText());
                        }
                    }
                }
            }
        }
        return infoList;
    }

    private @NotNull BiFunction<Long, ChatFormatting, MutableComponent> getAmountFormatter(AEKey key) {
        return (num, color) -> {
            double amount = num / DISPLAY_SCALE.doubleValue();
            var prefix = amount >= 0 ? "+" : "-";
            double displayAmount = Math.abs(amount) / key.getAmountPerUnit();
            var component = Component.literal(prefix + THROUGHPUT_FORMAT.format(displayAmount)).withStyle(color);
            var unit = key.getUnitSymbol();
            if (unit != null) {
                component.append(Component.literal(unit).withStyle(ChatFormatting.GRAY));
            }
            return component;
        };
    }

    private static long scaleStat(BigDecimal value) {
        return value.multiply(DISPLAY_SCALE).setScale(0, RoundingMode.HALF_UP).longValue();
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

    private void onFilterChanged(int slot) {
        stats[slot] = null;
        lastAmount[slot] = 0;
        currentAmount[slot] = 0;
        lastMinuteStat[slot] = 0;
        lastHourStat[slot] = 0;
        lastDayStat[slot] = 0;
        nowStat[slot] = 0;
    }

    private class AEItem extends ExportOnlyAEItemList implements CurrentGettable {

        AEItem() {
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

        AEFluid() {
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

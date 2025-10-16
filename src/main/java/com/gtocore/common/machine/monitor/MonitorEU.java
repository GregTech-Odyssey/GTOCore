package com.gtocore.common.machine.monitor;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.PowerSubstationMachine;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import com.google.common.collect.ImmutableBiMap;
import com.hepdd.gtmthings.api.misc.EnergyStat;
import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.hepdd.gtmthings.common.item.IWirelessMonitor;
import com.hepdd.gtmthings.utils.FormatUtil;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonitorEU extends AbstractInfoProviderMonitor implements IWirelessMonitor, ITeamInformationProvider {

    private WirelessEnergyContainer WirelessEnergyContainerCache;
    private List<FormattedCharSequence> textListCache;
    private static final ImmutableBiMap<Integer, DisplayRegistry> DISPLAY_REGISTRY = ImmutableBiMap.<Integer, DisplayRegistry>builder()
            .put(0, DisplayRegistry.TOTAL_ENERGY)
            .put(1, DisplayRegistry.ENERGY_TRANSFER_LIMIT)
            .put(2, DisplayRegistry.ENERGY_STAT_TITLE)
            .put(3, DisplayRegistry.ENERGY_STAT_MINUTE)
            .put(4, DisplayRegistry.ENERGY_STAT_HOUR)
            .put(5, DisplayRegistry.ENERGY_STAT_DAY)
            .put(6, DisplayRegistry.ENERGY_STAT_NOW)
            .put(7, DisplayRegistry.ENERGY_STAT_TITLE_INPUT)
            .put(8, DisplayRegistry.ENERGY_STAT_MINUTE_INPUT)
            .put(9, DisplayRegistry.ENERGY_STAT_HOUR_INPUT)
            .put(10, DisplayRegistry.ENERGY_STAT_DAY_INPUT)
            .put(11, DisplayRegistry.ENERGY_STAT_NOW_INPUT)
            .put(12, DisplayRegistry.ENERGY_STAT_TITLE_OUTPUT)
            .put(13, DisplayRegistry.ENERGY_STAT_MINUTE_OUTPUT)
            .put(14, DisplayRegistry.ENERGY_STAT_HOUR_OUTPUT)
            .put(15, DisplayRegistry.ENERGY_STAT_DAY_OUTPUT)
            .put(16, DisplayRegistry.ENERGY_STAT_NOW_OUTPUT)
            .put(17, DisplayRegistry.ENERGY_STAT_REMAINING_TIME)
            .put(18, DisplayRegistry.ENERGY_STAT_BOUND_INFO)
            .build();
    @DescSynced
    private Component[] bufferCache = new Component[0];

    @DescSynced
    private float energyFullness = 0.0f;

    @DescSynced
    private ArrayList<String> EnergyInputHistoryDay = new ArrayList<>();

    @DescSynced
    private ArrayList<String> EnergyOutputHistoryDay = new ArrayList<>();

    @DescSynced
    private ArrayList<String> EnergyInputHistoryHour = new ArrayList<>();

    @DescSynced
    private ArrayList<String> EnergyOutputHistoryHour = new ArrayList<>();

    @DescSynced
    private ArrayList<String> EnergyInputHistoryMinute = new ArrayList<>();

    @DescSynced
    private ArrayList<String> EnergyOutputHistoryMinute = new ArrayList<>();

    public MonitorEU(Object o) {
        this((MetaMachineBlockEntity) o);
    }

    public MonitorEU(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (getOffsetTimer() % 10 == 0 && bufferCache != null) {
            textListCache = Stream.of(bufferCache)
                    .map(component -> Objects.requireNonNullElse(component, Component.empty()))
                    .map(Component::getVisualOrderText)
                    .toList();

        }
    }

    @Override
    public void setWirelessEnergyContainerCache(WirelessEnergyContainer container) {
        this.WirelessEnergyContainerCache = container;
    }

    @Override
    public WirelessEnergyContainer getWirelessEnergyContainerCache() {
        return this.WirelessEnergyContainerCache;
    }

    @Override
    public void syncInfoFromServer() {
        if (textListCache == null) {
            bufferCache = getComponentArray();
        }
    }

    public Component[] getComponentArray() {
        WirelessEnergyContainer container = this.getWirelessEnergyContainer();
        if (container == null) {
            return new Component[0];
        } else {
            Component[] textListCache = new Component[DISPLAY_REGISTRY.size()];
            BigInteger energyTotal = container.getStorage();
            // Total energy
            textListCache[0] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.1", Component.literal(FormatUtil.formatBigIntegerNumberOrSic(energyTotal)), new Component[0]).withStyle(ChatFormatting.GOLD));
            // Energy rate
            long rate = container.getRate();
            textListCache[1] = (Component.translatable(
                    "gtmthings.machine.wireless_energy_monitor.tooltip.2",
                    Component.literal(FormatUtil.formatBigIntegerNumberOrSic(BigInteger.valueOf(rate))),
                    Component.literal(String.valueOf(rate / GTValues.VEX[GTUtil.getFloorTierByVoltage(rate)])),
                    Component.literal(GTValues.VNF[GTUtil.getFloorTierByVoltage(rate)])).withStyle(ChatFormatting.GRAY));
            EnergyStat stat = container.getEnergyStat();
            // Energy stats title
            textListCache[2] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.net_power"));
            BigDecimal avgMinute = stat.minute.getAvgByTick();
            textListCache[3] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.last_minute",
                    Component.literal(FormatUtil.formatBigDecimalNumberOrSic(avgMinute)).withStyle(ChatFormatting.DARK_AQUA),
                    Component.literal(FormatUtil.voltageAmperage(avgMinute).toEngineeringString()),
                    FormatUtil.voltageName(avgMinute)));
            BigDecimal avgHour = stat.hour.getAvgByTick();
            textListCache[4] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.last_hour",
                    Component.literal(FormatUtil.formatBigDecimalNumberOrSic(avgHour)).withStyle(ChatFormatting.YELLOW),
                    Component.literal(FormatUtil.voltageAmperage(avgHour).toEngineeringString()),
                    FormatUtil.voltageName(avgHour)));
            BigDecimal avgDay = stat.day.getAvgByTick();
            textListCache[5] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.last_day",
                    Component.literal(FormatUtil.formatBigDecimalNumberOrSic(avgDay)).withStyle(ChatFormatting.DARK_GREEN),
                    Component.literal(FormatUtil.voltageAmperage(avgDay).toEngineeringString()),
                    FormatUtil.voltageName(avgDay)));
            BigDecimal avgEnergy = stat.getAvgEnergy();
            textListCache[6] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.now",
                    Component.literal(FormatUtil.formatBigDecimalNumberOrSic(avgEnergy)).withStyle(ChatFormatting.DARK_PURPLE),
                    Component.literal(FormatUtil.voltageAmperage(avgEnergy).toEngineeringString()),
                    FormatUtil.voltageName(avgEnergy)));

            textListCache[7] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.input_power"));
            BigDecimal InputMinute = stat.minute.getAvgInputByTick();
            textListCache[8] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.last_minute",
                    Component.literal(FormatUtil.formatBigDecimalNumberOrSic(InputMinute)).withStyle(ChatFormatting.DARK_AQUA),
                    Component.literal(FormatUtil.voltageAmperage(InputMinute).toEngineeringString()),
                    FormatUtil.voltageName(InputMinute)));
            BigDecimal InputHour = stat.hour.getAvgInputByTick();
            textListCache[9] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.last_hour",
                    Component.literal(FormatUtil.formatBigDecimalNumberOrSic(InputHour)).withStyle(ChatFormatting.YELLOW),
                    Component.literal(FormatUtil.voltageAmperage(InputHour).toEngineeringString()),
                    FormatUtil.voltageName(InputHour)));
            BigDecimal InputDay = stat.day.getAvgInputByTick();
            textListCache[10] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.last_day",
                    Component.literal(FormatUtil.formatBigDecimalNumberOrSic(InputDay)).withStyle(ChatFormatting.DARK_GREEN),
                    Component.literal(FormatUtil.voltageAmperage(InputDay).toEngineeringString()),
                    FormatUtil.voltageName(InputDay)));
            BigDecimal InputEnergy = stat.getAvgInput();
            textListCache[11] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.now",
                    Component.literal(FormatUtil.formatBigDecimalNumberOrSic(InputEnergy)).withStyle(ChatFormatting.DARK_PURPLE),
                    Component.literal(FormatUtil.voltageAmperage(InputEnergy).toEngineeringString()),
                    FormatUtil.voltageName(InputEnergy)));

            textListCache[12] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.output_power"));
            BigDecimal OutputMinute = stat.minute.getAvgOutputByTick();
            textListCache[13] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.last_minute",
                    Component.literal(FormatUtil.formatBigDecimalNumberOrSic(OutputMinute)).withStyle(ChatFormatting.DARK_AQUA),
                    Component.literal(FormatUtil.voltageAmperage(OutputMinute).toEngineeringString()),
                    FormatUtil.voltageName(OutputMinute)));
            BigDecimal OutputHour = stat.hour.getAvgOutputByTick();
            textListCache[14] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.last_hour",
                    Component.literal(FormatUtil.formatBigDecimalNumberOrSic(OutputHour)).withStyle(ChatFormatting.YELLOW),
                    Component.literal(FormatUtil.voltageAmperage(OutputHour).toEngineeringString()),
                    FormatUtil.voltageName(OutputHour)));
            BigDecimal OutputDay = stat.day.getAvgOutputByTick();
            textListCache[15] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.last_day",
                    Component.literal(FormatUtil.formatBigDecimalNumberOrSic(OutputDay)).withStyle(ChatFormatting.DARK_GREEN),
                    Component.literal(FormatUtil.voltageAmperage(OutputDay).toEngineeringString()),
                    FormatUtil.voltageName(OutputDay)));
            BigDecimal OutputEnergy = stat.getAvgOutput();
            textListCache[16] = (Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.now",
                    Component.literal(FormatUtil.formatBigDecimalNumberOrSic(OutputEnergy)).withStyle(ChatFormatting.DARK_PURPLE),
                    Component.literal(FormatUtil.voltageAmperage(OutputEnergy).toEngineeringString()),
                    FormatUtil.voltageName(OutputEnergy)));

            int compare = OutputEnergy.compareTo(BigDecimal.valueOf(0L));
            BigInteger multiply = OutputEnergy.abs().toBigInteger().multiply(BigInteger.valueOf(20L));
            if (compare > 0) {
                textListCache[17] = (Component.translatable("gtceu.multiblock.power_substation.time_to_fill",
                        container.getCapacity() == null ?
                                Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.time_to_fill") :
                                PowerSubstationMachine.getTimeToFillDrainText(container.getCapacity().subtract(energyTotal).divide(multiply)))
                        .withStyle(ChatFormatting.GRAY));
            } else if (compare < 0) {
                textListCache[17] = (Component.translatable("gtceu.multiblock.power_substation.time_to_drain",
                        PowerSubstationMachine.getTimeToFillDrainText(energyTotal.divide(multiply))).withStyle(ChatFormatting.GRAY));
            } else textListCache[17] = Component.translatable("gtceu.multiblock.power_substation.time_to_drain",
                    Component.translatable("gtceu.multiblock.power_substation.time_forever").withStyle(ChatFormatting.GRAY));

            if (container.getBindPos() == null) {
                textListCache[18] = Component.translatable("gtocore.machine.monitor.eu.no_container")
                        .withStyle(ChatFormatting.RED);
            } else {
                String pos = container.getBindPos().pos().toShortString();
                textListCache[18] = (Component.translatable("gtmthings.machine.wireless_energy_hatch.tooltip.2",
                        Component.translatable("recipe.condition.dimension.tooltip",
                                new Object[] { container.getBindPos().dimension().location().toString() }).append(" [").append(pos).append("] "))
                        .withStyle(ChatFormatting.GRAY));
            }
            energyFullness = (container.getCapacity() == null || container.getCapacity().equals(BigInteger.ZERO)) ?
                    0f : new BigDecimal(energyTotal).divide(new BigDecimal(container.getCapacity()), 4, RoundingMode.DOWN).floatValue();
            EnergyInputHistoryDay = stat.day.getInputHistory().stream()
                    .map(BigInteger::toString) // 将每个 BigInteger 转换为 String
                    .collect(Collectors.toCollection(ArrayList::new));
            EnergyOutputHistoryDay = stat.day.getOutputHistory().stream()
                    .map(BigInteger::toString) // 将每个 BigInteger 转换为 String
                    .collect(Collectors.toCollection(ArrayList::new));
            EnergyInputHistoryHour = stat.hour.getInputHistory().stream()
                    .map(BigInteger::toString) // 将每个 BigInteger 转换为 String
                    .collect(Collectors.toCollection(ArrayList::new));
            EnergyOutputHistoryHour = stat.hour.getOutputHistory().stream()
                    .map(BigInteger::toString) // 将每个 BigInteger 转换为 String
                    .collect(Collectors.toCollection(ArrayList::new));
            EnergyInputHistoryMinute = stat.minute.getInputHistory().stream()
                    .map(BigInteger::toString) // 将每个 BigInteger 转换为 String
                    .collect(Collectors.toCollection(ArrayList::new));
            EnergyOutputHistoryMinute = stat.minute.getOutputHistory().stream()
                    .map(BigInteger::toString) // 将每个 BigInteger 转换为 String
                    .collect(Collectors.toCollection(ArrayList::new));
            return textListCache;
        }
    }

    @SuppressWarnings("all")
    public DisplayComponentList provideInformation() {
        var informationList = ITeamInformationProvider.super.provideInformation();
        if (bufferCache.length == DISPLAY_REGISTRY.size()) {
            for (int i = 0; i < bufferCache.length; i++) {
                if (DISPLAY_REGISTRY.containsKey(i) && bufferCache[i] != null) {
                    informationList.addIfAbsent(
                            DISPLAY_REGISTRY.get(i).id(),
                            bufferCache[i].getVisualOrderText());
                }
            }
            informationList.addIfAbsent(
                    DisplayRegistry.EU_STATUS_BAR.id(),
                    DisplayComponent.progressBar(DisplayRegistry.EU_STATUS_BAR.id(), energyFullness, Component.translatable("gtocore.machine.monitor.eu.fullness", String.format("%.2f", energyFullness * 100)).getString()));

            informationList.addIfAbsent(
                    DisplayRegistry.EnergyInputHistoryDay.id(), // 假设你在 DisplayRegistry 中定义了这个ID
                    DisplayComponent.lineChart(DisplayRegistry.EnergyInputHistoryDay.id(), this.EnergyInputHistoryDay.stream().map(BigInteger::new).toList()));

            informationList.addIfAbsent(
                    DisplayRegistry.EnergyOutputHistoryDay.id(), // 假设你在 DisplayRegistry 中定义了这个ID
                    DisplayComponent.lineChart(DisplayRegistry.EnergyOutputHistoryDay.id(), this.EnergyOutputHistoryDay.stream().map(BigInteger::new).toList()));

            informationList.addIfAbsent(
                    DisplayRegistry.EnergyInputHistoryHour.id(), // 假设你在 DisplayRegistry 中定义了这个ID
                    DisplayComponent.lineChart(DisplayRegistry.EnergyInputHistoryHour.id(), this.EnergyInputHistoryHour.stream().map(BigInteger::new).toList()));

            informationList.addIfAbsent(
                    DisplayRegistry.EnergyOutputHistoryHour.id(), // 假设你在 DisplayRegistry 中定义了这个ID
                    DisplayComponent.lineChart(DisplayRegistry.EnergyOutputHistoryHour.id(), this.EnergyOutputHistoryHour.stream().map(BigInteger::new).toList()));

            informationList.addIfAbsent(
                    DisplayRegistry.EnergyInputHistoryMinute.id(), // 假设你在 DisplayRegistry 中定义了这个ID
                    DisplayComponent.lineChart(DisplayRegistry.EnergyInputHistoryMinute.id(), this.EnergyInputHistoryMinute.stream().map(BigInteger::new).toList()));

            informationList.addIfAbsent(
                    DisplayRegistry.EnergyOutputHistoryMinute.id(), // 假设你在 DisplayRegistry 中定义了这个ID
                    DisplayComponent.lineChart(DisplayRegistry.EnergyOutputHistoryMinute.id(), this.EnergyOutputHistoryMinute.stream().map(BigInteger::new).toList()));
        }
        return informationList;
    }

    @Override
    public List<ResourceLocation> getAvailableRLs() {
        var rls = ITeamInformationProvider.super.getAvailableRLs();
        rls.addAll(DISPLAY_REGISTRY.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .map(DisplayRegistry::id)
                .toList());
        rls.add(DisplayRegistry.EU_STATUS_BAR.id());
        rls.add(DisplayRegistry.EnergyInputHistoryDay.id());
        rls.add(DisplayRegistry.EnergyOutputHistoryDay.id());
        rls.add(DisplayRegistry.EnergyInputHistoryHour.id());
        rls.add(DisplayRegistry.EnergyOutputHistoryHour.id());
        rls.add(DisplayRegistry.EnergyInputHistoryMinute.id());
        rls.add(DisplayRegistry.EnergyOutputHistoryMinute.id());
        return rls;
    }
}

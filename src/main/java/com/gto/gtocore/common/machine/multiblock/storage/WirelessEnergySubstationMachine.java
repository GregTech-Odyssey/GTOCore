package com.gto.gtocore.common.machine.multiblock.storage;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.machine.feature.IExtendWirelessEnergyContainerHolder;
import com.gto.gtocore.api.machine.feature.ITierCasingMachine;
import com.gto.gtocore.api.machine.multiblock.NoRecipeLogicMultiblockMachine;
import com.gto.gtocore.api.machine.trait.TierCasingTrait;
import com.gto.gtocore.common.block.WirelessEnergyUnitBlock;
import com.gto.gtocore.common.wireless.ExtendTransferData;
import com.gto.gtocore.common.wireless.ExtendWirelessEnergyContainer;
import com.gto.gtocore.utils.FunctionContainer;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import com.hepdd.gtmthings.api.misc.ITransferData;
import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.hepdd.gtmthings.utils.TeamUtil;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.*;

public final class WirelessEnergySubstationMachine extends NoRecipeLogicMultiblockMachine implements IMachineLife, IExtendWirelessEnergyContainerHolder, ITierCasingMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            WirelessEnergySubstationMachine.class, NoRecipeLogicMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private boolean display;

    @Getter
    @Persisted
    private UUID UUID;

    @Getter
    @Setter
    private WirelessEnergyContainer WirelessEnergyContainerCache;

    private List<Component> textListCache;

    private final TierCasingTrait tierCasingTrait;

    public WirelessEnergySubstationMachine(IMachineBlockEntity holder) {
        super(holder);
        tierCasingTrait = new TierCasingTrait(this, GTOValues.GLASS_TIER);
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote && componentData.equals("display")) {
            display = !display;
        }
    }

    @Override
    public void onMachinePlaced(@Nullable LivingEntity player, ItemStack stack) {
        if (player != null) {
            this.UUID = player.getUUID();
        }
    }

    @Override
    public void onStructureInvalid() {
        ExtendWirelessEnergyContainer container = getWirelessEnergyContainer();
        if (container == null) return;
        container.setCapacity(BigInteger.ZERO);
        container.setLoss(0);
        super.onStructureInvalid();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        ExtendWirelessEnergyContainer container = getWirelessEnergyContainer();
        if (container == null) return;
        Integer tier = getCasingTiers().get(GTOValues.GLASS_TIER);
        FunctionContainer<ArrayList<WirelessEnergyUnitBlock>, ?> functionContainer = getMultiblockState().getMatchContext().get("wirelessEnergyUnit");
        int loss = 0;
        int i = 0;
        BigInteger capacity = BigInteger.ZERO;
        if (functionContainer != null) {
            ArrayList<WirelessEnergyUnitBlock> blocks = functionContainer.getValue();
            for (WirelessEnergyUnitBlock block : blocks) {
                if (block.getTier() <= tier) {
                    i++;
                    capacity = capacity.add(block.getCapacity());
                    loss += block.getLoss();
                }
            }
            blocks.clear();
        }
        container.setLoss(i == 0 ? 0 : loss / i);
        container.setCapacity(capacity.multiply(BigInteger.valueOf(Math.max(1, i / 2))));
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        if (textListCache == null || getOffsetTimer() % 10 == 0) {
            textListCache = new ArrayList<>();
            ExtendWirelessEnergyContainer container = getWirelessEnergyContainer();
            if (container == null) return;
            textListCache.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0", TeamUtil.GetName(getLevel(), this.UUID)).withStyle(ChatFormatting.AQUA));
            textListCache.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.1", FormattingUtil.formatNumbers(container.getStorage()) + " / " + FormattingUtil.formatNumbers(container.getCapacity())).withStyle(ChatFormatting.GRAY));
            textListCache.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.2", FormattingUtil.formatNumbers(container.getRate()), container.getRate() / GTValues.VEX[GTUtil.getFloorTierByVoltage(container.getRate())], Component.literal(GTValues.VNF[GTUtil.getFloorTierByVoltage(container.getRate())])).withStyle(ChatFormatting.GRAY));
            textListCache.add(Component.translatable("gtceu.machine.fluid_drilling_rig.depletion", (double) container.getLoss() / 10));
            textListCache.add(Component.translatable("gui.ae2.SpatialAnchorStatistics").append(": ").append(ComponentPanelWidget.withButton(display ? Component.translatable("waila.ae2.Showing") : Component.translatable("config.jade.display_fluids_none"), "display")));
            if (display) {
                WirelessEnergyContainer.observed = true;
                UUID teamUUID = TeamUtil.getTeamUUID(this.UUID);
                List<Map.Entry<MetaMachine, ITransferData>> sortedEntries = WirelessEnergyContainer.TRANSFER_DATA.entrySet()
                        .stream()
                        .filter(e -> e.getValue() instanceof ExtendTransferData data && data.UUID().equals(teamUUID))
                        .sorted(Comparator.comparingLong(entry -> entry.getValue().Throughput()))
                        .toList();
                WirelessEnergyContainer.TRANSFER_DATA.clear();
                for (Map.Entry<MetaMachine, ITransferData> m : sortedEntries) {
                    MetaMachine machine = m.getKey();
                    long eut = m.getValue().Throughput();
                    String pos = machine.getPos().toShortString();
                    if (eut > 0) {
                        MutableComponent component = Component.translatable(machine.getBlockState().getBlock().getDescriptionId())
                                .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("recipe.condition.dimension.tooltip", machine.getLevel().dimension().location()).append(" [").append(pos).append("] ").append(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0", TeamUtil.GetName(getLevel(), m.getValue().UUID()))))))
                                .append("\n+").append(FormattingUtil.formatNumbers(eut)).append(" EU/t (").append(GTValues.VNF[GTUtil.getFloorTierByVoltage(eut)]).append(") ");
                        if (m.getValue() instanceof ExtendTransferData data) {
                            component.append(Component.translatable("gtocore.machine.energy_loss", Component.literal(FormattingUtil.formatNumbers(data.loss())).append(" EU/t (").append(GTValues.VNF[GTUtil.getFloorTierByVoltage(data.loss())]).append(")")));
                        }
                        textListCache.add(component);
                    }
                }
            }
        }
        textList.addAll(textListCache);
    }

    @Override
    public Map<String, Integer> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }
}

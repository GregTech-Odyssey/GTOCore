package com.gto.gtocore.common.machine.multiblock.storage;

import com.gregtechceu.gtceu.api.capability.IEnergyInfoProvider;
import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.machine.feature.IExtendWirelessEnergyContainerHolder;
import com.gto.gtocore.api.machine.feature.multiblock.ITierCasingMachine;
import com.gto.gtocore.api.machine.multiblock.NoRecipeLogicMultiblockMachine;
import com.gto.gtocore.api.machine.trait.TierCasingTrait;
import com.gto.gtocore.common.block.WirelessEnergyUnitBlock;
import com.gto.gtocore.common.wireless.ExtendWirelessEnergyContainer;
import com.gto.gtocore.utils.FunctionContainer;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.hepdd.gtmthings.utils.TeamUtil;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class WirelessEnergySubstationMachine extends NoRecipeLogicMultiblockMachine implements IMachineLife, IExtendWirelessEnergyContainerHolder, ITierCasingMachine, IEnergyInfoProvider {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            WirelessEnergySubstationMachine.class, NoRecipeLogicMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Getter
    @Persisted
    private UUID UUID;

    @Getter
    @Setter
    private WirelessEnergyContainer WirelessEnergyContainerCache;

    private final TierCasingTrait tierCasingTrait;

    public WirelessEnergySubstationMachine(IMachineBlockEntity holder) {
        super(holder);
        tierCasingTrait = new TierCasingTrait(this, GTOValues.GLASS_TIER);
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
        ExtendWirelessEnergyContainer container = getWirelessEnergyContainer();
        if (container == null) return;
        textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0", TeamUtil.GetName(getLevel(), this.UUID)).withStyle(ChatFormatting.AQUA));
        textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.1", FormattingUtil.formatNumbers(container.getStorage()) + " / " + FormattingUtil.formatNumbers(container.getCapacity())).withStyle(ChatFormatting.GRAY));
        textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.2", FormattingUtil.formatNumbers(container.getRate()), container.getRate() / GTValues.VEX[GTUtil.getFloorTierByVoltage(container.getRate())], Component.literal(GTValues.VNF[GTUtil.getFloorTierByVoltage(container.getRate())])).withStyle(ChatFormatting.GRAY));
        textList.add(Component.translatable("gtceu.machine.fluid_drilling_rig.depletion", (double) container.getLoss() / 10));
    }

    @Override
    public Map<String, Integer> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }

    @Override
    public EnergyInfo getEnergyInfo() {
        var container = getWirelessEnergyContainer();
        if (container == null) {
            return new EnergyInfo(BigInteger.ZERO, BigInteger.ZERO);
        } else {
            return new EnergyInfo(container.getCapacity(), container.getStorage());
        }
    }

    @Override
    public boolean supportsBigIntEnergyValues() {
        return true;
    }
}

package com.gtocore.api.machine;

import com.gtocore.common.machine.multiblock.WirelessChargerMachine;
import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.api.machine.feature.IElectricMachine;
import com.gtolib.utils.GTOUtils;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.hepdd.gtmthings.api.capability.IBindable;
import com.hepdd.gtmthings.utils.TeamUtil;

import java.util.Map;
import java.util.Set;

public interface IWirelessChargerInteraction extends IIWirelessInteractor<WirelessChargerMachine>, IBindable {

    BlockPos getPos();

    @Override
    default Map<ResourceLocation, Set<WirelessChargerMachine>> getMachineNet() {
        return WirelessChargerMachine.NETWORK;
    }

    @Override
    default boolean firstTestMachine(WirelessChargerMachine machine) {
        Level level = machine.getLevel();
        if (level == null) return false;
        var uuid = getUUID();
        if (uuid == null) return false;
        return testMachine(machine) && TeamUtil.getTeamUUID(uuid).equals(TeamUtil.getTeamUUID(machine.getUUID()));
    }

    @Override
    default boolean testMachine(WirelessChargerMachine machine) {
        return machine.isMachine() && machine.isFormed() && machine.isWorkingEnabled() && machine.getRate() > 0 && GTOUtils.calculateDistance(machine.getPos(), getPos()) < machine.getRange();
    }

    @Override
    default boolean display() {
        return false;
    }

    default void charge() {
        if (this instanceof IElectricMachine electricMachine && electricMachine.self().getOffsetTimer() % 20 == 0) {
            var machine = getNetMachine();
            if (machine != null && !machine.isOnlyProvideToPlayer) {
                if (!machine.isProvideToMultiBlockHatch && this instanceof MultiblockPartMachine) return;
                var stored = machine.getEnergyStored();
                if (stored > 0) {
                    var electricContainer = electricMachine.gtolib$getEnergyContainer();
                    if (electricContainer.getEnergyStored() < electricContainer.getEnergyCapacity()) {
                        machine.removeEnergy(electricContainer.addEnergy(Math.min(GTValues.V[electricMachine.getTier()] * 40, stored)));
                    }
                }
            }
        }
    }

    static void charge(WirelessChargerMachine machine, ItemStack stack) {
        if (machine != null) {
            var stored = machine.getEnergyStored();
            if (stored > 0) {
                var electricitem = GTCapabilityHelper.getElectricItem(stack);
                if (electricitem != null) {
                    if (electricitem.chargeable() && electricitem.getCharge() < electricitem.getMaxCharge()) machine.removeEnergy(electricitem.charge(Math.min(stored, GTValues.V[electricitem.getTier()] * machine.getRate()), electricitem.getTier(), true, false));
                } else {
                    var energyItem = GTCapabilityHelper.getForgeEnergyItem(stack);
                    if (energyItem != null && energyItem.canReceive() && energyItem.getEnergyStored() < energyItem.getMaxEnergyStored()) machine.removeEnergy(energyItem.receiveEnergy(MathUtil.saturatedCast(stored), false));
                }
            }
        }
    }
}

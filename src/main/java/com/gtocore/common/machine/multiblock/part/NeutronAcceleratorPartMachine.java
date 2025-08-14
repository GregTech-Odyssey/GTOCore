package com.gtocore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

import com.hepdd.gtmthings.api.machine.WirelessEnergyReceiveCoverHolder;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class NeutronAcceleratorPartMachine extends EnergyHatchPartMachine implements WirelessEnergyReceiveCoverHolder {

    public NeutronAcceleratorPartMachine(MetaMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, IO.IN, 1, args);
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        NotifiableEnergyContainer container;
        container = NotifiableEnergyContainer.receiverContainer(this,
                GTValues.V[tier] << 1, GTValues.V[tier], 1);
        container.setSideInputCondition((s) -> s == getFrontFacing() && isWorkingEnabled());
        container.setCapabilityValidator((s) -> s == null || s == getFrontFacing());
        return container;
    }

    public long consumeEnergy() {
        if (isWorkingEnabled() && energyContainer.getEnergyStored() > 0) {
            return Math.abs(energyContainer.changeEnergy(-getMaxEUConsume())) *
                    (10 + ThreadLocalRandom.current().nextInt(11));
        } else {
            return 0L;
        }
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return true;
    }

    private long getMaxEUConsume() {
        return Math.round(GTValues.V[tier] * 0.8);
    }

    @Override
    public boolean canShared() {
        return false;
    }
}

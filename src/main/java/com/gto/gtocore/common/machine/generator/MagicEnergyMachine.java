package com.gto.gtocore.common.machine.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;

import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.MethodsReturnNonnullByDefault;

import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class MagicEnergyMachine extends TieredEnergyMachine {

    private TickableSubscription energySubs;

    public MagicEnergyMachine(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, args);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            energySubs = subscribeServerTick(energySubs, this::checkEnergy);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (energySubs != null) {
            energySubs.unsubscribe();
            energySubs = null;
        }
    }

    private void checkEnergy() {
        if (getOffsetTimer() % 20 == 0 && !Objects.requireNonNull(getLevel()).getEntitiesOfClass(EndCrystal.class, AABB.ofSize(new Vec3(getPos().getX(), getPos().getY() + 1, getPos().getZ()), 1, 1, 1), e -> true).isEmpty()) {
            energyContainer.addEnergy(GTValues.V[getTier() + 4]);
        }
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        long tierVoltage = GTValues.V[getTier()];
        NotifiableEnergyContainer energyContainer = NotifiableEnergyContainer.emitterContainer(this,
                tierVoltage << 9, tierVoltage, getMaxInputOutputAmperage());
        energyContainer.setSideOutputCondition(side -> !hasFrontFacing() || side == getFrontFacing());
        return energyContainer;
    }

    @Override
    protected boolean isEnergyEmitter() {
        return true;
    }

    @Override
    protected long getMaxInputOutputAmperage() {
        return 16L;
    }
}

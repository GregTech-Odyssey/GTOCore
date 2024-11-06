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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MagicEnergyMachine extends TieredEnergyMachine {

    @Nullable
    protected TickableSubscription energySubs;

    public MagicEnergyMachine(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, args);
        this.energySubs = subscribeServerTick(energySubs, this::checkEnergy);
    }

    protected void checkEnergy() {
        if (getOffsetTimer() % 20 == 0 && !Objects.requireNonNull(getLevel()).getEntitiesOfClass(EndCrystal.class, AABB.ofSize(new Vec3(getPos().getX(), getPos().getY() + 1, getPos().getZ()), 1, 1, 1), e -> true).isEmpty()) {
            energyContainer.addEnergy(GTValues.V[getTier() + 4]);
        }
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        long tierVoltage = GTValues.V[getTier()];
        return NotifiableEnergyContainer.emitterContainer(this,
                tierVoltage * 512L, tierVoltage, getMaxInputOutputAmperage());
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

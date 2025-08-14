package com.gtocore.common.machine.generator;

import com.gtolib.GTOCore;
import com.gtolib.api.capability.IManaContainer;
import com.gtolib.api.machine.mana.feature.IManaMachine;
import com.gtolib.api.machine.mana.trait.NotifiableManaContainer;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;

import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class MagicEnergyMachine extends TieredEnergyMachine implements IManaMachine, IControllable {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MagicEnergyMachine.class, TieredEnergyMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private TickableSubscription energySubs;

    @Persisted
    private final NotifiableManaContainer manaContainer;

    @Persisted
    private boolean enabled;

    private final long tierMana;

    public MagicEnergyMachine(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier);
        tierMana = GTValues.V[tier] << 1;
        manaContainer = new NotifiableManaContainer(this, IO.IN, 64L * tierMana);
        manaContainer.setAcceptDistributor(true);
    }

    @Override
    public @NotNull IManaContainer getManaContainer() {
        return manaContainer;
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
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
        if (enabled && getOffsetTimer() % 20 == 0 && getLevel() != null && !getLevel().getEntitiesOfClass(EndCrystal.class, AABB.ofSize(new Vec3(getPos().getX(), getPos().getY() + 1, getPos().getZ()), 1, 1, 1), e -> true).isEmpty()) {
            if (manaContainer.removeMana(tierMana, 20, false) == tierMana) {
                energyContainer.addEnergy(tierMana * 20);
                if (energyContainer.getEnergyStored() == energyContainer.getEnergyCapacity()) {
                    doExplosion(tier);
                }
            } else {
                doExplosion(tier << 1);
            }
        }
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        long tierVoltage = GTValues.V[tier];
        NotifiableEnergyContainer energyContainer = NotifiableEnergyContainer.emitterContainer(this, tierVoltage << 8, tierVoltage, getMaxInputOutputAmperage());
        energyContainer.setSideOutputCondition(side -> !hasFrontFacing() || side == getFrontFacing());
        return energyContainer;
    }

    @Override
    protected boolean isEnergyEmitter() {
        return true;
    }

    @Override
    protected long getMaxInputOutputAmperage() {
        return 16 >> GTOCore.difficulty;
    }

    @Override
    public boolean isWorkingEnabled() {
        return enabled;
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        enabled = isWorkingAllowed;
    }
}

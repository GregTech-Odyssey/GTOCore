package com.gto.gtocore.mixin.gtm;

import com.gto.gtocore.api.GTOValues;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.capability.EnvironmentalHazardSavedData;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.CleanroomMachine;
import com.gregtechceu.gtceu.common.machine.trait.CleanroomLogic;
import com.gregtechceu.gtceu.config.ConfigHolder;

import net.minecraft.server.level.ServerLevel;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CleanroomLogic.class)
public abstract class CleanroomLogicMixin extends RecipeLogic {

    @Shadow(remap = false)
    protected abstract void adjustCleanAmount(boolean declined);

    @Shadow(remap = false)
    private @Nullable IMaintenanceMachine maintenanceMachine;

    @Shadow(remap = false)
    public abstract CleanroomMachine getMachine();

    @Shadow(remap = false)
    private @Nullable IEnergyContainer energyContainer;

    protected CleanroomLogicMixin(IRecipeLogicMachine machine) {
        super(machine);
    }

    @Override
    public void serverTick() {
        if (!this.isSuspend() && this.duration > 0) {
            EnvironmentalHazardSavedData environmentalHazards = EnvironmentalHazardSavedData.getOrCreate((ServerLevel) this.getMachine().getLevel());
            EnvironmentalHazardSavedData.HazardZone zone = environmentalHazards.getZoneByContainedPos(this.getMachine().getPos());
            if (this.maintenanceMachine != null && this.maintenanceMachine.getNumMaintenanceProblems() != 0 && zone == null) {
                if (this.progress > 0) {
                    --this.progress;
                }

                if (this.machine.self().getOffsetTimer() % this.duration == 0L) {
                    this.adjustCleanAmount(true);
                }

                this.setStatus(Status.IDLE);
                this.machine.afterWorking();
            } else {
                if (!this.gtocore$consumeEnergy()) {
                    if (this.progress > 0 && this.machine.regressWhenWaiting()) {
                        if (ConfigHolder.INSTANCE.machines.recipeProgressLowEnergy) {
                            this.progress = 1;
                        } else {
                            this.progress = Math.max(1, this.progress - 2);
                        }
                    }

                    if (this.machine.self().getOffsetTimer() % (long) this.duration == 0L) {
                        this.adjustCleanAmount(true);
                    }

                    this.setWaiting(null);
                    return;
                }

                this.setStatus(Status.WORKING);
                if (this.progress++ < this.getMaxProgress()) {
                    if (!this.machine.onWorking()) {
                        this.interruptRecipe();
                    }

                    return;
                }

                this.progress = 0;
                if (!this.machine.beforeWorking(null)) {
                    return;
                }

                this.adjustCleanAmount(false);
            }
        }

        if (this.isSuspend() && this.subscription != null) {
            this.subscription.unsubscribe();
            this.subscription = null;
        }
    }

    @Unique
    private boolean gtocore$consumeEnergy() {
        CleanroomMachine cleanroom = this.getMachine();
        long energyToDrain = cleanroom.isClean() ? (long) Math.min(4.0, Math.pow(4.0, cleanroom.getTier())) : GTOValues.VAEX[cleanroom.getTier()];
        if (this.energyContainer != null) {
            long resultEnergy = this.energyContainer.getEnergyStored() - energyToDrain;
            if (resultEnergy >= 0L && resultEnergy <= this.energyContainer.getEnergyCapacity()) {
                this.energyContainer.removeEnergy(energyToDrain);
                return true;
            }
        }

        return false;
    }
}

package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.INetMachineInteractor;
import com.gto.gtocore.api.machine.feature.IAirScrubberInteractor;

import com.gregtechceu.gtceu.api.data.medicalcondition.MedicalCondition;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.electric.AirScrubberMachine;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AirScrubberMachine.class)
public class AirScrubberMachineMixin extends SimpleTieredMachine {

    public AirScrubberMachineMixin(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        INetMachineInteractor.removeFromNet(IAirScrubberInteractor.NETWORK, (AirScrubberMachine) (Object) this);
    }

    @Override
    public void onWaiting() {
        super.onWaiting();
        INetMachineInteractor.removeFromNet(IAirScrubberInteractor.NETWORK, (AirScrubberMachine) (Object) this);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        INetMachineInteractor.removeFromNet(IAirScrubberInteractor.NETWORK, (AirScrubberMachine) (Object) this);
    }

    @Override
    public void setOverclockTier(int tier) {
        if (!isRemote()) {
            this.overclockTier = getMaxOverclockTier();
            this.recipeLogic.markLastRecipeDirty();
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean beforeWorking(GTRecipe recipe) {
        if (super.beforeWorking(recipe)) {
            INetMachineInteractor.addToNet(IAirScrubberInteractor.NETWORK, (AirScrubberMachine) (Object) this);
            return true;
        }
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onWorking() {
        return super.onWorking();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean isRecipeLogicAvailable() {
        return super.isRecipeLogicAvailable();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void cleanHazard(MedicalCondition condition, float amount) {}
}

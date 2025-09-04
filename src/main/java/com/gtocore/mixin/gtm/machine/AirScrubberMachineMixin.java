package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.api.machine.feature.IAirScrubberInteractor;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.electric.AirScrubberMachine;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AirScrubberMachine.class)
public class AirScrubberMachineMixin extends SimpleTieredMachine {

    public AirScrubberMachineMixin(MetaMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        IIWirelessInteractor.removeFromNet(IAirScrubberInteractor.NETWORK, (AirScrubberMachine) (Object) this);
    }

    @Override
    public void onWaiting() {
        super.onWaiting();
        IIWirelessInteractor.removeFromNet(IAirScrubberInteractor.NETWORK, (AirScrubberMachine) (Object) this);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        IIWirelessInteractor.removeFromNet(IAirScrubberInteractor.NETWORK, (AirScrubberMachine) (Object) this);
    }

    @Override
    public void setOverclockTier(int tier) {
        if (!isRemote()) {
            this.overclockTier = getMaxOverclockTier();
        }
    }

    @Override
    public boolean beforeWorking(GTRecipe recipe) {
        if (super.beforeWorking(recipe)) {
            IIWirelessInteractor.addToNet(IAirScrubberInteractor.NETWORK, (AirScrubberMachine) (Object) this);
            return true;
        }
        return false;
    }
}

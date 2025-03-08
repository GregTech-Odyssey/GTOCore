package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.LargeBoilerMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LargeBoilerMachine.class)
public abstract class LargeBoilerMachineMixin extends WorkableMultiblockMachine {

    protected LargeBoilerMachineMixin(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Shadow(remap = false)
    protected abstract void updateSteamSubscription();

    @Inject(method = "updateCurrentTemperature", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/common/machine/multiblock/steam/LargeBoilerMachine;doExplosion(F)V"), remap = false, cancellable = true)
    protected void updateCurrentTemperature(CallbackInfo ci) {
        updateSteamSubscription();
        ci.cancel();
    }

    @Override
    public void afterWorking() {
        super.onWorking();
        getRecipeLogic().markLastRecipeDirty();
    }
}

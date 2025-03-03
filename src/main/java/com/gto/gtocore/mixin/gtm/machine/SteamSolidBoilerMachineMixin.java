package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.machine.steam.SteamSolidBoilerMachine;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SteamSolidBoilerMachine.class)
public class SteamSolidBoilerMachineMixin {

    @Shadow(remap = false)
    @Final
    public NotifiableItemStackHandler fuelHandler;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtocore$init(IMachineBlockEntity holder, boolean isHighPressure, Object[] args, CallbackInfo ci) {
        fuelHandler.setFilter(i -> true);
    }
}

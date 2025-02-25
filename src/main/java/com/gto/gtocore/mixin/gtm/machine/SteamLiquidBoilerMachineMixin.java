package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.machine.steam.SteamLiquidBoilerMachine;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SteamLiquidBoilerMachine.class)
public class SteamLiquidBoilerMachineMixin {

    @Shadow(remap = false)
    @Final
    public NotifiableFluidTank fuelTank;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(IMachineBlockEntity holder, boolean isHighPressure, Object[] args, CallbackInfo ci) {
        fuelTank.setFilter(i -> true);
    }
}

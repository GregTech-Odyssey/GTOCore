package com.gtocore.mixin.gtm.api.machine;

import com.gtolib.api.machine.IDifficultyConfigDefinition;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MetaMachine.class)
public abstract class MetaMachineMixin {

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(MetaMachineBlockEntity holder, CallbackInfo ci) {
        IDifficultyConfigDefinition.update((MetaMachine) (Object) this);
    }
}

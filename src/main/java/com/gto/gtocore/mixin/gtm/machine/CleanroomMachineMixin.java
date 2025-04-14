package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.CleanroomMachine;
import com.gregtechceu.gtceu.common.machine.trait.CleanroomLogic;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CleanroomMachine.class)
public abstract class CleanroomMachineMixin extends WorkableElectricMultiblockMachine {

    @Shadow(remap = false)
    private int cleanAmount;

    @Shadow(remap = false)
    @NotNull
    public abstract CleanroomLogic getRecipeLogic();

    public CleanroomMachineMixin(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Inject(method = "onStructureFormed", at = @At("TAIL"))
    public void onStructureFormedHook(CallbackInfo ci) {
        if (!this.getRecipeLogic().isWorkingEnabled() && this.cleanAmount > 0) {
            this.cleanAmount = 0;
        }
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        if (!isWorkingAllowed) cleanAmount = 0;
        super.setWorkingEnabled(isWorkingAllowed);
    }
}

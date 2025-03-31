package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.feature.multiblock.ICheckPatternMachine;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(MultiblockControllerMachine.class)
public abstract class MultiblockControllerMachineMixin implements IMultiController, ICheckPatternMachine {

    @Override
    public boolean checkPattern() {
        if (gTOCore$getTime() < 1) {
            BlockPattern pattern = getPattern();
            if (pattern != null && pattern.checkPatternAt(getMultiblockState(), false)) {
                return true;
            } else {
                gTOCore$setTime(10);
            }
        } else {
            gTOCore$setTime(gTOCore$getTime() - 1);
        }
        return false;
    }
}

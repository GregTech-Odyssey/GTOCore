package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.feature.ICheckPatternMachine;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MultiblockControllerMachine.class)
public abstract class MultiblockControllerMachineMixin implements IMultiController, ICheckPatternMachine {

    @Unique
    private int gTOCore$time;

    @Override
    public void gTOCore$cleanTime() {
        gTOCore$time = 0;
    }

    @Override
    public int gTOCore$getTime() {
        return gTOCore$time;
    }

    @Override
    public boolean checkPattern() {
        if (gTOCore$time < 1) {
            BlockPattern pattern = getPattern();
            if (pattern != null && pattern.checkPatternAt(getMultiblockState(), false)) {
                return true;
            } else {
                gTOCore$time = 10;
            }
        } else {
            gTOCore$time--;
        }
        return false;
    }
}

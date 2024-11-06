package com.gto.gtocore.api.machine.multiblock;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.block.CoilBlock;

import net.minecraft.MethodsReturnNonnullByDefault;

import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CoilWorkableElectricMultipleRecipesMultiblockMachine extends WorkableElectricMultipleRecipesMachine {

    public CoilWorkableElectricMultipleRecipesMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    private ICoilType coilType = CoilBlock.CoilType.CUPRONICKEL;

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        var type = getMultiblockState().getMatchContext().get("CoilType");
        if (type instanceof ICoilType coil) {
            this.coilType = coil;
        }
    }

    public int getCoilTier() {
        return coilType.getTier();
    }

    @Override
    public int getParallel() {
        return isFormed() ? Math.min(2147483647, (int) Math.pow(2, getCoilType().getCoilTemperature() / 900D)) : 0;
    }
}

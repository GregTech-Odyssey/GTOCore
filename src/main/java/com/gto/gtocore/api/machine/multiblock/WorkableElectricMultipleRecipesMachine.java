package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.common.machine.trait.MultipleRecipesLogic;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import org.jetbrains.annotations.NotNull;

public class WorkableElectricMultipleRecipesMachine extends WorkableElectricMultiblockMachine implements IParallelMachine {

    public final boolean infinite;

    public WorkableElectricMultipleRecipesMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        infinite = false;
    }

    public WorkableElectricMultipleRecipesMachine(IMachineBlockEntity holder, boolean infinite) {
        super(holder);
        this.infinite = infinite;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new MultipleRecipesLogic(this);
    }

    @NotNull
    @Override
    public MultipleRecipesLogic getRecipeLogic() {
        return (MultipleRecipesLogic) super.getRecipeLogic();
    }

    @Override
    public int getParallel() {
        return isFormed() ? Integer.MAX_VALUE : 0;
    }
}

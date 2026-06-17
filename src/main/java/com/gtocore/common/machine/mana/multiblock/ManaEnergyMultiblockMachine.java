package com.gtocore.common.machine.mana.multiblock;

import com.gtolib.api.machine.mana.feature.IManaEnergyMachine;
import com.gtolib.api.recipe.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManaEnergyMultiblockMachine extends ManaMultiblockMachine implements IManaEnergyMachine {

    private IEnergyContainer container = IEnergyContainer.DEFAULT;

    public ManaEnergyMultiblockMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        container = new ManaEnergyContainer(getManaContainer().getMaxIORate(), getManaContainer());
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        container = IEnergyContainer.DEFAULT;
    }

    @Override
    @Nullable
    public GTRecipe getRealRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        recipe = super.getRealRecipe(unit, recipe);
        if (recipe == null) return null;
        long eu = recipe.getInputEUt();
        if (eu > 0) {
            recipe = GTORecipeModifiers.externalEnergyOverclocking(this, unit, recipe, eu, getManaContainer().getMaxIORate(), true, 1, 1);
            return recipe;
        } else {
            return GTORecipeModifiers.manaOverclocking(this, unit, recipe, getManaContainer().getMaxIORate());
        }
    }

    @Override
    public @NotNull IEnergyContainer getEnergyContainer() {
        return container;
    }
}

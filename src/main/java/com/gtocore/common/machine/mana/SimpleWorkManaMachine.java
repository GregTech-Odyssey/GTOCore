package com.gtocore.common.machine.mana;

import com.gtolib.api.machine.mana.feature.IManaEnergyMachine;
import com.gtolib.api.recipe.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleWorkManaMachine extends SimpleManaMachine implements IManaEnergyMachine {

    private final IEnergyContainer container;

    public SimpleWorkManaMachine(MetaMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
        container = new ManaEnergyContainer(getManaContainer().getMaxIORate(), getManaContainer());
    }

    @Nullable
    @Override
    public GTRecipe doModifyRecipe(RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        long eu = recipe.getInputEUt();
        if (eu > 0) {
            recipe = GTORecipeModifiers.externalEnergyOverclocking(this, unit, recipe, eu, getTierMana(), true, 1, 1);
            return recipe;
        } else {
            return GTORecipeModifiers.manaOverclocking(this, unit, recipe, getTierMana());
        }
    }

    @Override
    public @NotNull IEnergyContainer getEnergyContainer() {
        return container;
    }
}

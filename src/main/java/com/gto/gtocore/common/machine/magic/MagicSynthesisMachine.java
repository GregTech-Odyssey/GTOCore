package com.gto.gtocore.common.machine.magic;

import com.gto.gtocore.api.machine.SimpleNoEnergyMachine;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.common.data.GTMachines;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagicSynthesisMachine extends SimpleNoEnergyMachine {

    public MagicSynthesisMachine(IMachineBlockEntity holder, int tier) {
        super(holder, tier, GTMachines.defaultTankSizeFunction);
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe == null) return false;
        List<Content> recipeInputs = recipe.inputs.get(ItemRecipeCapability.CAP);
        for (int i = 0; i < recipeInputs.size(); i++) {
            if (!ItemRecipeCapability.CAP.of(recipeInputs.get(i).content).test(importItems.getStackInSlot(i))) return false;
        }
        return super.beforeWorking(recipe);
    }
}

package com.gto.gtocore.common.machine.multiblock.electric.viod;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class VoidTransporterMachine extends ElectricMultiblockMachine {

    public VoidTransporterMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (hasProxies()) {
            GTRecipe recipe = GTORecipeBuilder.ofRaw().EUt(480).duration(200).buildRawRecipe();
            if (recipe.matchTickRecipe(this).isSuccess()) return recipe;
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
    }
}

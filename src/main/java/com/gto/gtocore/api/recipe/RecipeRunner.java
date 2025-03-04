package com.gto.gtocore.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

public interface RecipeRunner {

    static boolean matchRecipe(IRecipeLogicMachine holder, GTRecipe recipe) {
        return recipe.matchRecipe(holder).isSuccess();
    }

    static boolean matchTickRecipe(IRecipeLogicMachine holder, GTRecipe recipe) {
        return recipe.matchTickRecipe(holder).isSuccess();
    }

    static boolean matchRecipeInput(IRecipeLogicMachine holder, GTRecipe recipe) {
        return recipe.matchRecipeContents(IO.IN, holder, recipe.inputs, false).isSuccess();
    }

    static boolean matchRecipeOutput(IRecipeLogicMachine holder, GTRecipe recipe) {
        return recipe.matchRecipeContents(IO.OUT, holder, recipe.outputs, false).isSuccess();
    }

    static boolean matchRecipeTickInput(IRecipeLogicMachine holder, GTRecipe recipe) {
        return recipe.matchRecipeContents(IO.IN, holder, recipe.tickInputs, true).isSuccess();
    }

    static boolean matchRecipeTickOutput(IRecipeLogicMachine holder, GTRecipe recipe) {
        return recipe.matchRecipeContents(IO.OUT, holder, recipe.tickOutputs, true).isSuccess();
    }

    static boolean handleRecipeInput(IRecipeLogicMachine holder, GTRecipe recipe) {
        return recipe.handleRecipeIO(IO.IN, holder, holder.getRecipeLogic().getChanceCaches());
    }

    static boolean handleRecipeOutput(IRecipeLogicMachine holder, GTRecipe recipe) {
        return recipe.handleRecipeIO(IO.OUT, holder, holder.getRecipeLogic().getChanceCaches());
    }

    static boolean handleRecipeIO(IRecipeLogicMachine holder, GTRecipe recipe, IO io) {
        return recipe.handleRecipeIO(io, holder, holder.getRecipeLogic().getChanceCaches());
    }
}

package com.gto.gtocore.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import java.util.List;

public interface RecipeRunner {

    static boolean checkConditions(IRecipeLogicMachine holder, GTRecipe recipe) {
        return recipe.checkConditions(holder.getRecipeLogic()).isSuccess();
    }

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
        return handleRecipeIO(holder, recipe, IO.IN);
    }

    static boolean handleRecipeOutput(IRecipeLogicMachine holder, GTRecipe recipe) {
        return handleRecipeIO(holder, recipe, IO.OUT);
    }

    static boolean handleRecipeIO(IRecipeLogicMachine holder, GTRecipe recipe, IO io) {
        return recipe.handleRecipeIO(io, holder, holder.getRecipeLogic().getChanceCaches());
    }

    /**
     * @return 是否失败
     */
    static boolean handleTickRecipe(IRecipeLogicMachine holder, IO io, GTRecipe recipe, List<Content> contents, RecipeCapability<?> capability) {
        if (contents == null || contents.isEmpty()) return false;
        List<IRecipeHandler<?>> handlers = holder.getCapabilitiesProxy().get(io, capability);
        if (handlers == null) return true;
        List contentList = contents.stream().map(Content::getContent).toList();
        for (IRecipeHandler<?> handler : handlers) {
            contentList = handler.handleRecipeInner(io, recipe, contentList, null, false);
            if (contentList == null || contentList.isEmpty()) return false;
        }
        return !contentList.isEmpty();
    }
}

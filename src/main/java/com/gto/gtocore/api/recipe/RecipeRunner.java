package com.gto.gtocore.api.recipe;

import com.gto.gtocore.api.machine.feature.multiblock.IMEOutputMachine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.util.List;
import java.util.Map;

public interface RecipeRunner {

    static boolean checkConditions(IRecipeLogicMachine holder, GTRecipe recipe) {
        return recipe.checkConditions(holder.getRecipeLogic()).isSuccess();
    }

    static boolean matchRecipe(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (holder instanceof IMEOutputMachine machine && machine.gTOCore$DualMEOutput(recipe)) {
            return matchRecipeInput(holder, recipe);
        }
        return recipe.matchRecipe(holder).isSuccess();
    }

    static boolean matchTickRecipe(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (!matchRecipeTickInput(holder, recipe)) return false;
        return matchRecipeTickOutput(holder, recipe);
    }

    static boolean matchRecipeInput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (recipe.inputs.isEmpty()) return true;
        return recipe.matchRecipeContents(IO.IN, holder, recipe.inputs, false).isSuccess();
    }

    static boolean matchRecipeOutput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (recipe.outputs.isEmpty()) return true;
        return recipe.matchRecipeContents(IO.OUT, holder, recipe.outputs, false).isSuccess();
    }

    static boolean matchRecipeTickInput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (recipe.tickInputs.isEmpty()) return true;
        return recipe.matchRecipeContents(IO.IN, holder, recipe.tickInputs, true).isSuccess();
    }

    static boolean matchRecipeTickOutput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (recipe.tickOutputs.isEmpty()) return true;
        return recipe.matchRecipeContents(IO.OUT, holder, recipe.tickOutputs, true).isSuccess();
    }

    static boolean handleRecipeInput(IRecipeLogicMachine holder, GTRecipe recipe) {
        return handleRecipeIO(holder, recipe, IO.IN);
    }

    static boolean handleRecipeOutput(IRecipeLogicMachine holder, GTRecipe recipe) {
        return handleRecipeIO(holder, recipe, IO.OUT);
    }

    static boolean handleRecipeIO(IRecipeLogicMachine holder, GTRecipe recipe, IO io) {
        return handleRecipeIO(holder, recipe, io, holder.getRecipeLogic().getChanceCaches());
    }

    static boolean handleRecipeIO(IRecipeCapabilityHolder holder, GTRecipe recipe, IO io, Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches) {
        return recipe.handleRecipeIO(io, holder, chanceCaches);
    }

    /**
     * @return 是否失败
     */
    static boolean handleTickRecipe(IRecipeCapabilityHolder holder, IO io, GTRecipe recipe, List<Content> contents, RecipeCapability<?> capability) {
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

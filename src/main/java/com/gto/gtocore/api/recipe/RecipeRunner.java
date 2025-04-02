package com.gto.gtocore.api.recipe;

import com.gto.gtocore.api.machine.feature.multiblock.IMEOutputMachine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface RecipeRunner {

    static boolean check(IRecipeLogicMachine machine, @Nullable GTRecipe recipe) {
        if (recipe == null) return false;
        if (recipe.parallels > 1) return true;
        return RecipeRunner.checkConditions(machine, recipe) && RecipeRunner.matchRecipe(machine, recipe) && RecipeRunner.matchTickRecipe(machine, recipe);
    }

    static boolean checkConditions(IRecipeLogicMachine holder, GTRecipe recipe) {
        return RecipeHelper.checkConditions(recipe, holder.getRecipeLogic()).isSuccess();
    }

    static boolean matchRecipe(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (holder instanceof IMEOutputMachine machine && machine.gTOCore$DualMEOutput(recipe)) {
            return matchRecipeInput(holder, recipe);
        }
        return RecipeHelper.matchRecipe(holder, recipe).isSuccess();
    }

    static boolean matchTickRecipe(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (!matchRecipeTickInput(holder, recipe)) return false;
        return matchRecipeTickOutput(holder, recipe);
    }

    static boolean matchRecipeInput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (recipe.inputs.isEmpty()) return true;
        return RecipeHelper.handleRecipe(holder, recipe, IO.IN, recipe.inputs, Collections.emptyMap(), false, true).isSuccess();
    }

    static boolean matchRecipeOutput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (recipe.outputs.isEmpty()) return true;
        return RecipeHelper.handleRecipe(holder, recipe, IO.OUT, recipe.outputs, Collections.emptyMap(), false, true).isSuccess();
    }

    static boolean matchRecipeTickInput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (recipe.tickInputs.isEmpty()) return true;
        return RecipeHelper.handleRecipe(holder, recipe, IO.IN, recipe.tickInputs, Collections.emptyMap(), true, true).isSuccess();
    }

    static boolean matchRecipeTickOutput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (recipe.tickOutputs.isEmpty()) return true;
        return RecipeHelper.handleRecipe(holder, recipe, IO.OUT, recipe.tickOutputs, Collections.emptyMap(), true, true).isSuccess();
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
        return RecipeHelper.handleRecipe(holder, recipe, io, io == IO.IN ? recipe.inputs : recipe.outputs, chanceCaches, false, false).isSuccess();
    }

    /**
     * @return 是否失败
     */
    static boolean handleTickRecipe(IRecipeCapabilityHolder holder, IO io, GTRecipe recipe, List<Content> contents, RecipeCapability<?> capability) {
        if (contents == null || contents.isEmpty()) return false;
        List<IRecipeHandler<?>> handlers = holder.getCapabilitiesFlat(io, capability);
        if (handlers.isEmpty()) return true;
        List<?> contentList = contents.stream().map(Content::getContent).toList();
        for (IRecipeHandler<?> handler : handlers) {
            contentList = handler.handleRecipeInner(io, recipe, (List) contentList, false);
            if (contentList == null || contentList.isEmpty()) return false;
        }
        return !contentList.isEmpty();
    }
}

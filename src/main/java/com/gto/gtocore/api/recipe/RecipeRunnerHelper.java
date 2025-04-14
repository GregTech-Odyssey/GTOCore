package com.gto.gtocore.api.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.feature.IRecipeSearchMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IMEOutputMachine;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraft.nbt.CompoundTag;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface RecipeRunnerHelper {

    GTRecipe EMPTY_RECIPE = new GTRecipe(GTORecipeTypes.DUMMY_RECIPES, GTOCore.id("empty"), Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), List.of(), List.of(), new CompoundTag(), 0, GTORecipeTypes.DUMMY_RECIPES.getCategory());

    static boolean check(IRecipeLogicMachine machine, @Nullable GTRecipe recipe) {
        if (recipe == null || !checkConditions(machine, recipe) || !matchTickRecipe(machine, recipe)) return false;
        if (recipe.parallels > 1) return true;
        return matchRecipe(machine, recipe);
    }

    static boolean checkConditions(IRecipeLogicMachine holder, GTRecipe recipe) {
        return RecipeHelper.checkConditions(recipe, holder.getRecipeLogic()).isSuccess();
    }

    static boolean matchRecipe(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (holder instanceof IRecipeSearchMachine searchMachine) {
            return searchMachine.matchRecipe(recipe);
        }
        return matchRecipeInput(holder, recipe) && matchRecipeOutput(holder, recipe);
    }

    static boolean matchTickRecipe(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (holder instanceof IRecipeSearchMachine searchMachine) {
            return searchMachine.matchTickRecipe(recipe);
        }
        return matchRecipeTickInput(holder, recipe) && matchRecipeTickOutput(holder, recipe);
    }

    static boolean matchRecipeInput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (recipe.inputs.isEmpty()) return true;
        return RecipeHelper.handleRecipe(holder, recipe, IO.IN, recipe.inputs, Collections.emptyMap(), false, true).isSuccess();
    }

    static boolean matchRecipeOutput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (recipe.outputs.isEmpty()) return true;
        if (holder instanceof IMEOutputMachine machine && machine.gTOCore$DualMEOutput(recipe)) {
            return true;
        }
        return RecipeHelper.handleRecipe(holder, recipe, IO.OUT, recipe.outputs, Collections.emptyMap(), false, true).isSuccess();
    }

    static boolean matchRecipeTickInput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        for (Map.Entry<RecipeCapability<?>, List<Content>> entry : recipe.tickInputs.entrySet()) {
            if (handleTickRecipe(holder, IO.IN, recipe, entry.getValue(), entry.getKey(), true)) {
                return false;
            }
        }
        return true;
    }

    static boolean matchRecipeTickOutput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        for (Map.Entry<RecipeCapability<?>, List<Content>> entry : recipe.tickOutputs.entrySet()) {
            if (handleTickRecipe(holder, IO.OUT, recipe, entry.getValue(), entry.getKey(), true)) {
                return false;
            }
        }
        return true;
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
    static boolean handleTickRecipe(IRecipeCapabilityHolder holder, IO io, @Nullable GTRecipe recipe, @Nullable List<Content> contents, RecipeCapability<?> capability) {
        return handleTickRecipe(holder, io, recipe, contents, capability, false);
    }

    private static boolean handleTickRecipe(IRecipeCapabilityHolder holder, IO io, @Nullable GTRecipe recipe, @Nullable List<Content> contents, RecipeCapability<?> capability, boolean simulate) {
        if (contents == null || contents.isEmpty()) return false;
        List<IRecipeHandler<?>> handlers = holder.getCapabilitiesFlat(io, capability);
        if (handlers.isEmpty()) return true;
        List contentList = new ObjectArrayList<>(contents.size());
        for (Content content : contents) {
            contentList.add(content.getContent());
        }
        for (IRecipeHandler<?> handler : handlers) {
            contentList = handler.handleRecipeInner(io, recipe, contentList, simulate);
            if (contentList == null || contentList.isEmpty()) return false;
        }
        return true;
    }

    static boolean handleRecipe(IRecipeCapabilityHolder holder, IO io, @Nullable List<?> contents, RecipeCapability<?> capability, boolean simulate) {
        if (contents == null || contents.isEmpty()) return true;
        List<IRecipeHandler<?>> handlers = holder.getCapabilitiesFlat(io, capability);
        if (handlers.isEmpty()) return false;
        for (IRecipeHandler<?> handler : handlers) {
            contents = handler.handleRecipe(io, EMPTY_RECIPE, contents, simulate);
            if (contents == null || contents.isEmpty()) return true;
        }
        return false;
    }
}

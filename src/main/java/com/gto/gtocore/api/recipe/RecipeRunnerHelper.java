package com.gto.gtocore.api.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.feature.IRecipeSearchMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IMEOutputMachine;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
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

    private static void setIdleReason(IRecipeLogicMachine machine, IdleReason reason) {
        if (machine.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
            enhancedRecipeLogic.gTOCore$setIdleReason(reason.reason());
        }
    }

    static boolean checkTier(IRecipeLogicMachine machine, GTRecipe recipe) {
        int tier = RecipeHelper.getRecipeEUtTier(recipe);
        if (tier > 0) {
            if (machine instanceof WorkableElectricMultiblockMachine electricMultiblockMachine) {
                return tier <= electricMultiblockMachine.getOverclockTier();
            } else if (machine instanceof SimpleTieredMachine tieredMachine) {
                return tier <= tieredMachine.getOverclockTier();
            }
        }
        return true;
    }

    static boolean check(IRecipeLogicMachine machine, @Nullable GTRecipe recipe) {
        if (recipe == null || !checkConditions(machine, recipe) || !matchTickRecipe(machine, recipe)) return false;
        if (recipe.parallels > 1) return true;
        return matchRecipe(machine, recipe);
    }

    static boolean checkConditions(IRecipeLogicMachine holder, GTRecipe recipe) {
        var conditionResult = RecipeHelper.checkConditions(recipe, holder.getRecipeLogic());
        if (conditionResult.isSuccess()) return true;
        if (holder.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
            enhancedRecipeLogic.gTOCore$setIdleReason(conditionResult.reason().copy());
        }
        return false;
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
        if (RecipeHelper.handleRecipe(holder, recipe, IO.OUT, recipe.outputs, Collections.emptyMap(), false, true).isSuccess()) {
            return true;
        }
        if (holder instanceof IRecipeLogicMachine machine) {
            setIdleReason(machine, IdleReason.OUTPUT_FULL);
        }
        return false;
    }

    static boolean matchRecipeTickInput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        for (Map.Entry<RecipeCapability<?>, List<Content>> entry : recipe.tickInputs.entrySet()) {
            var cap = entry.getKey();
            if (handleTickRecipe(holder, IO.IN, recipe, entry.getValue(), cap, true)) {
                if (holder instanceof IRecipeLogicMachine machine) {
                    if (cap == EURecipeCapability.CAP) {
                        setIdleReason(machine, IdleReason.NO_EU);
                    } else if (cap == CWURecipeCapability.CAP) {
                        setIdleReason(machine, IdleReason.NO_CWU);
                    }
                }
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
        if (RecipeHelper.handleRecipe(holder, recipe, io, io == IO.IN ? recipe.inputs : recipe.outputs, chanceCaches, false, false).isSuccess()) {
            return true;
        }
        if (io == IO.IN && holder instanceof IRecipeLogicMachine machine && machine.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
            enhancedRecipeLogic.gTOCore$setIdleReason(IdleReason.INVALID_INPUT.reason());
        }
        return false;
    }

    /**
     * @return 是否失败
     */
    static boolean handleTickRecipe(IRecipeCapabilityHolder holder, IO io, @Nullable GTRecipe recipe, @Nullable List<Content> contents, RecipeCapability<?> capability) {
        if (io == IO.OUT) {
            if (GTOConfig.getDifficulty() == 1) {
                if (!handleTickRecipe(holder, io, recipe, contents, capability, true)) {
                    handleTickRecipe(holder, io, recipe, contents, capability, false);
                    return false;
                } else {
                    return true;
                }
            }
            handleTickRecipe(holder, io, recipe, contents, capability, false);
            return false;
        }
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

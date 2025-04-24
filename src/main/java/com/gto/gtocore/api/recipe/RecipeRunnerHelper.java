package com.gto.gtocore.api.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.feature.IRecipeSearchMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IDistinctRecipeHolder;
import com.gto.gtocore.api.machine.feature.multiblock.IMEOutputMachine;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.chance.boost.ChanceBoostFunction;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraft.nbt.CompoundTag;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
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
            boolean success = true;
            if (machine instanceof WorkableElectricMultiblockMachine electricMultiblockMachine) {
                success = tier <= electricMultiblockMachine.getOverclockTier();
            } else if (machine instanceof SimpleTieredMachine tieredMachine) {
                success = tier <= tieredMachine.getOverclockTier();
            }
            if (!success) {
                setIdleReason(machine, IdleReason.VOLTAGE_TIER_NOT_SATISFIES);
                return false;
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
            enhancedRecipeLogic.gTOCore$setIdleReason(conditionResult.reason());
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
        if (handleRecipe(holder, recipe, IO.IN, recipe.inputs, Collections.emptyMap(), false, true)) {
            return true;
        }
        if (holder instanceof IRecipeLogicMachine machine) {
            setIdleReason(machine, IdleReason.NO_MATCH);
        }
        return false;
    }

    static boolean matchRecipeOutput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (recipe.outputs.isEmpty()) return true;
        if (holder instanceof IMEOutputMachine machine && machine.gTOCore$DualMEOutput(recipe)) {
            return true;
        }
        if (handleRecipe(holder, recipe, IO.OUT, recipe.outputs, Collections.emptyMap(), false, true)) {
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
        if (handleRecipe(holder, recipe, io, io == IO.IN ? recipe.inputs : recipe.outputs, chanceCaches, false, false)) {
            return true;
        }
        if (io == IO.IN && holder instanceof IRecipeLogicMachine machine) {
            setIdleReason(machine, IdleReason.INVALID_INPUT);
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

    static boolean handleContent(IRecipeCapabilityHolder holder, IO io, @Nullable List<?> contents, RecipeCapability<?> capability, boolean simulate) {
        if (contents == null || contents.isEmpty()) return true;
        List<IRecipeHandler<?>> handlers = holder.getCapabilitiesFlat(io, capability);
        if (handlers.isEmpty()) return false;
        for (IRecipeHandler<?> handler : handlers) {
            contents = handler.handleRecipe(io, EMPTY_RECIPE, contents, simulate);
            if (contents == null || contents.isEmpty()) return true;
        }
        return false;
    }

    static boolean handleRecipe(IRecipeCapabilityHolder holder, GTRecipe recipe, IO io, Map<RecipeCapability<?>, List<Content>> contents, Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches, boolean isTick, boolean simulated) {
        RecipeHandlerList currentDistinc = null;
        IDistinctRecipeHolder distinctRecipeHolder = null;
        if (io == IO.IN && holder instanceof IDistinctRecipeHolder recipeHolder && ((IEnhancedRecipeLogic) recipeHolder.getRecipeLogic()).canLockRecipe()) {
            distinctRecipeHolder = recipeHolder;
            if (recipe.id.equals(recipeHolder.getRecipeId())) {
                if (recipeHolder.isDistinctState() || !simulated) currentDistinc = recipeHolder.getCurrentDistinct();
            } else {
                recipeHolder.setCurrentDistinct(null);
                recipeHolder.setRecipeId(recipe.id);
            }
        }

        Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContents = new Reference2ObjectOpenHashMap<>();
        ChanceBoostFunction function = recipe.getType().getChanceFunction();
        int recipeTier = RecipeHelper.getPreOCRecipeEuTier(recipe);
        int chanceTier = recipeTier + recipe.ocLevel;
        for (var entry : contents.entrySet()) {
            RecipeCapability<?> cap = entry.getKey();
            if (!cap.doMatchInRecipe()) continue;
            var list = entry.getValue();
            if (list.isEmpty()) continue;
            List<Content> chancedContents = new ObjectArrayList<>();
            var contentList = new ObjectArrayList<>(list.size());
            for (Content cont : list) {
                if (simulated) {
                    contentList.add(cont.content);
                } else {
                    if (cont.chance >= cont.maxChance) {
                        contentList.add(cont.content);
                    } else {
                        chancedContents.add(cont);
                    }
                }
            }
            if (!chancedContents.isEmpty()) {
                var cache = chanceCaches.get(cap);
                chancedContents = ChanceLogic.OR.roll(chancedContents, function, recipeTier, chanceTier, cache, recipe.parallels);
                for (Content cont : chancedContents) {
                    contentList.add(cont.content);
                }
            }

            if (!contentList.isEmpty()) {
                recipeContents.put(cap, contentList);
            }
        }
        if (recipeContents.isEmpty()) return true;
        if (handleContentsInternal(io, io, holder, recipeContents, currentDistinc, recipe, distinctRecipeHolder, isTick, simulated)) return true;
        return handleContentsInternal(IO.BOTH, io, holder, recipeContents, currentDistinc, recipe, distinctRecipeHolder, isTick, simulated);
    }

    private static boolean handleContentsInternal(IO capIO, IO io, IRecipeCapabilityHolder holder, Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContents, @Nullable RecipeHandlerList currentDistinc, GTRecipe recipe, @Nullable IDistinctRecipeHolder distinctRecipeHolder, boolean isTick, boolean simulated) {
        if (currentDistinc != null && distinctRecipeHolder != null) {
            var contents = handleRecipeContent(currentDistinc, io, recipe, recipeContents, simulated, simulated);
            if (contents.isEmpty()) {
                if (!distinctRecipeHolder.isDistinctState()) distinctRecipeHolder.setCurrentDistinct(null);
                return true;
            } else {
                if (!simulated) return false;
            }
        }

        List<RecipeHandlerList> distinct;
        List<RecipeHandlerList> indistinct;
        if (capIO == IO.IN && holder instanceof IDistinctRecipeHolder recipeHolder) {
            distinct = recipeHolder.getDistinct();
            indistinct = recipeHolder.getIndistinct();
        } else {
            var handlers = holder.getCapabilitiesProxy().get(capIO);
            if (handlers == null) return false;
            if (!isTick && capIO == IO.OUT) {
                handlers.sort(RecipeHandlerList.COMPARATOR.reversed());
            }
            if (capIO == IO.OUT) {
                distinct = List.of();
                indistinct = handlers;
            } else {
                distinct = new ObjectArrayList<>();
                indistinct = new ObjectArrayList<>();
                for (var handler : handlers) {
                    if (handler.isDistinct()) distinct.add(handler);
                    else indistinct.add(handler);
                }
            }
        }

        for (var handler : distinct) {
            var contents = handleRecipeContent(handler, io, recipe, recipeContents, true, true);
            if (contents.isEmpty()) {
                if (!simulated) {
                    return handleRecipeContent(handler, io, recipe, recipeContents, false, false).isEmpty();
                } else {
                    if (distinctRecipeHolder != null) {
                        distinctRecipeHolder.setCurrentDistinct(handler);
                    }
                    return true;
                }
            }
        }

        for (var handler : indistinct) {
            if (handleRecipeContent(handler, io, recipe, recipeContents, simulated, false).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private static Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> handleRecipeContent(RecipeHandlerList list, IO io, GTRecipe recipe, Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> contents, boolean simulate, boolean distinct) {
        if (list.getHandlerMap().isEmpty()) return contents;
        var copy = distinct ? new Reference2ObjectOpenHashMap<>(contents) : contents;
        for (var it = copy.reference2ObjectEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            List left = entry.getValue();
            var handlerList = list.getCapability(entry.getKey());
            for (var handler : handlerList) {
                left = handler.handleRecipe(io, recipe, left, simulate);
                if (left == null || left.isEmpty()) {
                    it.remove();
                    break;
                }
            }
            if (!distinct) entry.setValue(left);
        }
        return copy;
    }
}

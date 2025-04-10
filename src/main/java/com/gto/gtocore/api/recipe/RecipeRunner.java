package com.gto.gtocore.api.recipe;

import com.gto.gtocore.api.machine.feature.multiblock.IDistinctRecipeHolder;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.chance.boost.ChanceBoostFunction;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public final class RecipeRunner {

    private final RecipeHandlerList currentDistinc;
    private final IRecipeCapabilityHolder holder;
    private final IDistinctRecipeHolder distinctRecipeHolder;
    private final GTRecipe recipe;
    private final IO io;
    private final boolean isTick;
    private final Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches;
    private final Map<IO, List<RecipeHandlerList>> capabilityProxies;
    private final boolean simulated;
    private Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContents;
    private final Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> searchRecipeContents;

    public RecipeRunner(GTRecipe recipe, IO io, boolean isTick, IRecipeCapabilityHolder holder, Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches, boolean simulated) {
        RecipeHandlerList list = null;
        if (io == IO.IN && holder instanceof IDistinctRecipeHolder recipeHolder && ((IEnhancedRecipeLogic) recipeHolder.getRecipeLogic()).canLockRecipe()) {
            distinctRecipeHolder = recipeHolder;
            if (recipe.id.equals(recipeHolder.getRecipeId())) {
                if (!simulated) list = recipeHolder.getCurrentDistinct();
            } else {
                recipeHolder.setCurrentDistinct(null);
                recipeHolder.setRecipeId(recipe.id);
            }
        } else {
            distinctRecipeHolder = null;
        }
        this.currentDistinc = list;
        this.holder = holder;
        this.recipe = recipe;
        this.io = io;
        this.isTick = isTick;
        this.chanceCaches = chanceCaches;
        this.capabilityProxies = holder.getCapabilitiesProxy();
        this.recipeContents = new Reference2ObjectOpenHashMap<>();
        this.searchRecipeContents = (simulated || currentDistinc != null) ? recipeContents : new Reference2ObjectOpenHashMap<>();
        this.simulated = simulated;
    }

    @NotNull
    public ActionResult handle(Map<RecipeCapability<?>, List<Content>> entries) {
        fillContentMatchList(entries);

        if (searchRecipeContents.isEmpty()) {
            return ActionResult.PASS_NO_CONTENTS;
        }

        return this.handleContents();
    }

    private void fillContentMatchList(Map<RecipeCapability<?>, List<Content>> entries) {
        ChanceBoostFunction function = recipe.getType().getChanceFunction();
        int recipeTier = RecipeHelper.getPreOCRecipeEuTier(recipe);
        int chanceTier = recipeTier + recipe.ocLevel;
        for (var entry : entries.entrySet()) {
            RecipeCapability<?> cap = entry.getKey();
            if (!cap.doMatchInRecipe()) continue;
            if (entry.getValue().isEmpty()) continue;
            List<Content> chancedContents = new ObjectArrayList<>();
            var contentList = this.recipeContents.computeIfAbsent(cap, c -> new ObjectArrayList<>());
            var searchContentList = this.searchRecipeContents.computeIfAbsent(cap, c -> new ObjectArrayList<>());
            for (Content cont : entry.getValue()) {
                if (currentDistinc == null) searchContentList.add(cont.content);
                if (simulated) continue;
                if (cont.chance >= cont.maxChance) {
                    contentList.add(cont.content);
                } else {
                    chancedContents.add(cont);
                }
            }
            if (!chancedContents.isEmpty()) {
                var cache = this.chanceCaches.get(cap);
                chancedContents = ChanceLogic.OR.roll(chancedContents, function, recipeTier, chanceTier, cache, recipe.parallels);
                for (Content cont : chancedContents) {
                    contentList.add(cont.content);
                }
            }

            if (contentList.isEmpty()) recipeContents.remove(cap);
        }
    }

    private ActionResult handleContents() {
        var result = handleContentsInternal(io);
        if (result.isSuccess()) return result;
        return handleContentsInternal(IO.BOTH);
    }

    private ActionResult handleContentsInternal(IO capIO) {
        if (recipeContents.isEmpty()) return ActionResult.SUCCESS;
        if (!capabilityProxies.containsKey(capIO)) {
            return ActionResult.FAIL_NO_CAPABILITIES;
        }

        if (currentDistinc != null) {
            recipeContents = handleRecipe(currentDistinc, io, recipe, recipeContents, false, false);
            if (recipeContents.isEmpty()) {
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.FAIL_NO_REASON;
            }
        }

        List<RecipeHandlerList> distinct;
        List<RecipeHandlerList> indistinct;
        if (io == IO.IN && holder instanceof IDistinctRecipeHolder recipeHolder) {
            distinct = recipeHolder.getDistinct();
            indistinct = recipeHolder.getIndistinct();
        } else {
            var handlers = capabilityProxies.get(capIO);
            if (!isTick && capIO == IO.OUT) {
                handlers.sort(RecipeHandlerList.COMPARATOR.reversed());
            }
            if (io == IO.OUT) {
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
            var res = handleRecipe(handler, io, recipe, searchRecipeContents, true, true);
            if (res.isEmpty()) {
                if (!simulated) {
                    recipeContents = handleRecipe(handler, io, recipe, recipeContents, false, false);
                    if (recipeContents.isEmpty()) {
                        return ActionResult.SUCCESS;
                    } else {
                        return ActionResult.FAIL_NO_REASON;
                    }
                } else {
                    if (distinctRecipeHolder != null) {
                        distinctRecipeHolder.setCurrentDistinct(handler);
                    }
                    recipeContents.clear();
                    return ActionResult.SUCCESS;
                }
            }
        }

        for (var handler : indistinct) {
            recipeContents = handleRecipe(handler, io, recipe, recipeContents, simulated, false);
            if (recipeContents.isEmpty()) {
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.FAIL_NO_REASON;
    }

    private static Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> handleRecipe(RecipeHandlerList list, IO io, GTRecipe recipe, Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> contents, boolean simulate, boolean distinct) {
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
        }
        return copy;
    }
}

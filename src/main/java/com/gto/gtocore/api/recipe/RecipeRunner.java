package com.gto.gtocore.api.recipe;

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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public final class RecipeRunner {

    public record RecipeHandlingResult(ActionResult result, @Nullable RecipeCapability<?> capability) {

        static final RecipeHandlingResult SUCCESS = new RecipeHandlingResult(ActionResult.SUCCESS, null);

        public boolean isSuccess() {
            return result.isSuccess();
        }
    }

    private final GTRecipe recipe;
    private final IO io;
    private final boolean isTick;
    private final Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches;
    private final Map<IO, List<RecipeHandlerList>> capabilityProxies;
    private final boolean simulated;
    private Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContents;
    private final Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> searchRecipeContents;

    public RecipeRunner(GTRecipe recipe, IO io, boolean isTick, IRecipeCapabilityHolder holder, Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches, boolean simulated) {
        this.recipe = recipe;
        this.io = io;
        this.isTick = isTick;
        this.chanceCaches = chanceCaches;
        this.capabilityProxies = holder.getCapabilitiesProxy();
        this.recipeContents = new Reference2ObjectOpenHashMap<>();
        this.searchRecipeContents = simulated ? recipeContents : new Reference2ObjectOpenHashMap<>();
        this.simulated = simulated;
    }

    @NotNull
    public RecipeHandlingResult handle(Map<RecipeCapability<?>, List<Content>> entries) {
        fillContentMatchList(entries);

        if (searchRecipeContents.isEmpty()) {
            return new RecipeHandlingResult(ActionResult.PASS_NO_CONTENTS, null);
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
                searchContentList.add(cont.content);
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

    private RecipeHandlingResult handleContents() {
        var result = handleContentsInternal(io);
        if (result.isSuccess()) return result;
        return handleContentsInternal(IO.BOTH);
    }

    private RecipeHandlingResult handleContentsInternal(IO capIO) {
        if (recipeContents.isEmpty()) return RecipeHandlingResult.SUCCESS;
        if (!capabilityProxies.containsKey(capIO)) {
            return new RecipeHandlingResult(ActionResult.FAIL_NO_CAPABILITIES, null);
        }

        var handlers = capabilityProxies.get(capIO);
        if (!isTick && capIO == IO.OUT) {
            handlers.sort(RecipeHandlerList.COMPARATOR.reversed());
        }
        List<RecipeHandlerList> distinct = new ObjectArrayList<>();
        List<RecipeHandlerList> indistinct = new ObjectArrayList<>();
        for (var handler : handlers) {
            if (handler.isDistinct()) distinct.add(handler);
            else indistinct.add(handler);
        }

        for (var handler : distinct) {
            var res = handleRecipe(handler, io, recipe, searchRecipeContents, true);
            if (res.isEmpty()) {
                if (!simulated) {
                    res = handleRecipe(handler, io, recipe, recipeContents, false);
                    if (res.isEmpty()) {
                        recipeContents.clear();
                        return RecipeHandlingResult.SUCCESS;
                    }
                } else {
                    recipeContents.clear();
                    return RecipeHandlingResult.SUCCESS;
                }
            }
        }

        for (var handler : indistinct) {
            recipeContents = handleRecipe(handler, io, recipe, recipeContents, simulated);
            if (recipeContents.isEmpty()) {
                return RecipeHandlingResult.SUCCESS;
            }
        }

        return new RecipeHandlingResult(ActionResult.FAIL_NO_REASON, null);
    }

    private static Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> handleRecipe(RecipeHandlerList list, IO io, GTRecipe recipe, Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> contents, boolean simulate) {
        if (list.getHandlerMap().isEmpty()) return contents;
        var copy = list.isDistinct() ? new Reference2ObjectOpenHashMap<>(contents) : contents;
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

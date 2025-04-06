package com.gto.gtocore.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.lookup.AbstractMapIngredient;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

class SearchRecipeIterator implements Iterator<GTRecipe> {

    boolean hasNext = true;

    final List<List<AbstractMapIngredient>> ingredients;
    final Predicate<GTRecipe> canHandle;
    final IRecipeCapabilityHolder holder;
    final GTRecipeType recipeType;
    private final RecipeIterator recipeIterator;

    SearchRecipeIterator(IRecipeCapabilityHolder holder, GTRecipeType recipeType, boolean tick) {
        this.holder = holder;
        this.recipeType = recipeType;
        if (tick) {
            canHandle = recipe -> RecipeRunnerHelper.matchRecipe(holder, recipe) && RecipeRunnerHelper.matchTickRecipe(holder, recipe);
        } else {
            canHandle = recipe -> RecipeRunnerHelper.matchRecipe(holder, recipe);
        }
        ingredients = fromHolder(holder);
        recipeIterator = createRecipeIterator();
    }

    RecipeIterator createRecipeIterator() {
        return new RecipeIterator(recipeType, ingredients, canHandle);
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public GTRecipe next() {
        while (recipeIterator.hasNext()) {
            GTRecipe recipe = recipeIterator.next();
            if (recipe == null) continue;
            return recipe;
        }
        for (GTRecipeType.ICustomRecipeLogic logic : recipeType.getCustomRecipeLogicRunners()) {
            GTRecipe recipe = logic.createCustomRecipe(holder);
            if (recipe != null) return recipe;
        }
        hasNext = false;
        return null;
    }

    private static List<List<AbstractMapIngredient>> fromHolder(@NotNull IRecipeCapabilityHolder r) {
        var handlerMap = r.getCapabilitiesFlat().getOrDefault(IO.IN, Collections.emptyMap());
        int size = handlerMap.size();
        List<List<AbstractMapIngredient>> list = new ObjectArrayList<>(size);
        for (var entry : handlerMap.entrySet()) {
            var cap = entry.getKey();
            var handlers = entry.getValue();
            if (!cap.isRecipeSearchFilter()) continue;
            for (var handler : handlers) {
                var compressed = cap.compressIngredients(handler.getContents());
                list.addAll(cap.convertCompressedIngredients(compressed));
            }
        }
        if (list.isEmpty()) return null;
        return list;
    }

    static class RecipeIterator implements Iterator<GTRecipe> {

        private int index;
        private final List<List<AbstractMapIngredient>> ingredients;
        private final GTRecipeType recipeType;
        private final Predicate<GTRecipe> canHandle;

        RecipeIterator(GTRecipeType recipeType, List<List<AbstractMapIngredient>> ingredients, Predicate<GTRecipe> canHandle) {
            this.ingredients = ingredients;
            this.recipeType = recipeType;
            this.canHandle = canHandle;
        }

        @Override
        public boolean hasNext() {
            return ingredients != null && this.index < this.ingredients.size();
        }

        @Override
        public GTRecipe next() {
            if (ingredients == null) return null;
            GTRecipe r = null;
            while (index < ingredients.size()) {
                r = recipeType.getLookup().recurseIngredientTreeFindRecipe(ingredients, recipeType.getLookup().getLookup(), canHandle, index, 0, (1L << index));
                ++index;
                if (r != null) break;
            }
            return r;
        }
    }
}

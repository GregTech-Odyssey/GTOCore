package com.gto.gtocore.integration.emi;

import com.gto.gtocore.common.data.GTORecipes;

import net.minecraft.resources.ResourceLocation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeManager;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.registry.EmiRecipes;
import dev.emi.emi.registry.EmiStackList;
import dev.emi.emi.runtime.EmiReloadLog;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

public final class EMIManager implements EmiRecipeManager {

    private final List<EmiRecipeCategory> categories;
    private final Map<EmiRecipeCategory, List<EmiIngredient>> workstations;
    private final List<EmiRecipe> recipes;
    private final Map<EmiStack, List<EmiRecipe>> byInput = new Object2ObjectOpenCustomHashMap<>(new EmiStackList.ComparisonHashStrategy());
    private final Map<EmiStack, List<EmiRecipe>> byOutput = new Object2ObjectOpenCustomHashMap<>(new EmiStackList.ComparisonHashStrategy());
    private final Map<EmiRecipeCategory, List<EmiRecipe>> byCategory = Maps.newHashMap();
    private final Map<ResourceLocation, EmiRecipe> byId = Maps.newHashMap();

    public EMIManager(List<EmiRecipeCategory> categories, Map<EmiRecipeCategory, List<EmiIngredient>> workstations, List<EmiRecipe> recipes) {
        recipes.addAll(GTORecipes.EMI_RECIPES);
        this.categories = categories;
        this.workstations = workstations;
        this.recipes = recipes;

        for (EmiRecipe recipe : recipes) {
            ResourceLocation id = recipe.getId();
            EmiRecipeCategory category = recipe.getCategory();
            byCategory.computeIfAbsent(category, a -> Lists.newArrayList()).add(recipe);
            if (id != null) {
                byId.put(id, recipe);
            }
        }

        Map<EmiStack, Set<EmiRecipe>> byInput = new Object2ObjectOpenCustomHashMap<>(new EmiStackList.ComparisonHashStrategy());
        Map<EmiStack, Set<EmiRecipe>> byOutput = new Object2ObjectOpenCustomHashMap<>(new EmiStackList.ComparisonHashStrategy());

        for (Map.Entry<EmiRecipeCategory, List<EmiRecipe>> entry : byCategory.entrySet()) {
            EmiRecipeCategory category = entry.getKey();
            List<EmiRecipe> cRecipes = entry.getValue();
            byCategory.put(category, cRecipes);
            for (EmiRecipe recipe : cRecipes) {
                recipe.getInputs().stream().flatMap(i -> i.getEmiStacks().stream()).forEach(i -> byInput.computeIfAbsent(i.copy(), b -> Sets.newLinkedHashSet()).add(recipe));
                recipe.getCatalysts().stream().flatMap(i -> i.getEmiStacks().stream()).forEach(i -> byInput.computeIfAbsent(i.copy(), b -> Sets.newLinkedHashSet()).add(recipe));
                recipe.getOutputs().forEach(i -> byOutput.computeIfAbsent(i.copy(), b -> Sets.newLinkedHashSet()).add(recipe));
            }
        }
        for (EmiStack key : byInput.keySet()) {
            Set<EmiRecipe> r = byInput.getOrDefault(key, null);
            if (r != null) {
                this.byInput.put(key, r.stream().toList());
            } else {
                EmiReloadLog.warn("Stack illegally self-mutated during recipe bake, causing recipe loss: " + key);
            }
        }
        for (EmiStack key : byOutput.keySet()) {
            Set<EmiRecipe> r = byOutput.getOrDefault(key, null);
            if (r != null) {
                this.byOutput.put(key, r.stream().toList());
            } else {
                EmiReloadLog.warn("Stack illegally self-mutated during recipe bake, causing recipe loss: " + key);
            }
        }
        for (EmiRecipeCategory category : workstations.keySet()) {
            List<EmiIngredient> w = workstations.getOrDefault(category, null);
            if (w != null) {
                workstations.put(category, w.stream().distinct().toList());
            } else {
                EmiReloadLog.warn("Recipe category illegally self-mutated during recipe bake, causing recipe loss: " + category);
            }
        }
        for (Map.Entry<EmiRecipeCategory, List<EmiRecipe>> entry : byCategory.entrySet()) {
            for (EmiIngredient ingredient : workstations.getOrDefault(entry.getKey(), List.of())) {
                for (EmiStack stack : ingredient.getEmiStacks()) {
                    EmiRecipes.byWorkstation.computeIfAbsent(stack, (s) -> Lists.newArrayList()).addAll(entry.getValue());
                }
            }
        }
    }

    @Override
    public List<EmiRecipeCategory> getCategories() {
        return categories;
    }

    @Override
    public List<EmiIngredient> getWorkstations(EmiRecipeCategory category) {
        return workstations.getOrDefault(category, List.of());
    }

    @Override
    public List<EmiRecipe> getRecipes() {
        return recipes;
    }

    @Override
    public List<EmiRecipe> getRecipes(EmiRecipeCategory category) {
        return byCategory.getOrDefault(category, List.of());
    }

    @Override
    public @Nullable EmiRecipe getRecipe(ResourceLocation id) {
        return byId.getOrDefault(id, null);
    }

    @Override
    public List<EmiRecipe> getRecipesByInput(EmiStack stack) {
        return byInput.getOrDefault(stack, List.of());
    }

    @Override
    public List<EmiRecipe> getRecipesByOutput(EmiStack stack) {
        return byOutput.getOrDefault(stack, List.of());
    }
}

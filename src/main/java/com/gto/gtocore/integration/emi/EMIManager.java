package com.gto.gtocore.integration.emi;

import com.gto.gtocore.common.data.GTORecipes;

import net.minecraft.resources.ResourceLocation;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeManager;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.registry.EmiRecipes;
import dev.emi.emi.registry.EmiStackList;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

public final class EMIManager implements EmiRecipeManager {

    private final List<EmiRecipeCategory> categories;
    private final Map<EmiRecipeCategory, List<EmiIngredient>> workstations;
    private final List<EmiRecipe> recipes;
    private final Map<EmiStack, List<EmiRecipe>> byInput = new Object2ObjectOpenCustomHashMap<>(new EmiStackList.ComparisonHashStrategy());
    private final Map<EmiStack, List<EmiRecipe>> byOutput = new Object2ObjectOpenCustomHashMap<>(new EmiStackList.ComparisonHashStrategy());
    private final Map<EmiRecipeCategory, List<EmiRecipe>> byCategory;
    private final Map<ResourceLocation, EmiRecipe> byId;

    public EMIManager(List<EmiRecipeCategory> categories, Map<EmiRecipeCategory, List<EmiIngredient>> workstations, List<EmiRecipe> recipes) {
        recipes.addAll(GTORecipes.EMI_RECIPES);
        this.categories = categories;
        this.workstations = workstations;
        this.recipes = recipes;
        byCategory = new Object2ObjectOpenHashMap<>(categories.size());
        byId = new Object2ObjectOpenHashMap<>(recipes.size());

        Map<EmiStack, Set<EmiRecipe>> byOutput = new Object2ObjectOpenCustomHashMap<>(
                new EmiStackList.ComparisonHashStrategy());

        for (EmiRecipe recipe : recipes) {
            ResourceLocation id = recipe.getId();
            EmiRecipeCategory category = recipe.getCategory();
            byCategory.computeIfAbsent(category, a -> Lists.newArrayList()).add(recipe);
            if (id != null) {
                byId.put(id, recipe);
            }

            for (EmiIngredient input : recipe.getInputs()) {
                for (EmiStack stack : input.getEmiStacks()) {
                    byInput.computeIfAbsent(stack, b -> Lists.newArrayList()).add(recipe);
                }
            }

            for (EmiIngredient catalyst : recipe.getCatalysts()) {
                for (EmiStack stack : catalyst.getEmiStacks()) {
                    byInput.computeIfAbsent(stack, b -> Lists.newArrayList()).add(recipe);
                }
            }

            for (EmiStack output : recipe.getOutputs()) {
                byOutput.computeIfAbsent(output, b -> Sets.newLinkedHashSet()).add(recipe);
            }
        }

        this.byOutput.putAll(byOutput
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Lists.newArrayList(entry.getValue()))));

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

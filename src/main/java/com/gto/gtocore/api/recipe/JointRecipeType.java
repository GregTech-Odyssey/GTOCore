package com.gto.gtocore.api.recipe;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

@Getter
public final class JointRecipeType extends GTORecipeType {

    private final GTRecipeType[] types;

    public static GTORecipeType register(String name, GTRecipeType... types) {
        GTORecipeType recipeType = new JointRecipeType(name, types);
        GTRegistries.RECIPE_TYPES.register(recipeType.registryName, recipeType);
        return recipeType;
    }

    private JointRecipeType(String name, GTRecipeType... types) {
        super(GTCEu.id(name), GTRecipeTypes.DUMMY);
        this.types = types;
        setXEIVisible(false);
        setRecipeUI(null);
        setRecipeBuilder(null);
    }

    @Override
    public Iterator<GTRecipe> searchRecipe(IRecipeCapabilityHolder holder) {
        for (GTRecipeType type : types) {
            Iterator<GTRecipe> result = type.searchRecipe(holder);
            if (result != null && result.hasNext()) {
                return result;
            }
        }
        return null;
    }

    @Override
    public Set<GTRecipeCategory> getCategories() {
        Set<GTRecipeCategory> categories = new ObjectOpenHashSet<>();
        for (GTRecipeType type : types) {
            categories.addAll(type.getCategories());
        }
        return Collections.unmodifiableSet(categories);
    }
}

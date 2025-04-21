package com.gto.gtocore.api.recipe;

import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

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
    public @NotNull Iterator<GTRecipe> searchRecipe(IRecipeCapabilityHolder holder, Predicate<GTRecipe> canHandle) {
        if (holder instanceof IRecipeLogicMachine recipeLogicMachine && recipeLogicMachine.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
            enhancedRecipeLogic.gTOCore$setIdleReason(IdleReason.NO_MATCH.reason());
        }
        if (!holder.hasCapabilityProxies()) return Collections.emptyIterator();
        return new JointSearchRecipeIterator(holder, this, canHandle);
    }

    @Override
    public Set<GTRecipeCategory> getCategories() {
        Set<GTRecipeCategory> categories = new ObjectOpenHashSet<>(types.length);
        for (GTRecipeType type : types) {
            categories.addAll(type.getCategories());
        }
        return Collections.unmodifiableSet(categories);
    }

    private static class JointSearchRecipeIterator extends SearchRecipeIterator {

        private final RecipeIterator[] recipeIterators;

        private JointSearchRecipeIterator(IRecipeCapabilityHolder holder, JointRecipeType recipeType, Predicate<GTRecipe> canHandle) {
            super(holder, recipeType, canHandle);
            recipeIterators = new RecipeIterator[recipeType.types.length];
            for (int i = 0; i < recipeType.types.length; i++) {
                recipeIterators[i] = new RecipeIterator(recipeType.types[i], ingredients, canHandle);
            }
        }

        @Override
        RecipeIterator createRecipeIterator() {
            return null;
        }

        @Override
        public GTRecipe next() {
            for (RecipeIterator recipeIterator : recipeIterators) {
                while (recipeIterator.hasNext()) {
                    GTRecipe recipe = recipeIterator.next();
                    if (recipe == null) continue;
                    return recipe;
                }
            }
            for (GTRecipeType type : ((JointRecipeType) recipeType).types) {
                for (GTRecipeType.ICustomRecipeLogic logic : type.getCustomRecipeLogicRunners()) {
                    GTRecipe recipe = logic.createCustomRecipe(holder);
                    if (recipe != null) return recipe;
                }
            }
            hasNext = false;
            return null;
        }
    }
}

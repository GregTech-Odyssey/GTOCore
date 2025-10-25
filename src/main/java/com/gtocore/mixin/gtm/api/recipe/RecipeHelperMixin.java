package com.gtocore.mixin.gtm.api.recipe;

import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.Map;

@Mixin(RecipeHelper.class)
public final class RecipeHelperMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static int getRecipeEUtTier(GTRecipe recipe) {
        return Recipe.of(recipe).tier;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static int getPreOCRecipeEuTier(GTRecipe recipe) {
        return Recipe.of(recipe).tier;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static boolean handleRecipe(IRecipeCapabilityHolder holder, GTRecipe recipe, IO io, Map<RecipeCapability<?>, List<Content>> contents, Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches, boolean simulated) {
        return RecipeRunner.handleRecipe(holder, (Recipe) recipe, io, contents, chanceCaches, simulated);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static boolean matchRecipe(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        return RecipeRunner.matchRecipe(holder, (Recipe) recipe);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static boolean matchTickRecipe(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        return RecipeRunner.matchTickRecipe(holder, (Recipe) recipe);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static boolean handleRecipeIO(IRecipeCapabilityHolder holder, GTRecipe recipe, IO io, Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches) {
        return RecipeRunner.handleRecipeIO(holder, (Recipe) recipe, io, chanceCaches);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static boolean handleTickRecipeIO(IRecipeCapabilityHolder holder, GTRecipe recipe, IO io, Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * @author .
     * @reason .
     */
    @Contract(pure = true)
    @Overwrite(remap = false)
    public static GTRecipe trimRecipeOutputs(GTRecipe recipe, Reference2IntOpenHashMap<RecipeCapability<?>> trimLimits) {
        if (trimLimits.isEmpty()) return recipe;
        var output = RecipeHelper.doTrim(recipe.outputs, trimLimits);
        recipe.outputs.clear();
        recipe.outputs.putAll(output);
        return recipe;
    }
}

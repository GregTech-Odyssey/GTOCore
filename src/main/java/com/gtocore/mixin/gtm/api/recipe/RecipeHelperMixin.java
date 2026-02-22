package com.gtocore.mixin.gtm.api.recipe;

import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Map;

@Mixin(RecipeHelper.class)
public final class RecipeHelperMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static int getRecipeEUtTier(GTRecipe recipe) {
        return recipe.tier;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static int getPreOCRecipeEuTier(GTRecipe recipe) {
        return recipe.tier;
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
}

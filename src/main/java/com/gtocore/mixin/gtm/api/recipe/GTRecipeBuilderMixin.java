package com.gtocore.mixin.gtm.api.recipe;

import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GTRecipeBuilder.class)
public class GTRecipeBuilderMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static GTRecipeBuilder of(ResourceLocation id, GTRecipeType recipeType) {
        return RecipeBuilder.of(id, recipeType);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static GTRecipeBuilder ofRaw() {
        return RecipeBuilder.ofRaw();
    }
}

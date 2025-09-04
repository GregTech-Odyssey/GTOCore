package com.gtocore.mixin.gtm.api.recipe;

import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeCapabilityMap;

import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(GTRecipeBuilder.class)
public class GTRecipeBuilderMixin {

    @Shadow(remap = false)
    public Map<RecipeCapability<?>, List<Content>> input;

    @Shadow(remap = false)
    public Map<RecipeCapability<?>, List<Content>> output;

    @Inject(method = "<init>(Lnet/minecraft/resources/ResourceLocation;Lcom/gregtechceu/gtceu/api/recipe/GTRecipeType;)V", at = @At("TAIL"), remap = false)
    private void init(ResourceLocation id, GTRecipeType recipeType, CallbackInfo ci) {
        input = new RecipeCapabilityMap<>();
        output = new RecipeCapabilityMap<>();
    }

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

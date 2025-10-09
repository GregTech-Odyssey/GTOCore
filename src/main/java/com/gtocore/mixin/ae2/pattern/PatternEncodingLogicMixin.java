package com.gtocore.mixin.ae2.pattern;

import com.gtocore.api.ae2.pattern.IEncodingLogic;

import com.gtolib.api.ae2.pattern.IDetails;

import appeng.crafting.pattern.AEProcessingPattern;
import appeng.parts.encoding.PatternEncodingLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatternEncodingLogic.class)
public class PatternEncodingLogicMixin implements IEncodingLogic {

    @Unique
    public String gtocore$recipe = "";
    @Unique
    public String gtocore$recipeType = "";

    @Inject(method = "loadProcessingPattern", at = @At("RETURN"), remap = false)
    private void loadProcessingPattern(AEProcessingPattern pattern, CallbackInfo ci) {
        if (((IDetails) pattern).getRecipe() != null) {
            gtocore$recipe = ((IDetails) pattern).getRecipe().id.toString();
        } else {
            gtocore$recipe = "";
        }
        if (((IDetails) pattern).getRecipeType() != null) {
            gtocore$recipeType = ((IDetails) pattern).getRecipeType().registryName.toString();
        } else {
            gtocore$recipeType = "";
        }
    }

    @Override
    public String gtocore$getRecipe() {
        return gtocore$recipe;
    }

    @Override
    public String gtocore$getRecipeType() {
        return gtocore$recipeType;
    }

    @Override
    public void gtocore$setRecipe(String recipe) {
        this.gtocore$recipe = recipe;
    }

    @Override
    public void gtocore$setRecipeType(String recipeType) {
        this.gtocore$recipeType = recipeType;
    }

    @Override
    public void gtocore$clearExtraRecipeInfo() {
        this.gtocore$recipeType = "";
        this.gtocore$recipe = "";
    }
}

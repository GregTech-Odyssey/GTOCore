package com.gto.gtocore.mixin.gtm.api.recipe;

import com.gto.gtocore.api.recipe.IGTRecipe;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;

@Mixin(GTRecipe.class)
public class GTRecipeMixin implements IGTRecipe {

    @Shadow(remap = false)
    public int parallels;

    @Unique
    private boolean gtocore$perfect;

    @Unique
    private IntSupplier gtocore$tierSupplier;

    @Unique
    private int gtocore$tier;

    @Inject(method = "<init>(Lcom/gregtechceu/gtceu/api/recipe/GTRecipeType;Lnet/minecraft/resources/ResourceLocation;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/List;Ljava/util/List;Lnet/minecraft/nbt/CompoundTag;ILcom/gregtechceu/gtceu/api/recipe/category/GTRecipeCategory;)V", at = @At("TAIL"), remap = false)
    public void gtocore$init(GTRecipeType recipeType, ResourceLocation id, Map<RecipeCapability<?>, List<Content>> inputs, Map<RecipeCapability<?>, List<Content>> outputs, Map<RecipeCapability<?>, List<Content>> tickInputs, Map<RecipeCapability<?>, List<Content>> tickOutputs, Map inputChanceLogics, Map outputChanceLogics, Map tickInputChanceLogics, Map tickOutputChanceLogics, List conditions, List ingredientActions, CompoundTag data, int duration, GTRecipeCategory recipeCategory, CallbackInfo ci) {
        gtocore$tier = -1;
        gtocore$tierSupplier = () -> {
            long EUt = 0;
            var inputlist = tickInputs.get(EURecipeCapability.CAP);
            if (inputlist != null) {
                for (Content content : inputlist) {
                    EUt += EURecipeCapability.CAP.of(content.getContent());
                }
            }
            if (EUt == 0) {
                var outputlist = tickOutputs.get(EURecipeCapability.CAP);
                if (outputlist != null) {
                    for (Content content : outputlist) {
                        EUt += EURecipeCapability.CAP.of(content.getContent());
                    }
                }
            }
            if (parallels > 1) EUt /= parallels;
            return GTUtil.getTierByVoltage(EUt);
        };
    }

    @Inject(method = "copy(Lcom/gregtechceu/gtceu/api/recipe/content/ContentModifier;Z)Lcom/gregtechceu/gtceu/api/recipe/GTRecipe;", at = @At("RETURN"), remap = false)
    public void gtocore$copy(ContentModifier modifier, boolean modifyDuration, CallbackInfoReturnable<GTRecipe> cir) {
        if (gtocore$tierSupplier == null && cir.getReturnValue() instanceof IGTRecipe igtRecipe) {
            igtRecipe.gtocore$setTier(gtocore$tier);
            igtRecipe.gtocore$clean();
        }
    }

    @Override
    public boolean gtocore$perfect() {
        return gtocore$perfect;
    }

    @Override
    public void gtocore$setPerfect(boolean perfect) {
        gtocore$perfect = perfect;
    }

    @Override
    public int gtocore$getTier() {
        if (gtocore$tier < 0) {
            gtocore$tier = gtocore$tierSupplier.getAsInt();
            gtocore$tierSupplier = null;
        }
        return gtocore$tier;
    }

    @Override
    public void gtocore$setTier(int tier) {
        gtocore$tier = tier;
    }

    @Override
    public void gtocore$clean() {
        gtocore$tierSupplier = null;
    }
}

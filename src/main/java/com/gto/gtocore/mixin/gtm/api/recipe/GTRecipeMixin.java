package com.gto.gtocore.mixin.gtm.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GTRecipe.class)
public class GTRecipeMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public ChanceLogic getChanceLogicForCapability(RecipeCapability<?> cap, IO io, boolean isTick) {
        return ChanceLogic.OR;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void postWorking(IRecipeCapabilityHolder holder) {}

    @Redirect(method = "handleRecipe", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), remap = false)
    private void handleRecipe(Logger instance, String string, Object object, Object o) {}
}

package com.gto.gtocore.mixin.gtm.api.recipe;

import com.gto.gtocore.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraft.network.chat.Component;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.Map;

@Mixin(RecipeHelper.class)
public class RecipeHelperMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static ActionResult handleRecipe(IRecipeCapabilityHolder holder, GTRecipe recipe, IO io, Map<RecipeCapability<?>, List<Content>> contents, Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches, boolean isTick, boolean simulated) {
        RecipeRunner runner = new RecipeRunner(recipe, io, isTick, holder, chanceCaches, simulated);
        var result = runner.handle(contents);
        if (result.isSuccess() || result.capability() == null) return result.result();
        String key = "gtceu.recipe_logic.insufficient_" + (io == IO.IN ? "in" : "out");
        return ActionResult.fail(Component.translatable(key).append(": ").append(result.capability().getName()));
    }
}

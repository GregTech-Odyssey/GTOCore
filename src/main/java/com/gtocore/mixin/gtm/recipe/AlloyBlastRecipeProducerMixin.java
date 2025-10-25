package com.gtocore.mixin.gtm.recipe;

import com.gtocore.api.data.material.GTOMaterialFlags;
import com.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.data.recipe.misc.alloyblast.AlloyBlastRecipeProducer;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AlloyBlastRecipeProducer.class)
public class AlloyBlastRecipeProducerMixin {

    @Redirect(method = "createBuilder", at = @At(value = "FIELD", target = "Lcom/gregtechceu/gtceu/common/data/GCYMRecipeTypes;ALLOY_BLAST_RECIPES:Lcom/gregtechceu/gtceu/api/recipe/GTRecipeType;", remap = false), remap = false)
    private GTRecipeType redirectAlloyBlastRecipeType(@Local(argsOnly = true) Material material) {
        return material.hasFlag(GTOMaterialFlags.NEED_BLAST_IN_SPACE) ? GTORecipeTypes.SPACE_SMELTING_RECIPES : GTORecipeTypes.ALLOY_BLAST_RECIPES;
    }
}

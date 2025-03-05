package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

interface LavaFurnace {

    static void init() {
        GTORecipeTypes.LAVA_FURNACE_RECIPES.recipeBuilder(GTOCore.id("lava_furnace"))
                .inputItems(new ItemStack(Blocks.COBBLESTONE.asItem()))
                .outputFluids(GTMaterials.Lava.getFluid(1000))
                .EUt(16)
                .duration(200)
                .save();

        GTORecipeTypes.LAVA_FURNACE_RECIPES.recipeBuilder(GTOCore.id("lava_furnace1"))
                .inputItems(new ItemStack(Blocks.ANDESITE.asItem()))
                .outputFluids(GTMaterials.Lava.getFluid(1000))
                .EUt(16)
                .duration(200)
                .save();
    }
}

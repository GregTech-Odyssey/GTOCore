package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import static com.gto.gtocore.common.data.GTORecipeTypes.LAVA_FURNACE_RECIPES;

interface LavaFurnace {

    static void init() {
        LAVA_FURNACE_RECIPES.recipeBuilder(GTOCore.id("lava_furnace"))
                .inputItems(new ItemStack(Blocks.COBBLESTONE.asItem()))
                .outputFluids(GTMaterials.Lava.getFluid(1000))
                .EUt(16)
                .duration(200)
                .save();

        LAVA_FURNACE_RECIPES.recipeBuilder(GTOCore.id("lava_furnace1"))
                .inputItems(new ItemStack(Blocks.ANDESITE.asItem()))
                .outputFluids(GTMaterials.Lava.getFluid(1000))
                .EUt(16)
                .duration(200)
                .save();
    }
}

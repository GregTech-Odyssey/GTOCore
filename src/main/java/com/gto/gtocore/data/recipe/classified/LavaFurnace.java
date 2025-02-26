package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

interface LavaFurnace {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.LAVA_FURNACE_RECIPES.recipeBuilder(GTOCore.id("lava_furnace"))
                .inputItems(new ItemStack(Blocks.COBBLESTONE.asItem()))
                .outputFluids(GTMaterials.Lava.getFluid(1000))
                .EUt(16)
                .duration(200)
                .save(provider);

        GTORecipeTypes.LAVA_FURNACE_RECIPES.recipeBuilder(GTOCore.id("lava_furnace1"))
                .inputItems(new ItemStack(Blocks.ANDESITE.asItem()))
                .outputFluids(GTMaterials.Lava.getFluid(1000))
                .EUt(16)
                .duration(200)
                .save(provider);
    }
}

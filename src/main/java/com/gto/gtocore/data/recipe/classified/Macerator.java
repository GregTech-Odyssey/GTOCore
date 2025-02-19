package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.enderio.base.common.init.EIOItems;

import java.util.function.Consumer;

interface Macerator {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("prescient_powder"))
                .inputItems(EIOItems.PRESCIENT_CRYSTAL.asStack())
                .outputItems(EIOItems.PRESCIENT_POWDER.asStack())
                .EUt(30)
                .duration(200)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("spacetime_dust"))
                .inputItems(TagPrefix.ingot, GTOMaterials.SpaceTime)
                .outputItems(TagPrefix.dust, GTOMaterials.SpaceTime)
                .EUt(2013265920)
                .duration(400)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("essence"))
                .inputItems(GTOBlocks.ESSENCE_BLOCK.asStack())
                .outputItems(GTOItems.ESSENCE.asStack())
                .chancedOutput(GTOItems.ESSENCE.asStack(), 5000, 400)
                .chancedOutput(GTOItems.ESSENCE.asStack(), 5000, 200)
                .chancedOutput(GTOItems.ESSENCE.asStack(), 5000, 100)
                .EUt(30)
                .duration(200)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("silicon_dust"))
                .inputItems(new ItemStack(AEItems.SILICON.asItem()))
                .outputItems(TagPrefix.dust, GTMaterials.Silicon)
                .EUt(16)
                .duration(200)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("vibrant_powder"))
                .inputItems(EIOItems.VIBRANT_CRYSTAL.asStack())
                .outputItems(EIOItems.VIBRANT_POWDER.asStack())
                .EUt(30)
                .duration(200)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("draconium_dust"))
                .inputItems(GTOBlocks.DRACONIUM_BLOCK_CHARGED.asStack())
                .outputItems(GTOItems.DRACONIUM_DIRT.asStack(9))
                .outputItems(TagPrefix.dust, GTMaterials.Obsidian)
                .outputItems(TagPrefix.dust, GTMaterials.EnderEye)
                .outputItems(TagPrefix.dust, GTMaterials.Redstone)
                .EUt(1920)
                .duration(400)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("sculk_catalyst"))
                .inputItems(new ItemStack(Blocks.SCULK_CATALYST.asItem()))
                .outputItems(TagPrefix.dust, GTMaterials.EchoShard)
                .EUt(7)
                .duration(100)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("sculk_shrieker"))
                .inputItems(new ItemStack(Blocks.SCULK_SHRIEKER.asItem()))
                .outputItems(TagPrefix.dust, GTMaterials.EchoShard)
                .EUt(7)
                .duration(100)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("pulsating_powder"))
                .inputItems(EIOItems.PULSATING_CRYSTAL.asStack())
                .outputItems(EIOItems.PULSATING_POWDER.asStack())
                .EUt(30)
                .duration(200)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("sculk_sensor"))
                .inputItems(new ItemStack(Blocks.SCULK_SENSOR.asItem()))
                .outputItems(TagPrefix.dust, GTMaterials.EchoShard)
                .EUt(7)
                .duration(100)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("ender_crystal_powder"))
                .inputItems(EIOItems.ENDER_CRYSTAL.asStack())
                .outputItems(EIOItems.ENDER_CRYSTAL_POWDER.asStack())
                .EUt(30)
                .duration(200)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("degenerate_rhenium_dust"))
                .inputItems(TagPrefix.plate, GTOMaterials.DegenerateRhenium)
                .outputItems(TagPrefix.dust, GTOMaterials.DegenerateRhenium)
                .EUt(31457280)
                .duration(100)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("prismarine_shard"))
                .inputItems(new ItemStack(Blocks.PRISMARINE.asItem()))
                .outputItems(new ItemStack(Items.PRISMARINE_SHARD.asItem()))
                .EUt(2)
                .duration(200)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("wheat_seeds"))
                .inputItems(new ItemStack(Items.WHEAT.asItem()))
                .outputItems(new ItemStack(Blocks.WHEAT.asItem(), 64))
                .EUt(2)
                .duration(200)
                .save(provider);

        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder(GTOCore.id("sky_dust"))
                .inputItems(new ItemStack(AEBlocks.SKY_STONE_BLOCK.block().asItem()))
                .outputItems(new ItemStack(AEItems.SKY_DUST.asItem()))
                .EUt(2)
                .duration(200)
                .save(provider);
    }
}

package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import static com.gto.gtocore.common.data.GTORecipeTypes.GRAVITATION_SHOCKBURST_RECIPES;

interface GravitationShockburst {

    static void init() {
        GRAVITATION_SHOCKBURST_RECIPES.recipeBuilder(GTOCore.id("command_block_broken"))
                .inputItems(new ItemStack(Blocks.COMMAND_BLOCK.asItem()))
                .inputItems(TagPrefix.dust, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .outputItems(GTOBlocks.COMMAND_BLOCK_BROKEN.asStack())
                .EUt(131941395333120L)
                .duration(20)
                .save();

        GRAVITATION_SHOCKBURST_RECIPES.recipeBuilder(GTOCore.id("repeating_command_block_core"))
                .inputItems(GTOItems.CHAIN_COMMAND_BLOCK_CORE.asStack())
                .inputItems(new ItemStack(Blocks.CALIBRATED_SCULK_SENSOR.asItem(), 64))
                .outputItems(GTOItems.REPEATING_COMMAND_BLOCK_CORE.asStack())
                .EUt(131941395333120L)
                .duration(20)
                .save();

        GRAVITATION_SHOCKBURST_RECIPES.recipeBuilder(GTOCore.id("chain_command_block_core"))
                .inputItems(GTOItems.COMMAND_BLOCK_CORE.asStack())
                .inputItems(new ItemStack(Blocks.OBSERVER.asItem(), 64))
                .outputItems(GTOItems.CHAIN_COMMAND_BLOCK_CORE.asStack())
                .EUt(131941395333120L)
                .duration(20)
                .save();

        GRAVITATION_SHOCKBURST_RECIPES.recipeBuilder(GTOCore.id("chain_command_block_broken"))
                .inputItems(new ItemStack(Blocks.CHAIN_COMMAND_BLOCK.asItem()))
                .inputItems(TagPrefix.dust, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .outputItems(GTOBlocks.CHAIN_COMMAND_BLOCK_BROKEN.asStack())
                .EUt(131941395333120L)
                .duration(20)
                .save();
    }
}

package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.world.level.block.Blocks;

import static com.gtocore.common.data.GTORecipeTypes.GRAVITATION_SHOCKBURST_RECIPES;

final class GravitationShockburst {

    public static void init() {
        GRAVITATION_SHOCKBURST_RECIPES.recipeBuilder("command_block_broken")
                .inputItems(Blocks.COMMAND_BLOCK.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .outputItems(GTOBlocks.COMMAND_BLOCK_BROKEN.asItem())
                .EUt(131941395333120L)
                .duration(20)
                .save();

        GRAVITATION_SHOCKBURST_RECIPES.recipeBuilder("repeating_command_block_core")
                .inputItems(GTOItems.CHAIN_COMMAND_BLOCK_CORE.asItem())
                .inputItems(Blocks.CALIBRATED_SCULK_SENSOR.asItem(), 64)
                .outputItems(GTOItems.REPEATING_COMMAND_BLOCK_CORE.asItem())
                .EUt(131941395333120L)
                .duration(20)
                .save();

        GRAVITATION_SHOCKBURST_RECIPES.recipeBuilder("chain_command_block_core")
                .inputItems(GTOItems.COMMAND_BLOCK_CORE.asItem())
                .inputItems(Blocks.OBSERVER.asItem(), 64)
                .outputItems(GTOItems.CHAIN_COMMAND_BLOCK_CORE.asItem())
                .EUt(131941395333120L)
                .duration(20)
                .save();

        GRAVITATION_SHOCKBURST_RECIPES.recipeBuilder("chain_command_block_broken")
                .inputItems(Blocks.CHAIN_COMMAND_BLOCK.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .outputItems(GTOBlocks.CHAIN_COMMAND_BLOCK_BROKEN.asItem())
                .EUt(131941395333120L)
                .duration(20)
                .save();
    }
}

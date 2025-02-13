package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.init.GTOBlocks;
import com.gto.gtocore.init.GTOItems;
import com.gto.gtocore.init.GTOMaterials;
import com.gto.gtocore.init.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

interface GravitationShockburst {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.GRAVITATION_SHOCKBURST_RECIPES.recipeBuilder(GTOCore.id("command_block_broken"))
                .inputItems(new ItemStack(Blocks.COMMAND_BLOCK.asItem()))
                .inputItems(TagPrefix.dust, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .outputItems(GTOBlocks.COMMAND_BLOCK_BROKEN.asStack())
                .EUt(131941395333120L)
                .duration(20)
                .save(provider);

        GTORecipeTypes.GRAVITATION_SHOCKBURST_RECIPES.recipeBuilder(GTOCore.id("repeating_command_block_core"))
                .inputItems(GTOItems.CHAIN_COMMAND_BLOCK_CORE.asStack())
                .inputItems(new ItemStack(Blocks.CALIBRATED_SCULK_SENSOR.asItem(), 64))
                .outputItems(GTOItems.REPEATING_COMMAND_BLOCK_CORE.asStack())
                .EUt(131941395333120L)
                .duration(20)
                .save(provider);

        GTORecipeTypes.GRAVITATION_SHOCKBURST_RECIPES.recipeBuilder(GTOCore.id("chain_command_block_core"))
                .inputItems(GTOItems.COMMAND_BLOCK_CORE.asStack())
                .inputItems(new ItemStack(Blocks.OBSERVER.asItem(), 64))
                .outputItems(GTOItems.CHAIN_COMMAND_BLOCK_CORE.asStack())
                .EUt(131941395333120L)
                .duration(20)
                .save(provider);

        GTORecipeTypes.GRAVITATION_SHOCKBURST_RECIPES.recipeBuilder(GTOCore.id("chain_command_block_broken"))
                .inputItems(new ItemStack(Blocks.CHAIN_COMMAND_BLOCK.asItem()))
                .inputItems(TagPrefix.dust, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .outputItems(GTOBlocks.CHAIN_COMMAND_BLOCK_BROKEN.asStack())
                .EUt(131941395333120L)
                .duration(20)
                .save(provider);
    }
}

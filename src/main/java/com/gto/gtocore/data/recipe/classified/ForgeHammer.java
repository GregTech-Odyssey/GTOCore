package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import static com.gto.gtocore.common.data.GTORecipeTypes.FORGE_HAMMER_RECIPES;

interface ForgeHammer {

    static void init() {
        FORGE_HAMMER_RECIPES.recipeBuilder(GTOCore.id("long_netherite_rod"))
                .inputItems(GTOItems.NETHERITE_ROD.asStack(2))
                .outputItems(GTOItems.LONG_NETHERITE_ROD.asStack())
                .EUt(30)
                .duration(200)
                .save();

        FORGE_HAMMER_RECIPES.recipeBuilder(GTOCore.id("prismarine_crystals"))
                .inputItems(new ItemStack(Items.PRISMARINE_SHARD.asItem()))
                .outputItems(new ItemStack(Items.PRISMARINE_CRYSTALS.asItem()))
                .EUt(16)
                .duration(20)
                .save();

        FORGE_HAMMER_RECIPES.recipeBuilder(GTOCore.id("magnetic_long_netherite_rod"))
                .inputItems(GTOItems.MAGNETIC_NETHERITE_ROD.asStack(2))
                .outputItems(GTOItems.MAGNETIC_LONG_NETHERITE_ROD.asStack())
                .EUt(30)
                .duration(200)
                .save();

        FORGE_HAMMER_RECIPES.recipeBuilder(GTOCore.id("long_magmatter_rod"))
                .inputItems(TagPrefix.rod, GTOMaterials.Magmatter, 2)
                .outputItems(TagPrefix.rodLong, GTOMaterials.Magmatter)
                .EUt(2013265920)
                .duration(300)
                .save();

        FORGE_HAMMER_RECIPES.recipeBuilder(GTOCore.id("special_ceramics_dust"))
                .inputItems(new ItemStack(Blocks.BROWN_GLAZED_TERRACOTTA.asItem()))
                .outputItems(TagPrefix.dust, GTOMaterials.SpecialCeramics)
                .EUt(7680)
                .duration(20)
                .save();
    }
}

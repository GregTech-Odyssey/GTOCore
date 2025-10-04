package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;

import static com.gtocore.common.data.GTORecipeTypes.FORGE_HAMMER_RECIPES;

final class ForgeHammer {

    public static void init() {
        FORGE_HAMMER_RECIPES.recipeBuilder("prismarine_crystals")
                .inputItems(Items.PRISMARINE_SHARD.asItem())
                .outputItems(Items.PRISMARINE_CRYSTALS.asItem())
                .EUt(16)
                .duration(20)
                .save();

        FORGE_HAMMER_RECIPES.recipeBuilder("long_magmatter_rod")
                .inputItems(TagPrefix.rod, GTOMaterials.Magmatter, 2)
                .outputItems(TagPrefix.rodLong, GTOMaterials.Magmatter)
                .EUt(2013265920)
                .duration(300)
                .save();

        FORGE_HAMMER_RECIPES.recipeBuilder("special_ceramics_dust")
                .inputItems(TagPrefix.block, GTOMaterials.TitaniumNitrideCeramic)
                .outputItems(TagPrefix.dust, GTOMaterials.SpecialCeramics, 4)
                .EUt(7680)
                .duration(400)
                .save();

        FORGE_HAMMER_RECIPES.recipeBuilder("wrought_iron")
                .inputItems(GTOItems.HOT_IRON_INGOT.asItem())
                .outputItems(TagPrefix.ingot, GTMaterials.WroughtIron)
                .EUt(16)
                .duration(200)
                .save();

        FORGE_HAMMER_RECIPES.recipeBuilder("diamond_lattice")
                .inputItems("avaritia:diamond_lattice_block")
                .outputItems("avaritia:diamond_lattice", 9)
                .EUt(1920)
                .duration(200)
                .save();
    }
}

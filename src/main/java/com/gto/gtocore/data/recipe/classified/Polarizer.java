package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;

import static com.gto.gtocore.common.data.GTORecipeTypes.POLARIZER_RECIPES;

interface Polarizer {

    static void init() {
        POLARIZER_RECIPES.recipeBuilder(GTOCore.id("attuned_tengam_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.PurifiedTengam)
                .outputItems(TagPrefix.dust, GTOMaterials.AttunedTengam)
                .EUt(125829120)
                .duration(400)
                .save();

        POLARIZER_RECIPES.recipeBuilder(GTOCore.id("small_attuned_tengam_dust"))
                .inputItems(TagPrefix.dustSmall, GTOMaterials.PurifiedTengam)
                .outputItems(TagPrefix.dustSmall, GTOMaterials.AttunedTengam)
                .EUt(31457280)
                .duration(400)
                .save();

        POLARIZER_RECIPES.recipeBuilder(GTOCore.id("triplet_neutronium_sphere"))
                .inputItems(GTOItems.NEUTRONIUM_SPHERE.asItem())
                .outputItems(GTOItems.TRIPLET_NEUTRONIUM_SPHERE.asItem())
                .EUt(5000000)
                .duration(200)
                .save();

        POLARIZER_RECIPES.recipeBuilder(GTOCore.id("energetic_netherite"))
                .inputItems(Items.NETHERITE_BLOCK.asItem())
                .outputItems(TagPrefix.dust, GTOMaterials.EnergeticNetherite)
                .EUt(2097152)
                .duration(200)
                .save();

        POLARIZER_RECIPES.recipeBuilder(GTOCore.id("energetic_netherite_a"))
                .inputItems(TagPrefix.ingot, GTMaterials.Netherite)
                .outputItems(TagPrefix.dust, GTOMaterials.EnergeticNetherite)
                .EUt(33554432)
                .duration(200)
                .save();

        POLARIZER_RECIPES.recipeBuilder(GTOCore.id("energetic_netherite_b"))
                .inputItems(TagPrefix.dust, GTMaterials.Netherite)
                .outputItems(TagPrefix.dust, GTOMaterials.EnergeticNetherite)
                .EUt(134217728)
                .duration(20)
                .save();
    }
}

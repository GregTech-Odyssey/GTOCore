package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.level.block.Blocks;

import com.enderio.base.common.init.EIOItems;

import static com.gtocore.common.data.GTORecipeTypes.MOLECULAR_TRANSFORMER_RECIPES;

final class MolecularTransformer {

    public static void init() {
        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("diamond")
                .inputItems(TagPrefix.gem, GTMaterials.Coal, 4)
                .outputItems(TagPrefix.gem, GTMaterials.Diamond)
                .EUt(7680)
                .duration(120)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("damascus_steel_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Steel)
                .outputItems(TagPrefix.dust, GTMaterials.DamascusSteel)
                .EUt(480)
                .duration(20)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("wrought_iron_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Iron)
                .outputItems(TagPrefix.dust, GTMaterials.WroughtIron)
                .EUt(120)
                .duration(20)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("bone")
                .inputItems(TagPrefix.dust, GTMaterials.Bone, 6)
                .outputItems(TagPrefix.rod, GTMaterials.Bone)
                .EUt(30)
                .duration(40)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("nether_star")
                .inputItems(Blocks.WITHER_SKELETON_SKULL.asItem(), 2)
                .outputItems(TagPrefix.gem, GTMaterials.NetherStar)
                .EUt(1920)
                .duration(18000)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("small_sunnarium_dust")
                .inputItems(EIOItems.CLAYED_GLOWSTONE.asItem())
                .outputItems(TagPrefix.dustSmall, GTOMaterials.Sunnarium)
                .EUt(524288)
                .duration(1600)
                .daytime()
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("steel_dust")
                .inputItems(TagPrefix.dust, GTMaterials.WroughtIron)
                .outputItems(TagPrefix.dust, GTMaterials.Steel)
                .EUt(480)
                .duration(80)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("coal")
                .inputItems(TagPrefix.gem, GTMaterials.Charcoal)
                .outputItems(TagPrefix.gem, GTMaterials.Coal)
                .EUt(120)
                .duration(200)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("quartzite_certus_quartz_crystal")
                .inputItems(TagPrefix.gem, GTMaterials.Quartzite)
                .outputItems(TagPrefix.gem, GTMaterials.CertusQuartz)
                .EUt(120)
                .duration(160)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("powdered_coal")
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 2)
                .outputItems(TagPrefix.dust, GTMaterials.Coal)
                .EUt(120)
                .duration(200)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("activated_carbon_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Coal)
                .outputItems(TagPrefix.dust, GTMaterials.ActivatedCarbon)
                .EUt(122880)
                .duration(20)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("energy_crystal")
                .inputItems(TagPrefix.gemExquisite, GTMaterials.Ruby)
                .outputItems(GTItems.ENERGIUM_CRYSTAL.asItem(), 2)
                .EUt(524288)
                .duration(200)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("quartz_certus_quartz_crystal")
                .inputItems(TagPrefix.gem, GTMaterials.NetherQuartz)
                .outputItems(TagPrefix.gem, GTMaterials.CertusQuartz)
                .EUt(120)
                .duration(160)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("lapotron_gem")
                .inputItems(TagPrefix.gemExquisite, GTMaterials.Sodalite)
                .outputItems(TagPrefix.gem, GTMaterials.Lapotron)
                .EUt(524288)
                .duration(800)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("small_bone_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Calcium)
                .outputItems(TagPrefix.dust, GTMaterials.Bone)
                .EUt(30)
                .duration(80)
                .save();

        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("graphene")
                .inputItems(TagPrefix.dust, GTMaterials.Graphite)
                .outputItems(TagPrefix.dust, GTMaterials.Graphene)
                .EUt(524288)
                .duration(180)
                .save();
        MOLECULAR_TRANSFORMER_RECIPES.recipeBuilder("etrium")
                .inputItems(TagPrefix.dust, GTOMaterials.EnrichedLightEmittingChargedSuspiciousWaste)
                .outputItems(TagPrefix.dust, GTOMaterials.Etrium)
                .EUt(524288)
                .duration(180)
                .save();
    }
}

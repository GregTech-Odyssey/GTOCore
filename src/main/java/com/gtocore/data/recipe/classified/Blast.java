package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.BLAST_RECIPES;

final class Blast {

    public static void init() {
        BLAST_RECIPES.builder("molten_stainless_steel")
                .inputItems(TagPrefix.dust, GTMaterials.StainlessSteel, 8)
                .inputFluids(GTMaterials.StainlessSteel, 1152)
                .outputFluids(GTMaterials.StainlessSteel.getFluid(FluidStorageKeys.MOLTEN, 2304))
                .EUt(120)
                .duration(480)
                .blastFurnaceTemp(2428)
                .circuitMeta(3)
                .save();

        BLAST_RECIPES.builder("molten_manganese_phosphide")
                .inputItems(TagPrefix.dust, GTMaterials.ManganesePhosphide, 8)
                .inputFluids(GTMaterials.ManganesePhosphide, 1152)
                .outputFluids(GTMaterials.ManganesePhosphide.getFluid(FluidStorageKeys.MOLTEN, 2304))
                .EUt(120)
                .duration(400)
                .blastFurnaceTemp(1200)
                .circuitMeta(3)
                .save();

        BLAST_RECIPES.recipeBuilder("alumina_ceramic")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.AluminaCeramic)
                .outputItems(TagPrefix.block, GTOMaterials.AluminaCeramic)
                .inputFluids(GTMaterials.Nitrogen.getFluid(500))
                .EUt(120)
                .duration(600)
                .blastFurnaceTemp(2700)
                .save();

        BLAST_RECIPES.recipeBuilder("hot_draconium_ingot_1")
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .inputFluids(GTMaterials.CetaneBoostedDiesel.getFluid(2000))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Draconium)
                .EUt(125829120)
                .duration(800)
                .blastFurnaceTemp(21600)
                .save();

        BLAST_RECIPES.recipeBuilder("hot_draconium_ingot_3")
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .inputFluids(GTMaterials.HighOctaneGasoline.getFluid(500))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Draconium)
                .EUt(125829120)
                .duration(200)
                .blastFurnaceTemp(21600)
                .save();

        BLAST_RECIPES.recipeBuilder("hot_draconium_ingot_2")
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .inputFluids(GTMaterials.Gasoline.getFluid(1000))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Draconium)
                .EUt(125829120)
                .duration(400)
                .blastFurnaceTemp(21600)
                .save();

        BLAST_RECIPES.recipeBuilder("rutherfordium_neutronium_boule")
                .inputItems(GTItems.NEUTRONIUM_BOULE.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Rutherfordium, 4)
                .inputFluids(GTMaterials.Radon.getFluid(8000))
                .outputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_BOULE.asItem())
                .EUt(30720)
                .duration(21000)
                .blastFurnaceTemp(8100)
                .save();

        BLAST_RECIPES.recipeBuilder("ostrum_ingot")
                .inputItems(TagPrefix.dust, GTOMaterials.Ostrum)
                .inputItems(TagPrefix.dustTiny, GTMaterials.TitaniumCarbide)
                .inputFluids(GTMaterials.SamariumIronArsenicOxide.getFluid(144))
                .outputItems(TagPrefix.ingot, GTOMaterials.Ostrum)
                .EUt(1920)
                .duration(1200)
                .blastFurnaceTemp(5200)
                .save();

        BLAST_RECIPES.recipeBuilder("shining_obsidian")
                .inputItems(TagPrefix.rock, GTMaterials.Obsidian)
                .inputItems(TagPrefix.dust, GTOMaterials.VibrantAlloy)
                .inputFluids(GTMaterials.Glowstone.getFluid(576))
                .outputItems(GTOBlocks.SHINING_OBSIDIAN.asItem())
                .EUt(480)
                .duration(600)
                .blastFurnaceTemp(2600)
                .save();

        BLAST_RECIPES.recipeBuilder("calorite_ingot")
                .inputItems(TagPrefix.dust, GTOMaterials.Calorite)
                .inputItems(TagPrefix.dustTiny, GTMaterials.NaquadahEnriched)
                .inputFluids(GTMaterials.Naquadah.getFluid(144))
                .outputItems(TagPrefix.ingot, GTOMaterials.Calorite)
                .EUt(1920)
                .duration(2400)
                .blastFurnaceTemp(6100)
                .save();

        BLAST_RECIPES.recipeBuilder("giga_chad")
                .inputItems(GTItems.FIELD_GENERATOR_UIV.asItem(), 64)
                .inputItems(GTItems.FIELD_GENERATOR_UXV.asItem(), 64)
                .inputItems(GTItems.FIELD_GENERATOR_OpV.asItem(), 64)
                .inputFluids(GTOMaterials.ExcitedDtec.getFluid(10000000))
                .outputItems(GTOItems.GIGA_CHAD.asItem())
                .EUt(2013265920)
                .duration(4000)
                .blastFurnaceTemp(36000)
                .save();

        BLAST_RECIPES.recipeBuilder("desh_ingot")
                .inputItems(TagPrefix.dust, GTOMaterials.Desh)
                .inputItems(TagPrefix.dustTiny, GTMaterials.Rhodium)
                .inputFluids(GTMaterials.BismuthBronze.getFluid(144))
                .outputItems(TagPrefix.ingot, GTOMaterials.Desh)
                .EUt(480)
                .duration(1600)
                .blastFurnaceTemp(4300)
                .save();

        BLAST_RECIPES.recipeBuilder("bedrock_smoke")
                .inputItems(TagPrefix.dust, GTOMaterials.Bedrockium)
                .inputFluids(GTMaterials.Xenon.getFluid(100))
                .outputFluids(GTOMaterials.BedrockSmoke.getFluid(1000))
                .EUt(7864320)
                .duration(400)
                .blastFurnaceTemp(16200)
                .save();

        BLAST_RECIPES.builder("small_trinium_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.TriniumCompound, 12)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Trinium, 12)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Ash, 12)
                .inputFluids(GTMaterials.Trinium, 288)
                .EUt(122880)
                .blastFurnaceTemp(9111)
                .duration(240)
                .save();
    }
}

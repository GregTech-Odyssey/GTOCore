package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static com.gto.gtocore.common.data.GTORecipeTypes.BLAST_RECIPES;

interface Blast {

    static void init() {
        BLAST_RECIPES.recipeBuilder(GTOCore.id("alumina_ceramic"))
                .inputItems(GTOTagPrefix.roughBlank, GTOMaterials.AluminaCeramic)
                .outputItems(TagPrefix.block, GTOMaterials.AluminaCeramic)
                .inputFluids(GTMaterials.Nitrogen.getFluid(500))
                .EUt(120)
                .duration(600)
                .blastFurnaceTemp(2700)
                .save();

        BLAST_RECIPES.recipeBuilder(GTOCore.id("hot_draconium_ingot_1"))
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .inputFluids(GTMaterials.CetaneBoostedDiesel.getFluid(2000))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Draconium)
                .EUt(125829120)
                .duration(800)
                .blastFurnaceTemp(21600)
                .save();

        BLAST_RECIPES.recipeBuilder(GTOCore.id("hot_draconium_ingot_3"))
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .inputFluids(GTMaterials.HighOctaneGasoline.getFluid(500))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Draconium)
                .EUt(125829120)
                .duration(200)
                .blastFurnaceTemp(21600)
                .save();

        BLAST_RECIPES.recipeBuilder(GTOCore.id("hot_draconium_ingot_2"))
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .inputFluids(GTMaterials.Gasoline.getFluid(1000))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Draconium)
                .EUt(125829120)
                .duration(400)
                .blastFurnaceTemp(21600)
                .save();

        BLAST_RECIPES.recipeBuilder(GTOCore.id("netherite_ingot"))
                .inputItems(new ItemStack(Items.NETHERITE_SCRAP.asItem()))
                .inputItems(TagPrefix.ingot, GTOMaterials.Calorite)
                .inputFluids(GTMaterials.Neon.getFluid(100))
                .outputItems(TagPrefix.ingot, GTMaterials.Netherite)
                .EUt(1920)
                .duration(800)
                .blastFurnaceTemp(6470)
                .save();

        BLAST_RECIPES.recipeBuilder(GTOCore.id("rutherfordium_neutronium_boule"))
                .inputItems(GTItems.NEUTRONIUM_BOULE.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Rutherfordium, 4)
                .inputFluids(GTMaterials.Radon.getFluid(8000))
                .outputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_BOULE.asStack())
                .EUt(30720)
                .duration(21000)
                .blastFurnaceTemp(8100)
                .save();

        BLAST_RECIPES.recipeBuilder(GTOCore.id("ostrum_ingot"))
                .inputItems(TagPrefix.dust, GTOMaterials.Ostrum)
                .inputItems(TagPrefix.dust, GTMaterials.TitaniumCarbide)
                .inputFluids(GTMaterials.SamariumIronArsenicOxide.getFluid(144))
                .outputItems(TagPrefix.ingot, GTOMaterials.Ostrum)
                .EUt(1920)
                .duration(2400)
                .blastFurnaceTemp(5200)
                .save();

        BLAST_RECIPES.recipeBuilder(GTOCore.id("shining_obsidian"))
                .inputItems(TagPrefix.rock, GTMaterials.Obsidian)
                .inputItems(TagPrefix.dust, GTOMaterials.VibrantAlloy)
                .inputFluids(GTMaterials.Glowstone.getFluid(576))
                .outputItems(GTOBlocks.SHINING_OBSIDIAN.asStack())
                .EUt(480)
                .duration(600)
                .blastFurnaceTemp(2600)
                .save();

        BLAST_RECIPES.recipeBuilder(GTOCore.id("calorite_ingot"))
                .inputItems(TagPrefix.ingot, GTMaterials.Naquadah)
                .inputItems(TagPrefix.dust, GTOMaterials.Calorite)
                .outputItems(TagPrefix.ingot, GTOMaterials.Calorite)
                .EUt(1920)
                .duration(3200)
                .blastFurnaceTemp(6100)
                .save();

        BLAST_RECIPES.recipeBuilder(GTOCore.id("giga_chad"))
                .inputItems(GTItems.FIELD_GENERATOR_UIV.asStack(64))
                .inputItems(GTItems.FIELD_GENERATOR_UXV.asStack(64))
                .inputItems(GTItems.FIELD_GENERATOR_OpV.asStack(64))
                .inputFluids(GTOMaterials.ExcitedDtec.getFluid(10000000))
                .outputItems(GTOItems.GIGA_CHAD.asStack())
                .EUt(2013265920)
                .duration(4000)
                .blastFurnaceTemp(36000)
                .save();

        BLAST_RECIPES.recipeBuilder(GTOCore.id("desh_ingot"))
                .inputItems(TagPrefix.dust, GTOMaterials.Desh)
                .inputItems(TagPrefix.dust, GTMaterials.Rhodium)
                .inputFluids(GTMaterials.BismuthBronze.getFluid(144))
                .outputItems(TagPrefix.ingot, GTOMaterials.Desh)
                .EUt(1920)
                .duration(1600)
                .blastFurnaceTemp(4300)
                .save();

        BLAST_RECIPES.recipeBuilder(GTOCore.id("bedrock_smoke"))
                .inputItems(TagPrefix.dust, GTOMaterials.Bedrockium)
                .inputFluids(GTMaterials.Xenon.getFluid(100))
                .outputFluids(GTOMaterials.BedrockSmoke.getFluid(1000))
                .EUt(7864320)
                .duration(400)
                .blastFurnaceTemp(16200)
                .save();
    }
}

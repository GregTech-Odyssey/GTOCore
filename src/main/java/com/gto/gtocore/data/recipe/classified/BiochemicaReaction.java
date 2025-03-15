package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.recipe.condition.GravityCondition;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;

import static com.gto.gtocore.common.data.GTORecipeTypes.BIOCHEMICAL_REACTION_RECIPES;

interface BiochemicaReaction {

    static void init() {
        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("pygro_spawn_egg"))
                .inputItems(Items.PIGLIN_SPAWN_EGG.asItem())
                .inputItems(GTOItems.GLACIO_SPIRIT.asItem())
                .outputItems("ad_astra:pygro_spawn_egg")
                .inputFluids(GTMaterials.Mutagen.getFluid(1000))
                .EUt(7680)
                .duration(1600)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("pygro_brute_spawn_egg"))
                .inputItems(Items.PIGLIN_BRUTE_SPAWN_EGG.asItem())
                .inputItems(GTOItems.GLACIO_SPIRIT.asItem())
                .outputItems("ad_astra:pygro_brute_spawn_egg")
                .inputFluids(GTMaterials.Mutagen.getFluid(1000))
                .EUt(7680)
                .duration(1600)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("mogler_spawn_egg"))
                .inputItems(Items.HOGLIN_SPAWN_EGG.asItem())
                .inputItems(GTOItems.GLACIO_SPIRIT.asItem())
                .outputItems("ad_astra:mogler_spawn_egg")
                .inputFluids(GTMaterials.Mutagen.getFluid(1000))
                .EUt(7680)
                .duration(1600)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("sulfur_creeper_spawn_egg"))
                .inputItems(Items.CREEPER_SPAWN_EGG.asItem())
                .inputItems(GTOItems.GLACIO_SPIRIT.asItem())
                .outputItems("ad_astra:sulfur_creeper_spawn_egg")
                .inputFluids(GTMaterials.Mutagen.getFluid(1000))
                .EUt(7680)
                .duration(1600)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("succinic_anhydride"))
                .inputItems(TagPrefix.dust, GTOMaterials.Succinimide, 12)
                .inputItems(TagPrefix.dust, GTOMaterials.BrevibacteriumFlavium)
                .outputItems(TagPrefix.dust, GTOMaterials.SuccinicAnhydride, 11)
                .outputFluids(GTMaterials.Ammonia.getFluid(1000))
                .EUt(7680)
                .duration(50)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("shewanella_petri_dish"))
                .inputItems(GTOItems.GREEN_ALGAE_FIBER.asItem())
                .inputItems(GTOItems.PREPARATION_PETRI_DISH.asItem())
                .outputItems(GTOItems.SHEWANELLA_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(2400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("brevibacterium_petri_dish"))
                .inputItems("farmersdelight:rich_soil")
                .inputItems(GTOItems.PREPARATION_PETRI_DISH.asItem())
                .outputItems(GTOItems.BREVIBACTERIUM_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(2400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("cupriavidus_petri_dish"))
                .inputItems(Items.COARSE_DIRT.asItem())
                .inputItems(GTOItems.PREPARATION_PETRI_DISH.asItem())
                .outputItems(GTOItems.CUPRIAVIDUS_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(2400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("eschericia_petri_dish"))
                .inputItems("farmersdelight:minced_beef")
                .inputItems(GTOItems.PREPARATION_PETRI_DISH.asItem())
                .outputItems(GTOItems.ESCHERICIA_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(2400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("bifidobacteriumm_petri_dish"))
                .inputItems(GTOItems.PREPARATION_PETRI_DISH.asItem())
                .outputItems(GTOItems.BIFIDOBACTERIUMM_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .inputFluids(GTMaterials.Milk.getFluid(1000))
                .EUt(30720)
                .duration(2400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("streptococcus_petri_dish"))
                .inputItems(Items.ROTTEN_FLESH.asItem())
                .inputItems(GTOItems.PREPARATION_PETRI_DISH.asItem())
                .outputItems(GTOItems.STREPTOCOCCUS_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(2400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("brevibacterium"))
                .inputItems(GTOItems.BREVIBACTERIUM_PETRI_DISH.asItem())
                .outputItems(TagPrefix.dust, GTOMaterials.BrevibacteriumFlavium)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("bifidobacteriumm"))
                .inputItems(GTOItems.BIFIDOBACTERIUMM_PETRI_DISH.asItem())
                .outputItems(TagPrefix.dust, GTOMaterials.BifidobacteriumBreve)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("eschericia"))
                .inputItems(GTOItems.ESCHERICIA_PETRI_DISH.asItem())
                .outputItems(TagPrefix.dust, GTOMaterials.EschericiaColi)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("streptococcus"))
                .inputItems(GTOItems.STREPTOCOCCUS_PETRI_DISH.asItem())
                .outputItems(TagPrefix.dust, GTOMaterials.StreptococcusPyogenes)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("cupriavidus"))
                .inputItems(GTOItems.CUPRIAVIDUS_PETRI_DISH.asItem())
                .outputItems(TagPrefix.dust, GTOMaterials.CupriavidusNecator)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("shewanella"))
                .inputItems(GTOItems.SHEWANELLA_PETRI_DISH.asItem())
                .outputItems(TagPrefix.dust, GTOMaterials.Shewanella)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("linoleic_acid"))
                .inputItems(TagPrefix.dust, GTOMaterials.Yeast, 6)
                .inputFluids(GTMaterials.Biomass.getFluid(1000))
                .outputFluids(GTOMaterials.LinoleicAcid.getFluid(1000))
                .EUt(480)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("chitosan"))
                .inputItems(TagPrefix.dust, GTOMaterials.BifidobacteriumBreve, 8)
                .inputFluids(GTOMaterials.Chitin.getFluid(1000))
                .outputFluids(GTOMaterials.Chitosan.getFluid(1000))
                .EUt(1920)
                .duration(100)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("pluripotency_induction_gene_plasmids"))
                .inputItems(TagPrefix.dust, GTOMaterials.EschericiaColi, 8)
                .inputFluids(GTOMaterials.Cas9Protein.getFluid(1000))
                .inputFluids(GTOMaterials.MycGene.getFluid(1000))
                .inputFluids(GTOMaterials.Oct4Gene.getFluid(1000))
                .inputFluids(GTOMaterials.Sox2Gene.getFluid(1000))
                .inputFluids(GTOMaterials.Kfl4Gene.getFluid(1000))
                .outputFluids(GTOMaterials.PluripotencyInductionGenePlasmids.getFluid(1000))
                .EUt(1920)
                .duration(50)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("cas9_protein"))
                .inputItems(TagPrefix.dust, GTOMaterials.StreptococcusPyogenes, 12)
                .inputFluids(GTMaterials.DistilledWater.getFluid(1000))
                .outputFluids(GTOMaterials.Cas9Protein.getFluid(1000))
                .EUt(480)
                .duration(100)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("biotin"))
                .inputItems(TagPrefix.dust, GTOMaterials.CupriavidusNecator, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Sugar, 2)
                .inputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(1000))
                .outputFluids(GTOMaterials.Biotin.getFluid(1000))
                .EUt(7680)
                .duration(40)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("clear_ammonia_solution"))
                .inputItems(TagPrefix.dust, GTOMaterials.BrevibacteriumFlavium, 4)
                .inputItems(TagPrefix.dust, GTMaterials.Sugar, 4)
                .outputItems(TagPrefix.dust, GTOMaterials.Glutamine, 40)
                .inputFluids(GTOMaterials.ClearAmmoniaSolution.getFluid(1000))
                .EUt(7680)
                .duration(500)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("raw_growth_medium"))
                .inputItems(TagPrefix.dust, GTOMaterials.Glutamine, 20)
                .inputFluids(GTOMaterials.BasicFibroblastGrowthFactor.getFluid(1000))
                .inputFluids(GTOMaterials.AmmoniumNitrateSolution.getFluid(1000))
                .inputFluids(GTOMaterials.B27Supplement.getFluid(1000))
                .inputFluids(GTOMaterials.EpidermalGrowthFactor.getFluid(1000))
                .outputFluids(GTMaterials.RawGrowthMedium.getFluid(4000))
                .EUt(480)
                .duration(500)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("unknownnutrientagar"))
                .inputItems(TagPrefix.dust, GTMaterials.Salt, 16)
                .inputItems(TagPrefix.dust, GTMaterials.Meat, 16)
                .inputItems(TagPrefix.dust, GTMaterials.Agar, 16)
                .inputFluids(GTOMaterials.UnknowWater.getFluid(4000))
                .inputFluids(GTMaterials.PhthalicAcid.getFluid(4000))
                .outputFluids(GTOMaterials.UnknownNutrientAgar.getFluid(8000))
                .EUt(1920)
                .duration(400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("rawgrowthmedium"))
                .inputItems(TagPrefix.dust, GTOMaterials.Glutamine, 20)
                .inputFluids(GTOMaterials.BiomediumRaw.getFluid(1000))
                .inputFluids(GTMaterials.Mutagen.getFluid(1000))
                .inputFluids(GTMaterials.Biomass.getFluid(100000))
                .outputFluids(GTMaterials.RawGrowthMedium.getFluid(100000))
                .EUt(8388608)
                .duration(20)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("biomediumraw"))
                .inputItems(GTItems.STEM_CELLS.asStack(64))
                .inputItems(GTOItems.TCETIESEAWEEDEXTRACT.asStack(16))
                .inputItems(TagPrefix.dust, GTMaterials.Tritanium)
                .inputFluids(GTMaterials.RawGrowthMedium.getFluid(1000))
                .outputFluids(GTOMaterials.BiomediumRaw.getFluid(1000))
                .EUt(1920)
                .duration(1200)
                .addCondition(new GravityCondition(true))
                .addData("radioactivity", 120)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("stem_cells"))
                .circuitMeta(1)
                .inputItems(GTOItems.STERILIZED_PETRI_DISH.asItem())
                .outputItems(GTItems.STEM_CELLS.asStack())
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.PluripotencyInductionGeneTherapyFluid.getFluid(1000))
                .inputFluids(GTOMaterials.AnimalCells.getFluid(1000))
                .inputFluids(GTMaterials.SterileGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(1000)
                .addData("radioactivity", 10)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("biological_cells"))
                .inputItems(GTOItems.STERILIZED_PETRI_DISH.asItem())
                .inputItems(GTItems.STEM_CELLS.asStack(1))
                .inputItems(TagPrefix.dust, GTMaterials.NaquadahEnriched)
                .inputFluids(GTOMaterials.BiohmediumSterilized.getFluid(1000))
                .inputFluids(GTMaterials.Mutagen.getFluid(1000))
                .outputItems(GTOItems.BIOLOGICAL_CELLS.asStack(1))
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .EUt(122880)
                .duration(400)
                .addData("radioactivity", 60)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder(GTOCore.id("rapidly_replicating_animal_cells"))
                .circuitMeta(2)
                .inputFluids(GTOMaterials.AnimalCells.getFluid(1000))
                .outputFluids(GTOMaterials.RapidlyReplicatingAnimalCells.getFluid(1000))
                .EUt(7680)
                .duration(500)
                .addData("radioactivity", 240)
                .save();
    }
}

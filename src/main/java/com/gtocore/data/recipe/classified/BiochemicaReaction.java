package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.recipe.condition.GravityCondition;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.common.data.GTMaterials.DistilledWater;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Molybdenum;
import static com.gtocore.api.data.tag.GTOTagPrefix.NANO;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.BIOCHEMICAL_REACTION_RECIPES;

final class BiochemicaReaction {

    public static void init() {
        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("pygro_spawn_egg")
                .inputItems(Items.PIGLIN_SPAWN_EGG.asItem())
                .inputItems(GTOItems.GLACIO_SPIRIT.asItem())
                .outputItems("ad_astra:pygro_spawn_egg")
                .inputFluids(GTMaterials.Mutagen.getFluid(1000))
                .EUt(7680)
                .duration(1600)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("pygro_brute_spawn_egg")
                .inputItems(Items.PIGLIN_BRUTE_SPAWN_EGG.asItem())
                .inputItems(GTOItems.GLACIO_SPIRIT.asItem())
                .outputItems("ad_astra:pygro_brute_spawn_egg")
                .inputFluids(GTMaterials.Mutagen.getFluid(1000))
                .EUt(7680)
                .duration(1600)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("mogler_spawn_egg")
                .inputItems(Items.HOGLIN_SPAWN_EGG.asItem())
                .inputItems(GTOItems.GLACIO_SPIRIT.asItem())
                .outputItems("ad_astra:mogler_spawn_egg")
                .inputFluids(GTMaterials.Mutagen.getFluid(1000))
                .EUt(7680)
                .duration(1600)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("sulfur_creeper_spawn_egg")
                .inputItems(Items.CREEPER_SPAWN_EGG.asItem())
                .inputItems(GTOItems.GLACIO_SPIRIT.asItem())
                .outputItems("ad_astra:sulfur_creeper_spawn_egg")
                .inputFluids(GTMaterials.Mutagen.getFluid(1000))
                .EUt(7680)
                .duration(1600)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.builder("succinic_acid_dust")
                .inputItems(dust, GTMaterials.Sugar, 24)
                .inputItems(dust, GTOMaterials.EschericiaColi)
                .outputItems(dust, GTOMaterials.SuccinicAcid, 14)
                .EUt(480)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("succinic_anhydride")
                .inputItems(dust, GTOMaterials.Succinimide, 12)
                .inputItems(dust, GTOMaterials.BrevibacteriumFlavium)
                .outputItems(dust, GTOMaterials.SuccinicAnhydride, 11)
                .outputFluids(GTMaterials.Ammonia.getFluid(1000))
                .EUt(7680)
                .duration(50)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("shewanella_petri_dish")
                .inputItems(GTOItems.GREEN_ALGAE_FIBER.asItem())
                .inputItems(GTOItems.PREPARATION_PETRI_DISH.asItem())
                .outputItems(GTOItems.SHEWANELLA_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(2400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("brevibacterium_petri_dish")
                .inputItems("farmersdelight:rich_soil")
                .inputItems(GTOItems.PREPARATION_PETRI_DISH.asItem())
                .outputItems(GTOItems.BREVIBACTERIUM_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(2400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("cupriavidus_petri_dish")
                .inputItems(Items.COARSE_DIRT.asItem())
                .inputItems(GTOItems.PREPARATION_PETRI_DISH.asItem())
                .outputItems(GTOItems.CUPRIAVIDUS_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(2400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("clostridium_pasteurianum_dish")
                .inputItems(Items.HONEY_BOTTLE.asItem())
                .inputItems(GTOItems.PREPARATION_PETRI_DISH.asItem())
                .outputItems(GTOItems.CLOSTRIDIUM_PASTEURIANUM_DISH.asItem())
                .outputItems(Items.GLASS_BOTTLE.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(2400)
                .save();

        // 10.1016/j.chemosphere.2018.02.079
        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("nano_molybdenum_biochem_reduction")
                .inputItems(dust, SodiumMolybdate, 4)
                .inputItems(dust, ClostridiumPasteurianum)
                .outputItems(NANO, Molybdenum, 4)
                .inputFluids(DistilledWater.getFluid(4000))
                .EUt(30720)
                .duration(2400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("eschericia_petri_dish")
                .inputItems("farmersdelight:minced_beef")
                .inputItems(GTOItems.PREPARATION_PETRI_DISH.asItem())
                .outputItems(GTOItems.ESCHERICIA_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(2400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("bifidobacteriumm_petri_dish")
                .inputItems(GTOItems.PREPARATION_PETRI_DISH.asItem())
                .outputItems(GTOItems.BIFIDOBACTERIUMM_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .inputFluids(GTMaterials.Milk.getFluid(1000))
                .EUt(30720)
                .duration(2400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("streptococcus_petri_dish")
                .inputItems(Items.ROTTEN_FLESH.asItem())
                .inputItems(GTOItems.PREPARATION_PETRI_DISH.asItem())
                .outputItems(GTOItems.STREPTOCOCCUS_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(2400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("brevibacterium")
                .inputItems(GTOItems.BREVIBACTERIUM_PETRI_DISH.asItem())
                .outputItems(dust, GTOMaterials.BrevibacteriumFlavium)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("bifidobacteriumm")
                .inputItems(GTOItems.BIFIDOBACTERIUMM_PETRI_DISH.asItem())
                .outputItems(dust, GTOMaterials.BifidobacteriumBreve)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("eschericia")
                .inputItems(GTOItems.ESCHERICIA_PETRI_DISH.asItem())
                .outputItems(dust, GTOMaterials.EschericiaColi)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("streptococcus")
                .inputItems(GTOItems.STREPTOCOCCUS_PETRI_DISH.asItem())
                .outputItems(dust, GTOMaterials.StreptococcusPyogenes)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("cupriavidus")
                .inputItems(GTOItems.CUPRIAVIDUS_PETRI_DISH.asItem())
                .outputItems(dust, GTOMaterials.CupriavidusNecator)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("clostridium_pasteurianum")
                .inputItems(GTOItems.CLOSTRIDIUM_PASTEURIANUM_DISH.asItem())
                .outputItems(dust, GTOMaterials.ClostridiumPasteurianum)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("shewanella")
                .inputItems(GTOItems.SHEWANELLA_PETRI_DISH.asItem())
                .outputItems(dust, GTOMaterials.Shewanella)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("linoleic_acid")
                .inputItems(dust, GTOMaterials.Yeast, 6)
                .inputFluids(GTMaterials.Biomass.getFluid(1000))
                .outputFluids(GTOMaterials.LinoleicAcid.getFluid(1000))
                .EUt(480)
                .duration(200)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("chitosan")
                .inputItems(dust, GTOMaterials.BifidobacteriumBreve, 8)
                .inputFluids(GTOMaterials.Chitin.getFluid(1000))
                .outputFluids(GTOMaterials.Chitosan.getFluid(1000))
                .EUt(1920)
                .duration(100)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("pluripotency_induction_gene_plasmids")
                .inputItems(dust, GTOMaterials.EschericiaColi, 8)
                .inputFluids(GTOMaterials.Cas9Protein.getFluid(1000))
                .inputFluids(GTOMaterials.MycGene.getFluid(1000))
                .inputFluids(GTOMaterials.Oct4Gene.getFluid(1000))
                .inputFluids(GTOMaterials.Sox2Gene.getFluid(1000))
                .inputFluids(GTOMaterials.Kfl4Gene.getFluid(1000))
                .outputFluids(GTOMaterials.PluripotencyInductionGenePlasmids.getFluid(1000))
                .EUt(1920)
                .duration(50)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("cas9_protein")
                .inputItems(dust, GTOMaterials.StreptococcusPyogenes, 12)
                .inputFluids(GTMaterials.DistilledWater.getFluid(1000))
                .outputFluids(GTOMaterials.Cas9Protein.getFluid(1000))
                .EUt(480)
                .duration(100)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("biotin")
                .inputItems(dust, GTOMaterials.CupriavidusNecator, 2)
                .inputItems(dust, GTMaterials.Sugar, 2)
                .inputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(1000))
                .outputFluids(GTOMaterials.Biotin.getFluid(1000))
                .EUt(7680)
                .duration(40)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("clear_ammonia_solution")
                .inputItems(dust, GTOMaterials.BrevibacteriumFlavium, 4)
                .inputItems(dust, GTMaterials.Sugar, 4)
                .outputItems(dust, GTOMaterials.Glutamine, 40)
                .inputFluids(GTOMaterials.ClearAmmoniaSolution.getFluid(1000))
                .EUt(7680)
                .duration(500)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("raw_growth_medium")
                .inputItems(dust, GTOMaterials.Glutamine, 20)
                .inputFluids(GTOMaterials.BasicFibroblastGrowthFactor.getFluid(1000))
                .inputFluids(GTOMaterials.AmmoniumNitrateSolution.getFluid(1000))
                .inputFluids(GTOMaterials.B27Supplement.getFluid(1000))
                .inputFluids(GTOMaterials.EpidermalGrowthFactor.getFluid(1000))
                .outputFluids(GTMaterials.RawGrowthMedium.getFluid(4000))
                .EUt(480)
                .duration(500)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("unknownnutrientagar")
                .inputItems(dust, GTMaterials.Salt, 16)
                .inputItems(dust, GTMaterials.Meat, 16)
                .inputItems(dust, GTMaterials.Agar, 16)
                .inputFluids(GTOMaterials.UnknowWater.getFluid(4000))
                .inputFluids(GTMaterials.PhthalicAcid.getFluid(4000))
                .outputFluids(GTOMaterials.UnknownNutrientAgar.getFluid(8000))
                .EUt(1920)
                .duration(400)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("rawgrowthmedium")
                .inputItems(dust, GTOMaterials.Glutamine, 20)
                .inputFluids(GTOMaterials.BiomediumRaw.getFluid(1000))
                .inputFluids(GTMaterials.Mutagen.getFluid(1000))
                .inputFluids(GTMaterials.Biomass.getFluid(100000))
                .outputFluids(GTMaterials.RawGrowthMedium.getFluid(100000))
                .EUt(8388608)
                .duration(20)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("biomediumraw")
                .inputItems(GTItems.STEM_CELLS.asItem(), 64)
                .inputItems(GTOItems.TCETIESEAWEEDEXTRACT.asItem(), 16)
                .inputItems(dust, GTMaterials.Tritanium)
                .inputFluids(GTMaterials.RawGrowthMedium.getFluid(1000))
                .outputFluids(GTOMaterials.BiomediumRaw.getFluid(1000))
                .EUt(1920)
                .duration(1200)
                .addCondition(new GravityCondition(true))
                .addData("radioactivity", 120)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("stem_cells")
                .circuitMeta(1)
                .inputItems(GTOItems.STERILIZED_PETRI_DISH.asItem())
                .outputItems(GTItems.STEM_CELLS.asItem())
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .inputFluids(GTOMaterials.PluripotencyInductionGeneTherapyFluid.getFluid(1000))
                .inputFluids(GTOMaterials.AnimalCells.getFluid(1000))
                .inputFluids(GTMaterials.SterileGrowthMedium.getFluid(1000))
                .EUt(30720)
                .duration(1000)
                .addData("radioactivity", 10)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("biological_cells")
                .inputItems(GTOItems.STERILIZED_PETRI_DISH.asItem())
                .inputItems(GTItems.STEM_CELLS.asItem(), 1)
                .inputItems(dust, GTMaterials.NaquadahEnriched)
                .inputFluids(GTOMaterials.BiohmediumSterilized.getFluid(1000))
                .inputFluids(GTMaterials.Mutagen.getFluid(1000))
                .outputItems(GTOItems.BIOLOGICAL_CELLS.asItem(), 1)
                .outputItems(GTOItems.CONTAMINATED_PETRI_DISH.asItem())
                .EUt(122880)
                .duration(400)
                .addData("radioactivity", 60)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("dragon_cells")
                .inputItems(GTOItems.DRAGON_STEM_CELLS.asItem(), 1)
                .inputItems(dust, GTMaterials.Naquadria, 16)
                .inputFluids(GTOMaterials.BiohmediumSterilized.getFluid(10000))
                .inputFluids(GTMaterials.Mutagen.getFluid(10000))
                .outputItems(GTOItems.DRAGON_CELLS.asItem(), 1)
                .EUt(491520)
                .duration(800)
                .addData("radioactivity", 560)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("rapidly_replicating_animal_cells")
                .circuitMeta(2)
                .inputFluids(GTOMaterials.AnimalCells.getFluid(1000))
                .outputFluids(GTOMaterials.RapidlyReplicatingAnimalCells.getFluid(1000))
                .EUt(7680)
                .duration(500)
                .addData("radioactivity", 240)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.builder("zombie_electrode")
                .inputItems("botania:virus_necrodermal", 4)
                .inputItems(GTOItems.CEREBRUM.asItem(), 8)
                .inputItems(TagPrefix.foil, GTMaterials.Tantalum, 16)
                .outputItems("enderio:zombie_electrode", 16)
                .outputItems(Items.ROTTEN_FLESH.asItem(), 4)
                .inputFluids(GTOMaterials.Biotin, 100)
                .inputFluids(GTOMaterials.EpidermalGrowthFactor, 100)
                .inputFluids(GTMaterials.Tritanium, 10)
                .EUt(8168)
                .duration(500)
                .save();

        BIOCHEMICAL_REACTION_RECIPES.recipeBuilder("alpha_lipoic_acid")
                .inputItems(dust, GTOMaterials.BrevibacteriumFlavium, 8)
                .inputItems(dust, GTMaterials.Sulfur, 4)
                .inputFluids(GTMaterials.Biomass.getFluid(1000))
                .outputFluids(GTOMaterials.LipoicAcid.getFluid(1000))
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .EUt(1920)
                .duration(200)
                .save();
    }
}

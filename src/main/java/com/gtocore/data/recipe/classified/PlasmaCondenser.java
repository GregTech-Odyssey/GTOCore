package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.PLASMA_CONDENSER_RECIPES;

final class PlasmaCondenser {

    public static void init() {
        PLASMA_CONDENSER_RECIPES.recipeBuilder("chromatic_glass")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.ChromaticGlass.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.ChromaticGlass, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("celestial_tungsten_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.CelestialTungsten.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.CelestialTungsten, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("awakened_draconium")
                .inputItems(GTOItems.AWAKENED_DRACONIUM_PLASMA_CONTAINMENT_CELL)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL)
                .outputFluids(GTMaterials.Helium, 100000)
                .outputFluids(GTOMaterials.AwakenedDraconium, 1000)
                .EUt(125829120)
                .duration(1200)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("orichalcum_ingot_condenser")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputFluids(GTOMaterials.Orichalcum.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Orichalcum)
                .outputFluids(GTMaterials.Helium, 14400)
                .EUt(1966080)
                .duration(60)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("degenerate_rhenium")
                .inputItems(GTOItems.RHENIUM_PLASMA_CONTAINMENT_CELL)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL)
                .outputFluids(GTMaterials.Helium, 100000)
                .outputFluids(GTOMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.LIQUID, 1000))
                .EUt(7864320)
                .duration(1200)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("quantumchromodynamic_protective_plating")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Vibranium)
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Infuscolium)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 10000))
                .inputFluids(GTOMaterials.HighEnergyQuarkGluon.getFluid(FluidStorageKeys.PLASMA, 100))
                .outputItems(GTOItems.QUANTUMCHROMODYNAMIC_PROTECTIVE_PLATING)
                .outputFluids(GTMaterials.Helium, 10000)
                .EUt(125829120)
                .duration(300)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("taranium_rich_liquid_helium_4_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.TaraniumRichLiquidHelium4.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.TaraniumRichLiquidHelium4, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("hassium")
                .inputFluids(GTOMaterials.MetastableHassium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTMaterials.Helium, 100000)
                .outputFluids(GTOMaterials.MetastableHassium.getFluid(FluidStorageKeys.LIQUID, 1000))
                .EUt(1966080)
                .duration(1200)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("adamantium_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Adamantium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Adamantium, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("neutronium_sphere")
                .notConsumable(GTOItems.BALL_FIELD_SHAPE.asItem())
                .inputItems(GTOItems.NEUTRON_PLASMA_CONTAINMENT_CELL)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 32000))
                .outputItems(GTOItems.NEUTRONIUM_SPHERE, 4)
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL)
                .outputFluids(GTMaterials.Helium, 32000)
                .EUt(1966080)
                .duration(800)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("mithril_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Mithril.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Mithril, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("quantum_chromo_dynamically_confined_matter_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.QuantumChromoDynamicallyConfinedMatter.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.QuantumChromoDynamicallyConfinedMatter, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("actinium_superhydride_dust")
                .inputItems(GTOItems.ACTINIUM_SUPERHYDRIDE_PLASMA_CONTAINMENT_CELL)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 24000))
                .outputItems(TagPrefix.dust, GTOMaterials.ActiniumSuperhydride, 13)
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL)
                .outputFluids(GTMaterials.Helium, 24000)
                .EUt(31457280)
                .duration(340)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("infuscolium_ingot_condenser")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputFluids(GTOMaterials.Infuscolium.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Infuscolium)
                .outputFluids(GTMaterials.Helium, 14400)
                .EUt(1966080)
                .duration(60)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("crystal_matrix")
                .inputItems(GTOItems.CRYSTAL_MATRIX_PLASMA_CONTAINMENT_CELL)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL)
                .outputFluids(GTMaterials.Helium, 100000)
                .outputFluids(GTOMaterials.CrystalMatrix, 1000)
                .EUt(503316480)
                .duration(1000)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("infuscolium_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Infuscolium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Infuscolium, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("heavy_quark_degenerate_matter_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.HeavyQuarkDegenerateMatter.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.HeavyQuarkDegenerateMatter, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("legendarium_ingot_condenser")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputFluids(GTOMaterials.Legendarium.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Legendarium)
                .outputFluids(GTMaterials.Helium, 14400)
                .EUt(1966080)
                .duration(60)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("echoite_ingot_condenser")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputFluids(GTOMaterials.Echoite.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Echoite)
                .outputFluids(GTMaterials.Helium, 14400)
                .EUt(1966080)
                .duration(60)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("starmetal_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Starmetal.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Starmetal, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("cosmic_neutronium")
                .inputItems(GTOItems.COSMIC_NEUTRON_PLASMA_CELL)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputItems(GTOItems.EXTREMELY_DURABLE_PLASMA_CELL)
                .outputFluids(GTMaterials.Helium, 100000)
                .outputFluids(GTOMaterials.CosmicNeutronium, 1000)
                .EUt(503316480)
                .duration(1200)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("legendarium_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Legendarium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Legendarium, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("enderium_ingot_condenser")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputFluids(GTOMaterials.Enderium.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Enderium)
                .outputFluids(GTMaterials.Helium, 14400)
                .EUt(1966080)
                .duration(60)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("quantum_chromo_dynamically_confined_matter_ingot_condenser")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputFluids(GTOMaterials.QuantumChromoDynamicallyConfinedMatter.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.QuantumChromoDynamicallyConfinedMatter)
                .outputFluids(GTMaterials.Helium, 14400)
                .EUt(1966080)
                .duration(60)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("vibranium_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Vibranium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Vibranium, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("heavy_quark_degenerate_matter_ingot_condenser")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputFluids(GTOMaterials.HeavyQuarkDegenerateMatter.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.HeavyQuarkDegenerateMatter)
                .outputFluids(GTMaterials.Helium, 14400)
                .EUt(1966080)
                .duration(60)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("astral_titanium_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.AstralTitanium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.AstralTitanium, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("mithril_ingot_condenser")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputFluids(GTOMaterials.Mithril.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Mithril)
                .outputFluids(GTMaterials.Helium, 14400)
                .EUt(1966080)
                .duration(60)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("cosmic_mesh")
                .inputItems(GTOItems.COSMIC_MESH_CONTAINMENT_UNIT)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT)
                .outputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.LIQUID, 1000))
                .EUt(503316480)
                .duration(800)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("starmetal_ingot_condenser")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputFluids(GTOMaterials.Starmetal.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Starmetal)
                .outputFluids(GTMaterials.Helium, 14400)
                .EUt(1966080)
                .duration(60)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("echoite_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Echoite.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Echoite, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("adamantium_ingot_condenser")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputFluids(GTOMaterials.Adamantium.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Adamantium)
                .outputFluids(GTMaterials.Helium, 14400)
                .EUt(1966080)
                .duration(60)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("chaos")
                .inputItems(GTOItems.CHAOS_CONTAINMENT_UNIT)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT)
                .outputFluids(GTMaterials.Helium, 100000)
                .outputFluids(GTOMaterials.Chaos, 1000)
                .EUt(503316480)
                .duration(1600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("vibranium_ingot_condenser")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputFluids(GTOMaterials.Vibranium.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Vibranium)
                .outputFluids(GTMaterials.Helium, 14400)
                .EUt(1966080)
                .duration(60)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("enderium_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Enderium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Enderium, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("orichalcum_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Orichalcum.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Orichalcum, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("americium_condenser")
                .circuitMeta(1)
                .inputFluids(GTMaterials.Americium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTMaterials.Americium, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("americium_ingot_condenser")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputFluids(GTMaterials.Americium.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingot, GTMaterials.Americium)
                .outputFluids(GTMaterials.Helium, 14400)
                .EUt(1966080)
                .duration(60)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("blazecube_condenser")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.BlazeCube.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.BlazeCube, 1000)
                .outputFluids(GTMaterials.Helium, 100000)
                .EUt(1966080)
                .duration(600)
                .save();

        PLASMA_CONDENSER_RECIPES.recipeBuilder("blazecube_ingot_condenser")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputFluids(GTOMaterials.BlazeCube.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingot, GTOMaterials.BlazeCube)
                .outputFluids(GTMaterials.Helium, 14400)
                .EUt(1966080)
                .duration(60)
                .save();
    }
}

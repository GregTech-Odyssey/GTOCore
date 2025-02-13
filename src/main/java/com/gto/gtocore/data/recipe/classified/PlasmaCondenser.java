package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.init.GTOItems;
import com.gto.gtocore.init.GTOMaterials;
import com.gto.gtocore.init.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface PlasmaCondenser {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("nickel_ingot"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputFluids(GTMaterials.Nickel.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingot, GTMaterials.Nickel)
                .outputFluids(GTMaterials.Helium.getFluid(14400))
                .EUt(1966080)
                .duration(50)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("iron_ingot"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputFluids(GTMaterials.Iron.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingot, GTMaterials.Iron)
                .outputFluids(GTMaterials.Helium.getFluid(14400))
                .EUt(1966080)
                .duration(50)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("celestial_tungsten_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.CelestialTungsten.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.CelestialTungsten.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("awakened_draconium"))
                .inputItems(GTOItems.AWAKENED_DRACONIUM_PLASMA_CONTAINMENT_CELL.asStack())
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asStack())
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .outputFluids(GTOMaterials.AwakenedDraconium.getFluid(1000))
                .EUt(125829120)
                .duration(1200)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("orichalcum_ingot_condenser"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputFluids(GTOMaterials.Orichalcum.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Orichalcum)
                .outputFluids(GTMaterials.Helium.getFluid(14400))
                .EUt(1966080)
                .duration(60)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("degenerate_rhenium"))
                .inputItems(GTOItems.RHENIUM_PLASMA_CONTAINMENT_CELL.asStack())
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asStack())
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .outputFluids(GTOMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.LIQUID, 1000))
                .EUt(7864320)
                .duration(1200)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("quantumchromodynamic_protective_plating"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.Vibranium)
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.Infuscolium)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 10000))
                .inputFluids(GTOMaterials.HighEnergyQuarkGluon.getFluid(FluidStorageKeys.PLASMA, 100))
                .outputItems(GTOItems.QUANTUMCHROMODYNAMIC_PROTECTIVE_PLATING.asStack())
                .outputFluids(GTMaterials.Helium.getFluid(10000))
                .EUt(125829120)
                .duration(300)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("taranium_rich_liquid_helium_4_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.TaraniumRichLiquidHelium4.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.TaraniumRichLiquidHelium4.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("hassium"))
                .inputFluids(GTOMaterials.MetastableHassium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .outputFluids(GTOMaterials.MetastableHassium.getFluid(FluidStorageKeys.LIQUID, 1000))
                .EUt(1966080)
                .duration(1200)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("adamantium_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Adamantium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Adamantium.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("neutronium_sphere"))
                .notConsumable(GTOItems.BALL_FIELD_SHAPE.asStack())
                .inputItems(GTOItems.NEUTRON_PLASMA_CONTAINMENT_CELL.asStack())
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 32000))
                .outputItems(GTOItems.NEUTRONIUM_SPHERE.asStack(4))
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asStack())
                .outputFluids(GTMaterials.Helium.getFluid(32000))
                .EUt(1966080)
                .duration(800)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("nitrogen_condenser"))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Nitrogen.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("mithril_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Mithril.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Mithril.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("nickel_condenser"))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Nickel.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTMaterials.Nickel.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("iron_condenser"))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Iron.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTMaterials.Iron.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("quantum_chromo_dynamically_confined_matter_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.QuantumChromoDynamicallyConfinedMatter.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.QuantumChromoDynamicallyConfinedMatter.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("actinium_superhydride_dust"))
                .inputItems(GTOItems.ACTINIUM_SUPERHYDRIDE_PLASMA_CONTAINMENT_CELL.asStack())
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 24000))
                .outputItems(TagPrefix.dust, GTOMaterials.ActiniumSuperhydride, 13)
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asStack())
                .outputFluids(GTMaterials.Helium.getFluid(24000))
                .EUt(31457280)
                .duration(340)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("infuscolium_ingot_condenser"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputFluids(GTOMaterials.Infuscolium.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Infuscolium)
                .outputFluids(GTMaterials.Helium.getFluid(14400))
                .EUt(1966080)
                .duration(60)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("crystal_matrix"))
                .inputItems(GTOItems.CRYSTAL_MATRIX_PLASMA_CONTAINMENT_CELL.asStack())
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asStack())
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .outputFluids(GTOMaterials.CrystalMatrix.getFluid(1000))
                .EUt(503316480)
                .duration(1000)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("infuscolium_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Infuscolium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Infuscolium.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("heavy_quark_degenerate_matter_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.HeavyQuarkDegenerateMatter.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.HeavyQuarkDegenerateMatter.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("legendarium_ingot_condenser"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputFluids(GTOMaterials.Legendarium.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Legendarium)
                .outputFluids(GTMaterials.Helium.getFluid(14400))
                .EUt(1966080)
                .duration(60)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("echoite_ingot_condenser"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputFluids(GTOMaterials.Echoite.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Echoite)
                .outputFluids(GTMaterials.Helium.getFluid(14400))
                .EUt(1966080)
                .duration(60)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("starmetal_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Starmetal.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Starmetal.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("cosmic_neutronium"))
                .inputItems(GTOItems.COSMIC_NEUTRON_PLASMA_CELL.asStack())
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputItems(GTOItems.EXTREMELY_DURABLE_PLASMA_CELL.asStack())
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .outputFluids(GTOMaterials.CosmicNeutronium.getFluid(1000))
                .EUt(503316480)
                .duration(1200)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("legendarium_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Legendarium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Legendarium.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("enderium_ingot_condenser"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputFluids(GTOMaterials.Enderium.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Enderium)
                .outputFluids(GTMaterials.Helium.getFluid(14400))
                .EUt(1966080)
                .duration(60)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("quantum_chromo_dynamically_confined_matter_ingot_condenser"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputFluids(GTOMaterials.QuantumChromoDynamicallyConfinedMatter.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.QuantumChromoDynamicallyConfinedMatter)
                .outputFluids(GTMaterials.Helium.getFluid(14400))
                .EUt(1966080)
                .duration(60)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("vibranium_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Vibranium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Vibranium.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("heavy_quark_degenerate_matter_ingot_condenser"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputFluids(GTOMaterials.HeavyQuarkDegenerateMatter.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.HeavyQuarkDegenerateMatter)
                .outputFluids(GTMaterials.Helium.getFluid(14400))
                .EUt(1966080)
                .duration(60)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("astral_titanium_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.AstralTitanium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.AstralTitanium.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("mithril_ingot_condenser"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputFluids(GTOMaterials.Mithril.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Mithril)
                .outputFluids(GTMaterials.Helium.getFluid(14400))
                .EUt(1966080)
                .duration(60)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("cosmic_mesh"))
                .inputItems(GTOItems.COSMIC_MESH_CONTAINMENT_UNIT.asStack())
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asStack())
                .outputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.LIQUID, 1000))
                .EUt(503316480)
                .duration(800)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("starmetal_ingot_condenser"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputFluids(GTOMaterials.Starmetal.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Starmetal)
                .outputFluids(GTMaterials.Helium.getFluid(14400))
                .EUt(1966080)
                .duration(60)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("echoite_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Echoite.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Echoite.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("adamantium_ingot_condenser"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputFluids(GTOMaterials.Adamantium.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Adamantium)
                .outputFluids(GTMaterials.Helium.getFluid(14400))
                .EUt(1966080)
                .duration(60)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("chaos"))
                .inputItems(GTOItems.CHAOS_CONTAINMENT_UNIT.asStack())
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asStack())
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .outputFluids(GTOMaterials.Chaos.getFluid(1000))
                .EUt(503316480)
                .duration(1600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("argon_condenser"))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Argon.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTMaterials.Argon.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("helium_condenser"))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTMaterials.Helium.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("oxygen_condenser"))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Oxygen.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("vibranium_ingot_condenser"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputFluids(GTOMaterials.Vibranium.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 14400))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Vibranium)
                .outputFluids(GTMaterials.Helium.getFluid(14400))
                .EUt(1966080)
                .duration(60)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("enderium_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Enderium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Enderium.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PLASMA_CONDENSER_RECIPES.recipeBuilder(GTOCore.id("orichalcum_condenser"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Orichalcum.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 100000))
                .outputFluids(GTOMaterials.Orichalcum.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(100000))
                .EUt(1966080)
                .duration(600)
                .save(provider);
    }
}

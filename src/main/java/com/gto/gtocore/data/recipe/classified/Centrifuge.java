package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.GTOCleanroomType;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.recipe.condition.GravityCondition;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

interface Centrifuge {

    static void init() {
        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("depleted_reactor_thorium_simple"))
                .inputItems(GTOItems.DEPLETED_REACTOR_THORIUM_SIMPLE.asStack())
                .outputItems(GTOItems.REACTOR_FUEL_ROD.asStack())
                .chancedOutput(TagPrefix.dust, GTMaterials.Uranium238, 4, 4000, 500)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(2), 1600, 500)
                .EUt(480)
                .duration(40)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("depleted_reactor_uranium_dual"))
                .inputItems(GTOItems.DEPLETED_REACTOR_URANIUM_DUAL.asStack())
                .outputItems(GTOItems.REACTOR_FUEL_ROD.asStack(2))
                .outputItems(TagPrefix.rod, GTMaterials.Steel, 4)
                .chancedOutput(TagPrefix.dust, GTMaterials.Plutonium239, 12, 2500, 100)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(4), 3600, 500)
                .EUt(480)
                .duration(80)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("fissioned_uranium_235_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.FissionedUranium235)
                .outputItems(TagPrefix.dust, GTMaterials.Tin)
                .outputItems(TagPrefix.dust, GTMaterials.Technetium)
                .EUt(1920)
                .duration(400)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("depleted_reactor_mox_simple"))
                .inputItems(GTOItems.DEPLETED_REACTOR_MOX_SIMPLE.asStack())
                .outputItems(GTOItems.REACTOR_FUEL_ROD.asStack())
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(8), 2000, 1000)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(2), 1600, 500)
                .EUt(480)
                .duration(40)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("depleted_reactor_naquadah_quad"))
                .inputItems(GTOItems.DEPLETED_REACTOR_NAQUADAH_QUAD.asStack())
                .outputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asStack(4))
                .outputItems(TagPrefix.rod, GTMaterials.TungstenCarbide, 12)
                .chancedOutput(TagPrefix.dust, GTMaterials.Plutonium239, 8, 8000, 200)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(8), 8000, 500)
                .EUt(480)
                .duration(160)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("dragon_element"))
                .inputFluids(GTOMaterials.TurbidDragonBlood.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Collagen)
                .outputFluids(GTOMaterials.DragonElement.getFluid(500))
                .EUt(7680)
                .duration(200)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("depleted_reactor_uranium_quad"))
                .inputItems(GTOItems.DEPLETED_REACTOR_URANIUM_QUAD.asStack())
                .outputItems(GTOItems.REACTOR_FUEL_ROD.asStack(4))
                .outputItems(TagPrefix.rod, GTMaterials.Steel, 12)
                .chancedOutput(TagPrefix.dust, GTMaterials.Plutonium239, 24, 2500, 100)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(8), 8000, 500)
                .EUt(480)
                .duration(160)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("depleted_reactor_naquadah_dual"))
                .inputItems(GTOItems.DEPLETED_REACTOR_NAQUADAH_DUAL.asStack())
                .outputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asStack(2))
                .outputItems(TagPrefix.rod, GTMaterials.TungstenCarbide, 4)
                .chancedOutput(TagPrefix.dust, GTMaterials.Plutonium239, 4, 8000, 200)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(4), 3600, 500)
                .EUt(480)
                .duration(80)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("superheavyradox"))
                .inputFluids(GTOMaterials.SuperHeavyRadox.getFluid(1000))
                .outputFluids(GTOMaterials.HeavyRadox.getFluid(2000))
                .EUt(1966080)
                .duration(200)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("depleted_reactor_uranium_simple"))
                .inputItems(GTOItems.DEPLETED_REACTOR_URANIUM_SIMPLE.asStack())
                .outputItems(GTOItems.REACTOR_FUEL_ROD.asStack())
                .chancedOutput(TagPrefix.dust, GTMaterials.Plutonium239, 6, 2500, 100)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(2), 1600, 500)
                .EUt(480)
                .duration(40)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("clean_bedrock_solution"))
                .inputFluids(GTOMaterials.BedrockSootSolution.getFluid(2000))
                .outputItems(TagPrefix.dustSmall, GTMaterials.Naquadah)
                .outputItems(TagPrefix.dustTiny, GTMaterials.NaquadahEnriched)
                .outputItems(TagPrefix.dustTiny, GTMaterials.Naquadria)
                .outputFluids(GTOMaterials.CleanBedrockSolution.getFluid(1000))
                .EUt(491520)
                .duration(200)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("tcetiedandelions"))
                .inputFluids(GTOMaterials.SeaweedBroth.getFluid(1000))
                .outputItems(GTOItems.TCETIEDANDELIONS.asStack(64))
                .EUt(120)
                .duration(200)
                .addCondition(new GravityCondition(false))
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("nuclear_waste"))
                .inputItems(GTOItems.NUCLEAR_WASTE.asStack())
                .outputItems(TagPrefix.dustTiny, GTMaterials.Plutonium239)
                .outputItems(TagPrefix.dustTiny, GTMaterials.Polonium)
                .outputItems(TagPrefix.dustTiny, GTMaterials.Uranium238)
                .outputItems(TagPrefix.dustTiny, GTMaterials.Thorium)
                .outputItems(TagPrefix.dustTiny, GTMaterials.Protactinium)
                .outputItems(TagPrefix.dustTiny, GTMaterials.Neptunium)
                .EUt(2048)
                .duration(200)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("free_electron_gas"))
                .notConsumable(GTOItems.SEPARATION_ELECTROMAGNET.asStack())
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .outputFluids(GTOMaterials.FreeElectronGas.getFluid(1000))
                .outputFluids(GTOMaterials.FreeAlphaGas.getFluid(500))
                .EUt(491520)
                .duration(200)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("iodine_dust"))
                .inputFluids(GTOMaterials.IodineContainingSlurry.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Iodine)
                .outputItems(TagPrefix.dust, GTMaterials.RockSalt, 2)
                .EUt(120)
                .duration(200)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("actinium_radium_nitrate_solution"))
                .notConsumable(TagPrefix.dust, GTOMaterials.TrifluoroaceticPhosphateEster)
                .inputFluids(GTOMaterials.ActiniumRadiumNitrateSolution.getFluid(13000))
                .outputItems(TagPrefix.dust, GTOMaterials.ActiniumNitrate, 26)
                .outputItems(TagPrefix.dust, GTOMaterials.RadiumNitrate, 27)
                .chancedOutput(TagPrefix.dust, GTMaterials.Francium, 4, 2500, 0)
                .chancedOutput(TagPrefix.dust, GTMaterials.Thorium, 2500, 0)
                .chancedOutput(TagPrefix.dust, GTMaterials.Protactinium, 2, 2500, 0)
                .chancedOutput(TagPrefix.dust, GTMaterials.Radium, 2500, 0)
                .outputFluids(GTMaterials.Water.getFluid(13000))
                .EUt(480)
                .duration(160)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("uranium_sulfate_waste_solution"))
                .inputFluids(GTOMaterials.UraniumSulfateWasteSolution.getFluid(1000))
                .outputItems(TagPrefix.dustTiny, GTMaterials.Lead)
                .outputItems(TagPrefix.dustTiny, GTMaterials.Barium)
                .outputItems(TagPrefix.dustTiny, GTMaterials.Strontium)
                .outputItems(TagPrefix.dustTiny, GTMaterials.Radium)
                .outputFluids(GTMaterials.DilutedSulfuricAcid.getFluid(1000))
                .EUt(480)
                .duration(500)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("depleted_reactor_mox_dual"))
                .inputItems(GTOItems.DEPLETED_REACTOR_MOX_DUAL.asStack())
                .outputItems(GTOItems.REACTOR_FUEL_ROD.asStack(2))
                .outputItems(TagPrefix.rod, GTMaterials.Steel, 4)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(16), 2000, 1000)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(4), 3600, 500)
                .EUt(480)
                .duration(80)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("flerovium"))
                .inputFluids(GTOMaterials.Flyb.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTMaterials.Flerovium.getFluid(288))
                .outputFluids(GTOMaterials.Ytterbium178.getFluid(288))
                .EUt(1920)
                .duration(290)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("rare_earth_chlorides"))
                .notConsumable(GTItems.ITEM_MAGNET_HV.asStack())
                .inputFluids(GTOMaterials.RareEarthChlorides.getFluid(2000))
                .outputFluids(GTOMaterials.LaNdOxidesSolution.getFluid(250))
                .outputFluids(GTOMaterials.SmGdOxidesSolution.getFluid(250))
                .outputFluids(GTOMaterials.TbHoOxidesSolution.getFluid(250))
                .outputFluids(GTOMaterials.ErLuOxidesSolution.getFluid(250))
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .EUt(480)
                .duration(200)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("quark_gluon"))
                .notConsumable(GTOItems.SEPARATION_ELECTROMAGNET.asStack())
                .inputFluids(GTOMaterials.QuarkGluon.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.HeavyQuarks.getFluid(200))
                .outputFluids(GTOMaterials.LightQuarks.getFluid(600))
                .outputFluids(GTOMaterials.Gluons.getFluid(200))
                .EUt(7864320)
                .duration(100)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("titanium_50_tetrafluoride"))
                .inputFluids(GTOMaterials.TitaniumTetrafluoride.getFluid(1000))
                .outputFluids(GTOMaterials.Titanium50Tetrafluoride.getFluid(10))
                .outputFluids(GTOMaterials.TitaniumTetrafluoride.getFluid(990))
                .EUt(480)
                .duration(200)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("inert_residues_dust"))
                .notConsumable(GTOItems.SEPARATION_ELECTROMAGNET.asStack())
                .inputItems(TagPrefix.dust, GTOMaterials.MetalResidue, 10)
                .inputFluids(GTMaterials.DistilledWater.getFluid(10000))
                .outputItems(TagPrefix.dust, GTOMaterials.InertResidues)
                .outputFluids(GTOMaterials.OxidizedResidualSolution.getFluid(10000))
                .EUt(491520)
                .duration(200)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("depleted_reactor_mox_quad"))
                .inputItems(GTOItems.DEPLETED_REACTOR_MOX_QUAD.asStack())
                .outputItems(GTOItems.REACTOR_FUEL_ROD.asStack(4))
                .outputItems(TagPrefix.rod, GTMaterials.Steel, 12)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(32), 2000, 1000)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(8), 8000, 500)
                .EUt(480)
                .duration(160)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("depleted_reactor_thorium_quad"))
                .inputItems(GTOItems.DEPLETED_REACTOR_THORIUM_QUAD.asStack())
                .outputItems(GTOItems.REACTOR_FUEL_ROD.asStack(4))
                .outputItems(TagPrefix.rod, GTMaterials.Steel, 12)
                .chancedOutput(TagPrefix.dust, GTMaterials.Uranium238, 16, 4000, 500)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(8), 8000, 500)
                .EUt(480)
                .duration(160)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("depleted_reactor_naquadah_simple"))
                .inputItems(GTOItems.DEPLETED_REACTOR_NAQUADAH_SIMPLE.asStack())
                .outputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asStack())
                .chancedOutput(TagPrefix.dust, GTMaterials.Plutonium239, 2, 8000, 200)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(2), 1600, 500)
                .EUt(480)
                .duration(40)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("taranium_enriched_liquid_helium_3"))
                .notConsumable(GTOItems.SEPARATION_ELECTROMAGNET.asStack())
                .inputFluids(GTOMaterials.DustyLiquidHeliumIII.getFluid(1000))
                .outputFluids(GTOMaterials.TaraniumEnrichedLiquidHelium3.getFluid(500))
                .EUt(3000)
                .duration(400)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("depleted_reactor_thorium_dual"))
                .inputItems(GTOItems.DEPLETED_REACTOR_THORIUM_DUAL.asStack())
                .outputItems(GTOItems.REACTOR_FUEL_ROD.asStack(2))
                .outputItems(TagPrefix.rod, GTMaterials.Steel, 4)
                .chancedOutput(TagPrefix.dust, GTMaterials.Uranium238, 8, 4000, 500)
                .chancedOutput(GTOItems.NUCLEAR_WASTE.asStack(4), 3600, 500)
                .EUt(480)
                .duration(80)
                .save();

        GTORecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("heavily_fluorinated_trinium_solution"))
                .inputFluids(GTOMaterials.HeavilyFluorinatedTriniumSolution.getFluid(8000))
                .outputItems(TagPrefix.dust, GTOMaterials.TriniumTetrafluoride, 60)
                .outputFluids(GTMaterials.Fluorine.getFluid(16000))
                .outputFluids(GTOMaterials.Perfluorobenzene.getFluid(2000))
                .EUt(30720)
                .duration(350)
                .save();
    }
}

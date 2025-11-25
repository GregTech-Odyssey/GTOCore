package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.FUSION_RECIPES;

final class Fusion {

    public static void init() {
        FUSION_RECIPES.recipeBuilder("titanium")
                .inputFluids(GTMaterials.Aluminium.getFluid(144))
                .inputFluids(GTMaterials.Fluorine.getFluid(144))
                .outputFluids(GTMaterials.Titanium.getFluid(FluidStorageKeys.PLASMA, 144))
                .duration(160)
                .EUt(49152)
                .fusionStartEU(100_000_000)
                .save();

        FUSION_RECIPES.recipeBuilder("sulfur")
                .inputFluids(GTMaterials.Aluminium.getFluid(144))
                .inputFluids(GTMaterials.Lithium.getFluid(144))
                .outputFluids(GTMaterials.Sulfur.getFluid(FluidStorageKeys.PLASMA, 144))
                .duration(120)
                .EUt(10240)
                .fusionStartEU(240_000_000)
                .save();

        FUSION_RECIPES.recipeBuilder("boron")
                .inputFluids(GTMaterials.Helium.getFluid(144))
                .inputFluids(GTMaterials.Lithium.getFluid(144))
                .outputFluids(GTMaterials.Boron.getFluid(FluidStorageKeys.PLASMA, 144))
                .duration(60)
                .EUt(10240)
                .fusionStartEU(50_000_000)
                .save();

        FUSION_RECIPES.recipeBuilder("calcium")
                .inputFluids(GTMaterials.Magnesium.getFluid(128))
                .inputFluids(GTMaterials.Oxygen.getFluid(128))
                .outputFluids(GTMaterials.Calcium.getFluid(FluidStorageKeys.PLASMA, 16))
                .duration(128)
                .EUt(7680)
                .fusionStartEU(120_000_000)
                .save();

        FUSION_RECIPES.recipeBuilder("zinc")
                .inputFluids(GTMaterials.Copper.getFluid(72))
                .inputFluids(GTMaterials.Trinium.getFluid(50))
                .outputFluids(GTMaterials.Zinc.getFluid(FluidStorageKeys.PLASMA, 72))
                .duration(16)
                .EUt(49152)
                .fusionStartEU(180_000_000)
                .save();

        FUSION_RECIPES.recipeBuilder("niobium")
                .inputFluids(GTMaterials.Cobalt.getFluid(144))
                .inputFluids(GTMaterials.Silicon.getFluid(144))
                .outputFluids(GTMaterials.Niobium.getFluid(FluidStorageKeys.PLASMA, 144))
                .duration(16)
                .EUt(49152)
                .fusionStartEU(125_000_000)
                .save();

        FUSION_RECIPES.recipeBuilder("lead")
                .inputFluids(GTMaterials.Tellurium.getFluid(288))
                .inputFluids(GTMaterials.Zinc.getFluid(288))
                .outputFluids(GTMaterials.Lead.getFluid(FluidStorageKeys.PLASMA, 288))
                .duration(4)
                .EUt(2932160)
                .fusionStartEU(1000_000_000)
                .save();

        FUSION_RECIPES.recipeBuilder("thorium")
                .inputFluids(GTMaterials.Osmium.getFluid(288))
                .inputFluids(GTMaterials.Silicon.getFluid(288))
                .outputFluids(GTMaterials.Thorium.getFluid(FluidStorageKeys.PLASMA, 288))
                .duration(4)
                .EUt(2932160)
                .fusionStartEU(1000_000_000)
                .save();

        FUSION_RECIPES.recipeBuilder("neon")
                .inputFluids(GTMaterials.Boron.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Calcium.getFluid(FluidStorageKeys.PLASMA, 16))
                .outputFluids(GTMaterials.Neon.getFluid(FluidStorageKeys.PLASMA, 125))
                .duration(64)
                .EUt(30720)
                .fusionStartEU(100_000_000)
                .save();

        FUSION_RECIPES.recipeBuilder("xenon")
                .inputFluids(GTMaterials.Curium.getFluid(72))
                .inputFluids(GTMaterials.Americium.getFluid(72))
                .outputFluids(GTMaterials.Xenon.getFluid(FluidStorageKeys.PLASMA, 144))
                .duration(72)
                .EUt(30720)
                .fusionStartEU(200_000_000)
                .save();

        FUSION_RECIPES.recipeBuilder("chromatic_glass")
                .inputFluids(GTMaterials.Glass.getFluid(288))
                .inputFluids(GTMaterials.Xenon.getFluid(FluidStorageKeys.PLASMA, 144))
                .outputFluids(GTOMaterials.ChromaticGlass.getFluid(FluidStorageKeys.PLASMA, 288))
                .duration(640)
                .EUt(122880)
                .fusionStartEU(2000_000_000)
                .save();

        FUSION_RECIPES.recipeBuilder("orichalcum_plasma")
                .inputFluids(GTMaterials.Einsteinium.getFluid(144))
                .inputFluids(GTMaterials.Sodium.getFluid(1152))
                .outputFluids(GTOMaterials.Orichalcum.getFluid(FluidStorageKeys.PLASMA, 144))
                .EUt(122880)
                .duration(200)
                .fusionStartEU(600000000)
                .save();

        FUSION_RECIPES.recipeBuilder("moscovium")
                .inputFluids(GTMaterials.Calcium.getFluid(32))
                .inputFluids(GTMaterials.Curium.getFluid(32))
                .outputFluids(GTMaterials.Moscovium.getFluid(32))
                .EUt(122880)
                .duration(128)
                .fusionStartEU(800000000)
                .save();

        FUSION_RECIPES.recipeBuilder("livermorium")
                .inputFluids(GTMaterials.Thorium.getFluid(32))
                .inputFluids(GTMaterials.Iron.getFluid(32))
                .outputFluids(GTMaterials.Livermorium.getFluid(32))
                .EUt(122880)
                .duration(128)
                .fusionStartEU(800000000)
                .save();

        FUSION_RECIPES.recipeBuilder("infinity")
                .inputFluids(GTOMaterials.CrystalMatrix.getFluid(2000))
                .inputFluids(GTOMaterials.CosmicNeutronium.getFluid(1000))
                .outputFluids(GTOMaterials.Infinity.getFluid(64))
                .EUt(7864320)
                .duration(4800)
                .fusionStartEU(2100000000)
                .save();

        FUSION_RECIPES.recipeBuilder("hot_oganesson")
                .inputFluids(GTOMaterials.OganessonBreedingBase.getFluid(16))
                .inputFluids(GTMaterials.Dysprosium.getFluid(16))
                .outputFluids(GTOMaterials.HotOganesson.getFluid(125))
                .EUt(491520)
                .duration(64)
                .fusionStartEU(960000000)
                .save();

        FUSION_RECIPES.recipeBuilder("hassium")
                .inputFluids(GTOMaterials.ScandiumTitanium50Mixture.getFluid(32))
                .inputFluids(GTMaterials.Radon.getFluid(250))
                .outputFluids(GTOMaterials.MetastableHassium.getFluid(FluidStorageKeys.PLASMA, 32))
                .EUt(491520)
                .duration(64)
                .fusionStartEU(960000000)
                .save();

        FUSION_RECIPES.recipeBuilder("vibranium_plasma")
                .inputFluids(GTOMaterials.VibraniumUnstable.getFluid(16))
                .inputFluids(GTOMaterials.Adamantium.getFluid(16))
                .outputFluids(GTOMaterials.Vibranium.getFluid(FluidStorageKeys.PLASMA, 16))
                .EUt(1966080)
                .duration(200)
                .fusionStartEU(1800000000)
                .save();

        FUSION_RECIPES.recipeBuilder("taranium_rich_liquid_helium_4_plasma")
                .inputFluids(GTOMaterials.TaraniumEnrichedLiquidHelium3.getFluid(125))
                .inputFluids(GTMaterials.Hydrogen.getFluid(125))
                .outputFluids(GTOMaterials.TaraniumRichLiquidHelium4.getFluid(FluidStorageKeys.PLASMA, 125))
                .EUt(1048576)
                .duration(128)
                .fusionStartEU(1200000000)
                .save();

        FUSION_RECIPES.recipeBuilder("mithril_plasma")
                .inputFluids(GTMaterials.Berkelium.getFluid(144))
                .inputFluids(GTMaterials.Potassium.getFluid(1152))
                .outputFluids(GTOMaterials.Mithril.getFluid(FluidStorageKeys.PLASMA, 144))
                .EUt(122880)
                .duration(200)
                .fusionStartEU(600000000)
                .save();

        FUSION_RECIPES.recipeBuilder("seaborgium")
                .inputFluids(GTMaterials.Calcium.getFluid(64))
                .inputFluids(GTMaterials.Plutonium239.getFluid(64))
                .outputFluids(GTMaterials.Seaborgium.getFluid(64))
                .EUt(65536)
                .duration(128)
                .fusionStartEU(720000000)
                .save();

        FUSION_RECIPES.recipeBuilder("tennessine")
                .inputFluids(GTMaterials.Lead.getFluid(16))
                .inputFluids(GTMaterials.Bromine.getFluid(16))
                .outputFluids(GTMaterials.Tennessine.getFluid(16))
                .EUt(262144)
                .duration(64)
                .fusionStartEU(960000000)
                .save();

        FUSION_RECIPES.recipeBuilder("awakened_draconium_plasma")
                .inputFluids(GTOMaterials.Draconium.getFluid(125))
                .inputFluids(GTOMaterials.QuantumChromoDynamicallyConfinedMatter.getFluid(125))
                .outputFluids(GTOMaterials.AwakenedDraconium.getFluid(FluidStorageKeys.PLASMA, 125))
                .EUt(7864320)
                .duration(800)
                .fusionStartEU(2100000000)
                .save();

        FUSION_RECIPES.recipeBuilder("silver_plasma")
                .inputFluids(GTMaterials.Europium.getFluid(16))
                .inputFluids(GTMaterials.Arsenic.getFluid(16))
                .outputFluids(GTMaterials.Silver.getFluid(FluidStorageKeys.PLASMA, 16))
                .EUt(65536)
                .duration(36)
                .fusionStartEU(480000000)
                .save();

        FUSION_RECIPES.recipeBuilder("dubnium")
                .inputFluids(GTMaterials.Europium.getFluid(64))
                .inputFluids(GTMaterials.Neon.getFluid(250))
                .outputFluids(GTMaterials.Dubnium.getFluid(64))
                .EUt(65536)
                .duration(128)
                .fusionStartEU(720000000)
                .save();

        FUSION_RECIPES.builder("americium_and_naquadria_to_neutronium_plasma")
                .inputFluids(GTMaterials.Americium.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Naquadria.getFluid(36))
                .outputFluids(GTMaterials.Neutronium.getFluid(36))
                .duration(200)
                .EUt(98304)
                .fusionStartEU(600_000_000)
                .save();
        FUSION_RECIPES.builder("kalendrite")
                .inputFluids(GTOMaterials.Resonarium, FluidStorageKeys.PLASMA, 36)
                .inputFluids(GTOMaterials.Gaia, 36)
                .outputFluids(GTOMaterials.Kalendrite, 36)
                .EUt(1150000)
                .duration(120)
                .fusionStartEU(1_800_000_000)
                .save();
    }
}

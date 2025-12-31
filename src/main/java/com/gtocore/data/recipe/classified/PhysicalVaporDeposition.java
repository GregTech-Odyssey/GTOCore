package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.GTValues.UHV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gtocore.common.data.GTORecipeTypes.PHYSICAL_VAPOR_DEPOSITION_RECIPES;

final class PhysicalVaporDeposition {

    public static void init() {
        PHYSICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("highly_insulating_foil")
                .inputItems(TagPrefix.foil, GTOMaterials.Polyetheretherketone)
                .inputFluids(GTOMaterials.Azafullerene, 10)
                .outputItems(GTOItems.HIGHLY_INSULATING_FOIL)
                .EUt(7680)
                .duration(240)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("cosmic_soc_wafer")
                .inputItems(GTOItems.PREPARED_COSMIC_SOC_WAFER)
                .inputFluids(GTMaterials.Argon.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.SIMPLE_COSMIC_SOC_WAFER)
                .EUt(7864320)
                .duration(600)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("fullerene_polymer_matrix_soft_tubing")
                .inputItems(TagPrefix.wireFine, GTOMaterials.Polyetheretherketone)
                .inputFluids(GTOMaterials.FullerenePolymerMatrixPulp, 18)
                .outputItems(GTOItems.FULLERENE_POLYMER_MATRIX_SOFT_TUBING)
                .EUt(100)
                .duration(80)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("electron_permeable_neutronium_coated_glass")
                .inputItems(GTOBlocks.AMPROSIUM_BOROSILICATE_GLASS.asItem())
                .inputFluids(GTMaterials.Sulfur.getFluid(FluidStorageKeys.PLASMA, 288))
                .outputItems(GTOBlocks.ELECTRON_PERMEABLE_AMPROSIUM_COATED_GLASS.asItem())
                .EUt(122880)
                .duration(100)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("non_photonic_matter_exclusion_glass")
                .inputItems(GTOBlocks.QUARKS_BOROSILICATE_GLASS.asItem())
                .inputFluids(GTOMaterials.Legendarium.getFluid(FluidStorageKeys.PLASMA, 576))
                .outputItems(GTOBlocks.NON_PHOTONIC_MATTER_EXCLUSION_GLASS.asItem())
                .EUt(1966080)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("omni_purpose_infinity_fused_glass")
                .inputItems(GTOBlocks.TARANIUM_BOROSILICATE_GLASS.asItem())
                .inputFluids(GTOMaterials.QuarkGluon.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOBlocks.OMNI_PURPOSE_INFINITY_FUSED_GLASS.asItem())
                .EUt(491520)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.builder("uranium_235_stainless_steel_target")
                .inputItems(GTOTagPrefix.TARGET_BASE, GTMaterials.StainlessSteel)
                .outputItems(GTOTagPrefix.STAINLESS_STEEL_TARGET, GTMaterials.Uranium235)
                .inputFluids(GTMaterials.Uranium235, 576)
                .EUt(30720)
                .duration(640)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.builder("uranium_stainless_steel_target")
                .inputItems(GTOTagPrefix.TARGET_BASE, GTMaterials.StainlessSteel)
                .outputItems(GTOTagPrefix.STAINLESS_STEEL_TARGET, GTMaterials.Uranium238)
                .inputFluids(GTMaterials.Uranium238, 576)
                .EUt(30720)
                .duration(640)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.builder("neptunium_stainless_steel_target")
                .inputItems(GTOTagPrefix.TARGET_BASE, GTMaterials.StainlessSteel)
                .outputItems(GTOTagPrefix.STAINLESS_STEEL_TARGET, GTMaterials.Neptunium)
                .inputFluids(GTMaterials.Neptunium, 576)
                .EUt(30720)
                .duration(640)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.builder("plutonium_stainless_steel_target")
                .inputItems(GTOTagPrefix.TARGET_BASE, GTMaterials.StainlessSteel)
                .outputItems(GTOTagPrefix.STAINLESS_STEEL_TARGET, GTMaterials.Plutonium239)
                .inputFluids(GTMaterials.Plutonium239, 576)
                .EUt(30720)
                .duration(640)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.builder("plutonium_241_stainless_steel_target")
                .inputItems(GTOTagPrefix.TARGET_BASE, GTMaterials.StainlessSteel)
                .outputItems(GTOTagPrefix.STAINLESS_STEEL_TARGET, GTMaterials.Plutonium241)
                .inputFluids(GTMaterials.Plutonium241, 576)
                .EUt(30720)
                .duration(640)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.builder("americium_stainless_steel_target")
                .inputItems(GTOTagPrefix.TARGET_BASE, GTMaterials.StainlessSteel)
                .outputItems(GTOTagPrefix.STAINLESS_STEEL_TARGET, GTMaterials.Americium)
                .inputFluids(GTMaterials.Americium, 576)
                .EUt(122880)
                .duration(640)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.builder("curium_stainless_steel_target")
                .inputItems(GTOTagPrefix.TARGET_BASE, GTMaterials.StainlessSteel)
                .outputItems(GTOTagPrefix.STAINLESS_STEEL_TARGET, GTMaterials.Curium)
                .inputFluids(GTMaterials.Curium, 576)
                .EUt(122880)
                .duration(640)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.builder("berkelium_stainless_steel_target")
                .inputItems(GTOTagPrefix.TARGET_BASE, GTMaterials.StainlessSteel)
                .outputItems(GTOTagPrefix.STAINLESS_STEEL_TARGET, GTMaterials.Berkelium)
                .inputFluids(GTMaterials.Berkelium, 576)
                .EUt(122880)
                .duration(640)
                .save();
        PHYSICAL_VAPOR_DEPOSITION_RECIPES.builder("high_pressure_static_adsorption_mfpc_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.ImpregnatedAlkaneFilledMFPC)
                .notConsumable(TagPrefix.plate, GTOMaterials.Laurenium, 4)
                .outputItems(TagPrefix.dust, GTOMaterials.HighPressureStaticAdsorptionMFPC)
                .inputFluids(GTMaterials.Hydrogen, 4000)
                .inputFluids(GTMaterials.Magnesium, 1152)
                .inputFluids(GTMaterials.Nickel.getFluid(FluidStorageKeys.PLASMA, 1152))
                .EUt(9000)
                .duration(1200)
                .save();
        PHYSICAL_VAPOR_DEPOSITION_RECIPES.builder("plasma_field_glass")
                .inputItems(GTOBlocks.ENDERIUM_BOROSILICATE_GLASS.asItem())
                .inputItems(GTItems.EMITTER_ZPM, 2)
                .outputItems(GTOBlocks.PLASMA_FIELD_GLASS.asItem())
                .inputFluids(GTOMaterials.DegenerateRhenium, FluidStorageKeys.PLASMA, 1000)
                .inputFluids(GTMaterials.MolybdenumDisilicide, 1296)
                .duration(200)
                .EUt(VA[UHV])
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }
}

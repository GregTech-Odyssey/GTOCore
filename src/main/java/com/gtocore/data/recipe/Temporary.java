package com.gtocore.data.recipe;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.GTOValues;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import java.util.Locale;

import static com.gtocore.common.data.GTORecipeTypes.*;

public final class Temporary {

    public static void init() {
        FUSION_RECIPES.builder("quicksilver")
                .inputFluids(GTOMaterials.AstralSilver.getFluid(576))
                .inputFluids(GTOMaterials.Gaia, 144)
                .outputFluids(GTOMaterials.Quicksilver, 288)
                .duration(640)
                .EUt(491520)
                .fusionStartEU(810_000_000L)
                .save();

        FUSION_RECIPES.builder("lemurite")
                .inputFluids(GTMaterials.Livermorium, 144)
                .inputFluids(GTMaterials.Naquadria, 288)
                .outputFluids(GTOMaterials.Lemurite, 288)
                .EUt(983040)
                .duration(720)
                .fusionStartEU(960_000_000L)
                .save();

        FUSION_RECIPES.builder("ceruclase")
                .inputFluids(GTOMaterials.Mithril, 144)
                .inputFluids(GTMaterials.Tungsten, 72)
                .outputFluids(GTOMaterials.Ceruclase, 144)
                .EUt(245760)
                .duration(480)
                .fusionStartEU(840_000_000L)
                .save();

        FUSION_RECIPES.builder("ignatius")
                .inputFluids(GTOMaterials.Orichalcum, 576)
                .inputFluids(GTOMaterials.Alfsteel, 144)
                .outputFluids(GTOMaterials.Ignatius, 576)
                .EUt(245760)
                .duration(1280)
                .fusionStartEU(1000_000_000L)
                .save();

        CRACKING_RECIPES.builder("transcending_matter")
                .inputItems(GTOItems.UNSTABLE_GAIA_SOUL.asItem())
                .inputFluids(GTOMaterials.Aether, 1000)
                .inputFluids(GTOMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.TranscendingMatter, 100000)
                .outputFluids(GTOMaterials.Aether, 999)
                .EUt(7864320)
                .duration(6000)
                .save();

        ASSEMBLER_RECIPES.builder("integral_framework_uev")
                .inputItems(GTMachines.HULL[GTValues.UEV].asStack())
                .inputItems(TagPrefix.gear, GTOMaterials.Gaiasteel, 4)
                .inputItems(TagPrefix.plate, GTOMaterials.Gaiasteel, 4)
                .inputItems(TagPrefix.cableGtOctal, GTOMaterials.TitanSteel)
                .inputItems(CustomTags.UEV_CIRCUITS, 2)
                .outputItems(GTOBlocks.INTEGRAL_FRAMEWORK_UEV.asStack())
                .inputFluids(GTOMaterials.Ceruclase, 288)
                .EUt(8388608)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("integral_framework_uiv")
                .inputItems(GTMachines.HULL[GTValues.UIV].asStack())
                .inputItems(TagPrefix.gear, GTOMaterials.Alduorite, 4)
                .inputItems(TagPrefix.plate, GTOMaterials.Alduorite, 4)
                .inputItems(TagPrefix.cableGtOctal, GTOMaterials.Adamantine)
                .inputItems(CustomTags.UIV_CIRCUITS, 2)
                .outputItems(GTOBlocks.INTEGRAL_FRAMEWORK_UIV.asStack())
                .inputFluids(GTOMaterials.Haderoth, 288)
                .EUt(33554432)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("integral_framework_uxv")
                .inputItems(GTMachines.HULL[GTValues.UXV].asStack())
                .inputItems(TagPrefix.gear, GTOMaterials.HexaphaseCopper, 4)
                .inputItems(TagPrefix.plate, GTOMaterials.HexaphaseCopper, 4)
                .outputItems(GTOBlocks.INTEGRAL_FRAMEWORK_UXV.asStack())
                .inputItems(TagPrefix.cableGtOctal, GTOMaterials.NaquadriaticTaranium)
                .inputItems(CustomTags.UXV_CIRCUITS, 2)
                .inputFluids(GTOMaterials.ChromaticGlass, 288)
                .duration(100)
                .EUt(134217728)
                .save();

        STELLAR_FORGE_RECIPES.builder("hexaphasecopper_plasma")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputItems(GTOTagPrefix.NANITES, GTMaterials.Copper, 4)
                .inputFluids(GTOMaterials.Haderoth, 2304)
                .inputFluids(GTMaterials.Copper, 2304)
                .outputFluids(GTOMaterials.HexaphaseCopper.getFluid(1000))
                .EUt(33554432)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        ASSEMBLER_RECIPES.builder("wyvern_core")
                .inputItems(GTOBlocks.UIV_WIRELESS_ENERGY_UNIT.asStack(1024))
                .inputItems(GTOBlocks.COMPONENT_ASSEMBLY_LINE_CASING_UIV.asStack(1024))
                .inputItems(CustomTags.UXV_CIRCUITS, 8192)
                .outputItems(GTOItems.WYVERN_CORE.asItem())
                .duration(200)
                .EUt(33554432)
                .save();

        STELLAR_FORGE_RECIPES.builder("enhancement_core")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asStack(64))
                .inputItems(GTOItems.WYVERN_CORE.asStack(64))
                .inputItems(GTOItems.WYVERN_CORE.asStack(64))
                .outputItems("avaritia:enhancement_core")
                .duration(2000)
                .EUt(33554432)
                .runLimit(1)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        ASSEMBLER_RECIPES.builder("ACCELERATOR_MAGNETIC_CONSTRAINED_RAIL_CASING".toLowerCase(Locale.ROOT))
                .inputItems(TagPrefix.frameGt, GTMaterials.NaquadahEnriched)
                .inputItems(GTOBlocks.INTEGRAL_FRAMEWORK_LUV.asStack())
                .inputItems(GTBlocks.SUPERCONDUCTING_COIL.asStack())
                .inputItems(TagPrefix.rodLong, GTMaterials.SamariumMagnetic, 8)
                .inputItems(TagPrefix.rodLong, GTMaterials.AnnealedCopper, 8)
                .inputItems(GTItems.VOLTAGE_COIL_LuV.asStack(4))
                .outputItems(GTOBlocks.ACCELERATOR_MAGNETIC_CONSTRAINED_RAIL_CASING.asStack())
                .inputFluids(GTMaterials.NiobiumTitanium, 3456)
                .EUt(960)
                .duration(200)
                .save();
        ASSEMBLER_RECIPES.builder("ACCELERATOR_uhv_coil".toLowerCase(Locale.ROOT))
                .inputItems(TagPrefix.frameGt, GTMaterials.NaquadahEnriched)
                .inputItems(GTOBlocks.IMPROVED_SUPERCONDUCTOR_COIL.asStack())
                .inputItems(TagPrefix.wireGtHex, GTMaterials.RutheniumTriniumAmericiumNeutronate, 8)
                .inputItems(GTItems.VOLTAGE_COIL_LuV.asStack(4))
                .outputItems(GTOBlocks.ACCELERATOR_ELECTROMAGNETIC_COIL_CONSTRAINT_CASING_UHV.asStack())
                .inputFluids(GTMaterials.NiobiumTitanium, 3456 * 4)
                .EUt(960)
                .duration(200)
                .save();
    }
}

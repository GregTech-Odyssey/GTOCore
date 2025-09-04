package com.gtocore.data.recipe.gtm.misc;

import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials.Color;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

public final class BatteryRecipes {

    public static void init() {
        standardBatteries();
        gemBatteries();
        batteryBlocks();
    }

    private static void standardBatteries() {
        // Tantalum Battery (since it doesn't fit elsewhere)
        ASSEMBLER_RECIPES.recipeBuilder("tantalum_capacitor")
                .inputItems(dust, Tantalum)
                .inputItems(foil, Manganese)
                .inputFluids(Polyethylene.getFluid(L))
                .outputItems(BATTERY_ULV_TANTALUM, 8)
                .duration(30).EUt(4).save();

        // :trol:
        VanillaRecipeHelper.addShapedRecipe("tantalum_capacitor", BATTERY_ULV_TANTALUM.asStack(2),
                " F ", "FDF", "B B",
                'F', new MaterialEntry(foil, Manganese),
                'D', new MaterialEntry(dust, Tantalum),
                'B', new MaterialEntry(bolt, Iron));

        // Battery Hull Recipes

        // LV
        VanillaRecipeHelper.addShapedRecipe("battery_hull_lv", BATTERY_HULL_LV.asStack(),
                "C", "P", "P",
                'C', new MaterialEntry(cableGtSingle, Tin),
                'P', new MaterialEntry(plate, BatteryAlloy));

        ASSEMBLER_RECIPES.recipeBuilder("battery_hull_lv")
                .inputItems(cableGtSingle, Tin)
                .inputItems(plate, BatteryAlloy)
                .inputFluids(Polyethylene.getFluid(L))
                .outputItems(BATTERY_HULL_LV)
                .duration(400).EUt(1).save();

        // MV
        VanillaRecipeHelper.addShapedRecipe("battery_hull_mv", BATTERY_HULL_MV.asStack(),
                "C C", "PPP", "PPP",
                'C', new MaterialEntry(cableGtSingle, Copper),
                'P', new MaterialEntry(plate, BatteryAlloy));

        ASSEMBLER_RECIPES.recipeBuilder("battery_hull_mv_copper")
                .inputItems(cableGtSingle, Copper, 2)
                .inputItems(plate, BatteryAlloy, 3)
                .inputFluids(Polyethylene.getFluid(L * 3))
                .outputItems(BATTERY_HULL_MV)
                .duration(200).EUt(2).save();

        ASSEMBLER_RECIPES.recipeBuilder("battery_hull_mv_annealed")
                .inputItems(cableGtSingle, AnnealedCopper, 2)
                .inputItems(plate, BatteryAlloy, 3)
                .inputFluids(Polyethylene.getFluid(L * 3))
                .outputItems(BATTERY_HULL_MV)
                .duration(200).EUt(2).save();

        // HV
        ASSEMBLER_RECIPES.recipeBuilder("battery_hull_hv")
                .inputItems(cableGtSingle, Gold, 4)
                .inputItems(plate, BatteryAlloy, 9)
                .inputFluids(Polyethylene.getFluid(1296))
                .outputItems(BATTERY_HULL_HV)
                .duration(300).EUt(4).save();

        // EV
        ASSEMBLER_RECIPES.recipeBuilder("battery_hull_ev")
                .inputItems(cableGtSingle, Aluminium, 2)
                .inputItems(plate, RedSteel, 2)
                .inputFluids(Polytetrafluoroethylene.getFluid(L))
                .outputItems(BATTERY_HULL_SMALL_VANADIUM)
                .duration(100).EUt(VA[HV]).save();

        // IV
        ASSEMBLER_RECIPES.recipeBuilder("battery_hull_iv")
                .inputItems(cableGtSingle, Platinum, 2)
                .inputItems(plate, RoseGold, 6)
                .inputFluids(Polytetrafluoroethylene.getFluid(288))
                .outputItems(BATTERY_HULL_MEDIUM_VANADIUM)
                .duration(200).EUt(VA[EV]).save();

        // LuV
        ASSEMBLER_RECIPES.recipeBuilder("battery_hull_luv")
                .inputItems(cableGtSingle, NiobiumTitanium, 2)
                .inputItems(plate, BlueSteel, 18)
                .inputFluids(Polybenzimidazole.getFluid(L))
                .outputItems(BATTERY_HULL_LARGE_VANADIUM)
                .duration(300).EUt(VA[IV]).save();

        // ZPM
        ASSEMBLER_RECIPES.recipeBuilder("battery_hull_zpm")
                .inputItems(cableGtSingle, Naquadah, 2)
                .inputItems(plate, Europium, 6)
                .inputFluids(Polybenzimidazole.getFluid(288))
                .outputItems(BATTERY_HULL_MEDIUM_NAQUADRIA)
                .duration(200).EUt(VA[LuV]).save();

        // UV
        ASSEMBLER_RECIPES.recipeBuilder("battery_hull_uv")
                .inputItems(cableGtSingle, YttriumBariumCuprate, 2)
                .inputItems(plate, Americium, 18)
                .inputFluids(Polybenzimidazole.getFluid(576))
                .outputItems(BATTERY_HULL_LARGE_NAQUADRIA)
                .duration(300).EUt(VA[ZPM]).save();

        // Battery Filling Recipes

        // LV
        CANNER_RECIPES.recipeBuilder("cadmium_battery_lv")
                .inputItems(BATTERY_HULL_LV)
                .inputItems(dust, Cadmium, 2)
                .outputItems(BATTERY_LV_CADMIUM)
                .duration(100).EUt(2).save();

        CANNER_RECIPES.recipeBuilder("lithium_battery_lv")
                .inputItems(BATTERY_HULL_LV)
                .inputItems(dust, Lithium, 2)
                .outputItems(BATTERY_LV_LITHIUM)
                .duration(100).EUt(2).save();

        CANNER_RECIPES.recipeBuilder("sodium_battery_lv")
                .inputItems(BATTERY_HULL_LV)
                .inputItems(dust, Sodium, 2)
                .outputItems(BATTERY_LV_SODIUM)
                .duration(100).EUt(2).save();

        // MV
        CANNER_RECIPES.recipeBuilder("cadmium_battery_mv")
                .inputItems(BATTERY_HULL_MV)
                .inputItems(dust, Cadmium, 8)
                .outputItems(BATTERY_MV_CADMIUM)
                .duration(400).EUt(2).save();

        CANNER_RECIPES.recipeBuilder("lithium_battery_mv")
                .inputItems(BATTERY_HULL_MV)
                .inputItems(dust, Lithium, 8)
                .outputItems(BATTERY_MV_LITHIUM)
                .duration(400).EUt(2).save();

        CANNER_RECIPES.recipeBuilder("sodium_battery_mv")
                .inputItems(BATTERY_HULL_MV)
                .inputItems(dust, Sodium, 8)
                .outputItems(BATTERY_MV_SODIUM)
                .duration(400).EUt(2).save();

        // HV
        CANNER_RECIPES.recipeBuilder("cadmium_battery_hv")
                .inputItems(BATTERY_HULL_HV)
                .inputItems(dust, Cadmium, 16)
                .outputItems(BATTERY_HV_CADMIUM)
                .duration(1600).EUt(2).save();

        CANNER_RECIPES.recipeBuilder("lithium_battery_hv")
                .inputItems(BATTERY_HULL_HV)
                .inputItems(dust, Lithium, 16)
                .outputItems(BATTERY_HV_LITHIUM)
                .duration(1600).EUt(2).save();

        CANNER_RECIPES.recipeBuilder("sodium_battery_hv")
                .inputItems(BATTERY_HULL_HV)
                .inputItems(dust, Sodium, 16)
                .outputItems(BATTERY_HV_SODIUM)
                .duration(1600).EUt(2).save();

        // EV
        CANNER_RECIPES.recipeBuilder("vanadium_battery_ev")
                .inputItems(BATTERY_HULL_SMALL_VANADIUM)
                .inputItems(dust, Vanadium, 2)
                .outputItems(BATTERY_EV_VANADIUM)
                .duration(100).EUt(VA[HV]).save();

        // IV
        CANNER_RECIPES.recipeBuilder("vanadium_battery_iv")
                .inputItems(BATTERY_HULL_MEDIUM_VANADIUM)
                .inputItems(dust, Vanadium, 8)
                .outputItems(BATTERY_IV_VANADIUM)
                .duration(150).EUt(1024).save();

        // LuV
        CANNER_RECIPES.recipeBuilder("vanadium_battery_luv")
                .inputItems(BATTERY_HULL_LARGE_VANADIUM)
                .inputItems(dust, Vanadium, 16)
                .outputItems(BATTERY_LuV_VANADIUM)
                .duration(200).EUt(VA[EV]).save();

        // ZPM
        CANNER_RECIPES.recipeBuilder("naquadria_battery_zpm")
                .inputItems(BATTERY_HULL_MEDIUM_NAQUADRIA)
                .inputItems(dust, Naquadria, 8)
                .outputItems(BATTERY_ZPM_NAQUADRIA)
                .duration(250).EUt(4096).save();

        // UV
        CANNER_RECIPES.recipeBuilder("naquadria_battery_uv")
                .inputItems(BATTERY_HULL_LARGE_NAQUADRIA)
                .inputItems(dust, Naquadria, 16)
                .outputItems(BATTERY_UV_NAQUADRIA)
                .duration(300).EUt(VA[IV]).save();

        EXTRACTOR_RECIPES.recipeBuilder("unpackage_lv_cadmium_battery").inputItems(BATTERY_LV_CADMIUM)
                .outputItems(BATTERY_HULL_LV).save();
        EXTRACTOR_RECIPES.recipeBuilder("unpackage_lv_lithium_battery").inputItems(BATTERY_LV_LITHIUM)
                .outputItems(BATTERY_HULL_LV).save();
        EXTRACTOR_RECIPES.recipeBuilder("unpackage_lv_sodium_battery").inputItems(BATTERY_LV_SODIUM)
                .outputItems(BATTERY_HULL_LV).save();

        EXTRACTOR_RECIPES.recipeBuilder("unpackage_mv_cadmium_battery").inputItems(BATTERY_MV_CADMIUM)
                .outputItems(BATTERY_HULL_MV).save();
        EXTRACTOR_RECIPES.recipeBuilder("unpackage_mv_lithium_battery").inputItems(BATTERY_MV_LITHIUM)
                .outputItems(BATTERY_HULL_MV).save();
        EXTRACTOR_RECIPES.recipeBuilder("unpackage_mv_sodium_battery").inputItems(BATTERY_MV_SODIUM)
                .outputItems(BATTERY_HULL_MV).save();

        EXTRACTOR_RECIPES.recipeBuilder("unpackage_hv_cadmium_battery").inputItems(BATTERY_HV_CADMIUM)
                .outputItems(BATTERY_HULL_HV).save();
        EXTRACTOR_RECIPES.recipeBuilder("unpackage_hv_lithium_battery").inputItems(BATTERY_HV_LITHIUM)
                .outputItems(BATTERY_HULL_HV).save();
        EXTRACTOR_RECIPES.recipeBuilder("unpackage_hv_sodium_battery").inputItems(BATTERY_HV_SODIUM)
                .outputItems(BATTERY_HULL_HV).save();

        EXTRACTOR_RECIPES.recipeBuilder("unpackage_ev_vanadium_battery").inputItems(BATTERY_EV_VANADIUM)
                .outputItems(BATTERY_HULL_SMALL_VANADIUM).save();
        EXTRACTOR_RECIPES.recipeBuilder("unpackage_iv_vanadium_battery").inputItems(BATTERY_IV_VANADIUM)
                .outputItems(BATTERY_HULL_MEDIUM_VANADIUM).save();
        EXTRACTOR_RECIPES.recipeBuilder("unpackage_luv_vanadium_battery").inputItems(BATTERY_LuV_VANADIUM)
                .outputItems(BATTERY_HULL_LARGE_VANADIUM).save();

        EXTRACTOR_RECIPES.recipeBuilder("unpackage_zpm_naquadria_battery").inputItems(BATTERY_ZPM_NAQUADRIA)
                .outputItems(BATTERY_HULL_MEDIUM_NAQUADRIA).save();
        EXTRACTOR_RECIPES.recipeBuilder("unpackage_uv_naquadria_battery").inputItems(BATTERY_UV_NAQUADRIA)
                .outputItems(BATTERY_HULL_LARGE_NAQUADRIA).save();
    }

    private static void gemBatteries() {
        // Energy Crystal
        MIXER_RECIPES.recipeBuilder("energium_dust")
                .inputItems(dust, Redstone, 5)
                .inputItems(dust, Ruby, 4)
                .circuitMeta(1)
                .outputItems(ENERGIUM_DUST, 9)
                .duration(600).EUt(VA[MV]).save();

        AUTOCLAVE_RECIPES.recipeBuilder("energy_crystal_water")
                .inputItems(ENERGIUM_DUST, 9)
                .inputFluids(Water.getFluid(1000))
                .outputItems(ENERGIUM_CRYSTAL)
                .duration(1800).EUt(VA[HV]).save();

        AUTOCLAVE_RECIPES.recipeBuilder("energy_crystal_distilled")
                .inputItems(ENERGIUM_DUST, 9)
                .inputFluids(DistilledWater.getFluid(1000))
                .outputItems(ENERGIUM_CRYSTAL)
                .duration(1200).EUt(320).save();

        AUTOCLAVE_RECIPES.recipeBuilder("energy_crystal_black_steel")
                .inputItems(ENERGIUM_DUST, 9)
                .inputFluids(BlackSteel.getFluid(L << 1))
                .outputItems(ENERGIUM_CRYSTAL)
                .duration(300).EUt(256).save();

        AUTOCLAVE_RECIPES.recipeBuilder("energy_crystal_blue_steel")
                .inputItems(ENERGIUM_DUST, 9)
                .inputFluids(RedSteel.getFluid(L / 2))
                .outputItems(ENERGIUM_CRYSTAL)
                .duration(150).EUt(192).save();

        // Lapotron Crystal
        MIXER_RECIPES.recipeBuilder("lapotron_dust")
                .inputItems(ENERGIUM_DUST, 3)
                .inputItems(dust, Lapis, 2)
                .circuitMeta(2)
                .outputItems(dust, Lapotron, 5)
                .duration(200).EUt(VA[HV]).save();

        AUTOCLAVE_RECIPES.recipeBuilder("lapotron_gem_water")
                .inputItems(dust, Lapotron, 15)
                .inputFluids(Water.getFluid(1000))
                .outputItems(gem, Lapotron)
                .duration(1800).EUt(VA[HV]).save();

        AUTOCLAVE_RECIPES.recipeBuilder("lapotron_gem_distilled")
                .inputItems(dust, Lapotron, 15)
                .inputFluids(DistilledWater.getFluid(1000))
                .outputItems(gem, Lapotron)
                .duration(1200).EUt(320).save();

        AUTOCLAVE_RECIPES.recipeBuilder("lapotron_gem_blue_steel")
                .inputItems(dust, Lapotron, 15)
                .inputFluids(RedSteel.getFluid(L << 1))
                .outputItems(gem, Lapotron)
                .duration(300).EUt(256).save();

        AUTOCLAVE_RECIPES.recipeBuilder("lapotron_gem_red_steel")
                .inputItems(dust, Lapotron, 15)
                .inputFluids(BlueSteel.getFluid(L / 2))
                .outputItems(gem, Lapotron)
                .duration(150).EUt(192).save();

        ASSEMBLER_RECIPES.recipeBuilder("lapotron_crystal")
                .inputItems(gem, Lapotron)
                .inputItems(CustomTags.HV_CIRCUITS, 2)
                .outputItems(LAPOTRON_CRYSTAL)
                .duration(600).EUt(VA[EV]).save();

        // Lapotronic Energy Orb
        LASER_ENGRAVER_RECIPES.recipeBuilder("engraved_lapotron_chip")
                .inputItems(LAPOTRON_CRYSTAL)
                .notConsumable(lens, Color.Blue)
                .outputItems(ENGRAVED_LAPOTRON_CHIP, 3)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(256).EUt(VA[HV]).save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("lapotronic_energy_orb")
                .inputItems(EXTREME_CIRCUIT_BOARD)
                .inputItems(POWER_INTEGRATED_CIRCUIT, 4)
                .inputItems(ENGRAVED_LAPOTRON_CHIP, 24)
                .inputItems(NANO_CENTRAL_PROCESSING_UNIT, 2)
                .inputItems(wireFine, Platinum, 16)
                .inputItems(plate, Platinum, 8)
                .outputItems(ENERGY_LAPOTRONIC_ORB)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(512).EUt(1024).save();
    }

    private static void batteryBlocks() {
        // Empty Tier I
        ASSEMBLER_RECIPES.recipeBuilder("empty_tier_1_battery")
                .inputItems(frameGt, Ultimet)
                .inputItems(plate, Ultimet, 6)
                .inputItems(screw, Ultimet, 24)
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_I)
                .duration(400).EUt(VA[HV]).save();

        // Lapotronic EV
        CANNER_RECIPES.recipeBuilder("ev_lapotronic_battery")
                .inputItems(GTBlocks.BATTERY_EMPTY_TIER_I.asStack(1))
                .inputItems(LAPOTRON_CRYSTAL)
                .outputItems(GTBlocks.BATTERY_LAPOTRONIC_EV)
                .duration(200).EUt(VA[HV]).save();

        PACKER_RECIPES.recipeBuilder("unpackage_ev_lapotronic_battery")
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_EV.asStack(1))
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_I.asStack(1))
                .outputItems(LAPOTRON_CRYSTAL)
                .circuitMeta(2)
                .duration(200).EUt(VA[LV]).save();

        // Lapotronic IV
        CANNER_RECIPES.recipeBuilder("iv_lapotronic_battery")
                .inputItems(GTBlocks.BATTERY_EMPTY_TIER_I.asStack(1))
                .inputItems(ENERGY_LAPOTRONIC_ORB)
                .outputItems(GTBlocks.BATTERY_LAPOTRONIC_IV.asStack(1))
                .duration(400).EUt(VA[HV]).save();

        // Empty Tier II
        ASSEMBLER_RECIPES.recipeBuilder("empty_tier_2_battery")
                .inputItems(frameGt, Ruridit)
                .inputItems(plate, Ruridit, 6)
                .inputItems(screw, Ruridit, 24)
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_II)
                .duration(400).EUt(VA[IV]).save();

        // Lapotronic LuV
        CANNER_RECIPES.recipeBuilder("luv_lapotronic_battery")
                .inputItems(GTBlocks.BATTERY_EMPTY_TIER_II.asStack(1))
                .inputItems(ENERGY_LAPOTRONIC_ORB_CLUSTER)
                .outputItems(GTBlocks.BATTERY_LAPOTRONIC_LuV)
                .duration(200).EUt(VA[EV]).save();

        // Lapotronic ZPM
        CANNER_RECIPES.recipeBuilder("zpm_lapotronic_battery")
                .inputItems(GTBlocks.BATTERY_EMPTY_TIER_II.asStack(1))
                .inputItems(ENERGY_MODULE)
                .outputItems(GTBlocks.BATTERY_LAPOTRONIC_ZPM)
                .duration(400).EUt(VA[EV]).save();

        // Empty Tier III
        ASSEMBLER_RECIPES.recipeBuilder("empty_tier_3_battery")
                .inputItems(frameGt, Neutronium)
                .inputItems(plate, Neutronium, 6)
                .inputItems(screw, Neutronium, 24)
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_III)
                .duration(400).EUt(VA[ZPM]).save();

        // Lapotronic UV
        CANNER_RECIPES.recipeBuilder("uv_lapotronic_battery")
                .inputItems(GTBlocks.BATTERY_EMPTY_TIER_III.asStack(1))
                .inputItems(ENERGY_CLUSTER)
                .outputItems(GTBlocks.BATTERY_LAPOTRONIC_UV)
                .duration(200).EUt(VA[IV]).save();

        // Ultimate UHV
        CANNER_RECIPES.recipeBuilder("uhv_ultimate_battery")
                .inputItems(GTBlocks.BATTERY_EMPTY_TIER_III.asStack(1))
                .inputItems(ULTIMATE_BATTERY)
                .outputItems(GTBlocks.BATTERY_ULTIMATE_UHV)
                .duration(400).EUt(VA[IV]).save();
    }
}

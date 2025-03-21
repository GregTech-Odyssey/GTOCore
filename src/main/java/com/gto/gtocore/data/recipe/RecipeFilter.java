package com.gto.gtocore.data.recipe;

import com.gto.gtocore.utils.RLUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;

import appeng.core.AppEng;
import com.glodblock.github.extendedae.ExtendedAE;
import com.kyanite.deeperdarker.DeeperDarker;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Set;

import static com.gregtechceu.gtceu.api.GTValues.VN;
import static com.gto.gtocore.common.data.GTORecipeTypes.*;
import static com.gto.gtocore.common.data.GTORecipes.SHAPED_FILTER_RECIPES;
import static com.gto.gtocore.common.data.GTORecipes.SHAPELESS_FILTER_RECIPES;

public interface RecipeFilter {

    static void init() {
        SHAPED_FILTER_RECIPES = new ObjectOpenHashSet<>();
        SHAPELESS_FILTER_RECIPES = new ObjectOpenHashSet<>();
        SHAPELESS_FILTER_RECIPES.add(GTCEu.id("coated_board_1x"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("large_plasma_turbine"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("maintenance_hatch_cleaning"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("vacuum_tube"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("iron_bucket"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("steam_alloy_smelter_bronze"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("basic_circuit_board"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("coated_board"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("good_circuit_board"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("resistor_wire"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("resistor_wire_fine"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("resistor_wire_fine_charcoal"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("resistor_wire_carbon"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("resistor_wire_fine_carbon"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("resistor_wire_charcoal"));

        PACKER_RECIPES.addFilter("unpackage_iv_lapotronic_battery");
        PACKER_RECIPES.addFilter("unpackage_luv_lapotronic_battery");
        PACKER_RECIPES.addFilter("unpackage_zpm_lapotronic_battery");
        PACKER_RECIPES.addFilter("unpackage_uv_lapotronic_battery");
        PACKER_RECIPES.addFilter("unpackage_uhv_ultimate_battery");

        MACERATOR_RECIPES.addFilter("macerate_wheat");

        AUTOCLAVE_RECIPES.addFilter("agar");

        ASSEMBLER_RECIPES.addFilter("casing_ulv");
        ASSEMBLER_RECIPES.addFilter("casing_lv");
        ASSEMBLER_RECIPES.addFilter("casing_mv");
        ASSEMBLER_RECIPES.addFilter("casing_hv");
        ASSEMBLER_RECIPES.addFilter("casing_ev");
        ASSEMBLER_RECIPES.addFilter("casing_iv");
        ASSEMBLER_RECIPES.addFilter("casing_luv");
        ASSEMBLER_RECIPES.addFilter("casing_zpm");
        ASSEMBLER_RECIPES.addFilter("casing_uv");
        ASSEMBLER_RECIPES.addFilter("casing_uhv");
        ASSEMBLER_RECIPES.addFilter("hull_ulv");
        ASSEMBLER_RECIPES.addFilter("hull_lv");
        ASSEMBLER_RECIPES.addFilter("hull_mv");
        ASSEMBLER_RECIPES.addFilter("hull_mv_annealed");
        ASSEMBLER_RECIPES.addFilter("hull_hv");
        ASSEMBLER_RECIPES.addFilter("hull_ev");
        ASSEMBLER_RECIPES.addFilter("hull_iv");
        ASSEMBLER_RECIPES.addFilter("hull_luv");
        ASSEMBLER_RECIPES.addFilter("hull_zpm");
        ASSEMBLER_RECIPES.addFilter("hull_uv");
        ASSEMBLER_RECIPES.addFilter("hull_uhv");
        ASSEMBLER_RECIPES.addFilter("wool_from_string");
        ASSEMBLER_RECIPES.addFilter("diode_glass");
        ASSEMBLER_RECIPES.addFilter("diode_glass_annealed");
        ASSEMBLER_RECIPES.addFilter("basic_circuit_board");
        ASSEMBLER_RECIPES.addFilter("phenolic_board");
        ASSEMBLER_RECIPES.addFilter("resistor_carbon");
        ASSEMBLER_RECIPES.addFilter("resistor_coal_annealed");
        ASSEMBLER_RECIPES.addFilter("resistor_coal");
        ASSEMBLER_RECIPES.addFilter("resistor_charcoal");
        ASSEMBLER_RECIPES.addFilter("resistor_charcoal_annealed");
        ASSEMBLER_RECIPES.addFilter("vacuum_tube_plain");
        ASSEMBLER_RECIPES.addFilter("vacuum_tube_red_alloy");
        ASSEMBLER_RECIPES.addFilter("vacuum_tube_red_alloy_annealed");

        ASSEMBLY_LINE_RECIPES.addFilter("dynamo_hatch_uhv");
        ASSEMBLY_LINE_RECIPES.addFilter("energy_hatch_uhv");
        ASSEMBLY_LINE_RECIPES.addFilter("energy_cluster");

        CIRCUIT_ASSEMBLER_RECIPES.addFilter("integrated_circuit_hv");
        CIRCUIT_ASSEMBLER_RECIPES.addFilter("electronic_circuit_lv");
        CIRCUIT_ASSEMBLER_RECIPES.addFilter("processor_assembly_hv");
        CIRCUIT_ASSEMBLER_RECIPES.addFilter("wetware_processor_luv_soc");
        CIRCUIT_ASSEMBLER_RECIPES.addFilter("wetware_board");

        BENDER_RECIPES.addFilter("bucket");

        MIXER_RECIPES.addFilter("rhodium_plated_palladium");
        MIXER_RECIPES.addFilter("ender_pearl_dust");
        MIXER_RECIPES.addFilter("rocket_fuel_from_dinitrogen_tetroxide");
        MIXER_RECIPES.addFilter("graphene");
        MIXER_RECIPES.addFilter("yttrium_barium_cuprate");
        MIXER_RECIPES.addFilter("raw_growth_medium");
        MIXER_RECIPES.addFilter("tantalum_carbide");
        MIXER_RECIPES.addFilter("pcb_coolant");

        CHEMICAL_RECIPES.addFilter("stem_cells");
        CHEMICAL_RECIPES.addFilter("formaldehyde");
        CHEMICAL_RECIPES.addFilter("ptfe_from_air");
        CHEMICAL_RECIPES.addFilter("ptfe_from_oxygen");
        CHEMICAL_RECIPES.addFilter("raw_sbr_from_air");
        CHEMICAL_RECIPES.addFilter("raw_sbr_from_oxygen");
        CHEMICAL_RECIPES.addFilter("polyvinyl_butyral");
        CHEMICAL_RECIPES.addFilter("polybenzimidazole");
        CHEMICAL_RECIPES.addFilter("polyphenylene_sulfide_from_air");
        CHEMICAL_RECIPES.addFilter("polyphenylene_sulfide_from_oxygen");
        CHEMICAL_RECIPES.addFilter("pva_from_air");
        CHEMICAL_RECIPES.addFilter("pva_from_oxygen");
        CHEMICAL_RECIPES.addFilter("pva_from_tetrachloride_air");
        CHEMICAL_RECIPES.addFilter("pva_from_tetrachloride_oxygen");
        CHEMICAL_RECIPES.addFilter("epoxy_from_bisphenol_a");
        CHEMICAL_RECIPES.addFilter("advanced_circuit_board_persulfate");
        CHEMICAL_RECIPES.addFilter("advanced_circuit_board_iron3");
        CHEMICAL_RECIPES.addFilter("extreme_circuit_board_iron3");
        CHEMICAL_RECIPES.addFilter("extreme_circuit_board_persulfate");
        CHEMICAL_RECIPES.addFilter("elite_circuit_board_persulfate");
        CHEMICAL_RECIPES.addFilter("elite_circuit_board_iron3");
        CHEMICAL_RECIPES.addFilter("wetware_circuit_board_persulfate");
        CHEMICAL_RECIPES.addFilter("wetware_circuit_board_iron3");
        CHEMICAL_RECIPES.addFilter("elite_circuit_board_persulfate");
        CHEMICAL_RECIPES.addFilter("elite_circuit_board_iron3");
        CHEMICAL_RECIPES.addFilter("magnesia_from_magnesite");
        CHEMICAL_RECIPES.addFilter("polyvinyl_chloride_from_air");
        CHEMICAL_RECIPES.addFilter("nano_cpu_wafer");
        CHEMICAL_RECIPES.addFilter("qbit_cpu_wafer_radon");
        CHEMICAL_RECIPES.addFilter("qbit_cpu_wafer_quantum_eye");
        CHEMICAL_RECIPES.addFilter("plastic_circuit_board_persulfate");
        CHEMICAL_RECIPES.addFilter("plastic_circuit_board_iron3");
        CHEMICAL_RECIPES.addFilter("polyethylene_from_oxygen");
        CHEMICAL_RECIPES.addFilter("polyethylene_from_air");
        CHEMICAL_RECIPES.addFilter("polyvinyl_chloride_from_oxygen");
        CHEMICAL_RECIPES.addFilter("methanol_from_monoxide");
        CHEMICAL_RECIPES.addFilter("hypochlorous_acid_mercury");
        CHEMICAL_RECIPES.addFilter("hypochlorous_acid");
        CHEMICAL_RECIPES.addFilter("benzene_from_biphenyl");
        CHEMICAL_RECIPES.addFilter("polychlorinated_biphenyl");
        CHEMICAL_RECIPES.addFilter("calcium_hydroxide");
        CHEMICAL_RECIPES.addFilter("calcite_from_quicklime");
        CHEMICAL_RECIPES.addFilter("ethylene_from_ethanol");
        CHEMICAL_RECIPES.addFilter("dimethylchlorosilane_from_chloromethane");
        CHEMICAL_RECIPES.addFilter("vinyl_chloride_from_ethane");
        CHEMICAL_RECIPES.addFilter("styrene_from_ethylbenzene");
        CHEMICAL_RECIPES.addFilter("soda_ash_from_carbon_dioxide");
        CHEMICAL_RECIPES.addFilter("formic_acid");

        LARGE_CHEMICAL_RECIPES.addFilter("epoxy_shortcut");
        LARGE_CHEMICAL_RECIPES.addFilter("polyethylene_from_tetrachloride_air");
        LARGE_CHEMICAL_RECIPES.addFilter("polyethylene_from_tetrachloride_oxygen");
        LARGE_CHEMICAL_RECIPES.addFilter("polyvinyl_chloride_from_tetrachloride_air");
        LARGE_CHEMICAL_RECIPES.addFilter("polyvinyl_chloride_from_tetrachloride_oxygen");
        LARGE_CHEMICAL_RECIPES.addFilter("ptfe_from_tetrachloride_air");
        LARGE_CHEMICAL_RECIPES.addFilter("ptfe_from_tetrachloride_oxygen");
        LARGE_CHEMICAL_RECIPES.addFilter("iron_2_chloride");

        CHEMICAL_BATH_RECIPES.addFilter("silicon_cool_down_distilled_water");
        CHEMICAL_BATH_RECIPES.addFilter("kanthal_cool_down_distilled_water");
        CHEMICAL_BATH_RECIPES.addFilter("red_steel_cool_down_distilled_water");
        CHEMICAL_BATH_RECIPES.addFilter("black_steel_cool_down_distilled_water");
        CHEMICAL_BATH_RECIPES.addFilter("blue_steel_cool_down_distilled_water");
        CHEMICAL_BATH_RECIPES.addFilter("silicon_cool_down");
        CHEMICAL_BATH_RECIPES.addFilter("kanthal_cool_down");
        CHEMICAL_BATH_RECIPES.addFilter("black_steel_cool_down");
        CHEMICAL_BATH_RECIPES.addFilter("red_steel_cool_down");
        CHEMICAL_BATH_RECIPES.addFilter("blue_steel_cool_down");
        CHEMICAL_BATH_RECIPES.addFilter("quantum_star");
        CHEMICAL_BATH_RECIPES.addFilter("quantum_eye");

        PRIMITIVE_BLAST_FURNACE_RECIPES.addFilter("steel_from_coal_block");
        PRIMITIVE_BLAST_FURNACE_RECIPES.addFilter("steel_from_charcoal_block");
        PRIMITIVE_BLAST_FURNACE_RECIPES.addFilter("steel_from_coke_block");
        PRIMITIVE_BLAST_FURNACE_RECIPES.addFilter("steel_from_coal_block_wrought");
        PRIMITIVE_BLAST_FURNACE_RECIPES.addFilter("steel_from_charcoal_block_wrought");
        PRIMITIVE_BLAST_FURNACE_RECIPES.addFilter("steel_from_coke_block_wrought");

        BLAST_RECIPES.addFilter("polycaprolactam");
        BLAST_RECIPES.addFilter("pyrite_metallurgy");
        BLAST_RECIPES.addFilter("aluminium_from_ruby_dust");
        BLAST_RECIPES.addFilter("aluminium_from_ruby_gem");
        BLAST_RECIPES.addFilter("aluminium_from_sapphire_gem");
        BLAST_RECIPES.addFilter("stibnite_metallurgy");
        BLAST_RECIPES.addFilter("soda_ash_from_calcite");
        BLAST_RECIPES.addFilter("pentlandite_metallurgy");
        BLAST_RECIPES.addFilter("aluminium_from_green_sapphire_gem");
        BLAST_RECIPES.addFilter("rutile_from_ilmenite");
        BLAST_RECIPES.addFilter("chalcopyrite_metallurgy");
        BLAST_RECIPES.addFilter("cobaltite_metallurgy");
        BLAST_RECIPES.addFilter("tetrahedrite_metallurgy");
        BLAST_RECIPES.addFilter("galena_metallurgy");
        BLAST_RECIPES.addFilter("sphalerite_metallurgy");
        BLAST_RECIPES.addFilter("aluminium_from_green_sapphire_dust");
        BLAST_RECIPES.addFilter("blast_silicon_dioxide");
        BLAST_RECIPES.addFilter("aluminium_from_sapphire_dust");
        BLAST_RECIPES.addFilter("titanium_from_tetrachloride");
        BLAST_RECIPES.addFilter("silicon_boule");
        BLAST_RECIPES.addFilter("phosphorus_boule");
        BLAST_RECIPES.addFilter("naquadah_boule");
        BLAST_RECIPES.addFilter("neutronium_boule");
        BLAST_RECIPES.addFilter("engraved_crystal_chip_from_emerald");
        BLAST_RECIPES.addFilter("engraved_crystal_chip_from_olivine");

        ARC_FURNACE_RECIPES.addFilter("tempered_glass");

        FORMING_PRESS_RECIPES.addFilter("credit_cupronickel");

        EXTRUDER_RECIPES.addFilter("nan_certificate");

        CENTRIFUGE_RECIPES.addFilter("rare_earth_separation");

        ELECTROLYZER_RECIPES.addFilter("bone_meal_electrolysis");

        AUTOCLAVE_RECIPES.addFilter("gravi_star");

        LASER_ENGRAVER_RECIPES.addFilter("crystal_cpu");

        FUSION_RECIPES.addFilter("americium_and_naquadria_to_neutronium_plasma");
        FUSION_RECIPES.addFilter("plutonium_241_and_hydrogen_gas_to_americium_plasma");

        Material[] fluidMap = { GTMaterials.Glue, GTMaterials.Polyethylene,
                GTMaterials.Polytetrafluoroethylene, GTMaterials.Polybenzimidazole };

        for (var machine : GTMachines.DUAL_IMPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                ASSEMBLER_RECIPES.addFilter("dual_import_bus_" + VN[tier].toLowerCase() + "_" + fluidMap[j].getName());
            }
        }

        for (var machine : GTMachines.DUAL_EXPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                ASSEMBLER_RECIPES.addFilter("dual_export_bus_" + VN[tier].toLowerCase() + "_" + fluidMap[j].getName());
            }
        }
    }

    static void initJsonFilter(Set<ResourceLocation> filters) {
        String[] ore1 = new String[] { "coal", "redstone", "emerald", "diamond" };
        String[] ore2 = new String[] { "iron", "copper", "gold" };
        String[] ore3 = new String[] { "desh", "ostrum", "calorite" };

        for (String o : ore1) {
            filters.add(RLUtils.mc(o + "_from_smelting_" + o + "_ore"));
            filters.add(RLUtils.mc(o + "_from_smelting_deepslate_" + o + "_ore"));
            filters.add(RLUtils.mc(o + "_from_blasting_" + o + "_ore"));
            filters.add(RLUtils.mc(o + "_from_blasting_deepslate_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_from_smelting_sculk_stone_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_from_blasting_sculk_stone_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_from_smelting_gloomslate_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_from_blasting_gloomslate_" + o + "_ore"));
        }
        for (String o : ore2) {
            filters.add(RLUtils.mc(o + "_ingot_from_smelting_" + o + "_ore"));
            filters.add(RLUtils.mc(o + "_ingot_from_smelting_deepslate_" + o + "_ore"));
            filters.add(RLUtils.mc(o + "_ingot_from_blasting_" + o + "_ore"));
            filters.add(RLUtils.mc(o + "_ingot_from_blasting_deepslate_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_ingot_from_smelting_sculk_stone_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_ingot_from_blasting_sculk_stone_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_ingot_from_smelting_gloomslate_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_ingot_from_blasting_gloomslate_" + o + "_ore"));
        }
        for (String o : ore3) {
            filters.add(RLUtils.ad("smelting/" + o + "_ingot_from_smelting_deepslate_" + o + "_ore"));
            filters.add(RLUtils.ad("blasting/" + o + "_ingot_from_blasting_deepslate_" + o + "_ore"));
            filters.add(RLUtils.ad("smelting/" + o + "_ingot_from_smelting_raw_" + o));
            filters.add(RLUtils.ad("blasting/" + o + "_ingot_from_blasting_raw_" + o));
        }
        filters.add(RLUtils.mc("gold_ingot_from_blasting_nether_gold_ore"));
        filters.add(RLUtils.mc("gold_ingot_from_smelting_nether_gold_ore"));
        filters.add(RLUtils.mc("lapis_lazuli_from_smelting_lapis_ore"));
        filters.add(RLUtils.mc("lapis_lazuli_from_smelting_deepslate_lapis_ore"));
        filters.add(RLUtils.mc("lapis_lazuli_from_blasting_lapis_ore"));
        filters.add(RLUtils.mc("lapis_lazuli_from_blasting_deepslate_lapis_ore"));
        filters.add(DeeperDarker.rl("lapis_lazuli_from_smelting_sculk_stone_lapis_ore"));
        filters.add(DeeperDarker.rl("lapis_lazuli_from_blasting_sculk_stone_lapis_ore"));
        filters.add(DeeperDarker.rl("lapis_lazuli_from_smelting_gloomslate_lapis_ore"));
        filters.add(DeeperDarker.rl("lapis_lazuli_from_blasting_gloomslate_lapis_ore"));
        filters.add(RLUtils.ad("smelting/desh_ingot_from_smelting_moon_desh_ore"));
        filters.add(RLUtils.ad("blasting/desh_ingot_from_blasting_moon_desh_ore"));
        filters.add(RLUtils.ad("smelting/ostrum_ingot_from_smelting_mars_ostrum_ore"));
        filters.add(RLUtils.ad("blasting/ostrum_ingot_from_blasting_mars_ostrum_ore"));
        filters.add(RLUtils.ad("smelting/calorite_ingot_from_smelting_venus_calorite_ore"));
        filters.add(RLUtils.ad("blasting/calorite_ingot_from_blasting_venus_calorite_ore"));
        filters.add(RLUtils.ad("smelting/iron_ingot_from_smelting_moon_iron_ore"));
        filters.add(RLUtils.ad("blasting/iron_ingot_from_blasting_moon_iron_ore"));
        filters.add(RLUtils.ad("smelting/ice_shard_from_smelting_moon_ice_shard_ore"));
        filters.add(RLUtils.ad("blasting/ice_shard_from_blasting_moon_ice_shard_ore"));
        filters.add(RLUtils.ad("smelting/ice_shard_from_smelting_deepslate_ice_shard_ore"));
        filters.add(RLUtils.ad("blasting/ice_shard_from_blasting_deepslate_ice_shard_ore"));
        filters.add(RLUtils.ad("smelting/iron_ingot_from_smelting_mars_iron_ore"));
        filters.add(RLUtils.ad("blasting/iron_ingot_from_blasting_mars_iron_ore"));
        filters.add(RLUtils.ad("smelting/diamond_from_smelting_mars_diamond_ore"));
        filters.add(RLUtils.ad("blasting/diamond_from_blasting_mars_diamond_ore"));
        filters.add(RLUtils.ad("smelting/ice_shard_from_smelting_mars_ice_shard_ore"));
        filters.add(RLUtils.ad("blasting/ice_shard_from_blasting_mars_ice_shard_ore"));
        filters.add(RLUtils.ad("smelting/coal_from_smelting_venus_coal_ore"));
        filters.add(RLUtils.ad("blasting/coal_from_blasting_venus_coal_ore"));
        filters.add(RLUtils.ad("smelting/gold_ingot_from_smelting_venus_gold_ore"));
        filters.add(RLUtils.ad("blasting/gold_ingot_from_blasting_venus_gold_ore"));
        filters.add(RLUtils.ad("smelting/diamond_from_smelting_venus_diamond_ore"));
        filters.add(RLUtils.ad("blasting/diamond_from_blasting_venus_diamond_ore"));
        filters.add(RLUtils.ad("smelting/iron_ingot_from_smelting_mercury_iron_ore"));
        filters.add(RLUtils.ad("blasting/iron_ingot_from_blasting_mercury_iron_ore"));
        filters.add(RLUtils.ad("smelting/ice_shard_from_smelting_glacio_ice_shard_ore"));
        filters.add(RLUtils.ad("blasting/ice_shard_from_blasting_glacio_ice_shard_ore"));
        filters.add(RLUtils.ad("smelting/coal_from_smelting_glacio_coal_ore"));
        filters.add(RLUtils.ad("blasting/coal_from_blasting_glacio_coal_ore"));
        filters.add(RLUtils.ad("smelting/copper_ingot_from_smelting_glacio_copper_ore"));
        filters.add(RLUtils.ad("blasting/copper_ingot_from_blasting_glacio_copper_ore"));
        filters.add(RLUtils.ad("smelting/iron_ingot_from_smelting_glacio_iron_ore"));
        filters.add(RLUtils.ad("blasting/iron_ingot_from_blasting_glacio_iron_ore"));
        filters.add(RLUtils.ad("smelting/lapis_lazuli_from_smelting_glacio_lapis_ore"));
        filters.add(RLUtils.ad("blasting/lapis_lazuli_from_blasting_glacio_lapis_ore"));

        filters.add(new ResourceLocation("torchmaster", "frozen_pearl"));

        filters.add(new ResourceLocation("mythicbotany", "alfsteel_block_decompress"));
        filters.add(new ResourceLocation("mythicbotany", "alfsteel_nugget_compress"));
        filters.add(new ResourceLocation("mythicbotany", "alfsteel_ingot_compress"));
        filters.add(new ResourceLocation("mythicbotany", "alfsteel_ingot_decompress"));
        filters.add(RLUtils.bot("mana_infusion/mana_diamond_block"));
        filters.add(RLUtils.bot("mana_infusion/manasteel_block"));
        filters.add(RLUtils.bot("conversions/manasteel_block_deconstruct"));
        filters.add(RLUtils.bot("conversions/manasteel_from_nuggets"));
        filters.add(RLUtils.bot("conversions/manasteel_to_nuggets"));
        filters.add(RLUtils.bot("manasteel_block"));
        filters.add(RLUtils.bot("conversions/terrasteel_block_deconstruct"));
        filters.add(RLUtils.bot("conversions/terrasteel_from_nuggets"));
        filters.add(RLUtils.bot("conversions/terrasteel_to_nuggets"));
        filters.add(RLUtils.bot("terrasteel_block"));
        filters.add(RLUtils.bot("conversions/elementium_block_deconstruct"));
        filters.add(RLUtils.bot("conversions/elementium_from_nuggets"));
        filters.add(RLUtils.bot("conversions/elementium_to_nuggets"));
        filters.add(RLUtils.bot("elementium_block"));
        filters.add(RLUtils.bot("conversions/manadiamond_block_deconstruct"));
        filters.add(RLUtils.bot("mana_diamond_block"));
        filters.add(RLUtils.bot("dragonstone_block"));
        filters.add(RLUtils.bot("conversions/dragonstone_block_deconstruct"));
        filters.add(RLUtils.bot("dye_white"));
        filters.add(RLUtils.bot("dye_light_gray"));
        filters.add(RLUtils.bot("dye_gray"));
        filters.add(RLUtils.bot("dye_black"));
        filters.add(RLUtils.bot("dye_brown"));
        filters.add(RLUtils.bot("dye_red"));
        filters.add(RLUtils.bot("dye_orange"));
        filters.add(RLUtils.bot("dye_yellow"));
        filters.add(RLUtils.bot("dye_lime"));
        filters.add(RLUtils.bot("dye_green"));
        filters.add(RLUtils.bot("dye_cyan"));
        filters.add(RLUtils.bot("dye_light_blue"));
        filters.add(RLUtils.bot("dye_blue"));
        filters.add(RLUtils.bot("dye_purple"));
        filters.add(RLUtils.bot("dye_magenta"));
        filters.add(RLUtils.bot("dye_pink"));

        filters.add(DeeperDarker.rl("reinforced_echo_shard"));
        filters.add(DeeperDarker.rl("resonarium_shovel_smithing"));
        filters.add(DeeperDarker.rl("resonarium_pickaxe_smithing"));
        filters.add(DeeperDarker.rl("resonarium_axe_smithing"));
        filters.add(DeeperDarker.rl("resonarium_hoe_smithing"));
        filters.add(DeeperDarker.rl("resonarium_sword_smithing"));
        filters.add(DeeperDarker.rl("resonarium_helmet_smithing"));
        filters.add(DeeperDarker.rl("resonarium_chestplate_smithing"));
        filters.add(DeeperDarker.rl("resonarium_leggings_smithing"));
        filters.add(DeeperDarker.rl("resonarium_boots_smithing"));

        filters.add(RLUtils.mc("wooden_shovel"));
        filters.add(RLUtils.mc("wooden_pickaxe"));
        filters.add(RLUtils.mc("wooden_axe"));
        filters.add(RLUtils.mc("wooden_hoe"));
        filters.add(RLUtils.mc("wooden_sword"));
        filters.add(RLUtils.mc("stone_shovel"));
        filters.add(RLUtils.mc("stone_pickaxe"));
        filters.add(RLUtils.mc("stone_axe"));
        filters.add(RLUtils.mc("stone_hoe"));
        filters.add(RLUtils.mc("stone_sword"));
        filters.add(RLUtils.mc("quartz"));
        filters.add(RLUtils.mc("quartz_from_blasting"));
        filters.add(RLUtils.mc("hay_block"));
        filters.add(RLUtils.mc("netherite_ingot"));
        filters.add(RLUtils.mc("netherite_scrap"));
        filters.add(RLUtils.mc("netherite_scrap_from_blasting"));
        filters.add(RLUtils.mc("infinity_nugget"));
        filters.add(RLUtils.mc("infinity_ingot"));
        filters.add(RLUtils.mc("infinity_ingot_from_infinity_nugget"));
        filters.add(RLUtils.mc("infinity_block_from_infinity_ingot"));
        filters.add(RLUtils.mc("crystal_matrix_ingot"));
        filters.add(RLUtils.mc("double_compressed_crafting_table"));
        filters.add(RLUtils.mc("compressed_crafting_table"));
        filters.add(RLUtils.mc("crystal_matrix"));
        filters.add(RLUtils.mc("neutron_pile"));
        filters.add(RLUtils.mc("neutron_pile_from_ingots"));
        filters.add(RLUtils.mc("neutron_ingot_from_nuggets"));
        filters.add(RLUtils.mc("neutron_ingot_from_neutron_block"));
        filters.add(RLUtils.mc("neutron_nugget"));
        filters.add(RLUtils.mc("neutron"));
        filters.add(RLUtils.mc("diamond_lattice_block"));
        filters.add(RLUtils.mc("diamond_lattice"));

        filters.add(RLUtils.avaritia("infinity_catalyst"));
        filters.add(RLUtils.avaritia("crystal_matrix_ingot"));
        filters.add(RLUtils.avaritia("diamond_lattice"));

        filters.add(ExtendedAE.id("fishbig"));

        filters.add(RLUtils.ad("refining/fuel_from_refining_oil"));
        filters.add(RLUtils.ad("oxygen_loading/oxygen_from_oxygen_loading_oxygen"));
        filters.add(RLUtils.ad("oxygen_loading/oxygen_from_oxygen_loading_water"));
        filters.add(RLUtils.ad("cryo_freezing/cryo_fuel_from_cryo_freezing_blue_ice"));
        filters.add(RLUtils.ad("cryo_freezing/cryo_fuel_from_cryo_freezing_ice_shard"));
        filters.add(RLUtils.ad("cryo_freezing/cryo_fuel_from_cryo_freezing_ice"));
        filters.add(RLUtils.ad("cryo_freezing/cryo_fuel_from_cryo_freezing_packed_ice"));
        filters.add(RLUtils.ad("compressing/calorite_plate_from_compressing_calorite_blocks"));
        filters.add(RLUtils.ad("compressing/calorite_plate_from_compressing_calorite_ingots"));
        filters.add(RLUtils.ad("compressing/desh_plate_from_compressing_desh_blocks"));
        filters.add(RLUtils.ad("compressing/desh_plate_from_compressing_desh_ingots"));
        filters.add(RLUtils.ad("compressing/iron_plate_from_compressing_iron_block"));
        filters.add(RLUtils.ad("compressing/iron_plate_from_compressing_iron_ingot"));
        filters.add(RLUtils.ad("compressing/ostrum_plate_from_compressing_ostrum_blocks"));
        filters.add(RLUtils.ad("compressing/ostrum_plate_from_compressing_ostrum_ingots"));
        filters.add(RLUtils.ad("compressing/steel_plate_from_compressing_steel_blocks"));
        filters.add(RLUtils.ad("compressing/steel_plate_from_compressing_steel_ingots"));
        filters.add(RLUtils.ad("alloying/steel_ingot_from_alloying_iron_ingot_and_coals"));
        filters.add(RLUtils.ad("nasa_workbench/tier_1_rocket_from_nasa_workbench"));
        filters.add(RLUtils.ad("nasa_workbench/tier_2_rocket_from_nasa_workbench"));
        filters.add(RLUtils.ad("nasa_workbench/tier_3_rocket_from_nasa_workbench"));
        filters.add(RLUtils.ad("nasa_workbench/tier_4_rocket_from_nasa_workbench"));
        filters.add(new ResourceLocation("ad_astra_rocketed", "nasa_workbench/default/tier_5_rocket_from_nasa_workbench"));
        filters.add(new ResourceLocation("ad_astra_rocketed", "nasa_workbench/default/tier_6_rocket_from_nasa_workbench"));
        filters.add(new ResourceLocation("ad_astra_rocketed", "nasa_workbench/default/tier_7_rocket_from_nasa_workbench"));
        filters.add(RLUtils.ad("compressor"));
        filters.add(RLUtils.ad("steel_block"));
        filters.add(RLUtils.ad("steel_ingot_from_steel_block"));
        filters.add(RLUtils.ad("steel_nugget"));
        filters.add(RLUtils.ad("energizer"));
        filters.add(RLUtils.ad("steel_cable"));
        filters.add(RLUtils.ad("steel_rod"));
        filters.add(RLUtils.ad("iron_rod"));
        filters.add(RLUtils.ad("desh_block"));
        filters.add(RLUtils.ad("desh_ingot"));
        filters.add(RLUtils.ad("desh_nugget"));
        filters.add(RLUtils.ad("desh_ingot_from_desh_block"));
        filters.add(RLUtils.ad("desh_cable"));
        filters.add(RLUtils.ad("ostrum_block"));
        filters.add(RLUtils.ad("ostrum_ingot"));
        filters.add(RLUtils.ad("ostrum_nugget"));
        filters.add(RLUtils.ad("ostrum_ingot_from_ostrum_block"));
        filters.add(RLUtils.ad("calorite_block"));
        filters.add(RLUtils.ad("calorite_ingot"));
        filters.add(RLUtils.ad("calorite_nugget"));
        filters.add(RLUtils.ad("calorite_ingot_from_calorite_block"));
        filters.add(RLUtils.ad("cable_duct"));
        filters.add(RLUtils.ad("steel_ingot"));
        filters.add(RLUtils.ad("nasa_workbench"));
        filters.add(RLUtils.ad("compressor"));
        filters.add(RLUtils.ad("coal_generator"));
        filters.add(RLUtils.ad("etrionic_blast_furnace"));
        filters.add(RLUtils.ad("fuel_refinery"));
        filters.add(RLUtils.ad("solar_panel"));
        filters.add(RLUtils.ad("water_pump"));
        filters.add(RLUtils.ad("cryo_freezer"));
        filters.add(RLUtils.ad("fan"));
        filters.add(RLUtils.ad("engine_frame"));
        filters.add(RLUtils.ad("steel_engine"));
        filters.add(RLUtils.ad("desh_engine"));
        filters.add(RLUtils.ad("ostrum_engine"));
        filters.add(RLUtils.ad("calorite_engine"));
        filters.add(RLUtils.ad("calorite_tank"));
        filters.add(RLUtils.ad("ostrum_tank"));
        filters.add(RLUtils.ad("desh_tank"));
        filters.add(RLUtils.ad("steel_tank"));
        filters.add(RLUtils.ad("rocket_fin"));
        filters.add(RLUtils.ad("rocket_nose_cone"));

        filters.add(RLUtils.eio("darksteel_upgrade"));
        filters.add(RLUtils.eio("iron_ingot_from_blasting"));
        filters.add(RLUtils.eio("iron_ingot_from_smelting"));
        filters.add(RLUtils.eio("gold_ingot_from_blasting"));
        filters.add(RLUtils.eio("gold_ingot_from_smelting"));
        filters.add(RLUtils.eio("copper_ingot_from_blasting"));
        filters.add(RLUtils.eio("copper_ingot_from_smelting"));
        filters.add(RLUtils.eio("copper_alloy_block"));
        filters.add(RLUtils.eio("copper_alloy_ingot"));
        filters.add(RLUtils.eio("copper_alloy_nugget"));
        filters.add(RLUtils.eio("copper_alloy_nugget_to_ingot"));
        filters.add(RLUtils.eio("energetic_alloy_block"));
        filters.add(RLUtils.eio("energetic_alloy_ingot"));
        filters.add(RLUtils.eio("energetic_alloy_nugget"));
        filters.add(RLUtils.eio("energetic_alloy_nugget_to_ingot"));
        filters.add(RLUtils.eio("vibrant_alloy_block"));
        filters.add(RLUtils.eio("vibrant_alloy_ingot"));
        filters.add(RLUtils.eio("vibrant_alloy_nugget"));
        filters.add(RLUtils.eio("vibrant_alloy_nugget_to_ingot"));
        filters.add(RLUtils.eio("redstone_alloy_block"));
        filters.add(RLUtils.eio("redstone_alloy_ingot"));
        filters.add(RLUtils.eio("redstone_alloy_nugget"));
        filters.add(RLUtils.eio("redstone_alloy_nugget_to_ingot"));
        filters.add(RLUtils.eio("conductive_alloy_block"));
        filters.add(RLUtils.eio("conductive_alloy_ingot"));
        filters.add(RLUtils.eio("conductive_alloy_nugget"));
        filters.add(RLUtils.eio("conductive_alloy_nugget_to_ingot"));
        filters.add(RLUtils.eio("pulsating_alloy_block"));
        filters.add(RLUtils.eio("pulsating_alloy_ingot"));
        filters.add(RLUtils.eio("pulsating_alloy_nugget"));
        filters.add(RLUtils.eio("pulsating_alloy_nugget_to_ingot"));
        filters.add(RLUtils.eio("dark_steel_block"));
        filters.add(RLUtils.eio("dark_steel_ingot"));
        filters.add(RLUtils.eio("dark_steel_nugget"));
        filters.add(RLUtils.eio("dark_steel_nugget_to_ingot"));
        filters.add(RLUtils.eio("soularium_block"));
        filters.add(RLUtils.eio("soularium_ingot"));
        filters.add(RLUtils.eio("soularium_nugget"));
        filters.add(RLUtils.eio("soularium_nugget_to_ingot"));
        filters.add(RLUtils.eio("end_steel_block"));
        filters.add(RLUtils.eio("end_steel_ingot"));
        filters.add(RLUtils.eio("end_steel_nugget"));
        filters.add(RLUtils.eio("end_steel_nugget_to_ingot"));
        filters.add(RLUtils.eio("wood_gear_corner"));
        filters.add(RLUtils.eio("wood_gear"));
        filters.add(RLUtils.eio("iron_gear"));
        filters.add(RLUtils.eio("energized_gear"));
        filters.add(RLUtils.eio("vibrant_gear"));
        filters.add(RLUtils.eio("dark_bimetal_gear"));
        filters.add(RLUtils.eio("pulsating_crystal"));
        filters.add(RLUtils.eio("vibrant_crystal"));
        filters.add(RLUtils.eio("stick"));
        filters.add(RLUtils.eio("sag_milling/ender_crystal"));
        filters.add(RLUtils.eio("sag_milling/precient_crystal"));
        filters.add(RLUtils.eio("sag_milling/pulsating_crystal"));
        filters.add(RLUtils.eio("sag_milling/vibrant_crystal"));
        filters.add(RLUtils.eio("sag_milling/soularium"));
        filters.add(RLUtils.eio("alloy_smelting/energetic_alloy_ingot"));
        filters.add(RLUtils.eio("alloy_smelting/vibrant_alloy_ingot"));
        filters.add(RLUtils.eio("alloy_smelting/dark_steel_ingot"));
        filters.add(RLUtils.eio("alloy_smelting/end_steel_ingot"));

        filters.add(RLUtils.sp("backpack"));
        filters.add(RLUtils.sp("pickup_upgrade"));
        filters.add(RLUtils.sp("filter_upgrade"));
        filters.add(RLUtils.sp("advanced_pickup_upgrade"));
        filters.add(RLUtils.sp("advanced_filter_upgrade"));
        filters.add(RLUtils.sp("magnet_upgrade"));
        filters.add(RLUtils.sp("advanced_magnet_upgrade"));
        filters.add(RLUtils.sp("advanced_magnet_upgrade_from_basic"));
        filters.add(RLUtils.sp("compacting_upgrade"));
        filters.add(RLUtils.sp("advanced_compacting_upgrade"));
        filters.add(RLUtils.sp("void_upgrade"));
        filters.add(RLUtils.sp("advanced_void_upgrade"));
        filters.add(RLUtils.sp("pump_upgrade"));
        filters.add(RLUtils.sp("advanced_pump_upgrade"));
        filters.add(RLUtils.sp("battery_upgrade"));
        filters.add(RLUtils.sp("tank_upgrade"));
        filters.add(RLUtils.sp("refill_upgrade"));
        filters.add(RLUtils.sp("advanced_refill_upgrade"));
        filters.add(RLUtils.sp("inception_upgrade"));
        filters.add(RLUtils.sp("auto_smelting_upgrade"));
        filters.add(RLUtils.sp("auto_smoking_upgrade"));
        filters.add(RLUtils.sp("auto_smoking_upgrade_from_auto_smelting_upgrade"));
        filters.add(RLUtils.sp("auto_blasting_upgrade"));
        filters.add(RLUtils.sp("auto_blasting_upgrade_from_auto_smelting_upgrade"));
        filters.add(RLUtils.sp("stack_upgrade_starter_tier"));
        filters.add(RLUtils.sp("stack_upgrade_tier_1"));
        filters.add(RLUtils.sp("stack_upgrade_tier_1_from_starter"));
        filters.add(RLUtils.sp("stack_upgrade_tier_2"));
        filters.add(RLUtils.sp("stack_upgrade_tier_3"));
        filters.add(RLUtils.sp("stack_upgrade_tier_4"));
        filters.add(RLUtils.sp("stack_upgrade_omega_tier"));

        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_1k_part"));
        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_4k_part"));
        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_16k_part"));
        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_64k_part"));
        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_256k_part"));
        filters.add(AppEng.makeId("decorative/quartz_block"));
        filters.add(AppEng.makeId("decorative/fluix_block"));
        filters.add(AppEng.makeId("misc/deconstruction_certus_quartz_block"));
        filters.add(AppEng.makeId("misc/deconstruction_fluix_block"));
        filters.add(AppEng.makeId("misc/fluixpearl"));
        filters.add(AppEng.makeId("network/cables/glass_fluix"));
        filters.add(AppEng.makeId("network/crafting/patterns_blank"));
        filters.add(AppEng.makeId("network/parts/export_bus"));
        filters.add(AppEng.makeId("network/parts/import_bus"));
        filters.add(AppEng.makeId("network/wireless_part"));
        filters.add(AppEng.makeId("network/crafting/cpu_crafting_unit"));
        filters.add(AppEng.makeId("materials/annihilationcore"));
        filters.add(AppEng.makeId("materials/formationcore"));
        filters.add(AppEng.makeId("materials/advancedcard"));
        filters.add(AppEng.makeId("materials/basiccard"));

        filters.add(RLUtils.fd("wheat_dough_from_water"));
        filters.add(RLUtils.fd("wheat_dough_from_eggs"));
        filters.add(RLUtils.fd("bread_from_smelting"));
        filters.add(RLUtils.fd("bread_from_smoking"));
        filters.add(RLUtils.fd("carrot_crate"));
        filters.add(RLUtils.fd("potato_crate"));
        filters.add(RLUtils.fd("beetroot_crate"));
        filters.add(RLUtils.fd("cabbage_crate"));
        filters.add(RLUtils.fd("tomato_crate"));
        filters.add(RLUtils.fd("onion_crate"));
        filters.add(RLUtils.fd("rice_bale"));
        filters.add(RLUtils.fd("rice_bag"));
        filters.add(RLUtils.fd("straw_bale"));
        filters.add(RLUtils.fd("carrot_from_crate"));
        filters.add(RLUtils.fd("potato_from_crate"));
        filters.add(RLUtils.fd("beetroot_from_crate"));
        filters.add(RLUtils.fd("cabbage"));
        filters.add(RLUtils.fd("tomato"));
        filters.add(RLUtils.fd("onion"));
        filters.add(RLUtils.fd("rice_panicle"));
        filters.add(RLUtils.fd("rice_from_bag"));
        filters.add(RLUtils.fd("straw"));
        filters.add(RLUtils.fd("paper_from_tree_bark"));

        filters.add(new ResourceLocation("farmersrespite", "green_tea_leaves_sack"));
        filters.add(new ResourceLocation("farmersrespite", "yellow_tea_leaves_sack"));
        filters.add(new ResourceLocation("farmersrespite", "black_tea_leaves_sack"));
        filters.add(new ResourceLocation("farmersrespite", "coffee_beans_sack"));
    }
}

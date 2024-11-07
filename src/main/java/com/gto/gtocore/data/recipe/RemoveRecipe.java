package com.gto.gtocore.data.recipe;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;

import appeng.core.AppEng;
import earth.terrarium.adastra.AdAstra;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.VN;
import static com.gto.gtocore.common.data.GTORecipes.DISASSEMBLY_RECORD;

public class RemoveRecipe {

    public static void init(Consumer<ResourceLocation> consumer) {
        DISASSEMBLY_RECORD.clear();
        consumer.accept(new ResourceLocation("minecraft", "coal_ingot_from_blasting_deepslate_coal_ore"));
        consumer.accept(new ResourceLocation("minecraft", "coal_ingot_from_blasting_coal_ore"));
        consumer.accept(new ResourceLocation("minecraft", "coal_ingot_from_smelting_deepslate_coal_ore"));
        consumer.accept(new ResourceLocation("minecraft", "coal_ingot_from_smelting_coal_ore"));
        consumer.accept(new ResourceLocation("minecraft", "copper_ingot_from_blasting_deepslate_copper_ore"));
        consumer.accept(new ResourceLocation("minecraft", "copper_ingot_from_blasting_copper_ore"));
        consumer.accept(new ResourceLocation("minecraft", "copper_ingot_from_smelting_deepslate_copper_ore"));
        consumer.accept(new ResourceLocation("minecraft", "copper_ingot_from_smelting_copper_ore"));
        consumer.accept(new ResourceLocation("minecraft", "redstone_ingot_from_blasting_deepslate_redstone_ore"));
        consumer.accept(new ResourceLocation("minecraft", "redstone_ingot_from_blasting_redstone_ore"));
        consumer.accept(new ResourceLocation("minecraft", "redstone_ingot_from_smelting_deepslate_redstone_ore"));
        consumer.accept(new ResourceLocation("minecraft", "redstone_ingot_from_smelting_redstone_ore"));
        consumer.accept(new ResourceLocation("minecraft", "emerald_ingot_from_blasting_deepslate_emerald_ore"));
        consumer.accept(new ResourceLocation("minecraft", "emerald_ingot_from_blasting_emerald_ore"));
        consumer.accept(new ResourceLocation("minecraft", "emerald_ingot_from_smelting_deepslate_emerald_ore"));
        consumer.accept(new ResourceLocation("minecraft", "emerald_ingot_from_smelting_emerald_ore"));
        consumer.accept(new ResourceLocation("minecraft", "lapis_lazuli_ingot_from_blasting_deepslate_lapis_ore"));
        consumer.accept(new ResourceLocation("minecraft", "lapis_lazuli_ingot_from_blasting_lapis_ore"));
        consumer.accept(new ResourceLocation("minecraft", "lapis_lazuli_ingot_from_smelting_deepslate_lapis_ore"));
        consumer.accept(new ResourceLocation("minecraft", "lapis_lazuli_ingot_from_smelting_lapis_ore"));
        consumer.accept(new ResourceLocation("minecraft", "diamond_ingot_from_blasting_deepslate_diamond_ore"));
        consumer.accept(new ResourceLocation("minecraft", "diamond_ingot_from_blasting_diamond_ore"));
        consumer.accept(new ResourceLocation("minecraft", "diamond_ingot_from_smelting_deepslate_diamond_ore"));
        consumer.accept(new ResourceLocation("minecraft", "diamond_ingot_from_smelting_diamond_ore"));
        consumer.accept(new ResourceLocation("minecraft", "iron_ingot_from_blasting_deepslate_iron_ore"));
        consumer.accept(new ResourceLocation("minecraft", "iron_ingot_from_blasting_iron_ore"));
        consumer.accept(new ResourceLocation("minecraft", "iron_ingot_from_smelting_deepslate_iron_ore"));
        consumer.accept(new ResourceLocation("minecraft", "iron_ingot_from_smelting_iron_ore"));
        consumer.accept(new ResourceLocation("minecraft", "gold_ingot_from_blasting_deepslate_gold_ore"));
        consumer.accept(new ResourceLocation("minecraft", "gold_ingot_from_blasting_gold_ore"));
        consumer.accept(new ResourceLocation("minecraft", "gold_ingot_from_blasting_nether_gold_ore"));
        consumer.accept(new ResourceLocation("minecraft", "gold_ingot_from_smelting_deepslate_gold_ore"));
        consumer.accept(new ResourceLocation("minecraft", "gold_ingot_from_smelting_gold_ore"));
        consumer.accept(new ResourceLocation("minecraft", "gold_ingot_from_smelting_nether_gold_ore"));
        consumer.accept(new ResourceLocation("minecraft", "quartz"));
        consumer.accept(new ResourceLocation("minecraft", "quartz_from_blasting"));
        consumer.accept(new ResourceLocation("minecraft", "hay_block"));
        consumer.accept(new ResourceLocation("minecraft", "netherite_ingot"));
        consumer.accept(new ResourceLocation("minecraft", "netherite_scrap"));
        consumer.accept(new ResourceLocation("minecraft", "netherite_scrap_from_blasting"));
        consumer.accept(new ResourceLocation("minecraft", "infinity"));
        consumer.accept(new ResourceLocation("minecraft", "infinity_ingot"));
        consumer.accept(new ResourceLocation("minecraft", "crystal_matrix_ingot"));
        consumer.accept(new ResourceLocation("minecraft", "double_compressed_crafting_table"));
        consumer.accept(new ResourceLocation("minecraft", "compressed_crafting_table"));
        consumer.accept(new ResourceLocation("minecraft", "crystal_matrix"));
        consumer.accept(new ResourceLocation("minecraft", "neutron_pile"));
        consumer.accept(new ResourceLocation("minecraft", "neutron_pile_from_ingots"));
        consumer.accept(new ResourceLocation("minecraft", "neutron_ingot_from_nuggets"));
        consumer.accept(new ResourceLocation("minecraft", "neutron_ingot_from_neutron_block"));
        consumer.accept(new ResourceLocation("minecraft", "neutron_nugget"));
        consumer.accept(new ResourceLocation("minecraft", "neutron"));

        consumer.accept(new ResourceLocation("avaritia", "crystal_matrix_ingot"));
        consumer.accept(new ResourceLocation("avaritia", "diamond_lattice"));
        consumer.accept(new ResourceLocation("avaritia", "extreme_crafting_table"));

        consumer.accept(new ResourceLocation("expatternprovider", "fishbig"));

        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "refining/fuel_from_refining_oil"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "oxygen_loading/oxygen_from_oxygen_loading_oxygen"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "oxygen_loading/oxygen_from_oxygen_loading_water"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "cryo_freezing/cryo_fuel_from_cryo_freezing_blue_ice"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "cryo_freezing/cryo_fuel_from_cryo_freezing_ice_shard"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "cryo_freezing/cryo_fuel_from_cryo_freezing_ice"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "cryo_freezing/cryo_fuel_from_cryo_freezing_packed_ice"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "compressing/calorite_plate_from_compressing_calorite_blocks"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "compressing/calorite_plate_from_compressing_calorite_ingots"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "compressing/desh_plate_from_compressing_desh_blocks"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "compressing/desh_plate_from_compressing_desh_ingots"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "compressing/iron_plate_from_compressing_iron_block"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "compressing/iron_plate_from_compressing_iron_ingot"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "compressing/ostrum_plate_from_compressing_ostrum_blocks"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "compressing/ostrum_plate_from_compressing_ostrum_ingots"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "compressing/steel_plate_from_compressing_steel_blocks"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "compressing/steel_plate_from_compressing_steel_ingots"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "alloying/steel_ingot_from_alloying_iron_ingot_and_coals"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "nasa_workbench/tier_1_rocket_from_nasa_workbench"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "nasa_workbench/tier_2_rocket_from_nasa_workbench"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "nasa_workbench/tier_3_rocket_from_nasa_workbench"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "nasa_workbench/tier_4_rocket_from_nasa_workbench"));
        consumer.accept(new ResourceLocation("ad_astra_rocketed", "nasa_workbench/default/tier_5_rocket_from_nasa_workbench"));
        consumer.accept(new ResourceLocation("ad_astra_rocketed", "nasa_workbench/default/tier_6_rocket_from_nasa_workbench"));
        consumer.accept(new ResourceLocation("ad_astra_rocketed", "nasa_workbench/default/tier_7_rocket_from_nasa_workbench"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "compressor"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "steel_block"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "energizer"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "steel_cable"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "desh_block"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "desh_ingot"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "desh_nugget"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "desh_ingot_from_desh_block"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "desh_cable"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "ostrum_block"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "ostrum_ingot"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "ostrum_nugget"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "ostrum_ingot_from_ostrum_block"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "calorite_block"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "calorite_ingot"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "calorite_nugget"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "calorite_ingot_from_calorite_block"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "cable_duct"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "steel_ingot"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "nasa_workbench"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "compressor"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "coal_generator"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "etrionic_blast_furnace"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "fuel_refinery"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "solar_panel"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "water_pump"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "cryo_freezer"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "fan"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "engine_frame"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "steel_engine"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "desh_engine"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "ostrum_engine"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "calorite_engine"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "calorite_tank"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "ostrum_tank"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "desh_tank"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "steel_tank"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "rocket_fin"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "rocket_nose_cone"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "smelting/desh_ingot_from_smelting_deepslate_desh_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "smelting/desh_ingot_from_smelting_moon_desh_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "smelting/desh_ingot_from_smelting_raw_desh"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "smelting/ostrum_ingot_from_smelting_deepslate_ostrum_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "smelting/ostrum_ingot_from_smelting_mars_ostrum_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "smelting/ostrum_ingot_from_smelting_raw_ostrum"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "smelting/calorite_ingot_from_smelting_deepslate_calorite_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "smelting/calorite_ingot_from_smelting_venus_calorite_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "smelting/calorite_ingot_from_smelting_raw_calorite"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "smelting/ice_shard_from_smelting_deepslate_ice_shard_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "smelting/ice_shard_from_smelting_glacio_ice_shard_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "smelting/ice_shard_from_smelting_mars_ice_shard_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "smelting/ice_shard_from_smelting_moon_ice_shard_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "blasting/desh_ingot_from_blasting_deepslate_desh_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "blasting/desh_ingot_from_blasting_moon_desh_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "blasting/desh_ingot_from_blasting_raw_desh"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "blasting/ostrum_ingot_from_blasting_deepslate_ostrum_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "blasting/ostrum_ingot_from_blasting_mars_ostrum_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "blasting/ostrum_ingot_from_blasting_raw_ostrum"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "blasting/calorite_ingot_from_blasting_deepslate_calorite_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "blasting/calorite_ingot_from_blasting_venus_calorite_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "blasting/calorite_ingot_from_blasting_raw_calorite"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "blasting/ice_shard_from_blasting_deepslate_ice_shard_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "blasting/ice_shard_from_blasting_glacio_ice_shard_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "blasting/ice_shard_from_blasting_mars_ice_shard_ore"));
        consumer.accept(new ResourceLocation(AdAstra.MOD_ID, "blasting/ice_shard_from_blasting_moon_ice_shard_ore"));

        consumer.accept(AppEng.makeId("decorative/quartz_block"));
        consumer.accept(AppEng.makeId("decorative/fluix_block"));
        consumer.accept(AppEng.makeId("misc/deconstruction_certus_quartz_block"));
        consumer.accept(AppEng.makeId("misc/deconstruction_fluix_block"));
        consumer.accept(AppEng.makeId("misc/fluixpearl"));
        consumer.accept(AppEng.makeId("network/cables/glass_fluix"));
        consumer.accept(AppEng.makeId("network/blocks/controller"));
        consumer.accept(AppEng.makeId("network/crafting/patterns_blank"));
        consumer.accept(AppEng.makeId("network/parts/export_bus"));
        consumer.accept(AppEng.makeId("network/parts/import_bus"));
        consumer.accept(AppEng.makeId("network/wireless_part"));
        consumer.accept(AppEng.makeId("network/crafting/cpu_crafting_unit"));
        consumer.accept(AppEng.makeId("materials/annihilationcore"));
        consumer.accept(AppEng.makeId("materials/formationcore"));
        consumer.accept(AppEng.makeId("materials/advancedcard"));
        consumer.accept(AppEng.makeId("materials/basiccard"));

        consumer.accept(GTCEu.id("shaped/maintenance_hatch_cleaning"));
        consumer.accept(GTCEu.id("shaped/vacuum_tube"));
        consumer.accept(GTCEu.id("packer/unpackage_lv_cadmium_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_lv_lithium_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_lv_sodium_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_mv_cadmium_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_mv_lithium_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_mv_sodium_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_hv_cadmium_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_hv_lithium_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_hv_sodium_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_ev_vanadium_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_iv_vanadium_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_luv_vanadium_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_zpm_naquadria_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_uv_naquadria_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_ev_lapotronic_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_iv_lapotronic_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_luv_lapotronic_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_zpm_lapotronic_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_uv_lapotronic_battery"));
        consumer.accept(GTCEu.id("packer/unpackage_uhv_ultimate_battery"));
        consumer.accept(GTCEu.id("macerator/macerate_wheat"));
        consumer.accept(GTCEu.id("autoclave/agar"));
        consumer.accept(GTCEu.id("assembler/casing_ulv"));
        consumer.accept(GTCEu.id("assembler/casing_uv"));
        consumer.accept(GTCEu.id("assembler/casing_mv"));
        consumer.accept(GTCEu.id("assembler/casing_hv"));
        consumer.accept(GTCEu.id("assembler/casing_ev"));
        consumer.accept(GTCEu.id("assembler/casing_iv"));
        consumer.accept(GTCEu.id("assembler/casing_luv"));
        consumer.accept(GTCEu.id("assembler/casing_zpm"));
        consumer.accept(GTCEu.id("assembler/casing_uv"));
        consumer.accept(GTCEu.id("assembler/casing_uhv"));
        consumer.accept(GTCEu.id("assembler/wool_from_string"));
        consumer.accept(GTCEu.id("assembly_line/dynamo_hatch_uhv"));
        consumer.accept(GTCEu.id("assembly_line/energy_hatch_uhv"));
        consumer.accept(GTCEu.id("bender/bucket"));
        consumer.accept(GTCEu.id("research_station/1_x_gtceu_uv_energy_input_hatch"));
        consumer.accept(GTCEu.id("research_station/1_x_gtceu_uv_energy_output_hatch"));
        consumer.accept(GTCEu.id("mixer/ender_pearl_dust"));
        consumer.accept(GTCEu.id("mixer/rocket_fuel_from_dinitrogen_tetroxide"));
        consumer.accept(GTCEu.id("chemical_reactor/stem_cells"));
        consumer.accept(GTCEu.id("large_chemical_reactor/stem_cells"));
        consumer.accept(GTCEu.id("forming_press/credit_cupronickel"));
        consumer.accept(GTCEu.id("extruder/nan_certificate"));
        consumer.accept(GTCEu.id("centrifuge/rare_earth_separation"));
        consumer.accept(GTCEu.id("electrolyzer/bone_meal_electrolysis"));
        consumer.accept(GTCEu.id("electric_blast_furnace/pyrite_metallurgy"));
        consumer.accept(GTCEu.id("electric_blast_furnace/aluminium_from_ruby_dust"));
        consumer.accept(GTCEu.id("electric_blast_furnace/aluminium_from_ruby_gem"));
        consumer.accept(GTCEu.id("electric_blast_furnace/aluminium_from_sapphire_gem"));
        consumer.accept(GTCEu.id("electric_blast_furnace/stibnite_metallurgy"));
        consumer.accept(GTCEu.id("electric_blast_furnace/soda_ash_from_calcite"));
        consumer.accept(GTCEu.id("electric_blast_furnace/pentlandite_metallurgy"));
        consumer.accept(GTCEu.id("electric_blast_furnace/aluminium_from_green_sapphire_gem"));
        consumer.accept(GTCEu.id("electric_blast_furnace/rutile_from_ilmenite"));
        consumer.accept(GTCEu.id("electric_blast_furnace/chalcopyrite_metallurgy"));
        consumer.accept(GTCEu.id("electric_blast_furnace/cobaltite_metallurgy"));
        consumer.accept(GTCEu.id("electric_blast_furnace/tetrahedrite_metallurgy"));
        consumer.accept(GTCEu.id("electric_blast_furnace/galena_metallurgy"));
        consumer.accept(GTCEu.id("electric_blast_furnace/sphalerite_metallurgy"));
        consumer.accept(GTCEu.id("electric_blast_furnace/aluminium_from_green_sapphire_dust"));
        consumer.accept(GTCEu.id("electric_blast_furnace/blast_silicon_dioxide"));
        consumer.accept(GTCEu.id("electric_blast_furnace/aluminium_from_sapphire_dust"));
        consumer.accept(GTCEu.id("electric_blast_furnace/titanium_from_tetrachloride"));

        Material[] fluidMap = new Material[] { GTMaterials.Glue, GTMaterials.Polyethylene,
                GTMaterials.Polytetrafluoroethylene, GTMaterials.Polybenzimidazole };

        for (var machine : GTMachines.FLUID_IMPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                consumer.accept(GTCEu.id("assembler/" + "fluid_hatch_" + VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
        }

        for (var machine : GTMachines.FLUID_EXPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                consumer.accept(GTCEu.id("assembler/" + "fluid_export_hatch_" + VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
        }

        for (var machine : GTMachines.ITEM_IMPORT_BUS) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                consumer.accept(GTCEu.id("assembler/" + "item_import_bus_" + VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
        }

        for (var machine : GTMachines.ITEM_EXPORT_BUS) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                consumer.accept(GTCEu.id("assembler/" + "item_export_bus_" + VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
        }

        for (var machine : GTMachines.DUAL_IMPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                consumer.accept(GTCEu.id("assembler/" + "dual_import_bus_" + VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
        }

        for (var machine : GTMachines.DUAL_EXPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                consumer.accept(GTCEu.id("assembler/" + "dual_export_bus_" + VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
        }
    }
}

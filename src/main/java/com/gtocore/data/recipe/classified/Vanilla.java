package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.*;
import com.gtocore.common.data.machines.*;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;
import com.gtolib.utils.RegistriesUtils;
import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.glodblock.github.extendedae.common.EPPItemAndBlock;

import static com.gtocore.common.data.GTOItems.SPOOLS_LARGE;

final class Vanilla {

    public static void init() {
        if (GTCEu.isProd()) {
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_tier_1"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_1"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_starter_tier"), GTMachines.SUPER_CHEST[GTValues.MV].asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:compacting_upgrade"), GTItems.ELECTRIC_PISTON_MV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.COVER_ITEM_VOIDING.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ITEM_MAGNET_LV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_tier_2"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_2"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_1"), GTMachines.SUPER_CHEST[GTValues.HV].asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:pickup_upgrade"), GTItems.ITEM_FILTER.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:refill_upgrade"), GTItems.ROBOT_ARM_MV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("tank_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:tank_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), RegistriesUtils.getItemStack("gtceu:bronze_drum"));
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ITEM_FILTER.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:magnet_upgrade"), GTItems.ITEM_MAGNET_HV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ROBOT_ARM_LV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:filter_upgrade"), GTItems.TAG_FILTER.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_starter_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_starter_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTMachines.SUPER_CHEST[GTValues.LV].asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:void_upgrade"), GTItems.COVER_ITEM_VOIDING_ADVANCED.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("auto_blasting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:auto_blasting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:blasting_upgrade"), GTItems.CONVEYOR_MODULE_LV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_omega_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_omega_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_4"), GTMachines.QUANTUM_CHEST[GTValues.UHV].asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_tier_4"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_4"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_3"), GTMachines.QUANTUM_CHEST[GTValues.IV].asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_tier_3"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_3"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_2"), GTMachines.SUPER_CHEST[GTValues.EV].asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("pump_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:pump_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ELECTRIC_PUMP_LV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("auto_smoking_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:auto_smoking_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:smoking_upgrade"), GTItems.CONVEYOR_MODULE_LV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ELECTRIC_PISTON_LV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTMachines.ITEM_COLLECTOR[GTValues.LV].asItem());

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("backpack"), RegistriesUtils.getItemStack("sophisticatedbackpacks:backpack"),
                    "ABA",
                    "BCB",
                    "DBD",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.WroughtIron), 'B', new ItemStack(Items.LEATHER.asItem()), 'C', RegistriesUtils.getItemStack("gtceu:wood_crate"), 'D', new ItemStack(Items.STRING.asItem()));
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("automatic_chisel"), MultiBlockG.CARVING_CENTER.asItem(),
                    "ABA",
                    "CDC",
                    "EFE",
                    'B', RegistriesUtils.getItemStack("chisel:chisel"), 'F', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.SteelMagnetic), 'D', GTItems.ROBOT_ARM_LV.asItem(), 'C', GTItems.CONVEYOR_MODULE_LV.asItem(), 'E', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'A', CustomTags.LV_CIRCUITS);
        }

        VanillaRecipeHelper.addSmeltingRecipe(GTOCore.id("raw_aluminum"), GTOItems.RAW_ALUMINUM.asStack(), ChemicalHelper.get(TagPrefix.ingot, GTMaterials.Aluminium), 0);
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("pattern_modifier_pro"), GTOItems.PATTERN_MODIFIER_PRO.asItem(), RegistriesUtils.getItemStack("expatternprovider:pattern_modifier"));

        switch (GTOCore.difficulty) {
            case 1 -> {
                VanillaRecipeHelper.addShapedRecipe(GTOCore.id("ender_eye"), ChemicalHelper.get(TagPrefix.gem, GTMaterials.EnderEye),
                        " A ",
                        "ABA",
                        " A ",
                        'A', new MaterialEntry(TagPrefix.dust, GTMaterials.Blaze), 'B', new MaterialEntry(TagPrefix.gem, GTMaterials.EnderPearl));
                VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_assembly_block"), GTOBlocks.STEAM_ASSEMBLY_BLOCK.asItem(),
                        "ABA",
                        "DCD",
                        "ADA",
                        'A', new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Bronze), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'C', new MaterialEntry(TagPrefix.frameGt, GTMaterials.Bronze), 'D', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze));
            }
            case 2 -> {
                VanillaRecipeHelper.addShapedRecipe(GTOCore.id("ender_eye"), ChemicalHelper.get(TagPrefix.gem, GTMaterials.EnderEye),
                        "AAA",
                        "ABA",
                        "AAA",
                        'A', new MaterialEntry(TagPrefix.dust, GTMaterials.Blaze), 'B', new MaterialEntry(TagPrefix.gem, GTMaterials.EnderPearl));
                VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_assembly_block"), GTOBlocks.STEAM_ASSEMBLY_BLOCK.asItem(),
                        "ABA",
                        "DCD",
                        "ABA",
                        'A', new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Bronze), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'C', new MaterialEntry(TagPrefix.frameGt, GTMaterials.Bronze), 'D', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze));
            }
            case 3 -> {
                VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_assembly_block"), GTOBlocks.STEAM_ASSEMBLY_BLOCK.asItem(),
                        "ABA",
                        "BCB",
                        "ABA",
                        'A', new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Bronze), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'C', new MaterialEntry(TagPrefix.frameGt, GTMaterials.Bronze));
            }
        }
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("me_wireless_connection_machine"), GTOMachines.ME_WIRELESS_CONNECTION_MACHINE.asItem(),
                "ABA",
                "B B",
                "ABA",
                'A', CustomTags.EV_CIRCUITS, 'B', RegistriesUtils.getItemStack("expatternprovider:wireless_connect"));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("me_pattern_content_sort_machine"), GTOMachines.ME_PATTERN_CONTENT_SORT_MACHINE.asItem(),
                "ABA",
                "BCB",
                "DBD",
                'A', GTItems.ROBOT_ARM_HV.asItem(), 'B', CustomTags.HV_CIRCUITS, 'C', GTBlocks.MACHINE_CASING_HV.asItem(), 'D', new ItemStack(AEItems.BLANK_PATTERN.asItem()));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("exchange_storage_monitor"), GTOAEParts.INSTANCE.getEXCHANGE_STORAGE_MONITOR().get().stack(1),
                "   ",
                "ABA",
                "   ",
                'A', CustomTags.MV_CIRCUITS, 'B', RegistriesUtils.getItemStack("ae2:storage_monitor"));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("simple_crafting_terminal"), GTOAEParts.INSTANCE.getSIMPLE_CRAFTING_TERMINAL().get().stack(1),
                "   ",
                "ABC",
                "DED",
                'A', CustomTags.CRAFTING_SAWS, 'B', Items.CRAFTING_TABLE, 'C', CustomTags.CRAFTING_SCREWDRIVERS,
                'D', new MaterialEntry(TagPrefix.screw, GTMaterials.WroughtIron), 'E', Items.CHEST);

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("me_2in1_terminal"), GTOAEParts.INSTANCE.getME_2IN1_TERMINAL().get().stack(1),
                "   ",
                "ABA",
                "DDD",
                'A', CustomTags.LuV_CIRCUITS, 'B', EPPItemAndBlock.EX_PATTERN_TERMINAL,
                'D', new MaterialEntry(TagPrefix.plate, GTMaterials.HastelloyX));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("me_2in1_terminal_wireless"), GTOItems.WIRELESS_ME2IN1.asItem(),
                "A",
                "B",
                "D",
                'A', AEItems.WIRELESS_RECEIVER.asItem(), 'B', GTOAEParts.INSTANCE.getME_2IN1_TERMINAL().get().asItem(),
                'D', AEBlocks.DENSE_ENERGY_CELL.asItem());

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("paper_dust"), ChemicalHelper.get(TagPrefix.dust, GTMaterials.Paper), "S", "m", 'S', RegistriesUtils.getItemStack("farmersdelight:tree_bark"));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("cooking_pot"), RegistriesUtils.getItemStack("farmersdelight:cooking_pot"),
                "ABA",
                "CDC",
                "EEE",
                'A', new MaterialEntry(TagPrefix.ingot, GTMaterials.Brick), 'B', new MaterialEntry(TagPrefix.rod, GTMaterials.Iron), 'C', new MaterialEntry(TagPrefix.plate, GTMaterials.Iron), 'D', new ItemStack(Items.WATER_BUCKET.asItem()), 'E', new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("gas_tank"), RegistriesUtils.getItemStack("ad_astra:gas_tank"),
                "AAA",
                "BCB",
                "AAA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'B', new MaterialEntry(TagPrefix.rod, GTMaterials.Iron), 'C', RegistriesUtils.getItemStack("enderio:fluid_tank"));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("item_conduit"), RegistriesUtils.getItemStack("enderio:item_conduit", 8),
                "AAA",
                "BCD",
                "AAA",
                'A', RegistriesUtils.getItemStack("enderio:conduit_binder"), 'B', new MaterialEntry(TagPrefix.pipeSmallItem, GTMaterials.Nickel), 'C', new MaterialEntry(TagPrefix.pipeSmallItem, GTMaterials.Cobalt), 'D', new MaterialEntry(TagPrefix.pipeSmallItem, GTMaterials.Brass));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("vibrant_photovoltaic_power_station"), GeneratorMultiblock.PHOTOVOLTAIC_POWER_STATION_VIBRANT.asItem(),
                "ABA",
                "BCB",
                "ADA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.TungstenSteel), 'B', new MaterialEntry(TagPrefix.block, GTOMaterials.DarkSteel), 'C', GTOBlocks.VIBRANT_PHOTOVOLTAIC_BLOCK.asItem(), 'D', CustomTags.EV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("primitive_distillation_tower"), MultiBlockC.PRIMITIVE_DISTILLATION_TOWER.asItem(),
                "ABA",
                "BCB",
                "ADA",
                'A', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.Steel), 'B', new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Lead), 'C', RegistriesUtils.getItemStack("gtceu:hp_steam_solid_boiler"), 'D', new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Potin));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ev_rocket_engine"), GTOMachines.ROCKET_ENGINE_GENERATOR[GTValues.EV].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', new MaterialEntry(TagPrefix.rotor, GTMaterials.Lead), 'B', CustomTags.EV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_EV.asItem(), 'D', GTMachines.HULL[GTValues.EV].asItem(), 'E', new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.Steel), 'F', GTItems.ELECTRIC_PUMP_EV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("iv_rocket_engine"), GTOMachines.ROCKET_ENGINE_GENERATOR[GTValues.IV].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', new MaterialEntry(TagPrefix.rotor, GTMaterials.Chromium), 'B', CustomTags.IV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_IV.asItem(), 'D', GTMachines.HULL[GTValues.IV].asItem(), 'E', new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.TungstenSteel), 'F', GTItems.ELECTRIC_PUMP_IV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("block_conversion_room"), MultiBlockD.BLOCK_CONVERSION_ROOM.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTItems.QUANTUM_EYE.asItem(), 'B', GTItems.FIELD_GENERATOR_LV.asItem(), 'C', new MaterialEntry(TagPrefix.block, GTOMaterials.VibrantAlloy));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("max_neutron_compressor"), GTOMachines.NEUTRON_COMPRESSOR[GTValues.MAX].asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', GTOItems.MAX_ELECTRIC_PUMP.asItem(), 'B', CustomTags.MAX_CIRCUITS, 'C', GTOItems.MAX_ELECTRIC_PISTON.asItem(), 'D', GTMachines.HULL[GTValues.MAX].asItem(), 'E', new MaterialEntry(TagPrefix.cableGtSingle, GTOMaterials.CosmicNeutronium));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("opv_thread_hatch"), GTOMachines.THREAD_HATCH[GTValues.OpV].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', CustomTags.MAX_CIRCUITS, 'B', GTItems.ROBOT_ARM_OpV.asItem(), 'C', GTItems.CONVEYOR_MODULE_OpV.asItem(), 'D', RegistriesUtils.getItemStack("gtceu:opv_parallel_hatch"), 'E', new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.AwakenedDraconium), 'F', GTOItems.INTEGRATED_CONTROL_CORE_OpV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("uv_thread_hatch"), GTOMachines.THREAD_HATCH[GTValues.UV].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', CustomTags.UHV_CIRCUITS, 'B', GTItems.ROBOT_ARM_UV.asItem(), 'C', GTItems.CONVEYOR_MODULE_UV.asItem(), 'D', RegistriesUtils.getItemStack("gtceu:uv_parallel_hatch"), 'E', new MaterialEntry(TagPrefix.wireGtHex, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide), 'F', GTOItems.INTEGRATED_CONTROL_CORE_UV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ancient_reactor_core"), MultiBlockG.ANCIENT_REACTOR_CORE.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Gold), 'C', new MaterialEntry(TagPrefix.block, GTOMaterials.PulsatingAlloy));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_steam_circuit_assembler"), MultiBlockA.LARGE_STEAM_CIRCUIT_ASSEMBLER.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'B', new ItemStack(Blocks.COMPARATOR.asItem()), 'C', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'D', new ItemStack(AEBlocks.MOLECULAR_ASSEMBLER.block().asItem()));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_foundry"), MultiBlockA.STEAM_FOUNDRY.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Potin), 'C', new MaterialEntry(TagPrefix.rodLong, GTMaterials.TinAlloy), 'D', RegistriesUtils.getItemStack("gtceu:lp_steam_alloy_smelter"));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("advanced_card"), new ItemStack(AEItems.ADVANCED_CARD.asItem(), 2),
                "AB ",
                "CDB",
                "EB ",
                'A', new MaterialEntry(TagPrefix.wireFine, GTMaterials.RedAlloy), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.StainlessSteel), 'C', new ItemStack(AEItems.ENGINEERING_PROCESSOR.asItem()), 'D', CustomTags.HV_CIRCUITS, 'E', new MaterialEntry(TagPrefix.wireFine, GTMaterials.Silver));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ev_lightning_rod"), GTOMachines.LIGHTNING_ROD[GTValues.EV].asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTItems.LAPOTRON_CRYSTAL.asItem(), 'B', GTMachines.POWER_TRANSFORMER[GTValues.EV].asItem(), 'C', GTMachines.HULL[GTValues.EV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("max_thread_hatch"), GTOMachines.THREAD_HATCH[GTValues.MAX].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTOItems.SUPRACHRONAL_CIRCUIT[GTValues.MAX].asItem(), 'B', GTOItems.MAX_ROBOT_ARM.asItem(), 'C', GTOItems.MAX_CONVEYOR_MODULE.asItem(), 'D', RegistriesUtils.getItemStack("gtceu:max_parallel_hatch"), 'E', new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.Hypogen), 'F', GTOItems.INTEGRATED_CONTROL_CORE_MAX.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("uev_thread_hatch"), GTOMachines.THREAD_HATCH[GTValues.UEV].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', CustomTags.UIV_CIRCUITS, 'B', GTItems.ROBOT_ARM_UEV.asItem(), 'C', GTItems.CONVEYOR_MODULE_UEV.asItem(), 'D', RegistriesUtils.getItemStack("gtceu:uev_parallel_hatch"), 'E', new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.Enderite), 'F', GTOItems.INTEGRATED_CONTROL_CORE_UEV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("lv_semi_fluid"), GTOMachines.SEMI_FLUID_GENERATOR[GTValues.LV].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.ELECTRIC_PISTON_LV.asItem(), 'B', CustomTags.LV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_LV.asItem(), 'D', GTMachines.HULL[GTValues.LV].asItem(), 'E', new MaterialEntry(TagPrefix.gear, GTMaterials.Potin), 'F', new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.Cobalt));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_steam_input_hatch"), GTOMachines.LARGE_STEAM_HATCH.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'B', RegistriesUtils.getItemStack("enderio:vibrant_crystal"), 'C', new MaterialEntry(TagPrefix.pipeTinyFluid, GTMaterials.Titanium), 'D', RegistriesUtils.getItemStack("gtceu:steam_input_hatch"));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("cleaning_configuration_maintenance_hatch"), GTOMachines.CLEANING_CONFIGURATION_MAINTENANCE_HATCH.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', RegistriesUtils.getItemStack("gtceu:cleaning_maintenance_hatch"), 'B', CustomTags.LuV_CIRCUITS, 'C', GTOMachines.AUTO_CONFIGURATION_MAINTENANCE_HATCH.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_cracker"), MultiBlockC.STEAM_CRACKER.asItem(),
                "ABA",
                "BCB",
                "DBD",
                'A', new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.TinAlloy), 'B', new MaterialEntry(TagPrefix.pipeQuadrupleFluid, GTMaterials.Potin), 'C', RegistriesUtils.getItemStack("gtceu:hp_steam_alloy_smelter"), 'D', GTOItems.ULV_FLUID_REGULATOR.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_steam_mixer"), MultiBlockA.LARGE_STEAM_MIXER.asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', RegistriesUtils.getItemStack("enderio:infinity_rod"), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'C', new MaterialEntry(TagPrefix.pipeHugeFluid, GTMaterials.Copper), 'D', MultiBlockA.STEAM_MIXER.asItem(), 'E', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("wood_rotor"), GTOItems.WOOD_ROTOR.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.TreatedWood), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Wood), 'C', new MaterialEntry(TagPrefix.frameGt, GTMaterials.Wood));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("steam_pressor"), MultiBlockA.STEAM_PRESSOR.asItem(),
                "ABA",
                "CDC",
                "AEA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'B', new MaterialEntry(TagPrefix.gearSmall, GTMaterials.Bronze), 'C', new MaterialEntry(TagPrefix.springSmall, GTMaterials.Iron), 'D', RegistriesUtils.getItemStack("gtceu:lp_steam_compressor"), 'E', new MaterialEntry(TagPrefix.gear, GTMaterials.WroughtIron));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_steam_bath"), MultiBlockA.LARGE_STEAM_BATH.asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', new MaterialEntry(TagPrefix.foil, GTMaterials.Steel), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'C', new MaterialEntry(TagPrefix.rotor, GTMaterials.Aluminium), 'D', MultiBlockA.STEAM_BATH.asItem(), 'E', new MaterialEntry(TagPrefix.block, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("zpm_naquadah_reactor"), GTOMachines.NAQUADAH_REACTOR_GENERATOR[GTValues.ZPM].asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', new MaterialEntry(TagPrefix.rod, GTMaterials.Naquadria), 'B', CustomTags.ZPM_CIRCUITS, 'C', GTItems.FIELD_GENERATOR_ZPM.asItem(), 'D', GTMachines.HULL[GTValues.ZPM].asItem(), 'E', new MaterialEntry(TagPrefix.cableGtQuadruple, GTMaterials.Naquadah));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_steam_ore_washer"), MultiBlockA.LARGE_STEAM_ORE_WASHER.asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', RegistriesUtils.getItemStack("enderio:infinity_rod"), 'B', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.Bronze), 'C', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'D', MultiBlockA.STEAM_ORE_WASHER.asItem(), 'E', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_steam_crusher"), MultiBlockC.LARGE_STEAM_CRUSHER.asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, GTMaterials.Brass), 'B', new MaterialEntry(TagPrefix.gear, GTMaterials.Diamond), 'C', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'D', MultiBlockC.STEAM_CRUSHER.asItem(), 'E', new MaterialEntry(TagPrefix.gear, GTMaterials.CobaltBrass));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_steam_centrifuge"), MultiBlockA.LARGE_STEAM_CENTRIFUGE.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.block, GTMaterials.Bronze), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'C', new MaterialEntry(TagPrefix.gearSmall, GTMaterials.Iron), 'D', MultiBlockA.STEAM_SEPARATOR.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_cracker"), MultiBlockA.LARGE_CRACKER.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTItems.FIELD_GENERATOR_LuV.asItem(), 'B', CustomTags.UV_CIRCUITS, 'C', new MaterialEntry(TagPrefix.spring, GTMaterials.Naquadah), 'D', GTMultiMachines.CRACKER.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_greenhouse"), MultiBlockG.LARGE_GREENHOUSE.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTItems.FIELD_GENERATOR_EV.asItem(), 'B', CustomTags.LuV_CIRCUITS, 'C', GTItems.SENSOR_EV.asItem(), 'D', MultiBlockD.GREENHOUSE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("mv_semi_fluid"), GTOMachines.SEMI_FLUID_GENERATOR[GTValues.MV].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.ELECTRIC_PISTON_MV.asItem(), 'B', CustomTags.MV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_MV.asItem(), 'D', GTMachines.HULL[GTValues.MV].asItem(), 'E', new MaterialEntry(TagPrefix.gear, GTOMaterials.EglinSteel), 'F', new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.AnnealedCopper));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("hv_semi_fluid"), GTOMachines.SEMI_FLUID_GENERATOR[GTValues.HV].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.ELECTRIC_PISTON_HV.asItem(), 'B', CustomTags.HV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_HV.asItem(), 'D', GTMachines.HULL[GTValues.HV].asItem(), 'E', new MaterialEntry(TagPrefix.gear, GTMaterials.Chromium), 'F', new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.Electrum));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_rock_crusher"), GCYMMachines.LARGE_ROCK_CRUSHER.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTItems.ELECTRIC_PISTON_IV.asItem(), 'B', CustomTags.IV_CIRCUITS, 'C', new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.Platinum), 'D', GTMachines.ROCK_CRUSHER[GTValues.IV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("block_bus"), GTOMachines.BLOCK_BUS.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.block, GTOMaterials.EnergeticAlloy), 'B', new MaterialEntry(TagPrefix.block, GTOMaterials.ConductiveAlloy), 'C', RegistriesUtils.getItemStack("enderio:vacuum_chest"), 'D', GTMachines.ITEM_IMPORT_BUS[GTValues.LuV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("cleaning_maintenance_hatch"), RegistriesUtils.getItemStack("gtceu:cleaning_maintenance_hatch"),
                "ABA",
                "CDC",
                "ABA",
                'A', GTBlocks.FILTER_CASING.asItem(), 'B', RegistriesUtils.getItemStack("gtceu:auto_maintenance_hatch"), 'C', GTItems.FIELD_GENERATOR_HV.asItem(), 'D', GTMachines.HULL[GTValues.HV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_separator"), MultiBlockA.STEAM_SEPARATOR.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.block, GTMaterials.Bronze), 'B', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, GTMaterials.Bronze), 'C', new MaterialEntry(TagPrefix.gear, GTMaterials.Rubber), 'D', GTBlocks.CASING_BRONZE_GEARBOX.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("uxv_thread_hatch"), GTOMachines.THREAD_HATCH[GTValues.UXV].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', CustomTags.OpV_CIRCUITS, 'B', GTItems.ROBOT_ARM_UXV.asItem(), 'C', GTItems.CONVEYOR_MODULE_UXV.asItem(), 'D', RegistriesUtils.getItemStack("gtceu:uxv_parallel_hatch"), 'E', new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.Legendarium), 'F', GTOItems.INTEGRATED_CONTROL_CORE_UXV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("gravity_configuration_hatch"), GTOMachines.GRAVITY_CONFIGURATION_HATCH.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOMachines.GRAVITY_HATCH.asItem(), 'B', CustomTags.UEV_CIRCUITS, 'C', GTItems.FIELD_GENERATOR_UEV.asItem(), 'D', GTOMachines.AUTO_CONFIGURATION_MAINTENANCE_HATCH.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_semi_fluid_generator"), GeneratorMultiblock.LARGE_SEMI_FLUID_GENERATOR.asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.ELECTRIC_PISTON_EV.asItem(), 'B', CustomTags.EV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_EV.asItem(), 'D', GTMachines.HULL[GTValues.EV].asItem(), 'E', new MaterialEntry(TagPrefix.gear, GTOMaterials.Inconel792), 'F', new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.Nichrome));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("heat_sensor"), GTOMachines.HEAT_SENSOR.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', new MaterialEntry(TagPrefix.pipeTinyFluid, GTMaterials.Steel), 'B', new ItemStack(Blocks.REPEATER.asItem()), 'C', GTBlocks.MACHINE_CASING_LV.asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("of_the_sea"), new ItemStack(Items.HEART_OF_THE_SEA.asItem()),
                "ABA",
                "BCB",
                "ABA",
                'A', GTItems.QUANTUM_STAR.asItem(), 'B', GTOItems.GLACIO_SPIRIT.asItem(), 'C', GTOItems.PELLET_ANTIMATTER.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("leap_forward_one_blast_furnace"), MultiBlockA.LEAP_FORWARD_ONE_BLAST_FURNACE.asItem(),
                "ABA",
                "BCB",
                "DDD",
                'A', new MaterialEntry(TagPrefix.foil, GTMaterials.WroughtIron), 'B', RegistriesUtils.getItemStack("ad_astra:airlock"), 'C', GTMultiMachines.PRIMITIVE_BLAST_FURNACE.asItem(), 'D', GTItems.FIRECLAY_BRICK.asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("sensor"), new ItemStack(Blocks.SCULK_SENSOR.asItem()),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.gem, GTMaterials.EchoShard), 'B', new MaterialEntry(TagPrefix.dust, GTMaterials.EchoShard), 'C', new ItemStack(Blocks.NOTE_BLOCK.asItem()), 'D', new ItemStack(Blocks.COMPARATOR.asItem()));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("law_filter_casing"), GTOBlocks.LAW_FILTER_CASING.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTItems.EMITTER_UEV.asItem(), 'B', GTBlocks.FILTER_CASING_STERILE.asItem(), 'C', GTMachines.MUFFLER_HATCH[GTValues.UEV].asItem(), 'D', new MaterialEntry(TagPrefix.frameGt, GTOMaterials.Mithril));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("iv_naquadah_reactor"), GTOMachines.NAQUADAH_REACTOR_GENERATOR[GTValues.IV].asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', new MaterialEntry(TagPrefix.rod, GTMaterials.Naquadah), 'B', CustomTags.IV_CIRCUITS, 'C', GTItems.FIELD_GENERATOR_IV.asItem(), 'D', GTMachines.HULL[GTValues.IV].asItem(), 'E', new MaterialEntry(TagPrefix.cableGtQuadruple, GTMaterials.Tungsten));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_crusher"), MultiBlockC.STEAM_CRUSHER.asItem(),
                "ABA",
                "BCB",
                "DBD",
                'A', new MaterialEntry(TagPrefix.gem, GTMaterials.Diamond), 'B', new MaterialEntry(TagPrefix.gearSmall, GTMaterials.Bronze), 'C', RegistriesUtils.getItemStack("gtceu:hp_steam_macerator"), 'D', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.WroughtIron));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_steam_thermal_centrifuge"), MultiBlockA.LARGE_STEAM_THERMAL_CENTRIFUGE.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'C', new MaterialEntry(TagPrefix.rodLong, GTMaterials.Copper), 'D', MultiBlockA.STEAM_SEPARATOR.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("precision_steam_mechanism"), GTOItems.PRECISION_STEAM_MECHANISM.asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', new MaterialEntry(TagPrefix.rod, GTMaterials.Bronze), 'B', new MaterialEntry(TagPrefix.gearSmall, GTMaterials.Bronze), 'C', new MaterialEntry(TagPrefix.springSmall, GTMaterials.Copper), 'D', RegistriesUtils.getItemStack("enderio:dark_bimetal_gear"), 'E', GTOItems.ULV_FLUID_REGULATOR.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("greenhouse"), MultiBlockD.GREENHOUSE.asItem(),
                "AAA",
                "BCB",
                "DED",
                'A', GTBlocks.CASING_TEMPERED_GLASS.asItem(), 'B', CustomTags.MV_CIRCUITS, 'C', GTMachines.HULL[GTValues.MV].asItem(), 'D', GTItems.ELECTRIC_PISTON_MV.asItem(), 'E', GTItems.ELECTRIC_PUMP_MV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("advanced_assembly_line_unit"), GTOBlocks.ADVANCED_ASSEMBLY_LINE_UNIT.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.HSSG), 'B', new MaterialEntry(TagPrefix.gear, GTMaterials.Rhodium), 'C', CustomTags.UV_CIRCUITS, 'D', GTBlocks.CASING_ASSEMBLY_LINE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_mixer"), MultiBlockA.STEAM_MIXER.asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Bronze), 'B', new MaterialEntry(TagPrefix.rod, GTMaterials.Steel), 'C', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'D', GTBlocks.CASING_BRONZE_PIPE.asItem(), 'E', new MaterialEntry(TagPrefix.gear, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("dragon_stabilizer_core"), GTOItems.DRAGON_STABILIZER_CORE.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plateDouble, GTOMaterials.Draconium), 'B', new MaterialEntry(TagPrefix.rodLong, GTOMaterials.CosmicNeutronium), 'C', GTOItems.STABILIZER_CORE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("sterile_configuration_cleaning_maintenance_hatch"), GTOMachines.STERILE_CONFIGURATION_CLEANING_MAINTENANCE_HATCH.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOMachines.STERILE_CLEANING_MAINTENANCE_HATCH.asItem(), 'B', GTOMachines.CLEANING_CONFIGURATION_MAINTENANCE_HATCH.asItem(), 'C', GTItems.FIELD_GENERATOR_UHV.asItem(), 'D', GTMachines.HULL[GTValues.UHV].asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("wireless_receiver"), new ItemStack(AEItems.WIRELESS_RECEIVER.asItem()),
                "ABA",
                "CDC",
                'A', CustomTags.EV_CIRCUITS, 'B', new ItemStack(AEItems.FLUIX_PEARL.asItem()), 'C', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, GTMaterials.Titanium), 'D', GTItems.SENSOR_HV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("processing_plant"), MultiBlockD.PROCESSING_PLANT.asItem(),
                "ABA",
                "CDE",
                "AFA",
                'A', new MaterialEntry(TagPrefix.foil, GTMaterials.Aluminium), 'B', GTItems.CONVEYOR_MODULE_MV.asItem(), 'C', GTItems.SENSOR_MV.asItem(), 'D', GTOBlocks.MULTI_FUNCTIONAL_CASING.asItem(), 'E', GTItems.EMITTER_MV.asItem(), 'F', GTItems.FLUID_REGULATOR_MV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("neutronium_pipe_casing"), GTOBlocks.AMPROSIUM_PIPE_CASING.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Neutronium), 'B', new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Neutronium), 'C', new MaterialEntry(TagPrefix.frameGt, GTMaterials.Neutronium));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("iridium_pipe_casing"), GTOBlocks.IRIDIUM_PIPE_CASING.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Iridium), 'B', new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Iridium), 'C', new MaterialEntry(TagPrefix.frameGt, GTMaterials.Iridium));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("liquefaction_furnace"), MultiBlockB.LIQUEFACTION_FURNACE.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Invar), 'B', new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.Nickel), 'C', new ItemStack(Blocks.BLAST_FURNACE.asItem()), 'D', GTMachines.EXTRACTOR[GTValues.LV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("iv_lightning_rod"), GTOMachines.LIGHTNING_ROD[GTValues.IV].asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTItems.ENERGY_LAPOTRONIC_ORB.asItem(), 'B', GTMachines.POWER_TRANSFORMER[GTValues.IV].asItem(), 'C', GTMachines.HULL[GTValues.IV].asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("creative_energy_cell"), new ItemStack(AEBlocks.CREATIVE_ENERGY_CELL.block().asItem()),
                "AAA",
                "ABA",
                "AAA",
                'A', new ItemStack(AEBlocks.DENSE_ENERGY_CELL.block().asItem()), 'B', GTItems.FIELD_GENERATOR_UV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("law_configuration_cleaning_maintenance_hatch"), GTOMachines.LAW_CONFIGURATION_CLEANING_MAINTENANCE_HATCH.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOMachines.LAW_CLEANING_MAINTENANCE_HATCH.asItem(), 'B', GTOMachines.STERILE_CONFIGURATION_CLEANING_MAINTENANCE_HATCH.asItem(), 'C', GTItems.FIELD_GENERATOR_UXV.asItem(), 'D', GTMachines.HULL[GTValues.UXV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("uhv_thread_hatch"), GTOMachines.THREAD_HATCH[GTValues.UHV].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', CustomTags.UEV_CIRCUITS, 'B', GTItems.ROBOT_ARM_UHV.asItem(), 'C', GTItems.CONVEYOR_MODULE_UHV.asItem(), 'D', RegistriesUtils.getItemStack("gtceu:uhv_parallel_hatch"), 'E', new MaterialEntry(TagPrefix.wireGtHex, GTMaterials.RutheniumTriniumAmericiumNeutronate), 'F', GTOItems.INTEGRATED_CONTROL_CORE_UHV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_steam_furnace"), MultiBlockA.LARGE_STEAM_FURNACE.asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', new MaterialEntry(TagPrefix.rodLong, GTMaterials.Potin), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'C', RegistriesUtils.getItemStack("enderio:reinforced_obsidian_block"), 'D', GTMultiMachines.STEAM_OVEN.asItem(), 'E', new MaterialEntry(TagPrefix.pipeHugeFluid, GTMaterials.Potin));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("vacuum_hatch"), GTOMachines.VACUUM_HATCH.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTItems.ROBOT_ARM_EV.asItem(), 'B', new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.VanadiumSteel), 'C', GTMachines.PUMP[GTValues.EV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("incubator"), MultiBlockD.INCUBATOR.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTBlocks.PLASTCRETE.asItem(), 'B', GTItems.FIELD_GENERATOR_HV.asItem(), 'C', GTBlocks.FILTER_CASING.asItem(), 'D', MultiBlockD.GREENHOUSE.asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("upgrade_smithing_template"), new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE.asItem()),
                "ABA",
                "ACA",
                "AAA",
                'A', new MaterialEntry(TagPrefix.gem, GTMaterials.Diamond), 'B', new ItemStack(Blocks.NETHERITE_BLOCK.asItem()), 'C', new MaterialEntry(TagPrefix.rock, GTMaterials.Netherrack));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_bath"), MultiBlockA.STEAM_BATH.asItem(),
                "ABA",
                "BCB",
                "DBD",
                'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Rubber), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'C', GTBlocks.CASING_BRONZE_PIPE.asItem(), 'D', new MaterialEntry(TagPrefix.gear, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("iron_rotor"), GTOItems.IRON_ROTOR.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', new ItemStack(Blocks.CHAIN.asItem()), 'B', new MaterialEntry(TagPrefix.turbineBlade, GTMaterials.Iron), 'C', new MaterialEntry(TagPrefix.rodLong, GTMaterials.Invar));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steel_rotor"), GTOItems.STEEL_ROTOR.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', new ItemStack(Blocks.CHAIN.asItem()), 'B', new MaterialEntry(TagPrefix.turbineBlade, GTMaterials.Steel), 'C', new MaterialEntry(TagPrefix.rodLong, GTMaterials.Invar));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("pulsating_photovoltaic_power_station"), GeneratorMultiblock.PHOTOVOLTAIC_POWER_STATION_PULSATING.asItem(),
                "ABA",
                "BCB",
                "ADA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Titanium), 'B', new MaterialEntry(TagPrefix.block, GTOMaterials.RedstoneAlloy), 'C', GTOBlocks.PULSATING_PHOTOVOLTAIC_BLOCK.asItem(), 'D', CustomTags.HV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("blaze_blast_furnace"), MultiBlockD.BLAZE_BLAST_FURNACE.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTOBlocks.BLAZE_CASING.asItem(), 'B', GTItems.FIELD_GENERATOR_IV.asItem(), 'C', GTMultiMachines.ELECTRIC_BLAST_FURNACE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_semi_fluid"), GTOMachines.SEMI_FLUID_GENERATOR[GTValues.ULV].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTOItems.ULV_ELECTRIC_PISTON.asItem(), 'B', CustomTags.ULV_CIRCUITS, 'C', GTOItems.ULV_ELECTRIC_MOTOR.asItem(), 'D', GTMachines.HULL[GTValues.ULV].asItem(), 'E', new MaterialEntry(TagPrefix.gear, GTMaterials.Stone), 'F', new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.Lead));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("multi_functional_casing"), GTOBlocks.MULTI_FUNCTIONAL_CASING.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.Aluminium), 'B', GTItems.ROBOT_ARM_MV.asItem(), 'C', GTItems.ELECTRIC_PISTON_MV.asItem(), 'D', GTBlocks.CASING_STEEL_SOLID.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_pyrolyse_oven"), MultiBlockA.LARGE_PYROLYSE_OVEN.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.StainlessSteel), 'B', GTItems.FIELD_GENERATOR_IV.asItem(), 'C', new MaterialEntry(TagPrefix.pipeHugeFluid, GTMaterials.VanadiumSteel), 'D', GTMultiMachines.PYROLYSE_OVEN.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("thermal_power_pump"), MultiBlockC.THERMAL_POWER_PUMP.asItem(),
                "ABA",
                "CDE",
                "FGF",
                'A', new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.Copper), 'B', RegistriesUtils.getItemStack("gtceu:hp_steam_compressor"), 'C', GTOBlocks.REINFORCED_WOOD_CASING.asItem(), 'D', GTMultiMachines.PRIMITIVE_PUMP.asItem(), 'E', RegistriesUtils.getItemStack("gtceu:pump_hatch"), 'F', new MaterialEntry(TagPrefix.plate, GTMaterials.Brass), 'G', RegistriesUtils.getItemStack("gtceu:hp_steam_extractor"));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("gravity_hatch"), GTOMachines.GRAVITY_HATCH.asItem(),
                "ABA",
                "BCB",
                "DBD",
                'A', GTItems.ROBOT_ARM_UV.asItem(), 'B', GTItems.GRAVI_STAR.asItem(), 'C', GTMachines.HULL[GTValues.UV].asItem(), 'D', GTItems.GRAVITATION_ENGINE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("astra_nasa_workbench"), RegistriesUtils.getItemStack("ad_astra:nasa_workbench"),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.ROBOT_ARM_HV.asItem(), 'B', GTItems.EMITTER_HV.asItem(), 'C', new ItemStack(Blocks.REDSTONE_TORCH.asItem()), 'D', RegistriesUtils.getItemStack("avaritia:compressed_crafting_table"), 'E', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.Steel), 'F', new MaterialEntry(TagPrefix.block, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("flint_axe"), RegistriesUtils.getItemStack("gtceu:flint_axe", 1, "{DisallowContainerItem:0b,GT.Behaviours:{DisableShields:1b},GT.Tool:{AttackDamage:6.0f,AttackSpeed:-3.2f,Damage:0,HarvestLevel:2,MaxDamage:64,ToolSpeed:3.5f},HideFlags:2}"),
                "AA",
                "BC",
                'A', new MaterialEntry(TagPrefix.gem, GTMaterials.Flint), 'B', GTOItems.PLANT_FIBER.asItem(), 'C', new MaterialEntry(TagPrefix.rod, GTMaterials.Wood));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("command_wand"), GTOItems.COMMAND_WAND.asItem(),
                "  A",
                " B ",
                "B  ",
                'A', GTOItems.COMMAND_BLOCK_CORE.asItem(), 'B', new MaterialEntry(TagPrefix.rod, GTOMaterials.Eternity));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("basic_card"), new ItemStack(AEItems.BASIC_CARD.asItem(), 2),
                "AB ",
                "CDB",
                "EB ",
                'A', new MaterialEntry(TagPrefix.wireFine, GTMaterials.RedAlloy), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Aluminium), 'C', new ItemStack(AEItems.ENGINEERING_PROCESSOR.asItem()), 'D', CustomTags.MV_CIRCUITS, 'E', new MaterialEntry(TagPrefix.wireFine, GTMaterials.Gold));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_piston_hammer"), MultiBlockA.STEAM_PISTON_HAMMER.asItem(),
                "ABA",
                "CDC",
                "AEA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'B', new MaterialEntry(TagPrefix.ring, GTMaterials.WroughtIron), 'C', new MaterialEntry(TagPrefix.spring, GTMaterials.Iron), 'D', RegistriesUtils.getItemStack("gtceu:lp_steam_forge_hammer"), 'E', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.WroughtIron));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("shrieker"), new ItemStack(Blocks.SCULK_SHRIEKER.asItem()),
                " A ",
                "ABA",
                " A ",
                'A', new MaterialEntry(TagPrefix.rod, GTMaterials.EchoShard), 'B', new ItemStack(Blocks.SCULK_SENSOR.asItem()));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("vacuum_configuration_hatch"), GTOMachines.VACUUM_CONFIGURATION_HATCH.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOMachines.VACUUM_HATCH.asItem(), 'B', CustomTags.UHV_CIRCUITS, 'C', GTItems.GRAVI_STAR.asItem(), 'D', GTOMachines.AUTO_CONFIGURATION_MAINTENANCE_HATCH.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("stabilizer_core"), GTOItems.STABILIZER_CORE.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plateDouble, GTOMaterials.Infuscolium), 'B', new MaterialEntry(TagPrefix.rodLong, GTMaterials.Neutronium), 'C', GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("machine_casing_circuit_assembly_line"), GTOBlocks.MACHINE_CASING_CIRCUIT_ASSEMBLY_LINE.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plate, GTOMaterials.Pikyonium), 'B', new MaterialEntry(TagPrefix.gear, GTMaterials.HSSG), 'C', GTItems.ROBOT_ARM_LuV.asItem(), 'D', new MaterialEntry(TagPrefix.frameGt, GTMaterials.Ruridit));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("mega_alloy_blast_smelter"), GCYMMachines.MEGA_ALLOY_BLAST_SMELTER.asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', new MaterialEntry(TagPrefix.spring, GTMaterials.NaquadahAlloy), 'B', CustomTags.ZPM_CIRCUITS, 'C', GTItems.FIELD_GENERATOR_ZPM.asItem(), 'D', GCYMMachines.BLAST_ALLOY_SMELTER.asItem(), 'E', new MaterialEntry(TagPrefix.plateDense, GTMaterials.Darmstadtium), 'F', new MaterialEntry(TagPrefix.wireGtHex, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("uiv_thread_hatch"), GTOMachines.THREAD_HATCH[GTValues.UIV].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', CustomTags.UXV_CIRCUITS, 'B', GTItems.ROBOT_ARM_UIV.asItem(), 'C', GTItems.CONVEYOR_MODULE_UIV.asItem(), 'D', RegistriesUtils.getItemStack("gtceu:uiv_parallel_hatch"), 'E', new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.Echoite), 'F', GTOItems.INTEGRATED_CONTROL_CORE_UIV.asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("creative_laser_hatch"), RegistriesUtils.getItemStack("gtmthings:creative_laser_hatch"),
                "ABA",
                "B B",
                "ABA",
                'A', new ItemStack(Blocks.CHAIN_COMMAND_BLOCK.asItem()), 'B', GTOItems.CHAOTIC_ENERGY_CORE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("rocket_large_turbine"), GeneratorMultiblock.ROCKET_LARGE_TURBINE.asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.ELECTRIC_PISTON_EV.asItem(), 'B', CustomTags.IV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_EV.asItem(), 'D', GTOMachines.ROCKET_ENGINE_GENERATOR[GTValues.EV].asItem(), 'E', new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.BlackSteel), 'F', new MaterialEntry(TagPrefix.plateDense, GTMaterials.Obsidian));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("energetic_photovoltaic_power_station"), GeneratorMultiblock.PHOTOVOLTAIC_POWER_STATION_ENERGETIC.asItem(),
                "ABA",
                "BCB",
                "ADA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'B', new MaterialEntry(TagPrefix.block, GTOMaterials.CopperAlloy), 'C', GTOBlocks.ENERGETIC_PHOTOVOLTAIC_BLOCK.asItem(), 'D', CustomTags.MV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("reaction_furnace"), MultiBlockB.REACTION_FURNACE.asItem(),
                "ABA",
                "CDC",
                "BAB",
                'A', GTMachines.ELECTRIC_FURNACE[GTValues.MV].asItem(), 'B', RegistriesUtils.getItemStack("gtceu:gold_drum"), 'C', new MaterialEntry(TagPrefix.cableGtOctal, GTMaterials.Iron), 'D', GTMachines.CHEMICAL_REACTOR[GTValues.LV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("aggregatione_core"), GTOBlocks.AGGREGATIONE_CORE.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.AttunedTengam), 'B', GTOBlocks.INFUSED_OBSIDIAN.asItem(), 'C', GTOBlocks.MAGIC_CORE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("luv_lightning_rod"), GTOMachines.LIGHTNING_ROD[GTValues.LuV].asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTItems.ENERGY_LAPOTRONIC_ORB_CLUSTER.asItem(), 'B', GTMachines.POWER_TRANSFORMER[GTValues.LuV].asItem(), 'C', GTMachines.HULL[GTValues.LuV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("desulfurizer"), MultiBlockB.DESULFURIZER.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTItems.ELECTRIC_PUMP_HV.asItem(), 'B', GTItems.ELECTRIC_MOTOR_HV.asItem(), 'C', CustomTags.EV_CIRCUITS, 'D', GTMachines.HULL[GTValues.HV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("cold_ice_freezer"), MultiBlockD.COLD_ICE_FREEZER.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTOBlocks.COLD_ICE_CASING.asItem(), 'B', GTItems.EMITTER_IV.asItem(), 'C', GTMultiMachines.VACUUM_FREEZER.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_steam_macerator"), MultiBlockA.LARGE_STEAM_MACERATOR.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.block, GTMaterials.Bronze), 'B', RegistriesUtils.getItemStack("enderio:energetic_alloy_grinding_ball"), 'C', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'D', GTMultiMachines.STEAM_GRINDER.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("luv_rocket_engine"), GTOMachines.ROCKET_ENGINE_GENERATOR[GTValues.LuV].asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', new MaterialEntry(TagPrefix.rotor, GTMaterials.RhodiumPlatedPalladium), 'B', CustomTags.LuV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_LuV.asItem(), 'D', GTMachines.HULL[GTValues.LuV].asItem(), 'E', new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.Osmium), 'F', GTItems.ELECTRIC_PUMP_LuV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("luv_naquadah_reactor"), GTOMachines.NAQUADAH_REACTOR_GENERATOR[GTValues.LuV].asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', new MaterialEntry(TagPrefix.rod, GTMaterials.NaquadahEnriched), 'B', CustomTags.LuV_CIRCUITS, 'C', GTItems.FIELD_GENERATOR_LuV.asItem(), 'D', GTMachines.HULL[GTValues.LuV].asItem(), 'E', new MaterialEntry(TagPrefix.cableGtQuadruple, GTMaterials.NiobiumNitride));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("neutronium_gearbox"), GTOBlocks.AMPROSIUM_GEARBOX.asItem(),
                "ABA",
                "CDC",
                "AEA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Neutronium), 'B', TagUtils.createTag(RLUtils.forge("tools/hammers")), 'C', new MaterialEntry(TagPrefix.gear, GTMaterials.Neutronium), 'D', new MaterialEntry(TagPrefix.frameGt, GTMaterials.Neutronium), 'E', TagUtils.createTag(RLUtils.forge("tools/wrench")));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("iridium_gearbox"), GTOBlocks.IRIDIUM_GEARBOX.asItem(),
                "ABA",
                "CDC",
                "AEA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Iridium), 'B', TagUtils.createTag(RLUtils.forge("tools/hammers")), 'C', new MaterialEntry(TagPrefix.gear, GTMaterials.Iridium), 'D', new MaterialEntry(TagPrefix.frameGt, GTMaterials.Iridium), 'E', TagUtils.createTag(RLUtils.forge("tools/wrench")));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("lava_furnace"), MultiBlockA.LAVA_FURNACE.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.Copper), 'B', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.WroughtIron), 'C', new MaterialEntry(TagPrefix.cableGtHex, GTMaterials.Tin), 'D', GTMultiMachines.STEAM_OVEN.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("dragon_egg_copier"), MultiBlockA.DRAGON_EGG_COPIER.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOBlocks.DRAGON_STRENGTH_TRITANIUM_CASING.asItem(), 'B', GTItems.FIELD_GENERATOR_UXV.asItem(), 'C', GTItems.ROBOT_ARM_UXV.asItem(), 'D', new ItemStack(Blocks.DRAGON_EGG.asItem()));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_ore_washer"), MultiBlockA.STEAM_ORE_WASHER.asItem(),
                "ABA",
                "BCB",
                "DBD",
                'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Rubber), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'C', GTBlocks.CASING_BRONZE_PIPE.asItem(), 'D', new MaterialEntry(TagPrefix.gear, GTMaterials.Potin));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("ulv_output_hatch"), GTMachines.FLUID_EXPORT_HATCH[GTValues.ULV].asItem(),
                " A ",
                "hBw",
                " C ",
                'B', GTItems.STICKY_RESIN.asItem(), 'C', new MaterialEntry(TagPrefix.block, GTMaterials.Glass), 'A', GTMachines.HULL[GTValues.ULV].asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("ulv_input_hatch"), GTMachines.FLUID_IMPORT_HATCH[GTValues.ULV].asItem(),
                " A ",
                "hBw",
                " C ",
                'B', GTItems.STICKY_RESIN.asItem(), 'C', GTMachines.HULL[GTValues.ULV].asItem(), 'A', new MaterialEntry(TagPrefix.block, GTMaterials.Glass));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("ulv_output_bus"), GTMachines.ITEM_EXPORT_BUS[GTValues.ULV].asItem(),
                " A ",
                "hBw",
                " C ",
                'B', GTItems.STICKY_RESIN.asItem(), 'C', new ItemStack(Blocks.CHEST.asItem()), 'A', GTMachines.HULL[GTValues.ULV].asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("ulv_input_bus"), GTMachines.ITEM_IMPORT_BUS[GTValues.ULV].asItem(),
                " A ",
                "hBw",
                " C ",
                'B', GTItems.STICKY_RESIN.asItem(), 'C', GTMachines.HULL[GTValues.ULV].asItem(), 'A', new ItemStack(Blocks.CHEST.asItem()));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("wireless_energy_substation"), MultiBlockG.WIRELESS_ENERGY_SUBSTATION.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'B', CustomTags.LV_CIRCUITS, 'D', new MaterialEntry(TagPrefix.frameGt, GTMaterials.Invar), 'C', new MaterialEntry(TagPrefix.plate, GTMaterials.EnderPearl), 'A', new MaterialEntry(TagPrefix.plateDense, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("boss_summoner"), MultiBlockG.BOSS_SUMMONER.asItem(),
                "ABA",
                "BCB",
                "DBD",
                'B', new MaterialEntry(TagPrefix.wireGtSingle, GTMaterials.Lead), 'D', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, GTMaterials.Steel), 'C', GTBlocks.MACHINE_CASING_ULV.asItem(), 'A', GTItems.VOLTAGE_COIL_ULV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_pump"), GTOItems.STEAM_PUMP.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'B', new MaterialEntry(TagPrefix.pipeHugeFluid, GTMaterials.Steel), 'D', GTItems.FLUID_REGULATOR_LV.asItem(), 'C', new MaterialEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Copper), 'A', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, GTMaterials.Electrum));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("tree_growth_simulator"), MultiBlockG.TREE_GROWTH_SIMULATOR.asItem(),
                "ABA",
                "CDC",
                "EFE",
                'B', GTItems.CONVEYOR_MODULE_LV.asItem(), 'F', GTItems.VOLTAGE_COIL_LV.asItem(), 'D', GTMachines.HULL[GTValues.LV].asItem(), 'C', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, GTMaterials.Magnalium), 'E', GTItems.ROBOT_ARM_LV.asItem(), 'A', new MaterialEntry(TagPrefix.pipeNormalRestrictive, GTMaterials.Brass));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("drone_control_center"), MultiBlockG.DRONE_CONTROL_CENTER.asItem(),
                "ABA",
                "CDC",
                "EEE",
                'B', GTItems.ROBOT_ARM_HV.asItem(), 'D', GTMachines.HULL[GTValues.HV].asItem(), 'C', GTItems.SENSOR_HV.asItem(), 'E', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.StainlessSteel), 'A', GTItems.EMITTER_HV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("three_dimensional_printer"), MultiBlockC.THREE_DIMENSIONAL_PRINTER.asItem(),
                "ABA",
                "CDC",
                "EFE",
                'B', GTItems.CONVEYOR_MODULE_HV.asItem(), 'F', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.Titanium), 'D', GTMachines.HULL[GTValues.HV].asItem(), 'C', GTItems.FLUID_REGULATOR_HV.asItem(), 'E', GTItems.ROBOT_ARM_HV.asItem(), 'A', GTItems.SENSOR_HV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ev_drone_hatch"), GTOMachines.DRONE_HATCH[GTValues.EV].asItem(),
                "ABA",
                "CDC",
                "ABA",
                'B', CustomTags.EV_CIRCUITS, 'D', GTMachines.ITEM_IMPORT_BUS[GTValues.EV].asItem(), 'C', GTItems.ROBOT_ARM_EV.asItem(), 'A', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, GTMaterials.Titanium));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("iv_drone_hatch"), GTOMachines.DRONE_HATCH[GTValues.IV].asItem(),
                "ABA",
                "CDC",
                "ABA",
                'B', CustomTags.IV_CIRCUITS, 'D', GTMachines.ITEM_IMPORT_BUS[GTValues.IV].asItem(), 'C', GTItems.ROBOT_ARM_IV.asItem(), 'A', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, GTMaterials.TungstenSteel));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("hv_drone_hatch"), GTOMachines.DRONE_HATCH[GTValues.HV].asItem(),
                "ABA",
                "CDC",
                "ABA",
                'B', CustomTags.HV_CIRCUITS, 'D', GTMachines.ITEM_IMPORT_BUS[GTValues.HV].asItem(), 'C', GTItems.ROBOT_ARM_HV.asItem(), 'A', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, GTMaterials.StainlessSteel));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("heater"), GTOMachines.HEATER.asItem(),
                "AAA",
                "BCB",
                "BDB",
                'B', new MaterialEntry(TagPrefix.bolt, GTMaterials.WroughtIron), 'D', GTBlocks.STEEL_BRICKS_HULL.asItem(), 'C', new ItemStack(Blocks.FURNACE.asItem()), 'A', new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("boiler"), GTOMachines.BOILER.asItem(),
                "AAA",
                "A A",
                "BCB",
                'B', new MaterialEntry(TagPrefix.bolt, GTMaterials.WroughtIron), 'C', GTBlocks.STEEL_BRICKS_HULL.asItem(), 'A', new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("machine_access_interface"), GTOMachines.MACHINE_ACCESS_INTERFACE.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'B', GTItems.COVER_FLUID_DETECTOR_ADVANCED.asItem(), 'D', GTMachines.HULL[GTValues.IV].asItem(), 'C', GTItems.COVER_ITEM_DETECTOR_ADVANCED.asItem(), 'A', GTItems.SENSOR_IV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("luv_processing_array"), MultiBlockG.PROCESSING_ARRAY[GTValues.LuV].asItem(),
                "ABA",
                "BCB",
                "ABA",
                'B', CustomTags.LuV_CIRCUITS, 'C', GTItems.EMITTER_LuV.asItem(), 'A', new MaterialEntry(TagPrefix.plate, GTMaterials.HSSE));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("iv_processing_array"), MultiBlockG.PROCESSING_ARRAY[GTValues.IV].asItem(),
                "ABA",
                "BCB",
                "ABA",
                'B', CustomTags.IV_CIRCUITS, 'C', GTItems.EMITTER_IV.asItem(), 'A', new MaterialEntry(TagPrefix.plate, GTMaterials.TungstenSteel));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("void_transporter"), MultiBlockG.VOID_TRANSPORTER.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'B', GTItems.FIELD_GENERATOR_MV.asItem(), 'C', RegistriesUtils.getItemStack("enderio:reinforced_obsidian_block"), 'A', GTItems.CARBON_FIBER_PLATE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("electric_cooking"), MultiBlockG.ELECTRIC_COOKING.asItem(),
                "ABA",
                "CDC",
                "EFE",
                'B', GTItems.ELECTRIC_PUMP_HV.asItem(), 'F', GTItems.ELECTRIC_MOTOR_HV.asItem(), 'D', RegistriesUtils.getItemStack("farmersdelight:cooking_pot"), 'C', new MaterialEntry(TagPrefix.pipeQuadrupleFluid, GTMaterials.StainlessSteel), 'E', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.StainlessSteel), 'A', CustomTags.HV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("isostatic_press"), MultiBlockG.ISOSTATIC_PRESS.asItem(),
                "ABA",
                "CDC",
                "EEE",
                'B', GTItems.ELECTRIC_PISTON_EV.asItem(), 'D', GTMachines.COMPRESSOR[GTValues.HV].asItem(), 'C', new MaterialEntry(TagPrefix.cableGtQuadruple, GTMaterials.Nichrome), 'E', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.Titanium), 'A', new MaterialEntry(TagPrefix.rodLong, GTMaterials.Titanium));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("sintering_furnace"), MultiBlockG.SINTERING_FURNACE.asItem(),
                "ABA",
                "BCB",
                "DBD",
                'B', new MaterialEntry(TagPrefix.cableGtQuadruple, GTMaterials.Kanthal), 'D', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.StainlessSteel), 'C', GTMachines.ELECTRIC_FURNACE[GTValues.HV].asItem(), 'A', CustomTags.HV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("spool_hatch"), GTOMachines.SPOOL_HATCH.asItem(),
                "ABA",
                "BCB",
                "DBD",
                'B', SPOOLS_LARGE.asItem(), 'D', GTItems.ELECTRIC_MOTOR_IV, 'C', GTMachines.HULL[GTValues.IV].asItem(), 'A', CustomTags.IV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("drawing_tower"), MultiBlockG.DRAWING_TOWER.asItem(),
                "ANA",
                "MCM",
                "DBD",
                'B', SPOOLS_LARGE.asItem(), 'M', new MaterialEntry(TagPrefix.spring, GTMaterials.HSLASteel), 'N', new MaterialEntry(TagPrefix.wireGtDouble, GTMaterials.Tungsten), 'D', GTItems.ELECTRIC_MOTOR_IV, 'C', GTMachines.HULL[GTValues.IV].asItem(), 'A', CustomTags.IV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("satellite_control_center"), MultiBlockG.SATELLITE_CONTROL_CENTER.asItem(),
                "ABA",
                "CDC",
                "EFE",
                'B', GTItems.EMITTER_HV.asItem(), 'F', new ItemStack(AEItems.WIRELESS_BOOSTER.asItem()), 'D', GTMachines.HULL[GTValues.HV].asItem(), 'C', CustomTags.HV_CIRCUITS, 'E', new MaterialEntry(TagPrefix.rodLong, GTMaterials.StainlessSteel), 'A', GTItems.SENSOR_HV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("performance_monitor"), GTOMachines.PERFORMANCE_MONITOR.asItem(),
                "AAA",
                "ABA",
                "AAA",
                'B', GTItems.PORTABLE_SCANNER.asItem(), 'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("drilling_control_center"), MultiBlockG.DRILLING_CONTROL_CENTER.asItem(),
                "ABA",
                "BCB",
                "DBD",
                'B', GTItems.FIELD_GENERATOR_IV.asItem(), 'D', new MaterialEntry(TagPrefix.wireGtHex, GTMaterials.SamariumIronArsenicOxide), 'C', GTMachines.WORLD_ACCELERATOR[GTValues.EV].asItem(), 'A', GTItems.SENSOR_IV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_arc_generator"), GCYMMachines.LARGE_ARC_GENERATOR.asItem(),
                "ABA",
                "BCB",
                "DBD",
                'B', GTOBlocks.MAGNESIUM_OXIDE_CERAMIC_HIGH_TEMPERATURE_INSULATION_MECHANICAL_BLOCK.asItem(), 'D', new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.EndSteel), 'C', GTOMachines.ARC_GENERATOR[GTValues.IV].asItem(), 'A', new MaterialEntry(TagPrefix.wireGtHex, GTMaterials.UraniumTriplatinum));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("rocket_assembler"), MultiBlockG.ROCKET_ASSEMBLER.asItem(),
                "ACA",
                "BDB",
                "AEA",
                'A', GTOItems.HEAVY_DUTY_PLATE_1.asItem(), 'B', GTItems.CONVEYOR_MODULE_HV, 'C', GTItems.EMITTER_HV.asItem(), 'D', RegistriesUtils.getItemStack("ad_astra:nasa_workbench"), 'E', GTItems.SENSOR_HV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("polymerization_reactor"), MultiBlockG.POLYMERIZATION_REACTOR.asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', new MaterialEntry(TagPrefix.pipeHugeFluid, GTMaterials.VanadiumSteel), 'B', GTItems.ELECTRIC_PUMP_HV.asItem(), 'C', new MaterialEntry(TagPrefix.pipeHugeFluid, GTMaterials.StainlessSteel), 'D', GTBlocks.COIL_KANTHAL.asItem(), 'E', CustomTags.EV_CIRCUITS, 'F', GTMachines.HULL[GTValues.HV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("electric_heater"), GTOMachines.ELECTRIC_HEATER.asItem(),
                "ABA",
                "BCB",
                "DBD",
                'A', new MaterialEntry(TagPrefix.rodLong, GTMaterials.AnnealedCopper), 'B', new MaterialEntry(TagPrefix.wireGtOctal, GTMaterials.Cupronickel), 'C', GTMachines.HULL[GTValues.LV].asItem(), 'D', new MaterialEntry(TagPrefix.plate, GTMaterials.AnnealedCopper));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_lathe"), GTOMachines.ULV_LATHE[GTValues.ULV].asItem(),
                "ABA",
                "CDE",
                "BAF",
                'A', new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.RedAlloy), 'B', CustomTags.ULV_CIRCUITS, 'C', GTOItems.ULV_ELECTRIC_MOTOR.asItem(), 'D', GTMachines.HULL[GTValues.ULV].asItem(), 'E', new MaterialEntry(TagPrefix.gem, GTMaterials.Diamond), 'F', GTOItems.ULV_ELECTRIC_PISTON.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_wiremill"), GTOMachines.ULV_WIREMILL[GTValues.ULV].asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOItems.ULV_ELECTRIC_MOTOR.asItem(), 'B', new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.RedAlloy), 'C', CustomTags.ULV_CIRCUITS, 'D', GTMachines.HULL[GTValues.ULV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("chemical_vapor_deposition"), MultiBlockC.CHEMICAL_VAPOR_DEPOSITION.asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', new MaterialEntry(TagPrefix.pipeTinyFluid, GTMaterials.Polytetrafluoroethylene), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.StainlessSteel), 'C', GTItems.FLUID_REGULATOR_HV.asItem(), 'D', GTMultiMachines.LARGE_CHEMICAL_REACTOR.asItem(), 'E', new MaterialEntry(TagPrefix.pipeTinyFluid, GTMaterials.TungstenCarbide));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("physical_vapor_deposition"), MultiBlockC.PHYSICAL_VAPOR_DEPOSITION.asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', new MaterialEntry(TagPrefix.pipeTinyFluid, GTMaterials.Polybenzimidazole), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.TungstenSteel), 'C', GTItems.FLUID_REGULATOR_EV.asItem(), 'D', GTMachines.HULL[GTValues.EV].asItem(), 'E', new MaterialEntry(TagPrefix.pipeTinyFluid, GTMaterials.Chromium));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("energy_injector"), MultiBlockC.ENERGY_INJECTOR.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTBlocks.SUPERCONDUCTING_COIL.asItem(), 'B', GTMachines.HI_AMP_TRANSFORMER_2A[GTValues.LuV].asItem(), 'C', GTMachines.CHARGER_4[GTValues.LuV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("crystallization_chamber"), MultiBlockG.CRYSTALLIZATION_CHAMBER.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTBlocks.COIL_CUPRONICKEL.asItem(), 'B', new MaterialEntry(TagPrefix.pipeHugeFluid, GTMaterials.Gold), 'C', GTItems.FLUID_REGULATOR_MV.asItem(), 'D', GTMachines.AUTOCLAVE[GTValues.LV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("algae_farm"), MultiBlockG.ALGAE_FARM.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, GTMaterials.StainlessSteel), 'B', new MaterialEntry(TagPrefix.rodLong, GTMaterials.StainlessSteel), 'C', new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Aluminium), 'D', GTMachines.FERMENTER[GTValues.HV].asItem());
        if (GTOCore.isExpert()) {
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("time_twister"), GTOItems.TIME_TWISTER.asItem(),
                    "ABA",
                    "CDC",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.plateDouble, GTOMaterials.Gaia), 'B', RegistriesUtils.getItemStack("gtmthings:ev_4a_wireless_energy_receive_cover"), 'C', RegistriesUtils.getItemStack("ars_nouveau:manipulation_essence"), 'D', GTItems.FIELD_GENERATOR_EV.asItem());
        } else if (GTOCore.isNormal()) {
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("time_twister"), GTOItems.TIME_TWISTER.asItem(),
                    "ABA",
                    "CDC",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.plateDouble, GTOMaterials.Gaiasteel), 'B', RegistriesUtils.getItemStack("gtmthings:hv_4a_wireless_energy_receive_cover"), 'C', RegistriesUtils.getItemStack("ars_nouveau:manipulation_essence"), 'D', GTItems.FIELD_GENERATOR_HV.asItem());

        } else {
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("time_twister"), GTOItems.TIME_TWISTER.asItem(),
                    "ABA",
                    "CDC",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.plateDouble, GTOMaterials.Terrasteel), 'B', RegistriesUtils.getItemStack("gtmthings:mv_4a_wireless_energy_receive_cover"), 'C', RegistriesUtils.getItemStack("ars_nouveau:manipulation_essence"), 'D', GTItems.FIELD_GENERATOR_MV.asItem());

        }
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_coke_oven"), MultiBlockG.LARGE_COKE_OVEN.asItem(),
                "AhA",
                "BCB",
                "DDD",
                'A', new MaterialEntry(TagPrefix.rod, GTMaterials.Steel), 'B', new MaterialEntry(TagPrefix.springSmall, GTMaterials.Steel), 'C', GTMultiMachines.COKE_OVEN.asItem(), 'D', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("gas_compressor"), MultiBlockG.GAS_COMPRESSOR.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', new MaterialEntry(TagPrefix.pipeHugeFluid, GTMaterials.Steel), 'B', new MaterialEntry(TagPrefix.plateDense, GTMaterials.Steel), 'C', GTItems.FLUID_REGULATOR_LV.asItem(), 'D', GTMachines.COMPRESSOR[GTValues.LV].asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("grass_harvester"), GTOItems.GRASS_HARVESTER.asItem(),
                "AA ",
                "B A",
                "B  ",
                'A', new ItemStack(Items.FLINT.asItem()), 'B', new ItemStack(Items.STICK.asItem()));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("order"), GTOItems.ORDER.asItem(), 1,
                " A ",
                "ABA",
                " A ",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Paper), 'B', CustomTags.MV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("infinite_intake_hatch"), GTOMachines.INFINITE_INTAKE_HATCH.asItem(),
                "ABA",
                "BCB",
                "ABA",
                'A', new MaterialEntry(TagPrefix.pipeTinyFluid, GTMaterials.Potin), 'B', GTOItems.AIR_VENT.asItem(), 'C', GTMachines.FLUID_IMPORT_HATCH[GTValues.ULV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("infinite_water_hatch"), GTOMachines.INFINITE_WATER_HATCH.asItem(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTItems.COVER_INFINITE_WATER.asItem(), 'B', RegistriesUtils.getItemStack("gtceu:tungsten_steel_drum"), 'C', GTItems.ELECTRIC_PUMP_IV.asItem(), 'D', RegistriesUtils.getItemStack("gtceu:reservoir_hatch"));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("fluid_cell"), GTItems.FLUID_CELL.asItem(),
                " Ah",
                "CDC",
                "hA ",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Tin), 'C', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, GTMaterials.Tin), 'D', new ItemStack(Items.GLASS_PANE.asItem()));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_vent_hatch"), GTOMachines.STEAM_VENT_HATCH.asItem(),
                "ABA",
                "ACA",
                "ABA",
                'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'B', new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Bronze), 'C', new MaterialEntry(TagPrefix.rotor, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_crystallization_chamber"), MultiBlockG.LARGE_CRYSTALLIZATION_CHAMBER.asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', CustomTags.UV_CIRCUITS, 'B', new MaterialEntry(TagPrefix.spring, GTMaterials.Naquadah), 'C', GTItems.FIELD_GENERATOR_LuV.asItem(), 'D', MultiBlockG.CRYSTALLIZATION_CHAMBER.asItem(), 'E', GTItems.SENSOR_LuV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_alchemical_device"), ManaMultiBlock.LARGE_ALCHEMICAL_DEVICE.asItem(),
                "AAA",
                "ABA",
                "ACA",
                'A', new MaterialEntry(TagPrefix.plate, GTOMaterials.Herbs), 'B', ManaMachine.ALCHEMY_CAULDRON.asItem(), 'C', GCYMMachines.LARGE_BREWER.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("compressed_chest"), RegistriesUtils.getItemStack("avaritia:compressed_chest"),
                "ABA",
                "BCB",
                "ABA",
                'A', RegistriesUtils.getItemStack("gtceu:wood_crate"), 'B', new ItemStack(Items.CHEST.asItem()), 'C', new ItemStack(AEBlocks.SMOOTH_SKY_STONE_CHEST.block().asItem()));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("structure_detect"), GTOItems.STRUCTURE_DETECT.asItem(),
                " A ",
                "ABA",
                " A ",
                'A', new ItemStack(Items.REDSTONE_TORCH.asItem()), 'B', RegistriesUtils.getItemStack("gtmthings:advanced_terminal"));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("tesseract_generator"), GTOMachines.TESSERACT_GENERATOR.asItem(),
                "ABA",
                "CDC",
                "EBE",
                'A', RegistriesUtils.getItemStack("botania:ender_hand"), 'B', RegistriesUtils.getItemStack("gtmthings:advanced_wireless_item_transfer_cover"), 'C', RegistriesUtils.getItemStack("gtmthings:advanced_wireless_fluid_transfer_cover"), 'D', GTOItems.ENTANGLED_SINGULARITY.asItem(), 'E', GTItems.FIELD_GENERATOR_MV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("solar_heat_collector"), GTOBlocks.SOLAR_HEAT_COLLECTOR_PIPE_CASING.asItem(),
                "AAA",
                "BBB",
                "CCC",
                'A', new ItemStack(Items.TINTED_GLASS.asItem()), 'B', new MaterialEntry(TagPrefix.pipeTinyFluid, GTMaterials.Steel), 'C', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.Silver));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("digital_miner"), MultiBlockH.DIGITAL_MINER.asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTOItems.ULV_ROBOT_ARM.asItem(), 'C', CustomTags.LV_CIRCUITS,
                'B', new MaterialEntry(TagPrefix.wireFine, GTMaterials.Zinc), 'D', GTBlocks.CASING_STEEL_SOLID.asItem(),
                'E', new MaterialEntry(TagPrefix.pipeHugeRestrictive, GTMaterials.CobaltBrass), 'F', new MaterialEntry(TagPrefix.toolHeadDrill, GTMaterials.Steel));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("electroplating_bath"), MultiBlockH.ELECTROPLATING_BATH.asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', GCYMBlocks.ELECTROLYTIC_CELL.asItem(), 'B', CustomTags.IV_CIRCUITS, 'C', GTOBlocks.CHEMICAL_GRADE_GLASS.asItem(), 'D', GTBlocks.HERMETIC_CASING_IV.asItem(), 'E', GTItems.ELECTRIC_PUMP_IV.asItem(), 'F', GTItems.ELECTRIC_MOTOR_IV.asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("atomizing_condenser"), MultiBlockH.ATOMIZING_CONDENSER.asItem(),
                "ABA",
                "CDC",
                "EFE",
                'A', CustomTags.IV_CIRCUITS, 'B', GTItems.ELECTRIC_PUMP_EV.asItem(), 'C', new MaterialEntry(TagPrefix.wireGtDouble, GTMaterials.TungstenSteel), 'D', GTItems.FLUID_REGULATOR_EV.asItem(), 'E', GTBlocks.CASING_ALUMINIUM_FROSTPROOF.asItem(), 'F', new MaterialEntry(TagPrefix.ring, GTMaterials.TungstenSteel));
    }
}

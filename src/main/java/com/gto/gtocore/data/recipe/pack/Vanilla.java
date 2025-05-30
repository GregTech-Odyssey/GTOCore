package com.gto.gtocore.data.recipe.pack;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMachines;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.machines.*;
import com.gto.gtocore.utils.RegistriesUtils;
import com.gto.gtocore.utils.TagUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;

import java.util.function.Consumer;

final class Vanilla {

    static void init(Consumer<FinishedRecipe> provider) {
        if (GTCEu.isProd()) {
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("stack_upgrade_tier_1"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_1"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_starter_tier"), GTMachines.SUPER_CHEST[GTValues.MV].asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("advanced_compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:compacting_upgrade"), GTItems.ELECTRIC_PISTON_MV.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.COVER_ITEM_VOIDING.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ITEM_MAGNET_LV.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("stack_upgrade_tier_2"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_2"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_1"), GTMachines.SUPER_CHEST[GTValues.HV].asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("advanced_pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:pickup_upgrade"), GTItems.ITEM_FILTER.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("advanced_refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:refill_upgrade"), GTItems.ROBOT_ARM_MV.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("tank_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:tank_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), RegistriesUtils.getItemStack("gtceu:bronze_drum"));
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ITEM_FILTER.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("advanced_magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:magnet_upgrade"), GTItems.ITEM_MAGNET_HV.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ROBOT_ARM_LV.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("advanced_filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:filter_upgrade"), GTItems.TAG_FILTER.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("stack_upgrade_starter_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_starter_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTMachines.SUPER_CHEST[GTValues.LV].asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("advanced_void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:void_upgrade"), GTItems.COVER_ITEM_VOIDING_ADVANCED.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("auto_blasting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:auto_blasting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:blasting_upgrade"), GTItems.CONVEYOR_MODULE_LV.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("stack_upgrade_tier_4"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_4"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_3"), GTMachines.QUANTUM_CHEST[GTValues.IV].asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("stack_upgrade_tier_3"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_3"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_2"), GTMachines.SUPER_CHEST[GTValues.EV].asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("pump_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:pump_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ELECTRIC_PUMP_LV.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("auto_smoking_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:auto_smoking_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:smoking_upgrade"), GTItems.CONVEYOR_MODULE_LV.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ELECTRIC_PISTON_LV.asStack());
            VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTMachines.ITEM_COLLECTOR[GTValues.LV].asStack());

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("backpack"), RegistriesUtils.getItemStack("sophisticatedbackpacks:backpack"),
                    "ABA",
                    "BCB",
                    "DBD",
                    'A', new UnificationEntry(TagPrefix.screw, GTMaterials.WroughtIron), 'B', new ItemStack(Items.LEATHER.asItem()), 'C', RegistriesUtils.getItemStack("gtceu:wood_crate"), 'D', new ItemStack(Items.STRING.asItem()));
        }
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("pattern_modifier"), GTOItems.PATTERN_MODIFIER.asStack(), RegistriesUtils.getItemStack("expatternprovider:pattern_modifier"));
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("item_storage_cell_64m_2"), GTOItems.ITEM_STORAGE_CELL_64M.asStack(), new ItemStack(AEItems.ITEM_CELL_HOUSING.asItem()), GTOItems.CELL_COMPONENT_64M.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("suprachronal_assembly_line_module"), MultiBlockMachineD.SUPRACHRONAL_ASSEMBLY_LINE_MODULE.asStack(), MultiBlockMachineD.SUPRACHRONAL_ASSEMBLY_LINE.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("fluid_storage_cell_1m"), GTOItems.FLUID_STORAGE_CELL_1M.asStack(), new ItemStack(AEItems.FLUID_CELL_HOUSING.asItem()), GTOItems.CELL_COMPONENT_1M.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("suprachronal_assembly_line"), MultiBlockMachineD.SUPRACHRONAL_ASSEMBLY_LINE.asStack(), MultiBlockMachineD.SUPRACHRONAL_ASSEMBLY_LINE_MODULE.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("item_storage_cell_256m"), GTOItems.ITEM_STORAGE_CELL_256M.asStack(), new ItemStack(AEItems.ITEM_CELL_HOUSING.asItem()), GTOItems.CELL_COMPONENT_256M.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("crafting_storage_4m"), GTOBlocks.CRAFTING_STORAGE_4M.asStack(), new ItemStack(AEBlocks.CRAFTING_UNIT.block().asItem()), GTOItems.CELL_COMPONENT_4M.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("fluid_infinity_cell"), GTOItems.FLUID_INFINITY_CELL.asStack(), new ItemStack(AEItems.FLUID_CELL_HOUSING.asItem()), GTOItems.INFINITE_CELL_COMPONENT.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("crafting_storage_16m"), GTOBlocks.CRAFTING_STORAGE_16M.asStack(), new ItemStack(AEBlocks.CRAFTING_UNIT.block().asItem()), GTOItems.CELL_COMPONENT_16M.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("crafting_storage_256m"), GTOBlocks.CRAFTING_STORAGE_256M.asStack(), new ItemStack(AEBlocks.CRAFTING_UNIT.block().asItem()), GTOItems.CELL_COMPONENT_256M.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("fluid_storage_cell_4m_2"), GTOItems.FLUID_STORAGE_CELL_4M.asStack(), new ItemStack(AEItems.FLUID_CELL_HOUSING.asItem()), GTOItems.CELL_COMPONENT_4M.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("small_flint_dust"), RegistriesUtils.getItemStack("gtceu:small_flint_dust", 3), RegistriesUtils.getItemStack("gtceu:flint_dust"));
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("item_storage_cell_4m_2"), GTOItems.ITEM_STORAGE_CELL_4M.asStack(), new ItemStack(AEItems.ITEM_CELL_HOUSING.asItem()), GTOItems.CELL_COMPONENT_4M.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("crafting_storage_64m"), GTOBlocks.CRAFTING_STORAGE_64M.asStack(), new ItemStack(AEBlocks.CRAFTING_UNIT.block().asItem()), GTOItems.CELL_COMPONENT_64M.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("tiny_flint_dust"), RegistriesUtils.getItemStack("gtceu:tiny_flint_dust", 2), RegistriesUtils.getItemStack("gtceu:small_flint_dust"));
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("item_infinity_cell"), GTOItems.ITEM_INFINITY_CELL.asStack(), new ItemStack(AEItems.ITEM_CELL_HOUSING.asItem()), GTOItems.INFINITE_CELL_COMPONENT.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("fluid_storage_cell_16m"), GTOItems.FLUID_STORAGE_CELL_16M.asStack(), new ItemStack(AEItems.FLUID_CELL_HOUSING.asItem()), GTOItems.CELL_COMPONENT_16M.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("item_storage_cell_1m"), GTOItems.ITEM_STORAGE_CELL_1M.asStack(), new ItemStack(AEItems.ITEM_CELL_HOUSING.asItem()), GTOItems.CELL_COMPONENT_1M.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("item_storage_cell_256m"), GTOItems.ITEM_STORAGE_CELL_256M.asStack(), new ItemStack(AEItems.ITEM_CELL_HOUSING.asItem()), GTOItems.CELL_COMPONENT_256M.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("fluid_storage_cell_64m_2"), GTOItems.FLUID_STORAGE_CELL_64M.asStack(), new ItemStack(AEItems.FLUID_CELL_HOUSING.asItem()), GTOItems.CELL_COMPONENT_64M.asStack());
        VanillaRecipeHelper.addShapelessRecipe(provider, GTOCore.id("crafting_storage_1m"), GTOBlocks.CRAFTING_STORAGE_1M.asStack(), new ItemStack(AEBlocks.CRAFTING_UNIT.block().asItem()), GTOItems.CELL_COMPONENT_1M.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("vibrant_photovoltaic_power_station"), GeneratorMultiblockMachine.PHOTOVOLTAIC_POWER_STATION_VIBRANT.asStack(),
                "ABA",
                "BCB",
                "ADA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.TungstenSteel), 'B', new UnificationEntry(TagPrefix.block, GTOMaterials.DarkSteel), 'C', GTOBlocks.VIBRANT_PHOTOVOLTAIC_BLOCK.asStack(), 'D', CustomTags.EV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("primitive_distillation_tower"), MultiBlockMachineC.PRIMITIVE_DISTILLATION_TOWER.asStack(),
                "ABA",
                "BCB",
                "ADA",
                'A', new UnificationEntry(TagPrefix.plateDouble, GTMaterials.Steel), 'B', new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Lead), 'C', RegistriesUtils.getItemStack("gtceu:hp_steam_solid_boiler"), 'D', new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Potin));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("ev_rocket_engine"), GTOMachines.ROCKET_ENGINE_GENERATOR[GTValues.EV].asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', new UnificationEntry(TagPrefix.rotor, GTMaterials.Lead), 'B', CustomTags.EV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_EV.asStack(), 'D', GTMachines.HULL[GTValues.EV].asStack(), 'E', new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Steel), 'F', GTItems.ELECTRIC_PUMP_EV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("iv_rocket_engine"), GTOMachines.ROCKET_ENGINE_GENERATOR[GTValues.IV].asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', new UnificationEntry(TagPrefix.rotor, GTMaterials.Chromium), 'B', CustomTags.IV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_IV.asStack(), 'D', GTMachines.HULL[GTValues.IV].asStack(), 'E', new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.TungstenSteel), 'F', GTItems.ELECTRIC_PUMP_IV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("block_conversion_room"), MultiBlockMachineD.BLOCK_CONVERSION_ROOM.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTItems.QUANTUM_EYE.asStack(), 'B', GTItems.FIELD_GENERATOR_LV.asStack(), 'C', new UnificationEntry(TagPrefix.block, GTOMaterials.VibrantAlloy));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("max_neutron_compressor"), GTOMachines.NEUTRON_COMPRESSOR[GTValues.MAX].asStack(),
                "ABA",
                "CDC",
                "EBE",
                'A', GTOItems.MAX_ELECTRIC_PUMP.asStack(), 'B', CustomTags.MAX_CIRCUITS, 'C', GTOItems.MAX_ELECTRIC_PISTON.asStack(), 'D', GTMachines.HULL[GTValues.MAX].asStack(), 'E', new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.CosmicNeutronium));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("opv_thread_hatch"), GTOMachines.THREAD_HATCH[GTValues.OpV].asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', CustomTags.MAX_CIRCUITS, 'B', GTItems.ROBOT_ARM_OpV.asStack(), 'C', GTItems.CONVEYOR_MODULE_OpV.asStack(), 'D', RegistriesUtils.getItemStack("gtceu:opv_parallel_hatch"), 'E', new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.AwakenedDraconium), 'F', GTItems.FIELD_GENERATOR_OpV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("reactor_core"), GTOBlocks.REACTOR_CORE.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.Steel), 'B', new UnificationEntry(TagPrefix.plate, GTMaterials.Gold), 'C', new UnificationEntry(TagPrefix.block, GTOMaterials.PulsatingAlloy));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_steam_circuit_assembler"), MultiBlockMachineA.LARGE_STEAM_CIRCUIT_ASSEMBLER.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.Bronze), 'B', new ItemStack(Blocks.COMPARATOR.asItem()), 'C', GTOItems.PRECISION_STEAM_MECHANISM.asStack(), 'D', new ItemStack(AEBlocks.MOLECULAR_ASSEMBLER.block().asItem()));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("crafting_unit"), new ItemStack(AEBlocks.CRAFTING_UNIT.block().asItem()),
                "ABA",
                "CDC",
                "AEA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.Aluminium), 'B', new ItemStack(AEItems.CALCULATION_PROCESSOR.asItem()), 'C', RegistriesUtils.getItemStack("ae2:fluix_glass_cable"), 'D', CustomTags.MV_CIRCUITS, 'E', new ItemStack(AEItems.LOGIC_PROCESSOR.asItem()));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("steam_assembly_block"), GTOBlocks.STEAM_ASSEMBLY_BLOCK.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Bronze), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asStack(), 'C', new UnificationEntry(TagPrefix.frameGt, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("steam_foundry"), MultiBlockMachineA.STEAM_FOUNDRY.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.Bronze), 'B', new UnificationEntry(TagPrefix.plate, GTMaterials.Potin), 'C', new UnificationEntry(TagPrefix.rodLong, GTMaterials.TinAlloy), 'D', RegistriesUtils.getItemStack("gtceu:lp_steam_alloy_smelter"));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("advanced_card"), new ItemStack(AEItems.ADVANCED_CARD.asItem(), 2),
                "AB ",
                "CDB",
                "EB ",
                'A', new UnificationEntry(TagPrefix.wireFine, GTMaterials.RedAlloy), 'B', new UnificationEntry(TagPrefix.plate, GTMaterials.StainlessSteel), 'C', new ItemStack(AEItems.ENGINEERING_PROCESSOR.asItem()), 'D', CustomTags.HV_CIRCUITS, 'E', new UnificationEntry(TagPrefix.wireFine, GTMaterials.Silver));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("ev_lightning_rod"), GTOMachines.LIGHTNING_ROD[GTValues.EV].asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTItems.LAPOTRON_CRYSTAL.asStack(), 'B', GTMachines.POWER_TRANSFORMER[GTValues.EV].asStack(), 'C', GTMachines.HULL[GTValues.EV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("max_thread_hatch"), GTOMachines.THREAD_HATCH[GTValues.MAX].asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTOItems.SUPRACHRONAL_CIRCUIT[GTValues.MAX].asStack(), 'B', GTOItems.MAX_ROBOT_ARM.asStack(), 'C', GTOItems.MAX_CONVEYOR_MODULE.asStack(), 'D', RegistriesUtils.getItemStack("gtceu:max_parallel_hatch"), 'E', new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.Hypogen), 'F', GTOItems.MAX_FIELD_GENERATOR.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("uev_thread_hatch"), GTOMachines.THREAD_HATCH[GTValues.UEV].asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', CustomTags.UIV_CIRCUITS, 'B', GTItems.ROBOT_ARM_UEV.asStack(), 'C', GTItems.CONVEYOR_MODULE_UEV.asStack(), 'D', RegistriesUtils.getItemStack("gtceu:uev_parallel_hatch"), 'E', new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.Enderite), 'F', GTItems.FIELD_GENERATOR_UEV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("lv_semi_fluid"), GTOMachines.SEMI_FLUID_GENERATOR[GTValues.LV].asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.ELECTRIC_PISTON_LV.asStack(), 'B', CustomTags.LV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_LV.asStack(), 'D', GTMachines.HULL[GTValues.LV].asStack(), 'E', new UnificationEntry(TagPrefix.gear, GTMaterials.Potin), 'F', new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Cobalt));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("item_storage_cell_16m"), GTOItems.ITEM_STORAGE_CELL_16M.asStack(),
                "ABA",
                "BCB",
                "DDD",
                'A', new ItemStack(AEBlocks.QUARTZ_GLASS.block().asItem()), 'B', new UnificationEntry(TagPrefix.dust, GTMaterials.Redstone), 'C', GTOItems.CELL_COMPONENT_16M.asStack(), 'D', new UnificationEntry(TagPrefix.ingot, GTMaterials.Iron));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_steam_input_hatch"), GTOMachines.LARGE_STEAM_HATCH.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOItems.PRECISION_STEAM_MECHANISM.asStack(), 'B', RegistriesUtils.getItemStack("enderio:vibrant_crystal"), 'C', new UnificationEntry(TagPrefix.pipeTinyFluid, GTMaterials.Titanium), 'D', RegistriesUtils.getItemStack("gtceu:steam_input_hatch"));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("cleaning_configuration_maintenance_hatch"), GTOMachines.CLEANING_CONFIGURATION_MAINTENANCE_HATCH.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', RegistriesUtils.getItemStack("gtceu:cleaning_maintenance_hatch"), 'B', CustomTags.LuV_CIRCUITS, 'C', GTOMachines.AUTO_CONFIGURATION_MAINTENANCE_HATCH.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("fluid_storage_cell_64m"), GTOItems.FLUID_STORAGE_CELL_64M.asStack(),
                "ABA",
                "BCB",
                "DDD",
                'A', new ItemStack(AEBlocks.QUARTZ_GLASS.block().asItem()), 'B', new UnificationEntry(TagPrefix.dust, GTMaterials.Redstone), 'C', GTOItems.CELL_COMPONENT_64M.asStack(), 'D', new UnificationEntry(TagPrefix.ingot, GTMaterials.Copper));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("steam_cracker"), MultiBlockMachineC.STEAM_CRACKER.asStack(),
                "ABA",
                "BCB",
                "DBD",
                'A', new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.TinAlloy), 'B', new UnificationEntry(TagPrefix.pipeQuadrupleFluid, GTMaterials.Potin), 'C', RegistriesUtils.getItemStack("gtceu:hp_steam_alloy_smelter"), 'D', GTOItems.ULV_FLUID_REGULATOR.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_steam_mixer"), MultiBlockMachineA.LARGE_STEAM_MIXER.asStack(),
                "ABA",
                "CDC",
                "EBE",
                'A', RegistriesUtils.getItemStack("enderio:infinity_rod"), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asStack(), 'C', new UnificationEntry(TagPrefix.pipeHugeFluid, GTMaterials.Copper), 'D', MultiBlockMachineA.STEAM_MIXER.asStack(), 'E', new UnificationEntry(TagPrefix.plate, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("wood_rotor"), GTOItems.WOOD_ROTOR.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.TreatedWood), 'B', new UnificationEntry(TagPrefix.plate, GTMaterials.Wood), 'C', new UnificationEntry(TagPrefix.frameGt, GTMaterials.Wood));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("steam_pressor"), MultiBlockMachineA.STEAM_PRESSOR.asStack(),
                "ABA",
                "CDC",
                "AEA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.Bronze), 'B', new UnificationEntry(TagPrefix.gearSmall, GTMaterials.Bronze), 'C', new UnificationEntry(TagPrefix.springSmall, GTMaterials.Iron), 'D', RegistriesUtils.getItemStack("gtceu:lp_steam_compressor"), 'E', new UnificationEntry(TagPrefix.gear, GTMaterials.WroughtIron));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_steam_bath"), MultiBlockMachineA.LARGE_STEAM_BATH.asStack(),
                "ABA",
                "CDC",
                "EBE",
                'A', new UnificationEntry(TagPrefix.foil, GTMaterials.Steel), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asStack(), 'C', new UnificationEntry(TagPrefix.rotor, GTMaterials.Aluminium), 'D', MultiBlockMachineA.STEAM_BATH.asStack(), 'E', new UnificationEntry(TagPrefix.block, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("zpm_naquadah_reactor"), GTOMachines.NAQUADAH_REACTOR_GENERATOR[GTValues.ZPM].asStack(),
                "ABA",
                "CDC",
                "EBE",
                'A', new UnificationEntry(TagPrefix.rod, GTMaterials.Naquadria), 'B', CustomTags.ZPM_CIRCUITS, 'C', GTItems.FIELD_GENERATOR_ZPM.asStack(), 'D', GTMachines.HULL[GTValues.ZPM].asStack(), 'E', new UnificationEntry(TagPrefix.cableGtQuadruple, GTMaterials.Naquadah));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_steam_ore_washer"), MultiBlockMachineA.LARGE_STEAM_ORE_WASHER.asStack(),
                "ABA",
                "CDC",
                "EBE",
                'A', RegistriesUtils.getItemStack("enderio:infinity_rod"), 'B', new UnificationEntry(TagPrefix.plateDouble, GTMaterials.Bronze), 'C', GTOItems.PRECISION_STEAM_MECHANISM.asStack(), 'D', MultiBlockMachineA.STEAM_ORE_WASHER.asStack(), 'E', new UnificationEntry(TagPrefix.plate, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_steam_crusher"), MultiBlockMachineC.LARGE_STEAM_CRUSHER.asStack(),
                "ABA",
                "CDC",
                "EBE",
                'A', new UnificationEntry(GTOTagPrefix.curvedPlate, GTMaterials.Brass), 'B', new UnificationEntry(TagPrefix.gear, GTMaterials.Diamond), 'C', GTOItems.PRECISION_STEAM_MECHANISM.asStack(), 'D', MultiBlockMachineC.STEAM_CRUSHER.asStack(), 'E', new UnificationEntry(TagPrefix.gear, GTMaterials.CobaltBrass));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_steam_centrifuge"), MultiBlockMachineA.LARGE_STEAM_CENTRIFUGE.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.block, GTMaterials.Bronze), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asStack(), 'C', new UnificationEntry(TagPrefix.gearSmall, GTMaterials.Iron), 'D', MultiBlockMachineA.STEAM_SEPARATOR.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_cracker"), MultiBlockMachineA.LARGE_CRACKER.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTItems.FIELD_GENERATOR_LuV.asStack(), 'B', CustomTags.UV_CIRCUITS, 'C', new UnificationEntry(TagPrefix.spring, GTMaterials.Naquadah), 'D', GTMultiMachines.CRACKER.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("weather_control"), MultiBlockMachineD.WEATHER_CONTROL.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new ItemStack(Blocks.LIGHTNING_ROD.asItem()), 'B', new UnificationEntry(TagPrefix.plateDouble, GTMaterials.Steel), 'C', new ItemStack(Blocks.DAYLIGHT_DETECTOR.asItem()), 'D', new UnificationEntry(TagPrefix.block, GTMaterials.Amethyst));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_greenhouse"), MultiBlockMachineA.LARGE_GREENHOUSE.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTItems.FIELD_GENERATOR_EV.asStack(), 'B', CustomTags.LuV_CIRCUITS, 'C', GTItems.SENSOR_EV.asStack(), 'D', MultiBlockMachineD.GREENHOUSE.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("mv_semi_fluid"), GTOMachines.SEMI_FLUID_GENERATOR[GTValues.MV].asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.ELECTRIC_PISTON_MV.asStack(), 'B', CustomTags.MV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_MV.asStack(), 'D', GTMachines.HULL[GTValues.MV].asStack(), 'E', new UnificationEntry(TagPrefix.gear, GTOMaterials.EglinSteel), 'F', new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.AnnealedCopper));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("hv_semi_fluid"), GTOMachines.SEMI_FLUID_GENERATOR[GTValues.HV].asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.ELECTRIC_PISTON_HV.asStack(), 'B', CustomTags.HV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_HV.asStack(), 'D', GTMachines.HULL[GTValues.HV].asStack(), 'E', new UnificationEntry(TagPrefix.gear, GTMaterials.Chromium), 'F', new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Electrum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("law_cleaning_maintenance_hatch"), GTOMachines.LAW_CLEANING_MAINTENANCE_HATCH.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOBlocks.LAW_FILTER_CASING.asStack(), 'B', GTOMachines.STERILE_CLEANING_MAINTENANCE_HATCH.asStack(), 'C', GTItems.FIELD_GENERATOR_UEV.asStack(), 'D', GTMachines.HULL[GTValues.UEV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_rock_crusher"), MultiBlockMachineA.LARGE_ROCK_CRUSHER.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTItems.ELECTRIC_PISTON_IV.asStack(), 'B', CustomTags.IV_CIRCUITS, 'C', new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Platinum), 'D', GTMachines.ROCK_CRUSHER[GTValues.IV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("formation_core"), new ItemStack(AEItems.FORMATION_CORE.asItem()),
                "ABC",
                'A', CustomTags.ULV_CIRCUITS, 'B', new ItemStack(AEItems.LOGIC_PROCESSOR.asItem()), 'C', new UnificationEntry(TagPrefix.dust, GTMaterials.CertusQuartz));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("item_storage_cell_64m"), GTOItems.ITEM_STORAGE_CELL_64M.asStack(),
                "ABA",
                "BCB",
                "DDD",
                'A', new ItemStack(AEBlocks.QUARTZ_GLASS.block().asItem()), 'B', new UnificationEntry(TagPrefix.dust, GTMaterials.Redstone), 'C', GTOItems.CELL_COMPONENT_64M.asStack(), 'D', new UnificationEntry(TagPrefix.ingot, GTMaterials.Iron));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("fluid_storage_cell_16m"), GTOItems.FLUID_STORAGE_CELL_16M.asStack(),
                "ABA",
                "BCB",
                "DDD",
                'A', new ItemStack(AEBlocks.QUARTZ_GLASS.block().asItem()), 'B', new UnificationEntry(TagPrefix.dust, GTMaterials.Redstone), 'C', GTOItems.CELL_COMPONENT_16M.asStack(), 'D', new UnificationEntry(TagPrefix.ingot, GTMaterials.Copper));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("block_bus"), GTOMachines.BLOCK_BUS.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.block, GTOMaterials.EnergeticAlloy), 'B', new UnificationEntry(TagPrefix.block, GTOMaterials.ConductiveAlloy), 'C', RegistriesUtils.getItemStack("enderio:vacuum_chest"), 'D', GTMachines.ITEM_IMPORT_BUS[GTValues.LuV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("cleaning_maintenance_hatch"), RegistriesUtils.getItemStack("gtceu:cleaning_maintenance_hatch"),
                "ABA",
                "CDC",
                "ABA",
                'A', GTBlocks.FILTER_CASING.asStack(), 'B', RegistriesUtils.getItemStack("gtceu:auto_maintenance_hatch"), 'C', GTItems.FIELD_GENERATOR_HV.asStack(), 'D', GTMachines.HULL[GTValues.HV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("steam_separator"), MultiBlockMachineA.STEAM_SEPARATOR.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.block, GTMaterials.Bronze), 'B', new UnificationEntry(GTOTagPrefix.curvedPlate, GTMaterials.Bronze), 'C', new UnificationEntry(TagPrefix.gear, GTMaterials.Rubber), 'D', GTBlocks.CASING_BRONZE_GEARBOX.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("uxv_thread_hatch"), GTOMachines.THREAD_HATCH[GTValues.UXV].asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', CustomTags.OpV_CIRCUITS, 'B', GTItems.ROBOT_ARM_UXV.asStack(), 'C', GTItems.CONVEYOR_MODULE_UXV.asStack(), 'D', RegistriesUtils.getItemStack("gtceu:uxv_parallel_hatch"), 'E', new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.Legendarium), 'F', GTItems.FIELD_GENERATOR_UXV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("creative_data_access_hatch"), RegistriesUtils.getItemStack("gtceu:creative_data_access_hatch"),
                "ABA",
                "BCB",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plateDouble, GTOMaterials.Chaos), 'B', new ItemStack(Blocks.REPEATING_COMMAND_BLOCK.asItem()), 'C', RegistriesUtils.getItemStack("gtceu:advanced_data_access_hatch"));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("gravity_configuration_hatch"), GTOMachines.GRAVITY_CONFIGURATION_HATCH.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOMachines.GRAVITY_HATCH.asStack(), 'B', CustomTags.UEV_CIRCUITS, 'C', GTItems.FIELD_GENERATOR_UEV.asStack(), 'D', GTOMachines.AUTO_CONFIGURATION_MAINTENANCE_HATCH.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_semi_fluid_generator"), GeneratorMultiblockMachine.LARGE_SEMI_FLUID_GENERATOR.asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.ELECTRIC_PISTON_EV.asStack(), 'B', CustomTags.EV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_EV.asStack(), 'D', GTMachines.HULL[GTValues.EV].asStack(), 'E', new UnificationEntry(TagPrefix.gear, GTOMaterials.Inconel792), 'F', new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Nichrome));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("heat_sensor"), GTOMachines.HEAT_SENSOR.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', new UnificationEntry(TagPrefix.pipeTinyFluid, GTMaterials.Steel), 'B', new ItemStack(Blocks.REPEATER.asItem()), 'C', GTBlocks.MACHINE_CASING_LV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("of_the_sea"), new ItemStack(Items.HEART_OF_THE_SEA.asItem()),
                "ABA",
                "BCB",
                "ABA",
                'A', GTItems.QUANTUM_STAR.asStack(), 'B', GTOItems.GLACIO_SPIRIT.asStack(), 'C', GTOItems.PELLET_ANTIMATTER.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("leap_forward_one_blast_furnace"), MultiBlockMachineA.LEAP_FORWARD_ONE_BLAST_FURNACE.asStack(),
                "ABA",
                "BCB",
                "DDD",
                'A', new UnificationEntry(TagPrefix.foil, GTMaterials.WroughtIron), 'B', RegistriesUtils.getItemStack("ad_astra:airlock"), 'C', GTMultiMachines.PRIMITIVE_BLAST_FURNACE.asStack(), 'D', GTItems.FIRECLAY_BRICK.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("sensor"), new ItemStack(Blocks.SCULK_SENSOR.asItem()),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.gem, GTMaterials.EchoShard), 'B', new UnificationEntry(TagPrefix.dust, GTMaterials.EchoShard), 'C', new ItemStack(Blocks.NOTE_BLOCK.asItem()), 'D', new ItemStack(Blocks.COMPARATOR.asItem()));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("law_filter_casing"), GTOBlocks.LAW_FILTER_CASING.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTItems.EMITTER_UEV.asStack(), 'B', GTBlocks.FILTER_CASING_STERILE.asStack(), 'C', GTMachines.MUFFLER_HATCH[GTValues.UEV].asStack(), 'D', new UnificationEntry(TagPrefix.frameGt, GTOMaterials.Mithril));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("iv_naquadah_reactor"), GTOMachines.NAQUADAH_REACTOR_GENERATOR[GTValues.IV].asStack(),
                "ABA",
                "CDC",
                "EBE",
                'A', new UnificationEntry(TagPrefix.rod, GTMaterials.Naquadah), 'B', CustomTags.IV_CIRCUITS, 'C', GTItems.FIELD_GENERATOR_IV.asStack(), 'D', GTMachines.HULL[GTValues.IV].asStack(), 'E', new UnificationEntry(TagPrefix.cableGtQuadruple, GTMaterials.Tungsten));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("import_bus"), RegistriesUtils.getItemStack("ae2:import_bus"),
                "ABC",
                'A', new ItemStack(AEItems.ANNIHILATION_CORE.asItem()), 'B', GTItems.ROBOT_ARM_LV.asStack(), 'C', RegistriesUtils.getItemStack("ae2:fluix_glass_cable"));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("steam_crusher"), MultiBlockMachineC.STEAM_CRUSHER.asStack(),
                "ABA",
                "BCB",
                "DBD",
                'A', new UnificationEntry(TagPrefix.gem, GTMaterials.Diamond), 'B', new UnificationEntry(TagPrefix.gearSmall, GTMaterials.Bronze), 'C', RegistriesUtils.getItemStack("gtceu:hp_steam_macerator"), 'D', new UnificationEntry(TagPrefix.plateDouble, GTMaterials.WroughtIron));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_steam_thermal_centrifuge"), MultiBlockMachineA.LARGE_STEAM_THERMAL_CENTRIFUGE.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.Bronze), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asStack(), 'C', new UnificationEntry(TagPrefix.rodLong, GTMaterials.Copper), 'D', MultiBlockMachineA.STEAM_SEPARATOR.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("precision_steam_mechanism"), GTOItems.PRECISION_STEAM_MECHANISM.asStack(),
                "ABA",
                "CDC",
                "EBE",
                'A', new UnificationEntry(TagPrefix.rod, GTMaterials.Bronze), 'B', new UnificationEntry(TagPrefix.gearSmall, GTMaterials.Bronze), 'C', new UnificationEntry(TagPrefix.springSmall, GTMaterials.Copper), 'D', RegistriesUtils.getItemStack("enderio:dark_bimetal_gear"), 'E', GTOItems.ULV_FLUID_REGULATOR.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("greenhouse"), MultiBlockMachineD.GREENHOUSE.asStack(),
                "AAA",
                "BCB",
                "DED",
                'A', GTBlocks.CASING_TEMPERED_GLASS.asStack(), 'B', CustomTags.MV_CIRCUITS, 'C', GTMachines.HULL[GTValues.MV].asStack(), 'D', GTItems.ELECTRIC_PISTON_MV.asStack(), 'E', GTItems.ELECTRIC_PUMP_MV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("advanced_assembly_line_unit"), GTOBlocks.ADVANCED_ASSEMBLY_LINE_UNIT.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.HSSG), 'B', new UnificationEntry(TagPrefix.gear, GTMaterials.Rhodium), 'C', CustomTags.UV_CIRCUITS, 'D', GTBlocks.CASING_ASSEMBLY_LINE.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("steam_mixer"), MultiBlockMachineA.STEAM_MIXER.asStack(),
                "ABA",
                "CDC",
                "EBE",
                'A', new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Bronze), 'B', new UnificationEntry(TagPrefix.rod, GTMaterials.Steel), 'C', new UnificationEntry(TagPrefix.plate, GTMaterials.Bronze), 'D', GTBlocks.CASING_BRONZE_PIPE.asStack(), 'E', new UnificationEntry(TagPrefix.gear, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("dragon_stabilizer_core"), GTOItems.DRAGON_STABILIZER_CORE.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plateDouble, GTOMaterials.Draconium), 'B', new UnificationEntry(TagPrefix.rodLong, GTOMaterials.CosmicNeutronium), 'C', GTOItems.STABILIZER_CORE.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("sterile_configuration_cleaning_maintenance_hatch"), GTOMachines.STERILE_CONFIGURATION_CLEANING_MAINTENANCE_HATCH.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOMachines.STERILE_CLEANING_MAINTENANCE_HATCH.asStack(), 'B', GTOMachines.CLEANING_CONFIGURATION_MAINTENANCE_HATCH.asStack(), 'C', GTItems.FIELD_GENERATOR_UHV.asStack(), 'D', GTMachines.HULL[GTValues.UHV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("wireless_receiver"), new ItemStack(AEItems.WIRELESS_RECEIVER.asItem()),
                "ABA",
                "CDC",
                'A', CustomTags.EV_CIRCUITS, 'B', new ItemStack(AEItems.FLUIX_PEARL.asItem()), 'C', new UnificationEntry(GTOTagPrefix.curvedPlate, GTMaterials.Titanium), 'D', GTItems.SENSOR_HV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("processing_plant"), MultiBlockMachineD.PROCESSING_PLANT.asStack(),
                "ABA",
                "CDE",
                "AFA",
                'A', new UnificationEntry(TagPrefix.foil, GTMaterials.Aluminium), 'B', GTItems.CONVEYOR_MODULE_MV.asStack(), 'C', GTItems.SENSOR_MV.asStack(), 'D', GTOBlocks.MULTI_FUNCTIONAL_CASING.asStack(), 'E', GTItems.EMITTER_MV.asStack(), 'F', GTItems.FLUID_REGULATOR_MV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("neutronium_pipe_casing"), GTOBlocks.NEUTRONIUM_PIPE_CASING.asStack(2),
                "ABA",
                "BCB",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.Neutronium), 'B', new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Neutronium), 'C', new UnificationEntry(TagPrefix.frameGt, GTMaterials.Neutronium));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("liquefaction_furnace"), MultiBlockMachineB.LIQUEFACTION_FURNACE.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.Invar), 'B', new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Nickel), 'C', new ItemStack(Blocks.BLAST_FURNACE.asItem()), 'D', GTMachines.EXTRACTOR[GTValues.LV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("item_storage_cell_1m"), GTOItems.ITEM_STORAGE_CELL_1M.asStack(),
                "ABA",
                "BCB",
                "DDD",
                'A', new ItemStack(AEBlocks.QUARTZ_GLASS.block().asItem()), 'B', new UnificationEntry(TagPrefix.dust, GTMaterials.Redstone), 'C', GTOItems.CELL_COMPONENT_1M.asStack(), 'D', new UnificationEntry(TagPrefix.ingot, GTMaterials.Iron));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("iv_lightning_rod"), GTOMachines.LIGHTNING_ROD[GTValues.IV].asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTItems.ENERGY_LAPOTRONIC_ORB.asStack(), 'B', GTMachines.POWER_TRANSFORMER[GTValues.IV].asStack(), 'C', GTMachines.HULL[GTValues.IV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("creative_energy_cell"), new ItemStack(AEBlocks.CREATIVE_ENERGY_CELL.block().asItem()),
                "AAA",
                "ABA",
                "AAA",
                'A', new ItemStack(AEBlocks.DENSE_ENERGY_CELL.block().asItem()), 'B', GTItems.FIELD_GENERATOR_UV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("law_configuration_cleaning_maintenance_hatch"), GTOMachines.LAW_CONFIGURATION_CLEANING_MAINTENANCE_HATCH.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOMachines.LAW_CLEANING_MAINTENANCE_HATCH.asStack(), 'B', GTOMachines.STERILE_CONFIGURATION_CLEANING_MAINTENANCE_HATCH.asStack(), 'C', GTItems.FIELD_GENERATOR_UXV.asStack(), 'D', GTMachines.HULL[GTValues.UXV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("fluid_storage_cell_4m"), GTOItems.FLUID_STORAGE_CELL_4M.asStack(),
                "ABA",
                "BCB",
                "DDD",
                'A', new ItemStack(AEBlocks.QUARTZ_GLASS.block().asItem()), 'B', new UnificationEntry(TagPrefix.dust, GTMaterials.Redstone), 'C', GTOItems.CELL_COMPONENT_4M.asStack(), 'D', new UnificationEntry(TagPrefix.ingot, GTMaterials.Copper));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("uhv_thread_hatch"), GTOMachines.THREAD_HATCH[GTValues.UHV].asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', CustomTags.UEV_CIRCUITS, 'B', GTItems.ROBOT_ARM_UHV.asStack(), 'C', GTItems.CONVEYOR_MODULE_UHV.asStack(), 'D', RegistriesUtils.getItemStack("gtceu:uhv_parallel_hatch"), 'E', new UnificationEntry(TagPrefix.wireGtHex, GTMaterials.RutheniumTriniumAmericiumNeutronate), 'F', GTItems.FIELD_GENERATOR_UHV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_steam_furnace"), MultiBlockMachineA.LARGE_STEAM_FURNACE.asStack(),
                "ABA",
                "CDC",
                "EBE",
                'A', new UnificationEntry(TagPrefix.rodLong, GTMaterials.Potin), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asStack(), 'C', RegistriesUtils.getItemStack("enderio:reinforced_obsidian_block"), 'D', GTMultiMachines.STEAM_OVEN.asStack(), 'E', new UnificationEntry(TagPrefix.pipeHugeFluid, GTMaterials.Potin));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("vacuum_hatch"), GTOMachines.VACUUM_HATCH.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTItems.ROBOT_ARM_EV.asStack(), 'B', new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.VanadiumSteel), 'C', GTMachines.PUMP[GTValues.EV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("incubator"), MultiBlockMachineD.INCUBATOR.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTBlocks.PLASTCRETE.asStack(), 'B', GTItems.FIELD_GENERATOR_HV.asStack(), 'C', GTBlocks.FILTER_CASING.asStack(), 'D', MultiBlockMachineD.GREENHOUSE.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("upgrade_smithing_template"), new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE.asItem()),
                "ABA",
                "ACA",
                "AAA",
                'A', new UnificationEntry(TagPrefix.gem, GTMaterials.Diamond), 'B', new ItemStack(Blocks.NETHERITE_BLOCK.asItem()), 'C', new UnificationEntry(TagPrefix.rock, GTMaterials.Netherrack));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("steam_bath"), MultiBlockMachineA.STEAM_BATH.asStack(),
                "ABA",
                "BCB",
                "DBD",
                'A', new UnificationEntry(TagPrefix.screw, GTMaterials.Rubber), 'B', new UnificationEntry(TagPrefix.plate, GTMaterials.Bronze), 'C', GTBlocks.CASING_BRONZE_PIPE.asStack(), 'D', new UnificationEntry(TagPrefix.gear, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("iron_rotor"), GTOItems.IRON_ROTOR.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', new ItemStack(Blocks.CHAIN.asItem()), 'B', new UnificationEntry(TagPrefix.turbineBlade, GTMaterials.Iron), 'C', new UnificationEntry(TagPrefix.rodLong, GTMaterials.Invar));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("steel_rotor"), GTOItems.STEEL_ROTOR.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', new ItemStack(Blocks.CHAIN.asItem()), 'B', new UnificationEntry(TagPrefix.turbineBlade, GTMaterials.Steel), 'C', new UnificationEntry(TagPrefix.rodLong, GTMaterials.Invar));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("item_storage_cell_4m"), GTOItems.ITEM_STORAGE_CELL_4M.asStack(),
                "ABA",
                "BCB",
                "DDD",
                'A', new ItemStack(AEBlocks.QUARTZ_GLASS.block().asItem()), 'B', new UnificationEntry(TagPrefix.dust, GTMaterials.Redstone), 'C', GTOItems.CELL_COMPONENT_4M.asStack(), 'D', new UnificationEntry(TagPrefix.ingot, GTMaterials.Iron));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("pulsating_photovoltaic_power_station"), GeneratorMultiblockMachine.PHOTOVOLTAIC_POWER_STATION_PULSATING.asStack(),
                "ABA",
                "BCB",
                "ADA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.Titanium), 'B', new UnificationEntry(TagPrefix.block, GTOMaterials.RedstoneAlloy), 'C', GTOBlocks.PULSATING_PHOTOVOLTAIC_BLOCK.asStack(), 'D', CustomTags.HV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("blaze_blast_furnace"), MultiBlockMachineD.BLAZE_BLAST_FURNACE.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTOBlocks.BLAZE_CASING.asStack(), 'B', GTItems.FIELD_GENERATOR_IV.asStack(), 'C', GTMultiMachines.ELECTRIC_BLAST_FURNACE.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("ulv_semi_fluid"), GTOMachines.SEMI_FLUID_GENERATOR[GTValues.ULV].asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTOItems.ULV_ELECTRIC_PISTON.asStack(), 'B', CustomTags.ULV_CIRCUITS, 'C', GTOItems.ULV_ELECTRIC_MOTOR.asStack(), 'D', GTMachines.HULL[GTValues.ULV].asStack(), 'E', new UnificationEntry(TagPrefix.gear, GTMaterials.Stone), 'F', new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Lead));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("fluid_storage_cell_256m"), GTOItems.FLUID_STORAGE_CELL_256M.asStack(),
                "ABA",
                "BCB",
                "DDD",
                'A', new ItemStack(AEBlocks.QUARTZ_GLASS.block().asItem()), 'B', new UnificationEntry(TagPrefix.dust, GTMaterials.Redstone), 'C', GTOItems.CELL_COMPONENT_256M.asStack(), 'D', new UnificationEntry(TagPrefix.ingot, GTMaterials.Copper));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("multi_functional_casing"), GTOBlocks.MULTI_FUNCTIONAL_CASING.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plateDouble, GTMaterials.Aluminium), 'B', GTItems.ROBOT_ARM_MV.asStack(), 'C', GTItems.ELECTRIC_PISTON_MV.asStack(), 'D', GTBlocks.CASING_STEEL_SOLID.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_pyrolyse_oven"), MultiBlockMachineA.LARGE_PYROLYSE_OVEN.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plateDouble, GTMaterials.StainlessSteel), 'B', GTItems.FIELD_GENERATOR_IV.asStack(), 'C', new UnificationEntry(TagPrefix.pipeHugeFluid, GTMaterials.VanadiumSteel), 'D', GTMultiMachines.PYROLYSE_OVEN.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("thermal_power_pump"), MultiBlockMachineC.THERMAL_POWER_PUMP.asStack(),
                "ABA",
                "CDE",
                "FGF",
                'A', new UnificationEntry(TagPrefix.pipeSmallFluid, GTMaterials.Copper), 'B', RegistriesUtils.getItemStack("gtceu:hp_steam_compressor"), 'C', GTOBlocks.REINFORCED_WOOD_CASING.asStack(), 'D', GTMultiMachines.PRIMITIVE_PUMP.asStack(), 'E', RegistriesUtils.getItemStack("gtceu:pump_hatch"), 'F', new UnificationEntry(TagPrefix.plate, GTMaterials.Brass), 'G', RegistriesUtils.getItemStack("gtceu:hp_steam_extractor"));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("gravity_hatch"), GTOMachines.GRAVITY_HATCH.asStack(),
                "ABA",
                "BCB",
                "DBD",
                'A', GTItems.ROBOT_ARM_UV.asStack(), 'B', GTItems.GRAVI_STAR.asStack(), 'C', GTMachines.HULL[GTValues.UV].asStack(), 'D', GTItems.GRAVITATION_ENGINE.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("astra_nasa_workbench"), RegistriesUtils.getItemStack("ad_astra:nasa_workbench"),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.ROBOT_ARM_HV.asStack(), 'B', GTItems.EMITTER_HV.asStack(), 'C', new ItemStack(Blocks.REDSTONE_TORCH.asItem()), 'D', RegistriesUtils.getItemStack("avaritia:compressed_crafting_table"), 'E', new UnificationEntry(TagPrefix.plateDouble, GTMaterials.Steel), 'F', new UnificationEntry(TagPrefix.block, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("flint_axe"), RegistriesUtils.getItemStack("gtceu:flint_axe", 1, "{DisallowContainerItem:0b,GT.Behaviours:{DisableShields:1b},GT.Tool:{AttackDamage:6.0f,AttackSpeed:-3.2f,Damage:0,HarvestLevel:2,MaxDamage:16,ToolSpeed:3.5f},HideFlags:2}"),
                "AA",
                "BC",
                'A', new UnificationEntry(TagPrefix.gem, GTMaterials.Flint), 'B', GTOItems.PLANT_FIBER.asStack(), 'C', new UnificationEntry(TagPrefix.rod, GTMaterials.Wood));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("command_wand"), GTOItems.COMMAND_WAND.asStack(),
                "  A",
                " B ",
                "B  ",
                'A', GTOItems.COMMAND_BLOCK_CORE.asStack(), 'B', new UnificationEntry(TagPrefix.rod, GTOMaterials.Eternity));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("basic_card"), new ItemStack(AEItems.BASIC_CARD.asItem(), 2),
                "AB ",
                "CDB",
                "EB ",
                'A', new UnificationEntry(TagPrefix.wireFine, GTMaterials.RedAlloy), 'B', new UnificationEntry(TagPrefix.plate, GTMaterials.Aluminium), 'C', new ItemStack(AEItems.ENGINEERING_PROCESSOR.asItem()), 'D', CustomTags.MV_CIRCUITS, 'E', new UnificationEntry(TagPrefix.wireFine, GTMaterials.Gold));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("item_storage_cell_256m"), GTOItems.ITEM_STORAGE_CELL_256M.asStack(),
                "ABA",
                "BCB",
                "DDD",
                'A', new ItemStack(AEBlocks.QUARTZ_GLASS.block().asItem()), 'B', new UnificationEntry(TagPrefix.dust, GTMaterials.Redstone), 'C', GTOItems.CELL_COMPONENT_256M.asStack(), 'D', new UnificationEntry(TagPrefix.ingot, GTMaterials.Iron));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("steam_piston_hammer"), MultiBlockMachineA.STEAM_PISTON_HAMMER.asStack(),
                "ABA",
                "CDC",
                "AEA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.Bronze), 'B', new UnificationEntry(TagPrefix.ring, GTMaterials.WroughtIron), 'C', new UnificationEntry(TagPrefix.spring, GTMaterials.Iron), 'D', RegistriesUtils.getItemStack("gtceu:lp_steam_forge_hammer"), 'E', new UnificationEntry(TagPrefix.plateDouble, GTMaterials.WroughtIron));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("shrieker"), new ItemStack(Blocks.SCULK_SHRIEKER.asItem()),
                " A ",
                "ABA",
                " A ",
                'A', new UnificationEntry(TagPrefix.rod, GTMaterials.EchoShard), 'B', new ItemStack(Blocks.SCULK_SENSOR.asItem()));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("vacuum_configuration_hatch"), GTOMachines.VACUUM_CONFIGURATION_HATCH.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOMachines.VACUUM_HATCH.asStack(), 'B', CustomTags.UHV_CIRCUITS, 'C', GTItems.GRAVI_STAR.asStack(), 'D', GTOMachines.AUTO_CONFIGURATION_MAINTENANCE_HATCH.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("stabilizer_core"), GTOItems.STABILIZER_CORE.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plateDouble, GTOMaterials.Infuscolium), 'B', new UnificationEntry(TagPrefix.rodLong, GTMaterials.Neutronium), 'C', GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("machine_casing_circuit_assembly_line"), GTOBlocks.MACHINE_CASING_CIRCUIT_ASSEMBLY_LINE.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plate, GTOMaterials.Pikyonium), 'B', new UnificationEntry(TagPrefix.gear, GTMaterials.HSSG), 'C', GTItems.ROBOT_ARM_LuV.asStack(), 'D', new UnificationEntry(TagPrefix.frameGt, GTMaterials.Ruridit));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("mega_alloy_blast_smelter"), MultiBlockMachineA.MEGA_ALLOY_BLAST_SMELTER.asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', new UnificationEntry(TagPrefix.spring, GTMaterials.NaquadahAlloy), 'B', CustomTags.ZPM_CIRCUITS, 'C', GTItems.FIELD_GENERATOR_ZPM.asStack(), 'D', GCYMMachines.BLAST_ALLOY_SMELTER.asStack(), 'E', new UnificationEntry(TagPrefix.plateDense, GTMaterials.Darmstadtium), 'F', new UnificationEntry(TagPrefix.wireGtHex, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("uiv_thread_hatch"), GTOMachines.THREAD_HATCH[GTValues.UIV].asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', CustomTags.UXV_CIRCUITS, 'B', GTItems.ROBOT_ARM_UIV.asStack(), 'C', GTItems.CONVEYOR_MODULE_UIV.asStack(), 'D', RegistriesUtils.getItemStack("gtceu:uiv_parallel_hatch"), 'E', new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.Echoite), 'F', GTItems.FIELD_GENERATOR_UIV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("creative_laser_hatch"), RegistriesUtils.getItemStack("gtmthings:creative_laser_hatch"),
                "ABA",
                "B B",
                "ABA",
                'A', new ItemStack(Blocks.CHAIN_COMMAND_BLOCK.asItem()), 'B', GTOItems.CHAOTIC_ENERGY_CORE.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("rocket_large_turbine"), GeneratorMultiblockMachine.ROCKET_LARGE_TURBINE.asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.ELECTRIC_PISTON_EV.asStack(), 'B', CustomTags.IV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_EV.asStack(), 'D', GTOMachines.ROCKET_ENGINE_GENERATOR[GTValues.EV].asStack(), 'E', new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.BlackSteel), 'F', new UnificationEntry(TagPrefix.plateDense, GTMaterials.Obsidian));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("energetic_photovoltaic_power_station"), GeneratorMultiblockMachine.PHOTOVOLTAIC_POWER_STATION_ENERGETIC.asStack(),
                "ABA",
                "BCB",
                "ADA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.Steel), 'B', new UnificationEntry(TagPrefix.block, GTOMaterials.CopperAlloy), 'C', GTOBlocks.ENERGETIC_PHOTOVOLTAIC_BLOCK.asStack(), 'D', CustomTags.MV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("reaction_furnace"), MultiBlockMachineB.REACTION_FURNACE.asStack(),
                "ABA",
                "CDC",
                "BAB",
                'A', GTMachines.ELECTRIC_FURNACE[GTValues.MV].asStack(), 'B', RegistriesUtils.getItemStack("gtceu:gold_drum"), 'C', new UnificationEntry(TagPrefix.cableGtOctal, GTMaterials.Iron), 'D', GTMachines.CHEMICAL_REACTOR[GTValues.LV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("aggregatione_core"), GTOBlocks.AGGREGATIONE_CORE.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', new UnificationEntry(TagPrefix.ingot, GTOMaterials.AttunedTengam), 'B', GTOBlocks.INFUSED_OBSIDIAN.asStack(), 'C', GTOBlocks.MAGIC_CORE.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("luv_lightning_rod"), GTOMachines.LIGHTNING_ROD[GTValues.LuV].asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTItems.ENERGY_LAPOTRONIC_ORB_CLUSTER.asStack(), 'B', GTMachines.POWER_TRANSFORMER[GTValues.LuV].asStack(), 'C', GTMachines.HULL[GTValues.LuV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("desulfurizer"), MultiBlockMachineB.DESULFURIZER.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTItems.ELECTRIC_PUMP_HV.asStack(), 'B', GTItems.ELECTRIC_MOTOR_HV.asStack(), 'C', CustomTags.EV_CIRCUITS, 'D', GTMachines.HULL[GTValues.HV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("cold_ice_freezer"), MultiBlockMachineD.COLD_ICE_FREEZER.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTOBlocks.COLD_ICE_CASING.asStack(), 'B', GTItems.EMITTER_IV.asStack(), 'C', GTMultiMachines.VACUUM_FREEZER.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_steam_macerator"), MultiBlockMachineA.LARGE_STEAM_MACERATOR.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.block, GTMaterials.Bronze), 'B', RegistriesUtils.getItemStack("enderio:energetic_alloy_grinding_ball"), 'C', GTOItems.PRECISION_STEAM_MECHANISM.asStack(), 'D', GTMultiMachines.STEAM_GRINDER.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("luv_rocket_engine"), GTOMachines.ROCKET_ENGINE_GENERATOR[GTValues.LuV].asStack(),
                "ABA",
                "CDC",
                "EFE",
                'A', new UnificationEntry(TagPrefix.rotor, GTMaterials.RhodiumPlatedPalladium), 'B', CustomTags.LuV_CIRCUITS, 'C', GTItems.ELECTRIC_MOTOR_LuV.asStack(), 'D', GTMachines.HULL[GTValues.LuV].asStack(), 'E', new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Osmium), 'F', GTItems.ELECTRIC_PUMP_LuV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("luv_naquadah_reactor"), GTOMachines.NAQUADAH_REACTOR_GENERATOR[GTValues.LuV].asStack(),
                "ABA",
                "CDC",
                "EBE",
                'A', new UnificationEntry(TagPrefix.rod, GTMaterials.NaquadahEnriched), 'B', CustomTags.LuV_CIRCUITS, 'C', GTItems.FIELD_GENERATOR_LuV.asStack(), 'D', GTMachines.HULL[GTValues.LuV].asStack(), 'E', new UnificationEntry(TagPrefix.cableGtQuadruple, GTMaterials.NiobiumNitride));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("neutronium_gearbox"), GTOBlocks.NEUTRONIUM_GEARBOX.asStack(2),
                "ABA",
                "CDC",
                "AEA",
                'A', new UnificationEntry(TagPrefix.plate, GTMaterials.Neutronium), 'B', TagUtils.createTag(new ResourceLocation("forge", "tools/hammers")), 'C', new UnificationEntry(TagPrefix.gear, GTMaterials.Neutronium), 'D', new UnificationEntry(TagPrefix.frameGt, GTMaterials.Neutronium), 'E', TagUtils.createTag(new ResourceLocation("forge", "tools/wrench")));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("annihilation_core"), new ItemStack(AEItems.ANNIHILATION_CORE.asItem()),
                "ABC",
                'A', CustomTags.ULV_CIRCUITS, 'B', new ItemStack(AEItems.LOGIC_PROCESSOR.asItem()), 'C', new UnificationEntry(TagPrefix.dust, GTMaterials.NetherQuartz));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("gravitation_shockburst"), MultiBlockMachineB.GRAVITATION_SHOCKBURST.asStack(),
                "ABA",
                "BCB",
                "ABA",
                'A', GTOBlocks.CREATE_CASING.asStack(), 'B', GTOItems.SUPRACHRONAL_MAINFRAME_COMPLEX.asStack(), 'C', GTOItems.CREATE_ULTIMATE_BATTERY.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("lava_furnace"), MultiBlockMachineA.LAVA_FURNACE.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', new UnificationEntry(TagPrefix.plateDouble, GTMaterials.Copper), 'B', new UnificationEntry(TagPrefix.plateDouble, GTMaterials.WroughtIron), 'C', new UnificationEntry(TagPrefix.cableGtHex, GTMaterials.Tin), 'D', GTMultiMachines.STEAM_OVEN.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("sterile_cleaning_maintenance_hatch"), GTOMachines.STERILE_CLEANING_MAINTENANCE_HATCH.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTBlocks.FILTER_CASING_STERILE.asStack(), 'B', RegistriesUtils.getItemStack("gtceu:cleaning_maintenance_hatch"), 'C', GTItems.FIELD_GENERATOR_ZPM.asStack(), 'D', GTMachines.HULL[GTValues.ZPM].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("fluid_storage_cell_1m"), GTOItems.FLUID_STORAGE_CELL_1M.asStack(),
                "ABA",
                "BCB",
                "DDD",
                'A', new ItemStack(AEBlocks.QUARTZ_GLASS.block().asItem()), 'B', new UnificationEntry(TagPrefix.dust, GTMaterials.Redstone), 'C', GTOItems.CELL_COMPONENT_1M.asStack(), 'D', new UnificationEntry(TagPrefix.ingot, GTMaterials.Copper));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("dragon_egg_copier"), MultiBlockMachineA.DRAGON_EGG_COPIER.asStack(),
                "ABA",
                "CDC",
                "ABA",
                'A', GTOBlocks.DRAGON_STRENGTH_TRITANIUM_CASING.asStack(), 'B', GTItems.FIELD_GENERATOR_UXV.asStack(), 'C', GTItems.ROBOT_ARM_UXV.asStack(), 'D', new ItemStack(Blocks.DRAGON_EGG.asItem()));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("steam_ore_washer"), MultiBlockMachineA.STEAM_ORE_WASHER.asStack(),
                "ABA",
                "BCB",
                "DBD",
                'A', new UnificationEntry(TagPrefix.screw, GTMaterials.Rubber), 'B', new UnificationEntry(TagPrefix.plate, GTMaterials.Steel), 'C', GTBlocks.CASING_BRONZE_PIPE.asStack(), 'D', new UnificationEntry(TagPrefix.gear, GTMaterials.Potin));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("export_bus"), RegistriesUtils.getItemStack("ae2:export_bus"),
                "ABC",
                'A', new ItemStack(AEItems.FORMATION_CORE.asItem()), 'B', GTItems.ROBOT_ARM_LV.asStack(), 'C', RegistriesUtils.getItemStack("ae2:fluix_glass_cable"));
    }
}

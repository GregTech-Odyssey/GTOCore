package com.gtocore.data.recipe;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.machines.GTAEMachines;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import appeng.core.definitions.AEBlocks;
import com.hepdd.gtmthings.data.CustomItems;
import com.hepdd.gtmthings.data.CustomMachines;
import com.hepdd.gtmthings.data.WirelessMachines;

import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.SCANNER_RECIPES;

public final class GTMTRecipe {

    public static void init() {
        ASSEMBLER_RECIPES.builder("virtual_item_supply_machine")
                .inputItems(GTOMachines.PROGRAMMABLEC_HATCH[2].asItem())
                .inputItems(CustomItems.PROGRAMMABLE_COVER.get(), 16)
                .inputItems("gtmthings:virtual_item_provider", 4)
                .inputItems(GTItems.ROBOT_ARM_MV, 16)
                .outputItems(GTAEMachines.VIRTUAL_ITEM_SUPPLY_MACHINE.asItem())
                .EUt(120)
                .duration(400)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("programmable_cover")
                .inputItems(GTItems.ROBOT_ARM_MV, 2)
                .inputItems(CustomItems.VIRTUAL_ITEM_PROVIDER.asItem())
                .inputItems(CustomTags.HV_CIRCUITS, 2)
                .outputItems(CustomItems.PROGRAMMABLE_COVER)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .duration(200)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("virtual_item_provider")
                .inputItems(CustomTags.LV_CIRCUITS)
                .inputItems(AEBlocks.QUARTZ_VIBRANT_GLASS.block().asItem())
                .inputItems(TagPrefix.foil, GTMaterials.PolyvinylChloride, 8)
                .outputItems(CustomItems.VIRTUAL_ITEM_PROVIDER.asItem())
                .EUt(480)
                .duration(200)
                .save();

        SCANNER_RECIPES.recipeBuilder("wireless_energy_binding_tool")
                .inputItems(TagPrefix.plate, GTMaterials.Paper)
                .inputItems(GTItems.COVER_SCREEN)
                .outputItems(CustomItems.WIRELESS_ENERGY_BINDING_TOOL.asItem())
                .EUt(30)
                .duration(400)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("cover_maintenance_detector")
                .inputItems(GTItems.EMITTER_LV)
                .inputItems(TagPrefix.plate, GTMaterials.Steel)
                .circuitMeta(1)
                .inputFluids(GTMaterials.SolderingAlloy, 72)
                .outputItems(GTItems.COVER_MAINTENANCE_DETECTOR)
                .EUt(16).duration(100)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_monitor")
                .inputItems(GTMachines.HULL[1].asItem())
                .inputItems(GTItems.COVER_SCREEN)
                .inputItems(Items.ENDER_PEARL, 16)
                .inputItems(GTItems.TERMINAL)
                .inputItems(CustomTags.LV_CIRCUITS, 4)
                .inputItems(TagPrefix.foil, GTMaterials.Steel, 16)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(WirelessMachines.WIRELESS_ENERGY_MONITOR.asItem())
                .duration(400)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_interface")
                .inputItems(GTMachines.ENERGY_INPUT_HATCH[1].asItem())
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV.asItem())
                .inputItems(Items.ENDER_PEARL, 16)
                .inputItems(CustomTags.LV_CIRCUITS, 4)
                .inputItems(TagPrefix.spring, GTMaterials.Iron, 4)
                .inputItems(TagPrefix.foil, GTMaterials.Steel, 16)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(WirelessMachines.WIRELESS_ENERGY_INTERFACE.asItem())
                .duration(400)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_item_transfer_cover")
                .inputItems(GTItems.SENSOR_LV)
                .inputItems(GTItems.EMITTER_LV)
                .inputItems(GTItems.ROBOT_ARM_LV)
                .inputItems(CustomTags.LV_CIRCUITS, 2)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.Steel, 2)
                .inputFluids(GTMaterials.Polyethylene, 288)
                .outputItems(CustomItems.WIRELESS_ITEM_TRANSFER_COVER.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_fluid_transfer_cover")
                .inputItems(GTItems.SENSOR_LV)
                .inputItems(GTItems.EMITTER_LV)
                .inputItems(GTItems.FLUID_REGULATOR_LV)
                .inputItems(CustomTags.LV_CIRCUITS, 2)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.Steel, 2)
                .inputFluids(GTMaterials.Polyethylene, 288)
                .outputItems(CustomItems.WIRELESS_FLUID_TRANSFER_COVER.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_lv")
                .inputItems(GTItems.SENSOR_LV)
                .inputItems(GTItems.EMITTER_LV)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.LV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_LV)
                .inputItems(TagPrefix.spring, GTMaterials.Tin, 1)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Tin, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Steel, 4)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_lv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV.asItem(), 2)
                .inputItems(GTItems.INDUCTOR, 4)
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Tin, 4)
                .inputItems(GTItems.VOLTAGE_COIL_LV, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_mv")
                .inputItems(GTItems.SENSOR_MV)
                .inputItems(GTItems.EMITTER_MV)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.MV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_MV)
                .inputItems(GTItems.ULTRA_LOW_POWER_INTEGRATED_CIRCUIT)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Copper, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Aluminium, 4)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_mv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV.asItem(), 2)
                .inputItems(GTItems.INDUCTOR, 8)
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Copper, 4)
                .inputItems(GTItems.VOLTAGE_COIL_MV, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_hv")
                .inputItems(GTItems.SENSOR_HV)
                .inputItems(GTItems.EMITTER_HV)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.HV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_HV)
                .inputItems(GTItems.LOW_POWER_INTEGRATED_CIRCUIT)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Gold, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.StainlessSteel, 4)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.HV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_hv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV.asItem(), 2)
                .inputItems(GTItems.SMD_INDUCTOR, 4)
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Gold, 4)
                .inputItems(GTItems.VOLTAGE_COIL_HV, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.HV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_ev")
                .inputItems(GTItems.SENSOR_EV)
                .inputItems(GTItems.EMITTER_EV)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.EV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_EV)
                .inputItems(GTItems.POWER_INTEGRATED_CIRCUIT)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Aluminium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Titanium, 4)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.EV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_ev_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV.asItem(), 2)
                .inputItems(GTItems.SMD_INDUCTOR, 8)
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Aluminium, 4)
                .inputItems(GTItems.VOLTAGE_COIL_EV, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.EV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_iv")
                .inputItems(GTItems.SENSOR_IV)
                .inputItems(GTItems.EMITTER_IV)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.IV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_IV)
                .inputItems(GTItems.HIGH_POWER_INTEGRATED_CIRCUIT)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Platinum, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.TungstenSteel, 4)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.IV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_iv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV.asItem(), 2)
                .inputItems(GTItems.ADVANCED_SMD_INDUCTOR, 4)
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Platinum, 4)
                .inputItems(GTItems.VOLTAGE_COIL_IV, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.IV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_luv")
                .inputItems(GTItems.SENSOR_LuV)
                .inputItems(GTItems.EMITTER_LuV)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.LuV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_LuV)
                .inputItems(GTItems.HIGH_POWER_INTEGRATED_CIRCUIT, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.NiobiumTitanium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.RhodiumPlatedPalladium, 4)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_luv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV.asItem(), 2)
                .inputItems(GTItems.ADVANCED_SMD_INDUCTOR, 8)
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.NiobiumTitanium, 4)
                .inputItems(GTItems.VOLTAGE_COIL_LuV, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_zpm")
                .inputItems(GTItems.SENSOR_ZPM)
                .inputItems(GTItems.EMITTER_ZPM)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.ZPM_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_ZPM)
                .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.VanadiumGallium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.NaquadahAlloy, 4)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_zpm_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM.asItem(), 2)
                .inputItems(GTItems.ADVANCED_SMD_INDUCTOR, 16)
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.VanadiumGallium, 4)
                .inputItems(GTItems.VOLTAGE_COIL_ZPM, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uv")
                .inputItems(GTItems.SENSOR_UV)
                .inputItems(GTItems.EMITTER_UV)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_UV)
                .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.YttriumBariumCuprate, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Darmstadtium, 4)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV.asItem(), 2)
                .inputItems(GTItems.ADVANCED_SMD_INDUCTOR, 32)
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.YttriumBariumCuprate, 4)
                .inputItems(GTItems.VOLTAGE_COIL_UV, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uhv")
                .inputItems(GTItems.SENSOR_UHV)
                .inputItems(GTItems.EMITTER_UHV)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UHV_CIRCUITS, 2)
                .inputItems(GTOItems.UHV_VOLTAGE_COIL)
                .inputItems(GTOItems.NM_CHIP)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Europium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Neutronium, 4)
                .inputFluids(GTOMaterials.MutatedLivingSolder, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UHV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uhv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV.asItem(), 2)
                .inputItems(GTOItems.SMD_INDUCTOR_BIOWARE, 8)
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Europium, 4)
                .inputItems(GTOItems.UHV_VOLTAGE_COIL, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.MutatedLivingSolder, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UHV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uev")
                .inputItems(GTItems.SENSOR_UEV)
                .inputItems(GTItems.EMITTER_UEV)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UEV_CIRCUITS, 2)
                .inputItems(GTOItems.UEV_VOLTAGE_COIL)
                .inputItems(GTOItems.NM_CHIP, 2)
                .inputItems(TagPrefix.cableGtSingle, GTOMaterials.Mithril, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.Quantanium, 4)
                .inputFluids(GTOMaterials.MutatedLivingSolder, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UEV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uev_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV.asItem(), 2)
                .inputItems(GTOItems.SMD_INDUCTOR_OPTICAL, 8)
                .inputItems(TagPrefix.cableGtQuadruple, GTOMaterials.Mithril, 4)
                .inputItems(GTOItems.UEV_VOLTAGE_COIL, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.MutatedLivingSolder, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UEV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uiv")
                .inputItems(GTItems.SENSOR_UIV)
                .inputItems(GTItems.EMITTER_UIV)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UIV_CIRCUITS, 2)
                .inputItems(GTOItems.UIV_VOLTAGE_COIL)
                .inputItems(GTOItems.PM_CHIP)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Neutronium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.Adamantine, 4)
                .inputFluids(GTOMaterials.MutatedLivingSolder, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UIV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uiv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV.asItem(), 2)
                .inputItems(GTOItems.SMD_DIODE_EXOTIC, 8)
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Neutronium, 4)
                .inputItems(GTOItems.UIV_VOLTAGE_COIL, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.MutatedLivingSolder, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UIV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uxv")
                .inputItems(GTItems.SENSOR_UXV)
                .inputItems(GTItems.EMITTER_UXV)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UXV_CIRCUITS, 2)
                .inputItems(GTOItems.UXV_VOLTAGE_COIL)
                .inputItems(GTOItems.PM_CHIP, 2)
                .inputItems(TagPrefix.cableGtSingle, GTOMaterials.Taranium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.Vibranium, 4)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UXV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uxv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV.asItem(), 2)
                .inputItems(GTOItems.SMD_DIODE_COSMIC, 8)
                .inputItems(TagPrefix.cableGtQuadruple, GTOMaterials.Taranium, 4)
                .inputItems(GTOItems.UXV_VOLTAGE_COIL, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UXV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_opv")
                .inputItems(GTItems.SENSOR_OpV)
                .inputItems(GTItems.EMITTER_OpV)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.OpV_CIRCUITS, 2)
                .inputItems(GTOItems.OPV_VOLTAGE_COIL)
                .inputItems(GTOItems.FM_CHIP)
                .inputItems(TagPrefix.cableGtSingle, GTOMaterials.CrystalMatrix, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.Draconium, 4)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.OpV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_opv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV.asItem(), 2)
                .inputItems(GTOItems.SMD_INDUCTOR_SUPRACAUSAL, 8)
                .inputItems(TagPrefix.cableGtQuadruple, GTOMaterials.CrystalMatrix, 4)
                .inputItems(GTOItems.OPV_VOLTAGE_COIL, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 144)
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.OpV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_max")
                .inputItems(GTOItems.MAX_SENSOR)
                .inputItems(GTOItems.MAX_EMITTER)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.MAX_CIRCUITS, 2)
                .inputItems(GTOItems.MAX_VOLTAGE_COIL)
                .inputItems(GTOItems.FM_CHIP, 2)
                .inputItems(TagPrefix.cableGtSingle, GTOMaterials.CosmicNeutronium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.ChaosInfinityAlloy, 4)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 144)
                .outputItems(GTOItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX)
                .duration(200)
                .EUt(GTValues.VA[GTValues.MAX])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_max_4a")
                .inputItems(GTOItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX, 2)
                .inputItems(GTOItems.SMD_INDUCTOR_SUPRACAUSAL, 32)
                .inputItems(TagPrefix.cableGtQuadruple, GTOMaterials.CosmicNeutronium, 4)
                .inputItems(GTOItems.MAX_VOLTAGE_COIL, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 144)
                .outputItems(GTOItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX_4A)
                .duration(200)
                .EUt(GTValues.VA[GTValues.MAX])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("mv_bedrock_ore_miner")
                .inputItems(GTOMachines.DIGITAL_MINER[GTValues.LV].asItem(), 4)
                .inputItems(GTItems.FIELD_GENERATOR_LV, 4)
                .inputItems(GTItems.CONVEYOR_MODULE_MV, 4)
                .inputItems(GTItems.EMITTER_MV, 2)
                .inputItems(GTItems.SENSOR_MV, 2)
                .inputItems(CustomTags.HV_CIRCUITS, 4)
                .inputFluids(GTMaterials.SolderingAlloy, 288)
                .outputItems(GTMultiMachines.BEDROCK_ORE_MINER[GTValues.MV].asItem())
                .duration(400)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("hv_bedrock_ore_miner")
                .inputItems(GTOMachines.DIGITAL_MINER[GTValues.MV].asItem(), 4)
                .inputItems(GTItems.FIELD_GENERATOR_MV, 4)
                .inputItems(GTItems.CONVEYOR_MODULE_HV, 4)
                .inputItems(GTItems.EMITTER_HV, 2)
                .inputItems(GTItems.SENSOR_HV, 2)
                .inputItems(CustomTags.EV_CIRCUITS, 4)
                .inputFluids(GTMaterials.SolderingAlloy, 288)
                .outputItems(GTMultiMachines.BEDROCK_ORE_MINER[GTValues.HV].asItem())
                .duration(400)
                .EUt(GTValues.VA[GTValues.HV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("ev_bedrock_ore_miner")
                .inputItems(GTOMachines.DIGITAL_MINER[GTValues.HV].asItem(), 4)
                .inputItems(GTItems.FIELD_GENERATOR_HV, 4)
                .inputItems(GTItems.CONVEYOR_MODULE_EV, 4)
                .inputItems(GTItems.EMITTER_EV, 2)
                .inputItems(GTItems.SENSOR_EV, 2)
                .inputItems(CustomTags.IV_CIRCUITS, 4)
                .inputFluids(GTMaterials.SolderingAlloy, 288)
                .outputItems(GTMultiMachines.BEDROCK_ORE_MINER[GTValues.EV].asItem())
                .duration(400)
                .EUt(GTValues.VA[GTValues.EV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("me_export_buffer")
                .inputItems(GTMachines.BUFFER[GTValues.LuV].asItem())
                .inputItems(GTAEMachines.ITEM_EXPORT_BUS_ME.asItem())
                .inputItems(GTAEMachines.FLUID_EXPORT_HATCH_ME.asItem())
                .inputFluids(GTMaterials.SolderingAlloy, 576)
                .outputItems(CustomMachines.ME_EXPORT_BUFFER.asItem())
                .duration(400)
                .EUt(GTValues.VA[GTValues.LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_computation_transmitter_hatch")
                .inputItems(GTResearchMachines.COMPUTATION_HATCH_TRANSMITTER.asItem())
                .inputItems(CustomTags.UV_CIRCUITS)
                .inputItems(GTItems.EMITTER_ZPM)
                .inputFluids(GTMaterials.Polybenzimidazole, 576)
                .outputItems(WirelessMachines.WIRELESS_COMPUTATION_HATCH_TRANSMITTER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_computation_receiver_hatch")
                .inputItems(GTResearchMachines.COMPUTATION_HATCH_RECEIVER.asItem())
                .inputItems(CustomTags.UV_CIRCUITS)
                .inputItems(GTItems.SENSOR_ZPM)
                .inputFluids(GTMaterials.Polybenzimidazole, 576)
                .outputItems(WirelessMachines.WIRELESS_COMPUTATION_HATCH_RECEIVER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("advanced_wireless_item_transfer_cover")
                .inputItems(CustomItems.WIRELESS_ITEM_TRANSFER_COVER.asItem())
                .inputItems(CustomTags.MV_CIRCUITS)
                .outputItems(CustomItems.ADVANCED_WIRELESS_ITEM_TRANSFER_COVER)
                .inputFluids(GTMaterials.SolderingAlloy, 72)
                .duration(100)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("advanced_wireless_fluid_transfer_cover")
                .inputItems(CustomItems.WIRELESS_FLUID_TRANSFER_COVER.asItem())
                .inputItems(CustomTags.MV_CIRCUITS)
                .outputItems(CustomItems.ADVANCED_WIRELESS_FLUID_TRANSFER_COVER)
                .inputFluids(GTMaterials.SolderingAlloy, 72)
                .duration(100)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.builder("lv_digital_miner")
                .inputItems(GTMachines.MINER[GTValues.LV].asItem())
                .inputItems(GTItems.ROBOT_ARM_LV, 2)
                .inputItems(GTItems.EMITTER_LV, 2)
                .inputItems(GTItems.SENSOR_LV, 2)
                .inputItems(CustomTags.MV_CIRCUITS, 4)
                .inputItems(GTOTagPrefix.cableGtSingle, GTMaterials.Tin, 4)
                .outputItems(GTOMachines.DIGITAL_MINER[GTValues.LV].asItem())
                .inputFluids(GTMaterials.SolderingAlloy, 576)
                .duration(400)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.builder("mv_digital_miner")
                .inputItems(GTMachines.MINER[GTValues.MV].asItem())
                .inputItems(GTItems.ROBOT_ARM_MV, 2)
                .inputItems(GTItems.EMITTER_MV, 2)
                .inputItems(GTItems.SENSOR_MV, 2)
                .inputItems(CustomTags.HV_CIRCUITS, 4)
                .inputItems(GTOTagPrefix.cableGtSingle, GTMaterials.Copper, 4)
                .outputItems(GTOMachines.DIGITAL_MINER[GTValues.MV].asItem())
                .inputFluids(GTMaterials.SolderingAlloy, 576)
                .duration(400)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.builder("hv_digital_miner")
                .inputItems(GTMachines.MINER[GTValues.HV].asItem())
                .inputItems(GTItems.ROBOT_ARM_HV, 2)
                .inputItems(GTItems.EMITTER_HV, 2)
                .inputItems(GTItems.SENSOR_HV, 2)
                .inputItems(CustomTags.EV_CIRCUITS, 4)
                .inputItems(GTOTagPrefix.cableGtSingle, GTMaterials.Gold, 4)
                .outputItems(GTOMachines.DIGITAL_MINER[GTValues.HV].asItem())
                .inputFluids(GTMaterials.SolderingAlloy, 576)
                .duration(400)
                .EUt(GTValues.VA[GTValues.HV])
                .save();

        VanillaRecipeHelper.addShapedRecipe(true, "advanced_terminal", CustomItems.ADVANCED_TERMINAL.asItem(),
                "SGS", "PBP", "PWP",
                'S', new MaterialEntry(TagPrefix.screw, GTMaterials.Steel),
                'G', Tags.Items.GLASS_PANES,
                'B', CustomTags.LV_CIRCUITS,
                'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel),
                'W', GTItems.TERMINAL);

        VanillaRecipeHelper.addShapedRecipe("wireless_energy_terminal", CustomItems.WIRELESS_ENERGY_TERMINAL.asItem(),
                "ABA",
                "CDC",
                "ACA",
                'A', new MaterialEntry(TagPrefix.foil, GTMaterials.Tin), 'B', GTItems.COVER_SCREEN.asItem(), 'C', new MaterialEntry(TagPrefix.gem, GTMaterials.EnderPearl), 'D', CustomItems.ADVANCED_TERMINAL.asItem());
    }
}

package com.gtocore.data.recipe;

import com.gtocore.common.data.GTOItems;
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

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.hepdd.gtmthings.data.CustomItems;
import com.hepdd.gtmthings.data.CustomMachines;
import com.hepdd.gtmthings.data.WirelessMachines;

import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.SCANNER_RECIPES;

public final class GTMTRecipe {

    public static void init() {
        ASSEMBLER_RECIPES.recipeBuilder("programmable_cover")
                .inputItems(GTItems.ROBOT_ARM_MV.asStack(2))
                .inputItems(CustomItems.VIRTUAL_ITEM_PROVIDER.asStack())
                .inputItems(CustomTags.HV_CIRCUITS, 2)
                .outputItems(CustomItems.PROGRAMMABLE_COVER)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .duration(200)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("virtual_item_provider")
                .inputItems(GTItems.PROGRAMMED_CIRCUIT.asItem())
                .inputItems(new ItemStack(AEBlocks.QUARTZ_VIBRANT_GLASS.block().asItem()))
                .inputItems(TagPrefix.foil, GTMaterials.PolyvinylChloride, 8)
                .outputItems(CustomItems.VIRTUAL_ITEM_PROVIDER.asItem())
                .EUt(480)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("virtual_item_provider_cell")
                .inputItems(new ItemStack(AEItems.ITEM_CELL_256K.asItem()))
                .inputItems(CustomItems.VIRTUAL_ITEM_PROVIDER.asItem())
                .inputItems(GTItems.CONVEYOR_MODULE_HV.asStack(2))
                .inputFluids(GTMaterials.Polyethylene.getFluid(288))
                .outputItems(CustomItems.VIRTUAL_ITEM_PROVIDER_CELL.asItem())
                .EUt(480)
                .duration(800)
                .save();

        SCANNER_RECIPES.recipeBuilder("wireless_energy_binding_tool")
                .inputItems(TagPrefix.plate, GTMaterials.Paper)
                .inputItems(GTItems.COVER_SCREEN.asItem())
                .outputItems(CustomItems.WIRELESS_ENERGY_BINDING_TOOL.asItem())
                .EUt(30)
                .duration(400)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("cover_maintenance_detector")
                .inputItems(GTItems.EMITTER_LV)
                .inputItems(TagPrefix.plate, GTMaterials.Steel)
                .circuitMeta(1)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(72))
                .outputItems(GTItems.COVER_MAINTENANCE_DETECTOR)
                .EUt(16).duration(100)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_monitor")
                .inputItems(GTMachines.HULL[1].getItem())
                .inputItems(GTItems.COVER_SCREEN.asItem())
                .inputItems(Items.ENDER_PEARL, 16)
                .inputItems(GTItems.TERMINAL.asItem())
                .inputItems(CustomTags.LV_CIRCUITS, 4)
                .inputItems(TagPrefix.foil, GTMaterials.Steel, 16)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(WirelessMachines.WIRELESS_ENERGY_MONITOR.getItem())
                .duration(400)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_interface")
                .inputItems(GTMachines.ENERGY_INPUT_HATCH[1].getItem())
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV.asItem())
                .inputItems(Items.ENDER_PEARL, 16)
                .inputItems(CustomTags.LV_CIRCUITS, 4)
                .inputItems(TagPrefix.spring, GTMaterials.Iron, 4)
                .inputItems(TagPrefix.foil, GTMaterials.Steel, 16)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(WirelessMachines.WIRELESS_ENERGY_INTERFACE.getItem())
                .duration(400)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_item_transfer_cover")
                .inputItems(GTItems.SENSOR_LV.asItem())
                .inputItems(GTItems.EMITTER_LV.asItem())
                .inputItems(GTItems.ROBOT_ARM_LV.asItem())
                .inputItems(CustomTags.LV_CIRCUITS, 2)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.Steel, 2)
                .inputFluids(GTMaterials.Polyethylene.getFluid(288))
                .outputItems(CustomItems.WIRELESS_ITEM_TRANSFER_COVER.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_fluid_transfer_cover")
                .inputItems(GTItems.SENSOR_LV.asItem())
                .inputItems(GTItems.EMITTER_LV.asItem())
                .inputItems(GTItems.FLUID_REGULATOR_LV.asItem())
                .inputItems(CustomTags.LV_CIRCUITS, 2)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.Steel, 2)
                .inputFluids(GTMaterials.Polyethylene.getFluid(288))
                .outputItems(CustomItems.WIRELESS_FLUID_TRANSFER_COVER.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_lv")
                .inputItems(GTItems.SENSOR_LV.asItem())
                .inputItems(GTItems.EMITTER_LV.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.LV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_LV.asItem())
                .inputItems(TagPrefix.spring, GTMaterials.Tin, 1)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Tin, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Steel, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_lv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV.asStack(2))
                .inputItems(GTItems.INDUCTOR.asStack(4))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Tin, 4)
                .inputItems(GTItems.VOLTAGE_COIL_LV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_mv")
                .inputItems(GTItems.SENSOR_MV.asItem())
                .inputItems(GTItems.EMITTER_MV.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.MV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_MV.asItem())
                .inputItems(GTItems.ULTRA_LOW_POWER_INTEGRATED_CIRCUIT.asItem())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Copper, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Aluminium, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_mv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV.asStack(2))
                .inputItems(GTItems.INDUCTOR.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Copper, 4)
                .inputItems(GTItems.VOLTAGE_COIL_MV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_hv")
                .inputItems(GTItems.SENSOR_HV.asItem())
                .inputItems(GTItems.EMITTER_HV.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.HV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_HV.asItem())
                .inputItems(GTItems.LOW_POWER_INTEGRATED_CIRCUIT.asItem())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Gold, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.StainlessSteel, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.HV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_hv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV.asStack(2))
                .inputItems(GTItems.SMD_INDUCTOR.asStack(4))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Gold, 4)
                .inputItems(GTItems.VOLTAGE_COIL_HV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.HV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_ev")
                .inputItems(GTItems.SENSOR_EV.asItem())
                .inputItems(GTItems.EMITTER_EV.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.EV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_EV.asItem())
                .inputItems(GTItems.POWER_INTEGRATED_CIRCUIT.asItem())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Aluminium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Titanium, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.EV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_ev_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV.asStack(2))
                .inputItems(GTItems.SMD_INDUCTOR.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Aluminium, 4)
                .inputItems(GTItems.VOLTAGE_COIL_EV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.EV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_iv")
                .inputItems(GTItems.SENSOR_IV.asItem())
                .inputItems(GTItems.EMITTER_IV.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.IV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_IV.asItem())
                .inputItems(GTItems.HIGH_POWER_INTEGRATED_CIRCUIT.asItem())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Platinum, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.TungstenSteel, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.IV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_iv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV.asStack(2))
                .inputItems(GTItems.ADVANCED_SMD_INDUCTOR.asStack(4))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Platinum, 4)
                .inputItems(GTItems.VOLTAGE_COIL_IV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.IV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_luv")
                .inputItems(GTItems.SENSOR_LuV.asItem())
                .inputItems(GTItems.EMITTER_LuV.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.LuV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_LuV.asItem())
                .inputItems(GTItems.HIGH_POWER_INTEGRATED_CIRCUIT.asStack(2))
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.NiobiumTitanium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.RhodiumPlatedPalladium, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_luv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV.asStack(2))
                .inputItems(GTItems.ADVANCED_SMD_INDUCTOR.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.NiobiumTitanium, 4)
                .inputItems(GTItems.VOLTAGE_COIL_LuV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_zpm")
                .inputItems(GTItems.SENSOR_ZPM.asItem())
                .inputItems(GTItems.EMITTER_ZPM.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.ZPM_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_ZPM.asItem())
                .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT.asItem())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.VanadiumGallium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.NaquadahAlloy, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_zpm_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM.asStack(2))
                .inputItems(GTItems.ADVANCED_SMD_INDUCTOR.asStack(16))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.VanadiumGallium, 4)
                .inputItems(GTItems.VOLTAGE_COIL_ZPM.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uv")
                .inputItems(GTItems.SENSOR_UV.asItem())
                .inputItems(GTItems.EMITTER_UV.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_UV.asItem())
                .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT.asStack(2))
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.YttriumBariumCuprate, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Darmstadtium, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV.asStack(2))
                .inputItems(GTItems.ADVANCED_SMD_INDUCTOR.asStack(32))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.YttriumBariumCuprate, 4)
                .inputItems(GTItems.VOLTAGE_COIL_UV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uhv")
                .inputItems(GTItems.SENSOR_UHV.asItem())
                .inputItems(GTItems.EMITTER_UHV.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UHV_CIRCUITS, 2)
                .inputItems(GTOItems.UHV_VOLTAGE_COIL.asItem())
                .inputItems(GTOItems.NM_CHIP.asItem())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Europium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Neutronium, 4)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UHV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uhv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV.asStack(2))
                .inputItems(GTOItems.SMD_INDUCTOR_BIOWARE.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Europium, 4)
                .inputItems(GTOItems.UHV_VOLTAGE_COIL.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UHV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uev")
                .inputItems(GTItems.SENSOR_UEV.asItem())
                .inputItems(GTItems.EMITTER_UEV.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UEV_CIRCUITS, 2)
                .inputItems(GTOItems.UEV_VOLTAGE_COIL.asItem())
                .inputItems(GTOItems.NM_CHIP.asStack(2))
                .inputItems(TagPrefix.cableGtSingle, GTOMaterials.Mithril, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.Quantanium, 4)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UEV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uev_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV.asStack(2))
                .inputItems(GTOItems.SMD_INDUCTOR_OPTICAL.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTOMaterials.Mithril, 4)
                .inputItems(GTOItems.UEV_VOLTAGE_COIL.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UEV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uiv")
                .inputItems(GTItems.SENSOR_UIV.asItem())
                .inputItems(GTItems.EMITTER_UIV.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UIV_CIRCUITS, 2)
                .inputItems(GTOItems.UIV_VOLTAGE_COIL.asItem())
                .inputItems(GTOItems.PM_CHIP.asItem())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Neutronium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.Adamantine, 4)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UIV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uiv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV.asStack(2))
                .inputItems(GTOItems.SMD_DIODE_EXOTIC.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Neutronium, 4)
                .inputItems(GTOItems.UIV_VOLTAGE_COIL.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UIV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uxv")
                .inputItems(GTItems.SENSOR_UXV.asItem())
                .inputItems(GTItems.EMITTER_UXV.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UXV_CIRCUITS, 2)
                .inputItems(GTOItems.UXV_VOLTAGE_COIL.asItem())
                .inputItems(GTOItems.PM_CHIP.asStack(2))
                .inputItems(TagPrefix.cableGtSingle, GTOMaterials.Taranium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.Vibranium, 4)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UXV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_uxv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV.asStack(2))
                .inputItems(GTOItems.SMD_DIODE_COSMIC.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTOMaterials.Taranium, 4)
                .inputItems(GTOItems.UXV_VOLTAGE_COIL.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UXV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_opv")
                .inputItems(GTItems.SENSOR_OpV.asItem())
                .inputItems(GTItems.EMITTER_OpV.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.OpV_CIRCUITS, 2)
                .inputItems(GTOItems.OPV_VOLTAGE_COIL.asItem())
                .inputItems(GTOItems.FM_CHIP.asItem())
                .inputItems(TagPrefix.cableGtSingle, GTOMaterials.CrystalMatrix, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.Draconium, 4)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.OpV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_opv_4a")
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV.asStack(2))
                .inputItems(GTOItems.SMD_INDUCTOR_SUPRACAUSAL.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTOMaterials.CrystalMatrix, 4)
                .inputItems(GTOItems.OPV_VOLTAGE_COIL.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.OpV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_max")
                .inputItems(GTOItems.MAX_SENSOR.asItem())
                .inputItems(GTOItems.MAX_EMITTER.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.MAX_CIRCUITS, 2)
                .inputItems(GTOItems.MAX_VOLTAGE_COIL.asItem())
                .inputItems(GTOItems.FM_CHIP.asStack(2))
                .inputItems(TagPrefix.cableGtSingle, GTOMaterials.CosmicNeutronium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.ChaosInfinityAlloy, 4)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(144))
                .outputItems(GTOItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.MAX])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_energy_receive_cover_max_4a")
                .inputItems(GTOItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX.asStack(2))
                .inputItems(GTOItems.SMD_INDUCTOR_SUPRACAUSAL.asStack(32))
                .inputItems(TagPrefix.cableGtQuadruple, GTOMaterials.CosmicNeutronium, 4)
                .inputItems(GTOItems.MAX_VOLTAGE_COIL.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(144))
                .outputItems(GTOItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX_4A.asItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.MAX])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("lv_digital_miner")
                .inputItems(GTMachines.MINER[GTValues.LV].getItem())
                .inputItems(GTItems.CONVEYOR_MODULE_LV.asStack(2))
                .inputItems(GTItems.ROBOT_ARM_LV.asStack(2))
                .inputItems(GTItems.EMITTER_LV.asItem())
                .inputItems(GTItems.SENSOR_LV.asItem())
                .inputItems(CustomTags.MV_CIRCUITS, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomMachines.DIGITAL_MINER[GTValues.LV].getItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("mv_digital_miner")
                .inputItems(GTMachines.MINER[GTValues.MV].getItem())
                .inputItems(GTItems.CONVEYOR_MODULE_MV.asStack(2))
                .inputItems(GTItems.ROBOT_ARM_MV.asStack(2))
                .inputItems(GTItems.EMITTER_MV.asItem())
                .inputItems(GTItems.SENSOR_MV.asItem())
                .inputItems(CustomTags.HV_CIRCUITS, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomMachines.DIGITAL_MINER[GTValues.MV].getItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("hv_digital_miner")
                .inputItems(GTMachines.MINER[GTValues.HV].getItem())
                .inputItems(GTItems.CONVEYOR_MODULE_HV.asStack(2))
                .inputItems(GTItems.ROBOT_ARM_HV.asStack(2))
                .inputItems(GTItems.EMITTER_HV.asItem())
                .inputItems(GTItems.SENSOR_HV.asItem())
                .inputItems(CustomTags.EV_CIRCUITS, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomMachines.DIGITAL_MINER[GTValues.HV].getItem())
                .duration(200)
                .EUt(GTValues.VA[GTValues.HV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("mv_bedrock_ore_miner")
                .inputItems(CustomMachines.DIGITAL_MINER[GTValues.LV].asStack(4))
                .inputItems(GTItems.FIELD_GENERATOR_LV.asStack(4))
                .inputItems(GTItems.CONVEYOR_MODULE_MV.asStack(4))
                .inputItems(GTItems.EMITTER_MV.asStack(2))
                .inputItems(GTItems.SENSOR_MV.asStack(2))
                .inputItems(CustomTags.HV_CIRCUITS, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(288))
                .outputItems(GTMultiMachines.BEDROCK_ORE_MINER[GTValues.MV].getItem())
                .duration(400)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("hv_bedrock_ore_miner")
                .inputItems(CustomMachines.DIGITAL_MINER[GTValues.MV].asStack(4))
                .inputItems(GTItems.FIELD_GENERATOR_MV.asStack(4))
                .inputItems(GTItems.CONVEYOR_MODULE_HV.asStack(4))
                .inputItems(GTItems.EMITTER_HV.asStack(2))
                .inputItems(GTItems.SENSOR_HV.asStack(2))
                .inputItems(CustomTags.EV_CIRCUITS, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(288))
                .outputItems(GTMultiMachines.BEDROCK_ORE_MINER[GTValues.HV].getItem())
                .duration(400)
                .EUt(GTValues.VA[GTValues.HV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("ev_bedrock_ore_miner")
                .inputItems(CustomMachines.DIGITAL_MINER[GTValues.HV].asStack(4))
                .inputItems(GTItems.FIELD_GENERATOR_HV.asStack(4))
                .inputItems(GTItems.CONVEYOR_MODULE_EV.asStack(4))
                .inputItems(GTItems.EMITTER_EV.asStack(2))
                .inputItems(GTItems.SENSOR_EV.asStack(2))
                .inputItems(CustomTags.IV_CIRCUITS, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(288))
                .outputItems(GTMultiMachines.BEDROCK_ORE_MINER[GTValues.EV].getItem())
                .duration(400)
                .EUt(GTValues.VA[GTValues.EV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("me_export_buffer")
                .inputItems(GTMachines.BUFFER[GTValues.LuV].getItem())
                .inputItems(GTAEMachines.ITEM_EXPORT_BUS_ME.getItem())
                .inputItems(GTAEMachines.FLUID_EXPORT_HATCH_ME.getItem())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(576))
                .outputItems(CustomMachines.ME_EXPORT_BUFFER.getItem())
                .duration(400)
                .EUt(GTValues.VA[GTValues.LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_computation_transmitter_hatch")
                .inputItems(GTResearchMachines.COMPUTATION_HATCH_TRANSMITTER.getItem())
                .inputItems(CustomTags.UV_CIRCUITS)
                .inputItems(GTItems.SENSOR_ZPM)
                .inputFluids(GTMaterials.Polybenzimidazole.getFluid(576))
                .outputItems(WirelessMachines.WIRELESS_COMPUTATION_HATCH_TRANSMITTER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("wireless_computation_receiver_hatch")
                .inputItems(GTResearchMachines.COMPUTATION_HATCH_TRANSMITTER.getItem())
                .inputItems(CustomTags.UV_CIRCUITS)
                .inputItems(GTItems.EMITTER_ZPM)
                .inputFluids(GTMaterials.Polybenzimidazole.getFluid(576))
                .outputItems(WirelessMachines.WIRELESS_COMPUTATION_HATCH_RECEIVER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("advanced_wireless_item_transfer_cover")
                .inputItems(CustomItems.WIRELESS_ITEM_TRANSFER_COVER.asItem())
                .inputItems(CustomTags.MV_CIRCUITS)
                .outputItems(CustomItems.ADVANCED_WIRELESS_ITEM_TRANSFER_COVER)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(72))
                .duration(100)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("advanced_wireless_fluid_transfer_cover")
                .inputItems(CustomItems.WIRELESS_FLUID_TRANSFER_COVER.asItem())
                .inputItems(CustomTags.MV_CIRCUITS)
                .outputItems(CustomItems.ADVANCED_WIRELESS_FLUID_TRANSFER_COVER)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(72))
                .duration(100)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        VanillaRecipeHelper.addShapedRecipe(true, "advanced_terminal", CustomItems.ADVANCED_TERMINAL.asStack(),
                "SGS", "PBP", "PWP",
                'S', new MaterialEntry(TagPrefix.screw, GTMaterials.Steel),
                'G', Tags.Items.GLASS_PANES,
                'B', CustomTags.LV_CIRCUITS,
                'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel),
                'W', GTItems.TERMINAL);

        VanillaRecipeHelper.addShapedRecipe("wireless_energy_terminal", CustomItems.WIRELESS_ENERGY_TERMINAL.asStack(),
                "ABA",
                "CDC",
                "ACA",
                'A', new MaterialEntry(TagPrefix.foil, GTMaterials.Tin), 'B', GTItems.COVER_SCREEN.asStack(), 'C', new MaterialEntry(TagPrefix.gem, GTMaterials.EnderPearl), 'D', CustomItems.ADVANCED_TERMINAL.asStack());
    }
}

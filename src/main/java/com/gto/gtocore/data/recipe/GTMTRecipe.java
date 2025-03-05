package com.gto.gtocore.data.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMachines;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTAEMachines;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.hepdd.gtmthings.data.CustomItems;
import com.hepdd.gtmthings.data.CustomMachines;
import com.hepdd.gtmthings.data.WirelessMachines;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.VN;
import static com.gto.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;

public interface GTMTRecipe {

    static void init(Consumer<FinishedRecipe> provider) {
        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("virtual_item_provider"))
                .inputItems(GTItems.PROGRAMMED_CIRCUIT.asStack())
                .inputItems(new ItemStack(AEBlocks.QUARTZ_VIBRANT_GLASS.block().asItem()))
                .inputItems(TagPrefix.foil, GTMaterials.PolyvinylChloride, 8)
                .outputItems(CustomItems.VIRTUAL_ITEM_PROVIDER.asStack())
                .EUt(480)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("virtual_item_provider_cell"))
                .inputItems(new ItemStack(AEItems.ITEM_CELL_256K.asItem()))
                .inputItems(CustomItems.VIRTUAL_ITEM_PROVIDER.asStack())
                .inputItems(GTItems.CONVEYOR_MODULE_HV.asStack(2))
                .inputFluids(GTMaterials.Polyethylene.getFluid(288))
                .outputItems(CustomItems.VIRTUAL_ITEM_PROVIDER_CELL.asStack())
                .EUt(480)
                .duration(800)
                .save();

        GTORecipeTypes.SCANNER_RECIPES.recipeBuilder("wireless_energy_binding_tool")
                .inputItems(TagPrefix.plate, GTMaterials.Paper)
                .inputItems(GTItems.COVER_SCREEN.asStack())
                .outputItems(CustomItems.WIRELESS_ENERGY_BINDING_TOOL.asStack())
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

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_monitor"))
                .inputItems(GTMachines.HULL[1].asStack())
                .inputItems(GTItems.COVER_SCREEN.asStack())
                .inputItems(Items.ENDER_PEARL, 16)
                .inputItems(GTItems.TERMINAL.asStack())
                .inputItems(CustomTags.LV_CIRCUITS, 4)
                .inputItems(TagPrefix.foil, GTMaterials.Steel, 16)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(WirelessMachines.WIRELESS_ENERGY_MONITOR.asStack())
                .duration(400)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_interface"))
                .inputItems(GTMachines.ENERGY_INPUT_HATCH[1].asStack())
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV.asStack())
                .inputItems(Items.ENDER_PEARL, 16)
                .inputItems(CustomTags.LV_CIRCUITS, 4)
                .inputItems(TagPrefix.spring, GTMaterials.Iron, 4)
                .inputItems(TagPrefix.foil, GTMaterials.Steel, 16)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(WirelessMachines.WIRELESS_ENERGY_INTERFACE.asStack())
                .duration(400)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_item_transfer_cover"))
                .inputItems(GTItems.SENSOR_LV.asStack())
                .inputItems(GTItems.EMITTER_LV.asStack())
                .inputItems(GTItems.ROBOT_ARM_LV.asStack())
                .inputItems(CustomTags.LV_CIRCUITS, 2)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.Steel, 2)
                .inputFluids(GTMaterials.Polyethylene.getFluid(288))
                .outputItems(CustomItems.WIRELESS_ITEM_TRANSFER_COVER.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_fluid_transfer_cover"))
                .inputItems(GTItems.SENSOR_LV.asStack())
                .inputItems(GTItems.EMITTER_LV.asStack())
                .inputItems(GTItems.FLUID_REGULATOR_LV.asStack())
                .inputItems(CustomTags.LV_CIRCUITS, 2)
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.Steel, 2)
                .inputFluids(GTMaterials.Polyethylene.getFluid(288))
                .outputItems(CustomItems.WIRELESS_FLUID_TRANSFER_COVER.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_lv"))
                .inputItems(GTItems.SENSOR_LV.asStack())
                .inputItems(GTItems.EMITTER_LV.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.LV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_LV.asStack())
                .inputItems(TagPrefix.spring, GTMaterials.Tin, 1)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Tin, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Steel, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_lv_4a"))
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV.asStack(2))
                .inputItems(GTItems.INDUCTOR.asStack(4))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Tin, 4)
                .inputItems(GTItems.VOLTAGE_COIL_LV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_mv"))
                .inputItems(GTItems.SENSOR_MV.asStack())
                .inputItems(GTItems.EMITTER_MV.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.MV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_MV.asStack())
                .inputItems(GTItems.ULTRA_LOW_POWER_INTEGRATED_CIRCUIT.asStack())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Copper, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Aluminium, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_mv_4a"))
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV.asStack(2))
                .inputItems(GTItems.INDUCTOR.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Copper, 4)
                .inputItems(GTItems.VOLTAGE_COIL_MV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_hv"))
                .inputItems(GTItems.SENSOR_HV.asStack())
                .inputItems(GTItems.EMITTER_HV.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.HV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_HV.asStack())
                .inputItems(GTItems.LOW_POWER_INTEGRATED_CIRCUIT.asStack())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Gold, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.StainlessSteel, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.HV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_hv_4a"))
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV.asStack(2))
                .inputItems(GTItems.SMD_INDUCTOR.asStack(4))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Gold, 4)
                .inputItems(GTItems.VOLTAGE_COIL_HV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.HV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_ev"))
                .inputItems(GTItems.SENSOR_EV.asStack())
                .inputItems(GTItems.EMITTER_EV.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.EV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_EV.asStack())
                .inputItems(GTItems.POWER_INTEGRATED_CIRCUIT.asStack())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Aluminium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Titanium, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.EV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_ev_4a"))
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV.asStack(2))
                .inputItems(GTItems.SMD_INDUCTOR.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Aluminium, 4)
                .inputItems(GTItems.VOLTAGE_COIL_EV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.EV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_iv"))
                .inputItems(GTItems.SENSOR_IV.asStack())
                .inputItems(GTItems.EMITTER_IV.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.IV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_IV.asStack())
                .inputItems(GTItems.HIGH_POWER_INTEGRATED_CIRCUIT.asStack())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Platinum, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.TungstenSteel, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.IV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_iv_4a"))
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV.asStack(2))
                .inputItems(GTItems.ADVANCED_SMD_INDUCTOR.asStack(4))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Platinum, 4)
                .inputItems(GTItems.VOLTAGE_COIL_IV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.IV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_luv"))
                .inputItems(GTItems.SENSOR_LuV.asStack())
                .inputItems(GTItems.EMITTER_LuV.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.LuV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_LuV.asStack())
                .inputItems(GTItems.HIGH_POWER_INTEGRATED_CIRCUIT.asStack(2))
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.NiobiumTitanium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.RhodiumPlatedPalladium, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_luv_4a"))
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV.asStack(2))
                .inputItems(GTItems.ADVANCED_SMD_INDUCTOR.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.NiobiumTitanium, 4)
                .inputItems(GTItems.VOLTAGE_COIL_LuV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_zpm"))
                .inputItems(GTItems.SENSOR_ZPM.asStack())
                .inputItems(GTItems.EMITTER_ZPM.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.ZPM_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_ZPM.asStack())
                .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT.asStack())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.VanadiumGallium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.NaquadahAlloy, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_zpm_4a"))
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM.asStack(2))
                .inputItems(GTItems.ADVANCED_SMD_INDUCTOR.asStack(16))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.VanadiumGallium, 4)
                .inputItems(GTItems.VOLTAGE_COIL_ZPM.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_uv"))
                .inputItems(GTItems.SENSOR_UV.asStack())
                .inputItems(GTItems.EMITTER_UV.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UV_CIRCUITS, 2)
                .inputItems(GTItems.VOLTAGE_COIL_UV.asStack())
                .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT.asStack(2))
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.YttriumBariumCuprate, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Darmstadtium, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_uv_4a"))
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV.asStack(2))
                .inputItems(GTItems.ADVANCED_SMD_INDUCTOR.asStack(32))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.YttriumBariumCuprate, 4)
                .inputItems(GTItems.VOLTAGE_COIL_UV.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_uhv"))
                .inputItems(GTItems.SENSOR_UHV.asStack())
                .inputItems(GTItems.EMITTER_UHV.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UHV_CIRCUITS, 2)
                .inputItems(GTOItems.UHV_VOLTAGE_COIL.asStack())
                .inputItems(GTOItems.NM_CHIP.asStack())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Europium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTMaterials.Neutronium, 4)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UHV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_uhv_4a"))
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV.asStack(2))
                .inputItems(GTOItems.SMD_INDUCTOR_BIOWARE.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Europium, 4)
                .inputItems(GTOItems.UHV_VOLTAGE_COIL.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UHV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_uev"))
                .inputItems(GTItems.SENSOR_UEV.asStack())
                .inputItems(GTItems.EMITTER_UEV.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UEV_CIRCUITS, 2)
                .inputItems(GTOItems.UEV_VOLTAGE_COIL.asStack())
                .inputItems(GTOItems.NM_CHIP.asStack(2))
                .inputItems(TagPrefix.cableGtSingle, GTOMaterials.Mithril, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.Quantanium, 4)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UEV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_uev_4a"))
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV.asStack(2))
                .inputItems(GTOItems.SMD_INDUCTOR_OPTICAL.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTOMaterials.Mithril, 4)
                .inputItems(GTOItems.UEV_VOLTAGE_COIL.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UEV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_uiv"))
                .inputItems(GTItems.SENSOR_UIV.asStack())
                .inputItems(GTItems.EMITTER_UIV.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UIV_CIRCUITS, 2)
                .inputItems(GTOItems.UIV_VOLTAGE_COIL.asStack())
                .inputItems(GTOItems.PM_CHIP.asStack())
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.Neutronium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.Adamantine, 4)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UIV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_uiv_4a"))
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV.asStack(2))
                .inputItems(GTOItems.SMD_DIODE_EXOTIC.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Neutronium, 4)
                .inputItems(GTOItems.UIV_VOLTAGE_COIL.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UIV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_uxv"))
                .inputItems(GTItems.SENSOR_UXV.asStack())
                .inputItems(GTItems.EMITTER_UXV.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.UXV_CIRCUITS, 2)
                .inputItems(GTOItems.UXV_VOLTAGE_COIL.asStack())
                .inputItems(GTOItems.PM_CHIP.asStack(2))
                .inputItems(TagPrefix.cableGtSingle, GTOMaterials.Taranium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.Vibranium, 4)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UXV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_uxv_4a"))
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV.asStack(2))
                .inputItems(GTOItems.SMD_DIODE_COSMIC.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTOMaterials.Taranium, 4)
                .inputItems(GTOItems.UXV_VOLTAGE_COIL.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.UXV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_opv"))
                .inputItems(GTItems.SENSOR_OpV.asStack())
                .inputItems(GTItems.EMITTER_OpV.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.OpV_CIRCUITS, 2)
                .inputItems(GTOItems.OPV_VOLTAGE_COIL.asStack())
                .inputItems(GTOItems.FM_CHIP.asStack())
                .inputItems(TagPrefix.cableGtSingle, GTOMaterials.CrystalMatrix, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.Draconium, 4)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.OpV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_opv_4a"))
                .inputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV.asStack(2))
                .inputItems(GTOItems.SMD_INDUCTOR_SUPRACAUSAL.asStack(8))
                .inputItems(TagPrefix.cableGtQuadruple, GTOMaterials.CrystalMatrix, 4)
                .inputItems(GTOItems.OPV_VOLTAGE_COIL.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(144))
                .outputItems(CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.OpV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_max"))
                .inputItems(GTOItems.MAX_SENSOR.asStack())
                .inputItems(GTOItems.MAX_EMITTER.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.EnderPearl, 4)
                .inputItems(CustomTags.MAX_CIRCUITS, 2)
                .inputItems(GTOItems.MAX_VOLTAGE_COIL.asStack())
                .inputItems(GTOItems.FM_CHIP.asStack(2))
                .inputItems(TagPrefix.cableGtSingle, GTOMaterials.CosmicNeutronium, 2)
                .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
                .inputItems(TagPrefix.plate, GTOMaterials.Chaos, 4)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(144))
                .outputItems(GTOItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.MAX])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_max_4a"))
                .inputItems(GTOItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX.asStack(2))
                .inputItems(GTOItems.SMD_INDUCTOR_SUPRACAUSAL.asStack(32))
                .inputItems(TagPrefix.cableGtQuadruple, GTOMaterials.CosmicNeutronium, 4)
                .inputItems(GTOItems.MAX_VOLTAGE_COIL.asStack(2))
                .inputItems(TagPrefix.plateDouble, GTMaterials.BatteryAlloy, 2)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(144))
                .outputItems(GTOItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX_4A.asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.MAX])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("lv_digital_miner"))
                .inputItems(GTMachines.MINER[GTValues.LV].asStack())
                .inputItems(GTItems.CONVEYOR_MODULE_LV.asStack(2))
                .inputItems(GTItems.ROBOT_ARM_LV.asStack(2))
                .inputItems(GTItems.EMITTER_LV.asStack())
                .inputItems(GTItems.SENSOR_LV.asStack())
                .inputItems(CustomTags.MV_CIRCUITS, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomMachines.DIGITAL_MINER[GTValues.LV].asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("mv_digital_miner"))
                .inputItems(GTMachines.MINER[GTValues.MV].asStack())
                .inputItems(GTItems.CONVEYOR_MODULE_MV.asStack(2))
                .inputItems(GTItems.ROBOT_ARM_MV.asStack(2))
                .inputItems(GTItems.EMITTER_MV.asStack())
                .inputItems(GTItems.SENSOR_MV.asStack())
                .inputItems(CustomTags.HV_CIRCUITS, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomMachines.DIGITAL_MINER[GTValues.MV].asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("hv_digital_miner"))
                .inputItems(GTMachines.MINER[GTValues.HV].asStack())
                .inputItems(GTItems.CONVEYOR_MODULE_HV.asStack(2))
                .inputItems(GTItems.ROBOT_ARM_HV.asStack(2))
                .inputItems(GTItems.EMITTER_HV.asStack())
                .inputItems(GTItems.SENSOR_HV.asStack())
                .inputItems(CustomTags.EV_CIRCUITS, 2)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomMachines.DIGITAL_MINER[GTValues.HV].asStack())
                .duration(200)
                .EUt(GTValues.VA[GTValues.HV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("mv_bedrock_ore_miner"))
                .inputItems(CustomMachines.DIGITAL_MINER[GTValues.LV].asStack(4))
                .inputItems(GTItems.FIELD_GENERATOR_LV.asStack(4))
                .inputItems(GTItems.CONVEYOR_MODULE_MV.asStack(4))
                .inputItems(GTItems.EMITTER_MV.asStack(2))
                .inputItems(GTItems.SENSOR_MV.asStack(2))
                .inputItems(CustomTags.HV_CIRCUITS, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(288))
                .outputItems(GTMultiMachines.BEDROCK_ORE_MINER[GTValues.MV].asStack())
                .duration(400)
                .EUt(GTValues.VA[GTValues.MV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("hv_bedrock_ore_miner"))
                .inputItems(CustomMachines.DIGITAL_MINER[GTValues.MV].asStack(4))
                .inputItems(GTItems.FIELD_GENERATOR_MV.asStack(4))
                .inputItems(GTItems.CONVEYOR_MODULE_HV.asStack(4))
                .inputItems(GTItems.EMITTER_HV.asStack(2))
                .inputItems(GTItems.SENSOR_HV.asStack(2))
                .inputItems(CustomTags.EV_CIRCUITS, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(288))
                .outputItems(GTMultiMachines.BEDROCK_ORE_MINER[GTValues.HV].asStack())
                .duration(400)
                .EUt(GTValues.VA[GTValues.HV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("ev_bedrock_ore_miner"))
                .inputItems(CustomMachines.DIGITAL_MINER[GTValues.HV].asStack(4))
                .inputItems(GTItems.FIELD_GENERATOR_HV.asStack(4))
                .inputItems(GTItems.CONVEYOR_MODULE_EV.asStack(4))
                .inputItems(GTItems.EMITTER_EV.asStack(2))
                .inputItems(GTItems.SENSOR_EV.asStack(2))
                .inputItems(CustomTags.IV_CIRCUITS, 4)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(288))
                .outputItems(GTMultiMachines.BEDROCK_ORE_MINER[GTValues.EV].asStack())
                .duration(400)
                .EUt(GTValues.VA[GTValues.EV])
                .save();

        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("huge_item_import_bus_ulv"))
                .inputItems(GTMachines.ITEM_IMPORT_BUS[0].asStack())
                .inputItems(GTMachines.STEEL_CRATE.asStack())
                .inputItems(GTOItems.ULV_CONVEYOR_MODULE)
                .outputItems(CustomMachines.HUGE_ITEM_IMPORT_BUS[0].asStack())
                .duration(200)
                .EUt(GTValues.VA[1])
                .save();

        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("huge_item_export_bus_ulv"))
                .inputItems(GTMachines.ITEM_EXPORT_BUS[0].asStack())
                .inputItems(GTMachines.STEEL_CRATE.asStack())
                .inputItems(GTOItems.ULV_CONVEYOR_MODULE)
                .outputItems(CustomMachines.HUGE_ITEM_EXPORT_BUS[0].asStack())
                .duration(200)
                .EUt(GTValues.VA[1])
                .save();

        for (int tier : GTValues.tiersBetween(GTValues.LV, GTValues.OpV)) {
            String tierName = VN[tier].toLowerCase();
            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("huge_item_import_bus_" + tierName))
                    .inputItems(GTMachines.ITEM_IMPORT_BUS[tier].asStack())
                    .inputItems(tier > GTValues.EV ? GTMachines.QUANTUM_CHEST[tier] : GTMachines.SUPER_CHEST[tier])
                    .outputItems(CustomMachines.HUGE_ITEM_IMPORT_BUS[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier + 1])
                    .save();

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("huge_item_export_bus_" + tierName))
                    .inputItems(GTMachines.ITEM_EXPORT_BUS[tier].asStack())
                    .inputItems(tier > GTValues.EV ? GTMachines.QUANTUM_CHEST[tier] : GTMachines.SUPER_CHEST[tier])
                    .outputItems(CustomMachines.HUGE_ITEM_EXPORT_BUS[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier + 1])
                    .save();

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("huge_fluid_import_bus_" + tierName))
                    .inputItems(GTMachines.FLUID_IMPORT_HATCH[tier].asStack())
                    .inputItems(tier > GTValues.EV ? GTMachines.QUANTUM_TANK[tier] : GTMachines.SUPER_TANK[tier])
                    .outputItems(GTOMachines.HUGE_FLUID_IMPORT_HATCH[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier + 1])
                    .save();

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("huge_fluid_export_bus_" + tierName))
                    .inputItems(GTMachines.FLUID_EXPORT_HATCH[tier].asStack())
                    .inputItems(tier > GTValues.EV ? GTMachines.QUANTUM_TANK[tier] : GTMachines.SUPER_TANK[tier])
                    .outputItems(GTOMachines.HUGE_FLUID_EXPORT_HATCH[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier + 1])
                    .save();
        }

        for (int tier = GTValues.ULV; tier < GTValues.MAX; tier++) {
            String tierName = VN[tier].toLowerCase();
            MachineDefinition item_input = CustomMachines.HUGE_ITEM_IMPORT_BUS[tier];
            MachineDefinition item_output = CustomMachines.HUGE_ITEM_EXPORT_BUS[tier];
            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("huge_item_output_to_input_" + tierName), item_input.asStack(), "d", "B", 'B', item_output.asStack());
            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("huge_item_input_to_output_" + tierName), item_output.asStack(), "d", "B", 'B', item_input.asStack());

            if (tier == GTValues.ULV) continue;
            MachineDefinition fluid_input = GTOMachines.HUGE_FLUID_IMPORT_HATCH[tier];
            MachineDefinition fluid_output = GTOMachines.HUGE_FLUID_EXPORT_HATCH[tier];
            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("huge_fluid_output_to_input_" + tierName), fluid_input.asStack(), "d", "B", 'B', fluid_output.asStack());
            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("huge_fluid_input_to_output_" + tierName), fluid_output.asStack(), "d", "B", 'B', fluid_input.asStack());
        }

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("me_export_buffer"))
                .inputItems(GTMachines.BUFFER[GTValues.LuV].asStack())
                .inputItems(GTAEMachines.ITEM_EXPORT_BUS_ME.asStack())
                .inputItems(GTAEMachines.FLUID_EXPORT_HATCH_ME.asStack())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(576))
                .outputItems(CustomMachines.ME_EXPORT_BUFFER.asStack())
                .duration(400)
                .EUt(GTValues.VA[GTValues.LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_computation_transmitter_hatch"))
                .inputItems(GTResearchMachines.COMPUTATION_HATCH_TRANSMITTER.asStack())
                .inputItems(CustomTags.UV_CIRCUITS)
                .inputItems(GTItems.SENSOR_ZPM)
                .inputFluids(GTMaterials.Polybenzimidazole.getFluid(576))
                .outputItems(WirelessMachines.WIRELESS_COMPUTATION_HATCH_TRANSMITTER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_computation_receiver_hatch"))
                .inputItems(GTResearchMachines.COMPUTATION_HATCH_TRANSMITTER.asStack())
                .inputItems(CustomTags.UV_CIRCUITS)
                .inputItems(GTItems.EMITTER_ZPM)
                .inputFluids(GTMaterials.Polybenzimidazole.getFluid(576))
                .outputItems(WirelessMachines.WIRELESS_COMPUTATION_HATCH_RECEIVER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("advanced_wireless_item_transfer_cover"))
                .inputItems(CustomItems.WIRELESS_ITEM_TRANSFER_COVER.asStack())
                .inputItems(CustomTags.MV_CIRCUITS)
                .outputItems(CustomItems.ADVANCED_WIRELESS_ITEM_TRANSFER_COVER)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(72))
                .duration(100)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("advanced_wireless_fluid_transfer_cover"))
                .inputItems(CustomItems.WIRELESS_FLUID_TRANSFER_COVER.asStack())
                .inputItems(CustomTags.MV_CIRCUITS)
                .outputItems(CustomItems.ADVANCED_WIRELESS_FLUID_TRANSFER_COVER)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(72))
                .duration(100)
                .EUt(GTValues.VA[GTValues.LV])
                .save();

        VanillaRecipeHelper.addShapedRecipe(provider, true, "advanced_terminal", CustomItems.ADVANCED_TERMINAL.asStack(),
                "SGS", "PBP", "PWP",
                'S', new UnificationEntry(TagPrefix.screw, GTMaterials.Steel),
                'G', Tags.Items.GLASS_PANES,
                'B', CustomTags.LV_CIRCUITS,
                'P', new UnificationEntry(TagPrefix.plate, GTMaterials.Steel),
                'W', GTItems.TERMINAL);
    }
}

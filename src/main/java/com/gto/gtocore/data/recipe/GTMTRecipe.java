package com.gto.gtocore.data.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMachines;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTAEMachines;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import com.hepdd.gtmthings.data.CustomItems;
import com.hepdd.gtmthings.data.CustomMachines;
import com.hepdd.gtmthings.data.WirelessMachines;
import com.tterrag.registrate.util.entry.ItemEntry;

import java.util.List;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;

public class GTMTRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        ASSEMBLER_RECIPES.recipeBuilder("cover_maintenance_detector")
                .inputItems(GTItems.EMITTER_LV)
                .inputItems(TagPrefix.plate, GTMaterials.Steel)
                .circuitMeta(1)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(72))
                .outputItems(GTItems.COVER_MAINTENANCE_DETECTOR)
                .EUt(16).duration(100)
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_receive_cover_max"))
                .inputItems(GTOItems.SENSOR_MAX.asStack())
                .inputItems(GTOItems.EMITTER_MAX.asStack())
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
                .save(provider);

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
                .save(provider);

        List<ItemEntry<ComponentItem>> WIRELESS_ENERGY_RECEIVE_COVER = List.of(
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV,
                GTOItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX);

        List<ItemEntry<ComponentItem>> WIRELESS_ENERGY_RECEIVE_COVER_4A = List.of(
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV_4A,
                GTOItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX_4A);

        for (int tier : GTValues.tiersBetween(GTValues.LV, GTValues.MAX)) {
            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase()))
                    .inputItems(GTMachines.ENERGY_INPUT_HATCH[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER.get(tier - 1).asStack())
                    .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase()))
                    .inputItems(GTMachines.ENERGY_OUTPUT_HATCH[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER.get(tier - 1).asStack())
                    .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);
        }

        for (int tier : GTValues.tiersBetween(GTValues.LV, GTValues.MAX)) {
            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_4a"))
                    .inputItems(tier > GTValues.HV ? GTMachines.ENERGY_INPUT_HATCH_4A[tier].asStack() : GTOMachines.ENERGY_INPUT_HATCH_4A[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER.get(tier - 1).asStack(2))
                    .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_4A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_16a"))
                    .inputItems(tier > GTValues.HV ? GTMachines.ENERGY_INPUT_HATCH_16A[tier].asStack() : GTOMachines.ENERGY_INPUT_HATCH_16A[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack())
                    .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_16A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_4a"))
                    .inputItems(tier > GTValues.HV ? GTMachines.ENERGY_OUTPUT_HATCH_4A[tier].asStack() : GTOMachines.ENERGY_OUTPUT_HATCH_4A[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER.get(tier - 1).asStack(2))
                    .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_4A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_16a"))
                    .inputItems(tier > GTValues.HV ? GTMachines.ENERGY_OUTPUT_HATCH_16A[tier].asStack() : GTOMachines.ENERGY_OUTPUT_HATCH_16A[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack())
                    .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_16A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);
        }

        for (int tier : GTValues.tiersBetween(GTValues.EV, GTValues.MAX)) {
            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_64a"))
                    .inputItems(GTMachines.SUBSTATION_ENERGY_INPUT_HATCH[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(2))
                    .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack())
                    .outputItems(GTOMachines.WIRELESS_ENERGY_INPUT_HATCH_64A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_64a"))
                    .inputItems(GTMachines.SUBSTATION_ENERGY_OUTPUT_HATCH[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(2))
                    .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack())
                    .outputItems(GTOMachines.WIRELESS_ENERGY_OUTPUT_HATCH_64A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);
        }

        for (int tier : GTValues.tiersBetween(GTValues.IV, GTValues.MAX)) {
            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_256a"))
                    .inputItems(GTMachines.LASER_INPUT_HATCH_256[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(2))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_256A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_1024a"))
                    .inputItems(GTMachines.LASER_INPUT_HATCH_1024[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(4))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_1024A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_4096a"))
                    .inputItems(GTMachines.LASER_INPUT_HATCH_4096[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(8))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_4096A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_16384a"))
                    .inputItems(GTOMachines.LASER_INPUT_HATCH_16384[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(12))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_16384A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_65536a"))
                    .inputItems(GTOMachines.LASER_INPUT_HATCH_65536[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(16))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_65536A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_262144a"))
                    .inputItems(GTOMachines.LASER_INPUT_HATCH_262144[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(24))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(GTOMachines.WIRELESS_ENERGY_INPUT_HATCH_262144A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_1048576a"))
                    .inputItems(GTOMachines.LASER_INPUT_HATCH_1048576[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(32))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(GTOMachines.WIRELESS_ENERGY_INPUT_HATCH_1048576A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_1048576a"))
                    .inputItems(GTOMachines.LASER_INPUT_HATCH_1048576[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(48))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(GTOMachines.WIRELESS_ENERGY_INPUT_HATCH_1048576A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_4194304a"))
                    .inputItems(GTOMachines.LASER_INPUT_HATCH_4194304[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(64))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(GTOMachines.WIRELESS_ENERGY_INPUT_HATCH_4194304A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_256a"))
                    .inputItems(GTMachines.LASER_OUTPUT_HATCH_256[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(2))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_256A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_1024a"))
                    .inputItems(GTMachines.LASER_OUTPUT_HATCH_1024[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(4))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_1024A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_4096a"))
                    .inputItems(GTMachines.LASER_OUTPUT_HATCH_4096[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(8))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_4096A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_16384a"))
                    .inputItems(GTOMachines.LASER_OUTPUT_HATCH_16384[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(12))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_16384A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_65536a"))
                    .inputItems(GTOMachines.LASER_OUTPUT_HATCH_65536[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(16))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_65536A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_262144a"))
                    .inputItems(GTOMachines.LASER_OUTPUT_HATCH_262144[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(24))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(GTOMachines.WIRELESS_ENERGY_OUTPUT_HATCH_262144A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_1048576a"))
                    .inputItems(GTOMachines.LASER_OUTPUT_HATCH_1048576[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(32))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(GTOMachines.WIRELESS_ENERGY_OUTPUT_HATCH_1048576A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_1048576a"))
                    .inputItems(GTOMachines.LASER_OUTPUT_HATCH_1048576[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(48))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(GTOMachines.WIRELESS_ENERGY_OUTPUT_HATCH_1048576A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_4194304a"))
                    .inputItems(GTOMachines.LASER_OUTPUT_HATCH_4194304[tier].asStack())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(64))
                    .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
                    .outputItems(GTOMachines.WIRELESS_ENERGY_OUTPUT_HATCH_4194304A[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);
        }

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
                .save(provider);

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
                .save(provider);

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
                .save(provider);

        for (int tier : GTValues.tiersBetween(GTValues.LV, GTValues.OpV)) {
            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("huge_item_import_bus_" + GTValues.VN[tier].toLowerCase()))
                    .inputItems(GTMachines.ITEM_IMPORT_BUS[tier].asStack())
                    .inputItems(tier > GTValues.EV ? GTMachines.QUANTUM_CHEST[tier] : GTMachines.SUPER_CHEST[tier])
                    .outputItems(CustomMachines.HUGE_ITEM_IMPORT_BUS[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("huge_item_export_bus_" + GTValues.VN[tier].toLowerCase()))
                    .inputItems(GTMachines.ITEM_EXPORT_BUS[tier].asStack())
                    .inputItems(tier > GTValues.EV ? GTMachines.QUANTUM_CHEST[tier] : GTMachines.SUPER_CHEST[tier])
                    .outputItems(CustomMachines.HUGE_ITEM_EXPORT_BUS[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("huge_fluid_import_bus_" + GTValues.VN[tier].toLowerCase()))
                    .inputItems(GTMachines.FLUID_IMPORT_HATCH[tier].asStack())
                    .inputItems(tier > GTValues.EV ? GTMachines.QUANTUM_TANK[tier] : GTMachines.SUPER_TANK[tier])
                    .outputItems(GTOMachines.HUGE_FLUID_IMPORT_HATCH[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("huge_fluid_export_bus_" + GTValues.VN[tier].toLowerCase()))
                    .inputItems(GTMachines.FLUID_EXPORT_HATCH[tier].asStack())
                    .inputItems(tier > GTValues.EV ? GTMachines.QUANTUM_TANK[tier] : GTMachines.SUPER_TANK[tier])
                    .outputItems(GTOMachines.HUGE_FLUID_EXPORT_HATCH[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);

            GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("huge_dual_hatch_" + GTValues.VN[tier].toLowerCase()))
                    .inputItems(CustomMachines.HUGE_ITEM_IMPORT_BUS[tier].asStack())
                    .inputItems(GTOMachines.HUGE_FLUID_IMPORT_HATCH[tier].asStack())
                    .inputItems(GTMachines.BUFFER[tier].asStack())
                    .outputItems(CustomMachines.HUGE_INPUT_DUAL_HATCH[tier].asStack())
                    .duration(200)
                    .EUt(GTValues.VA[tier])
                    .save(provider);
        }

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("me_export_buffer"))
                .inputItems(GTAEMachines.ITEM_EXPORT_BUS_ME.asStack())
                .inputItems(GTAEMachines.FLUID_EXPORT_HATCH_ME.asStack())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(CustomMachines.ME_EXPORT_BUFFER.asStack())
                .duration(400)
                .EUt(GTValues.VA[GTValues.HV])
                .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_computation_transmitter_hatch"))
                .inputItems(GTResearchMachines.COMPUTATION_HATCH_TRANSMITTER.asStack())
                .inputItems(CustomTags.ZPM_CIRCUITS)
                .inputItems(GTItems.SENSOR_ZPM)
                .inputFluids(GTMaterials.Polybenzimidazole.getFluid(288))
                .outputItems(WirelessMachines.WIRELESS_COMPUTATION_HATCH_TRANSMITTER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wireless_computation_receiver_hatch"))
                .inputItems(GTResearchMachines.COMPUTATION_HATCH_TRANSMITTER.asStack())
                .inputItems(CustomTags.ZPM_CIRCUITS)
                .inputItems(GTItems.EMITTER_ZPM)
                .inputFluids(GTMaterials.Polybenzimidazole.getFluid(288))
                .outputItems(WirelessMachines.WIRELESS_COMPUTATION_HATCH_RECEIVER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200)
                .EUt(GTValues.VA[GTValues.ZPM])
                .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("advanced_wireless_item_transfer_cover"))
                .inputItems(CustomItems.WIRELESS_ITEM_TRANSFER_COVER.asStack())
                .inputItems(CustomTags.MV_CIRCUITS)
                .outputItems(CustomItems.ADVANCED_WIRELESS_ITEM_TRANSFER_COVER)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(72))
                .duration(100)
                .EUt(GTValues.VA[GTValues.LV])
                .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("advanced_wireless_fluid_transfer_cover"))
                .inputItems(CustomItems.WIRELESS_FLUID_TRANSFER_COVER.asStack())
                .inputItems(CustomTags.MV_CIRCUITS)
                .outputItems(CustomItems.ADVANCED_WIRELESS_FLUID_TRANSFER_COVER)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(72))
                .duration(100)
                .EUt(GTValues.VA[GTValues.LV])
                .save(provider);

        VanillaRecipeHelper.addShapedRecipe(provider, true, "advanced_terminal", CustomItems.ADVANCED_TERMINAL.asStack(),
                "SGS", "PBP", "PWP",
                'S', new UnificationEntry(TagPrefix.screw, GTMaterials.Steel),
                'G', Tags.Items.GLASS_PANES,
                'B', CustomTags.LV_CIRCUITS,
                'P', new UnificationEntry(TagPrefix.plate, GTMaterials.Steel),
                'W', GTItems.TERMINAL);
    }
}

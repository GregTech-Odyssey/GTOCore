package com.gtocore.data.recipe;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.machines.*;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import com.enderio.machines.common.init.MachineBlocks;
import org.apache.commons.lang3.ArrayUtils;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.data.recipe.GTCraftingComponents.*;
import static com.gregtechceu.gtceu.data.recipe.misc.MetaTileEntityLoader.registerMachineRecipe;
import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.LASER_WELDER_RECIPES;
import static com.gtocore.data.CraftingComponents.INTEGRATED_CONTROL_CORE;

public final class MachineRecipe {

    public static void init() {
        HatchRecipe.init();
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("multiblock_crate"), MultiBlockG.MULTIBLOCK_CRATE.asItem(), "RPR",
                "PCP", "RPR", 'P', new MaterialEntry(plateDouble, GTMaterials.Steel), 'R', new MaterialEntry(TagPrefix.rodLong, GTMaterials.Steel), 'C', GTMachines.BRONZE_CRATE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("evaporation_plant"),
                MultiBlockA.EVAPORATION_PLANT.asItem(), "CBC", "FMF", "CBC", 'M', GTMachines.HULL[HV].asItem(),
                'B', new MaterialEntry(TagPrefix.wireGtDouble, GTMaterials.Kanthal), 'C', CustomTags.HV_CIRCUITS,
                'F', GTItems.ELECTRIC_PUMP_HV);
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("casing_uev"), GTBlocks.MACHINE_CASING_UEV.asItem(),
                "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(plate, GTOMaterials.Quantanium));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("casing_uiv"), GTBlocks.MACHINE_CASING_UIV.asItem(),
                "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(plate, GTOMaterials.Adamantium));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("casing_uxv"), GTBlocks.MACHINE_CASING_UXV.asItem(),
                "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(plate, GTOMaterials.Vibranium));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("casing_opv"), GTBlocks.MACHINE_CASING_OpV.asItem(),
                "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(plate, GTOMaterials.Draconium));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("casing_max"), GTBlocks.MACHINE_CASING_MAX.asItem(),
                "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(plate, GTOMaterials.ChaosInfinityAlloy));
        LASER_WELDER_RECIPES.recipeBuilder("casing_ulv").EUt(16).inputItems(plate, WroughtIron, 8)
                .outputItems(GTBlocks.MACHINE_CASING_ULV.asItem()).circuitMeta(8).duration(25).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_lv").EUt(16).inputItems(plate, Steel, 8)
                .outputItems(GTBlocks.MACHINE_CASING_LV.asItem()).circuitMeta(8).duration(50).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_mv").EUt(16).inputItems(plate, Aluminium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_MV.asItem()).circuitMeta(8).duration(50).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_hv").EUt(16).inputItems(plate, StainlessSteel, 8)
                .outputItems(GTBlocks.MACHINE_CASING_HV.asItem()).circuitMeta(8).duration(50).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_ev").EUt(16).inputItems(plate, Titanium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_EV.asItem()).circuitMeta(8).duration(50).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_iv").EUt(16).inputItems(plate, TungstenSteel, 8)
                .outputItems(GTBlocks.MACHINE_CASING_IV.asItem()).circuitMeta(8).duration(50).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_luv").EUt(16).inputItems(plate, RhodiumPlatedPalladium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_LuV.asItem()).circuitMeta(8).duration(50).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_zpm").EUt(16).inputItems(plate, NaquadahAlloy, 8)
                .outputItems(GTBlocks.MACHINE_CASING_ZPM.asItem()).circuitMeta(8).duration(50).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_uv").EUt(16).inputItems(plate, Darmstadtium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_UV.asItem()).circuitMeta(8).duration(50).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_uhv").EUt(16).inputItems(plate, Neutronium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_UHV.asItem()).circuitMeta(8).duration(50).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_uev").EUt(16).inputItems(plate, GTOMaterials.Quantanium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_UEV.asItem()).circuitMeta(8).duration(50).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_uiv").EUt(16).inputItems(plate, GTOMaterials.Adamantium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_UIV.asItem()).circuitMeta(8).duration(50).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_uxv").EUt(16).inputItems(plate, GTOMaterials.Vibranium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_UXV.asItem()).circuitMeta(8).duration(50).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_opv").EUt(16).inputItems(plate, GTOMaterials.Draconium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_OpV.asItem()).circuitMeta(8).duration(50).save();
        LASER_WELDER_RECIPES.recipeBuilder("casing_max").EUt(16).inputItems(plate, GTOMaterials.ChaosInfinityAlloy, 8)
                .outputItems(GTBlocks.MACHINE_CASING_MAX.asItem()).circuitMeta(8).duration(50).save();

        int multiplier = GTOCore.isExpert() ? 2 : 1;
        ASSEMBLER_RECIPES.recipeBuilder("hull_ulv").duration(25).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_ULV.asItem()).inputItems(cableGtSingle, RedAlloy, 2)
                .inputFluids(Polyethylene.getFluid(L * multiplier)).outputItems(GTMachines.HULL[0]).save();
        ASSEMBLER_RECIPES.recipeBuilder("hull_lv").duration(50).EUt(16).inputItems(GTBlocks.MACHINE_CASING_LV.asItem())
                .inputItems(cableGtSingle, Tin, 2).inputFluids(Polyethylene.getFluid(L * multiplier))
                .outputItems(GTMachines.HULL[1]).save();
        ASSEMBLER_RECIPES.recipeBuilder("hull_mv").duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_MV.asItem()).inputItems(cableGtSingle, Copper, 2)
                .inputFluids(Polyethylene.getFluid(L * multiplier)).outputItems(GTMachines.HULL[2]).save();
        ASSEMBLER_RECIPES.recipeBuilder("hull_hv").duration(50).EUt(16).inputItems(GTBlocks.MACHINE_CASING_HV.asItem())
                .inputItems(cableGtSingle, Gold, 2).inputFluids(PolyvinylChloride.getFluid(L * multiplier))
                .outputItems(GTMachines.HULL[3]).save();
        ASSEMBLER_RECIPES.recipeBuilder("hull_ev").duration(50).EUt(16).inputItems(GTBlocks.MACHINE_CASING_EV.asItem())
                .inputItems(cableGtSingle, Aluminium, 2).inputFluids(PolyvinylChloride.getFluid(L * multiplier))
                .outputItems(GTMachines.HULL[4]).save();
        ASSEMBLER_RECIPES.recipeBuilder("hull_iv").duration(50).EUt(16).inputItems(GTBlocks.MACHINE_CASING_IV.asItem())
                .inputItems(cableGtSingle, Platinum, 2).inputFluids(Polytetrafluoroethylene.getFluid(L * multiplier))
                .outputItems(GTMachines.HULL[5]).save();
        ASSEMBLER_RECIPES.recipeBuilder("hull_luv").duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_LuV.asItem()).inputItems(cableGtSingle, NiobiumTitanium, 2)
                .inputFluids(Polytetrafluoroethylene.getFluid(L * multiplier)).outputItems(GTMachines.HULL[6]).save();
        ASSEMBLER_RECIPES.recipeBuilder("hull_zpm").duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_ZPM.asItem()).inputItems(cableGtSingle, VanadiumGallium, 2)
                .inputFluids(Polybenzimidazole.getFluid(L * multiplier)).outputItems(GTMachines.HULL[7]).save();
        ASSEMBLER_RECIPES.recipeBuilder("hull_uv").duration(50).EUt(16).inputItems(GTBlocks.MACHINE_CASING_UV.asItem())
                .inputItems(cableGtSingle, YttriumBariumCuprate, 2).inputFluids(Polybenzimidazole.getFluid(L * multiplier))
                .outputItems(GTMachines.HULL[8]).save();
        ASSEMBLER_RECIPES.recipeBuilder("hull_uhv").duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_UHV.asItem())
                .inputItems(cableGtSingle, Europium, 2)
                .inputFluids(GTOMaterials.Polyetheretherketone.getFluid(L * multiplier))
                .outputItems(GTMachines.HULL[9]).save();

        ASSEMBLER_RECIPES.recipeBuilder("hull_uev").duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_UEV.asItem())
                .inputItems(cableGtSingle, GTOMaterials.Mithril, 2)
                .inputFluids(GTOMaterials.Polyetheretherketone.getFluid(L * multiplier))
                .outputItems(GTMachines.HULL[10]).save();
        ASSEMBLER_RECIPES.recipeBuilder("hull_uiv").duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_UIV.asItem())
                .inputItems(cableGtSingle, Neutronium, 2)
                .inputFluids(GTOMaterials.Zylon.getFluid(L * multiplier))
                .outputItems(GTMachines.HULL[11]).save();
        ASSEMBLER_RECIPES.recipeBuilder("hull_uxv").duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_UXV.asItem())
                .inputItems(cableGtSingle, GTOMaterials.Taranium, 2)
                .inputFluids(GTOMaterials.Zylon.getFluid(L * multiplier))
                .outputItems(GTMachines.HULL[12]).save();
        ASSEMBLER_RECIPES.recipeBuilder("hull_opv").duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_OpV.asItem())
                .inputItems(cableGtSingle, GTOMaterials.CrystalMatrix, 2)
                .inputFluids(GTOMaterials.FullerenePolymerMatrixPulp.getFluid(L * multiplier))
                .outputItems(GTMachines.HULL[13]).save();
        ASSEMBLER_RECIPES.recipeBuilder("hull_max").duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_MAX.asItem())
                .inputItems(cableGtSingle, GTOMaterials.CosmicNeutronium, 2)
                .inputFluids(GTOMaterials.Radox.getFluid(L * multiplier))
                .outputItems(GTMachines.HULL[14]).save();

        registerMachineRecipe(ArrayUtils.subarray(GTMachines.TRANSFORMER, UHV, MAX),
                "WCC",
                "TH ", "WCC", 'W', POWER_COMPONENT, 'C', CABLE, 'T', CABLE_TIER_UP, 'H', HULL);
        registerMachineRecipe(
                ArrayUtils.subarray(GTMachines.HI_AMP_TRANSFORMER_2A, UHV, MAX),
                "WCC", "TH ", "WCC",
                'W', POWER_COMPONENT, 'C', CABLE_DOUBLE, 'T', CABLE_TIER_UP_DOUBLE, 'H', HULL);
        registerMachineRecipe(
                ArrayUtils.subarray(GTMachines.HI_AMP_TRANSFORMER_4A, UHV, MAX),
                "WCC", "TH ", "WCC",
                'W', POWER_COMPONENT, 'C', CABLE_QUAD, 'T', CABLE_TIER_UP_QUAD, 'H', HULL);
        registerMachineRecipe(GTOMachines.DEHYDRATOR, "WCW", "AMA", "PRP", 'M', HULL, 'P', PLATE, 'C',
                CIRCUIT, 'W', WIRE_QUAD, 'R', ROBOT_ARM, 'A', CABLE_QUAD);
        registerMachineRecipe(GTOMachines.ARC_GENERATOR, "WEW", "AMA", "WSW", 'M', HULL, 'E',
                EMITTER, 'W', WIRE_HEX, 'S', SENSOR, 'A', CABLE_TIER_UP);
        registerMachineRecipe(GTOMachines.UNPACKER, "WCW", "VMR", "BCB", 'M', HULL, 'R', ROBOT_ARM, 'V',
                CONVEYOR, 'C', CIRCUIT, 'W', CABLE, 'B', Tags.Items.CHESTS_WOODEN);
        registerMachineRecipe(GTOMachines.CLUSTER, "MMM", "CHC", "MMM", 'H', HULL, 'M', MOTOR, 'C', CIRCUIT);
        registerMachineRecipe(GTOMachines.ROLLING, "EWE", "CMC", "PWP", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C',
                CIRCUIT, 'W', CABLE);
        registerMachineRecipe(GTOMachines.LAMINATOR, "WPW", "CMC", "GGG", 'M', HULL, 'P', PUMP, 'C', CIRCUIT, 'W',
                CABLE, 'G', CONVEYOR);
        registerMachineRecipe(GTOMachines.LOOM, "CWC", "EME", "EWE", 'M', HULL, 'E', MOTOR, 'C', CIRCUIT,
                'W', CABLE);
        registerMachineRecipe(GTOMachines.VACUUM_PUMP, "CLC", "LML", "PLP", 'M', HULL, 'P', PUMP, 'C', CIRCUIT, 'L', PIPE_LARGE);
        registerMachineRecipe(GTOMachines.LASER_WELDER, "WEW", "CMC", "PPP", 'M', HULL, 'P', PLATE, 'C', CIRCUIT, 'E', EMITTER, 'W', CABLE);
        registerMachineRecipe(GTOMachines.WORLD_DATA_SCANNER, "CDC", "BAB", "CDC", 'A', HULL, 'B', CABLE, 'C', SENSOR, 'D', CIRCUIT);
        registerMachineRecipe(GTOMachines.ACCELERATE_HATCH, "CFC", "FAF", "CFC", 'A', HULL, 'F', FIELD_GENERATOR, 'C', SENSOR);
        registerMachineRecipe(GTOMachines.OVERCLOCK_HATCH, "CFC", "FAF", "CFC", 'A', HULL, 'C', INTEGRATED_CONTROL_CORE, 'F', VOLTAGE_COIL);

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("lv_thermal_generator"), GTOMachines.THERMAL_GENERATOR[LV].asItem(), "ABA", "CDC", "EFE", 'B', GTItems.ELECTRIC_MOTOR_LV.asItem(), 'F', GTBlocks.FIREBOX_BRONZE.asItem(), 'D', GTMachines.HULL[LV].asItem(), 'C', CustomTags.LV_CIRCUITS, 'E', new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Cobalt), 'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Invar));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("mv_thermal_generator"), GTOMachines.THERMAL_GENERATOR[MV].asItem(), "ABA", "CDC", "EFE", 'B', GTItems.ELECTRIC_MOTOR_MV.asItem(), 'F', GTBlocks.FIREBOX_STEEL.asItem(), 'D', GTMachines.HULL[MV].asItem(), 'C', CustomTags.MV_CIRCUITS, 'E', new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.AnnealedCopper), 'A', new MaterialEntry(TagPrefix.plate, GTOMaterials.DarkSteel));

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("thermal_generator"), GTOMachines.THERMAL_GENERATOR[0].asItem(),
                "PVP", "CMC", "WBW", 'M', GTMachines.HULL[0].asItem(), 'P', new MaterialEntry(plate, Steel), 'V',
                GTOItems.ULV_ELECTRIC_MOTOR.asItem(), 'C', CustomTags.ULV_CIRCUITS, 'W', new MaterialEntry(wireGtSingle, Lead), 'B', GTMachines.STEAM_SOLID_BOILER.first().asItem());

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_wind_mill_turbine"), GTOMachines.WIND_MILL_TURBINE[0].asItem(),
                "RGR", "MHM", "WCW", 'H', GTOMachines.THERMAL_GENERATOR[0].asItem(), 'G', new MaterialEntry(gear, Bronze), 'R', new MaterialEntry(rod, WroughtIron),
                'C', CustomTags.ULV_CIRCUITS, 'W', new MaterialEntry(cableGtSingle, RedAlloy), 'M', new MaterialEntry(rod, IronMagnetic));

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("lv_wind_mill_turbine"), GTOMachines.WIND_MILL_TURBINE[1].asItem(),
                "RGR", "MHM", "WCW", 'H', GTMachines.HULL[1].asItem(), 'G', new MaterialEntry(gear, Steel), 'R', new MaterialEntry(rod, Invar),
                'C', CustomTags.LV_CIRCUITS, 'W', new MaterialEntry(cableGtSingle, Tin), 'M', GTItems.ELECTRIC_MOTOR_LV.asItem());

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("mv_wind_mill_turbine"), GTOMachines.WIND_MILL_TURBINE[2].asItem(),
                "RGR", "MHM", "WCW", 'H', GTMachines.HULL[2].asItem(), 'G', new MaterialEntry(gear, Aluminium), 'R', new MaterialEntry(rod, VanadiumSteel),
                'C', CustomTags.MV_CIRCUITS, 'W', new MaterialEntry(cableGtSingle, Copper), 'M', GTItems.ELECTRIC_MOTOR_MV.asItem());

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("hv_wind_mill_turbine"), GTOMachines.WIND_MILL_TURBINE[3].asItem(),
                "RGR", "MHM", "WCW", 'H', GTMachines.HULL[3].asItem(), 'G', new MaterialEntry(gear, StainlessSteel), 'R', new MaterialEntry(rod, BlackSteel),
                'C', CustomTags.HV_CIRCUITS, 'W', new MaterialEntry(cableGtSingle, Gold), 'M', GTItems.ELECTRIC_MOTOR_HV.asItem());

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_loom"), GTOMachines.ULV_LOOM[0].asItem(),
                "CWC", "EME", "EWE", 'M', GTMachines.HULL[0].asItem(), 'E', GTOItems.ULV_ELECTRIC_MOTOR.asItem(), 'C', CustomTags.ULV_CIRCUITS, 'W', new MaterialEntry(cableGtSingle, RedAlloy));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_packer"), GTOMachines.ULV_PACKER[0].asItem(),
                "BCA", "RMV", "WCW", 'M', GTMachines.HULL[0].asItem(), 'R', GTItems.RESISTOR.asItem(), 'V', Tags.Items.CHESTS_WOODEN,
                'C', CustomTags.ULV_CIRCUITS, 'W', new MaterialEntry(cableGtSingle, RedAlloy), 'B', GTOItems.ULV_ROBOT_ARM.asItem(), 'A', GTOItems.ULV_CONVEYOR_MODULE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_unpacker"), GTOMachines.ULV_UNPACKER[0].asItem(),
                "WCW", "VMR", "ACB", 'M', GTMachines.HULL[0].asItem(), 'R', GTItems.RESISTOR.asItem(), 'V', Tags.Items.CHESTS_WOODEN,
                'C', CustomTags.ULV_CIRCUITS, 'W', new MaterialEntry(cableGtSingle, RedAlloy), 'B', GTOItems.ULV_ROBOT_ARM.asItem(), 'A', GTOItems.ULV_CONVEYOR_MODULE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_chemical_reactor"), GTOMachines.ULV_CHEMICAL_REACTOR[0].asItem(), "GRG", "WEW", "CMC", 'M', GTMachines.HULL[0].asItem(), 'R', new MaterialEntry(rotor, Tin), 'E',
                GTOItems.ULV_ELECTRIC_MOTOR.asItem(), 'C', CustomTags.ULV_CIRCUITS, 'W', new MaterialEntry(cableGtSingle, RedAlloy), 'G', Blocks.GLASS.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_fluid_solidifier"), GTOMachines.ULV_FLUID_SOLIDIFIER[0].asItem(), "PGP", "WMW", "CBC", 'M', GTMachines.HULL[0].asItem(), 'P', GTOItems.ULV_ELECTRIC_PUMP.asItem(), 'C',
                CustomTags.ULV_CIRCUITS, 'W', new MaterialEntry(cableGtSingle, RedAlloy), 'G', Blocks.GLASS.asItem(), 'B', Tags.Items.CHESTS_WOODEN);
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_assembler"), GTOMachines.ULV_ASSEMBLER[0].asItem(), "ACA", "VMV", "WCW", 'M', GTMachines.HULL[0].asItem(), 'V', GTOItems.ULV_CONVEYOR_MODULE.asItem(), 'A',
                GTOItems.ULV_ROBOT_ARM.asItem(), 'C', CustomTags.ULV_CIRCUITS, 'W', new MaterialEntry(cableGtSingle, RedAlloy));

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_rolling"), GCYMMachines.LARGE_ROLLING.asItem(),
                "PKP", "BZB", "FKH", 'Z', CustomTags.IV_CIRCUITS, 'B', GTItems.ELECTRIC_MOTOR_IV.asItem(), 'P',
                GTItems.ELECTRIC_PISTON_IV.asItem(), 'H', GTOMachines.ROLLING[IV].asItem(),
                'F', GTOMachines.CLUSTER[IV].asItem(), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_bender"), GCYMMachines.LARGE_BENDER.asItem(),
                "PKP", "BZB", "HKH", 'Z', CustomTags.IV_CIRCUITS, 'H', GTItems.ELECTRIC_MOTOR_IV.asItem(), 'P',
                GTItems.ELECTRIC_PISTON_IV.asItem(), 'B', GTMachines.BENDER[IV].asItem(), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_forming"), GCYMMachines.LARGE_FORMING.asItem(),
                "PKP", "GZG", "HKH", 'Z', CustomTags.IV_CIRCUITS, 'P', GTItems.ELECTRIC_PISTON_IV.asItem(),
                'G', GTMachines.FORMING_PRESS[IV].asItem(), 'H', GTMachines.FORGE_HAMMER[IV].asItem(), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_crusher"), GCYMMachines.LARGE_CRUSHER.asItem(),
                "PKP", "GZG", "HKH", 'Z', CustomTags.IV_CIRCUITS, 'P', GTItems.ELECTRIC_PISTON_IV.asItem(),
                'G', GTMachines.MACERATOR[IV].asItem(), 'H', GTMachines.FORGE_HAMMER[IV].asItem(), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_laminator"), GCYMMachines.LARGE_LAMINATOR.asItem(), "RKR", "CXC",
                "MKM", 'C', CustomTags.IV_CIRCUITS, 'R', GTItems.ELECTRIC_PUMP_IV.asItem(), 'M', GTItems.CONVEYOR_MODULE_IV.asItem(), 'X',
                GTOMachines.LAMINATOR[IV].asItem(), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_laser_welder"), GCYMMachines.LARGE_LASER_WELDER.asItem(), "ICI",
                "EXE", "PKP", 'C', CustomTags.IV_CIRCUITS, 'P', new MaterialEntry(plateDouble, TantalumCarbide), 'I',
                GTItems.EMITTER_IV.asItem(), 'E', GTItems.CONVEYOR_MODULE_IV.asItem(), 'X', GTOMachines.LASER_WELDER[IV].asItem(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("generator_array"),
                GeneratorMultiblock.GENERATOR_ARRAY.asItem(),
                "ABA", "BCB", "ABA", 'A', new MaterialEntry(plate, Steel),
                'B', CustomTags.LV_CIRCUITS, 'C', GTItems.EMITTER_LV.asItem());

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("hermetic_casing_uev"),
                GTOBlocks.HERMETIC_CASING_UEV.asItem(), "PPP", "PFP", "PPP", 'P',
                new MaterialEntry(plate, GTOMaterials.Quantanium), 'F',
                new MaterialEntry(pipeLargeFluid, Neutronium));

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("hermetic_casing_uiv"),
                GTOBlocks.HERMETIC_CASING_UIV.asItem(), "PPP", "PFP", "PPP", 'P',
                new MaterialEntry(plate, GTOMaterials.Adamantium), 'F',
                new MaterialEntry(pipeLargeFluid, Neutronium));

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("hermetic_casing_uxv"),
                GTOBlocks.HERMETIC_CASING_UXV.asItem(), "PPP", "PFP", "PPP", 'P',
                new MaterialEntry(plate, GTOMaterials.Vibranium), 'F',
                new MaterialEntry(pipeLargeFluid, GTOMaterials.Enderium));

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("hermetic_casing_opv"),
                GTOBlocks.HERMETIC_CASING_OpV.asItem(), "PPP", "PFP", "PPP", 'P',
                new MaterialEntry(plate, GTOMaterials.Draconium), 'F',
                new MaterialEntry(pipeLargeFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter));

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("quantum_tank_uev"),
                GTMachines.QUANTUM_TANK[UEV].asItem(),
                "CGC", "PHP", "CUC", 'C', CustomTags.UEV_CIRCUITS, 'P',
                new MaterialEntry(plate, GTOMaterials.Quantanium), 'U',
                GTItems.ELECTRIC_PUMP_UHV.asItem(),
                'G', GTItems.FIELD_GENERATOR_UV.asItem(), 'H',
                GTOBlocks.HERMETIC_CASING_UEV.asItem());

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("quantum_tank_uiv"),
                GTMachines.QUANTUM_TANK[UIV].asItem(),
                "CGC", "PHP", "CUC", 'C', CustomTags.UIV_CIRCUITS, 'P',
                new MaterialEntry(plate, GTOMaterials.Adamantium), 'U',
                GTItems.ELECTRIC_PUMP_UEV.asItem(),
                'G', GTItems.FIELD_GENERATOR_UHV.asItem(), 'H',
                GTOBlocks.HERMETIC_CASING_UIV.asItem());

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("quantum_tank_uxv"),
                GTMachines.QUANTUM_TANK[UXV].asItem(),
                "CGC", "PHP", "CUC", 'C', CustomTags.UXV_CIRCUITS, 'P',
                new MaterialEntry(plate, GTOMaterials.Vibranium), 'U',
                GTItems.ELECTRIC_PUMP_UIV.asItem(),
                'G', GTItems.FIELD_GENERATOR_UEV.asItem(), 'H',
                GTOBlocks.HERMETIC_CASING_UXV.asItem());

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("quantum_tank_opv"),
                GTMachines.QUANTUM_TANK[OpV].asItem(),
                "CGC", "PHP", "CUC", 'C', CustomTags.OpV_CIRCUITS, 'P',
                new MaterialEntry(plate, GTOMaterials.Draconium), 'U',
                GTItems.ELECTRIC_PUMP_UXV.asItem(),
                'G', GTItems.FIELD_GENERATOR_UIV.asItem(), 'H',
                GTOBlocks.HERMETIC_CASING_OpV.asItem());

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("quantum_chest_uev"),
                GTMachines.QUANTUM_CHEST[UEV].asItem(), "CPC", "PHP", "CFC", 'C',
                CustomTags.UEV_CIRCUITS, 'P',
                new MaterialEntry(plate, GTOMaterials.Quantanium), 'F',
                GTItems.FIELD_GENERATOR_UV.asItem(), 'H', GTMachines.HULL[10].asItem());

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("quantum_chest_uiv"),
                GTMachines.QUANTUM_CHEST[UIV].asItem(), "CPC", "PHP", "CFC", 'C',
                CustomTags.UIV_CIRCUITS, 'P',
                new MaterialEntry(plate, GTOMaterials.Adamantium), 'F',
                GTItems.FIELD_GENERATOR_UHV.asItem(), 'H', GTMachines.HULL[11].asItem());

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("quantum_chest_uxv"),
                GTMachines.QUANTUM_CHEST[UXV].asItem(), "CPC", "PHP", "CFC", 'C',
                CustomTags.UXV_CIRCUITS, 'P',
                new MaterialEntry(plate, GTOMaterials.Vibranium), 'F',
                GTItems.FIELD_GENERATOR_UEV.asItem(), 'H', GTMachines.HULL[12].asItem());

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("quantum_chest_opv"),
                GTMachines.QUANTUM_CHEST[OpV].asItem(), "CPC", "PHP", "CFC", 'C',
                CustomTags.OpV_CIRCUITS, 'P',
                new MaterialEntry(plate, GTOMaterials.Draconium), 'F',
                GTItems.FIELD_GENERATOR_UIV.asItem(), 'H', GTMachines.HULL[13].asItem());

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("large_block_conversion_room"),
                MultiBlockD.LARGE_BLOCK_CONVERSION_ROOM.asItem(), "SES", "EHE", "SES",
                'S', GTItems.SENSOR_UHV.asItem(), 'E', GTItems.EMITTER_UHV.asItem(), 'H',
                MultiBlockD.BLOCK_CONVERSION_ROOM.asItem());

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("hp_steam_vacuum_pump"), GTOMachines.STEAM_VACUUM_PUMP.right().asItem(), "ABA", "CDC", "ECE", 'B', new MaterialEntry(TagPrefix.pipeHugeFluid, GTMaterials.TinAlloy), 'D', GTOMachines.STEAM_VACUUM_PUMP.first().asItem(), 'C', new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.TinAlloy), 'E', new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron), 'A', GTMachines.STEEL_DRUM.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_vacuum_pump"), GTOMachines.STEAM_VACUUM_PUMP.first().asItem(), "DSD",
                "SMS", "GSG", 'M', GTBlocks.BRONZE_BRICKS_HULL.asItem(), 'S', new MaterialEntry(pipeNormalFluid, Bronze), 'D', GTMachines.BRONZE_DRUM.asItem(), 'G', new MaterialEntry(gearSmall, Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("steam_alloy_smelter_bronze"), GTMachines.STEAM_ALLOY_SMELTER.left().asItem(),
                "XXX", "FMF", "XXX", 'M', GTBlocks.BRONZE_BRICKS_HULL.asItem(), 'X', new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.Bronze), 'F', MachineBlocks.PRIMITIVE_ALLOY_SMELTER.asItem());

        ASSEMBLER_RECIPES.recipeBuilder("infinity_fluid_drilling_rig")
                .inputItems(GTMachines.HULL[UV])
                .inputItems(frameGt, Ruridit, 4)
                .inputItems(CustomTags.UV_CIRCUITS, 4)
                .inputItems(GTItems.ELECTRIC_MOTOR_UV, 4)
                .inputItems(GTItems.ELECTRIC_PUMP_UV, 4)
                .inputItems(gear, Neutronium, 4)
                .circuitMeta(2)
                .outputItems(MultiBlockD.INFINITY_FLUID_DRILLING_RIG)
                .duration(400).EUt(VA[UV]).save();

        ASSEMBLER_RECIPES.recipeBuilder("wood_distillation")
                .inputItems(MultiBlockA.LARGE_PYROLYSE_OVEN.asItem(), 16)
                .inputItems(GCYMMachines.LARGE_DISTILLERY.asItem(), 16)
                .inputItems(CustomTags.ZPM_CIRCUITS, 64)
                .inputItems(GTItems.EMITTER_LuV.asItem(), 32)
                .inputItems(pipeHugeFluid, StainlessSteel, 64)
                .inputItems(GTItems.ELECTRIC_PUMP_IV.asItem(), 32)
                .inputItems(plate, WatertightSteel, 64)
                .inputItems(plateDouble, StainlessSteel, 64)
                .inputFluids(SolderingAlloy.getFluid(5184))
                .outputItems(MultiBlockB.WOOD_DISTILLATION)
                .duration(800).EUt(VA[ZPM])
                .save();

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("large_steam_solar_boiler"), MultiBlockH.LARGE_STEAM_SOLAR_BOILER.asItem(),
                "AAA", "BCB", "DDD",
                'A', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.Silver), 'B', GTOItems.PRECISION_STEAM_MECHANISM.asItem(), 'C', GTBlocks.STEEL_HULL.asItem(), 'D', new MaterialEntry(TagPrefix.pipeQuadrupleFluid, GTMaterials.Steel));
    }
}

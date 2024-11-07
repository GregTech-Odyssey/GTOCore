package com.gto.gtocore.data.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.*;
import com.gto.gtocore.common.data.machines.*;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.common.Tags;

import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;
import static com.gregtechceu.gtceu.data.recipe.CraftingComponent.*;
import static com.gregtechceu.gtceu.data.recipe.misc.MetaTileEntityLoader.registerMachineRecipe;

public class MachineRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        HatchRecipe.init(provider);
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("casing_uev"), GTBlocks.MACHINE_CASING_UEV.asStack(),
                "PPP",
                "PwP", "PPP", 'P', new UnificationEntry(TagPrefix.plate, GTOMaterials.Quantanium));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("casing_uiv"), GTBlocks.MACHINE_CASING_UIV.asStack(),
                "PPP",
                "PwP", "PPP", 'P', new UnificationEntry(TagPrefix.plate, GTOMaterials.Adamantium));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("casing_uxv"), GTBlocks.MACHINE_CASING_UXV.asStack(),
                "PPP",
                "PwP", "PPP", 'P', new UnificationEntry(TagPrefix.plate, GTOMaterials.Vibranium));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("casing_opv"), GTBlocks.MACHINE_CASING_OpV.asStack(),
                "PPP",
                "PwP", "PPP", 'P', new UnificationEntry(TagPrefix.plate, GTOMaterials.Draconium));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("casing_max"), GTBlocks.MACHINE_CASING_MAX.asStack(),
                "PPP",
                "PwP", "PPP", 'P', new UnificationEntry(TagPrefix.plate, GTOMaterials.Chaos));
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder("casing_ulv").EUt(16).inputItems(plate, WroughtIron, 8)
                .outputItems(GTBlocks.MACHINE_CASING_ULV.asStack()).circuitMeta(8).duration(25).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder("casing_lv").EUt(16).inputItems(plate, Steel, 8)
                .outputItems(GTBlocks.MACHINE_CASING_LV.asStack()).circuitMeta(8).duration(50).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder("casing_mv").EUt(16).inputItems(plate, Aluminium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_MV.asStack()).circuitMeta(8).duration(50).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder("casing_hv").EUt(16).inputItems(plate, StainlessSteel, 8)
                .outputItems(GTBlocks.MACHINE_CASING_HV.asStack()).circuitMeta(8).duration(50).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder("casing_ev").EUt(16).inputItems(plate, Titanium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_EV.asStack()).circuitMeta(8).duration(50).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder("casing_iv").EUt(16).inputItems(plate, TungstenSteel, 8)
                .outputItems(GTBlocks.MACHINE_CASING_IV.asStack()).circuitMeta(8).duration(50).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder("casing_luv").EUt(16).inputItems(plate, RhodiumPlatedPalladium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_LuV.asStack()).circuitMeta(8).duration(50).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder("casing_zpm").EUt(16).inputItems(plate, NaquadahAlloy, 8)
                .outputItems(GTBlocks.MACHINE_CASING_ZPM.asStack()).circuitMeta(8).duration(50).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder("casing_uv").EUt(16).inputItems(plate, Darmstadtium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_UV.asStack()).circuitMeta(8).duration(50).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder("casing_uhv").EUt(16).inputItems(plate, Neutronium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_UHV.asStack()).circuitMeta(8).duration(50).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("casing_uev")).EUt(16).inputItems(plate, GTOMaterials.Quantanium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_UEV.asStack()).circuitMeta(8).duration(50).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("casing_uiv")).EUt(16).inputItems(plate, GTOMaterials.Adamantium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_UIV.asStack()).circuitMeta(8).duration(50).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("casing_uxv")).EUt(16).inputItems(plate, GTOMaterials.Vibranium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_UXV.asStack()).circuitMeta(8).duration(50).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("casing_opv")).EUt(16).inputItems(plate, GTOMaterials.Draconium, 8)
                .outputItems(GTBlocks.MACHINE_CASING_OpV.asStack()).circuitMeta(8).duration(50).save(provider);
        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id("casing_max")).EUt(16).inputItems(plate, GTOMaterials.Chaos, 8)
                .outputItems(GTBlocks.MACHINE_CASING_MAX.asStack()).circuitMeta(8).duration(50).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("hull_uhv")).duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_UHV.asStack())
                .inputItems(cableGtSingle, Europium, 2)
                .inputFluids(GTOMaterials.Polyetheretherketone.getFluid(L * 2))
                .outputItems(GTMachines.HULL[9]).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("hull_uev")).duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_UEV.asStack())
                .inputItems(cableGtSingle, GTOMaterials.Mithril, 2)
                .inputFluids(GTOMaterials.Polyetheretherketone.getFluid(L * 2))
                .outputItems(GTMachines.HULL[10]).save(provider);
        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("hull_uiv")).duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_UIV.asStack())
                .inputItems(cableGtSingle, GTMaterials.Neutronium, 2)
                .inputFluids(GTOMaterials.Zylon.getFluid(L * 2))
                .outputItems(GTMachines.HULL[11]).save(provider);
        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("hull_uxv")).duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_UXV.asStack())
                .inputItems(cableGtSingle, GTOMaterials.Taranium, 2)
                .inputFluids(GTOMaterials.Zylon.getFluid(L * 2))
                .outputItems(GTMachines.HULL[12]).save(provider);
        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("hull_opv")).duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_OpV.asStack())
                .inputItems(cableGtSingle, GTOMaterials.CrystalMatrix, 2)
                .inputFluids(GTOMaterials.FullerenePolymerMatrixPulp.getFluid(L * 2))
                .outputItems(GTMachines.HULL[13]).save(provider);
        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("hull_max")).duration(50).EUt(16)
                .inputItems(GTBlocks.MACHINE_CASING_MAX.asStack())
                .inputItems(cableGtSingle, GTOMaterials.CosmicNeutronium, 2)
                .inputFluids(GTOMaterials.Radox.getFluid(L * 2))
                .outputItems(GTMachines.HULL[14]).save(provider);

        registerMachineRecipe(provider, ArrayUtils.subarray(GTMachines.TRANSFORMER, GTValues.UHV, GTValues.MAX),
                "WCC",
                "TH ", "WCC", 'W', POWER_COMPONENT, 'C', CABLE, 'T', CABLE_TIER_UP, 'H', HULL);
        registerMachineRecipe(provider,
                ArrayUtils.subarray(GTMachines.HI_AMP_TRANSFORMER_2A, GTValues.UHV, GTValues.MAX),
                "WCC", "TH ", "WCC",
                'W', POWER_COMPONENT, 'C', CABLE_DOUBLE, 'T', CABLE_TIER_UP_DOUBLE, 'H', HULL);
        registerMachineRecipe(provider,
                ArrayUtils.subarray(GTMachines.HI_AMP_TRANSFORMER_4A, GTValues.UHV, GTValues.MAX),
                "WCC", "TH ", "WCC",
                'W', POWER_COMPONENT, 'C', CABLE_QUAD, 'T', CABLE_TIER_UP_QUAD, 'H', HULL);
        registerMachineRecipe(provider, GTOMachines.DEHYDRATOR, "WCW", "AMA", "PRP", 'M', HULL, 'P', PLATE, 'C',
                CIRCUIT, 'W', WIRE_QUAD, 'R', ROBOT_ARM, 'A', CABLE_QUAD);
        registerMachineRecipe(provider, GTOMachines.LIGHTNING_PROCESSOR, "WEW", "AMA", "WSW", 'M', HULL, 'E',
                EMITTER, 'W', WIRE_HEX, 'S', SENSOR, 'A', CABLE_TIER_UP);
        registerMachineRecipe(provider, GTOMachines.UNPACKER, "WCW", "VMR", "BCB", 'M', HULL, 'R', ROBOT_ARM, 'V',
                CONVEYOR, 'C', CIRCUIT, 'W', CABLE, 'B', Tags.Items.CHESTS_WOODEN);
        registerMachineRecipe(provider, GTOMachines.CLUSTER, "MMM", "CHC", "MMM", 'H', HULL, 'M', MOTOR, 'C', CIRCUIT);
        registerMachineRecipe(provider, GTOMachines.ROLLING, "EWE", "CMC", "PWP", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C',
                CIRCUIT, 'W', CABLE);
        registerMachineRecipe(provider, GTOMachines.LAMINATOR, "WPW", "CMC", "GGG", 'M', HULL, 'P', PUMP, 'C', CIRCUIT, 'W',
                CABLE, 'G', CONVEYOR);
        registerMachineRecipe(provider, GTOMachines.LOOM, "CWC", "EME", "EWE", 'M', HULL, 'E', MOTOR, 'C', CIRCUIT,
                'W', CABLE);
        registerMachineRecipe(provider, GTOMachines.VACUUM_PUMP, "CLC", "LML", "PLP", 'M', HULL, 'P', PUMP, 'C', CIRCUIT, 'L', PIPE_LARGE);
        registerMachineRecipe(provider, GTOMachines.LASER_WELDER, "WEW", "CMC", "PPP", 'M', HULL, 'P', PLATE, 'C', CIRCUIT, 'E', EMITTER, 'W', CABLE);

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("rotor_holder_uhv"), GTOMachines.ROTOR_HOLDER[UHV].asStack(),
                "SGS", "GHG", "SGS", 'H', GTMachines.HULL[GTValues.UHV].asStack(), 'G',
                new UnificationEntry(TagPrefix.gear, GTOMaterials.Orichalcum), 'S',
                new UnificationEntry(TagPrefix.gearSmall, GTMaterials.Neutronium));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("rotor_holder_uev"), GTOMachines.ROTOR_HOLDER[UEV].asStack(),
                "SGS", "GHG", "SGS", 'H', GTMachines.HULL[GTValues.UEV].asStack(), 'G',
                new UnificationEntry(TagPrefix.gear, GTOMaterials.AstralTitanium), 'S',
                new UnificationEntry(TagPrefix.gearSmall, GTOMaterials.Quantanium));

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("thermal_generator"), GTOMachines.THERMAL_GENERATOR[0].asStack(),
                "PVP", "CMC", "WBW", 'M', GTBlocks.MACHINE_CASING_ULV.asStack(), 'P', new UnificationEntry(plate, Steel), 'V',
                new UnificationEntry(rotor, Tin), 'C', CustomTags.ULV_CIRCUITS, 'W', new UnificationEntry(wireGtSingle, RedAlloy), 'B', GTMachines.STEAM_SOLID_BOILER.first().asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("ulv_wind_mill_turbine"), GTOMachines.WIND_MILL_TURBINE[0].asStack(),
                "RGR", "MHM", "WCW", 'H', GTOMachines.THERMAL_GENERATOR[0].asStack(), 'G', new UnificationEntry(gear, Bronze), 'R', new UnificationEntry(rod, WroughtIron),
                'C', CustomTags.ULV_CIRCUITS, 'W', new UnificationEntry(cableGtSingle, Lead), 'M', new UnificationEntry(rod, IronMagnetic));

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("lv_wind_mill_turbine"), GTOMachines.WIND_MILL_TURBINE[1].asStack(),
                "RGR", "MHM", "WCW", 'H', GTMachines.HULL[1].asStack(), 'G', new UnificationEntry(gear, Steel), 'R', new UnificationEntry(rod, Invar),
                'C', CustomTags.LV_CIRCUITS, 'W', new UnificationEntry(cableGtSingle, Tin), 'M', GTItems.ELECTRIC_MOTOR_LV.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("mv_wind_mill_turbine"), GTOMachines.WIND_MILL_TURBINE[2].asStack(),
                "RGR", "MHM", "WCW", 'H', GTMachines.HULL[2].asStack(), 'G', new UnificationEntry(gear, Aluminium), 'R', new UnificationEntry(rod, VanadiumSteel),
                'C', CustomTags.MV_CIRCUITS, 'W', new UnificationEntry(cableGtSingle, Copper), 'M', GTItems.ELECTRIC_MOTOR_MV.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("hv_wind_mill_turbine"), GTOMachines.WIND_MILL_TURBINE[3].asStack(),
                "RGR", "MHM", "WCW", 'H', GTMachines.HULL[3].asStack(), 'G', new UnificationEntry(gear, StainlessSteel), 'R', new UnificationEntry(rod, BlackSteel),
                'C', CustomTags.HV_CIRCUITS, 'W', new UnificationEntry(cableGtSingle, Gold), 'M', GTItems.ELECTRIC_MOTOR_HV.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("ulv_packer"), GTOMachines.ULV_PACKER[0].asStack(),
                "BCB", "RMV", "WCW", 'M', GTBlocks.MACHINE_CASING_ULV.asStack(), 'R', GTItems.RESISTOR.asStack(), 'V',
                new UnificationEntry(rod, IronMagnetic), 'C', CustomTags.ULV_CIRCUITS, 'W', new UnificationEntry(wireGtSingle, GTMaterials.Lead), 'B', GTOItems.ROBOT_ARM_ULV.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("ulv_unpacker"), GTOMachines.ULV_UNPACKER[0].asStack(),
                "WCW", "VMR", "BCB", 'M', GTBlocks.MACHINE_CASING_ULV.asStack(), 'R', GTItems.RESISTOR.asStack(), 'V',
                new UnificationEntry(rod, IronMagnetic), 'C', CustomTags.ULV_CIRCUITS, 'W', new UnificationEntry(wireGtSingle, GTMaterials.Lead), 'B', GTOItems.ROBOT_ARM_ULV.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_bender_and_forming"), MultiBlockMachineB.LARGE_BENDER_AND_FORMING.asStack(),
                "PKP", "BZB", "FKH", 'Z', CustomTags.IV_CIRCUITS, 'B', GTOMachines.ROLLING[IV].asStack(), 'P',
                GTItems.ELECTRIC_PISTON_IV.asStack(), 'H', GTMachines.BENDER[IV].asStack(),
                'F', GTOMachines.CLUSTER[IV].asStack(), 'K', new UnificationEntry(cableGtSingle, Platinum));

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("generator_array"),
                GeneratorMachine.GENERATOR_ARRAY.asStack(),
                "ABA", "BCB", "ABA", 'A', new UnificationEntry(plate, Steel),
                'B', CustomTags.LV_CIRCUITS, 'C', GTItems.EMITTER_LV.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("hermetic_casing_uev"),
                GTOBlocks.HERMETIC_CASING_UEV.asStack(), "PPP", "PFP", "PPP", 'P',
                new UnificationEntry(TagPrefix.plate, GTOMaterials.Quantanium), 'F',
                new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Neutronium));

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("hermetic_casing_uiv"),
                GTOBlocks.HERMETIC_CASING_UIV.asStack(), "PPP", "PFP", "PPP", 'P',
                new UnificationEntry(TagPrefix.plate, GTOMaterials.Adamantium), 'F',
                new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Neutronium));

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("hermetic_casing_uxv"),
                GTOBlocks.HERMETIC_CASING_UXV.asStack(), "PPP", "PFP", "PPP", 'P',
                new UnificationEntry(TagPrefix.plate, GTOMaterials.Vibranium), 'F',
                new UnificationEntry(TagPrefix.pipeLargeFluid, GTOMaterials.Enderium));

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("hermetic_casing_opv"),
                GTOBlocks.HERMETIC_CASING_OpV.asStack(), "PPP", "PFP", "PPP", 'P',
                new UnificationEntry(TagPrefix.plate, GTOMaterials.Draconium), 'F',
                new UnificationEntry(TagPrefix.pipeLargeFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter));

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("quantum_tank_uev"),
                GTMachines.QUANTUM_TANK[UEV].asStack(),
                "CGC", "PHP", "CUC", 'C', CustomTags.UEV_CIRCUITS, 'P',
                new UnificationEntry(TagPrefix.plate, GTOMaterials.Quantanium), 'U',
                GTItems.ELECTRIC_PUMP_UHV.asStack(),
                'G', GTItems.FIELD_GENERATOR_UV.asStack(), 'H',
                GTOBlocks.HERMETIC_CASING_UEV.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("quantum_tank_uiv"),
                GTMachines.QUANTUM_TANK[UIV].asStack(),
                "CGC", "PHP", "CUC", 'C', CustomTags.UIV_CIRCUITS, 'P',
                new UnificationEntry(TagPrefix.plate, GTOMaterials.Adamantium), 'U',
                GTItems.ELECTRIC_PUMP_UEV.asStack(),
                'G', GTItems.FIELD_GENERATOR_UHV.asStack(), 'H',
                GTOBlocks.HERMETIC_CASING_UIV.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("quantum_tank_uxv"),
                GTMachines.QUANTUM_TANK[UXV].asStack(),
                "CGC", "PHP", "CUC", 'C', CustomTags.UXV_CIRCUITS, 'P',
                new UnificationEntry(TagPrefix.plate, GTOMaterials.Vibranium), 'U',
                GTItems.ELECTRIC_PUMP_UIV.asStack(),
                'G', GTItems.FIELD_GENERATOR_UEV.asStack(), 'H',
                GTOBlocks.HERMETIC_CASING_UXV.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("quantum_tank_opv"),
                GTMachines.QUANTUM_TANK[OpV].asStack(),
                "CGC", "PHP", "CUC", 'C', CustomTags.OpV_CIRCUITS, 'P',
                new UnificationEntry(TagPrefix.plate, GTOMaterials.Draconium), 'U',
                GTItems.ELECTRIC_PUMP_UXV.asStack(),
                'G', GTItems.FIELD_GENERATOR_UIV.asStack(), 'H',
                GTOBlocks.HERMETIC_CASING_OpV.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("quantum_chest_uev"),
                GTMachines.QUANTUM_CHEST[UEV].asStack(), "CPC", "PHP", "CFC", 'C',
                CustomTags.UEV_CIRCUITS, 'P',
                new UnificationEntry(TagPrefix.plate, GTOMaterials.Quantanium), 'F',
                GTItems.FIELD_GENERATOR_UV.asStack(), 'H', GTMachines.HULL[10].asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("quantum_chest_uiv"),
                GTMachines.QUANTUM_CHEST[UIV].asStack(), "CPC", "PHP", "CFC", 'C',
                CustomTags.UIV_CIRCUITS, 'P',
                new UnificationEntry(TagPrefix.plate, GTOMaterials.Adamantium), 'F',
                GTItems.FIELD_GENERATOR_UHV.asStack(), 'H', GTMachines.HULL[11].asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("quantum_chest_uxv"),
                GTMachines.QUANTUM_CHEST[UXV].asStack(), "CPC", "PHP", "CFC", 'C',
                CustomTags.UXV_CIRCUITS, 'P',
                new UnificationEntry(TagPrefix.plate, GTOMaterials.Vibranium), 'F',
                GTItems.FIELD_GENERATOR_UEV.asStack(), 'H', GTMachines.HULL[12].asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("quantum_chest_opv"),
                GTMachines.QUANTUM_CHEST[OpV].asStack(), "CPC", "PHP", "CFC", 'C',
                CustomTags.OpV_CIRCUITS, 'P',
                new UnificationEntry(TagPrefix.plate, GTOMaterials.Draconium), 'F',
                GTItems.FIELD_GENERATOR_UIV.asStack(), 'H', GTMachines.HULL[13].asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("large_block_conversion_room"),
                AdvancedMultiBlockMachineA.LARGE_BLOCK_CONVERSION_ROOM.asStack(), "SES", "EHE", "SES",
                'S', GTItems.SENSOR_ZPM.asStack(), 'E', GTItems.EMITTER_ZPM.asStack(), 'H',
                AdvancedMultiBlockMachineA.BLOCK_CONVERSION_ROOM.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("steam_vacuum_pump"), GTOMachines.STEAM_VACUUM_PUMP.first().asStack(), "DSD",
                "SMS", "GSG", 'M', GTBlocks.BRONZE_BRICKS_HULL.asStack(), 'S', new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Bronze), 'D', GTMachines.BRONZE_DRUM.asStack(), 'G', new UnificationEntry(TagPrefix.gearSmall, GTMaterials.Bronze));

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("infinity_fluid_drilling_rig"))
                .inputItems(GTMachines.HULL[UV])
                .inputItems(frameGt, Ruridit, 4)
                .inputItems(CustomTags.UV_CIRCUITS, 4)
                .inputItems(GTItems.ELECTRIC_MOTOR_UV, 4)
                .inputItems(GTItems.ELECTRIC_PUMP_UV, 4)
                .inputItems(gear, Neutronium, 4)
                .circuitMeta(2)
                .outputItems(AdvancedMultiBlockMachineA.INFINITY_FLUID_DRILLING_RIG)
                .duration(400).EUt(VA[UV]).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("wood_distillation"))
                .inputItems(MultiBlockMachineA.COKING_TOWER.asStack(), 2)
                .inputItems(GCyMMachines.LARGE_DISTILLERY.asStack(), 4)
                .inputItems(CustomTags.LuV_CIRCUITS, 16)
                .inputItems(GTItems.EMITTER_LuV.asStack(), 4)
                .inputItems(pipeHugeFluid, StainlessSteel, 8)
                .inputItems(GTItems.ELECTRIC_PUMP_IV.asStack(), 8)
                .inputItems(plate, WatertightSteel, 16)
                .inputItems(plateDouble, StainlessSteel, 32)
                .inputFluids(SolderingAlloy.getFluid(1296))
                .outputItems(MultiBlockMachineB.WOOD_DISTILLATION)
                .duration(400).EUt(VA[LuV])
                .save(provider);
    }
}

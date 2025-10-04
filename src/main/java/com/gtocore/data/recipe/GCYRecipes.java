package com.gtocore.data.recipe;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GCYMBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.CASING_TEMPERED_GLASS;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.data.recipe.CustomTags.*;
import static com.gtocore.common.data.machines.GCYMMachines.*;

public final class GCYRecipes {

    public static void init() {
        registerPartsRecipes();
        registerMultiblockControllerRecipes();
    }

    private static void registerMultiblockControllerRecipes() {
        VanillaRecipeHelper.addShapedRecipe(true, "large_macerator", LARGE_MACERATION_TOWER.asItem(), "PCP",
                "BXB", "MKM", 'C', IV_CIRCUITS, 'P', new MaterialEntry(plate, TungstenCarbide), 'B',
                ELECTRIC_PISTON_IV.asItem(), 'M', ELECTRIC_MOTOR_IV.asItem(), 'X', MACERATOR[IV].asItem(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_arc_smelter", LARGE_ARC_SMELTER.asItem(), "KDK",
                "CXC", "PPP", 'C', IV_CIRCUITS, 'P', new MaterialEntry(plate, TantalumCarbide), 'X',
                ARC_FURNACE[IV].asItem(), 'D', new MaterialEntry(dust, Graphite), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_ore_washer", LARGE_CHEMICAL_BATH.asItem(), "PGP",
                "CXC", "MKM", 'C', IV_CIRCUITS, 'G', CASING_TEMPERED_GLASS.asItem(), 'P',
                ELECTRIC_PUMP_IV.asItem(), 'M', CONVEYOR_MODULE_IV.asItem(), 'X', ORE_WASHER[IV].asItem(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_sifter", LARGE_SIFTING_FUNNEL.asItem(), "PCP",
                "EXE", "PKP", 'C', IV_CIRCUITS, 'P', new MaterialEntry(plate, HSLASteel), 'E',
                ELECTRIC_PISTON_IV.asItem(), 'X', SIFTER[IV].asItem(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_engraver", LARGE_ENGRAVING_LASER.asItem(), "ICI",
                "EXE", "PKP", 'C', IV_CIRCUITS, 'P', new MaterialEntry(plateDouble, TantalumCarbide), 'I',
                EMITTER_IV.asItem(), 'E', ELECTRIC_PISTON_IV.asItem(), 'X', LASER_ENGRAVER[IV].asItem(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_packer", LARGE_PACKER.asItem(), "RCR", "PXP", "KPK",
                'C', EV_CIRCUITS, 'P', new MaterialEntry(plate, HSLASteel), 'R', ROBOT_ARM_HV.asItem(),
                'K', CONVEYOR_MODULE_HV.asItem(), 'X', PACKER[HV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "large_mixer", LARGE_MIXER.asItem(), "FCF", "RXR", "MKM",
                'C', IV_CIRCUITS, 'F', ChemicalHelper.get(pipeNormalFluid, Polybenzimidazole), 'R',
                ChemicalHelper.get(rotor, Osmiridium), 'M', ELECTRIC_MOTOR_IV.asItem(), 'X', MIXER[IV].asItem(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_centrifuge", LARGE_CENTRIFUGE.asItem(), "SFS",
                "CXC", "MKM", 'C', IV_CIRCUITS, 'F', ChemicalHelper.get(pipeHugeFluid, StainlessSteel), 'S',
                ChemicalHelper.get(spring, MolybdenumDisilicide), 'M', ELECTRIC_MOTOR_IV.asItem(), 'X',
                CENTRIFUGE[IV].asItem(), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_assembler", LARGE_ASSEMBLER.asItem(), "RKR", "CXC",
                "MKM", 'C', IV_CIRCUITS, 'R', ROBOT_ARM_IV.asItem(), 'M', CONVEYOR_MODULE_IV.asItem(), 'X',
                ASSEMBLER[IV].asItem(), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_circuit_assembler",
                LARGE_CIRCUIT_ASSEMBLER.asItem(), "RKR", "CXC", "MKM", 'C', IV_CIRCUITS, 'R',
                ROBOT_ARM_IV.asItem(), 'M', CONVEYOR_MODULE_IV.asItem(), 'X', CIRCUIT_ASSEMBLER[IV].asItem(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_electrolyzer", LARGE_ELECTROLYZER.asItem(), "PCP",
                "WXW", "PKP", 'C', IV_CIRCUITS, 'P', new MaterialEntry(plate, BlackSteel), 'W',
                new MaterialEntry(cableGtSingle, Platinum), 'X', ELECTROLYZER[IV].asItem(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_electromagnet", LARGE_ELECTROMAGNET.asItem(), "PWP",
                "CXC", "PKP", 'C', IV_CIRCUITS, 'P', new MaterialEntry(plate, BlueSteel), 'W',
                new MaterialEntry(wireGtQuadruple, Osmium), 'X', ELECTROMAGNETIC_SEPARATOR[IV].asItem(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "blast_alloy_smelter", BLAST_ALLOY_SMELTER.asItem(), "TCT",
                "WXW", "TCT", 'C', EV_CIRCUITS, 'T', new MaterialEntry(plate, TantalumCarbide), 'W',
                new MaterialEntry(cableGtSingle, Aluminium), 'X', ALLOY_SMELTER[EV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "mega_blast_furnace", MEGA_BLAST_FURNACE.asItem(), "PCP",
                "FSF", "DWD", 'C', ZPM_CIRCUITS, 'S', GTMultiMachines.ELECTRIC_BLAST_FURNACE.asItem(), 'F',
                FIELD_GENERATOR_ZPM.asItem(), 'P', new MaterialEntry(spring, Naquadah), 'D',
                new MaterialEntry(plateDense, NaquadahAlloy), 'W',
                new MaterialEntry(wireGtQuadruple, EnrichedNaquadahTriniumEuropiumDuranide));
        VanillaRecipeHelper.addShapedRecipe(true, "mega_vacuum_freezer", MEGA_VACUUM_FREEZER.asItem(), "PCP",
                "FSF", "DWD", 'C', ZPM_CIRCUITS, 'S', GTMultiMachines.VACUUM_FREEZER.asItem(), 'F', FIELD_GENERATOR_ZPM.asItem(), 'P',
                new MaterialEntry(pipeNormalFluid, NiobiumTitanium), 'D',
                new MaterialEntry(plateDense, RhodiumPlatedPalladium), 'W',
                new MaterialEntry(wireGtQuadruple, EnrichedNaquadahTriniumEuropiumDuranide));
        VanillaRecipeHelper.addShapedRecipe(true, "large_autoclave", LARGE_AUTOCLAVE.asItem(), "PCP", "PAP",
                "BKB", 'C', IV_CIRCUITS, 'A', AUTOCLAVE[IV].asItem(), 'P',
                new MaterialEntry(plateDouble, HSLASteel), 'B', ELECTRIC_PUMP_IV.asItem(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_material_press", LARGE_MATERIAL_PRESS.asItem(),
                "PKP", "GZG", "FKF", 'Z', IV_CIRCUITS, 'P', ELECTRIC_PISTON_IV.asItem(), 'G', COMPRESSOR[IV].asItem(),
                'F', ELECTRIC_MOTOR_IV.asItem(), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_brewer", LARGE_BREWER.asItem(), "SZS", "FBH", "EKE",
                'Z', IV_CIRCUITS, 'S', new MaterialEntry(spring, MolybdenumDisilicide), 'F',
                FERMENTER[IV].asItem(), 'E', ELECTRIC_PUMP_IV.asItem(), 'B', BREWERY[IV].asItem(), 'H',
                FLUID_HEATER[IV].asItem(), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_cutter", LARGE_CUTTER.asItem(), "SMS", "CZL", "EKE",
                'Z', IV_CIRCUITS, 'L', LATHE[IV].asItem(), 'E', ELECTRIC_MOTOR_IV.asItem(), 'C',
                CUTTER[IV].asItem(), 'M', CONVEYOR_MODULE_IV.asItem(), 'S',
                new MaterialEntry(toolHeadBuzzSaw, HSSE), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_distillery", LARGE_DISTILLERY.asItem(), "PZP",
                "EDE", "PZP", 'Z', IV_CIRCUITS, 'D', GTMultiMachines.DISTILLATION_TOWER.asItem(), 'E',
                ELECTRIC_PUMP_IV.asItem(), 'P', ChemicalHelper.get(pipeLargeFluid, Iridium));
        VanillaRecipeHelper.addShapedRecipe(true, "large_extractor", LARGE_EXTRACTOR.asItem(), "PTP", "EZC",
                "BKB", 'Z', IV_CIRCUITS, 'B', ELECTRIC_PISTON_IV.asItem(), 'P', ELECTRIC_PUMP_IV.asItem(),
                'E', EXTRACTOR[IV].asItem(), 'C', CANNER[IV].asItem(), 'T', CASING_TEMPERED_GLASS.asItem(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_extruder", LARGE_EXTRUDER.asItem(), "PZP", "SES",
                "PKP", 'Z', IV_CIRCUITS, 'E', EXTRUDER[IV].asItem(), 'P', ELECTRIC_PISTON_IV.asItem(), 'S',
                new MaterialEntry(spring, MolybdenumDisilicide), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_solidifier", LARGE_SOLIDIFIER.asItem(), "PZP",
                "ESE", "PKP", 'Z', IV_CIRCUITS, 'S', FLUID_SOLIDIFIER[IV].asItem(), 'E',
                ELECTRIC_PUMP_IV.asItem(), 'P', ChemicalHelper.get(pipeNormalFluid, Polyethylene), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_wiremill", LARGE_WIREMILL.asItem(), "PZP", "SWS",
                "MKM", 'Z', IV_CIRCUITS, 'W', WIREMILL[IV].asItem(), 'P',
                new MaterialEntry(plate, HSLASteel), 'S', new MaterialEntry(spring, HSLASteel), 'M',
                ELECTRIC_MOTOR_IV.asItem(), 'K', new MaterialEntry(cableGtSingle, Platinum));
    }

    private static void registerPartsRecipes() {
        VanillaRecipeHelper.addShapedRecipe("crushing_wheels", CRUSHING_WHEELS.asItem(), "TTT", "UCU",
                "UMU", 'T', new MaterialEntry(gearSmall, TungstenCarbide), 'U', ChemicalHelper.get(gear, Ultimet),
                'C', CASING_SECURE_MACERATION.asItem(), 'M', ELECTRIC_MOTOR_IV.asItem());
        VanillaRecipeHelper.addShapedRecipe("slicing_blades", SLICING_BLADES.asItem(), "PPP", "UCU", "UMU",
                'P', new MaterialEntry(plate, TungstenCarbide), 'U', ChemicalHelper.get(gear, Ultimet), 'C',
                CASING_SHOCK_PROOF.asItem(), 'M', ELECTRIC_MOTOR_IV.asItem());
        VanillaRecipeHelper.addShapedRecipe("electrolytic_cell", ELECTROLYTIC_CELL.asItem(), "WWW", "WCW",
                "ZKZ", 'W', new MaterialEntry(wireGtDouble, Platinum), 'Z', IV_CIRCUITS, 'C',
                CASING_NONCONDUCTING.asItem(), 'K', ChemicalHelper.get(cableGtSingle, Tungsten));
        VanillaRecipeHelper.addShapedRecipe("heat_vent", HEAT_VENT.asItem(), "PDP", "RLR", "PDP", 'P',
                new MaterialEntry(plate, TantalumCarbide), 'D',
                ChemicalHelper.get(plateDouble, MolybdenumDisilicide), 'R', ChemicalHelper.get(rotor, Titanium), 'L',
                ChemicalHelper.get(rodLong, MolybdenumDisilicide));
        VanillaRecipeHelper.addShapedRecipe(true, "parallel_hatch_mk1", PARALLEL[IV].asItem(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_IV.asItem(), 'E', EMITTER_IV.asItem(), 'Z', LuV_CIRCUITS, 'H', HULL[IV].asItem(),
                'C', new MaterialEntry(cableGtDouble, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "parallel_hatch_mk2", PARALLEL[LuV].asItem(), "SZE",
                "ZHZ", "CZC", 'S', SENSOR_LuV.asItem(), 'E', EMITTER_LuV.asItem(), 'Z', ZPM_CIRCUITS, 'H',
                HULL[LuV].asItem(), 'C', new MaterialEntry(cableGtDouble, NiobiumTitanium));
        VanillaRecipeHelper.addShapedRecipe(true, "parallel_hatch_mk3", PARALLEL[ZPM].asItem(), "SZE",
                "ZHZ", "CZC", 'S', SENSOR_ZPM.asItem(), 'E', EMITTER_ZPM.asItem(), 'Z', UV_CIRCUITS, 'H',
                HULL[ZPM].asItem(), 'C', new MaterialEntry(cableGtDouble, VanadiumGallium));
        VanillaRecipeHelper.addShapedRecipe(true, "parallel_hatch_mk4", PARALLEL[UV].asItem(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UV.asItem(), 'E', EMITTER_UV.asItem(), 'Z', UHV_CIRCUITS, 'H', HULL[UV].asItem(),
                'C', new MaterialEntry(cableGtDouble, YttriumBariumCuprate));
        VanillaRecipeHelper.addShapedRecipe(true, "parallel_hatch_mk5", PARALLEL[UHV].asItem(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UHV.asItem(), 'E', EMITTER_UHV.asItem(), 'Z', UEV_CIRCUITS, 'H', HULL[UHV].asItem(),
                'C', new MaterialEntry(cableGtDouble, Europium));
        VanillaRecipeHelper.addShapedRecipe(true, "parallel_hatch_mk6", PARALLEL[UEV].asItem(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UEV.asItem(), 'E', EMITTER_UEV.asItem(), 'Z', UIV_CIRCUITS, 'H', HULL[UEV].asItem(),
                'C', new MaterialEntry(cableGtDouble, GTOMaterials.Mithril));
        VanillaRecipeHelper.addShapedRecipe(true, "parallel_hatch_mk7", PARALLEL[UIV].asItem(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UIV.asItem(), 'E', EMITTER_UIV.asItem(), 'Z', UXV_CIRCUITS, 'H', HULL[UIV].asItem(),
                'C', new MaterialEntry(cableGtDouble, Neutronium));
        VanillaRecipeHelper.addShapedRecipe(true, "parallel_hatch_mk8", PARALLEL[UXV].asItem(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UXV.asItem(), 'E', EMITTER_UXV.asItem(), 'Z', OpV_CIRCUITS, 'H', HULL[UXV].asItem(),
                'C', new MaterialEntry(cableGtDouble, GTOMaterials.Taranium));
        VanillaRecipeHelper.addShapedRecipe(true, "parallel_hatch_mk9", PARALLEL[OpV].asItem(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_OpV.asItem(), 'E', EMITTER_OpV.asItem(), 'Z', MAX_CIRCUITS, 'H', HULL[OpV].asItem(),
                'C', new MaterialEntry(cableGtDouble, GTOMaterials.CrystalMatrix));
        VanillaRecipeHelper.addShapedRecipe(true, "parallel_hatch_mk10", PARALLEL[MAX].asItem(), "SZE", "ZHZ",
                "CZC", 'S', GTOItems.MAX_SENSOR.asItem(), 'E', GTOItems.MAX_EMITTER.asItem(), 'Z', GTOItems.SUPRACHRONAL_CIRCUIT[MAX].asItem(), 'H', HULL[MAX].asItem(),
                'C', new MaterialEntry(cableGtDouble, GTOMaterials.CosmicNeutronium));
    }
}

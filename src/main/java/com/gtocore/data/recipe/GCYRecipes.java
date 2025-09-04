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
        VanillaRecipeHelper.addShapedRecipe(true, "large_macerator", LARGE_MACERATION_TOWER.asStack(), "PCP",
                "BXB", "MKM", 'C', IV_CIRCUITS, 'P', new MaterialEntry(plate, TungstenCarbide), 'B',
                ELECTRIC_PISTON_IV.asStack(), 'M', ELECTRIC_MOTOR_IV.asStack(), 'X', MACERATOR[IV].asStack(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_arc_smelter", LARGE_ARC_SMELTER.asStack(), "KDK",
                "CXC", "PPP", 'C', IV_CIRCUITS, 'P', new MaterialEntry(plate, TantalumCarbide), 'X',
                ARC_FURNACE[IV].asStack(), 'D', new MaterialEntry(dust, Graphite), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_ore_washer", LARGE_CHEMICAL_BATH.asStack(), "PGP",
                "CXC", "MKM", 'C', IV_CIRCUITS, 'G', CASING_TEMPERED_GLASS.asStack(), 'P',
                ELECTRIC_PUMP_IV.asStack(), 'M', CONVEYOR_MODULE_IV.asStack(), 'X', ORE_WASHER[IV].asStack(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_sifter", LARGE_SIFTING_FUNNEL.asStack(), "PCP",
                "EXE", "PKP", 'C', IV_CIRCUITS, 'P', new MaterialEntry(plate, HSLASteel), 'E',
                ELECTRIC_PISTON_IV.asStack(), 'X', SIFTER[IV].asStack(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_engraver", LARGE_ENGRAVING_LASER.asStack(), "ICI",
                "EXE", "PKP", 'C', IV_CIRCUITS, 'P', new MaterialEntry(plateDouble, TantalumCarbide), 'I',
                EMITTER_IV.asStack(), 'E', ELECTRIC_PISTON_IV.asStack(), 'X', LASER_ENGRAVER[IV].asStack(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_packer", LARGE_PACKER.asStack(), "RCR", "PXP", "KPK",
                'C', EV_CIRCUITS, 'P', new MaterialEntry(plate, HSLASteel), 'R', ROBOT_ARM_HV.asStack(),
                'K', CONVEYOR_MODULE_HV.asStack(), 'X', PACKER[HV].getItem());
        VanillaRecipeHelper.addShapedRecipe(true, "large_mixer", LARGE_MIXER.asStack(), "FCF", "RXR", "MKM",
                'C', IV_CIRCUITS, 'F', ChemicalHelper.get(pipeNormalFluid, Polybenzimidazole), 'R',
                ChemicalHelper.get(rotor, Osmiridium), 'M', ELECTRIC_MOTOR_IV.asStack(), 'X', MIXER[IV].asStack(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_centrifuge", LARGE_CENTRIFUGE.asStack(), "SFS",
                "CXC", "MKM", 'C', IV_CIRCUITS, 'F', ChemicalHelper.get(pipeHugeFluid, StainlessSteel), 'S',
                ChemicalHelper.get(spring, MolybdenumDisilicide), 'M', ELECTRIC_MOTOR_IV.asStack(), 'X',
                CENTRIFUGE[IV].asStack(), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_assembler", LARGE_ASSEMBLER.asStack(), "RKR", "CXC",
                "MKM", 'C', IV_CIRCUITS, 'R', ROBOT_ARM_IV.asStack(), 'M', CONVEYOR_MODULE_IV.asStack(), 'X',
                ASSEMBLER[IV].asStack(), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_circuit_assembler",
                LARGE_CIRCUIT_ASSEMBLER.asStack(), "RKR", "CXC", "MKM", 'C', IV_CIRCUITS, 'R',
                ROBOT_ARM_IV.asStack(), 'M', CONVEYOR_MODULE_IV.asStack(), 'X', CIRCUIT_ASSEMBLER[IV].asStack(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_electrolyzer", LARGE_ELECTROLYZER.asStack(), "PCP",
                "WXW", "PKP", 'C', IV_CIRCUITS, 'P', new MaterialEntry(plate, BlackSteel), 'W',
                new MaterialEntry(cableGtSingle, Platinum), 'X', ELECTROLYZER[IV].asStack(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_electromagnet", LARGE_ELECTROMAGNET.asStack(), "PWP",
                "CXC", "PKP", 'C', IV_CIRCUITS, 'P', new MaterialEntry(plate, BlueSteel), 'W',
                new MaterialEntry(wireGtQuadruple, Osmium), 'X', ELECTROMAGNETIC_SEPARATOR[IV].asStack(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "blast_alloy_smelter", BLAST_ALLOY_SMELTER.asStack(), "TCT",
                "WXW", "TCT", 'C', EV_CIRCUITS, 'T', new MaterialEntry(plate, TantalumCarbide), 'W',
                new MaterialEntry(cableGtSingle, Aluminium), 'X', ALLOY_SMELTER[EV].getItem());
        VanillaRecipeHelper.addShapedRecipe(true, "mega_blast_furnace", MEGA_BLAST_FURNACE.asStack(), "PCP",
                "FSF", "DWD", 'C', ZPM_CIRCUITS, 'S', GTMultiMachines.ELECTRIC_BLAST_FURNACE.asStack(), 'F',
                FIELD_GENERATOR_ZPM.asStack(), 'P', new MaterialEntry(spring, Naquadah), 'D',
                new MaterialEntry(plateDense, NaquadahAlloy), 'W',
                new MaterialEntry(wireGtQuadruple, EnrichedNaquadahTriniumEuropiumDuranide));
        VanillaRecipeHelper.addShapedRecipe(true, "mega_vacuum_freezer", MEGA_VACUUM_FREEZER.asStack(), "PCP",
                "FSF", "DWD", 'C', ZPM_CIRCUITS, 'S', GTMultiMachines.VACUUM_FREEZER.asStack(), 'F', FIELD_GENERATOR_ZPM.asStack(), 'P',
                new MaterialEntry(pipeNormalFluid, NiobiumTitanium), 'D',
                new MaterialEntry(plateDense, RhodiumPlatedPalladium), 'W',
                new MaterialEntry(wireGtQuadruple, EnrichedNaquadahTriniumEuropiumDuranide));
        VanillaRecipeHelper.addShapedRecipe(true, "large_autoclave", LARGE_AUTOCLAVE.asStack(), "PCP", "PAP",
                "BKB", 'C', IV_CIRCUITS, 'A', AUTOCLAVE[IV].asStack(), 'P',
                new MaterialEntry(plateDouble, HSLASteel), 'B', ELECTRIC_PUMP_IV.asStack(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_material_press", LARGE_MATERIAL_PRESS.asStack(),
                "PKP", "GZG", "FKF", 'Z', IV_CIRCUITS, 'P', ELECTRIC_PISTON_IV.asStack(), 'G', COMPRESSOR[IV].asStack(),
                'F', ELECTRIC_MOTOR_IV.asStack(), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_brewer", LARGE_BREWER.asStack(), "SZS", "FBH", "EKE",
                'Z', IV_CIRCUITS, 'S', new MaterialEntry(spring, MolybdenumDisilicide), 'F',
                FERMENTER[IV].asStack(), 'E', ELECTRIC_PUMP_IV.asStack(), 'B', BREWERY[IV].asStack(), 'H',
                FLUID_HEATER[IV].asStack(), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_cutter", LARGE_CUTTER.asStack(), "SMS", "CZL", "EKE",
                'Z', IV_CIRCUITS, 'L', LATHE[IV].asStack(), 'E', ELECTRIC_MOTOR_IV.asStack(), 'C',
                CUTTER[IV].asStack(), 'M', CONVEYOR_MODULE_IV.asStack(), 'S',
                new MaterialEntry(toolHeadBuzzSaw, HSSE), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_distillery", LARGE_DISTILLERY.asStack(), "PZP",
                "EDE", "PZP", 'Z', IV_CIRCUITS, 'D', GTMultiMachines.DISTILLATION_TOWER.asStack(), 'E',
                ELECTRIC_PUMP_IV.asStack(), 'P', ChemicalHelper.get(pipeLargeFluid, Iridium));
        VanillaRecipeHelper.addShapedRecipe(true, "large_extractor", LARGE_EXTRACTOR.asStack(), "PTP", "EZC",
                "BKB", 'Z', IV_CIRCUITS, 'B', ELECTRIC_PISTON_IV.asStack(), 'P', ELECTRIC_PUMP_IV.asStack(),
                'E', EXTRACTOR[IV].asStack(), 'C', CANNER[IV].asStack(), 'T', CASING_TEMPERED_GLASS.asStack(), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_extruder", LARGE_EXTRUDER.asStack(), "PZP", "SES",
                "PKP", 'Z', IV_CIRCUITS, 'E', EXTRUDER[IV].asStack(), 'P', ELECTRIC_PISTON_IV.asStack(), 'S',
                new MaterialEntry(spring, MolybdenumDisilicide), 'K', new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_solidifier", LARGE_SOLIDIFIER.asStack(), "PZP",
                "ESE", "PKP", 'Z', IV_CIRCUITS, 'S', FLUID_SOLIDIFIER[IV].asStack(), 'E',
                ELECTRIC_PUMP_IV.asStack(), 'P', ChemicalHelper.get(pipeNormalFluid, Polyethylene), 'K',
                new MaterialEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(true, "large_wiremill", LARGE_WIREMILL.asStack(), "PZP", "SWS",
                "MKM", 'Z', IV_CIRCUITS, 'W', WIREMILL[IV].asStack(), 'P',
                new MaterialEntry(plate, HSLASteel), 'S', new MaterialEntry(spring, HSLASteel), 'M',
                ELECTRIC_MOTOR_IV.asStack(), 'K', new MaterialEntry(cableGtSingle, Platinum));
    }

    private static void registerPartsRecipes() {
        VanillaRecipeHelper.addShapedRecipe("crushing_wheels", CRUSHING_WHEELS.asStack(), "TTT", "UCU",
                "UMU", 'T', new MaterialEntry(gearSmall, TungstenCarbide), 'U', ChemicalHelper.get(gear, Ultimet),
                'C', CASING_SECURE_MACERATION.asStack(), 'M', ELECTRIC_MOTOR_IV.asItem());
        VanillaRecipeHelper.addShapedRecipe("slicing_blades", SLICING_BLADES.asStack(), "PPP", "UCU", "UMU",
                'P', new MaterialEntry(plate, TungstenCarbide), 'U', ChemicalHelper.get(gear, Ultimet), 'C',
                CASING_SHOCK_PROOF.asStack(), 'M', ELECTRIC_MOTOR_IV.asItem());
        VanillaRecipeHelper.addShapedRecipe("electrolytic_cell", ELECTROLYTIC_CELL.asStack(), "WWW", "WCW",
                "ZKZ", 'W', new MaterialEntry(wireGtDouble, Platinum), 'Z', IV_CIRCUITS, 'C',
                CASING_NONCONDUCTING.asStack(), 'K', ChemicalHelper.get(cableGtSingle, Tungsten));
        VanillaRecipeHelper.addShapedRecipe("heat_vent", HEAT_VENT.asStack(), "PDP", "RLR", "PDP", 'P',
                new MaterialEntry(plate, TantalumCarbide), 'D',
                ChemicalHelper.get(plateDouble, MolybdenumDisilicide), 'R', ChemicalHelper.get(rotor, Titanium), 'L',
                ChemicalHelper.get(rodLong, MolybdenumDisilicide));
        VanillaRecipeHelper.addShapedRecipe("parallel_hatch_mk1", PARALLEL[IV].asStack(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_IV.asStack(), 'E', EMITTER_IV.asStack(), 'Z', LuV_CIRCUITS, 'H', HULL[IV].asStack(),
                'C', new MaterialEntry(cableGtDouble, Platinum));
        VanillaRecipeHelper.addShapedRecipe("parallel_hatch_mk2", PARALLEL[LuV].asStack(), "SZE",
                "ZHZ", "CZC", 'S', SENSOR_LuV.asStack(), 'E', EMITTER_LuV.asStack(), 'Z', ZPM_CIRCUITS, 'H',
                HULL[LuV].asStack(), 'C', new MaterialEntry(cableGtDouble, NiobiumTitanium));
        VanillaRecipeHelper.addShapedRecipe("parallel_hatch_mk3", PARALLEL[ZPM].asStack(), "SZE",
                "ZHZ", "CZC", 'S', SENSOR_ZPM.asStack(), 'E', EMITTER_ZPM.asStack(), 'Z', UV_CIRCUITS, 'H',
                HULL[ZPM].asStack(), 'C', new MaterialEntry(cableGtDouble, VanadiumGallium));
        VanillaRecipeHelper.addShapedRecipe("parallel_hatch_mk4", PARALLEL[UV].asStack(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UV.asStack(), 'E', EMITTER_UV.asStack(), 'Z', UHV_CIRCUITS, 'H', HULL[UV].asStack(),
                'C', new MaterialEntry(cableGtDouble, YttriumBariumCuprate));
        VanillaRecipeHelper.addShapedRecipe("parallel_hatch_mk5", PARALLEL[UHV].asStack(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UHV.asStack(), 'E', EMITTER_UHV.asStack(), 'Z', UEV_CIRCUITS, 'H', HULL[UHV].asStack(),
                'C', new MaterialEntry(cableGtDouble, Europium));
        VanillaRecipeHelper.addShapedRecipe("parallel_hatch_mk6", PARALLEL[UEV].asStack(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UEV.asStack(), 'E', EMITTER_UEV.asStack(), 'Z', UIV_CIRCUITS, 'H', HULL[UEV].asStack(),
                'C', new MaterialEntry(cableGtDouble, GTOMaterials.Mithril));
        VanillaRecipeHelper.addShapedRecipe("parallel_hatch_mk7", PARALLEL[UIV].asStack(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UIV.asStack(), 'E', EMITTER_UIV.asStack(), 'Z', UXV_CIRCUITS, 'H', HULL[UIV].asStack(),
                'C', new MaterialEntry(cableGtDouble, Neutronium));
        VanillaRecipeHelper.addShapedRecipe("parallel_hatch_mk8", PARALLEL[UXV].asStack(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UXV.asStack(), 'E', EMITTER_UXV.asStack(), 'Z', OpV_CIRCUITS, 'H', HULL[UXV].asStack(),
                'C', new MaterialEntry(cableGtDouble, GTOMaterials.Taranium));
        VanillaRecipeHelper.addShapedRecipe("parallel_hatch_mk9", PARALLEL[OpV].asStack(), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_OpV.asStack(), 'E', EMITTER_OpV.asStack(), 'Z', MAX_CIRCUITS, 'H', HULL[OpV].asStack(),
                'C', new MaterialEntry(cableGtDouble, GTOMaterials.CrystalMatrix));
        VanillaRecipeHelper.addShapedRecipe("parallel_hatch_mk10", PARALLEL[MAX].asStack(), "SZE", "ZHZ",
                "CZC", 'S', GTOItems.MAX_SENSOR.asStack(), 'E', GTOItems.MAX_EMITTER.asStack(), 'Z', GTOItems.SUPRACHRONAL_CIRCUIT[MAX].asStack(), 'H', HULL[MAX].asStack(),
                'C', new MaterialEntry(cableGtDouble, GTOMaterials.CosmicNeutronium));

        // if (GTOCore.isSimple()) {
        // VanillaRecipeHelper.addShapedRecipe( "parallel_hatch_mk0", PARALLEL[EV].asStack(), "SZE", "ZHZ",
        // "CZC", 'S', SENSOR_EV.asStack(), 'E', EMITTER_EV.asStack(), 'Z', IV_CIRCUITS, 'H', HULL[EV].asStack(),
        // 'C', new MaterialEntry(cableGtDouble, Platinum));
        //
        // }
    }
}

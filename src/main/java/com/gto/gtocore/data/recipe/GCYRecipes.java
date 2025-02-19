package com.gto.gtocore.data.recipe;

import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GCYMBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.CASING_TEMPERED_GLASS;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.data.recipe.CustomTags.*;
import static com.gto.gtocore.common.data.machines.GCYMMachines.*;

public interface GCYRecipes {

    static void init(Consumer<FinishedRecipe> provider) {
        registerPartsRecipes(provider);
        registerMultiblockControllerRecipes(provider);
    }

    private static void registerMultiblockControllerRecipes(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_macerator", LARGE_MACERATION_TOWER.asStack(), "PCP",
                "BXB", "MKM", 'C', IV_CIRCUITS, 'P', new UnificationEntry(plate, TungstenCarbide), 'B',
                ELECTRIC_PISTON_IV.asStack(), 'M', ELECTRIC_MOTOR_IV.asStack(), 'X', MACERATOR[IV].asStack(), 'K',
                new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_arc_smelter", LARGE_ARC_SMELTER.asStack(), "KDK",
                "CXC", "PPP", 'C', IV_CIRCUITS, 'P', new UnificationEntry(plate, TantalumCarbide), 'X',
                ARC_FURNACE[IV].asStack(), 'D', new UnificationEntry(dust, Graphite), 'K',
                new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_ore_washer", LARGE_CHEMICAL_BATH.asStack(), "PGP",
                "CXC", "MKM", 'C', IV_CIRCUITS, 'G', CASING_TEMPERED_GLASS.asStack(), 'P',
                ELECTRIC_PUMP_IV.asStack(), 'M', CONVEYOR_MODULE_IV.asStack(), 'X', ORE_WASHER[IV].asStack(), 'K',
                new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_sifter", LARGE_SIFTING_FUNNEL.asStack(), "PCP",
                "EXE", "PKP", 'C', IV_CIRCUITS, 'P', new UnificationEntry(plate, HSLASteel), 'E',
                ELECTRIC_PISTON_IV.asStack(), 'X', SIFTER[IV].asStack(), 'K',
                new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_engraver", LARGE_ENGRAVING_LASER.asStack(), "ICI",
                "EXE", "PKP", 'C', IV_CIRCUITS, 'P', new UnificationEntry(plateDouble, TantalumCarbide), 'I',
                EMITTER_IV.asStack(), 'E', ELECTRIC_PISTON_IV.asStack(), 'X', LASER_ENGRAVER[IV].asStack(), 'K',
                new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_packer", LARGE_PACKER.asStack(), "RCR", "PXP", "KPK",
                'C', EV_CIRCUITS, 'P', new UnificationEntry(plate, HSLASteel), 'R', ROBOT_ARM_HV.asStack(),
                'K', CONVEYOR_MODULE_HV.asStack(), 'X', PACKER[HV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_mixer", LARGE_MIXER.asStack(), "FCF", "RXR", "MKM",
                'C', IV_CIRCUITS, 'F', ChemicalHelper.get(pipeNormalFluid, Polybenzimidazole), 'R',
                ChemicalHelper.get(rotor, Osmiridium), 'M', ELECTRIC_MOTOR_IV.asStack(), 'X', MIXER[IV].asStack(), 'K',
                new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_centrifuge", LARGE_CENTRIFUGE.asStack(), "SFS",
                "CXC", "MKM", 'C', IV_CIRCUITS, 'F', ChemicalHelper.get(pipeHugeFluid, StainlessSteel), 'S',
                ChemicalHelper.get(spring, MolybdenumDisilicide), 'M', ELECTRIC_MOTOR_IV.asStack(), 'X',
                CENTRIFUGE[IV].asStack(), 'K', new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_assembler", LARGE_ASSEMBLER.asStack(), "RKR", "CXC",
                "MKM", 'C', IV_CIRCUITS, 'R', ROBOT_ARM_IV.asStack(), 'M', CONVEYOR_MODULE_IV.asStack(), 'X',
                ASSEMBLER[IV].asStack(), 'K', new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_circuit_assembler",
                LARGE_CIRCUIT_ASSEMBLER.asStack(), "RKR", "CXC", "MKM", 'C', IV_CIRCUITS, 'R',
                ROBOT_ARM_IV.asStack(), 'M', CONVEYOR_MODULE_IV.asStack(), 'X', CIRCUIT_ASSEMBLER[IV].asStack(), 'K',
                new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_electrolyzer", LARGE_ELECTROLYZER.asStack(), "PCP",
                "WXW", "PKP", 'C', IV_CIRCUITS, 'P', new UnificationEntry(plate, BlackSteel), 'W',
                new UnificationEntry(cableGtSingle, Platinum), 'X', ELECTROLYZER[IV].asStack(), 'K',
                new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_electromagnet", LARGE_ELECTROMAGNET.asStack(), "PWP",
                "CXC", "PKP", 'C', IV_CIRCUITS, 'P', new UnificationEntry(plate, BlueSteel), 'W',
                new UnificationEntry(wireGtQuadruple, Osmium), 'X', ELECTROMAGNETIC_SEPARATOR[IV].asStack(), 'K',
                new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "blast_alloy_smelter", BLAST_ALLOY_SMELTER.asStack(), "TCT",
                "WXW", "TCT", 'C', EV_CIRCUITS, 'T', new UnificationEntry(plate, TantalumCarbide), 'W',
                new UnificationEntry(cableGtSingle, Aluminium), 'X', ALLOY_SMELTER[EV].asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, true, "mega_blast_furnace", MEGA_BLAST_FURNACE.asStack(), "PCP",
                "FSF", "DWD", 'C', ZPM_CIRCUITS, 'S', GTMultiMachines.ELECTRIC_BLAST_FURNACE.asStack(), 'F',
                FIELD_GENERATOR_ZPM.asStack(), 'P', new UnificationEntry(spring, Naquadah), 'D',
                new UnificationEntry(plateDense, NaquadahAlloy), 'W',
                new UnificationEntry(wireGtQuadruple, EnrichedNaquadahTriniumEuropiumDuranide));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "mega_vacuum_freezer", MEGA_VACUUM_FREEZER.asStack(), "PCP",
                "FSF", "DWD", 'C', ZPM_CIRCUITS, 'S', GTMultiMachines.VACUUM_FREEZER.asStack(), 'F', FIELD_GENERATOR_ZPM.asStack(), 'P',
                new UnificationEntry(pipeNormalFluid, NiobiumTitanium), 'D',
                new UnificationEntry(plateDense, RhodiumPlatedPalladium), 'W',
                new UnificationEntry(wireGtQuadruple, EnrichedNaquadahTriniumEuropiumDuranide));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_autoclave", LARGE_AUTOCLAVE.asStack(), "PCP", "PAP",
                "BKB", 'C', IV_CIRCUITS, 'A', AUTOCLAVE[IV].asStack(), 'P',
                new UnificationEntry(plateDouble, HSLASteel), 'B', ELECTRIC_PUMP_IV.asStack(), 'K',
                new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_material_press", LARGE_MATERIAL_PRESS.asStack(),
                "PKP", "GZG", "FKF", 'Z', IV_CIRCUITS, 'P', ELECTRIC_PISTON_IV.asStack(), 'G', COMPRESSOR[IV].asStack(),
                'F', ELECTRIC_MOTOR_IV.asStack(), 'K', new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_brewer", LARGE_BREWER.asStack(), "SZS", "FBH", "EKE",
                'Z', IV_CIRCUITS, 'S', new UnificationEntry(spring, MolybdenumDisilicide), 'F',
                FERMENTER[IV].asStack(), 'E', ELECTRIC_PUMP_IV.asStack(), 'B', BREWERY[IV].asStack(), 'H',
                FLUID_HEATER[IV].asStack(), 'K', new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_cutter", LARGE_CUTTER.asStack(), "SMS", "CZL", "EKE",
                'Z', IV_CIRCUITS, 'L', LATHE[IV].asStack(), 'E', ELECTRIC_MOTOR_IV.asStack(), 'C',
                CUTTER[IV].asStack(), 'M', CONVEYOR_MODULE_IV.asStack(), 'S',
                new UnificationEntry(toolHeadBuzzSaw, TungstenCarbide), 'K',
                new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_distillery", LARGE_DISTILLERY.asStack(), "PZP",
                "EDE", "PZP", 'Z', IV_CIRCUITS, 'D', GTMultiMachines.DISTILLATION_TOWER.asStack(), 'E',
                ELECTRIC_PUMP_IV.asStack(), 'P', ChemicalHelper.get(pipeLargeFluid, Iridium));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_extractor", LARGE_EXTRACTOR.asStack(), "PTP", "EZC",
                "BKB", 'Z', IV_CIRCUITS, 'B', ELECTRIC_PISTON_IV.asStack(), 'P', ELECTRIC_PUMP_IV.asStack(),
                'E', EXTRACTOR[IV].asStack(), 'C', CANNER[IV].asStack(), 'T', CASING_TEMPERED_GLASS.asStack(), 'K',
                new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_extruder", LARGE_EXTRUDER.asStack(), "PZP", "SES",
                "PKP", 'Z', IV_CIRCUITS, 'E', EXTRUDER[IV].asStack(), 'P', ELECTRIC_PISTON_IV.asStack(), 'S',
                new UnificationEntry(spring, MolybdenumDisilicide), 'K', new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_solidifier", LARGE_SOLIDIFIER.asStack(), "PZP",
                "ESE", "PKP", 'Z', IV_CIRCUITS, 'S', FLUID_SOLIDIFIER[IV].asStack(), 'E',
                ELECTRIC_PUMP_IV.asStack(), 'P', ChemicalHelper.get(pipeNormalFluid, Polyethylene), 'K',
                new UnificationEntry(cableGtSingle, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, true, "large_wiremill", LARGE_WIREMILL.asStack(), "PZP", "SWS",
                "MKM", 'Z', IV_CIRCUITS, 'W', WIREMILL[IV].asStack(), 'P',
                new UnificationEntry(plate, HSLASteel), 'S', new UnificationEntry(spring, HSLASteel), 'M',
                ELECTRIC_MOTOR_IV.asStack(), 'K', new UnificationEntry(cableGtSingle, Platinum));
    }

    private static void registerPartsRecipes(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider, "crushing_wheels", CRUSHING_WHEELS.asStack(2), "TTT", "UCU",
                "UMU", 'T', new UnificationEntry(gearSmall, TungstenCarbide), 'U', ChemicalHelper.get(gear, Ultimet),
                'C', CASING_SECURE_MACERATION.asStack(), 'M', ELECTRIC_MOTOR_IV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, "slicing_blades", SLICING_BLADES.asStack(2), "PPP", "UCU", "UMU",
                'P', new UnificationEntry(plate, TungstenCarbide), 'U', ChemicalHelper.get(gear, Ultimet), 'C',
                CASING_SHOCK_PROOF.asStack(), 'M', ELECTRIC_MOTOR_IV.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, "electrolytic_cell", ELECTROLYTIC_CELL.asStack(2), "WWW", "WCW",
                "ZKZ", 'W', new UnificationEntry(wireGtDouble, Platinum), 'Z', IV_CIRCUITS, 'C',
                CASING_NONCONDUCTING.asStack(), 'K', ChemicalHelper.get(cableGtSingle, Tungsten));
        VanillaRecipeHelper.addShapedRecipe(provider, "heat_vent", HEAT_VENT.asStack(2), "PDP", "RLR", "PDP", 'P',
                new UnificationEntry(plate, TantalumCarbide), 'D',
                ChemicalHelper.get(plateDouble, MolybdenumDisilicide), 'R', ChemicalHelper.get(rotor, Titanium), 'L',
                ChemicalHelper.get(rodLong, MolybdenumDisilicide));
        VanillaRecipeHelper.addShapedRecipe(provider, "parallel_hatch_mk1", PARALLEL[IV].asStack(1), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_IV.asStack(), 'E', EMITTER_IV.asStack(), 'Z', LuV_CIRCUITS, 'H', HULL[IV].asStack(),
                'C', new UnificationEntry(cableGtDouble, Platinum));
        VanillaRecipeHelper.addShapedRecipe(provider, "parallel_hatch_mk2", PARALLEL[LuV].asStack(1), "SZE",
                "ZHZ", "CZC", 'S', SENSOR_LuV.asStack(), 'E', EMITTER_LuV.asStack(), 'Z', ZPM_CIRCUITS, 'H',
                HULL[LuV].asStack(), 'C', new UnificationEntry(cableGtDouble, NiobiumTitanium));
        VanillaRecipeHelper.addShapedRecipe(provider, "parallel_hatch_mk3", PARALLEL[ZPM].asStack(1), "SZE",
                "ZHZ", "CZC", 'S', SENSOR_ZPM.asStack(), 'E', EMITTER_ZPM.asStack(), 'Z', UV_CIRCUITS, 'H',
                HULL[ZPM].asStack(), 'C', new UnificationEntry(cableGtDouble, VanadiumGallium));
        VanillaRecipeHelper.addShapedRecipe(provider, "parallel_hatch_mk4", PARALLEL[UV].asStack(1), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UV.asStack(), 'E', EMITTER_UV.asStack(), 'Z', UHV_CIRCUITS, 'H', HULL[UV].asStack(),
                'C', new UnificationEntry(cableGtDouble, YttriumBariumCuprate));
        VanillaRecipeHelper.addShapedRecipe(provider, "parallel_hatch_mk5", PARALLEL[UHV].asStack(1), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UHV.asStack(), 'E', EMITTER_UHV.asStack(), 'Z', UEV_CIRCUITS, 'H', HULL[UHV].asStack(),
                'C', new UnificationEntry(cableGtDouble, Europium));
        VanillaRecipeHelper.addShapedRecipe(provider, "parallel_hatch_mk6", PARALLEL[UEV].asStack(1), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UEV.asStack(), 'E', EMITTER_UEV.asStack(), 'Z', UIV_CIRCUITS, 'H', HULL[UEV].asStack(),
                'C', new UnificationEntry(cableGtDouble, GTOMaterials.Mithril));
        VanillaRecipeHelper.addShapedRecipe(provider, "parallel_hatch_mk7", PARALLEL[UIV].asStack(1), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UIV.asStack(), 'E', EMITTER_UIV.asStack(), 'Z', UXV_CIRCUITS, 'H', HULL[UIV].asStack(),
                'C', new UnificationEntry(cableGtDouble, Neutronium));
        VanillaRecipeHelper.addShapedRecipe(provider, "parallel_hatch_mk8", PARALLEL[UXV].asStack(1), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_UXV.asStack(), 'E', EMITTER_UXV.asStack(), 'Z', OpV_CIRCUITS, 'H', HULL[UXV].asStack(),
                'C', new UnificationEntry(cableGtDouble, GTOMaterials.Taranium));
        VanillaRecipeHelper.addShapedRecipe(provider, "parallel_hatch_mk9", PARALLEL[OpV].asStack(1), "SZE", "ZHZ",
                "CZC", 'S', SENSOR_OpV.asStack(), 'E', EMITTER_OpV.asStack(), 'Z', MAX_CIRCUITS, 'H', HULL[OpV].asStack(),
                'C', new UnificationEntry(cableGtDouble, GTOMaterials.CrystalMatrix));
        VanillaRecipeHelper.addShapedRecipe(provider, "parallel_hatch_mk10", PARALLEL[MAX].asStack(1), "SZE", "ZHZ",
                "CZC", 'S', GTOItems.MAX_SENSOR.asStack(), 'E', GTOItems.MAX_EMITTER.asStack(), 'Z', GTOItems.SUPRACHRONAL_CIRCUIT[MAX].asStack(), 'H', HULL[MAX].asStack(),
                'C', new UnificationEntry(cableGtDouble, GTOMaterials.CosmicNeutronium));
    }
}

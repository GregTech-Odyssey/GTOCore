package com.gto.gtocore.data;

import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CraftingComponent;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.data.recipe.GTCraftingComponents.*;

public final class CraftingComponents {

    public static CraftingComponent BUFFER;
    public static CraftingComponent FLUID_REGULATOR;

    public static void init() {
        PUMP.add(14, GTOItems.MAX_ELECTRIC_PUMP.asStack());

        CONVEYOR.add(14, GTOItems.MAX_CONVEYOR_MODULE.asStack());

        MOTOR.add(14, GTOItems.MAX_ELECTRIC_MOTOR.asStack());

        PISTON.add(14, GTOItems.MAX_ELECTRIC_PISTON.asStack());

        EMITTER.add(14, GTOItems.MAX_EMITTER.asStack());

        SENSOR.add(14, GTOItems.MAX_SENSOR.asStack());

        FIELD_GENERATOR.add(14, GTOItems.MAX_FIELD_GENERATOR.asStack());

        ROBOT_ARM.add(14, GTOItems.MAX_ROBOT_ARM.asStack());

        GRINDER.add(5, GTItems.COMPONENT_GRINDER_DIAMOND.asStack())
                .add(6, GTItems.COMPONENT_GRINDER_DIAMOND.asStack())
                .add(7, GTItems.COMPONENT_GRINDER_TUNGSTEN.asStack())
                .add(8, GTItems.COMPONENT_GRINDER_TUNGSTEN.asStack())
                .add(9, GTItems.COMPONENT_GRINDER_TUNGSTEN.asStack())
                .add(10, GTItems.COMPONENT_GRINDER_TUNGSTEN.asStack())
                .add(11, GTItems.COMPONENT_GRINDER_TUNGSTEN.asStack())
                .add(12, GTOItems.BEDROCK_DRILL.asStack())
                .add(13, GTOItems.BEDROCK_DRILL.asStack());

        SAWBLADE.add(3, new UnificationEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.VanadiumSteel))
                .add(4, new UnificationEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.TungstenSteel))
                .add(5, new UnificationEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.TungstenSteel))
                .add(6, new UnificationEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.HSSE))
                .add(7, new UnificationEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.HSSE))
                .add(8, new UnificationEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.NaquadahAlloy))
                .add(9, new UnificationEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.NaquadahAlloy))
                .add(10, new UnificationEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.Duranium))
                .add(11, new UnificationEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.Duranium))
                .add(12, new UnificationEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.Neutronium))
                .add(13, new UnificationEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.Neutronium));

        WIRE_ELECTRIC.add(10, new UnificationEntry(TagPrefix.wireGtSingle, GTMaterials.Mendelevium))
                .add(11, new UnificationEntry(TagPrefix.wireGtSingle, GTMaterials.Mendelevium))
                .add(12, new UnificationEntry(TagPrefix.wireGtSingle, GTMaterials.Mendelevium))
                .add(13, new UnificationEntry(TagPrefix.wireGtSingle, GTOMaterials.Uruium))
                .add(14, new UnificationEntry(TagPrefix.wireGtSingle, GTOMaterials.Uruium));

        WIRE_QUAD.add(10, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Mithril))
                .add(11, new UnificationEntry(TagPrefix.wireGtQuadruple, GTMaterials.Neutronium))
                .add(12, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Taranium))
                .add(13, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.CrystalMatrix))
                .add(14, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.CosmicNeutronium));

        WIRE_OCT.add(10, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.Mithril))
                .add(11, new UnificationEntry(TagPrefix.wireGtOctal, GTMaterials.Neutronium))
                .add(12, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.Taranium))
                .add(13, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.CrystalMatrix))
                .add(14, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.CosmicNeutronium));

        WIRE_HEX.add(10, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.Mithril))
                .add(11, new UnificationEntry(TagPrefix.wireGtHex, GTMaterials.Neutronium))
                .add(12, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.Taranium))
                .add(13, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.CrystalMatrix))
                .add(14, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.CosmicNeutronium));

        CABLE.add(10, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.Mithril))
                .add(11, new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Neutronium))
                .add(12, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.Taranium))
                .add(13, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.CrystalMatrix))
                .add(14, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.CosmicNeutronium));

        CABLE_DOUBLE.add(10, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.Mithril))
                .add(11, new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Neutronium))
                .add(12, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.Taranium))
                .add(13, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.CrystalMatrix))
                .add(14, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.CosmicNeutronium));

        CABLE_QUAD.add(10, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.Mithril))
                .add(11, new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Neutronium))
                .add(12, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.Taranium))
                .add(13, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.CrystalMatrix))
                .add(14, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.CosmicNeutronium));

        WIRE_ELECTRIC.add(10, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.Mithril))
                .add(11, new UnificationEntry(TagPrefix.cableGtQuadruple, GTMaterials.Neutronium))
                .add(12, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.Taranium))
                .add(13, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.CrystalMatrix))
                .add(14, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.CosmicNeutronium));

        CABLE_OCT.add(10, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.Mithril))
                .add(11, new UnificationEntry(TagPrefix.cableGtOctal, GTMaterials.Neutronium))
                .add(12, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.Taranium))
                .add(13, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.CrystalMatrix))
                .add(14, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.CosmicNeutronium));

        CABLE_HEX.add(10, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.Mithril))
                .add(11, new UnificationEntry(TagPrefix.cableGtHex, GTMaterials.Neutronium))
                .add(12, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.Taranium))
                .add(13, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.CrystalMatrix))
                .add(14, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium));

        CABLE_TIER_UP.add(9, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.Mithril))
                .add(10, new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Neutronium))
                .add(11, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.Taranium))
                .add(12, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.CrystalMatrix))
                .add(13, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.CosmicNeutronium))
                .add(14, new UnificationEntry(TagPrefix.wireGtSingle, GTOMaterials.SpaceTime));

        CABLE_TIER_UP_DOUBLE.add(9, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.Mithril))
                .add(10, new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Neutronium))
                .add(11, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.Taranium))
                .add(12, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.CrystalMatrix))
                .add(13, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.CosmicNeutronium))
                .add(14, new UnificationEntry(TagPrefix.wireGtDouble, GTOMaterials.SpaceTime));

        CABLE_TIER_UP_QUAD.add(9, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.Mithril))
                .add(10, new UnificationEntry(TagPrefix.cableGtQuadruple, GTMaterials.Neutronium))
                .add(11, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.Taranium))
                .add(12, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.CrystalMatrix))
                .add(13, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.CosmicNeutronium))
                .add(14, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.SpaceTime));

        CABLE_TIER_UP_OCT.add(9, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.Mithril))
                .add(10, new UnificationEntry(TagPrefix.cableGtOctal, GTMaterials.Neutronium))
                .add(11, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.Taranium))
                .add(12, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.CrystalMatrix))
                .add(13, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.CosmicNeutronium))
                .add(14, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.SpaceTime));

        CABLE_TIER_UP_HEX.add(9, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.Mithril))
                .add(10, new UnificationEntry(TagPrefix.cableGtHex, GTMaterials.Neutronium))
                .add(11, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.Taranium))
                .add(12, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.CrystalMatrix))
                .add(13, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium))
                .add(14, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.SpaceTime));

        PIPE_NORMAL.add(9, new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Neutronium))
                .add(10, new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Neutronium))
                .add(11, new UnificationEntry(TagPrefix.pipeNormalFluid, GTOMaterials.Enderium))
                .add(12, new UnificationEntry(TagPrefix.pipeNormalFluid, GTOMaterials.Enderium))
                .add(13, new UnificationEntry(TagPrefix.pipeNormalFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter))
                .add(14, new UnificationEntry(TagPrefix.pipeNormalFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter));

        PIPE_LARGE.add(7, new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Iridium))
                .add(9, new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Neutronium))
                .add(10, new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Neutronium))
                .add(11, new UnificationEntry(TagPrefix.pipeLargeFluid, GTOMaterials.Enderium))
                .add(12, new UnificationEntry(TagPrefix.pipeLargeFluid, GTOMaterials.Enderium))
                .add(13, new UnificationEntry(TagPrefix.pipeLargeFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter))
                .add(14, new UnificationEntry(TagPrefix.pipeLargeFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter));

        PIPE_NONUPLE.add(0, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Bronze))
                .add(1, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Bronze))
                .add(2, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Steel))
                .add(3, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.StainlessSteel))
                .add(9, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Neutronium))
                .add(10, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Neutronium))
                .add(11, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTOMaterials.Enderium))
                .add(12, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTOMaterials.Enderium))
                .add(13, new UnificationEntry(TagPrefix.pipeNonupleFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter))
                .add(14, new UnificationEntry(TagPrefix.pipeNonupleFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter));

        GLASS.add(UHV, GTBlocks.FUSION_GLASS.asStack())
                .add(UEV, GTBlocks.FUSION_GLASS.asStack())
                .add(UIV, GTOBlocks.FORCE_FIELD_GLASS.asStack())
                .add(UXV, GTOBlocks.FORCE_FIELD_GLASS.asStack())
                .add(OpV, GTOBlocks.FORCE_FIELD_GLASS.asStack())
                .add(MAX, GTOBlocks.FORCE_FIELD_GLASS.asStack());

        PLATE.add(10, new UnificationEntry(TagPrefix.plate, GTOMaterials.Quantanium))
                .add(11, new UnificationEntry(TagPrefix.plate, GTOMaterials.Adamantium))
                .add(12, new UnificationEntry(TagPrefix.plate, GTOMaterials.Vibranium))
                .add(13, new UnificationEntry(TagPrefix.plate, GTOMaterials.Draconium))
                .add(14, new UnificationEntry(TagPrefix.plate, GTOMaterials.Chaos));

        HULL_PLATE.add(0, new UnificationEntry(TagPrefix.plate, GTMaterials.WroughtIron))
                .add(1, new UnificationEntry(TagPrefix.plate, GTMaterials.Polyethylene))
                .add(2, new UnificationEntry(TagPrefix.plate, GTMaterials.Polyethylene))
                .add(3, new UnificationEntry(TagPrefix.plate, GTMaterials.PolyvinylChloride))
                .add(4, new UnificationEntry(TagPrefix.plate, GTMaterials.PolyvinylChloride))
                .add(5, new UnificationEntry(TagPrefix.plate, GTMaterials.Polytetrafluoroethylene))
                .add(6, new UnificationEntry(TagPrefix.plate, GTMaterials.Polytetrafluoroethylene))
                .add(7, new UnificationEntry(TagPrefix.plate, GTMaterials.Polybenzimidazole))
                .add(8, new UnificationEntry(TagPrefix.plate, GTMaterials.Polybenzimidazole))
                .add(9, new UnificationEntry(TagPrefix.plate, GTOMaterials.Polyetheretherketone))
                .add(10, new UnificationEntry(TagPrefix.plate, GTOMaterials.Polyetheretherketone))
                .add(11, new UnificationEntry(TagPrefix.plate, GTOMaterials.Zylon))
                .add(12, new UnificationEntry(TagPrefix.plate, GTOMaterials.Zylon))
                .add(13, new UnificationEntry(TagPrefix.plate, GTOMaterials.FullerenePolymerMatrixPulp))
                .add(14, new UnificationEntry(TagPrefix.plate, GTOMaterials.Radox));

        ROTOR.add(9, new UnificationEntry(TagPrefix.rotor, GTMaterials.Neutronium))
                .add(10, new UnificationEntry(TagPrefix.rotor, GTOMaterials.Quantanium))
                .add(11, new UnificationEntry(TagPrefix.rotor, GTOMaterials.Adamantium))
                .add(12, new UnificationEntry(TagPrefix.rotor, GTOMaterials.Vibranium))
                .add(13, new UnificationEntry(TagPrefix.rotor, GTOMaterials.Draconium))
                .add(14, new UnificationEntry(TagPrefix.rotor, GTOMaterials.TranscendentMetal));

        COIL_HEATING.add(9, new UnificationEntry(TagPrefix.wireGtDouble, GTOMaterials.AbyssalAlloy))
                .add(10, new UnificationEntry(TagPrefix.wireGtDouble, GTOMaterials.TitanSteel))
                .add(11, new UnificationEntry(TagPrefix.wireGtDouble, GTOMaterials.Adamantine))
                .add(12, new UnificationEntry(TagPrefix.wireGtDouble,
                        GTOMaterials.NaquadriaticTaranium))
                .add(13, new UnificationEntry(TagPrefix.wireGtDouble, GTOMaterials.Starmetal))
                .add(14, new UnificationEntry(TagPrefix.wireGtDouble, GTOMaterials.Hypogen));

        COIL_HEATING_DOUBLE.add(9, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.AbyssalAlloy))
                .add(10, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.TitanSteel))
                .add(11, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Adamantine))
                .add(12, new UnificationEntry(TagPrefix.wireGtQuadruple,
                        GTOMaterials.NaquadriaticTaranium))
                .add(13, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Starmetal))
                .add(14, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Hypogen));

        COIL_ELECTRIC.add(9, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.Mithril))
                .add(10, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.Mithril))
                .add(11, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.Mithril))
                .add(12, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.Mithril))
                .add(13, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.CrystalMatrix))
                .add(14, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.CrystalMatrix));

        STICK_DISTILLATION.add(9, new UnificationEntry(TagPrefix.spring, GTMaterials.Europium))
                .add(10, new UnificationEntry(TagPrefix.spring, GTOMaterials.Mithril))
                .add(11, new UnificationEntry(TagPrefix.spring, GTMaterials.Neutronium))
                .add(12, new UnificationEntry(TagPrefix.spring, GTOMaterials.Taranium))
                .add(13, new UnificationEntry(TagPrefix.spring, GTOMaterials.CrystalMatrix))
                .add(14, new UnificationEntry(TagPrefix.spring, GTOMaterials.CosmicNeutronium));

        STICK_ELECTROMAGNETIC.add(5, new UnificationEntry(TagPrefix.rod, GTMaterials.VanadiumGallium))
                .add(6, new UnificationEntry(TagPrefix.rod, GTMaterials.VanadiumGallium))
                .add(7, new UnificationEntry(TagPrefix.rod, GTMaterials.NiobiumTitanium))
                .add(8, new UnificationEntry(TagPrefix.rod, GTMaterials.NiobiumTitanium))
                .add(9, GTOItems.NETHERITE_ROD.asStack())
                .add(10, GTOItems.NETHERITE_ROD.asStack())
                .add(11, new UnificationEntry(TagPrefix.rod, GTOMaterials.Mithril))
                .add(12, new UnificationEntry(TagPrefix.rod, GTOMaterials.Mithril))
                .add(13, new UnificationEntry(TagPrefix.rod, GTOMaterials.Echoite))
                .add(14, new UnificationEntry(TagPrefix.rod, GTOMaterials.Echoite));

        PIPE_REACTOR.add(9, new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Polybenzimidazole))
                .add(10, new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Polybenzimidazole))
                .add(11, new UnificationEntry(TagPrefix.pipeHugeFluid, GTMaterials.Polybenzimidazole))
                .add(12, new UnificationEntry(TagPrefix.pipeNormalFluid,
                        GTOMaterials.FullerenePolymerMatrixPulp))
                .add(13, new UnificationEntry(TagPrefix.pipeLargeFluid,
                        GTOMaterials.FullerenePolymerMatrixPulp))
                .add(14, new UnificationEntry(TagPrefix.pipeHugeFluid,
                        GTOMaterials.FullerenePolymerMatrixPulp));

        POWER_COMPONENT.add(10, GTOItems.NM_CHIP.asStack())
                .add(11, GTOItems.PM_CHIP.asStack())
                .add(12, GTOItems.PM_CHIP.asStack())
                .add(13, GTOItems.FM_CHIP.asStack())
                .add(14, GTOItems.FM_CHIP.asStack());

        VOLTAGE_COIL.add(9, GTOItems.UHV_VOLTAGE_COIL.asStack())
                .add(10, GTOItems.UEV_VOLTAGE_COIL.asStack())
                .add(11, GTOItems.UIV_VOLTAGE_COIL.asStack())
                .add(12, GTOItems.UXV_VOLTAGE_COIL.asStack())
                .add(13, GTOItems.OPV_VOLTAGE_COIL.asStack())
                .add(14, GTOItems.MAX_VOLTAGE_COIL.asStack());

        SPRING.add(10, new UnificationEntry(TagPrefix.spring, GTOMaterials.Mithril))
                .add(11, new UnificationEntry(TagPrefix.spring, GTMaterials.Neutronium))
                .add(12, new UnificationEntry(TagPrefix.spring, GTOMaterials.Taranium))
                .add(13, new UnificationEntry(TagPrefix.spring, GTOMaterials.CrystalMatrix))
                .add(14, new UnificationEntry(TagPrefix.spring, GTOMaterials.CosmicNeutronium));

        CRATE.add(9, GTMachines.SUPER_CHEST[2].asStack())
                .add(10, GTMachines.SUPER_CHEST[3].asStack())
                .add(11, GTMachines.SUPER_CHEST[4].asStack())
                .add(12, GTMachines.QUANTUM_CHEST[5].asStack())
                .add(13, GTMachines.QUANTUM_CHEST[6].asStack())
                .add(14, GTMachines.QUANTUM_CHEST[7].asStack());

        DRUM.add(9, GTMachines.SUPER_TANK[2].asStack())
                .add(10, GTMachines.SUPER_TANK[3].asStack())
                .add(11, GTMachines.SUPER_TANK[4].asStack())
                .add(12, GTMachines.QUANTUM_TANK[5].asStack())
                .add(13, GTMachines.QUANTUM_TANK[6].asStack())
                .add(14, GTMachines.QUANTUM_TANK[7].asStack());

        FRAME.add(9, new UnificationEntry(TagPrefix.frameGt, GTMaterials.Tritanium))
                .add(10, new UnificationEntry(TagPrefix.frameGt, GTMaterials.Neutronium))
                .add(11, new UnificationEntry(TagPrefix.frameGt, GTOMaterials.Quantanium))
                .add(12, new UnificationEntry(TagPrefix.frameGt, GTOMaterials.Adamantium))
                .add(13, new UnificationEntry(TagPrefix.frameGt, GTOMaterials.Draconium))
                .add(14, new UnificationEntry(TagPrefix.frameGt, GTOMaterials.Infinity));

        BUFFER = new CraftingComponent(GTMachines.BUFFER[1].asStack())
                .add(1, GTMachines.BUFFER[1].asStack())
                .add(2, GTMachines.BUFFER[2].asStack())
                .add(3, GTMachines.BUFFER[3].asStack())
                .add(4, GTMachines.BUFFER[4].asStack())
                .add(5, GTMachines.BUFFER[5].asStack())
                .add(6, GTMachines.BUFFER[6].asStack())
                .add(7, GTMachines.BUFFER[7].asStack())
                .add(8, GTMachines.BUFFER[8].asStack())
                .add(9, GTMachines.BUFFER[9].asStack())
                .add(10, GTMachines.BUFFER[10].asStack())
                .add(11, GTMachines.BUFFER[11].asStack())
                .add(12, GTMachines.BUFFER[12].asStack())
                .add(13, GTMachines.BUFFER[13].asStack())
                .add(14, GTMachines.BUFFER[14].asStack());

        FLUID_REGULATOR = new CraftingComponent(GTItems.FLUID_REGULATOR_LV.asStack())
                .add(1, GTItems.FLUID_REGULATOR_LV.asStack())
                .add(2, GTItems.FLUID_REGULATOR_MV.asStack())
                .add(3, GTItems.FLUID_REGULATOR_HV.asStack())
                .add(4, GTItems.FLUID_REGULATOR_EV.asStack())
                .add(5, GTItems.FLUID_REGULATOR_IV.asStack())
                .add(6, GTItems.FLUID_REGULATOR_LuV.asStack())
                .add(7, GTItems.FLUID_REGULATOR_ZPM.asStack())
                .add(8, GTItems.FLUID_REGULATOR_UV.asStack())
                .add(9, GTItems.FLUID_REGULATOR_UHV.asStack())
                .add(10, GTItems.FLUID_REGULATOR_UEV.asStack())
                .add(11, GTItems.FLUID_REGULATOR_UIV.asStack())
                .add(12, GTItems.FLUID_REGULATOR_UXV.asStack())
                .add(13, GTItems.FLUID_REGULATOR_OpV.asStack());
    }
}

package com.gtocore.data;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
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
    public static CraftingComponent INTEGRATED_CONTROL_CORE;

    public static void init() {
        PUMP.add(14, GTOItems.MAX_ELECTRIC_PUMP.asItem());

        CONVEYOR.add(14, GTOItems.MAX_CONVEYOR_MODULE.asItem());

        MOTOR.add(14, GTOItems.MAX_ELECTRIC_MOTOR.asItem());

        PISTON.add(14, GTOItems.MAX_ELECTRIC_PISTON.asItem());

        EMITTER.add(14, GTOItems.MAX_EMITTER.asItem());

        SENSOR.add(14, GTOItems.MAX_SENSOR.asItem());

        FIELD_GENERATOR.add(14, GTOItems.MAX_FIELD_GENERATOR.asItem());

        ROBOT_ARM.add(14, GTOItems.MAX_ROBOT_ARM.asItem());

        GRINDER.add(5, GTItems.COMPONENT_GRINDER_DIAMOND.asItem())
                .add(6, GTItems.COMPONENT_GRINDER_DIAMOND.asItem())
                .add(7, GTItems.COMPONENT_GRINDER_TUNGSTEN.asItem())
                .add(8, GTItems.COMPONENT_GRINDER_TUNGSTEN.asItem())
                .add(9, GTItems.COMPONENT_GRINDER_TUNGSTEN.asItem())
                .add(10, GTItems.COMPONENT_GRINDER_TUNGSTEN.asItem())
                .add(11, GTItems.COMPONENT_GRINDER_TUNGSTEN.asItem())
                .add(12, GTOItems.BEDROCK_DRILL.asItem())
                .add(13, GTOItems.BEDROCK_DRILL.asItem());

        SAWBLADE.add(3, new MaterialEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.VanadiumSteel))
                .add(4, new MaterialEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.TungstenSteel))
                .add(5, new MaterialEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.TungstenSteel))
                .add(6, new MaterialEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.HSSE))
                .add(7, new MaterialEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.HSSE))
                .add(8, new MaterialEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.NaquadahAlloy))
                .add(9, new MaterialEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.NaquadahAlloy))
                .add(10, new MaterialEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.Duranium))
                .add(11, new MaterialEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.Duranium))
                .add(12, new MaterialEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.Neutronium))
                .add(13, new MaterialEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.Neutronium));

        WIRE_ELECTRIC.add(10, new MaterialEntry(TagPrefix.wireGtSingle, GTMaterials.Mendelevium))
                .add(11, new MaterialEntry(TagPrefix.wireGtSingle, GTMaterials.Mendelevium))
                .add(12, new MaterialEntry(TagPrefix.wireGtSingle, GTMaterials.Mendelevium))
                .add(13, new MaterialEntry(TagPrefix.wireGtSingle, GTOMaterials.Uruium))
                .add(14, new MaterialEntry(TagPrefix.wireGtSingle, GTOMaterials.Uruium));

        WIRE_QUAD.add(10, new MaterialEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Mithril))
                .add(11, new MaterialEntry(TagPrefix.wireGtQuadruple, GTMaterials.Neutronium))
                .add(12, new MaterialEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Taranium))
                .add(13, new MaterialEntry(TagPrefix.wireGtQuadruple, GTOMaterials.CrystalMatrix))
                .add(14, new MaterialEntry(TagPrefix.wireGtQuadruple, GTOMaterials.CosmicNeutronium));

        WIRE_OCT.add(10, new MaterialEntry(TagPrefix.wireGtOctal, GTOMaterials.Mithril))
                .add(11, new MaterialEntry(TagPrefix.wireGtOctal, GTMaterials.Neutronium))
                .add(12, new MaterialEntry(TagPrefix.wireGtOctal, GTOMaterials.Taranium))
                .add(13, new MaterialEntry(TagPrefix.wireGtOctal, GTOMaterials.CrystalMatrix))
                .add(14, new MaterialEntry(TagPrefix.wireGtOctal, GTOMaterials.CosmicNeutronium));

        WIRE_HEX.add(10, new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.Mithril))
                .add(11, new MaterialEntry(TagPrefix.wireGtHex, GTMaterials.Neutronium))
                .add(12, new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.Taranium))
                .add(13, new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.CrystalMatrix))
                .add(14, new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.CosmicNeutronium));

        CABLE.add(10, new MaterialEntry(TagPrefix.cableGtSingle, GTOMaterials.Mithril))
                .add(11, new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Neutronium))
                .add(12, new MaterialEntry(TagPrefix.cableGtSingle, GTOMaterials.Taranium))
                .add(13, new MaterialEntry(TagPrefix.cableGtSingle, GTOMaterials.CrystalMatrix))
                .add(14, new MaterialEntry(TagPrefix.cableGtSingle, GTOMaterials.CosmicNeutronium));

        CABLE_DOUBLE.add(10, new MaterialEntry(TagPrefix.cableGtDouble, GTOMaterials.Mithril))
                .add(11, new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.Neutronium))
                .add(12, new MaterialEntry(TagPrefix.cableGtDouble, GTOMaterials.Taranium))
                .add(13, new MaterialEntry(TagPrefix.cableGtDouble, GTOMaterials.CrystalMatrix))
                .add(14, new MaterialEntry(TagPrefix.cableGtDouble, GTOMaterials.CosmicNeutronium));

        CABLE_QUAD.add(10, new MaterialEntry(TagPrefix.cableGtDouble, GTOMaterials.Mithril))
                .add(11, new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.Neutronium))
                .add(12, new MaterialEntry(TagPrefix.cableGtDouble, GTOMaterials.Taranium))
                .add(13, new MaterialEntry(TagPrefix.cableGtDouble, GTOMaterials.CrystalMatrix))
                .add(14, new MaterialEntry(TagPrefix.cableGtDouble, GTOMaterials.CosmicNeutronium));

        WIRE_ELECTRIC.add(10, new MaterialEntry(TagPrefix.cableGtQuadruple, GTOMaterials.Mithril))
                .add(11, new MaterialEntry(TagPrefix.cableGtQuadruple, GTMaterials.Neutronium))
                .add(12, new MaterialEntry(TagPrefix.cableGtQuadruple, GTOMaterials.Taranium))
                .add(13, new MaterialEntry(TagPrefix.cableGtQuadruple, GTOMaterials.CrystalMatrix))
                .add(14, new MaterialEntry(TagPrefix.cableGtQuadruple, GTOMaterials.CosmicNeutronium));

        CABLE_OCT.add(10, new MaterialEntry(TagPrefix.cableGtOctal, GTOMaterials.Mithril))
                .add(11, new MaterialEntry(TagPrefix.cableGtOctal, GTMaterials.Neutronium))
                .add(12, new MaterialEntry(TagPrefix.cableGtOctal, GTOMaterials.Taranium))
                .add(13, new MaterialEntry(TagPrefix.cableGtOctal, GTOMaterials.CrystalMatrix))
                .add(14, new MaterialEntry(TagPrefix.cableGtOctal, GTOMaterials.CosmicNeutronium));

        CABLE_HEX.add(10, new MaterialEntry(TagPrefix.cableGtHex, GTOMaterials.Mithril))
                .add(11, new MaterialEntry(TagPrefix.cableGtHex, GTMaterials.Neutronium))
                .add(12, new MaterialEntry(TagPrefix.cableGtHex, GTOMaterials.Taranium))
                .add(13, new MaterialEntry(TagPrefix.cableGtHex, GTOMaterials.CrystalMatrix))
                .add(14, new MaterialEntry(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium));

        CABLE_TIER_UP.add(9, new MaterialEntry(TagPrefix.cableGtSingle, GTOMaterials.Mithril))
                .add(10, new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Neutronium))
                .add(11, new MaterialEntry(TagPrefix.cableGtSingle, GTOMaterials.Taranium))
                .add(12, new MaterialEntry(TagPrefix.cableGtSingle, GTOMaterials.CrystalMatrix))
                .add(13, new MaterialEntry(TagPrefix.cableGtSingle, GTOMaterials.CosmicNeutronium))
                .add(14, new MaterialEntry(TagPrefix.wireGtSingle, GTOMaterials.SpaceTime));

        CABLE_TIER_UP_DOUBLE.add(9, new MaterialEntry(TagPrefix.cableGtDouble, GTOMaterials.Mithril))
                .add(10, new MaterialEntry(TagPrefix.cableGtDouble, GTMaterials.Neutronium))
                .add(11, new MaterialEntry(TagPrefix.cableGtDouble, GTOMaterials.Taranium))
                .add(12, new MaterialEntry(TagPrefix.cableGtDouble, GTOMaterials.CrystalMatrix))
                .add(13, new MaterialEntry(TagPrefix.cableGtDouble, GTOMaterials.CosmicNeutronium))
                .add(14, new MaterialEntry(TagPrefix.wireGtDouble, GTOMaterials.SpaceTime));

        CABLE_TIER_UP_QUAD.add(9, new MaterialEntry(TagPrefix.cableGtQuadruple, GTOMaterials.Mithril))
                .add(10, new MaterialEntry(TagPrefix.cableGtQuadruple, GTMaterials.Neutronium))
                .add(11, new MaterialEntry(TagPrefix.cableGtQuadruple, GTOMaterials.Taranium))
                .add(12, new MaterialEntry(TagPrefix.cableGtQuadruple, GTOMaterials.CrystalMatrix))
                .add(13, new MaterialEntry(TagPrefix.cableGtQuadruple, GTOMaterials.CosmicNeutronium))
                .add(14, new MaterialEntry(TagPrefix.wireGtQuadruple, GTOMaterials.SpaceTime));

        CABLE_TIER_UP_OCT.add(9, new MaterialEntry(TagPrefix.cableGtOctal, GTOMaterials.Mithril))
                .add(10, new MaterialEntry(TagPrefix.cableGtOctal, GTMaterials.Neutronium))
                .add(11, new MaterialEntry(TagPrefix.cableGtOctal, GTOMaterials.Taranium))
                .add(12, new MaterialEntry(TagPrefix.cableGtOctal, GTOMaterials.CrystalMatrix))
                .add(13, new MaterialEntry(TagPrefix.cableGtOctal, GTOMaterials.CosmicNeutronium))
                .add(14, new MaterialEntry(TagPrefix.wireGtOctal, GTOMaterials.SpaceTime));

        CABLE_TIER_UP_HEX.add(9, new MaterialEntry(TagPrefix.cableGtHex, GTOMaterials.Mithril))
                .add(10, new MaterialEntry(TagPrefix.cableGtHex, GTMaterials.Neutronium))
                .add(11, new MaterialEntry(TagPrefix.cableGtHex, GTOMaterials.Taranium))
                .add(12, new MaterialEntry(TagPrefix.cableGtHex, GTOMaterials.CrystalMatrix))
                .add(13, new MaterialEntry(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium))
                .add(14, new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.SpaceTime));

        PIPE_NORMAL.add(9, new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Neutronium))
                .add(10, new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Neutronium))
                .add(11, new MaterialEntry(TagPrefix.pipeNormalFluid, GTOMaterials.Enderium))
                .add(12, new MaterialEntry(TagPrefix.pipeNormalFluid, GTOMaterials.Enderium))
                .add(13, new MaterialEntry(TagPrefix.pipeNormalFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter))
                .add(14, new MaterialEntry(TagPrefix.pipeNormalFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter));

        PIPE_LARGE.add(7, new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Iridium))
                .add(9, new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Neutronium))
                .add(10, new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Neutronium))
                .add(11, new MaterialEntry(TagPrefix.pipeLargeFluid, GTOMaterials.Enderium))
                .add(12, new MaterialEntry(TagPrefix.pipeLargeFluid, GTOMaterials.Enderium))
                .add(13, new MaterialEntry(TagPrefix.pipeLargeFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter))
                .add(14, new MaterialEntry(TagPrefix.pipeLargeFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter));

        PIPE_NONUPLE.add(0, new MaterialEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Bronze))
                .add(1, new MaterialEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Bronze))
                .add(2, new MaterialEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Steel))
                .add(3, new MaterialEntry(TagPrefix.pipeNonupleFluid, GTMaterials.StainlessSteel))
                .add(9, new MaterialEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Neutronium))
                .add(10, new MaterialEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Neutronium))
                .add(11, new MaterialEntry(TagPrefix.pipeNonupleFluid, GTOMaterials.Enderium))
                .add(12, new MaterialEntry(TagPrefix.pipeNonupleFluid, GTOMaterials.Enderium))
                .add(13, new MaterialEntry(TagPrefix.pipeNonupleFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter))
                .add(14, new MaterialEntry(TagPrefix.pipeNonupleFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter));

        GLASS.add(UHV, GTBlocks.FUSION_GLASS.asItem())
                .add(UEV, GTBlocks.FUSION_GLASS.asItem())
                .add(UIV, GTOBlocks.FORCE_FIELD_GLASS.asItem())
                .add(UXV, GTOBlocks.FORCE_FIELD_GLASS.asItem())
                .add(OpV, GTOBlocks.FORCE_FIELD_GLASS.asItem())
                .add(MAX, GTOBlocks.FORCE_FIELD_GLASS.asItem());

        PLATE.add(10, new MaterialEntry(TagPrefix.plate, GTOMaterials.Quantanium))
                .add(11, new MaterialEntry(TagPrefix.plate, GTOMaterials.Adamantium))
                .add(12, new MaterialEntry(TagPrefix.plate, GTOMaterials.Vibranium))
                .add(13, new MaterialEntry(TagPrefix.plate, GTOMaterials.Draconium))
                .add(14, new MaterialEntry(TagPrefix.plate, GTOMaterials.ChaosInfinityAlloy));

        HULL_PLATE.add(0, new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron))
                .add(1, new MaterialEntry(TagPrefix.plate, GTMaterials.Polyethylene))
                .add(2, new MaterialEntry(TagPrefix.plate, GTMaterials.Polyethylene))
                .add(3, new MaterialEntry(TagPrefix.plate, GTMaterials.PolyvinylChloride))
                .add(4, new MaterialEntry(TagPrefix.plate, GTMaterials.PolyvinylChloride))
                .add(5, new MaterialEntry(TagPrefix.plate, GTMaterials.Polytetrafluoroethylene))
                .add(6, new MaterialEntry(TagPrefix.plate, GTMaterials.Polytetrafluoroethylene))
                .add(7, new MaterialEntry(TagPrefix.plate, GTMaterials.Polybenzimidazole))
                .add(8, new MaterialEntry(TagPrefix.plate, GTMaterials.Polybenzimidazole))
                .add(9, new MaterialEntry(TagPrefix.plate, GTOMaterials.Polyetheretherketone))
                .add(10, new MaterialEntry(TagPrefix.plate, GTOMaterials.Polyetheretherketone))
                .add(11, new MaterialEntry(TagPrefix.plate, GTOMaterials.Zylon))
                .add(12, new MaterialEntry(TagPrefix.plate, GTOMaterials.Zylon))
                .add(13, new MaterialEntry(TagPrefix.plate, GTOMaterials.FullerenePolymerMatrixPulp))
                .add(14, new MaterialEntry(TagPrefix.plate, GTOMaterials.Radox));

        ROTOR.add(9, new MaterialEntry(TagPrefix.rotor, GTMaterials.Neutronium))
                .add(10, new MaterialEntry(TagPrefix.rotor, GTOMaterials.Quantanium))
                .add(11, new MaterialEntry(TagPrefix.rotor, GTOMaterials.Adamantium))
                .add(12, new MaterialEntry(TagPrefix.rotor, GTOMaterials.Vibranium))
                .add(13, new MaterialEntry(TagPrefix.rotor, GTOMaterials.Draconium))
                .add(14, new MaterialEntry(TagPrefix.rotor, GTOMaterials.TranscendentMetal));

        COIL_HEATING.add(9, new MaterialEntry(TagPrefix.wireGtDouble, GTOMaterials.AbyssalAlloy))
                .add(10, new MaterialEntry(TagPrefix.wireGtDouble, GTOMaterials.TitanSteel))
                .add(11, new MaterialEntry(TagPrefix.wireGtDouble, GTOMaterials.Adamantine))
                .add(12, new MaterialEntry(TagPrefix.wireGtDouble,
                        GTOMaterials.NaquadriaticTaranium))
                .add(13, new MaterialEntry(TagPrefix.wireGtDouble, GTOMaterials.Starmetal))
                .add(14, new MaterialEntry(TagPrefix.wireGtDouble, GTOMaterials.Hypogen));

        COIL_HEATING_DOUBLE.add(9, new MaterialEntry(TagPrefix.wireGtQuadruple, GTOMaterials.AbyssalAlloy))
                .add(10, new MaterialEntry(TagPrefix.wireGtQuadruple, GTOMaterials.TitanSteel))
                .add(11, new MaterialEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Adamantine))
                .add(12, new MaterialEntry(TagPrefix.wireGtQuadruple,
                        GTOMaterials.NaquadriaticTaranium))
                .add(13, new MaterialEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Starmetal))
                .add(14, new MaterialEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Hypogen));

        COIL_ELECTRIC.add(9, new MaterialEntry(TagPrefix.wireGtOctal, GTOMaterials.Mithril))
                .add(10, new MaterialEntry(TagPrefix.wireGtOctal, GTOMaterials.Mithril))
                .add(11, new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.Mithril))
                .add(12, new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.Mithril))
                .add(13, new MaterialEntry(TagPrefix.wireGtOctal, GTOMaterials.CrystalMatrix))
                .add(14, new MaterialEntry(TagPrefix.wireGtHex, GTOMaterials.CrystalMatrix));

        ROD_DISTILLATION.add(9, new MaterialEntry(TagPrefix.spring, GTMaterials.Europium))
                .add(10, new MaterialEntry(TagPrefix.spring, GTOMaterials.Mithril))
                .add(11, new MaterialEntry(TagPrefix.spring, GTMaterials.Neutronium))
                .add(12, new MaterialEntry(TagPrefix.spring, GTOMaterials.Taranium))
                .add(13, new MaterialEntry(TagPrefix.spring, GTOMaterials.CrystalMatrix))
                .add(14, new MaterialEntry(TagPrefix.spring, GTOMaterials.CosmicNeutronium));

        ROD_ELECTROMAGNETIC.add(5, new MaterialEntry(TagPrefix.rod, GTMaterials.VanadiumGallium))
                .add(6, new MaterialEntry(TagPrefix.rod, GTMaterials.VanadiumGallium))
                .add(7, new MaterialEntry(TagPrefix.rod, GTMaterials.NiobiumTitanium))
                .add(8, new MaterialEntry(TagPrefix.rod, GTMaterials.NiobiumTitanium))
                .add(9, new MaterialEntry(TagPrefix.rod, GTOMaterials.EnergeticNetherite))
                .add(10, new MaterialEntry(TagPrefix.rod, GTOMaterials.EnergeticNetherite))
                .add(11, new MaterialEntry(TagPrefix.rod, GTOMaterials.Mithril))
                .add(12, new MaterialEntry(TagPrefix.rod, GTOMaterials.Mithril))
                .add(13, new MaterialEntry(TagPrefix.rod, GTOMaterials.Echoite))
                .add(14, new MaterialEntry(TagPrefix.rod, GTOMaterials.Echoite));

        PIPE_REACTOR.add(9, new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Polybenzimidazole))
                .add(10, new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Polybenzimidazole))
                .add(11, new MaterialEntry(TagPrefix.pipeHugeFluid, GTMaterials.Polybenzimidazole))
                .add(12, new MaterialEntry(TagPrefix.pipeNormalFluid,
                        GTOMaterials.FullerenePolymerMatrixPulp))
                .add(13, new MaterialEntry(TagPrefix.pipeLargeFluid,
                        GTOMaterials.FullerenePolymerMatrixPulp))
                .add(14, new MaterialEntry(TagPrefix.pipeHugeFluid,
                        GTOMaterials.FullerenePolymerMatrixPulp));

        POWER_COMPONENT.add(10, GTOItems.NM_CHIP.asItem())
                .add(11, GTOItems.PM_CHIP.asItem())
                .add(12, GTOItems.PM_CHIP.asItem())
                .add(13, GTOItems.FM_CHIP.asItem())
                .add(14, GTOItems.FM_CHIP.asItem());

        VOLTAGE_COIL.add(9, GTOItems.UHV_VOLTAGE_COIL.asItem())
                .add(10, GTOItems.UEV_VOLTAGE_COIL.asItem())
                .add(11, GTOItems.UIV_VOLTAGE_COIL.asItem())
                .add(12, GTOItems.UXV_VOLTAGE_COIL.asItem())
                .add(13, GTOItems.OPV_VOLTAGE_COIL.asItem())
                .add(14, GTOItems.MAX_VOLTAGE_COIL.asItem());

        SPRING.add(10, new MaterialEntry(TagPrefix.spring, GTOMaterials.Mithril))
                .add(11, new MaterialEntry(TagPrefix.spring, GTMaterials.Neutronium))
                .add(12, new MaterialEntry(TagPrefix.spring, GTOMaterials.Taranium))
                .add(13, new MaterialEntry(TagPrefix.spring, GTOMaterials.CrystalMatrix))
                .add(14, new MaterialEntry(TagPrefix.spring, GTOMaterials.CosmicNeutronium));

        CRATE.add(9, GTMachines.SUPER_CHEST[2].asItem())
                .add(10, GTMachines.SUPER_CHEST[3].asItem())
                .add(11, GTMachines.SUPER_CHEST[4].asItem())
                .add(12, GTMachines.QUANTUM_CHEST[5].asItem())
                .add(13, GTMachines.QUANTUM_CHEST[6].asItem())
                .add(14, GTMachines.QUANTUM_CHEST[7].asItem());

        DRUM.add(9, GTMachines.SUPER_TANK[2].asItem())
                .add(10, GTMachines.SUPER_TANK[3].asItem())
                .add(11, GTMachines.SUPER_TANK[4].asItem())
                .add(12, GTMachines.QUANTUM_TANK[5].asItem())
                .add(13, GTMachines.QUANTUM_TANK[6].asItem())
                .add(14, GTMachines.QUANTUM_TANK[7].asItem());

        FRAME.add(9, new MaterialEntry(TagPrefix.frameGt, GTMaterials.Tritanium))
                .add(10, new MaterialEntry(TagPrefix.frameGt, GTMaterials.Neutronium))
                .add(11, new MaterialEntry(TagPrefix.frameGt, GTOMaterials.Quantanium))
                .add(12, new MaterialEntry(TagPrefix.frameGt, GTOMaterials.Adamantium))
                .add(13, new MaterialEntry(TagPrefix.frameGt, GTOMaterials.Draconium))
                .add(14, new MaterialEntry(TagPrefix.frameGt, GTOMaterials.Infinity));

        BUFFER = CraftingComponent.of("buffer", GTMachines.BUFFER[1].asItem())
                .add(1, GTMachines.BUFFER[1].asItem())
                .add(2, GTMachines.BUFFER[2].asItem())
                .add(3, GTMachines.BUFFER[3].asItem())
                .add(4, GTMachines.BUFFER[4].asItem())
                .add(5, GTMachines.BUFFER[5].asItem())
                .add(6, GTMachines.BUFFER[6].asItem())
                .add(7, GTMachines.BUFFER[7].asItem())
                .add(8, GTMachines.BUFFER[8].asItem())
                .add(9, GTMachines.BUFFER[9].asItem())
                .add(10, GTMachines.BUFFER[10].asItem())
                .add(11, GTMachines.BUFFER[11].asItem())
                .add(12, GTMachines.BUFFER[12].asItem())
                .add(13, GTMachines.BUFFER[13].asItem())
                .add(14, GTMachines.BUFFER[14].asItem());

        FLUID_REGULATOR = CraftingComponent.of("fluid_regulator", GTItems.FLUID_REGULATOR_LV.asItem())
                .add(1, GTItems.FLUID_REGULATOR_LV.asItem())
                .add(2, GTItems.FLUID_REGULATOR_MV.asItem())
                .add(3, GTItems.FLUID_REGULATOR_HV.asItem())
                .add(4, GTItems.FLUID_REGULATOR_EV.asItem())
                .add(5, GTItems.FLUID_REGULATOR_IV.asItem())
                .add(6, GTItems.FLUID_REGULATOR_LuV.asItem())
                .add(7, GTItems.FLUID_REGULATOR_ZPM.asItem())
                .add(8, GTItems.FLUID_REGULATOR_UV.asItem())
                .add(9, GTItems.FLUID_REGULATOR_UHV.asItem())
                .add(10, GTItems.FLUID_REGULATOR_UEV.asItem())
                .add(11, GTItems.FLUID_REGULATOR_UIV.asItem())
                .add(12, GTItems.FLUID_REGULATOR_UXV.asItem())
                .add(13, GTItems.FLUID_REGULATOR_OpV.asItem());

        INTEGRATED_CONTROL_CORE = CraftingComponent.of("integrated_control_core", GTOItems.INTEGRATED_CONTROL_CORE_UV.asItem())
                .add(8, GTOItems.INTEGRATED_CONTROL_CORE_UV.asItem())
                .add(9, GTOItems.INTEGRATED_CONTROL_CORE_UHV.asItem())
                .add(10, GTOItems.INTEGRATED_CONTROL_CORE_UEV.asItem())
                .add(11, GTOItems.INTEGRATED_CONTROL_CORE_UIV.asItem())
                .add(12, GTOItems.INTEGRATED_CONTROL_CORE_UXV.asItem())
                .add(13, GTOItems.INTEGRATED_CONTROL_CORE_OpV.asItem())
                .add(14, GTOItems.INTEGRATED_CONTROL_CORE_MAX.asItem());
    }
}

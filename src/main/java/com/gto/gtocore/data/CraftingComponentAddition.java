package com.gto.gtocore.data;

import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CraftingComponent;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.data.recipe.CraftingComponent.*;

public final class CraftingComponentAddition {

    public static Component BUFFER;

    public static void init() {
        CraftingComponent.PUMP.appendIngredients(Stream.of(new Object[][] {
                { 14, GTOItems.ELECTRIC_PUMP_MAX.asStack() },
        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CraftingComponent.CONVEYOR.appendIngredients(Stream.of(new Object[][] {
                { 14, GTOItems.CONVEYOR_MODULE_MAX.asStack() },
        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CraftingComponent.MOTOR.appendIngredients(Stream.of(new Object[][] {
                { 14, GTOItems.ELECTRIC_MOTOR_MAX.asStack() },
        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CraftingComponent.PISTON.appendIngredients(Stream.of(new Object[][] {
                { 14, GTOItems.ELECTRIC_PISTON_MAX.asStack() },
        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CraftingComponent.EMITTER.appendIngredients(Stream.of(new Object[][] {
                { 14, GTOItems.EMITTER_MAX.asStack() },
        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CraftingComponent.SENSOR.appendIngredients(Stream.of(new Object[][] {
                { 14, GTOItems.SENSOR_MAX.asStack() },
        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CraftingComponent.FIELD_GENERATOR.appendIngredients(Stream.of(new Object[][] {
                { 14, GTOItems.FIELD_GENERATOR_MAX.asStack() },
        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CraftingComponent.ROBOT_ARM.appendIngredients(Stream.of(new Object[][] {
                { 14, GTOItems.ROBOT_ARM_MAX.asStack() },
        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        WIRE_ELECTRIC.appendIngredients(Stream.of(new Object[][] {
                { 10, new UnificationEntry(TagPrefix.wireGtSingle, GTMaterials.Mendelevium) },
                { 11, new UnificationEntry(TagPrefix.wireGtSingle, GTMaterials.Mendelevium) },
                { 12, new UnificationEntry(TagPrefix.wireGtSingle, GTMaterials.Mendelevium) },
                { 13, new UnificationEntry(TagPrefix.wireGtSingle, GTOMaterials.Uruium) },
                { 14, new UnificationEntry(TagPrefix.wireGtSingle, GTOMaterials.Uruium) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        WIRE_QUAD.appendIngredients(Stream.of(new Object[][] {
                { 10, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Mithril) },
                { 11, new UnificationEntry(TagPrefix.wireGtQuadruple, GTMaterials.Neutronium) },
                { 12, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Taranium) },
                { 13, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.CrystalMatrix) },
                { 14, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.CosmicNeutronium) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        WIRE_OCT.appendIngredients(Stream.of(new Object[][] {
                { 10, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.Mithril) },
                { 11, new UnificationEntry(TagPrefix.wireGtOctal, GTMaterials.Neutronium) },
                { 12, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.Taranium) },
                { 13, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.CrystalMatrix) },
                { 14, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.CosmicNeutronium) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        WIRE_HEX.appendIngredients(Stream.of(new Object[][] {
                { 10, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.Mithril) },
                { 11, new UnificationEntry(TagPrefix.wireGtHex, GTMaterials.Neutronium) },
                { 12, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.Taranium) },
                { 13, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.CrystalMatrix) },
                { 14, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.CosmicNeutronium) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CABLE.appendIngredients(Stream.of(new Object[][] {
                { 10, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.Mithril) },
                { 11, new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Neutronium) },
                { 12, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.Taranium) },
                { 13, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.CrystalMatrix) },
                { 14, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.CosmicNeutronium) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CABLE_DOUBLE.appendIngredients(Stream.of(new Object[][] {
                { 10, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.Mithril) },
                { 11, new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Neutronium) },
                { 12, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.Taranium) },
                { 13, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.CrystalMatrix) },
                { 14, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.CosmicNeutronium) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CABLE_QUAD.appendIngredients(Stream.of(new Object[][] {
                { 10, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.Mithril) },
                { 11, new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Neutronium) },
                { 12, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.Taranium) },
                { 13, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.CrystalMatrix) },
                { 14, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.CosmicNeutronium) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        WIRE_ELECTRIC.appendIngredients(Stream.of(new Object[][] {
                { 10, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.Mithril) },
                { 11, new UnificationEntry(TagPrefix.cableGtQuadruple, GTMaterials.Neutronium) },
                { 12, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.Taranium) },
                { 13, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.CrystalMatrix) },
                { 14, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.CosmicNeutronium) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CABLE_OCT.appendIngredients(Stream.of(new Object[][] {
                { 10, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.Mithril) },
                { 11, new UnificationEntry(TagPrefix.cableGtOctal, GTMaterials.Neutronium) },
                { 12, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.Taranium) },
                { 13, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.CrystalMatrix) },
                { 14, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.CosmicNeutronium) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CABLE_HEX.appendIngredients(Stream.of(new Object[][] {
                { 10, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.Mithril) },
                { 11, new UnificationEntry(TagPrefix.cableGtHex, GTMaterials.Neutronium) },
                { 12, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.Taranium) },
                { 13, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.CrystalMatrix) },
                { 14, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CABLE_TIER_UP.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.Mithril) },
                { 10, new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Neutronium) },
                { 11, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.Taranium) },
                { 12, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.CrystalMatrix) },
                { 13, new UnificationEntry(TagPrefix.cableGtSingle, GTOMaterials.CosmicNeutronium) },
                { 14, new UnificationEntry(TagPrefix.wireGtSingle, GTOMaterials.SpaceTime) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CABLE_TIER_UP_DOUBLE.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.Mithril) },
                { 10, new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Neutronium) },
                { 11, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.Taranium) },
                { 12, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.CrystalMatrix) },
                { 13, new UnificationEntry(TagPrefix.cableGtDouble, GTOMaterials.CosmicNeutronium) },
                { 14, new UnificationEntry(TagPrefix.wireGtDouble, GTOMaterials.SpaceTime) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CABLE_TIER_UP_QUAD.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.Mithril) },
                { 10, new UnificationEntry(TagPrefix.cableGtQuadruple, GTMaterials.Neutronium) },
                { 11, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.Taranium) },
                { 12, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.CrystalMatrix) },
                { 13, new UnificationEntry(TagPrefix.cableGtQuadruple, GTOMaterials.CosmicNeutronium) },
                { 14, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.SpaceTime) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CABLE_TIER_UP_OCT.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.Mithril) },
                { 10, new UnificationEntry(TagPrefix.cableGtOctal, GTMaterials.Neutronium) },
                { 11, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.Taranium) },
                { 12, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.CrystalMatrix) },
                { 13, new UnificationEntry(TagPrefix.cableGtOctal, GTOMaterials.CosmicNeutronium) },
                { 14, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.SpaceTime) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        CABLE_TIER_UP_HEX.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.Mithril) },
                { 10, new UnificationEntry(TagPrefix.cableGtHex, GTMaterials.Neutronium) },
                { 11, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.Taranium) },
                { 12, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.CrystalMatrix) },
                { 13, new UnificationEntry(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium) },
                { 14, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.SpaceTime) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        PIPE_NORMAL.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Neutronium) },
                { 10, new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Neutronium) },
                { 11, new UnificationEntry(TagPrefix.pipeNormalFluid, GTOMaterials.Enderium) },
                { 12, new UnificationEntry(TagPrefix.pipeNormalFluid, GTOMaterials.Enderium) },
                { 13, new UnificationEntry(TagPrefix.pipeNormalFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter) },
                { 14, new UnificationEntry(TagPrefix.pipeNormalFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        PIPE_LARGE.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Neutronium) },
                { 10, new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Neutronium) },
                { 11, new UnificationEntry(TagPrefix.pipeLargeFluid, GTOMaterials.Enderium) },
                { 12, new UnificationEntry(TagPrefix.pipeLargeFluid, GTOMaterials.Enderium) },
                { 13, new UnificationEntry(TagPrefix.pipeLargeFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter) },
                { 14, new UnificationEntry(TagPrefix.pipeLargeFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        PIPE_NONUPLE.appendIngredients(Stream.of(new Object[][] {
                { 0, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Bronze) },
                { 1, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Bronze) },
                { 2, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Steel) },
                { 3, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.StainlessSteel) },
                { 9, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Neutronium) },
                { 10, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Neutronium) },
                { 11, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTOMaterials.Enderium) },
                { 12, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTOMaterials.Enderium) },
                { 13, new UnificationEntry(TagPrefix.pipeNonupleFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter) },
                { 14, new UnificationEntry(TagPrefix.pipeNonupleFluid,
                        GTOMaterials.HeavyQuarkDegenerateMatter) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        GLASS.appendIngredients(Stream.of(new Object[][] {
                { UHV, GTBlocks.FUSION_GLASS.asStack() },
                { UEV, GTBlocks.FUSION_GLASS.asStack() },
                { UIV, GTOBlocks.FORCE_FIELD_GLASS.asStack() },
                { UXV, GTOBlocks.FORCE_FIELD_GLASS.asStack() },
                { OpV, GTOBlocks.FORCE_FIELD_GLASS.asStack() },
                { MAX, GTOBlocks.FORCE_FIELD_GLASS.asStack() },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        PLATE.appendIngredients(Stream.of(new Object[][] {
                { 10, new UnificationEntry(TagPrefix.plate, GTOMaterials.Quantanium) },
                { 11, new UnificationEntry(TagPrefix.plate, GTOMaterials.Adamantium) },
                { 12, new UnificationEntry(TagPrefix.plate, GTOMaterials.Vibranium) },
                { 13, new UnificationEntry(TagPrefix.plate, GTOMaterials.Draconium) },
                { 14, new UnificationEntry(TagPrefix.plate, GTOMaterials.Chaos) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        HULL_PLATE.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.plate, GTOMaterials.Polyetheretherketone) },
                { 10, new UnificationEntry(TagPrefix.plate, GTOMaterials.Polyetheretherketone) },
                { 11, new UnificationEntry(TagPrefix.plate, GTOMaterials.Zylon) },
                { 12, new UnificationEntry(TagPrefix.plate, GTOMaterials.Zylon) },
                { 13, new UnificationEntry(TagPrefix.plate,
                        GTOMaterials.FullerenePolymerMatrixPulp) },
                { 14, new UnificationEntry(TagPrefix.plate, GTOMaterials.Radox) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        ROTOR.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.rotor, GTMaterials.Neutronium) },
                { 10, new UnificationEntry(TagPrefix.rotor, GTOMaterials.Quantanium) },
                { 11, new UnificationEntry(TagPrefix.rotor, GTOMaterials.Adamantium) },
                { 12, new UnificationEntry(TagPrefix.rotor, GTOMaterials.Vibranium) },
                { 13, new UnificationEntry(TagPrefix.rotor, GTOMaterials.Draconium) },
                { 14, new UnificationEntry(TagPrefix.rotor, GTOMaterials.TranscendentMetal) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        COIL_HEATING.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.wireGtDouble, GTOMaterials.AbyssalAlloy) },
                { 10, new UnificationEntry(TagPrefix.wireGtDouble, GTOMaterials.TitanSteel) },
                { 11, new UnificationEntry(TagPrefix.wireGtDouble, GTOMaterials.Adamantine) },
                { 12, new UnificationEntry(TagPrefix.wireGtDouble,
                        GTOMaterials.NaquadriaticTaranium) },
                { 13, new UnificationEntry(TagPrefix.wireGtDouble, GTOMaterials.Starmetal) },
                { 14, new UnificationEntry(TagPrefix.wireGtDouble, GTOMaterials.Hypogen) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        COIL_HEATING_DOUBLE.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.AbyssalAlloy) },
                { 10, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.TitanSteel) },
                { 11, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Adamantine) },
                { 12, new UnificationEntry(TagPrefix.wireGtQuadruple,
                        GTOMaterials.NaquadriaticTaranium) },
                { 13, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Starmetal) },
                { 14, new UnificationEntry(TagPrefix.wireGtQuadruple, GTOMaterials.Hypogen) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        COIL_ELECTRIC.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.Mithril) },
                { 10, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.Mithril) },
                { 11, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.Mithril) },
                { 12, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.Mithril) },
                { 13, new UnificationEntry(TagPrefix.wireGtOctal, GTOMaterials.CrystalMatrix) },
                { 14, new UnificationEntry(TagPrefix.wireGtHex, GTOMaterials.CrystalMatrix) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        STICK_DISTILLATION.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.spring, GTMaterials.Europium) },
                { 10, new UnificationEntry(TagPrefix.spring, GTOMaterials.Mithril) },
                { 11, new UnificationEntry(TagPrefix.spring, GTMaterials.Neutronium) },
                { 12, new UnificationEntry(TagPrefix.spring, GTOMaterials.Taranium) },
                { 13, new UnificationEntry(TagPrefix.spring, GTOMaterials.CrystalMatrix) },
                { 14, new UnificationEntry(TagPrefix.spring, GTOMaterials.CosmicNeutronium) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        STICK_ELECTROMAGNETIC.appendIngredients(Stream.of(new Object[][] {
                { 5, new UnificationEntry(TagPrefix.rod, GTMaterials.VanadiumGallium) },
                { 6, new UnificationEntry(TagPrefix.rod, GTMaterials.VanadiumGallium) },
                { 7, new UnificationEntry(TagPrefix.rod, GTMaterials.NiobiumTitanium) },
                { 8, new UnificationEntry(TagPrefix.rod, GTMaterials.NiobiumTitanium) },
                { 9, GTOItems.NETHERITE_ROD.asStack() },
                { 10, GTOItems.NETHERITE_ROD.asStack() },
                { 11, new UnificationEntry(TagPrefix.rod, GTOMaterials.Mithril) },
                { 12, new UnificationEntry(TagPrefix.rod, GTOMaterials.Mithril) },
                { 13, new UnificationEntry(TagPrefix.rod, GTOMaterials.Echoite) },
                { 14, new UnificationEntry(TagPrefix.rod, GTOMaterials.Echoite) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        PIPE_REACTOR.appendIngredients(Stream.of(new Object[][] {
                { 9, new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Polybenzimidazole) },
                { 10, new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Polybenzimidazole) },
                { 11, new UnificationEntry(TagPrefix.pipeHugeFluid, GTMaterials.Polybenzimidazole) },
                { 12, new UnificationEntry(TagPrefix.pipeNormalFluid,
                        GTOMaterials.FullerenePolymerMatrixPulp) },
                { 13, new UnificationEntry(TagPrefix.pipeLargeFluid,
                        GTOMaterials.FullerenePolymerMatrixPulp) },
                { 14, new UnificationEntry(TagPrefix.pipeHugeFluid,
                        GTOMaterials.FullerenePolymerMatrixPulp) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        POWER_COMPONENT.appendIngredients(Stream.of(new Object[][] {
                { 10, GTOItems.NM_CHIP.asStack() },
                { 11, GTOItems.PM_CHIP.asStack() },
                { 12, GTOItems.PM_CHIP.asStack() },
                { 13, GTOItems.FM_CHIP.asStack() },
                { 14, GTOItems.FM_CHIP.asStack() },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        VOLTAGE_COIL.appendIngredients(Stream.of(new Object[][] {
                { 9, GTOItems.UHV_VOLTAGE_COIL.asStack() },
                { 10, GTOItems.UEV_VOLTAGE_COIL.asStack() },
                { 11, GTOItems.UIV_VOLTAGE_COIL.asStack() },
                { 12, GTOItems.UXV_VOLTAGE_COIL.asStack() },
                { 13, GTOItems.OPV_VOLTAGE_COIL.asStack() },
                { 14, GTOItems.MAX_VOLTAGE_COIL.asStack() },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        SPRING.appendIngredients(Stream.of(new Object[][] {
                { 10, new UnificationEntry(TagPrefix.spring, GTOMaterials.Mithril) },
                { 11, new UnificationEntry(TagPrefix.spring, GTMaterials.Neutronium) },
                { 12, new UnificationEntry(TagPrefix.spring, GTOMaterials.Taranium) },
                { 13, new UnificationEntry(TagPrefix.spring, GTOMaterials.CrystalMatrix) },
                { 14, new UnificationEntry(TagPrefix.spring, GTOMaterials.CosmicNeutronium) },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));

        BUFFER = new Component(Stream.of(new Object[][] {

                { 1, GTMachines.BUFFER[1].asStack() },
                { 2, GTMachines.BUFFER[2].asStack() },
                { 3, GTMachines.BUFFER[3].asStack() },
                { 4, GTMachines.BUFFER[4].asStack() },
                { 5, GTMachines.BUFFER[5].asStack() },
                { 6, GTMachines.BUFFER[6].asStack() },
                { 7, GTMachines.BUFFER[7].asStack() },
                { 8, GTMachines.BUFFER[8].asStack() },
                { 9, GTMachines.BUFFER[9].asStack() },
                { 10, GTMachines.BUFFER[10].asStack() },
                { 11, GTMachines.BUFFER[11].asStack() },
                { 12, GTMachines.BUFFER[12].asStack() },
                { 13, GTMachines.BUFFER[13].asStack() },
                { 14, GTMachines.BUFFER[14].asStack() },

        }).collect(Collectors.toMap(data -> (Integer) data[0], data -> data[1])));
    }
}

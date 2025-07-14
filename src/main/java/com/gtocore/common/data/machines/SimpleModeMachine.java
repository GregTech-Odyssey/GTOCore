package com.gtocore.common.data.machines;

import com.gtocore.common.machine.multiblock.part.ae.MESimplePatternBufferPartMachine;

import com.gtolib.api.renderer.machine.OverlayTieredMachineRenderer;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;

import static com.gregtechceu.gtceu.api.GTValues.MV;
import static com.gtolib.utils.register.MachineRegisterUtils.machine;

public final class SimpleModeMachine {

    public static void init() {}

    public static final MachineDefinition ME_SIMPLE_PATTERN_BUFFER = machine("me_simple_pattern_buffer", "ME简单样板总成", MESimplePatternBufferPartMachine::new)
            .langValue("ME Simple Pattern Buffer")
            .addTooltipsFromClass(MESimplePatternBufferPartMachine.class)
            .tier(MV)
            .allRotation()
            .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
            .tooltipsKey("gtceu.part_sharing.enabled")
            .renderer(() -> new OverlayTieredMachineRenderer(MV, GTCEu.id("block/machine/part/me_pattern_buffer")))
            .register();
}

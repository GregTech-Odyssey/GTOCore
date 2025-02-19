package com.gto.gtocore.api.machine.part;

import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;

public interface GTOPartAbility {

    PartAbility NEUTRON_ACCELERATOR = new PartAbility("neutron_accelerator");
    PartAbility THREAD_HATCH = new PartAbility("thread_hatch");
    PartAbility ACCELERATE_HATCH = new PartAbility("accelerate_hatch");
    PartAbility ITEMS_INPUT = new PartAbility("items_input");
    PartAbility ITEMS_OUTPUT = new PartAbility("items_output");
    PartAbility DRONE_HATCH = new PartAbility("drone_hatch");
    PartAbility INPUT_MANA = new PartAbility("input_mana");
    PartAbility OUTPUT_MANA = new PartAbility("output_mana");
    PartAbility EXTRACT_MANA = new PartAbility("extract_mana");
}

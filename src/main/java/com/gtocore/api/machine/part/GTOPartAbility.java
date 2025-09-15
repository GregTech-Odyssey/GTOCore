package com.gtocore.api.machine.part;

import com.gtocore.common.data.GTOMachines;

import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.common.data.GTMachines;

public final class GTOPartAbility {

    public static final PartAbility NEUTRON_ACCELERATOR = new PartAbility("neutron_accelerator");
    public static final PartAbility THREAD_HATCH = new PartAbility("thread_hatch");
    public static final PartAbility OVERCLOCK_HATCH = new PartAbility("overclock_hatch");
    public static final PartAbility ACCELERATE_HATCH = new PartAbility("accelerate_hatch");
    public static final PartAbility DRONE_HATCH = new PartAbility("drone_hatch");
    public static final PartAbility PASSTHROUGH_HATCH_MANA = new PartAbility("passthrogh_hatch_mana");
    public static final PartAbility INPUT_MANA = new PartAbility("input_mana");
    public static final PartAbility OUTPUT_MANA = new PartAbility("output_mana");
    public static final PartAbility EXTRACT_MANA = new PartAbility("extract_mana");
    public static final PartAbility COMPUTING_COMPONENT = new PartAbility("computing_component");
    public static final PartAbility CATALYST_HATCH = new PartAbility("catalyst_hatch");

    public static final PartAbility ITEMS_INPUT = new PartAbility("items_input");
    public static final PartAbility ITEMS_OUTPUT = new PartAbility("items_output");

    public static final PartAbility STEAM_IMPORT_FLUIDS = new PartAbility("steam_import_fluids");
    public static final PartAbility STEAM_EXPORT_FLUIDS = new PartAbility("steam_export_fluids");

    public static void init() {
        PartAbility.STEAM_IMPORT_ITEMS.register(2, GTMachines.ITEM_IMPORT_BUS[0].getBlock());
        PartAbility.STEAM_EXPORT_ITEMS.register(2, GTMachines.ITEM_EXPORT_BUS[0].getBlock());
        STEAM_IMPORT_FLUIDS.register(2, GTOMachines.INFINITE_INTAKE_HATCH.getBlock());
        for (var machine : GTMachines.ITEM_IMPORT_BUS) {
            if (machine != null) ITEMS_INPUT.register(machine.getTier(), machine.getBlock());
        }
        for (var machine : GTMachines.ITEM_EXPORT_BUS) {
            if (machine != null) ITEMS_OUTPUT.register(machine.getTier(), machine.getBlock());
        }
    }
}

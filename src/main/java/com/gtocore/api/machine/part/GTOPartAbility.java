package com.gtocore.api.machine.part;

import com.gtocore.common.data.GTOMachines;

import com.gtolib.api.lang.CNEN;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.common.data.GTMachines;

import com.fast.fastcollection.O2OOpenCacheHashMap;

import java.util.Map;

public final class GTOPartAbility extends PartAbility {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    public static final PartAbility NEUTRON_ACCELERATOR = new GTOPartAbility("neutron_accelerator", "中子加速器", "Neutron Accelerator");
    public static final PartAbility THREAD_HATCH = new GTOPartAbility("thread_hatch", "线程仓", "Thread Hatch");
    public static final PartAbility OVERCLOCK_HATCH = new GTOPartAbility("overclock_hatch", "超频仓", "Overclock Hatch");
    public static final PartAbility ACCELERATE_HATCH = new GTOPartAbility("accelerate_hatch", "加速仓", "Accelerate Hatch");
    public static final PartAbility DRONE_HATCH = new GTOPartAbility("drone_hatch", "无人机仓", "Drone Hatch");
    public static final PartAbility PASSTHROUGH_HATCH_MANA = new GTOPartAbility("passthrogh_hatch_mana", "魔力通行仓", "Mana Passthrough Hatch");
    public static final PartAbility INPUT_MANA = new GTOPartAbility("input_mana", "魔力输入仓", "Input Mana");
    public static final PartAbility OUTPUT_MANA = new GTOPartAbility("output_mana", "魔力输出仓", "Output Mana");
    public static final PartAbility EXTRACT_MANA = new GTOPartAbility("extract_mana", "魔力抽取仓", "Extract Mana");
    public static final PartAbility COMPUTING_COMPONENT = new GTOPartAbility("computing_component", "计算组件", "Computing Component Hatch");
    public static final PartAbility CATALYST_HATCH = new GTOPartAbility("catalyst_hatch", "催化剂仓", "Catalyst Hatch");

    public static final PartAbility ITEMS_INPUT = new GTOPartAbility("items_input", "物品输入仓", "Items Input");
    public static final PartAbility ITEMS_OUTPUT = new GTOPartAbility("items_output", "物品输出仓", "Items Output");

    public static final PartAbility STEAM_IMPORT_FLUIDS = new GTOPartAbility("steam_import_fluids", "蒸汽流体输入仓", "Steam Import Fluids");
    public static final PartAbility STEAM_EXPORT_FLUIDS = new GTOPartAbility("steam_export_fluids", "蒸汽流体输出仓", "Steam Export Fluids");

    public GTOPartAbility(String name, String cn, String en) {
        super(name);
        if (LANG != null) {
            LANG.put("gtocore.part_ability." + name, new CNEN(cn, en));
        }
    }

    static {
        if (LANG != null) {
            LANG.put("gtocore.part_ability.export_items", new CNEN("物品输出仓", "Items Output"));
            LANG.put("gtocore.part_ability.import_items", new CNEN("物品输入仓", "Items Input"));
            LANG.put("gtocore.part_ability.export_fluids", new CNEN("流体输出仓", "Fluids Output"));
            LANG.put("gtocore.part_ability.import_fluids", new CNEN("流体输入仓", "Fluids Input"));
            LANG.put("gtocore.part_ability.export_fluids_1x", new CNEN("流体输出仓(一重)", "Fluids Output (1x)"));
            LANG.put("gtocore.part_ability.import_fluids_1x", new CNEN("流体输入仓(一重)", "Fluids Input (1x)"));
            LANG.put("gtocore.part_ability.export_fluids_4x", new CNEN("流体输出仓(四重)", "Fluids Output (4x)"));
            LANG.put("gtocore.part_ability.import_fluids_4x", new CNEN("流体输入仓(四重)", "Fluids Input (4x)"));
            LANG.put("gtocore.part_ability.export_fluids_9x", new CNEN("流体输出仓(九重)", "Fluids Output (9x)"));
            LANG.put("gtocore.part_ability.import_fluids_9x", new CNEN("流体输入仓(九重)", "Fluids Input (9x)"));
            LANG.put("gtocore.part_ability.input_energy", new CNEN("能源仓", "Energy Input"));
            LANG.put("gtocore.part_ability.output_energy", new CNEN("动力仓", "Energy Output"));
            LANG.put("gtocore.part_ability.substation_input_energy", new CNEN("变电站能量输入仓", "Substation Energy Input"));
            LANG.put("gtocore.part_ability.substation_output_energy", new CNEN("变电站能量输出仓", "Substation Energy Output"));
            LANG.put("gtocore.part_ability.rotor_holder", new CNEN("转子支架", "Rotor Holder"));
            LANG.put("gtocore.part_ability.pump_fluid_hatch", new CNEN("水泵仓", "Pump Fluid Hatch"));
            LANG.put("gtocore.part_ability.steam", new CNEN("蒸汽仓", "Steam Hatch"));
            LANG.put("gtocore.part_ability.steam_import_items", new CNEN("蒸汽物品输入仓", "Steam Import Items"));
            LANG.put("gtocore.part_ability.steam_export_items", new CNEN("蒸汽物品输出仓", "Steam Export Items"));
            LANG.put("gtocore.part_ability.maintenance", new CNEN("维护仓", "Maintenance Hatch"));
            LANG.put("gtocore.part_ability.muffler", new CNEN("消声仓", "Muffler"));
            LANG.put("gtocore.part_ability.tank_valve", new CNEN("储罐阀门", "Tank Valve"));
            LANG.put("gtocore.part_ability.passthrough_hatch", new CNEN("通行仓", "Passthrough Hatch"));
            LANG.put("gtocore.part_ability.parallel_hatch", new CNEN("并行仓", "Parallel Hatch"));
            LANG.put("gtocore.part_ability.input_laser", new CNEN("激光能源仓", "Input Laser"));
            LANG.put("gtocore.part_ability.output_laser", new CNEN("激光动力仓", "Output Laser"));
            LANG.put("gtocore.part_ability.computation_data_reception", new CNEN("算力数据靶仓", "Computation Data Reception"));
            LANG.put("gtocore.part_ability.computation_data_transmission", new CNEN("算力数据源仓", "Computation Data Transmission"));
            LANG.put("gtocore.part_ability.optical_data_reception", new CNEN("光学数据靶仓", "Optical Data Reception"));
            LANG.put("gtocore.part_ability.optical_data_transmission", new CNEN("光学数据源仓", "Optical Data Transmission"));
            LANG.put("gtocore.part_ability.data_access", new CNEN("数据访问仓", "Data Access"));
            LANG.put("gtocore.part_ability.hpca_component", new CNEN("高性能计算组件", "HPCA Component"));

        }
    }

    public static void init() {
        PartAbility.STEAM_IMPORT_ITEMS.register(2, GTMachines.ITEM_IMPORT_BUS[0].get());
        PartAbility.STEAM_EXPORT_ITEMS.register(2, GTMachines.ITEM_EXPORT_BUS[0].get());
        STEAM_IMPORT_FLUIDS.register(2, GTOMachines.INFINITE_INTAKE_HATCH.get());
        for (var machine : GTMachines.ITEM_IMPORT_BUS) {
            if (machine != null) ITEMS_INPUT.register(machine.getTier(), machine.get());
        }
        for (var machine : GTMachines.ITEM_EXPORT_BUS) {
            if (machine != null) ITEMS_OUTPUT.register(machine.getTier(), machine.get());
        }
    }
}

package com.gtocore.common.block;

import com.gtocore.common.data.GTOBlocks;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.common.data.GTBlocks;

import net.minecraft.world.level.block.Block;

import com.tterrag.registrate.util.entry.RegistryEntry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Supplier;

@DataGeneratorScanned
public final class BlockMap {

    public static Block[] ABS_CASING;
    public static Block[] LIGHT;
    public static Block[] WIRELESS_ENERGY_UNIT;
    public static Block[] ME_STORAGE_CORE;
    public static Block[] CRAFTING_STORAGE_CORE;

    public static final Object2ObjectOpenHashMap<String, Block[]> MAP = new Object2ObjectOpenHashMap<>();

    public static final Int2ObjectMap<Supplier<?>> SCMAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectMap<Supplier<?>> SEPMMAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectMap<Supplier<?>> CALMAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectMap<Supplier<?>> GLASSMAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectMap<Supplier<?>> MACHINECASINGMAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectMap<Supplier<?>> INTEGRALFRAMEWORKMAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectMap<Supplier<?>> GRAVITONFLOWMAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectMap<Supplier<?>> COMPUTER_CASING_MAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectMap<Supplier<?>> COMPUTER_HEAT_MAP = new Int2ObjectOpenHashMap<>();

    public static void init() {
        GLASSMAP.put(2, GTBlocks.CASING_TEMPERED_GLASS);
        COMPUTER_CASING_MAP.put(1, GTBlocks.COMPUTER_CASING);
        COMPUTER_CASING_MAP.put(2, GTOBlocks.BIOCOMPUTER_CASING);
        COMPUTER_CASING_MAP.put(3, GTOBlocks.GRAVITON_COMPUTER_CASING);
        COMPUTER_HEAT_MAP.put(1, GTBlocks.COMPUTER_HEAT_VENT);
        COMPUTER_HEAT_MAP.put(2, GTOBlocks.PHASE_CHANGE_BIOCOMPUTER_COOLING_VENTS);
        COMPUTER_HEAT_MAP.put(3, GTOBlocks.ANTI_ENTROPY_COMPUTER_CONDENSATION_MATRIX);
    }

    public static final String namePrefix = "gtocore.adv_terminal.block_map";

    @RegisterLanguage(namePrefix = namePrefix, cn = "无线能量单元", en = "Wireless Energy Unit")
    private static final String wireless_energy_unit = "wireless_energy_unit";

    @RegisterLanguage(namePrefix = namePrefix, cn = "ME存储核心", en = "ME Storage Core")
    private static final String me_storage_core = "me_storage_core";

    @RegisterLanguage(namePrefix = namePrefix, cn = "合成存储核心", en = "Crafting storage Core")
    private static final String crafting_storage_core = "crafting_storage_core";

    @RegisterLanguage(namePrefix = namePrefix, cn = "恒星热力容器", en = "Stellar Containment Casing")
    private static final String stellar_containment_casing = "stellar_containment_casing";

    @RegisterLanguage(namePrefix = namePrefix, cn = "线圈", en = "Heating Coils")
    private static final String heating_coils = "heating_coils";

    @RegisterLanguage(namePrefix = namePrefix, cn = "过滤方块", en = "Cleanroom Filters")
    private static final String cleanroom_filters = "cleanroom_filters";

    @RegisterLanguage(namePrefix = namePrefix, cn = "电容", en = "Batteries")
    private static final String batteries = "batteries";

    @RegisterLanguage(namePrefix = namePrefix, cn = "电梯模块", en = "Space Elevator Power Module")
    private static final String space_elevator_power_module = "space_elevator_power_module";

    @RegisterLanguage(namePrefix = namePrefix, cn = "部装外壳", en = "Component Assembly Line Casing")
    private static final String component_assembly_line_casing = "component_assembly_line_casing";

    @RegisterLanguage(namePrefix = namePrefix, cn = "玻璃", en = "Glass")
    private static final String glass = "glass";

    @RegisterLanguage(namePrefix = namePrefix, cn = "机器外壳", en = "Machine Casing")
    private static final String machine_casing = "machine_casing";

    @RegisterLanguage(namePrefix = namePrefix, cn = "整体框架", en = "Integral Framework")
    private static final String integral_framework = "integral_framework";

    @RegisterLanguage(namePrefix = namePrefix, cn = "引力流调节器", en = "Graviton Flow Modulator")
    private static final String graviton_flow_modulator = "graviton_flow_modulator";

    @RegisterLanguage(namePrefix = namePrefix, cn = "计算机外壳", en = "Computer Casing")
    private static final String computer_casing = "computer_casing";

    @RegisterLanguage(namePrefix = namePrefix, cn = "计算机散热", en = "Computer Heat")
    private static final String computer_heat = "computer_heat";

    @RegisterLanguage(namePrefix = namePrefix, cn = "ABS方块", en = "ABS Casing")
    private static final String abs_casing = "abs_casing";

    @RegisterLanguage(namePrefix = namePrefix, cn = "灯", en = "Light")
    private static final String light = "light";

    public static void build() {
        var coils = new ArrayList<>(GTCEuAPI.HEATING_COILS.entrySet());
        coils.sort(Comparator.comparingInt(entry -> entry.getKey().getTier()));
        MAP.put(heating_coils, coils.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));

        MAP.put(cleanroom_filters, new Block[] { GTBlocks.FILTER_CASING.get(), GTBlocks.FILTER_CASING_STERILE.get(), GTOBlocks.LAW_FILTER_CASING.get() });

        var batteries0 = new ArrayList<>(GTCEuAPI.PSS_BATTERIES.entrySet());
        batteries0.sort(Comparator.comparingInt(entry -> entry.getKey().getTier()));
        MAP.put(batteries, batteries0.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));

        var tiers = new ArrayList<>(SCMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        MAP.put(stellar_containment_casing, tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));

        tiers = new ArrayList<>(SEPMMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        MAP.put(space_elevator_power_module, tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));

        tiers = new ArrayList<>(CALMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        MAP.put(component_assembly_line_casing, tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));

        tiers = new ArrayList<>(GLASSMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        MAP.put(glass, tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));

        tiers = new ArrayList<>(MACHINECASINGMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        MAP.put(machine_casing, tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));

        tiers = new ArrayList<>(INTEGRALFRAMEWORKMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        MAP.put(integral_framework, tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));

        tiers = new ArrayList<>(GRAVITONFLOWMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        MAP.put(graviton_flow_modulator, tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));

        tiers = new ArrayList<>(COMPUTER_CASING_MAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        MAP.put(computer_casing, tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));

        tiers = new ArrayList<>(COMPUTER_HEAT_MAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        MAP.put(computer_heat, tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));

        WIRELESS_ENERGY_UNIT = arr(GTOBlocks.LV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.MV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.HV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.EV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.IV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.LUV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.ZPM_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.UV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.UHV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.UEV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.UIV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.UXV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.OPV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.MAX_WIRELESS_ENERGY_UNIT.get());
        MAP.put(wireless_energy_unit, WIRELESS_ENERGY_UNIT);

        ME_STORAGE_CORE = arr(GTOBlocks.T1_ME_STORAGE_CORE.get(), GTOBlocks.T2_ME_STORAGE_CORE.get(), GTOBlocks.T3_ME_STORAGE_CORE.get(), GTOBlocks.T4_ME_STORAGE_CORE.get(), GTOBlocks.T5_ME_STORAGE_CORE.get());
        MAP.put(me_storage_core, ME_STORAGE_CORE);

        CRAFTING_STORAGE_CORE = arr(GTOBlocks.T1_CRAFTING_STORAGE_CORE.get(), GTOBlocks.T2_CRAFTING_STORAGE_CORE.get(), GTOBlocks.T3_CRAFTING_STORAGE_CORE.get(), GTOBlocks.T4_CRAFTING_STORAGE_CORE.get(), GTOBlocks.T5_CRAFTING_STORAGE_CORE.get());
        MAP.put(crafting_storage_core, CRAFTING_STORAGE_CORE);

        ABS_CASING = arr(GTOBlocks.ABS_BLACK_CASING.get(), GTOBlocks.ABS_BLUE_CASING.get(), GTOBlocks.ABS_BROWN_CASING.get(), GTOBlocks.ABS_GREEN_CASING.get(), GTOBlocks.ABS_GREY_CASING.get(), GTOBlocks.ABS_LIME_CASING.get(), GTOBlocks.ABS_ORANGE_CASING.get(), GTOBlocks.ABS_RAD_CASING.get(), GTOBlocks.ABS_WHITE_CASING.get(), GTOBlocks.ABS_YELLOW_CASING.get(), GTOBlocks.ABS_CYAN_CASING.get(), GTOBlocks.ABS_MAGENTA_CASING.get(), GTOBlocks.ABS_PINK_CASING.get(), GTOBlocks.ABS_PURPLE_CASING.get(), GTOBlocks.ABS_LIGHT_BULL_CASING.get(), GTOBlocks.ABS_LIGHT_GREY_CASING.get());
        MAP.put(abs_casing, ABS_CASING);

        LIGHT = GTBlocks.LAMPS.values().stream().map(RegistryEntry::get).toArray(Block[]::new);
        MAP.put(light, LIGHT);
    }

    public static Block[] arr(Block... blocks) {
        return blocks;
    }
}

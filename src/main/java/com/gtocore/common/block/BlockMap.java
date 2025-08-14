package com.gtocore.common.block;

import com.gtocore.common.data.GTOBlocks;

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

public final class BlockMap {

    public static Block[] ABS_CASING;
    public static Block[] LIGHT;

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

    public static void build() {
        var coils = new ArrayList<>(GTCEuAPI.HEATING_COILS.entrySet());
        coils.sort(Comparator.comparingInt(entry -> entry.getKey().getTier()));
        // 线圈
        MAP.put("heating_coils", coils.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));
        // 过滤方块
        MAP.put("cleanroom_filters", new Block[] { GTBlocks.FILTER_CASING.get(), GTBlocks.FILTER_CASING_STERILE.get(), GTOBlocks.LAW_FILTER_CASING.get() });
        var batteries = new ArrayList<>(GTCEuAPI.PSS_BATTERIES.entrySet());
        batteries.sort(Comparator.comparingInt(entry -> entry.getKey().getTier()));
        // 电容
        MAP.put("batteries", batteries.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));
        var tiers = new ArrayList<>(SCMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        // 恒星热力容器
        MAP.put("stellar_containment_casing", tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));
        tiers = new ArrayList<>(SEPMMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        // 电梯模块
        MAP.put("space_elevator_power_module", tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));
        tiers = new ArrayList<>(CALMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        // 部装外壳
        MAP.put("component_assembly_line_casing", tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));
        tiers = new ArrayList<>(GLASSMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        // 玻璃
        MAP.put("glass", tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));
        tiers = new ArrayList<>(MACHINECASINGMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        // 机器外壳
        MAP.put("machine_casing", tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));
        tiers = new ArrayList<>(INTEGRALFRAMEWORKMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        // 整体框架
        MAP.put("integral_framework", tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));
        tiers = new ArrayList<>(GRAVITONFLOWMAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        // 引力流调节器
        MAP.put("graviton_flow_modulator", tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));
        tiers = new ArrayList<>(COMPUTER_CASING_MAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        // 计算机外壳
        MAP.put("computer_casing", tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));
        tiers = new ArrayList<>(COMPUTER_HEAT_MAP.int2ObjectEntrySet());
        tiers.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        // 计算机散热
        MAP.put("computer_heat", tiers.stream().map(Map.Entry::getValue).map(Supplier::get).toArray(Block[]::new));
        ABS_CASING = arr(GTOBlocks.ABS_BLACK_CASING.get(), GTOBlocks.ABS_BLUE_CASING.get(), GTOBlocks.ABS_BROWN_CASING.get(), GTOBlocks.ABS_GREEN_CASING.get(), GTOBlocks.ABS_GREY_CASING.get(), GTOBlocks.ABS_LIME_CASING.get(), GTOBlocks.ABS_ORANGE_CASING.get(), GTOBlocks.ABS_RAD_CASING.get(), GTOBlocks.ABS_WHITE_CASING.get(), GTOBlocks.ABS_YELLOW_CASING.get(), GTOBlocks.ABS_CYAN_CASING.get(), GTOBlocks.ABS_MAGENTA_CASING.get(), GTOBlocks.ABS_PINK_CASING.get(), GTOBlocks.ABS_PURPLE_CASING.get(), GTOBlocks.ABS_LIGHT_BULL_CASING.get(), GTOBlocks.ABS_LIGHT_GREY_CASING.get());
        // ABS方块
        MAP.put("abs_casing", ABS_CASING);
        LIGHT = GTBlocks.LAMPS.values().stream().map(RegistryEntry::get).toArray(Block[]::new);
        // 灯
        MAP.put("light", LIGHT);
    }

    public static Block[] arr(Block... blocks) {
        return blocks;
    }
}

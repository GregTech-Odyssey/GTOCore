package com.gtocore.common.machine.mana;

import com.gtocore.common.machine.mana.PlatformBlockType.*;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlatformTemplateStorage {

    // 结构常量
    private static final PlatformBlockStructure COBBLESTONE_CORE_16x16x1;
    private static final PlatformBlockStructure COBBLESTONE_ROAD_X_16x16x1;
    private static final PlatformBlockStructure COBBLESTONE_ROAD_Z_16x16x1;
    private static final PlatformBlockStructure COBBLESTONE_ROAD_CROSS_16x16x1;

    // 预设常量
    private static final PlatformPreset PRESET_COBBLESTONE_SINGLE;
    private static final PlatformPreset PRESET_COBBLESTONE_ROADS;
    private static final PlatformPreset PRESET_COBBLESTONE_FULL;

    static {
        // 1. 圆石核心平台 (16x16x1)
        List<String[]> coreDepth = new ArrayList<>();
        String coreRow = "CCCCCCCCCCCCCCCC";
        for (int z = 0; z < 16; z++) {
            coreDepth.add(new String[] { coreRow });
        }
        Map<Character, Block> coreMapping = new HashMap<>();
        coreMapping.put('C', Blocks.COBBLESTONE);
        int[] coreMaterials = new int[] { 256 };
        COBBLESTONE_CORE_16x16x1 = PlatformBlockStructure.ofFull(BlockType.CORE, coreDepth, coreMapping, coreMaterials);

        // 2. X方向道路
        List<String[]> roadXDepth = new ArrayList<>();
        String roadXRow = "CCCCCCCCCCCCCCCC";
        for (int z = 0; z < 16; z++) {
            roadXDepth.add(new String[] { roadXRow });
        }
        Map<Character, Block> roadXMapping = new HashMap<>();
        roadXMapping.put('C', Blocks.COBBLESTONE);
        int[] roadXMaterials = new int[] { 256 };
        COBBLESTONE_ROAD_X_16x16x1 = PlatformBlockStructure.ofFull(BlockType.ROAD_X, roadXDepth, roadXMapping, roadXMaterials);

        // 3. Z方向道路
        List<String[]> roadZDepth = new ArrayList<>();
        String roadZRow = "CCCCCCCCCCCCCCCC";
        for (int z = 0; z < 16; z++) {
            roadZDepth.add(new String[] { roadZRow });
        }
        Map<Character, Block> roadZMapping = new HashMap<>();
        roadZMapping.put('C', Blocks.COBBLESTONE);
        int[] roadZMaterials = new int[] { 256 };
        COBBLESTONE_ROAD_Z_16x16x1 = PlatformBlockStructure.ofFull(BlockType.ROAD_Z, roadZDepth, roadZMapping, roadZMaterials);

        // 4. 十字路口
        List<String[]> crossDepth = new ArrayList<>();
        String crossRow = "CCCCCCCCCCCCCCCC";
        for (int z = 0; z < 16; z++) {
            crossDepth.add(new String[] { crossRow });
        }
        Map<Character, Block> crossMapping = new HashMap<>();
        crossMapping.put('C', Blocks.COBBLESTONE);
        int[] crossMaterials = new int[] { 256 };
        COBBLESTONE_ROAD_CROSS_16x16x1 = PlatformBlockStructure.ofFull(BlockType.ROAD_CROSS, crossDepth, crossMapping, crossMaterials);

        // 预设
        PRESET_COBBLESTONE_SINGLE = PlatformPreset.builder("cobblestone_single")
                .displayName("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_single.name")
                .description("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_single.desc")
                .source("vanilla")
                .addStructure(COBBLESTONE_CORE_16x16x1)
                .build();

        PRESET_COBBLESTONE_ROADS = PlatformPreset.builder("cobblestone_roads")
                .displayName("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_roads.name")
                .description("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_roads.desc")
                .source("vanilla")
                .addStructure(COBBLESTONE_ROAD_X_16x16x1)
                .addStructure(COBBLESTONE_ROAD_Z_16x16x1)
                .addStructure(COBBLESTONE_ROAD_CROSS_16x16x1)
                .build();

        PRESET_COBBLESTONE_FULL = PlatformPreset.builder("cobblestone_full")
                .displayName("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_full.name")
                .description("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_full.desc")
                .source("vanilla")
                .addStructure(COBBLESTONE_CORE_16x16x1)
                .addStructure(COBBLESTONE_ROAD_X_16x16x1)
                .addStructure(COBBLESTONE_ROAD_Z_16x16x1)
                .addStructure(COBBLESTONE_ROAD_CROSS_16x16x1)
                .build();
    }

    /**
     * 获取所有预设模板
     */
    public static List<PlatformPreset> initializePresets() {
        return List.of(
                PRESET_COBBLESTONE_SINGLE,
                PRESET_COBBLESTONE_ROADS,
                PRESET_COBBLESTONE_FULL);
    }
}

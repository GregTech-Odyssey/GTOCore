package com.gtocore.common.machine.mana;

import com.gtocore.common.machine.mana.PlatformBlockType.*;

import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class PlatformTemplateStorage {

    // 存储所有预设
    private static final List<PlatformPreset> presets = new ArrayList<>();

    static {
        // 1. 圆石核心平台 (16x16x1)
        PlatformBlockStructure core = PlatformBlockStructure.structure(BlockType.CORE)
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .where('C', Blocks.COBBLESTONE)
                .materials(256)
                .build();

        // 2. X方向道路 (16x16x1)
        PlatformBlockStructure roadX = PlatformBlockStructure.structure(BlockType.ROAD_X)
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .where('C', Blocks.COBBLESTONE)
                .materials(256)
                .build();

        // 3. Z方向道路 (16x16x1)
        PlatformBlockStructure roadZ = PlatformBlockStructure.structure(BlockType.ROAD_Z)
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .where('C', Blocks.COBBLESTONE)
                .materials(256)
                .build();

        // 4. 十字路口 (16x16x1)
        PlatformBlockStructure cross = PlatformBlockStructure.structure(BlockType.ROAD_CROSS)
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .addLayer("CCCCCCCCCCCCCCCC")
                .where('C', Blocks.COBBLESTONE)
                .materials(256)
                .build();

        // 注册预设
        presets.add(
                PlatformPreset.preset("cobblestone_single")
                        .displayName("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_single.name")
                        .description("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_single.desc")
                        .source("vanilla")
                        .addStructure(core)
                        .build());

        presets.add(
                PlatformPreset.preset("cobblestone_roads")
                        .displayName("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_roads.name")
                        .description("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_roads.desc")
                        .source("vanilla")
                        .addStructure(roadX)
                        .addStructure(roadZ)
                        .addStructure(cross)
                        .build());

        presets.add(
                PlatformPreset.preset("cobblestone_full")
                        .displayName("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_full.name")
                        .description("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_full.desc")
                        .source("vanilla")
                        .addStructure(core)
                        .addStructure(roadX)
                        .addStructure(roadZ)
                        .addStructure(cross)
                        .build());
    }

    /**
     * 获取所有预设模板
     */
    public static List<PlatformPreset> initializePresets() {
        return List.copyOf(presets);
    }
}

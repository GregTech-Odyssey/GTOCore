package com.gtocore.common.machine.mana;

import com.gtocore.common.machine.mana.PlatformBlockType.*;

import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class PlatformTemplateStorage {

    private static final List<PlatformPreset> presets = new ArrayList<>();

    static {
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

        // 从文件加载X方向道路
        PlatformBlockStructure roadX = PlatformBlockStructure.structure(BlockType.ROAD_X)
                .addLayersFromResource("assets/gtocore/platforms/road_x.txt")
                .where('A', Blocks.COBBLESTONE)
                .where('~', Blocks.AIR)
                .materials(256)
                .build();

        // 从文件加载Z方向道路
        PlatformBlockStructure roadZ = PlatformBlockStructure.structure(BlockType.ROAD_Z)
                .addLayersFromResource("assets/gtocore/platforms/road_z.txt")
                .where('A', Blocks.COBBLESTONE)
                .where('~', Blocks.AIR)
                .materials(256)
                .build();

        // 从文件加载十字路口
        PlatformBlockStructure cross = PlatformBlockStructure.structure(BlockType.ROAD_CROSS)
                .addLayersFromResource("assets/gtocore/platforms/cross.txt")
                .where('A', Blocks.COBBLESTONE)
                .where('~', Blocks.AIR)
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

    public static List<PlatformPreset> initializePresets() {
        return List.copyOf(presets);
    }
}

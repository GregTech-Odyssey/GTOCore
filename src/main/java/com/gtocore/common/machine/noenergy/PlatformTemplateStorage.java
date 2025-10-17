package com.gtocore.common.machine.noenergy;

import com.gtocore.common.machine.noenergy.PlatformBlockType.PlatformPreset;

import com.gtolib.GTOCore;
import com.gtolib.api.lang.CNEN;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gtocore.common.machine.noenergy.PlatformBlockType.PlatformBlockStructure.structure;

public final class PlatformTemplateStorage {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    private static final List<PlatformPreset> presets = new ArrayList<>();
    private static final List<PlatformPreset> exPresets = new ArrayList<>();

    private static final String platform = add("平台", "platform");
    private static final String platform_3_3 = add("平台(3*3)", "platform(3*3)");
    private static final String platform_large = add("平台(大)", "platform(large)");
    private static final String road = add("道路", "road");
    private static final String factory = add("工厂", "factory");

    static {

        // 平台标准预设库
        {
            String high_saturation_chessboard = add("high_saturation_chessboard", "高饱和棋盘", "High saturation chessboard");
            String high_saturation_panel = add("high_saturation_panel", "高饱和嵌板", "High saturation panel");

            presets.add(
                    PlatformPreset.preset("platform_standard_library")
                            .displayName(add("平台标准预设库", "Platform standard preset library"))
                            .addStructure(structure("high_saturation_chessboard_1_blue_pink")
                                    .type(platform)
                                    .displayName(high_saturation_chessboard)
                                    .description(add("1×1 蓝·粉", "1×1 blue·pink"))
                                    .source("阿龙-还有一件事")
                                    .resource(GTOCore.id(in("high_saturation_chessboard_1")))
                                    .symbolMap(GTOCore.id(in("high_saturation_chessboard_blue_pink.json")))
                                    .materials(0, 144)
                                    .build())
                            .addStructure(structure("high_saturation_chessboard_1_orange_white")
                                    .type(platform)
                                    .displayName(high_saturation_chessboard)
                                    .description(add("1×1 橙·白", "1×1 orange·white"))
                                    .source("阿龙-还有一件事")
                                    .resource(GTOCore.id(in("high_saturation_chessboard_1")))
                                    .symbolMap(GTOCore.id(in("high_saturation_chessboard_orange_white.json")))
                                    .materials(0, 144)
                                    .build())
                            .addStructure(structure("high_saturation_chessboard_1_yellow_lime")
                                    .type(platform)
                                    .displayName(high_saturation_chessboard)
                                    .description(add("1×1 黄·青", "1×1 yellow·lime"))
                                    .source("阿龙-还有一件事")
                                    .resource(GTOCore.id(in("high_saturation_chessboard_1")))
                                    .symbolMap(GTOCore.id(in("high_saturation_chessboard_yellow_lime.json")))
                                    .materials(0, 144)
                                    .build())
                            .addStructure(structure("high_saturation_chessboard_3_blue_pink")
                                    .type(platform_3_3)
                                    .displayName(high_saturation_chessboard)
                                    .description(add("3×3 蓝·粉", "3×3 blue·pink"))
                                    .source("阿龙-还有一件事")
                                    .resource(GTOCore.id(in("high_saturation_chessboard_3")))
                                    .symbolMap(GTOCore.id(in("high_saturation_chessboard_blue_pink.json")))
                                    .materials(0, 1296)
                                    .build())
                            .addStructure(structure("high_saturation_chessboard_3_orange_white")
                                    .type(platform_3_3)
                                    .displayName(high_saturation_chessboard)
                                    .description(add("3×3 橙·白", "3×3 orange·white"))
                                    .source("阿龙-还有一件事")
                                    .resource(GTOCore.id(in("high_saturation_chessboard_3")))
                                    .symbolMap(GTOCore.id(in("high_saturation_chessboard_orange_white.json")))
                                    .materials(0, 1296)
                                    .build())
                            .addStructure(structure("high_saturation_chessboard_3_yellow_lime")
                                    .type(platform_3_3)
                                    .displayName(high_saturation_chessboard)
                                    .description(add("3×3 黄·青", "3×3 yellow·lime"))
                                    .source("阿龙-还有一件事")
                                    .resource(GTOCore.id(in("high_saturation_chessboard_3")))
                                    .symbolMap(GTOCore.id(in("high_saturation_chessboard_yellow_lime.json")))
                                    .materials(0, 1296)
                                    .build())
                            .addStructure(structure("high_saturation_panel_1_white_pink")
                                    .type(platform)
                                    .displayName(high_saturation_panel)
                                    .description(add("1×1 白嵌粉", "1×1 white Embed pink"))
                                    .source("阿龙-还有一件事")
                                    .resource(GTOCore.id(in("high_saturation_panel_1")))
                                    .symbolMap(GTOCore.id(in("high_saturation_panel_white_pink.json")))
                                    .materials(0, 144)
                                    .build())
                            .addStructure(structure("high_saturation_panel_1_black_blue")
                                    .type(platform)
                                    .displayName(high_saturation_panel)
                                    .description(add("1×1 黑嵌蓝", "1×1 black Embed blue"))
                                    .source("阿龙-还有一件事")
                                    .resource(GTOCore.id(in("high_saturation_panel_1")))
                                    .symbolMap(GTOCore.id(in("high_saturation_panel_black_blue.json")))
                                    .materials(0, 144)
                                    .build())
                            .addStructure(structure("high_saturation_panel_3_white_pink")
                                    .type(platform_3_3)
                                    .displayName(high_saturation_panel)
                                    .description(add("3×3 白嵌粉", "3×3 white Embed pink"))
                                    .source("阿龙-还有一件事")
                                    .resource(GTOCore.id(in("high_saturation_panel_1")))
                                    .symbolMap(GTOCore.id(in("high_saturation_panel_white_pink.json")))
                                    .materials(0, 144)
                                    .build())
                            .addStructure(structure("high_saturation_panel_3_black_blue")
                                    .type(platform_3_3)
                                    .displayName(high_saturation_panel)
                                    .description(add("3×3 黑嵌蓝", "3×3 black Embed blue"))
                                    .source("阿龙-还有一件事")
                                    .resource(GTOCore.id(in("high_saturation_panel_1")))
                                    .symbolMap(GTOCore.id(in("high_saturation_panel_black_blue.json")))
                                    .materials(0, 144)
                                    .build())
                            .addStructure(structure("white_floor_with_greenery_and_orange_and_yellow_edges")
                                    .type(platform_large)
                                    .displayName(high_saturation_panel)
                                    .description(add("2×2 带绿化的镶橙黄边白色地板", "2×2 White floor with greenery and orange and yellow edges"))
                                    .source("阿龙-还有一件事")
                                    .resource(GTOCore.id(in("white_floor_with_greenery_and_orange_and_yellow_edges")))
                                    .symbolMap(GTOCore.id(in("white_floor_with_greenery_and_orange_and_yellow_edges.json")))
                                    .materials(0, 576)
                                    .build())
                            .build());
        }

        // 平台扩展预设库
        {
            String light_colored_road_floor = add("light_colored_road_floor", "浅色带公路地板", "Light-colored road floor");

            String gray_floor_with_lights = add("gray_floor_with_lights", "浅色带灯带地板", "Gray floor with lights");

            presets.add(
                    PlatformPreset.preset("platform_extension_library")
                            .displayName(add("平台扩展预设库", "Platform extended preset library"))
                            .addStructure(structure("light_colored_road_floor_1")
                                    .type(platform)
                                    .displayName(light_colored_road_floor)
                                    .source("神官")
                                    .resource(GTOCore.id(in("light_colored_road_floor_1")))
                                    .symbolMap(GTOCore.id(in("light_colored_road_floor_1.json")))
                                    .materials(0, 100)
                                    .build())
                            .addStructure(structure("light_colored_road_floor_2")
                                    .type(road)
                                    .displayName(light_colored_road_floor)
                                    .source("神官")
                                    .resource(GTOCore.id(in("light_colored_road_floor_2")))
                                    .symbolMap(GTOCore.id(in("light_colored_road_floor_2.json")))
                                    .materials(0, 20)
                                    .build())
                            .addStructure(structure("light_colored_road_floor_3")
                                    .type(platform_3_3)
                                    .displayName(light_colored_road_floor)
                                    .source("神官")
                                    .resource(GTOCore.id(in("light_colored_road_floor_3")))
                                    .symbolMap(GTOCore.id(in("light_colored_road_floor_3.json")))
                                    .materials(0, 676)
                                    .build())
                            .addStructure(structure("light_colored_road_floor_4")
                                    .type(platform_large)
                                    .displayName(light_colored_road_floor)
                                    .source("神官")
                                    .resource(GTOCore.id(in("light_colored_road_floor_4")))
                                    .symbolMap(GTOCore.id(in("light_colored_road_floor_4.json")))
                                    .materials(0, 676)
                                    .build())
                            .addStructure(structure("gray_floor_with_lights_1")
                                    .type(platform)
                                    .displayName(gray_floor_with_lights)
                                    .source("呼")
                                    .resource(GTOCore.id(in("gray_floor_with_lights_1")))
                                    .symbolMap(GTOCore.id(in("gray_floor_with_lights_1.json")))
                                    .materials(2, 100)
                                    .build())
                            .addStructure(structure("gray_floor_with_lights_2")
                                    .type(road)
                                    .displayName(gray_floor_with_lights)
                                    .source("呼")
                                    .resource(GTOCore.id(in("gray_floor_with_lights_2")))
                                    .symbolMap(GTOCore.id(in("gray_floor_with_lights_2.json")))
                                    .materials(2, 20)
                                    .build())
                            .addStructure(structure("gray_floor_with_lights_3")
                                    .type(platform_3_3)
                                    .displayName(gray_floor_with_lights)
                                    .source("呼")
                                    .resource(GTOCore.id(in("gray_floor_with_lights_3")))
                                    .symbolMap(GTOCore.id(in("gray_floor_with_lights_3.json")))
                                    .materials(2, 676)
                                    .build())
                            .addStructure(structure("gray_floor_with_lights_4")
                                    .type(platform_large)
                                    .displayName(gray_floor_with_lights)
                                    .source("呼")
                                    .resource(GTOCore.id(in("gray_floor_with_lights_4")))
                                    .symbolMap(GTOCore.id(in("gray_floor_with_lights_4.json")))
                                    .materials(2, 676)
                                    .build())
                            .build());
        }

        // 工厂标准预设库
        {
            presets.add(
                    PlatformPreset.preset("factory_standard_library")
                            .displayName(add("工厂标准预设库", "Factory standard preset library"))
                            .addStructure(structure("standard_factory_building")
                                    .type(platform)
                                    .displayName(add("标准厂房", "Standard factory building"))
                                    .source("疏影")
                                    .resource(GTOCore.id(in("standard_factory_building")))
                                    .symbolMap(GTOCore.id(in("standard_factory_building.json")))
                                    .materials(0, 400)
                                    .materials(1, 100)
                                    .build())
                            .addStructure(structure("long_corridor_factory_building")
                                    .type(platform)
                                    .displayName(add("长廊厂房", "Long corridor factory building"))
                                    .source("疏影")
                                    .resource(GTOCore.id(in("long_corridor_factory_building")))
                                    .symbolMap(GTOCore.id(in("long_corridor_factory_building.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .build());
        }

        if (GTCEu.isDataGen() || GTCEu.isModLoaded("gtoepp")) {
            exPresets.add(
                    PlatformPreset.preset("sy_1_batch_construction_template")
                            .displayName(add("SY-1批量建造模板", "SY-1 batch construction template"))
                            .description(add("涵盖大部分使用场景的建筑模板", "Building templates covering most usage scenarios"))
                            .addStructure(structure("rubiks_cube_factory")
                                    .type(factory)
                                    .displayName(add("魔方厂房", "Rubik's Cube Factory"))
                                    .resource(GTOEpp(in("sy_1/rubiks_cube_factory")))
                                    .symbolMap(GTOEpp(in("sy_1/rubiks_cube_factory.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("starry_sky_theme_suite_earth_style")
                                    .type(factory)
                                    .displayName(add("星空主题套房 - 地球款", "Starry Sky Theme Suite - Earth Style"))
                                    .resource(GTOEpp(in("sy_1/starry_sky_theme_suite_earth_style")))
                                    .symbolMap(GTOEpp(in("sy_1/starry_sky_theme_suite_earth_style.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("starry_sky_theme_suite_solar_system_style")
                                    .type(factory)
                                    .displayName(add("星空主题套房 - 太阳系款", "Starry Sky Theme Suite - Solar System Style"))
                                    .resource(GTOEpp(in("sy_1/starry_sky_theme_suite_solar_system_style")))
                                    .symbolMap(GTOEpp(in("sy_1/starry_sky_theme_suite_solar_system_style.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("starry_sky_theme_suite_barnard_style")
                                    .type(factory)
                                    .displayName(add("星空主题套房 - 巴纳德款", "Starry Sky Theme Suite - Barnard Style"))
                                    .resource(GTOEpp(in("sy_1/starry_sky_theme_suite_barnard_style")))
                                    .symbolMap(GTOEpp(in("sy_1/starry_sky_theme_suite_barnard_style.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("starry_sky_theme_suite_proxima_style")
                                    .type(factory)
                                    .displayName(add("星空主题套房  - 比邻星款", "Starry Sky Theme Suite - Proxima Style"))
                                    .resource(GTOEpp(in("sy_1/starry_sky_theme_suite_proxima_style")))
                                    .symbolMap(GTOEpp(in("sy_1/starry_sky_theme_suite_proxima_style.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("starry_sky_theme_suite_ross_128b_style")
                                    .type(factory)
                                    .displayName(add("星空主题套房 - 罗斯128b款", "Starry Sky Theme Suite - Ross 128b Style"))
                                    .resource(GTOEpp(in("sy_1/starry_sky_theme_suite_ross_128b_style")))
                                    .symbolMap(GTOEpp(in("sy_1/starry_sky_theme_suite_ross_128b_style.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("extra_large_factory")
                                    .type(factory)
                                    .displayName(add("特大厂房", "Extra large factory"))
                                    .resource(GTOEpp(in("sy_1/extra_large_factory")))
                                    .symbolMap(GTOEpp(in("sy_1/extra_large_factory.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("assembly_plant")
                                    .type(factory)
                                    .displayName(add("装配工厂", "Assembly Plant"))
                                    .resource(GTOEpp(in("sy_1/assembly_plant")))
                                    .symbolMap(GTOEpp(in("sy_1/assembly_plant.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("trans_space_assembly_plant")
                                    .type(factory)
                                    .displayName(add("超时空装配厂", "Trans-Space Assembly Plant"))
                                    .description(add("拥有地下49层", "It has 49 underground floors"))
                                    .resource(GTOEpp(in("sy_1/trans_space_assembly_plant")))
                                    .symbolMap(GTOEpp(in("sy_1/trans_space_assembly_plant.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("cell_culture_center")
                                    .type(factory)
                                    .displayName(add("细胞培育中心", "Cell Culture Center"))
                                    .resource(GTOEpp(in("sy_1/cell_culture_center")))
                                    .symbolMap(GTOEpp(in("sy_1/cell_culture_center.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("bacteria_factory")
                                    .type(factory)
                                    .displayName(add("细菌工厂", "Bacteria Factory"))
                                    .resource(GTOEpp(in("sy_1/bacteria_factory")))
                                    .symbolMap(GTOEpp(in("sy_1/bacteria_factory.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("fluid_refinery")
                                    .type(factory)
                                    .displayName(add("流体精炼厂", "Fluid Refinery"))
                                    .resource(GTOEpp(in("sy_1/fluid_refinery")))
                                    .symbolMap(GTOEpp(in("sy_1/fluid_refinery.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("silica_rock_power_plant")
                                    .type(factory)
                                    .displayName(add("硅岩发电厂", "Silica rock power plant"))
                                    .resource(GTOEpp(in("sy_1/silica_rock_power_plant")))
                                    .symbolMap(GTOEpp(in("sy_1/silica_rock_power_plant.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("nuclear_power_plant")
                                    .type(factory)
                                    .displayName(add("核电站", "Nuclear power plant"))
                                    .resource(GTOEpp(in("sy_1/nuclear_power_plant")))
                                    .symbolMap(GTOEpp(in("sy_1/nuclear_power_plant.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("mineral_processing_center")
                                    .type(factory)
                                    .displayName(add("矿物处理中心", "Mineral Processing Center"))
                                    .resource(GTOEpp(in("sy_1/mineral_processing_center")))
                                    .symbolMap(GTOEpp(in("sy_1/mineral_processing_center.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("deep_compositing_center")
                                    .type(factory)
                                    .displayName(add("深度合成中心", "Deep Compositing Center"))
                                    .resource(GTOEpp(in("sy_1/deep_compositing_center")))
                                    .symbolMap(GTOEpp(in("sy_1/deep_compositing_center.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("institute_of_microphysics")
                                    .type(factory)
                                    .displayName(add("微观物理研究所", "Institute of Microphysics"))
                                    .resource(GTOEpp(in("sy_1/institute_of_microphysics")))
                                    .symbolMap(GTOEpp(in("sy_1/institute_of_microphysics.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("supercomputing_center_tai_chi_computer_room")
                                    .type(factory)
                                    .displayName(add("超算中心 - 太极机房", "Supercomputing Center - Tai Chi Computer Room"))
                                    .resource(GTOEpp(in("sy_1/supercomputing_center_tai_chi_computer_room")))
                                    .symbolMap(GTOEpp(in("sy_1/supercomputing_center_tai_chi_computer_room.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("supercomputing_center_simple_computer_room")
                                    .type(factory)
                                    .displayName(add("超算中心 - 简易机房", "Supercomputing Center - Simple Computer Room"))
                                    .resource(GTOEpp(in("sy_1/supercomputing_center_simple_computer_room")))
                                    .symbolMap(GTOEpp(in("sy_1/supercomputing_center_simple_computer_room.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .addStructure(structure("space_elevator")
                                    .type(factory)
                                    .displayName(add("太空电梯", "Space Elevator"))
                                    .resource(GTOEpp(in("sy_1/space_elevator")))
                                    .symbolMap(GTOEpp(in("sy_1/space_elevator.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .source("疏影")
                            .build());
        }
    }

    private static String add(String key, String cn, String en) {
        if (LANG != null) LANG.put(key, new CNEN(cn, en));
        return "gtocore.platform." + key;
    }

    private static String add(String cn, String en) {
        String key = en.replace(' ', '_').toLowerCase();
        return add(key, cn, en);
    }

    private static String in(String path) {
        return "platforms/" + path;
    }

    private static ResourceLocation GTOEpp(String name) {
        return RLUtils.fromNamespaceAndPath("gtoepp", name);
    }

    static List<PlatformPreset> initializePresets() {
        List<PlatformPreset> preset = new ArrayList<>();
        presets.forEach(c -> {
            if (c != null) preset.add(c);
        });
        exPresets.forEach(c -> {
            if (c != null) preset.add(c);
        });
        return preset;
    }
}

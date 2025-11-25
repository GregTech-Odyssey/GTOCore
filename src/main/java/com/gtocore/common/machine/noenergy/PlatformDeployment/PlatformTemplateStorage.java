package com.gtocore.common.machine.noenergy.PlatformDeployment;

import com.gtocore.common.machine.noenergy.PlatformDeployment.PlatformBlockType.PlatformPreset;

import com.gtolib.GTOCore;
import com.gtolib.api.lang.CNEN;

import com.gregtechceu.gtceu.GTCEu;

import com.fast.fastcollection.O2OOpenCacheHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gtocore.common.machine.noenergy.PlatformDeployment.PlatformBlockType.PlatformBlockStructure.structure;

public final class PlatformTemplateStorage {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    private static final List<PlatformPreset> preset = new ArrayList<>();

    private static final String platform = add("平台", "platform");
    private static final String platform_3_3 = add("平台(3*3)", "platform(3*3)");
    private static final String platform_large = add("平台(大)", "platform(large)");
    private static final String road = add("道路", "road");
    private static final String factory = add("工厂", "factory");

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

    static List<PlatformPreset> initializePresets() {
        return preset;
    }

    private static final String high_saturation_chessboard = add("高饱和棋盘", "High saturation chessboard");
    private static final String high_saturation_panel = add("高饱和嵌板", "High saturation panel");
    private static final String light_colored_road_floor = add("浅色带公路地板", "Light-colored road floor");
    private static final String gray_floor_with_lights = add("浅色带灯带地板", "Gray floor with lights");

    static {
        var presets = new ArrayList<PlatformPreset>();
        // 平台标准预设库
        {
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
                                    .type(factory)
                                    .displayName(add("标准厂房", "Standard factory building"))
                                    .source("疏影")
                                    .resource(GTOCore.id(in("standard_factory_building")))
                                    .symbolMap(GTOCore.id(in("standard_factory_building.json")))
                                    .materials(0, 400)
                                    .materials(1, 100)
                                    .build())
                            .addStructure(structure("long_corridor_factory_building")
                                    .type(factory)
                                    .displayName(add("长廊厂房", "Long corridor factory building"))
                                    .source("疏影")
                                    .resource(GTOCore.id(in("long_corridor_factory_building")))
                                    .symbolMap(GTOCore.id(in("long_corridor_factory_building.json")))
                                    .materials(0, 800)
                                    .materials(1, 800)
                                    .build())
                            .build());
        }
        presets.forEach(c -> {
            if (c != null) preset.add(c);
        });
        if (GTCEu.isModLoaded("gtoepp")) {
            org.com.gtoepp.platforms.PlatformPresets.extendedPresets.forEach(c -> {
                if (c != null) preset.add(c);
            });
        }
    }
}

package com.gtocore.common.machine.mana;

import com.gtocore.common.machine.mana.PlatformBlockType.*;

import com.gtolib.GTOCore;
import com.gtolib.api.lang.CNEN;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gtocore.common.machine.mana.PlatformBlockType.PlatformBlockStructure.structure;

public final class PlatformTemplateStorage {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    private static final List<PlatformPreset> presets = new ArrayList<>();

    private static final String platform = add("平台", "platform");
    private static final String platform_3_3 = add("平台(3*3)", "platform(3*3)");
    private static final String platform_large = add("平台(大)", "platform(large)");
    private static final String road = add("道路", "road");
    private static final String factory = add("工厂", "factory");

    static {

        String high_saturation_series = add("high_saturation_series", "高饱和系列", "High saturation series");

        PlatformBlockStructure high_saturation_chessboard_1_blue_pink = structure("high_saturation_chessboard_1_blue_pink")
                .type(platform)
                .displayName(high_saturation_series)
                .description(add("1×1 蓝·粉", "1×1 blue·pink"))
                .source("阿龙-还有一件事")
                .resource(GTOCore.id(in("high_saturation_chessboard_1")))
                .symbolMap(GTOCore.id(in("high_saturation_chessboard_1_blue_pink.json")))
                .materials(0, 144)
                .build();

        PlatformBlockStructure high_saturation_chessboard_1_orange_white = structure("high_saturation_chessboard_1_orange_white")
                .type(platform)
                .displayName(high_saturation_series)
                .description(add("1×1 橙·白", "1×1 orange·white"))
                .source("阿龙-还有一件事")
                .resource(GTOCore.id(in("high_saturation_chessboard_1")))
                .symbolMap(GTOCore.id(in("high_saturation_chessboard_1_orange_white.json")))
                .materials(0, 144)
                .build();

        presets.add(
                PlatformPreset.preset("platform_standard_library")
                        .displayName(add("平台标准预设库", "Platform standard preset library"))
                        .description(add("基础预设平台", "Basic preset platform"))
                        .source("maple")
                        .addStructure(high_saturation_chessboard_1_blue_pink)
                        .addStructure(high_saturation_chessboard_1_orange_white)
                        .build());

        String light_colored_road_floor = add("light_colored_road_floor", "浅色带公路地板", "Light-colored road floor");

        PlatformBlockStructure light_colored_road_floor_1 = structure("light_colored_road_floor_1")
                .type(platform)
                .displayName(light_colored_road_floor)
                .source("神官")
                .resource(GTOCore.id(in("light_colored_road_floor_1")))
                .symbolMap(GTOCore.id(in("light_colored_road_floor_1.json")))
                .materials(0, 100)
                .build();

        PlatformBlockStructure light_colored_road_floor_2 = structure("light_colored_road_floor_2")
                .type(road)
                .displayName(light_colored_road_floor)
                .source("神官")
                .resource(GTOCore.id(in("light_colored_road_floor_2")))
                .symbolMap(GTOCore.id(in("light_colored_road_floor_2.json")))
                .materials(0, 20)
                .build();

        PlatformBlockStructure light_colored_road_floor_3 = structure("light_colored_road_floor_3")
                .type(platform_3_3)
                .displayName(light_colored_road_floor)
                .source("神官")
                .resource(GTOCore.id(in("light_colored_road_floor_3")))
                .symbolMap(GTOCore.id(in("light_colored_road_floor_3.json")))
                .materials(0, 676)
                .build();

        PlatformBlockStructure light_colored_road_floor_4 = structure("light_colored_road_floor_4")
                .type(platform_large)
                .displayName(light_colored_road_floor)
                .source("神官")
                .resource(GTOCore.id(in("light_colored_road_floor_4")))
                .symbolMap(GTOCore.id(in("light_colored_road_floor_4.json")))
                .materials(0, 676)
                .build();

        String gray_floor_with_lights = add("gray_floor_with_lights", "浅色带灯带地板", "Gray floor with lights");

        PlatformBlockStructure gray_floor_with_lights_1 = structure("gray_floor_with_lights_1")
                .type(platform)
                .displayName(gray_floor_with_lights)
                .source("呼")
                .resource(GTOCore.id(in("gray_floor_with_lights_1")))
                .symbolMap(GTOCore.id(in("gray_floor_with_lights_1.json")))
                .materials(1, 100)
                .build();

        PlatformBlockStructure gray_floor_with_lights_2 = structure("gray_floor_with_lights_2")
                .type(road)
                .displayName(gray_floor_with_lights)
                .source("呼")
                .resource(GTOCore.id(in("gray_floor_with_lights_2")))
                .symbolMap(GTOCore.id(in("gray_floor_with_lights_2.json")))
                .materials(1, 20)
                .build();

        PlatformBlockStructure gray_floor_with_lights_3 = structure("gray_floor_with_lights_3")
                .type(platform_3_3)
                .displayName(gray_floor_with_lights)
                .source("呼")
                .resource(GTOCore.id(in("gray_floor_with_lights_3")))
                .symbolMap(GTOCore.id(in("gray_floor_with_lights_3.json")))
                .materials(1, 676)
                .build();

        PlatformBlockStructure gray_floor_with_lights_4 = structure("gray_floor_with_lights_4")
                .type(platform_large)
                .displayName(gray_floor_with_lights)
                .source("呼")
                .resource(GTOCore.id(in("gray_floor_with_lights_4")))
                .symbolMap(GTOCore.id(in("gray_floor_with_lights_4.json")))
                .materials(1, 676)
                .build();

        presets.add(
                PlatformPreset.preset("platform_extension_library")
                        .displayName(add("平台扩展预设库", "Platform extended preset library"))
                        .description(add("扩展预设平台", "Expanded preset platform"))
                        .addStructure(light_colored_road_floor_1)
                        .addStructure(light_colored_road_floor_2)
                        .addStructure(light_colored_road_floor_3)
                        .addStructure(light_colored_road_floor_4)
                        .addStructure(gray_floor_with_lights_1)
                        .addStructure(gray_floor_with_lights_2)
                        .addStructure(gray_floor_with_lights_3)
                        .addStructure(gray_floor_with_lights_4)
                        .build());

        PlatformBlockStructure house = structure("house")
                .type(platform)
                .displayName(add("这是房子", "house"))
                .source("某村民")
                .preview(true)
                .resource(GTOCore.id(in("house")))
                .symbolMap(GTOCore.id(in("house.json")))
                .materials(0, 256)
                .build();

        presets.add(
                PlatformPreset.preset("factory_standard_library")
                        .displayName(add("工厂标准预设库", "Factory standard preset library"))
                        .description(add("基础预设工厂", "Basic preset Factory"))
                        .addStructure(house)
                        .build());

        presets.add(
                PlatformPreset.preset("factory_extension_library")
                        .displayName(add("工厂扩展预设库", "Factory extension preset library"))
                        .description(add("扩展预设工厂", "Expanded preset Factory"))
                        .addStructure(house)
                        .build());
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

    public static List<PlatformPreset> initializePresets() {
        List<PlatformPreset> preset = new ArrayList<>();
        preset.addAll(presets);
        return preset;
    }
}

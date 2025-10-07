package com.gtocore.common.machine.mana;

import com.gtocore.common.machine.mana.PlatformBlockType.*;

import com.gtolib.GTOCore;
import com.gtolib.api.lang.CNEN;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class PlatformTemplateStorage {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    private static final List<PlatformPreset> presets = new ArrayList<>();

    private static final String platform = add("平台", "platform");
    private static final String platform_3_3 = add("平台(3*3)", "platform(3*3)");
    private static final String platform_large = add("平台(大)", "platform(large)");
    private static final String road = add("道路", "road");
    private static final String factory = add("工厂", "factory");

    /// "叫 49*49 浅色带公路地板吧，然后cn是 神官。"

    static {

        PlatformBlockStructure house = PlatformBlockStructure.structure("house")
                .type(platform)
                .displayName(add("这是房子", "house"))
                .source(add("a_villager", "某村民", "A villager"))
                .preview(true)
                .resource(GTOCore.id(in("house")))
                .symbolMap(GTOCore.id(in("house.json")))
                .materials(0, 256)
                .build();

        presets.add(
                PlatformPreset.preset("platform_standard_library")
                        .displayName(add("平台标准预设库", "Platform standard preset library"))
                        .description(add("基础预设平台", "Basic preset platform"))
                        .source("maple")
                        .addStructure(house)
                        .build());

        PlatformBlockStructure light_colored_road_floor_1 = PlatformBlockStructure.structure("light_colored_road_floor_1")
                .type(platform)
                .displayName(add("light_colored_road_floor", "浅色带公路地板", "Light-colored road floor"))
                .source("神官")
                .resource(GTOCore.id(in("light_colored_road_floor_1")))
                .symbolMap(GTOCore.id(in("light_colored_road_floor_1.json")))
                .materials(0, 256)
                .build();

        PlatformBlockStructure light_colored_road_floor_2 = PlatformBlockStructure.structure("light_colored_road_floor_2")
                .type(road)
                .displayName(add("light_colored_road_floor", "浅色带公路地板", "Light-colored road floor"))
                .source("神官")
                .resource(GTOCore.id(in("light_colored_road_floor_2")))
                .symbolMap(GTOCore.id(in("light_colored_road_floor_2.json")))
                .materials(0, 256)
                .build();

        PlatformBlockStructure light_colored_road_floor_3 = PlatformBlockStructure.structure("light_colored_road_floor_3")
                .type(platform_3_3)
                .displayName(add("light_colored_road_floor", "浅色带公路地板", "Light-colored road floor"))
                .source("神官")
                .resource(GTOCore.id(in("light_colored_road_floor_3")))
                .symbolMap(GTOCore.id(in("light_colored_road_floor_3.json")))
                .materials(0, 256)
                .build();

        PlatformBlockStructure light_colored_road_floor_4 = PlatformBlockStructure.structure("light_colored_road_floor_4")
                .type(platform_large)
                .displayName(add("light_colored_road_floor", "浅色带公路地板", "Light-colored road floor"))
                .source("神官")
                .resource(GTOCore.id(in("light_colored_road_floor_4")))
                .symbolMap(GTOCore.id(in("light_colored_road_floor_4.json")))
                .materials(0, 256)
                .build();

        presets.add(
                PlatformPreset.preset("platform_extension_library")
                        .displayName(add("平台扩展预设库", "Platform extended preset library"))
                        .description(add("扩展预设平台", "Expanded preset platform"))
                        .addStructure(light_colored_road_floor_1)
                        .addStructure(light_colored_road_floor_2)
                        .addStructure(light_colored_road_floor_3)
                        .build());

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

        PlatformBlockStructure zhengchang = PlatformBlockStructure.structure("zhengchang")
                .displayName("正常")
                .preview(true)
                .resource(GTOCore.id(in("20251006-001254-039-50c31aea.txt")))
                .symbolMap(GTOCore.id(in("20251006-001254-039-50c31aea.json")))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xzhou = PlatformBlockStructure.structure("xzhou")
                .displayName("x轴")
                .preview(true)
                .resource(GTOCore.id(in("20251006-005833-199-df5062fd.txt")))
                .symbolMap(GTOCore.id(in("20251006-005833-199-df5062fd.json")))
                .materials(0, 256)
                .build();

        PlatformBlockStructure zzhou = PlatformBlockStructure.structure("zzhou")
                .displayName("z轴")
                .preview(true)
                .resource(GTOCore.id(in("20251006-005833-207-d5e3d1ba.txt")))
                .symbolMap(GTOCore.id(in("20251006-005833-207-d5e3d1ba.json")))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xzzhou = PlatformBlockStructure.structure("xzzhou")
                .displayName("xz轴")
                .preview(true)
                .resource(GTOCore.id(in("20251006-005836-291-4770a081.txt")))
                .symbolMap(GTOCore.id(in("20251006-005836-291-4770a081.json")))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xuanzhuan90 = PlatformBlockStructure.structure("xuanzhuan90")
                .displayName("旋转90")
                .preview(true)
                .resource(GTOCore.id(in("20251006-151353-921")))
                .symbolMap(GTOCore.id(in("20251006-151353-921.json")))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xuanzhuan180 = PlatformBlockStructure.structure("xuanzhuan180")
                .displayName("旋转180")
                .preview(true)
                .resource(GTOCore.id(in("20251006-151355-820")))
                .symbolMap(GTOCore.id(in("20251006-151355-820.json")))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xuanzhuan270 = PlatformBlockStructure.structure("xuanzhuan270")
                .displayName("旋转270")
                .preview(true)
                .resource(GTOCore.id(in("20251006-151357-911")))
                .symbolMap(GTOCore.id(in("20251006-151357-911.json")))
                .materials(0, 256)
                .build();

        PlatformBlockStructure snowfield_cabin = PlatformBlockStructure.structure("snowfield_cabin")
                .displayName("雪原小屋")
                .preview(true)
                .resource(GTOCore.id(in("20251006-163632-455")))
                .symbolMap(GTOCore.id(in("20251006-163632-455.json")))
                .materials(0, 256)
                .build();

        PlatformBlockStructure ice_sheet = PlatformBlockStructure.structure("ice_sheet")
                .displayName("冰原")
                .preview(true)
                .resource(GTOCore.id(in("20251006-171440-018")))
                .symbolMap(GTOCore.id(in("20251006-171440-018.json")))
                .materials(0, 256)
                .build();

        PlatformBlockStructure village = PlatformBlockStructure.structure("village")
                .displayName("一个村庄")
                .preview(true)
                .resource(GTOCore.id(in("20251006-215326-886")))
                .symbolMap(GTOCore.id(in("20251006-215326-886.json")))
                .materials(0, 256)
                .build();

        presets.add(
                PlatformPreset.preset("cobblestone_single")
                        .displayName("测试")
                        .description("这是一些方块")
                        .source("maple")
                        .addStructure(zhengchang)
                        .addStructure(xzhou)
                        .addStructure(zzhou)
                        .addStructure(xzzhou)
                        .addStructure(xuanzhuan90)
                        .addStructure(xuanzhuan180)
                        .addStructure(xuanzhuan270)
                        .addStructure(snowfield_cabin)
                        .addStructure(ice_sheet)
                        .addStructure(village)
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

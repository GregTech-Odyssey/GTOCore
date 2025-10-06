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

    private static final String platform = add("platform", "平台", "platform");

    static {
        PlatformBlockStructure roadZ = PlatformBlockStructure.structure("roadZ")
                .type(platform)
                .displayName(add("half_a_house", "这是半个房子", "half_a_house"))
                .description("这是半个房子")
                .source("某村民")
                .preview(true)
                .resource(GTOCore.id("platforms/20251005-002738-451-ad553080.txt"))
                .symbolMap(GTOCore.id("platforms/20251005-002738-451-ad553080.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure zhengchang = PlatformBlockStructure.structure("zhengchang")
                .displayName("正常")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-001254-039-50c31aea.txt"))
                .symbolMap(GTOCore.id("platforms/20251006-001254-039-50c31aea.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xzhou = PlatformBlockStructure.structure("xzhou")
                .displayName("x轴")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-005833-199-df5062fd.txt"))
                .symbolMap(GTOCore.id("platforms/20251006-005833-199-df5062fd.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure zzhou = PlatformBlockStructure.structure("zzhou")
                .displayName("z轴")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-005833-207-d5e3d1ba.txt"))
                .symbolMap(GTOCore.id("platforms/20251006-005833-207-d5e3d1ba.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xzzhou = PlatformBlockStructure.structure("xzzhou")
                .displayName("xz轴")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-005836-291-4770a081.txt"))
                .symbolMap(GTOCore.id("platforms/20251006-005836-291-4770a081.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xuanzhuan90 = PlatformBlockStructure.structure("xuanzhuan90")
                .displayName("旋转90")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-151353-921"))
                .symbolMap(GTOCore.id("platforms/20251006-151353-921.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xuanzhuan180 = PlatformBlockStructure.structure("xuanzhuan180")
                .displayName("旋转180")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-151355-820"))
                .symbolMap(GTOCore.id("platforms/20251006-151355-820.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xuanzhuan270 = PlatformBlockStructure.structure("xuanzhuan270")
                .displayName("旋转270")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-151357-911"))
                .symbolMap(GTOCore.id("platforms/20251006-151357-911.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xuanzhuan270xzzhou = PlatformBlockStructure.structure("xuanzhuan270xzzhou")
                .displayName("旋转270xz轴")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-005850-401-49f00fa3.txt"))
                .symbolMap(GTOCore.id("platforms/20251006-005850-401-49f00fa3.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure snowfield_cabin = PlatformBlockStructure.structure("snowfield_cabin")
                .displayName("雪原小屋")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-163632-455"))
                .symbolMap(GTOCore.id("platforms/20251006-163632-455.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure ice_sheet = PlatformBlockStructure.structure("ice_sheet")
                .displayName("冰原")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-171440-018"))
                .symbolMap(GTOCore.id("platforms/20251006-171440-018.json"))
                .materials(0, 256)
                .build();

        // 注册预设组
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
                        .addStructure(xuanzhuan270xzzhou)
                        .addStructure(snowfield_cabin)
                        .addStructure(ice_sheet)
                        .build());

        presets.add(
                PlatformPreset.preset("cobblestone_single22")
                        .displayName("一些方块")
                        .description("这是一些方块")
                        .source("maple")
                        .addStructure(roadZ)
                        .build());
    }

    private static String add(String key, String cn, String en) {
        if (LANG != null) LANG.put(key, new CNEN(cn, en));
        return key;
    }

    public static List<PlatformPreset> initializePresets() {
        return List.copyOf(presets);
    }
}

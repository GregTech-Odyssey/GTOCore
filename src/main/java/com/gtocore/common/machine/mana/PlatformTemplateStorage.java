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

    private static final String platform = "";

    static {
        PlatformBlockStructure roadZ = PlatformBlockStructure.structure("roadZ")
                .displayName("半个房子")
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
                .resource(GTOCore.id("platforms/20251006-001335-122-e140bdfa.txt"))
                .symbolMap(GTOCore.id("platforms/20251006-001335-122-e140bdfa.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure zzhou = PlatformBlockStructure.structure("zzhou")
                .displayName("z轴")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-001342-991-e325cd45.txt"))
                .symbolMap(GTOCore.id("platforms/20251006-001342-991-e325cd45.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xzzhou = PlatformBlockStructure.structure("xzzhou")
                .displayName("xz轴")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-001346-878-e6a59a21.txt"))
                .symbolMap(GTOCore.id("platforms/20251006-001346-878-e6a59a21.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xuanzhuan90 = PlatformBlockStructure.structure("xuanzhuan90")
                .displayName("旋转90")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-001356-529-a712915a.txt"))
                .symbolMap(GTOCore.id("platforms/20251006-001356-529-a712915a.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xuanzhuan180 = PlatformBlockStructure.structure("xuanzhuan180")
                .displayName("旋转180")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-001359-302-77b19911.txt"))
                .symbolMap(GTOCore.id("platforms/20251006-001359-302-77b19911.json"))
                .materials(0, 256)
                .build();

        PlatformBlockStructure xuanzhuan270 = PlatformBlockStructure.structure("xuanzhuan270")
                .displayName("旋转270")
                .preview(true)
                .resource(GTOCore.id("platforms/20251006-001359-302-77b19911.txt"))
                .symbolMap(GTOCore.id("platforms/20251006-001359-302-77b19911.json"))
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

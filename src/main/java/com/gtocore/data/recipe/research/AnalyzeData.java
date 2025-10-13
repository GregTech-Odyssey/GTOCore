package com.gtocore.data.recipe.research;

import com.gtolib.api.lang.CNEN;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import java.util.Map;

import static com.gtocore.data.recipe.builder.research.ExResearchManager.writeAnalyzeResearchToMap;

public final class AnalyzeData {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    static {
        addData("empty", "空", "empty", 0, 0);

        addData("error1", "§k1§r错误§k1§r", "§k1§rError§k1§r", 0, 1);
        addTooltip("error1", "只是一个意外罢了", "It was just an accident");
        addData("error2", "§k22§r错误§k22§r", "§k22§rError§k22§r", 0, 2);
        addTooltip("error2", "只是两个意外罢了", "It was just two accident");
        addData("error3", "§k333§r错误§k333§r", "§k333§rError§k333§r", 0, 3);
        addData("error4", "§k4444§r错误§k4444§r", "§k4444§rError§k4444§r", 0, 4);
        addData("error5", "§k55555§r错误§k55555§r", "§k55555§rError§k55555§r", 0, 5);
    }

    private static void addData(String key, String cn, String en, int dataTier, int dataCrystal) {
        writeAnalyzeResearchToMap(key, dataTier, dataCrystal);
        if (LANG != null) LANG.put(key, new CNEN(cn, en));
    }

    private static void addData(String cn, String en, int dataTier, int dataCrystal) {
        String key = en.replace(' ', '_').toLowerCase();
        writeAnalyzeResearchToMap(key, dataTier, dataCrystal);
        if (LANG != null) LANG.put(key, new CNEN(cn, en));
    }

    private static void addTooltip(String key, String cn, String en) {
        if (LANG != null) LANG.put(key + ".tooltip", new CNEN(cn, en));
    }
}

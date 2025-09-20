package com.gtocore.common.data;

import com.gtolib.GTOCore;
import com.gtolib.api.lang.CNEN;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconType;
import com.gregtechceu.gtceu.api.fluids.FluidState;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKey;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import java.util.Map;

import static com.gtocore.data.lang.LangHandler.addCNEN;

public class GTOFluidStorageKey {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    public static void initLang() {
        LANG.forEach((k, v) -> addCNEN("gtocore.fluid." + k, v.cn(), v.en()));
    }

    // 储能阳极液
    public static final FluidStorageKey ENERGY_STORAGE_ANODE = key(("energy_storage_anode"), "储能阳极液");
    // 储能阴极液
    public static final FluidStorageKey ENERGY_STORAGE_CATHODE = key(("energy_storage_cathode"), "储能阴极液");
    // 释能阳极液
    public static final FluidStorageKey ENERGY_RELEASE_ANODE = key(("energy_release_anode"), "释能阳极液");
    // 释能阴极液
    public static final FluidStorageKey ENERGY_RELEASE_CATHODE = key(("energy_release_cathode"), "释能阴极液");

    private static FluidStorageKey key(String id, String cn) {
        if (LANG != null) {
            LANG.put(id, new CNEN("%s" + cn, "%s " + FormattingUtil.toEnglishName(id)));
        }
        return new FluidStorageKey(GTOCore.id(id),
                MaterialIconType.liquid,
                m -> m.getName() + "_" + id,
                m -> "gtocore.fluid." + id,
                FluidState.LIQUID, 0);
    }
}

package com.gtocore.api.ae2;

import com.gtolib.api.ae2.AEKeyTypeMap;

import appeng.api.stacks.AEKeyType;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMaps;

import java.util.Map;

public class AE2Utils {

    public static <T> Map<AEKeyType, T> AEKeyTypeMapBetter(Map<AEKeyType, T> map) {
        if (map.size() == 1) {
            var e = map.entrySet().iterator().next();
            return Reference2ReferenceMaps.singleton(e.getKey(), e.getValue());
        } else if (map.size() == 2) {
            var item = map.get(AEKeyTypeMap.ITEM_TYPE);
            if (item != null) {
                var fluid = map.get(AEKeyTypeMap.FLUID_TYPE);
                if (fluid != null) {
                    return new AEKeyTypeMap<>(item, fluid);
                }
            }
        }
        return new Reference2ReferenceArrayMap<>(map);
    }
}

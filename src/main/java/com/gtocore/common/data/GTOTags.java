package com.gtocore.common.data;

import com.gtolib.utils.RLUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;

public final class GTOTags {

    private static final ResourceLocation RED_ALLOY_INGOT = RLUtils.parse("morered:red_alloy_ingot");

    public static boolean cache;

    public static TagEntry convert(ResourceLocation id, TagEntry tagEntry) {
        if (cache) return tagEntry;
        if (tagEntry.getId().equals(RED_ALLOY_INGOT)) {
            cache = true;
            return null;
        }
        return tagEntry;
    }
}

package com.gto.gtocore.utils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class TagUtils {

    private TagUtils() {}

    public static TagKey<Item> createTag(ResourceLocation id) {
        return TagKey.create(BuiltInRegistries.ITEM.key(), id);
    }

    public static TagKey<Item> createTGTag(String path) {
        return createTag(new ResourceLocation("gt", path));
    }
}

package com.gto.gtocore.data.tag;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.utils.TagUtils;

import com.gregtechceu.gtceu.api.data.tag.TagUtil;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public final class Tags {

    private Tags() {}

    public static final Map<TagKey<Item>, Integer> CIRCUITS_ARRAY;

    public static final TagKey<Block> ALL_LAYER_STONE = TagUtil.createBlockTag("all_layer_stone");
    public static final TagKey<Item> HUMAN_EGG = TagUtils.createTag(GTOCore.id("human_egg"));

    static {
        ImmutableMap.Builder<TagKey<Item>, Integer> circuits_array = ImmutableMap.builder();
        for (int i = 0; i < 15; i++) {
            circuits_array.put(CustomTags.CIRCUITS_ARRAY[i], i);
        }
        CIRCUITS_ARRAY = circuits_array.build();
    }
}

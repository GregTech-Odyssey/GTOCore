package com.gto.gtocore.common.block;

import com.gto.gtocore.common.data.GTOBlocks;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import com.google.common.collect.ImmutableMap;

public final class ColorBlockMap {

    public static final ImmutableMap<DyeColor, Block> ABS_MAP;

    static {
        ImmutableMap.Builder<DyeColor, Block> ABSBuilder = ImmutableMap.builder();

        ABSBuilder.put(DyeColor.BLACK, GTOBlocks.ABS_BLACK_CASING.get());
        ABSBuilder.put(DyeColor.BLUE, GTOBlocks.ABS_BLUE_CASING.get());
        ABSBuilder.put(DyeColor.BROWN, GTOBlocks.ABS_BROWN_CASING.get());
        ABSBuilder.put(DyeColor.GREEN, GTOBlocks.ABS_GREEN_CASING.get());
        ABSBuilder.put(DyeColor.GRAY, GTOBlocks.ABS_GREY_CASING.get());
        ABSBuilder.put(DyeColor.LIME, GTOBlocks.ABS_LIME_CASING.get());
        ABSBuilder.put(DyeColor.ORANGE, GTOBlocks.ABS_ORANGE_CASING.get());
        ABSBuilder.put(DyeColor.RED, GTOBlocks.ABS_RAD_CASING.get());
        ABSBuilder.put(DyeColor.WHITE, GTOBlocks.ABS_WHITE_CASING.get());
        ABSBuilder.put(DyeColor.YELLOW, GTOBlocks.ABS_YELLOW_CASING.get());
        ABSBuilder.put(DyeColor.CYAN, GTOBlocks.ABS_CYAN_CASING.get());
        ABSBuilder.put(DyeColor.MAGENTA, GTOBlocks.ABS_MAGENTA_CASING.get());
        ABSBuilder.put(DyeColor.PINK, GTOBlocks.ABS_PINK_CASING.get());
        ABSBuilder.put(DyeColor.PURPLE, GTOBlocks.ABS_PURPLE_CASING.get());
        ABSBuilder.put(DyeColor.LIGHT_BLUE, GTOBlocks.ABS_LIGHT_BULL_CASING.get());
        ABSBuilder.put(DyeColor.LIGHT_GRAY, GTOBlocks.ABS_LIGHT_GREY_CASING.get());

        ABS_MAP = ABSBuilder.build();
    }
}

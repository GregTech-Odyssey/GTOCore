package com.gtocore.common.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import org.jetbrains.annotations.NotNull;

public final class ApothItem extends ColoringItems {

    private ApothItem(Properties properties, int color) {
        super(properties, color, 0);
    }

    public static ApothItem create(Properties properties, int color) {
        return new ApothItem(properties, color);
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        return Rarity.UNCOMMON;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 16;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }
}

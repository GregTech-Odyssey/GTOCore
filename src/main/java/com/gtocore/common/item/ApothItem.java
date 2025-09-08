package com.gtocore.common.item;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.NotNull;

public class ApothItem extends ComponentItem {

    private final int itemColor;

    protected ApothItem(Properties properties, int color) {
        super(properties);
        this.itemColor = color;
    }

    public static ApothItem create(Properties properties, int color) {
        return new ApothItem(properties, color);
    }

    @OnlyIn(Dist.CLIENT)
    public static ItemColor color() {
        return (itemStack, index) -> {
            if (!(itemStack.getItem() instanceof ApothItem apoth) || index >= 1) {
                return -1;
            }
            return apoth.itemColor;
        };
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }
}

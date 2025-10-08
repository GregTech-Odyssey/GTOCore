package com.gtocore.common.item;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ColoringItems extends ComponentItem {

    private final int itemColor;
    private final int tintLayer;

    protected ColoringItems(Item.Properties properties, int color, int tintLayer) {
        super(properties);
        this.itemColor = color;
        this.tintLayer = tintLayer;
    }

    public static ColoringItems create(Item.Properties properties, int color, int tintLayer) {
        return new ColoringItems(properties, color, tintLayer);
    }

    @OnlyIn(Dist.CLIENT)
    public static ItemColor color() {
        return (itemStack, index) -> {
            if (!(itemStack.getItem() instanceof ColoringItems item)) {
                return -1;
            }
            return (index == item.tintLayer) ? item.itemColor : -1;
        };
    }
}

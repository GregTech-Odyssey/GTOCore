package com.gtocore.api.data;

import com.gtocore.common.data.GTOItems;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;

import appeng.api.stacks.AEItemKey;

import java.util.function.Supplier;

public enum Algae {

    BlueAlge(170, 180, 10, GTOItems.BLUE_ALGAE),
    BrownAlge(10, 140, 240, GTOItems.BROWN_ALGAE),
    GoldAlge(80, 70, 190, GTOItems.GOLD_ALGAE),
    GreenAlge(180, 40, 220, GTOItems.GREEN_ALGAE),
    RedAlge(40, 200, 230, GTOItems.RED_ALGAE);

    public static void init() {
        for (Algae algae : VALUES) {
            algae.item = algae.itemSupplier.get();
            algae.itemSupplier = null;
        }
    }

    private static final Algae[] VALUES = values();

    public final int redAbsorption;
    public final int greenAbsorption;
    public final int blueAbsorption;
    private Supplier<Item> itemSupplier;
    private Item item;

    Algae(int redAbsorption, int greenAbsorption, int blueAbsorption, Supplier<Item> itemSupplier) {
        this.redAbsorption = redAbsorption;
        this.greenAbsorption = greenAbsorption;
        this.blueAbsorption = blueAbsorption;
        this.itemSupplier = itemSupplier;
    }

    public AEItemKey aeKey() {
        return AEItemKey.of(item);
    }

    public static boolean isAlgae(AEItemKey key) {
        var item = key.getItem();
        for (Algae algae : VALUES) {
            if (algae.item == item) {
                return true;
            }
        }
        return false;
    }

    public int getColor() {
        return ((255 - redAbsorption) << 16) | ((255 - greenAbsorption) << 8) | (255 - blueAbsorption);
    }

    public Component getDisplayName() {
        return Component.empty().append(this.aeKey().getDisplayName()).withStyle(Style.EMPTY.withColor(this.getColor()));
    }
}

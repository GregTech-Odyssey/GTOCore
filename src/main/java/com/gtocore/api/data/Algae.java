package com.gtocore.api.data;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterEnumLang;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;

import appeng.api.stacks.AEItemKey;

import java.util.function.Supplier;

@DataGeneratorScanned
@RegisterEnumLang(keyPrefix = "gtocore.algae")
public enum Algae {

    BlueAlgae(170, 180, 10, "蓝藻", "Blue"),
    BrownAlgae(10, 140, 240, "褐藻", "Brown"),
    GoldAlgae(80, 70, 190, "金藻", "Gold"),
    GreenAlgae(180, 40, 220, "绿藻", "Green"),
    RedAlgae(40, 200, 230, "红藻", "Red");

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
    public Supplier<Item> itemSupplier;
    private Item item;
    @RegisterEnumLang.CnValue("short")
    public final String cn;
    @RegisterEnumLang.EnValue("short")
    public final String en;
    @RegisterEnumLang.CnValue("full")
    public final String cnFull;
    @RegisterEnumLang.EnValue("full")
    public final String enFull;

    Algae(int redAbsorption, int greenAbsorption, int blueAbsorption, String cn, String en) {
        this.redAbsorption = redAbsorption;
        this.greenAbsorption = greenAbsorption;
        this.blueAbsorption = blueAbsorption;
        this.cn = cn;
        this.cnFull = cn;
        this.en = en;
        this.enFull = en + " Algae";
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
        return Component.empty().append(Component.translatable("gtocore.algae.short." + this.name())).withStyle(Style.EMPTY.withColor(this.getColor()));
    }
}

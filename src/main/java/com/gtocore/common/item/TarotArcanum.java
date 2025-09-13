package com.gtocore.common.item;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import dev.shadowsoffire.placebo.color.GradientColor;
import org.jetbrains.annotations.NotNull;

public final class TarotArcanum extends ComponentItem {

    private final int itemColor;
    private final int serialNumber;

    private TarotArcanum(Properties properties, int color, int serialNumber) {
        super(properties);
        this.itemColor = color;
        this.serialNumber = serialNumber;
    }

    public static TarotArcanum create(Properties properties, int color, int serialNumber) {
        return new TarotArcanum(properties, color, serialNumber);
    }

    @OnlyIn(Dist.CLIENT)
    public static ItemColor color() {
        return (itemStack, index) -> {
            if (!(itemStack.getItem() instanceof TarotArcanum tarot) || index >= 1) {
                return -1;
            }
            return tarot.itemColor;
        };
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        return Rarity.EPIC;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack pStack) {
        return Component.translatable(this.getDescriptionId(pStack)).withStyle(s -> s.withColor(GradientColor.RAINBOW));
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 8;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }
}

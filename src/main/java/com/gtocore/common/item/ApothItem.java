package com.gtocore.common.item;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixRegistry;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import org.jetbrains.annotations.Nullable;

public class ApothItem extends ComponentItem {

    public static final Reference2IntMap<DynamicHolder<Affix>> AFFIX_COLOR_CACHE = new Reference2IntOpenHashMap<>();
    public static final Reference2IntMap<Enchantment> ENCHANTMENT_COLOR_CACHE = new Reference2IntOpenHashMap<>();
    protected static final int DEFAULT_COLOR = 0xFF20B2AA;

    protected ApothItem(Properties properties) {
        super(properties);
    }

    public static ApothItem create(Properties properties) {
        return new ApothItem(properties);
    }

    @OnlyIn(Dist.CLIENT)
    public static ItemColor color() {
        return (itemStack, index) -> {
            if (!(itemStack.getItem() instanceof ApothItem apoth) || index >= 1) {
                return -1;
            }
            if (apoth.getAffix(itemStack) != null) {
                DynamicHolder<Affix> affix = AffixRegistry.INSTANCE.holder(apoth.getAffix(itemStack));
                return AFFIX_COLOR_CACHE.getOrDefault(affix, DEFAULT_COLOR);
            }
            if (apoth.getEnchantment(itemStack) != null) {
                Enchantment enchantment = apoth.getEnchantment(itemStack);
                return ENCHANTMENT_COLOR_CACHE.getOrDefault(enchantment, DEFAULT_COLOR);
            }
            return DEFAULT_COLOR;
        };
    }

    @Nullable
    public Affix getAffix(ItemStack stack) {
        try {
            return AffixRegistry.INSTANCE.holder(ResourceLocation.parse(stack.getOrCreateTag().getString("a"))).get();
        } catch (NullPointerException e) {
            return null;
        }
        // example usage ↑
        // TODO?
    }

    @Nullable
    public Enchantment getEnchantment(ItemStack stack) {
        return null; // TODO?
    }

    public static <AFFIX extends Affix> void registerAffixColor(DynamicHolder<AFFIX> affix, int color) {
        AFFIX_COLOR_CACHE.put((DynamicHolder<Affix>) affix, color | -16777216);
    }

    public static void registerEnchantmentColor(Enchantment enchantment, int color) {
        ENCHANTMENT_COLOR_CACHE.put(enchantment, color | -16777216);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}

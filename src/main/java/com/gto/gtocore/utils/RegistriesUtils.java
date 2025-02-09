package com.gto.gtocore.utils;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.utils.SupplierMemoizer;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public final class RegistriesUtils {

    public static Item getItem(String s) {
        Item i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
        if (i == Items.AIR) {
            GTOCore.LOGGER.atError().log("未找到ID为{}的物品", s);
            return Items.BARRIER;
        }
        return i;
    }

    public static ItemStack getItemStack(String s) {
        return getItemStack(s, 1);
    }

    public static ItemStack getItemStack(String s, int a) {
        return new ItemStack(getItem(s), a);
    }

    public static ItemStack getItemStack(String s, int a, String nbt) {
        ItemStack stack = getItemStack(s, a);
        try {
            stack.setTag(TagParser.parseTag(nbt));
        } catch (Exception ignored) {}
        return stack;
    }

    public static Supplier<? extends Block> getSupplierBlock(String s) {
        return SupplierMemoizer.memoize(() -> getBlock(s));
    }

    public static Block getBlock(String s) {
        return BuiltInRegistries.BLOCK.get(new ResourceLocation(s));
    }

    public static Fluid getFluid(String s) {
        return BuiltInRegistries.FLUID.get(new ResourceLocation(s));
    }
}

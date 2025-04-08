package com.gto.gtocore.utils;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.utils.memoization.GTMemoizer;

import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public final class RegistriesUtils {

    private RegistriesUtils() {}

    public static Item getItem(String s) {
        return getItem(new ResourceLocation(s));
    }

    public static Item getItem(String mod, String name) {
        return getItem(new ResourceLocation(mod, name));
    }

    public static Item getItem(ResourceLocation id) {
        Item i = ForgeRegistries.ITEMS.getValue(id);
        if (i == Items.AIR) {
            GTOCore.LOGGER.atError().log("未找到ID为{}的物品", id);
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
        return GTMemoizer.memoize(() -> getBlock(s));
    }

    public static Block getBlock(String s) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
    }

    public static Fluid getFluid(String s) {
        var fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(s));
        return fluid == null ? Fluids.EMPTY : fluid;
    }
}

package com.gto.gtocore.utils;

import com.gto.gtocore.GTOCore;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

public class Registries {

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

    public static Block getBlock(String s) {
        Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
        if (b == Blocks.AIR) {
            GTOCore.LOGGER.atError().log("未找到ID为{}的方块", s);
            return Blocks.BARRIER;
        }
        return b;
    }

    public static Fluid getFluid(String s) {
        Fluid f = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(s));
        if (f == Fluids.EMPTY) {
            GTOCore.LOGGER.atError().log("未找到ID为{}的流体", s);
            return Fluids.WATER;
        }
        return f;
    }
}

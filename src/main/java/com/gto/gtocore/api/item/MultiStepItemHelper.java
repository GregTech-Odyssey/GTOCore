package com.gto.gtocore.api.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public final class MultiStepItemHelper {

    public static ItemStack toMultiStepItem(ItemStack stack, int step, int max) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putInt("craft_step", max);
        nbt.putInt("current_craft_step", step);
        return stack;
    }

    public static ItemStack setStep(ItemStack stack, int step, int max) {
        CompoundTag nbt = stack.getTag();
        if (nbt == null || max == 0) {
            throw new IllegalArgumentException("ItemStack is not a MultiStepItem");
        }
        nbt.putInt("current_craft_step", Math.min(max, step));
        return stack;
    }

    public static ItemStack locateStep(ItemStack stack0, int step) {
        ItemStack stack = stack0.copy();
        if (stack.getTag() != null) {
            stack.getTag().putInt("current_craft_step", step);
        }
        return stack;
    }

    public static ItemStack locateStep(ItemStack stack0, int step, String name) {
        ItemStack stack = stack0.copy();
        CompoundTag nbt = stack.getOrCreateTag();
        if (stack.getTag() != null) {
            stack.getTag().putInt("current_craft_step", step);
        }
        if (name != null && !name.isEmpty()) {
            CompoundTag display = nbt.getCompound("display");
            String jsonName = "{\"text\":\"" + name + "\",\"italic\":false}";
            display.putString("Name", jsonName);
            nbt.put("display", display);
        }
        stack.setTag(nbt);
        return stack;
    }

    public static ItemStack promoteStep(ItemStack stack) {
        int step = getStep(stack) + 1;
        int max = getMaxStep(stack);
        if (step < max) {
            return setStep(stack, step, max);
        }
        CompoundTag nbt = stack.getTag();
        if (nbt != null) {
            nbt.remove("craft_step");
            nbt.remove("current_craft_step");
            if (nbt.isEmpty()) {
                stack.setTag(null);
            }
        }
        return stack;
    }

    public static int getStep(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt == null) return 0;
        return nbt.getInt("current_craft_step");
    }

    public static int getMaxStep(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt == null) return 0;
        return nbt.getInt("craft_step");
    }
}

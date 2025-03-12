package com.gto.gtocore.api.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record NBTItem(Item item, CompoundTag nbt) {

    public static NBTItem of(ItemStack stack) {
        return new NBTItem(stack.getItem(), stack.hasTag() ? stack.getTag() : null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NBTItem content && item == content.item) {
            if (nbt == null) {
                return content.nbt == null;
            } else {
                return nbt.equals(content.nbt);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (item == null ? 0 : item.hashCode());
        result = 31 * result + (nbt == null ? 0 : nbt.hashCode());
        return result;
    }
}

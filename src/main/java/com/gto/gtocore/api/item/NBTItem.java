package com.gto.gtocore.api.item;

import com.gto.gtocore.utils.ItemUtils;
import com.gto.gtocore.utils.RegistriesUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record NBTItem(Item item, CompoundTag nbt) {

    public static NBTItem of(ItemStack stack) {
        return new NBTItem(stack.getItem(), stack.hasTag() ? stack.getTag() : null);
    }

    public static NBTItem of(CompoundTag tag) {
        return new NBTItem(RegistriesUtils.getItem(tag.getString("id")), tag.getCompound("tag"));
    }

    public ItemStack toStack() {
        return toStack(1);
    }

    public ItemStack toStack(int count) {
        return new ItemStack(item, count, nbt);
    }

    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();
        tag.putString("id", ItemUtils.getId(item));
        if (nbt != null) {
            tag.put("tag", nbt);
        }
        return tag;
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

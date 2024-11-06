package com.gto.gtocore.integration.ae2.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class InfinityCellDataStorage {

    public static final InfinityCellDataStorage EMPTY = new InfinityCellDataStorage();

    public ListTag stackKeys;
    public long[] stackAmounts;

    public InfinityCellDataStorage() {
        stackKeys = new ListTag();
        stackAmounts = new long[0];
    }

    public InfinityCellDataStorage(ListTag stackKeys, long[] stackAmounts) {
        this.stackKeys = stackKeys;
        this.stackAmounts = stackAmounts;
    }

    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("keys", stackKeys);
        nbt.putLongArray("amounts", stackAmounts);
        return nbt;
    }

    public static InfinityCellDataStorage fromNbt(CompoundTag nbt) {
        ListTag stackKeys = nbt.getList("keys", Tag.TAG_COMPOUND);
        long[] stackAmounts = nbt.getLongArray("amounts");
        return new InfinityCellDataStorage(stackKeys, stackAmounts);
    }
}

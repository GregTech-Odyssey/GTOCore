package com.gto.gtocore.integration.ae2;

import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;

import net.minecraft.nbt.ListTag;

import appeng.api.stacks.AEKey;
import it.unimi.dsi.fastutil.objects.Object2LongMap;

public final class KeyMap extends KeyStorage {

    public static synchronized void put(Object2LongMap<AEKey> storage, AEKey key, long value) {
        storage.put(key, value);
    }

    @Override
    public void deserializeNBT(ListTag tags) {
        for (int i = 0; i < tags.size(); i++) {
            var tag = tags.getCompound(i);
            var key = AEKey.fromTagGeneric(tag.getCompound("key"));
            long value = tag.getLong("value");
            put(storage, key, value);
        }
    }
}

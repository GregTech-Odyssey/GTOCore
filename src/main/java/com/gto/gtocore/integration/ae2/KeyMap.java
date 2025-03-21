package com.gto.gtocore.integration.ae2;

import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;

import net.minecraft.nbt.ListTag;

import appeng.api.stacks.AEKey;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class KeyMap extends KeyStorage {

    @Override
    public @NotNull ListTag serializeNBT() {
        synchronized (storage) {
            return super.serializeNBT();
        }
    }

    @Override
    public void deserializeNBT(ListTag tags) {
        for (int i = 0; i < tags.size(); i++) {
            var tag = tags.getCompound(i);
            var key = AEKey.fromTagGeneric(tag.getCompound("key"));
            long value = tag.getLong("value");
            synchronized (storage) {
                storage.put(key, value);
            }
        }
    }

    @Override
    public @NotNull Iterator<Object2LongMap.Entry<AEKey>> iterator() {
        synchronized (storage) {
            return super.iterator();
        }
    }
}

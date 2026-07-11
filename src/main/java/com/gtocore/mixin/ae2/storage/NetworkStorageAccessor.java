package com.gtocore.mixin.ae2.storage;

import appeng.me.storage.NetworkStorage;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = NetworkStorage.class, remap = false)
public interface NetworkStorageAccessor {

    @Accessor("priorityInventory")
    ObjectArrayList<?> gtocore$getPriorityInventory();
}

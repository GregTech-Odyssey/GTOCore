package com.gtocore.mixin.ae2.storage;

import com.gtocore.common.machine.multiblock.part.ae.StorageAccessPartMachine;

import com.gtolib.utils.holder.IntObjectHolder;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.me.storage.NetworkStorage;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

@Mixin(NetworkStorage.class)
public abstract class NetworkStorageMixin {

    @Unique
    private List<IntObjectHolder<MEStorage>> gtolib$inventory;

    @Mutable
    @Shadow(remap = false)
    @Final
    private NavigableMap<Integer, List<MEStorage>> priorityInventory;

    @Shadow(remap = false)
    private boolean mountsInUse;

    @Shadow(remap = false)
    protected abstract boolean isQueuedForRemoval(MEStorage inv);

    @Shadow(remap = false)
    protected abstract void flushQueuedOperations();

    @Unique
    private boolean gtocore$inUse;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtolib$init(CallbackInfo ci) {
        gtolib$inventory = new ArrayList<>();
        priorityInventory = null;
    }

    @Inject(method = "mount", at = @At(value = "INVOKE", target = "Ljava/util/NavigableMap;values()Ljava/util/Collection;"), remap = false, cancellable = true)
    private void gtolib$mount(int priority, MEStorage inventory, CallbackInfo ci) {
        ci.cancel();
        if (inventory instanceof StorageAccessPartMachine m1) {
            for (var inv : gtolib$inventory) {
                if (inv.obj instanceof StorageAccessPartMachine m2 && m1.getClass() == m2.getClass() && m1.uuid.equals(m2.uuid)) {
                    return;
                }
            }
        } else {
            var owner = inventory.getStorageOwner();
            for (var inv : gtolib$inventory) {
                if (inv.obj.getStorageOwner() == owner) {
                    return;
                }
            }
        }
        gtolib$inventory.add(new IntObjectHolder<>(priority, inventory));
        gtolib$inventory.sort(IntObjectHolder.PRIORITY_SORTER);
    }

    @Inject(method = "unmount", at = @At(value = "INVOKE", target = "Ljava/util/NavigableMap;entrySet()Ljava/util/Set;"), remap = false, cancellable = true)
    private void gtolib$unmount(MEStorage inventory, CallbackInfo ci) {
        var ii = gtolib$inventory.iterator();
        while (ii.hasNext()) {
            if (ii.next().obj == inventory) ii.remove();
        }
        ci.cancel();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public long insert(AEKey what, long amount, Actionable type, IActionSource src) {
        if (mountsInUse) return 0;
        var remaining = amount;
        this.mountsInUse = true;
        try {
            var ii = gtolib$inventory.iterator();
            while (ii.hasNext() && remaining > 0) {
                var inv = ii.next().obj;
                if (isQueuedForRemoval(inv)) continue;
                remaining -= inv.insert(what, remaining, type, src);
            }
        } finally {
            this.mountsInUse = false;
        }
        flushQueuedOperations();
        return amount - remaining;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (mountsInUse) return 0;
        var extracted = 0L;
        this.mountsInUse = true;
        try {
            for (int i = gtolib$inventory.size() - 1; i >= 0 && extracted < amount; i--) {
                var inv = gtolib$inventory.get(i).obj;
                if (isQueuedForRemoval(inv)) {
                    continue;
                }
                extracted += inv.extract(what, amount - extracted, mode, source);
            }
        } finally {
            this.mountsInUse = false;
        }
        flushQueuedOperations();
        return extracted;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void getAvailableStacks(KeyCounter out) {
        if (gtocore$inUse) return;
        gtocore$inUse = true;
        try {
            gtolib$inventory.forEach(entry -> entry.obj.getAvailableStacks(out));
        } finally {
            gtocore$inUse = false;
        }
    }
}

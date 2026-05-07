package com.gtocore.mixin.ae2.eae;

import com.gtocore.integration.ae.hooks.IPreciseBus;

import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;

import com.glodblock.github.extendedae.api.StorageMode;
import com.glodblock.github.extendedae.common.parts.PartPreciseStorageBus;
import com.glodblock.github.extendedae.common.parts.base.PartSpecialStorageBus;
import org.spongepowered.asm.mixin.*;

@Mixin(PartPreciseStorageBus.class)
public abstract class PreciseStorageMixin implements IPreciseBus {

    @Shadow(remap = false)
    private StorageMode storageMode;

    @Override
    public StorageMode gtocore$getStorageMode() {
        return storageMode;
    }

    @Mixin(PartPreciseStorageBus.PreciseInventory.class)
    public static class PreciseInvMixin extends PartSpecialStorageBus.StorageBusInventory {

        @Shadow(remap = false)
        @Final
        PartPreciseStorageBus this$0;

        public PreciseInvMixin(MEStorage inventory) {
            super(inventory);
        }

        /**
         * @author .
         * @reason use proper method signature
         */
        @Overwrite(remap = false)
        public void getAvailableStacks(KeyCounter out) {
            var storageMode = ((IPreciseBus) this$0).gtocore$getStorageMode();
            if (storageMode == StorageMode.DEFAULT || storageMode == null) {
                super.getAvailableStacks(out);
                return;
            }
            var filter = (PartPreciseStorageBus.PreciseFilter) this.getPartitionList();
            var current = new KeyCounter();
            super.getAvailableStacks(current);
            for (var entry : current) {
                long value = entry.getLongValue();
                long threshold = filter.getAmount(entry.getKey());
                if (storageMode.test(value, threshold)) {
                    out.add(entry.getKey(), value);
                }
            }
        }
    }
}

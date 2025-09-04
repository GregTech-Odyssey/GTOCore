package com.gtocore.mixin.ae2.storage;

import com.gtolib.api.ae2.AEKeyTypeMap;

import appeng.api.stacks.AEKeyType;
import appeng.api.storage.MEStorage;
import appeng.me.storage.CompositeStorage;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMaps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(CompositeStorage.class)
public class CompositeStorageMixin {

    @Shadow(remap = false)
    private Map<AEKeyType, MEStorage> storages;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void init(Map storages, CallbackInfo ci) {
        if (storages instanceof AEKeyTypeMap) return;
        setStorages(this.storages);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void setStorages(Map<AEKeyType, MEStorage> storages) {
        if (storages == null) {
            this.storages = null;
            return;
        }

        Map<AEKeyType, MEStorage> optimized = null;

        if (storages.size() == 1) {
            var entry = storages.entrySet().iterator().next();
            optimized = Reference2ReferenceMaps.singleton(entry.getKey(), entry.getValue());
        } else if (storages.size() == 2) {
            var item = storages.get(AEKeyTypeMap.ITEM_TYPE);
            var fluid = storages.get(AEKeyTypeMap.FLUID_TYPE);
            if (item != null && fluid != null) {
                optimized = new AEKeyTypeMap<>(item, fluid);
            }
        }

        this.storages = (optimized != null) ? optimized : storages;
    }
}

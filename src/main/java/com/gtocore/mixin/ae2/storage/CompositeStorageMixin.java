package com.gtocore.mixin.ae2.storage;

import com.gtocore.api.ae2.AE2Utils;

import com.gtolib.api.ae2.AEKeyTypeMap;

import appeng.api.stacks.AEKeyType;
import appeng.api.storage.MEStorage;
import appeng.me.storage.CompositeStorage;
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
    public void init(Map<AEKeyType, MEStorage> storages, CallbackInfo ci) {
        if (storages instanceof AEKeyTypeMap) return;
        setStorages(this.storages);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void setStorages(Map<AEKeyType, MEStorage> storages) {
        this.storages = AE2Utils.AEKeyTypeMapBetter(storages);
    }
}

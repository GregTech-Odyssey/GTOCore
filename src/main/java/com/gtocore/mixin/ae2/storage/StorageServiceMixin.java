package com.gtocore.mixin.ae2.storage;

import com.gtocore.api.ae2.stacks.FuzzyKeyCounter;

import com.gtolib.api.ae2.IExpandedStorageService;

import net.minecraft.server.MinecraftServer;

import appeng.api.stacks.KeyCounter;
import appeng.me.service.StorageService;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StorageService.class)
public abstract class StorageServiceMixin implements IExpandedStorageService {

    @Shadow(remap = false)
    @Final
    private KeyCounter cachedAvailableStacks;

    @Shadow(remap = false)
    public abstract KeyCounter getCachedInventory();

    @Unique
    private FuzzyKeyCounter gtolib$fuzzyKeyCounter;
    @Unique
    private boolean gtolib$fuzzyStacksNeedUpdate;
    @Unique
    private boolean gtolib$lazyStacksNeedUpdate;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(CallbackInfo ci) {
        gtolib$fuzzyStacksNeedUpdate = true;
        gtolib$lazyStacksNeedUpdate = true;
    }

    @Inject(method = "onServerEndTick", at = @At("HEAD"), remap = false)
    private void onServerEndTick(MinecraftServer server, CallbackInfo ci) {
        gtolib$fuzzyStacksNeedUpdate = true;
        var secondCycle = server.getTickCount() % 20 == 0;
        if (secondCycle) gtolib$lazyStacksNeedUpdate = true;
    }

    @Override
    public FuzzyKeyCounter getFuzzyKeyCounter() {
        if (gtolib$fuzzyStacksNeedUpdate) {
            gtolib$fuzzyStacksNeedUpdate = false;
            if (gtolib$fuzzyKeyCounter == null) {
                gtolib$fuzzyKeyCounter = new FuzzyKeyCounter(cachedAvailableStacks);
            } else {
                gtolib$fuzzyKeyCounter.clear();
                gtolib$fuzzyKeyCounter.addAll(cachedAvailableStacks);
            }
        }
        return gtolib$fuzzyKeyCounter;
    }

    @Override
    public KeyCounter getLazyKeyCounter() {
        if (gtolib$lazyStacksNeedUpdate) {
            gtolib$lazyStacksNeedUpdate = false;
            return getCachedInventory();
        }
        return cachedAvailableStacks;
    }
}

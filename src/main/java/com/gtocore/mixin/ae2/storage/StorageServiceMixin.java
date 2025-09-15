package com.gtocore.mixin.ae2.storage;

import com.gtocore.api.ae2.stacks.FuzzyKeyCounter;

import com.gtolib.api.ae2.IExpandedStorageService;

import appeng.api.networking.storage.IStorageWatcherNode;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.hooks.ticking.TickHandler;
import appeng.me.helpers.InterestManager;
import appeng.me.helpers.StackWatcher;
import appeng.me.service.StorageService;
import appeng.me.storage.NetworkStorage;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StorageService.class)
public abstract class StorageServiceMixin implements IExpandedStorageService {

    @Shadow(remap = false)
    private boolean cachedStacksNeedUpdate;

    @Shadow(remap = false)
    @Final
    private KeyCounter cachedAvailableStacks;

    @Shadow(remap = false)
    @Final
    private NetworkStorage storage;

    @Shadow(remap = false)
    @Final
    private Object2LongMap<AEKey> cachedAvailableAmounts;

    @Shadow(remap = false)
    protected abstract void postWatcherUpdate(AEKey what, long newAmount);

    @Shadow(remap = false)
    @Final
    private InterestManager<StackWatcher<IStorageWatcherNode>> interestManager;

    @Unique
    private FuzzyKeyCounter gtolib$fuzzyKeyCounter;
    @Unique
    private Object gtolib$Lock;
    @Unique
    private boolean gtolib$fuzzyStacksNeedUpdate;
    @Unique
    private boolean gtolib$lazyStacksNeedUpdate;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(CallbackInfo ci) {
        gtolib$Lock = new Object();
        gtolib$fuzzyStacksNeedUpdate = true;
        gtolib$lazyStacksNeedUpdate = true;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void onServerEndTick() {
        gtolib$fuzzyStacksNeedUpdate = true;
        var secondCycle = TickHandler.instance().getCurrentTick() % 20 == 0;
        if (secondCycle) gtolib$lazyStacksNeedUpdate = true;
        if (secondCycle && !interestManager.isEmpty()) {
            synchronized (gtolib$Lock) {
                cachedStacksNeedUpdate = false;

                cachedAvailableStacks.clear();
                storage.getAvailableStacks(cachedAvailableStacks);
                cachedAvailableStacks.removeEmptySubmaps();

                for (var it = ((Object2LongOpenHashMap<AEKey>) cachedAvailableAmounts).object2LongEntrySet().fastIterator(); it.hasNext();) {
                    var entry = it.next();
                    var what = entry.getKey();
                    var newAmount = cachedAvailableStacks.get(what);
                    if (newAmount != entry.getLongValue()) {
                        postWatcherUpdate(what, newAmount);
                        if (newAmount == 0) {
                            it.remove();
                        } else {
                            entry.setValue(newAmount);
                        }
                    }
                }

                for (var entry : cachedAvailableStacks) {
                    var what = entry.getKey();
                    var newAmount = entry.getLongValue();
                    if (newAmount != cachedAvailableAmounts.getLong(what)) {
                        postWatcherUpdate(what, newAmount);
                        cachedAvailableAmounts.put(what, newAmount);
                    }
                }
            }
        } else {
            cachedStacksNeedUpdate = true;
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public KeyCounter getCachedInventory() {
        synchronized (gtolib$Lock) {
            if (cachedStacksNeedUpdate) {
                cachedStacksNeedUpdate = false;
                cachedAvailableStacks.clear();
                storage.getAvailableStacks(cachedAvailableStacks);
                cachedAvailableStacks.removeEmptySubmaps();
            }
            return cachedAvailableStacks;
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private void updateCachedStacks() {}

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

    @Override
    public Object gtolib$getLock() {
        return gtolib$Lock;
    }
}

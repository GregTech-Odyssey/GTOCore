package com.gtocore.mixin.ae2;

import com.gtocore.api.ae2.stacks.FuzzyKeyCounter;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.util.prioritylist.FuzzyPriorityList;
import com.google.common.collect.ImmutableSet;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FuzzyPriorityList.class)
public class FuzzyPriorityListMixin {

    @Mutable
    @Shadow(remap = false)
    @Final
    private KeyCounter list;

    @Shadow(remap = false)
    @Final
    private FuzzyMode mode;
    @Unique
    private FuzzyKeyCounter gtolib$counter;
    @Unique
    private boolean gtolib$isEmpty;
    @Unique
    private Iterable<AEKey> gtolib$items;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(KeyCounter in, FuzzyMode mode, CallbackInfo ci) {
        gtolib$counter = new FuzzyKeyCounter(in);
        gtolib$isEmpty = in.isEmpty();
        gtolib$items = ImmutableSet.copyOf(in.keySet());
        list = null;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public boolean isListed(AEKey input) {
        return !gtolib$counter.findFuzzy(input, this.mode).isEmpty();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public boolean isEmpty() {
        return gtolib$isEmpty;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public Iterable<AEKey> getItems() {
        return gtolib$items;
    }
}

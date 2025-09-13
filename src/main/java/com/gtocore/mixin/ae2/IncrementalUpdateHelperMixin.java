package com.gtocore.mixin.ae2;

import com.gregtechceu.gtceu.utils.collection.OpenCacheHashSet;

import appeng.api.stacks.AEKey;
import appeng.menu.me.common.IncrementalUpdateHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(IncrementalUpdateHelper.class)
public class IncrementalUpdateHelperMixin {

    @Mutable
    @Shadow(remap = false)
    @Final
    private Set<AEKey> changes;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtolib$init(CallbackInfo ci) {
        changes = new OpenCacheHashSet<>();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void addChange(AEKey entry) {
        changes.add(entry);
    }
}

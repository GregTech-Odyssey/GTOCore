package com.gtocore.mixin.ae2.storage;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(targets = "appeng.me.cells.CreativeCellInventory")
public abstract class CreativeCellInventoryMixin {
    @Final
    @Shadow(remap = false)
    private Set<AEKey> configured;


    @Inject(at=@At("HEAD"),method="getAvailableStacks",remap = false, cancellable = true)
    public void getAvailableStacks(KeyCounter out, CallbackInfo ci){
        for (AEKey key : this.configured) {
            out.add(key, (long) Integer.MAX_VALUE *Integer.MAX_VALUE);
        }
        ci.cancel();
    }
}
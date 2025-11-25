package com.gtocore.mixin.map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xaero.map.cache.BlockStateShortShapeCache;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Mixin(value = BlockStateShortShapeCache.class, remap = false)
public class BlockStateShortShapeCacheMixin {

    @Shadow
    private CompletableFuture<Boolean> ioThreadWaitingFor;

    @Shadow
    private Supplier<Boolean> ioThreadWaitingForSupplier;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void supplyForIOThread() {
        CompletableFuture<Boolean> waitingFor = this.ioThreadWaitingFor;
        if (waitingFor != null && !waitingFor.isDone()) {
            waitingFor.complete(ioThreadWaitingForSupplier == null || this.ioThreadWaitingForSupplier.get());
        }
    }
}

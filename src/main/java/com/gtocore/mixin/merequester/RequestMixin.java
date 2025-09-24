package com.gtocore.mixin.merequester;

import com.almostreliable.merequester.requester.Requests;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Requests.Request.class)
public abstract class RequestMixin {

    @Shadow(remap = false)
    private long batch;

    @Final
    @Shadow(remap = false)
    Requests this$0;

    /**
     * @author remakefactory
     * @reason clamp to MAX()
     */
    @Overwrite(remap = false)
    public void updateBatch(long batch) {
        long oldBatch = this.batch;
        this.batch = Math.max(1L, batch);
        if (oldBatch != this.batch) {
            this.this$0.onChange();
        }
    }
}

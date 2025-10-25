package com.gtocore.mixin.merequester;

import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.TickingRequest;
import com.almostreliable.merequester.requester.RequesterBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RequesterBlockEntity.class)
public abstract class RequesterBlockEntityMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public TickingRequest getTickingRequest(IGridNode node) {
        return new TickingRequest(100, 200, false, false);
    }
}

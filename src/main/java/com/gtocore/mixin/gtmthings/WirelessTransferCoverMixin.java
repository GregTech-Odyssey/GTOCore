package com.gtocore.mixin.gtmthings;

import com.gtocore.integration.gtmthings.WirelessTransferBindIndex;

import com.gregtechceu.gtceu.api.cover.CoverBehavior;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import com.hepdd.gtmthings.common.cover.AdvancedWirelessTransferCover;
import com.hepdd.gtmthings.common.cover.WirelessTransferCover;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Feeds the {@link WirelessTransferBindIndex} so a bound block can show, via Jade, which covers point at it.
 * Both wireless transfer covers share identical fields and load/attach/remove hooks, so one mixin targets both.
 */
@Mixin({ WirelessTransferCover.class, AdvancedWirelessTransferCover.class })
public abstract class WirelessTransferCoverMixin {

    @Shadow(remap = false)
    protected BlockPos targetPos;
    @Shadow(remap = false)
    private String dimensionId;
    @Shadow(remap = false)
    @Final
    protected int transferType;

    @Inject(method = "onLoad", at = @At("TAIL"), remap = false)
    private void gtocore$onLoad(CallbackInfo ci) {
        WirelessTransferBindIndex.register((CoverBehavior) (Object) this, targetPos, dimensionId, transferType);
    }

    @Inject(method = "onAttached", at = @At("TAIL"), remap = false)
    private void gtocore$onAttached(ItemStack itemStack, ServerPlayer player, CallbackInfo ci) {
        WirelessTransferBindIndex.register((CoverBehavior) (Object) this, targetPos, dimensionId, transferType);
    }

    @Inject(method = "onRemoved", at = @At("TAIL"), remap = false)
    private void gtocore$onRemoved(CallbackInfo ci) {
        WirelessTransferBindIndex.unregister((CoverBehavior) (Object) this, targetPos, dimensionId);
    }
}

package com.gtocore.mixin.mc;

import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.renderer.GameRenderer.class)
public class GameRendererMixin {

    /**
     * Inject at HEAD to disable night vision blink effect
     * while preserving vanilla logic and shader mod compatibility.
     * Early return prevents null pointer issues.
     */
    @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
    private static void onGetNightVisionScale(LivingEntity livingEntity, float nanoTime, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(1.0f);
    }
}

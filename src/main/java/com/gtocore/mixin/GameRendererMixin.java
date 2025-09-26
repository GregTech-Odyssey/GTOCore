package com.gtocore.mixin;

import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Overwrite;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.renderer.GameRenderer.class)
public class GameRendererMixin {

    /**
     * @author .
     * @reason Disable night vision blink effect
     */
    @Overwrite
    public static float getNightVisionScale(LivingEntity livingEntity, float nanoTime) {
        return 1.0f;
    }
}

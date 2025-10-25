package com.gtocore.mixin.sodium;

import com.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SodiumWorldRenderer.class)
public class SodiumWorldRendererMixin {

    @ModifyExpressionValue(method = "renderGlobalBlockEntities", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/viewport/Viewport;isBoxVisible(Lnet/minecraft/world/phys/AABB;)Z", remap = false), remap = false)
    private boolean renderGlobalBlockEntities(boolean original, @Local BlockEntity blockEntity) {
        if (original) return true;
        return GTOConfig.INSTANCE.disableEmbeddiumBECulling && blockEntity instanceof MetaMachineBlockEntity;
    }
}

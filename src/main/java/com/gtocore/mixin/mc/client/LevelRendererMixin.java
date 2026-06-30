package com.gtocore.mixin.mc.client;

import com.gtocore.client.renderer.item.ItemCountRenderer;
import com.gtocore.utils.StxckUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Inject(method = "renderEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void renderItemCount(Entity entity, double x, double y, double z, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, CallbackInfo ci) {
        var entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (entityRenderDispatcher.camera == null) return;
        if (entity instanceof ItemEntity itemEntity) {
            var maxDistance = StxckUtil.getMinItemCountRenderDistance();
            if (entityRenderDispatcher.distanceToSqr(entity) > maxDistance * maxDistance) return;
            var offset = entityRenderDispatcher.getRenderer(entity).getRenderOffset(entity, partialTicks);
            var light = entityRenderDispatcher.getPackedLightCoords(entity, partialTicks);
            var nx = Mth.lerp(partialTicks, entity.xOld, entity.getX()) - x + offset.x();
            var ny = Mth.lerp(partialTicks, entity.yOld, entity.getY()) - y + offset.y();
            var nz = Mth.lerp(partialTicks, entity.zOld, entity.getZ()) - z + offset.z();
            poseStack.pushPose();
            poseStack.translate(nx, ny, nz);
            ItemCountRenderer.renderItemCount(itemEntity, poseStack, bufferSource, light, entityRenderDispatcher);
            poseStack.popPose();
        }
    }
}

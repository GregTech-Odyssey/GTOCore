package com.gtocore.mixin.mc;

import com.gtocore.client.model.PerspectiveModel;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At("HEAD"),
            cancellable = true)
    private void gtocore$renderPerspectiveModel(ItemStack stack, ItemDisplayContext displayContext, boolean leftHand,
                                                PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                                int packedOverlay, BakedModel model, CallbackInfo ci) {
        if (!stack.isEmpty() && model instanceof PerspectiveModel) {
            poseStack.pushPose();
            try {
                BakedModel transformedModel = ForgeHooksClient.handleCameraTransforms(poseStack, model, displayContext, leftHand);
                poseStack.translate(-0.5F, -0.5F, -0.5F);
                if (transformedModel instanceof PerspectiveModel perspectiveModel) {
                    perspectiveModel.renderItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
                    ci.cancel();
                }
            } finally {
                poseStack.popPose();
            }
        }
    }
}

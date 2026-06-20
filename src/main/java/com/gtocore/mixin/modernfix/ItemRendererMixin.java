package com.gtocore.mixin.modernfix;

import com.gregtechceu.gtceu.api.item.MetaMachineItem;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = ItemRenderer.class, priority = 500)
public class ItemRendererMixin {

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V"), index = 0)
    private BakedModel gto$skipModernFixFastItemRenderingForMetaMachines(BakedModel model, ItemStack stack,
                                                                         int combinedLight, int combinedOverlay,
                                                                         PoseStack matrixStack, VertexConsumer buffer,
                                                                         @Local(ordinal = 0) BakedModel originalModel) {
        if (stack.getItem() instanceof MetaMachineItem && originalModel != null &&
                model.getClass().getName().equals("org.embeddedt.modernfix.render.SimpleItemModelView")) {
            return originalModel;
        }
        return model;
    }
}

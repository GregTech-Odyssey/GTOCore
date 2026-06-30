package com.gtocore.client.model;

import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.Nullable;

public interface ShaderItemMaskProvider {

    @Nullable
    RuntimeMask capture(ShaderItemBakedModel model, ItemStack stack, PoseStack poseStack, int packedLight, int packedOverlay);

    record RuntimeMask(int textureId, int textureWidth, int textureHeight, int viewportX, int viewportY) {}
}

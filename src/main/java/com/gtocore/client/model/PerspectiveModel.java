package com.gtocore.client.model;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;

public interface PerspectiveModel extends BakedModel {

    PerspectiveModelState getModelState();

    void renderItem(net.minecraft.world.item.ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                    MultiBufferSource buffer, int packedLight, int packedOverlay);

    @Override
    default List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable net.minecraft.core.Direction side,
                                     net.minecraft.util.RandomSource random) {
        return Collections.emptyList();
    }

    @Override
    default boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    default TextureAtlasSprite getParticleIcon() {
        return getParticleIcon(net.minecraftforge.client.model.data.ModelData.EMPTY);
    }

    @Override
    default ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    default BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        PerspectiveModelState state = getModelState();
        if (state != null) {
            Transformation transform = state.getTransform(transformType);
            Vector3f translation = transform.getTranslation();
            Vector3f scale = transform.getScale();
            poseStack.translate(translation.x(), translation.y(), translation.z());
            poseStack.mulPose(transform.getLeftRotation());
            poseStack.scale(scale.x(), scale.y(), scale.z());
            poseStack.mulPose(transform.getRightRotation());
            if (applyLeftHandTransform) {
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            }
            return this;
        }
        return this;
    }
}

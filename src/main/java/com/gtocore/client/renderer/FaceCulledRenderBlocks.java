package com.gtocore.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
class FaceCulledRenderBlocks extends ItemRenderer {

    private List<Direction> directions = new ArrayList<>();

    FaceCulledRenderBlocks(Minecraft minecraft, TextureManager textureManager, ModelManager modelManager, ItemColors itemColors, BlockEntityWithoutLevelRenderer blockEntityRenderer) {
        super(minecraft, textureManager, modelManager, itemColors, blockEntityRenderer);
    }

    @Override
    public void renderModelLists(@NotNull BakedModel model, @NotNull ItemStack stack, int combinedLight, int combinedOverlay, @NotNull PoseStack poseStack, @NotNull VertexConsumer buffer) {
        RandomSource randomsource = RandomSource.create();
        for (Direction direction : directions) {
            randomsource.setSeed(42L);
            this.renderQuadList(poseStack, buffer, model.getQuads(null, direction, randomsource), stack, combinedLight, combinedOverlay);
        }
        randomsource.setSeed(42L);
        this.renderQuadList(poseStack, buffer, model.getQuads(null, null, randomsource), stack, combinedLight, combinedOverlay);
    }
}

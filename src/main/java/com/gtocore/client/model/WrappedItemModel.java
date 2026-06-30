package com.gtocore.client.model;

import com.gtolib.GTOCore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class WrappedItemModel implements PerspectiveModel {

    private static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();
    private static final FaceBakery FACE_BAKERY = new FaceBakery();

    protected final BakedModel wrapped;
    protected net.minecraft.client.resources.model.ModelState parentState;
    protected LivingEntity entity;
    protected ClientLevel world;

    protected WrappedItemModel(BakedModel wrapped) {
        this.wrapped = wrapped;
        this.parentState = TransformUtils.stateFromItemTransforms(wrapped.getTransforms());
    }

    public static List<BakedQuad> bakeItem(List<TextureAtlasSprite> sprites) {
        List<BakedQuad> quads = new LinkedList<>();
        for (int layerIndex = 0; layerIndex < sprites.size(); layerIndex++) {
            TextureAtlasSprite sprite = sprites.get(layerIndex);
            List<BlockElement> elements = ITEM_MODEL_GENERATOR.processFrames(layerIndex, "layer" + layerIndex, sprite.contents());
            for (BlockElement element : elements) {
                for (Map.Entry<Direction, BlockElementFace> entry : element.faces.entrySet()) {
                    quads.add(FACE_BAKERY.bakeQuad(
                            element.from,
                            element.to,
                            entry.getValue(),
                            sprite,
                            entry.getKey(),
                            new PerspectiveModelState(Collections.emptyMap()),
                            element.rotation,
                            element.shade,
                            GTOCore.id("dynamic")));
                }
            }
        }
        return quads;
    }

    protected void renderWrapped(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                 int packedOverlay, boolean fabulous) {
        renderWrapped(stack, poseStack, buffer, packedLight, packedOverlay, fabulous, Function.identity());
    }

    protected void renderWrapped(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                 int packedOverlay, boolean fabulous,
                                 Function<com.mojang.blaze3d.vertex.VertexConsumer, com.mojang.blaze3d.vertex.VertexConsumer> consumerWrapper) {
        BakedModel resolved = resolveWrappedModel(stack);
        forEachRenderLayer(resolved, stack, fabulous, (renderPass, renderType) -> renderWrappedPass(
                renderPass,
                stack,
                poseStack,
                buffer,
                packedLight,
                packedOverlay,
                renderType,
                consumerWrapper));
    }

    protected BakedModel resolveWrappedModel(ItemStack stack) {
        return wrapped.getOverrides().resolve(wrapped, stack, world, entity, 0);
    }

    protected void forEachRenderLayer(BakedModel resolved, ItemStack stack, boolean fabulous,
                                      BiConsumer<BakedModel, RenderType> renderer) {
        for (BakedModel renderPass : resolved.getRenderPasses(stack, fabulous)) {
            for (RenderType renderType : renderPass.getRenderTypes(stack, fabulous)) {
                renderer.accept(renderPass, renderType);
            }
        }
    }

    protected void renderWrappedPass(BakedModel renderPass, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer,
                                     int packedLight, int packedOverlay, RenderType renderType) {
        renderWrappedPass(renderPass, stack, poseStack, buffer, packedLight, packedOverlay, renderType, Function.identity());
    }

    protected void renderWrappedPass(BakedModel renderPass, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer,
                                     int packedLight, int packedOverlay, RenderType renderType,
                                     Function<com.mojang.blaze3d.vertex.VertexConsumer, com.mojang.blaze3d.vertex.VertexConsumer> consumerWrapper) {
        Minecraft.getInstance().getItemRenderer().renderModelLists(
                renderPass,
                stack,
                packedLight,
                packedOverlay,
                poseStack,
                consumerWrapper.apply(buffer.getBuffer(renderType)));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, net.minecraft.util.RandomSource random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return wrapped.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return wrapped.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return wrapped.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return wrapped.getParticleIcon();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(ModelData modelData) {
        return wrapped.getParticleIcon(modelData);
    }

    @Override
    public net.minecraft.client.renderer.block.model.ItemTransforms getTransforms() {
        return wrapped.getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return wrapped.getOverrides();
    }

    public boolean isCosmic() {
        return false;
    }
}

package com.gto.gtocore.client.renderer.machine;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.client.ClientUtil;
import com.gto.gtocore.client.renderer.RenderBufferHelper;
import com.gto.gtocore.common.machine.multiblock.noenergy.GodForgeMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.client.renderer.GTRenderTypes;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;
import com.gregtechceu.gtceu.client.util.BloomUtils;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

import com.lowdragmc.shimmer.client.shader.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import org.joml.Quaternionf;

import java.util.function.Consumer;

public final class GodforgeRenderer extends WorkableCasingMachineRenderer {

    private static final ResourceLocation NEUTRON_STAR_MODEL = GTOCore.id("obj/neutron_star");

    public GodforgeRenderer() {
        super(GTOCore.id("block/transcendentally_amplified_magnetic_confinement_casing"), GTCEu.id("block/multiblock/fusion_reactor"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (blockEntity instanceof IMachineBlockEntity machineBlockEntity && machineBlockEntity.getMetaMachine() instanceof GodForgeMachine machine && machine.isFormed() && machine.isActive()) {
            float tick = machine.getOffsetTimer() + partialTicks;
            if (GTCEu.Mods.isShimmerLoaded()) {
                PoseStack finalStack = RenderUtils.copyPoseStack(poseStack);
                BloomUtils.entityBloom(source -> renderAll(machine, tick, finalStack, source));
            } else {
                renderAll(machine, tick, poseStack, buffer);
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderAll(GodForgeMachine machine, float tick, PoseStack poseStack, MultiBufferSource buffer) {
        float color = machine.color;
        poseStack.pushPose();
        switch (machine.getFrontFacing()) {
            case NORTH -> {
                poseStack.translate(0.5, 0.5, 61.5);
                poseStack.mulPose(Axis.XN.rotationDegrees(90));
                renderCurvedCone(poseStack, buffer, color);
                renderStar(tick, poseStack, buffer);
            }
            case SOUTH -> {
                poseStack.translate(0.5, 0.5, -60.5);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                renderCurvedCone(poseStack, buffer, color);
                renderStar(tick, poseStack, buffer);
            }
            case WEST -> {
                poseStack.translate(61.5, 0.5, 0.5);
                poseStack.mulPose(Axis.ZP.rotationDegrees(90));
                renderCurvedCone(poseStack, buffer, color);
                renderStar(tick, poseStack, buffer);
            }
            case EAST -> {
                poseStack.translate(-60.5, 0.5, 0.5);
                poseStack.mulPose(Axis.ZN.rotationDegrees(90));
                renderCurvedCone(poseStack, buffer, color);
                renderStar(tick, poseStack, buffer);
            }
        }
        poseStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderCurvedCone(PoseStack poseStack, MultiBufferSource buffer, float color) {
        RenderBufferHelper.renderCone(poseStack, buffer.getBuffer(GTRenderTypes.getLightRing()), 0.5f, 2, 61, 10, 20, color, color, 1, 1);
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderStar(float tick, PoseStack poseStack, MultiBufferSource buffer) {
        poseStack.translate(0, -61, 0);
        poseStack.scale(0.6F, 0.6F, 0.6F);
        poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(0.0F, 1.0F, 1.0F, tick % 360.0F));
        ClientUtil.modelRenderer().renderModel(poseStack.last(), buffer.getBuffer(RenderType.translucent()), null, ClientUtil.getBakedModel(NEUTRON_STAR_MODEL), 1.0F, 1.0F, 1.0F, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onAdditionalModel(Consumer<ResourceLocation> registry) {
        super.onAdditionalModel(registry);
        registry.accept(NEUTRON_STAR_MODEL);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasTESR(BlockEntity blockEntity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isGlobalRenderer(BlockEntity blockEntity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getViewDistance() {
        return 256;
    }
}

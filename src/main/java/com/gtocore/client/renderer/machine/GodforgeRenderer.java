package com.gtocore.client.renderer.machine;

import com.gtocore.client.renderer.RenderBufferHelper;
import com.gtocore.client.renderer.StructurePattern;
import com.gtocore.client.renderer.StructureVBO;
import com.gtocore.client.renderer.TextureUpdateRequester;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.machine.multiblock.noenergy.GodForgeMachine;

import com.gtolib.GTOCore;
import com.gtolib.utils.ClientUtil;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.client.renderer.GTRenderTypes;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;
import com.gregtechceu.gtceu.client.util.BloomUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.lowdragmc.shimmer.client.shader.RenderUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.math.Axis;
import org.joml.Quaternionf;

import java.util.function.Consumer;

public final class GodforgeRenderer extends WorkableCasingMachineRenderer {

    private static final ResourceLocation NEUTRON_STAR_MODEL = GTOCore.id("obj/neutron_star");
    private static boolean initialized = false;
    private VertexBuffer ringOne, ringTwo, ringThree;
    private TextureUpdateRequester textureUpdateRequester;

    public GodforgeRenderer() {
        super(GTOCore.id("block/transcendentally_amplified_magnetic_confinement_casing"), GTCEu.id("block/multiblock/fusion_reactor"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (!initialized) {
            initRings();
            initialized = true;
        }
        if (blockEntity instanceof IMachineBlockEntity machineBlockEntity && machineBlockEntity.getMetaMachine() instanceof GodForgeMachine machine && machine.isFormed() && (machine.isActive() || blockEntity.getLevel() instanceof TrackedDummyWorld)) {
            float tick = machine.getOffsetTimer() + partialTicks;
            if (GTCEu.Mods.isShimmerLoaded() && !(blockEntity.getLevel() instanceof TrackedDummyWorld)) {
                PoseStack finalStack = RenderUtils.copyPoseStack(poseStack);
                BloomUtils.entityBloom(source -> renderAll(machine, tick, finalStack, source));
            } else {
                renderAll(machine, tick, poseStack, buffer);
            }
        }
        if (blockEntity instanceof IMachineBlockEntity machineBlockEntity && machineBlockEntity.getMetaMachine() instanceof GodForgeMachine machine && machine.rotation > 0 && !(blockEntity.getLevel() instanceof TrackedDummyWorld)) {
            if (machine.isActive() || machine.timer > machine.rotation) {
                RenderRing(machine, poseStack, partialTicks);
            } else {
                RenderRing(machine, poseStack, -partialTicks);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void initRings() {
        StructureVBO ringStructure = (new StructureVBO()).addMapping('B', GTOBlocks.SINGULARITY_REINFORCED_STELLAR_SHIELDING_CASING.get())
                .addMapping('C', GTOBlocks.CELESTIAL_MATTER_GUIDANCE_CASING.get())
                .addMapping('D', GTOBlocks.BOUNDLESS_GRAVITATIONALLY_SEVERED_STRUCTURE_CASING.get())
                .addMapping('E', GTOBlocks.TRANSCENDENTALLY_AMPLIFIED_MAGNETIC_CONFINEMENT_CASING.get())
                .addMapping('F', GTOBlocks.STELLAR_ENERGY_SIPHON_CASING.get())
                .addMapping('1', GTOBlocks.REMOTE_GRAVITON_FLOW_MODULATOR.get())
                .addMapping('2', GTOBlocks.MEDIAL_GRAVITON_FLOW_MODULATOR.get())
                .addMapping('3', GTOBlocks.CENTRAL_GRAVITON_FLOW_MODULATOR.get())
                .addMapping('H', GTOBlocks.SPATIALLY_TRANSCENDENT_GRAVITATIONAL_LENS_BLOCK.get());

        ringOne = ringStructure.assignStructure(StructurePattern.ringOne)
                .build();
        ringTwo = ringStructure.assignStructure(StructurePattern.ringTwo)
                .build();
        ringThree = ringStructure.assignStructure(StructurePattern.ringThree)
                .build();

        textureUpdateRequester = ringStructure.getTextureUpdateRequestor();
    }

    @OnlyIn(Dist.CLIENT)
    private void RenderRing(GodForgeMachine machine, PoseStack poseStack, float partialTicks) {
        VertexBuffer ring = ringOne;
        if (machine.tier == 2) {
            ring = ringTwo;
        } else if (machine.tier == 3) {
            ring = ringThree;
        }

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFunc(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getRendertypeSolidShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
        textureUpdateRequester.requestUpdate();
        poseStack.pushPose();
        // move the model to correct pos and rotate model to face machine
        switch (machine.getFrontFacing()) {
            case NORTH -> {
                poseStack.translate(.5f, .5f, 121.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(270));
            }
            case SOUTH -> {
                poseStack.translate(.5f, .5f, -120.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
            }
            case WEST -> {
                poseStack.translate(121.5f, .5f, 0.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(0));
            }
            case EAST -> {
                poseStack.translate(-120.5f, .5f, 0.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
            }
        }
        // rotate
        poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(1.0f, 0.0f, 0.0f, (machine.rotation + partialTicks) % 360.0F));
        ring.bind();
        ring.drawWithShader(poseStack.last().pose(), RenderSystem.getProjectionMatrix(), RenderSystem.getShader());
        VertexBuffer.unbind();
        poseStack.popPose();
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
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

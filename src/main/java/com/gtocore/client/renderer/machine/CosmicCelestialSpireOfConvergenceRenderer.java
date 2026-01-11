package com.gtocore.client.renderer.machine;

import com.gtocore.client.renderer.StructurePattern;
import com.gtocore.client.renderer.StructureVBO;
import com.gtocore.client.renderer.TextureUpdateRequester;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.machine.mana.multiblock.CosmicCelestialSpireOfConvergence;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.math.Axis;
import org.joml.Quaternionf;

public class CosmicCelestialSpireOfConvergenceRenderer extends WorkableCasingMachineRenderer {

    private VertexBuffer vbo;
    private static boolean initialized = false;
    private TextureUpdateRequester textureUpdateRequester;

    public CosmicCelestialSpireOfConvergenceRenderer() {
        super(GTOCore.id("block/casings/spell_prism_casing"), GTCEu.id("block/multiblock/gcym/large_centrifuge"));
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
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (!initialized) {
            initVBO();
            initialized = true;
        }
        if (blockEntity instanceof MetaMachineBlockEntity machineBlockEntity && machineBlockEntity.getMetaMachine() instanceof CosmicCelestialSpireOfConvergence machine && !(blockEntity.getLevel() instanceof TrackedDummyWorld)) {
            if (machine.isFormed()) {
                if (machine.isActive()) {
                    renderVBO(machine, poseStack, 0.1f, partialTicks);
                } else {
                    renderVBO(machine, poseStack, -0.04f, -partialTicks);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void initVBO() {
        StructureVBO ringStructure = (new StructureVBO())
                .addMapping('X', GTOBlocks.THE_SOLARIS_LENS.get())
                .addMapping('[', RegistriesUtils.getBlock("botania:bifrost_perm"));
        vbo = ringStructure.assignStructure(StructurePattern.tinyLight)
                .offset(-1f, 0f, 0f)
                .build();

        textureUpdateRequester = ringStructure.getTextureUpdateRequestor();
    }

    @OnlyIn(Dist.CLIENT)
    private void renderVBO(CosmicCelestialSpireOfConvergence machine, PoseStack poseStack, float rotSpeed, float partialTicks) {
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
                poseStack.translate(.5f, 8.5f, 5.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(270));
            }
            case SOUTH -> {
                poseStack.translate(.5f, 8.5f, -4.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
            }
            case WEST -> {
                poseStack.translate(5.5f, 8.5f, 0.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(0));
            }
            case EAST -> {
                poseStack.translate(-4.5f, 8.5f, 0.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
            }
        }
        var ticks = (machine.getOffsetTimer() + partialTicks) * rotSpeed;
        // rotate
        poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(1.0f, 1.0f, 1.0f, (ticks) % 360.0F));
        poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(-1.0f, 2.0f, 0.0f, (float) ((Math.sqrt(ticks) / 4) % 360.0F)));
        poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(-2.0f, -1.0f, 3.0f, (ticks / 2) % 360.0F));

        vbo.bind();
        vbo.drawWithShader(poseStack.last().pose(), RenderSystem.getProjectionMatrix(), RenderSystem.getShader());
        VertexBuffer.unbind();
        poseStack.popPose();
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
    }
}

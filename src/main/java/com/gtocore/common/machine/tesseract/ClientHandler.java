package com.gtocore.common.machine.tesseract;

import com.gtocore.client.renderer.RenderHelper;
import com.gtocore.common.item.TesseractTargetMarker;

import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.lowdragmc.lowdraglib.client.utils.RenderBufferUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientHandler {

    private static final float HUE_RED = 0.0f;
    private static final float HUE_PURPLE = 0.8333f;

    private static float lerpHue(float percent) {
        return HUE_RED + percent * (HUE_PURPLE - HUE_RED);
    }

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        if (Minecraft.getInstance().player == null) return;
        var item = Minecraft.getInstance().player.getMainHandItem();
        if (TesseractTargetMarker.isTesseractTargetMarker(item)) {
            var faces = TesseractTargetMarker.getAllPatternFaces(item);
            onRenderDirected(event, faces);
        }
        if (!DirectedTesseractMachine.HIGHLIGHTS.isEmpty()) {
            DirectedTesseractMachine.HIGHLIGHTS.elementSet().forEach(faces -> onRenderDirected(event, List.copyOf(faces)));
        }
        if (!AdvancedTesseractMachine.HIGHLIGHTS.isEmpty()) {
            AdvancedTesseractMachine.HIGHLIGHTS.elementSet().forEach(faces -> onRenderBlocks(event, List.copyOf(faces)));
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        DirectedTesseractMachine.HIGHLIGHTS.elementSet().forEach(DirectedTesseractMachine.HIGHLIGHTS::remove);
        AdvancedTesseractMachine.HIGHLIGHTS.elementSet().forEach(AdvancedTesseractMachine.HIGHLIGHTS::remove);
    }

    public static void onRenderDirected(RenderLevelStageEvent event, List<TesseractDirectedTarget> faces) {
        if (faces.isEmpty()) return;
        var level = GTUtil.getClientLevel();
        var poseStack = event.getPoseStack();
        poseStack.pushPose();
        {
            Vec3 pos = event.getCamera().getPosition();
            poseStack.translate(-pos.x, -pos.y, -pos.z);
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
                var totalLength = faces.size() * 2;
                for (var face : faces) {
                    if (face.pos().dimension() != level.dimension()) continue;
                    var order = face.order();
                    float percent;
                    if (order < 0) percent = (totalLength + order) / ((float) totalLength * 2);
                    else percent = order / ((float) totalLength * 2);
                    Color color = Color.getHSBColor(lerpHue(percent), 1.0f, 1.0f);

                    var faceMinX = face.pos().pos().getX() + (face.face() == Direction.EAST ? 1.0 : 0.0);
                    var faceMinY = face.pos().pos().getY() + (face.face() == Direction.UP ? 1.0 : 0.0);
                    var faceMinZ = face.pos().pos().getZ() + (face.face() == Direction.SOUTH ? 1.0 : 0.0);
                    var faceMaxX = faceMinX + (face.face().getAxis() == Direction.Axis.X ? 0.0 : 1.0);
                    var faceMaxY = faceMinY + (face.face().getAxis() == Direction.Axis.Y ? 0.0 : 1.0);
                    var faceMaxZ = faceMinZ + (face.face().getAxis() == Direction.Axis.Z ? 0.0 : 1.0);

                    RenderSystem.disableDepthTest();
                    RenderSystem.enableBlend();
                    RenderSystem.disableCull();
                    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                    poseStack.pushPose();
                    {
                        Tesselator tesselator = Tesselator.getInstance();
                        BufferBuilder buffer = tesselator.getBuilder();
                        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                        RenderSystem.setShader(GameRenderer::getPositionColorShader);
                        RenderBufferUtils.renderCubeFace(
                                poseStack,
                                buffer,
                                (float) faceMinX, (float) faceMinY, (float) faceMinZ,
                                (float) faceMaxX, (float) faceMaxY, (float) faceMaxZ,
                                color.getRed() / 255f,
                                color.getGreen() / 255f,
                                color.getBlue() / 255f,
                                0.16f,
                                false);
                        tesselator.end();
                        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
                        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
                        RenderSystem.lineWidth(6);
                        RenderBufferUtils.drawCubeFrame(
                                poseStack,
                                buffer,
                                (float) faceMinX, (float) faceMinY, (float) faceMinZ,
                                (float) faceMaxX, (float) faceMaxY, (float) faceMaxZ,
                                color.getRed() / 255f,
                                color.getGreen() / 255f,
                                color.getBlue() / 255f,
                                0.4f);
                        tesselator.end();
                    }
                    poseStack.popPose();
                    poseStack.pushPose();
                    {
                        poseStack.translate((faceMinX + faceMaxX) / 2.0, (faceMinY + faceMaxY) / 2.0, (faceMinZ + faceMaxZ) / 2.0);
                        poseStack.scale(-0.03f, -0.03f, -0.03f);
                        poseStack.mulPose(event.getCamera().rotation());
                        Matrix4f matrix4f = poseStack.last().pose();
                        Font font = Minecraft.getInstance().font;
                        font.drawInBatch(
                                String.valueOf(order),
                                -font.width(String.valueOf(order)) / 2f,
                                -font.lineHeight / 2f,
                                color.getRGB(),
                                false,
                                matrix4f,
                                event.getLevelRenderer().renderBuffers.bufferSource(),
                                Font.DisplayMode.SEE_THROUGH,
                                0,
                                15728880);
                        font.drawInBatch(
                                String.valueOf(order),
                                -font.width(String.valueOf(order)) / 2f,
                                -font.lineHeight / 2f,
                                color.getRGB(),
                                false,
                                matrix4f,
                                event.getLevelRenderer().renderBuffers.bufferSource(),
                                Font.DisplayMode.NORMAL,
                                0,
                                15728880);
                    }
                    poseStack.popPose();
                    RenderSystem.enableCull();
                    RenderSystem.disableBlend();
                    RenderSystem.enableDepthTest();
                }
            }
        }
        poseStack.popPose();
    }

    public static void onRenderBlocks(RenderLevelStageEvent event, List<Long> packedPositions) {
        if (packedPositions.isEmpty()) return;
        var level = GTUtil.getClientLevel();
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            var poseStack = event.getPoseStack();
            poseStack.pushPose();
            {
                for (int i = 0; i < packedPositions.size(); i++) {
                    var blockPos = BlockPos.of(packedPositions.get(i));
                    if (!level.isLoaded(blockPos)) continue;
                    var order = i + 1;
                    var color = Color.getHSBColor(lerpHue(order / (float) (packedPositions.size() * 2)), 1.0f, 1.0f);

                    RenderHelper.highlightBlock(
                            event.getCamera(),
                            poseStack,
                            color.getRed() / 255f,
                            color.getGreen() / 255f,
                            color.getBlue() / 255f,
                            blockPos,
                            blockPos);

                    RenderSystem.disableDepthTest();
                    RenderSystem.enableBlend();
                    RenderSystem.disableCull();
                    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                    poseStack.pushPose();
                    {
                        Vec3 pos = event.getCamera().getPosition();
                        poseStack.translate(-pos.x, -pos.y, -pos.z);
                        poseStack.translate(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
                        poseStack.scale(-0.03f, -0.03f, -0.03f);
                        poseStack.mulPose(event.getCamera().rotation());
                        Matrix4f matrix4f = poseStack.last().pose();
                        Font font = Minecraft.getInstance().font;
                        font.drawInBatch(
                                String.valueOf(order),
                                -font.width(String.valueOf(order)) / 2f,
                                -font.lineHeight / 2f,
                                color.getRGB(),
                                false,
                                matrix4f,
                                event.getLevelRenderer().renderBuffers.bufferSource(),
                                Font.DisplayMode.SEE_THROUGH,
                                0,
                                15728880);
                        font.drawInBatch(
                                String.valueOf(order),
                                -font.width(String.valueOf(order)) / 2f,
                                -font.lineHeight / 2f,
                                color.getRGB(),
                                false,
                                matrix4f,
                                event.getLevelRenderer().renderBuffers.bufferSource(),
                                Font.DisplayMode.NORMAL,
                                0,
                                15728880);
                    }
                    poseStack.popPose();
                    RenderSystem.enableCull();
                    RenderSystem.disableBlend();
                    RenderSystem.enableDepthTest();
                }
            }
            poseStack.popPose();
        }
    }
}

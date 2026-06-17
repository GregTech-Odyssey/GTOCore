package com.gtocore.common.machine.tesseract;

import com.gtocore.client.renderer.RenderHelper;
import com.gtocore.common.item.TesseractTargetMarker;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.List;

import static com.gregtechceu.gtceu.utils.GTUtil.getClientLevel;

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
        if (event.phase == TickEvent.Phase.START) {
            List.copyOf(DirectedTesseractMachine.HIGHLIGHTS.elementSet()).forEach(DirectedTesseractMachine.HIGHLIGHTS::remove);
            List.copyOf(AdvancedTesseractMachine.HIGHLIGHTS.elementSet()).forEach(AdvancedTesseractMachine.HIGHLIGHTS::remove);
        }
    }

    public static void onRenderDirected(RenderLevelStageEvent event, List<TesseractDirectedTarget> faces) {
        if (faces.isEmpty()) return;
        var level = getClientLevel();
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) return;
        var poseStack = event.getPoseStack();
        poseStack.pushPose();
        {
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

                RenderHelper.highlightBox(
                        event.getCamera(),
                        poseStack,
                        color.getRed() / 255f,
                        color.getGreen() / 255f,
                        color.getBlue() / 255f,
                        0.16f,
                        color.getRed() / 255f,
                        color.getGreen() / 255f,
                        color.getBlue() / 255f,
                        0.4f,
                        6,
                        false,
                        faceMinX, faceMinY, faceMinZ,
                        faceMaxX, faceMaxY, faceMaxZ);
                RenderHelper.renderSeeThroughText(
                        event.getCamera(),
                        poseStack,
                        (faceMinX + faceMaxX) / 2.0,
                        (faceMinY + faceMaxY) / 2.0,
                        (faceMinZ + faceMaxZ) / 2.0,
                        color.getRGB(),
                        String.valueOf(order),
                        event.getLevelRenderer().renderBuffers.bufferSource());
            }
        }
        poseStack.popPose();
    }

    public static void onRenderBlocks(RenderLevelStageEvent event, List<Long> packedPositions) {
        if (packedPositions.isEmpty()) return;
        var level = getClientLevel();
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

                    RenderHelper.renderSeeThroughText(
                            event.getCamera(),
                            poseStack,
                            blockPos,
                            color.getRGB(),
                            String.valueOf(order),
                            event.getLevelRenderer().renderBuffers.bufferSource());
                }
            }
            poseStack.popPose();
        }
    }

    @SubscribeEvent
    public static void onInteractionKeyPressed(InputEvent.InteractionKeyMappingTriggered event) {
        if (event.isPickBlock() &&
                TesseractTargetMarker.isTesseractTargetMarker(Minecraft.getInstance().player.getMainHandItem())) {
            event.setCanceled(true);
            TesseractTargetMarker.sendCopyConfigPacket(getClientLevel(), Minecraft.getInstance().player);
        }
    }
}

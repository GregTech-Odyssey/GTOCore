package com.gtocore.client.renderer;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.client.utils.RenderBufferUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public final class RenderHelper {

    public static void renderCylinder(PoseStack poseStack, VertexConsumer buffer, float x, float y, float z,
                                      float radius, float height, int sides, float red, float green, float blue, float alpha) {
        Matrix4f mat = poseStack.last().pose();
        float angleStep = (float) (2.0 * Math.PI / sides);

        for (int i = 0; i < sides; i++) {
            float angle1 = i * angleStep;
            float angle2 = (i + 1) * angleStep;

            float cosAngle1 = Mth.cos(angle1);
            float sinAngle1 = Mth.sin(angle1);
            float cosAngle2 = Mth.cos(angle2);
            float sinAngle2 = Mth.sin(angle2);

            buffer.vertex(mat, x + cosAngle1 * radius, y, z + sinAngle1 * radius)
                    .color(red, green, blue, alpha).endVertex();
            buffer.vertex(mat, x + cosAngle2 * radius, y, z + sinAngle2 * radius)
                    .color(red, green, blue, alpha).endVertex();
            buffer.vertex(mat, x + cosAngle2 * radius, y + height, z + sinAngle2 * radius)
                    .color(red, green, blue, alpha).endVertex();

            buffer.vertex(mat, x + cosAngle1 * radius, y, z + sinAngle1 * radius)
                    .color(red, green, blue, alpha).endVertex();
            buffer.vertex(mat, x + cosAngle2 * radius, y + height, z + sinAngle2 * radius)
                    .color(red, green, blue, alpha).endVertex();
            buffer.vertex(mat, x + cosAngle1 * radius, y + height, z + sinAngle1 * radius)
                    .color(red, green, blue, alpha).endVertex();
        }
    }

    public static void renderTexturedCylinder(PoseStack poseStack, VertexConsumer buffer,
                                              float x, float y, float z,
                                              float radius, float height, int sides,
                                              ResourceLocation sprite,
                                              float subTextureU0, float subTextureV0, float subTextureU1, float subTextureV1) {
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);

        TextureAtlasSprite atlasSprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(sprite);

        Matrix4f mat = poseStack.last().pose();
        float angleStep = (float) (2.0 * Math.PI / sides);

        float u0o = atlasSprite.getU0();
        float u1o = atlasSprite.getU1();
        float v0o = atlasSprite.getV0();
        float v1o = atlasSprite.getV1();

        float u0 = Mth.lerp(subTextureU0, u0o, u1o);
        float u1 = Mth.lerp(subTextureU1, u0o, u1o);
        float v0 = Mth.lerp(subTextureV0, v0o, v1o);
        float v1 = Mth.lerp(subTextureV1, v0o, v1o);

        float uRange = u1 - u0;
        float vRange = v1 - v0;

        for (int i = 0; i < sides; i++) {
            float angle1 = i * angleStep;
            float angle2 = (i + 1) * angleStep;

            float cos1 = Mth.cos(angle1);
            float sin1 = Mth.sin(angle1);
            float cos2 = Mth.cos(angle2);
            float sin2 = Mth.sin(angle2);

            float bx1 = x + cos1 * radius;
            float bz1 = z + sin1 * radius;
            float bx2 = x + cos2 * radius;
            float bz2 = z + sin2 * radius;

            // U 映射：沿环绕方向均匀分布，避免最后一个面与第一个面缝隙可考虑重复最后一个U为1.0
            float uA = u0 + (i / (float) sides) * uRange;
            float uB = u0 + ((i + 1) / (float) sides) * uRange;
            float vBottom = v0;
            float vTop = v0 + vRange;

            // 三角形 1
            buffer.vertex(mat, bx1, y, bz1)
                    .uv(uA, vBottom)
                    .endVertex();
            buffer.vertex(mat, bx2, y, bz2)
                    .uv(uB, vBottom)
                    .endVertex();
            buffer.vertex(mat, bx2, y + height, bz2)
                    .uv(uB, vTop)
                    .endVertex();

            // 三角形 2
            buffer.vertex(mat, bx1, y, bz1)
                    .uv(uA, vBottom)
                    .endVertex();
            buffer.vertex(mat, bx2, y + height, bz2)
                    .uv(uB, vTop)
                    .endVertex();
            buffer.vertex(mat, bx1, y + height, bz1)
                    .uv(uA, vTop)
                    .endVertex();
        }
    }

    public static void renderCone(PoseStack poseStack, VertexConsumer buffer, float baseRadius, float topRadius, float height,
                                  float curvature, int sides, float red, float green, float blue, float alpha) {
        Matrix4f mat = poseStack.last().pose();
        float angleDelta = (float) (2.0 * Math.PI / sides);

        for (int i = 0; i < sides; i++) {
            float angle1 = i * angleDelta;
            float angle2 = angle1 + angleDelta;

            float cosAngle1 = Mth.cos(angle1);
            float sinAngle1 = Mth.sin(angle1);
            float cosAngle2 = Mth.cos(angle2);
            float sinAngle2 = Mth.sin(angle2);

            float baseX1 = cosAngle1 * baseRadius;
            float baseZ1 = sinAngle1 * baseRadius;
            float baseX2 = cosAngle2 * baseRadius;
            float baseZ2 = sinAngle2 * baseRadius;

            float topX1 = cosAngle1 * topRadius;
            float topZ1 = sinAngle1 * topRadius;
            float topX2 = cosAngle2 * topRadius;
            float topZ2 = sinAngle2 * topRadius;

            for (float j = 0; j <= curvature; j += 1.0f) {
                float lerpFactor = j / curvature;
                float curX1 = baseX1 + lerpFactor * (topX1 - baseX1);
                float curZ1 = baseZ1 + lerpFactor * (topZ1 - baseZ1);
                float curX2 = baseX2 + lerpFactor * (topX2 - baseX2);
                float curZ2 = baseZ2 + lerpFactor * (topZ2 - baseZ2);
                float curY = height * (1 - lerpFactor);

                buffer.vertex(mat, curX1, curY, curZ1).color(red, green, blue, alpha).endVertex();
                buffer.vertex(mat, curX2, curY, curZ2).color(red, green, blue, alpha).endVertex();
            }
        }
    }

    public static void highlightBlock(Camera camera, PoseStack poseStack, float r, float g, float b, BlockPos... poses) {
        Vec3 pos = camera.getPosition();
        float lightR = (1.0f + r * 4f) / 5.0f;
        float lightG = (1.0f + g * 4f) / 5.0f;
        float lightB = (1.0f + b * 4f) / 5.0f;
        poseStack.pushPose();
        poseStack.translate(-pos.x, -pos.y, -pos.z);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderBufferUtils.renderCubeFace(poseStack, buffer, poses[0].getX(), poses[0].getY(), poses[0].getZ(), poses[1].getX() + 1, poses[1].getY() + 1, poses[1].getZ() + 1, lightR, lightG, lightB, 0.25f, true);
        tesselator.end();
        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
        RenderSystem.lineWidth(3);
        RenderBufferUtils.drawCubeFrame(poseStack, buffer, poses[0].getX(), poses[0].getY(), poses[0].getZ(), poses[1].getX() + 1, poses[1].getY() + 1, poses[1].getZ() + 1, r, g, b, 0.5f);
        tesselator.end();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        poseStack.popPose();
    }

    public static void highlightSphere(Camera camera, PoseStack poseStack, BlockPos blockPos, float radius) {
        Vec3 pos = camera.getPosition();
        poseStack.pushPose();
        poseStack.translate(-pos.x, -pos.y, -pos.z);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderBufferUtils.shapeSphere(poseStack, buffer, blockPos.getX(), blockPos.getY(), blockPos.getZ(), radius, 40, 50, 0.2f, 0.2f, 1.0f, 0.25f);
        tesselator.end();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        poseStack.popPose();
    }

    public static BufferBuilder openGUIBuffer() {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        return buffer;
    }

    public static void closeAndDrawGUIBuffer(@NotNull BufferBuilder buffer) {
        BufferUploader.drawWithShader(buffer.end());
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    public static void drawInGUI(
                                 GuiGraphics gui,
                                 BufferBuilder buffer,
                                 TextureAtlasSprite sprite,
                                 int x,
                                 int y,
                                 float width,
                                 float height,
                                 int color) {
        float x2 = x + width;
        float y2 = y + height;
        float z = 0F;
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        Matrix4f matrix4f = gui.pose().last().pose();
        buffer.vertex(matrix4f, (float) x, (float) y, z)
                .uv(u0, v0)
                .color(color)
                .endVertex();
        buffer.vertex(matrix4f, (float) x, y2, z)
                .uv(u0, v1)
                .color(color)
                .endVertex();
        buffer.vertex(matrix4f, x2, y2, z)
                .uv(u1, v1)
                .color(color)
                .endVertex();
        buffer.vertex(matrix4f, x2, (float) y, z)
                .uv(u1, v0)
                .color(color)
                .endVertex();
    }
}

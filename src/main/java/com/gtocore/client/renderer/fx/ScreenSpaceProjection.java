package com.gtocore.client.renderer.fx;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class ScreenSpaceProjection {

    public static final float MIN_VISIBLE_RADIUS = 0.001F;

    private ScreenSpaceProjection() {}

    public static Sphere projectSphere(Vec3 center, float innerRadius, float outerRadius, Camera camera, PoseStack poseStack, Matrix4f projectionMatrix) {
        ProjectionContext context = createContext(center, camera, poseStack, projectionMatrix);
        if (context == null) {
            return null;
        }

        if (!isOffsetVisible(context.cameraRelative, camera.getLeftVector(), outerRadius, context.viewProjection) ||
                !isOffsetVisible(context.cameraRelative, camera.getLeftVector(), innerRadius, context.viewProjection)) {
            return null;
        }

        float outerRadiusPixels = projectRadiusPixels(context.cameraRelative, camera, outerRadius, context);
        float innerRadiusPixels = projectRadiusPixels(context.cameraRelative, camera, innerRadius, context);
        if (outerRadiusPixels <= MIN_VISIBLE_RADIUS || innerRadiusPixels >= outerRadiusPixels) {
            return null;
        }

        return new Sphere(context.centerScreenX, context.centerScreenY, innerRadiusPixels, outerRadiusPixels);
    }

    public static Cylinder projectCylinder(Vec3 baseCenter, float radius, float height, Camera camera, PoseStack poseStack, Matrix4f projectionMatrix) {
        Vec3 center = baseCenter.add(0.0D, height * 0.5D, 0.0D);
        ProjectionContext context = createContext(center, camera, poseStack, projectionMatrix);
        if (context == null) {
            return null;
        }

        float boundingRadius = (float) Math.hypot(radius, height * 0.5F);
        float projectedRadius = projectRadiusPixels(context.cameraRelative, camera, boundingRadius, context);
        if (projectedRadius <= MIN_VISIBLE_RADIUS) {
            return null;
        }
        float projectedPatternRadius = projectRadiusPixels(context.cameraRelative, camera, radius, context);
        return new Cylinder(context.centerScreenX, context.centerScreenY, projectedRadius, projectedPatternRadius);
    }

    private static ProjectionContext createContext(Vec3 center, Camera camera, PoseStack poseStack, Matrix4f projectionMatrix) {
        Minecraft minecraft = Minecraft.getInstance();
        int screenWidth = minecraft.getWindow().getWidth();
        int screenHeight = minecraft.getWindow().getHeight();
        if (screenWidth <= 0 || screenHeight <= 0) {
            return null;
        }

        Vec3 cameraRelative = center.subtract(camera.getPosition());
        Matrix4f viewProjection = new Matrix4f(projectionMatrix);
        viewProjection.mul(poseStack.last().pose());

        Vector4f centerClip = new Vector4f((float) cameraRelative.x, (float) cameraRelative.y, (float) cameraRelative.z, 1.0F);
        viewProjection.transform(centerClip);
        if (centerClip.w <= 0.0F) {
            return null;
        }

        float centerNdcX = centerClip.x / centerClip.w;
        float centerNdcY = centerClip.y / centerClip.w;
        float centerScreenX = (centerNdcX * 0.5F + 0.5F) * screenWidth;
        float centerScreenY = (centerNdcY * 0.5F + 0.5F) * screenHeight;
        return new ProjectionContext(cameraRelative, viewProjection, centerNdcX, centerNdcY, centerScreenX, centerScreenY, screenWidth, screenHeight);
    }

    private static boolean isOffsetVisible(Vec3 cameraRelative, Vector3f axis, float radius, Matrix4f viewProjection) {
        Vector4f clip = new Vector4f((float) (cameraRelative.x + axis.x() * radius),
                (float) (cameraRelative.y + axis.y() * radius),
                (float) (cameraRelative.z + axis.z() * radius), 1.0F);
        viewProjection.transform(clip);
        return clip.w > 0.0F;
    }

    private static float projectRadiusPixels(Vec3 cameraRelative, Camera camera, float radius, ProjectionContext context) {
        float horizontalRadius = projectAxisRadiusPixels(cameraRelative, camera.getLeftVector(), radius, context);
        float verticalRadius = projectAxisRadiusPixels(cameraRelative, camera.getUpVector(), radius, context);
        return Math.max(horizontalRadius, verticalRadius);
    }

    private static float projectAxisRadiusPixels(Vec3 cameraRelative, Vector3f axis, float radius, ProjectionContext context) {
        Vector4f clip = new Vector4f((float) (cameraRelative.x + axis.x() * radius),
                (float) (cameraRelative.y + axis.y() * radius),
                (float) (cameraRelative.z + axis.z() * radius), 1.0F);
        context.viewProjection.transform(clip);
        if (clip.w <= 0.0F) {
            return 0.0F;
        }

        float ndcX = clip.x / clip.w;
        float ndcY = clip.y / clip.w;
        return (float) Math.hypot((ndcX - context.centerNdcX) * 0.5F * context.screenWidth,
                (ndcY - context.centerNdcY) * 0.5F * context.screenHeight);
    }

    public record Sphere(float centerX, float centerY, float innerRadius, float outerRadius) {}

    public record Cylinder(float centerX, float centerY, float radius, float patternRadius) {}

    private record ProjectionContext(Vec3 cameraRelative, Matrix4f viewProjection,
                                     float centerNdcX, float centerNdcY,
                                     float centerScreenX, float centerScreenY,
                                     int screenWidth, int screenHeight) {}
}

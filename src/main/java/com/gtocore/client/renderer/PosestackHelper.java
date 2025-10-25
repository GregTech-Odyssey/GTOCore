package com.gtocore.client.renderer;

import net.minecraft.util.Mth;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PosestackHelper {

    /**
     * Renders the confusion overlay effect anywhere. This method will do something to the poseStack so push before and
     * pop after
     * 
     * @param posestack    The PoseStack to apply the effect to
     * @param fx           The current effect strength (0.0 - 1.0)
     * @param maxFx        The maximum effect strength (0.0 - 1.0)
     * @param intensity    The intensity of the effect, usually the amplifier of the effect
     * @param accTicks     The accumulated ticks the effect has been active for
     * @param partialTicks The partial ticks of the current frame
     */
    public static void spinningTransformPosestack(PoseStack posestack, float fx, float maxFx, int intensity, long accTicks, float partialTicks) {
        float f1 = Mth.lerp(partialTicks, fx, maxFx);
        if (f1 > 0.0F) {
            float f2 = 5.0F / (f1 * f1 + 5.0F) - f1 * 0.04F;
            f2 *= f2;
            Axis axis = Axis.of(new Vector3f(0.0F, Mth.SQRT_OF_TWO / 2.0F, Mth.SQRT_OF_TWO / 2.0F));
            posestack.mulPose(axis.rotationDegrees(((float) accTicks + partialTicks) * (float) intensity));
            posestack.scale(1.0F / f2, 1.0F, 1.0F);
            float f3 = -((float) accTicks + partialTicks) * (float) intensity;
            posestack.mulPose(axis.rotationDegrees(f3));
        }
    }

    public static void stereoTransformPosestack(PoseStack poseStack, float axisX, float axisY, float axisZ, float angle) {
        poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(axisX, axisY, axisZ, angle));
    }
}

package com.gtocore.client.renderer.machine;

import com.gtolib.GTOCore;
import com.gtolib.utils.ClientUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.vertex.PoseStack;

import java.util.function.Consumer;

public final class CelestialCondenserRenderer extends WorkableCasingMachineRenderer {

    private static final ResourceLocation MODEL_LOCATION = GTOCore.id("block/machine/celestial_condenser");

    public CelestialCondenserRenderer() {
        // 使用适当的纹理路径，这里假设使用默认的工作机器外观
        super(GTOCore.id("block/casings/solid_machine_casing"), GTOCore.id("block/machine/celestial_condenser_overlay"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (blockEntity instanceof MetaMachineBlockEntity) {

            // 渲染Celestial Condenser特殊模型（透明部分）
            poseStack.pushPose();
            // 调整模型位置和缩放
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.scale(1.0f, 1.0f, 1.0f);

            // 使用透明渲染类型渲染模型
            ClientUtil.modelRenderer().renderModel(
                    poseStack.last(),
                    buffer.getBuffer(RenderType.translucent()), // 使用透明渲染类型
                    null,
                    ClientUtil.getBakedModel(MODEL_LOCATION),
                    1.0f, 1.0f, 1.0f, // RGB颜色
                    combinedLight,
                    combinedOverlay);

            poseStack.popPose();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onAdditionalModel(Consumer<ResourceLocation> registry) {
        super.onAdditionalModel(registry);
        registry.accept(MODEL_LOCATION);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasTESR(BlockEntity blockEntity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isGlobalRenderer(BlockEntity blockEntity) {
        return false; // 根据需求调整，如果不是大型结构，通常设为false
    }
}

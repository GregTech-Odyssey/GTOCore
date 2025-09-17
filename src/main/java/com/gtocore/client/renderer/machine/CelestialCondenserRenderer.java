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
        super(GTOCore.id("block/machines/celestial_condenser/side"), GTOCore.id("block/machines/celestial_condenser/face"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (blockEntity instanceof MetaMachineBlockEntity) {

            poseStack.pushPose();
            poseStack.translate(0, 1, 0);
            poseStack.scale(1.0f, 1.0f, 1.0f);
            ClientUtil.modelRenderer().renderModel(
                    poseStack.last(),
                    buffer.getBuffer(RenderType.translucent()),
                    null,
                    ClientUtil.getBakedModel(MODEL_LOCATION),
                    1.0f, 1.0f, 1.0f,
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
        return false;
    }
}

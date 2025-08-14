package com.gtocore.client.renderer.machine;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.utils.ClientUtil;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
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

import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.lowdragmc.shimmer.client.shader.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionf;

import java.util.function.Consumer;

public final class AdvancedHyperRenderer extends WorkableCasingMachineRenderer {

    private static final ResourceLocation BLUE_STAR_MODEL = GTOCore.id("obj/blue_star");

    public AdvancedHyperRenderer() {
        super(GTOCore.id("block/casings/enhance_hyper_mechanical_casing"), GTCEu.id("block/multiblock/fusion_reactor"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (blockEntity instanceof MetaMachineBlockEntity machineBlockEntity && machineBlockEntity.getMetaMachine() instanceof ElectricMultiblockMachine machine && machine.isFormed() && (machine.isActive() || blockEntity.getLevel() instanceof TrackedDummyWorld)) {
            float tick = machine.getOffsetTimer() + partialTicks;
            if (GTCEu.Mods.isShimmerLoaded() && !(blockEntity.getLevel() instanceof TrackedDummyWorld)) {
                PoseStack finalStack = RenderUtils.copyPoseStack(poseStack);
                BloomUtils.entityBloom(source -> renderStar(machine, tick, finalStack, source));
            } else {
                renderStar(machine, tick, poseStack, buffer);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderStar(ElectricMultiblockMachine machine, float tick, PoseStack poseStack, MultiBufferSource buffer) {
        double x = 0.5, y = 8.25, z = 0.5;
        switch (machine.getFrontFacing()) {
            case NORTH -> z = 18.5;
            case SOUTH -> z = -17.5;
            case WEST -> x = 18.5;
            case EAST -> x = -17.5;
        }
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.scale(0.10F, 0.10F, 0.10F);
        poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(1.0F, 1.0F, 0.0F, (tick % 36.0F) * 4));
        ClientUtil.modelRenderer().renderModel(poseStack.last(), buffer.getBuffer(RenderType.translucent()), null, ClientUtil.getBakedModel(BLUE_STAR_MODEL), 1.0F, 1.0F, 1.0F, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.translucent());
        poseStack.popPose();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onAdditionalModel(Consumer<ResourceLocation> registry) {
        super.onAdditionalModel(registry);
        registry.accept(BLUE_STAR_MODEL);
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
        return 128;
    }
}

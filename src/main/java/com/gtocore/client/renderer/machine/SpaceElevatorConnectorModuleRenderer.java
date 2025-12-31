package com.gtocore.client.renderer.machine;

import com.gtocore.client.renderer.GTORenderTypes;
import com.gtocore.client.renderer.RenderHelper;
import com.gtocore.common.machine.multiblock.electric.space.SuperSpaceElevatorMachine;
import com.gtocore.common.machine.multiblock.electric.space.spacestaion.SpaceElevatorConnectorModule;

import com.gtolib.GTOCore;
import com.gtolib.utils.ClientUtil;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import java.util.function.Consumer;

public final class SpaceElevatorConnectorModuleRenderer extends WorkableCasingMachineRenderer {

    private static final ResourceLocation CLIMBER_MODEL = GTOCore.id("obj/climber");
    private static final ResourceLocation CABLE_TEXTURE = GTOCore.id("block/obj/climber_cable");
    // GTOCore.id("block/casings/high_strength_space_elevator_cable_ctm");

    public SpaceElevatorConnectorModuleRenderer() {
        super(GTOCore.id("block/casings/spacecraft_dynamic_protective_mechanical_casing"), GTCEu.id("block/multiblock/fusion_reactor"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (blockEntity instanceof MetaMachineBlockEntity machineBlockEntity) {
            MetaMachine metaMachine = machineBlockEntity.getMetaMachine();
            if (metaMachine instanceof SpaceElevatorConnectorModule machine && machine.isFormed() && (machine.getMaxTier() > 0 || blockEntity.getLevel() instanceof TrackedDummyWorld)) {
                double x = 0.5, y = -15, z = 0.5;
                switch (machine.getFrontFacing().getOpposite()) {
                    case NORTH -> z = 2.5;
                    case SOUTH -> z = -1.5;
                    case WEST -> x = 2.5;
                    case EAST -> x = -1.5;
                }
                {
                    poseStack.pushPose();
                    poseStack.rotateAround(Axis.ZN.rotationDegrees(180F), (float) x, (float) y, (float) z);
                    poseStack.rotateAround(Axis.YP.rotationDegrees(machine.getOffsetTimer() / 16f), (float) x, (float) y, (float) z);
                    for (float i = (-partialTicks - machine.getOffsetTimer()) % 32 / 16f - 1; i < 360F; i += 2f) {
                        RenderHelper.renderTexturedCylinder(poseStack, buffer.getBuffer(GTORenderTypes.LIGHT_CYLINDER_TEXTURED),
                                (float) x, (float) y + i, (float) z,
                                2.2F, 2F, 16,
                                CABLE_TEXTURE, 0, 0, 1f, 1f);
                    }
                    poseStack.popPose();
                }
                {
                    poseStack.pushPose();
                    poseStack.translate(x, y - machine.getHigh(), z);
                    RendererModel(poseStack, buffer, 12, CLIMBER_MODEL);
                    poseStack.popPose();
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    static void RendererModel(PoseStack poseStack, MultiBufferSource buffer, float scale, ResourceLocation climberModel) {
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        ClientUtil.modelRenderer().renderModel(poseStack.last(), buffer.getBuffer(RenderType.solid()), null, ClientUtil.getBakedModel(climberModel), 1.0F, 1.0F, 1.0F, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid());
        poseStack.popPose();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onAdditionalModel(Consumer<ResourceLocation> registry) {
        super.onAdditionalModel(registry);
        registry.accept(CLIMBER_MODEL);
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

    @Override
    public boolean shouldRender(BlockEntity blockEntity, Vec3 cameraPos) {
        if (blockEntity instanceof MetaMachineBlockEntity machineBlockEntity) {
            MetaMachine metaMachine = machineBlockEntity.getMetaMachine();
            return (metaMachine instanceof SuperSpaceElevatorMachine machine && machine.isFormed() &&
                    (machine.getSpoolCount() >= machine.getMaxSpoolCount() ||
                            blockEntity.getLevel() instanceof TrackedDummyWorld)) ||
                    super.shouldRender(blockEntity, cameraPos);
        }
        return super.shouldRender(blockEntity, cameraPos);
    }
}

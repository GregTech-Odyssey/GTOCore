package com.gtocore.client.renderer.machine;

import com.gtocore.common.machine.multiblock.noenergy.HarmonyMachine;

import com.gtolib.GTOCore;
import com.gtolib.utils.ClientUtil;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

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
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionf;

import java.util.List;
import java.util.function.Consumer;

public final class EyeOfHarmonyRenderer extends CustomPartRenderer {

    private static final ResourceLocation SPACE_MODEL = GTOCore.id("obj/space");
    static final ResourceLocation STAR_MODEL = GTOCore.id("obj/star");
    private static final List<ResourceLocation> ORBIT_OBJECTS = List.of(
            GTOCore.id("obj/the_nether"),
            GTOCore.id("obj/overworld"),
            GTOCore.id("obj/the_end"));

    public EyeOfHarmonyRenderer() {
        super(GTOCore.id("block/casings/dimensionally_transcendent_casing"), GTOCore.id("block/multiblock/cosmos_simulation"), GTCEu.id("block/casings/hpca/high_power_casing"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (blockEntity instanceof MetaMachineBlockEntity machineBlockEntity && machineBlockEntity.getMetaMachine() instanceof HarmonyMachine machine && machine.isFormed() && (machine.isActive() || blockEntity.getLevel() instanceof TrackedDummyWorld)) {
            float tick = machine.getOffsetTimer() + partialTicks;
            double x = 0.5, y = 0.5, z = 0.5;
            switch (machine.getFrontFacing()) {
                case NORTH -> z = 16.5;
                case SOUTH -> z = -15.5;
                case WEST -> x = 16.5;
                case EAST -> x = -15.5;
            }
            poseStack.pushPose();
            poseStack.translate(x, y, z);
            renderStar(tick, poseStack, buffer);
            renderOrbitObjects(tick, poseStack, buffer, x, y, z);
            renderOuterSpaceShell(poseStack, buffer);
            poseStack.popPose();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderStar(float tick, PoseStack poseStack, MultiBufferSource buffer) {
        poseStack.pushPose();
        poseStack.scale(0.02F, 0.02F, 0.02F);
        poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(0.0F, 1.0F, 1.0F, (tick / 2) % 360.0F));
        ClientUtil.modelRenderer().renderModel(poseStack.last(), buffer.getBuffer(RenderType.translucent()), null, ClientUtil.getBakedModel(STAR_MODEL), 1.0F, 1.0F, 1.0F, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.translucent());
        poseStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderOrbitObjects(float tick, PoseStack poseStack, MultiBufferSource buffer, double x, double y, double z) {
        for (int a = 1; a < 4; a++) {
            float scale = 0.007F + 0.003F * a;
            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(1.0F, 0.0F, 1.0F, (tick * 1.5F / a) % 360.0F));
            poseStack.translate(x + (a * 100 + 160) * Math.sin(tick * a / 80 + 0.4), y, z + (a * 100 + 160) * Math.cos(tick * a / 80 + 0.4));
            ClientUtil.modelRenderer().renderModel(poseStack.last(), buffer.getBuffer(RenderType.solid()), null, ClientUtil.getBakedModel(ORBIT_OBJECTS.get(a - 1)), 1.0F, 1.0F, 1.0F, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid());
            poseStack.popPose();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderOuterSpaceShell(PoseStack poseStack, MultiBufferSource buffer) {
        float scale = 0.01F * 17.5F;
        SpaceElevatorRenderer.RendererModel(poseStack, buffer, scale, SPACE_MODEL);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onAdditionalModel(Consumer<ResourceLocation> registry) {
        super.onAdditionalModel(registry);
        registry.accept(SPACE_MODEL);
        registry.accept(STAR_MODEL);
        registry.accept(ORBIT_OBJECTS.get(0));
        registry.accept(ORBIT_OBJECTS.get(1));
        registry.accept(ORBIT_OBJECTS.get(2));
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

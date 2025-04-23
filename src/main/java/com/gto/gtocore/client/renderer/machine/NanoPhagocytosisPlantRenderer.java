package com.gto.gtocore.client.renderer.machine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;
import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.client.ClientUtil;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.platform.Mod;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Quaternionf;

import javax.swing.plaf.PanelUI;
import java.util.function.Consumer;

public final class NanoPhagocytosisPlantRenderer extends WorkableCasingMachineRenderer {

    private static final ResourceLocation NANO_PHAGOCYTOSIS_PLANT_RING_PART_A = GTOCore.id("obj/nano_phagocytosis_plant_ring_part_1");

    public NanoPhagocytosisPlantRenderer(){
        super(GTOCore.id("block/casings/naquadah_reinforced_plant_casing"), GTCEu.id("block/multiblock/fusion_reactor"));
    }

    private float currentRotation = 0f;
    private float targetResetRotation = 0f;
    private boolean isResetting = false;

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (!(blockEntity instanceof IMachineBlockEntity machineBlockEntity) || !(machineBlockEntity.getMetaMachine() instanceof ElectricMultiblockMachine machine) || !machine.isFormed()){
            return;
        }

        float tick = machine.getOffsetTimer() + partialTicks;
        double x = 0.5, y = 13.0, z = 0.5;
        switch (machine.getFrontFacing()) {
            case NORTH -> z = 9.5;
            case SOUTH -> z = -9.5;
            case WEST -> x = 9.5;
            case EAST -> x = -9.5;
        }
        poseStack.translate(x, y, z);
        updateRotationState(machine, partialTicks);
        renderRotationRing(tick, poseStack, buffer);
    }

    @OnlyIn(Dist.CLIENT)
    private void updateRotationState(ElectricMultiblockMachine machine, float partialTicks) {
        final float ROTATION_SPEED = 0.6f;

        if (machine.isActive()) {
            if (isResetting) {
                isResetting = false;
            }
            currentRotation += ROTATION_SPEED * partialTicks;
        } else {
            if (!isResetting) {
                targetResetRotation = (currentRotation > 180) ? 360 : 0;
                isResetting = true;
            }
            currentRotation = moveTowards(currentRotation, targetResetRotation, ROTATION_SPEED * partialTicks);

            if (Math.abs(currentRotation - targetResetRotation) < 0.1f) {
                currentRotation = targetResetRotation;
                isResetting = false;
            }
        }
        currentRotation = (currentRotation % 360 + 360) % 360;
    }

    @OnlyIn(Dist.CLIENT)
    private float moveTowards(float current, float target, float maxDelta) {
        float delta = target - current;
        if (Math.abs(delta) <= maxDelta) {
            return target;
        } else {
            return current + Math.copySign(maxDelta, delta);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void renderRotationRing(float tick, PoseStack poseStack, MultiBufferSource buffer) {
        poseStack.pushPose();
        poseStack.mulPose(new Quaternionf().rotationX(currentRotation * ((float) Math.PI / 180f)));
        ClientUtil.modelRenderer().renderModel(poseStack.last(), buffer.getBuffer(RenderType.entityTranslucent(NANO_PHAGOCYTOSIS_PLANT_RING_PART_A)), null, ClientUtil.getBakedModel(NANO_PHAGOCYTOSIS_PLANT_RING_PART_A), 1.0f, 1.0f, 1.0f, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.entityTranslucent(NANO_PHAGOCYTOSIS_PLANT_RING_PART_A));
        poseStack.popPose();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onAdditionalModel(Consumer<ResourceLocation> registry) {
        super.onAdditionalModel(registry);
        registry.accept(NANO_PHAGOCYTOSIS_PLANT_RING_PART_A);
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
}

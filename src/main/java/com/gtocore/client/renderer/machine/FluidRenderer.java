package com.gtocore.client.renderer.machine;

import com.gtolib.api.machine.feature.multiblock.IFluidRendererMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.client.renderer.block.FluidBlockRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;
import com.gregtechceu.gtceu.client.util.RenderUtil;
import com.gregtechceu.gtceu.config.ConfigHolder;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.RenderTypeHelper;

import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.function.Supplier;

public class FluidRenderer extends WorkableCasingMachineRenderer {

    protected final FluidBlockRenderer fluidBlockRenderer;

    public static Supplier<IRenderer> create(ResourceLocation baseCasing, ResourceLocation workableModel) {
        return () -> new FluidRenderer(baseCasing, workableModel);
    }

    protected FluidRenderer(ResourceLocation baseCasing, ResourceLocation workableModel) {
        super(baseCasing, workableModel);
        fluidBlockRenderer = FluidBlockRenderer.Builder.create()
                .setForcedLight(LightTexture.FULL_BRIGHT)
                .getRenderer();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getViewDistance() {
        return 96;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isGlobalRenderer(BlockEntity blockEntity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasTESR(BlockEntity blockEntity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.render(blockEntity, partialTicks, stack, buffer, combinedLight, combinedOverlay);
        if (!ConfigHolder.INSTANCE.client.renderer.renderFluids) return;
        if (blockEntity instanceof MetaMachineBlockEntity mm) {
            renderFluid(mm.getMetaMachine(), stack, buffer, combinedLight, combinedOverlay);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderFluid(MetaMachine metaMachine, PoseStack stack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (metaMachine instanceof IFluidRendererMachine machine) {
            var cachedFluid = machine.getCachedFluid();
            if (cachedFluid == null) return;
            stack.pushPose();
            fluidBlockRenderer.drawBlocks(machine.getFluidBlockOffsets(), stack.last().pose(), buffer.getBuffer(RenderTypeHelper.getEntityRenderType(ItemBlockRenderTypes.getRenderLayer(cachedFluid.defaultFluidState()), false)), cachedFluid, RenderUtil.FluidTextureType.STILL, combinedOverlay, combinedLight);
            stack.popPose();
        }
    }
}

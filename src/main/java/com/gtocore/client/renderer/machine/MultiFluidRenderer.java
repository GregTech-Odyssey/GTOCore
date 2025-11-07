package com.gtocore.client.renderer.machine;

import com.gtocore.api.machine.IMultiFluidRendererMachine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.client.util.RenderUtil;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.RenderTypeHelper;

import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MultiFluidRenderer extends FluidRenderer {

    public static Supplier<IRenderer> create(ResourceLocation baseCasing, ResourceLocation workableModel) {
        return () -> new MultiFluidRenderer(baseCasing, workableModel);
    }

    protected MultiFluidRenderer(ResourceLocation baseCasing, ResourceLocation workableModel) {
        super(baseCasing, workableModel);
    }

    @Override
    protected void renderFluid(MetaMachine metaMachine, PoseStack stack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.renderFluid(metaMachine, stack, buffer, combinedLight, combinedOverlay);
        if (metaMachine instanceof IMultiFluidRendererMachine machine) {
            for (var entry : machine.getFluidBlockOffsets().asMap().entrySet()) {
                var fluid = entry.getKey();
                if (fluid == null) continue;
                var positions = entry.getValue();
                stack.pushPose();
                fluidBlockRenderer.drawBlocks(positions.stream().filter(Objects::nonNull).map(p -> p.offset(Vec3i.ZERO.subtract(metaMachine.getPos()))).collect(Collectors.toSet()),
                        stack.last().pose(), buffer.getBuffer(RenderTypeHelper.getEntityRenderType(ItemBlockRenderTypes.getRenderLayer(fluid.defaultFluidState()), false)), fluid, RenderUtil.FluidTextureType.STILL, combinedOverlay, combinedLight);
                stack.popPose();
            }
        }
    }
}

package com.gtocore.eio_travel.client.travel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class OutlineBuffer implements MultiBufferSource {

    public static final OutlineBuffer INSTANCE = new OutlineBuffer();

    private OutlineBuffer() {}

    @Override
    public VertexConsumer getBuffer(RenderType type) {
        return Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(OutlineRenderType.get(type));
    }
}

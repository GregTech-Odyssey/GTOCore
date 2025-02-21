package com.gto.gtocore.client.renderer;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

public class GTORenderTypes extends RenderType {

    public static final RenderType LIGHT_CYLINDER = RenderType.create("light_cylinder",
            DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_STRIP, 256, false, false,
            RenderType.CompositeState.builder()
                    .setCullState(NO_CULL)
                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                    .createCompositeState(false));

    private GTORenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize,
                           boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }
}

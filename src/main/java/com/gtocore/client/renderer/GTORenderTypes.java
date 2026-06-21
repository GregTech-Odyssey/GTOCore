package com.gtocore.client.renderer;

import com.gtolib.GTOCore;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class GTORenderTypes extends RenderType {

    private static ShaderInstance blackHoleEventHorizonShader;
    private static final Map<ResourceLocation, ShaderInstance> SHADERS = new ConcurrentHashMap<>();

    private static final ShaderStateShard BLACK_HOLE_EVENT_HORIZON_SHADER = new ShaderStateShard(() -> Objects.requireNonNull(blackHoleEventHorizonShader, "Black hole shader not loaded"));

    public static final RenderType LIGHT_CYLINDER = RenderType.create("light_cylinder",
            DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_STRIP, 256, false, false,
            RenderType.CompositeState.builder()
                    .setCullState(NO_CULL)
                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                    .createCompositeState(false));
    public static final RenderType LIGHT_TRIANGLES = RenderType.create("light_triangles",
            DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES, 131072, false, false,
            RenderType.CompositeState.builder()
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setCullState(CULL)
                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                    .createCompositeState(false));
    public static final RenderType LIGHT_CYLINDER_TEXTURED = RenderType.create("light_cylinder_textured",
            DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.TRIANGLE_STRIP, 131072, true, false,
            RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setCullState(NO_CULL)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setShaderState(RenderStateShard.POSITION_TEX_SHADER)
                    .createCompositeState(false));
    public static final RenderType BLACK_HOLE_CORE = RenderType.create("black_hole_core",
            DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES, 131072, false, false,
            RenderType.CompositeState.builder()
                    .setCullState(NO_CULL)
                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                    .createCompositeState(false));
    public static final RenderType BLACK_HOLE_EVENT_HORIZON = RenderType.create("black_hole_event_horizon",
            DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES, 131072, false, false,
            RenderType.CompositeState.builder()
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setCullState(NO_CULL)
                    .setShaderState(BLACK_HOLE_EVENT_HORIZON_SHADER)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));

    private GTORenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize,
                           boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static final ResourceLocation BLACK_HOLE_EVENT_HORIZON_SHADER_LOCATION = GTOCore.id("black_hole_event_horizon");

    public static final ResourceLocation CRUPTIX = GTOCore.id("cruptix");
    public static final ResourceLocation ITEM_RESONANCE_WAVE = GTOCore.id("item_resonance_wave");

    @OnlyIn(Dist.CLIENT)
    public static void setBlackHoleEventHorizonShader(ShaderInstance shader) {
        blackHoleEventHorizonShader = shader;
        SHADERS.put(BLACK_HOLE_EVENT_HORIZON_SHADER_LOCATION, shader);
    }

    @OnlyIn(Dist.CLIENT)
    public static ShaderInstance getBlackHoleEventHorizonShader() {
        return blackHoleEventHorizonShader;
    }

    @OnlyIn(Dist.CLIENT)
    public static void setShader(ResourceLocation shaderLocation, ShaderInstance shader) {
        SHADERS.put(shaderLocation, shader);
    }

    @OnlyIn(Dist.CLIENT)
    public static ShaderInstance getShader(ResourceLocation shaderLocation) {
        return SHADERS.get(shaderLocation);
    }
}

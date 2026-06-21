package com.gtocore.integration.teammap.client;

import com.gtocore.integration.teammap.bridge.GuiMapBridge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import com.mojang.blaze3d.systems.RenderSystem;
import xaero.lib.client.graphics.shader.LibShaders;
import xaero.map.graphics.CustomRenderTypes;
import xaero.map.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;
import xaero.map.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.map.gui.GuiMap;

/** Draws the standalone team base map through Xaero's own map shader. */
public final class TeamOverlayRenderer {

    private static final int REGION_BLOCKS = 32 * 16;

    public static void render(GuiGraphics graphics, GuiMap map) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || !ClientTeamData.hasTeam()) return;
        GuiMapBridge access = (GuiMapBridge) map;
        ResourceKey<Level> dimension = access.gtoTeamMap$getViewedDimension();
        if (dimension == null) dimension = mc.level.dimension();
        double scale = access.gtoTeamMap$getScale();
        if (!(scale > 0.001 && scale < 128)) return;

        double cameraX = access.gtoTeamMap$getCameraX();
        double cameraZ = access.gtoTeamMap$getCameraZ();
        double halfWorldX = mc.getWindow().getWidth() / (2.0 * scale);
        double halfWorldZ = mc.getWindow().getHeight() / (2.0 * scale);
        int minRegionX = (int) Math.floor((cameraX - halfWorldX) / REGION_BLOCKS);
        int maxRegionX = (int) Math.floor((cameraX + halfWorldX) / REGION_BLOCKS);
        int minRegionZ = (int) Math.floor((cameraZ - halfWorldZ) / REGION_BLOCKS);
        int maxRegionZ = (int) Math.floor((cameraZ + halfWorldZ) / REGION_BLOCKS);

        // The injection point already has Xaero's map scale on the pose stack.
        // Covering the viewport here replaces the personal base while leaving
        // map elements, buttons and tooltips (rendered later) untouched.
        int coverLeft = (int) Math.floor(-halfWorldX) - 2;
        int coverTop = (int) Math.floor(-halfWorldZ) - 2;
        int coverRight = (int) Math.ceil(halfWorldX) + 2;
        int coverBottom = (int) Math.ceil(halfWorldZ) + 2;
        graphics.fill(coverLeft, coverTop, coverRight, coverBottom, 0xFF000000);
        graphics.flush();

        long visibleRegionCount = (long) (maxRegionX - minRegionX + 1) * (maxRegionZ - minRegionZ + 1);
        if (visibleRegionCount > 32) return;

        MultiTextureRenderTypeRendererProvider provider = map.getMapProcessor().getMultiTextureRenderTypeRenderers();
        MultiTextureRenderTypeRenderer renderer = provider.getRenderer(
                texture -> RenderSystem.setShaderTexture(0, texture),
                MultiTextureRenderTypeRendererProvider::defaultTextureBind,
                CustomRenderTypes.MAP);
        TeamRegionTextureCache.beginFrame();
        ResourceLocation dimensionId = dimension.location();
        var matrix = graphics.pose().last().pose();
        for (int regionZ = minRegionZ; regionZ <= maxRegionZ; regionZ++) {
            for (int regionX = minRegionX; regionX <= maxRegionX; regionX++) {
                int texture = TeamRegionTextureCache.get(dimensionId, regionX, regionZ);
                if (texture < 0) continue;
                // A sub-pixel overlap prevents rasterisation cracks between
                // independently submitted region quads without visibly stretching them.
                float overlap = (float) (0.25 / scale);
                float left = (float) (regionX * (double) REGION_BLOCKS - cameraX) - overlap;
                float top = (float) (regionZ * (double) REGION_BLOCKS - cameraZ) - overlap;
                GuiMap.renderTexturedModalRectWithLighting3(matrix, left, top,
                        REGION_BLOCKS + overlap * 2, REGION_BLOCKS + overlap * 2, texture, true, renderer);
            }
        }
        LibShaders.WORLD_MAP.setBrightness(map.getMapProcessor().getBrightness());
        LibShaders.WORLD_MAP.setWithLight(true);
        provider.draw(renderer);
        LibShaders.WORLD_MAP.setWithLight(false);
    }
}

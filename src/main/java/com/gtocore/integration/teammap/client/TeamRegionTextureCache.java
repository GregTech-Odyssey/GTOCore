package com.gtocore.integration.teammap.client;

import com.gtocore.integration.teammap.data.SharedEntry;

import com.gregtechceu.gtceu.integration.map.GroupingMapRenderer;
import com.gregtechceu.gtceu.integration.map.xaeros.worldmap.bedrockore.BedrockOreChunkHighlighter;
import com.gregtechceu.gtceu.integration.map.xaeros.worldmap.fluid.FluidChunkHighlighter;

import net.minecraft.Util;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.thread.ProcessorMailbox;
import net.minecraft.world.level.Level;

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/** Async CPU assembly plus render-thread upload of Xaero-format team regions. */
public final class TeamRegionTextureCache {

    private static final int REGION_CHUNKS = 32;
    private static final int TEXTURE_SIZE = REGION_CHUNKS * 16;
    private static final int MAX_TEXTURES = TeamOverlayRenderer.MAX_VISIBLE_REGIONS;
    private static final int MAX_PENDING = 8;
    private static final FluidChunkHighlighter FLUID_HIGHLIGHTER = new FluidChunkHighlighter();
    private static final BedrockOreChunkHighlighter BEDROCK_ORE_HIGHLIGHTER = new BedrockOreChunkHighlighter();
    private static final Map<RegionKey, CachedTexture> CACHE = new LinkedHashMap<>(16, 0.75f, true);
    private static final Map<RegionKey, PendingBuild> PENDING = new LinkedHashMap<>();
    private static final ProcessorMailbox<Runnable> BUILDER = ProcessorMailbox.create(
            Util.backgroundExecutor(), "GTO Team Map Texture Builder");
    private static int uploadBudget;

    public static void beginFrame() {
        uploadBudget = 1;
    }

    public static int get(ResourceLocation dimension, int regionX, int regionZ) {
        RegionKey key = new RegionKey(dimension, regionX, regionZ);
        long terrainRevision = ClientTeamData.terrainRegionRevision(dimension, regionX, regionZ);
        long gtRevision = NativeGtTeamOverlay.revision();
        int highlightMode = highlightMode();
        CachedTexture cached = CACHE.get(key);
        if (cached != null && cached.terrainRevision() == terrainRevision && cached.gtRevision() == gtRevision && cached.highlightMode() == highlightMode)
            return id(cached);

        // GT layer toggles do not require rebuilding the terrain on the worker.
        if (cached != null && cached.terrainRevision() == terrainRevision && cached.basePixels() != null) {
            if (uploadBudget-- <= 0) return id(cached);
            return upload(key, cached.basePixels(), terrainRevision, gtRevision, highlightMode, cached);
        }

        PendingBuild pending = PENDING.get(key);
        if (pending == null || pending.terrainRevision() != terrainRevision) {
            if (pending != null) pending.future().cancel(false);
            pending = new PendingBuild(terrainRevision, scheduleBuild(key));
            PENDING.put(key, pending);
            trimPending();
        }
        if (!pending.future().isDone() || uploadBudget-- <= 0) return id(cached);

        PENDING.remove(key);
        BuildResult result;
        try {
            result = pending.future().join();
        } catch (RuntimeException ignored) {
            return id(cached);
        }
        if (!result.hasContent()) {
            close(cached == null ? null : cached.texture());
            CACHE.put(key, new CachedTexture(null, terrainRevision, gtRevision, highlightMode, null));
            trim();
            return -1;
        }
        return upload(key, result.pixels(), terrainRevision, gtRevision, highlightMode, cached);
    }

    public static void clear() {
        CACHE.values().forEach(value -> close(value.texture()));
        CACHE.clear();
        PENDING.values().forEach(value -> value.future().cancel(false));
        PENDING.clear();
    }

    private static BuildResult buildBase(RegionKey key) {
        int[] pixels = new int[TEXTURE_SIZE * TEXTURE_SIZE];
        boolean content = false;
        for (int localZ = 0; localZ < REGION_CHUNKS; localZ++) {
            for (int localX = 0; localX < REGION_CHUNKS; localX++) {
                int chunkX = key.regionX() * REGION_CHUNKS + localX;
                int chunkZ = key.regionZ() * REGION_CHUNKS + localZ;
                SharedEntry entry = ClientTeamData.terrain(key.dimension(), chunkX, chunkZ);
                if (entry == null) {
                    if (ClientTeamData.hasBedrockProspection(key.dimension(), chunkX, chunkZ)) {
                        content = true;
                        fillChunk(pixels, localX, localZ, 0x000000FF);
                    }
                    continue;
                }
                int[] colors = colors(entry);
                if (colors.length != 256) continue;
                content = true;
                for (int z = 0; z < 16; z++) {
                    int source = z * 16;
                    int target = (localZ * 16 + z) * TEXTURE_SIZE + localX * 16;
                    System.arraycopy(colors, source, pixels, target, 16);
                }
            }
        }
        return new BuildResult(pixels, content);
    }

    private static CompletableFuture<BuildResult> scheduleBuild(RegionKey key) {
        CompletableFuture<BuildResult> future = new CompletableFuture<>();
        BUILDER.tell(() -> {
            if (future.isCancelled()) return;
            try {
                future.complete(buildBase(key));
            } catch (RuntimeException exception) {
                future.completeExceptionally(exception);
            }
        });
        return future;
    }

    private static int upload(RegionKey key, int[] basePixels, long terrainRevision, long gtRevision,
                              int highlightMode, CachedTexture old) {
        int[] rendered = basePixels.clone();
        applyHighlights(key, rendered);
        XaeroTexture texture = new XaeroTexture(rendered);
        if (old != null) close(old.texture());
        CACHE.put(key, new CachedTexture(texture, terrainRevision, gtRevision, highlightMode, basePixels));
        trim();
        return texture.getId();
    }

    private static void applyHighlights(RegionKey key, int[] pixels) {
        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, key.dimension());
        for (int localZ = 0; localZ < REGION_CHUNKS; localZ++) {
            for (int localX = 0; localX < REGION_CHUNKS; localX++) {
                int chunkX = key.regionX() * REGION_CHUNKS + localX;
                int chunkZ = key.regionZ() * REGION_CHUNKS + localZ;
                applyHighlight(pixels, localX, localZ,
                        FLUID_HIGHLIGHTER.getChunkHighlitColor(dimension, chunkX, chunkZ));
                applyHighlight(pixels, localX, localZ,
                        BEDROCK_ORE_HIGHLIGHTER.getChunkHighlitColor(dimension, chunkX, chunkZ));
            }
        }
    }

    private static int[] colors(SharedEntry entry) {
        int[] exact = entry.payload().getIntArray("xaero_colors");
        if (exact.length == 256) return exact;
        int[] legacy = entry.payload().getIntArray("native_colors");
        if (legacy.length != 256) return new int[0];
        int[] converted = new int[256];
        for (int i = 0; i < converted.length; i++) converted[i] = pack(legacy[i], 255);
        return converted;
    }

    private static int pack(int rgbOrArgb, int light) {
        int red = rgbOrArgb >>> 16 & 255;
        int green = rgbOrArgb >>> 8 & 255;
        int blue = rgbOrArgb & 255;
        return blue << 24 | green << 16 | red << 8 | light;
    }

    private static void applyHighlight(int[] pixels, int localChunkX, int localChunkZ, int[] overlay) {
        if (overlay == null || overlay.length != 256) return;
        for (int z = 0; z < 16; z++) for (int x = 0; x < 16; x++) {
            int color = overlay[z * 16 + x];
            int alpha = color & 255;
            if (alpha == 0) continue;
            float foreground = alpha / 255.0F;
            float background = 1.0F - foreground;
            int offset = (localChunkZ * 16 + z) * TEXTURE_SIZE + localChunkX * 16 + x;
            int base = pixels[offset];
            int red = Math.min(255, (int) (((base >>> 8) & 255) * background + ((color >>> 8) & 255) * foreground));
            int green = Math.min(255, (int) (((base >>> 16) & 255) * background + ((color >>> 16) & 255) * foreground));
            int blue = Math.min(255, (int) (((base >>> 24) & 255) * background + ((color >>> 24) & 255) * foreground));
            pixels[offset] = blue << 24 | green << 16 | red << 8 | (base & 255);
        }
    }

    private static void fillChunk(int[] pixels, int localChunkX, int localChunkZ, int color) {
        for (int z = 0; z < 16; z++) {
            int from = (localChunkZ * 16 + z) * TEXTURE_SIZE + localChunkX * 16;
            java.util.Arrays.fill(pixels, from, from + 16, color);
        }
    }

    private static int highlightMode() {
        GroupingMapRenderer renderer = GroupingMapRenderer.getInstance();
        int result = renderer.doShowLayer("bedrock_fluids") ? 1 : 0;
        if (renderer.doShowLayer("bedrock_ore_veins")) result |= 2;
        return result;
    }

    private static int id(CachedTexture cached) {
        return cached == null || cached.texture() == null ? -1 : cached.texture().getId();
    }

    private static void trim() {
        while (CACHE.size() > MAX_TEXTURES) {
            var iterator = CACHE.entrySet().iterator();
            close(iterator.next().getValue().texture());
            iterator.remove();
        }
    }

    private static void trimPending() {
        while (PENDING.size() > MAX_PENDING) {
            var iterator = PENDING.entrySet().iterator();
            iterator.next().getValue().future().cancel(false);
            iterator.remove();
        }
    }

    private static void close(XaeroTexture texture) {
        if (texture != null) texture.close();
    }

    private static final class XaeroTexture extends AbstractTexture {

        private XaeroTexture(int[] pixels) {
            RenderSystem.assertOnRenderThreadOrInit();
            TextureUtil.prepareImage(getId(), GL11.GL_RGBA8, TEXTURE_SIZE, TEXTURE_SIZE);
            bind();
            ByteBuffer rgba = BufferUtils.createByteBuffer(TEXTURE_SIZE * TEXTURE_SIZE * 4);
            for (int packed : pixels) {
                rgba.put((byte) (packed >>> 8));
                rgba.put((byte) (packed >>> 16));
                rgba.put((byte) (packed >>> 24));
                rgba.put((byte) packed);
            }
            rgba.flip();
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, rgba);
            // Match Xaero's native texture parameters. Clamp-to-edge is the key
            // difference: without it, linear minification samples the opposite
            // side of each independent region and exposes visible seams.
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_BASE_LEVEL, 0);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 0);
        }

        @Override
        public void load(ResourceManager manager) throws IOException {}
    }

    private record RegionKey(ResourceLocation dimension, int regionX, int regionZ) {}

    private record BuildResult(int[] pixels, boolean hasContent) {}

    private record PendingBuild(long terrainRevision, CompletableFuture<BuildResult> future) {}

    private record CachedTexture(XaeroTexture texture, long terrainRevision, long gtRevision,
                                 int highlightMode, int[] basePixels) {}
}

package com.gtocore.integration.teammap.client;

import com.gtocore.integration.teammap.data.SharedEntry;
import com.gtocore.integration.teammap.data.SharedKind;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.worldgen.GTOreDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.ores.GeneratedVeinMetadata;
import com.gregtechceu.gtceu.api.gui.misc.ProspectorMode;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.integration.map.layer.builtin.OreRenderLayer;
import com.gregtechceu.gtceu.integration.map.xaeros.XaerosRenderer;
import com.gregtechceu.gtceu.integration.map.xaeros.minimap.ore.OreVeinElement;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.module.MinimapSession;
import xaero.map.WorldMapSession;

import java.util.HashMap;
import java.util.Map;

/** Adds team records to GTCEu's renderer maps without touching its personal disk cache. */
public final class NativeGtTeamOverlay {

    private static final Map<OreKey, OreVeinElement> TEAM_ORES = new HashMap<>();
    private static final Map<ChunkKey, ProspectorMode.FluidInfo> TEAM_FLUIDS = new HashMap<>();
    private static final Map<ChunkKey, ProspectorMode.OreInfo[]> TEAM_BEDROCK_ORES = new HashMap<>();
    private static boolean dirty;
    private static int dirtyDelay;
    private static long revision;
    private static int validationTicks;

    public static void markDirty() {
        dirty = true;
        dirtyDelay = 5;
    }

    public static long revision() {
        return revision;
    }

    public static void tick() {
        if (dirty && dirtyDelay-- > 0) return;
        if (!dirty && ++validationTicks < 40) return;
        validationTicks = 0;
        if (!dirty && nativeEntriesArePresent()) return;
        dirty = false;
        removeOwnEntries();
        if (ClientTeamData.hasTeam()) ClientTeamData.gtEntries().forEach(NativeGtTeamOverlay::add);
        refreshHighlights();
        revision++;
    }

    public static void removeTeamOre(ResourceKey<Level> dimension, GeneratedVeinMetadata vein) {
        String id = OreRenderLayer.getId(vein);
        OreKey key = new OreKey(dimension, id);
        OreVeinElement own = TEAM_ORES.remove(key);
        if (own != null && XaerosRenderer.oreElements.get(dimension, id) == own) {
            XaerosRenderer.oreElements.remove(dimension, id);
        }
    }

    public static void removeTeamFluid(ResourceKey<Level> dimension, int chunkX, int chunkZ) {
        ChunkKey key = new ChunkKey(dimension, new ChunkPos(chunkX, chunkZ));
        ProspectorMode.FluidInfo own = TEAM_FLUIDS.remove(key);
        if (own != null && XaerosRenderer.fluidElements.get(key.dimension(), key.pos()) == own) {
            XaerosRenderer.fluidElements.remove(key.dimension(), key.pos());
        }
    }

    public static void removeTeamBedrockOre(ResourceKey<Level> dimension, int chunkX, int chunkZ) {
        ChunkKey key = new ChunkKey(dimension, new ChunkPos(chunkX, chunkZ));
        ProspectorMode.OreInfo[] own = TEAM_BEDROCK_ORES.remove(key);
        if (own != null && XaerosRenderer.bedrockOreElements.get(key.dimension(), key.pos()) == own) {
            XaerosRenderer.bedrockOreElements.remove(key.dimension(), key.pos());
        }
    }

    public static ProspectorMode.OreInfo[] teamBedrockOre(ResourceKey<Level> dimension, int chunkX, int chunkZ) {
        SharedEntry entry = ClientTeamData.gtEntry(SharedKind.BEDROCK_ORE, dimension.location(), chunkX, chunkZ);
        if (entry == null) return null;
        try {
            return decodeBedrockOre(entry);
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    public static ProspectorMode.FluidInfo teamFluid(ResourceKey<Level> dimension, int chunkX, int chunkZ) {
        SharedEntry entry = ClientTeamData.gtEntry(SharedKind.BEDROCK_FLUID, dimension.location(), chunkX, chunkZ);
        if (entry == null) return null;
        try {
            return decodeFluid(entry);
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    private static void add(SharedEntry entry) {
        if (entry.kind() == SharedKind.TERRAIN) return;
        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, entry.dimension());
        try {
            switch (entry.kind()) {
                case ORE_VEIN -> addOre(entry, dimension);
                case BEDROCK_FLUID -> addFluid(entry, dimension);
                case BEDROCK_ORE -> addBedrockOre(entry, dimension);
                default -> {}
            }
        } catch (RuntimeException ignored) {
            // A missing registry entry from a changed datapack must not break the map.
        }
    }

    private static void addOre(SharedEntry entry, ResourceKey<Level> dimension) {
        ResourceLocation definitionId = new ResourceLocation(entry.payload().getString("definition"));
        GTOreDefinition definition = GTRegistries.ORE_VEINS.get(definitionId);
        if (definition == null) return;
        GeneratedVeinMetadata vein = new GeneratedVeinMetadata(
                new ResourceLocation(entry.payload().getString("id")),
                new ChunkPos(entry.payload().getLong("origin")),
                BlockPos.of(entry.payload().getLong("center")), definition,
                entry.payload().getBoolean("depleted"));
        String id = OreRenderLayer.getId(vein);
        if (XaerosRenderer.oreElements.get(dimension, id) != null) return;
        OreVeinElement element = new OreVeinElement(vein, OreRenderLayer.getName(vein).getString());
        XaerosRenderer.oreElements.put(dimension, id, element);
        TEAM_ORES.put(new OreKey(dimension, id), element);
    }

    private static void addFluid(SharedEntry entry, ResourceKey<Level> dimension) {
        ChunkPos pos = new ChunkPos(entry.chunkX(), entry.chunkZ());
        if (XaerosRenderer.fluidElements.get(dimension, pos) != null) return;
        ProspectorMode.FluidInfo info = decodeFluid(entry);
        XaerosRenderer.fluidElements.put(dimension, pos, info);
        TEAM_FLUIDS.put(new ChunkKey(dimension, pos), info);
    }

    private static void addBedrockOre(SharedEntry entry, ResourceKey<Level> dimension) {
        ChunkPos pos = new ChunkPos(entry.chunkX(), entry.chunkZ());
        if (XaerosRenderer.bedrockOreElements.get(dimension, pos) != null) return;
        ProspectorMode.OreInfo[] values = decodeBedrockOre(entry);
        if (values == null) return;
        XaerosRenderer.bedrockOreElements.put(dimension, pos, values);
        TEAM_BEDROCK_ORES.put(new ChunkKey(dimension, pos), values);
    }

    private static ProspectorMode.FluidInfo decodeFluid(SharedEntry entry) {
        Fluid fluid = BuiltInRegistries.FLUID.get(new ResourceLocation(entry.payload().getString("fluid")));
        return new ProspectorMode.FluidInfo(fluid,
                entry.payload().getInt("yield"), entry.payload().getInt("left"));
    }

    private static ProspectorMode.OreInfo[] decodeBedrockOre(SharedEntry entry) {
        ListTag list = entry.payload().getList("ores", Tag.TAG_COMPOUND);
        ProspectorMode.OreInfo[] values = new ProspectorMode.OreInfo[list.size()];
        for (int i = 0; i < values.length; i++) {
            var tag = list.getCompound(i);
            Material material = GTCEuAPI.materialManager.getMaterial(tag.getString("material"));
            if (material == null) return null;
            values[i] = new ProspectorMode.OreInfo(material, tag.getInt("weight"),
                    tag.getInt("left"), tag.getInt("yield"));
        }
        return values;
    }

    private static void removeOwnEntries() {
        TEAM_ORES.forEach((key, value) -> {
            if (XaerosRenderer.oreElements.get(key.dimension(), key.id()) == value)
                XaerosRenderer.oreElements.remove(key.dimension(), key.id());
        });
        TEAM_FLUIDS.forEach((key, value) -> {
            if (XaerosRenderer.fluidElements.get(key.dimension(), key.pos()) == value)
                XaerosRenderer.fluidElements.remove(key.dimension(), key.pos());
        });
        TEAM_BEDROCK_ORES.forEach((key, value) -> {
            if (XaerosRenderer.bedrockOreElements.get(key.dimension(), key.pos()) == value)
                XaerosRenderer.bedrockOreElements.remove(key.dimension(), key.pos());
        });
        TEAM_ORES.clear();
        TEAM_FLUIDS.clear();
        TEAM_BEDROCK_ORES.clear();
    }

    private static boolean nativeEntriesArePresent() {
        for (var entry : TEAM_ORES.entrySet())
            if (XaerosRenderer.oreElements.get(entry.getKey().dimension(), entry.getKey().id()) != entry.getValue()) return false;
        for (var entry : TEAM_FLUIDS.entrySet())
            if (XaerosRenderer.fluidElements.get(entry.getKey().dimension(), entry.getKey().pos()) != entry.getValue()) return false;
        for (var entry : TEAM_BEDROCK_ORES.entrySet())
            if (XaerosRenderer.bedrockOreElements.get(entry.getKey().dimension(), entry.getKey().pos()) != entry.getValue()) return false;
        return true;
    }

    private static void refreshHighlights() {
        try {
            WorldMapSession session = WorldMapSession.getCurrentSession();
            if (session != null && session.isUsable() && session.getMapProcessor().getMapWorld() != null) {
                session.getMapProcessor().getMapWorld().getDimensionsList().forEach(dimension -> dimension.getHighlightHandler().clearCachedHashes());
            }
        } catch (RuntimeException ignored) {}
        try {
            if (BuiltInHudModules.MINIMAP.getCurrentSession() instanceof MinimapSession session) {
                var handler = session.getProcessor().getMinimapWriter().getDimensionHighlightHandler();
                if (handler != null) handler.requestRefresh();
            }
        } catch (RuntimeException ignored) {}
    }

    private record OreKey(ResourceKey<Level> dimension, String id) {}

    private record ChunkKey(ResourceKey<Level> dimension, ChunkPos pos) {}
}

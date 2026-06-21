package com.gtocore.integration.teammap.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public record TerrainChunkRecord(ResourceLocation dimension, int chunkX, int chunkZ,
                                 CompoundTag surface, long contentHash, long revision) {

    public SharedEntry asEntry() {
        return new SharedEntry(SharedKind.TERRAIN, dimension, chunkX, chunkZ,
                chunkX + "," + chunkZ, contentHash, revision, surface);
    }
}

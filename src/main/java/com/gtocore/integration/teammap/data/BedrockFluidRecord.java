package com.gtocore.integration.teammap.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public record BedrockFluidRecord(ResourceLocation dimension, int chunkX, int chunkZ,
                                 CompoundTag data, long contentHash, long revision) {

    public SharedEntry asEntry() {
        return new SharedEntry(SharedKind.BEDROCK_FLUID, dimension, chunkX, chunkZ,
                chunkX + "," + chunkZ, contentHash, revision, data);
    }
}

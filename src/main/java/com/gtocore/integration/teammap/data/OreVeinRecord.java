package com.gtocore.integration.teammap.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public record OreVeinRecord(ResourceLocation dimension, ResourceLocation veinId, int chunkX, int chunkZ,
                            CompoundTag data, long contentHash, long revision) {

    public SharedEntry asEntry() {
        return new SharedEntry(SharedKind.ORE_VEIN, dimension, chunkX, chunkZ,
                stableKey(veinId, data.getLong("center")), contentHash, revision, data);
    }

    public static String stableKey(ResourceLocation veinId, long center) {
        return stableKey(veinId.toString(), center);
    }

    public static String stableKey(String veinId, long center) {
        return veinId + "@" + Long.toUnsignedString(center, 16);
    }
}

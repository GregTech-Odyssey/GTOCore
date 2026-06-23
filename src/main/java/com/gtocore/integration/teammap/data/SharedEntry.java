package com.gtocore.integration.teammap.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public record SharedEntry(SharedKind kind, ResourceLocation dimension, int chunkX, int chunkZ,
                          String stableKey, long contentHash, long revision, CompoundTag payload) {

    public SharedEntry {
        Objects.requireNonNull(kind);
        Objects.requireNonNull(dimension);
        Objects.requireNonNull(stableKey);
        payload = payload.copy();
    }

    public String mapKey() {
        return kind.name() + '|' + dimension + '|' + stableKey;
    }

    public SharedEntry withRevision(long value) {
        return new SharedEntry(kind, dimension, chunkX, chunkZ, stableKey, contentHash, value, payload);
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putByte("kind", (byte) kind.ordinal());
        tag.putString("dimension", dimension.toString());
        tag.putInt("chunk_x", chunkX);
        tag.putInt("chunk_z", chunkZ);
        tag.putString("key", stableKey);
        tag.putLong("hash", contentHash);
        tag.putLong("revision", revision);
        tag.put("payload", payload.copy());
        return tag;
    }

    public static SharedEntry fromTag(CompoundTag tag) {
        int ordinal = tag.getByte("kind");
        if (ordinal < 0 || ordinal >= SharedKind.values().length) {
            throw new IllegalArgumentException("Unknown shared entry kind " + ordinal);
        }
        return new SharedEntry(SharedKind.values()[ordinal], ResourceLocation.parse(tag.getString("dimension")),
                tag.getInt("chunk_x"), tag.getInt("chunk_z"), tag.getString("key"),
                tag.getLong("hash"), tag.getLong("revision"), tag.getCompound("payload"));
    }

    public static long hash(CompoundTag tag) {
        long h = 0xcbf29ce484222325L;
        String text = tag.toString();
        for (int i = 0; i < text.length(); i++) {
            h ^= text.charAt(i);
            h *= 0x100000001b3L;
        }
        return h;
    }
}

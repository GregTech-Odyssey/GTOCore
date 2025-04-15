package com.gto.gtocore.utils;

import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.common.saved.CommonSavaedData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import earth.terrarium.adastra.common.utils.ModUtils;

import java.util.UUID;

public final class ServerUtils {

    private ServerUtils() {}

    public static CompoundTag getPersistentData() {
        return CommonSavaedData.getData();
    }

    public static final String IDENTIFIER_KEY = "server_id";

    public static UUID getServerIdentifier() {
        final var data = getPersistentData();

        if (!data.contains(IDENTIFIER_KEY)) {
            data.putUUID(IDENTIFIER_KEY, UUID.randomUUID());
            CommonSavaedData.INSTANCE.setDirty();
        }

        return data.getUUID(IDENTIFIER_KEY);
    }

    public static void runCommandSilent(MinecraftServer server, String command) {
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput(), command);
    }

    public static void teleportToDimension(ServerLevel serverLevel, Entity entity, Vec3 vec3) {
        entity.moveTo(vec3);
        ModUtils.teleportToDimension(entity, serverLevel);
    }

    public static void teleportToDimension(MinecraftServer server, Entity entity, ResourceLocation dim, Vec3 vec3) {
        ServerLevel serverLevel = server.getLevel(GTODimensions.getDimensionKey(dim));
        if (serverLevel == null) return;
        teleportToDimension(serverLevel, entity, vec3);
    }
}

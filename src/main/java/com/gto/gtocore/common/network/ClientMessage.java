package com.gto.gtocore.common.network;

import com.gto.gtocore.api.misc.PlanetManagement;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ClientMessage {

    static void sendData(String channel, @Nullable CompoundTag data) {
        new FromClientMessage(channel, data).sendToServer();
    }

    static void checkPlanetIsUnlocked(ResourceLocation planet) {
        if (PlanetManagement.isClientUnlocked(planet)) return;
        CompoundTag data = new CompoundTag();
        data.putString("planet", planet.toString());
        sendData("planetIsUnlocked", data);
    }

    static void handle(String channel, @NotNull ServerPlayer serverPlayer, CompoundTag data) {
        switch (channel) {
            case "planetIsUnlocked": {
                ResourceLocation planet = new ResourceLocation(data.getString("planet"));
                PlanetManagement.checkIsUnlocked(serverPlayer, planet);
                break;
            }
            default: {
                KeyMessage.pressAction(serverPlayer, channel);
            }
        }
    }
}

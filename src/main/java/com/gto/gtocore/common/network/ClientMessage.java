package com.gto.gtocore.common.network;

import com.gto.gtocore.api.entity.IEnhancedPlayer;
import com.gto.gtocore.common.saved.PlanetsTravelSavaedData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import dev.latvian.mods.kubejs.net.SendDataFromClientMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ClientMessage {

    static void sendData(String channel, @Nullable CompoundTag data) {
        new SendDataFromClientMessage(channel, data).sendToServer();
    }

    static void disableDrift(boolean drift) {
        CompoundTag data = new CompoundTag();
        data.putBoolean("disableDrift", drift);
        sendData("disableDrift", data);
    }

    static void checkPlanetIsUnlocked(ResourceLocation planet) {
        if (PlanetsTravelSavaedData.isClientUnlocked(planet)) return;
        CompoundTag data = new CompoundTag();
        data.putString("planet", planet.toString());
        sendData("planetIsUnlocked", data);
    }

    static void handle(String channel, @NotNull ServerPlayer serverPlayer, CompoundTag data) {
        switch (channel) {
            case "disableDrift": {
                if (serverPlayer instanceof IEnhancedPlayer player) {
                    player.gtocore$setDrift(data.getBoolean("disableDrift"));
                }
                break;
            }
            case "planetIsUnlocked": {
                ResourceLocation planet = new ResourceLocation(data.getString("planet"));
                PlanetsTravelSavaedData.checkIsUnlocked(serverPlayer, planet);
                break;
            }
            default: {
                KeyMessage.pressAction(serverPlayer, channel);
            }
        }
    }
}

package com.gto.gtocore.api.entity;

import com.gto.gtocore.api.data.GTOWorldGenLayers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public interface IEnhancedPlayer {

    boolean gTOCore$canFly();

    boolean gTOCore$isSpaceState();

    boolean gTOCore$isWardenState();

    static boolean spaceTick(ServerLevel level, LivingEntity entity) {
        if (entity instanceof IEnhancedPlayer player) {
            if (player.gTOCore$isSpaceState()) return false;
            return !(player.gTOCore$isWardenState() && level.dimension().location().equals(GTOWorldGenLayers.OTHERSIDE));
        }
        return true;
    }
}

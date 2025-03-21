package com.gto.gtocore.api.entity;

import com.gto.gtocore.api.data.GTODimensions;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface IEnhancedPlayer {

    boolean gTOCore$canFly();

    boolean gTOCore$isSpaceState();

    boolean gTOCore$isWardenState();

    boolean gTOCore$isDisableDrift();

    boolean gTOCore$isAmprosium();

    void gtocore$setDrift(boolean drift);

    static boolean spaceTick(ServerLevel level, LivingEntity entity) {
        if (entity instanceof IEnhancedPlayer player) {
            if (player.gTOCore$isSpaceState()) return false;
            return !(player.gTOCore$isWardenState() && level.dimension().location().equals(GTODimensions.OTHERSIDE));
        }
        return true;
    }

    static float gravity(Entity entity, float gravity) {
        if (entity instanceof IEnhancedPlayer player && player.gTOCore$isAmprosium()) {
            return 0;
        }
        return gravity;
    }
}

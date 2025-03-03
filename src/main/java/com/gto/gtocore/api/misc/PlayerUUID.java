package com.gto.gtocore.api.misc;

import net.minecraft.world.entity.player.Player;

import java.util.Set;
import java.util.UUID;

public interface PlayerUUID {

    UUID MOD_AUTHORS = UUID.fromString("eed176c0-26d1-4a4b-96dc-293217e99544");

    Set<UUID> DEVELOPER = Set.of(MOD_AUTHORS);

    static boolean isDeveloper(Player player) {
        return DEVELOPER.contains(player.getUUID());
    }
}

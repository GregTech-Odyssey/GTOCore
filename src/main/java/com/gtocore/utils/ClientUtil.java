package com.gtocore.utils;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientUtil {

    @OnlyIn(Dist.CLIENT)
    public static boolean isClientPlayer(Entity entity) {
        return entity instanceof AbstractClientPlayer;
    }
}

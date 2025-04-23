package com.gto.gtocore.common.forge;

import com.gto.gtocore.api.playerskill.utils.UtilsAttribute;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExperienceEventHandler {

    // 加入世界
    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
            UtilsAttribute.freshDelayApplyModifier(player);
        }
    }

    // 克隆
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            UtilsAttribute.freshDelayApplyModifier(player);
        }
    }

    // 重生
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            UtilsAttribute.freshDelayApplyModifier(player);
        }
    }

    // 维度切换
    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            UtilsAttribute.freshDelayApplyModifier(player);
        }
    }
}

package com.gto.gtocore.api.playerskill.utils;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.playerskill.data.ExperienceSystemManager;
import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.server.TickTask;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.*;

/**
 * 工具类，用于处理玩家属性修饰符的应用和移除
 * 主要负责根据玩家的经验等级应用相应的属性增益效果
 */
public class UtilsAttribute {

    /**
     * 将指定经验等级的属性修饰符应用到玩家身上
     * 
     * @param player   目标玩家
     * @param expLevel 经验等级对象，包含要应用的属性修饰符
     */
    public static void applyModifiers(Player player, BasicExperienceLevel expLevel) {
        removeAllGTOCoreExpModifiers(player);
        expLevel.getAttributeModifiers().forEach(attribute -> Optional.ofNullable(player.getAttribute(attribute.attribute()))
                .ifPresent(attr -> {
                    try {
                        attr.addPermanentModifier(attribute.getModifier(expLevel));
                    } catch (Exception e) {
                        System.err.println("Error applying modifier: " + e.getMessage());
                    }
                }));
    }

    /**
     * 移除玩家身上所有GTOCore经验系统添加的属性修饰符
     * 
     * @param player 目标玩家
     */
    public static void removeAllGTOCoreExpModifiers(Player player) {
        player.getAttributes().getSyncableAttributes().forEach(attribute -> attribute.getModifiers().stream()
                .filter(modifier -> modifier.getName().contains("gtocore.exp"))
                .map(AttributeModifier::getId)
                .toList()
                .forEach(attribute::removeModifier));
    }

    /**
     * 重新应用玩家所有的属性修饰符
     * 会从经验系统管理器中获取玩家数据，并应用所有经验等级的修饰符
     * 
     * @param player 目标玩家
     */
    public static void freshApplyModifiers(Player player) {
        if (ExperienceSystemManager.INSTANCE == null || !ExperienceSystemManager.INSTANCE.isEnabled()) {
            return;
        }
        GTOCore.LOGGER.info("Fresh apply modifiers for player: {}", player.getName().getString());
        UUID playerId = player.getUUID();
        PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(playerId);
        playerData.getExperienceLevelLists().forEach(level -> UtilsAttribute.applyModifiers(player, level));
    }

    /**
     * 延迟一个游戏刻后重新应用玩家的属性修饰符
     * 
     * @param player 目标玩家
     */
    public static void freshDelayApplyModifier(Player player) {
        Objects.requireNonNull(player.level().getServer()).tell(new TickTask(1, () -> UtilsAttribute.freshApplyModifiers(player)));
    }
}

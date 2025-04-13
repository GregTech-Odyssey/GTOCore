package com.gto.gtocore.api.playerskill.utils;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.playerskill.data.ExperienceSystemManager;
import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.server.TickTask;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class UtilsAttribute {

    public static void applyModifiers(Player player, BasicExperienceLevel expLevel) {
        removeAllGTOCoreExpModifiers(player);
        Arrays.stream(expLevel.getAttributeModifiers())
                .forEach(attribute -> Optional.ofNullable(player.getAttribute(attribute.attribute()))
                        .ifPresent(attr -> {
                            try {
                                attr.addPermanentModifier(attribute.getModifier(expLevel));
                            } catch (Exception e) {
                                System.err.println("Error applying modifier: " + e.getMessage());
                            }
                        }));
    }

    public static void removeAllGTOCoreExpModifiers(Player player) {
        player.getAttributes().getSyncableAttributes().forEach(attribute -> attribute.getModifiers().stream()
                .filter(modifier -> modifier.getName().contains("gtocore.exp"))
                .map(AttributeModifier::getId)
                .toList()
                .forEach(attribute::removeModifier));
    }

    public static void freshApplyModifiers(Player player) {
        if (ExperienceSystemManager.INSTANCE == null || !ExperienceSystemManager.INSTANCE.isEnabled()) {
            return;
        }
        GTOCore.LOGGER.info("Fresh apply modifiers for player: {}", player.getName().getString());
        UUID playerId = player.getUUID();
        PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(playerId);
        playerData.getExperienceLevelLists().forEach(level -> UtilsAttribute.applyModifiers(player, level));
    }

    public static void freshDelayApplyModifier(Player player) {
        Objects.requireNonNull(player.level().getServer()).tell(new TickTask(1, () -> UtilsAttribute.freshApplyModifiers(player)));
    }
}

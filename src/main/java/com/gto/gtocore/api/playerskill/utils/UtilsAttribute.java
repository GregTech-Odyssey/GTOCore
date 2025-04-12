package com.gto.gtocore.api.playerskill.utils;

import com.gto.gtocore.api.playerskill.data.ExperienceSystemManager;
import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.UUID;

public class UtilsAttribute {

    public static void applyModifiers(Player player, BasicExperienceLevel expLevel) {
        for (BasicExperienceLevel.ATTRIBUTE_RECORD attributeRecord : expLevel.getAttributeModifiers()) {
            AttributeModifier modifier = attributeRecord.getModifier(expLevel);
            Objects.requireNonNull(player.getAttribute(attributeRecord.attribute())).removeModifier(modifier); // Prevent
                                                                                                               // duplicates
            Objects.requireNonNull(player.getAttribute(attributeRecord.attribute())).addPermanentModifier(modifier);
        }
    }

    public static void freshApplyModifiers(Player player) {
        if (ExperienceSystemManager.INSTANCE == null || !ExperienceSystemManager.INSTANCE.isEnabled()) {
            return;
        }

        UUID playerId = player.getUUID();
        PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(playerId);
        playerData.getExperienceLevelLists().forEach(level -> {
            UtilsAttribute.applyModifiers(player, level);
        });
    }
}

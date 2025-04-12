package com.gto.gtocore.api.playerskill.experiencelevel.normal;

import com.gto.gtocore.api.playerskill.experiencelevel.NormalExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.special.BodyExperienceLevel;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HealthExperienceLevel extends NormalExperienceLevel {

    List<ATTRIBUTE_RECORD> MODIFIERS_LIST = new ArrayList<>(List.of(
            new ATTRIBUTE_RECORD(Attributes.MAX_HEALTH,
                    "gtocore.health_health_bonus", // level_attribute_bonus
                    UUID.fromString("d9c9b8f0-5a9e-51ee-1v99-2452ac120002"),
                    (expLevel) -> expLevel.getLevel() << 1)));

    public HealthExperienceLevel(BodyExperienceLevel _bodyExperienceLevel) {
        super(_bodyExperienceLevel);
    }

    @Override
    public ATTRIBUTE_RECORD[] getAttributeModifiers() {
        return new ATTRIBUTE_RECORD[] {
                new ATTRIBUTE_RECORD(Attributes.MAX_HEALTH,
                        "gtocore.health_health_bonus", // level_attribute_bonus
                        UUID.fromString("d9c9b8f0-5a9e-51ee-1299-2452ac120002"),
                        (expLevel) -> expLevel.getLevel() << 1)
        };
    }

    @Override
    public String getName() {
        return Component.translatable("gtocore.player_exp_status.health_name").getString();
    }

    @Override
    public int getExperienceForNextLevel() {
        return (int) (100 * Math.pow(1.5, level)); // 示例经验计算
    }
}

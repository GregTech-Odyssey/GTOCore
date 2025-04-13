package com.gto.gtocore.api.playerskill.experiencelevel.normal;

import com.gto.gtocore.api.playerskill.SkillData;
import com.gto.gtocore.api.playerskill.experiencelevel.NormalExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.special.BodyExperienceLevel;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class HealthExperienceLevel extends NormalExperienceLevel {

    public HealthExperienceLevel(BodyExperienceLevel _bodyExperienceLevel) {
        super(_bodyExperienceLevel, SkillData.SkillType.HEALTH);
    }

    @Override
    public ATTRIBUTE_RECORD[] getAttributeModifiers() {
        return new ATTRIBUTE_RECORD[] {
                new ATTRIBUTE_RECORD(Attributes.MAX_HEALTH,
                        "gtocore.exp.health_health_bonus", // level_attribute_bonus
                        UUID.randomUUID(),
                        (expLevel) -> expLevel.getLevel() << 2)
        };
    }

    @Override
    public String getName() {
        return Component.translatable("gtocore.player_exp_status.health_name").getString();
    }

    @Override
    public int getExperienceForNextLevel() {
        return (int) (100 * Math.pow(1.5, level));
    }
}

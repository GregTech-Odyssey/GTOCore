package com.gto.gtocore.api.playerskill.experiencelevel.normal;

import com.gto.gtocore.api.playerskill.SkillData;
import com.gto.gtocore.api.playerskill.experiencelevel.NormalExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.special.BodyExperienceLevel;

import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class AttackExperienceLevel extends NormalExperienceLevel {

    public AttackExperienceLevel(BodyExperienceLevel _bodyExperienceLevel) {
        super(_bodyExperienceLevel, SkillData.SkillType.STRENGTH);
    }

    @Override
    public ATTRIBUTE_RECORD[] getAttributeModifiers() {
        return new ATTRIBUTE_RECORD[] {
                new ATTRIBUTE_RECORD(Attributes.ATTACK_DAMAGE,
                        "gtocore.exp.attack_attack_bonus", // level_attribute_bonus
                        UUID.randomUUID(),
                        (expLevel) -> expLevel.getLevel() << 1)
        };
    }
}

package com.gto.gtocore.api.playerskill.experiencelevel.normal;

import com.gto.gtocore.api.playerskill.SkillRegistry;
import com.gto.gtocore.api.playerskill.experiencelevel.NormalExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.special.LifeIntensityExperienceLevel;

import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class PhysiqueExperienceLevel extends NormalExperienceLevel {

    public PhysiqueExperienceLevel(LifeIntensityExperienceLevel _lifeIntensityExperienceLevel) {
        super(_lifeIntensityExperienceLevel, SkillRegistry.PHYSIQUE);
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
}

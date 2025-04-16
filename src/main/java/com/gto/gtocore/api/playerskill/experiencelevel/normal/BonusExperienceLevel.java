package com.gto.gtocore.api.playerskill.experiencelevel.normal;

import com.gto.gtocore.api.playerskill.SkillRegistry;
import com.gto.gtocore.api.playerskill.experiencelevel.NormalExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.special.LifeIntensityExperienceLevel;

public class BonusExperienceLevel extends NormalExperienceLevel {

    public BonusExperienceLevel(LifeIntensityExperienceLevel _lifeIntensityExperienceLevel) {
        super(_lifeIntensityExperienceLevel, SkillRegistry.BONUS);
    }

    // @Override
    // public ATTRIBUTE_RECORD[] getAttributeModifiers() {
    // return new ATTRIBUTE_RECORD[] {
    // new ATTRIBUTE_RECORD(Attributes.MAX_HEALTH, (expLevel) -> 20L)
    // , new ATTRIBUTE_RECORD(Attributes.ATTACK_DAMAGE, (expLevel) -> 3L)
    // };
    // }
}

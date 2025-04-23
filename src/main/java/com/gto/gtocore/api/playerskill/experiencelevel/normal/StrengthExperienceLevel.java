package com.gto.gtocore.api.playerskill.experiencelevel.normal;

import com.gto.gtocore.api.playerskill.SkillRegistry;
import com.gto.gtocore.api.playerskill.experiencelevel.NormalExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.special.LifeIntensityExperienceLevel;

public class StrengthExperienceLevel extends NormalExperienceLevel {

    public StrengthExperienceLevel(LifeIntensityExperienceLevel _lifeIntensityExperienceLevel) {
        super(_lifeIntensityExperienceLevel, SkillRegistry.STRENGTH);
    }
}

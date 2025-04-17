package com.gto.gtocore.api.playerskill.event;

import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

public abstract class SkillEvent {

    public final BasicExperienceLevel experienceLevel;

    public SkillEvent(BasicExperienceLevel experienceLevel) {
        this.experienceLevel = experienceLevel;
    }
}

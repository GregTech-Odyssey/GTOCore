package com.gto.gtocore.api.playerskill.experiencelevel;

import com.gto.gtocore.api.playerskill.experiencelevel.special.BodyExperienceLevel;

public abstract class NormalExperienceLevel extends BasicExperienceLevel {

    protected BodyExperienceLevel bodyExperienceLevel;

    @Override
    public int getMaxLevel() {
        return (this.bodyExperienceLevel.level) << 1;
    }

    @Override
    public void addExperience(int amount) {
        experience += amount;
        while (experience >= getExperienceForNextLevel() && level < getMaxLevel()) {
            experience -= getExperienceForNextLevel();
            level++;
        }
    }

    protected NormalExperienceLevel(BodyExperienceLevel _bodyExperienceLevel) {
        super();
        this.bodyExperienceLevel = _bodyExperienceLevel;
    }
}

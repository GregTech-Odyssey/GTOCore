package com.gto.gtocore.api.playerskill.experiencelevel;

import com.gto.gtocore.api.playerskill.SkillData;
import com.gto.gtocore.api.playerskill.experiencelevel.special.BodyExperienceLevel;

public abstract class NormalExperienceLevel extends BasicExperienceLevel {

    protected BodyExperienceLevel bodyExperienceLevel;

    @Override
    public void addExperience(int amount) {
        experience += amount;
        while (experience >= getExperienceForNextLevel() && level < getMaxLevel()) {
            experience -= getExperienceForNextLevel();
            level++;
        }
    }

    protected NormalExperienceLevel(BodyExperienceLevel _bodyExperienceLevel, SkillData.SkillType skillType) {
        super(skillType);
        this.bodyExperienceLevel = _bodyExperienceLevel;
    }

    public int getMaxVoltage() {
        return bodyExperienceLevel.getVoltage();
    }

    @Override
    public int getMaxLevel() {
        return (bodyExperienceLevel.getVoltage() + 1) * skillType.LevelStepPerVoltage;
    }
}

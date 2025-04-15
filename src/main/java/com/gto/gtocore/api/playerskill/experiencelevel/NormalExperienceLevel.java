package com.gto.gtocore.api.playerskill.experiencelevel;

import com.gto.gtocore.api.playerskill.SkillData;
import com.gto.gtocore.api.playerskill.experiencelevel.special.LifeIntensityExperienceLevel;

public abstract class NormalExperienceLevel extends BasicExperienceLevel {

    protected LifeIntensityExperienceLevel lifeIntensityExperienceLevel;

    @Override
    public void addExperience(long amount) {
        experience += amount;
        while (experience >= getExperienceForNextLevel() && level < getMaxLevel()) {
            experience -= getExperienceForNextLevel();
            level++;
        }
    }

    protected NormalExperienceLevel(LifeIntensityExperienceLevel _lifeIntensityExperienceLevel, SkillData.SkillType skillType) {
        super(skillType);
        this.lifeIntensityExperienceLevel = _lifeIntensityExperienceLevel;
    }

    public long getMaxVoltage() {
        return lifeIntensityExperienceLevel.getVoltage();
    }

    @Override
    public long getMaxLevel() {
        return (lifeIntensityExperienceLevel.getVoltage() + 1) * skillType.LevelStepPerVoltage;
    }
}

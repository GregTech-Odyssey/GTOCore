package com.gto.gtocore.api.playerskill.experiencelevel;

import com.gto.gtocore.api.playerskill.SkillType;
import com.gto.gtocore.api.playerskill.experiencelevel.special.LifeIntensityExperienceLevel;

public abstract class NormalExperienceLevel extends BasicExperienceLevel {

    protected LifeIntensityExperienceLevel lifeIntensityExperienceLevel;

    @Override
    public void addExperience(long amount) {
        if (this.skillType.getLevelStepPerVoltage() == 0) {
            return;
        } // 不可升级
        experience += amount;
        while (experience >= getExperienceForNextLevel() && level < getMaxLevel()) {
            experience -= getExperienceForNextLevel();
            level++;
        }
    }

    protected NormalExperienceLevel(LifeIntensityExperienceLevel _lifeIntensityExperienceLevel, SkillType skillType) {
        super(skillType);
        this.lifeIntensityExperienceLevel = _lifeIntensityExperienceLevel;
    }

    public long getMaxVoltage() {
        return lifeIntensityExperienceLevel.getVoltage();
    }

    @Override
    public long getMaxLevel() {
        return (lifeIntensityExperienceLevel.getVoltage() + 1) * skillType.getLevelStepPerVoltage();
    }
}

package com.gto.gtocore.api.playerskill.experiencelevel.special;

import com.gto.gtocore.api.playerskill.SkillRegistry;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import com.gregtechceu.gtceu.api.GTValues;

public class LifeIntensityExperienceLevel extends BasicExperienceLevel {

    @Override
    public long getMaxVoltage() { // 0 - GTValues.TIER_COUNT-1
        return (GTValues.TIER_COUNT - 1);
    }

    @Override
    public long getMaxLevel() {
        return getMaxVoltage() * skillType.getLevelStepPerVoltage();
    }

    public LifeIntensityExperienceLevel() {
        super(SkillRegistry.LIFE_INTENSITY);
        this.level = 0;
    }

    @Override
    public ATTRIBUTE_RECORD[] getAttributeModifiers() {
        return new ATTRIBUTE_RECORD[] {};
    }

    public void addExperience(long amount) {
        experience += amount;
        while (experience >= getExperienceForNextLevel()) {
            experience -= getExperienceForNextLevel();
            level++;
        }
    }
}

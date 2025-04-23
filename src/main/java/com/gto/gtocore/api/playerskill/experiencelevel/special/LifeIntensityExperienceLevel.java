package com.gto.gtocore.api.playerskill.experiencelevel.special;

import com.gto.gtocore.api.playerskill.SkillRegistry;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import com.gregtechceu.gtceu.api.GTValues;

import net.minecraft.world.entity.player.Player;

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
    public void freshLevel(Player player) {
        while (experience >= getExperienceForNextLevel()) {
            long experienceForNextLevel = getExperienceForNextLevel();
            experience -= experienceForNextLevel;
            whenLevelAdded(level, level + 1, experienceForNextLevel, player);
            level++;
        }
    }

    public void addExperience(long amount, Player player) {
        whenExperienceAdded(experience, experience + amount, player);
        experience += amount;
        freshLevel(player);
    }
}

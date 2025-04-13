package com.gto.gtocore.api.playerskill.experiencelevel.special;

import com.gto.gtocore.api.playerskill.SkillData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import com.gregtechceu.gtceu.api.GTValues;

import net.minecraft.network.chat.Component;

public class BodyExperienceLevel extends BasicExperienceLevel {
    @Override
    public int getMaxVoltage() { // 0 - GTValues.TIER_COUNT-1
        return (GTValues.TIER_COUNT - 1);
    }

    @Override
    public int getMaxLevel() {
        return getMaxVoltage() * skillType.LevelStepPerVoltage;
    }

    @Override
    public int getVoltage() {
        return (level - 1) / skillType.LevelStepPerVoltage;
    }

    public BodyExperienceLevel() {
        super(SkillData.SkillType.BODY);
        this.level = 0;
    }

    @Override
    public ATTRIBUTE_RECORD[] getAttributeModifiers() {
        return new ATTRIBUTE_RECORD[] {};
    }

    public void addExperience(int amount) {
        experience += amount;
        while (experience >= getExperienceForNextLevel()) {
            experience -= getExperienceForNextLevel();
            level++;
        }
    }

    @Override
    public String getName() {
        return Component.translatable("gtocore.player_exp_status.body_name").getString();
    }

    public int getExperienceForNextLevel() {
        return (int) (100 * Math.pow(1.5, level)); // 示例经验计算
    }
}

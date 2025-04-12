package com.gto.gtocore.api.playerskill.experiencelevel.special;

import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.network.chat.Component;

public class BodyExperienceLevel extends BasicExperienceLevel {

    private static final int maxBodyLevel = 20;

    @Override
    public int getMaxLevel() {
        return maxBodyLevel;
    }

    public BodyExperienceLevel() {
        super();
        this.level = 1;
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

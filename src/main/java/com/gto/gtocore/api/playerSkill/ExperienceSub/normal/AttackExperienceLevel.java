package com.gto.gtocore.api.playerSkill.ExperienceSub.normal;

import com.google.gson.JsonObject;
import com.gto.gtocore.api.playerSkill.ExperienceSub.ExperienceLevel;

public class AttackExperienceLevel extends ExperienceLevel {
    private static final int BASE_ATTACK_POWER = 4; // 基础攻击力

    @Override
    public void addExperience(int amount) {
        experience += amount;
        while (experience >= getExperienceForNextLevel()) {
            experience -= getExperienceForNextLevel();
            level++;
        }
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getExperience() {
        return experience;
    }

    public int getAttackPower() {
        return BASE_ATTACK_POWER + level; // 每级增加1点攻击力
    }

    public int getExperienceForNextLevel() {
        return (int) (100 * Math.pow(1.5, level)); // 示例经验计算
    }

    @Override
    public void saveData(JsonObject jsonObject) {
        jsonObject.addProperty("level", level);
        jsonObject.addProperty("experience", experience);
    }

    @Override
    public void loadData(JsonObject jsonObject) {
        this.level = jsonObject.get("level").getAsInt();
        this.experience = jsonObject.get("experience").getAsInt();
    }
}
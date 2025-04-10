package com.gto.gtocore.api.playerSkill.ExperienceSub;

import com.google.gson.JsonObject;
import lombok.Getter;

public class BodyExperienceLevel {
    @Getter
    protected int level;
    @Getter
    protected int experience;
    private static final int BASE_ATTACK_POWER = 1; // 基础攻击力
    private int maxBodyLevel;

    public BodyExperienceLevel() {
        this.level = 0;
        this.experience = 0;
    }

    public void addExperience(int amount) {
        experience += amount;
        while (experience >= getExperienceForNextLevel()) {
            experience -= getExperienceForNextLevel();
            level++;
        }
    }
    public int getExperienceForNextLevel() {
        return (int) (100 * Math.pow(1.5, level)); // 示例经验计算
    }
    public void saveData(JsonObject jsonObject) {
        jsonObject.addProperty("level", level);
        jsonObject.addProperty("experience", experience);
    }

    public void loadData(JsonObject jsonObject) {
        this.level = jsonObject.get("level").getAsInt();
        this.experience = jsonObject.get("experience").getAsInt();
    }
}
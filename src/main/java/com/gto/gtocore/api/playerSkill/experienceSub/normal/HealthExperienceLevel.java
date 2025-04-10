package com.gto.gtocore.api.playerSkill.experienceSub.normal;

import com.google.gson.JsonObject;
import com.gto.gtocore.api.playerSkill.experienceSub.BodyExperienceLevel;
import com.gto.gtocore.api.playerSkill.experienceSub.NormalExperienceLevel;

public class HealthExperienceLevel extends NormalExperienceLevel {
    private static final int BASE_HEALTH = 20; // 基础生命值

    public HealthExperienceLevel(BodyExperienceLevel _bodyExperienceLevela) {
        super(_bodyExperienceLevela);
    }

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
    public String getName() {
        return "身体强度";
    }

    @Override
    public int getExperience() {
        return experience;
    }

    public int getMaxHealth() {
        return BASE_HEALTH + level * 2; // 每级增加2点生命值
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
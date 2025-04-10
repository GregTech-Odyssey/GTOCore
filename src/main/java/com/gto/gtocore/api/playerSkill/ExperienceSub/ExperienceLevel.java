package com.gto.gtocore.api.playerSkill.ExperienceSub;

import com.google.gson.JsonObject;

public abstract class ExperienceLevel {
    protected int level;
    protected int experience;

    public ExperienceLevel() {
        this.level = 0;
        this.experience = 0;
    }

    public abstract void addExperience(int amount);
    public abstract int getLevel();
    public abstract int getExperience();
    public abstract void saveData(JsonObject jsonObject);
    public abstract void loadData(JsonObject jsonObject);
}
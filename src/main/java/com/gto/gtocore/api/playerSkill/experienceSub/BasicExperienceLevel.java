package com.gto.gtocore.api.playerSkill.experienceSub;

import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;

public abstract class BasicExperienceLevel {
    protected int level;
    protected int experience;


    public BasicExperienceLevel() {
        this.level = 0;
        this.experience = 0;
    }

    public ChatFormatting getNameColor(){
        return ChatFormatting.GOLD;
    }

    public int getMaxLevel(){
        return 20;
    }

    public abstract void addExperience(int amount);
    public abstract int getLevel();
    public abstract String getName();
    public abstract int getExperienceForNextLevel();
    public abstract int getExperience();
    public abstract void saveData(JsonObject jsonObject);
    public abstract void loadData(JsonObject jsonObject);
}

package com.gto.gtocore.api.playerSkill.experienceSub;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;

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
    public abstract void saveData(CompoundTag nbt);
    public abstract void loadData(CompoundTag nbt);
}
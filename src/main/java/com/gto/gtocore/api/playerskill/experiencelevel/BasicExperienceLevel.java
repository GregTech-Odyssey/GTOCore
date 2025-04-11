package com.gto.gtocore.api.playerskill.experiencelevel;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;

import lombok.Getter;

@Getter
public abstract class BasicExperienceLevel {

    protected int level;
    protected int experience;

    protected BasicExperienceLevel() {
        this.level = 0;
        this.experience = 0;
    }

    public ChatFormatting getNameColor() {
        return ChatFormatting.GOLD;
    }

    public int getMaxLevel() {
        return 20;
    }

    public void saveData(CompoundTag nbt) {
        nbt.putInt("level", level);
        nbt.putInt("experience", experience);
    }

    public void loadData(CompoundTag nbt) {
        this.level = nbt.getInt("level");
        this.experience = nbt.getInt("experience");
    }

    public abstract void addExperience(int amount);

    public abstract String getName();

    public abstract int getExperienceForNextLevel();
}

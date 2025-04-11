package com.gto.gtocore.api.playerSkill.experienceSub;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

public class BodyExperienceLevel extends BasicExperienceLevel {

    private static final int BASE_ATTACK_POWER = 1; // 基础攻击力
    private int maxBodyLevel;

    public BodyExperienceLevel() {
        super();
        this.level = 1;
    }

    public void addExperience(int amount) {
        experience += amount;
        while (experience >= getExperienceForNextLevel()) {
            experience -= getExperienceForNextLevel();
            level++;
        }
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public String getName() {return Component.translatable("gtocore.player_exp_status.body_name").getString();}

    @Override
    public int getExperience() {
        return this.experience;
    }

    public int getExperienceForNextLevel() {
        return (int) (100 * Math.pow(1.5, level)); // 示例经验计算
    }

    @Override
    public void saveData(CompoundTag nbt) {
        nbt.putInt("level", level);
        nbt.putInt("experience", experience);
    }

    @Override
    public void loadData(CompoundTag nbt) {
        this.level = nbt.getInt("level");
        this.experience = nbt.getInt("experience");
    }
}
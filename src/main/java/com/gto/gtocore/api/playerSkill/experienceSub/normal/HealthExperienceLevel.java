package com.gto.gtocore.api.playerSkill.experienceSub.normal;

import com.google.gson.JsonObject;
import com.gto.gtocore.api.playerSkill.experienceSub.BodyExperienceLevel;
import com.gto.gtocore.api.playerSkill.experienceSub.NormalExperienceLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

public class HealthExperienceLevel extends NormalExperienceLevel {
    private static final int BASE_HEALTH = 20; // 基础生命值

    public HealthExperienceLevel(BodyExperienceLevel _bodyExperienceLevela) {
        super(_bodyExperienceLevela);
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public String getName() {
        return Component.translatable("gtocore.player_exp_status.health_name").getString();
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
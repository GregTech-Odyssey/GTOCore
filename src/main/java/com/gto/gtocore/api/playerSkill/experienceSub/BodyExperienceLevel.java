package com.gto.gtocore.api.playerSkill.experienceSub;

import com.google.gson.JsonObject;
import lombok.Getter;
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
    public void saveData(JsonObject jsonObject) {
        jsonObject.addProperty("level", level);
        jsonObject.addProperty("experience", experience);
    }

    public void loadData(JsonObject jsonObject) {
        this.level = jsonObject.get("level").getAsInt();
        this.experience = jsonObject.get("experience").getAsInt();
    }
}
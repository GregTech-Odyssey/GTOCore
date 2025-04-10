package com.gto.gtocore.api.playerSkill;

import com.google.gson.JsonObject;
import com.gto.gtocore.api.playerSkill.ExperienceSub.normal.AttackExperienceLevel;
import com.gto.gtocore.api.playerSkill.ExperienceSub.BodyExperienceLevel;
import com.gto.gtocore.api.playerSkill.ExperienceSub.normal.HealthExperienceLevel;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PlayerData {
    private UUID playerId;
    private HealthExperienceLevel healthExperience;
    private AttackExperienceLevel attackExperience;
    private BodyExperienceLevel bodyExperienceLevel;

    public PlayerData(UUID playerId) {
        this.playerId = playerId;
        this.healthExperience = new HealthExperienceLevel();
        this.attackExperience = new AttackExperienceLevel();
        this.bodyExperienceLevel = new BodyExperienceLevel();
    }

    public void addHealthExperience(int amount) {
        healthExperience.addExperience(amount);
    }

    public void addAttackExperience(int amount) {
        attackExperience.addExperience(amount);
    }

    public void addBodyExperience(int amount) { bodyExperienceLevel.addExperience(amount);}

    public void saveData(JsonObject jsonObject) {
        JsonObject healthData = new JsonObject();
        healthExperience.saveData(healthData);
        jsonObject.add("healthExperience", healthData);

        JsonObject attackData = new JsonObject();
        attackExperience.saveData(attackData);
        jsonObject.add("attackExperience", attackData);

        JsonObject bodyData = new JsonObject();
        bodyExperienceLevel.saveData(bodyData);
        jsonObject.add("bodyExperience", bodyData);
    }

    public void loadData(JsonObject jsonObject) {
        bodyExperienceLevel.loadData(jsonObject.getAsJsonObject("bodyExperience"));
        healthExperience.loadData(jsonObject.getAsJsonObject("healthExperience"));
        attackExperience.loadData(jsonObject.getAsJsonObject("attackExperience"));
    }
}
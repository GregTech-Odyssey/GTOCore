package com.gto.gtocore.api.playerSkill.logic;

import com.google.gson.JsonObject;
import com.gto.gtocore.api.playerSkill.experienceSub.BasicExperienceLevel;
import com.gto.gtocore.api.playerSkill.experienceSub.normal.AttackExperienceLevel;
import com.gto.gtocore.api.playerSkill.experienceSub.BodyExperienceLevel;
import com.gto.gtocore.api.playerSkill.experienceSub.normal.HealthExperienceLevel;
import com.gto.gtocore.api.playerSkill.utils.utilsData;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Getter
public class PlayerData {
    private final UUID playerId;
    private final HealthExperienceLevel healthExperienceLevel;
    private final AttackExperienceLevel attackExperienceLevel;
    private final BodyExperienceLevel bodyExperienceLevel;

    public PlayerData(UUID playerId) {
        this.playerId = playerId;
        this.bodyExperienceLevel = new BodyExperienceLevel();
        this.healthExperienceLevel = new HealthExperienceLevel(bodyExperienceLevel);
        this.attackExperienceLevel = new AttackExperienceLevel(bodyExperienceLevel);
    }

    public void addHealthExperience(int amount) {
        healthExperienceLevel.addExperience(amount);
    }

    public void addAttackExperience(int amount) {
        attackExperienceLevel.addExperience(amount);
    }

    public void addBodyExperience(int amount) { bodyExperienceLevel.addExperience(amount);}

    public List<BasicExperienceLevel> getExperienceLevelLists() {
        return List.of(bodyExperienceLevel, healthExperienceLevel, attackExperienceLevel);
    }

    public void saveData(JsonObject jsonObject) {
        utilsData.saveExperienceToJson("bodyExperience", bodyExperienceLevel, jsonObject);
        utilsData.saveExperienceToJson("healthExperienceLevel", healthExperienceLevel, jsonObject);
        utilsData.saveExperienceToJson("attackExperienceLevel", attackExperienceLevel, jsonObject);
    }

    public void loadData(JsonObject jsonObject) {
        loadExperience(jsonObject, "bodyExperience", bodyExperienceLevel, BodyExperienceLevel::new);
        loadExperience(jsonObject, "healthExperienceLevel", healthExperienceLevel, () -> new HealthExperienceLevel(bodyExperienceLevel));
        loadExperience(jsonObject, "attackExperienceLevel", attackExperienceLevel, () -> new AttackExperienceLevel(bodyExperienceLevel));
    }

    private void loadExperience(JsonObject jsonObject, String jsonExperienceKey, BasicExperienceLevel experienceLevel, Supplier<BasicExperienceLevel> initializer) {
        if (jsonObject.has(jsonExperienceKey)) {
            experienceLevel.loadData(jsonObject.getAsJsonObject(jsonExperienceKey));
        } else {
            experienceLevel = initializer.get();
        }
    }
}
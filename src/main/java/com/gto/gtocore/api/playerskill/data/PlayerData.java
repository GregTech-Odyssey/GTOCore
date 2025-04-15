package com.gto.gtocore.api.playerskill.data;

import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.normal.AttackExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.normal.HealthExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.special.BodyExperienceLevel;
import com.gto.gtocore.api.playerskill.utils.UtilsData;

import net.minecraft.nbt.CompoundTag;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

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

    public void addHealthExperience(long amount) {
        healthExperienceLevel.addExperience(amount);
    }

    public void addAttackExperience(long amount) {
        attackExperienceLevel.addExperience(amount);
    }

    public void addBodyExperience(long amount) {
        bodyExperienceLevel.addExperience(amount);
    }

    public List<BasicExperienceLevel> getExperienceLevelLists() {
        return List.of(bodyExperienceLevel, healthExperienceLevel, attackExperienceLevel);
    }

    public void saveData(CompoundTag nbt) {
        UtilsData.saveExperienceToNbt("bodyExperience", bodyExperienceLevel, nbt);
        UtilsData.saveExperienceToNbt("healthExperienceLevel", healthExperienceLevel, nbt);
        UtilsData.saveExperienceToNbt("attackExperienceLevel", attackExperienceLevel, nbt);
    }

    public void loadData(CompoundTag nbt) {
        loadExperience(nbt, "bodyExperience", bodyExperienceLevel);
        loadExperience(nbt, "healthExperienceLevel", healthExperienceLevel);
        loadExperience(nbt, "attackExperienceLevel", attackExperienceLevel);
    }

    private static void loadExperience(CompoundTag nbt, String nbtKey, BasicExperienceLevel experienceLevel) {
        if (nbt.contains(nbtKey)) {
            experienceLevel.loadData(nbt.getCompound(nbtKey));
        }
    }
}

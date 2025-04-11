package com.gto.gtocore.api.playerskill.logic;

import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.special.BodyExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.normal.AttackExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.normal.HealthExperienceLevel;
import com.gto.gtocore.api.playerskill.utils.UtilsData;

import net.minecraft.nbt.CompoundTag;

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

    public void addBodyExperience(int amount) {
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
        loadExperience(nbt, "bodyExperience", bodyExperienceLevel, BodyExperienceLevel::new);
        loadExperience(nbt, "healthExperienceLevel", healthExperienceLevel, () -> new HealthExperienceLevel(bodyExperienceLevel));
        loadExperience(nbt, "attackExperienceLevel", attackExperienceLevel, () -> new AttackExperienceLevel(bodyExperienceLevel));
    }

    private static void loadExperience(CompoundTag nbt, String nbtKey, BasicExperienceLevel experienceLevel, Supplier<BasicExperienceLevel> initializer) {
        if (nbt.contains(nbtKey)) {
            experienceLevel.loadData(nbt.getCompound(nbtKey));
        } else {
            experienceLevel = initializer.get();
        }
    }
}

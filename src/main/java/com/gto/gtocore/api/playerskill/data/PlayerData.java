package com.gto.gtocore.api.playerskill.data;

import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.normal.BonusExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.normal.PhysiqueExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.normal.StrengthExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.special.LifeIntensityExperienceLevel;
import com.gto.gtocore.api.playerskill.utils.UtilsData;

import net.minecraft.nbt.CompoundTag;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class PlayerData {

    private final UUID playerId;
    private final PhysiqueExperienceLevel physiqueExperienceLevel;
    private final StrengthExperienceLevel strengthExperienceLevel;
    private final LifeIntensityExperienceLevel lifeIntensityExperienceLevel;
    private final BonusExperienceLevel bonusExperienceLevel;

    public PlayerData(UUID playerId) {
        this.playerId = playerId;
        this.lifeIntensityExperienceLevel = new LifeIntensityExperienceLevel();
        this.physiqueExperienceLevel = new PhysiqueExperienceLevel(lifeIntensityExperienceLevel);
        this.strengthExperienceLevel = new StrengthExperienceLevel(lifeIntensityExperienceLevel);
        this.bonusExperienceLevel = new BonusExperienceLevel(lifeIntensityExperienceLevel);
    }

    public List<BasicExperienceLevel> getExperienceLevelLists() {
        return List.of(lifeIntensityExperienceLevel, physiqueExperienceLevel, strengthExperienceLevel, bonusExperienceLevel);
    }

    public void saveData(CompoundTag nbt) {
        UtilsData.saveExperienceToNbt(lifeIntensityExperienceLevel.skillType.getNbtKey(), lifeIntensityExperienceLevel, nbt);
        UtilsData.saveExperienceToNbt(physiqueExperienceLevel.skillType.getNbtKey(), physiqueExperienceLevel, nbt);
        UtilsData.saveExperienceToNbt(strengthExperienceLevel.skillType.getNbtKey(), strengthExperienceLevel, nbt);
        UtilsData.saveExperienceToNbt(bonusExperienceLevel.skillType.getNbtKey(), bonusExperienceLevel, nbt);
    }

    public void loadData(CompoundTag nbt) {
        loadExperience(nbt, lifeIntensityExperienceLevel.skillType.getNbtKey(), lifeIntensityExperienceLevel);
        loadExperience(nbt, physiqueExperienceLevel.skillType.getNbtKey(), physiqueExperienceLevel);
        loadExperience(nbt, strengthExperienceLevel.skillType.getNbtKey(), strengthExperienceLevel);
        loadExperience(nbt, bonusExperienceLevel.skillType.getNbtKey(), bonusExperienceLevel);
    }

    private static void loadExperience(CompoundTag nbt, String nbtKey, BasicExperienceLevel experienceLevel) {
        if (nbt.contains(nbtKey)) {
            experienceLevel.loadData(nbt.getCompound(nbtKey));
        }
    }
}

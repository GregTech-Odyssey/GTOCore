package com.gto.gtocore.api.playerskill.experiencelevel;

import com.gto.gtocore.api.playerskill.SkillType;
import com.gto.gtocore.api.playerskill.data.AttributeRecord;
import com.gto.gtocore.api.playerskill.data.ExperienceSystemManager;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class BasicExperienceLevel {

    protected long level;
    protected long experience;
    public SkillType skillType;

    protected BasicExperienceLevel(SkillType skillType) {
        this.level = 0;
        this.experience = 0;
        this.skillType = skillType;
    }

    public ChatFormatting getNameColor() {
        return ChatFormatting.GOLD;
    }

    public long getVoltage() {
        return skillType.getLevelStepPerVoltage() == 0 ? 0 : (level - 1) / skillType.getLevelStepPerVoltage();
    }

    public abstract long getMaxVoltage();

    public void saveData(CompoundTag nbt) {
        nbt.putLong("level", level);
        nbt.putLong("experience", experience);
    }

    public void loadData(CompoundTag nbt) {
        this.level = nbt.getLong("level");
        this.experience = nbt.getLong("experience");
    }

    public List<AttributeRecord> getAttributeModifiers() {
        return this.skillType.getAttributeRecords();
    }

    public abstract long getMaxLevel();

    public abstract void addExperience(long amount);

    public void setLevel(long amount) {
        this.level = amount;
        ExperienceSystemManager.INSTANCE.saveAll();
    }

    public void setExperience(long amount) {
        this.experience = amount;
        ExperienceSystemManager.INSTANCE.saveAll();
    }

    public String getName() {
        return skillType.getName();
    }

    public long getExperienceForNextLevel() {
        return skillType.getNextLevelExperienceFormula().applyAsLong(this);
    }
}

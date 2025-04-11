package com.gto.gtocore.api.playerSkill.experienceSub.normal;

import net.minecraft.nbt.CompoundTag;
import com.gto.gtocore.api.playerSkill.experienceSub.BodyExperienceLevel;
import com.gto.gtocore.api.playerSkill.experienceSub.NormalExperienceLevel;
import net.minecraft.network.chat.Component;

public class AttackExperienceLevel extends NormalExperienceLevel {
    private static final int BASE_ATTACK_POWER = 4; // 基础攻击力

    public AttackExperienceLevel(BodyExperienceLevel _bodyExperienceLevel) {
        super(_bodyExperienceLevel);
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public String getName() {
        return Component.translatable("gtocore.player_exp_status.attack_name").getString();
    }

    @Override
    public int getExperience() {
        return experience;
    }

    public int getAttackPower() {
        return BASE_ATTACK_POWER + level; // 每级增加1点攻击力
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
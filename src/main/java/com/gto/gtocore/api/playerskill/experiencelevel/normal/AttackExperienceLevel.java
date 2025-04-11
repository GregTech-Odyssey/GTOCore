package com.gto.gtocore.api.playerskill.experiencelevel.normal;

import com.gto.gtocore.api.playerskill.experiencelevel.special.BodyExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.NormalExperienceLevel;

import net.minecraft.network.chat.Component;

public class AttackExperienceLevel extends NormalExperienceLevel {

    private static final int BASE_ATTACK_POWER = 4; // 基础攻击力

    public AttackExperienceLevel(BodyExperienceLevel _bodyExperienceLevel) {
        super(_bodyExperienceLevel);
    }

    @Override
    public String getName() {
        return Component.translatable("gtocore.player_exp_status.attack_name").getString();
  }



    public int getAttackPower() {
        return BASE_ATTACK_POWER + level; // 每级增加1点攻击力
    }

    @Override
    public int getExperienceForNextLevel() {
        return (int) (100 * Math.pow(1.5, level)); // 示例经验计算
    }
}

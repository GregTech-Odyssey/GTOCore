package com.gto.gtocore.api.playerskill.experiencelevel.normal;

import com.gto.gtocore.api.playerskill.experiencelevel.special.BodyExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.NormalExperienceLevel;

import net.minecraft.network.chat.Component;

public class HealthExperienceLevel extends NormalExperienceLevel {

    private static final int BASE_HEALTH = 20; // 基础生命值

    public HealthExperienceLevel(BodyExperienceLevel _bodyExperienceLevela) {
        super(_bodyExperienceLevela);
    }

    @Override
    public String getName() {
        return Component.translatable("gtocore.player_exp_status.health_name").getString();
    }

    public int getMaxHealth() {
        return BASE_HEALTH + (level << 1); // 每级增加2点生命值
    }

    @Override
    public int getExperienceForNextLevel() {
        return (int) (100 * Math.pow(1.5, level)); // 示例经验计算
    }
}

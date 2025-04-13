package com.gto.gtocore.api.playerskill;

import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.network.chat.Component;

import java.util.Map;

public final class SkillData {

    private SkillData() {}

    public enum SkillType {

        BODY("gtocore.player_exp_status.body_name", 3),
        HEALTH("gtocore.player_exp_status.health_name", 2),
        ATTACK("gtocore.player_exp_status.attack_name", 3);

        public final String NameTranslateKey;
        public final int LevelStepPerVoltage;

        SkillType(String nameTranslateKey, int levelStepPerVoltage) {
            NameTranslateKey = nameTranslateKey;
            LevelStepPerVoltage = levelStepPerVoltage;
        }

        public String getName() {
            return Component.translatable(this.NameTranslateKey).getString();
        }

        public BasicExperienceLevel getExperienceLevel(PlayerData playerData) {
            return switch (this) {
                case HEALTH -> playerData.getHealthExperienceLevel();
                case ATTACK -> playerData.getAttackExperienceLevel();
                case BODY -> playerData.getBodyExperienceLevel();
            };
        }
    }

    public static class GainExperience {

        public static final int GAP_TICK = 20 * 60 * 60 * 2; // 2hour
        public static final Map<SkillType, Integer> EXPERIENCE_RATES = Map.of(
                SkillType.HEALTH, 15,
                SkillType.ATTACK, 15,
                SkillType.BODY, 5);
    } // 定时免费的升级，配置每多少tick给各个技能加多少经验
}

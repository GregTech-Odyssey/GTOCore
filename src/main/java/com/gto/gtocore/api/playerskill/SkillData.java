package com.gto.gtocore.api.playerskill;

import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.function.Supplier;

public final class SkillData {

    private SkillData() {}

    public enum SkillType {
        BODY("gtocore.player_exp_status.body_name", 3,"生命强度","Life Intensity"),
        HEALTH("gtocore.player_exp_status.health_name", 2,"体格","Physique"),
        ATTACK("gtocore.player_exp_status.attack_name", 3,"力量","Strength");

        public final String NameTranslateKey;
        public final int LevelStepPerVoltage;
        public final String chineseName;
        public final String englishName;

        SkillType(String nameTranslateKey, int levelStepPerVoltage, String chineseName, String englishName) {
            NameTranslateKey = nameTranslateKey;
            LevelStepPerVoltage = levelStepPerVoltage;
            this.chineseName = chineseName;
            this.englishName = englishName;
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

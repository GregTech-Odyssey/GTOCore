package com.gto.gtocore.api.playerskill;

import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;
import com.gto.gtocore.api.playerskill.logic.PlayerData;

import java.util.Map;

public final class SkillData {
    private SkillData() {}
    public enum SkillType {
        HEALTH, ATTACK, BODY;
        public BasicExperienceLevel getExperienceLevel(PlayerData playerData) {
            return switch (this) {
                case HEALTH -> playerData.getHealthExperienceLevel();
                case ATTACK -> playerData.getAttackExperienceLevel();
                case BODY -> playerData.getBodyExperienceLevel();
                default -> throw new IllegalStateException("Unexpected skill type: " + this);
            };
        }
    }


    public static class GainExperience {
        public static final int GAP_TICK = 20*60*60;
        public static final Map<SkillType, Integer> EXPERIENCE_RATES = Map.of(
                SkillType.HEALTH, 15,
                SkillType.ATTACK, 15,
                SkillType.BODY, 10
        );
    } // 配置每多少tick给各个技能加多少经验
}
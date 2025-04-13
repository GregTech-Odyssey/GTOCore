package com.gto.gtocore.api.playerskill;

import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import java.util.Map;
import java.util.Set;

public final class SkillData {

    private SkillData() {}

    public static final Set<String> MEAT_KEYWORDS = Set.of("porkchop", "beef", "chicken", "mutton", "rabbit", "cod", "salmon", "ham");

    public enum SkillType {

        HEALTH,
        ATTACK,
        BODY;

        public BasicExperienceLevel getExperienceLevel(PlayerData playerData) {
            return switch (this) {
                case HEALTH -> playerData.getHealthExperienceLevel();
                case ATTACK -> playerData.getAttackExperienceLevel();
                case BODY -> playerData.getBodyExperienceLevel();
            };
        }
    }

    public static class ExperienceIncome {

        public static final int EAT_MEAT = 10;
    }

    public static class GainExperience {

        public static final int GAP_TICK = 20 * 60 * 60;
        public static final Map<SkillType, Integer> EXPERIENCE_RATES = Map.of(
                SkillType.HEALTH, 15,
                SkillType.ATTACK, 15,
                SkillType.BODY, 10);
    } // 配置每多少tick给各个技能加多少经验
}

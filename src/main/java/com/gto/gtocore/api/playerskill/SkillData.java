package com.gto.gtocore.api.playerskill;

import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.network.chat.Component;

import lombok.Builder;

import java.util.Map;
import java.util.function.*;

public final class SkillData {

    private SkillData() {}

    public enum SkillType {

        LIFE_INTENSITY(SkillTypeBuilder.builder()
                .NameTranslateKey("gtocore.player_exp_status.body_name")
                .LevelStepPerVoltage(3)
                .chineseName("生命强度")
                .englishName("Life Intensity")
                .nextLevelExperienceFormula(level -> (long) (100 * Math.pow(2, level.getLevel())))
                .experienceLevelGetter(PlayerData::getLifeIntensityExperienceLevel)
                .upgradePackageBonusFormula((tierGap, experienceForNextLevel) -> (long) (experienceForNextLevel * Math.pow(2, tierGap) * ((double) 1 / 16)))
                .nbtKey("bodyExperience")
                .build()),

        PHYSIQUE(SkillTypeBuilder.builder()
                .NameTranslateKey("gtocore.player_exp_status.health_name")
                .LevelStepPerVoltage(5)
                .chineseName("体格")
                .englishName("Physique")
                .nextLevelExperienceFormula(level -> (long) (100 * Math.pow(1.5, level.getLevel())))
                .experienceLevelGetter(PlayerData::getPhysiqueExperienceLevel)
                .upgradePackageBonusFormula((tierGap, experienceForNextLevel) -> (long) (experienceForNextLevel * Math.pow(2, tierGap) * ((double) 1 / 4)))
                .nbtKey("healthExperienceLevel")
                .build()),

        STRENGTH(SkillTypeBuilder.builder()
                .NameTranslateKey("gtocore.player_exp_status.attack_name")
                .LevelStepPerVoltage(5)
                .chineseName("力量")
                .englishName("Strength")
                .nextLevelExperienceFormula(level -> (long) (100 * Math.pow(1.5, level.getLevel())))
                .experienceLevelGetter(PlayerData::getStrengthExperienceLevel)
                .upgradePackageBonusFormula((tierGap, experienceForNextLevel) -> (long) (experienceForNextLevel * Math.pow(2, tierGap) * ((double) 1 / 4)))
                .nbtKey("attackExperienceLevel")
                .build());

        public final String NameTranslateKey;
        public final long LevelStepPerVoltage;
        public final String chineseName;
        public final String englishName;
        public final String nbtKey; // 存储数据，不能改
        public final ToLongFunction<BasicExperienceLevel> nextLevelExperienceFormula;
        public final Function<PlayerData, BasicExperienceLevel> experienceLevelGetter;
        public final ToLongBiFunction<Long, Long> upgradePackageBonusFormula;

        SkillType(SkillTypeBuilder builder) {
            this.NameTranslateKey = builder.NameTranslateKey;
            this.LevelStepPerVoltage = builder.LevelStepPerVoltage;
            this.chineseName = builder.chineseName;
            this.englishName = builder.englishName;
            this.nextLevelExperienceFormula = builder.nextLevelExperienceFormula;
            this.experienceLevelGetter = builder.experienceLevelGetter;
            this.upgradePackageBonusFormula = builder.upgradePackageBonusFormula;
            this.nbtKey = builder.nbtKey;
        }

        public String getName() {
            return Component.translatable(this.NameTranslateKey).getString();
        }

        public BasicExperienceLevel getExperienceLevel(PlayerData playerData) {
            return experienceLevelGetter.apply(playerData);
        }

        @Builder
        private static class SkillTypeBuilder {

            private String NameTranslateKey;
            private long LevelStepPerVoltage;
            private String chineseName;
            private String englishName;
            public final String nbtKey;
            private ToLongFunction<BasicExperienceLevel> nextLevelExperienceFormula;
            private Function<PlayerData, BasicExperienceLevel> experienceLevelGetter;
            public ToLongBiFunction<Long, Long> upgradePackageBonusFormula;
        }
    }

    public static class GainExperience {

        public static final long GAP_TICK = 20 * 60 * 60 * 2; // 2hour
        public static final Map<SkillType, Long> EXPERIENCE_RATES = Map.of(
                SkillType.PHYSIQUE, 15L,
                SkillType.STRENGTH, 15L,
                SkillType.LIFE_INTENSITY, 5L);
    } // 定时免费的升级，配置每多少tick给各个技能加多少经验
}

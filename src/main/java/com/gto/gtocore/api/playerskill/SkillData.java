package com.gto.gtocore.api.playerskill;

import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.network.chat.Component;

import lombok.Builder;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class SkillData {

    private SkillData() {}

    public enum SkillType {

        LIFE_INTENSITY(SkillTypeBuilder.builder()
                .NameTranslateKey("gtocore.player_exp_status.body_name")
                .LevelStepPerVoltage(3)
                .chineseName("生命强度")
                .englishName("Life Intensity")
                .nextLevelExperienceFormula(level -> (int) (100 * Math.pow(1.5, level.getLevel())))
                .experienceLevelGetter(PlayerData::getBodyExperienceLevel)
                .upgradePackageBonusFormula((tierGap, experienceForNextLevel) -> (int) (experienceForNextLevel * Math.pow(2, tierGap) * ((double) 1 / 16)))
                .build()),

        PHYSIQUE(SkillTypeBuilder.builder()
                .NameTranslateKey("gtocore.player_exp_status.health_name")
                .LevelStepPerVoltage(2)
                .chineseName("体格")
                .englishName("Physique")
                .nextLevelExperienceFormula(level -> (int) (100 * Math.pow(1.5, level.getLevel())))
                .experienceLevelGetter(PlayerData::getHealthExperienceLevel)
                .upgradePackageBonusFormula((tierGap, experienceForNextLevel) -> (int) (experienceForNextLevel * Math.pow(2, tierGap) * ((double) 1 / 16)))
                .build()),

        STRENGTH(SkillTypeBuilder.builder()
                .NameTranslateKey("gtocore.player_exp_status.attack_name")
                .LevelStepPerVoltage(3)
                .chineseName("力量")
                .englishName("Strength")
                .nextLevelExperienceFormula(level -> (int) (100 * Math.pow(1.5, level.getLevel())))
                .experienceLevelGetter(PlayerData::getAttackExperienceLevel)
                .upgradePackageBonusFormula((tierGap, experienceForNextLevel) -> (int) (experienceForNextLevel * Math.pow(2, tierGap) * ((double) 1 / 16)))
                .build());

        public final String NameTranslateKey;
        public final int LevelStepPerVoltage;
        public final String chineseName;
        public final String englishName;
        public final Function<BasicExperienceLevel, Integer> nextLevelExperienceFormula;
        public final Function<PlayerData, BasicExperienceLevel> experienceLevelGetter;
        public final BiFunction<Integer, Integer, Integer> upgradePackageBonusFormula;

        SkillType(SkillTypeBuilder builder) {
            this.NameTranslateKey = builder.NameTranslateKey;
            this.LevelStepPerVoltage = builder.LevelStepPerVoltage;
            this.chineseName = builder.chineseName;
            this.englishName = builder.englishName;
            this.nextLevelExperienceFormula = builder.nextLevelExperienceFormula;
            this.experienceLevelGetter = builder.experienceLevelGetter;
            this.upgradePackageBonusFormula = builder.upgradePackageBonusFormula;
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
            private int LevelStepPerVoltage;
            private String chineseName;
            private String englishName;
            private Function<BasicExperienceLevel, Integer> nextLevelExperienceFormula;
            private Function<PlayerData, BasicExperienceLevel> experienceLevelGetter;
            public BiFunction<Integer, Integer, Integer> upgradePackageBonusFormula;
        }
    }

    public static class Formula {

        public static final BiFunction<Integer, Integer, Integer> upgradePackageBonus = (tierGap, experienceForNextLevel) -> (int) (experienceForNextLevel * Math.pow(2, tierGap) * ((double) 1 / 16));
        // 每个升级包，都可以获得 到下一级的经验* 2^电压差 * (1/16) 的经验
    }

    public static class GainExperience {

        public static final int GAP_TICK = 20 * 60 * 60 * 2; // 2hour
        public static final Map<SkillType, Integer> EXPERIENCE_RATES = Map.of(
                SkillType.PHYSIQUE, 15,
                SkillType.STRENGTH, 15,
                SkillType.LIFE_INTENSITY, 5);
    } // 定时免费的升级，配置每多少tick给各个技能加多少经验
}

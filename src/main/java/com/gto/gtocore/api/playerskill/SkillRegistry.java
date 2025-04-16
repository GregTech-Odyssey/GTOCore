package com.gto.gtocore.api.playerskill;

import com.gto.gtocore.api.playerskill.data.PlayerData;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SkillRegistry {

    private static final ConcurrentHashMap<String, SkillType> SKILL_TYPES = new ConcurrentHashMap<>();

    // 预定义技能类型常量，方便引用
    public static final SkillType LIFE_INTENSITY;
    public static final SkillType PHYSIQUE;
    public static final SkillType STRENGTH;

    static {
        // 初始化预定义技能类型
        LIFE_INTENSITY = register(
                SkillType.builder()
                        .id("life_intensity")
                        .nameTranslateKey("gtocore.player_exp_status.body_name")
                        .levelStepPerVoltage(3)
                        .chineseName("生命强度")
                        .englishName("Life Intensity")
                        .nextLevelExperienceFormula(level -> (long) (100 * Math.pow(2, level.getLevel())))
                        .experienceLevelGetter(PlayerData::getLifeIntensityExperienceLevel)
                        .upgradePackageBonus((tierGap, experienceForNextLevel) -> (long) (experienceForNextLevel * Math.pow(2, tierGap) * ((double) 1 / 4)))
                        .nbtKey("bodyExperience")
                        .build());

        PHYSIQUE = register(
                SkillType.builder()
                        .id("physique")
                        .nameTranslateKey("gtocore.player_exp_status.health_name")
                        .levelStepPerVoltage(5)
                        .chineseName("体格")
                        .englishName("Physique")
                        .nextLevelExperienceFormula(level -> (long) (100 * Math.pow(1.5, level.getLevel())))
                        .experienceLevelGetter(PlayerData::getPhysiqueExperienceLevel)
                        .upgradePackageBonus((tierGap, experienceForNextLevel) -> (long) (experienceForNextLevel * Math.pow(2, tierGap) * ((double) 1 / 4)))
                        .nbtKey("healthExperienceLevel")
                        .build());

        STRENGTH = register(
                SkillType.builder()
                        .id("strength")
                        .nameTranslateKey("gtocore.player_exp_status.attack_name")
                        .levelStepPerVoltage(5)
                        .chineseName("力量")
                        .englishName("Strength")
                        .nextLevelExperienceFormula(level -> (long) (100 * Math.pow(1.5, level.getLevel())))
                        .experienceLevelGetter(PlayerData::getStrengthExperienceLevel)
                        .upgradePackageBonus((tierGap, experienceForNextLevel) -> (long) (experienceForNextLevel * Math.pow(2, tierGap) * ((double) 1 / 4)))
                        .nbtKey("attackExperienceLevel")
                        .build());
    }

    /**
     * 注册一个新的技能类型
     * 
     * @param skillType 要注册的技能类型
     * @return 已注册的技能类型
     */
    public static SkillType register(SkillType skillType) {
        SKILL_TYPES.put(skillType.getId(), skillType);
        return skillType;
    }

    /**
     * 根据ID获取技能类型
     * 
     * @param id 技能类型的ID
     * @return 包含技能类型的Optional
     */
    public static Optional<SkillType> getById(String id) {
        return Optional.ofNullable(SKILL_TYPES.get(id.toLowerCase()));
    }

    /**
     * 获取所有注册的技能类型
     * 
     * @return 不可修改的技能类型集合
     */
    public static Collection<SkillType> getAll() {
        return Collections.unmodifiableCollection(SKILL_TYPES.values());
    }
}

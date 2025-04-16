package com.gto.gtocore.api.playerskill;

import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 技能注册表类，负责管理所有可用的技能类型
 * <p>
 * 该类提供了注册、获取和管理游戏中所有技能类型的功能。它使用线程安全的ConcurrentHashMap
 * 存储所有注册的技能类型，并预定义了几种基础技能（生命强度、体格、力量和平衡难度）。
 * <p>
 * 技能系统是游戏中角色成长和进步的核心机制，通过这些技能玩家可以提升其游戏中的各种属性。
 */
public class SkillRegistry {

    /**
     * 存储所有注册的技能类型的线程安全哈希表
     * 键为技能类型ID（小写），值为对应的技能类型对象
     */
    private static final ConcurrentHashMap<String, SkillType> SKILL_TYPES = new ConcurrentHashMap<>();

    /**
     * 预定义技能类型常量，方便引用
     */
    public static final SkillType LIFE_INTENSITY; // 生命强度技能
    public static final SkillType PHYSIQUE;       // 体格技能
    public static final SkillType STRENGTH;       // 力量技能
    public static final SkillType BONUS;          // 平衡难度技能

    static {
        // 初始化预定义技能类型

        /**
         * 生命强度技能
         * - 该技能影响玩家的生命值相关属性
         * - 每3级提升一次电压等级
         * - 经验需求随级别呈指数增长(100 * 2^level)
         */
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

        /**
         * 体格技能
         * - 该技能提升玩家的护甲值
         * - 每5级提升一次电压等级
         * - 经验需求随级别呈1.5次方增长(100 * 1.5^level)
         * - 每级提供1点护甲值加成
         */
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
                        .attributeRecord(new BasicExperienceLevel.ATTRIBUTE_RECORD(Attributes.ARMOR, BasicExperienceLevel::getLevel))
                        .build());

        /**
         * 力量技能
         * - 该技能提升玩家的攻击伤害
         * - 每5级提升一次电压等级
         * - 经验需求随级别呈1.5次方增长(100 * 1.5^level)
         * - 每级提供2点攻击伤害加成
         */
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
                        .attributeRecord(new BasicExperienceLevel.ATTRIBUTE_RECORD(Attributes.ATTACK_DAMAGE, (expLevel) -> expLevel.getLevel() << 1))
                        .build());

        /**
         * 平衡难度技能
         * - 该技能用于游戏平衡
         * - 不可升级(levelStepPerVoltage = 0)
         * - 提供基础生命值(20)和攻击伤害(3)
         * - 不可见(isVisible = false)
         */
        BONUS = register(
                SkillType.builder()
                        .id("bonus")
                        .nameTranslateKey("gtocore.player_exp_status.bonus_name")
                        .levelStepPerVoltage(0) // 不可升级
                        .chineseName("平衡难度")
                        .englishName("Bonus")
                        .nextLevelExperienceFormula(level -> 1L)
                        .experienceLevelGetter(PlayerData::getBonusExperienceLevel)
                        .isVisible(false) // 不可见
                        .nbtKey("bonusExperienceLevel")
                        .attributeRecord(new BasicExperienceLevel.ATTRIBUTE_RECORD(Attributes.MAX_HEALTH, (expLevel) -> 20L))
                        .attributeRecord(new BasicExperienceLevel.ATTRIBUTE_RECORD(Attributes.ATTACK_DAMAGE, (expLevel) -> 3L))
                        .build());
    }

    /**
     * 注册一个新的技能类型
     * <p>
     * 将提供的技能类型添加到技能注册表中，以便后续可以通过ID检索
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
     * <p>
     * 查找并返回与给定ID匹配的技能类型。如果没有找到匹配的技能类型，则返回空Optional
     * 
     * @param id 技能类型的ID（不区分大小写，将转换为小写进行匹配）
     * @return 包含技能类型的Optional，如果未找到则为空
     */
    public static Optional<SkillType> getById(String id) {
        return Optional.ofNullable(SKILL_TYPES.get(id.toLowerCase()));
    }

    /**
     * 获取所有注册的技能类型
     * <p>
     * 返回一个不可修改的集合，包含所有当前注册的技能类型
     * 
     * @return 不可修改的技能类型集合
     */
    public static Collection<SkillType> getAll() {
        return Collections.unmodifiableCollection(SKILL_TYPES.values());
    }
}

package com.gto.gtocore.api.playerskill;

import com.gto.gtocore.api.playerskill.data.AttributeRecord;
import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.eventhandler.normal.ExperienceAddedSendMessageHandler;
import com.gto.gtocore.api.playerskill.eventhandler.normal.LevelAddedSendMessageHandler;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 技能注册表类，负责管理所有可用的技能类型
 * <p>
 * 该类提供了注册、获取和管理游戏中所有技能类型的功能。它使用线程安全的ConcurrentHashMap
 * 存储所有注册的技能类型，并预定义了几种基础技能（生命强度、体格、力量和平衡难度）。
 * <p>
 * 技能系统是游戏中角色成长和进步的核心机制，通过这些技能玩家可以提升其游戏中的各种属性。
 */
public final class SkillRegistry {

    private SkillRegistry() {}

    private static final ConcurrentHashMap<String, SkillType> SKILL_TYPES = new ConcurrentHashMap<>();

    public static final SkillType LIFE_INTENSITY; // 生命强度技能
    public static final SkillType PHYSIQUE;       // 体格技能
    public static final SkillType STRENGTH;       // 力量技能

    public static final UUID PHYSIQUE_UUID = UUID.fromString("8d5764a7-223a-475d-aa3d-5a952a46435a");
    public static final UUID STRENGTH_UUID = UUID.fromString("30dbe619-d089-4f4d-b804-69823395d8ba");

    public static final Set<UUID> SKILL_UUID = Set.of(PHYSIQUE_UUID, STRENGTH_UUID);

    static {
        /*
         * 生命强度技能
         * - 该技能影响玩家的生命值相关属性
         * - 每3级提升一次电压等级
         * - 经验需求随级别呈指数增长(100 * 2^level)
         */
        LIFE_INTENSITY = register(SkillType.builder()
                .id("life_intensity") // 必须 id
                .nameTranslateKey("gtocore.player_exp_status.body_name") // 必须
                .levelStepPerVoltage(3) // 必须,若为0则无法升级
                .chineseName("生命强度") // 必须
                .englishName("Life Intensity") // 必须
                .nextLevelExperienceFormula(level -> (long) (100 * Math.pow(2, level.getLevel()))) // 必须,如果无法升级，则返回任意数字都行
                .experienceLevelGetter(PlayerData::getLifeIntensityExperienceLevel) // 必须
                .upgradePackageBonus((tierGap, experienceForNextLevel) -> (long) (experienceForNextLevel * Math.pow(2, tierGap) * ((double) 1 / 4))) // 选择，如果不填则不生成经验包，若生成，返回值为每个经验包的经验，tier为(经验包电压-玩家等级所处电压)
                .nbtKey("bodyExperience") // 必须
                .SkillEventHandler(new ExperienceAddedSendMessageHandler())
                .SkillEventHandler(new LevelAddedSendMessageHandler())
                .build());

        /*
         * 体格技能
         * - 该技能提升玩家的护甲值
         * - 每5级提升一次电压等级
         * - 经验需求随级别呈1.5次方增长(100 * 1.5^level)
         * - 每级提供0.5点护甲值加成
         */
        PHYSIQUE = register(SkillType.builder()
                .id("physique")
                .nameTranslateKey("gtocore.player_exp_status.health_name")
                .levelStepPerVoltage(5)
                .chineseName("体格")
                .englishName("Physique")
                .nextLevelExperienceFormula(level -> (long) (100 * Math.pow(1.5, level.getLevel())))
                .experienceLevelGetter(PlayerData::getPhysiqueExperienceLevel)
                .upgradePackageBonus((tierGap, experienceForNextLevel) -> (long) (experienceForNextLevel * Math.pow(2, tierGap) * ((double) 1 / 4)))
                .nbtKey("healthExperienceLevel")
                .attributeRecord(new AttributeRecord(PHYSIQUE_UUID, Attributes.ARMOR, AttributeModifier.Operation.ADDITION, (expLevel) -> (double) expLevel.getLevel() / 2))
                .SkillEventHandler(new ExperienceAddedSendMessageHandler())
                .SkillEventHandler(new LevelAddedSendMessageHandler())
                .build());

        /*
         * 力量技能
         * - 该技能提升玩家的攻击伤害
         * - 每5级提升一次电压等级
         * - 经验需求随级别呈1.5次方增长(100 * 1.5^level)
         * - 每级提供0.5点攻击伤害加成
         */
        STRENGTH = register(SkillType.builder()
                .id("strength")
                .nameTranslateKey("gtocore.player_exp_status.attack_name")
                .levelStepPerVoltage(5)
                .chineseName("力量")
                .englishName("Strength")
                .nextLevelExperienceFormula(level -> (long) (100 * Math.pow(1.5, level.getLevel())))
                .experienceLevelGetter(PlayerData::getStrengthExperienceLevel)
                .upgradePackageBonus((tierGap, experienceForNextLevel) -> (long) (experienceForNextLevel * Math.pow(2, tierGap) * ((double) 1 / 4)))
                .nbtKey("attackExperienceLevel")
                .attributeRecord(new AttributeRecord(STRENGTH_UUID, Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION, (expLevel) -> (double) (expLevel.getLevel() / 2)))
                .SkillEventHandler(new ExperienceAddedSendMessageHandler())
                .SkillEventHandler(new LevelAddedSendMessageHandler())
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
    private static SkillType register(SkillType skillType) {
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
        return SKILL_TYPES.values();
    }
}

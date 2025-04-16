package com.gto.gtocore.api.playerskill;

import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.network.chat.Component;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.*;

/**
 * 技能类型定义类
 * 用于定义游戏中各种玩家技能的基本属性和行为
 */
@Builder
@Getter(AccessLevel.PUBLIC)
public class SkillType {

    /**
     * 技能的唯一标识符
     */
    @NotNull
    private final String id; // 唯一标识符

    /**
     * 用于翻译技能名称的本地化键
     */
    @NotNull
    private final String nameTranslateKey;

    /**
     * 每个电压等级对应的技能等级数
     * 例如：值为5意味着每5级技能提升1个电压等级
     */
    @NotNull
    private final Integer levelStepPerVoltage;

    /**
     * 技能的中文名称
     */
    @NotNull
    private final String chineseName;

    /**
     * 技能的英文名称
     */
    @NotNull
    private final String englishName;

    /**
     * 用于在NBT数据中存储该技能信息的键名
     * 这个键名一旦确定就不能更改，否则会导致数据丢失
     */
    @NotNull
    private final String nbtKey; // 存储数据，不能改

    /**
     * 计算下一级所需经验值的公式
     * 接收当前经验等级对象作为参数，返回需要的经验值数量
     */
    @NotNull
    private final ToLongFunction<BasicExperienceLevel> nextLevelExperienceFormula;

    /**
     * 从玩家数据中获取经验等级对象的函数
     */
    @NotNull
    private final Function<PlayerData, BasicExperienceLevel> experienceLevelGetter;

    /**
     * 是否生成升级包的标志
     * 通过Builder的upgradePackageBonus方法自动设置为true
     */
    @Builder.ObtainVia(method = "originGenerateUpgradePackage")
    public Boolean generateUpgradePackage; // 是否生成升级包

    /**
     * 计算升级包奖励的公式
     * 接收两个参数(tierGap, experienceForNextLevel)并返回一个长整型值
     *
     */
    private ToLongBiFunction<Long, Long> upgradePackageBonusFormula;

    /**
     * 该技能提供的属性加成记录列表
     */
    @Getter
    @NotNull
    @Singular
    private List<BasicExperienceLevel.ATTRIBUTE_RECORD> attributeRecords; // 属性加成

    /**
     * 技能是否可见的标志
     * 默认为true，表示技能在UI中可见
     */
    @NotNull
    @Builder.Default
    public final Boolean isVisible = true; // 是否可见

    /**
     * 获取generateUpgradePackage的默认值
     * 
     * @return 默认返回false，表示不生成升级包
     */
    public static boolean originGenerateUpgradePackage() {
        return false;
    }

    /**
     * SkillType的Builder内部类，提供流式API构建SkillType实例
     */
    public static class SkillTypeBuilder {

        /**
         * 设置升级包奖励计算公式并自动启用升级包生成
         * 
         * @param upgradePackageBonusFormula 升级包奖励计算公式
         * @return 当前Builder实例，支持链式调用
         */
        public SkillTypeBuilder upgradePackageBonus(ToLongBiFunction<Long, Long> upgradePackageBonusFormula) {
            this.upgradePackageBonusFormula = upgradePackageBonusFormula;
            this.generateUpgradePackage = true;
            return this;
        }
    }

    /**
     * 比较两个SkillType是否相等
     * 只比较id字段
     * 
     * @param other 要比较的另一个SkillType对象
     * @return 如果id相同则返回true
     */
    public boolean equals(SkillType other) {
        return this.id.equals(other.id);
    }

    /**
     * 获取本地化后的技能名称
     * 
     * @return 翻译后的技能名称字符串
     */
    public String getName() {
        return Component.translatable(this.nameTranslateKey).getString();
    }

    /**
     * 获取指定玩家的此技能经验等级对象
     * 
     * @param playerData 玩家数据对象
     * @return 与此技能关联的经验等级对象
     */
    public BasicExperienceLevel getExperienceLevel(PlayerData playerData) {
        return experienceLevelGetter.apply(playerData);
    }
}

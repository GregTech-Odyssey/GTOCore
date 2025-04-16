package com.gto.gtocore.api.playerskill.experiencelevel;

import com.gto.gtocore.api.playerskill.SkillType;
import com.gto.gtocore.api.playerskill.data.ExperienceSystemManager;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * 经验等级系统的基类
 * 定义了经验等级系统的基本结构和共通功能
 */
@Getter
public abstract class BasicExperienceLevel {

    /**
     * 当前等级值
     */
    protected long level;

    /**
     * 累积的经验值
     */
    protected long experience;

    /**
     * 关联的技能类型
     */
    public SkillType skillType;

    /**
     * 属性记录类，用于定义技能提供的属性加成
     * 包含属性类型和基于经验等级的属性值计算器
     */
    public record ATTRIBUTE_RECORD(Attribute attribute,
                                   Function<BasicExperienceLevel, Long> valueCalculator) {

        /**
         * 创建Minecraft属性修饰符
         * 
         * @param expLevel 经验等级对象
         * @return 创建的属性修饰符，用于应用到实体上
         */
        public AttributeModifier getModifier(BasicExperienceLevel expLevel) {
            return new AttributeModifier(UUID.randomUUID(), "gtocore.exp." + expLevel.skillType.getEnglishName().toLowerCase() + "_" + attribute.getDescriptionId().toLowerCase() + "_bonus", valueCalculator.apply(expLevel), AttributeModifier.Operation.ADDITION);
        }
    }

    /**
     * 构造函数
     * 
     * @param skillType 关联的技能类型
     */
    protected BasicExperienceLevel(SkillType skillType) {
        this.level = 0;
        this.experience = 0;
        this.skillType = skillType;
    }

    /**
     * 获取名称显示颜色
     * 
     * @return 默认返回金色
     */
    public ChatFormatting getNameColor() {
        return ChatFormatting.GOLD;
    }

    /**
     * 计算当前电压等级
     * 电压等级=(当前等级-1)/每电压级别的等级步长
     * 
     * @return 当前电压等级
     */
    public long getVoltage() {
        return skillType.getLevelStepPerVoltage() == 0 ? 0 : (level - 1) / skillType.getLevelStepPerVoltage();
    }

    /**
     * 获取最大电压等级
     * 由子类实现
     * 
     * @return 最大电压等级
     */
    public abstract long getMaxVoltage();

    /**
     * 将数据保存到NBT标签
     * 
     * @param nbt 目标NBT标签
     */
    public void saveData(CompoundTag nbt) {
        nbt.putLong("level", level);
        nbt.putLong("experience", experience);
    }

    /**
     * 从NBT标签加载数据
     * 
     * @param nbt 源NBT标签
     */
    public void loadData(CompoundTag nbt) {
        this.level = nbt.getLong("level");
        this.experience = nbt.getLong("experience");
    }

    /**
     * 获取此技能提供的所有属性修饰符
     * 
     * @return 属性记录列表
     */
    public List<ATTRIBUTE_RECORD> getAttributeModifiers() {
        return this.skillType.getAttributeRecords();
    }

    /**
     * 获取最大等级限制
     * 由子类实现
     * 
     * @return 最大等级值
     */
    public abstract long getMaxLevel();

    /**
     * 添加经验值并自动升级
     * 由子类实现具体升级逻辑
     * 
     * @param amount 要添加的经验值数量
     */
    public abstract void addExperience(long amount);

    /**
     * 设置当前等级
     * 
     * @param amount 新的等级值
     */
    public void setLevel(long amount) {
        this.level = amount;
        ExperienceSystemManager.INSTANCE.saveAll();
    }

    /**
     * 设置当前经验值
     * 
     * @param amount 新的经验值
     */
    public void setExperience(long amount) {
        this.experience = amount;
        ExperienceSystemManager.INSTANCE.saveAll();
    }

    /**
     * 获取技能名称
     * 
     * @return 本地化后的技能名称
     */
    public String getName() {
        return skillType.getName();
    }

    /**
     * 计算升级到下一级所需的经验值
     * 
     * @return 所需经验值数量
     */
    public long getExperienceForNextLevel() {
        return skillType.getNextLevelExperienceFormula().applyAsLong(this);
    }
}

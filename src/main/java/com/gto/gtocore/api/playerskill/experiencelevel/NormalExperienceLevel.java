package com.gto.gtocore.api.playerskill.experiencelevel;

import com.gto.gtocore.api.playerskill.SkillType;
import com.gto.gtocore.api.playerskill.experiencelevel.special.LifeIntensityExperienceLevel;

/**
 * 普通经验等级系统类
 * 继承自BasicExperienceLevel，代表常规技能的经验等级系统
 * 普通技能等级受到生命强度等级的限制
 */
public abstract class NormalExperienceLevel extends BasicExperienceLevel {

    /**
     * 关联的生命强度经验等级对象
     * 用于限制当前技能的最大等级
     */
    protected LifeIntensityExperienceLevel lifeIntensityExperienceLevel;

    /**
     * 实现经验值添加和自动升级的逻辑
     * 当累积的经验值达到升级所需经验时自动升级
     * 
     * @param amount 要添加的经验值数量
     */
    @Override
    public void addExperience(long amount) {
        if (this.skillType.getLevelStepPerVoltage() == 0) {
            return;
        } // 不可升级
        experience += amount;
        while (experience >= getExperienceForNextLevel() && level < getMaxLevel()) {
            experience -= getExperienceForNextLevel();
            level++;
        }
    }

    /**
     * 构造函数
     * 
     * @param _lifeIntensityExperienceLevel 关联的生命强度经验等级对象
     * @param skillType                     关联的技能类型
     */
    protected NormalExperienceLevel(LifeIntensityExperienceLevel _lifeIntensityExperienceLevel, SkillType skillType) {
        super(skillType);
        this.lifeIntensityExperienceLevel = _lifeIntensityExperienceLevel;
    }

    /**
     * 获取最大电压等级
     * 普通技能的最大电压等级受到生命强度电压等级的限制
     * 
     * @return 当前可达到的最大电压等级
     */
    public long getMaxVoltage() {
        return lifeIntensityExperienceLevel.getVoltage();
    }

    /**
     * 获取最大等级限制
     * 最大等级=(生命强度电压等级+1)*每电压级别的等级步长
     * 
     * @return 最大等级值
     */
    @Override
    public long getMaxLevel() {
        return (lifeIntensityExperienceLevel.getVoltage() + 1) * skillType.getLevelStepPerVoltage();
    }
}

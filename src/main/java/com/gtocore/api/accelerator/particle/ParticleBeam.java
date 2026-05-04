package com.gtocore.api.accelerator.particle;

import net.minecraft.world.phys.Vec3;

import lombok.Getter;

/**
 * 粒子束流在运行时的存在形式，包含粒子束流的类型，能量，聚焦，数量，位置等参数
 * 粒子束流在机器gui中显示其基本数据，如：粒子类型，能量，聚焦，数量等
 * <p>
 * 动能决定机器能否运行配方或运行何种配方
 * 聚焦度决定配方的粒子撞击成功率和粒子额外输出倍率
 * 数量决定配方的运行速度
 * <p>
 * 粒子束流的数量对应流体单位mB，即1数量的束流相当于144mB对应的流体
 * 粒子束流的能量对应能量单位EU，即1eV = 1EU，每提高1eV消耗1EU
 * <p>
 * 粒子束流的类型对应粒子定义{@link ParticleDefinition}，包含粒子束流的基本属性，如质量，电荷，粒子宽度等
 * 粒子束流的位置对应粒子束流在粒子路径上的位置，由粒子路径管理器{@link com.gtocore.api.accelerator.pathing.ParticlePathingManager}负责计算和更新
 * 粒子束流的速度和加速度由粒子路径管理器根据粒子束流的参数和机器组件的布局计算得出，并在粒子束流运动过程中不断更新
 */
@Getter
public class ParticleBeam {

    private final ParticleDefinition definition;
    private double energy;
    private double focus;
    private double quantity;
    private Vec3 position;
    private Vec3 velocity;

    public ParticleBeam(ParticleDefinition definition, double energy, double focus, double quantity, Vec3 position) {
        this.definition = definition;
        this.energy = energy;
        this.focus = focus;
        this.quantity = quantity;
        this.position = position;
        this.velocity = Vec3.ZERO; // 初始速度为零，后续由粒子路径管理器计算
    }
}

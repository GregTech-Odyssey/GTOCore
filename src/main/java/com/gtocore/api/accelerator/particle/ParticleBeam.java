package com.gtocore.api.accelerator.particle;

import com.gtocore.api.accelerator.Particles;

import com.gregtechceu.gtceu.api.recipe.content.ContentInner;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
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
public class ParticleBeam extends ContentInner {

    public static final String PARTICLE_KEY = "particle";
    private final ParticleDefinition definition;
    private double energy;
    private double focus;
    private Vec3 position;
    private Vec3 velocity;

    public ParticleBeam(ParticleDefinition definition, double energy, double focus, long amount, Vec3 position) {
        this(definition, energy, focus, amount, position, Vec3.ZERO);
    }

    public ParticleBeam(ParticleDefinition definition, double energy, double focus, long amount, Vec3 position, Vec3 velocity) {
        this.definition = definition;
        this.energy = energy;
        this.focus = focus;
        this.amount = amount;
        this.position = position == null ? Vec3.ZERO : position;
        this.velocity = velocity == null ? Vec3.ZERO : velocity;
        this.amount = amount;
    }

    @Override
    public boolean isEmpty() {
        return definition == null || amount <= 0 || amount <= 0;
    }

    @Override
    public ParticleBeam copy() {
        return new ParticleBeam(definition, energy, focus, amount, position, velocity);
    }

    @Override
    public ParticleBeam copy(long amount) {
        return new ParticleBeam(definition, energy, focus, amount, position, velocity);
    }

    @Override
    public CompoundTag toNbt() {
        var tag = new CompoundTag();
        if (isEmpty()) {
            tag.putBoolean("empty", true);
            return tag;
        }
        tag.putString(PARTICLE_KEY, Particles.REGISTRY_KEY.getKey(definition).toString());
        tag.putDouble("energy", energy);
        tag.putDouble("focus", focus);
        tag.putLong("quantity", amount);
        tag.put("position", writeVec3(position));
        tag.put("velocity", writeVec3(velocity));
        return tag;
    }

    public static ParticleBeam fromNbt(CompoundTag tag) {
        if (tag.getBoolean("empty")) return empty();
        ParticleDefinition definition = Particles.EMPTY;
        if (tag.tags.get(PARTICLE_KEY) instanceof StringTag particleTag) {
            var key = ResourceLocation.tryParse(particleTag.getAsString());
            var value = key == null ? null : Particles.REGISTRY_KEY.get(key);
            if (value != null) definition = value;
        }
        return new ParticleBeam(
                definition,
                tag.getDouble("energy"),
                tag.getDouble("focus"),
                tag.getLong("quantity"),
                readVec3(tag.getCompound("position")),
                readVec3(tag.getCompound("velocity")));
    }

    public static ParticleBeam empty() {
        return new ParticleBeam(Particles.EMPTY, 0, 0, 0, Vec3.ZERO, Vec3.ZERO);
    }

    private static CompoundTag writeVec3(Vec3 vec3) {
        var tag = new CompoundTag();
        tag.putDouble("x", vec3.x);
        tag.putDouble("y", vec3.y);
        tag.putDouble("z", vec3.z);
        return tag;
    }

    private static Vec3 readVec3(CompoundTag tag) {
        return new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
    }
}

package com.gtocore.api.accelerator.particle;

import com.gtocore.api.accelerator.Particles;

import net.minecraft.world.phys.Vec3;

import com.gto.datasynclib.datasream.codec.DataCodec;
import com.gto.datasynclib.datasream.data.Data;
import com.gto.datasynclib.datasream.data.ListData;
import com.gto.datasynclib.datasream.data.NullData;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

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
public final class ParticleBeam {

    public static final DataCodec<Vec3> VEC3_DATA_CODEC = new DataCodec<>() {

        @Override
        public @NotNull Data encode(Vec3 obj) {
            return Data.valueOf(new double[] { obj.x, obj.y, obj.z });
        }

        @Override
        public Vec3 decode(@NotNull Data data, int dataVersion) {
            var array = data.getDoubleArray();
            return new Vec3(array[0], array[1], array[2]);
        }
    };

    public static final DataCodec<ParticleBeam> DATA_CODEC = new DataCodec<>() {

        @Override
        public ParticleBeam decode(@NotNull Data data, int dataVersion) {
            if (data.isNull()) return empty();
            var listData = data.asListData();
            var definition = listData.get(0, Particles.REGISTRY_KEY.dataCodec(), dataVersion);
            if (definition == null) {
                definition = Particles.EMPTY;
            }
            return new ParticleBeam(
                    definition,
                    listData.getDouble(1),
                    listData.getDouble(2),
                    listData.getLong(3),
                    listData.get(4, VEC3_DATA_CODEC, dataVersion),
                    listData.get(5, VEC3_DATA_CODEC, dataVersion));
        }

        @Override
        public @NotNull Data encode(ParticleBeam obj) {
            if (obj.isEmpty()) {
                return NullData.INSTANCE;
            }
            var listData = new ListData();
            listData.add(Particles.REGISTRY_KEY.dataCodec(), obj.definition);
            listData.addDouble(obj.energy);
            listData.addDouble(obj.focus);
            listData.addLong(obj.amount);
            listData.add(VEC3_DATA_CODEC, obj.position);
            listData.add(VEC3_DATA_CODEC, obj.velocity);
            return listData;
        }
    };

    private final ParticleDefinition definition;
    private long amount;
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
    }

    public boolean isEmpty() {
        return definition == null || amount <= 0;
    }

    public ParticleBeam copy() {
        return new ParticleBeam(definition, energy, focus, amount, position, velocity);
    }

    public ParticleBeam copy(long amount) {
        return new ParticleBeam(definition, energy, focus, amount, position, velocity);
    }

    public static ParticleBeam empty() {
        return new ParticleBeam(Particles.EMPTY, 0, 0, 0, Vec3.ZERO, Vec3.ZERO);
    }
}

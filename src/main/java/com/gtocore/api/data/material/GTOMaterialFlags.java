package com.gtocore.api.data.material;

import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;

public final class GTOMaterialFlags {

    // 基础工厂方法
    private static MaterialFlag createFlag(String name) {
        return new MaterialFlag.Builder(name).build();
    }

    // 需要依赖其他flags的工厂方法
    private static MaterialFlag createFlagWithDependencies(String name, MaterialFlag... requiredFlags) {
        return new MaterialFlag.Builder(name).requireFlags(requiredFlags).build();
    }

    public static final MaterialFlag GENERATE_SMALL_DUST = createFlag("generate_small_dust");

    public static final MaterialFlag GENERATE_TINY_DUST = createFlag("generate_tiny_dust");

    public static final MaterialFlag GENERATE_CATALYST = createFlag("generate_catalyst");

    public static final MaterialFlag GENERATE_NANITES = createFlag("generate_nanites");

    public static final MaterialFlag GENERATE_MILLED = createFlag("generate_milled");

    public static final MaterialFlag GENERATE_CURVED_PLATE = createFlag("generate_curved_plate");

    public static final MaterialFlag GENERATE_COMPONENT = createFlagWithDependencies(
            "generate_component",
            GENERATE_CURVED_PLATE,
            MaterialFlags.GENERATE_RING,
            MaterialFlags.GENERATE_ROUND
    );

    public static final MaterialFlag GENERATE_CERAMIC = createFlagWithDependencies(
            "generate_ceramic",
            MaterialFlags.FORCE_GENERATE_BLOCK
    );

    public static final MaterialFlag GENERATE_CRYSTAL_SEED = createFlag("generate_crystal_seed");

    public static final MaterialFlag GENERATE_ARTIFICIAL_GEM = createFlagWithDependencies(
            "generate_artificial_gem",
            GENERATE_CRYSTAL_SEED
    );

    public static final MaterialFlag GENERATE_COIN = createFlag("generate_coin");

    public static final MaterialFlag GENERATE_PARTICLE_SOURCE = createFlag("generate_particle_source");

    public static final MaterialFlag GENERATE_TARGET_BASE = createFlag("generate_target_base");

    public static final MaterialFlag GENERATE_BERYLLIUM_TARGET = createFlag("generate_beryllium_target");

    public static final MaterialFlag GENERATE_STAINLESS_STEEL_TARGET = createFlag("generate_stainless_steel_target");

    public static final MaterialFlag GENERATE_ZIRCONIUM_CARBIDE_TARGET = createFlag("generate_zirconium_carbide_target");

    public static final MaterialFlag GENERATE_BREEDER_ROD = createFlag("generate_breeder_rod");
}

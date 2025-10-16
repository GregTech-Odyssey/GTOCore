package com.gtocore.api.data.material;

import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;

public final class GTOMaterialFlags {

    public static final MaterialFlag GENERATE_SMALL_DUST = new MaterialFlag.Builder("generate_small_dust")
            .build();

    public static final MaterialFlag GENERATE_TINY_DUST = new MaterialFlag.Builder("generate_tiny_dust")
            .build();

    public static final MaterialFlag GENERATE_CATALYST = new MaterialFlag.Builder("generate_catalyst")
            .build();

    public static final MaterialFlag GENERATE_NANITES = new MaterialFlag.Builder("generate_nanites")
            .build();

    public static final MaterialFlag GENERATE_MILLED = new MaterialFlag.Builder("generate_milled")
            .build();

    public static final MaterialFlag GENERATE_CURVED_PLATE = new MaterialFlag.Builder("generate_curved_plate")
            .build();

    public static final MaterialFlag GENERATE_COMPONENT = new MaterialFlag.Builder("generate_component")
            .requireFlags(GENERATE_CURVED_PLATE, MaterialFlags.GENERATE_RING, MaterialFlags.GENERATE_ROUND)
            .build();

    public static final MaterialFlag GENERATE_CERAMIC = new MaterialFlag.Builder("generate_ceramic")
            .requireFlags(MaterialFlags.FORCE_GENERATE_BLOCK)
            .build();

    public static final MaterialFlag GENERATE_CRYSTAL_SEED = new MaterialFlag.Builder("generate_crystal_seed")
            .build();

    public static final MaterialFlag GENERATE_ARTIFICIAL_GEM = new MaterialFlag.Builder("generate_artificial_gem")
            .requireFlags(GENERATE_CRYSTAL_SEED)
            .build();

    public static final MaterialFlag GENERATE_COIN = new MaterialFlag.Builder("generate_coin")
            .build();

    public static final MaterialFlag GENERATE_PARTICLE_SOURCE = new MaterialFlag.Builder("generate_particle_source")
            .build();

    public static final MaterialFlag GENERATE_TARGET_BASE = new MaterialFlag.Builder("generate_target_base")
            .build();

    public static final MaterialFlag GENERATE_BERYLLIUM_TARGET = new MaterialFlag.Builder("generate_beryllium_target")
            .build();

    public static final MaterialFlag GENERATE_STAINLESS_STEEL_TARGET = new MaterialFlag.Builder("generate_stainless_steel_target")
            .build();

    public static final MaterialFlag GENERATE_ZIRCONIUM_CARBIDE_TARGET = new MaterialFlag.Builder("generate_zirconium_carbide_target")
            .build();

    public static final MaterialFlag GENERATE_BREEDER_ROD = new MaterialFlag.Builder("generate_breeder_rod")
            .build();

    public static final MaterialFlag GENERATE_MXene = new MaterialFlag.Builder("generate_mxene")
            .build();

    public static final MaterialFlag GENERATE_MEMBRANE_ELECTRODE = new MaterialFlag.Builder("generate_membrane_electrode")
            .build();

    public static final MaterialFlag CAN_BE_COOLED_DOWN_BY_BATHING = new MaterialFlag.Builder("can_be_cooled_down_by_bathing")
            .build();

    public static final MaterialFlag GENERATE_FIBER = new MaterialFlag.Builder("generate_carbon_fiber")
            .build();

    public static final MaterialFlag IS_CARBON_FIBER = new MaterialFlag.Builder("is_carbon_fiber").requireFlags(GENERATE_FIBER)
            .build();

    public static final MaterialFlag COMPOSITE_MATERIAL = new MaterialFlag.Builder("composite_material")
            .build();

    public static final MaterialFlag NEED_BLAST_IN_SPACE = new MaterialFlag.Builder("need_blast_in_space")
            .build();

    public static final MaterialFlag HAS_NANOSCALE_FORM = new MaterialFlag.Builder("need_blast_in_space")
            .build();
}

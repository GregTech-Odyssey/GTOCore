package com.gto.gtocore.api.data.chemical.material.info;

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

    public static final MaterialFlag GENERATE_MAGICCRYSTAL = new MaterialFlag.Builder("generate_magiccrystal")
            .build();

    public static final MaterialFlag GENERATE_COMPONENT = new MaterialFlag.Builder("generate_component")
            .requireFlags(GENERATE_CURVED_PLATE, MaterialFlags.GENERATE_RING)
            .build();

    public static final MaterialFlag GENERATE_CERAMIC = new MaterialFlag.Builder("GENERATE_CERAMIC")
            .requireFlags(MaterialFlags.FORCE_GENERATE_BLOCK)
            .build();
}

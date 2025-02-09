package com.gto.gtocore.common.block;

import com.gregtechceu.gtceu.common.data.GTBlocks;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.function.Supplier;

public final class BlockMap {

    public static final Int2ObjectOpenHashMap<Supplier<?>> SCMAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectOpenHashMap<Supplier<?>> SEPMMAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectOpenHashMap<Supplier<?>> CALMAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectOpenHashMap<Supplier<?>> GLASSMAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectOpenHashMap<Supplier<?>> MACHINECASINGMAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectOpenHashMap<Supplier<?>> GRAVITONFLOWMAP = new Int2ObjectOpenHashMap<>();

    public static final Int2ObjectOpenHashMap<Supplier<?>> MGMAP = new Int2ObjectOpenHashMap<>();

    public static void init() {
        BlockMap.GLASSMAP.put(2, GTBlocks.CASING_TEMPERED_GLASS);
    }
}

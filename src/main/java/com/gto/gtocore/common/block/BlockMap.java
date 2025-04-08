package com.gto.gtocore.common.block;

import com.gto.gtocore.common.data.GTOBlocks;

import com.gregtechceu.gtceu.common.data.GTBlocks;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.function.Supplier;

public interface BlockMap {

    Int2ObjectMap<Supplier<?>> SCMAP = new Int2ObjectOpenHashMap<>();

    Int2ObjectMap<Supplier<?>> SEPMMAP = new Int2ObjectOpenHashMap<>();

    Int2ObjectMap<Supplier<?>> CALMAP = new Int2ObjectOpenHashMap<>();

    Int2ObjectMap<Supplier<?>> GLASSMAP = new Int2ObjectOpenHashMap<>();

    Int2ObjectMap<Supplier<?>> MACHINECASINGMAP = new Int2ObjectOpenHashMap<>();

    Int2ObjectMap<Supplier<?>> INTEGRALFRAMEWORKMAP = new Int2ObjectOpenHashMap<>();

    Int2ObjectMap<Supplier<?>> GRAVITONFLOWMAP = new Int2ObjectOpenHashMap<>();

    Int2ObjectMap<Supplier<?>> COMPUTER_CASING_MAP = new Int2ObjectOpenHashMap<>();

    Int2ObjectMap<Supplier<?>> COMPUTER_HEAT_MAP = new Int2ObjectOpenHashMap<>();

    static void init() {
        GLASSMAP.put(2, GTBlocks.CASING_TEMPERED_GLASS);
        COMPUTER_CASING_MAP.put(1, GTBlocks.COMPUTER_CASING);
        COMPUTER_CASING_MAP.put(2, GTOBlocks.BIOCOMPUTER_CASING);
        COMPUTER_CASING_MAP.put(3, GTOBlocks.GRAVITON_COMPUTER_CASING);
        COMPUTER_HEAT_MAP.put(1, GTBlocks.COMPUTER_HEAT_VENT);
        COMPUTER_HEAT_MAP.put(2, GTOBlocks.PHASE_CHANGE_BIOCOMPUTER_COOLING_VENTS);
        COMPUTER_HEAT_MAP.put(3, GTOBlocks.ANTI_ENTROPY_COMPUTER_CONDENSATION_MATRIX);
    }
}

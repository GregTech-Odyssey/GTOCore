package com.gtocore.common.machine.multiblock;

import com.gtocore.api.pattern.GTOPredicates;

import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;

import net.minecraft.core.BlockPos;

import com.gto.datasynclib.datasream.DataComponentKey;
import com.gto.fastcollection.OpenCacheHashSet;

import java.util.Collections;
import java.util.Set;

public final class FluidRenderUtils {

    private static final String FLUID_BLOCK_OFFSETS_FIELD = "fluidBlockOffsets";

    private FluidRenderUtils() {}

    public static Set<BlockPos> loadFluidBlockOffsets(MultiblockControllerMachine machine) {
        return loadFluidBlockOffsets(machine, GTOPredicates.DataKeys.A);
    }

    public static void markFluidBlockOffsetsForSync(MultiblockControllerMachine machine) {
        machine.markFieldsForSync(FLUID_BLOCK_OFFSETS_FIELD);
    }

    public static Set<BlockPos> loadFluidBlockOffsets(MultiblockControllerMachine machine, DataComponentKey<Set<BlockPos>> key) {
        Set<BlockPos> fluidBlockOffsets = new OpenCacheHashSet<>();
        BlockPos origin = machine.getPos();
        for (BlockPos pos : machine.getMultiblockState().getMatchContext().getOrDefault(key, Collections.emptySet())) {
            if (pos != null) {
                fluidBlockOffsets.add(pos.subtract(origin));
            }
        }
        return fluidBlockOffsets;
    }
}

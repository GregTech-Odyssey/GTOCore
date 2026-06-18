package com.gtocore.common.machine.multiblock;

import com.gtocore.api.pattern.GTOPredicates;

import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;

import net.minecraft.core.BlockPos;

import com.gto.datasynclib.datasream.DataComponentKey;

import java.util.Collections;
import java.util.Set;

public final class FluidRenderUtils {

    private static final String FLUID_BLOCK_OFFSETS_FIELD = "fluidBlockOffsets";

    private FluidRenderUtils() {}

    public static void loadFluidBlockOffsets(MultiblockControllerMachine machine, Set<BlockPos> fluidBlockOffsets) {
        loadFluidBlockOffsets(machine, GTOPredicates.DataKeys.A, fluidBlockOffsets);
    }

    public static void loadFluidBlockOffsetsAndSync(MultiblockControllerMachine machine, Set<BlockPos> fluidBlockOffsets) {
        loadFluidBlockOffsets(machine, fluidBlockOffsets);
        machine.markFieldsForSync(FLUID_BLOCK_OFFSETS_FIELD);
    }

    public static void loadFluidBlockOffsets(MultiblockControllerMachine machine, DataComponentKey<Set<BlockPos>> key, Set<BlockPos> fluidBlockOffsets) {
        fluidBlockOffsets.clear();
        BlockPos origin = machine.getPos();
        for (BlockPos pos : machine.getMultiblockState().getMatchContext().getOrDefault(key, Collections.emptySet())) {
            if (pos != null) {
                fluidBlockOffsets.add(pos.subtract(origin));
            }
        }
    }

    public static void clearFluidBlockOffsetsAndSync(MultiblockControllerMachine machine, Set<BlockPos> fluidBlockOffsets) {
        fluidBlockOffsets.clear();
        machine.markFieldsForSync(FLUID_BLOCK_OFFSETS_FIELD);
    }
}

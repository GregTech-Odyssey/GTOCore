package com.gtocore.common.machine.multiblock;

import com.gtocore.api.pattern.GTOPredicates;

import com.gtolib.api.machine.feature.multiblock.IFluidRendererMachine;

import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;

import net.minecraft.core.BlockPos;

import com.gto.datasynclib.datasream.DataComponentKey;
import com.gto.fastcollection.OpenCacheHashSet;

import java.util.Collections;
import java.util.Set;

public final class FluidRenderUtils {

    private static final String FLUID_BLOCK_OFFSETS_FIELD = "fluidBlockOffsets";

    private FluidRenderUtils() {}

    public static void loadFluidBlockOffsets(IFluidRendererMachine machine) {
        clearFluidBlockOffsets(machine);
        loadFluidBlockOffsets(machine.getFluidBlockOffsets(), (MultiblockControllerMachine) machine, GTOPredicates.DataKeys.A);
    }

    public static void clearFluidBlockOffsets(IFluidRendererMachine machine) {
        machine.getFluidBlockOffsets().clear();
        ((MultiblockControllerMachine) machine).markFieldsForSync(FLUID_BLOCK_OFFSETS_FIELD);
    }

    public static Set<BlockPos> emptyFluidBlockOffsets() {
        return new OpenCacheHashSet<>();
    }

    private static void loadFluidBlockOffsets(Set<BlockPos> fluidBlockOffsets, MultiblockControllerMachine machine, DataComponentKey<Set<BlockPos>> key) {
        BlockPos origin = machine.getPos();
        for (BlockPos pos : machine.getMultiblockState().getMatchContext().getOrDefault(key, Collections.emptySet())) {
            if (pos != null) {
                fluidBlockOffsets.add(pos.subtract(origin));
            }
        }
    }
}

package com.gtocore.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Fluid;

import com.google.common.collect.Multimap;

public interface IMultiFluidRendererMachine {

    Multimap<Fluid, BlockPos> getFluidBlockOffsets();
}

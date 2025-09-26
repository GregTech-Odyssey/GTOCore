package com.gtocore.common.machine.multiblock.part.maintenance;

import com.gtocore.api.machine.part.ITempPartMachine;
import com.gtocore.api.machine.part.IVacuumPartMachine;

import com.gtolib.api.machine.feature.IGravityPartMachine;

import net.minecraft.core.BlockPos;

public interface IModularMaintenance extends IVacuumPartMachine, IGravityPartMachine, ITempPartMachine {

    @Override
    default void doExplosion(BlockPos pos, float explosionPower) {}
}

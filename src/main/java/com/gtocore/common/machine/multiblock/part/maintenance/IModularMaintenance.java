package com.gtocore.common.machine.multiblock.part.maintenance;

import com.gtocore.api.machine.ITempPartMachine;
import com.gtocore.api.machine.IVacuumPartMachine;

import com.gtolib.api.machine.feature.IGravityPartMachine;

import net.minecraft.core.BlockPos;

public interface IModularMaintenance extends IVacuumPartMachine, IGravityPartMachine, ITempPartMachine {

    @Override
    default void doExplosion(BlockPos pos, float explosionPower) {}
}

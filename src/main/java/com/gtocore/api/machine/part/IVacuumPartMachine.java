package com.gtocore.api.machine.part;

import com.gtolib.api.machine.feature.IVacuumMachine;

import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public interface IVacuumPartMachine extends IVacuumMachine {

    default int getVacuumTier() {
        Level level = self().getLevel();
        if (level == null) return 0;
        for (Direction side : GTUtil.DIRECTIONS) {
            if (side.getAxis() != Direction.Axis.Y && self().getNeighborMachine(side) instanceof IVacuumMachine recipeLogicMachine && !(recipeLogicMachine instanceof IVacuumPartMachine)) {
                return recipeLogicMachine.getVacuumTier();
            }
        }
        return 0;
    }
}

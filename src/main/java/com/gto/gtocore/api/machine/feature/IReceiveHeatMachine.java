package com.gto.gtocore.api.machine.feature;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IReceiveHeatMachine {

    BlockPos getPos();

    Level getLevel();

    default boolean heating(int value) {
        BlockPos pos = getPos();
        BlockPos[] coordinates = { pos.offset(1, 0, 0),
                pos.offset(-1, 0, 0),
                pos.offset(0, 1, 0),
                pos.offset(0, -1, 0),
                pos.offset(0, 0, 1),
                pos.offset(0, 0, -1) };
        for (BlockPos blockPos : coordinates) {
            if (MetaMachine.getMachine(getLevel(), blockPos) instanceof IHeaterMachine heaterMachine) {
                if (heaterMachine.getHeatCapacity() == 0) continue;
                int demand = Math.min(value, heaterMachine.getHeatCapacity());
                if (demand > 0) {
                    if (heaterMachine.reduceTemperature(demand)) {
                        value -= demand;
                    }
                } else {
                    break;
                }
            }
        }
        return value <= 0;
    }
}

package com.gto.gtocore.api.machine.feature;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRedstoneSignalMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IReceiveHeatMachine extends ITemperatureMachine {

    BlockPos getPos();

    Level getLevel();

    default void tickUpdate() {
        if (self().getOffsetTimer() % 20 != 0) return;
        raiseTemperature(getHeatCapacity());
        if (this instanceof IRedstoneSignalMachine redstoneSignalMachine) redstoneSignalMachine.updateSignal();
        if (this instanceof IRecipeLogicMachine machine && machine.getRecipeLogic().isWorking()) return;
        reduceTemperature(1);
        if (getTemperature() > 293 && this instanceof IRecipeLogicMachine machine) machine.getRecipeLogic().updateTickSubscription();
    }

    @Override
    default int reduceTemperature(int value) {
        int change = Math.min(getTemperature() - 293, value);
        if (change <= 0) {
            setTemperature(293);
            return 0;
        }
        setTemperature(getTemperature() - change);
        return change;
    }

    @Override
    default void raiseTemperature(int value) {
        BlockPos pos = getPos();
        BlockPos[] coordinates = { pos.offset(1, 0, 0),
                pos.offset(-1, 0, 0),
                pos.offset(0, 1, 0),
                pos.offset(0, -1, 0),
                pos.offset(0, 0, 1),
                pos.offset(0, 0, -1) };
        for (BlockPos blockPos : coordinates) {
            if (getTemperature() >= getMaxTemperature()) {
                doExplosion(5);
                break;
            }
            if (MetaMachine.getMachine(getLevel(), blockPos) instanceof IHeaterMachine heaterMachine) {
                if (heaterMachine.getHeatCapacity() == 0 || heaterMachine.getTemperature() <= getTemperature()) continue;
                int demand = Math.min(value, Math.min(getMaxTemperature() - getTemperature(), Math.min(heaterMachine.getTemperature() - getTemperature(), heaterMachine.getHeatCapacity())));
                if (demand > 0) {
                    int change = heaterMachine.reduceTemperature(demand);
                    value -= change;
                    setTemperature(getTemperature() + change);
                } else {
                    break;
                }
            }
        }
    }
}

package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class SuperSpaceElevatorMachine extends SpaceElevatorMachine {

    public SuperSpaceElevatorMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    int getBaseHigh() {
        return 50;
    }

    @Override
    void update(boolean promptly) {
        if (promptly || getOffsetTimer() % 40 == 0) {
            mam = 0;
            Level level = getLevel();
            if (level == null) return;
            BlockPos blockPos = MachineUtil.getOffsetPos(4, -2, getFrontFacing(), getPos());
            BlockPos[] coordinatess = new BlockPos[] {
                    blockPos.offset(9, 2, 1),
                    blockPos.offset(9, 2, -1),
                    blockPos.offset(11, 2, 1),
                    blockPos.offset(11, 2, -1),
                    blockPos.offset(13, 2, 1),
                    blockPos.offset(13, 2, -1),
                    blockPos.offset(15, 2, 1),
                    blockPos.offset(15, 2, -1),
                    blockPos.offset(-9, 2, 1),
                    blockPos.offset(-9, 2, -1),
                    blockPos.offset(-11, 2, 1),
                    blockPos.offset(-11, 2, -1),
                    blockPos.offset(-13, 2, 1),
                    blockPos.offset(-13, 2, -1),
                    blockPos.offset(-15, 2, 1),
                    blockPos.offset(-15, 2, -1),
                    blockPos.offset(1, 2, 9),
                    blockPos.offset(-1, 2, 9),
                    blockPos.offset(1, 2, 11),
                    blockPos.offset(-1, 2, 11),
                    blockPos.offset(1, 2, 13),
                    blockPos.offset(-1, 2, 13),
                    blockPos.offset(1, 2, 15),
                    blockPos.offset(-1, 2, 15),
                    blockPos.offset(1, 2, -9),
                    blockPos.offset(-1, 2, -9),
                    blockPos.offset(1, 2, -11),
                    blockPos.offset(-1, 2, -11),
                    blockPos.offset(1, 2, -13),
                    blockPos.offset(-1, 2, -13),
                    blockPos.offset(1, 2, -15),
                    blockPos.offset(-1, 2, -15),
                    blockPos.offset(10, 2, 7),
                    blockPos.offset(10, 2, -7),
                    blockPos.offset(12, 2, 7),
                    blockPos.offset(12, 2, -7),
                    blockPos.offset(14, 2, 7),
                    blockPos.offset(14, 2, -7),
                    blockPos.offset(-10, 2, 7),
                    blockPos.offset(-10, 2, -7),
                    blockPos.offset(-12, 2, 7),
                    blockPos.offset(-12, 2, -7),
                    blockPos.offset(-14, 2, 7),
                    blockPos.offset(-14, 2, -7),
                    blockPos.offset(7, 2, 10),
                    blockPos.offset(-7, 2, 10),
                    blockPos.offset(7, 2, 12),
                    blockPos.offset(-7, 2, 12),
                    blockPos.offset(7, 2, 14),
                    blockPos.offset(-7, 2, 14),
                    blockPos.offset(7, 2, -10),
                    blockPos.offset(-7, 2, -10),
                    blockPos.offset(7, 2, -12),
                    blockPos.offset(-7, 2, -12),
                    blockPos.offset(7, 2, -14),
                    blockPos.offset(-7, 2, -14),
                    blockPos.offset(21, 2, 12),
                    blockPos.offset(12, 2, 21),
                    blockPos.offset(-21, 2, 12),
                    blockPos.offset(-12, 2, 21),
                    blockPos.offset(21, 2, -12),
                    blockPos.offset(12, 2, -21),
                    blockPos.offset(-21, 2, -12),
                    blockPos.offset(-12, 2, -21) };
            for (BlockPos blockPoss : coordinatess) {
                MetaMachine metaMachine = MetaMachine.getMachine(level, blockPoss);
                if (metaMachine instanceof SpaceElevatorModuleMachine moduleMachine && moduleMachine.isFormed()) {
                    moduleMachine.spaceElevatorMachine = this;
                    mam++;
                }
            }
        }
    }
}

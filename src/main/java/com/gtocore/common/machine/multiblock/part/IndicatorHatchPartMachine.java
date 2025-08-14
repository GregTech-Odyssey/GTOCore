package com.gtocore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;

import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class IndicatorHatchPartMachine extends MultiblockPartMachine {

    private int redstoneSignalOutput;

    public IndicatorHatchPartMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    public void setRedstoneSignalOutput(int redstoneSignalOutput) {
        if (this.redstoneSignalOutput != redstoneSignalOutput) {
            this.redstoneSignalOutput = redstoneSignalOutput;
            updateSignal();
        }
    }

    @Override
    public int getOutputSignal(@Nullable Direction side) {
        if (side == getFrontFacing().getOpposite()) {
            return redstoneSignalOutput;
        }
        return 0;
    }

    @Override
    public boolean canConnectRedstone(Direction side) {
        return side == getFrontFacing();
    }

    @Override
    public boolean canShared() {
        return false;
    }

    public int getRedstoneSignalOutput() {
        return this.redstoneSignalOutput;
    }
}

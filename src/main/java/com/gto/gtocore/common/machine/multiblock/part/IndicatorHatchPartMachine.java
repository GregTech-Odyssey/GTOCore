package com.gto.gtocore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IndicatorHatchPartMachine extends MultiblockPartMachine {

    private int redstoneSignalOutput;

    public IndicatorHatchPartMachine(IMachineBlockEntity holder) {
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
}

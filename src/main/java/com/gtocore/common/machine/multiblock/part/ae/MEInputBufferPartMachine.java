package com.gtocore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.KeyCounter;

import java.util.List;

public class MEInputBufferPartMachine extends MEPatternBufferPartMachine {

    MEInputBufferPartMachine(MetaMachineBlockEntity holder, int maxPatternCount) {
        super(holder, maxPatternCount);
    }

    @Override
    public List<IPatternDetails> getAvailablePatterns() {
        return List.of();
    }

    @Override
    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        return false;
    }

    @Override
    public boolean isBusy() {
        return true;
    }
}

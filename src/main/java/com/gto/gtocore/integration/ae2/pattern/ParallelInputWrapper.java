package com.gto.gtocore.integration.ae2.pattern;

import net.minecraft.world.level.Level;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import org.jetbrains.annotations.Nullable;

final class ParallelInputWrapper implements IPatternDetails.IInput {

    private final IPatternDetails.IInput delegation;
    private final GenericStack[] possibleInputs;

    ParallelInputWrapper(IPatternDetails.IInput delegation, int parallel) {
        this.delegation = delegation;
        GenericStack[] possibleInputs = delegation.getPossibleInputs();
        this.possibleInputs = new GenericStack[possibleInputs.length];
        for (int i = 0; i < possibleInputs.length; i++) {
            this.possibleInputs[i] = new GenericStack(possibleInputs[i].what(), possibleInputs[i].amount() * parallel);
        }
    }

    @Override
    public GenericStack[] getPossibleInputs() {
        return possibleInputs;
    }

    @Override
    public long getMultiplier() {
        return delegation.getMultiplier();
    }

    @Override
    public boolean isValid(AEKey input, Level level) {
        return delegation.isValid(input, level);
    }

    @Override
    public @Nullable AEKey getRemainingKey(AEKey template) {
        return delegation.getRemainingKey(template);
    }
}

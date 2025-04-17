package com.gto.gtocore.integration.ae2.pattern;

import net.minecraft.world.level.Level;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AESmithingTablePattern;

public final class ParallelAESmithingTablePattern extends AESmithingTablePattern {

    private final IInput[] input;
    private final GenericStack[] output;

    public ParallelAESmithingTablePattern(AEItemKey definition, Level level, int parallel) {
        super(definition, level);
        IInput[] inputs = super.getInputs();
        this.input = new IInput[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            this.input[i] = new ParallelInputWrapper(inputs[i], parallel);
        }
        GenericStack[] outputs = super.getOutputs();
        this.output = new GenericStack[outputs.length];
        for (int i = 0; i < outputs.length; i++) {
            this.output[i] = new GenericStack(outputs[i].what(), outputs[i].amount() * parallel);
        }
    }

    @Override
    public IInput[] getInputs() {
        return input;
    }

    @Override
    public GenericStack[] getOutputs() {
        return output;
    }
}

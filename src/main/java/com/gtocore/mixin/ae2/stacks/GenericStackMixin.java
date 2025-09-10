package com.gtocore.mixin.ae2.stacks;

import com.gtolib.api.ae2.stacks.IGenericStack;

import appeng.api.stacks.GenericStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GenericStack.class)
public class GenericStackMixin implements IGenericStack {

    @Mutable
    @Shadow(remap = false)
    @Final
    private long amount;

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
    }
}

package com.gtocore.mixin.ae2.pattern;

import com.gtolib.api.ae2.pattern.IDetails;

import appeng.api.stacks.KeyCounter;
import appeng.crafting.pattern.AEProcessingPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AEProcessingPattern.class)
public abstract class AEProcessingPatternMixin implements IDetails {

    @Unique
    private KeyCounter[] gtolib$inputHolder;

    @Override
    public KeyCounter[] gtolib$getInputHolder() {
        if (gtolib$inputHolder == null) {
            var length = getInputs().length;
            gtolib$inputHolder = new KeyCounter[length];
            for (int i = 0; i < length; i++) {
                gtolib$inputHolder[i] = new KeyCounter();
            }
        }
        return gtolib$inputHolder;
    }
}

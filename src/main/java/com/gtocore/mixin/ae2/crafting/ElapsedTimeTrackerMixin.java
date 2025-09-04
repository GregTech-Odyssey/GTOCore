package com.gtocore.mixin.ae2.crafting;

import com.gtocore.api.ae2.crafting.IElapsedTimeTracker;

import appeng.api.stacks.AEKeyType;
import appeng.crafting.execution.ElapsedTimeTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ElapsedTimeTracker.class)
public abstract class ElapsedTimeTrackerMixin implements IElapsedTimeTracker {

    @Shadow(remap = false)
    abstract void decrementItems(long itemDiff, AEKeyType keyType);

    @Shadow(remap = false)
    abstract void addMaxItems(long itemDiff, AEKeyType keyType);

    @Override
    public void gtolib$decrementItems(long itemDiff, AEKeyType keyType) {
        decrementItems(itemDiff, keyType);
    }

    @Override
    public void gtolib$addMaxItems(long itemDiff, AEKeyType keyType) {
        addMaxItems(itemDiff, keyType);
    }
}

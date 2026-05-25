package com.gtocore.common.machine.multiblock.part.ae;

public abstract class AbstractRecipeInternalSlot extends MEPatternPartMachineKt.AbstractInternalSlot {

    private Runnable onContentsChanged = () -> {};

    public abstract boolean isEmpty();

    public abstract boolean isItemEmpty();

    public abstract boolean isFluidEmpty();

    public final void markContentsChanged() {
        onContentsChanged.run();
    }

    @Override
    public final void setOnContentsChanged(final Runnable onContentsChanged) {
        this.onContentsChanged = onContentsChanged;
    }

    @Override
    public final Runnable getOnContentsChanged() {
        return this.onContentsChanged;
    }
}

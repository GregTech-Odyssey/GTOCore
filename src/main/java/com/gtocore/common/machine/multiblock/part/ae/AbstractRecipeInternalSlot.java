package com.gtocore.common.machine.multiblock.part.ae;

public abstract class AbstractRecipeInternalSlot extends MEPatternPartMachineKt.AbstractInternalSlot {

    private Runnable onContentsChanged = () -> {};
    private boolean isContentsChanged = true;

    public abstract boolean isEmpty();

    public final void markContentsChanged() {
        isContentsChanged = true;
        onContentsChanged.run();
    }

    public boolean isContentsChanged() {
        if (isContentsChanged) {
            isContentsChanged = false;
            return true;
        }
        return false;
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

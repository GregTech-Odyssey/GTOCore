package com.gtocore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;

public abstract class AbstractRecipeInternalSlot extends MEPatternPartMachineKt.AbstractInternalSlot {

    private Runnable onContentsChanged = () -> {};
    private boolean itemChanged = true;
    private boolean fluidChanged = true;

    public abstract boolean isEmpty();

    public abstract long getItemAmount(ItemIngredient ingredient, long limit);

    public abstract long getFluidAmount(FluidIngredient ingredient, long limit);

    public final void markContentsChanged() {
        itemChanged = true;
        fluidChanged = true;
        onContentsChanged.run();
    }

    public final boolean consumeItemChanged() {
        if (!itemChanged) return false;
        itemChanged = false;
        return true;
    }

    public final boolean consumeFluidChanged() {
        if (!fluidChanged) return false;
        fluidChanged = false;
        return true;
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

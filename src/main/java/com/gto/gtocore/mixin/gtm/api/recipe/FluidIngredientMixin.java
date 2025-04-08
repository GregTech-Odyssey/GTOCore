package com.gto.gtocore.mixin.gtm.api.recipe;

import com.gto.gtocore.utils.EmptyStream;

import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.nbt.CompoundTag;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FluidIngredient.class)
public abstract class FluidIngredientMixin {

    @Shadow(remap = false)
    public FluidIngredient.Value[] values;

    @Shadow(remap = false)
    private int amount;

    @Shadow(remap = false)
    private CompoundTag nbt;

    @Unique
    private static final EmptyStream<FluidIngredient.Value> EMPTY_STREAM = EmptyStream.create(new FluidIngredient.Value[0]);

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public FluidIngredient copy() {
        FluidIngredient ingredient = new FluidIngredient(EMPTY_STREAM, amount, nbt == null ? null : nbt.copy());
        ingredient.values = values;
        return ingredient;
    }
}

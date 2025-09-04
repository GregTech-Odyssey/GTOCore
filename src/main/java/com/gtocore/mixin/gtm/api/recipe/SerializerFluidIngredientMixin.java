package com.gtocore.mixin.gtm.api.recipe;

import com.gtolib.api.recipe.ingredient.FastFluidIngredient;

import com.gregtechceu.gtceu.api.recipe.content.SerializerFluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SerializerFluidIngredient.class)
public class SerializerFluidIngredientMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public FluidIngredient fromNetwork(FriendlyByteBuf buf) {
        return FastFluidIngredient.fromNetwork(buf);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public FluidIngredient fromJson(JsonElement json) {
        return FastFluidIngredient.fromJson(json);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public FluidIngredient of(Object o) {
        if (o instanceof FluidIngredient ingredient) {
            return ingredient;
        }
        if (o instanceof FluidStack stack) {
            return FastFluidIngredient.of(stack);
        }
        return FastFluidIngredient.EMPTY;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public FluidIngredient defaultValue() {
        return FastFluidIngredient.EMPTY;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public Codec<FluidIngredient> codec() {
        return FastFluidIngredient.CODEC;
    }
}

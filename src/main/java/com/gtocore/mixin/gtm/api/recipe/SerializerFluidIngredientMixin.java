package com.gtocore.mixin.gtm.api.recipe;

import com.gtolib.api.recipe.ingredient.FastFluidIngredient;

import com.gregtechceu.gtceu.api.recipe.content.IContentSerializer;
import com.gregtechceu.gtceu.api.recipe.content.SerializerFluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SerializerFluidIngredient.class)
public abstract class SerializerFluidIngredientMixin implements IContentSerializer<FluidIngredient> {

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

    @Override
    public Tag toNbt(FluidIngredient content) {
        return ((FastFluidIngredient) content).toNbt();
    }

    @Override
    public FluidIngredient fromNbt(Tag tag) {
        return FastFluidIngredient.fromNbt(tag);
    }
}

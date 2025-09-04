package com.gtocore.mixin.gtm.api.recipe;

import com.gtolib.api.recipe.ingredient.FastFluidIngredient;

import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(FluidIngredient.class)
public class FluidIngredientMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void init(CallbackInfo ci) {
        FluidIngredient.EMPTY = FastFluidIngredient.EMPTY;
        FluidIngredient.CODEC = FastFluidIngredient.CODEC;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static FluidIngredient of() {
        return FastFluidIngredient.EMPTY;
    }

    @Inject(method = "of(I[Lnet/minecraft/world/level/material/Fluid;)Lcom/gregtechceu/gtceu/api/recipe/ingredient/FluidIngredient;", at = @At("HEAD"), remap = false, cancellable = true)
    private static void items(int amount, Fluid[] items, CallbackInfoReturnable<FluidIngredient> cir) {
        cir.setReturnValue(FastFluidIngredient.of(amount, items));
    }

    @Inject(method = "of([Lnet/minecraftforge/fluids/FluidStack;)Lcom/gregtechceu/gtceu/api/recipe/ingredient/FluidIngredient;", at = @At("HEAD"), remap = false, cancellable = true)
    private static void stacks(FluidStack[] stacks, CallbackInfoReturnable<FluidIngredient> cir) {
        cir.setReturnValue(FastFluidIngredient.of(stacks));
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static FluidIngredient of(Stream<Fluid> stacks, int amount, CompoundTag nbt) {
        return FastFluidIngredient.of(stacks, amount, nbt);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static FluidIngredient of(TagKey<Fluid> tag, int amount) {
        throw new UnsupportedOperationException("TagKey is not supported");
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static FluidIngredient of(TagKey<Fluid> tag, int amount, CompoundTag nbt) {
        throw new UnsupportedOperationException("TagKey is not supported");
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static FluidIngredient fromNetwork(FriendlyByteBuf buf) {
        return FastFluidIngredient.fromNetwork(buf);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static FluidIngredient fromJson(@Nullable JsonElement json) {
        return FastFluidIngredient.fromJson(json);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static FluidIngredient fromJson(@Nullable JsonElement json, boolean allowAir) {
        return FastFluidIngredient.fromJson(json);
    }
}

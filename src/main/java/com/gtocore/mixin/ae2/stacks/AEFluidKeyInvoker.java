package com.gtocore.mixin.ae2.stacks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;

import appeng.api.stacks.AEFluidKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AEFluidKey.class)
public interface AEFluidKeyInvoker {

    @Invoker(value = "<init>", remap = false)
    static @NotNull AEFluidKey of(Fluid fluid, @Nullable CompoundTag tag) {
        throw new AssertionError();
    }
}

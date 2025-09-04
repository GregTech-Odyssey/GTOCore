package com.gtocore.mixin.ae2.stacks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import appeng.api.stacks.AEItemKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AEItemKey.class)
public interface AEItemKeyInvoker {

    @Invoker(value = "of", remap = false)
    static @NotNull AEItemKey of(ItemLike item, @Nullable CompoundTag tag, @Nullable CompoundTag caps) {
        throw new AssertionError();
    }

    @Invoker(value = "serializeStackCaps", remap = false)
    static CompoundTag serializeStackCaps(ItemStack stack) {
        throw new AssertionError();
    }
}

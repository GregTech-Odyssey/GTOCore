package com.gtocore.mixin.ae2.gui;

import com.gtolib.api.ae2.gui.hooks.IRepoSlot;

import net.minecraft.world.item.ItemStack;

import appeng.client.gui.me.common.RepoSlot;
import appeng.menu.me.common.GridInventoryEntry;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = RepoSlot.class, remap = false)
public class RepoSlotMixin implements IRepoSlot {

    @Override
    @Shadow
    public GridInventoryEntry getEntry() {
        throw new UnsupportedOperationException("This method should not be called directly.");
    }

    @Override
    @Shadow
    public long getStoredAmount() {
        throw new UnsupportedOperationException("This method should not be called directly.");
    }

    @Override
    @Shadow
    public boolean isCraftable() {
        throw new UnsupportedOperationException("This method should not be called directly.");
    }

    @Override
    @Shadow(remap = true)
    public @NotNull ItemStack getItem() {
        throw new UnsupportedOperationException("This method should not be called directly.");
    }

    @Override
    @Shadow(remap = true)
    public boolean hasItem() {
        throw new UnsupportedOperationException("This method should not be called directly.");
    }
}

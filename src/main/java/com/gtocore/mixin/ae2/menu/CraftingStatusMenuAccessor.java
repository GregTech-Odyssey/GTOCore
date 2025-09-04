package com.gtocore.mixin.ae2.menu;

import appeng.menu.me.crafting.CraftingStatusMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Comparator;

@Mixin(CraftingStatusMenu.class)
public interface CraftingStatusMenuAccessor {

    @Accessor(value = "CPU_COMPARATOR", remap = false)
    static Comparator<CraftingStatusMenu.CraftingCpuListEntry> getCPU_COMPARATOR() {
        throw new UnsupportedOperationException("This method should not be called directly.");
    }
}

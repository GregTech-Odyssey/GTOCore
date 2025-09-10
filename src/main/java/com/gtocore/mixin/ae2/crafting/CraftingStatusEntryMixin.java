package com.gtocore.mixin.ae2.crafting;

import net.minecraft.world.item.Items;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.menu.me.crafting.CraftingStatusEntry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CraftingStatusEntry.class)
public class CraftingStatusEntryMixin {

    @Shadow(remap = false)
    @Final
    private @Nullable AEKey what;

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public AEKey getWhat() {
        if (what == null) {
            return AEItemKey.of(Items.BARRIER);
        }
        return what;
    }
}

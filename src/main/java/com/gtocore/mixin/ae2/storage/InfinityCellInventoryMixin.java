package com.gtocore.mixin.ae2.storage;

import appeng.api.stacks.AEKey;
import com.glodblock.github.extendedae.common.inventory.InfinityCellInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InfinityCellInventory.class)
public class InfinityCellInventoryMixin {

    @Redirect(method = "getAvailableStacks", at = @At(value = "INVOKE", target = "Lcom/glodblock/github/extendedae/common/items/InfinityCell;getAsIntMax(Lappeng/api/stacks/AEKey;)J"), remap = false)
    private long getAsLongMax(AEKey key) {
        return 0x1fffffffffffffffL;
    }
}

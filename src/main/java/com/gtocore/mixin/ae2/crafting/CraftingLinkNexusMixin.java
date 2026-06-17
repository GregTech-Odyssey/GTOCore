package com.gtocore.mixin.ae2.crafting;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.crafting.CraftingLinkNexus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftingLinkNexus.class)
public abstract class CraftingLinkNexusMixin {

    @Redirect(method = "isDead", at = @At(value = "INVOKE", target = "Lappeng/api/networking/IGridNode;getGrid()Lappeng/api/networking/IGrid;"), remap = false)
    private IGrid gto$missingRequesterNodeIsDead(IGridNode node) {
        return node == null ? null : node.getGrid();
    }
}

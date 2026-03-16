package com.gtocore.mixin.ae2.menu;

import com.gtolib.api.ae2.me2in1.Me2in1Menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import appeng.menu.AEBaseMenu;
import appeng.menu.implementations.PatternAccessTermMenu;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PatternAccessTermMenu.class, priority = 1900)
public abstract class PatternAccessTermMenuMixin extends AEBaseMenu {

    public PatternAccessTermMenuMixin(MenuType<?> menuType, int id, Inventory playerInventory, Object host) {
        super(menuType, id, playerInventory, host);
    }

    @Redirect(
              method = "doAction",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z",
                       remap = true),
              remap = false)
    private boolean modifyDoActionAdd(Inventory playerInv, ItemStack stack) {
        if ((Object) this instanceof Me2in1Menu menu) {
            var after = menu.getEncoding().transferPatternToBuffer(stack);
            if (after.isEmpty()) {
                // If the pattern is transferred to the buffer, we don't need to add it to the player's inventory.
                return true;
            }
        }
        return playerInv.add(stack);
    }
}

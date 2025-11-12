package com.gtocore.mixin.adastra;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import earth.terrarium.adastra.common.items.GasTankItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GasTankItem.class)
public abstract class GasTankItemMixin {

    @Redirect(
              method = "onUseTick",
              at = @At(
                       value = "INVOKE",
                       target = "Lnet/minecraft/world/entity/player/Inventory;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
    private void adastra$fixOffhandUpdate(Inventory instance, int slot, ItemStack item) {
        if (instance.player instanceof Player player) {
            InteractionHand activeHand = player.getUsedItemHand();
            player.setItemInHand(activeHand, item);
        }
    }
}

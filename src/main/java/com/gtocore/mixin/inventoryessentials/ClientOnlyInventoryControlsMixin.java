package com.gtocore.mixin.inventoryessentials;

import com.gtolib.api.ae2.me2in1.Me2in1Menu;

import net.blay09.mods.inventoryessentials.client.ClientOnlyInventoryControls;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientOnlyInventoryControls.class)
public class ClientOnlyInventoryControlsMixin {

    @Inject(method = "slotClick(Lnet/minecraft/world/inventory/AbstractContainerMenu;IILnet/minecraft/world/inventory/ClickType;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false)
    private void onSlotClick(AbstractContainerMenu menu, int slotIndex, int mouseButton, ClickType clickType, CallbackInfo ci) {
        if (menu instanceof Me2in1Menu) ci.cancel();
    }
}

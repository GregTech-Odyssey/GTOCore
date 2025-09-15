package com.gtocore.mixin.ae2.wtlib;

import com.gtolib.api.ae2.me2in1.Wireless;

import net.minecraft.world.item.ItemStack;

import appeng.api.upgrades.IUpgradeInventory;
import de.mari_023.ae2wtlib.terminal.ItemWT;
import de.mari_023.ae2wtlib.wut.ItemWUT;
import de.mari_023.ae2wtlib.wut.WUTHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemWT.class)
public abstract class ItemWTMixin {

    @Inject(method = "hasQuantumUpgrade", at = @At("HEAD"), cancellable = true, remap = false)
    private static void callHasQuantumUpgrade(ItemStack stack, IUpgradeInventory inventory, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof Wireless.Item ||
                stack.getItem() instanceof ItemWUT && WUTHandler.hasTerminal(stack, Wireless.ID)) {
            cir.setReturnValue(true);
        }
    }
}

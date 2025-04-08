package com.gto.gtocore.mixin.forge;

import net.minecraftforge.items.ItemStackHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemStackHandler.class, priority = 0)
public class ItemStackHandlerMixin {

    @Inject(method = "validateSlotIndex", at = @At("HEAD"), remap = false, cancellable = true)
    protected void validateSlotIndex(int slot, CallbackInfo ci) {
        ci.cancel();
    }
}

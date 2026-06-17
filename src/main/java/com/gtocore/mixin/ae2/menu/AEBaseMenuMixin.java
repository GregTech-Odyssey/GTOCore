package com.gtocore.mixin.ae2.menu;

import net.minecraft.world.entity.player.Player;

import appeng.api.networking.security.IActionHost;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.common.MEStorageMenu;

import de.mari_023.ae2wtlib.terminal.WTMenuHost;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AEBaseMenu.class)
public abstract class AEBaseMenuMixin {

    @Shadow(remap = false)
    protected abstract IActionHost getActionHost();

    @Inject(method = "stillValid", at = @At("RETURN"), cancellable = true)
    private void gtolib$stillValid(Player PlayerEntity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;
        if (getActionHost() instanceof WTMenuHost) {
            if ((Object) this instanceof MEStorageMenu menu) {
                ((MEStorageMenuAccessor) menu).callUpdatePowerStatus();
            }
            cir.setReturnValue(true);
        }
    }
}

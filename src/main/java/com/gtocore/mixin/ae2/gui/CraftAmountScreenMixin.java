package com.gtocore.mixin.ae2.gui;

import com.gtocore.integration.ae.IConfirmLongMenu;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.crafting.CraftAmountScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.NumberEntryWidget;
import appeng.menu.me.crafting.CraftAmountMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftAmountScreen.class)
public class CraftAmountScreenMixin extends AEBaseScreen<CraftAmountMenu> {

    @Shadow(remap = false)
    @Final
    private NumberEntryWidget amountToCraft;

    @Shadow(remap = false)
    @Final
    private Button next;

    public CraftAmountScreenMixin(CraftAmountMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lappeng/client/gui/widgets/NumberEntryWidget;setMaxValue(J)V", remap = false), remap = false)
    private void redirectSetMaxValue(appeng.client.gui.widgets.NumberEntryWidget instance, long maxValue) {
        instance.setMaxValue(Long.MAX_VALUE);
    }

    @Inject(method = "confirm", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private void onConfirm(CallbackInfo ci) {
        long amount = amountToCraft.getLongValue().orElse(0);
        boolean craftMissingAmount = this.amountToCraft.startsWithEquals();
        if (amount <= 0) {
            return;
        }
        ((IConfirmLongMenu) this.menu).gtocore$confirmLong(amount, craftMissingAmount, hasShiftDown());
        ci.cancel();
    }

    @Inject(method = "updateBeforeRender", at = @At("TAIL"), remap = false)
    private void onUpdateBeforeRender(CallbackInfo ci) {
        this.next.active = this.amountToCraft.getLongValue().orElse(0) > 0;
    }
}

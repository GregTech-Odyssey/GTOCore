package com.gtocore.mixin.ae2.gui;

import com.gtolib.api.ae2.GTOSettings;
import com.gtolib.api.ae2.ShowMolecularAssembler;

import net.minecraft.world.item.ItemStack;

import appeng.api.util.IConfigManager;
import appeng.items.tools.powered.WirelessTerminalItem;
import appeng.util.ConfigManager;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WirelessTerminalItem.class)
public class WirelessTerminalItemMixin {

    @Inject(remap = false, method = "getConfigManager", at = @At(value = "TAIL"))
    private void gto$ae2$getConfigManager(ItemStack target, CallbackInfoReturnable<IConfigManager> cir, @Local(name = "out") ConfigManager out) {
        out.registerSetting(GTOSettings.TERMINAL_SHOW_MOLECULAR_ASSEMBLERS, ShowMolecularAssembler.ALL);
    }
}

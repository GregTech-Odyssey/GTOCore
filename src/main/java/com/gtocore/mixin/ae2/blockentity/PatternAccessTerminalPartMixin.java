package com.gtocore.mixin.ae2.blockentity;

import appeng.parts.reporting.PatternAccessTerminalPart;
import appeng.util.ConfigManager;

import gto_ae.api.config.ExtendedSettings;
import gto_ae.menu.ShowMolecularAssembler;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatternAccessTerminalPart.class)
public abstract class PatternAccessTerminalPartMixin {

    @Shadow(remap = false)
    @Final
    private ConfigManager configManager;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(CallbackInfo ci) {
        this.configManager.registerSetting(ExtendedSettings.TERMINAL_SHOW_MOLECULAR_ASSEMBLERS, ShowMolecularAssembler.ALL);
    }
}

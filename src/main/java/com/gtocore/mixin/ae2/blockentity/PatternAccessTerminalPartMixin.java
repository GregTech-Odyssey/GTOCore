package com.gtocore.mixin.ae2.blockentity;

import com.gtolib.api.ae2.GTOSettings;
import com.gtolib.api.ae2.ShowMolecularAssembler;

import appeng.parts.reporting.PatternAccessTerminalPart;
import appeng.util.ConfigManager;
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
        this.configManager.registerSetting(GTOSettings.TERMINAL_SHOW_MOLECULAR_ASSEMBLERS, ShowMolecularAssembler.ALL);
    }
}

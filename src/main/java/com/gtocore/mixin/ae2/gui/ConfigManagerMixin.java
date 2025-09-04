package com.gtocore.mixin.ae2.gui;

import com.gtolib.api.ae2.GTOSettings;
import com.gtolib.api.ae2.ShiftTransferTo;
import com.gtolib.api.ae2.ShowMolecularAssembler;

import appeng.api.config.Setting;
import appeng.api.config.Settings;
import appeng.api.config.ShowPatternProviders;
import appeng.util.ConfigManager;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ConfigManager.class)
public class ConfigManagerMixin {

    @Mutable
    @Shadow(remap = false)
    @Final
    private Map<Setting<?>, Enum<?>> settings;

    @Unique
    private static Map<Setting<?>, Object> gto$ae2$settings;

    @Unique
    private static Map<Setting<?>, Object> gto$ae2getSettings() {
        if (gto$ae2$settings == null) {
            gto$ae2$settings = Map.of(Settings.TERMINAL_SHOW_PATTERN_PROVIDERS, ShowPatternProviders.VISIBLE, GTOSettings.TERMINAL_SHOW_MOLECULAR_ASSEMBLERS, ShowMolecularAssembler.ALL, GTOSettings.ME2IN1_SHIFT_TRANSFER_TO, ShiftTransferTo.INVENTORY_OR_BUFFER);
        }
        return gto$ae2$settings;
    }

    @Inject(method = "<init>(Ljava/lang/Runnable;)V", at = @At("TAIL"), remap = false)
    private void gto$ae2$init(Runnable changeListener, CallbackInfo ci) {
        settings = new Reference2ReferenceOpenHashMap<>();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public <T extends Enum<T>> T getSetting(Setting<T> setting) {
        var oldValue = this.settings.get(setting);
        if (oldValue == null) {
            return (T) gto$ae2getSettings().get(setting);
        }
        return (T) oldValue;
    }
}

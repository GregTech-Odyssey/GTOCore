package com.gtocore.mixin.ae2.gui;

import com.gtolib.api.ae2.BlockingType;
import com.gtolib.api.ae2.GTOSettings;
import com.gtolib.api.ae2.ShiftTransferTo;
import com.gtolib.api.ae2.ShowMolecularAssembler;

import appeng.api.config.Setting;
import appeng.api.config.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Settings.class)
public class SettingsMixin {

    @SafeVarargs
    @Shadow(remap = false)
    private static <T extends Enum<T>> Setting<T> register(String name, T firstOption, T... moreOptions) {
        return null;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void init(CallbackInfo ci) {
        GTOSettings.BLOCKING_TYPE = register("blocking_type", BlockingType.NONE, BlockingType.ALL, BlockingType.CONTAIN, BlockingType.NON_CONTAIN);
        GTOSettings.TERMINAL_SHOW_MOLECULAR_ASSEMBLERS = register("show_molecular_assemblers", ShowMolecularAssembler.ALL, ShowMolecularAssembler.ONLY_MOLECULAR_ASSEMBLER, ShowMolecularAssembler.EXPECT_MOLECULAR_ASSEMBLER);
        GTOSettings.ME2IN1_SHIFT_TRANSFER_TO = register("me2in1_shift_transfer_to", ShiftTransferTo.INVENTORY_OR_BUFFER, ShiftTransferTo.CURRENTLY_VISIBLE_ACCESSOR);
    }
}

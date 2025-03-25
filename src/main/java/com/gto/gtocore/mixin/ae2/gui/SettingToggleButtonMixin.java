package com.gto.gtocore.mixin.ae2.gui;

import com.gto.gtocore.integration.ae2.BlockingType;
import com.gto.gtocore.integration.ae2.GTOSettings;

import net.minecraft.network.chat.Component;

import appeng.api.config.CondenserOutput;
import appeng.api.config.Setting;
import appeng.api.config.Settings;
import appeng.client.gui.Icon;
import appeng.client.gui.widgets.SettingToggleButton;
import appeng.core.localization.ButtonToolTips;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SettingToggleButton.class)
public class SettingToggleButtonMixin {

    @Shadow(remap = false)
    private static <T extends Enum<T>> void registerApp(Icon icon, Setting<T> setting, T val, ButtonToolTips title, ButtonToolTips hint) {}

    @Shadow(remap = false)
    private static <T extends Enum<T>> void registerApp(Icon icon, Setting<T> setting, T val, ButtonToolTips title, Component... tooltipLines) {}

    @Redirect(method = "<init>(Lappeng/api/config/Setting;Ljava/lang/Enum;Ljava/util/function/Predicate;Lappeng/client/gui/widgets/SettingToggleButton$IHandler;)V", at = @At(value = "INVOKE", target = "Lappeng/client/gui/widgets/SettingToggleButton;registerApp(Lappeng/client/gui/Icon;Lappeng/api/config/Setting;Ljava/lang/Enum;Lappeng/core/localization/ButtonToolTips;Lappeng/core/localization/ButtonToolTips;)V", ordinal = 0), remap = false)
    private <T extends Enum<T>> void register(Icon icon, Setting<T> setting, T val, ButtonToolTips title, ButtonToolTips hint) {
        registerApp(Icon.CONDENSER_OUTPUT_TRASH, Settings.CONDENSER_OUTPUT, CondenserOutput.TRASH,
                ButtonToolTips.CondenserOutput,
                ButtonToolTips.Trash);

        registerApp(Icon.BLOCKING_MODE_YES, GTOSettings.BLOCKING_TYPE, BlockingType.ALL, ButtonToolTips.InterfaceBlockingMode, Component.translatable("gtocore.pattern.blocking_mode"));
        registerApp(Icon.BLOCKING_MODE_NO, GTOSettings.BLOCKING_TYPE, BlockingType.CONTAIN, ButtonToolTips.InterfaceBlockingMode, Component.translatable("ftbquests.file.defaults"));
        registerApp(Icon.BLOCKING_MODE_NO, GTOSettings.BLOCKING_TYPE, BlockingType.NON_CONTAIN, ButtonToolTips.InterfaceBlockingMode, Component.translatable("gtocore.pattern.blocking_reverse"));
    }
}

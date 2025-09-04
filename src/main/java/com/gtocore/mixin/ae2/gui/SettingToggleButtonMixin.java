package com.gtocore.mixin.ae2.gui;

import com.gtolib.api.ae2.BlockingType;
import com.gtolib.api.ae2.GTOSettings;
import com.gtolib.api.ae2.ShiftTransferTo;
import com.gtolib.api.ae2.ShowMolecularAssembler;
import com.gtolib.api.ae2.gui.hooks.SettingToggleButtonReflect;

import net.minecraft.network.chat.Component;

import appeng.api.config.CondenserOutput;
import appeng.api.config.Setting;
import appeng.api.config.Settings;
import appeng.client.gui.Icon;
import appeng.client.gui.widgets.SettingToggleButton;
import appeng.core.localization.ButtonToolTips;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.Map;

@Mixin(SettingToggleButton.class)
public class SettingToggleButtonMixin {

    @Shadow(remap = false)
    private static Map<Object, Object> appearances;

    @Unique
    private static <T extends Enum<T>> void gto$registerApp(Icon icon, Setting<T> setting, T val, Component title, Component... tooltipLines) {
        var lines = new ObjectArrayList<Component>();
        lines.add(title);
        Collections.addAll(lines, tooltipLines);

        appearances.put(
                SettingToggleButtonReflect.newEnumPair(setting, val),
                SettingToggleButtonReflect.newButtonAppearance(icon, null, lines));
    }

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
        registerApp(Icon.BLOCKING_MODE_YES, GTOSettings.BLOCKING_TYPE, BlockingType.CONTAIN, ButtonToolTips.InterfaceBlockingMode, Component.translatable("gui.tooltips.ae2.Blocking"));
        registerApp(Icon.BLOCKING_MODE_YES, GTOSettings.BLOCKING_TYPE, BlockingType.NON_CONTAIN, ButtonToolTips.InterfaceBlockingMode, Component.translatable("gtocore.pattern.blocking_reverse"));
        registerApp(Icon.BLOCKING_MODE_NO, GTOSettings.BLOCKING_TYPE, BlockingType.NONE, ButtonToolTips.InterfaceBlockingMode, Component.translatable("gui.tooltips.ae2.NonBlocking"));
        registerApp(Icon.LEVEL_ITEM, GTOSettings.TERMINAL_SHOW_MOLECULAR_ASSEMBLERS, ShowMolecularAssembler.ALL, ButtonToolTips.InterfaceTerminalDisplayMode, Component.translatable("gtocore.ae.appeng.crafting.show_molecular_assembler_all"));
        registerApp(Icon.PATTERN_ACCESS_SHOW, GTOSettings.TERMINAL_SHOW_MOLECULAR_ASSEMBLERS, ShowMolecularAssembler.ONLY_MOLECULAR_ASSEMBLER, ButtonToolTips.InterfaceTerminalDisplayMode, Component.translatable("gtocore.ae.appeng.crafting.show_molecular_assembler_only"));
        registerApp(Icon.PATTERN_ACCESS_HIDE, GTOSettings.TERMINAL_SHOW_MOLECULAR_ASSEMBLERS, ShowMolecularAssembler.EXPECT_MOLECULAR_ASSEMBLER, ButtonToolTips.InterfaceTerminalDisplayMode, Component.translatable("gtocore.ae.appeng.crafting.show_molecular_assembler_expect"));

        gto$registerApp(Icon.ARROW_DOWN, GTOSettings.ME2IN1_SHIFT_TRANSFER_TO, ShiftTransferTo.INVENTORY_OR_BUFFER,
                Component.translatable("gtocore.ae.appeng.me2in1.shift_transfer_to"),
                Component.translatable("gtocore.ae.appeng.me2in1.shift_transfer_to.inventory_or_buffer"));
        gto$registerApp(Icon.ARROW_RIGHT, GTOSettings.ME2IN1_SHIFT_TRANSFER_TO, ShiftTransferTo.CURRENTLY_VISIBLE_ACCESSOR,
                Component.translatable("gtocore.ae.appeng.me2in1.shift_transfer_to"),
                Component.translatable("gtocore.ae.appeng.me2in1.shift_transfer_to.accessor"));
    }
}

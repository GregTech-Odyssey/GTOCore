package com.gtocore.mixin.ae2.gui;

import com.gtolib.api.ae2.IPatterEncodingTermMenu;
import com.gtolib.api.ae2.ModifyIcon;
import com.gtolib.api.ae2.ModifyIconButton;

import net.minecraft.network.chat.Component;

import appeng.client.gui.WidgetContainer;
import appeng.client.gui.me.items.EncodingModePanel;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.client.gui.me.items.ProcessingEncodingPanel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProcessingEncodingPanel.class)
public abstract class ProcessingEncodingPanelMixin extends EncodingModePanel {

    @Unique
    private ModifyIconButton gtolib$multipleTow;
    @Unique
    private ModifyIconButton gtolib$multipleThree;
    @Unique
    private ModifyIconButton gtolib$multipleFive;
    @Unique
    private ModifyIconButton gtolib$dividingTow;
    @Unique
    private ModifyIconButton gtolib$dividingThree;
    @Unique
    private ModifyIconButton gtolib$dividingFive;

    protected ProcessingEncodingPanelMixin(PatternEncodingTermScreen<?> screen, WidgetContainer widgets) {
        super(screen, widgets);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(PatternEncodingTermScreen<?> screen, WidgetContainer widgets, CallbackInfo ci) {
        gtolib$multipleTow = new ModifyIconButton(b -> ((IPatterEncodingTermMenu) menu).gtolib$modifyPatter(2), ModifyIcon.MULTIPLY_2,
                Component.translatable("gtocore.pattern.multiply", 2),
                Component.translatable("gtocore.pattern.tooltip.multiply", 2));

        gtolib$multipleThree = new ModifyIconButton(b -> ((IPatterEncodingTermMenu) menu).gtolib$modifyPatter(3), ModifyIcon.MULTIPLY_3,
                Component.translatable("gtocore.pattern.multiply", 3),
                Component.translatable("gtocore.pattern.tooltip.multiply", 3));

        gtolib$multipleFive = new ModifyIconButton(b -> ((IPatterEncodingTermMenu) menu).gtolib$modifyPatter(5), ModifyIcon.MULTIPLY_5,
                Component.translatable("gtocore.pattern.multiply", 5),
                Component.translatable("gtocore.pattern.tooltip.multiply", 5));

        gtolib$dividingTow = new ModifyIconButton(b -> ((IPatterEncodingTermMenu) menu).gtolib$modifyPatter(-2), ModifyIcon.DIVISION_2,
                Component.translatable("gtocore.pattern.divide", 2),
                Component.translatable("gtocore.pattern.tooltip.divide", 2));

        gtolib$dividingThree = new ModifyIconButton(b -> ((IPatterEncodingTermMenu) menu).gtolib$modifyPatter(-3), ModifyIcon.DIVISION_3,
                Component.translatable("gtocore.pattern.divide", 3),
                Component.translatable("gtocore.pattern.tooltip.divide", 3));

        gtolib$dividingFive = new ModifyIconButton(b -> ((IPatterEncodingTermMenu) menu).gtolib$modifyPatter(-5), ModifyIcon.DIVISION_5,
                Component.translatable("gtocore.pattern.divide", 5),
                Component.translatable("gtocore.pattern.tooltip.divide", 5));

        widgets.add("modify1", gtolib$multipleTow);
        widgets.add("modify2", gtolib$multipleThree);
        widgets.add("modify3", gtolib$multipleFive);
        widgets.add("modify4", gtolib$dividingTow);
        widgets.add("modify5", gtolib$dividingThree);
        widgets.add("modify6", gtolib$dividingFive);
    }

    @Inject(method = "setVisible", at = @At("TAIL"), remap = false)
    private void setVisibleHooks(boolean visible, CallbackInfo ci) {
        gtolib$multipleTow.setVisibility(visible);
        gtolib$multipleThree.setVisibility(visible);
        gtolib$multipleFive.setVisibility(visible);
        gtolib$dividingTow.setVisibility(visible);
        gtolib$dividingThree.setVisibility(visible);
        gtolib$dividingFive.setVisibility(visible);
    }
}

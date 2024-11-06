package com.gto.gtocore.mixin.ae2.gui;

import com.gto.gtocore.client.gui.ModifyIcon;
import com.gto.gtocore.client.gui.ModifyIconButton;
import com.gto.gtocore.client.gui.PatterEncodingTermMenuModify;

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

/**
 * @author EasterFG on 2024/9/12
 */

@Mixin(ProcessingEncodingPanel.class)
public abstract class ProcessingEncodingPanelMixin extends EncodingModePanel {

    @Unique
    private ModifyIconButton gTLCore$multipleTow;
    @Unique
    private ModifyIconButton gTLCore$multipleThree;
    @Unique
    private ModifyIconButton gTLCore$multipleFive;
    @Unique
    private ModifyIconButton gTLCore$dividingTow;
    @Unique
    private ModifyIconButton gTLCore$dividingThree;
    @Unique
    private ModifyIconButton gTLCore$dividingFive;

    public ProcessingEncodingPanelMixin(PatternEncodingTermScreen<?> screen, WidgetContainer widgets) {
        super(screen, widgets);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(PatternEncodingTermScreen<?> screen, WidgetContainer widgets, CallbackInfo ci) {
        gTLCore$multipleTow = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).gTLCore$modifyPatter(2), ModifyIcon.MULTIPLY_2,
                Component.translatable("gtocore.pattern.multiply", 2),
                Component.translatable("gtocore.pattern.tooltip.multiply", 2));

        gTLCore$multipleThree = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).gTLCore$modifyPatter(3), ModifyIcon.MULTIPLY_3,
                Component.translatable("gtocore.pattern.multiply", 3),
                Component.translatable("gtocore.pattern.tooltip.multiply", 3));

        gTLCore$multipleFive = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).gTLCore$modifyPatter(5), ModifyIcon.MULTIPLY_5,
                Component.translatable("gtocore.pattern.multiply", 5),
                Component.translatable("gtocore.pattern.tooltip.multiply", 5));

        gTLCore$dividingTow = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).gTLCore$modifyPatter(-2), ModifyIcon.DIVISION_2,
                Component.translatable("gtocore.pattern.divide", 2),
                Component.translatable("gtocore.pattern.tooltip.divide", 2));

        gTLCore$dividingThree = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).gTLCore$modifyPatter(-3), ModifyIcon.DIVISION_3,
                Component.translatable("gtocore.pattern.divide", 3),
                Component.translatable("gtocore.pattern.tooltip.divide", 3));

        gTLCore$dividingFive = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).gTLCore$modifyPatter(-5), ModifyIcon.DIVISION_5,
                Component.translatable("gtocore.pattern.divide", 5),
                Component.translatable("gtocore.pattern.tooltip.divide", 5));

        widgets.add("modify1", gTLCore$multipleTow);
        widgets.add("modify2", gTLCore$multipleThree);
        widgets.add("modify3", gTLCore$multipleFive);
        widgets.add("modify4", gTLCore$dividingTow);
        widgets.add("modify5", gTLCore$dividingThree);
        widgets.add("modify6", gTLCore$dividingFive);
    }

    @Inject(method = "setVisible", at = @At("TAIL"), remap = false)
    public void setVisibleHooks(boolean visible, CallbackInfo ci) {
        this.gTLCore$multipleTow.setVisibility(visible);
        this.gTLCore$multipleThree.setVisibility(visible);
        this.gTLCore$multipleFive.setVisibility(visible);
        this.gTLCore$dividingTow.setVisibility(visible);
        this.gTLCore$dividingThree.setVisibility(visible);
        this.gTLCore$dividingFive.setVisibility(visible);
    }
}

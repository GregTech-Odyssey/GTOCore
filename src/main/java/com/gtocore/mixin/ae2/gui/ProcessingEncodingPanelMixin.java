package com.gtocore.mixin.ae2.gui;

import com.gtocore.api.ae2.gui.TinyTextButton;

import com.gtolib.api.ae2.IPatterEncodingTermMenu;
import com.gtolib.api.ae2.ModifyIcon;
import com.gtolib.api.ae2.ModifyIconButton;

import net.minecraft.network.chat.Component;

import appeng.client.gui.WidgetContainer;
import appeng.client.gui.me.items.EncodingModePanel;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.client.gui.me.items.ProcessingEncodingPanel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;

@Mixin(ProcessingEncodingPanel.class)
public abstract class ProcessingEncodingPanelMixin extends EncodingModePanel {

    @Unique
    private TinyTextButton gtolib$multipleTow;
    @Unique
    private TinyTextButton gtolib$multipleThree;
    @Unique
    private TinyTextButton gtolib$multipleFive;
    @Unique
    private TinyTextButton gtolib$dividingTow;
    @Unique
    private TinyTextButton gtolib$dividingThree;
    @Unique
    private TinyTextButton gtolib$dividingFive;
    @Unique
    private ModifyIconButton gtolib$clearSecOutput;
    @Unique
    private ModifyIconButton gtolib$recipeInfo;

    protected ProcessingEncodingPanelMixin(PatternEncodingTermScreen<?> screen, WidgetContainer widgets) {
        super(screen, widgets);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(PatternEncodingTermScreen<?> screen, WidgetContainer widgets, CallbackInfo ci) {
        gtolib$multipleTow = new TinyTextButton(Component.literal("×2"), b -> ((IPatterEncodingTermMenu) menu).gtolib$modifyPatter(2),
                Component.translatable("gtocore.pattern.multiply", 2),
                Component.translatable("gtocore.pattern.tooltip.multiply", 2));

        gtolib$multipleThree = new TinyTextButton(Component.literal("×3"), b -> ((IPatterEncodingTermMenu) menu).gtolib$modifyPatter(3),
                Component.translatable("gtocore.pattern.multiply", 3),
                Component.translatable("gtocore.pattern.tooltip.multiply", 3));

        gtolib$multipleFive = new TinyTextButton(Component.literal("×5"), b -> ((IPatterEncodingTermMenu) menu).gtolib$modifyPatter(5),
                Component.translatable("gtocore.pattern.multiply", 5),
                Component.translatable("gtocore.pattern.tooltip.multiply", 5));

        gtolib$dividingTow = new TinyTextButton(Component.literal("÷2"), b -> ((IPatterEncodingTermMenu) menu).gtolib$modifyPatter(-2),
                Component.translatable("gtocore.pattern.divide", 2),
                Component.translatable("gtocore.pattern.tooltip.divide", 2));

        gtolib$dividingThree = new TinyTextButton(Component.literal("÷3"), b -> ((IPatterEncodingTermMenu) menu).gtolib$modifyPatter(-3),
                Component.translatable("gtocore.pattern.divide", 3),
                Component.translatable("gtocore.pattern.tooltip.divide", 3));

        gtolib$dividingFive = new TinyTextButton(Component.literal("÷5"), b -> ((IPatterEncodingTermMenu) menu).gtolib$modifyPatter(-5),
                Component.translatable("gtocore.pattern.divide", 5),
                Component.translatable("gtocore.pattern.tooltip.divide", 5));

        gtolib$clearSecOutput = new ModifyIconButton(b -> ((IPatterEncodingTermMenu) menu).gtolib$clearSecOutput(),
                ModifyIcon.CLEAR_SEC_OUTPUT,
                Component.translatable("gtocore.pattern.clearSecOutput"),
                Component.translatable("gtocore.pattern.tooltip.clearSecOutput"));

        gtolib$recipeInfo = new ModifyIconButton(b -> ((IPatterEncodingTermMenu) menu).gtolib$clickRecipeInfo(),
                ModifyIcon.RECORD_RECIPE_INFO,
                Component.empty(),
                Component.empty()) {

            @Override
            public @NotNull List<Component> getTooltipMessage() {
                return Collections.singletonList(((IPatterEncodingTermMenu) menu).gtolib$getRecipeInfoTooltip());
            }
        };

        // TODO new button recipe info

        widgets.add("modify1", gtolib$multipleTow);
        widgets.add("modify2", gtolib$multipleThree);
        widgets.add("modify3", gtolib$multipleFive);
        widgets.add("modify4", gtolib$dividingTow);
        widgets.add("modify5", gtolib$dividingThree);
        widgets.add("modify6", gtolib$dividingFive);
        widgets.add("clearSecOutput", gtolib$clearSecOutput);
        widgets.add("recipeInfo", gtolib$recipeInfo);
    }

    @Inject(method = "setVisible", at = @At("TAIL"), remap = false)
    private void setVisibleHooks(boolean visible, CallbackInfo ci) {
        gtolib$multipleTow.setVisibility(visible);
        gtolib$multipleThree.setVisibility(visible);
        gtolib$multipleFive.setVisibility(visible);
        gtolib$dividingTow.setVisibility(visible);
        gtolib$dividingThree.setVisibility(visible);
        gtolib$dividingFive.setVisibility(visible);
        gtolib$clearSecOutput.setVisibility(visible);
        gtolib$recipeInfo.setVisibility(visible);
    }
}

package com.gtocore.mixin.ae2.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.WidgetContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

/**
 * AEBaseScreen主要做的事情：
 * 1. 初始化
 * -
 */
@Mixin(AEBaseScreen.class)
public interface AEBaseScreenAccessor {

    @Invoker(value = "addToLeftToolbar", remap = false)
    <B extends Button> B gtolib$addToLeftToolbar(B button);

    @Accessor(value = "widgets", remap = false)
    WidgetContainer gtolib$getWidgets();

    @Invoker(value = "drawTooltip", remap = false)
    void gtolib$drawTooltip(GuiGraphics guiGraphics, int x, int y, List<Component> lines);

    @Invoker(value = "switchToScreen", remap = false)
    void gtolib$switchToScreen(AEBaseScreen<?> screen);
}

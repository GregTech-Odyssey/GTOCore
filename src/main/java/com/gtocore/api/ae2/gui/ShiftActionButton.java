package com.gtocore.api.ae2.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ShiftActionButton extends Button {

    private final Component normalText;
    private final Component shiftText;
    private final OnPress normalPress;
    private final OnPress shiftPress;

    public ShiftActionButton(int x, int y, int width, int height,
                             Component normalText, Component shiftText,
                             OnPress normalPress, OnPress shiftPress) {
        super(x, y, width, height, normalText, normalPress, DEFAULT_NARRATION);
        this.normalText = normalText;
        this.shiftText = shiftText;
        this.normalPress = normalPress;
        this.shiftPress = shiftPress;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.setMessage(Screen.hasShiftDown() ? shiftText : normalText);
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onPress() {
        if (Screen.hasShiftDown()) {
            this.shiftPress.onPress(this);
        } else {
            this.normalPress.onPress(this);
        }
    }
}

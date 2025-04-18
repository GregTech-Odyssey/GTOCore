package com.gto.gtocore.mixin.lowdraglib;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(ButtonWidget.class)
public abstract class ButtonWidgetMixin extends Widget {

    @Shadow
    protected boolean isClicked;

    @Shadow
    protected Consumer<ClickData> onPressCallback;

    public ButtonWidgetMixin(Position selfPosition, Size size) {
        super(selfPosition, size);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY)) {
            isClicked = true;
            ClickData clickData = new ClickData();
            writeClientAction(1, clickData::writeToBuf);
            if (onPressCallback != null) {
                onPressCallback.accept(clickData);
            }
            playButtonClickSound();
            return true;
        }
        return false;
    }
}

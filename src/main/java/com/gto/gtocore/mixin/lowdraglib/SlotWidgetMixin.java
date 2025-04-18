package com.gto.gtocore.mixin.lowdraglib;

import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.EmiStackInteraction;
import dev.emi.emi.screen.EmiScreenManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SlotWidget.EMICallWrapper.class)
public abstract class SlotWidgetMixin {

    /**
     * @author .
     * @reason fix bug
     */
    @Overwrite
    public static boolean keyPressed(Object ingredient, int keyCode, int scanCode, int modifiers) {
        if (ingredient instanceof EmiStack emiStack) {
            return EmiScreenManager.stackInteraction(new EmiStackInteraction(emiStack), (bind) -> bind.matchesKey(keyCode, scanCode));
        }
        return false;
    }
}

package com.gto.gtocore.mixin.lowdraglib;

import com.gto.gtocore.client.gui.PatternPreview;

import net.minecraft.client.gui.components.events.ContainerEventHandler;

import com.lowdragmc.lowdraglib.emi.ModularWrapperWidget;
import com.lowdragmc.lowdraglib.jei.ModularWrapper;
import dev.emi.emi.api.widget.Widget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author EasterFG on 2025/2/28
 */
@Mixin(ModularWrapperWidget.class)
public abstract class ModularWrapperWidgetMixin extends Widget implements ContainerEventHandler {

    @Shadow(remap = false)
    @Final
    public ModularWrapper<?> modular;

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        ModularWrapper<?> wrapper = modular;
        if (wrapper.getWidget() instanceof PatternPreview patternPreview) {
            patternPreview.keyPressed(keyCode, scanCode, modifiers);
            return true;
        }
        return false;
    }
}

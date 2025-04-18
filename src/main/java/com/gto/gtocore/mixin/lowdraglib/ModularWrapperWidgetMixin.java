package com.gto.gtocore.mixin.lowdraglib;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.emi.ModularWrapperWidget;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.jei.ModularWrapper;
import dev.emi.emi.api.widget.Widget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(ModularWrapperWidget.class)
public abstract class ModularWrapperWidgetMixin extends Widget implements ContainerEventHandler {

    @Shadow
    @Final
    public ModularWrapper<?> modular;

    @Override
    public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
        if (modular.tooltipTexts != null && !modular.tooltipTexts.isEmpty()) {
            List<ClientTooltipComponent> tooltips = modular.tooltipTexts.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).collect(Collectors.toList());
            if (modular.tooltipComponent != null) {
                tooltips.add(DrawerHelper.getClientTooltipComponent(modular.tooltipComponent));
            }
            return tooltips;
        }
        return super.getTooltip(mouseX, mouseY);
    }
}

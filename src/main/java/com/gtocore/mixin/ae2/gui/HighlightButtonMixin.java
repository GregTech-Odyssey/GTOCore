package com.gtocore.mixin.ae2.gui;

import com.gtocore.client.Message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import com.glodblock.github.extendedae.client.button.EPPButton;
import com.glodblock.github.extendedae.client.button.HighlightButton;
import com.glodblock.github.extendedae.client.gui.GuiExPatternTerminal;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = HighlightButton.class, remap = false)
public abstract class HighlightButtonMixin extends EPPButton {

    @Shadow
    private ResourceKey<Level> dim;

    @Shadow
    private BlockPos pos;

    public HighlightButtonMixin(OnPress onPress) {
        super(onPress);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        var containerScreen = Minecraft.getInstance().screen;
        if (containerScreen instanceof GuiExPatternTerminal<?> &&
                button == 1 && this.isMouseOver(mouseX, mouseY)) {
            if (dim != null && pos != null) {
                Message.NETWORK_PACK.send(buf -> buf.writeGlobalPos(GlobalPos.of(dim, pos)));
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void setTooltip(@Nullable Tooltip tooltip) {
        super.setTooltip(tooltip);
        var containerScreen = Minecraft.getInstance().screen;
        if (containerScreen instanceof GuiExPatternTerminal<?>) {
            var t = Tooltip.create(Component.translatable("gui.expatternprovider.ex_pattern_access_terminal.tooltip.03").append(Component.translatable("gtocore.ae.appeng.highlight_button.try_open_ui")));
            super.setTooltip(t);
        }
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0 || button == 1;
    }
}

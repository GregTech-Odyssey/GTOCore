package com.gto.gtocore.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

import appeng.client.gui.Icon;
import appeng.client.gui.style.Blitter;
import appeng.client.gui.widgets.ITooltip;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author EasterFG on 2024/9/13
 */
public class ModifyIconButton extends Button implements ITooltip {

    private final ModifyIcon icon;

    private final Component displayName;
    private final Component displayValue;

    public ModifyIconButton(Button.OnPress onPress, ModifyIcon icon, Component displayName, Component displayValue) {
        super(0, 0, 8, 8, Component.empty(), onPress, DEFAULT_NARRATION);
        this.icon = icon;
        this.displayName = displayName;
        this.displayValue = displayValue;
    }

    public void setVisibility(boolean vis) {
        this.visible = vis;
        this.active = vis;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        if (this.visible) {
            ModifyIcon icon = this.getIcon();
            Blitter blitter = icon.getBlitter();
            if (!this.active) {
                blitter.opacity(0.5F);
            }

            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            if (this.isFocused()) {
                guiGraphics.fill(this.getX() - 1, this.getY() - 1, this.getX() + this.width + 1, this.getY(), -1);
                guiGraphics.fill(this.getX() - 1, this.getY(), this.getX(), this.getY() + this.height, -1);
                guiGraphics.fill(this.getX() + this.width, this.getY(), this.getX() + this.width + 1, this.getY() + this.height, -1);
                guiGraphics.fill(this.getX() - 1, this.getY() + this.height, this.getX() + this.width + 1, this.getY() + this.height + 1, -1);
            }

            PoseStack pose = guiGraphics.pose();
            pose.pushPose();
            pose.translate((float) this.getX(), (float) this.getY(), 0.0F);
            pose.scale(0.5F, 0.5F, 1.0F);
            Icon.TOOLBAR_BUTTON_BACKGROUND.getBlitter().dest(0, 0).blit(guiGraphics);
            blitter.dest(0, 0).blit(guiGraphics);
            pose.popPose();
            RenderSystem.enableDepthTest();
        }
    }

    private ModifyIcon getIcon() {
        return this.icon;
    }

    @Override
    public Rect2i getTooltipArea() {
        return new Rect2i(this.getX(), this.getY(), 8, 8);
    }

    @Override
    public boolean isTooltipAreaVisible() {
        return this.visible;
    }

    @Override
    public List<Component> getTooltipMessage() {
        return Collections.singletonList(Component.empty().append(this.displayName).append("\n").append(this.displayValue));
    }
}

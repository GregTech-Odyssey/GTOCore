package com.gtocore.api.ae2.gui;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

import appeng.client.gui.Icon;
import appeng.client.gui.widgets.ITooltip;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import lombok.Setter;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TinyTextButton extends Button implements ITooltip {

    @Setter
    private List<Component> tooltips;

    public TinyTextButton(Component pMessage, OnPress pOnPress, Component... tooltips) {
        super(0, 0, 8, 8, pMessage, pOnPress, DEFAULT_NARRATION);
        this.tooltips = List.of(tooltips);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        if (isFocused()) {
            // Draw 1px border with 4 quads, don't rely on the background as it can be disabled.
            guiGraphics.fill(getX() - 1, getY() - 1, getX() + width + 1, getY(), 0xFFFFFFFF);
            guiGraphics.fill(getX() - 1, getY(), getX(), getY() + height, 0xFFFFFFFF);
            guiGraphics.fill(getX() + width, getY(), getX() + width + 1, getY() + height, 0xFFFFFFFF);
            guiGraphics.fill(getX() - 1, getY() + height, getX() + width + 1, getY() + height + 1, 0xFFFFFFFF);
        }

        var pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(getX(), getY(), 0.0F);
        pose.scale(0.5f, 0.5f, 1.f);
        Icon.TOOLBAR_BUTTON_BACKGROUND.getBlitter().dest(0, 0).blit(guiGraphics);
        pose.popPose();

        var stack = guiGraphics.pose();
        stack.pushPose();

        var font = Minecraft.getInstance().font;

        stack.scale(0.5f, 0.5f, 0.5f);
        var matrix = stack.last().pose();

        RenderSystem.disableBlend();
        float X = (getX() + 4.0f) * 2 - font.width(getMessage().getVisualOrderText()) / 2.0f;
        float Y = (getY() + 2.0f) * 2;
        MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        font.drawInBatch(getMessage(), X, Y, 0xffffff, true, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        buffer.endBatch();
        RenderSystem.enableBlend();

        stack.popPose();
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    @Override
    public List<Component> getTooltipMessage() {
        return tooltips;
    }

    @Override
    public Rect2i getTooltipArea() {
        return new Rect2i(getX(), getY(), 8, 8);
    }

    @Override
    public boolean isTooltipAreaVisible() {
        return this.visible;
    }
}

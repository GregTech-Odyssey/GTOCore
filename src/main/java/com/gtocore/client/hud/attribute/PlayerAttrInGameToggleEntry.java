package com.gtocore.client.hud.attribute;

import com.gtocore.config.GTOConfig;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

@DataGeneratorScanned
public class PlayerAttrInGameToggleEntry extends PlayerAttrEntry {

    private static final int TOGGLE_WIDTH = 30;
    private static final int TOGGLE_HEIGHT = 14;
    private static final int KNOB_SIZE = 10;

    @RegisterLanguage(en = "Hide in-game", cn = "游戏中隐藏")
    public static final String OPTION_INVISIBLE_IN_GAME = "gtocore.hud.client.attributes.option.invisible_in_game";

    public PlayerAttrInGameToggleEntry() {
        super(Component.translatable(OPTION_INVISIBLE_IN_GAME));
    }

    private static void setHiddenInGame(boolean hidden) {
        GTOConfig.set("clientAttributesHUDHideInGame", hidden, "client", "hud");
    }

    private static boolean isHiddenInGame() {
        return GTOConfig.INSTANCE.client.hud.clientAttributesHUDHideInGame;
    }

    @Override
    public boolean isEditorVisible() {
        return true;
    }

    @Override
    public boolean isPreviewVisible() {
        return false;
    }

    @Override
    public Component createPreviewLine() {
        var value = isHiddenInGame();
        return Component.empty()
                .append(getLabel())
                .append(Component.literal(": "))
                .append(Component.translatable(value ? "options.on" : "options.off")
                        .withStyle(value ? ChatFormatting.GREEN : ChatFormatting.RED));
    }

    @Override
    public void renderEditor(GuiGraphics guiGraphics, Rect2i bounds, int mouseX, int mouseY) {
        Font font = font();
        boolean value = isHiddenInGame();
        guiGraphics.drawString(font, getLabel(), bounds.getX(), bounds.getY() + 3, TEXT_COLOR, false);

        Rect2i toggleBounds = getToggleBounds(bounds);
        int background = value ? 0xFF2E8B57 : 0xFF444444;
        int knobColor = 0xFFFFFFFF;
        guiGraphics.fill(toggleBounds.getX(), toggleBounds.getY(),
                toggleBounds.getX() + toggleBounds.getWidth(),
                toggleBounds.getY() + toggleBounds.getHeight(),
                background);

        int knobX = value ? toggleBounds.getX() + toggleBounds.getWidth() - KNOB_SIZE - 2 : toggleBounds.getX() + 2;
        guiGraphics.fill(knobX, toggleBounds.getY() + 2, knobX + KNOB_SIZE, toggleBounds.getY() + 2 + KNOB_SIZE,
                knobColor);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, Rect2i bounds) {
        if (button != 0 || !contains(getToggleBounds(bounds), mouseX, mouseY)) {
            return false;
        }
        setHiddenInGame(!isHiddenInGame());
        return true;
    }

    private Rect2i getToggleBounds(Rect2i bounds) {
        int x = bounds.getX() + bounds.getWidth() - TOGGLE_WIDTH;
        int y = bounds.getY() + (bounds.getHeight() - TOGGLE_HEIGHT) / 2;
        return new Rect2i(x, y, TOGGLE_WIDTH, TOGGLE_HEIGHT);
    }
}

package com.gtocore.integration.ae.client;

import com.gtocore.integration.ae.PatternEncoderStats;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.AESubScreen;
import appeng.client.gui.Icon;
import appeng.client.gui.widgets.TabButton;
import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantics;

import java.util.ArrayList;
import java.util.List;

public final class PatternEncoderStatsScreen<C extends AEBaseMenu, P extends AEBaseScreen<C>>
                                            extends AESubScreen<C, P> {

    private static final int CONTENT_LEFT = 10;
    private static final int CONTENT_TOP = 26;
    private static final int CONTENT_WIDTH = 180;
    private static final int LINE_HEIGHT = 11;
    private static final int MAX_VISIBLE_LINES = 16;

    private final PatternEncoderStats.Stats stats;

    public PatternEncoderStatsScreen(P parent, PatternEncoderStats.Stats stats) {
        super(parent, "/screens/terminals/pattern_encoder_stats.json");
        this.stats = stats;
        addBackButton();
        setTextContent(TEXT_ID_DIALOG_TITLE, Component.translatable("gtocore.pattern_encoder_stats.title"));
    }

    @Override
    protected void init() {
        super.init();
        setSlotsHidden(SlotSemantics.TOOLBOX, true);
    }

    @Override
    public void drawFG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY) {
        super.drawFG(guiGraphics, offsetX, offsetY, mouseX, mouseY);

        List<Component> lines = stats.isEmpty() ?
                java.util.List.of(Component.translatable("gtocore.pattern_encoder_stats.empty")
                        .withStyle(ChatFormatting.GRAY)) :
                stats.lines();
        var visualLines = splitLines(lines);
        var visibleLines = visualLines;
        Component hiddenLine = null;
        if (visualLines.size() > MAX_VISIBLE_LINES) {
            int hidden = visualLines.size() - MAX_VISIBLE_LINES + 1;
            visibleLines = visualLines.subList(0, MAX_VISIBLE_LINES - 1);
            hiddenLine = Component.translatable("gtocore.pattern_encoder_stats.hidden", hidden)
                    .withStyle(ChatFormatting.GRAY);
        }

        int y = CONTENT_TOP;
        for (var line : visibleLines) {
            guiGraphics.drawString(font, line, CONTENT_LEFT, y, 0x404040, false);
            y += LINE_HEIGHT;
        }

        if (hiddenLine != null) {
            for (var line : font.split(hiddenLine, CONTENT_WIDTH)) {
                guiGraphics.drawString(font, line, CONTENT_LEFT, y, 0x404040, false);
                y += LINE_HEIGHT;
            }
        }
    }

    private List<FormattedCharSequence> splitLines(List<Component> lines) {
        var result = new ArrayList<FormattedCharSequence>();
        for (var line : lines) {
            result.addAll(font.split(line, CONTENT_WIDTH));
        }
        return result;
    }

    private void addBackButton() {
        TabButton button = new TabButton(Icon.ARROW_LEFT, getParent().getTitle(), btn -> returnToParent());
        widgets.add("back", button);
    }
}

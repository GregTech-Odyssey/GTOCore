package com.gtocore.integration.ae;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.api.config.Settings;
import appeng.api.config.TerminalStyle;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.Scrollbar;
import appeng.client.gui.widgets.SettingToggleButton;
import appeng.core.AEConfig;
import appeng.core.AppEng;

public class PatternContentAccessTerminalScreen extends AEBaseScreen<PatternContentAccessTerminalMenu> {

    private final Scrollbar scrollbar;

    private static final int GUI_WIDTH = 195;
    private static final int GUI_TOP_AND_BOTTOM_PADDING = 54;
    private static final int GUI_PADDING_X = 8;
    private static final int GUI_HEADER_HEIGHT = 17;
    private static final int GUI_FOOTER_HEIGHT = 97;

    private static final int COLUMNS = 9;

    private int lastScrollValue = -1;

    /**
     * Height of a table-row in pixels.
     */
    private static final int ROW_HEIGHT = 18;

    /**
     * Size of a slot in both x and y dimensions in pixel, most likely always the same as ROW_HEIGHT.
     */
    private static final int SLOT_SIZE = ROW_HEIGHT;
    private static final Rect2i HEADER_BBOX = new Rect2i(0, 0, GUI_WIDTH, GUI_HEADER_HEIGHT);

    private static final Rect2i ROW_INVENTORY_TOP_BBOX = new Rect2i(0, 35, GUI_WIDTH, ROW_HEIGHT);
    private static final Rect2i ROW_INVENTORY_MIDDLE_BBOX = new Rect2i(0, 71, GUI_WIDTH, ROW_HEIGHT);
    private static final Rect2i ROW_INVENTORY_BOTTOM_BBOX = new Rect2i(0, 107, GUI_WIDTH, ROW_HEIGHT);
    private static final Rect2i FOOTER_BBOX = new Rect2i(0, 125, GUI_WIDTH, GUI_FOOTER_HEIGHT);

    private int visibleRows = 0;

    public PatternContentAccessTerminalScreen(PatternContentAccessTerminalMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.imageWidth = GUI_WIDTH;
        this.scrollbar = widgets.addScrollBar("scrollbar");

        TerminalStyle terminalStyle = AEConfig.instance().getTerminalStyle();
        this.addToLeftToolbar(
                new SettingToggleButton<>(Settings.TERMINAL_STYLE, terminalStyle, this::toggleTerminalStyle));
    }

    @Override
    public void init() {
        this.visibleRows = config.getTerminalStyle().getRows(
                (this.height - GUI_HEADER_HEIGHT - GUI_FOOTER_HEIGHT - GUI_TOP_AND_BOTTOM_PADDING) / ROW_HEIGHT);

        this.imageHeight = GUI_HEADER_HEIGHT + GUI_FOOTER_HEIGHT + this.visibleRows * ROW_HEIGHT;

        super.init();

        this.resetScrollbar();
        this.positionSlots();

        this.lastScrollValue = this.scrollbar.getCurrentScroll();
    }

    @Override
    protected void updateBeforeRender() {
        super.updateBeforeRender();

        int currentScroll = this.scrollbar.getCurrentScroll();
        if (currentScroll != this.lastScrollValue) {
            this.lastScrollValue = currentScroll;
            positionSlots();
        }
    }

    private void positionSlots() {
        int scroll = scrollbar.getCurrentScroll();
        int slotIndex = 0;
        for (var slot : this.menu.TargetAEKeys) {
            int row = slotIndex / COLUMNS;
            int col = slotIndex % COLUMNS;

            int displayRow = row - scroll;

            slot.setActive(displayRow >= 0 && displayRow < this.visibleRows);
            slot.x = GUI_PADDING_X + col * SLOT_SIZE;
            slot.y = GUI_HEADER_HEIGHT + 1 + displayRow * ROW_HEIGHT;
            slotIndex++;
        }
    }

    private void reinitialize() {
        this.children().removeAll(this.renderables);
        this.renderables.clear();
        this.init();
    }

    private void toggleTerminalStyle(SettingToggleButton<TerminalStyle> btn, boolean backwards) {
        TerminalStyle next = btn.getNextValue(backwards);
        AEConfig.instance().setTerminalStyle(next);
        btn.set(next);
        this.reinitialize();
    }

    private void resetScrollbar() {
        scrollbar.setHeight(this.visibleRows * ROW_HEIGHT - 2);

        int totalRows = (this.menu.TargetAEKeys.length + COLUMNS - 1) / COLUMNS;
        scrollbar.setRange(0, totalRows - this.visibleRows, 2);
    }

    @Override
    public void drawBG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX,
                       int mouseY, float partialTicks) {
        blit(guiGraphics, offsetX, offsetY, HEADER_BBOX);
        int currentY = offsetY + GUI_HEADER_HEIGHT;

        blit(guiGraphics, offsetX, currentY + this.visibleRows * ROW_HEIGHT, FOOTER_BBOX);

        for (int i = 0; i < this.visibleRows; ++i) {
            boolean firstLine = i == 0;
            boolean lastLine = i == this.visibleRows - 1;

            Rect2i bbox = selectRowBackgroundBox(firstLine, lastLine);
            blit(guiGraphics, offsetX, currentY, bbox);

            currentY += ROW_HEIGHT;
        }
    }

    private void blit(GuiGraphics guiGraphics, int offsetX, int offsetY, Rect2i srcRect) {
        var texture = AppEng.makeId("textures/guis/patternaccessterminal.png");
        guiGraphics.blit(texture, offsetX, offsetY, srcRect.getX(), srcRect.getY(), srcRect.getWidth(),
                srcRect.getHeight());
    }

    private Rect2i selectRowBackgroundBox(boolean firstLine, boolean lastLine) {
        if (firstLine) {
            return ROW_INVENTORY_TOP_BBOX;
        } else if (lastLine) {
            return ROW_INVENTORY_BOTTOM_BBOX;
        } else {
            return ROW_INVENTORY_MIDDLE_BBOX;
        }
    }
}

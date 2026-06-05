package com.gtocore.client.hud;

import com.gtocore.config.GTOConfig;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
@DataGeneratorScanned
public class HUDPropertyGroup implements IMoveableHUD {

    @RegisterLanguage(en = "Player Realtime Attribute", cn = "玩家实时属性")
    public static final String DISPLAY_NAME = "gtocore.hud.client_property.name";

    @RegisterLanguage(en = "No adjustable attributes.", cn = "暂无可调属性")
    public static final String EMPTY_MESSAGE = "gtocore.hud.client_property.empty";

    public static final HUDPropertyGroup INSTANCE = new HUDPropertyGroup(
            Component.translatable(DISPLAY_NAME),
            ClientAdjustablePropertyCache::getEntries,
            () -> GTOConfig.INSTANCE.client.hud.clientPropertyHUDEnabled,
            enabled -> GTOConfig.set("clientPropertyHUDEnabled", enabled, "client", "hud"),
            () -> GTOConfig.INSTANCE.client.hud.clientPropertyHUDDefaultX,
            () -> GTOConfig.INSTANCE.client.hud.clientPropertyHUDDefaultY,
            value -> GTOConfig.set("clientPropertyHUDDefaultX", value, "client", "hud"),
            value -> GTOConfig.set("clientPropertyHUDDefaultY", value, "client", "hud"));

    private static final int PREVIEW_PADDING = 4;
    private static final int PREVIEW_LINE_SPACING = 2;

    private static final int EDITOR_WIDTH = 190;
    private static final int EDITOR_PADDING = 6;
    private static final int EDITOR_ENTRY_SPACING = 4;
    private static final int EDITOR_HEADER_GAP = 6;

    private final Component title;
    private final Supplier<List<HUDPropertyEntry>> entriesSupplier;
    private final BooleanSupplier enabledGetter;
    private final BooleanConsumer enabledSetter;
    private final IntSupplier relativeXGetter;
    private final IntSupplier relativeYGetter;
    private final IntConsumer relativeXSetter;
    private final IntConsumer relativeYSetter;

    private HUDPropertyEntry activeEntry;
    private boolean draggingPosition;
    private int dragStartX;
    private int dragStartY;
    private int pendingMovedX;
    private int pendingMovedY;

    public HUDPropertyGroup(Component title, Supplier<List<HUDPropertyEntry>> entriesSupplier,
                            BooleanSupplier enabledGetter, BooleanConsumer enabledSetter,
                            IntSupplier relativeXGetter, IntSupplier relativeYGetter,
                            IntConsumer relativeXSetter, IntConsumer relativeYSetter) {
        this.title = title;
        this.entriesSupplier = entriesSupplier;
        this.enabledGetter = enabledGetter;
        this.enabledSetter = enabledSetter;
        this.relativeXGetter = relativeXGetter;
        this.relativeYGetter = relativeYGetter;
        this.relativeXSetter = relativeXSetter;
        this.relativeYSetter = relativeYSetter;
    }

    @Override
    public Component getDisplayName() {
        return title;
    }

    @Override
    public boolean isEnabled() {
        return enabledGetter.getAsBoolean();
    }

    @Override
    public void setEnabled(boolean enabled) {
        enabledSetter.accept(enabled);
        if (!enabled) {
            activeEntry = null;
            draggingPosition = false;
            pendingMovedX = 0;
            pendingMovedY = 0;
        }
    }

    @Override
    public boolean isPositionDragging() {
        return draggingPosition || pendingMovedX != 0 || pendingMovedY != 0;
    }

    @Override
    public void renderGeneral(GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        List<Component> lines = collectPreviewLines();
        if (lines.isEmpty()) {
            return;
        }

        Rect2i bounds = getPreviewBounds(screenWidth, screenHeight);
        Font font = Minecraft.getInstance().font;
        int lineHeight = font.lineHeight + PREVIEW_LINE_SPACING;
        int lineY = bounds.getY() + PREVIEW_PADDING;

        guiGraphics.fill(bounds.getX(), bounds.getY(),
                bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight(), 0xC0101010);
        drawOutline(guiGraphics, bounds, 0xFFFFFFFF);

        for (Component line : lines) {
            guiGraphics.drawString(font, line, bounds.getX() + PREVIEW_PADDING, lineY, 0xFFFFFFFF, false);
            lineY += lineHeight;
        }
    }

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (!isEnabled() || mc.level == null || mc.options.renderDebug || mc.options.hideGui || isEditorActive()) {
            return;
        }
        renderGeneral(guiGraphics, partialTick, screenWidth, screenHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Rect2i bounds = getEditorBounds(getScreenWidth(), getScreenHeight());
        guiGraphics.fill(bounds.getX(), bounds.getY(),
                bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight(), 0xD0101010);
        drawOutline(guiGraphics, bounds, 0xFFFFFFFF);

        Font font = Minecraft.getInstance().font;
        int contentX = bounds.getX() + EDITOR_PADDING;
        int cursorY = bounds.getY() + EDITOR_PADDING;

        guiGraphics.drawString(font, title, contentX, cursorY, 0xFFFFFFFF, false);
        cursorY += font.lineHeight + EDITOR_HEADER_GAP;

        List<HUDPropertyEntry> entries = entriesSupplier.get();
        if (entries.isEmpty()) {
            guiGraphics.drawString(font, Component.translatable(EMPTY_MESSAGE), contentX, cursorY, 0xFFB8C2CC, false);
            return;
        }

        for (HUDPropertyEntry entry : entries) {
            if (!entry.isVisible()) {
                continue;
            }
            Rect2i entryBounds = getEntryBounds(bounds, entries, entry);
            if (entryBounds != null) {
                entry.renderEditor(guiGraphics, entryBounds, mouseX, mouseY);
            }
        }
    }

    @Override
    public Rect2i getBounds(int screenWidth, int screenHeight) {
        return isEditorActive() ? getEditorBounds(screenWidth, screenHeight) : getPreviewBounds(screenWidth, screenHeight);
    }

    @Override
    public Rect2i getPropertyAnchorBounds(int screenWidth, int screenHeight) {
        return getBounds(screenWidth, screenHeight);
    }

    @Override
    public void setTopLeftPosition(int x, int y, int screenWidth, int screenHeight) {
        Rect2i bounds = getPreviewBounds(screenWidth, screenHeight);
        int maxX = Math.max(0, screenWidth - bounds.getWidth());
        int maxY = Math.max(0, screenHeight - bounds.getHeight());
        int clampedX = Mth.clamp(x, 0, maxX);
        int clampedY = Mth.clamp(y, 0, maxY);
        int xRange = Math.max(1, screenWidth - bounds.getWidth());
        int yRange = Math.max(1, screenHeight - bounds.getHeight());
        relativeXSetter.accept((int) Mth.clamp(Math.round((clampedX * 100.0) / xRange), 0, 100));
        relativeYSetter.accept((int) Mth.clamp(Math.round((clampedY * 100.0) / yRange), 0, 100));
        pendingMovedX = 0;
        pendingMovedY = 0;
        draggingPosition = false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Rect2i bounds = getEditorBounds(getScreenWidth(), getScreenHeight());
        if (!contains(bounds, mouseX, mouseY)) {
            return false;
        }

        List<HUDPropertyEntry> entries = entriesSupplier.get();
        for (HUDPropertyEntry entry : entries) {
            if (!entry.isVisible()) {
                continue;
            }
            Rect2i entryBounds = getEntryBounds(bounds, entries, entry);
            if (entryBounds != null && contains(entryBounds, mouseX, mouseY)) {
                activeEntry = entry.mouseClicked(mouseX, mouseY, button, entryBounds) ? entry : null;
                draggingPosition = false;
                pendingMovedX = 0;
                pendingMovedY = 0;
                return true;
            }
        }

        activeEntry = null;
        draggingPosition = true;
        dragStartX = (int) mouseX;
        dragStartY = (int) mouseY;
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        Rect2i bounds = getEditorBounds(getScreenWidth(), getScreenHeight());
        List<HUDPropertyEntry> entries = entriesSupplier.get();
        if (activeEntry != null) {
            Rect2i entryBounds = getEntryBounds(bounds, entries, activeEntry);
            if (entryBounds != null && activeEntry.mouseDragged(mouseX, mouseY, button, dragX, dragY, entryBounds)) {
                return true;
            }
            if (activeEntry.isInteracting()) {
                return true;
            }
        }

        if (draggingPosition) {
            pendingMovedX = (int) (mouseX - dragStartX);
            pendingMovedY = (int) (mouseY - dragStartY);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        Rect2i bounds = getEditorBounds(getScreenWidth(), getScreenHeight());
        List<HUDPropertyEntry> entries = entriesSupplier.get();
        boolean handled = false;

        if (activeEntry != null) {
            Rect2i entryBounds = getEntryBounds(bounds, entries, activeEntry);
            handled = entryBounds != null && activeEntry.mouseReleased(mouseX, mouseY, button, entryBounds);
            activeEntry = null;
        }

        boolean moved = pendingMovedX != 0 || pendingMovedY != 0;
        if (draggingPosition || moved) {
            Rect2i baseBounds = getEditorBoundsWithoutPending(getScreenWidth(), getScreenHeight());
            setTopLeftPosition(baseBounds.getX() + pendingMovedX, baseBounds.getY() + pendingMovedY,
                    getScreenWidth(), getScreenHeight());
            handled = true;
        }

        draggingPosition = false;
        pendingMovedX = 0;
        pendingMovedY = 0;
        return handled;
    }

    private List<Component> collectPreviewLines() {
        List<Component> lines = new ArrayList<>();
        lines.add(title.copy());
        for (HUDPropertyEntry entry : entriesSupplier.get()) {
            if (entry.isVisible()) {
                lines.add(entry.createPreviewLine());
            }
        }
        return lines;
    }

    private Rect2i getPreviewBounds(int screenWidth, int screenHeight) {
        List<Component> lines = collectPreviewLines();
        if (lines.isEmpty()) {
            return new Rect2i(getBaseX(screenWidth, 0), getBaseY(screenHeight, 0), 0, 0);
        }

        Font font = Minecraft.getInstance().font;
        int width = 0;
        for (Component line : lines) {
            width = Math.max(width, font.width(line));
        }
        width += PREVIEW_PADDING * 2;
        int height = PREVIEW_PADDING * 2 + lines.size() * (font.lineHeight + PREVIEW_LINE_SPACING) - PREVIEW_LINE_SPACING;
        int x = getBaseX(screenWidth, width);
        int y = getBaseY(screenHeight, height);
        return new Rect2i(x, y, width, height);
    }

    private Rect2i getEditorBounds(int screenWidth, int screenHeight) {
        Rect2i baseBounds = getEditorBoundsWithoutPending(screenWidth, screenHeight);
        return new Rect2i(baseBounds.getX() + pendingMovedX, baseBounds.getY() + pendingMovedY,
                baseBounds.getWidth(), baseBounds.getHeight());
    }

    private Rect2i getEditorBoundsWithoutPending(int screenWidth, int screenHeight) {
        int height = getEditorHeight();
        Rect2i previewBounds = getPreviewBounds(screenWidth, screenHeight);
        return new Rect2i(previewBounds.getX(), previewBounds.getY(), EDITOR_WIDTH, height);
    }

    private int getEditorHeight() {
        Font font = Minecraft.getInstance().font;
        List<HUDPropertyEntry> entries = entriesSupplier.get();
        if (entries.isEmpty()) {
            return EDITOR_PADDING * 2 + font.lineHeight * 2 + EDITOR_HEADER_GAP;
        }

        int height = EDITOR_PADDING * 2 + font.lineHeight + EDITOR_HEADER_GAP;
        for (HUDPropertyEntry entry : entries) {
            if (!entry.isVisible()) {
                continue;
            }
            height += entry.getEditorHeight() + EDITOR_ENTRY_SPACING;
        }
        return height - EDITOR_ENTRY_SPACING + EDITOR_PADDING;
    }

    private Rect2i getEntryBounds(Rect2i panelBounds, List<HUDPropertyEntry> entries, HUDPropertyEntry targetEntry) {
        Font font = Minecraft.getInstance().font;
        int x = panelBounds.getX() + EDITOR_PADDING;
        int y = panelBounds.getY() + EDITOR_PADDING + font.lineHeight + EDITOR_HEADER_GAP;
        int width = panelBounds.getWidth() - EDITOR_PADDING * 2;

        for (HUDPropertyEntry entry : entries) {
            if (!entry.isVisible()) {
                continue;
            }
            Rect2i entryBounds = new Rect2i(x, y, width, entry.getEditorHeight());
            if (entry == targetEntry) {
                return entryBounds;
            }
            y += entry.getEditorHeight() + EDITOR_ENTRY_SPACING;
        }
        return null;
    }

    private int getBaseX(int screenWidth, int contentWidth) {
        int maxX = Math.max(0, screenWidth - contentWidth);
        return (int) (relativeXGetter.getAsInt() / 100d * maxX);
    }

    private int getBaseY(int screenHeight, int contentHeight) {
        int maxY = Math.max(0, screenHeight - contentHeight);
        return (int) (relativeYGetter.getAsInt() / 100d * maxY);
    }

    private int getScreenWidth() {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth();
    }

    private int getScreenHeight() {
        return Minecraft.getInstance().getWindow().getGuiScaledHeight();
    }

    private boolean isEditorActive() {
        Minecraft mc = Minecraft.getInstance();
        return mc.screen instanceof HUDScreen || (mc.screen != null && IMoveableHUD.activeHuds.contains(this));
    }

    private void drawOutline(GuiGraphics guiGraphics, Rect2i bounds, int color) {
        if (bounds.getWidth() <= 0 || bounds.getHeight() <= 0) {
            return;
        }
        int left = bounds.getX();
        int top = bounds.getY();
        int right = left + bounds.getWidth() - 1;
        int bottom = top + bounds.getHeight() - 1;
        guiGraphics.hLine(left, right, top, color);
        guiGraphics.hLine(left, right, bottom, color);
        guiGraphics.vLine(left, top, bottom, color);
        guiGraphics.vLine(right, top, bottom, color);
    }

    private boolean contains(Rect2i bounds, double mouseX, double mouseY) {
        return bounds.contains((int) mouseX, (int) mouseY);
    }
}

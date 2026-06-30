package com.gtocore.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class HUDScreen extends Screen {

    private static final int BUTTON_SIZE = 20;
    private static final int BUTTON_MARGIN = 6;
    private static final int DROPDOWN_GAP = 4;
    private static final int DROPDOWN_ENTRY_HEIGHT = 18;
    private static final int DROPDOWN_TEXT_PADDING = 6;
    private static final int DROPDOWN_MIN_WIDTH = 110;

    private IMoveableHUD activeHud;
    private IMoveableHUD selectedHud;
    private HudAnchor activeHudOrigin;
    private int activeMouseButton = -1;
    private boolean dropdownOpen = false;

    public HUDScreen() {
        super(Component.literal("HUD Editor"));
    }

    @Override
    protected void init() {
        super.init();
        ensureSelectedHud();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        List<IMoveableHUD> hiddenHuds = getHiddenHuds();
        if (hiddenHuds.isEmpty()) {
            dropdownOpen = false;
        }

        for (IMoveableHUD hud : getEnabledHuds()) {
            hud.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        if (selectedHud != null && selectedHud.isEnabled()) {
            IMoveableHUD.drawOutline(guiGraphics, selectedHud.getPropertyAnchorBounds(width, height), 0xFF7FDBFF);
        }

        renderCornerButton(guiGraphics, mouseX, mouseY, hiddenHuds);
        if (dropdownOpen) {
            renderHiddenHudDropdown(guiGraphics, mouseX, mouseY, hiddenHuds);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        List<IMoveableHUD> hiddenHuds = getHiddenHuds();
        Rect2i buttonBounds = getCornerButtonBounds();

        if (!isPositionDragActive() && button == 0 && dropdownOpen) {
            IMoveableHUD hiddenHud = getHiddenHudAt(mouseX, mouseY, hiddenHuds);
            if (hiddenHud != null) {
                dropdownOpen = false;
                hiddenHud.setEnabled(true);
                hiddenHud.setTopLeftPosition((int) mouseX, (int) mouseY, width, height);
                setSelectedHud(hiddenHud);
                return beginHudInteraction(hiddenHud, mouseX, mouseY, button);
            }
        }

        if (button == 0 && contains(buttonBounds, mouseX, mouseY)) {
            if (isPositionDragActive()) {
                return true;
            }
            dropdownOpen = !hiddenHuds.isEmpty() && !dropdownOpen;
            return true;
        }

        if (dropdownOpen && !contains(getDropdownBounds(hiddenHuds), mouseX, mouseY)) {
            dropdownOpen = false;
        }

        List<IMoveableHUD> enabledHuds = getEnabledHuds();
        for (int i = enabledHuds.size() - 1; i >= 0; i--) {
            IMoveableHUD hud = enabledHuds.get(i);
            if (hud.mouseClicked(mouseX, mouseY, button)) {
                dropdownOpen = false;
                setSelectedHud(hud);
                return captureHudInteraction(hud, button);
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (activeHud != null) {
            boolean handled = activeHud.mouseDragged(mouseX, mouseY, button, dragX, dragY);
            if (handled || activeHud.isPositionDragging()) {
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (activeHud != null) {
            IMoveableHUD hud = activeHud;
            HudAnchor origin = activeHudOrigin;
            boolean shouldHide = hud.isPositionDragging() && contains(getCornerButtonBounds(), mouseX, mouseY);
            hud.mouseReleased(mouseX, mouseY, activeMouseButton >= 0 ? activeMouseButton : button);
            if (shouldHide && origin != null) {
                hud.setTopLeftPosition(origin.x(), origin.y(), width, height);
                hud.setEnabled(false);
            }
            clearHudInteraction();
            ensureSelectedHud();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void removed() {
        releaseActiveHudAtCursor();
        dropdownOpen = false;
        super.removed();
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics) {}

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private boolean beginHudInteraction(IMoveableHUD hud, double mouseX, double mouseY, int button) {
        if (!hud.mouseClicked(mouseX, mouseY, button)) {
            return false;
        }
        return captureHudInteraction(hud, button);
    }

    private boolean captureHudInteraction(IMoveableHUD hud, int button) {
        Rect2i bounds = hud.getBounds(width, height);
        activeHud = hud;
        activeHudOrigin = new HudAnchor(bounds.getX(), bounds.getY());
        activeMouseButton = button;
        return true;
    }

    private void clearHudInteraction() {
        activeHud = null;
        activeHudOrigin = null;
        activeMouseButton = -1;
    }

    private void releaseActiveHudAtCursor() {
        if (activeHud == null) {
            return;
        }
        activeHud.mouseReleased(getGuiMouseX(), getGuiMouseY(), Math.max(activeMouseButton, 0));
        clearHudInteraction();
    }

    private void renderCornerButton(GuiGraphics guiGraphics, int mouseX, int mouseY, List<IMoveableHUD> hiddenHuds) {
        Rect2i buttonBounds = getCornerButtonBounds();
        boolean positionDragging = isPositionDragActive();
        boolean enabled = positionDragging || !hiddenHuds.isEmpty();
        boolean hovered = contains(buttonBounds, mouseX, mouseY);
        int background = positionDragging ? 0xCC7A1C1C : enabled ? 0xCC1F1F1F : 0x88202020;
        int border = positionDragging ? 0xFFFFB0B0 : hovered && enabled ? 0xFFFFFFFF : 0xFF909090;
        int textColor = enabled ? 0xFFFFFFFF : 0xFF777777;
        String label = positionDragging ? "\uE21E" : "+";

        guiGraphics.fill(buttonBounds.getX(), buttonBounds.getY(),
                buttonBounds.getX() + buttonBounds.getWidth(),
                buttonBounds.getY() + buttonBounds.getHeight(),
                background);
        IMoveableHUD.drawOutline(guiGraphics, buttonBounds, border);
        guiGraphics.drawCenteredString(font, label,
                buttonBounds.getX() + buttonBounds.getWidth() / 2,
                buttonBounds.getY() + (buttonBounds.getHeight() - font.lineHeight) / 2,
                textColor);
    }

    private void renderHiddenHudDropdown(GuiGraphics guiGraphics, int mouseX, int mouseY, List<IMoveableHUD> hiddenHuds) {
        if (hiddenHuds.isEmpty()) {
            return;
        }
        Rect2i dropdownBounds = getDropdownBounds(hiddenHuds);
        guiGraphics.fill(dropdownBounds.getX(), dropdownBounds.getY(),
                dropdownBounds.getX() + dropdownBounds.getWidth(),
                dropdownBounds.getY() + dropdownBounds.getHeight(),
                0xD0101010);
        IMoveableHUD.drawOutline(guiGraphics, dropdownBounds, 0xFFFFFFFF);

        for (int i = 0; i < hiddenHuds.size(); i++) {
            Rect2i entryBounds = getDropdownEntryBounds(dropdownBounds, i);
            boolean hovered = contains(entryBounds, mouseX, mouseY);
            guiGraphics.fill(entryBounds.getX(), entryBounds.getY(),
                    entryBounds.getX() + entryBounds.getWidth(),
                    entryBounds.getY() + entryBounds.getHeight(),
                    hovered ? 0x60FFFFFF : 0x00000000);
            guiGraphics.drawString(font, hiddenHuds.get(i).getDisplayName(),
                    entryBounds.getX() + DROPDOWN_TEXT_PADDING,
                    entryBounds.getY() + (entryBounds.getHeight() - font.lineHeight) / 2,
                    0xFFFFFFFF,
                    false);
        }
    }

    private boolean isPositionDragActive() {
        return activeHud != null && activeHud.isPositionDragging();
    }

    private void ensureSelectedHud() {
        if (selectedHud != null && selectedHud.isEnabled()) {
            return;
        }
        selectedHud = getEnabledHuds().stream().findFirst().orElse(null);
    }

    private void setSelectedHud(IMoveableHUD hud) {
        selectedHud = hud;
    }

    private List<IMoveableHUD> getEnabledHuds() {
        List<IMoveableHUD> huds = new ArrayList<>();
        for (IMoveableHUD hud : IMoveableHUD.REGISTERED_HUDS.values()) {
            if (hud.isEnabled()) {
                huds.add(hud);
            }
        }
        return huds;
    }

    private List<IMoveableHUD> getHiddenHuds() {
        List<IMoveableHUD> huds = new ArrayList<>();
        for (IMoveableHUD hud : IMoveableHUD.REGISTERED_HUDS.values()) {
            if (!hud.isEnabled()) {
                huds.add(hud);
            }
        }
        return huds;
    }

    private IMoveableHUD getHiddenHudAt(double mouseX, double mouseY, List<IMoveableHUD> hiddenHuds) {
        if (hiddenHuds.isEmpty()) {
            return null;
        }
        Rect2i dropdownBounds = getDropdownBounds(hiddenHuds);
        if (!contains(dropdownBounds, mouseX, mouseY)) {
            return null;
        }
        for (int i = 0; i < hiddenHuds.size(); i++) {
            if (contains(getDropdownEntryBounds(dropdownBounds, i), mouseX, mouseY)) {
                return hiddenHuds.get(i);
            }
        }
        return null;
    }

    private Rect2i getCornerButtonBounds() {
        return new Rect2i(width - BUTTON_MARGIN - BUTTON_SIZE, BUTTON_MARGIN, BUTTON_SIZE, BUTTON_SIZE);
    }

    private Rect2i getDropdownBounds(List<IMoveableHUD> hiddenHuds) {
        Rect2i buttonBounds = getCornerButtonBounds();
        int dropdownWidth = DROPDOWN_MIN_WIDTH;
        for (IMoveableHUD hud : hiddenHuds) {
            dropdownWidth = Math.max(dropdownWidth, font.width(hud.getDisplayName()) + (DROPDOWN_TEXT_PADDING * 2));
        }
        int dropdownX = buttonBounds.getX() + buttonBounds.getWidth() - dropdownWidth;
        int dropdownY = buttonBounds.getY() + buttonBounds.getHeight() + DROPDOWN_GAP;
        return new Rect2i(dropdownX, dropdownY, dropdownWidth, hiddenHuds.size() * DROPDOWN_ENTRY_HEIGHT);
    }

    private Rect2i getDropdownEntryBounds(Rect2i dropdownBounds, int index) {
        return new Rect2i(dropdownBounds.getX(),
                dropdownBounds.getY() + (index * DROPDOWN_ENTRY_HEIGHT),
                dropdownBounds.getWidth(),
                DROPDOWN_ENTRY_HEIGHT);
    }

    private boolean contains(Rect2i bounds, double mouseX, double mouseY) {
        return bounds != null && bounds.contains((int) mouseX, (int) mouseY);
    }

    private double getGuiMouseX() {
        Minecraft mc = minecraft;
        if (mc == null || mc.getWindow().getScreenWidth() == 0) {
            return 0;
        }
        return mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth();
    }

    private double getGuiMouseY() {
        Minecraft mc = minecraft;
        if (mc == null || mc.getWindow().getScreenHeight() == 0) {
            return 0;
        }
        return mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight();
    }

    private record HudAnchor(int x, int y) {}
}

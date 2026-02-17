package com.gtocore.integration.ae.client;

import com.gtocore.integration.ae.hooks.IExtendedPatternEncodingTerm;
import com.gtocore.integration.jech.PinYinUtils;

import com.gtolib.api.ae2.gui.widgets.AEListBox;
import com.gtolib.utils.holder.IntHolder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

import appeng.api.client.AEKeyRendering;
import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.stacks.AEKey;
import appeng.client.Point;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.Tooltip;
import appeng.client.gui.widgets.AETextField;
import appeng.core.localization.GuiText;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AESearchPatternProviderListBox extends AEListBox {

    Int2ObjectMap<String> searchMap = new Int2ObjectOpenHashMap<>();
    final IntHolder maxWidth = new IntHolder(0);
    IExtendedPatternEncodingTerm term;
    AETextField searchField;
    List<SimpleItem> allItems = new ArrayList<>();

    public AESearchPatternProviderListBox(AEBaseScreen<?> screen) {
        super(screen);
        term = (IExtendedPatternEncodingTerm) screen;
        searchField = new AETextField(screen.getStyle(), Minecraft.getInstance().font, 0, -20, 100, 12) {

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button == 1 && this.isMouseOver(mouseX, mouseY)) {
                    setValue("");
                }
                return super.mouseClicked(mouseX, mouseY, button);
            }
        };
        searchField.setPlaceholder(GuiText.SearchPlaceholder.text());
        searchField.setResponder(value -> updateSearch());
    }

    @Override
    public void updateBeforeRender() {
        searchField.setX(getX() + getGuiLeft());
        searchField.setY(getY() + getGuiTop() - 14);
        searchField.setWidth(Math.max(getBounds().getWidth(), 30));
        super.updateBeforeRender();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        searchField.setVisible(b);
    }

    @Override
    public void populateScreen(Consumer<AbstractWidget> addWidget, Rect2i bounds, AEBaseScreen<?> screen) {
        super.populateScreen(addWidget, bounds, screen);
        addWidget.accept(searchField);
    }

    @Override
    public void addExclusionZones(List<Rect2i> exclusionZones, Rect2i screenBounds) {
        super.addExclusionZones(exclusionZones, screenBounds);
        exclusionZones.add(new Rect2i(getX() + getGuiLeft(), getY() - 14 + getGuiTop(), width(), 12));
    }

    public void reset() {
        maxWidth.value = 0;
        getScrollbar().setCurrentScroll(0);
        this.clearItems();
        searchMap.clear();
        allItems.clear();
        searchField.setValue("");
    }

    public void addPatternContainerGroup(PatternContainerGroup group, int index) {
        String nameStr = group.name().getString().toLowerCase();
        searchMap.put(index, nameStr);
        int nameWidth = Minecraft.getInstance().font.width(group.name());
        if (nameWidth > maxWidth.value) {
            maxWidth.value = nameWidth;
        }
        SimpleItem item = new SimpleItem(group, index);
        allItems.add(item);
        this.addItem(item);
    }

    private void updateSearch() {
        String searchText = searchField.getValue().toLowerCase();
        this.clearItems();
        for (SimpleItem item : allItems) {
            String name = searchMap.get(item.index);
            if (PinYinUtils.match(name, searchText)) {
                this.addItem(item);
                item.setVisible(true);
            } else {
                item.setVisible(false);
            }
        }
    }

    class SimpleItem implements ListItem {

        boolean visible = true;
        Point position = new Point(0, 0);
        int index;
        AEKey icon;
        Component name;

        SimpleItem(PatternContainerGroup group, int index) {
            this.index = index;
            this.icon = group.icon();
            this.name = group.name();
        }

        @Override
        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @Override
        public void setPosition(Point position) {
            this.position = position;
        }

        @Override
        public Rect2i getBounds() {
            return new Rect2i(position.getX(), position.getY(), maxWidth.value + 18, 18);
        }

        @Override
        public void drawForegroundLayer(GuiGraphics guiGraphics, Rect2i bounds, Point mouse) {
            if (visible) {
                var font = Minecraft.getInstance().font;
                var alpha = getBounds().contains(mouse.getX(), mouse.getY()) ? 0xff : 0xcc;
                guiGraphics.fill(bounds.getX(), bounds.getY(), bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight(), 0x7777aa | (alpha << 24));
                if (icon != null) {
                    AEKeyRendering.drawInGui(Minecraft.getInstance(), guiGraphics, bounds.getX() + 1, bounds.getY() + 1, icon);
                }
                guiGraphics.drawString(font, name, bounds.getX() + 18, bounds.getY() + (9 - font.lineHeight / 2), 0xFFFFFF);
            }
        }

        @Override
        public boolean onMouseUp(Point mousePos, int button) {
            if (!visible || !getBounds().contains(mousePos.getX(), mousePos.getY())) {
                return false; // Ignore clicks outside the item bounds
            }
            switch (button) {
                case 0, 1 -> term.gto$getMenu().gtolib$sendPattern(index);
                // copies the tag
                // to clipboard
                default -> {
                    return false; // Other buttons do nothing
                }
            }
            AESearchPatternProviderListBox.this.setVisible(false);
            return true; // Indicate that the click was handled
        }

        @Override
        public Tooltip getTooltip(int mouseX, int mouseY) {
            return new Tooltip(Component.translatable("gtocore.ae.appeng.craft.encode_send.desc"));
        }
    }
}

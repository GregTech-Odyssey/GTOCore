package com.gtocore.client.hud;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;

import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

@Setter
@Getter
public abstract class HUDPropertyEntry {

    protected static final int ROW_HEIGHT = 24;
    protected static final int TEXT_COLOR = 0xFFFFFFFF;
    protected static final int MUTED_TEXT_COLOR = 0xFFB8C2CC;

    private final String id;
    private final Component label;
    private boolean visibleInGame;

    protected HUDPropertyEntry(String id, Component label, boolean visibleInGame) {
        this.id = id;
        this.label = label;
        this.visibleInGame = visibleInGame;
    }

    public int getEditorHeight() {
        return ROW_HEIGHT;
    }

    public abstract Component createPreviewLine();

    public abstract void renderEditor(GuiGraphics guiGraphics, Rect2i bounds, int mouseX, int mouseY);

    public boolean mouseClicked(double mouseX, double mouseY, int button, Rect2i bounds) {
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY, Rect2i bounds) {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button, Rect2i bounds) {
        return false;
    }

    public boolean isInteracting() {
        return false;
    }

    protected Font font() {
        return Minecraft.getInstance().font;
    }

    protected static boolean contains(Rect2i bounds, double mouseX, double mouseY) {
        return bounds != null && bounds.contains((int) mouseX, (int) mouseY);
    }

    public static final class IntegerEntry extends HUDPropertyEntry {

        private static final int TRACK_HEIGHT = 4;
        private static final int TRACK_HOTSPOT_HEIGHT = 12;
        private static final int KNOB_WIDTH = 6;

        private int minValue;
        private int maxValue;
        private final IntSupplier getter;
        private final IntConsumer setter;

        private boolean sliding;
        private int previewValue;
        private boolean pendingCommit;

        public IntegerEntry(String id, Component label, IntSupplier getter,
                            IntConsumer setter, boolean visibleInGame) {
            super(id, label, visibleInGame);
            this.minValue = 0;
            this.maxValue = 0;
            this.getter = getter;
            this.setter = setter;
            this.previewValue = clampValue(getter.getAsInt());
        }

        public void setRange(int minValue, int maxValue) {
            if (maxValue < minValue) {
                throw new IllegalArgumentException("maxValue must be >= minValue");
            }
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.previewValue = clampValue(previewValue);
            this.pendingCommit = previewValue != getCommittedValue();
        }

        public Component createControlLabel(int value) {
            return Component.empty()
                    .append(getLabel().copy())
                    .append(Component.literal(": "))
                    .append(Component.literal(Integer.toString(value)));
        }

        @Override
        public Component createPreviewLine() {
            return createControlLabel(getCommittedValue());
        }

        @Override
        public void renderEditor(GuiGraphics guiGraphics, Rect2i bounds, int mouseX, int mouseY) {
            Font font = font();
            int currentValue = getDisplayedValue();
            String valueText = Integer.toString(currentValue);

            guiGraphics.drawString(font, getLabel(), bounds.getX(), bounds.getY(), TEXT_COLOR, false);
            guiGraphics.drawString(font, valueText,
                    bounds.getX() + bounds.getWidth() - font.width(valueText),
                    bounds.getY(), MUTED_TEXT_COLOR, false);

            Rect2i trackBounds = getTrackBounds(bounds);
            int fillWidth = getFillWidth(trackBounds, currentValue);
            guiGraphics.fill(trackBounds.getX(), trackBounds.getY(),
                    trackBounds.getX() + trackBounds.getWidth(),
                    trackBounds.getY() + trackBounds.getHeight(),
                    0xFF2B3640);
            guiGraphics.fill(trackBounds.getX(), trackBounds.getY(),
                    trackBounds.getX() + fillWidth,
                    trackBounds.getY() + trackBounds.getHeight(),
                    0xFF7FDBFF);

            int knobX = getKnobX(trackBounds, currentValue);
            guiGraphics.fill(knobX, trackBounds.getY() - 2, knobX + KNOB_WIDTH, trackBounds.getY() + TRACK_HEIGHT + 2,
                    sliding ? 0xFFFFFFFF : 0xFFB8D8FF);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button, Rect2i bounds) {
            if (button != 0 || !contains(getTrackHotspotBounds(bounds), mouseX, mouseY)) {
                return false;
            }
            sliding = true;
            updatePreviewValue(mouseX, bounds);
            return true;
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY, Rect2i bounds) {
            if (!sliding) {
                return false;
            }
            updatePreviewValue(mouseX, bounds);
            return true;
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button, Rect2i bounds) {
            if (!sliding) {
                return false;
            }
            updatePreviewValue(mouseX, bounds);
            if (pendingCommit) {
                setter.accept(previewValue);
            }
            previewValue = clampValue(previewValue);
            pendingCommit = false;
            sliding = false;
            return true;
        }

        @Override
        public boolean isInteracting() {
            return sliding;
        }

        private void updatePreviewValue(double mouseX, Rect2i bounds) {
            Rect2i trackBounds = getTrackBounds(bounds);
            if (trackBounds.getWidth() <= 1 || minValue == maxValue) {
                previewValue = minValue;
            } else {
                double normalized = Mth.clamp((mouseX - trackBounds.getX()) / (trackBounds.getWidth() - 1.0), 0.0, 1.0);
                previewValue = clampValue(minValue + (int) Math.round(normalized * (maxValue - minValue)));
            }
            pendingCommit = previewValue != getCommittedValue();
        }

        private int getDisplayedValue() {
            return sliding ? previewValue : getCommittedValue();
        }

        private int getCommittedValue() {
            return clampValue(getter.getAsInt());
        }

        private int clampValue(int value) {
            return Mth.clamp(value, minValue, maxValue);
        }

        private Rect2i getTrackBounds(Rect2i bounds) {
            return new Rect2i(bounds.getX(), bounds.getY() + bounds.getHeight() - TRACK_HEIGHT - 2,
                    bounds.getWidth(), TRACK_HEIGHT);
        }

        private Rect2i getTrackHotspotBounds(Rect2i bounds) {
            return new Rect2i(bounds.getX(), bounds.getY() + font().lineHeight,
                    bounds.getWidth(), TRACK_HOTSPOT_HEIGHT);
        }

        private int getFillWidth(Rect2i trackBounds, int value) {
            if (trackBounds.getWidth() <= 0) {
                return 0;
            }
            if (minValue == maxValue) {
                return trackBounds.getWidth();
            }
            double normalized = (value - minValue) / (double) (maxValue - minValue);
            return Mth.clamp((int) Math.round(normalized * trackBounds.getWidth()), 0, trackBounds.getWidth());
        }

        private int getKnobX(Rect2i trackBounds, int value) {
            if (trackBounds.getWidth() <= 0) {
                return trackBounds.getX();
            }
            int fillWidth = getFillWidth(trackBounds, value);
            return Mth.clamp(trackBounds.getX() + fillWidth - (KNOB_WIDTH / 2),
                    trackBounds.getX(), trackBounds.getX() + trackBounds.getWidth() - KNOB_WIDTH);
        }
    }

    public static final class BooleanEntry extends HUDPropertyEntry {

        private static final int TOGGLE_WIDTH = 30;
        private static final int TOGGLE_HEIGHT = 14;
        private static final int KNOB_SIZE = 10;

        private final BooleanSupplier getter;
        private final BooleanConsumer setter;

        public BooleanEntry(String id, Component label, BooleanSupplier getter, BooleanConsumer setter,
                            boolean visibleInGame) {
            super(id, label, visibleInGame);
            this.getter = getter;
            this.setter = setter;
        }

        public BooleanSupplier getter() {
            return getter;
        }

        public BooleanConsumer setter() {
            return setter;
        }

        public Component createControlLabel(boolean value) {
            return Component.empty()
                    .append(getLabel().copy())
                    .append(Component.literal(": "))
                    .append(Component.translatable(value ? "options.on" : "options.off"));
        }

        @Override
        public Component createPreviewLine() {
            return createControlLabel(getter.getAsBoolean());
        }

        @Override
        public void renderEditor(GuiGraphics guiGraphics, Rect2i bounds, int mouseX, int mouseY) {
            Font font = font();
            boolean value = getter.getAsBoolean();
            guiGraphics.drawString(font, getLabel(), bounds.getX(), bounds.getY() + 3, TEXT_COLOR, false);

            Rect2i toggleBounds = getToggleBounds(bounds);
            int background = value ? 0xFF2E8B57 : 0xFF444444;
            guiGraphics.fill(toggleBounds.getX(), toggleBounds.getY(),
                    toggleBounds.getX() + toggleBounds.getWidth(),
                    toggleBounds.getY() + toggleBounds.getHeight(),
                    background);

            int knobX = value ? toggleBounds.getX() + toggleBounds.getWidth() - KNOB_SIZE - 2 : toggleBounds.getX() + 2;
            guiGraphics.fill(knobX, toggleBounds.getY() + 2, knobX + KNOB_SIZE, toggleBounds.getY() + 2 + KNOB_SIZE,
                    0xFFFFFFFF);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button, Rect2i bounds) {
            if (button != 0 || !contains(getToggleBounds(bounds), mouseX, mouseY)) {
                return false;
            }
            setter.accept(!getter.getAsBoolean());
            return true;
        }

        private Rect2i getToggleBounds(Rect2i bounds) {
            int x = bounds.getX() + bounds.getWidth() - TOGGLE_WIDTH;
            int y = bounds.getY() + (bounds.getHeight() - TOGGLE_HEIGHT) / 2;
            return new Rect2i(x, y, TOGGLE_WIDTH, TOGGLE_HEIGHT);
        }
    }
}

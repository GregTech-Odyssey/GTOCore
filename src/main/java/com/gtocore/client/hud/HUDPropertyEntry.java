package com.gtocore.client.hud;

import com.gtolib.api.player.PlayerAttributes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import lombok.Getter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static com.gtocore.client.hud.ClientAdjustablePropertyCache.getPlayerAttributes;

@Getter
public abstract class HUDPropertyEntry {

    protected static final int ROW_HEIGHT = 24;
    protected static final int TEXT_COLOR = 0xFFFFFFFF;
    protected static final int MUTED_TEXT_COLOR = 0xFFB8C2CC;
    protected static final int DISABLED_TEXT_COLOR = 0xFF7F8790;

    private final String id;
    private final Component label;

    protected HUDPropertyEntry(String id, Component label) {
        this.id = id;
        this.label = label;
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

    protected Component createControlLabel(Component value) {
        return Component.empty()
                .append(getLabel().copy())
                .append(Component.literal(": "))
                .append(value);
    }

    protected static boolean contains(Rect2i bounds, double mouseX, double mouseY) {
        return bounds != null && bounds.contains((int) mouseX, (int) mouseY);
    }

    public boolean isVisible() {
        return true;
    }

    protected static boolean isAvailable(PlayerAttributes.NumericAttribute attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes != null && playerAttributes.getNumeric(attribute).isAvailable();
    }

    protected static boolean isAvailable(PlayerAttributes.BooleanAttribute attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes != null && playerAttributes.getBoolean(attribute).isAvailable();
    }

    protected static int getCurrentValue(PlayerAttributes.IntAttribute attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes == null ? 0 : playerAttributes.getNumericCurrentInt(attribute);
    }

    protected static float getCurrentValue(PlayerAttributes.NumericAttribute attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes == null ? 0.0F : playerAttributes.getNumericCurrentFloat(attribute);
    }

    protected static float getCurrentMin(PlayerAttributes.NumericAttribute attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes == null ? 0.0F : playerAttributes.getNumeric(attribute).getMin();
    }

    protected static float getCurrentMax(PlayerAttributes.NumericAttribute attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes == null ? 0.0F : playerAttributes.getNumeric(attribute).getMax();
    }

    protected static boolean getCurrentValue(PlayerAttributes.BooleanAttribute attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes != null && playerAttributes.getBoolean(attribute).isCurrent();
    }

    protected static void setCurrentValue(PlayerAttributes.IntAttribute attribute, int value) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        if (playerAttributes == null) {
            return;
        }
        playerAttributes.setNumericCurrent(attribute, value);
        PlayerAttributes.syncToServer(Minecraft.getInstance().player, attribute, (float) value);
    }

    protected static void setCurrentValue(PlayerAttributes.NumericAttribute attribute, float value) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        if (playerAttributes == null) {
            return;
        }
        playerAttributes.setNumericCurrent(attribute, value);
        PlayerAttributes.syncToServer(Minecraft.getInstance().player, attribute, value);
    }

    protected static void setCurrentValue(PlayerAttributes.BooleanAttribute attribute, boolean value) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        if (playerAttributes == null) {
            return;
        }
        playerAttributes.setBooleanCurrent(attribute, value);
        PlayerAttributes.syncToServer(Minecraft.getInstance().player, attribute, value);
    }

    private abstract static class SliderEntry extends HUDPropertyEntry {

        private static final int TRACK_HEIGHT = 4;
        private static final int TRACK_HOTSPOT_HEIGHT = 12;
        private static final int KNOB_WIDTH = 6;

        private boolean sliding;
        private float previewValue;
        private boolean pendingCommit;

        protected SliderEntry(String id, Component label) {
            super(id, label);
        }

        protected final void initializePreviewValue() {
            previewValue = snapValue(readValue());
        }

        @Override
        public Component createPreviewLine() {
            return createControlLabel(Component.literal(formatValue(getCommittedValue())));
        }

        @Override
        public void renderEditor(GuiGraphics guiGraphics, Rect2i bounds, int mouseX, int mouseY) {
            if (!isVisible()) {
                return;
            }
            Font font = font();
            float currentValue = getDisplayedValue();
            String valueText = formatValue(currentValue);

            guiGraphics.drawString(font, getLabel(), bounds.getX(), bounds.getY(), TEXT_COLOR, false);
            guiGraphics.drawString(font, valueText,
                    bounds.getX() + bounds.getWidth() - font.width(valueText),
                    bounds.getY(), MUTED_TEXT_COLOR, false);

            Rect2i trackBounds = getTrackBounds(bounds);
            int fillWidth = getFillWidth(trackBounds, currentValue);
            int trackColor = 0xFF2B3640;
            int fillColor = 0xFF7FDBFF;
            int knobColor = sliding ? 0xFFFFFFFF : 0xFFB8D8FF;
            guiGraphics.fill(trackBounds.getX(), trackBounds.getY(),
                    trackBounds.getX() + trackBounds.getWidth(),
                    trackBounds.getY() + trackBounds.getHeight(),
                    trackColor);
            guiGraphics.fill(trackBounds.getX(), trackBounds.getY(),
                    trackBounds.getX() + fillWidth,
                    trackBounds.getY() + trackBounds.getHeight(),
                    fillColor);

            int knobX = getKnobX(trackBounds, currentValue);
            guiGraphics.fill(knobX, trackBounds.getY() - 2, knobX + KNOB_WIDTH, trackBounds.getY() + TRACK_HEIGHT + 2,
                    knobColor);
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
                writeValue(previewValue);
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

        protected abstract float minValue();

        protected abstract float maxValue();

        protected abstract float readValue();

        protected abstract void writeValue(float value);

        protected abstract float snapValue(float value);

        protected abstract String formatValue(float value);

        protected boolean valuesEqual(float a, float b) {
            return Math.abs(a - b) < 1.0E-6F;
        }

        private void updatePreviewValue(double mouseX, Rect2i bounds) {
            Rect2i trackBounds = getTrackBounds(bounds);
            var minValue = minValue();
            var maxValue = maxValue();
            if (trackBounds.getWidth() <= 1 || valuesEqual(minValue, maxValue)) {
                previewValue = minValue;
            } else {
                float normalized = Mth.clamp((float) ((mouseX - trackBounds.getX()) / (trackBounds.getWidth() - 1.0)), 0.0F, 1.0F);
                previewValue = clampValue(minValue + normalized * (maxValue - minValue));
            }
            pendingCommit = !valuesEqual(previewValue, getCommittedValue());
        }

        private float getDisplayedValue() {
            return sliding ? previewValue : getCommittedValue();
        }

        private float getCommittedValue() {
            return clampValue(readValue());
        }

        private float clampValue(float value) {
            return snapValue(Mth.clamp(value, minValue(), maxValue()));
        }

        private Rect2i getTrackBounds(Rect2i bounds) {
            return new Rect2i(bounds.getX(), bounds.getY() + bounds.getHeight() - TRACK_HEIGHT - 2,
                    bounds.getWidth(), TRACK_HEIGHT);
        }

        private Rect2i getTrackHotspotBounds(Rect2i bounds) {
            return new Rect2i(bounds.getX(), bounds.getY() + font().lineHeight,
                    bounds.getWidth(), TRACK_HOTSPOT_HEIGHT);
        }

        private int getFillWidth(Rect2i trackBounds, float value) {
            if (trackBounds.getWidth() <= 0) {
                return 0;
            }
            var minValue = minValue();
            var maxValue = maxValue();
            if (valuesEqual(minValue, maxValue)) {
                return trackBounds.getWidth();
            }
            float normalized = (value - minValue) / (maxValue - minValue);
            return Mth.clamp((int) Math.round(normalized * trackBounds.getWidth()), 0, trackBounds.getWidth());
        }

        private int getKnobX(Rect2i trackBounds, float value) {
            if (trackBounds.getWidth() <= 0) {
                return trackBounds.getX();
            }
            int fillWidth = getFillWidth(trackBounds, value);
            return Mth.clamp(trackBounds.getX() + fillWidth - (KNOB_WIDTH / 2),
                    trackBounds.getX(), trackBounds.getX() + trackBounds.getWidth() - KNOB_WIDTH);
        }
    }

    public static final class IntegerEntry extends SliderEntry {

        private final PlayerAttributes.IntAttribute attribute;

        public IntegerEntry(String id, Component label, PlayerAttributes.IntAttribute attribute) {
            super(id, label);
            this.attribute = attribute;
            initializePreviewValue();
        }

        @Override
        public boolean isVisible() {
            return isAvailable(attribute);
        }

        public Component createControlLabel(int value) {
            return createControlLabel(Component.literal(Integer.toString(value)));
        }

        @Override
        protected float minValue() {
            return getCurrentMin(attribute);
        }

        @Override
        protected float maxValue() {
            return getCurrentMax(attribute);
        }

        @Override
        protected float readValue() {
            return getCurrentValue(attribute);
        }

        @Override
        protected void writeValue(float value) {
            setCurrentValue(attribute, Math.round(value));
        }

        @Override
        protected float snapValue(float value) {
            return Math.round(value);
        }

        @Override
        protected String formatValue(float value) {
            return Integer.toString((int) Math.round(value));
        }
    }

    public static final class FloatEntry extends SliderEntry {

        private static final DecimalFormat VALUE_FORMAT = new DecimalFormat("0.###", DecimalFormatSymbols.getInstance(Locale.ROOT));

        private final PlayerAttributes.NumericAttribute attribute;

        public FloatEntry(String id, Component label, PlayerAttributes.NumericAttribute attribute) {
            super(id, label);
            this.attribute = attribute;
            initializePreviewValue();
        }

        @Override
        public boolean isVisible() {
            return isAvailable(attribute);
        }

        public Component createControlLabel(float value) {
            return createControlLabel(Component.literal(formatValue(value)));
        }

        @Override
        protected float minValue() {
            return getCurrentMin(attribute);
        }

        @Override
        protected float maxValue() {
            return getCurrentMax(attribute);
        }

        @Override
        protected float readValue() {
            return getCurrentValue(attribute);
        }

        @Override
        protected void writeValue(float value) {
            setCurrentValue(attribute, value);
        }

        @Override
        protected float snapValue(float value) {
            return value;
        }

        @Override
        protected String formatValue(float value) {
            synchronized (VALUE_FORMAT) {
                return VALUE_FORMAT.format(value);
            }
        }
    }

    public static final class BooleanEntry extends HUDPropertyEntry {

        private static final int TOGGLE_WIDTH = 30;
        private static final int TOGGLE_HEIGHT = 14;
        private static final int KNOB_SIZE = 10;

        private final PlayerAttributes.BooleanAttribute attribute;

        public BooleanEntry(String id, Component label, PlayerAttributes.BooleanAttribute attribute) {
            super(id, label);
            this.attribute = attribute;
        }

        @Override
        public boolean isVisible() {
            return isAvailable(attribute);
        }

        public Component createControlLabel(boolean value) {
            return createControlLabel(Component.translatable(value ? "options.on" : "options.off"));
        }

        @Override
        public Component createPreviewLine() {
            return createControlLabel(getCurrentValue(attribute));
        }

        @Override
        public void renderEditor(GuiGraphics guiGraphics, Rect2i bounds, int mouseX, int mouseY) {
            if (!isVisible()) {
                return;
            }
            Font font = font();
            boolean value = getCurrentValue(attribute);
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
            setCurrentValue(attribute, !getCurrentValue(attribute));
            return true;
        }

        private Rect2i getToggleBounds(Rect2i bounds) {
            int x = bounds.getX() + bounds.getWidth() - TOGGLE_WIDTH;
            int y = bounds.getY() + (bounds.getHeight() - TOGGLE_HEIGHT) / 2;
            return new Rect2i(x, y, TOGGLE_WIDTH, TOGGLE_HEIGHT);
        }
    }
}

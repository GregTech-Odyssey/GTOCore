package com.gtocore.client.hud.attribute;

import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.api.player.attribute.*;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

@Getter
public abstract class PlayerAttrEntry {

    private static List<PlayerAttrEntry> ENTRIES;

    protected static final int ROW_HEIGHT = 24;
    protected static final int TEXT_COLOR = 0xFFFFFFFF;
    protected static final int MUTED_TEXT_COLOR = 0xFFB8C2CC;

    private final Component label;

    protected PlayerAttrEntry(Component label) {
        this.label = label;
    }

    public static List<PlayerAttrEntry> getEntries() {
        init();
        return ENTRIES;
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

    public boolean isEditorVisible() {
        return isVisible();
    }

    public boolean isPreviewVisible() {
        return isVisible();
    }

    protected Font font() {
        return Minecraft.getInstance().font;
    }

    protected static boolean contains(Rect2i bounds, double mouseX, double mouseY) {
        return bounds != null && bounds.contains((int) mouseX, (int) mouseY);
    }

    public boolean isVisible() {
        return true;
    }

    protected static boolean isAvailable(NumericAttribute<?> attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes != null && playerAttributes.get(attribute).isAvailable();
    }

    protected static boolean isAvailable(BooleanAttribute attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes != null && playerAttributes.getBoolean(attribute).isAvailable();
    }

    protected static int getCurrentValue(IntAttribute attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes == null ? 0 : playerAttributes.getNumericCurrentInt(attribute);
    }

    protected static float getCurrentValue(NumericAttribute<?> attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes == null ? 0.0F : playerAttributes.getNumericCurrentFloat(attribute);
    }

    protected static float getCurrentMin(NumericAttribute<?> attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes == null ? 0.0F : playerAttributes.getNumeric(attribute).getMin();
    }

    protected static float getCurrentMax(NumericAttribute<?> attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes == null ? 0.0F : playerAttributes.getNumeric(attribute).getMax();
    }

    protected static boolean getCurrentValue(BooleanAttribute attribute) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        return playerAttributes != null && playerAttributes.getBoolean(attribute).getCurrent();
    }

    protected static void setCurrentValue(IntAttribute attribute, int value) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        if (playerAttributes == null) {
            return;
        }
        playerAttributes.setNumericCurrent(attribute, value);
        PlayerAttributes.syncToServer(Minecraft.getInstance().player, attribute);
    }

    protected static void setCurrentValue(NumericAttribute<?> attribute, float value) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        if (playerAttributes == null) {
            return;
        }
        playerAttributes.setNumericCurrent(attribute, value);
        PlayerAttributes.syncToServer(Minecraft.getInstance().player, attribute);
    }

    protected static void setCurrentValue(BooleanAttribute attribute, boolean value) {
        PlayerAttributes playerAttributes = getPlayerAttributes();
        if (playerAttributes == null) {
            return;
        }
        playerAttributes.setBooleanCurrent(attribute, value);
        PlayerAttributes.syncToServer(Minecraft.getInstance().player, attribute);
    }

    private static PlayerAttributes getPlayerAttributes() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return null;
        }
        IEnhancedPlayer enhancedPlayer = IEnhancedPlayer.of(mc.player);
        return enhancedPlayer.getPlayerData().getPlayerAttributes();
    }

    private abstract static class SliderEntry extends PlayerAttrEntry {

        private static final int TRACK_HEIGHT = 4;
        private static final int TRACK_HOTSPOT_HEIGHT = 12;
        private static final int KNOB_WIDTH = 6;

        private boolean sliding;
        private float previewValue;
        private boolean pendingCommit;

        protected SliderEntry(Component label) {
            super(label);
        }

        protected final void initializePreviewValue() {
            previewValue = snapValue(readValue());
        }

        @Override
        public Component createPreviewLine() {
            return Component.empty()
                    .append(getLabel())
                    .append(Component.literal(": "))
                    .append(Component.literal(formatValue(getCommittedValue())).withStyle(ChatFormatting.AQUA));
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

        protected static boolean valuesEqual(float a, float b) {
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
            return Mth.clamp(Math.round(normalized * trackBounds.getWidth()), 0, trackBounds.getWidth());
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

        private final IntAttribute attribute;

        public IntegerEntry(Component label, IntAttribute attribute) {
            super(label);
            this.attribute = attribute;
            initializePreviewValue();
        }

        @Override
        public boolean isVisible() {
            return isAvailable(attribute);
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
            return Integer.toString(Math.round(value));
        }
    }

    public static final class FloatEntry extends SliderEntry {

        private static final DecimalFormat VALUE_FORMAT = new DecimalFormat("0.###", DecimalFormatSymbols.getInstance(Locale.ROOT));

        private final NumericAttribute<?> attribute;

        public FloatEntry(Component label, NumericAttribute<?> attribute) {
            super(label);
            this.attribute = attribute;
            initializePreviewValue();
        }

        @Override
        public boolean isVisible() {
            return isAvailable(attribute);
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

    public static final class BooleanEntry extends PlayerAttrEntry {

        private static final int TOGGLE_WIDTH = 30;
        private static final int TOGGLE_HEIGHT = 14;
        private static final int KNOB_SIZE = 10;

        private final BooleanAttribute attribute;

        public BooleanEntry(Component label, BooleanAttribute attribute) {
            super(label);
            this.attribute = attribute;
        }

        @Override
        public boolean isVisible() {
            return isAvailable(attribute);
        }

        @Override
        public Component createPreviewLine() {
            var value = getCurrentValue(attribute);
            return Component.empty()
                    .append(getLabel())
                    .append(Component.literal(": "))
                    .append(Component.translatable(value ? "options.on" : "options.off")
                            .withStyle(value ? ChatFormatting.GREEN : ChatFormatting.RED));
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

    private static void init() {
        if (ENTRIES != null) {
            return;
        }
        var entries = new ImmutableList.Builder<PlayerAttrEntry>();
        for (var attribute : PlayerAttributes.REGISTRY.values()) {
            PlayerAttrEntry entry = createEntry(attribute);
            if (entry != null) {
                entries.add(entry);
            }
        }
        entries.add(new PlayerAttrInGameToggleEntry());
        ENTRIES = entries.build();
    }

    private static PlayerAttrEntry createEntry(AttributeDefinition<?, ?> attribute) {
        Component label = Component.translatable(attribute.getLangKey());

        return switch (attribute) {
            case BooleanAttribute booleanAttribute -> new PlayerAttrEntry.BooleanEntry(label, booleanAttribute);
            case IntAttribute intAttribute -> new PlayerAttrEntry.IntegerEntry(label, intAttribute);
            case NumericAttribute<?> numericAttribute -> new PlayerAttrEntry.FloatEntry(label, numericAttribute);
            default -> null;
        };
    }
}

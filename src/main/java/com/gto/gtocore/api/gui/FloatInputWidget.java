package com.gto.gtocore.api.gui;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;

import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.architectury.utils.value.FloatSupplier;
import it.unimi.dsi.fastutil.floats.FloatConsumer;

public final class FloatInputWidget extends WidgetGroup {

    private static String toText(float value) {
        return String.valueOf(value);
    }

    record ChangeValues(float regular, float shift, float ctrl, float ctrlShift) {}

    private static float clamp(float value) {
        return Mth.clamp(value, min, max);
    }

    private final ChangeValues CHANGE_VALUES = new ChangeValues(1.0F, 8.0F, 64.0F, 512.0F);
    private final FloatSupplier valueSupplier;
    private static final float min = 0;
    private static final float max = Float.MAX_VALUE;
    private final FloatConsumer onChanged;
    private TextFieldWidget textField;

    public FloatInputWidget(int x, int y, int width, int height, FloatSupplier valueSupplier, FloatConsumer onChanged) {
        super(x, y, width, height);
        this.valueSupplier = valueSupplier;
        this.onChanged = onChanged;
        buildUI();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        textField.setCurrentString(toText(valueSupplier.getAsFloat()));
    }

    @Override
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        buffer.writeUtf(toText(valueSupplier.getAsFloat()));
    }

    @Override
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        textField.setCurrentString(buffer.readUtf());
    }

    private void buildUI() {
        int buttonWidth = Mth.clamp(getSize().width / 5, 15, 40);
        int textFieldWidth = getSize().width - (2 * buttonWidth) - 4;
        addWidget(new ButtonWidget(0, 0, buttonWidth, 20, new GuiTextureGroup(GuiTextures.VANILLA_BUTTON, getButtonTexture("-", buttonWidth)), this::decrease).setHoverTooltips("gui.widget.incrementButton.default_tooltip"));
        textField = new TextFieldWidget(buttonWidth + 2, 0, textFieldWidth, 20, () -> toText(valueSupplier.getAsFloat()), stringValue -> setValue(clamp(Float.parseFloat(stringValue))));
        updateTextFieldRange();
        addWidget(textField);
        addWidget(new ButtonWidget(buttonWidth + textFieldWidth + 4, 0, buttonWidth, 20, new GuiTextureGroup(GuiTextures.VANILLA_BUTTON, getButtonTexture("+", buttonWidth)), this::increase).setHoverTooltips("gui.widget.incrementButton.default_tooltip"));
    }

    private IGuiTexture getButtonTexture(String prefix, int buttonWidth) {
        var texture = new TextTexture(prefix + "1");
        if (!GTCEu.isClientSide()) {
            return texture;
        }
        int maxTextWidth = buttonWidth - 4;
        texture.setSupplier(() -> {
            float amount = GTUtil.isCtrlDown() ? GTUtil.isShiftDown() ? CHANGE_VALUES.ctrlShift : CHANGE_VALUES.ctrl : GTUtil.isShiftDown() ? CHANGE_VALUES.shift : CHANGE_VALUES.regular;
            String text = prefix + toText(amount);
            texture.scale(maxTextWidth / (float) Math.max(Minecraft.getInstance().font.width(text), maxTextWidth));
            return text;
        });
        return texture;
    }

    private void increase(ClickData cd) {
        float ONE_POSITIVE = 1.0F;
        changeValue(cd, ONE_POSITIVE);
    }

    private void decrease(ClickData cd) {
        float ONE_NEGATIVE = -1;
        changeValue(cd, ONE_NEGATIVE);
    }

    private void changeValue(ClickData cd, float multiplier) {
        if (!cd.isRemote) {
            float amount = cd.isCtrlClick ? cd.isShiftClick ? CHANGE_VALUES.ctrlShift : CHANGE_VALUES.ctrl : cd.isShiftClick ? CHANGE_VALUES.shift : CHANGE_VALUES.regular;
            setValue(clamp(valueSupplier.getAsFloat() + amount * multiplier));
        }
    }

    private FloatInputWidget setValue(float value) {
        onChanged.accept(value);
        return this;
    }

    private void updateTextFieldRange() {
        textField.setNumbersOnly(min, max).setWheelDur(0.01F);
        setValue(clamp(valueSupplier.getAsFloat()));
    }
}

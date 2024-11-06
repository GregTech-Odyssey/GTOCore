package com.gto.gtocore.api.gui;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FloatInputWidget extends WidgetGroup {

    protected String toText(float value) {
        return String.valueOf(value);
    }

    protected record ChangeValues(float regular, float shift, float ctrl, float ctrlShift) {}

    protected float clamp(float value) {
        return Mth.clamp(value, min, max);
    }

    private final ChangeValues CHANGE_VALUES = new ChangeValues(1F, 8F, 64F, 512F);
    private final Supplier<Float> valueSupplier;
    private final float min = 0;
    private final float max = Float.MAX_VALUE;
    private final Consumer<Float> onChanged;
    private TextFieldWidget textField;

    public FloatInputWidget(int x, int y, int width, int height, Supplier<Float> valueSupplier, Consumer<Float> onChanged) {
        super(x, y, width, height);
        this.valueSupplier = valueSupplier;
        this.onChanged = onChanged;
        buildUI();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        textField.setCurrentString(toText(valueSupplier.get()));
    }

    @Override
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        buffer.writeUtf(toText(valueSupplier.get()));
    }

    @Override
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        textField.setCurrentString(buffer.readUtf());
    }

    private void buildUI() {
        int buttonWidth = Mth.clamp(this.getSize().width / 5, 15, 40);
        int textFieldWidth = this.getSize().width - (2 * buttonWidth) - 4;
        this.addWidget(new ButtonWidget(0, 0, buttonWidth, 20, new GuiTextureGroup(GuiTextures.VANILLA_BUTTON, getButtonTexture("-", buttonWidth)), this::decrease).setHoverTooltips("gui.widget.incrementButton.default_tooltip"));
        this.textField = new TextFieldWidget(buttonWidth + 2, 0, textFieldWidth, 20, () -> toText(valueSupplier.get()), stringValue -> this.setValue(clamp(Float.parseFloat(stringValue))));
        this.updateTextFieldRange();
        this.addWidget(this.textField);
        this.addWidget(new ButtonWidget(buttonWidth + textFieldWidth + 4, 0, buttonWidth, 20, new GuiTextureGroup(GuiTextures.VANILLA_BUTTON, getButtonTexture("+", buttonWidth)), this::increase).setHoverTooltips("gui.widget.incrementButton.default_tooltip"));
    }

    private IGuiTexture getButtonTexture(String prefix, int buttonWidth) {
        var texture = new TextTexture(prefix + "1");
        if (!LDLib.isRemote()) {
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
        float ONE_POSITIVE = 1F;
        this.changeValue(cd, ONE_POSITIVE);
    }

    private void decrease(ClickData cd) {
        float ONE_NEGATIVE = -1;
        this.changeValue(cd, ONE_NEGATIVE);
    }

    private void changeValue(ClickData cd, float multiplier) {
        if (!cd.isRemote) {
            float amount = cd.isCtrlClick ? cd.isShiftClick ? CHANGE_VALUES.ctrlShift : CHANGE_VALUES.ctrl : cd.isShiftClick ? CHANGE_VALUES.shift : CHANGE_VALUES.regular;
            this.setValue(clamp(valueSupplier.get() + amount * multiplier));
        }
    }

    public FloatInputWidget setValue(float value) {
        onChanged.accept(value);
        return this;
    }

    protected void updateTextFieldRange() {
        textField.setNumbersOnly(min, max).setWheelDur(0.01F);
        this.setValue(clamp(valueSupplier.get()));
    }
}

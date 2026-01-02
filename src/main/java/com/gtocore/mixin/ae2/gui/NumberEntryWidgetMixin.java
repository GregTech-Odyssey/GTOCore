package com.gtocore.mixin.ae2.gui;

import com.gtocore.api.ae2.gui.ShiftActionButton;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

import appeng.client.Point;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.widgets.ConfirmableTextField;
import appeng.client.gui.widgets.NumberEntryWidget;
import appeng.client.gui.widgets.ValidationIcon;
import org.spongepowered.asm.mixin.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(value = NumberEntryWidget.class, remap = false)
public abstract class NumberEntryWidgetMixin {

    @Unique
    private static final long[] gtolib$MULT_STEP = new long[] { 2L, 3L, 5L, 10L };
    @Unique
    private static final Component gtolib$MULT = Component.literal("×"); // U+00D7
    @Unique
    private static final Component gtolib$DIV = Component.literal("÷"); // U+00F7
    @Shadow
    private static final long[] STEPS = new long[] { 1L, 10L, 100L, 1000L };
    @Shadow
    private static final Component PLUS = Component.literal("+");
    @Shadow
    private static final Component MINUS = Component.literal("−");
    @Shadow
    private Rect2i textFieldBounds;
    @Shadow
    private Point currentScreenOrigin;
    @Shadow
    private Rect2i bounds;
    @Final
    @Shadow
    private ConfirmableTextField textField;
    @Shadow
    private List<Button> buttons;
    @Shadow
    private boolean hideValidationIcon;
    @Shadow
    private ValidationIcon validationIcon;
    @Shadow
    private long minValue;
    @Shadow
    private long maxValue;

    @Shadow
    protected abstract void validate();

    @Shadow
    protected abstract Optional<BigDecimal> getValueInternal();

    @Shadow
    protected abstract void setValueInternal(BigDecimal value);

    @Shadow
    protected abstract Component makeLabel(Component operator, long step);

    @Shadow
    protected abstract void addQty(long qty);

    @Shadow
    public abstract void setTextFieldBounds(Rect2i bounds);

    @Shadow
    protected abstract BigDecimal convertToInternalValue(long externalValue);

    /**
     * @author FYWinds
     * @reason Reorganize the buttons to enhance usability
     */
    @Overwrite
    public void populateScreen(Consumer<AbstractWidget> addWidget, Rect2i bounds, AEBaseScreen<?> screen) {
        int left = bounds.getX() + this.bounds.getX();
        int top = bounds.getY() + this.bounds.getY();
        List<Button> buttons = new ArrayList(11);
        buttons.add(new ShiftActionButton(left, top, 22, 20,
                this.makeLabel(PLUS, STEPS[0]),
                this.makeLabel(gtolib$MULT, gtolib$MULT_STEP[0]),
                (btn) -> this.addQty(STEPS[0]),
                (btn) -> this.gtolib$multQty(gtolib$MULT_STEP[0])));
        buttons.add(new ShiftActionButton(left + 24, top, 28, 20,
                this.makeLabel(PLUS, STEPS[1]),
                this.makeLabel(gtolib$MULT, gtolib$MULT_STEP[1]),
                (btn) -> this.addQty(STEPS[1]),
                (btn) -> this.gtolib$multQty(gtolib$MULT_STEP[1])));
        buttons.add(new ShiftActionButton(left + 54, top, 32, 20,
                this.makeLabel(PLUS, STEPS[2]),
                this.makeLabel(gtolib$MULT, gtolib$MULT_STEP[2]),
                (btn) -> this.addQty(STEPS[2]),
                (btn) -> this.gtolib$multQty(gtolib$MULT_STEP[2])));
        buttons.add(new ShiftActionButton(left + 88, top, 38, 20,
                this.makeLabel(PLUS, STEPS[3]),
                this.makeLabel(gtolib$MULT, gtolib$MULT_STEP[3]),
                (btn) -> this.addQty(STEPS[3]),
                (btn) -> this.gtolib$multQty(gtolib$MULT_STEP[3])));

        // Ceil
        buttons.add(Button.builder(Component.literal("C"),
                (b) -> this.setValueInternal(this.getValueInternal().orElse(BigDecimal.ZERO).setScale(0, RoundingMode.CEILING)))
                .bounds(left + 128, top, 12, 20).build());

        buttons.forEach(addWidget);
        this.currentScreenOrigin = Point.fromTopLeft(bounds);
        this.setTextFieldBounds(this.textFieldBounds);
        screen.setInitialFocus(this.textField);
        addWidget.accept(this.textField);

        buttons.add(new ShiftActionButton(left, top + 42, 22, 20,
                this.makeLabel(MINUS, STEPS[0]),
                this.makeLabel(gtolib$DIV, gtolib$MULT_STEP[0]),
                (btn) -> this.addQty(-STEPS[0]),
                (btn) -> this.gtolib$multQty(1.0d / gtolib$MULT_STEP[0])));
        buttons.add(new ShiftActionButton(left + 24, top + 42, 28, 20,
                this.makeLabel(MINUS, STEPS[1]),
                this.makeLabel(gtolib$DIV, gtolib$MULT_STEP[1]),
                (btn) -> this.addQty(-STEPS[1]),
                (btn) -> this.gtolib$multQty(1.0d / gtolib$MULT_STEP[1])));
        buttons.add(new ShiftActionButton(left + 54, top + 42, 32, 20,
                this.makeLabel(MINUS, STEPS[2]),
                this.makeLabel(gtolib$DIV, gtolib$MULT_STEP[2]),
                (btn) -> this.addQty(-STEPS[2]),
                (btn) -> this.gtolib$multQty(1.0d / gtolib$MULT_STEP[2])));
        buttons.add(new ShiftActionButton(left + 88, top + 42, 38, 20,
                this.makeLabel(MINUS, STEPS[3]),
                this.makeLabel(gtolib$DIV, gtolib$MULT_STEP[3]),
                (btn) -> this.addQty(-STEPS[3]),
                (btn) -> this.gtolib$multQty(1.0d / gtolib$MULT_STEP[3])));

        if (!this.hideValidationIcon) {
            this.validationIcon = new ValidationIcon();
            this.validationIcon.setX(left + 104);
            this.validationIcon.setY(top + 27);
            buttons.add(this.validationIcon);
        }

        // Floor
        buttons.add(Button.builder(Component.literal("F"),
                (b) -> this.setValueInternal(this.getValueInternal().orElse(BigDecimal.ZERO).setScale(0, RoundingMode.FLOOR)))
                .bounds(left + 128, top + 42, 12, 20).build());

        buttons.subList(4, buttons.size()).forEach(addWidget);
        this.buttons = buttons;
        this.validate();
    }

    @Unique
    private void gtolib$multQty(double qty) {
        BigDecimal currentValue = this.getValueInternal().orElse(BigDecimal.ZERO);
        BigDecimal newValue = currentValue.multiply(BigDecimal.valueOf(qty));
        BigDecimal minimum = this.convertToInternalValue(this.minValue).setScale(0, RoundingMode.CEILING);
        BigDecimal maximum = this.convertToInternalValue(this.maxValue).setScale(0, RoundingMode.FLOOR);
        if (newValue.compareTo(minimum) < 0) {
            newValue = minimum;
        } else if (newValue.compareTo(maximum) > 0) {
            newValue = maximum;
        }

        this.setValueInternal(newValue);
    }
}

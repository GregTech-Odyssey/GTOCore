package com.gtocore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.FloatInputWidget;
import com.gregtechceu.gtceu.api.gui.widget.ToggleButtonWidget;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.data.lang.LangHandler;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;

import com.lowdragmc.lowdraglib.gui.widget.TextBoxWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SensorPartMachine extends MultiblockPartMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(SensorPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);
    @Persisted
    @DescSynced
    private float min;
    @Persisted
    @DescSynced
    private float max;
    @Persisted
    private boolean isInverted;
    @Persisted
    private int redstoneSignalOutput;

    public SensorPartMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(Position.ORIGIN, new Size(176, 112));
        group.addWidget(new TextBoxWidget(35, 28, 65, List.of(LocalizationUtils.format("cover.advanced_energy_detector.min") + ":")));
        group.addWidget(new TextBoxWidget(35, 74, 65, List.of(LocalizationUtils.format("cover.advanced_energy_detector.max") + ":")));
        group.addWidget(new FloatInputWidget(80, 26, 85, 18, this::getMin, this::setMin));
        group.addWidget(new FloatInputWidget(80, 72, 85, 18, this::getMax, this::setMax));
        group.addWidget(new MyToggleButtonWidget());
        return group;
    }

    public void update(float a) {
        int output = computeRedstoneBetweenValues(a, max, min, isInverted);
        if (redstoneSignalOutput != output) {
            redstoneSignalOutput = output;
            updateSignal();
        }
    }

    private static int computeRedstoneBetweenValues(float value, float maxValue, float minValue, boolean isInverted) {
        if (value < minValue) {
            return isInverted ? 15 : 0;
        }
        if (value > maxValue) {
            return isInverted ? 15 : 0;
        }
        return Math.round(15 * (isInverted ? (maxValue - value) : (value - minValue)) / (maxValue - minValue));
    }

    @Override
    public int getOutputSignal(@Nullable Direction side) {
        if (side == getFrontFacing().getOpposite()) {
            return redstoneSignalOutput;
        }
        return 0;
    }

    @Override
    public boolean canConnectRedstone(Direction side) {
        return side == getFrontFacing();
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public boolean canShared() {
        return false;
    }

    private class MyToggleButtonWidget extends ToggleButtonWidget {

        MyToggleButtonWidget() {
            super(8, 8, 20, 20, GuiTextures.INVERT_REDSTONE_BUTTON, SensorPartMachine.this::isInverted, SensorPartMachine.this::setInverted);
        }

        @Override
        public void updateScreen() {
            super.updateScreen();
            setHoverTooltips(List.copyOf(LangHandler.getMultiLang("gtocore.machine.sensor.invert." + (isPressed ? "enabled" : "disabled"))));
        }
    }

    private float getMin() {
        return this.min;
    }

    private float getMax() {
        return this.max;
    }

    private boolean isInverted() {
        return this.isInverted;
    }

    private void setMin(final float min) {
        this.min = min;
    }

    private void setMax(final float max) {
        this.max = max;
    }

    private void setInverted(final boolean isInverted) {
        this.isInverted = isInverted;
    }

    public int getRedstoneSignalOutput() {
        return this.redstoneSignalOutput;
    }
}

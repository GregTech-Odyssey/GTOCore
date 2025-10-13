package com.gtocore.common.machine.multiblock.part.maintenance;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes.KEY_MULTIPLY;

@Scanned
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ACMHatchPartMachine extends TieredPartMachine implements IMaintenanceMachine {

    @DynamicInitialValue(key = "maintenance.configurable_duration.max", typeKey = KEY_MULTIPLY, simpleValue = "1.3F", normalValue = "1.2F", expertValue = "1.1F", cn = "配方处理速度调节上限 : %s 倍", cnComment = "不计超频，配方处理速度为正常速度的最高倍率", en = "Configurable Recipe Speed Multiplier Maximum : %s Multiplier", enComment = "Ignore overclocking, the recipe processing speed is the highest multiplier for normal speed")
    private static float MAX_DURATION_MULTIPLIER = 1.0F;
    @DynamicInitialValue(key = "maintenance.configurable_duration.min", typeKey = KEY_MULTIPLY, simpleValue = "0.7F", normalValue = "0.8F", expertValue = "0.9F", cn = "配方处理速度调节下限 : %s 倍", cnComment = "不计超频，配方处理速度为正常速度的最低倍率", en = "Configurable Recipe Speed Multiplier Minimum : %s Multiplier", enComment = "Ignore overclocking, the recipe processing speed is the lowest multiplier for normal speed")
    private static float MIN_DURATION_MULTIPLIER = 1.0F;
    private static final float DURATION_ACTION_AMOUNT = 0.01F;

    protected void setDurationMultiplierPercent(float durationMultiplierPercent) {
        this.durationMultiplier = durationMultiplierPercent / 100.0F;
    }

    protected float getDurationMultiplierPercent() {
        return this.durationMultiplier * 100.0F;
    }

    @Persisted
    private float durationMultiplier = 1.0F;

    public ACMHatchPartMachine(MetaMachineBlockEntity metaTileEntityId) {
        super(metaTileEntityId, 5);
    }

    @Override
    public void setTaped(boolean ignored) {}

    @Override
    public boolean isTaped() {
        return false;
    }

    @Override
    public boolean isFullAuto() {
        return true;
    }

    @Override
    public byte startProblems() {
        return NO_PROBLEMS;
    }

    @Override
    public byte getMaintenanceProblems() {
        return NO_PROBLEMS;
    }

    @Override
    public void setMaintenanceProblems(byte problems) {}

    @Override
    public int getTimeActive() {
        return 0;
    }

    @Override
    public void setTimeActive(int time) {}

    @Override
    @Nullable
    public GTRecipe modifyRecipe(GTRecipe recipe) {
        recipe.duration = Math.max(1, (int) (recipe.duration * durationMultiplier));
        return recipe;
    }

    @Override
    public boolean afterWorking(IWorkableMultiController controller) {
        return true;
    }

    @Override
    public float getTimeMultiplier() {
        var result = 1.0F;
        if (durationMultiplier < 1.0) result = -20 * durationMultiplier + 21;
        else result = -8 * durationMultiplier + 9;
        return BigDecimal.valueOf(result).setScale(2, RoundingMode.HALF_UP).floatValue();
    }

    protected void incInternalMultiplier(int multiplier) {
        float newDurationMultiplier = durationMultiplier + DURATION_ACTION_AMOUNT * multiplier;
        if (newDurationMultiplier >= MAX_DURATION_MULTIPLIER) {
            durationMultiplier = MAX_DURATION_MULTIPLIER;
            return;
        }
        durationMultiplier = newDurationMultiplier;
    }

    protected void decInternalMultiplier(int multiplier) {
        float newDurationMultiplier = durationMultiplier - DURATION_ACTION_AMOUNT * multiplier;
        if (newDurationMultiplier <= MIN_DURATION_MULTIPLIER) {
            durationMultiplier = MIN_DURATION_MULTIPLIER;
            return;
        }
        durationMultiplier = newDurationMultiplier;
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group;
        group = new WidgetGroup(0, 0, 150, 70);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 150 - 8, 70 - 8).setBackground(GuiTextures.DISPLAY).addWidget(new ComponentPanelWidget(4, 5, list -> {
            list.add(getTextWidgetText(this::getDurationMultiplier));
            var buttonText = Component.translatable("gtceu.maintenance.configurable_duration.modify");
            buttonText.append(" ");
            buttonText.append(ComponentPanelWidget.withButton(Component.literal("[-]"), "sub"));
            buttonText.append(" ");
            buttonText.append(ComponentPanelWidget.withButton(Component.literal("[+]"), "add"));
            list.add(buttonText);
        }).setMaxWidthLimit(150 - 8 - 8 - 4).clickHandler((componentData, clickData) -> {
            if (!clickData.isRemote) {
                int multiplier = clickData.isCtrlClick ? 100 : clickData.isShiftClick ? 10 : 1;
                if ("sub".equals(componentData)) {
                    decInternalMultiplier(multiplier);
                } else if ("add".equals(componentData)) {
                    incInternalMultiplier(multiplier);
                }
            }
        })));
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    protected static Component getTextWidgetText(Supplier<Float> multiplier) {
        Component tooltip;
        String format = String.format("%.2f", multiplier.get());
        if (multiplier.get() == 1.0) {
            tooltip = Component.translatable("gtceu.maintenance.configurable_" + "duration" + ".unchanged_description");
        } else {
            tooltip = Component.translatable("gtceu.maintenance.configurable_" + "duration" + ".changed_description", format);
        }
        return Component.translatable("gtceu.maintenance.configurable_" + "duration", format).setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip)));
    }

    public static float getMAX_DURATION_MULTIPLIER() {
        return ACMHatchPartMachine.MAX_DURATION_MULTIPLIER;
    }

    public static float getMIN_DURATION_MULTIPLIER() {
        return ACMHatchPartMachine.MIN_DURATION_MULTIPLIER;
    }

    @Override
    public float getDurationMultiplier() {
        return this.durationMultiplier;
    }
}

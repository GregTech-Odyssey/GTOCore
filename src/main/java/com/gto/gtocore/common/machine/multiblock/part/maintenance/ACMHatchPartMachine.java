package com.gto.gtocore.common.machine.multiblock.part.maintenance;

import com.gto.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
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
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ACMHatchPartMachine extends TieredPartMachine implements IMaintenanceMachine {

    static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ACMHatchPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);

    private static final float MAX_DURATION_MULTIPLIER = 1 + (0.1F * (4 - GTOConfig.getDifficulty()));
    private static final float MIN_DURATION_MULTIPLIER = 1 - (0.1F * (4 - GTOConfig.getDifficulty()));
    private static final float DURATION_ACTION_AMOUNT = 0.01f;

    @Persisted
    private float durationMultiplier = 1.0f;

    public ACMHatchPartMachine(IMachineBlockEntity metaTileEntityId) {
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
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public @Nullable GTRecipe modifyRecipe(GTRecipe recipe) {
        recipe.duration = Math.max(1, (int) (recipe.duration * durationMultiplier));
        return recipe;
    }

    @Override
    public float getTimeMultiplier() {
        var result = 1.0f;
        if (durationMultiplier < 1.0)
            result = -20 * durationMultiplier + 21;
        else
            result = -8 * durationMultiplier + 9;
        return BigDecimal.valueOf(result)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();
    }

    private void incInternalMultiplier(int multiplier) {
        float newDurationMultiplier = durationMultiplier + DURATION_ACTION_AMOUNT * multiplier;
        if (newDurationMultiplier >= MAX_DURATION_MULTIPLIER) {
            durationMultiplier = MAX_DURATION_MULTIPLIER;
            return;
        }
        durationMultiplier = newDurationMultiplier;
    }

    private void decInternalMultiplier(int multiplier) {
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
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 150 - 8, 70 - 8).setBackground(GuiTextures.DISPLAY)
                .addWidget(new ComponentPanelWidget(4, 5, list -> {
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

    private static Component getTextWidgetText(Supplier<Float> multiplier) {
        Component tooltip;
        if (multiplier.get() == 1.0) {
            tooltip = Component.translatable("gtceu.maintenance.configurable_" + "duration" + ".unchanged_description");
        } else {
            tooltip = Component.translatable("gtceu.maintenance.configurable_" + "duration" + ".changed_description",
                    multiplier.get());
        }
        return Component.translatable("gtceu.maintenance.configurable_" + "duration", multiplier.get())
                .setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip)));
    }
}

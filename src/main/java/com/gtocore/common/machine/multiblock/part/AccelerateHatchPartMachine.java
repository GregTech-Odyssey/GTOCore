package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.part.AmountConfigurationHatchPartMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;

@DataGeneratorScanned
public final class AccelerateHatchPartMachine extends AmountConfigurationHatchPartMachine {

    @RegisterLanguage(cn = "耗时百分比", en = "Percentage of duration")
    private static final String PERCENTAGE = "gtocore.machine.accelerate_hatch.percentage";

    public AccelerateHatchPartMachine(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier, 52 - 2L * tier, 100);
    }

    @Override
    public Widget createUIWidget() {
        return ((WidgetGroup) super.createUIWidget()).addWidget(new LabelWidget(24, -16, () -> PERCENTAGE));
    }

    @Override
    public GTRecipe modifyRecipe(GTRecipe recipe) {
        if (isFormed() && getControllers().first() instanceof WorkableElectricMultiblockMachine machine) {
            int reduction = (int) getCurrent();
            int t = machine.getTier() - getTier();
            if (t > 0) {
                reduction = Math.min(100, reduction + 20 * t);
            }
            recipe.duration = Math.max(1, recipe.duration * reduction / 100);
        }
        return recipe;
    }

    @Override
    protected long getCurrent() {
        if (current == -1) current = min;
        return current;
    }
}

package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.part.AmountConfigurationHatchPartMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;

@DataGeneratorScanned
public final class OverclockHatchPartMachine extends AmountConfigurationHatchPartMachine {

    @RegisterLanguage(cn = "超频时间除数", en = "Divisor of duration")
    private static final String DIVISOR = "gtocore.machine.overclock_hatch.divisor";

    public OverclockHatchPartMachine(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier, 2, tier - 6);
    }

    @Override
    public Widget createUIWidget() {
        return ((WidgetGroup) super.createUIWidget()).addWidget(new LabelWidget(24, -16, () -> DIVISOR));
    }

    public double getCurrentMultiplier() {
        return 1D / getCurrent();
    }
}

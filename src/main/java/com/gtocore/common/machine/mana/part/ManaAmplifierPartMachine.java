package com.gtocore.common.machine.mana.part;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.mana.feature.IManaMachine;
import com.gtolib.api.machine.mana.trait.NotifiableManaContainer;
import com.gtolib.api.machine.part.AmountConfigurationHatchPartMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;

@DataGeneratorScanned
public final class ManaAmplifierPartMachine extends AmountConfigurationHatchPartMachine implements IManaMachine {

    @RegisterLanguage(cn = "最大魔力", en = "Max Mana")
    private static final String MAX = "gtocore.machine.mana_amplifier_part.max_mana";

    @Persisted
    private final NotifiableManaContainer manaContainer;

    public ManaAmplifierPartMachine(MetaMachineBlockEntity holder) {
        super(holder, 2, 1, Long.MAX_VALUE);
        manaContainer = new ManaContainer(this);
        manaContainer.setAcceptDistributor(true);
    }

    @Override
    public Widget createUIWidget() {
        return ((WidgetGroup) super.createUIWidget()).addWidget(new LabelWidget(24, -16, () -> MAX));
    }

    @Override
    public GTRecipe modifyRecipe(GTRecipe recipe) {
        IMultiController controller = null;
        for (var c : getControllers()) {
            controller = c;
            break;
        }
        if (controller instanceof IOverclockMachine overclockMachine) {
            if (manaContainer.removeMana(overclockMachine.getOverclockVoltage(), 1, true) == overclockMachine.getOverclockVoltage()) {
                manaContainer.removeMana(overclockMachine.getOverclockVoltage(), 1, false);
                Recipe.of(recipe).perfect = true;
                return recipe;
            }
        }
        return null;
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }

    private static final class ManaContainer extends NotifiableManaContainer {

        private ManaContainer(ManaAmplifierPartMachine machine) {
            super(machine, IO.IN, Long.MAX_VALUE);
        }

        @Override
        protected long extractionRate() {
            return getMaxMana() - getCurrentMana();
        }

        @Override
        public long getMaxMana() {
            return ((ManaAmplifierPartMachine) machine).getCurrent();
        }
    }

    @Override
    public @NotNull NotifiableManaContainer getManaContainer() {
        return this.manaContainer;
    }
}

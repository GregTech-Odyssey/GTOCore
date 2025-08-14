package com.gtocore.common.machine.mana.part;

import com.gtolib.api.machine.mana.feature.IManaMachine;
import com.gtolib.api.machine.mana.trait.NotifiableManaContainer;
import com.gtolib.api.machine.part.AmountConfigurationHatchPartMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

public final class ManaAmplifierPartMachine extends AmountConfigurationHatchPartMachine implements IManaMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ManaAmplifierPartMachine.class, AmountConfigurationHatchPartMachine.MANAGED_FIELD_HOLDER);
    @Persisted
    private final NotifiableManaContainer manaContainer;

    public ManaAmplifierPartMachine(MetaMachineBlockEntity holder) {
        super(holder, 2, 1, Integer.MAX_VALUE);
        manaContainer = new ManaContainer(this);
        manaContainer.setAcceptDistributor(true);
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
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
            return Math.min(getMaxMana() - getCurrentMana(), 20L * ((ManaAmplifierPartMachine) machine).getCurrent());
        }
    }

    @Override
    public @NotNull NotifiableManaContainer getManaContainer() {
        return this.manaContainer;
    }
}

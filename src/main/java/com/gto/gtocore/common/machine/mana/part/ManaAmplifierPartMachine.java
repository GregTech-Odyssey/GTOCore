package com.gto.gtocore.common.machine.mana.part;

import com.gto.gtocore.api.machine.mana.feature.IManaMachine;
import com.gto.gtocore.api.machine.mana.trait.NotifiableManaContainer;
import com.gto.gtocore.api.machine.part.AmountConfigurationHatchPartMachine;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;
import com.gto.gtocore.api.recipe.IGTRecipe;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class ManaAmplifierPartMachine extends AmountConfigurationHatchPartMachine implements IManaMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ManaAmplifierPartMachine.class, AmountConfigurationHatchPartMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private final NotifiableManaContainer manaContainer;

    public ManaAmplifierPartMachine(IMachineBlockEntity holder) {
        super(holder, 2, 1, Integer.MAX_VALUE);
        manaContainer = new ManaContainer(this);
        manaContainer.setAcceptDistributor(true);
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public boolean afterWorking(IWorkableMultiController controller) {
        if (controller.getRecipeLogic() instanceof IEnhancedRecipeLogic recipeLogic) {
            recipeLogic.gtocore$setModifyRecipe();
        }
        return super.afterWorking(controller);
    }

    @Override
    public GTRecipe modifyRecipe(GTRecipe recipe) {
        if (recipe instanceof IGTRecipe igtRecipe) {
            IMultiController controller = null;
            for (var c : getControllers()) {
                controller = c;
                break;
            }
            if (controller instanceof IOverclockMachine overclockMachine) {
                if (manaContainer.removeMana(overclockMachine.getOverclockVoltage(), 1, true) == overclockMachine.getOverclockVoltage()) {
                    manaContainer.removeMana(overclockMachine.getOverclockVoltage(), 1, false);
                    igtRecipe.gtocore$setPerfect(true);
                    return recipe;
                }
            }
        }
        return null;
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }

    private static class ManaContainer extends NotifiableManaContainer {

        public ManaContainer(ManaAmplifierPartMachine machine) {
            super(machine, IO.IN, Long.MAX_VALUE, Integer.MAX_VALUE);
        }

        @Override
        protected long extractionRate() {
            return Math.min(getMaxMana() - getCurrentMana(), 20L * ((ManaAmplifierPartMachine) machine).getCurrent());
        }
    }
}

package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.machine.part.AmountConfigurationHatchPartMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

public final class AccelerateHatchPartMachine extends AmountConfigurationHatchPartMachine {

    public AccelerateHatchPartMachine(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier, 52 - 2L * tier, 100);
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

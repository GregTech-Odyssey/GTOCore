package com.gto.gtocore.common.machine.multiblock.part;

import com.gto.gtocore.api.machine.part.AmountConfigurationHatchPartMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

public final class AccelerateHatchPartMachine extends AmountConfigurationHatchPartMachine {

    public AccelerateHatchPartMachine(IMachineBlockEntity holder, int tier) {
        super(holder, tier, 52 - 2 * tier, 100);
    }

    @Override
    public GTRecipe modifyRecipe(GTRecipe recipe) {
        IMultiController controller = getControllers().first();
        if (controller instanceof WorkableElectricMultiblockMachine machine) {
            int reduction = getCurrent();
            int t = machine.getTier() - getTier();
            if (t > 0) {
                reduction = Math.min(100, reduction + 20 * t);
            }
            recipe.duration = recipe.duration * 100 / reduction;
        }
        return recipe;
    }
}

package com.gto.gtocore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AlloyBlastSmelterMachine extends CoilWorkableElectricMultiblockMachine {

    private double duration = 1;

    public AlloyBlastSmelterMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params,
                                          @NotNull OCResult result) {
        if (machine instanceof AlloyBlastSmelterMachine alloyBlastSmelterMachine) {
            double dm = Math.max(0.2, Math.min(1, 20 / Math.sqrt(alloyBlastSmelterMachine.getRecipeLogic().getTotalContinuousRunningTime())));
            alloyBlastSmelterMachine.duration = dm;
            GTRecipe recipe1 = recipe.copy();
            recipe1.duration = (int) (recipe.duration * dm);
            return GTRecipeModifiers.ebfOverclock(machine, recipe1, params, result);
        }
        return null;
    }

    @Override
    public void afterWorking() {
        duration = 1;
        super.afterWorking();
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        if (this.isFormed()) {
            textList.add(Component.translatable("gtocore.machine.total_time", getRecipeLogic().getTotalContinuousRunningTime()));
            textList.add(Component.translatable("gtocore.machine.alloy_blast_smelter.duration", FormattingUtil.formatNumbers(duration)));
        }
    }
}

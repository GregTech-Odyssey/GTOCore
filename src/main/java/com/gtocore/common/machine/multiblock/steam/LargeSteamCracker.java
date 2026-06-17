package com.gtocore.common.machine.multiblock.steam;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

public class LargeSteamCracker extends BaseSteamMultiblockMachine {

    public LargeSteamCracker(MetaMachineBlockEntity holder) {
        super(holder, 1, 32, 1);
    }

    @Override
    boolean oc() {
        return true;
    }

    private float getEfficiencyMultiplier() {
        return maxOCamount * 0.125f + 1.0f;
    }

    @Override
    protected @Nullable GTRecipe getRealRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe r) {
        var r1 = super.getRealRecipe(unit, r);
        if (r1 != null) {
            var content = r1.fluidOutputs.getFirst();
            r1.fluidOutputs = Collections.singletonList(new Content<>(content, (long) (content.amount * getEfficiencyMultiplier())));
            return r1;
        }
        return null;
    }
}

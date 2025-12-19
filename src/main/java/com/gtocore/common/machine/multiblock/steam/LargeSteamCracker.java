package com.gtocore.common.machine.multiblock.steam;

import com.gtolib.api.recipe.ingredient.FastFluidIngredient;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;

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
    protected @Nullable GTRecipe getRealRecipe(GTRecipe r) {
        var r1 = super.getRealRecipe(r);
        if (r1 != null) {
            r1 = r1.copy();
            var content = ((FastFluidIngredient) r1.getOutputContents(FluidRecipeCapability.CAP).getFirst().content).copy();
            content.setAmount((int) (content.getAmount() * getEfficiencyMultiplier()));
            r1.outputs.put(FluidRecipeCapability.CAP, Collections.singletonList(new Content(content, 10000, 0)));
            return r1;
        }
        return null;
    }
}

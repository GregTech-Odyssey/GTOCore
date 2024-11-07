package com.gto.gtocore.common.machine.magic;

import com.gto.gtocore.api.machine.SimpleManaMachine;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.common.data.GTMachines;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagicSynthesisMachine extends SimpleManaMachine {

    protected TickableSubscription tickSubs;

    public MagicSynthesisMachine(IMachineBlockEntity holder, int tier) {
        super(holder, tier, GTMachines.defaultTankSizeFunction);
        this.tickSubs = subscribeServerTick(tickSubs, this::tickUpdate);
    }

    private void tickUpdate() {
        getManaContainer().addMana(1);
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe == null) return false;
        List<Content> recipeInputs = recipe.inputs.get(ItemRecipeCapability.CAP);
        for (int i = 0; i < recipeInputs.size(); i++) {
            if (!ItemRecipeCapability.CAP.of(recipeInputs.get(i).content).test(importItems.getStackInSlot(i))) return false;
        }
        return super.beforeWorking(recipe);
    }
}

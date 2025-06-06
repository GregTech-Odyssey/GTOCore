package com.gto.gtocore.api.machine.trait;

import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class MultiblockTrait extends MachineTrait {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MultiblockTrait.class);

    protected MultiblockTrait(WorkableMultiblockMachine machine) {
        super(machine);
    }

    public boolean alwaysTryModifyRecipe() {
        return false;
    }

    public GTRecipe modifyRecipe(@NotNull GTRecipe recipe) {
        return recipe;
    }

    public boolean beforeWorking(@NotNull GTRecipe recipe) {
        return false;
    }

    public void customText(@NotNull List<Component> textList) {}

    public void onStructureFormed() {}

    public void onStructureInvalid() {}

    @Override
    public WorkableMultiblockMachine getMachine() {
        return (WorkableMultiblockMachine) machine;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}

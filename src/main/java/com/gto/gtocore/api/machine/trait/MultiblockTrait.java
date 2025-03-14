package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.api.machine.feature.multiblock.IMultiblockTraitHolder;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class MultiblockTrait extends MachineTrait {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MultiblockTrait.class);

    protected MultiblockTrait(IMultiblockTraitHolder machine) {
        super((MetaMachine) machine);
        machine.getMultiblockTraits().add(this);
    }

    public boolean alwaysTryModifyRecipe() {
        return false;
    }

    public GTRecipe modifyRecipe(@NotNull GTRecipe recipe) {
        return recipe;
    }

    /**
     * @return 是否取消配方
     */
    public boolean beforeWorking(@NotNull GTRecipe recipe) {
        return false;
    }

    /**
     * @return 是否中断配方
     */
    public boolean onWorking() {
        return false;
    }

    public void afterWorking() {}

    public void customText(@NotNull List<Component> textList) {}

    public void onStructureFormed() {}

    public void onStructureInvalid() {}

    @Override
    public MultiblockControllerMachine getMachine() {
        return (MultiblockControllerMachine) machine;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}

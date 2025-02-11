package com.gto.gtocore.api.machine.feature.multiblock;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.world.item.Item;

public interface IArrayMachine {

    default ElectricMultiblockMachine multiblockMachine() {
        return (ElectricMultiblockMachine) this;
    }

    default MachineDefinition getMachineDefinition() {
        if (getMachineDefinitionCache() == null && getStorageItem() instanceof MetaMachineItem metaMachineItem) {
            setMachineDefinitionCache(metaMachineItem.getDefinition());
        }
        return getMachineDefinitionCache();
    }

    default GTRecipeType[] recipeTypes() {
        if (getRecipeTypeCache() == null) {
            MachineDefinition definition = getMachineDefinition();
            setRecipeTypeCache(definition == null ? new GTRecipeType[] { GTRecipeTypes.DUMMY_RECIPES } : definition.getRecipeTypes());
        }
        return getRecipeTypeCache();
    }

    default void onStorageChanged() {
        setMachineDefinitionCache(null);
        setRecipeTypeCache(null);
        if (multiblockMachine().isFormed()) {
            if (multiblockMachine().getRecipeLogic().getLastRecipe() != null) {
                multiblockMachine().getRecipeLogic().markLastRecipeDirty();
            }
            multiblockMachine().getRecipeLogic().updateTickSubscription();
        }
    }

    Item getStorageItem();

    void setMachineDefinitionCache(MachineDefinition definition);

    MachineDefinition getMachineDefinitionCache();

    void setRecipeTypeCache(GTRecipeType[] type);

    GTRecipeType[] getRecipeTypeCache();
}

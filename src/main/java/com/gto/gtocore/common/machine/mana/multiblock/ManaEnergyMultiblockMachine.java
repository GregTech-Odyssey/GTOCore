package com.gto.gtocore.common.machine.mana.multiblock;

import com.gto.gtocore.api.gui.OverclockConfigurator;
import com.gto.gtocore.api.machine.feature.IOverclockConfigMachine;
import com.gto.gtocore.api.machine.mana.feature.IManaEnergyMachine;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;
import com.gto.gtocore.common.data.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManaEnergyMultiblockMachine extends ManaMultiblockMachine implements IManaEnergyMachine, IOverclockConfigMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ManaEnergyMultiblockMachine.class, ManaMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private int ocLimit = 20;

    public ManaEnergyMultiblockMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        addHandlerList(RecipeHandlerList.of(IO.IN, new ManaEnergyRecipeHandler(Integer.MAX_VALUE, getManaContainer())));
    }

    @Nullable
    protected GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        recipe = super.getRealRecipe(recipe);
        long eu = RecipeHelper.getInputEUt(recipe);
        if (eu > 0) {
            recipe = GTORecipeModifiers.externalEnergyOverclocking(this, recipe, eu, getManaContainer().getMaxConsumption(), true, 1, 1);
            return recipe;
        } else {
            return GTORecipeModifiers.manaOverclocking(this, recipe, getManaContainer().getMaxConsumption(), true, 1, 1);
        }
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new OverclockConfigurator(this));
    }

    @Override
    public void gTOCore$setOCLimit(int number) {
        if (number != ocLimit) {
            if (getRecipeLogic().getLastRecipe() != null && getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
                enhancedRecipeLogic.gtocore$setModifyRecipe();
            }
            ocLimit = number;
        }
    }

    @Override
    public int gTOCore$getOCLimit() {
        return ocLimit;
    }
}

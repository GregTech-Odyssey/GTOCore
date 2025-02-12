package com.gto.gtocore.common.machine.mana;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.capability.IManaContainer;
import com.gto.gtocore.api.gui.OverclockConfigurator;
import com.gto.gtocore.api.machine.SimpleNoEnergyMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IOverclockConfigMachine;
import com.gto.gtocore.api.machine.mana.feature.IManaEnergyMachine;
import com.gto.gtocore.api.machine.mana.feature.IManaMachine;
import com.gto.gtocore.api.machine.mana.trait.NotifiableManaContainer;
import com.gto.gtocore.common.data.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleManaMachine extends SimpleNoEnergyMachine implements IManaMachine, IManaEnergyMachine, IOverclockConfigMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SimpleManaMachine.class, SimpleNoEnergyMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private int ocLimit = 20;

    @Persisted
    @DescSynced
    private final NotifiableManaContainer manaContainer;

    private final int tierMana;

    public SimpleManaMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
        tierMana = GTOValues.MANA[tier];
        manaContainer = new NotifiableManaContainer(this, IO.IN, 256 * tierMana, tierMana);
        manaContainer.setAcceptDistributor(true);
        IManaEnergyMachine.addProxy(capabilitiesProxy, tierMana, manaContainer);
    }

    @Nullable
    @Override
    public GTRecipe doModifyRecipe(@NotNull GTRecipe recipe) {
        long eu = RecipeHelper.getInputEUt(recipe);
        if (eu > 0) {
            recipe = GTORecipeModifiers.externalEnergyOverclocking(this, recipe, eu, tierMana, false, 1, 1);
            return recipe;
        } else {
            return GTORecipeModifiers.manaOverclocking(this, recipe, tierMana, false, 1, 1);
        }
    }

    @Override
    public @NotNull IManaContainer getManaContainer() {
        return manaContainer;
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new OverclockConfigurator(this));
    }

    @Override
    public void gTOCore$setOCLimit(int number) {
        if (number != ocLimit) {
            if (getRecipeLogic().getLastRecipe() != null) {
                getRecipeLogic().markLastRecipeDirty();
            }
            ocLimit = number;
        }
    }

    @Override
    public int gTOCore$getOCLimit() {
        return ocLimit;
    }
}

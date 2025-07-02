package com.gtocore.common.machine.mana;

import com.gtolib.api.gui.OverclockConfigurator;
import com.gtolib.api.machine.feature.IOverclockConfigMachine;
import com.gtolib.api.machine.mana.feature.IManaEnergyMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleWorkManaMachine extends SimpleManaMachine implements IManaEnergyMachine, IOverclockConfigMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SimpleWorkManaMachine.class, SimpleManaMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private int ocLimit = 20;

    private final IEnergyContainer container;

    public SimpleWorkManaMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
        container = new ManaEnergyContainer(getManaContainer().getMaxIORate(), getManaContainer());
    }

    @Nullable
    @Override
    public Recipe doModifyRecipe(@NotNull Recipe recipe) {
        long eu = recipe.getInputEUt();
        if (eu > 0) {
            recipe = RecipeModifierFunction.externalEnergyOverclocking(this, recipe, eu, getTierMana(), true, 1, 1);
            return recipe;
        } else {
            return RecipeModifierFunction.manaOverclocking(this, recipe, getTierMana());
        }
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new OverclockConfigurator(this));
    }

    @Override
    public void gTOCore$setOCLimit(int number) {
        ocLimit = number;
    }

    @Override
    public int gTOCore$getOCLimit() {
        return ocLimit;
    }

    @Override
    public @NotNull IEnergyContainer gtocore$getEnergyContainer() {
        return container;
    }
}

package com.gtocore.mixin.gtm.machine;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.SteamEnergyContainer;
import com.gtolib.api.machine.feature.DummyEnergyMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.steam.SimpleSteamMachine;
import com.gregtechceu.gtceu.api.machine.steam.SteamWorkableMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SimpleSteamMachine.class)
public abstract class SimpleSteamMachineMixin extends SteamWorkableMachine implements DummyEnergyMachine {

    protected SimpleSteamMachineMixin(MetaMachineBlockEntity holder, boolean isHighPressure, Object... args) {
        super(holder, isHighPressure, args);
    }

    @Shadow(remap = false)
    public abstract double getConversionRate();

    @Unique
    private SteamEnergyContainer gtolib$container;

    @Override
    public @NotNull IEnergyContainer gtolib$getEnergyContainer() {
        if (gtolib$container == null) gtolib$container = new SteamEnergyContainer(getConversionRate(), steamTank);
        return gtolib$container;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static ModifierFunction recipeModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        if (!(machine instanceof SimpleSteamMachine steamMachine)) {
            return RecipeModifier.nullWrongType(SimpleSteamMachine.class, machine);
        }
        if (RecipeHelper.getRecipeEUtTier(recipe) > GTValues.LV || !steamMachine.checkVenting()) {
            return ModifierFunction.NULL;
        }
        return r -> {
            if (steamMachine.isHighPressure) return r;
            return RecipeModifierFunction.recipeReduction((Recipe) r, 0.5 * GTOCore.difficulty, 2);
        };
    }
}

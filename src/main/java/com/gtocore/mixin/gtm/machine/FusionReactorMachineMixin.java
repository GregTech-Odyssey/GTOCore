package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FusionReactorMachine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FusionReactorMachine.class)
public abstract class FusionReactorMachineMixin extends WorkableElectricMultiblockMachine {

    @Shadow(remap = false)
    protected long heat;

    @Final
    @Shadow(remap = false)
    protected NotifiableEnergyContainer energyContainer;

    @Shadow(remap = false)
    public abstract long getMaxVoltage();

    protected FusionReactorMachineMixin(MetaMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public @Nullable GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        var eu = recipe.data.getLong("eu_to_start");
        if (eu > energyContainer.getEnergyCapacity()) return null;
        long heatDiff = eu - heat;
        if (heatDiff > 0) {
            if (energyContainer.getEnergyStored() < heatDiff) return null;
            energyContainer.removeEnergy(heatDiff);
            heat += heatDiff;
        }
        return RecipeModifierFunction.perfectOverclocking(this, (Recipe) recipe);
    }

    @Override
    public long getOverclockVoltage() {
        return super.getMaxVoltage();
    }
}

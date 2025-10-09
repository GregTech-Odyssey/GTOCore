package com.gtocore.common.machine.multiblock.generator;

import com.gtocore.common.machine.multiblock.part.WirelessEnergyInterfacePartMachine;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;

import org.jetbrains.annotations.NotNull;

public final class WirelessEnergyGeneratorMachine extends ElectricMultiblockMachine {

    private WirelessEnergyInterfacePartMachine energyInterfacePartMachine;

    public WirelessEnergyGeneratorMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onPartScan(@NotNull IMultiPart part) {
        super.onPartScan(part);
        if (energyInterfacePartMachine == null && part instanceof WirelessEnergyInterfacePartMachine hatchPartMachine) {
            energyInterfacePartMachine = hatchPartMachine;
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        energyInterfacePartMachine = null;
    }

    @Override
    public Recipe getRealRecipe(@NotNull Recipe recipe) {
        return RecipeModifierFunction.generatorOverclocking(this, recipe);
    }

    @Override
    public long getOverclockVoltage() {
        if (energyInterfacePartMachine == null) return energyContainer.getOverclockVoltage();
        return Long.MAX_VALUE;
    }

    @Override
    public boolean useEnergy(long eu, boolean simulate) {
        if (eu >= 0) return true;
        if (energyInterfacePartMachine == null) return super.useEnergy(eu, simulate);
        var container = energyInterfacePartMachine.getWirelessEnergyContainer();
        if (container == null) return false;
        if (simulate) return true;
        container.unrestrictedAddEnergy(-eu);
        return true;
    }
}

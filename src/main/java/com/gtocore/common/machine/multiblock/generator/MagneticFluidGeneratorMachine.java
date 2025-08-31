package com.gtocore.common.machine.multiblock.generator;

import com.gtocore.common.machine.multiblock.part.WirelessEnergyHatchPartMachine;

import com.gtolib.api.GTOValues;
import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.LaserHatchPartMachine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MagneticFluidGeneratorMachine extends TierCasingMultiblockMachine {

    private int outputTier = 0;
    private boolean laser;

    public MagneticFluidGeneratorMachine(MetaMachineBlockEntity holder) {
        super(holder, GTOValues.GLASS_TIER);
    }

    @Override
    public void onPartScan(@NotNull IMultiPart part) {
        super.onPartScan(part);
        if (outputTier > 0) return;
        if (part instanceof LaserHatchPartMachine laserHatchPartMachine) {
            outputTier = laserHatchPartMachine.getTier();
            laser = true;
        } else if (part instanceof EnergyHatchPartMachine || part instanceof WirelessEnergyHatchPartMachine) {
            outputTier = ((TieredIOPartMachine) part).getTier();
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        int tier = getCasingTier(GTOValues.GLASS_TIER);
        if (tier < outputTier) outputTier = 0;
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        outputTier = 0;
        laser = false;
    }

    @Nullable
    @Override
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        if (outputTier < 1) return null;
        return RecipeModifierFunction.generatorOverclocking(this, ParallelLogic.accurateParallel(this, recipe, laser ? (int) Math.pow(1.5, outputTier - 1) : 1));
    }
}

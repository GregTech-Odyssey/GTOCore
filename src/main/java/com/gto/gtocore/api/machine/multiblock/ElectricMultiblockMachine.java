package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.machine.feature.multiblock.IEnhancedMultiblockMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IMultiblockTraitHolder;
import com.gto.gtocore.api.machine.trait.MultiblockTrait;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.network.chat.Component;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ElectricMultiblockMachine extends WorkableElectricMultiblockMachine implements IEnhancedMultiblockMachine, IMultiblockTraitHolder {

    private long overclockVoltage = -1;

    @Getter
    private final List<MultiblockTrait> multiblockTraits = new ArrayList<>(2);

    public ElectricMultiblockMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public boolean alwaysTryModifyRecipe() {
        if (getDefinition().isAlwaysTryModifyRecipe()) return true;
        for (MultiblockTrait trait : multiblockTraits) {
            if (trait.alwaysTryModifyRecipe()) return true;
        }
        return false;
    }

    @Override
    @Nullable
    public GTRecipe fullModifyRecipe(@NotNull GTRecipe recipe) {
        recipe = recipe.trimRecipeOutputs(getOutputLimits());
        if (!isGenerator() && GTUtil.getTierByVoltage(RecipeHelper.getInputEUt(recipe)) > getMaxOverclockTier()) return null;
        for (MultiblockTrait trait : multiblockTraits) {
            recipe = trait.modifyRecipe(recipe);
            if (recipe == null) return null;
        }
        return doModifyRecipe(recipe);
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe == null) return true;
        for (MultiblockTrait trait : multiblockTraits) {
            if (trait.beforeWorking(recipe)) return false;
        }
        return super.beforeWorking(recipe);
    }

    @Override
    public boolean onWorking() {
        for (MultiblockTrait trait : multiblockTraits) {
            if (trait.onWorking()) return false;
        }
        return super.onWorking();
    }

    @Override
    public void afterWorking() {
        for (MultiblockTrait trait : multiblockTraits) {
            trait.afterWorking();
        }
        super.afterWorking();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        multiblockTraits.forEach(MultiblockTrait::onStructureFormed);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        multiblockTraits.forEach(MultiblockTrait::onStructureInvalid);
        overclockVoltage = -1;
    }

    @Override
    public void onPartUnload() {
        super.onPartUnload();
        overclockVoltage = -1;
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        MachineUtils.addMachineText(textList, this, this::customText);
        for (IMultiPart part : getParts()) {
            part.addMultiText(textList);
        }
    }

    @Override
    public @NotNull EnergyContainerList getEnergyContainer() {
        if (energyContainer == null) {
            energyContainer = super.getEnergyContainer();
        }
        return energyContainer;
    }

    @Override
    public long getOverclockVoltage() {
        if (overclockVoltage < 0) {
            overclockVoltage = super.getOverclockVoltage();
        }
        return overclockVoltage;
    }
}

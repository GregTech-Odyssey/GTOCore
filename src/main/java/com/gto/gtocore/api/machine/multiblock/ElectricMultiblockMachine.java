package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.machine.feature.IEnhancedMultiblockMachine;
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ElectricMultiblockMachine extends WorkableElectricMultiblockMachine implements IEnhancedMultiblockMachine {

    private long overclockVoltage = -1;

    private final Set<MultiblockTrait> multiblockTraits = new LinkedHashSet<>(2);

    public ElectricMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    protected void addTraits(MultiblockTrait trait) {
        multiblockTraits.add(trait);
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

    protected void customText(@NotNull List<Component> textList) {
        multiblockTraits.forEach(trait -> trait.customText(textList));
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

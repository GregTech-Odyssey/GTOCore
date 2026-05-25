package com.gtocore.common.machine.multiblock.electric;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.RecipeHelper;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.VoidFluidHandler;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DistillationTowerMachine extends ElectricMultiblockMachine {

    private volatile List<IFluidHandler> fluidOutputs;

    private IFluidHandler firstValid = null;

    public DistillationTowerMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public Comparator<IMultiPart> getPartSorter() {
        return Comparator.comparingInt(p -> p.self().getPos().getY());
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        final int startY = getPos().getY() + 1;
        List<IWorkableMultiPart> parts = Arrays.stream(getParts())
                .filter(IWorkableMultiPart.class::isInstance).map(IWorkableMultiPart.class::cast)
                .filter(part -> PartAbility.EXPORT_FLUIDS.isApplicable(part.self().getBlockState().getBlock()))
                .filter(part -> part.self().getPos().getY() >= startY).toList();
        if (!parts.isEmpty()) {
            int maxY = parts.getLast().self().getPos().getY();
            fluidOutputs = new ArrayList<>(maxY - startY);
            int outputIndex = 0;
            for (int y = startY; y <= maxY; ++y) {
                if (parts.size() <= outputIndex) {
                    fluidOutputs.add(VoidFluidHandler.INSTANCE);
                    continue;
                }
                var part = parts.get(outputIndex);
                if (part.self().getPos().getY() == y) {
                    var handler = part.getRecipeHandlers().getFirst().getCapabilities(IFluidHandler.class).stream().findFirst().orElse(VoidFluidHandler.INSTANCE);
                    addOutput(handler);
                    outputIndex++;
                } else if (part.self().getPos().getY() > y) {
                    fluidOutputs.add(VoidFluidHandler.INSTANCE);
                } else {
                    GTCEu.LOGGER.error("The Distillation Tower at {} has a fluid export hatch with an unexpected Y position", getPos());
                    onStructureInvalid();
                    return;
                }
            }
        } else onStructureInvalid();
    }

    private void addOutput(IFluidHandler handler) {
        fluidOutputs.add(handler);
        if (firstValid == null && handler != VoidFluidHandler.INSTANCE) firstValid = handler;
    }

    @Override
    public void onStructureInvalid() {
        fluidOutputs = null;
        firstValid = null;
        super.onStructureInvalid();
    }

    @Override
    public boolean matchRecipeOutput(GTRecipe recipe) {
        var items = RecipeHelper.copyContents(recipe.itemOutputs, 1);
        for (var handler : getOutputUnits(recipe)) {
            if (handler.handleRecipeItem(IO.OUT, recipe, items, true)) {
                updateWorkingRecipe(recipe);
                return applyFluidOutputs(recipe, FluidAction.SIMULATE);
            }
        }
        return false;
    }

    @Override
    public boolean handleRecipeOutput(GTRecipe recipe) {
        var items = RecipeHelper.copyAndRoll(recipe, recipe.itemOutputs);
        for (var handler : getOutputUnits(recipe)) {
            if (handler.handleRecipeItem(IO.OUT, recipe, items, false)) {
                return applyFluidOutputs(recipe, FluidAction.EXECUTE);
            }
        }
        return false;
    }

    private boolean applyFluidOutputs(GTRecipe recipe, FluidAction action) {
        var fluids = recipe.fluidOutputs;
        if (fluids.isEmpty()) return true;
        // Distillery recipes should output to the first non-void handler
        if (recipe.definition.recipeType == GTRecipeTypes.DISTILLERY_RECIPES) {
            if (firstValid == null) return false;
            var output = fluids.getFirst();
            var fluid = output.inner.getFluidStack(output.getIntAmount());
            int filled = (firstValid instanceof NotifiableFluidTank nft) ? nft.fillInternal(fluid, action) : firstValid.fill(fluid, action);
            return filled == fluid.getAmount();
        }
        boolean valid = true;
        var outputs = fluidOutputs;
        for (int i = 0; i < Math.min(fluids.size(), outputs.size()); ++i) {
            var handler = outputs.get(i);
            var output = fluids.get(i);
            var fluid = output.inner.getFluidStack(output.getIntAmount());
            int filled = (handler instanceof NotifiableFluidTank nft) ? nft.fillInternal(fluid, action) : handler.fill(fluid, action);
            if (filled != fluid.getAmount()) valid = false;
            if (action.simulate() && !valid) break;
        }
        return valid;
    }

    private void updateWorkingRecipe(GTRecipe recipe) {
        if (recipe.definition.recipeType == GTRecipeTypes.DISTILLERY_RECIPES) return;
        var contents = recipe.fluidOutputs;
        if (contents.isEmpty()) return;
        var outputs = fluidOutputs;
        var size = Math.min(contents.size(), outputs.size());
        if (size == 0) {
            recipe.fluidOutputs = Collections.emptyList();
        } else {
            var trimmed = new ArrayList<Content<FluidIngredient>>(size);
            for (int i = 0; i < size; ++i) {
                if (!(outputs.get(i) instanceof VoidFluidHandler)) trimmed.add(contents.get(i));
            }
            recipe.fluidOutputs = trimmed;
        }
    }
}

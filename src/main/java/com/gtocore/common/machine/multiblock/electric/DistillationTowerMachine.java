package com.gtocore.common.machine.multiblock.electric;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.machine.trait.InaccessibleInfiniteTank;
import com.gtolib.api.recipe.AsyncRecipeOutputTask;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.api.recipe.ingredient.FastFluidIngredient;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.VoidFluidHandler;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DistillationTowerMachine extends ElectricMultiblockMachine {

    private List<IFluidHandler> fluidOutputs;

    public DistillationTowerMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new DistillationTowerLogic(this);
    }

    @Override
    public Comparator<IMultiPart> getPartSorter() {
        return Comparator.comparingInt(p -> p.self().getPos().getY());
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        final int startY = getPos().getY() + 1;
        List<IMultiPart> parts = Arrays.stream(getParts()).filter(part -> PartAbility.EXPORT_FLUIDS.isApplicable(part.self().getBlockState().getBlock())).filter(part -> part.self().getPos().getY() >= startY).toList();
        if (!parts.isEmpty()) {
            int maxY = parts.get(parts.size() - 1).self().getPos().getY();
            fluidOutputs = new ObjectArrayList<>(maxY - startY);
            int outputIndex = 0;
            for (int y = startY; y <= maxY; ++y) {
                if (parts.size() <= outputIndex) {
                    fluidOutputs.add(VoidFluidHandler.INSTANCE);
                    continue;
                }
                var part = parts.get(outputIndex);
                if (part.self().getPos().getY() == y) {
                    var handler = part.getRecipeHandlers().get(0).getCapability(FluidRecipeCapability.CAP).stream().filter(IFluidHandler.class::isInstance).findFirst().map(IFluidHandler.class::cast).orElse(VoidFluidHandler.INSTANCE);
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
    }

    @Override
    public void onStructureInvalid() {
        fluidOutputs = null;
        super.onStructureInvalid();
    }

    private static final class DistillationTowerLogic extends RecipeLogic implements IEnhancedRecipeLogic {

        @Persisted
        private GTRecipe workingRecipe = null;

        private DistillationTowerLogic(IRecipeLogicMachine machine) {
            super(machine);
        }

        @Override
        public DistillationTowerMachine getMachine() {
            return (DistillationTowerMachine) super.getMachine();
        }

        @Override

        public GTRecipe getLastRecipe() {
            return workingRecipe;
        }

        @Override
        protected boolean matchRecipe(GTRecipe recipe) {
            return RecipeRunner.matchTickRecipe(this.machine, (Recipe) recipe) && matchDTRecipe((Recipe) recipe);
        }

        @Override
        public void findAndHandleRecipe() {
            workingRecipe = null;
            super.findAndHandleRecipe();
        }

        private boolean matchDTRecipe(Recipe recipe) {
            if (!RecipeRunner.matchRecipeInput(machine, recipe)) return false;
            var items = recipe.getOutputContents(ItemRecipeCapability.CAP);
            if (!items.isEmpty()) {
                if (!RecipeRunner.handleRecipe(machine, recipe, IO.OUT, Map.of(ItemRecipeCapability.CAP, items), Collections.emptyMap(), true)) return false;
            }
            return applyFluidOutputs(recipe, FluidAction.SIMULATE);
        }

        private void updateWorkingRecipe(GTRecipe recipe) {
            this.workingRecipe = recipe.copy();
            var contents = recipe.getOutputContents(FluidRecipeCapability.CAP);
            var outputs = getMachine().getFluidOutputs();
            List<Content> trimmed = new ArrayList<>(12);
            var size = Math.min(contents.size(), outputs.size());
            for (int i = 0; i < size; ++i) {
                if (!(outputs.get(i) instanceof VoidFluidHandler)) trimmed.add(contents.get(i));
            }
            this.workingRecipe.outputs.put(FluidRecipeCapability.CAP, trimmed);
        }

        @Override
        protected boolean handleRecipeIO(GTRecipe recipe, IO io) {
            if (io != IO.OUT) {
                var handleIO = super.handleRecipeIO(recipe, io);
                if (handleIO) {
                    updateWorkingRecipe(recipe);
                } else {
                    this.workingRecipe = null;
                }
                return handleIO;
            }
            if (getMachine().isDualMEOutput(recipe)) {
                AsyncRecipeOutputTask.addAsyncLogic(this, () -> output((Recipe) recipe));
            } else {
                output((Recipe) recipe);
            }
            workingRecipe = null;
            return true;
        }

        private void output(Recipe recipe) {
            var items = recipe.getOutputContents(ItemRecipeCapability.CAP);
            if (!items.isEmpty()) {
                RecipeRunner.handleRecipe(machine, recipe, IO.OUT, Map.of(ItemRecipeCapability.CAP, items), Collections.emptyMap(), false);
            }
            applyFluidOutputs(recipe, FluidAction.EXECUTE);
        }

        private boolean applyFluidOutputs(GTRecipe recipe, FluidAction action) {
            var fluids = recipe.getOutputContents(FluidRecipeCapability.CAP).stream().map(Content::getContent).map(FluidRecipeCapability.CAP::of).toList();
            if (fluids.isEmpty()) return true;
            boolean valid = true;
            var outputs = getMachine().getFluidOutputs();
            var size = Math.min(fluids.size(), outputs.size());
            for (int i = 0; i < size; ++i) {
                var handler = outputs.get(i);
                var ingredient = fluids.get(i);
                var fluid = ingredient.getStacks()[0];
                if (handler instanceof InaccessibleInfiniteTank tank) {
                    if (action.simulate()) continue;
                    tank.fillInternal(fluid, FastFluidIngredient.getAmount(ingredient));
                } else {
                    int filled = (handler instanceof NotifiableFluidTank nft) ? nft.fillInternal(fluid, action) : handler.fill(fluid, action);
                    if (filled != fluid.getAmount()) valid = false;
                    if (action.simulate() && !valid) break;
                }
            }
            return valid;
        }
    }

    private List<IFluidHandler> getFluidOutputs() {
        return this.fluidOutputs;
    }
}

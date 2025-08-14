package com.gtocore.common.machine.multiblock.electric.voidseries;

import com.gtocore.common.data.GTOBedrockFluids;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.item.DimensionDataItem;

import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.ContentBuilder;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.common.data.GTItems.PROGRAMMED_CIRCUIT;
import static net.minecraft.network.chat.Component.translatable;

public final class VoidFluidDrillingRigMachine extends StorageMultiblockMachine {

    private static final Recipe RECIPE = RecipeBuilder.ofRaw()
            .notConsumable(PROGRAMMED_CIRCUIT.get())
            .EUt(VA[GTValues.LuV])
            .duration(20)
            .buildRawRecipe();

    private int c;
    private List<FluidStack> fluidStacks;

    public VoidFluidDrillingRigMachine(MetaMachineBlockEntity holder) {
        super(holder, 1, i -> i.is(GTOItems.DIMENSION_DATA.get()) && i.hasTag());
    }

    @Override
    public void onMachineChanged() {
        fluidStacks = GTOBedrockFluids.ALL_BEDROCK_FLUID.get(GTODimensions.getDimensionKey(DimensionDataItem.getDimension(getStorageStack())));
        if (fluidStacks == null) return;
        c = checkingCircuit(false);
        getRecipeLogic().updateTickSubscription();
    }

    private Recipe getRecipe() {
        if (fluidStacks == null) return null;
        if (!isEmpty()) {
            if (RecipeRunner.matchRecipeInput(this, RECIPE)) {
                Recipe recipe = RECIPE.copy();
                recipe.setEut(getOverclockVoltage());
                FluidStack fluidStack = fluidStacks.get(Math.min(fluidStacks.size() - 1, c)).copy();
                fluidStack.setAmount(fluidStack.getAmount() * (1 << Math.max(0, getTier() - 6)));
                recipe.outputs.put(FluidRecipeCapability.CAP, List.of(ContentBuilder.create().fluid(fluidStack).builder()));
                return recipe;
            }
        }
        return null;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onMachineChanged();
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        if (fluidStacks != null && isFormed() && !getStorageStack().isEmpty()) {
            textList.add(translatable("gui.ae2.Fluids").append(":"));
            fluidStacks.forEach(f -> textList.add(f.getFluid().getFluidType().getDescription().copy().append("x" + f.getAmount())));
        }
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }
}

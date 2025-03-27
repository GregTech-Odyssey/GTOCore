package com.gto.gtocore.common.machine.multiblock.electric.voidseries;

import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.common.data.GTOBedrockFluids;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.item.DimensionDataItem;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minecraft.network.chat.Component.translatable;

public final class VoidFluidDrillingRigMachine extends StorageMultiblockMachine {

    private int c;
    private List<FluidStack> fluidStacks;

    public VoidFluidDrillingRigMachine(IMachineBlockEntity holder) {
        super(holder, 1, i -> i.is(GTOItems.DIMENSION_DATA.get()) && i.hasTag());
    }

    @Override
    protected void onMachineChanged() {
        fluidStacks = GTOBedrockFluids.ALL_BEDROCK_FLUID.get(GTODimensions.getDimensionKey(DimensionDataItem.getDimension(getStorageStack())));
        c = MachineUtils.checkingCircuit(this, false);
    }

    @Override
    protected GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        if (!getStorageStack().isEmpty()) {
            recipe.tickInputs.put(EURecipeCapability.CAP, List.of(new Content(getOverclockVoltage(), ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0)));
            FluidStack fluidStack = fluidStacks.get(c).copy();
            fluidStack.setAmount(fluidStack.getAmount() * (1 << Math.max(0, getTier() - 6)));
            recipe.outputs.put(FluidRecipeCapability.CAP, List.of(new Content(FluidIngredient.of(fluidStack), ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0)));
            return recipe;
        }
        return recipe;
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
}

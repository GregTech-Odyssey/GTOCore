package com.gto.gtocore.api.item.tool.ae2.patternTool;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;

import java.util.List;

public class Ae2GtmProcessingPattern extends Ae2BaseProcessingPattern {

    public final GTRecipe recipe;

    public Ae2GtmProcessingPattern(ItemStack patternStack, ServerPlayer serverPlayer, GTRecipe recipe) {
        super(patternStack, serverPlayer);
        this.recipe = recipe;
    }

    public static Ae2GtmProcessingPattern of(GTRecipe recipe, ServerPlayer serverPlayer) {
        List<ItemStack> inputItemStack = RecipeStackHelper.getInputItemStacksFromRecipe(recipe);
        List<FluidStack> inputFluidStack = RecipeStackHelper.getInputFluidStacksFromRecipe(recipe);

        List<ItemStack> outputItemStack = RecipeStackHelper.getOutputItemStacksFromRecipe(recipe);
        List<FluidStack> outputFluidStack = RecipeStackHelper.getOutputFluidStacksFromRecipe(recipe);

        int inputsLength = inputItemStack.size() + inputFluidStack.size();
        var Inputs = new GenericStack[inputsLength];
        add(inputItemStack, inputFluidStack, Inputs);

        int outputsLength = outputItemStack.size() + outputFluidStack.size();
        var Outputs = new GenericStack[outputsLength];
        add(outputItemStack, outputFluidStack, Outputs);

        ItemStack patternStack = PatternDetailsHelper.encodeProcessingPattern(Inputs, Outputs);
        return new Ae2GtmProcessingPattern(patternStack, serverPlayer, recipe);
    }

    private static void add(List<ItemStack> inputItemStack, List<FluidStack> inputFluidStack, GenericStack[] inputs) {
        int inputIndex = 0;
        for (ItemStack itemStack : inputItemStack) {
            inputs[inputIndex++] = new GenericStack(AEItemKey.of(itemStack.getItem()), itemStack.getCount());
        }
        for (FluidStack fluidStack : inputFluidStack) {
            inputs[inputIndex++] = new GenericStack(AEFluidKey.of(fluidStack.getFluid()), fluidStack.getAmount());
        }
    }
    // 将模头，模具加入忽略名单
    // this.PATTERNIGNOREITEMS.addAll(Arrays.stream(GTItems.SHAPE_MOLDS).map(ItemProviderEntry::asItem).toList());
    // this.PATTERNIGNOREITEMS.addAll(Arrays.stream(GTItems.SHAPE_EXTRUDERS).map(ItemProviderEntry::asItem).toList());
}

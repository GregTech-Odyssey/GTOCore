package com.gto.gtocore.api.item.tool.ae2.patternTool;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ConflictAnalysisObj {

    public final GTRecipe recipe;

    private final List<ItemStack> inputItemStacks = new ArrayList<>();
    private final List<FluidStack> inputFluidStacks = new ArrayList<>();
    private final List<ItemStack> outputItemStacks = new ArrayList<>();
    private final List<FluidStack> outputFluidStacks = new ArrayList<>();

    public final List<String> inputItemIds = new ArrayList<>();
    public final List<String> inputFluidIds = new ArrayList<>();
    private final List<String> outputItemIds = new ArrayList<>();
    private final List<String> outputFluidIds = new ArrayList<>();

    private final Map<String, Long> inputItemIdsMap = new HashMap<>();
    private final Map<String, Long> inputFluidIdsMap = new HashMap<>();
    private final Map<String, Long> outputItemIdsMap = new HashMap<>();
    private final Map<String, Long> outputFluidIdsMap = new HashMap<>();

    public Number CIRCUIT;
    private boolean hasINTEGRATED_CIRCUIT = false;

    public ConflictAnalysisObj(GTRecipe recipe) {
        this.recipe = recipe;
        this.populateItemStacks(recipe.getInputContents(ItemRecipeCapability.CAP), inputItemStacks, inputItemIds, inputItemIdsMap);
        this.populateFluidStacks(recipe.getInputContents(FluidRecipeCapability.CAP), inputFluidStacks, inputFluidIds, inputFluidIdsMap);
        this.populateItemStacks(recipe.getOutputContents(ItemRecipeCapability.CAP), outputItemStacks, outputItemIds, outputItemIdsMap);
        this.populateFluidStacks(recipe.getOutputContents(FluidRecipeCapability.CAP), outputFluidStacks, outputFluidIds, outputFluidIdsMap);
    }

    private void populateItemStacks(List<Content> contentList,
                                    List<ItemStack> stackList,
                                    List<String> itemIdList,
                                    Map<String, Long> itemIdsMap) {
        contentList.forEach(content -> {
            ItemStack itemStack = ItemRecipeCapability.CAP.of(content.getContent()).kjs$getFirst();
            if (itemStack.getItem().equals((GTItems.INTEGRATED_CIRCUIT.asItem()))) {
                this.CIRCUIT = IntCircuitBehaviour.getCircuitConfiguration(itemStack);
                hasINTEGRATED_CIRCUIT = true;
                return; // 不将电路编入ids
            }
            stackList.add(itemStack);
            itemIdList.add(itemStack.kjs$getId());
            itemIdsMap.put(itemStack.kjs$getId(), (long) itemStack.getCount());
        });
        if (!hasINTEGRATED_CIRCUIT) {
            this.CIRCUIT = 0;
        }
    }

    private void populateFluidStacks(List<Content> contentList,
                                     List<FluidStack> stackList,
                                     List<String> fluidIdList,
                                     Map<String, Long> FluidIdsMap) {
        contentList.forEach(content -> {
            FluidStack[] fluidStackList = FluidRecipeCapability.CAP.of(content.getContent()).getStacks();

            FluidStack fluidStack = fluidStackList.length > 0 ? fluidStackList[0] : FluidStack.empty();
            stackList.add(fluidStack);
            if (!fluidStack.isEmpty()) {
                String key = Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid())).toString();
                fluidIdList.add(key);
                FluidIdsMap.put(key, fluidStack.getAmount());
            }
        });
    }

    public String toString() {
        return ("""
                // ---------- start ---------- //
                CIRCUIT: %S
                inputs:
                List<ItemStack>: %s
                List<FluidStack> : %s
                inputItemIds : %s
                inputFluidIds : %s
                inputItemIdsMap : %s
                inputFluidIdsMap : %s
                outputs:
                List<ItemStack>: %s
                List<FluidStack> : %s
                outputItemIds : %s
                outputFluidIds : %s
                outputItemIdsMap : %s
                outputFluidIdsMap : %s
                // ---------- stopA ---------- //
                """.formatted(
                this.CIRCUIT,
                inputItemStacks,
                inputFluidStacks,

                inputItemIds,
                inputFluidIds,

                inputItemIdsMap,
                inputFluidIdsMap,

                outputItemStacks,
                outputFluidStacks,

                outputItemIds,
                outputFluidIds,

                outputItemIdsMap,
                outputFluidIdsMap));
    }
}

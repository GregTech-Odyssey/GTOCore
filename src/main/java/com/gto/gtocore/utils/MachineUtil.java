package com.gto.gtocore.utils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntCircuitIngredient;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MachineUtil {

    public static BlockPos getOffsetPos(int a, int b, Direction facing, BlockPos pos) {
        int x = 0, z = 0;
        switch (facing) {
            case NORTH -> z = a;
            case SOUTH -> z = -a;
            case WEST -> x = a;
            case EAST -> x = -a;
        }
        return pos.offset(x, b, z);
    }

    public static long[] getFluidAmount(WorkableMultiblockMachine machine, Fluid... fluids) {
        long[] amounts = new long[fluids.length];
        Map<Fluid, Integer> fluidIndexMap = new HashMap<>();
        for (int i = 0; i < fluids.length; i++) {
            fluidIndexMap.put(fluids[i], i);
        }
        for (IRecipeHandler<?> handler : Objects.requireNonNullElseGet(machine.getCapabilitiesProxy().get(IO.IN, FluidRecipeCapability.CAP), Collections::<IRecipeHandler<?>>emptyList)) {
            for (Object contents : handler.getContents()) {
                if (contents instanceof FluidStack fluidStack) {
                    Integer index = fluidIndexMap.get(fluidStack.getFluid());
                    if (index != null) {
                        amounts[index] += fluidStack.getAmount();
                    }
                }
            }
        }
        return amounts;
    }

    public static long[] getItemAmount(WorkableMultiblockMachine machine, Item... items) {
        long[] amounts = new long[items.length];
        Map<Item, Integer> itemIndexMap = new HashMap<>();
        for (int i = 0; i < items.length; i++) {
            itemIndexMap.put(items[i], i);
        }
        for (IRecipeHandler<?> handler : Objects.requireNonNullElseGet(machine.getCapabilitiesProxy().get(IO.IN, ItemRecipeCapability.CAP), Collections::<IRecipeHandler<?>>emptyList)) {
            if (!handler.isProxy()) {
                for (Object contents : handler.getContents()) {
                    if (contents instanceof ItemStack itemStack) {
                        Integer index = itemIndexMap.get(itemStack.getItem());
                        if (index != null) {
                            amounts[index] += itemStack.getCount();
                        }
                    }
                }
            }
        }
        return amounts;
    }

    public static boolean inputItem(WorkableMultiblockMachine machine, ItemStack item) {
        if (!item.isEmpty()) {
            GTRecipe recipe = new GTRecipeBuilder(item.kjs$getIdLocation(), GTRecipeTypes.DUMMY_RECIPES).inputItems(item).buildRawRecipe();
            if (recipe.matchRecipe(machine).isSuccess()) {
                return recipe.handleRecipeIO(IO.IN, machine, machine.recipeLogic.getChanceCaches());
            }
        }
        return false;
    }

    public static boolean outputItem(WorkableMultiblockMachine machine, ItemStack item) {
        if (!item.isEmpty()) {
            GTRecipe recipe = new GTRecipeBuilder(item.kjs$getIdLocation(), GTRecipeTypes.DUMMY_RECIPES).outputItems(item).buildRawRecipe();
            if (recipe.matchRecipe(machine).isSuccess()) {
                return recipe.handleRecipeIO(IO.OUT, machine, machine.recipeLogic.getChanceCaches());
            }
        }
        return false;
    }

    public static boolean notConsumableItem(WorkableMultiblockMachine machine, ItemStack item) {
        return new GTRecipeBuilder(item.kjs$getIdLocation(), GTRecipeTypes.DUMMY_RECIPES).inputItems(item).buildRawRecipe().matchRecipe(machine).isSuccess();
    }

    public static boolean notConsumableCircuit(WorkableMultiblockMachine machine, int configuration) {
        return new GTRecipeBuilder(GTCEu.id(String.valueOf(configuration)), GTRecipeTypes.DUMMY_RECIPES).inputItems(IntCircuitIngredient.circuitInput(configuration)).buildRawRecipe()
                .matchRecipe(machine).isSuccess();
    }

    public static boolean inputFluid(WorkableMultiblockMachine machine, FluidStack fluid) {
        if (!fluid.isEmpty()) {
            GTRecipe recipe = new GTRecipeBuilder(Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid.getFluid())), GTRecipeTypes.DUMMY_RECIPES).inputFluids(fluid).buildRawRecipe();
            if (recipe.matchRecipe(machine).isSuccess()) {
                return recipe.handleRecipeIO(IO.IN, machine, machine.recipeLogic.getChanceCaches());
            }
        }
        return false;
    }

    public static boolean outputFluid(WorkableMultiblockMachine machine, FluidStack fluid) {
        if (!fluid.isEmpty()) {
            GTRecipe recipe = new GTRecipeBuilder(Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid.getFluid())), GTRecipeTypes.DUMMY_RECIPES).outputFluids(fluid).buildRawRecipe();
            if (recipe.matchRecipe(machine).isSuccess()) {
                return recipe.handleRecipeIO(IO.OUT, machine, machine.recipeLogic.getChanceCaches());
            }
        }
        return false;
    }

    public static boolean inputEU(WorkableMultiblockMachine machine, long eu) {
        if (eu != 0) {
            GTRecipe recipe = new GTRecipeBuilder(GTCEu.id(String.valueOf(eu)), GTRecipeTypes.DUMMY_RECIPES).inputEU(eu).buildRawRecipe();
            if (recipe.matchRecipe(machine).isSuccess()) {
                return recipe.handleRecipeIO(IO.IN, machine, machine.recipeLogic.getChanceCaches());
            }
        }
        return false;
    }
}

package com.gto.gtocore.utils;

import com.gto.gtocore.api.machine.feature.multiblock.IParallelMachine;
import com.gto.gtocore.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.capability.IParallelHatch;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntCircuitIngredient;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTMath;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public final class MachineUtils {

    public static final Function<MultiblockMachineDefinition, BlockPattern> EMPTY_PATTERN = (d) -> new BlockPattern(new TraceabilityPredicate[0][0][0], new RelativeDirection[0], new int[0][0], new int[0]) {};

    public static List<MultiblockShapeInfo> getMatchingShapes(BlockPattern blockPattern) {
        return repetitionDFS(blockPattern, new ArrayList<>(), blockPattern.aisleRepetitions, new Stack<>());
    }

    private static List<MultiblockShapeInfo> repetitionDFS(BlockPattern pattern, List<MultiblockShapeInfo> pages, int[][] aisleRepetitions, Stack<Integer> repetitionStack) {
        if (repetitionStack.size() == aisleRepetitions.length) {
            int[] repetition = new int[repetitionStack.size()];
            for (int i = 0; i < repetitionStack.size(); i++) {
                repetition[i] = repetitionStack.get(i);
            }
            pages.add(new MultiblockShapeInfo(pattern.getPreview(repetition)));
        } else {
            for (int i = aisleRepetitions[repetitionStack.size()][0]; i <= aisleRepetitions[repetitionStack.size()][1]; i++) {
                repetitionStack.push(i);
                repetitionDFS(pattern, pages, aisleRepetitions, repetitionStack);
                repetitionStack.pop();
            }
        }
        return pages;
    }

    public static boolean isUD(Direction facing) {
        return facing == Direction.UP || facing == Direction.DOWN;
    }

    public static BlockPos getOffsetPos(int a, int b, int c, Direction facing, BlockPos pos) {
        int x = 0, z = 0;
        switch (facing) {
            case NORTH -> {
                z = a;
                x = c;
            }
            case SOUTH -> {
                z = -a;
                x = -c;
            }
            case WEST -> {
                x = a;
                z = c;
            }
            case EAST -> {
                x = -a;
                z = -c;
            }
        }
        return pos.offset(x, b, z);
    }

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

    public static BlockPos getOffsetPos(int a, Direction facing, BlockPos pos) {
        int x = 0, y = 0, z = 0;
        switch (facing) {
            case UP -> y = -a;
            case DOWN -> y = a;
            case NORTH -> z = a;
            case SOUTH -> z = -a;
            case WEST -> x = a;
            case EAST -> x = -a;
        }
        return pos.offset(x, y, z);
    }

    public static void addMachineText(List<Component> textList, WorkableMultiblockMachine machine, Consumer<List<Component>> customConsumer) {
        MultiblockDisplayText.Builder builder = MultiblockDisplayText.builder(textList, machine.isFormed()).setWorkingStatus(machine.recipeLogic.isWorkingEnabled(), machine.recipeLogic.isActive());
        if (!machine.isFormed()) return;
        int numThread = 0;
        int numParallels;
        if (machine instanceof IParallelMachine parallelMachine && parallelMachine.getParallel() > 0) {
            numParallels = parallelMachine.getParallel();
        } else {
            numParallels = getHatchParallel(machine);
        }
        if (machine instanceof ElectricMultiblockMachine electricMultiblockMachine) {
            builder.addEnergyUsageLine(electricMultiblockMachine.getEnergyContainer()).addEnergyTierLine(electricMultiblockMachine.getTier());
            if (electricMultiblockMachine instanceof CrossRecipeMultiblockMachine crossRecipeMultiblockMachine) {
                numThread = crossRecipeMultiblockMachine.getThread();
            }
        }
        builder.addMachineModeLine(machine.getRecipeType(), machine.getRecipeTypes().length > 1);
        if (numThread > 1) {
            Component thread = Component.literal(String.valueOf(numThread)).withStyle(ChatFormatting.DARK_AQUA);
            Component parallels = Component.literal(FormattingUtil.formatNumbers(numParallels)).withStyle(ChatFormatting.DARK_PURPLE);
            builder.addCustom(text -> text.add(Component.translatable("gtocore.machine.thread", thread, parallels).withStyle(ChatFormatting.GRAY)));
        } else {
            builder.addParallelsLine(numParallels);
        }
        builder.addCustom(customConsumer)
                .addCustom(text -> machine.getDefinition().getAdditionalDisplay().accept(machine, text))
                .addWorkingStatusLine()
                .addProgressLine(machine.recipeLogic.getProgress(), machine.recipeLogic.getMaxProgress(), machine.recipeLogic.getProgressPercent())
                .addOutputLines(machine.recipeLogic.getLastRecipe());
    }

    public static void addRecipeTypeText(List<Component> textList, IRecipeLogicMachine machine) {
        GTRecipeType type = machine.getRecipeType();
        if (type == GTRecipeTypes.DUMMY_RECIPES) return;
        textList.add(Component.translatable("gtceu.gui.machinemode", Component.translatable(type.registryName.toLanguageKey())).withStyle(ChatFormatting.AQUA));
    }

    public static int getHatchParallel(MetaMachine machine) {
        if (machine instanceof IMultiController controller && controller.isFormed()) {
            Optional<IParallelHatch> parallelHatch = controller.getParallelHatch();
            if (parallelHatch.isPresent()) {
                return Math.max(1, parallelHatch.get().getCurrentParallel());
            }
        }
        return 1;
    }

    public static int checkingCircuit(IRecipeLogicMachine machine, boolean sum) {
        int circuit = 0;
        for (IRecipeHandler<?> handler : Objects.requireNonNullElseGet(machine.getCapabilitiesProxy().get(IO.IN, ItemRecipeCapability.CAP), Collections::<IRecipeHandler<?>>emptyList)) {
            if (!handler.isProxy()) {
                for (Object contents : handler.getContents()) {
                    if (contents instanceof ItemStack itemStack && itemStack.is(GTItems.PROGRAMMED_CIRCUIT.get())) {
                        int c = IntCircuitBehaviour.getCircuitConfiguration(itemStack);
                        if (sum) {
                            circuit += c;
                        } else {
                            return c;
                        }
                    }
                }
            }
        }
        return circuit;
    }

    public static int[] getFluidAmount(IRecipeLogicMachine machine, Fluid... fluids) {
        int[] amounts = new int[fluids.length];
        Map<Fluid, Integer> fluidIndexMap = new Object2IntOpenHashMap<>();
        for (int i = 0; i < fluids.length; i++) {
            fluidIndexMap.put(fluids[i], i);
        }
        forEachInputFluids(machine, fluidStack -> {
            Integer index = fluidIndexMap.get(fluidStack.getFluid());
            if (index != null) {
                amounts[index] = GTMath.saturatedCast(fluidStack.getAmount() + amounts[index]);
            }
            return false;
        });
        return amounts;
    }

    public static int[] getItemAmount(IRecipeLogicMachine machine, Item... items) {
        int[] amounts = new int[items.length];
        Map<Item, Integer> itemIndexMap = new Object2IntOpenHashMap<>();
        for (int i = 0; i < items.length; i++) {
            itemIndexMap.put(items[i], i);
        }
        forEachInputItems(machine, itemStack -> {
            Integer index = itemIndexMap.get(itemStack.getItem());
            if (index != null) {
                amounts[index] = GTMath.saturatedCast(itemStack.getCount() + amounts[index]);
            }
            return false;
        });
        return amounts;
    }

    public static void forEachInputItems(IRecipeLogicMachine machine, Function<ItemStack, Boolean> function) {
        for (IRecipeHandler<?> handler : Objects.requireNonNullElseGet(machine.getCapabilitiesProxy().get(IO.IN, ItemRecipeCapability.CAP), Collections::<IRecipeHandler<?>>emptyList)) {
            if (!handler.isProxy()) {
                for (Object contents : handler.getContents()) {
                    if (contents instanceof ItemStack itemStack) {
                        if (function.apply(itemStack)) return;
                    }
                }
            }
        }
    }

    public static void forEachInputFluids(IRecipeLogicMachine machine, Function<FluidStack, Boolean> function) {
        for (IRecipeHandler<?> handler : Objects.requireNonNullElseGet(machine.getCapabilitiesProxy().get(IO.IN, FluidRecipeCapability.CAP), Collections::<IRecipeHandler<?>>emptyList)) {
            if (!handler.isProxy()) {
                for (Object contents : handler.getContents()) {
                    if (contents instanceof FluidStack fluidStack) {
                        if (function.apply(fluidStack)) return;
                    }
                }
            }
        }
    }

    public static boolean inputItem(IRecipeLogicMachine machine, ItemStack item) {
        if (!item.isEmpty()) {
            GTRecipe recipe = GTORecipeBuilder.ofRaw().inputItems(item).buildRawRecipe();
            if (RecipeRunner.matchRecipe(machine, recipe)) {
                return RecipeRunner.handleRecipeIO(machine, recipe, IO.IN);
            }
        }
        return false;
    }

    public static boolean outputItem(IRecipeLogicMachine machine, ItemStack item) {
        if (!item.isEmpty()) {
            GTRecipe recipe = GTORecipeBuilder.ofRaw().outputItems(item).buildRawRecipe();
            if (RecipeRunner.matchRecipe(machine, recipe)) {
                return RecipeRunner.handleRecipeOutput(machine, recipe);
            }
        }
        return false;
    }

    public static boolean notConsumableItem(IRecipeLogicMachine machine, ItemStack item) {
        return RecipeRunner.matchRecipe(machine, GTORecipeBuilder.ofRaw().inputItems(item).buildRawRecipe());
    }

    public static boolean notConsumableCircuit(IRecipeLogicMachine machine, int configuration) {
        return RecipeRunner.matchRecipe(machine, GTORecipeBuilder.ofRaw().inputItems(IntCircuitIngredient.circuitInput(configuration)).buildRawRecipe());
    }

    public static boolean inputFluid(IRecipeLogicMachine machine, Fluid fluid, int amount) {
        return inputFluid(machine, new FluidStack(fluid, amount));
    }

    public static boolean inputFluid(IRecipeLogicMachine machine, FluidStack fluid) {
        if (!fluid.isEmpty()) {
            GTRecipe recipe = GTORecipeBuilder.ofRaw().inputFluids(fluid).buildRawRecipe();
            if (RecipeRunner.matchRecipe(machine, recipe)) {
                return RecipeRunner.handleRecipeIO(machine, recipe, IO.IN);
            }
        }
        return false;
    }

    public static boolean outputFluid(IRecipeLogicMachine machine, Fluid fluid, int amount) {
        return outputFluid(machine, new FluidStack(fluid, amount));
    }

    public static boolean outputFluid(IRecipeLogicMachine machine, FluidStack fluid) {
        if (!fluid.isEmpty()) {
            GTRecipe recipe = GTORecipeBuilder.ofRaw().outputFluids(fluid).buildRawRecipe();
            if (RecipeRunner.matchRecipe(machine, recipe)) {
                return RecipeRunner.handleRecipeOutput(machine, recipe);
            }
        }
        return false;
    }

    public static boolean inputEU(IRecipeLogicMachine machine, long eu) {
        if (eu != 0) {
            GTRecipe recipe = GTORecipeBuilder.ofRaw().inputEU(eu).buildRawRecipe();
            if (RecipeRunner.matchRecipe(machine, recipe)) {
                return RecipeRunner.handleRecipeIO(machine, recipe, IO.IN);
            }
        }
        return false;
    }
}

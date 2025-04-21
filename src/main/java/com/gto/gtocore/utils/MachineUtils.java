package com.gto.gtocore.utils;

import com.gto.gtocore.api.machine.feature.multiblock.IParallelMachine;
import com.gto.gtocore.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.IdleReason;
import com.gto.gtocore.api.recipe.RecipeRunnerHelper;

import com.gregtechceu.gtceu.api.capability.IParallelHatch;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class MachineUtils {

    private MachineUtils() {}

    private static final GTRecipe[] CIRCUIT_RECIPE = new GTRecipe[32];

    public static final Function<MultiblockMachineDefinition, BlockPattern> EMPTY_PATTERN = (d) -> new BlockPattern(new TraceabilityPredicate[0][0][0], new RelativeDirection[0], new int[0][0], new int[0]);

    public static List<MultiblockShapeInfo> getMatchingShapes(boolean fast, BlockPattern blockPattern) {
        List<Supplier<MultiblockShapeInfo>> list = repetitionDFS(blockPattern, new ArrayList<>(), blockPattern.aisleRepetitions, new Stack<>());
        if (fast) {
            List<MultiblockShapeInfo> shapes = new ArrayList<>();
            shapes.add(list.get(0).get());
            if (list.size() > 1) {
                shapes.add(list.get(list.size() - 1).get());
            }
            return shapes;
        }
        return list.stream().map(Supplier::get).toList();
    }

    private static List<Supplier<MultiblockShapeInfo>> repetitionDFS(BlockPattern pattern, List<Supplier<MultiblockShapeInfo>> pages, int[][] aisleRepetitions, Stack<Integer> repetitionStack) {
        if (repetitionStack.size() == aisleRepetitions.length) {
            int[] repetition = new int[repetitionStack.size()];
            for (int i = 0; i < repetitionStack.size(); i++) {
                repetition[i] = repetitionStack.get(i);
            }
            pages.add(() -> new MultiblockShapeInfo(pattern.getPreview(repetition)));
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
        if (machine.isRemote()) return;
        MultiblockDisplayText.Builder builder = MultiblockDisplayText.builder(textList, machine.isFormed()).setWorkingStatus(machine.recipeLogic.isWorkingEnabled(), machine.recipeLogic.isActive());
        if (!machine.isFormed()) return;
        int numThread = 0;
        int numParallels;
        if (machine instanceof IParallelMachine parallelMachine && parallelMachine.getParallel() > 0) {
            numParallels = parallelMachine.getParallel();
        } else {
            numParallels = getHatchParallel(machine);
        }
        if (machine instanceof WorkableElectricMultiblockMachine electricMultiblockMachine) {
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
                .addCustom(text -> IdleReason.addMachineText(text, machine))
                .addWorkingStatusLine()
                .addProgressLine(machine.recipeLogic.getProgress(), machine.recipeLogic.getMaxProgress(), machine.recipeLogic.getProgressPercent())
                .addOutputLines(machine.recipeLogic.getLastRecipe());
    }

    public static void addRecipeTypeText(List<Component> textList, IRecipeLogicMachine machine) {
        GTRecipeType type = machine.getRecipeType();
        if (type == GTRecipeTypes.DUMMY_RECIPES) return;
        textList.add(Component.translatable("gtceu.gui.machinemode", Component.translatable(type.registryName.toLanguageKey())).withStyle(ChatFormatting.AQUA));
    }

    /**
     * 获取控制器的并行数量。
     * 
     * @param machine 元机器对象，可以是多控制器机器。
     * @return 并行数量，如果机器是多控制器且已形成，则返回并行接口的当前并行数量，否则返回默认值1。
     */
    public static int getHatchParallel(MetaMachine machine) {
        if (machine instanceof IMultiController controller && controller.isFormed()) {
            Optional<IParallelHatch> parallelHatch = controller.getParallelHatch();
            if (parallelHatch.isPresent()) {
                return Math.max(1, parallelHatch.get().getCurrentParallel());
            }
        }
        return 1;
    }

    /**
     * 检查机器中的电路配置。
     *
     * @param machine 机器对象，包含电路配置信息。
     * @param sum     是否将所有电路配置值相加。如果为true，则返回所有电路配置值的总和；如果为false，则返回第一个找到的电路配置值。
     * @return 电路配置值。如果sum为true，则返回所有电路配置值的总和；如果sum为false，则返回第一个找到的电路配置值。如果没有找到电路配置，则返回0。
     */
    public static int checkingCircuit(IRecipeCapabilityHolder machine, boolean sum) {
        AtomicInteger circuit = new AtomicInteger();
        forEachInputItems(machine, itemStack -> {
            if (itemStack.is(GTItems.PROGRAMMED_CIRCUIT.get())) {
                circuit.addAndGet(IntCircuitBehaviour.getCircuitConfiguration(itemStack));
                return !sum;
            }
            return false;
        });
        return circuit.get();
    }

    /**
     * 获取指定流体在机器中的总量。
     *
     * @param machine 机器对象，用于获取输入流体信息。
     * @param fluids  需要查询的流体数组。
     * @return 一个整数数组，每个元素表示对应流体的总量。
     */
    public static int[] getFluidAmount(IRecipeCapabilityHolder machine, Fluid... fluids) {
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

    /**
     * 获取指定物品在机器中的总量。
     *
     * @param machine 机器对象，用于获取输入物品信息。
     * @param items   需要查询的物品数组。
     * @return 一个整数数组，每个元素表示对应物品的总量。
     */
    public static int[] getItemAmount(IRecipeCapabilityHolder machine, Item... items) {
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

    /**
     * 遍历机器的所有输入物品，并对每个物品应用给定的函数。
     * 如果函数对某个物品返回 true，则立即返回，不再继续遍历。
     * 如果机器包含非{@link com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler}的仓，不要修改。
     *
     * @param machine  要遍历的机器实例
     * @param function 应用于每个物品的函数，函数接收一个 ItemStack 参数并返回一个 Boolean 值
     */
    public static void forEachInputItems(IRecipeCapabilityHolder machine, Predicate<ItemStack> function) {
        for (IRecipeHandler<?> handler : machine.getCapabilitiesFlat(IO.IN, ItemRecipeCapability.CAP)) {
            for (Object contents : handler.getContents()) {
                if (contents instanceof ItemStack itemStack && !itemStack.isEmpty()) {
                    if (function.test(itemStack)) return;
                }
            }
        }
    }

    /**
     * 遍历机器的所有输入流体，并对每个流体应用给定的函数。
     * 如果函数对某个流体返回true，则立即返回，不再继续遍历。
     * 如果机器包含非{@link com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank}的仓，不要修改。
     *
     * @param machine  要遍历的机器实例
     * @param function 要应用于每个流体栈的函数
     */
    public static void forEachInputFluids(IRecipeCapabilityHolder machine, Predicate<FluidStack> function) {
        for (IRecipeHandler<?> handler : machine.getCapabilitiesFlat(IO.IN, FluidRecipeCapability.CAP)) {
            for (Object contents : handler.getContents()) {
                if (contents instanceof FluidStack fluidStack && !fluidStack.isEmpty()) {
                    if (function.test(fluidStack)) return;
                }
            }
        }
    }

    private static List<Ingredient> toIngredient(ItemStack... item) {
        List<Ingredient> contentList = new ObjectArrayList<>(item.length);
        for (ItemStack content : item) {
            if (content.isEmpty()) continue;
            contentList.add(Ingredient.of(content));
        }
        return contentList;
    }

    private static List<FluidIngredient> toFluidIngredient(FluidStack... fluid) {
        List<FluidIngredient> contentList = new ObjectArrayList<>(fluid.length);
        for (FluidStack content : fluid) {
            if (content.isEmpty()) continue;
            contentList.add(FluidIngredient.of(content));
        }
        return contentList;
    }

    public static boolean inputItem(IRecipeCapabilityHolder machine, ItemStack... item) {
        List<?> contentList = toIngredient(item);
        return RecipeRunnerHelper.handleRecipe(machine, IO.IN, new ObjectArrayList<>(contentList), ItemRecipeCapability.CAP, true) && RecipeRunnerHelper.handleRecipe(machine, IO.IN, contentList, ItemRecipeCapability.CAP, false);
    }

    public static boolean outputItem(IRecipeCapabilityHolder machine, ItemStack... item) {
        List<?> contentList = toIngredient(item);
        return RecipeRunnerHelper.handleRecipe(machine, IO.OUT, contentList, ItemRecipeCapability.CAP, false);
    }

    public static boolean notConsumableItem(IRecipeCapabilityHolder machine, ItemStack... item) {
        List<?> contentList = toIngredient(item);
        return RecipeRunnerHelper.handleRecipe(machine, IO.IN, contentList, ItemRecipeCapability.CAP, true);
    }

    public static boolean notConsumableCircuit(IRecipeCapabilityHolder machine, int configuration) {
        GTRecipe recipe = CIRCUIT_RECIPE[configuration];
        if (recipe == null) {
            recipe = GTORecipeBuilder.ofRaw().inputItems(IntCircuitIngredient.circuitInput(configuration)).buildRawRecipe();
            CIRCUIT_RECIPE[configuration] = recipe;
        }
        return RecipeRunnerHelper.matchRecipe(machine, recipe);
    }

    public static boolean inputFluid(IRecipeCapabilityHolder machine, Fluid fluid, int amount) {
        return inputFluid(machine, new FluidStack(fluid, amount));
    }

    public static boolean inputFluid(IRecipeCapabilityHolder machine, FluidStack... fluid) {
        List<?> contentList = toFluidIngredient(fluid);
        return RecipeRunnerHelper.handleRecipe(machine, IO.IN, new ObjectArrayList<>(contentList), FluidRecipeCapability.CAP, true) && RecipeRunnerHelper.handleRecipe(machine, IO.IN, contentList, FluidRecipeCapability.CAP, false);
    }

    public static boolean outputFluid(IRecipeCapabilityHolder machine, Fluid fluid, int amount) {
        return outputFluid(machine, new FluidStack(fluid, amount));
    }

    public static boolean outputFluid(IRecipeCapabilityHolder machine, FluidStack... fluid) {
        List<?> contentList = toFluidIngredient(fluid);
        return RecipeRunnerHelper.handleRecipe(machine, IO.OUT, contentList, FluidRecipeCapability.CAP, false);
    }
}

package com.gtocore.common.machine.multiblock.electric.assembly;

import com.gtocore.common.machine.multiblock.part.HugeBusPartMachine;
import com.gtocore.data.IdleReason;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.ingredient.FastFluidIngredient;
import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.utils.ItemUtils;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.capability.IFluidHandler;

import com.hepdd.gtmthings.common.block.machine.multiblock.part.CreativeInputHatchPartMachine;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class AdvancedAssemblyLineMachine extends ElectricMultiblockMachine {

    private final List<CustomItemStackHandler> itemStackTransfers = new ReferenceArrayList<>();
    private final List<CustomFluidTank> fluidTankTransfers = new ReferenceArrayList<>();

    public AdvancedAssemblyLineMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Nullable
    @Override
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        var config = ConfigHolder.INSTANCE.machines;

        if (config.orderedAssemblyLineItems) {
            if (!checkItemInputs(recipe)) {
                setIdleReason(IdleReason.ORDERED);
                return null;
            }
        }

        if (config.orderedAssemblyLineFluids) {
            if (!checkFluidInputs(recipe)) {
                setIdleReason(IdleReason.ORDERED);
                return null;
            }
        }
        return RecipeModifierFunction.laserLossOverclocking(this, RecipeModifierFunction.hatchParallel(this, recipe));
    }

    /**
     * 检查给定配方的物品输入是否与机器的物品存储区有序匹配。
     */
    private boolean checkItemInputs(GTRecipe recipe) {
        var inputs = recipe.inputs.get(ItemRecipeCapability.CAP);
        if (itemStackTransfers.size() < inputs.size()) return false;

        for (int i = 0; i < inputs.size(); i++) {
            var content = inputs.get(i).getContent();
            if (content instanceof FastSizedIngredient ingredient && !ingredient.isEmpty()) {
                if (!matchItem(this.itemStackTransfers.get(i), ingredient)) return false;
            }
        }

        return true;
    }

    /**
     * 检查给定配方的流体输入是否与机器的流体存储区有序匹配。
     */
    private boolean checkFluidInputs(GTRecipe recipe) {
        var inputs = recipe.inputs.get(FluidRecipeCapability.CAP);
        if (fluidTankTransfers.size() < inputs.size()) return false;

        for (int i = 0; i < inputs.size(); i++) {
            var content = inputs.get(i).getContent();
            if (content instanceof FastFluidIngredient ingredient && !ingredient.isEmpty()) {
                if (!matchFluid(this.fluidTankTransfers.get(i), ingredient)) return false;
            }
        }

        return true;
    }

    /**
     * 验证给定的存储区是否仅包含与当前需求匹配的唯一种类物品。
     */
    private boolean matchItem(CustomItemStackHandler storage, FastSizedIngredient currentIngredient) {
        Item item = Items.AIR;
        for (var stack : storage.stacks) {
            if (stack.isEmpty()) continue;

            Item providedItem = stack.getItem();
            if (providedItem != item) {
                if (item != Items.AIR) {
                    return false;
                }

                if (!currentIngredient.testItem(providedItem)) {
                    return false;
                }

                item = providedItem;
            }
        }

        return currentIngredient.testItem(item);
    }

    /**
     * 验证给定的流体存储区是否与当前需求匹配。
     */
    private boolean matchFluid(CustomFluidTank storage, FastFluidIngredient currentIngredient) {
        return currentIngredient.testFluid(storage.getFluid().getFluid());
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new AssemblyLineLogic(this);
    }

    @Override
    public Comparator<IMultiPart> getPartSorter() {
        return Comparator.comparing(p -> p.self().getPos(), RelativeDirection.RIGHT.getSorter(getFrontFacing(), getUpwardsFacing(), isFlipped()));
    }

    /**
     * 绑定物品和流体存储
     */
    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        itemStackTransfers.clear();
        fluidTankTransfers.clear();

        for (Object part : getParts()) {
            if (part instanceof ItemBusPartMachine itemBusPart) {
                itemStackTransfers.add(itemBusPart.getInventory().storage);
            } else if (part instanceof HugeBusPartMachine hugeBusPartMachine) {
                itemStackTransfers.add(hugeBusPartMachine.getInventory().storage);
            } else if (part instanceof FluidHatchPartMachine fluidHatchPartMachine) {
                Collections.addAll(fluidTankTransfers, fluidHatchPartMachine.tank.getStorages());
            } else if (part instanceof CreativeInputHatchPartMachine creativeInputHatchPartMachine) {
                // Not supported this as the creative hatch did not expose its real tanks
            }
        }
    }

    private static class AssemblyLineLogic extends RecipeLogic {

        private AssemblyLineLogic(IRecipeLogicMachine machine) {
            super(machine);
        }

        @NotNull
        @Override
        public AdvancedAssemblyLineMachine getMachine() {
            return (AdvancedAssemblyLineMachine) super.getMachine();
        }

        @Override
        protected boolean handleRecipeIO(GTRecipe recipe, IO io) {
            if (io == IO.IN) {
                if (ConfigHolder.INSTANCE.machines.orderedAssemblyLineItems) {
                    if (!consumeOrderedItemInputs(recipe)) {
                        return false;
                    }
                } else {
                    var items = recipe.getInputContents(ItemRecipeCapability.CAP);
                    if (!RecipeHelper.handleRecipe(this.machine, recipe, io, Map.of(ItemRecipeCapability.CAP, items), chanceCaches, false)) {
                        return false;
                    }
                }
                if (ConfigHolder.INSTANCE.machines.orderedAssemblyLineFluids) {
                    return consumeOrderedFluidInputs(recipe);
                } else {
                    var fluids = recipe.getInputContents(FluidRecipeCapability.CAP);
                    return RecipeHelper.handleRecipe(this.machine, recipe, io, Map.of(FluidRecipeCapability.CAP, fluids), chanceCaches, false);
                }
            } else {
                return super.handleRecipeIO(recipe, io);
            }
        }

        private boolean consumeOrderedItemInputs(GTRecipe recipe) {
            var itemInputs = recipe.inputs.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList());
            if (itemInputs.isEmpty()) return true;
            var machineInputs = getMachine().itemStackTransfers;
            if (machineInputs.size() < itemInputs.size()) return false;
            for (int i = 0; i < itemInputs.size(); i++) {
                var inputSlot = machineInputs.get(i);
                var recipeInput = ItemRecipeCapability.CAP.of(itemInputs.get(i).content);
                boolean tested = false;
                var amount = ItemUtils.getSizedAmount(recipeInput);
                for (int j = 0; j < inputSlot.size; j++) {
                    var stack = inputSlot.getStackInSlot(j);
                    if (stack.isEmpty() || (!tested && !recipeInput.test(stack))) continue;
                    tested = true;
                    amount -= inputSlot.extractItem(j, MathUtil.saturatedCast(amount), false).getCount();
                    if (amount <= 0) break;
                }
                if (amount > 0) return false;
            }
            return true;
        }

        private boolean consumeOrderedFluidInputs(GTRecipe recipe) {
            var fluidInputs = recipe.inputs.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList());
            if (fluidInputs.isEmpty()) return true;

            var machineInputs = getMachine().fluidTankTransfers;
            if (machineInputs.size() < fluidInputs.size()) return false;

            for (int i = 0; i < fluidInputs.size(); i++) {
                var inputTank = machineInputs.get(i);
                var recipeInput = FluidRecipeCapability.CAP.of(fluidInputs.get(i).content);

                if (inputTank.getFluid().isEmpty() ||
                        !recipeInput.test(inputTank.getFluid())) {
                    return false;
                }

                inputTank.drain(recipeInput.getAmount(), IFluidHandler.FluidAction.EXECUTE);
            }
            return true;
        }
    }
}

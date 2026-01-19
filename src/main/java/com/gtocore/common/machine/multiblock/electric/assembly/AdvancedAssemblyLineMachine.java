package com.gtocore.common.machine.multiblock.electric.assembly;

import com.gtocore.common.machine.multiblock.part.HugeBusPartMachine;
import com.gtocore.data.IdleReason;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
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
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.capability.IFluidHandler;

import com.hepdd.gtmthings.common.block.machine.multiblock.part.CreativeInputBusPartMachine;
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
    private final List<CustomFluidTank[]> fluidTankTransfers = new ReferenceArrayList<>();

    public AdvancedAssemblyLineMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Nullable
    @Override
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        var config = ConfigHolder.INSTANCE.machines;

        if (config.orderedAssemblyLineItems) {
            if (!checkItemInputs(recipe)) {
                setIdleReason(IdleReason.ORDERED_ITEM);
                return null;
            }
        }

        if (config.orderedAssemblyLineFluids) {
            if (!checkFluidInputs(recipe)) {
                setIdleReason(IdleReason.ORDERED_FLUID);
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
            var content = inputs.get(i).inner;
            if (content instanceof ItemIngredient ingredient && !ingredient.isEmpty()) {
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
            var content = inputs.get(i).inner;
            if (content instanceof FluidIngredient ingredient && !ingredient.isEmpty()) {
                if (!matchFluid(this.fluidTankTransfers.get(i), ingredient)) return false;
            }
        }

        return true;
    }

    /**
     * 验证给定的存储区是否仅包含与当前需求匹配的唯一种类物品。
     */
    private boolean matchItem(CustomItemStackHandler storage, ItemIngredient currentIngredient) {
        Item item = Items.AIR;
        for (int slot = 0; slot < storage.getSlots(); slot++) {
            var stack = storage.getStackInSlot(slot);
            Item providedItem = stack.getItem();
            if (providedItem == Items.AIR) continue;
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

        return item != Items.AIR;
    }

    /**
     * 验证给定的流体存储区是否与当前需求匹配。
     */
    private boolean matchFluid(CustomFluidTank[] storage, FluidIngredient currentIngredient) {
        var fluid = Fluids.EMPTY;
        for (var tank : storage) {
            var providedFluid = tank.getFluid().getFluid();
            if (providedFluid == Fluids.EMPTY) continue;
            if (providedFluid != fluid) {
                if (fluid != Fluids.EMPTY) {
                    return false;
                }

                if (!currentIngredient.testFluid(providedFluid)) {
                    return false;
                }

                fluid = providedFluid;
            }
        }
        return fluid != Fluids.EMPTY;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new AssemblyLineLogic(this);
    }

    @Override
    public Comparator<IMultiPart> getPartSorter() {
        return Comparator.comparing(p -> p.self().getPos(), RelativeDirection.RIGHT.getSorter(getFrontFacing(), getUpwardsFacing(), isFlipped()));
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        itemStackTransfers.clear();
        fluidTankTransfers.clear();
    }

    /**
     * 绑定物品和流体存储
     */
    @Override
    public void onStructureFormed() {
        itemStackTransfers.clear();
        fluidTankTransfers.clear();
        super.onStructureFormed();
    }

    @Override
    public void onPartScan(@NotNull IMultiPart part) {
        super.onPartScan(part);
        switch (part) {
            case ItemBusPartMachine itemBusPart -> {
                var inv = itemBusPart.getInventory();
                if (inv.handlerIO == IO.IN || inv.handlerIO == IO.BOTH) {
                    itemStackTransfers.add(inv.storage);
                }
            }
            case HugeBusPartMachine hugeBusPartMachine -> itemStackTransfers.add(hugeBusPartMachine.getInventory().storage);
            case FluidHatchPartMachine fluidHatchPartMachine -> fluidTankTransfers.add(fluidHatchPartMachine.tank.getStorages());
            case CreativeInputBusPartMachine creativeInputBusPartMachine -> itemStackTransfers.add(creativeInputBusPartMachine.getInventory().storage);
            case CreativeInputHatchPartMachine creativeInputHatchPartMachine -> fluidTankTransfers.add(creativeInputHatchPartMachine.tank.getStorages());
            default -> {}
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
            if (!(io == IO.IN)) {
                return super.handleRecipeIO(recipe, io);
            }
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
        }

        private boolean consumeOrderedItemInputs(GTRecipe recipe) {
            var itemInputs = recipe.inputs.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList());
            if (itemInputs.isEmpty()) return true;
            var machineInputs = getMachine().itemStackTransfers;
            if (machineInputs.size() < itemInputs.size()) return false;
            for (int i = 0; i < itemInputs.size(); i++) {
                var inputSlot = machineInputs.get(i);
                var recipeInput = ItemRecipeCapability.CAP.of(itemInputs.get(i));
                boolean tested = false;
                var amount = recipeInput.amount;
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
                var inputTankArray = machineInputs.get(i);
                var recipeInput = FluidRecipeCapability.CAP.of(fluidInputs.get(i));
                long amountToDrain = recipeInput.amount;

                for (var tankInHatch : inputTankArray) {
                    if (tankInHatch.isEmpty() || !recipeInput.test(tankInHatch.getFluid())) {
                        continue;
                    }
                    var drainedStack = tankInHatch.drain(MathUtil.saturatedCast(amountToDrain), IFluidHandler.FluidAction.EXECUTE);
                    amountToDrain -= drainedStack.getAmount();
                    if (amountToDrain <= 0) {
                        break;
                    }
                }

                if (amountToDrain > 0) {
                    return false;
                }
            }
            return true;
        }
    }
}

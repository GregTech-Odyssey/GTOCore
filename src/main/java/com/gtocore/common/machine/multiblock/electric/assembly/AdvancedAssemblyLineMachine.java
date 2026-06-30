package com.gtocore.common.machine.multiblock.electric.assembly;

import com.gtocore.common.machine.multiblock.part.HugeBusPartMachine;
import com.gtocore.data.IdleReason;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.capability.IFluidHandler;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public final class AdvancedAssemblyLineMachine extends ElectricMultiblockMachine {

    private final List<CustomItemStackHandler> itemStackTransfers = new ReferenceArrayList<>();
    private final List<CustomFluidTank[]> fluidTankTransfers = new ReferenceArrayList<>();

    public AdvancedAssemblyLineMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Nullable
    @Override
    public GTRecipe getRealRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        recipe = GTORecipeModifiers.parallel(this, unit, recipe);
        if (recipe == null) return null;
        return RecipeModifier.laserLossOverclocking(this, unit, recipe);
    }

    /**
     * 检查给定配方的物品输入是否与机器的物品存储区有序匹配。
     */
    private boolean checkItemInputs(GTRecipe recipe) {
        var inputs = recipe.itemInputs;
        if (inputs.isEmpty()) return true;
        if (itemStackTransfers.size() < inputs.size()) return false;
        for (int i = 0; i < inputs.size(); i++) {
            var content = inputs.get(i);
            if (!content.isEmpty()) {
                if (!matchItem(this.itemStackTransfers.get(i), content)) return false;
            }
        }
        return true;
    }

    /**
     * 检查给定配方的流体输入是否与机器的流体存储区有序匹配。
     */
    private boolean checkFluidInputs(GTRecipe recipe) {
        var inputs = recipe.fluidInputs;
        if (inputs.isEmpty()) return true;
        if (fluidTankTransfers.size() < inputs.size()) return false;
        for (int i = 0; i < inputs.size(); i++) {
            var content = inputs.get(i);
            if (!content.isEmpty()) {
                if (!matchFluid(this.fluidTankTransfers.get(i), content)) return false;
            }
        }
        return true;
    }

    /**
     * 验证给定的存储区是否仅包含与当前需求匹配的唯一种类物品。
     */
    private boolean matchItem(CustomItemStackHandler storage, Content<ItemIngredient> currentIngredient) {
        Item item = Items.AIR;
        for (int slot = 0; slot < storage.getSlots(); slot++) {
            var stack = storage.getStackInSlot(slot);
            Item providedItem = stack.getItem();
            if (providedItem == Items.AIR) continue;
            if (providedItem != item) {
                if (item != Items.AIR) {
                    return false;
                }

                if (!currentIngredient.inner.testItem(providedItem)) {
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
    private boolean matchFluid(CustomFluidTank[] storage, Content<FluidIngredient> currentIngredient) {
        var fluid = Fluids.EMPTY;
        for (var tank : storage) {
            var providedFluid = tank.getFluid().getFluid();
            if (providedFluid == Fluids.EMPTY) continue;
            if (providedFluid != fluid) {
                if (fluid != Fluids.EMPTY) {
                    return false;
                }

                if (!currentIngredient.inner.testFluid(providedFluid)) {
                    return false;
                }

                fluid = providedFluid;
            }
        }
        return fluid != Fluids.EMPTY;
    }

    @Override
    public boolean matchRecipeInput(RecipeHandlerUnit unit, GTRecipe recipe) {
        var config = ConfigHolder.INSTANCE.machines;
        if (config.orderedAssemblyLineItems) {
            if (!checkItemInputs(recipe)) {
                setIdleReason(IdleReason.ORDERED_ITEM);
                return false;
            }
        } else {
            var items = RecipeHelper.copyContents(recipe.itemInputs, 1);
            if (!unit.handleRecipeItem(IO.IN, recipe, items, true)) {
                return false;
            }
        }
        if (config.orderedAssemblyLineFluids) {
            if (!checkFluidInputs(recipe)) {
                setIdleReason(IdleReason.ORDERED_FLUID);
                return false;
            }
        } else {
            var fluids = RecipeHelper.copyContents(recipe.fluidInputs, 1);
            return unit.handleRecipeFluid(IO.IN, recipe, fluids, true);
        }
        return true;
    }

    @Override
    public boolean handleRecipeInput(RecipeHandlerUnit unit, GTRecipe recipe) {
        var items = RecipeHelper.copyAndRoll(recipe, recipe.itemInputs);
        var fluids = RecipeHelper.copyAndRoll(recipe, recipe.fluidInputs);
        if (ConfigHolder.INSTANCE.machines.orderedAssemblyLineItems) {
            if (!consumeOrderedItemInputs(items)) {
                return false;
            }
        } else {
            if (!unit.handleRecipeItem(IO.IN, recipe, items, false)) {
                return false;
            }
        }
        if (ConfigHolder.INSTANCE.machines.orderedAssemblyLineFluids) {
            return consumeOrderedFluidInputs(fluids);
        } else {
            return unit.handleRecipeFluid(IO.IN, recipe, fluids, false);
        }
    }

    private boolean consumeOrderedItemInputs(List<Content<ItemIngredient>> items) {
        if (items.isEmpty()) return true;
        var machineInputs = itemStackTransfers;
        if (machineInputs.size() < items.size()) return false;
        for (int i = 0; i < items.size(); i++) {
            var inputSlot = machineInputs.get(i);
            var recipeInput = items.get(i);
            boolean tested = false;
            for (int j = 0; j < inputSlot.size; j++) {
                var stack = inputSlot.getStackInSlot(j);
                if (stack.isEmpty() || (!tested && !recipeInput.inner.test(stack))) continue;
                tested = true;
                recipeInput.shrink(inputSlot.extract(j, recipeInput.getIntAmount(), false));
                if (recipeInput.amount <= 0) break;
            }
            if (recipeInput.amount > 0) return false;
        }
        return true;
    }

    private boolean consumeOrderedFluidInputs(List<Content<FluidIngredient>> fluids) {
        if (fluids.isEmpty()) return true;
        var machineInputs = fluidTankTransfers;
        if (machineInputs.size() < fluids.size()) return false;
        for (int i = 0; i < fluids.size(); i++) {
            var inputTankArray = machineInputs.get(i);
            var recipeInput = fluids.get(i);
            for (var tankInHatch : inputTankArray) {
                if (tankInHatch.isEmpty() || !recipeInput.inner.test(tankInHatch.getFluid())) continue;
                recipeInput.shrink(tankInHatch.drain(recipeInput.getIntAmount(), IFluidHandler.FluidAction.EXECUTE).getAmount());
                if (recipeInput.amount <= 0) break;
            }
            if (recipeInput.amount > 0) {
                return false;
            }
        }
        return true;
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
            default -> {}
        }
    }
}

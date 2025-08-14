package com.gtocore.common.machine.multiblock.electric.assembly;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public final class CircuitAssemblyLineMachine extends StorageMultiblockMachine {

    private int inputEUt;

    private int parallel;

    public CircuitAssemblyLineMachine(MetaMachineBlockEntity holder) {
        super(holder, 64, i -> ItemUtils.getId(i).contains("precision_circuit_assembly_robot_mk"));
    }

    @Override
    public @NotNull NotifiableItemStackHandler createMachineStorage(Predicate<ItemStack> filter) {
        NotifiableItemStackHandler storage = new NotifiableItemStackHandler(
                this, 1, IO.IN, IO.BOTH, slots -> new CustomItemStackHandler(1) {

                    @Override
                    public void onContentsChanged(int slot) {
                        super.onContentsChanged(slot);
                        onMachineChanged();
                    }
                });
        storage.setFilter(filter);
        return storage;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onMachineChanged();
    }

    @Override
    public void onMachineChanged() {
        inputEUt = 0;
        ItemStack item = getStorageStack();
        if (item.getItem() == GTOItems.PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK1.get()) {
            inputEUt = GTValues.VA[GTValues.UV];
        } else if (item.getItem() == GTOItems.PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK2.get()) {
            inputEUt = GTValues.VA[GTValues.UHV];
        } else if (item.getItem() == GTOItems.PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK3.get()) {
            inputEUt = GTValues.VA[GTValues.UEV];
        } else if (item.getItem() == GTOItems.PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK4.get()) {
            inputEUt = GTValues.VA[GTValues.UIV];
        } else if (item.getItem() == GTOItems.PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK5.get()) {
            inputEUt = GTValues.VA[GTValues.UXV];
        }
        parallel = item.getCount() << 1;
    }

    @Nullable
    @Override
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        if (inputEUt == recipe.getInputEUt()) {
            recipe = ParallelLogic.accurateParallel(this, recipe, parallel);
        }
        return RecipeModifierFunction.overclocking(this, recipe);
    }
}

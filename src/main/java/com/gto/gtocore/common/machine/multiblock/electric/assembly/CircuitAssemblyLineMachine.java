package com.gto.gtocore.common.machine.multiblock.electric.assembly;

import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public final class CircuitAssemblyLineMachine extends StorageMultiblockMachine {

    private int inputEUt;

    private int parallel;

    public CircuitAssemblyLineMachine(IMachineBlockEntity holder) {
        super(holder, 64, i -> ItemUtils.getId(i).contains("precision_circuit_assembly_robot_mk"));
    }

    @Override
    protected @NotNull NotifiableItemStackHandler createMachineStorage(Function<ItemStack, Boolean> filter) {
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
        List<IRecipeHandler<?>> itemProxy = getCapabilitiesProxy().get(IO.IN, ItemRecipeCapability.CAP);
        if (itemProxy != null) {
            itemProxy.add(machineStorage);
        }
    }

    @Override
    protected void onMachineChanged() {
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
    protected GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        if (inputEUt == RecipeHelper.getInputEUt(recipe)) {
            recipe = GTORecipeModifiers.accurateParallel(this, recipe, parallel);
        }
        return GTORecipeModifiers.overclocking(this, recipe);
    }
}

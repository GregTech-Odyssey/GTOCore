package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.StorageMachine;
import com.gto.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CircuitAssemblyLineMachine extends StorageMachine {

    private int inputEUt;

    private int parallel;

    public CircuitAssemblyLineMachine(IMachineBlockEntity holder) {
        super(holder, 64, i -> i.kjs$getId().contains("precision_circuit_assembly_robot_mk"));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            machineStorage.addChangedListener(this::onMachineChanged);
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onMachineChanged();
    }

    protected void onMachineChanged() {
        inputEUt = 0;
        ItemStack item = getMachineStorageItem();
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
        parallel = item.getCount() * 2;
    }

    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params,
                                          @NotNull OCResult result) {
        if (machine instanceof CircuitAssemblyLineMachine circuitAssemblyLineMachine && circuitAssemblyLineMachine.inputEUt == RecipeHelper.getInputEUt(recipe)) {
            return GTRecipeModifiers.accurateParallel(machine, recipe, circuitAssemblyLineMachine.parallel, false).getFirst();
        }
        return recipe;
    }
}

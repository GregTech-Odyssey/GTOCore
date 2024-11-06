package com.gto.gtocore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;

import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class AdvancedAssemblyLineMachine extends WorkableElectricMultiblockMachine {

    public AdvancedAssemblyLineMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    private List<ItemStackTransfer> itemStackTransfers = new ArrayList<>();

    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, OCParams params, OCResult result) {
        if (machine instanceof AdvancedAssemblyLineMachine lineMachine) {
            Ingredient[] recipeIngredients = recipe.inputs.get(ItemRecipeCapability.CAP).stream().map(i -> ItemRecipeCapability.CAP.of(i.getContent())).toArray(Ingredient[]::new);
            int size = recipeIngredients.length;
            if (lineMachine.itemStackTransfers.size() < size) return null;
            Ingredient[] matchIngredients = new Ingredient[size];
            ItemStack recipeStack = recipeIngredients[0].getItems()[0];
            matchIngredients[0] = recipeIngredients[0];
            for (int i = 1; i < size; i++) {
                Ingredient currentIngredient = recipeIngredients[i];
                if (currentIngredient.test(recipeStack)) {
                    matchIngredients[i - 1] = Ingredient.EMPTY;
                    matchIngredients[i] = Ingredient.EMPTY;
                } else {
                    matchIngredients[i] = currentIngredient;
                }
                recipeStack = currentIngredient.getItems()[0];
            }
            for (int i = 0; i < size; i++) {
                Ingredient currentIngredient = matchIngredients[i];
                if (currentIngredient.isEmpty()) continue;
                ItemStackTransfer storage = lineMachine.itemStackTransfers.get(i);
                Set<Item> itemSet = new HashSet<>();
                ItemStack stack = ItemStack.EMPTY;
                for (int j = 0; j < storage.getSlots(); j++) {
                    ItemStack item = storage.getStackInSlot(j);
                    if (!item.isEmpty()) {
                        itemSet.add(item.getItem());
                        stack = item;
                    }
                }
                if (itemSet.size() != 1) return null;
                if (!currentIngredient.test(stack)) return null;
            }
            GTRecipe recipe1 = GTRecipeModifiers.hatchParallel(machine, recipe, false, params, result);
            if (recipe1 == null) return null;
            return RecipeHelper.applyOverclock(new OverclockingLogic(0.625, 4, false),
                    recipe1, lineMachine.getOverclockVoltage(), params, result);
        }
        return null;
    }

    @Override
    public void onStructureFormed() {
        getDefinition().setPartSorter(Comparator.comparing(it -> multiblockPartSorter().apply(it.self().getPos())));
        super.onStructureFormed();
        itemStackTransfers = getParts().stream().filter(ItemBusPartMachine.class::isInstance).map(ItemBusPartMachine.class::cast).map(i -> i.getInventory().storage).toList();
    }

    private Function<BlockPos, Integer> multiblockPartSorter() {
        return RelativeDirection.RIGHT.getSorter(getFrontFacing(), getUpwardsFacing(), isFlipped());
    }
}

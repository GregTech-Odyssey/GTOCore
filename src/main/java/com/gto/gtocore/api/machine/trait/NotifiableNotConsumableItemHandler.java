package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.IntFunction;

public class NotifiableNotConsumableItemHandler extends NotifiableItemStackHandler {

    public NotifiableNotConsumableItemHandler(MetaMachine machine, int slots, @NotNull IO capabilityIO, IntFunction<CustomItemStackHandler> storageFactory) {
        super(machine, slots, IO.IN, capabilityIO, storageFactory);
    }

    public NotifiableNotConsumableItemHandler(MetaMachine machine, int slots, @NotNull IO capabilityIO) {
        this(machine, slots, capabilityIO, CustomItemStackHandler::new);
    }

    @Override
    public boolean shouldSearchContent() {
        return false;
    }

    @Override
    public List<Ingredient> handleRecipe(IO io, GTRecipe recipe, List<?> left, boolean simulate) {
        return handleRecipeInner(io, recipe, (List<Ingredient>) left, true);
    }

    @Override
    public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate) {
        if (simulate && io == IO.IN) {
            left = new ObjectArrayList<>(left);
            for (var it = left.listIterator(); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }
                var items = ItemUtils.getInnerIngredient(ingredient).getItems();
                if (items.length == 0 || items[0].isEmpty()) {
                    it.remove();
                    continue;
                }
                int amount;
                if (ingredient instanceof SizedIngredient si) amount = si.getAmount();
                else amount = items[0].getCount();
                for (int slot = 0; slot < storage.getSlots(); ++slot) {
                    ItemStack stored = storage.getStackInSlot(slot);
                    int count = stored.getCount();
                    if (count == 0) continue;
                    if (ingredient.test(stored)) {
                        amount -= count;
                    }
                    if (amount < 1) {
                        it.remove();
                        break;
                    }
                }
            }
        }
        return left.isEmpty() ? null : left;
    }
}

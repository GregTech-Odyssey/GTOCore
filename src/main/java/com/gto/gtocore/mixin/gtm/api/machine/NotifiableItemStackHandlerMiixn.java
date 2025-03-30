package com.gto.gtocore.mixin.gtm.api.machine;

import com.gto.gtocore.api.recipe.FastSizedIngredient;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntProviderIngredient;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(NotifiableItemStackHandler.class)
public class NotifiableItemStackHandlerMiixn {

    /**
     * @author .
     * @reason 换成FastSizedIngredient
     */
    @Overwrite(remap = false)
    public static List<Ingredient> handleRecipe(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate, IO handlerIO, CustomItemStackHandler storage) {
        if (io != handlerIO) return left;
        if (io != IO.IN && io != IO.OUT) return left.isEmpty() ? null : left;
        ItemStack[] visited = new ItemStack[storage.getSlots()];
        for (var it = left.listIterator(); it.hasNext();) {
            var ingredient = it.next();
            if (ingredient.isEmpty()) {
                it.remove();
                continue;
            }
            if (io == IO.OUT && ingredient instanceof IntProviderIngredient provider) {
                provider.setItemStacks(null);
                provider.setSampledCount(null);
            }
            var items = ingredient.getItems();
            if (items.length == 0 || items[0].isEmpty()) {
                it.remove();
                continue;
            }
            int amount;
            if (ingredient instanceof FastSizedIngredient si) amount = si.getAmount();
            else amount = items[0].getCount();
            for (int slot = 0; slot < storage.getSlots(); ++slot) {
                ItemStack stored = storage.getStackInSlot(slot);
                int count = (visited[slot] == null ? stored.getCount() : visited[slot].getCount());
                if (io == IO.IN) {
                    if (count == 0) continue;
                    if ((visited[slot] == null && ingredient.test(stored)) || ingredient.test(visited[slot])) {
                        var extracted = storage.extractItem(slot, Math.min(count, amount), simulate);
                        if (!extracted.isEmpty()) {
                            visited[slot] = extracted.copyWithCount(count - extracted.getCount());
                        }
                        amount -= extracted.getCount();
                    }
                } else {
                    ItemStack output = items[0].copyWithCount(amount);
                    if (visited[slot] == null || visited[slot].is(output.getItem())) {
                        if (count < output.getMaxStackSize() && count < storage.getSlotLimit(slot)) {
                            var remainder = storage.insertItem(slot, output, simulate);
                            if (remainder.getCount() < amount) {
                                visited[slot] = output.copyWithCount(count + amount - remainder.getCount());
                            }
                            amount = remainder.getCount();
                        }
                    }
                }
                if (amount <= 0) {
                    it.remove();
                    break;
                }
            }
            if (amount > 0) {
                if (ingredient instanceof FastSizedIngredient si) {
                    si.setAmount(amount);
                } else {
                    items[0].setCount(amount);
                }
            }
        }
        return left.isEmpty() ? null : left;
    }
}

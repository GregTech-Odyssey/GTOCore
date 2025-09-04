package com.gtocore.mixin.gtm.api.machine;

import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gtolib.api.recipe.ingredient.SimpleIngredient;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandlerModifiable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(NotifiableItemStackHandler.class)
public abstract class NotifiableItemStackHandlerMiixn implements IItemHandlerModifiable {

    /**
     * @author .
     * @reason 换成FastSizedIngredient
     */
    @Overwrite(remap = false)
    public static List<Ingredient> handleRecipe(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate, IO handlerIO, CustomItemStackHandler storage) {
        if (io != handlerIO) return left;
        if (io != IO.IN && io != IO.OUT) return left.isEmpty() ? null : left;
        Runnable listener = null;
        if (!simulate) {
            listener = storage.getOnContentsChanged();
            storage.setOnContentsChangedAndfreeze(GTUtil.NOOP);
        }
        boolean changed = false;
        SimpleIngredient[] visited = new SimpleIngredient[storage.size];
        for (var it = left.listIterator(0); it.hasNext();) {
            var ingredient = it.next();
            if (ingredient.isEmpty()) {
                it.remove();
                continue;
            }
            var items = FastSizedIngredient.getInner(ingredient).getItems();
            if (items.length == 0 || items[0].isEmpty()) {
                it.remove();
                continue;
            }
            var itemStack = items[0];
            var item = itemStack.getItem();
            long amount;
            if (ingredient instanceof FastSizedIngredient si) amount = si.getAmount();
            else amount = itemStack.getCount();
            if (amount < 1) {
                it.remove();
                continue;
            }
            for (int slot = 0; slot < storage.size; ++slot) {
                ItemStack stored = storage.stacks[slot];
                int count = (visited[slot] == null ? stored.getCount() : visited[slot].amount());
                if (io == IO.IN) {
                    if (count == 0) continue;
                    if ((visited[slot] == null && ingredient.test(stored)) || (visited[slot] != null && ingredient.test(visited[slot].stack()))) {
                        var extracted = storage.extractItem(slot, Math.min(count, MathUtil.saturatedCast(amount)), simulate).getCount();
                        if (extracted > 0) {
                            if (simulate) {
                                visited[slot] = new SimpleIngredient(stored, count - extracted);
                            }
                            changed = true;
                            amount -= extracted;
                        }
                    }
                } else {
                    if (count < itemStack.getMaxStackSize() && count < storage.getSlotLimit(slot) && (count == 0 || stored.is(item)) && (visited[slot] == null || visited[slot].stack().is(item))) {
                        var remainder = storage.insertItemFast(slot, itemStack, MathUtil.saturatedCast(amount), simulate);
                        if (remainder < amount) {
                            if (simulate) {
                                visited[slot] = new SimpleIngredient(itemStack, remainder);
                            }
                            changed = true;
                            amount = remainder;
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
                    items[0].setCount(MathUtil.saturatedCast(amount));
                }
            }
        }
        if (listener != null) {
            storage.setOnContentsChangedAndfreeze(listener);
            if (changed) listener.run();
        }
        return left.isEmpty() ? null : left;
    }
}

package com.gto.gtocore.mixin.gtm.api.machine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntProviderIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(NotifiableItemStackHandler.class)
public abstract class NotifiableItemStackHandlerMixin {

    @Inject(method = "handleRecipe", at = @At("HEAD"), remap = false, cancellable = true)
    private static void handleRecipe(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate, IO handlerIO, CustomItemStackHandler storage, CallbackInfoReturnable<List<Ingredient>> cir) {
        if (simulate) return;
        cir.setReturnValue(gtocore$handle(io, left, handlerIO, storage));
    }

    @Unique
    private synchronized static List<Ingredient> gtocore$handle(IO io, List<Ingredient> left, IO handlerIO, CustomItemStackHandler storage) {
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
            if (ingredient instanceof SizedIngredient si) amount = si.getAmount();
            else amount = items[0].getCount();
            for (int slot = 0; slot < storage.getSlots(); ++slot) {
                ItemStack stored = storage.getStackInSlot(slot);
                int count = (visited[slot] == null ? stored.getCount() : visited[slot].getCount());
                if (io == IO.IN) {
                    if (count == 0) continue;
                    if ((visited[slot] == null && ingredient.test(stored)) || ingredient.test(visited[slot])) {
                        var extracted = storage.extractItem(slot, Math.min(count, amount), false);
                        if (!extracted.isEmpty()) {
                            visited[slot] = extracted.copyWithCount(count - extracted.getCount());
                        }
                        amount -= extracted.getCount();
                    }
                } else {
                    // IO.OUT
                    ItemStack output = items[0].copyWithCount(amount);
                    // Only try this slot if not visited or if visited with the same type of item
                    if (visited[slot] == null || visited[slot].is(output.getItem())) {
                        if (count < output.getMaxStackSize() && count < storage.getSlotLimit(slot)) {
                            var remainder = storage.insertItem(slot, output, false);
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
            // Modify ingredient if we didn't finish it off
            if (amount > 0) {
                if (ingredient instanceof SizedIngredient si) {
                    si.setAmount(amount);
                } else {
                    items[0].setCount(amount);
                }
            }
        }
        return left.isEmpty() ? null : left;
    }
}

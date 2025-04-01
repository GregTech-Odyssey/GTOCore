package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.recipe.FastSizedIngredient;
import com.gto.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class NotifiableCatalystHandler extends NotifiableItemStackHandler {

    private final boolean damage;

    public NotifiableCatalystHandler(MetaMachine machine, int slots, boolean damage) {
        super(machine, slots, IO.IN, IO.BOTH);
        this.damage = damage;
        setFilter(i -> ChemicalHelper.getPrefix(i.getItem()) == GTOTagPrefix.CATALYST || i.is(GTOItems.CATALYST_BASE.get()));
    }

    @Override
    public @NotNull List<Object> getContents() {
        List stacks = new ArrayList<>();
        if (machine instanceof IControllable controllable && controllable.isWorkingEnabled()) {
            for (int i = 0; i < getSlots(); ++i) {
                ItemStack stack = getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Item item = stack.getItem();
                    int damage = 10000 - stack.getDamageValue();
                    stacks.add(new ItemStack(item, damage * damage));
                }
            }
        }
        return stacks;
    }

    @Override
    public double getTotalContentAmount() {
        long amount = 0;
        if (machine instanceof IControllable controllable && controllable.isWorkingEnabled()) {
            for (int i = 0; i < getSlots(); ++i) {
                ItemStack stack = getStackInSlot(i);
                if (!stack.isEmpty()) {
                    long damage = 10000 - stack.getDamageValue();
                    amount += damage * damage;
                }
            }
        }
        return amount;
    }

    @Override
    public void importFromNearby(@NotNull Direction... facings) {}

    @Override
    @Nullable
    public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate) {
        if (io == IO.IN && machine instanceof IControllable controllable && controllable.isWorkingEnabled()) {
            for (var it = left.listIterator(); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }
                var items = ingredient.getItems();
                if (items.length == 0 || items[0].isEmpty()) {
                    it.remove();
                    continue;
                }
                int count;
                if (ingredient instanceof FastSizedIngredient si) count = si.getAmount();
                else count = items[0].getCount();
                for (int slot = 0; slot < storage.getSlots(); ++slot) {
                    ItemStack stored = storage.getStackInSlot(slot);
                    if (items[0].is(stored.getItem())) {
                        int damageValue = stored.getDamageValue();
                        if (damageValue > 9999) {
                            storage.setStackInSlot(slot, GTOItems.CATALYST_BASE.asStack());
                            continue;
                        }
                        int amount = Math.min((int) Math.ceil(Math.sqrt(count)), 10000 - damageValue);
                        if (!simulate && damage) stored.setDamageValue(damageValue + amount);
                        count = count - amount * amount;
                        if (count < 1) {
                            it.remove();
                            break;
                        }
                    }
                }
                if (count > 0) {
                    if (ingredient instanceof FastSizedIngredient si) {
                        si.setAmount(count);
                    } else {
                        items[0].setCount(count);
                    }
                }
            }
        }
        return left.isEmpty() ? null : left;
    }
}

package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gtolib.api.recipe.ingredient.FastSizedIngredient;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.function.ItemConsumer;
import com.gregtechceu.gtceu.api.capability.recipe.function.ItemPredicate;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.lookup.IntIngredientMap;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlotList;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import appeng.api.stacks.GenericStack;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class ExportOnlyAEItemList extends NotifiableItemStackHandler implements IConfigurableSlotList {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ExportOnlyAEItemList.class, NotifiableItemStackHandler.MANAGED_FIELD_HOLDER);

    @Persisted
    private final ExportOnlyAEItemSlot[] inventory;

    public ExportOnlyAEItemList(MetaMachine holder, int slots) {
        this(holder, slots, ExportOnlyAEItemSlot::new);
    }

    ExportOnlyAEItemList(MetaMachine holder, int slots, Supplier<ExportOnlyAEItemSlot> slotFactory) {
        super(holder, 0, IO.IN, IO.NONE, i -> new ItemStackHandlerDelegate());
        ((ItemStackHandlerDelegate) storage).list = this;
        this.inventory = new ExportOnlyAEItemSlot[slots];
        for (int i = 0; i < slots; i++) {
            this.inventory[i] = slotFactory.get();
            this.inventory[i].setOnContentsChanged(this::onContentsChanged);
        }
    }

    @Override
    public void onContentsChanged() {
        super.onContentsChanged();
        changed = true;
    }

    @Override
    public boolean isEmpty() {
        if (isEmpty == null) {
            isEmpty = true;
            for (var i : inventory) {
                if (i.config == null) continue;
                var stock = i.stock;
                if (stock == null || stock.amount() == 0) continue;
                var stack = i.getStack();
                if (stack.isEmpty()) continue;
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

    @Override
    public int getSlotLimit(int slot) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getSlots() {
        return inventory.length;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {}

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.inventory[slot].getStack();
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack;
    }

    @NotNull
    @Override
    public ItemStack extractItemInternal(int slot, int amount, boolean simulate) {
        return this.inventory[slot].extractItem(0, amount, simulate);
    }

    @Override
    public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate) {
        if (io == IO.IN) {
            boolean changed = false;
            for (var it = left.listIterator(0); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }
                long amount;
                if (ingredient instanceof FastSizedIngredient si) amount = si.getAmount();
                else amount = 1;
                if (amount < 1) {
                    it.remove();
                    continue;
                }
                for (var i : inventory) {
                    GenericStack stored = i.stock;
                    if (stored == null) continue;
                    long count = stored.amount();
                    if (count == 0) continue;
                    if (ingredient.test(i.getStack())) {
                        var extracted = i.extractItem(Math.min(count, amount), simulate, false);
                        if (extracted > 0) {
                            changed = true;
                            amount -= extracted;
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
                    }
                }
            }
            if (!simulate && changed) {
                onContentsChanged();
            }
        }
        return left.isEmpty() ? null : left;
    }

    @Override
    public boolean forEachItems(ItemPredicate function) {
        for (var i : inventory) {
            if (i.config == null) continue;
            var stock = i.stock;
            if (stock == null || stock.amount() == 0) continue;
            var stack = i.getStack();
            if (stack.isEmpty()) continue;
            if (function.test(stack, stock.amount())) return true;
        }
        return false;
    }

    @Override
    public void fastForEachItems(ItemConsumer function) {
        for (var i : inventory) {
            if (i.config == null) continue;
            var stock = i.stock;
            if (stock == null || stock.amount() == 0) continue;
            var stack = i.getStack();
            if (stack.isEmpty()) continue;
            function.accept(stack, stock.amount());
        }
    }

    @Override
    public IntIngredientMap getIngredientMap() {
        if (changed) {
            changed = false;
            intIngredientMap.clear();
            for (var i : inventory) {
                if (i.config == null) continue;
                var stock = i.stock;
                if (stock == null || stock.amount() == 0) continue;
                var stack = i.getStack();
                if (stack.isEmpty()) continue;
                IntIngredientMap.ITEM_CONVERSION.convert(stack, stock.amount(), intIngredientMap);
            }
        }
        return intIngredientMap;
    }

    @Override
    public IConfigurableSlot getConfigurableSlot(int index) {
        return inventory[index];
    }

    @Override
    public int getConfigurableSlots() {
        return inventory.length;
    }

    public boolean isAutoPull() {
        return false;
    }

    public boolean isStocking() {
        return false;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static final class ItemStackHandlerDelegate extends CustomItemStackHandler {

        private ExportOnlyAEItemList list;

        private ItemStackHandlerDelegate() {
            super();
        }

        @Override
        public int getSlots() {
            return list.inventory.length;
        }

        @Override
        @NotNull
        public ItemStack getStackInSlot(int slot) {
            return list.inventory[slot].getStack();
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack stack) {}

        @Override
        @NotNull
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        @NotNull
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (amount == 0) return ItemStack.EMPTY;
            return list.inventory[slot].extractItem(0, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    }

    public ExportOnlyAEItemSlot[] getInventory() {
        return this.inventory;
    }
}

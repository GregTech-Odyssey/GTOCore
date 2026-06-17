package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gtolib.api.recipe.RecipeType;
import com.gtolib.api.recipe.lookup.IIngredientConvertible;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableContentHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;
import com.gregtechceu.gtceu.api.transfer.item.ICustomItemStackHandler;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlotList;
import com.gregtechceu.gtceu.utils.function.ObjLongPredicate;

import net.minecraft.world.item.ItemStack;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;

import com.fast.recipesearch.IntLongMap;
import com.gto.datasynclib.annotations.SaveToDisk;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;

@Getter
public class ExportOnlyAEItemList extends NotifiableContentHandler implements ICustomItemStackHandler, IConfigurableSlotList {

    @SaveToDisk
    final ExportOnlyAEItemSlot[] inventory;

    public ExportOnlyAEItemList(MetaMachine holder, int slots) {
        this(holder, slots, ExportOnlyAEItemSlot::new);
    }

    ExportOnlyAEItemList(MetaMachine holder, int slots, Supplier<ExportOnlyAEItemSlot> slotFactory) {
        super(holder, IO.IN);
        this.inventory = new ExportOnlyAEItemSlot[slots];
        for (int i = 0; i < slots; i++) {
            this.inventory[i] = slotFactory.get();
            this.inventory[i].setHandler(this);
        }
    }

    @Override
    public boolean updateEmpty() {
        for (var i : inventory) {
            if (i.config == null) continue;
            var stock = i.stock;
            if (stock == null || stock.amount() == 0) continue;
            return false;
        }
        return true;
    }

    @Override
    public int getSlotLimit(int slot) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack itemStack) {
        return true;
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

    @Override
    public @NotNull ItemStack extractItem(int i, int i1, boolean b) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItemInternal(int slot, int amount, boolean simulate) {
        var inv = inventory[slot];
        var stack = inv.getStack();
        if (stack.isEmpty()) return ItemStack.EMPTY;
        amount = MathUtil.saturatedCast(inv.extract(amount, simulate, true));
        if (amount < 1) return ItemStack.EMPTY;
        return stack.copyWithCount(amount);
    }

    @Override
    public boolean canHandleItem() {
        return true;
    }

    @Override
    public boolean handleRecipeItem(IO io, GTRecipe recipe, List<Content<ItemIngredient>> items, boolean simulate) {
        if (io == IO.IN) {
            boolean changed = false;
            for (var it = items.iterator(); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }
                for (var i : inventory) {
                    GenericStack stored = i.stock;
                    if (stored == null) continue;
                    long count = stored.amount();
                    if (count == 0) continue;
                    if (stored.what() instanceof AEItemKey itemKey && ingredient.inner.testAeKay(itemKey)) {
                        var extracted = i.extract(ingredient.amount, simulate, false);
                        if (extracted > 0) {
                            changed = true;
                            ingredient.shrink(extracted);
                            if (ingredient.amount <= 0) {
                                it.remove();
                                break;
                            }
                        }
                    }
                }
            }
            if (!simulate && changed) {
                onContentsChanged();
            }
        }
        return items.isEmpty();
    }

    @Override
    public boolean forEachItems(ObjLongPredicate<ItemStack> function) {
        for (var i : inventory) {
            if (i.config == null) continue;
            var stock = i.stock;
            if (stock == null || stock.amount() == 0) continue;
            if (function.test(i.getReadOnlyStack(), stock.amount())) return true;
        }
        return false;
    }

    @Override
    public void fastForEachItems(ObjLongConsumer<ItemStack> function) {
        for (var i : inventory) {
            if (i.config == null) continue;
            var stock = i.stock;
            if (stock == null || stock.amount() == 0) continue;
            function.accept(i.getReadOnlyStack(), stock.amount());
        }
    }

    @Override
    public void fillSearchMap(@NotNull GTRecipeType type, @NotNull IntLongMap map) {
        boolean specialConverter = ((RecipeType) type).specialConverter;
        for (var i : inventory) {
            if (i.config == null) continue;
            var stock = i.stock;
            if (stock == null || stock.amount() == 0) continue;
            if (stock.what() instanceof AEItemKey itemKey) {
                if (specialConverter) {
                    type.convertItem(i.getReadOnlyStack(), stock.amount(), map);
                } else {
                    ((IIngredientConvertible) (Object) itemKey).gtolib$convert(stock.amount(), map);
                }
            }
        }
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
}

package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gtocore.common.machine.multiblock.part.ae.MEStockingBusPartMachine;

import com.gtolib.api.ae2.IExpandedStorageService;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.function.ItemConsumer;
import com.gregtechceu.gtceu.api.capability.recipe.function.ItemPredicate;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.lookup.IntIngredientMap;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExportOnlyAEStockingItemList extends ExportOnlyAEItemList {

    private final MEStockingBusPartMachine machine;

    public ExportOnlyAEStockingItemList(MEStockingBusPartMachine holder, int slots) {
        super(holder, slots, () -> new ExportOnlyAEStockingItemSlot(holder));
        this.machine = holder;
    }

    @Override
    public boolean forEachItems(ItemPredicate function) {
        if (machine.isWorkingEnabled()) return super.forEachItems(function);
        return false;
    }

    @Override
    public void fastForEachItems(ItemConsumer function) {
        if (machine.isWorkingEnabled()) super.fastForEachItems(function);
    }

    @Override
    public IntIngredientMap getIngredientMap() {
        if (machine.isWorkingEnabled()) return super.getIngredientMap();
        return IntIngredientMap.EMPTY;
    }

    @Override
    public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate) {
        if (machine.isWorkingEnabled()) return super.handleRecipeInner(io, recipe, left, simulate);
        return left;
    }

    @Override
    public boolean isAutoPull() {
        return machine.isAutoPull();
    }

    @Override
    public boolean isStocking() {
        return true;
    }

    @Override
    public boolean hasStackInConfig(GenericStack stack, boolean checkExternal) {
        boolean inThisBus = super.hasStackInConfig(stack, false);
        if (inThisBus) return true;
        if (checkExternal) {
            return machine.testConfiguredInOtherPart(stack);
        }
        return false;
    }

    private static final class ExportOnlyAEStockingItemSlot extends ExportOnlyAEItemSlot {

        private final MEStockingBusPartMachine machine;

        private ExportOnlyAEStockingItemSlot(MEStockingBusPartMachine machine) {
            super();
            this.machine = machine;
        }

        private ExportOnlyAEStockingItemSlot(MEStockingBusPartMachine machine, @Nullable GenericStack config, @Nullable GenericStack stock) {
            super(config, stock);
            this.machine = machine;
        }

        @Override
        public long extractItem(long amount, boolean simulate, boolean notify) {
            if (this.stock != null && this.config != null) {
                if (!machine.isOnline()) return 0;
                var grid = machine.getMainNode().getGrid();
                if (grid == null) return 0;
                long extracted = simulate ? Math.min(amount, IExpandedStorageService.of(grid.getStorageService()).getLazyKeyCounter().get(stock.what())) : grid.getStorageService().getInventory().extract(stock.what(), amount, Actionable.MODULATE, machine.getActionSource());
                if (extracted > 0) {
                    if (!simulate) {
                        this.stock = ExportOnlyAESlot.copy(stock, stock.amount() - extracted);
                        if (this.stock.amount() == 0) {
                            this.stock = null;
                            stack = null;
                        } else if (stack != null) stack.setCount(MathUtil.saturatedCast(stock.amount()));
                        if (notify) onContentsChanged();
                    }
                    return extracted;
                }
            }
            return 0;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == 0 && this.stock != null && this.config != null) {
                if (!machine.isOnline()) return ItemStack.EMPTY;
                var grid = machine.getMainNode().getGrid();
                if (grid == null) return ItemStack.EMPTY;
                var key = config.what();
                long extracted = simulate ? Math.min(amount, IExpandedStorageService.of(grid.getStorageService()).getLazyKeyCounter().get(key)) : grid.getStorageService().getInventory().extract(key, amount, Actionable.MODULATE, machine.getActionSource());
                if (extracted > 0) {
                    ItemStack resultStack = key instanceof AEItemKey itemKey ? itemKey.toStack((int) extracted) : ItemStack.EMPTY;
                    if (!simulate) {
                        this.stock = ExportOnlyAESlot.copy(stock, stock.amount() - extracted);
                        if (this.stock.amount() == 0) {
                            this.stock = null;
                            stack = null;
                        } else if (stack != null) stack.setCount(MathUtil.saturatedCast(stock.amount()));
                        onContentsChanged();
                    }
                    return resultStack;
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public @NotNull ExportOnlyAEStockingItemSlot copy() {
            return new ExportOnlyAEStockingItemSlot(machine, this.config == null ? null : copy(this.config), this.stock == null ? null : copy(this.stock));
        }
    }
}

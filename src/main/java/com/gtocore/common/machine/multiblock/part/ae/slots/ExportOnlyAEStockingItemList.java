package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gtocore.common.machine.multiblock.part.ae.MEStockingBusPartMachine;

import com.gtolib.api.ae2.stacks.IAEItemKey;
import com.gtolib.api.ae2.stacks.IKeyCounter;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.lookup.IntIngredientMap;
import com.gregtechceu.gtceu.utils.function.ObjectLongConsumer;
import com.gregtechceu.gtceu.utils.function.ObjectLongPredicate;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
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
    public boolean forEachItems(ObjectLongPredicate<ItemStack> function) {
        if (machine.isWorkingEnabled()) {
            if (!machine.isOnline()) return false;
            var grid = machine.getMainNode().getGrid();
            if (grid == null) return false;
            Reference2LongOpenHashMap<AEKey> map = null;
            for (var i : inventory) {
                if (i.config == null) continue;
                var stock = i.stock;
                if (stock == null) continue;
                if (map == null) {
                    map = IKeyCounter.of(grid.getStorageService().getCachedInventory()).gtolib$getMap();
                    if (map == null) break;
                }
                var amount = ((ExportOnlyAEStockingItemSlot) i).refresh(map, stock.amount(), stock.what());
                if (amount < 1) continue;
                if (function.test(i.getReadOnlyStack(), amount)) return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void fastForEachItems(ObjectLongConsumer<ItemStack> function) {
        if (machine.isWorkingEnabled()) {
            if (!machine.isOnline()) return;
            var grid = machine.getMainNode().getGrid();
            if (grid == null) return;
            Reference2LongOpenHashMap<AEKey> map = null;
            for (var i : inventory) {
                if (i.config == null) continue;
                var stock = i.stock;
                if (stock == null) continue;
                if (map == null) {
                    map = IKeyCounter.of(grid.getStorageService().getCachedInventory()).gtolib$getMap();
                    if (map == null) break;
                }
                var amount = ((ExportOnlyAEStockingItemSlot) i).refresh(map, stock.amount(), stock.what());
                if (amount < 1) continue;
                function.accept(i.getReadOnlyStack(), amount);
            }
        }
    }

    @Override
    public IntIngredientMap getIngredientMap() {
        if (machine.isWorkingEnabled()) {
            if (changed) {
                if (!machine.isOnline()) return IntIngredientMap.EMPTY;
                var grid = machine.getMainNode().getGrid();
                if (grid == null) return IntIngredientMap.EMPTY;
                Reference2LongOpenHashMap<AEKey> map = null;
                intIngredientMap.clear();
                for (var i : inventory) {
                    if (i.config == null) continue;
                    var stock = i.stock;
                    if (stock == null) continue;
                    if (stock.what() instanceof AEItemKey itemKey) {
                        if (map == null) {
                            map = IKeyCounter.of(grid.getStorageService().getCachedInventory()).gtolib$getMap();
                            if (map == null) return IntIngredientMap.EMPTY;
                        }
                        var amount = ((ExportOnlyAEStockingItemSlot) i).refresh(map, stock.amount(), itemKey);
                        if (amount < 1) continue;
                        ((IAEItemKey) (Object) itemKey).gtolib$convert(amount, intIngredientMap);
                    }
                }
                changed = false;
            }
            return intIngredientMap;
        }
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
        private long refreshTime;

        private ExportOnlyAEStockingItemSlot(MEStockingBusPartMachine machine) {
            super();
            this.machine = machine;
        }

        private ExportOnlyAEStockingItemSlot(MEStockingBusPartMachine machine, @Nullable GenericStack config, @Nullable GenericStack stock) {
            super(config, stock);
            this.machine = machine;
        }

        private long refresh(Reference2LongOpenHashMap<AEKey> map, long amount, AEKey request) {
            long time = machine.getOffsetTimer();
            if (refreshTime != time) {
                refreshTime = time;
                var storage = map.getLong(request);
                if (storage > 0) {
                    if (amount != storage) {
                        this.stock = new GenericStack(request, storage);
                        this.stack = null;
                    }
                } else {
                    this.stock = new GenericStack(request, storage);
                    this.stack = null;
                }
                return storage;
            }
            return amount;
        }

        @Override
        public long extractItem(long amount, boolean simulate, boolean notify) {
            if (this.stock != null && this.config != null) {
                if (!machine.isOnline()) return 0;
                var grid = machine.getMainNode().getGrid();
                if (grid == null) return 0;
                long extracted = simulate ? stock.amount() : grid.getStorageService().getInventory().extract(stock.what(), amount, Actionable.MODULATE, machine.getActionSource());
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
                var key = stock.what();
                long extracted = simulate ? stock.amount() : grid.getStorageService().getInventory().extract(key, amount, Actionable.MODULATE, machine.getActionSource());
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

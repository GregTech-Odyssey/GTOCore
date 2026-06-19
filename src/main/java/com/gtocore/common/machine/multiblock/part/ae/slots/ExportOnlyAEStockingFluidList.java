package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gtocore.common.machine.multiblock.part.ae.MEStockingHatchPartMachine;

import com.gtolib.api.recipe.RecipeType;
import com.gtolib.api.recipe.lookup.IIngredientConvertible;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.utils.function.ObjLongPredicate;

import net.minecraftforge.fluids.FluidStack;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyMap;
import appeng.api.stacks.GenericStack;

import com.fast.recipesearch.IntLongMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ObjLongConsumer;

public class ExportOnlyAEStockingFluidList extends ExportOnlyAEFluidList {

    private final MEStockingHatchPartMachine machine;

    public ExportOnlyAEStockingFluidList(MEStockingHatchPartMachine holder, int slots) {
        super(holder, slots, () -> new ExportOnlyAEStockingFluidSlot(holder));
        this.machine = holder;
    }

    @Override
    public boolean forEachFluids(ObjLongPredicate<FluidStack> function) {
        if (machine.isWorkingEnabled()) {
            if (!machine.isOnline()) return false;
            var grid = machine.getMainNode().getGrid();
            if (grid == null) return false;
            AEKeyMap<AEKey> map = null;
            int time = machine.getOffsetTimer();
            for (var i : inventory) {
                if (i.config == null) continue;
                var stock = i.stock;
                if (stock == null) continue;
                if (map == null) {
                    map = grid.getStorageService().getCachedInventory().getMap();
                    if (map.isEmpty()) return false;
                }
                var amount = ((ExportOnlyAEStockingFluidSlot) i).refresh(map, stock.amount(), stock.what(), time);
                if (amount < 1) continue;
                if (function.test(i.getReadOnlyStack(), amount)) return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void fastForEachFluids(ObjLongConsumer<FluidStack> function) {
        if (machine.isWorkingEnabled()) {
            if (!machine.isOnline()) return;
            var grid = machine.getMainNode().getGrid();
            if (grid == null) return;
            AEKeyMap<AEKey> map = null;
            int time = machine.getOffsetTimer();
            for (var i : inventory) {
                if (i.config == null) continue;
                var stock = i.stock;
                if (stock == null) continue;
                if (map == null) {
                    map = grid.getStorageService().getCachedInventory().getMap();
                    if (map.isEmpty()) return;
                }
                var amount = ((ExportOnlyAEStockingFluidSlot) i).refresh(map, stock.amount(), stock.what(), time);
                if (amount < 1) continue;
                function.accept(i.getReadOnlyStack(), amount);
            }
        }
    }

    @Override
    public void fillSearchMap(@NotNull GTRecipeType type, @NotNull IntLongMap map) {
        if (machine.isWorkingEnabled() && machine.isOnline()) {
            var grid = machine.getMainNode().getGrid();
            if (grid == null) return;
            AEKeyMap<AEKey> keyMap = null;
            boolean specialConverter = ((RecipeType) type).specialConverter;
            int time = machine.getOffsetTimer();
            for (var i : inventory) {
                if (i.config == null) continue;
                var stock = i.stock;
                if (stock == null) continue;
                if (stock.what() instanceof AEFluidKey fluidKey) {
                    if (keyMap == null) {
                        keyMap = grid.getStorageService().getCachedInventory().getMap();
                        if (keyMap.isEmpty()) return;
                    }
                    var amount = ((ExportOnlyAEStockingFluidSlot) i).refresh(keyMap, stock.amount(), fluidKey, time);
                    if (amount < 1) continue;
                    if (specialConverter) {
                        type.convertFluid(i.getReadOnlyStack(), amount, map);
                    } else {
                        ((IIngredientConvertible) (Object) fluidKey).gtolib$convert(amount, map);
                    }
                }
            }
        }
    }

    @Override
    public boolean handleRecipeFluid(IO io, GTRecipe recipe, List<Content<FluidIngredient>> fluids, boolean simulate) {
        if (machine.isWorkingEnabled()) return super.handleRecipeFluid(io, recipe, fluids, simulate);
        return false;
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
        boolean inThisHatch = super.hasStackInConfig(stack, false);
        if (inThisHatch) return true;
        if (checkExternal) {
            return machine.testConfiguredInOtherPart(stack);
        }
        return false;
    }

    private static final class ExportOnlyAEStockingFluidSlot extends ExportOnlyAEFluidSlot {

        private final MEStockingHatchPartMachine machine;
        private long refreshTime;

        private ExportOnlyAEStockingFluidSlot(MEStockingHatchPartMachine machine) {
            super();
            this.machine = machine;
        }

        private ExportOnlyAEStockingFluidSlot(MEStockingHatchPartMachine machine, @Nullable GenericStack config, @Nullable GenericStack stock) {
            super(config, stock);
            this.machine = machine;
        }

        private long refresh(AEKeyMap<AEKey> map, long amount, AEKey request, int time) {
            if (refreshTime != time) {
                refreshTime = time;
                var storage = map.getAmount(request);
                if (storage > 0) {
                    if (amount != storage) {
                        this.stock = new GenericStack(request, storage);
                        this.forgeStock = null;
                    }
                } else {
                    this.stock = null;
                    this.forgeStock = null;
                }
                return storage;
            }
            return amount;
        }

        @Override
        public @NotNull ExportOnlyAEFluidSlot copy() {
            return new ExportOnlyAEStockingFluidSlot(machine, this.config == null ? null : copy(this.config), this.stock == null ? null : copy(this.stock));
        }

        @Override
        public long extract(long amount, boolean simulate, boolean notify) {
            if (this.stock != null && this.config != null) {
                if (!machine.isOnline()) return 0;
                var grid = machine.getMainNode().getGrid();
                if (grid == null) return 0;
                long extracted = simulate ? Math.min(amount, stock.amount()) : grid.getStorageService().getInventory().extract(stock.what(), amount, Actionable.MODULATE, machine.getActionSource());
                if (extracted > 0) {
                    if (!simulate) {
                        machine.getThroughputCounter().remove(stock.what(), extracted);
                        this.stock = ExportOnlyAESlot.copy(stock, stock.amount() - extracted);
                        if (this.stock.amount() == 0) {
                            this.stock = null;
                            forgeStock = null;
                        } else if (forgeStock != null) forgeStock.setAmount(MathUtil.saturatedCast(stock.amount()));
                        if (notify) onContentsChanged();
                    }
                    return extracted;
                }
            }
            return 0;
        }
    }
}

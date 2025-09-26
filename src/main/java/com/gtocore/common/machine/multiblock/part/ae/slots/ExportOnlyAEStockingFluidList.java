package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gtocore.common.machine.multiblock.part.ae.MEStockingHatchPartMachine;

import com.gtolib.api.ae2.IExpandedStorageService;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.integration.ae2.utils.AEUtil;

import net.minecraftforge.fluids.FluidStack;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.GenericStack;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ExportOnlyAEStockingFluidList extends ExportOnlyAEFluidList {

    private final MEStockingHatchPartMachine machine;

    public ExportOnlyAEStockingFluidList(MEStockingHatchPartMachine holder, int slots) {
        super(holder, slots, () -> new ExportOnlyAEStockingFluidSlot(holder));
        this.machine = holder;
    }

    @Override
    public Object2LongOpenHashMap<FluidStack> getFluidMap() {
        if (!machine.isWorkingEnabled() || !machine.isOnline()) return null;
        return super.getFluidMap();
    }

    @Override
    public boolean forEachInputFluids(Predicate<FluidStack> function) {
        if (machine.isWorkingEnabled()) return super.forEachInputFluids(function);
        return false;
    }

    @Override
    public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, boolean simulate) {
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
        boolean inThisHatch = super.hasStackInConfig(stack, false);
        if (inThisHatch) return true;
        if (checkExternal) {
            return machine.testConfiguredInOtherPart(stack);
        }
        return false;
    }

    private static final class ExportOnlyAEStockingFluidSlot extends ExportOnlyAEFluidSlot {

        private final MEStockingHatchPartMachine machine;

        private ExportOnlyAEStockingFluidSlot(MEStockingHatchPartMachine machine) {
            super();
            this.machine = machine;
        }

        private ExportOnlyAEStockingFluidSlot(MEStockingHatchPartMachine machine, @Nullable GenericStack config, @Nullable GenericStack stock) {
            super(config, stock);
            this.machine = machine;
        }

        @Override
        public @NotNull ExportOnlyAEFluidSlot copy() {
            return new ExportOnlyAEStockingFluidSlot(machine, this.config == null ? null : copy(this.config), this.stock == null ? null : copy(this.stock));
        }

        @Override
        public long drain(long amount, boolean simulate, boolean notify) {
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
                        } else if (stack != null) stack.setAmount(MathUtil.saturatedCast(stock.amount()));
                        if (notify) onContentsChanged();
                    }
                    return extracted;
                }
            }
            return 0;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, @NotNull FluidAction action) {
            if (this.stock != null && this.config != null) {
                if (!machine.isOnline()) return FluidStack.EMPTY;
                var grid = machine.getMainNode().getGrid();
                if (grid == null) return FluidStack.EMPTY;
                var key = stock.what();
                long extracted = action.simulate() ? Math.min(maxDrain, IExpandedStorageService.of(grid.getStorageService()).getLazyKeyCounter().get(key)) : grid.getStorageService().getInventory().extract(key, maxDrain, Actionable.MODULATE, machine.getActionSource());
                if (extracted > 0) {
                    FluidStack resultStack = key instanceof AEFluidKey fluidKey ? AEUtil.toFluidStack(fluidKey, extracted) : FluidStack.EMPTY;
                    if (action.execute()) {
                        this.stock = ExportOnlyAESlot.copy(stock, stock.amount() - extracted);
                        if (this.stock.amount() == 0) {
                            this.stock = null;
                            stack = null;
                        } else if (stack != null) stack.setAmount(MathUtil.saturatedCast(stock.amount()));
                        onContentsChanged();
                    }
                    return resultStack;
                }
            }
            return FluidStack.EMPTY;
        }
    }
}

package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gtolib.api.recipe.ingredient.FastFluidIngredient;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlotList;
import com.gregtechceu.gtceu.utils.collection.O2LOpenCacheHashMap;

import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class ExportOnlyAEFluidList extends NotifiableFluidTank implements IConfigurableSlotList {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ExportOnlyAEFluidList.class, NotifiableFluidTank.MANAGED_FIELD_HOLDER);

    @Persisted
    protected ExportOnlyAEFluidSlot[] inventory;

    public ExportOnlyAEFluidList(MetaMachine machine, int slots) {
        this(machine, slots, ExportOnlyAEFluidSlot::new);
    }

    ExportOnlyAEFluidList(MetaMachine machine, int slots, Supplier<ExportOnlyAEFluidSlot> slotFactory) {
        super(machine, slots, 0, IO.IN, IO.NONE);
        this.inventory = new ExportOnlyAEFluidSlot[slots];
        for (int i = 0; i < slots; i++) {
            this.inventory[i] = slotFactory.get();
            this.storages[i] = new FluidStorageDelegate(inventory[i]);
            this.inventory[i].setOnContentsChanged(this::onContentsChanged);
        }
    }

    @Override
    public boolean isEmpty() {
        return getFluidMap() == null;
    }

    @Override
    public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, boolean simulate) {
        if (io == IO.IN) {
            boolean changed = false;
            for (var it = left.listIterator(0); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }
                long a = FastFluidIngredient.getAmount(ingredient);
                for (var i : inventory) {
                    var stored = i.stock;
                    if (stored == null) continue;
                    long amount = stored.amount();
                    if (amount == 0) continue;
                    if (ingredient.test(i.getFluid())) {
                        var drained = i.drain(a, simulate, false);
                        if (drained > 0) {
                            changed = true;
                            a -= drained;
                            FastFluidIngredient.setAmount(ingredient, a);
                        }
                    }
                    if (a <= 0) {
                        it.remove();
                        break;
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
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public boolean supportsFill(int tank) {
        return false;
    }

    @Override
    public FluidStack drainInternal(int maxDrain, FluidAction action) {
        if (maxDrain == 0) {
            return FluidStack.EMPTY;
        }
        FluidStack totalDrained = null;
        for (var tank : inventory) {
            if (totalDrained == null || totalDrained.isEmpty()) {
                totalDrained = tank.drain(maxDrain, action);
                if (totalDrained.isEmpty()) {
                    totalDrained = null;
                } else {
                    maxDrain -= totalDrained.getAmount();
                }
            } else {
                FluidStack copy = totalDrained.copy();
                copy.setAmount(maxDrain);
                FluidStack drain = tank.drain(copy, action);
                totalDrained.grow(drain.getAmount());
                maxDrain -= drain.getAmount();
            }
            if (maxDrain <= 0) break;
        }
        return totalDrained == null ? FluidStack.EMPTY : totalDrained;
    }

    @Override
    public Object2LongOpenHashMap<FluidStack> getFluidMap() {
        if (fluidMap == null) {
            fluidMap = new O2LOpenCacheHashMap<>();
        }
        if (changed) {
            changed = false;
            fluidMap.clear();
            for (var i : inventory) {
                if (i.config == null) continue;
                var stock = i.stock;
                if (stock == null || stock.amount() == 0) continue;
                var stack = i.getFluid();
                if (stack.isEmpty()) continue;
                fluidMap.addTo(stack, stock.amount());
            }
            isEmpty = fluidMap.isEmpty();
        }
        return isEmpty ? null : fluidMap;
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

    private static class FluidStorageDelegate extends CustomFluidTank {

        private final ExportOnlyAEFluidSlot fluid;

        FluidStorageDelegate(ExportOnlyAEFluidSlot fluid) {
            super(0);
            this.fluid = fluid;
        }

        @Override
        @NotNull
        public FluidStack getFluid() {
            return this.fluid.getFluid();
        }

        @Override
        @NotNull
        public FluidStack drain(int maxDrain, FluidAction action) {
            return fluid.drain(maxDrain, action);
        }

        @Override
        @NotNull
        public FluidStack drain(FluidStack resource, FluidAction action) {
            return fluid.drain(resource, action);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return 0;
        }

        @Override
        public boolean supportsFill(int tank) {
            return false;
        }
    }

    public ExportOnlyAEFluidSlot[] getInventory() {
        return this.inventory;
    }
}

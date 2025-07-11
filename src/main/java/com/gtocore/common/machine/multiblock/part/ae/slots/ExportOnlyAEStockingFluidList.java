package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gtocore.common.machine.multiblock.part.ae.MEStockingHatchPartMachine;

import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.integration.ae2.utils.AEUtil;

import net.minecraftforge.fluids.FluidStack;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExportOnlyAEStockingFluidList extends ExportOnlyAEFluidList {

    public ExportOnlyAEStockingFluidList(MEStockingHatchPartMachine holder, int slots) {
        super(holder, slots, () -> new ExportOnlyAEStockingFluidSlot(holder));
    }

    @Override
    public MEStockingHatchPartMachine getMachine() {
        return (MEStockingHatchPartMachine) machine;
    }

    @Override
    public boolean isAutoPull() {
        return getMachine().isAutoPull();
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
            return getMachine().testConfiguredInOtherPart(stack);
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
                long extracted = simulate ? stock.amount() : machine.getMainNode().getGrid().getStorageService().getInventory().extract(config.what(), amount, Actionable.MODULATE, machine.getActionSource());
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
                MEStorage aeNetwork = machine.getMainNode().getGrid().getStorageService().getInventory();
                Actionable actionable = action.simulate() ? Actionable.SIMULATE : Actionable.MODULATE;
                var key = config.what();
                long extracted = aeNetwork.extract(key, maxDrain, actionable, machine.getActionSource());
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

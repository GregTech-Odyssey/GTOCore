package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.api.machine.IMEHatchPart;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEInputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEInputHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEStockingHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAESlot;
import com.gregtechceu.gtceu.integration.ae2.utils.AEUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExportOnlyAEStockingFluidList extends ExportOnlyAEFluidList {

    public ExportOnlyAEStockingFluidList(MetaMachine holder, int slots) {
        super(holder, slots, () -> new ExportOnlyAEStockingFluidSlot((MEInputBusPartMachine) holder));
    }

    @Override
    public MEInputHatchPartMachine getMachine() {
        return (MEInputHatchPartMachine) super.getMachine();
    }

    @Override
    public boolean isAutoPull() {
        if (getMachine() instanceof MEStockingHatchPartMachine pull) return pull.isAutoPull();
        return true;
    }

    @Override
    public boolean isStocking() {
        return true;
    }

    @Override
    public boolean hasStackInConfig(GenericStack stack, boolean checkExternal) {
        boolean inThisBus = super.hasStackInConfig(stack, false);
        if (inThisBus) return true;
        if (checkExternal && getMachine() instanceof MEStockingHatchPartMachine pull) {
            return pull.testConfiguredInOtherPart(stack);
        }
        return false;
    }

    private static class ExportOnlyAEStockingFluidSlot extends ExportOnlyAEFluidSlot {

        private final Object lock = new Object();

        private final MEInputBusPartMachine machine;

        public ExportOnlyAEStockingFluidSlot(MEInputBusPartMachine machine) {
            super();
            this.machine = machine;
        }

        public ExportOnlyAEStockingFluidSlot(@Nullable GenericStack config, @Nullable GenericStack stock, MEInputBusPartMachine machine) {
            super(config, stock);
            this.machine = machine;
        }

        @Nullable
        public GenericStack requestStack() {
            synchronized (lock) {
                return super.requestStack();
            }
        }

        @Nullable
        public GenericStack exceedStack() {
            synchronized (lock) {
                return super.exceedStack();
            }
        }

        @Override
        public void addStack(@NotNull GenericStack stack) {
            synchronized (lock) {
                super.addStack(stack);
            }
        }

        @Override
        public void setStock(@Nullable GenericStack stack) {
            synchronized (lock) {
                super.setStock(stack);
            }
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            synchronized (lock) {
                super.deserializeNBT(tag);
            }
        }

        @Override
        public @NotNull ExportOnlyAEFluidSlot copy() {
            return new ExportOnlyAEStockingFluidSlot(this.config == null ? null : copy(this.config), this.stock == null ? null : copy(this.stock), machine);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, @NotNull FluidAction action) {
            synchronized (lock) {
                if (this.stock != null && this.config != null) {
                    if (!machine.isOnline()) return FluidStack.EMPTY;
                    MEStorage aeNetwork = machine.getMainNode().getGrid().getStorageService().getInventory();
                    Actionable actionable = action.simulate() ? Actionable.SIMULATE : Actionable.MODULATE;
                    var key = config.what();
                    long extracted = aeNetwork.extract(key, maxDrain, actionable, ((IMEHatchPart) machine).gtocore$getActionSource());
                    if (extracted > 0) {
                        FluidStack resultStack = key instanceof AEFluidKey fluidKey ? AEUtil.toFluidStack(fluidKey, extracted) : FluidStack.EMPTY;
                        if (action.execute()) {
                            this.stock = ExportOnlyAESlot.copy(stock, stock.amount() - extracted);
                            if (this.stock.amount() == 0) {
                                this.stock = null;
                            }
                            if (this.onContentsChanged != null) {
                                this.onContentsChanged.run();
                            }
                        }
                        return resultStack;
                    }
                }
                return FluidStack.EMPTY;
            }
        }
    }
}

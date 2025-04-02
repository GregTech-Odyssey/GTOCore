package com.gto.gtocore.common.machine.multiblock.part.ae.slots;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAESlot;
import com.gregtechceu.gtceu.integration.ae2.utils.AEUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExportOnlyAEStockingFluidList extends ExportOnlyAEFluidList {

    public ExportOnlyAEStockingFluidList(MetaMachine holder, int slots) {
        super(holder, slots, () -> new ExportOnlyAEStockingFluidSlot((NetworkSlotMachine) holder));
    }

    @Override
    public boolean isAutoPull() {
        return true;
    }

    @Override
    public boolean isStocking() {
        return true;
    }

    static class ExportOnlyAEStockingFluidSlot extends ExportOnlyAEFluidSlot {

        private final NetworkSlotMachine machine;

        ExportOnlyAEStockingFluidSlot(NetworkSlotMachine machine) {
            super();
            this.machine = machine;
        }

        ExportOnlyAEStockingFluidSlot(@Nullable GenericStack config, @Nullable GenericStack stock, NetworkSlotMachine machine) {
            super(config, stock);
            this.machine = machine;
        }

        @Override
        public ExportOnlyAEFluidSlot copy() {
            return new ExportOnlyAEStockingFluidSlot(this.config == null ? null : copy(this.config), this.stock == null ? null : copy(this.stock), machine);
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            if (this.stock != null && this.config != null) {
                if (!machine.isOnline()) return FluidStack.EMPTY;
                IGrid grid = machine.getMainNode().getGrid();
                if (grid == null) return FluidStack.EMPTY;
                MEStorage aeNetwork = grid.getStorageService().getInventory();
                Actionable actionable = action.simulate() ? Actionable.SIMULATE : Actionable.MODULATE;
                var key = config.what();
                long extracted = aeNetwork.extract(key, maxDrain, actionable, machine.getActionSource());
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

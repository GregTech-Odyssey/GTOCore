package com.gto.gtocore.common.machine.multiblock.part.ae.slots;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAESlot;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExportOnlyAEStockingItemList extends ExportOnlyAEItemList {

    public ExportOnlyAEStockingItemList(MetaMachine holder, int slots) {
        super(holder, slots, () -> new ExportOnlyAEStockingItemSlot((NetworkSlotMachine) holder));
    }

    @Override
    public boolean isAutoPull() {
        return true;
    }

    @Override
    public boolean isStocking() {
        return true;
    }

    public static class ExportOnlyAEStockingItemSlot extends ExportOnlyAEItemSlot {

        private final NetworkSlotMachine machine;

        public ExportOnlyAEStockingItemSlot(NetworkSlotMachine machine) {
            super();
            this.machine = machine;
        }

        public ExportOnlyAEStockingItemSlot(@Nullable GenericStack config, @Nullable GenericStack stock, NetworkSlotMachine machine) {
            super(config, stock);
            this.machine = machine;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == 0 && this.stock != null) {
                if (this.config != null) {
                    if (!machine.isOnline()) return ItemStack.EMPTY;
                    IGrid grid = machine.getMainNode().getGrid();
                    if (grid == null) return ItemStack.EMPTY;
                    MEStorage aeNetwork = grid.getStorageService().getInventory();
                    Actionable action = simulate ? Actionable.SIMULATE : Actionable.MODULATE;
                    var key = config.what();
                    long extracted = aeNetwork.extract(key, amount, action, machine.getActionSource());
                    if (extracted > 0) {
                        ItemStack resultStack = key instanceof AEItemKey itemKey ? itemKey.toStack((int) extracted) : ItemStack.EMPTY;
                        if (!simulate) {
                            // may as well update the display here
                            this.stock = ExportOnlyAESlot.copy(stock, stock.amount() - extracted);
                            if (this.stock.amount() == 0) {
                                this.stock = null;
                            }
                        }
                        return resultStack;
                    }
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ExportOnlyAEStockingItemSlot copy() {
            return new ExportOnlyAEStockingItemSlot(this.config == null ? null : copy(this.config), this.stock == null ? null : copy(this.stock), machine);
        }
    }
}

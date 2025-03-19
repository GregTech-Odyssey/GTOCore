package com.gto.gtocore.api.machine.trait;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEStockingBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAESlot;

import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExportOnlyAEStockingItemList extends ExportOnlyAEItemList {

    public ExportOnlyAEStockingItemList(MetaMachine holder, int slots) {
        super(holder, slots, () -> new ExportOnlyAEStockingItemSlot((MEStockingBusPartMachine) holder));
    }

    public MEStockingBusPartMachine getMachine() {
        return (MEStockingBusPartMachine) super.getMachine();
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
        boolean inThisBus = super.hasStackInConfig(stack, false);
        if (inThisBus) return true;
        if (checkExternal) {
            return getMachine().testConfiguredInOtherPart(stack);
        }
        return false;
    }

    private static class ExportOnlyAEStockingItemSlot extends ExportOnlyAEItemSlot {

        private final MEStockingBusPartMachine machine;

        public ExportOnlyAEStockingItemSlot(MEStockingBusPartMachine machine) {
            super();
            this.machine = machine;
        }

        public ExportOnlyAEStockingItemSlot(@Nullable GenericStack config, @Nullable GenericStack stock, MEStockingBusPartMachine machine) {
            super(config, stock);
            this.machine = machine;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == 0 && this.stock != null) {
                if (this.config != null) {
                    if (!machine.isOnline()) return ItemStack.EMPTY;
                    synchronized (this) {
                        MEStorage aeNetwork = machine.getMainNode().getGrid().getStorageService().getInventory();
                        Actionable action = simulate ? Actionable.SIMULATE : Actionable.MODULATE;
                        var key = config.what();
                        long extracted = aeNetwork.extract(key, amount, action, machine.getActionSource());
                        if (extracted > 0) {
                            ItemStack resultStack = key instanceof AEItemKey itemKey ? itemKey.toStack((int) extracted) : ItemStack.EMPTY;
                            if (!simulate) {
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
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public @NotNull ExportOnlyAEStockingItemSlot copy() {
            return new ExportOnlyAEStockingItemSlot(this.config == null ? null : copy(this.config), this.stock == null ? null : copy(this.stock), machine);
        }
    }
}

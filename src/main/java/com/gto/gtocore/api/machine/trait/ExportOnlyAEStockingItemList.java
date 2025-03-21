package com.gto.gtocore.api.machine.trait;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEInputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEStockingBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAESlot;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ExportOnlyAEStockingItemList extends ExportOnlyAEItemList {

    public ExportOnlyAEStockingItemList(MetaMachine holder, int slots) {
        super(holder, slots, () -> new ExportOnlyAEStockingItemSlot((MEInputBusPartMachine) holder));
    }

    @Override
    public MEInputBusPartMachine getMachine() {
        return (MEInputBusPartMachine) super.getMachine();
    }

    @Override
    public boolean isAutoPull() {
        if (getMachine() instanceof MEStockingBusPartMachine pull) return pull.isAutoPull();
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
        if (checkExternal && getMachine() instanceof MEStockingBusPartMachine pull) {
            return pull.testConfiguredInOtherPart(stack);
        }
        return false;
    }

    private static class ExportOnlyAEStockingItemSlot extends ExportOnlyAEItemSlot {

        private final Object lock = new Object();

        private final MEInputBusPartMachine machine;

        public ExportOnlyAEStockingItemSlot(MEInputBusPartMachine machine) {
            super();
            this.machine = machine;
        }

        public ExportOnlyAEStockingItemSlot(@Nullable GenericStack config, @Nullable GenericStack stock, MEInputBusPartMachine machine) {
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
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            synchronized (lock) {
                if (slot == 0 && this.stock != null) {
                    if (this.config != null) {
                        if (!machine.isOnline()) return ItemStack.EMPTY;
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
                return ItemStack.EMPTY;
            }
        }

        @Override
        public @NotNull ExportOnlyAEStockingItemSlot copy() {
            return new ExportOnlyAEStockingItemSlot(this.config == null ? null : copy(this.config), this.stock == null ? null : copy(this.stock), machine);
        }
    }
}

package com.gtocore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.nbt.CompoundTag;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingLink;
import appeng.api.networking.crafting.ICraftingRequester;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import appeng.helpers.MultiCraftingTracker;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

public class MERequestableInputBusMachine extends MEInputBusPartMachine implements ICraftingRequester {

    MultiCraftingTracker craftingTracker = new MultiCraftingTracker(this, aeItemHandler.getSize());

    public MERequestableInputBusMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    void syncME() {
        IGrid grid = this.getMainNode().getGrid();
        if (grid == null) {
            return;
        }
        var cg = grid.getCraftingService();
        MEStorage networkInv = grid.getStorageService().getInventory();
        for (int i = 0; i < this.aeItemHandler.getSize(); i++) {
            var aeTank = this.aeItemHandler.getInventory()[i];
            GenericStack exceedFluid = aeTank.exceedStack();
            if (exceedFluid != null) {
                long total = exceedFluid.amount();
                long inserted = networkInv.insert(exceedFluid.what(), exceedFluid.amount(), Actionable.MODULATE, this.getActionSourceField());
                throughputCounter.add(exceedFluid.what(), inserted);
                if (inserted > 0) {
                    aeTank.extractItem(inserted, false, true);
                    continue;
                } else {
                    aeTank.extractItem(total, false, true);
                }
            }
            GenericStack reqFluid = aeTank.requestStack();
            if (reqFluid != null) {
                long extracted = networkInv.extract(reqFluid.what(), reqFluid.amount(), Actionable.MODULATE, this.getActionSourceField());
                if (extracted < reqFluid.amount()) {
                    craftingTracker.handleCrafting(i, reqFluid.what(), reqFluid.amount() - extracted,
                            getLevel(), cg, getActionSourceField());
                }
                throughputCounter.remove(reqFluid.what(), extracted);
                if (extracted > 0) {
                    aeTank.addStack(new GenericStack(reqFluid.what(), extracted));
                }
            }
        }
    }

    @Override
    public long insertCraftedItems(ICraftingLink link, AEKey what, long amount, Actionable mode) {
        IGrid grid = this.getMainNode().getGrid();
        if (grid == null) {
            return 0;
        }

        long remaining = amount;
        for (var aeTank : this.aeItemHandler.getInventory()) {
            GenericStack reqFluid = aeTank.requestStack();
            if (reqFluid != null && reqFluid.what() == what) {
                var reqAmount = Math.min(reqFluid.amount(), remaining);
                remaining -= reqAmount;
                if (mode == Actionable.MODULATE) {
                    throughputCounter.remove(reqFluid.what(), reqAmount);
                    aeTank.addStack(new GenericStack(reqFluid.what(), reqAmount));
                }
            }
        }
        return amount - remaining;
    }

    @Override
    public void jobStateChange(ICraftingLink link) {
        updateInventorySubscription();
    }

    @Override
    public ImmutableSet<ICraftingLink> getRequestedJobs() {
        return craftingTracker.getRequestedJobs();
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        craftingTracker.readFromNBT(tag.getCompound("craftingTracker"));
    }

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        var craftingTrackerTag = new CompoundTag();
        craftingTracker.writeToNBT(craftingTrackerTag);
        tag.put("craftingTracker", craftingTrackerTag);
    }
}

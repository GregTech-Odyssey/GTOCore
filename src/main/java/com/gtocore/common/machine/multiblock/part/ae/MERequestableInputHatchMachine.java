package com.gtocore.common.machine.multiblock.part.ae;

import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEFluidSlot;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingLink;
import appeng.api.networking.crafting.ICraftingRequester;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import appeng.helpers.MultiCraftingTracker;

import com.google.common.collect.ImmutableSet;

public class MERequestableInputHatchMachine extends MEInputHatchPartMachine implements ICraftingRequester {

    MultiCraftingTracker craftingTracker = new MultiCraftingTracker(this, aeFluidHandler.getSize());

    public MERequestableInputHatchMachine(MetaMachineBlockEntity holder) {
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
        for (int i = 0; i < this.aeFluidHandler.getTanks(); i++) {
            ExportOnlyAEFluidSlot aeTank = this.aeFluidHandler.getInventory()[i];
            GenericStack exceedFluid = aeTank.exceedStack();
            if (exceedFluid != null) {
                long total = exceedFluid.amount();
                long inserted = networkInv.insert(exceedFluid.what(), exceedFluid.amount(), Actionable.MODULATE, this.getActionSourceField());
                throughputCounter.add(exceedFluid.what(), inserted);
                if (inserted > 0) {
                    aeTank.drain(inserted, false, true);
                    continue;
                } else {
                    aeTank.drain(total, false, true);
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
        for (ExportOnlyAEFluidSlot aeTank : this.aeFluidHandler.getInventory()) {
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
        craftingTracker.jobStateChange(link);
        updateTankSubscription();
    }

    @Override
    public ImmutableSet<ICraftingLink> getRequestedJobs() {
        return craftingTracker.getRequestedJobs();
    }
}

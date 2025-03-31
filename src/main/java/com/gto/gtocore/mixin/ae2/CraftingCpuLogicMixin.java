package com.gto.gtocore.mixin.ae2;

import com.gto.gtocore.common.data.GTOItems;

import net.minecraft.world.level.Level;

import appeng.api.networking.energy.IEnergyService;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.crafting.execution.CraftingCpuLogic;
import appeng.crafting.execution.ExecutingCraftingJob;
import appeng.crafting.inv.ListCraftingInventory;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.me.service.CraftingService;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CraftingCpuLogic.class)
public abstract class CraftingCpuLogicMixin {

    @Shadow(remap = false)
    @Final
    CraftingCPUCluster cluster;

    @Shadow(remap = false)
    private boolean cantStoreItems;

    @Shadow(remap = false)
    private ExecutingCraftingJob job;

    @Shadow(remap = false)
    public abstract void storeItems();

    @Shadow(remap = false)
    @Final
    private ListCraftingInventory inventory;

    @Shadow(remap = false)
    public abstract void cancel();

    @Shadow(remap = false)
    @Final
    private int[] usedOps;

    @Shadow(remap = false)
    public abstract int executeCrafting(int maxPatterns, CraftingService craftingService, IEnergyService energyService, Level level);

    @Shadow(remap = false)
    public abstract @Nullable GenericStack getFinalJobOutput();

    @Shadow(remap = false)
    protected abstract void finishJob(boolean success);

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void tickCraftingLogic(IEnergyService eg, CraftingService cc) {
        // Don't tick if we're not active.
        if (!cluster.isActive()) return;
        cantStoreItems = false;
        // If we don't have a job, just try to dump our items.
        if (this.job == null) {
            this.storeItems();
            if (!this.inventory.list.isEmpty()) {
                cantStoreItems = true;
            }
            return;
        }
        // Check if the job was cancelled.
        if (((ExecutingCraftingJobAccessor) job).getLink().isCanceled()) {
            cancel();
            return;
        }

        var remainingOperations = cluster.getCoProcessors() + 1 - (this.usedOps[0] + this.usedOps[1] + this.usedOps[2]);
        final var started = remainingOperations;
        while (remainingOperations > 0) {
            var pushedPatterns = executeCrafting(remainingOperations, cc, eg, cluster.getLevel());

            if (pushedPatterns > 0) {
                remainingOperations -= pushedPatterns;
            } else {
                GenericStack stack = getFinalJobOutput();
                if (stack != null && stack.what() instanceof AEItemKey itemKey && itemKey.getItem() == GTOItems.ORDER.get()) {
                    finishJob(true);
                }
                break;
            }
        }
        this.usedOps[2] = this.usedOps[1];
        this.usedOps[1] = this.usedOps[0];
        this.usedOps[0] = started - remainingOperations;
    }
}

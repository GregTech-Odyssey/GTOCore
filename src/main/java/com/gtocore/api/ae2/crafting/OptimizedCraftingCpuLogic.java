package com.gtocore.api.ae2.crafting;

import com.gtocore.common.data.GTOItems;

import com.gtolib.GTOCore;
import com.gtolib.api.ae2.IPatternProviderLogic;
import com.gtolib.utils.holder.IntHolder;
import com.gtolib.utils.holder.LongHolder;
import com.gtolib.utils.holder.ObjectHolder;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.crafting.IPatternDetails;
import appeng.api.features.IPlayerRegistry;
import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.*;
import appeng.api.networking.energy.IEnergyService;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.CraftingJobStatusPacket;
import appeng.crafting.CraftingLink;
import appeng.crafting.execution.CraftingCpuHelper;
import appeng.crafting.execution.CraftingCpuLogic;
import appeng.crafting.execution.CraftingSubmitResult;
import appeng.crafting.execution.ElapsedTimeTracker;
import appeng.crafting.inv.ListCraftingInventory;
import appeng.hooks.ticking.TickHandler;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.me.service.CraftingService;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

public class OptimizedCraftingCpuLogic extends CraftingCpuLogic {

    private final static int BREAK = 1;
    private final static int BREAK_TASK_LOOP = 2;

    final CraftingCPUCluster cluster;

    private ExecutingCraftingJob job = null;

    private final Set<Consumer<AEKey>> listeners = new ObjectOpenHashSet<>();

    private final SetMultimap<AEKey, GlobalPos> pendingRequests = HashMultimap.create();

    private final ListCraftingInventory.ChangeListener changeListener = what -> {
        lastModifiedOnTick = TickHandler.instance().getCurrentTick();
        for (var listener : listeners) {
            listener.accept(what);
        }
    };

    private final ListCraftingInventory inventory = new ListCraftingInventory(changeListener);

    private boolean cantStoreItems = false;

    private long lastModifiedOnTick = TickHandler.instance().getCurrentTick();

    public OptimizedCraftingCpuLogic(CraftingCPUCluster cluster) {
        super(cluster);
        this.cluster = cluster;
    }

    @Override
    public ICraftingSubmitResult trySubmitJob(IGrid grid, ICraftingPlan plan, IActionSource src, ICraftingRequester requester) {
        if (this.job != null) return CraftingSubmitResult.CPU_BUSY;
        if (!cluster.isActive()) return CraftingSubmitResult.CPU_OFFLINE;
        if (cluster.getAvailableStorage() < plan.bytes()) return CraftingSubmitResult.CPU_TOO_SMALL;

        if (!inventory.list.isEmpty()) GTOCore.LOGGER.error("Crafting CPU inventory is not empty yet a job was submitted.");

        var missingIngredient = CraftingCpuHelper.tryExtractInitialItems(plan, grid, inventory, src);
        if (missingIngredient != null) return CraftingSubmitResult.missingIngredient(missingIngredient);
        var playerId = src.player()
                .map(p -> p instanceof ServerPlayer serverPlayer ? IPlayerRegistry.getPlayerId(serverPlayer) : null)
                .orElse(null);
        var craftId = UUID.randomUUID();
        var linkCpu = new CraftingLink(CraftingCpuHelper.generateLinkData(craftId, requester == null, false), cluster);
        this.job = new ExecutingCraftingJob(plan, changeListener, linkCpu, playerId, this);
        cluster.updateOutput(plan.finalOutput());
        cluster.markDirty();
        notifyJobOwner(job, CraftingJobStatusPacket.Status.STARTED);
        if (requester != null) {
            var linkReq = new CraftingLink(CraftingCpuHelper.generateLinkData(craftId, false, true), requester);

            var craftingService = (CraftingService) grid.getCraftingService();
            craftingService.addLink(linkCpu);
            craftingService.addLink(linkReq);

            return CraftingSubmitResult.successful(linkReq);
        } else {
            return CraftingSubmitResult.successful(null);
        }
    }

    @Override
    public void tickCraftingLogic(IEnergyService eg, CraftingService cc) {
        if (!cluster.isActive()) return;
        cantStoreItems = false;
        if (this.job == null) {
            this.storeItems();
            if (!this.inventory.list.isEmpty()) {
                cantStoreItems = true;
            }
            return;
        }
        if (job.link.isCanceled()) {
            cancel();
            return;
        }

        if (executeCrafting(cluster.getCoProcessors(), cc, eg, cluster.getLevel()) == 0) {
            GenericStack stack = getFinalJobOutput();
            if (stack != null && stack.what() instanceof AEItemKey itemKey && itemKey.getItem() == GTOItems.ORDER.get()) {
                // the job is crafting an order and is waiting for an order, which means its dependencies have been
                // crafted
                final var waitingFor = getWaitingFor(itemKey);
                if (waitingFor > 0) {
                    final var remainingAmount = job.remainingAmount - waitingFor;
                    // Simulate inserting final result with the same logic as CraftingCpuLogic.insert
                    if (remainingAmount <= 0) {
                        finishJob(true);
                        cluster.updateOutput(null);
                    } else {
                        cluster.updateOutput(new GenericStack(itemKey, remainingAmount));
                    }
                }
            }
        }
    }

    @Override
    public int executeCrafting(int maxPatterns, CraftingService craftingService, IEnergyService energyService, Level level) {
        var job = this.job;
        if (job == null) return 0;

        IntHolder pushedPatterns = new IntHolder(0);

        var it = job.tasks.object2ObjectEntrySet().fastIterator();

        var expectedOutputs = new KeyCounter();
        var expectedContainerItems = new KeyCounter();

        taskLoop:
        while (it.hasNext()) {
            var task = it.next();
            var progress = task.getValue();
            if (progress.value <= 0) {
                it.remove();
                continue;
            }

            var details = task.getKey();

            expectedOutputs.clear();
            expectedContainerItems.clear();
            ObjectHolder<KeyCounter[]> craftingContainer = new ObjectHolder<>(ExecutingCraftingJob.extractPatternInputs(details, inventory, level, expectedOutputs, expectedContainerItems));
            if (craftingContainer.value == null) continue;
            var providerIterable = craftingService.getProviders(details).iterator();
            IntSupplier pushPatternSuccess = () -> {
                energyService.extractAEPower(CraftingCpuHelper.calculatePatternPower(craftingContainer.value), Actionable.MODULATE, PowerMultiplier.CONFIG);
                pushedPatterns.value++;

                for (var expectedOutput : expectedOutputs) {
                    job.waitingFor.insert(expectedOutput.getKey(), expectedOutput.getLongValue(), Actionable.MODULATE);
                }
                for (var expectedContainerItem : expectedContainerItems) {
                    job.waitingFor.insert(expectedContainerItem.getKey(), expectedContainerItem.getLongValue(), Actionable.MODULATE);
                    job.tt.gtolib$addMaxItems(expectedContainerItem.getLongValue(), expectedContainerItem.getKey().getType());
                }

                progress.value--;
                if (progress.value <= 0) {
                    it.remove();
                    return BREAK;
                }

                if (pushedPatterns.value > maxPatterns) {
                    return BREAK_TASK_LOOP;
                }

                expectedOutputs.reset();
                expectedContainerItems.reset();
                craftingContainer.value = ExecutingCraftingJob.extractPatternInputs(details, inventory, level, expectedOutputs, expectedContainerItems);
                return 0;
            };
            var targetOutput = details.getPrimaryOutput().what();
            while (providerIterable.hasNext()) {
                if (craftingContainer.value == null) break;
                ICraftingProvider provider = providerIterable.next();
                if (provider.isBusy()) continue;
                if (provider instanceof IPatternProviderLogic logic) {
                    var result = logic.gtolib$pushPattern(details, craftingContainer, pushPatternSuccess);
                    if (result != -1) {
                        this.pendingRequests.put(targetOutput, logic.gto$getPos());
                    }
                    if (result < 0) continue;
                    cluster.markDirty();
                    switch (result) {
                        case BREAK:
                            continue taskLoop;
                        case BREAK_TASK_LOOP:
                            break taskLoop;
                    }
                } else if (provider.pushPattern(details, craftingContainer.value)) {
                    var result = pushPatternSuccess.getAsInt();
                    if (result < 0) continue;
                    cluster.markDirty();
                    if (provider instanceof BlockEntity be) {
                        this.pendingRequests.put(targetOutput, GlobalPos.of(level.dimension(), be.getBlockPos()));
                    } else if (provider instanceof MetaMachine mm) {
                        this.pendingRequests.put(targetOutput, GlobalPos.of(level.dimension(), mm.getPos()));
                    }
                    switch (result) {
                        case BREAK:
                            continue taskLoop;
                        case BREAK_TASK_LOOP:
                            break taskLoop;
                    }
                }
            }

            if (craftingContainer.value != null) {
                CraftingCpuHelper.reinjectPatternInputs(inventory, craftingContainer.value);
            }
        }

        return pushedPatterns.value;
    }

    @Override
    public long insert(AEKey what, long amount, Actionable type) {
        if (what == null || job == null)
            return 0;

        var waitingFor = job.waitingFor.extract(what, amount, Actionable.SIMULATE);
        if (waitingFor <= 0) {
            return 0;
        }

        if (amount > waitingFor) {
            amount = waitingFor;
        }

        if (type == Actionable.MODULATE) {
            job.waitingFor.extract(what, amount, Actionable.MODULATE);
            if (amount == waitingFor) {
                this.pendingRequests.removeAll(what);
            }
            job.tt.gtolib$decrementItems(amount, what.getType());
            cluster.markDirty();
        }

        long inserted = amount;
        if (what.matches(job.finalOutput)) {
            inserted = job.link.insert(what, amount, type);

            if (type == Actionable.MODULATE) {
                changeListener.onChange(what);
                job.remainingAmount = Math.max(0, job.remainingAmount - amount);

                if (job.remainingAmount <= 0) {
                    finishJob(true);
                    cluster.updateOutput(null);
                } else {
                    cluster.updateOutput(new GenericStack(job.finalOutput.what(), job.remainingAmount));
                }
            }
        } else {
            if (type == Actionable.MODULATE) {
                inventory.insert(what, amount, Actionable.MODULATE);
            }
        }

        return inserted;
    }

    @Override
    public void cancel() {
        if (job == null) return;
        cluster.updateOutput(null);
        finishJob(false);
    }

    @Override
    public void storeItems() {
        if (this.inventory.list.isEmpty()) return;

        var g = cluster.getGrid();
        if (g == null) return;

        var storage = g.getStorageService().getInventory();

        for (var entry : this.inventory.list) {
            changeListener.onChange(entry.getKey());
            var inserted = storage.insert(entry.getKey(), entry.getLongValue(), Actionable.MODULATE, cluster.getSrc());

            entry.setValue(entry.getLongValue() - inserted);
        }
        this.inventory.list.removeZeros();

        cluster.markDirty();
    }

    @Override
    public long getLastModifiedOnTick() {
        return lastModifiedOnTick;
    }

    @Override
    public boolean hasJob() {
        return this.job != null;
    }

    @Override
    public GenericStack getFinalJobOutput() {
        return this.job != null ? this.job.finalOutput : null;
    }

    @Override
    public ElapsedTimeTracker getElapsedTimeTracker() {
        if (this.job != null) {
            return this.job.timeTracker;
        } else {
            return new ElapsedTimeTracker();
        }
    }

    @Override
    public void readFromNBT(CompoundTag data) {
        this.inventory.readFromNBT(data.getList("inventory", 10));
        if (data.contains("job")) {
            this.job = new ExecutingCraftingJob(data.getCompound("job"), changeListener, this);
            cluster.updateOutput(new GenericStack(job.finalOutput.what(), job.remainingAmount));
        } else {
            cluster.updateOutput(null);
        }
    }

    @Override
    public void writeToNBT(CompoundTag data) {
        data.put("inventory", this.inventory.writeToNBT());
        if (this.job != null) {
            data.put("job", this.job.writeToNBT());
        }
    }

    @Override
    public ICraftingLink getLastLink() {
        if (this.job != null) {
            return this.job.link;
        }
        return null;
    }

    @Override
    public ListCraftingInventory getInventory() {
        return this.inventory;
    }

    @Override
    public void addListener(Consumer<AEKey> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Consumer<AEKey> listener) {
        listeners.remove(listener);
    }

    @Override
    public long getStored(AEKey template) {
        return this.inventory.extract(template, Long.MAX_VALUE, Actionable.SIMULATE);
    }

    @Override
    public long getWaitingFor(AEKey template) {
        if (this.job != null) {
            return this.job.waitingFor.extract(template, Long.MAX_VALUE, Actionable.SIMULATE);
        }
        return 0;
    }

    @Override
    public void getAllWaitingFor(Set<AEKey> waitingFor) {
        if (this.job != null) {
            for (var entry : this.job.waitingFor.list) {
                waitingFor.add(entry.getKey());
            }
        }
    }

    @Override
    public long getPendingOutputs(AEKey template) {
        long count = 0;
        if (this.job != null) {
            for (ObjectIterator<Object2ObjectMap.Entry<IPatternDetails, LongHolder>> it = job.tasks.object2ObjectEntrySet().fastIterator(); it.hasNext();) {
                var entry = it.next();
                for (var output : entry.getKey().getOutputs()) {
                    if (template.matches(output)) {
                        count += output.amount() * entry.getValue().value;
                    }
                }
            }
        }
        return count;
    }

    public Set<GlobalPos> getPendingRequests(AEKey template) {
        return this.pendingRequests.get(template);
    }

    @Override
    public void getAllItems(KeyCounter out) {
        out.addAll(this.inventory.list);
        if (this.job != null) {
            out.addAll(job.waitingFor.list);
            for (ObjectIterator<Object2ObjectMap.Entry<IPatternDetails, LongHolder>> it = job.tasks.object2ObjectEntrySet().fastIterator(); it.hasNext();) {
                var entry = it.next();
                for (var output : entry.getKey().getOutputs()) {
                    out.add(output.what(), output.amount() * entry.getValue().value);
                }
            }
        }
    }

    @Override
    public boolean isCantStoreItems() {
        return cantStoreItems;
    }

    private void finishJob(boolean success) {
        if (success) {
            job.link.markDone();
        } else {
            job.link.cancel();
        }

        job.waitingFor.clear();
        for (ObjectIterator<Object2ObjectMap.Entry<IPatternDetails, LongHolder>> it = job.tasks.object2ObjectEntrySet().fastIterator(); it.hasNext();) {
            for (var output : it.next().getKey().getOutputs()) {
                changeListener.onChange(output.what());
            }
        }

        notifyJobOwner(job, success ? CraftingJobStatusPacket.Status.FINISHED : CraftingJobStatusPacket.Status.CANCELLED);

        this.job = null;

        this.pendingRequests.clear();

        this.storeItems();
    }

    private void notifyJobOwner(ExecutingCraftingJob job, CraftingJobStatusPacket.Status status) {
        this.lastModifiedOnTick = TickHandler.instance().getCurrentTick();

        var playerId = job.playerId;
        if (playerId == null) {
            return;
        }

        var server = cluster.getLevel().getServer();
        var connectedPlayer = IPlayerRegistry.getConnected(server, playerId);
        if (connectedPlayer != null) {
            var jobId = job.link.getCraftingID();
            NetworkHandler.instance().sendTo(
                    new CraftingJobStatusPacket(
                            jobId,
                            job.finalOutput.what(),
                            job.finalOutput.amount(),
                            job.remainingAmount,
                            status),
                    connectedPlayer);
        }
    }
}

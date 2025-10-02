package com.gtocore.api.ae2.crafting;

import com.gtocore.common.data.GTOItems;
import com.gtocore.config.GTOConfig;
import com.gtocore.integration.ae.CraftingCpuHelperExtended;

import com.gtolib.GTOCore;
import com.gtolib.api.ae2.IPatternProviderLogic;
import com.gtolib.api.ae2.pattern.IDetails;
import com.gtolib.api.ae2.pattern.IParallelPatternDetails;
import com.gtolib.api.ae2.stacks.IKeyCounter;
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
import it.unimi.dsi.fastutil.objects.*;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OptimizedCraftingCpuLogic extends CraftingCpuLogic {

    final CraftingCPUCluster cluster;

    private ExecutingCraftingJob job = null;

    private Consumer<AEKey> listener = null;

    private final SetMultimap<AEKey, GlobalPos> pendingRequests = HashMultimap.create();
    private final SetMultimap<AEKey, IPatternProviderLogic.PushResult> craftingResults = HashMultimap.create();

    private final ListCraftingInventory.ChangeListener changeListener = what -> {
        lastModifiedOnTick = TickHandler.instance().getCurrentTick();
        if (listener != null) {
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

        KeyCounter missingIng;
        if (GTOConfig.INSTANCE.allowMissingCraftingJobs && src.player().isPresent()) {
            missingIng = CraftingCpuHelperExtended.tryExtractInitialItemsIgnoreMissing(plan, grid, inventory, src);
        } else {
            var missingIngredient = CraftingCpuHelper.tryExtractInitialItems(plan, grid, inventory, src);
            if (missingIngredient != null) return CraftingSubmitResult.missingIngredient(missingIngredient);
            missingIng = new KeyCounter();
        }

        var playerId = src.player()
                .map(p -> p instanceof ServerPlayer serverPlayer ? IPlayerRegistry.getPlayerId(serverPlayer) : null)
                .orElse(null);
        var craftId = UUID.randomUUID();
        var linkCpu = new CraftingLink(CraftingCpuHelper.generateLinkData(craftId, requester == null, false), cluster);
        this.job = new ExecutingCraftingJob(plan, changeListener, linkCpu, playerId, missingIng);
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

    private static void purgePatternEverywhere(Reference2ObjectOpenHashMap<AEKey, Object2LongOpenHashMap<IPatternDetails>> allocations, Object patternDefinition) {
        if (allocations == null || allocations.isEmpty() || patternDefinition == null) return;
        for (var outIt = allocations.reference2ObjectEntrySet().fastIterator(); outIt.hasNext();) {
            var out = outIt.next();
            var inner = out.getValue();
            if (inner == null || inner.isEmpty()) {
                outIt.remove();
                continue;
            }
            for (var inIt = inner.object2LongEntrySet().fastIterator(); inIt.hasNext();) {
                var pe = inIt.next();
                if (pe.getKey().getDefinition().equals(patternDefinition)) {
                    inIt.remove();
                }
            }
            if (inner.isEmpty()) {
                outIt.remove();
            }
        }
    }

    @Override
    public int executeCrafting(int maxPatterns, CraftingService craftingService, IEnergyService energyService, Level level) {
        var job = this.job;
        if (job == null) return 0;

        IntHolder pushedPatterns = new IntHolder(0);

        var it = job.tasks.object2ObjectEntrySet().fastIterator();

        taskLoop:
        while (it.hasNext()) {
            var task = it.next();
            var progress = task.getValue();
            if (progress.value <= 0) {
                it.remove();
                continue;
            }

            // 寻找样板和对应的可用优先物品名额
            if (!job.allocations.isEmpty()) {
                job.defsToPurge.clear();
                for (var outerIt = job.allocations.reference2ObjectEntrySet().fastIterator(); outerIt.hasNext();) {
                    var outer = outerIt.next();
                    var inner = outer.getValue();
                    if (inner == null || inner.isEmpty()) continue;
                    for (var peIt = inner.object2LongEntrySet().fastIterator(); peIt.hasNext();) {
                        var pe = peIt.next();
                        if (pe.getLongValue() <= 0) {
                            job.defsToPurge.add(pe.getKey().getDefinition());
                        }
                    }
                }
                for (var def : job.defsToPurge) {
                    purgePatternEverywhere(job.allocations, def);
                }
            }

            var tmp_details = task.getKey();
            boolean isParallel = tmp_details instanceof IParallelPatternDetails;
            job.expectedOutputs.clear();
            ObjectHolder<KeyCounter[]> craftingContainer = new ObjectHolder<>(null);
            long parallelValue = 1;
            if (isParallel && progress.value > 1) {
                var parallel = getMaxParallel(progress.value, tmp_details, IKeyCounter.of(inventory.list).gtolib$getMap());
                if (parallel == 0) continue;
                if (parallel > 1) {
                    var parallelPatternDetails = ((IParallelPatternDetails) tmp_details).getCopy();
                    parallelPatternDetails.parallel(parallel);
                    if ((craftingContainer.value = extractPatternInputs(parallelPatternDetails, inventory, job.expectedOutputs)) == null) {
                        continue;
                    } else {
                        parallelValue = parallel;
                        tmp_details = parallelPatternDetails;
                    }
                }
            } else {
                if ((craftingContainer.value = extractPatternInputs(tmp_details, inventory, job.expectedOutputs)) == null) continue;
            }
            var details = tmp_details;

            // 查找优先物品数量和识别可用并行
            var targetOutputKey = details.getPrimaryOutput().what();
            boolean allocationLimited = false;
            long cappedParallel = parallelValue;

            if (!job.allocations.isEmpty()) {
                job.totalConsumed.clear();
                for (var kc : craftingContainer.value) {
                    if (kc == null) continue;
                    for (var entry : kc) {
                        job.totalConsumed.addTo(entry.getKey(), entry.getLongValue());
                    }
                }
                long minAllowedUnits = Long.MAX_VALUE;
                var patternDefinition = details.getDefinition();
                for (var eIt = job.totalConsumed.reference2LongEntrySet().fastIterator(); eIt.hasNext();) {
                    var e = eIt.next();
                    var consumedKey = e.getKey();
                    long consumedTotal = e.getLongValue();
                    var allocMap = job.allocations.get(consumedKey);
                    if (allocMap == null || allocMap.isEmpty()) {
                        continue;
                    }
                    IPatternDetails allocKey = null;
                    for (var aeIt = allocMap.object2LongEntrySet().fastIterator(); aeIt.hasNext();) {
                        var ae = aeIt.next();
                        if (ae.getKey().getDefinition().equals(patternDefinition)) {
                            allocKey = ae.getKey();
                            break;
                        }
                    }

                    if (allocKey == null) {
                        this.craftingResults.put(targetOutputKey, IPatternProviderLogic.PushResult.INSUFFICIENT_PRIORITY);

                        CraftingCpuHelper.reinjectPatternInputs(inventory, craftingContainer.value);
                        continue taskLoop;
                    }
                    long quota = allocMap.getLong(allocKey);
                    long perUnit = Math.max(1, consumedTotal / parallelValue);
                    long allowedUnits = quota / perUnit;
                    if (allowedUnits < minAllowedUnits) {
                        minAllowedUnits = allowedUnits;
                        if (minAllowedUnits == 0) {
                            break;
                        }
                    }
                }
                if (minAllowedUnits != Long.MAX_VALUE) {
                    if (minAllowedUnits <= 0) {
                        this.craftingResults.put(targetOutputKey, IPatternProviderLogic.PushResult.INSUFFICIENT_PRIORITY);

                        CraftingCpuHelper.reinjectPatternInputs(inventory, craftingContainer.value);
                        continue;
                    }
                    if (minAllowedUnits < parallelValue) {
                        if (details instanceof IParallelPatternDetails parDetails && parDetails.getParallel() > 1) {
                            CraftingCpuHelper.reinjectPatternInputs(inventory, craftingContainer.value);
                            parDetails.parallel(minAllowedUnits);
                            job.expectedOutputs.reset();
                            craftingContainer.value = extractPatternInputs(details, inventory, job.expectedOutputs);
                            if (craftingContainer.value == null) {
                                continue;
                            }
                            cappedParallel = minAllowedUnits;
                            allocationLimited = true;
                        } else {
                            this.craftingResults.put(targetOutputKey, IPatternProviderLogic.PushResult.INSUFFICIENT_PRIORITY);
                            CraftingCpuHelper.reinjectPatternInputs(inventory, craftingContainer.value);
                            continue;
                        }
                    }
                }
            }

            for (ICraftingProvider iCraftingProvider : craftingService.getProviders(details)) {
                if (craftingContainer.value == null) break;
                if (iCraftingProvider.isBusy()) continue;

                job.currentConsumed.clear();
                for (var kc : craftingContainer.value) {
                    if (kc == null) continue;
                    for (var entry : kc) {
                        job.currentConsumed.addTo(entry.getKey(), entry.getLongValue());
                    }
                }
                long finalParallelValue = cappedParallel;
                boolean finalAllocationLimited = allocationLimited;
                double powerNeeded = 0.0;
                var kcArrPre = craftingContainer.value;
                if (kcArrPre != null) {
                    powerNeeded = CraftingCpuHelper.calculatePatternPower(kcArrPre) * finalParallelValue;
                }
                final double powerNeededFinal = powerNeeded;
                Supplier<IPatternProviderLogic.PushResult> pushPatternSuccess = () -> {
                    if (powerNeededFinal > 0) {
                        energyService.extractAEPower(powerNeededFinal, Actionable.MODULATE, PowerMultiplier.CONFIG);
                    }
                    pushedPatterns.value++;

                    for (var expectedOutput : job.expectedOutputs) {
                        job.waitingFor.insert(expectedOutput.getKey(), expectedOutput.getLongValue(), Actionable.MODULATE);
                    }

                    job.purgeDefsLocal.clear();
                    for (var ceIt = job.currentConsumed.reference2LongEntrySet().fastIterator(); ceIt.hasNext();) {
                        var ce = ceIt.next();
                        var key = ce.getKey();
                        var map = job.allocations.get(key);
                        if (map == null || map.isEmpty()) continue;
                        IPatternDetails matchedKey = null;
                        var detailsDef = details.getDefinition();
                        for (var aeIt = map.object2LongEntrySet().fastIterator(); aeIt.hasNext();) {
                            var ae = aeIt.next();
                            if (ae.getKey().getDefinition().equals(detailsDef)) {
                                matchedKey = ae.getKey();
                                break;
                            }
                        }
                        if (matchedKey != null) {
                            long q = map.getLong(matchedKey);
                            long newQ = q - ce.getLongValue();
                            if (newQ <= 0) {
                                job.purgeDefsLocal.add(detailsDef);
                            } else {
                                map.put(matchedKey, newQ);
                            }
                        }
                    }
                    // Perform global purge once per definition if any reached <= 0
                    for (var def : job.purgeDefsLocal) {
                        purgePatternEverywhere(job.allocations, def);
                    }

                    progress.value -= finalParallelValue;
                    if (progress.value <= 0) {
                        it.remove();
                        return IPatternProviderLogic.PushResult.BREAK;
                    }

                    if (pushedPatterns.value > maxPatterns) {
                        return IPatternProviderLogic.PushResult.BREAK_TASK_LOOP;
                    }

                    if (isParallel || finalAllocationLimited) {
                        return IPatternProviderLogic.PushResult.BREAK;
                    }

                    job.expectedOutputs.reset();
                    craftingContainer.value = extractPatternInputs(details, inventory, job.expectedOutputs);
                    return IPatternProviderLogic.PushResult.SUCCESS;
                };

                if (iCraftingProvider instanceof IPatternProviderLogic logic) {
                    var result = logic.gtolib$pushPattern(details, craftingContainer, pushPatternSuccess);
                    if (result != IPatternProviderLogic.PushResult.PATTERN_DOES_NOT_EXIST) {
                        this.pendingRequests.put(targetOutputKey, logic.gto$getPos());
                    }
                    if (!result.success()) {
                        this.craftingResults.put(targetOutputKey, result);
                        continue;
                    }
                    this.craftingResults.removeAll(targetOutputKey);
                    this.craftingResults.put(targetOutputKey, result);
                    cluster.markDirty();
                    switch (result) {
                        case BREAK:
                            continue taskLoop;
                        case BREAK_TASK_LOOP:
                            break taskLoop;
                    }
                } else if (iCraftingProvider.pushPattern(details, craftingContainer.value)) {
                    var result = pushPatternSuccess.get();
                    if (!result.success()) {
                        this.craftingResults.put(targetOutputKey, result);
                        continue;
                    }
                    this.craftingResults.removeAll(targetOutputKey);
                    this.craftingResults.put(targetOutputKey, result);
                    cluster.markDirty();
                    if (iCraftingProvider instanceof BlockEntity be) {
                        this.pendingRequests.put(targetOutputKey, GlobalPos.of(level.dimension(), be.getBlockPos()));
                    } else if (iCraftingProvider instanceof MetaMachine mm) {
                        this.pendingRequests.put(targetOutputKey, GlobalPos.of(level.dimension(), mm.getPos()));
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
        this.listener = listener;
    }

    @Override
    public void removeListener(Consumer<AEKey> listener) {
        this.listener = null;
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

    public SetMultimap<AEKey, IPatternProviderLogic.PushResult> getCraftingResults() {
        return this.craftingResults;
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
        this.craftingResults.clear();

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

    private static KeyCounter[] extractPatternInputs(IPatternDetails details, ListCraftingInventory sourceInv, KeyCounter expectedOutputs) {
        var inputs = details.getInputs();
        KeyCounter[] inputHolder = getInputHolder((IDetails) details);
        boolean found = true;

        var counter = IKeyCounter.of(sourceInv.list);
        for (int x = 0; x < inputs.length; x++) {
            var list = inputHolder[x];
            var input = inputs[x];
            long remainingMultiplier = input.getMultiplier();
            for (var stack : input.getPossibleInputs()) {
                var what = stack.what();
                if (counter.gtolib$contains(what)) {
                    var amount = stack.amount();
                    var extracted = sourceInv.extract(what, amount * remainingMultiplier, Actionable.MODULATE);
                    if (extracted == 0) continue;
                    list.add(what, extracted);
                    remainingMultiplier -= (extracted / amount);
                    if (remainingMultiplier == 0) break;
                }
            }

            if (remainingMultiplier > 0) {
                found = false;
                break;
            }
        }

        if (!found) {
            CraftingCpuHelper.reinjectPatternInputs(sourceInv, inputHolder);
            return null;
        }

        for (var output : details.getOutputs()) {
            expectedOutputs.add(output.what(), output.amount());
        }

        return inputHolder;
    }

    private static long getMaxParallel(long maxParallel, IPatternDetails details, Object2LongOpenHashMap<AEKey> sourceInv) {
        if (sourceInv == null) return 0;
        for (IPatternDetails.IInput input : details.getInputs()) {
            long extracted = 0;
            for (var stack : input.getPossibleInputs()) {
                extracted += (sourceInv.getLong(stack.what()) / stack.amount());
            }
            maxParallel = Math.min(maxParallel, extracted / input.getMultiplier());
            if (maxParallel < 1) {
                return 0;
            }
        }
        return maxParallel;
    }

    private static KeyCounter[] getInputHolder(IDetails details) {
        int length = details.getInputs().length;
        var inputHolder = new KeyCounter[length];
        var ih = details.gtolib$getInputHolder();
        for (int x = 0; x < length; x++) {
            var kc = ih[x];
            kc.clear();
            inputHolder[x] = kc;
        }
        return inputHolder;
    }
}

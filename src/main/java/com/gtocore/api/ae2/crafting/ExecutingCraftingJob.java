package com.gtocore.api.ae2.crafting;

import com.gtolib.api.ae2.pattern.IParallelPatternDetails;
import com.gtolib.utils.holder.LongHolder;

import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import appeng.api.config.Actionable;
import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingPlan;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.crafting.CraftingLink;
import appeng.crafting.execution.ElapsedTimeTracker;
import appeng.crafting.inv.ListCraftingInventory;
import appeng.me.service.CraftingService;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.jetbrains.annotations.Nullable;

class ExecutingCraftingJob {

    private static final String NBT_LINK = "link";
    private static final String NBT_PLAYER_ID = "playerId";
    private static final String NBT_FINAL_OUTPUT = "finalOutput";
    private static final String NBT_WAITING_FOR = "waitingFor";
    private static final String NBT_TIME_TRACKER = "timeTracker";
    private static final String NBT_REMAINING_AMOUNT = "remainingAmount";
    private static final String NBT_TASKS = "tasks";
    private static final String NBT_CRAFTING_PROGRESS = "#craftingProgress";

    final CraftingLink link;
    final ListCraftingInventory waitingFor;
    final Object2ObjectOpenHashMap<IPatternDetails, LongHolder> tasks = new O2OOpenCacheHashMap<>();
    final ElapsedTimeTracker timeTracker;
    final IElapsedTimeTracker tt;
    GenericStack finalOutput;
    long remainingAmount;
    Integer playerId;

    ExecutingCraftingJob(ICraftingPlan plan, ListCraftingInventory.ChangeListener changeListener, CraftingLink link, @Nullable Integer playerId, KeyCounter missingIng) {
        this(plan, changeListener, link, playerId);
        for (var what : missingIng.keySet()) {
            long amount = missingIng.get(what);
            waitingFor.insert(what, amount, Actionable.MODULATE);
            tt.gtolib$addMaxItems(amount, what.getType());
        }
    }

    ExecutingCraftingJob(ICraftingPlan plan, ListCraftingInventory.ChangeListener changeListener, CraftingLink link, @Nullable Integer playerId) {
        this.finalOutput = plan.finalOutput();
        this.remainingAmount = this.finalOutput.amount();
        this.waitingFor = new ListCraftingInventory(changeListener);

        // Fill waiting for and tasks
        this.timeTracker = new ElapsedTimeTracker();
        this.tt = (IElapsedTimeTracker) timeTracker;
        for (var entry : plan.emittedItems()) {
            waitingFor.insert(entry.getKey(), entry.getLongValue(), Actionable.MODULATE);
            tt.gtolib$addMaxItems(entry.getLongValue(), entry.getKey().getType());
        }
        for (var it = ((Object2LongOpenHashMap<IPatternDetails>) plan.patternTimes()).object2LongEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            var key = entry.getKey();
            long value = entry.getLongValue();
            tasks.computeIfAbsent(key, p -> new LongHolder(0)).value += value;
            for (var output : key.getOutputs()) {
                var amount = output.amount() * value * output.what().getAmountPerUnit();
                tt.gtolib$addMaxItems(amount, output.what().getType());
            }
        }
        this.link = link;
        this.playerId = playerId;
    }

    ExecutingCraftingJob(CompoundTag data, ListCraftingInventory.ChangeListener changeListener, OptimizedCraftingCpuLogic cpu) {
        this.link = new CraftingLink(data.getCompound(NBT_LINK), cpu.cluster);
        IGrid grid = cpu.cluster.getGrid();
        if (grid != null) {
            ((CraftingService) grid.getCraftingService()).addLink(link);
        }

        this.finalOutput = GenericStack.readTag(data.getCompound(NBT_FINAL_OUTPUT));
        this.remainingAmount = data.getLong(NBT_REMAINING_AMOUNT);
        this.waitingFor = new ListCraftingInventory(changeListener);
        this.waitingFor.readFromNBT(data.getList(NBT_WAITING_FOR, Tag.TAG_COMPOUND));
        this.timeTracker = new ElapsedTimeTracker(data.getCompound(NBT_TIME_TRACKER));
        this.tt = (IElapsedTimeTracker) timeTracker;
        if (data.contains(NBT_PLAYER_ID, Tag.TAG_INT)) {
            this.playerId = data.getInt(NBT_PLAYER_ID);
        } else {
            this.playerId = null;
        }

        ListTag tasksTag = data.getList(NBT_TASKS, Tag.TAG_COMPOUND);
        for (int i = 0; i < tasksTag.size(); ++i) {
            final CompoundTag item = tasksTag.getCompound(i);
            var pattern = AEItemKey.fromTag(item);
            var details = PatternDetailsHelper.decodePattern(pattern, cpu.cluster.getLevel());
            if (details != null) {
                long parallel = item.getLong("parallel");
                if (parallel > 0) {
                    details = IParallelPatternDetails.of(details, cpu.cluster.getLevel(), parallel);
                }
                final LongHolder tp = new LongHolder(item.getLong(NBT_CRAFTING_PROGRESS));
                this.tasks.put(details, tp);
            }
        }
    }

    CompoundTag writeToNBT() {
        CompoundTag data = new CompoundTag();

        CompoundTag linkData = new CompoundTag();
        link.writeToNBT(linkData);
        data.put(NBT_LINK, linkData);

        data.put(NBT_FINAL_OUTPUT, GenericStack.writeTag(finalOutput));

        data.put(NBT_WAITING_FOR, waitingFor.writeToNBT());
        data.put(NBT_TIME_TRACKER, timeTracker.writeToNBT());

        final ListTag list = new ListTag();
        for (ObjectIterator<Object2ObjectMap.Entry<IPatternDetails, LongHolder>> it = this.tasks.object2ObjectEntrySet().fastIterator(); it.hasNext();) {
            var e = it.next();
            var details = e.getKey();
            var item = details.getDefinition().toTag();
            item.putLong(NBT_CRAFTING_PROGRESS, e.getValue().value);
            if (details instanceof IParallelPatternDetails parallelPatternDetails) {
                item.putLong("parallel", parallelPatternDetails.getParallel());
            }
            list.add(item);
        }
        data.put(NBT_TASKS, list);

        data.putLong(NBT_REMAINING_AMOUNT, remainingAmount);
        if (this.playerId != null) {
            data.putInt(NBT_PLAYER_ID, this.playerId);
        }

        return data;
    }
}

package com.gtocore.integration.ftbquests;

import com.gtolib.GTOCore;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.KeyCounter;

import dev.ftb.mods.ftbquests.item.MissingItem;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class MEQuestDetectorTaskIndex {

    private static volatile Snapshot snapshot = Snapshot.EMPTY;

    private MEQuestDetectorTaskIndex() {}

    public static Snapshot getSnapshot() {
        var current = snapshot;
        var file = ServerQuestFile.INSTANCE;
        if (file == null) {
            return Snapshot.EMPTY;
        }
        if (current.fileIdentity == file) {
            return current;
        }

        synchronized (MEQuestDetectorTaskIndex.class) {
            current = snapshot;
            if (current.fileIdentity == file) {
                return current;
            }

            snapshot = rebuild(file);
            return snapshot;
        }
    }

    public static synchronized void invalidate(String reason) {
        GTOCore.LOGGER.info("[ME Quest Detector] Invalidating task index: {}", reason);
        snapshot = Snapshot.EMPTY;
    }

    private static Snapshot rebuild(ServerQuestFile file) {
        long startedAt = System.nanoTime();
        var byTaskId = new Long2ObjectOpenHashMap<IndexedItemTask>();
        var autoDetectByKey = new java.util.HashMap<AEItemKey, List<IndexedItemTask>>();
        var autoDetectTasks = new ArrayList<IndexedItemTask>();
        int expandedKeys = 0;

        for (var itemTask : file.collect(ItemTask.class)) {
            var indexedTask = indexTask(itemTask);
            if (indexedTask == null) {
                continue;
            }

            byTaskId.put(itemTask.id, indexedTask);
            expandedKeys += indexedTask.keys().size();

            if (indexedTask.autoDetectEligible()) {
                autoDetectTasks.add(indexedTask);
                for (var key : indexedTask.keys()) {
                    autoDetectByKey.computeIfAbsent(key, ignored -> new ArrayList<>()).add(indexedTask);
                }
            }
        }

        var built = new Snapshot(
                file,
                Collections.unmodifiableList(autoDetectTasks),
                Collections.unmodifiableMap(autoDetectByKey.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> List.copyOf(e.getValue())))),
                byTaskId,
                expandedKeys);

        long elapsedMicros = (System.nanoTime() - startedAt) / 1_000L;
        GTOCore.LOGGER.info(
                "[ME Quest Detector] Rebuilt task index: {} indexed item tasks, {} auto-detect tasks, {} expanded keys in {} us",
                byTaskId.size(), autoDetectTasks.size(), expandedKeys, elapsedMicros);
        return built;
    }

    @Nullable
    private static IndexedItemTask indexTask(ItemTask task) {
        if (task.getItemStack().isEmpty() || task.getItemStack().getItem() instanceof MissingItem) {
            return null;
        }

        var validItems = task.getValidDisplayItems();
        var rawItems = validItems.isEmpty() ? List.of(task.getItemStack()) : validItems;
        var keys = new LinkedHashSet<AEItemKey>();
        for (var stack : rawItems) {
            if (stack.isEmpty() || stack.getItem() instanceof MissingItem) {
                continue;
            }

            var normalized = stack.copy();
            normalized.setCount(1);
            var key = AEItemKey.of(normalized);
            if (key != null) {
                keys.add(key);
            }
        }

        if (keys.isEmpty()) {
            return null;
        }

        boolean autoDetectEligible = task.submitItemsOnInventoryChange() && !task.isOnlyFromCrafting() && !task.isTaskScreenOnly();
        boolean manualSubmitEligible = task.consumesResources() && !task.isTaskScreenOnly();

        if (keys.size() > 128) {
            GTOCore.LOGGER.info("[ME Quest Detector] Task {} expanded to {} concrete keys; this task will be watched exactly, not by global scan",
                    task.id, keys.size());
        }

        return new IndexedItemTask(task, List.copyOf(keys), autoDetectEligible, manualSubmitEligible);
    }

    public record IndexedItemTask(ItemTask task, List<AEItemKey> keys, boolean autoDetectEligible,
                                  boolean manualSubmitEligible) {

        public boolean isActiveForAutoDetect(TeamData teamData) {
            return autoDetectEligible && !teamData.isLocked() && !teamData.isCompleted(task) && teamData.canStartTasks(task.getQuest()) && checkSequential(teamData, task);
        }

        public boolean isActiveForManualSubmit(TeamData teamData) {
            return manualSubmitEligible && !teamData.isLocked() && !teamData.isCompleted(task) && teamData.canStartTasks(task.getQuest()) && checkSequential(teamData, task);
        }

        private static boolean checkSequential(TeamData teamData, Task task) {
            Quest quest = task.getQuest();
            if (!quest.getRequireSequentialTasks()) {
                return true;
            }

            var tasks = quest.getTasksAsList();
            int index = tasks.indexOf(task);
            return index >= 0 && (index == 0 || teamData.isCompleted(tasks.get(index - 1)));
        }
    }

    public record Snapshot(@Nullable Object fileIdentity,
                           List<IndexedItemTask> autoDetectTasks,
                           Map<AEItemKey, List<IndexedItemTask>> autoDetectByKey,
                           Long2ObjectOpenHashMap<IndexedItemTask> byTaskId,
                           int expandedKeyCount) {

        private static final Snapshot EMPTY = new Snapshot(null, List.of(), Map.of(), new Long2ObjectOpenHashMap<>(), 0);

        public Set<AEItemKey> collectWatchedKeys(TeamData teamData) {
            if (autoDetectTasks.isEmpty()) {
                return Set.of();
            }

            var watched = new LinkedHashSet<AEItemKey>();
            for (var indexedTask : autoDetectTasks) {
                if (indexedTask.isActiveForAutoDetect(teamData)) {
                    watched.addAll(indexedTask.keys());
                }
            }
            return watched;
        }

        public List<IndexedItemTask> getAutoDetectTasks(AEItemKey key) {
            return autoDetectByKey.getOrDefault(key, List.of());
        }

        @Nullable
        public IndexedItemTask getTask(long taskId) {
            return byTaskId.get(taskId);
        }

        public long computeStoredAmount(IndexedItemTask indexedTask, KeyCounter storage) {
            long total = 0L;
            long maxProgress = indexedTask.task().getMaxProgress();
            for (var key : indexedTask.keys()) {
                long remaining = maxProgress - total;
                if (remaining <= 0L) {
                    break;
                }
                total += Math.min(storage.get(key), remaining);
            }
            return Math.min(total, maxProgress);
        }

        public Collection<IndexedItemTask> getAutoDetectTasks() {
            return autoDetectTasks;
        }
    }
}

package com.gtocore.integration.ftbquests;

import com.gtocore.integration.ae.MEQuestDetectorPart;

import com.gtolib.GTOCore;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;

import java.util.List;

public final class MEQuestDetectorSubmitHelper {

    private MEQuestDetectorSubmitHelper() {}

    public static void submitFromNetwork(ItemTask task, TeamData teamData, ServerPlayer player, ItemStack craftedItem) {
        if (!task.consumesResources() || !craftedItem.isEmpty() || task.isTaskScreenOnly() || teamData.isCompleted(task) || teamData.isLocked() || !teamData.canStartTasks(task.getQuest())) {
            return;
        }

        var snapshot = MEQuestDetectorTaskIndex.getSnapshot();
        var indexedTask = snapshot.getTask(task.id);
        if (indexedTask == null || !indexedTask.isActiveForManualSubmit(teamData)) {
            return;
        }

        long remaining = task.getMaxProgress() - teamData.getProgress(task);
        if (remaining <= 0L) {
            return;
        }

        List<MEQuestDetectorPart> detectors = MEQuestDetectorPart.getActiveDetectors(teamData.getTeamId());
        if (detectors.isEmpty()) {
            return;
        }

        GTOCore.LOGGER.info(
                "[ME Quest Detector] Attempting network submit for task {} on team {} with {} detectors and {} remaining items",
                task.id, teamData.getTeamId(), detectors.size(), remaining);

        long submitted = 0L;
        for (var detector : detectors) {
            long used = detector.extractForTask(indexedTask, remaining - submitted);
            if (used > 0L) {
                submitted += used;
                GTOCore.LOGGER.info("[ME Quest Detector] Detector {} submitted {} items for task {}", detector, used, task.id);
            }
            if (submitted >= remaining) {
                break;
            }
        }

        if (submitted > 0L) {
            long finalSubmitted = submitted;
            MEQuestDetectorRuntime.runWithoutDirtyNotification(() -> teamData.addProgress(task, finalSubmitted));
            GTOCore.LOGGER.info("[ME Quest Detector] Submitted {} items from ME network for task {}", submitted, task.id);
        }
    }
}

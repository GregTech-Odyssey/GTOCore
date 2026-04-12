package com.gtocore.integration.ae;

import com.gtocore.integration.ftbquests.IMEQuestDetectorRuntimeTarget;
import com.gtocore.integration.ftbquests.MEQuestDetectorRuntime;
import com.gtocore.integration.ftbquests.MEQuestDetectorTaskIndex;

import com.gtolib.GTOCore;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.IStackWatcher;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageWatcherNode;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.core.AppEng;
import appeng.items.parts.PartModels;
import appeng.me.helpers.MachineSource;
import appeng.parts.PartModel;
import appeng.parts.reporting.AbstractDisplayPart;

import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class MEQuestDetectorPart extends AbstractDisplayPart implements IMEQuestDetectorRuntimeTarget {

    @PartModels
    public static final ResourceLocation MODEL_OFF = new ResourceLocation(AppEng.MOD_ID, "part/storage_monitor_off");
    @PartModels
    public static final ResourceLocation MODEL_ON = new ResourceLocation(AppEng.MOD_ID, "part/storage_monitor_on");
    @PartModels
    public static final ResourceLocation MODEL_LOCKED_OFF = new ResourceLocation(AppEng.MOD_ID, "part/storage_monitor_locked_off");
    @PartModels
    public static final ResourceLocation MODEL_LOCKED_ON = new ResourceLocation(AppEng.MOD_ID, "part/storage_monitor_locked_on");

    public static final IPartModel MODELS_OFF = new PartModel(MODEL_BASE, MODEL_OFF, MODEL_STATUS_OFF);
    public static final IPartModel MODELS_ON = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_ON);
    public static final IPartModel MODELS_HAS_CHANNEL = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_HAS_CHANNEL);
    public static final IPartModel MODELS_LOCKED_OFF = new PartModel(MODEL_BASE, MODEL_LOCKED_OFF, MODEL_STATUS_OFF);
    public static final IPartModel MODELS_LOCKED_ON = new PartModel(MODEL_BASE, MODEL_LOCKED_ON, MODEL_STATUS_ON);
    public static final IPartModel MODELS_LOCKED_HAS_CHANNEL = new PartModel(MODEL_BASE, MODEL_LOCKED_ON, MODEL_STATUS_HAS_CHANNEL);

    private static final List<MEQuestDetectorPart> ACTIVE_DETECTORS = new ArrayList<>();
    private static final long FULL_SYNC_COOLDOWN_TICKS = 10L;

    private final IActionSource actionSource = new MachineSource(this);
    private final IStorageWatcherNode watcherNode = new IStorageWatcherNode() {

        @Override
        public void updateWatcher(IStackWatcher newWatcher) {
            storageWatcher = newWatcher;
            GTOCore.LOGGER.info("[ME Quest Detector] Watcher updated for detector {}", MEQuestDetectorPart.this);
            refreshDetectorState("watcher-update", true);
        }

        @Override
        public void onStackChange(AEKey what, long amount) {
            if (!(what instanceof AEItemKey itemKey)) {
                return;
            }
            onDetectedStackChange(itemKey, amount);
        }
    };

    @Nullable
    private UUID ownerTeamId;
    @Nullable
    private UUID registeredTeamId;
    @Nullable
    private IStackWatcher storageWatcher;
    private Set<AEItemKey> configuredWatchKeys = Set.of();
    private boolean enabled;
    private boolean pendingFullSync;
    private boolean syncInProgress;
    private long lastFullSyncTick = Long.MIN_VALUE;
    private long lastContextChangeTick = Long.MIN_VALUE;

    public MEQuestDetectorPart(IPartItem<?> partItem) {
        super(partItem, false);
        getMainNode().setIdlePowerUsage(0);
        getMainNode().addService(IStorageWatcherNode.class, watcherNode);
    }

    public static List<MEQuestDetectorPart> getActiveDetectors(UUID teamId) {
        synchronized (ACTIVE_DETECTORS) {
            return ACTIVE_DETECTORS.stream()
                    .filter(detector -> Objects.equals(detector.ownerTeamId, teamId) && detector.isDetectorRuntimeActive())
                    .toList();
        }
    }

    @Override
    public void addToWorld() {
        super.addToWorld();
        synchronized (ACTIVE_DETECTORS) {
            ACTIVE_DETECTORS.add(this);
        }
        GTOCore.LOGGER.info("[ME Quest Detector] Added detector part {} to world", this);
        refreshDetectorState("add-to-world", false);
    }

    @Override
    public void removeFromWorld() {
        unregisterRuntimeBinding("remove-from-world");
        synchronized (ACTIVE_DETECTORS) {
            ACTIVE_DETECTORS.remove(this);
        }
        configuredWatchKeys = Set.of();
        GTOCore.LOGGER.info("[ME Quest Detector] Removed detector part {} from world", this);
        super.removeFromWorld();
    }

    @Override
    protected void onMainNodeStateChanged(IGridNodeListener.State reason) {
        super.onMainNodeStateChanged(reason);
        GTOCore.LOGGER.info("[ME Quest Detector] Main node state changed for {}: online={}, active={}, powered={}, reason={}",
                this, getMainNode().isOnline(), getMainNode().isActive(), getMainNode().isPowered(), reason);
        refreshDetectorState("main-node-state:" + reason, true);
    }

    @Override
    public boolean onPartActivate(Player player, InteractionHand hand, Vec3 pos) {
        if (isClientSide()) {
            return true;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return true;
        }

        if (ServerQuestFile.INSTANCE == null) {
            player.displayClientMessage(Component.literal("FTB Quests is not ready on the server."), true);
            return true;
        }

        TeamData teamData = ServerQuestFile.INSTANCE.getOrCreateTeamData(serverPlayer);
        ownerTeamId = teamData.getTeamId();
        enabled = true;
        pendingFullSync = true;

        GTOCore.LOGGER.info("[ME Quest Detector] Bound detector {} to team {} by player {}", this, ownerTeamId,
                serverPlayer.getGameProfile().getName());

        refreshDetectorState("player-bind", true);
        serverPlayer.displayClientMessage(Component.literal("ME Quest Detector bound to your FTB team and enabled."), true);
        return true;
    }

    @Override
    public boolean onPartShiftActivate(Player player, InteractionHand hand, Vec3 pos) {
        if (isClientSide()) {
            return true;
        }

        enabled = false;
        ownerTeamId = null;
        pendingFullSync = false;
        configuredWatchKeys = Set.of();
        refreshDetectorState("player-clear", false);

        if (player != null) {
            player.displayClientMessage(Component.literal("ME Quest Detector disabled and unbound."), true);
        }
        return true;
    }

    @Override
    public void readFromNBT(CompoundTag data) {
        super.readFromNBT(data);
        enabled = data.getBoolean("enabled");
        ownerTeamId = data.hasUUID("ownerTeamId") ? data.getUUID("ownerTeamId") : null;
        pendingFullSync = data.getBoolean("pendingFullSync");
    }

    @Override
    public void writeToNBT(CompoundTag data) {
        super.writeToNBT(data);
        data.putBoolean("enabled", enabled);
        data.putBoolean("pendingFullSync", pendingFullSync);
        if (ownerTeamId != null) {
            data.putUUID("ownerTeamId", ownerTeamId);
        }
    }

    @Override
    public void writeToStream(FriendlyByteBuf data) {
        super.writeToStream(data);
        data.writeBoolean(enabled);
        data.writeBoolean(ownerTeamId != null);
        if (ownerTeamId != null) {
            data.writeUUID(ownerTeamId);
        }
    }

    @Override
    public boolean readFromStream(FriendlyByteBuf data) {
        boolean changed = super.readFromStream(data);
        enabled = data.readBoolean();
        ownerTeamId = data.readBoolean() ? data.readUUID() : null;
        return changed;
    }

    @Override
    public IPartModel getStaticModels() {
        return enabled && ownerTeamId != null ? selectModel(MODELS_LOCKED_OFF, MODELS_LOCKED_ON, MODELS_LOCKED_HAS_CHANNEL) : selectModel(MODELS_OFF, MODELS_ON, MODELS_HAS_CHANNEL);
    }

    @Override
    public UUID getDetectorBoundTeamId() {
        return ownerTeamId;
    }

    @Override
    public boolean isDetectorRuntimeActive() {
        return enabled && ownerTeamId != null && getBlockEntity() != null && !getBlockEntity().isRemoved();
    }

    @Override
    public void onDetectorContextChanged(String reason) {
        pendingFullSync = true;
        var level = getLevel();
        if (level != null) {
            long gameTime = level.getGameTime();
            if (lastContextChangeTick != Long.MIN_VALUE && gameTime - lastContextChangeTick < FULL_SYNC_COOLDOWN_TICKS) {
                GTOCore.LOGGER.info(
                        "[ME Quest Detector] Coalescing context change for detector {} at gameTime {} because last context change was at {} and reason was {}",
                        this, gameTime, lastContextChangeTick, reason);
                return;
            }
            lastContextChangeTick = gameTime;
        }

        GTOCore.LOGGER.info("[ME Quest Detector] Detector {} received context change notification: {}", this, reason);
        if (getMainNode().isOnline() && !syncInProgress) {
            performFullSync(reason);
        }
    }

    public long extractForTask(MEQuestDetectorTaskIndex.IndexedItemTask indexedTask, long requestedAmount) {
        if (requestedAmount <= 0L || !enabled || ownerTeamId == null || !getMainNode().isOnline()) {
            return 0L;
        }

        var grid = getMainNode().getGrid();
        if (grid == null) {
            return 0L;
        }

        var inventory = grid.getStorageService().getInventory();
        long extracted = 0L;
        for (var key : indexedTask.keys()) {
            long remaining = requestedAmount - extracted;
            if (remaining <= 0L) {
                break;
            }
            long current = inventory.extract(key, remaining, appeng.api.config.Actionable.MODULATE, actionSource);
            extracted += current;
            if (current > 0L) {
                GTOCore.LOGGER.info("[ME Quest Detector] Detector {} extracted {} of {} for task {}", this, current, key,
                        indexedTask.task().id);
            }
        }
        return extracted;
    }

    private void onDetectedStackChange(AEItemKey key, long newAmount) {
        if (!enabled || ownerTeamId == null || !getMainNode().isOnline()) {
            return;
        }

        TeamData teamData = getTeamData();
        if (teamData == null) {
            return;
        }

        var grid = getMainNode().getGrid();
        if (grid == null) {
            return;
        }

        var cachedInventory = grid.getStorageService().getCachedInventory();
        var snapshot = MEQuestDetectorTaskIndex.getSnapshot();
        boolean changed = false;

        GTOCore.LOGGER.info("[ME Quest Detector] Stack change detected on {}: key={}, amount={}", this, key, newAmount);
        for (var indexedTask : snapshot.getAutoDetectTasks(key)) {
            if (!indexedTask.isActiveForAutoDetect(teamData)) {
                continue;
            }

            long oldProgress = teamData.getProgress(indexedTask.task());
            long newProgress = snapshot.computeStoredAmount(indexedTask, cachedInventory);
            if (oldProgress != newProgress) {
                long taskId = indexedTask.task().id;
                MEQuestDetectorRuntime.runWithoutDirtyNotification(() -> teamData.setProgress(indexedTask.task(), newProgress));
                GTOCore.LOGGER.info(
                        "[ME Quest Detector] Updated task {} from {} to {} after stack change on detector {}",
                        taskId, oldProgress, newProgress, this);
                changed = true;
            }
        }

        if (changed) {
            pendingFullSync = true;
            performFullSync("stack-change-follow-up");
        }
    }

    private void refreshDetectorState(String reason, boolean tryFullSync) {
        syncRuntimeBinding(reason);
        reconfigureWatchers(reason);
        if (tryFullSync && pendingFullSync) {
            performFullSync(reason);
        }
        getHost().markForSave();
        getHost().markForUpdate();
    }

    private void syncRuntimeBinding(String reason) {
        UUID targetTeam = isDetectorRuntimeActive() ? ownerTeamId : null;
        if (Objects.equals(registeredTeamId, targetTeam)) {
            return;
        }

        unregisterRuntimeBinding(reason);

        if (targetTeam != null) {
            registeredTeamId = targetTeam;
            MEQuestDetectorRuntime.bind(targetTeam, this);
            GTOCore.LOGGER.info("[ME Quest Detector] Runtime binding refreshed for {}: team={}, reason={}",
                    this, targetTeam, reason);
        }
    }

    private void unregisterRuntimeBinding(String reason) {
        if (registeredTeamId == null) {
            return;
        }

        MEQuestDetectorRuntime.unbind(registeredTeamId, this);
        GTOCore.LOGGER.info("[ME Quest Detector] Runtime binding removed for {}: team={}, reason={}",
                this, registeredTeamId, reason);
        registeredTeamId = null;
    }

    private void reconfigureWatchers(String reason) {
        var watcher = storageWatcher;
        if (watcher == null) {
            configuredWatchKeys = Set.of();
            return;
        }

        watcher.reset();

        TeamData teamData = getTeamData();
        if (!enabled || ownerTeamId == null || !getMainNode().isOnline() || teamData == null) {
            configuredWatchKeys = Set.of();
            GTOCore.LOGGER.info("[ME Quest Detector] Watchers reset for {} because detector is inactive: {}", this, reason);
            return;
        }

        var targetKeys = MEQuestDetectorTaskIndex.getSnapshot().collectWatchedKeys(teamData);
        for (var key : targetKeys) {
            watcher.add(key);
        }
        configuredWatchKeys = targetKeys;
        GTOCore.LOGGER.info("[ME Quest Detector] Configured {} watched keys for detector {}: {}", targetKeys.size(), this, reason);
    }

    private void performFullSync(String reason) {
        if (!pendingFullSync || syncInProgress || !enabled || ownerTeamId == null || !getMainNode().isOnline()) {
            return;
        }

        var level = getLevel();
        if (level != null) {
            long gameTime = level.getGameTime();
            if (lastFullSyncTick != Long.MIN_VALUE && gameTime - lastFullSyncTick < FULL_SYNC_COOLDOWN_TICKS) {
                GTOCore.LOGGER.info("[ME Quest Detector] Throttling full sync for detector {} at gameTime {} because last sync was at {} and reason was {}",
                        this, gameTime, lastFullSyncTick, reason);
                pendingFullSync = true;
                return;
            }
            lastFullSyncTick = gameTime;
        }

        TeamData teamData = getTeamData();
        var grid = getMainNode().getGrid();
        if (teamData == null || grid == null) {
            return;
        }

        syncInProgress = true;
        try {
            int pass = 0;
            boolean changed;
            do {
                pass++;
                changed = false;
                pendingFullSync = false;

                var snapshot = MEQuestDetectorTaskIndex.getSnapshot();
                var cachedInventory = grid.getStorageService().getCachedInventory();
                GTOCore.LOGGER.info("[ME Quest Detector] Starting full sync pass {} for detector {}: {}", pass, this, reason);

                for (var indexedTask : snapshot.getAutoDetectTasks()) {
                    if (!indexedTask.isActiveForAutoDetect(teamData)) {
                        continue;
                    }

                    long oldProgress = teamData.getProgress(indexedTask.task());
                    long newProgress = snapshot.computeStoredAmount(indexedTask, cachedInventory);
                    if (oldProgress != newProgress) {
                        MEQuestDetectorRuntime.runWithoutDirtyNotification(() -> teamData.setProgress(indexedTask.task(), newProgress));
                        GTOCore.LOGGER.info(
                                "[ME Quest Detector] Full sync updated task {} from {} to {} on pass {} for detector {}",
                                indexedTask.task().id, oldProgress, newProgress, pass, this);
                        changed = true;
                    }
                }
            } while (changed && pass < 4);

            reconfigureWatchers("full-sync-complete:" + reason);
            GTOCore.LOGGER.info("[ME Quest Detector] Full sync finished for detector {} after reason {}", this, reason);
        } finally {
            syncInProgress = false;
        }

        if (pendingFullSync) {
            GTOCore.LOGGER.info("[ME Quest Detector] Full sync re-run requested for detector {} after {}", this, reason);
            performFullSync("deferred:" + reason);
        }
    }

    @Nullable
    private TeamData getTeamData() {
        if (ownerTeamId == null || ServerQuestFile.INSTANCE == null) {
            return null;
        }
        return ServerQuestFile.INSTANCE.getOrCreateTeamData(ownerTeamId);
    }

    @Override
    public String toString() {
        return "MEQuestDetectorPart{" +
                "team=" + ownerTeamId +
                ", enabled=" + enabled +
                ", watchedKeys=" + configuredWatchKeys.size() +
                '}';
    }
}


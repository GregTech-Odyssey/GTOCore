package com.gtocore.integration.ae.wireless;

import com.gtocore.common.saved.WirelessSavedData;

import com.gtolib.api.network.NetworkPack;

import com.gregtechceu.gtceu.api.machine.TickableSubscription;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import com.fast.fastcollection.O2OOpenCacheHashMap;
import com.lowdragmc.lowdraglib.LDLib;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
@Setter
@Mod.EventBusSubscriber
public class WirelessMachineRunTime {

    public static final SyncField GRID_CACHE = new SyncField();
    public static final O2OOpenCacheHashMap<UUID, SyncField> GRID_ACCESSIBLE_CACHEs = new O2OOpenCacheHashMap<>();

    static Runnable connectPageFreshRun = () -> {};

    final WirelessMachine machine;

    String gridWillAdded = "";
    WirelessGrid gridConnected = null; // ONLY SERVER
    TickableSubscription initTickableSubscription = null;
    boolean shouldAutoConnect = false;

    // 编辑网络昵称的输入缓存
    private String gridNicknameEdit = "";

    public WirelessMachineRunTime(WirelessMachine machine) {
        this.machine = machine;
    }

    private static <T> void clientRefresh(LogicalSide side, T oldValue, T newValue) {
        if (!side.isServer()) {
            connectPageFreshRun.run();
        }
    }

    private static <T> void serverNoop(LogicalSide side, T oldValue, T newValue) {
        // No operation on server side
    }

    private static final NetworkPack gridCacheSYNCER = NetworkPack.registerS2C("wirelessMachineGridCacheSyncS2C", WirelessMachineRunTime::read);

    @Getter
    public static class SyncField {

        List<WirelessGrid> grids;
        boolean needSyncGridCache = false;

        void setGridsAndMark(List<WirelessGrid> newValues) {
            this.grids = newValues;
            needSyncGridCache = true;
        }

        void write(FriendlyByteBuf buf) {
            writeHeader(buf);
            buf.writeVarInt(grids.size());
            for (var grid : grids) {
                buf.writeNbt(grid.encodeToNbt());
            }
        }

        void writeHeader(FriendlyByteBuf buf) {
            buf.writeOptional(Optional.empty(), FriendlyByteBuf::writeUUID);
        }

        void clear() {
            grids = null;
            needSyncGridCache = false;
        }
    }

    static SyncField getAccessibleCacheForPlayer(UUID playerUUID) {
        return GRID_ACCESSIBLE_CACHEs.computeIfAbsent(playerUUID, uuid -> new SyncField() {

            @Override
            void writeHeader(FriendlyByteBuf buf) {
                buf.writeOptional(Optional.of(playerUUID), FriendlyByteBuf::writeUUID);
            }
        });
    }

    private static void read(Player p, FriendlyByteBuf buf) {
        SyncField field = buf.readOptional(FriendlyByteBuf::readUUID)
                .map(WirelessMachineRunTime::getAccessibleCacheForPlayer)
                .orElse(GRID_CACHE);
        var size = buf.readVarInt();
        List<WirelessGrid> grids = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            if (buf.readNbt() instanceof CompoundTag tag) {
                grids.add(WirelessGrid.Companion.decodeFromNbt(tag));
            }
        }
        field.grids = grids;
        clientRefresh(LogicalSide.CLIENT, null, null);
    }

    static void refreshCachesOnServer(UUID requesterUUID) {
        if (LDLib.isRemote()) return;
        // 全量
        GRID_CACHE.setGridsAndMark(WirelessSavedData.Companion.getINSTANCE().getGridPool());
        // 可访问（服务端统一裁剪）
        WirelessMachineRunTime.getAccessibleCacheForPlayer(requesterUUID).setGridsAndMark(
                WirelessSavedData.Companion.accessibleGridsFor(requesterUUID));
    }

    @SubscribeEvent
    public static void onTickEnd(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.getServer() != null) {
            Stream.concat(GRID_ACCESSIBLE_CACHEs.values().stream(), Stream.of(GRID_CACHE))
                    .filter(s -> s.needSyncGridCache)
                    .forEach(s -> {
                        gridCacheSYNCER.send(s::write, event.getServer());
                        s.needSyncGridCache = false;
                    });
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        refreshCachesOnServer(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        GRID_CACHE.clear();
        GRID_ACCESSIBLE_CACHEs.clear();
    }
}

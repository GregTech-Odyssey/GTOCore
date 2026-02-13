package com.gtocore.integration.ae.wireless;

import com.gtolib.api.capability.ISync;
import com.gtolib.api.network.NetworkPack;
import com.gtolib.utils.ServerUtils;

import com.gregtechceu.gtceu.api.machine.TickableSubscription;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WirelessMachineRunTime {

    final WirelessMachine machine;

    String gridWillAdded = "";
    WirelessGrid gridConnected = null; // ONLY SERVER
    static Runnable connectPageFreshRun = () -> {};
    static Runnable detailsPageFreshRun = () -> {};
    TickableSubscription initTickableSubscription = null;
    boolean shouldAutoConnect = false;

    // 编辑网络昵称的输入缓存
    private String gridNicknameEdit = "";

    // 防止 UI 侧刷新循环：仅在客户端接收端刷新 UI，服务端不触发 fresh()
    private ISync.EnumSyncedField<FilterInMachineType> FilterInMachineTypeSyncField;

    public WirelessMachineRunTime(WirelessMachine machine) {
        this.machine = machine;

        // 初始化枚举同步字段
        this.FilterInMachineTypeSyncField = ISync.createEnumField(machine);

        FilterInMachineTypeSyncField.setReceiverListener(WirelessMachineRunTime::clientRefresh);
        FilterInMachineTypeSyncField.setSenderListener(WirelessMachineRunTime::serverNoop);
        FilterInMachineTypeSyncField.set(FilterInMachineType.BOTH);

        this.FilterInMachineTypeSyncField.set(FilterInMachineType.BOTH);
    }

    private static <T> void clientRefresh(LogicalSide side, T oldValue, T newValue) {
        if (!side.isServer()) {
            connectPageFreshRun.run();
            detailsPageFreshRun.run();
        }
    }

    private static <T> void serverNoop(LogicalSide side, T oldValue, T newValue) {
        // No operation on server side
    }

    private static final NetworkPack gridCacheSYNCER = NetworkPack.registerS2C("wirelessMachineGridCacheSyncS2C", WirelessMachineRunTime::read);

    public enum SyncField {

        GRID_CACHE,
        GRID_ACCESSIBLE_CACHE;

        List<WirelessGrid> values;

        public void setAndSync(List<WirelessGrid> newValues) {
            this.values = newValues;
            gridCacheSYNCER.send(this::write, ServerUtils.getServer());
        }

        public List<WirelessGrid> get() {
            return values;
        }

        void write(FriendlyByteBuf buf) {
            buf.writeVarInt(ordinal());
            buf.writeVarInt(values.size());
            for (var grid : values) {
                buf.writeNbt(grid.encodeToNbt());
            }
        }
    }

    private static void read(Player p, FriendlyByteBuf buf) {
        var field = SyncField.values()[buf.readVarInt()];
        var size = buf.readVarInt();
        List<WirelessGrid> grids = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            if (buf.readNbt() instanceof CompoundTag tag) {
                grids.add(WirelessGrid.Companion.decodeFromNbt(tag));
            }
        }
        field.values = grids;
        clientRefresh(LogicalSide.CLIENT, null, null);
    }
}

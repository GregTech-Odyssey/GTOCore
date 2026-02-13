package com.gtocore.integration.ae.wireless;

import com.gtolib.api.capability.ISync;
import com.gtolib.api.network.SyncManagedFieldHolder;
import com.gtolib.utils.ServerUtils;

import com.gregtechceu.gtceu.api.machine.TickableSubscription;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.gtocore.common.saved.WirelessSavedDataKt.createWirelessSyncedField;

// ...existing imports...

@Getter
@Setter
public class WirelessMachineRunTime {

    static class Sync implements ISync {

        ISync.ObjectSyncedField<List<WirelessGrid>> gridCache;
        ISync.ObjectSyncedField<List<WirelessGrid>> gridAccessibleCache;

        @Override
        public Level getLevel() {
            return ServerUtils.getServer().overworld();
        }

        @Override
        public BlockPos getPos() {
            return BlockPos.ZERO.offset(0, -947, 0);
        }

        @Override
        public SyncManagedFieldHolder getSyncHolder() {
            return holder;
        }
    }

    static final Sync syncObj = new Sync();
    static {

        syncObj.gridCache = createWirelessSyncedField(syncObj).set(new ArrayList<>());
        syncObj.gridAccessibleCache = createWirelessSyncedField(syncObj).set(new ArrayList<>());
    }
    static SyncManagedFieldHolder holder = new SyncManagedFieldHolder(Sync.class);

    static ISync.ObjectSyncedField<List<WirelessGrid>> getGridCache() {
        return syncObj.gridCache;
    }

    static ISync.ObjectSyncedField<List<WirelessGrid>> getGridAccessibleCache() {
        return syncObj.gridAccessibleCache;
    }

    // ...existing code...
    final WirelessMachine machine;

    String gridWillAdded = "";
    WirelessGrid gridConnected = null; // ONLY SERVER
    Runnable connectPageFreshRun = () -> {};
    Runnable detailsPageFreshRun = () -> {};
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

        getGridCache().setReceiverListener(this::clientRefresh);
        getGridCache().setSenderListener(this::serverNoop);
        getGridAccessibleCache().setReceiverListener(this::clientRefresh);
        getGridAccessibleCache().setSenderListener(this::serverNoop);
        FilterInMachineTypeSyncField.setReceiverListener(this::clientRefresh);
        FilterInMachineTypeSyncField.setSenderListener(this::serverNoop);
        FilterInMachineTypeSyncField.set(FilterInMachineType.BOTH);

        this.FilterInMachineTypeSyncField.set(FilterInMachineType.BOTH);
    }

    private <T> void clientRefresh(LogicalSide side, T oldValue, T newValue) {
        if (!side.isServer()) {
            connectPageFreshRun.run();
            detailsPageFreshRun.run();
        }
    }

    private <T> void serverNoop(LogicalSide side, T oldValue, T newValue) {
        // No operation on server side
    }
}

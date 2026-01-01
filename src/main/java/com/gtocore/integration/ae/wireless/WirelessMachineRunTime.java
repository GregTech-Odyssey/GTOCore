package com.gtocore.integration.ae.wireless;

import com.gtolib.api.capability.ISync;

import com.gregtechceu.gtceu.api.machine.TickableSubscription;

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

    // ...existing code...
    final WirelessMachine machine;

    String gridWillAdded = "";
    WirelessGrid gridConnected = null; // ONLY SERVER
    Runnable connectPageFreshRun = () -> {};
    Runnable detailsPageFreshRun = () -> {};
    TickableSubscription initTickableSubscription = null;
    boolean shouldAutoConnect = false;

    ISync.ObjectSyncedField<List<WirelessGrid>> gridCache;
    ISync.ObjectSyncedField<List<WirelessGrid>> gridAccessibleCache;

    // 编辑网络昵称的输入缓存
    private String gridNicknameEdit = "";

    // 防止 UI 侧刷新循环：仅在客户端接收端刷新 UI，服务端不触发 fresh()
    private ISync.EnumSyncedField<FilterInMachineType> FilterInMachineTypeSyncField;

    public WirelessMachineRunTime(WirelessMachine machine) {
        this.machine = machine;

        // 初始化枚举同步字段
        this.FilterInMachineTypeSyncField = ISync.createEnumField(machine);
        this.gridCache = createWirelessSyncedField(machine).set(new ArrayList<>());
        this.gridAccessibleCache = createWirelessSyncedField(machine).set(new ArrayList<>());

        gridCache.setReceiverListener(this::clientRefresh);
        gridCache.setSenderListener(this::serverNoop);
        gridAccessibleCache.setReceiverListener(this::clientRefresh);
        gridAccessibleCache.setSenderListener(this::serverNoop);
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

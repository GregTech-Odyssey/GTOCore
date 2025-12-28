package com.gtocore.integration.ae.wireless;

import com.gtocore.common.saved.STATUS;
import com.gtocore.common.saved.WirelessSavedData;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.capability.ISync;
import com.gtolib.api.player.IEnhancedPlayer;

import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.gregtechceu.gtceu.utils.TaskHandler;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import com.hepdd.gtmthings.api.capability.IBindable;

import java.util.UUID;

@DataGeneratorScanned
public interface WirelessMachine extends IGridConnectedMachine, ISync, IBindable {

    // Companion object constants -> public static final fields
    @RegisterLanguage(cn = "网络节点选择", en = "Grid Node Selector")
    String gridNodeSelector = "gtocore.integration.ae.WirelessMachine.gridNodeSelector";

    @RegisterLanguage(cn = "网络节点列表", en = "Grid Node List")
    String gridNodeList = "gtocore.integration.ae.WirelessMachine.gridNodeList";

    @RegisterLanguage(cn = "绑定到玩家 : %s", en = "Bind to player: %s")
    String player = "gtocore.integration.ae.WirelessMachine.player";

    @RegisterLanguage(cn = "当前连接到 : %s", en = "Currently connected: %s")
    String currentlyConnectedTo = "gtocore.integration.ae.WirelessMachine.currentlyConnectedTo";

    @RegisterLanguage(cn = "创建网络", en = "Create Grid")
    String createGrid = "gtocore.integration.ae.WirelessMachine.createGrid";

    @RegisterLanguage(cn = "全球可用无线网络 : %s / %s", en = "Global available wireless grids: %s / %s")
    String globalWirelessGrid = "gtocore.integration.ae.WirelessMachine.globalWirelessGrid";

    @RegisterLanguage(cn = "删除", en = "Remove")
    String removeGrid = "gtocore.integration.ae.WirelessMachine.removeGrid";

    @RegisterLanguage(cn = "你的无线网络 : ", en = "Your wireless grids: ")
    String yourWirelessGrid = "gtocore.integration.ae.WirelessMachine.yourWirelessGrid";

    @RegisterLanguage(cn = "查找机器，机器将会高亮", en = "Find Machine, machine will be highlighted")
    String findMachine = "gtocore.integration.ae.WirelessMachine.findMachine";

    @RegisterLanguage(cn = "此机器被禁止连接ME无线网络", en = "This machine is banned from connecting to ME wireless network")
    String banned = "gtocore.integration.ae.WirelessMachine.banned";

    @RegisterLanguage(cn = "断开无线网络", en = "Leave Wireless Grid")
    String leave = "gtocore.integration.ae.WirelessMachine.leave";

    @RegisterLanguage(cn = "修改网络昵称", en = "Rename Grid")
    String renameGrid = "gtocore.integration.ae.WirelessMachine.renameGrid";

    // Properties (Kotlin 'var' fields) -> abstract getter/setter signatures
    WirelessMachinePersisted getWirelessMachinePersisted0();

    void setWirelessMachinePersisted0(WirelessMachinePersisted v);

    WirelessMachineRunTime getWirelessMachineRunTime0();

    void setWirelessMachineRunTime0(WirelessMachineRunTime v);

    // requesterUUID val with getter - keep as default method but preserve original logic in comment
    default UUID getRequesterUUID() {
        // Original Kotlin:
        // val requesterUUID: UUID
        // get() = self().ownerUUID ?: uuid
        // Try to preserve behavior but delegate to self()/getUUID(); if underlying names differ, adjust.

        UUID owner = self().getOwnerUUID();
        return owner != null ? owner : this.getUUID();
    }

    // Callback stubs - can be overridden by implementing classes
    default void addedToGrid(String gridName) {
        // original empty body in Kotlin
    }

    default void removedFromGrid(String gridName) {
        // original empty body in Kotlin
    }

    default boolean allowThisMachineConnectToWirelessGrid() {
        return true; // Kotlin default
    }

    // Lifecycle hooks - large Kotlin-specific implementations are preserved in comments
    default void onWirelessMachineLoad() {
        // Original Kotlin implementation preserved below for manual conversion.
        /*
         * if (self().isRemote) return
         * wirelessMachineRunTime.initTickableSubscription = TaskHandler.enqueueServerTick(level as ServerLevel, {
         * if (mainNode.node != null && self().holder.offsetTimer % 20 == 0) {
         * if (!wirelessMachinePersisted.beSet && wirelessMachineRunTime.shouldAutoConnect) {
         * // 使用按请求者计算的可访问列表，寻找“当前玩家”的默认网格
         * WirelessSavedData.Companion.accessibleGridsFor(self().ownerUUID ?: uuid)
         * .firstOrNull { it.isDefault }
         * ?.let { joinGrid(it.name) }
         * wirelessMachinePersisted.beSet = true
         * } else {
         * if (wirelessMachinePersisted.gridConnectedName.isNotEmpty()) {
         * linkGrid(wirelessMachinePersisted.gridConnectedName)
         * }
         * }
         * // 机器加载完成后进行一次数据同步，避免 UI 打开时需要主动拉取
         * wirelessMachineRunTime.initTickableSubscription?.unsubscribe()
         * }
         * }, GTUtil.NOOP, 40)
         */
        if (self().isRemote()) return;
        this.getWirelessMachineRunTime0().setInitTickableSubscription(
                TaskHandler.enqueueServerTick((ServerLevel) getLevel(), () -> {
                    if (this.getMainNode().getNode() != null) {
                        if (!this.getWirelessMachinePersisted0().isBeSet() && this.getWirelessMachineRunTime0().isShouldAutoConnect()) {
                            // 使用按请求者计算的可访问列表，寻找“当前玩家”的默认网格
                            for (var grid : WirelessSavedData.Companion.accessibleGridsFor(self().getOwnerUUID() != null ? self().getOwnerUUID() : this.getUUID())) {
                                if (grid.isDefault()) {
                                    joinGrid(grid.getName());
                                    break;
                                }
                            }
                            this.getWirelessMachinePersisted0().setBeSet(true);
                        } else {
                            if (!this.getWirelessMachinePersisted0().getGridConnectedName().isEmpty()) {
                                linkGrid(this.getWirelessMachinePersisted0().getGridConnectedName());
                            }
                        }
                        // 机器加载完成后进行一次数据同步，避免 UI 打开时需要主动拉取
                        this.getWirelessMachineRunTime0().getInitTickableSubscription().unsubscribe();
                    }
                }, 20, 40));
    }

    default void onWirelessMachineUnLoad() {
        /*
         * if (self().isRemote) return
         * wirelessMachineRunTime.initTickableSubscription?.unsubscribe()
         * unLinkGrid()
         */
        if (self().isRemote()) return;
        this.getWirelessMachineRunTime0().getInitTickableSubscription().unsubscribe();
        unLinkGrid();
    }

    default void onWirelessMachineClientTick() {
        // original empty body
    }

    default void onWirelessMachinePlaced(LivingEntity player, ItemStack stack) {
        /*
         * player?.let {
         * self().ownerUUID = it.uuid
         * if (player is Player) {
         * if (IEnhancedPlayer.of(player).playerData.shiftState) wirelessMachineRunTime.shouldAutoConnect = true
         * }
         * }
         * self().requestSync()
         */
        if (player != null) {
            self().setOwnerUUID(player.getUUID());
            if (player instanceof Player) {
                if (IEnhancedPlayer.of((Player) player).getPlayerData().shiftState) {
                    this.getWirelessMachineRunTime0().setShouldAutoConnect(true);
                }
            }
        }
        self().requestSync();
    }

    // getUUID comes from other interfaces; keep default to match Kotlin's override
    default UUID getUUID() {
        // Kotlin override: override fun getUUID(): UUID = self().ownerUUID ?: UUID.randomUUID()\
        UUID owner = this.self().getOwnerUUID();
        return owner != null ? owner : UUID.randomUUID();
    }

    // Utility methods
    default void refreshCachesOnServer() {
        /*
         * if (self().isRemote) return
         * // 全量
         * wirelessMachineRunTime.gridCache.setAndSyncToClient(WirelessSavedData.Companion.INSTANCE.gridPool)
         * // 可访问（服务端统一裁剪）
         * wirelessMachineRunTime.gridAccessibleCache.setAndSyncToClient(
         * WirelessSavedData.Companion.accessibleGridsFor(requesterUUID),
         * )
         */
        if (self().isRemote()) return;
        // 全量
        this.getWirelessMachineRunTime0().getGridCache().setAndSyncToClient(WirelessSavedData.Companion.getINSTANCE().getGridPool());
        // 可访问（服务端统一裁剪）
        this.getWirelessMachineRunTime0().getGridAccessibleCache().setAndSyncToClient(
                WirelessSavedData.Companion.accessibleGridsFor(getRequesterUUID()));
    }

    default WirelessMachineRunTime createWirelessMachineRunTime() {
        // Kotlin: fun createWirelessMachineRunTime() = WirelessMachineRunTime(this)
        return new WirelessMachineRunTime(this);
    }

    default WirelessMachinePersisted createWirelessMachinePersisted() {
        // Kotlin: fun createWirelessMachinePersisted() = WirelessMachinePersisted(this)
        return new WirelessMachinePersisted(this);
    }

    /**
     * 连接网格，例如机器加载
     * 
     * @param gridName 网格名称
     */
    default void linkGrid(String gridName) {
        /*
         * if (!allowThisMachineConnectToWirelessGrid()) return
         * if (self().isRemote) return
         * when (WirelessSavedData.Companion.joinToGrid(gridName, this, requesterUUID)) {
         * STATUS.SUCCESS, STATUS.ALREADY_JOINT -> {
         * wirelessMachineRunTime.gridConnected = WirelessSavedData.Companion.findGridByName(gridName)
         * }
         * STATUS.NOT_FOUND_GRID -> {
         * wirelessMachineRunTime.gridConnected = null
         * wirelessMachinePersisted.gridConnectedName = ""
         * }
         * STATUS.NOT_PERMISSION -> {
         * wirelessMachineRunTime.gridConnected = null
         * }
         * }
         */
        if (!allowThisMachineConnectToWirelessGrid()) return;
        if (self().isRemote()) return;
        STATUS status = WirelessSavedData.Companion.joinToGrid(gridName, this, getRequesterUUID());
        switch (status) {
            case SUCCESS, ALREADY_JOINT -> {
                this.getWirelessMachineRunTime0().setGridConnected(WirelessSavedData.Companion.findGridByName(gridName));
            }
            case NOT_FOUND_GRID -> {
                this.getWirelessMachineRunTime0().setGridConnected(null);
                this.getWirelessMachinePersisted0().setGridConnectedName("");
            }
            case NOT_PERMISSION -> {
                this.getWirelessMachineRunTime0().setGridConnected(null);
            }
        }
    }

    default void joinGrid(String gridName) {
        /*
         * if (!allowThisMachineConnectToWirelessGrid()) return
         * if (self().isRemote) return
         * wirelessMachinePersisted.gridConnectedName = gridName
         * linkGrid(gridName)
         * // 连接后立刻同步到客户端
         * refreshCachesOnServer()
         */
        if (!allowThisMachineConnectToWirelessGrid()) return;
        if (self().isRemote()) return;
        this.getWirelessMachinePersisted0().setGridConnectedName(gridName);
        linkGrid(gridName);
        // 连接后立刻同步到客户端
        refreshCachesOnServer();
    }

    default void unLinkGrid() {
        /*
         * if (self().isRemote) return
         * WirelessSavedData.Companion.leaveGrid(this)
         */
        if (self().isRemote()) return;
        WirelessSavedData.Companion.leaveGrid(this);
    }

    default void leaveGrid() {
        /*
         * if (self().isRemote) return
         * unLinkGrid()
         * wirelessMachinePersisted.gridConnectedName = ""
         * // 退出后立刻同步
         * refreshCachesOnServer()
         */
        if (self().isRemote()) return;
        unLinkGrid();
        this.getWirelessMachinePersisted0().setGridConnectedName("");
        // 退出后立刻同步
        refreshCachesOnServer();
    }

    default IFancyUIProvider getSetupFancyUIProvider() {
        // The original Kotlin implementation uses a Kotlin GUI DSL (rootFresh/hBox/vBox/...) with lambda receivers.
        // Automatic and correct translation to Java requires precise Kotlin-generated types for those DSL receivers.
        // Preserve the original Kotlin body here for manual conversion and return a provider that throws at runtime.
        return WirelessMachineUIKt.getSetupFancyUIProvider(this);
    }

    @Override
    MetaMachine self();
}

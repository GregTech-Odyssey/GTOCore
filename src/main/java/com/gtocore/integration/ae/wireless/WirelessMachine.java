package com.gtocore.integration.ae.wireless;

import com.gtocore.common.saved.NetworkSummary;
import com.gtocore.common.saved.STATUS;
import com.gtocore.common.saved.TopologySummary;
import com.gtocore.common.saved.WirelessNetworkSavedData;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.capability.ISync;

import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.gregtechceu.gtceu.utils.TaskHandler;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import com.hepdd.gtmthings.api.capability.IBindable;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;

import java.util.List;
import java.util.UUID;

/**
 * 重写后的无线网络节点接口。
 * <p>
 * 每个网络有源节点（Source，提供AE网络）和子节点（Child，消费AE网络）。
 * 子节点连接到一个源节点，源节点最多供应 maxOutputsPerInput 个子节点。
 * 源节点掉线时自动重分配子节点到其余源节点。
 */
@DataGeneratorScanned
public interface WirelessMachine extends IGridConnectedMachine, ISync, IBindable {

    ReferenceSet<MachineDefinition> WIRELESS_MACHINE_DEFINITIONS = new ReferenceOpenHashSet<>();

    enum NodeType {
        SOURCE,
        CHILD
    }

    // ==================== Language Keys ====================

    @RegisterLanguage(cn = "网络节点选择", en = "Network Node Selector")
    String KEY_NODE_SELECTOR = "gtocore.wireless.node_selector";

    @RegisterLanguage(cn = "绑定玩家: %s", en = "Player: %s")
    String KEY_PLAYER = "gtocore.wireless.player";

    @RegisterLanguage(cn = "已连接: %s", en = "Connected: %s")
    String KEY_CONNECTED = "gtocore.wireless.connected";

    @RegisterLanguage(cn = "无", en = "None")
    String KEY_NONE = "gtocore.wireless.none";

    @RegisterLanguage(cn = "新建", en = "New")
    String KEY_CREATE = "gtocore.wireless.create";

    @RegisterLanguage(cn = "断开连接", en = "Disconnect")
    String KEY_LEAVE = "gtocore.wireless.leave";

    @RegisterLanguage(cn = "删除", en = "Del")
    String KEY_REMOVE = "gtocore.wireless.remove";

    @RegisterLanguage(cn = "确认删除?", en = "Confirm?")
    String KEY_CONFIRM_DELETE = "gtocore.wireless.confirm_delete";

    @RegisterLanguage(cn = "节点: %s", en = "Node: %s")
    String KEY_NODE_TYPE = "gtocore.wireless.node_type";

    @RegisterLanguage(cn = "源节点", en = "Source")
    String KEY_SOURCE_NODE = "gtocore.wireless.source_node";

    @RegisterLanguage(cn = "子节点", en = "Child")
    String KEY_CHILD_NODE = "gtocore.wireless.child_node";

    @RegisterLanguage(cn = "源节点 (%d/%d 已连接)", en = "Source (%d/%d connected)")
    String KEY_INPUT_STATUS = "gtocore.wireless.input_status";

    @RegisterLanguage(cn = "子节点 (已连接)", en = "Child (connected)")
    String KEY_OUTPUT_CONNECTED = "gtocore.wireless.output_connected";

    @RegisterLanguage(cn = "子节点 (未分配！)", en = "Child (unassigned!)")
    String KEY_OUTPUT_UNASSIGNED = "gtocore.wireless.output_unassigned";

    @RegisterLanguage(cn = "⚠ %d 个子节点未分配", en = "⚠ %d child nodes unassigned")
    String KEY_UNASSIGNED_WARNING = "gtocore.wireless.unassigned_warning";

    @RegisterLanguage(cn = "此机器禁止连接无线网络", en = "This machine cannot connect to wireless networks")
    String KEY_BANNED = "gtocore.wireless.banned";

    @RegisterLanguage(cn = "可用网络: %d", en = "Networks: %d")
    String KEY_AVAILABLE = "gtocore.wireless.available";

    @RegisterLanguage(cn = "切换节点类型", en = "Toggle Node Type")
    String KEY_TOGGLE_TYPE = "gtocore.wireless.toggle_type";

    @RegisterLanguage(cn = "网络拓扑", en = "Network Topology")
    String KEY_TOPOLOGY = "gtocore.wireless.topology";

    @RegisterLanguage(cn = "选择网络查看拓扑", en = "Select a network to view topology")
    String KEY_TOPOLOGY_HINT = "gtocore.wireless.topology_hint";

    @RegisterLanguage(cn = "未分配", en = "Unassigned")
    String KEY_UNASSIGNED = "gtocore.wireless.unassigned";

    @RegisterLanguage(cn = "重命名", en = "Rename")
    String KEY_RENAME = "gtocore.wireless.rename";

    @RegisterLanguage(cn = "每个源节点最大载荷: %d", en = "Max Load Per Source: %d")
    String KEY_MAX_CONNECTIONS = "gtocore.wireless.max_connections";

    @RegisterLanguage(cn = "设置", en = "Set")
    String KEY_SET = "gtocore.wireless.set";

    // ==================== Node Type ====================

    /** SOURCE (源节点) 提供 AE 网络，CHILD (子节点) 使用 AE 网络。 */
    NodeType getNodeType();

    /** 是否支持在 GUI 中切换节点类型。 */
    default boolean supportsNodeTypeSwitching() {
        return false;
    }

    /** 切换节点类型。如果当前已连接网络，先离开再重新以新类型加入。 */
    default void setNodeType(NodeType type) {}

    // ==================== Persisted State ====================

    String getConnectedNetworkId();

    void setConnectedNetworkId(String id);

    // ==================== Sync Fields ====================

    ISync.ObjectSyncedField<List<NetworkSummary>> getNetworkListCache();

    ISync.IntSyncedField getUnassignedOutputCount();

    ISync.ObjectSyncedField<List<TopologySummary>> getTopologyCache();

    ISync.IntSyncedField getNodeTypeSync();

    // ==================== UUID ====================

    default UUID getRequesterUUID() {
        UUID owner = self().getOwnerUUID();
        return owner != null ? owner : getUUID();
    }

    @Override
    default UUID getUUID() {
        UUID owner = self().getOwnerUUID();
        return owner != null ? owner : UUID.randomUUID();
    }

    // ==================== Callbacks ====================

    default void addedToNetwork(String networkId) {}

    default void removedFromNetwork(String networkId) {}

    default boolean allowWirelessConnection() {
        return true;
    }

    // ==================== Lifecycle ====================

    default void onWirelessLoad() {
        if (self().isRemote()) return;
        TaskHandler.enqueueTick(getLevel(), () -> {
            if (getMainNode().getNode() != null) {
                String id = getConnectedNetworkId();
                if (!id.isEmpty()) {
                    linkNetwork(id);
                    if (self().getLevel() != null) {
                        WirelessNetworkSavedData.requireWriteToAll();
                    }
                }
            }
        }, 20, 40);
    }

    default void onWirelessUnload() {
        if (self().isRemote()) return;
        unlinkNetwork();
        if (self().getLevel() != null) {
            WirelessNetworkSavedData.requireWriteToAll();
        }
    }

    default void onWirelessPlaced(LivingEntity player, ItemStack stack) {
        if (player != null) {
            self().setOwnerUUID(player.getUUID());
            // Shift-place: auto-connect to the player's starred (default) network
            if (player instanceof Player p && p.isShiftKeyDown()) {
                UUID requester = getRequesterUUID();
                String defaultId = WirelessNetworkSavedData.Companion.getDefaultNetworkId(requester);
                if (defaultId != null && !defaultId.isEmpty()) {
                    WirelessNetwork net = WirelessNetworkSavedData.Companion.findNetworkById(defaultId);
                    if (net != null && WirelessNetworkSavedData.Companion.checkPermission(net.getOwner(), requester)) {
                        setConnectedNetworkId(defaultId);
                    }
                }
                WirelessNetworkSavedData.write(player.level());
            }
        }
        self().requestSync();
    }

    // ==================== Network Operations ====================

    /** 加入网络并持久化。 */
    default void joinNetwork(String networkId) {
        if (!allowWirelessConnection()) return;
        if (self().isRemote()) return;
        setConnectedNetworkId(networkId);
        linkNetwork(networkId);
        refreshNetworkListOnServer();
    }

    /** 内部连接（加载/恢复时调用，不变更持久化状态）。 */
    default void linkNetwork(String networkId) {
        if (!allowWirelessConnection()) return;
        if (self().isRemote()) return;
        STATUS status = WirelessNetworkSavedData.Companion.joinNetwork(networkId, this, getRequesterUUID());
        switch (status) {
            case SUCCESS, ALREADY_JOINT -> {}
            case NOT_FOUND_GRID -> setConnectedNetworkId("");
            case NOT_PERMISSION -> {}
        }
    }

    /** 断开连接（不清除持久化状态）。 */
    default void unlinkNetwork() {
        if (self().isRemote()) return;
        WirelessNetworkSavedData.Companion.leaveNetwork(this);
    }

    /** 断开连接并清除持久化。 */
    default void leaveNetwork() {
        if (self().isRemote()) return;
        unlinkNetwork();
        setConnectedNetworkId("");
        refreshNetworkListOnServer();
    }

    /**
     * 切换节点类型并保持连接。
     * 如果当前已连接网络，先离开旧角色再以新角色重新加入同一网络。
     */
    default void switchNodeType(NodeType newType) {
        if (self().isRemote()) return;
        if (getNodeType() == newType) return;
        String currentNetwork = getConnectedNetworkId();
        if (!currentNetwork.isEmpty()) {
            unlinkNetwork();
        }
        setNodeType(newType);
        if (!currentNetwork.isEmpty()) {
            setConnectedNetworkId(currentNetwork);
            linkNetwork(currentNetwork);
        }
        refreshNetworkListOnServer();
    }

    /** 同步网络列表、拓扑和未分配数到客户端。仅服务端调用。 */
    default void refreshNetworkListOnServer() {
        if (self().isRemote()) return;
        String connId = getConnectedNetworkId();
        // Auto-clear stale connection (network deleted by others)
        WirelessNetwork net = WirelessNetworkSavedData.Companion.findNetworkById(connId);
        if (net == null && !connId.isEmpty()) {
            setConnectedNetworkId("");
            connId = "";
        }
        String syncConnId = connId;
        getNetworkListCache().setAndSyncToClient(
                WirelessNetworkSavedData.Companion.getNetworkSummaries(getRequesterUUID(), syncConnId));
        getUnassignedOutputCount().setAndSyncToClient(net != null ? net.getUnassignedOutputCount() : 0);
        // Only sync topology for connected network
        if (syncConnId.isEmpty()) {
            getTopologyCache().setAndSyncToClient(java.util.List.of());
        } else {
            getTopologyCache().setAndSyncToClient(
                    WirelessNetworkSavedData.Companion.getTopologySummaries(getRequesterUUID())
                            .stream().filter(t -> t.getNetworkId().equals(syncConnId)).toList());
        }
        getNodeTypeSync().setAndSyncToClient(getNodeType().ordinal());
        WirelessNetworkSavedData.write(self().getLevel());
    }

    // ==================== GUI ====================

    default IFancyUIProvider getWirelessUIProvider() {
        return WirelessNodeUIKt.createWirelessUIProvider(this);
    }

    default IFancyUIProvider getWirelessTopologyProvider() {
        return WirelessNodeUIKt.createTopologyUIProvider(this);
    }

    @Override
    MetaMachine self();
}

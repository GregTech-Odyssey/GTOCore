package com.gtocore.integration.ae

import com.gtocore.api.gui.ktflexible.misc.InitFancyMachineUIWidget
import com.gtocore.common.saved.NetworkSummary
import com.gtocore.common.saved.TopologySummary
import com.gtocore.common.saved.WirelessNetworkSavedData
import com.gtocore.common.saved.createNetworkSummarySyncField
import com.gtocore.common.saved.createTopologySyncField
import com.gtocore.integration.ae.wireless.WirelessMachine

import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

import appeng.api.networking.GridFlags
import appeng.api.networking.IManagedGridNode
import appeng.api.util.AECableType
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget
import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder
import com.gtolib.api.capability.ISync
import com.gtolib.api.network.SyncManagedFieldHolder
import com.lowdragmc.lowdraglib.gui.modular.ModularUI
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted

/**
 * ME 无线连接机 — 可切换 INPUT/OUTPUT 节点类型。
 * 放置在 AE 线缆旁，作为 INPUT 节点为无线网络中的 OUTPUT 节点提供 AE 网络访问；
 * 或作为 OUTPUT 节点（叶子节点）从远程 INPUT 节点获取 AE 网络。
 */
class MeWirelessConnectMachine(holder: MetaMachineBlockEntity) :
    MetaMachine(holder),
    WirelessMachine,
    IMachineLife,
    ISync,
    IFancyUIMachine {

    companion object {
        @JvmStatic
        val syncManager = SyncManagedFieldHolder(MeWirelessConnectMachine::class.java)
    }

    // ==================== AE2 Grid ====================
    val gridHolder = GridNodeHolder(this)

    @DescSynced
    var isGridOnline: Boolean = false

    override fun isOnline(): Boolean = isGridOnline
    override fun setOnline(p0: Boolean) {
        isGridOnline = p0
    }
    override fun getCableConnectionType(dir: Direction): AECableType = AECableType.DENSE_SMART
    override fun getMainNode(): IManagedGridNode? = gridHolder.mainNode

    init {
        this.getMainNode()?.setFlags(GridFlags.DENSE_CAPACITY)
    }

    // ==================== WirelessMachine - Node Type (switchable) ====================
    @Persisted
    @DescSynced
    private var _nodeType: Int = WirelessMachine.NodeType.CHILD.ordinal

    override fun getNodeType(): WirelessMachine.NodeType = WirelessMachine.NodeType.entries.getOrElse(_nodeType) { WirelessMachine.NodeType.SOURCE }

    override fun supportsNodeTypeSwitching(): Boolean = true

    override fun setNodeType(type: WirelessMachine.NodeType) {
        _nodeType = type.ordinal
    }

    // ==================== WirelessMachine - Persisted State ====================
    @Persisted
    @DescSynced
    private var _connectedNetworkId: String = ""

    override fun getConnectedNetworkId(): String = _connectedNetworkId
    override fun setConnectedNetworkId(id: String) {
        _connectedNetworkId = id
    }

    // ==================== WirelessMachine - Sync Fields ====================
    private val _networkListCache: ISync.ObjectSyncedField<List<NetworkSummary>> = createNetworkSummarySyncField(this)
    private val _unassignedOutputCount: ISync.IntSyncedField = ISync.createIntField(this)
    private val _topologyCache: ISync.ObjectSyncedField<List<TopologySummary>> = createTopologySyncField(this)
    private val _nodeTypeSync: ISync.IntSyncedField = ISync.createIntField(this)

    override fun getNetworkListCache(): ISync.ObjectSyncedField<List<NetworkSummary>> = _networkListCache
    override fun getUnassignedOutputCount(): ISync.IntSyncedField = _unassignedOutputCount
    override fun getTopologyCache(): ISync.ObjectSyncedField<List<TopologySummary>> = _topologyCache
    override fun getNodeTypeSync(): ISync.IntSyncedField = _nodeTypeSync

    // ==================== ISync ====================
    override fun getSyncHolder(): SyncManagedFieldHolder = syncManager
    override fun isRemote() = super<MetaMachine>.isRemote

    // ==================== Data Migration ====================
    // TODO: 数据迁移 — 后续版本删除此方法。
    // 旧版本使用 @Persisted var wirelessMachinePersisted (WirelessMachinePersisted) 存储连接信息，
    // NBT key "wirelessMachinePersisted" → {gridName: "...", beSet: true/false}
    // 新版本使用 @Persisted var _connectedNetworkId (String) 和 @Persisted var _nodeType (Int)。
    // 此方法将旧格式的 gridName 读取并写入 _connectedNetworkId，确保已放置的机器不丢失连接。
    override fun loadCustomPersistedData(tag: CompoundTag) {
        super.loadCustomPersistedData(tag)
        if (_connectedNetworkId.isEmpty() && tag.contains("wirelessMachinePersisted")) {
            val oldData = tag.getCompound("wirelessMachinePersisted")
            val oldGridName = oldData.getString("gridName")
            if (oldGridName.isNotEmpty()) {
                _connectedNetworkId = oldGridName
            }
        }
    }

    // ==================== Lifecycle ====================
    override fun onLoad() {
        super.onLoad()
        registerSync()
        onWirelessLoad()
    }

    override fun onMachinePlaced(player: LivingEntity?, stack: ItemStack?) {
        super.onMachinePlaced(player, stack)
        onWirelessPlaced(player, stack)
    }

    override fun onUnload() {
        onWirelessUnload()
        unregisterSync()
        super.onUnload()
    }

    // ==================== GUI ====================
    private val fancyUIProvider: IFancyUIProvider by lazy { wirelessUIProvider }
    private val topologyProvider: IFancyUIProvider by lazy { wirelessTopologyProvider }

    override fun createUI(entityPlayer: Player): ModularUI = ModularUI(176, 166, this, entityPlayer)
        .widget(
            InitFancyMachineUIWidget(this, 176, 166) {
                if (!isRemote) refreshNetworkListOnServer()
            },
        )

    override fun attachSideTabs(sideTabs: TabsWidget) {
        sideTabs.mainTab = this
        sideTabs.attachSubTab(topologyProvider)
    }

    override fun getTabIcon(): IGuiTexture = fancyUIProvider.tabIcon
    override fun getTabTooltips(): MutableList<Component?> = fancyUIProvider.tabTooltips
    override fun createMainPage(p0: FancyMachineUIWidget?): Widget = fancyUIProvider.createMainPage(p0)
}

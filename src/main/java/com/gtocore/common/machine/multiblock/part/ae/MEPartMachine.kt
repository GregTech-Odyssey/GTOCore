package com.gtocore.common.machine.multiblock.part.ae

import com.gtocore.api.gui.ktflexible.misc.InitFancyMachineUIWidget
import com.gtocore.common.saved.NetworkSummary
import com.gtocore.common.saved.TopologySummary
import com.gtocore.common.saved.createNetworkSummarySyncField
import com.gtocore.common.saved.createTopologySyncField
import com.gtocore.integration.ae.wireless.WirelessMachine

import net.minecraft.MethodsReturnNonnullByDefault
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.items.IItemHandlerModifiable

import appeng.api.networking.IManagedGridNode
import appeng.api.networking.security.IActionSource
import com.gregtechceu.gtceu.api.GTValues
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity
import com.gregtechceu.gtceu.api.capability.recipe.IO
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget
import com.gregtechceu.gtceu.api.item.tool.GTToolType
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDistinctPart
import com.gregtechceu.gtceu.api.machine.multiblock.part.WorkableTieredIOPartMachine
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder
import com.gtolib.api.capability.ISync
import com.gtolib.api.machine.feature.IMEPartMachine
import com.gtolib.api.network.SyncManagedFieldHolder
import com.lowdragmc.lowdraglib.gui.modular.ModularUI
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted
import com.mojang.datafixers.util.Pair

import java.util.*
import javax.annotation.ParametersAreNonnullByDefault

/**
 * ME 部件机器基类 — OUTPUT 节点。
 * 用于多方块结构中的 ME 总线/样板供应器等，
 * 通过无线网络连接到 INPUT 节点获取 AE 网络访问。
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
internal abstract class MEPartMachine(holder: MetaMachineBlockEntity, io: IO) :
    WorkableTieredIOPartMachine(holder, GTValues.LuV, io),
    WirelessMachine,
    IMEPartMachine,
    ISync,
    IDistinctPart,
    IMachineLife {

    // ==================== AE2 Grid ====================
    @Persisted
    private val nodeHolder: GridNodeHolder = GridNodeHolder(this)

    @DescSynced
    var onlineField: Boolean = false

    val actionSourceField: IActionSource = IActionSource.ofMachine { nodeHolder.getMainNode().node }

    @Persisted
    protected var distinctField: Boolean = false

    @Persisted
    var isAllFacing: Boolean = false

    override fun getItemHandlerCap(side: Direction?, useCoverCapability: Boolean): IItemHandlerModifiable? = null
    override fun getFluidHandlerCap(side: Direction?, useCoverCapability: Boolean): IFluidHandlerModifiable? = null

    override fun onToolClick(toolType: MutableSet<GTToolType>, itemStack: ItemStack, context: UseOnContext): Pair<GTToolType?, InteractionResult?> {
        val result = super.onToolClick(toolType, itemStack, context)
        if (result.second == InteractionResult.PASS && toolType.contains(GTToolType.WIRE_CUTTER)) {
            val player = context.player ?: return result
            return Pair.of<GTToolType?, InteractionResult?>(GTToolType.WIRE_CUTTER, onWireCutterClick(player, context.hand))
        }
        return result
    }

    override fun shouldRenderGrid(player: Player, pos: BlockPos, state: BlockState, held: ItemStack, toolTypes: MutableSet<GTToolType?>): Boolean = super.shouldRenderGrid(player, pos, state, held, toolTypes) || toolTypes.contains(GTToolType.WIRE_CUTTER)

    private fun onWireCutterClick(playerIn: Player, hand: InteractionHand): InteractionResult {
        playerIn.swing(hand)
        if (isAllFacing) {
            getMainNode().setExposedOnSides(EnumSet.of(this.getFrontFacing()))
            if (isRemote) {
                playerIn.displayClientMessage(Component.translatable("gtocore.me_front"), true)
            }
            isAllFacing = false
        } else {
            getMainNode().setExposedOnSides(EnumSet.allOf(Direction::class.java))
            if (isRemote) {
                playerIn.displayClientMessage(Component.translatable("gtocore.me_any"), true)
            }
            isAllFacing = true
        }
        return InteractionResult.CONSUME
    }

    // ==================== WirelessMachine - Node Type ====================
    override fun getNodeType(): WirelessMachine.NodeType = WirelessMachine.NodeType.CHILD

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
    companion object {
        val syncFieldHolder = SyncManagedFieldHolder(MEPartMachine::class.java)
        const val CONFIG_SIZE: Int = 16
    }

    override fun getSyncHolder(): SyncManagedFieldHolder = syncFieldHolder

    // ==================== Data Migration ====================
    // TODO: 数据迁移 — 后续版本删除此方法。
    // 旧版本使用 @Persisted var wirelessMachinePersisted (WirelessMachinePersisted) 存储连接信息，
    // NBT key "wirelessMachinePersisted" → {gridName: "...", beSet: true/false}
    // 新版本使用 @Persisted var _connectedNetworkId (String)。
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
        if (isAllFacing) {
            mainNode.setExposedOnSides(EnumSet.allOf(Direction::class.java))
        }
        if (isRemote) return
        onWirelessLoad()
        getHandlerList().isDistinct = distinctField
        getHandlerList().color = paintingColor
    }

    override fun getMainNode(): IManagedGridNode = nodeHolder.getMainNode()

    override fun onPaintingColorChanged(color: Int) {
        handlerList.setColor(color, true)
    }

    override fun isDistinct(): Boolean = distinctField
    override fun setDistinct(isDistinct: Boolean) {
        this.distinctField = isDistinct
        handlerList.setDistinctAndNotify(isDistinct)
    }

    override fun setOnline(isOnline: Boolean) {
        this.onlineField = isOnline
    }
    override fun isOnline(): Boolean = this.onlineField
    override fun getActionSource(): IActionSource = this.actionSourceField

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
    override fun createUI(entityPlayer: Player?): ModularUI? = ModularUI(176, 166, this, entityPlayer)
        .widget(
            InitFancyMachineUIWidget(this, 176, 166) {
                if (!isRemote) refreshNetworkListOnServer()
            },
        )

    override fun attachSideTabs(sideTabs: TabsWidget) {
        super<WorkableTieredIOPartMachine>.attachSideTabs(sideTabs)
        sideTabs.attachSubTab(wirelessUIProvider)
    }
}

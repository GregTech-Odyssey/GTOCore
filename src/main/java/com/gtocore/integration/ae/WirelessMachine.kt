package com.gtocore.integration.ae

import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.api.lang.ComponentSupplier
import com.gtocore.client.forge.GTORenderData
import com.gtocore.client.forge.GTORenderManager
import com.gtocore.client.forge.GTORenderType
import com.gtocore.common.data.GTOMachines.ME_WIRELESS_CONNECTION_MACHINE
import com.gtocore.common.data.translation.EnumTranslation.both
import com.gtocore.common.data.translation.EnumTranslation.other
import com.gtocore.common.data.translation.EnumTranslation.patternHatch
import com.gtocore.common.data.translation.EnumTranslation.wirelessConnectionMachine
import com.gtocore.common.saved.STATUS
import com.gtocore.common.saved.WirelessGrid
import com.gtocore.common.saved.WirelessSavedData
import com.gtocore.common.saved.createWirelessSyncedField
import com.gtocore.config.GTOConfig
import com.gtocore.utils.toTicks

import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fml.LogicalSide

import appeng.core.definitions.AEItems
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.gregtechceu.gtceu.api.machine.TickableSubscription
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine
import com.gregtechceu.gtceu.utils.GTUtil
import com.gregtechceu.gtceu.utils.TaskHandler
import com.gtolib.api.annotation.DataGeneratorScanned
import com.gtolib.api.annotation.language.RegisterLanguage
import com.gtolib.api.capability.ISync
import com.gtolib.api.capability.ISync.createEnumField
import com.gtolib.api.gui.ktflexible.blank
import com.gtolib.api.gui.ktflexible.button
import com.gtolib.api.gui.ktflexible.field
import com.gtolib.api.gui.ktflexible.rootFresh
import com.gtolib.api.player.IEnhancedPlayer
import com.hepdd.gtmthings.api.capability.IBindable
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable

import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

enum class FilterInMachineType(val component: ComponentSupplier) {
    BOTH(both),
    PATTERN_HATCH(patternHatch),
    WIRELESS_CONNECT_MACHINE(wirelessConnectionMachine),
    OTHER(other),
}
class WirelessMachineRunTime(var machine: WirelessMachine) {
    var gridWillAdded: String = ""
    var gridConnected: WirelessGrid? = null // ONLY SERVER
    var connectPageFreshRun: Runnable = Runnable {}
    var detailsPageFreshRun: Runnable = Runnable {}
    var initTickableSubscription: TickableSubscription? = null
    var shouldAutoConnect = false
    var gridCache = createWirelessSyncedField(machine).set(mutableListOf())
    var gridAccessibleCache = createWirelessSyncedField(machine).set(mutableListOf())

    // 新增：编辑网络昵称的输入缓存
    var gridNicknameEdit: String = ""

    // 防止 UI 侧刷新循环：仅在客户端接收端刷新 UI，服务端不触发 fresh()
    var FilterInMachineTypeSyncField: ISync.EnumSyncedField<FilterInMachineType> = createEnumField(machine)
    init {
        // 客户端接收端刷新函数
        fun <T> clientRefresh(): (LogicalSide, T, T) -> Unit = { f, _, _ ->
            if (!f.isServer) {
                connectPageFreshRun.run()
                detailsPageFreshRun.run()
            }
        }

        // 服务端发送端不刷新，避免服务端 fresh 再次触发业务逻辑造成循环
        fun <T> serverNoop(): (LogicalSide, T, T) -> Unit = { _, _, _ -> }

        gridCache.setReceiverListener(clientRefresh())
        gridCache.setSenderListener(serverNoop())
        gridAccessibleCache.setReceiverListener(clientRefresh())
        gridAccessibleCache.setSenderListener(serverNoop())
        FilterInMachineTypeSyncField.setReceiverListener(clientRefresh())
        FilterInMachineTypeSyncField.setSenderListener(serverNoop())
        FilterInMachineTypeSyncField.set(FilterInMachineType.BOTH)
    }
}
class WirelessMachinePersisted(var machine: WirelessMachine) :
    ITagSerializable<CompoundTag>,
    IContentChangeAware {
    var onContentChanged: Runnable? = null
    var gridConnectedName: String = ""
        set(value) {
            if (field != value) {
                field = value
                onContentChanged?.run()
                machine.self().requestSync()
            }
        }
    var beSet = false // 是否已经设置过网格连接
        set(value) {
            if (field != value) {
                field = value
                onContentChanged?.run()
                machine.self().requestSync()
            }
        }
    override fun serializeNBT(): CompoundTag = CompoundTag().apply {
        putString("gridName", gridConnectedName)
        putBoolean("beSet", beSet)
    }

    override fun deserializeNBT(nbt: CompoundTag?) {
        nbt?.let {
            gridConnectedName = it.getString("gridName")
            beSet = it.getBoolean("beSet")
        }
    }

    override fun setOnContentsChanged(aa: Runnable?) {
        this.onContentChanged = aa
    }

    override fun getOnContentsChanged(): Runnable? = onContentChanged
}

@DataGeneratorScanned
interface WirelessMachine :
    IGridConnectedMachine,
    ISync,
    IBindable {
    val requesterUUID: UUID
        get() = self().ownerUUID ?: uuid

    @DataGeneratorScanned
    companion object {
        @RegisterLanguage(cn = "网络节点选择", en = "Grid Node Selector")
        const val gridNodeSelector = "gtocore.integration.ae.WirelessMachine.gridNodeSelector"

        @RegisterLanguage(cn = "网络节点列表", en = "Grid Node List")
        const val gridNodeList = "gtocore.integration.ae.WirelessMachine.gridNodeList"

        @RegisterLanguage(cn = "绑定到玩家 : %s", en = "Bind to player: %s")
        const val player = "gtocore.integration.ae.WirelessMachine.player"

        @RegisterLanguage(cn = "当前连接到 : %s", en = "Currently connected: %s")
        const val currentlyConnectedTo = "gtocore.integration.ae.WirelessMachine.currentlyConnectedTo"

        @RegisterLanguage(cn = "创建网络", en = "Create Grid")
        const val createGrid = "gtocore.integration.ae.WirelessMachine.createGrid"

        @RegisterLanguage(cn = "全球可用无线网络 : %s / %s", en = "Global available wireless grids: %s / %s")
        const val globalWirelessGrid = "gtocore.integration.ae.WirelessMachine.globalWirelessGrid"

        @RegisterLanguage(cn = "删除", en = "Remove")
        const val removeGrid = "gtocore.integration.ae.WirelessMachine.removeGrid"

        @RegisterLanguage(cn = "你的无线网络 : ", en = "Your wireless grids: ")
        const val yourWirelessGrid = "gtocore.integration.ae.WirelessMachine.yourWirelessGrid"

        @RegisterLanguage(cn = "查找机器，机器将会高亮", en = "Find Machine, machine will be highlighted")
        const val findMachine = "gtocore.integration.ae.WirelessMachine.findMachine"

        @RegisterLanguage(cn = "此机器被禁止连接ME无线网络", en = "This machine is banned from connecting to ME wireless network")
        const val banned = "gtocore.integration.ae.WirelessMachine.banned"

        @RegisterLanguage(cn = "断开无线网络", en = "Leave Wireless Grid")
        const val leave = "gtocore.integration.ae.WirelessMachine.leave"

        @RegisterLanguage(cn = "修改网络昵称", en = "Rename Grid")
        const val renameGrid = "gtocore.integration.ae.WirelessMachine.renameGrid"
    }

    // ////////////////////////////////
    // ****** 机器必须实现此字段 ******//
    // //////////////////////////////
    var wirelessMachinePersisted: WirelessMachinePersisted // @Persisted + @DescSync
    var wirelessMachineRunTime: WirelessMachineRunTime // @SyncedManager

    // ////////////////////////////////
    // ****** WirelessSavedData的回调，可通过重写自定义，服务端方法 ******//
    // //////////////////////////////
    fun addedToGrid(gridName: String) {
    }
    fun removedFromGrid(gridName: String) {
    }
    fun allowThisMachineConnectToWirelessGrid() = true

    // ////////////////////////////////
    // ****** 机器必须在相应时机调用 ******//
    // //////////////////////////////
    fun onWirelessMachineLoad() {
        if (self().isRemote) return
        wirelessMachineRunTime.initTickableSubscription = TaskHandler.enqueueServerTick(level as ServerLevel, {
            if (mainNode.node != null && self().offsetTimer % 20 == 0L) {
                if (!wirelessMachinePersisted.beSet && wirelessMachineRunTime.shouldAutoConnect) {
                    // 使用按请求者计算的可访问列表，寻找“当前玩家”的默认网格
                    WirelessSavedData.accessibleGridsFor(self().ownerUUID ?: uuid)
                        .firstOrNull { it.isDefault }
                        ?.let { joinGrid(it.name) }
                    wirelessMachinePersisted.beSet = true
                } else {
                    if (wirelessMachinePersisted.gridConnectedName.isNotEmpty()) {
                        linkGrid(wirelessMachinePersisted.gridConnectedName)
                    }
                }
                // 机器加载完成后进行一次数据同步，避免 UI 打开时需要主动拉取
                syncDataToClientInServer()
                wirelessMachineRunTime.initTickableSubscription?.unsubscribe()
            }
        }, GTUtil.NOOP, 40)
    }
    fun onWirelessMachineUnLoad() {
        if (self().isRemote) return
        wirelessMachineRunTime.initTickableSubscription?.unsubscribe()
        unLinkGrid()
    }
    fun onWirelessMachineClientTick() {
    }
    fun onWirelessMachinePlaced(player: LivingEntity?, stack: ItemStack?) {
        player?.let {
            self().ownerUUID = it.uuid
            if (player is Player) {
                if (IEnhancedPlayer.of(player).playerData.shiftState) wirelessMachineRunTime.shouldAutoConnect = true
            }
        }
        self().requestSync()
    }

    override fun getUUID(): UUID = self().ownerUUID ?: UUID.randomUUID()

    // ////////////////////////////////
    // ****** 工具集 ******//
    // //////////////////////////////
    // 同步网络数据到客户端（包含全量网格与当前请求者可访问网格）
    fun refreshCachesOnServer() {
        if (self().isRemote) return
        // 全量
        wirelessMachineRunTime.gridCache.setAndSyncToClient(WirelessSavedData.INSTANCE.gridPool)
        // 可访问（服务端统一裁剪）
        wirelessMachineRunTime.gridAccessibleCache.setAndSyncToClient(
            WirelessSavedData.accessibleGridsFor(requesterUUID),
        )
    }

    // 兼容旧命名
    fun syncDataToClientInServer() = refreshCachesOnServer()

    fun createWirelessMachineRunTime() = WirelessMachineRunTime(this)
    fun createWirelessMachinePersisted() = WirelessMachinePersisted(this)

    fun linkGrid(gridName: String) { // 连接网格，例如机器加载
        if (!allowThisMachineConnectToWirelessGrid()) return
        if (self().isRemote) return
        when (WirelessSavedData.joinToGrid(gridName, this, requesterUUID)) {
            STATUS.SUCCESS, STATUS.ALREADY_JOINT -> {
                wirelessMachineRunTime.gridConnected = WirelessSavedData.findGridByName(gridName)
            }
            STATUS.NOT_FOUND_GRID -> {
                wirelessMachineRunTime.gridConnected = null
                wirelessMachinePersisted.gridConnectedName = ""
            }
            STATUS.NOT_PERMISSION -> {
                wirelessMachineRunTime.gridConnected = null
            }
        }
    }

    fun joinGrid(gridName: String) {
        if (!allowThisMachineConnectToWirelessGrid()) return
        if (self().isRemote) return
        wirelessMachinePersisted.gridConnectedName = gridName
        linkGrid(gridName)
        // 连接后立刻同步到客户端
        refreshCachesOnServer()
    }

    fun unLinkGrid() { // 机器下线但不退出
        if (self().isRemote) return
        WirelessSavedData.leaveGrid(this)
    }

    fun leaveGrid() { // 退出网格
        if (self().isRemote) return
        unLinkGrid()
        wirelessMachinePersisted.gridConnectedName = ""
        // 退出后立刻同步
        refreshCachesOnServer()
    }

    fun getSetupFancyUIProvider(): IFancyUIProvider = object : IFancyUIProvider {
        override fun getTabIcon(): IGuiTexture? = ItemStackTexture(AEItems.WIRELESS_RECEIVER.stack())

        override fun getTitle(): Component? = Component.translatable(gridNodeSelector)

        override fun createMainPage(p0: FancyMachineUIWidget?) = rootFresh(176, 166) {
            if (GTOConfig.INSTANCE.aeLog) println(1)
            // 移除页面打开即同步，避免触发刷新循环；改为由机器加载与按钮操作驱动同步
            hBox(height = availableHeight, { spacing = 4 }) {
                blank()
                vBox(width = this@rootFresh.availableWidth - 4, style = { spacing = 4 }) {
                    blank()
                    if (!allowThisMachineConnectToWirelessGrid()) {
                        textBlock(
                            maxWidth = availableWidth - 4,
                            textSupplier = { Component.translatable(banned) },
                        )
                        return@vBox
                    }
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = { Component.translatable(player, self().playerOwner?.name ?: "无") },
                    )
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = {
                            val id = wirelessMachinePersisted.gridConnectedName
                            val nick = wirelessMachineRunTime.gridCache.get().firstOrNull { it.name == id }?.nickname
                            Component.translatable(currentlyConnectedTo, (nick ?: id).ifEmpty { "无" })
                        },
                    )
                    // 重新加入“创建网络”输入与按钮
                    hBox(height = 16, { spacing = 4 }) {
                        field(
                            width = 60,
                            getter = { wirelessMachineRunTime.gridWillAdded },
                            setter = { wirelessMachineRunTime.gridWillAdded = it },
                        )
                        button(transKet = createGrid, width = this@vBox.availableWidth - 60 - 8) { ck ->
                            if (!ck.isRemote) {
                                val input = wirelessMachineRunTime.gridWillAdded.trim()
                                if (input.isNotEmpty() &&
                                    wirelessMachineRunTime.gridCache.get().none { it.nickname == input }
                                ) {
                                    WirelessSavedData.createNewGrid(
                                        input,
                                        requesterUUID,
                                    )
                                    wirelessMachineRunTime.gridWillAdded = ""
                                    refreshCachesOnServer()
                                }
                            }
                        }
                    }
                    // 基于服务端同步下来的 gridCache 推断连接状态，避免等待 Persisted 同步导致按钮延迟显示
                    val isConnectedClient = wirelessMachinePersisted.gridConnectedName.isNotEmpty() ||
                        wirelessMachineRunTime.gridCache.get().any { grid ->
                            grid.connectionPoolTable.any { it.pos == self().pos && it.level == self().holder.level().dimension() }
                        }
                    if (isConnectedClient) {
                        button(width = availableWidth - 4, transKet = leave) { ck ->
                            if (!ck.isRemote) {
                                leaveGrid()
                            }
                        }
                    }
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = {
                            Component.translatable(
                                globalWirelessGrid,
                                wirelessMachineRunTime.gridAccessibleCache.get().count(),
                                wirelessMachineRunTime.gridCache.get().count(),
                            )
                        },
                    )
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = { Component.translatable(yourWirelessGrid) },
                    )
                    val availableHeight = 166 - ((4 * 10) + (1 * 16) + (if (isConnectedClient) 16 else 0) + (4 * 7))
                    val finalListHeight = maxOf(0, (((availableHeight / 16)+1) * 16) - 2)
                    vScroll(width = availableWidth, height = finalListHeight, { spacing = 2 }) a@{
                        wirelessMachineRunTime.gridAccessibleCache.get()
                            .forEach { grid ->
                                hBox(height = 14, { spacing = 4 }) {
                                    button(
                                        height = 14,
                                        text = { "${if (grid.isDefault) "⭐" else ""}${grid.nickname}" },
                                        width = this@a.availableWidth - 48 - 8 + 12 - 4 - 18,
                                        onClick = {
                                            if (!it.isRemote) {
                                                leaveGrid()
                                                joinGrid(grid.name)
                                            }
                                        },
                                    )
                                    if (!grid.isDefault) {
                                        button(height = 14, text = { "⭐" }, width = 18, onClick = {
                                            if (!it.isRemote) {
                                                WirelessSavedData.setAsDefault(grid.name, requesterUUID)
                                                refreshCachesOnServer()
                                            }
                                        })
                                    } else {
                                        button(height = 14, text = { "⚝" }, width = 18, onClick = {
                                            if (!it.isRemote) {
                                                WirelessSavedData.cancelAsDefault(
                                                    grid.name,
                                                    requesterUUID,
                                                )
                                                refreshCachesOnServer()
                                            }
                                        })
                                    }
                                    button(height = 14, transKet = removeGrid, width = 36, onClick = {
                                        if (!it.isRemote) {
                                            WirelessSavedData.removeGrid(grid.name, requesterUUID)
                                            refreshCachesOnServer()
                                        }
                                    })
                                }
                            }
                    }
                }
            }
        }.also { wirelessMachineRunTime.connectPageFreshRun = Runnable { it.fresh() } }
    }
    fun getDetailFancyUIProvider(): IFancyUIProvider = object : IFancyUIProvider {
        override fun getTabIcon(): IGuiTexture? = ItemStackTexture(AEItems.WIRELESS_RECEIVER.stack())

        override fun getTitle(): Component? = Component.translatable(gridNodeList)

        override fun createMainPage(p0: FancyMachineUIWidget?): Widget? = rootFresh(256, 166) {
            if (GTOConfig.INSTANCE.aeLog) println(2)
            // 移除页面打开即同步，避免触发刷新循环；改为由机器加载与按钮操作驱动同步

            hBox(height = availableHeight, { spacing = 4 }) {
                blank()
                vBox(width = this@rootFresh.availableWidth - 4, { spacing = 4 }) {
                    blank()
                    if (!allowThisMachineConnectToWirelessGrid()) {
                        textBlock(
                            maxWidth = availableWidth - 4,
                            textSupplier = { Component.translatable(banned) },
                        )
                        return@vBox
                    }
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = {
                            val id = wirelessMachinePersisted.gridConnectedName
                            val nick = wirelessMachineRunTime.gridCache.get().firstOrNull { it.name == id }?.nickname
                            Component.translatable(currentlyConnectedTo, (nick ?: id).ifEmpty { "无" })
                        },
                    )
                    // 重命名区域（仅在已连接时显示）
                    if (wirelessMachinePersisted.gridConnectedName.isNotEmpty()) {
                        hBox(height = 16, { spacing = 4 }) {
                            field(
                                width = 120,
                                getter = {
                                    val id = wirelessMachinePersisted.gridConnectedName
                                    if (wirelessMachineRunTime.gridNicknameEdit.isEmpty()) {
                                        wirelessMachineRunTime.gridNicknameEdit = wirelessMachineRunTime.gridCache.get().firstOrNull { it.name == id }?.nickname ?: id
                                    }
                                    wirelessMachineRunTime.gridNicknameEdit
                                },
                                setter = { wirelessMachineRunTime.gridNicknameEdit = it },
                            )
                            button(transKet = renameGrid, width = 80) { ck ->
                                if (!ck.isRemote) {
                                    WirelessSavedData.setGridNickname(
                                        wirelessMachinePersisted.gridConnectedName,
                                        requesterUUID,
                                        wirelessMachineRunTime.gridNicknameEdit,
                                    )
                                    refreshCachesOnServer()
                                }
                            }
                        }
                    }
                    // filter 选择器
                    hScroll(width = availableWidth - 4, height = 20, style = { spacing = 4 }) {
                        val syncField = wirelessMachineRunTime.FilterInMachineTypeSyncField
                        syncField.get().javaClass.enumConstants.forEach { select: FilterInMachineType ->
                            button(
                                width = if (self().isRemote) Minecraft.getInstance().font.width(select.component.get().string) + 20 else 20,
                                height = 16,
                                text = { select.component.get().string },
                                onClick = { ck ->
                                    if (!ck.isRemote) syncField.setAndSyncToClient(select)
                                },
                            )
                        }
                    }
                    if (self().isRemote) {
                        vScroll(width = availableWidth, height = 152 - 4 - 10 - 16 - 20, { spacing = 2 }) {
                            val availableWidth1 = availableWidth
                            // filter 应用，数据展示。
                            val objects = wirelessMachineRunTime.gridCache.get().firstOrNull { it.name == wirelessMachinePersisted.gridConnectedName }?.connectionPoolTable
                            val machineTypeFilter: (WirelessGrid.MachineInfo) -> Boolean = { info: WirelessGrid.MachineInfo ->
                                when (wirelessMachineRunTime.FilterInMachineTypeSyncField.get()) {
                                    FilterInMachineType.BOTH -> true
                                    FilterInMachineType.PATTERN_HATCH -> info.descriptionId.contains("pattern")
                                    FilterInMachineType.WIRELESS_CONNECT_MACHINE -> info.descriptionId == ME_WIRELESS_CONNECTION_MACHINE.descriptionId
                                    FilterInMachineType.OTHER -> !info.descriptionId.contains("pattern") && info.descriptionId != ME_WIRELESS_CONNECTION_MACHINE.descriptionId
                                }
                            }
                            objects
                                ?.filter(machineTypeFilter)
                                ?.groupBy { it.level }
                                ?.forEach { level, line ->
                                    textBlock(maxWidth = availableWidth1, textSupplier = { Component.literal("- ${level.location().path} (${line.size})") })
                                    line.groupBy { it.owner }
                                        .forEach { owner, line1 ->
                                            textBlock(maxWidth = availableWidth1, tab = 10, textSupplier = { Component.literal("- $owner") })
                                            line1.sortedBy { it.descriptionId }
                                                .forEach {
                                                    textBlock(maxWidth = availableWidth1, tab = 20, textSupplier = { Component.literal("- ${Component.translatable(it.descriptionId).string} (${it.pos.x},${it.pos.y},${it.pos.z})") })
                                                    hBox(height = 99) {
                                                        blank(width = 20)
                                                        button(width = availableWidth1 - 20 - 4, height = 14, transKet = findMachine) { ck ->
                                                            if (ck.isRemote) {
                                                                GTORenderManager.tasks.push(
                                                                    GTORenderType.BLOCK_LINE(
                                                                        data = GTORenderData.BLOCK_LINE_DATA(
                                                                            pos = it.pos,
                                                                            level = self().holder.level().dimension(),
                                                                            durationTick = 1.minutes.toTicks(),
                                                                            flickerCycle = 1.seconds.toTicks(),
                                                                        ),
                                                                    ),
                                                                ) // 推送高亮数据
                                                            }
                                                        }
                                                    }
                                                }
                                        }
                                }
                        }
                    }
                }
            }
        }.also { wirelessMachineRunTime.detailsPageFreshRun = Runnable { it.fresh() } }
    }
}

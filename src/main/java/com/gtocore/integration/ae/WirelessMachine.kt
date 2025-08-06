package com.gtocore.integration.ae

import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.client.forge.GTORenderData
import com.gtocore.client.forge.GTORenderManager
import com.gtocore.client.forge.GTORenderType
import com.gtocore.common.data.GTOMachines.ME_WIRELESS_CONNECTION_MACHINE
import com.gtocore.common.data.translation.EnumTranslation.both
import com.gtocore.common.data.translation.EnumTranslation.other
import com.gtocore.common.data.translation.EnumTranslation.patternHatch
import com.gtocore.common.data.translation.EnumTranslation.wirelessConnectionMachine
import com.gtocore.common.network.EnumSyncField
import com.gtocore.common.network.UUIDSyncField
import com.gtocore.common.network.createLogicalSide
import com.gtocore.common.saved.STATUS
import com.gtocore.common.saved.WirelessGrid
import com.gtocore.common.saved.WirelessSavedData
import com.gtocore.common.saved.WirelessSyncField
import com.gtocore.config.GTOConfig
import com.gtocore.utils.ComponentSupplier
import com.gtocore.utils.toTicks

import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.TickTask
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

import appeng.core.definitions.AEItems
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine
import com.gtolib.api.annotation.Scanned
import com.gtolib.api.annotation.language.RegisterLanguage
import com.gtolib.api.gui.ktflexible.blank
import com.gtolib.api.gui.ktflexible.button
import com.gtolib.api.gui.ktflexible.field
import com.gtolib.api.gui.ktflexible.rootFresh
import com.hepdd.gtmthings.api.capability.IBindable
import com.hepdd.gtmthings.utils.TeamUtil
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable

import java.util.UUID
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
    var isInitialized = false

    var gridCache = WirelessSyncField(
        createLogicalSide(machine.self().isRemote),
        { "machine.getGridInfoInMachine.gridConnectedName-${machine.self().holder.currentPos}" },
        WirelessSavedData.INSTANCE.gridPool,
        onSyncCallBack = { f, old, new ->
            if (f.side.isServer) {
                machine.self().level?.server?.tell(
                    TickTask(10) {
                        connectPageFreshRun.run()
                        detailsPageFreshRun.run()
                    },
                )
            } else {
                connectPageFreshRun.run()
                detailsPageFreshRun.run()
            }
            if (GTOConfig.INSTANCE.aeLog)println("isRemote :${machine.self().isRemote} Fresh,pos:${machine.self().holder.currentPos}, old:$old, new:$new,oldSize:${old.size}, newSize:${new.size}")
        },
    )

    var teamUUID = UUIDSyncField(
        createLogicalSide(machine.self().isRemote),
        { "machine.getGridInfoInMachine.teamUUID-${machine.self().holder.currentPos}" },
        UUID.randomUUID(),
        onSyncCallBack = { f, old, new ->
            if (f.side.isServer) {
                machine.self().level?.server?.tell(
                    TickTask(10) {
                        connectPageFreshRun.run()
                        detailsPageFreshRun.run()
                    },
                )
            } else {
                connectPageFreshRun.run()
                detailsPageFreshRun.run()
            }
        },
    )

    var FilterInMachineTypeSyncField = EnumSyncField(
        createLogicalSide(machine.self().isRemote),
        { "machine.getGridInfoInMachine.FilterInMachineTypeSyncField-${machine.self().holder.currentPos}" },
        FilterInMachineType.BOTH,
        onSyncCallBack = { f, old, new ->
            if (f.side.isServer) {
                machine.self().level?.server?.tell(
                    TickTask(10) {
                        connectPageFreshRun.run()
                        detailsPageFreshRun.run()
                    },
                )
            } else {
                connectPageFreshRun.run()
                detailsPageFreshRun.run()
            }
        },
        enumClass = FilterInMachineType::class.java,
    )
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
        putUUID("teamUUID", machine.wirelessMachineRunTime.teamUUID.value)
    }

    override fun deserializeNBT(nbt: CompoundTag?) {
        nbt?.let {
            gridConnectedName = it.getString("gridName")
            beSet = it.getBoolean("beSet")
            machine.wirelessMachineRunTime.teamUUID.value = if (it.hasUUID("teamUUID"))it.getUUID("teamUUID") else machine.self().ownerUUID ?: UUID.randomUUID()
        }
    }

    override fun setOnContentsChanged(aa: Runnable?) {
        this.onContentChanged = aa
    }

    override fun getOnContentsChanged(): Runnable? = onContentChanged
}

@Scanned
interface WirelessMachine :
    IGridConnectedMachine,
    IBindable {
    @Scanned
    companion object {
        @RegisterLanguage(cn = "网络节点选择", en = "Grid Node Selector")
        const val gridNodeSelector = "gtocore.integration.ae.WirelessMachine.gridNodeSelector"

        @RegisterLanguage(cn = "网络节点列表", en = "Grid Node List")
        const val gridNodeList = "gtocore.integration.ae.WirelessMachine.gridNodeList"

        @RegisterLanguage(cn = "当前连接到 %s", en = "Currently connected to %s")
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
    }

    override fun getUUID(): UUID = getGridPermissionUUID()

    // ////////////////////////////////
    // ****** 机器必须实现此字段 ******//
    // //////////////////////////////
    var wirelessMachinePersisted: WirelessMachinePersisted // persisted + descSync
    var wirelessMachineRunTime: WirelessMachineRunTime

    // ////////////////////////////////
    // ****** WirelessSavedData的回调，可通过重写自定义，服务端方法 ******//
    // //////////////////////////////
    fun addedToGrid(gridName: String) {
    }
    fun removedFromGrid(gridName: String) {
    }

    // ////////////////////////////////
    // ****** 机器必须在相应时机调用 ******//
    // //////////////////////////////
    fun onWirelessMachineLoad() {
        if (self().isRemote) return
        val nowTick = self().offsetTimer
        self().subscribeServerTick {
            if (self().offsetTimer > nowTick + 40 && self().offsetTimer % 10 == 0L && mainNode.node != null) {
                if (!wirelessMachineRunTime.isInitialized && !self().isRemote) {
                    if (!wirelessMachinePersisted.beSet) {
                        WirelessSavedData.INSTANCE.gridPool.firstOrNull { it.owner == getGridPermissionUUID() && it.isDefault }?.let {
                            joinGrid(it.name)
                        }
                    } else {
                        if (wirelessMachinePersisted.gridConnectedName.isNotEmpty()) {
                            linkGrid(wirelessMachinePersisted.gridConnectedName)
                        }
                    }
                    wirelessMachineRunTime.isInitialized = true
                }
            }
        }
    }
    fun onWirelessMachineUnLoad() {
        wirelessMachineRunTime.gridCache.unregister()
        wirelessMachineRunTime.teamUUID.unregister()
        wirelessMachineRunTime.FilterInMachineTypeSyncField.unregister()
        if (self().isRemote) return
        unLinkGrid()
    }
    fun onWirelessMachineClientTick() {
    }
    fun onWirelessMachinePlaced(player: LivingEntity?, stack: ItemStack?) {
        player?.let { self().ownerUUID = it.uuid }
        if (!self().isRemote) wirelessMachineRunTime.teamUUID.updateInServer(TeamUtil.getTeamUUID(self().ownerUUID ?: UUID.randomUUID()))
        self().requestSync()
    }

    // ////////////////////////////////
    // ****** 工具集 ******//
    // //////////////////////////////
    // 同步网络数据到客户端
    fun syncDataToClientInServer() {
        if (!this.self().isRemote) {
            if (GTOConfig.INSTANCE.aeLog)println("isRemote :${self().isRemote} Syncing network data for ${self().pos}, will send size : ${WirelessSavedData.INSTANCE.gridPool.size}")
            wirelessMachineRunTime.gridCache.updateInServer(WirelessSavedData.INSTANCE.gridPool)
        }
    }
    fun createWirelessMachinePersisted() = WirelessMachinePersisted(this)
    fun createWirelessMachineRunTime() = WirelessMachineRunTime(this)
    fun linkGrid(gridName: String) { // 连接网格，例如机器加载
        if (self().isRemote) return
        val status = WirelessSavedData.joinToGrid(gridName, this, getGridPermissionUUID())
        when (status) {
            STATUS.SUCCESS -> {
                wirelessMachineRunTime.gridConnected = WirelessSavedData.INSTANCE.gridPool.first { it.name == gridName }
            }
            STATUS.ALREADY_JOINT -> {
                wirelessMachineRunTime.gridConnected = WirelessSavedData.INSTANCE.gridPool.first { it.name == gridName }
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
        if (self().isRemote) return
        wirelessMachinePersisted.gridConnectedName = gridName
        wirelessMachinePersisted.beSet = true
        linkGrid(gridName)
    }
    fun unLinkGrid() { // 机器下线但不退出
        if (self().isRemote) return
        WirelessSavedData.leaveGrid(this)
    }
    fun leaveGrid() { // 退出网格
        if (self().isRemote) return
        unLinkGrid()
        wirelessMachinePersisted.gridConnectedName = ""
    }
    private fun getGridPermissionUUID(): UUID = wirelessMachineRunTime.teamUUID.value
    fun getSetupFancyUIProvider(): IFancyUIProvider = object : IFancyUIProvider {
        override fun getTabIcon(): IGuiTexture? = ItemStackTexture(AEItems.WIRELESS_RECEIVER.stack())

        override fun getTitle(): Component? = Component.translatable(gridNodeSelector)

        override fun createMainPage(p0: FancyMachineUIWidget?) = rootFresh(176, 166) {
            println(1)
            hBox(height = availableHeight, { spacing = 4 }) {
                blank()
                vBox(width = this@rootFresh.availableWidth - 4, style = { spacing = 4 }) {
                    blank()
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = { Component.translatable(currentlyConnectedTo, wirelessMachinePersisted.gridConnectedName.ifEmpty { "无" }) },
                    )
                    hBox(height = 16, { spacing = 4 }) {
                        field(
                            width = 60,
                            getter = { wirelessMachineRunTime.gridWillAdded },
                            setter = { wirelessMachineRunTime.gridWillAdded = it },
                        )
                        button(transKet = createGrid, width = this@vBox.availableWidth - 60 - 8) { ck ->
                            if (!ck.isRemote &&
                                wirelessMachineRunTime.gridWillAdded.isNotEmpty() &&
                                wirelessMachineRunTime.gridCache.value.none { it.name == wirelessMachineRunTime.gridWillAdded }
                            ) {
                                WirelessSavedData.createNewGrid(
                                    wirelessMachineRunTime.gridWillAdded,
                                    getGridPermissionUUID(),
                                )
                            }
                            println("isRemote :${ck.isRemote} Ask for sync in 2")
                            syncDataToClientInServer()
                            println("isRemote :${ck.isRemote} Create Grid: ${wirelessMachineRunTime.gridWillAdded}")
                            if (GTOConfig.INSTANCE.aeLog)println("isRemote :${ck.isRemote} GridCacheValue: ${wirelessMachineRunTime.gridCache.value}")
                            if (GTOConfig.INSTANCE.aeLog)println("isRemote :${ck.isRemote} GridSavedValue: ${WirelessSavedData.INSTANCE.gridPool}")
                        }
                    }
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = {
                            Component.translatable(globalWirelessGrid, wirelessMachineRunTime.gridCache.value.count { it.owner == getGridPermissionUUID() }, wirelessMachineRunTime.gridCache.value.count())
                        },
                    )
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = { Component.translatable(yourWirelessGrid) },
                    )
                    vScroll(width = availableWidth, height = 176 - 4 - 20 - 36 - 16, { spacing = 2 }) a@{
                        wirelessMachineRunTime.gridCache.value.filter { it.owner == getGridPermissionUUID() }
                            .forEach { grid ->
                                hBox(height = 14, { spacing = 4 }) {
                                    button(
                                        height = 14,
                                        text = { "${if (grid.isDefault) "⭐" else ""}${grid.name}" },
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
                                                WirelessSavedData.setAsDefault(grid.name, getGridPermissionUUID())
                                            }
                                            syncDataToClientInServer()
                                        })
                                    } else {
                                        button(height = 14, text = { "⚝" }, width = 18, onClick = {
                                            if (!it.isRemote) {
                                                WirelessSavedData.cancelAsDefault(
                                                    grid.name,
                                                    getGridPermissionUUID(),
                                                )
                                            }
                                            syncDataToClientInServer()
                                        })
                                    }
                                    button(height = 14, transKet = removeGrid, width = 36, onClick = {
                                        if (!it.isRemote) {
                                            WirelessSavedData.removeGrid(grid.name, getGridPermissionUUID())
                                        }
                                        syncDataToClientInServer()
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
            println(2)

            hBox(height = availableHeight, { spacing = 4 }) {
                blank()
                vBox(width = this@rootFresh.availableWidth - 4, { spacing = 4 }) {
                    blank()
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = { Component.translatable(currentlyConnectedTo, wirelessMachinePersisted.gridConnectedName.ifEmpty { "无" }) },
                    )
                    // filter 选择器
                    hScroll(width = availableWidth - 4, height = 20, style = { spacing = 4 }) {
                        val syncField: EnumSyncField<FilterInMachineType> = wirelessMachineRunTime.FilterInMachineTypeSyncField
                        syncField.enumClass.enumConstants.forEach { select: FilterInMachineType ->
                            button(
                                width = if (self().isRemote) Minecraft.getInstance().font.width(select.component.get().string) + 20 else 20,
                                height = 16,
                                text = { select.component.get().string },
                                onClick = { ck ->
                                    if (!ck.isRemote) syncField.updateInServer(select)
                                },
                            )
                        }
                    }
                    if (self().isRemote) {
                        vScroll(width = availableWidth, height = 176 - 4 - 10 - 16 - 20, { spacing = 2 }) {
                            val availableWidth1 = availableWidth
                            // filter 应用，数据展示。
                            val objects = wirelessMachineRunTime.gridCache.value.firstOrNull { it.name == wirelessMachinePersisted.gridConnectedName }?.connectionPoolTable
                            val machineTypeFilter: (WirelessGrid.MachineInfo) -> Boolean = { info: WirelessGrid.MachineInfo ->
                                when (wirelessMachineRunTime.FilterInMachineTypeSyncField.value) {
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
                                                                GTORenderManager.tasks.push(GTORenderType.BLOCK_LINE(
                                                                    data = GTORenderData.BLOCK_LINE_DATA(
                                                                        pos = it.pos,
                                                                        level = self().holder.level().dimension(),
                                                                        durationTick = 1.minutes.toTicks(),
                                                                        flickerCycle = 1.seconds.toTicks()
                                                                    )
                                                                )) // 推送高亮数据
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

package com.gtocore.integration.ae

import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.common.network.BooleanSyncField
import com.gtocore.common.network.createLogicalSide
import com.gtocore.common.saved.STATUS
import com.gtocore.common.saved.WirelessGrid
import com.gtocore.common.saved.WirelessSavedData
import com.gtocore.common.saved.WirelessSyncField

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
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
import com.hepdd.gtmthings.utils.TeamUtil
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable

import java.util.UUID
class WirelessMachineRunTime(var machine: WirelessMachine) {
    var gridWillAdded: String = ""
    var gridConnected: WirelessGrid? = null // ONLY SERVER
    var connectPageFreshRun: Runnable = Runnable {}
    var detailsPageFreshRun: Runnable = Runnable {}
    var freshTick = 0L
    var isInitialized = false
    var gridCache = WirelessSyncField(
        createLogicalSide(machine.self().isRemote),
        { "machine.getGridInfoInMachine.gridConnectedName-${machine.self().pos}" },
        WirelessSavedData.gridPool,
        onSyncCallBack = { f, old, new ->
            connectPageFreshRun.run()
            detailsPageFreshRun.run()
            println("isRemote :${machine.self().isRemote} Fresh,pos:${machine.self().pos}, old:$old, new:$new,oldSize:${old.size}, newSize:${new.size}")
        },
    )
    var updateGridPoolSignal = BooleanSyncField(
        createLogicalSide(machine.self().isRemote),
        { "machine.getGridInfoInMachine.updateGridPoolSignal-${machine.self().pos}" },
        value = false,
        onSyncCallBack = { f, old, new ->
            if (new && !machine.self().isRemote) {
                gridCache.updateInServer(WirelessSavedData.gridPool)
            }
        },
    )
}
class WirelessMachinePersisted(var machine: WirelessMachine) :
    ITagSerializable<CompoundTag>,
    IContentChangeAware {
    var onContentChanged: Runnable? = null
    var gridConnectedName: String = ""
        set(value) {
            field = value
            onContentChanged?.run()
            machine.self().requestSync()
        }
    var beSet = false // 是否已经设置过网格连接

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

@Scanned
interface WirelessMachine : IGridConnectedMachine {
    @Scanned
    companion object {
        @RegisterLanguage(cn = "连接ME网络", en = "Connect to ME Grid")
        val connectToGrid = "gtocore.integration.ae.WirelessMachine.connectToGrid"

        @RegisterLanguage(cn = "网络节点列表", en = "Grid Node List")
        val gridNodeList = "gtocore.integration.ae.WirelessMachine.gridNodeList"

        @RegisterLanguage(cn = "当前连接到 %s", en = "Currently connected to %s")
        val currentlyConnectedTo = "gtocore.integration.ae.WirelessMachine.currentlyConnectedTo"

        @RegisterLanguage(cn = "创建网络", en = "Create Grid")
        val createGrid = "gtocore.integration.ae.WirelessMachine.createGrid"

        @RegisterLanguage(cn = "全球可用无线网络 : %s / %s", en = "Global available wireless grids: %s / %s")
        val globalWirelessGrid = "gtocore.integration.ae.WirelessMachine.globalWirelessGrid"

        @RegisterLanguage(cn = "删除", en = "Remove")
        val removeGrid = "gtocore.integration.ae.WirelessMachine.removeGrid"

        @RegisterLanguage(cn = "你的无线网络 : ", en = "Your wireless grids: ")
        val yourWirelessGrid = "gtocore.integration.ae.WirelessMachine.yourWirelessGrid"
    }

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
            if (self().offsetTimer > nowTick + 20) {
                if (!wirelessMachineRunTime.isInitialized && !self().isRemote) {
                    if (!wirelessMachinePersisted.beSet) {
                        WirelessSavedData.gridPool.firstOrNull { it.owner == getGridPermissionUUID() && it.isDefault }?.let {
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
        if (self().isRemote) return
        unLinkGrid()
    }
    fun onWirelessMachineClientTick() {
    }
    fun onWirelessMachinePlaced(player: LivingEntity?, stack: ItemStack?) {
        player?.let { self().ownerUUID = it.uuid }
    }

    // ////////////////////////////////
    // ****** 工具集 ******//
    // //////////////////////////////
    // 客户端请求同步网络数据
    fun syncDataToClientInServer() {
        if (!this.self().isRemote) {
            println("isRemote :${self().isRemote} Syncing network data for ${self().pos}")
            wirelessMachineRunTime.gridCache.updateInServer(WirelessSavedData.gridPool)
        }
    }
    fun askSyncDataFromServer() {
        if (self().isRemote) {
            wirelessMachineRunTime.updateGridPoolSignal.updateInClient(true)
        }
    }
    fun createWirelessMachinePersisted() = WirelessMachinePersisted(this)
    fun createWirelessMachineRunTime() = WirelessMachineRunTime(this)
    fun linkGrid(gridName: String) { // 连接网格，例如机器加载
        if (self().isRemote) return
        val status = WirelessSavedData.joinToGrid(gridName, this, getGridPermissionUUID())
        when (status) {
            STATUS.SUCCESS -> {
                wirelessMachineRunTime.gridConnected = WirelessSavedData.gridPool.first { it.name == gridName }
            }
            STATUS.ALREADY_JOINT -> {
                wirelessMachineRunTime.gridConnected = WirelessSavedData.gridPool.first { it.name == gridName }
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
    fun getGridPermissionUUID(): UUID = TeamUtil.getTeamUUID(self().ownerUUID ?: UUID.randomUUID())
    fun getUIRequesterUUID(): UUID = TeamUtil.getTeamUUID(self().ownerUUID ?: UUID.randomUUID())
    fun getSetupFancyUIProvider(): IFancyUIProvider = object : IFancyUIProvider {
        override fun getTabIcon(): IGuiTexture? = ItemStackTexture(AEItems.WIRELESS_RECEIVER.stack())

        override fun getTitle(): Component? = Component.translatable(connectToGrid)

        override fun createMainPage(p0: FancyMachineUIWidget?) = rootFresh(176, 166) {
            hBox(height = availableHeight, { spacing = 4 }) {
                blank()
                vBox(width = this@rootFresh.availableWidth - 4, { spacing = 4 }) {
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
                        button(transKet = createGrid, width = this@vBox.availableWidth - 60 - 8) {
                            if (!self().isRemote &&
                                wirelessMachineRunTime.gridWillAdded.isNotEmpty() &&
                                wirelessMachineRunTime.gridCache.value.none { it.name == wirelessMachineRunTime.gridWillAdded }
                            ) {
                                WirelessSavedData.createNewGrid(
                                    wirelessMachineRunTime.gridWillAdded,
                                    getUIRequesterUUID(),
                                )
                            }
                            println("isRemote :${self().isRemote} Ask for sync in 2")
                            syncDataToClientInServer()
                            println("isRemote :${self().isRemote} Create Grid: ${wirelessMachineRunTime.gridWillAdded}")
                            println("isRemote :${self().isRemote} GridCacheValue: ${wirelessMachineRunTime.gridCache.value}")
                            println("isRemote :${self().isRemote} GridSavedValue: ${WirelessSavedData.gridPool}")
                        }
                    }
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = {
                            Component.translatable(globalWirelessGrid, wirelessMachineRunTime.gridCache.value.count { it.owner == getUIRequesterUUID() }, wirelessMachineRunTime.gridCache.value.count())
                        },
                    )
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = { Component.translatable(yourWirelessGrid) },
                    )
                    vScroll(width = availableWidth, height = 176 - 4 - 20 - 36 - 16, { spacing = 2 }) a@{
                        wirelessMachineRunTime.gridCache.value.filter { it.owner == getUIRequesterUUID() }
                            .forEach { grid ->
                                hBox(height = 14, { spacing = 4 }) {
                                    button(
                                        height = 14,
                                        text = { "${if (grid.isDefault) "⭐" else ""}${grid.name}" },
                                        width = this@a.availableWidth - 48 - 8 + 12 - 4 - 18,
                                    ) {
                                        if (!self().isRemote) {
                                            leaveGrid()
                                            joinGrid(grid.name)
                                        }
                                    }
                                    if (!grid.isDefault) {
                                        button(height = 14, text = { "⭐" }, width = 18) {
                                            if (!self().isRemote) {
                                                WirelessSavedData.setAsDefault(grid.name, getUIRequesterUUID())
                                            }
                                            println("isRemote :${self().isRemote} Ask for sync in 3")
                                            syncDataToClientInServer()
                                        }
                                    } else {
                                        button(height = 14, text = { "⚝" }, width = 18) {
                                            if (!self().isRemote) {
                                                WirelessSavedData.cancelAsDefault(
                                                    grid.name,
                                                    getUIRequesterUUID(),
                                                )
                                            }
                                            println("isRemote :${self().isRemote} Ask for sync in 4")
                                            syncDataToClientInServer()
                                        }
                                    }
                                    button(height = 14, transKet = removeGrid, width = 36) {
                                        if (!self().isRemote) {
                                            WirelessSavedData.removeGrid(grid.name, getUIRequesterUUID())
                                        }
                                        println("isRemote :${self().isRemote} Ask for sync in 5")
                                        syncDataToClientInServer()
                                    }
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
            hBox(height = availableHeight, { spacing = 4 }) {
                blank()
                vBox(width = this@rootFresh.availableWidth - 4, { spacing = 4 }) {
                    blank()
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = { Component.translatable(currentlyConnectedTo, wirelessMachinePersisted.gridConnectedName.ifEmpty { "无" }) },
                    )
                    vScroll(width = availableWidth, height = 176 - 4 - 10 - 16, { spacing = 2 }) {
                        wirelessMachineRunTime.gridCache.value.firstOrNull { it.name == wirelessMachinePersisted.gridConnectedName }?.connectionPoolTable
                            ?.groupBy { it.level }
                            ?.forEach { level, line ->
                                textBlock(maxWidth = availableWidth, textSupplier = { Component.literal("- ${level.location().path} (${line.size})") })
                                line.groupBy { it.owner }
                                    .forEach { owner, line1 ->
                                        textBlock(maxWidth = availableWidth, tab = 10, textSupplier = { Component.literal("- $owner") })
                                        line1.sortedBy { it.descriptionId }
                                            .forEach {
                                                textBlock(maxWidth = availableWidth, tab = 20, textSupplier = { Component.literal("- ${Component.translatable(it.descriptionId).string} (${it.pos.x},${it.pos.y},${it.pos.z})") })
                                            }
                                    }
                            }
                    }
                }
            }
        }.also { wirelessMachineRunTime.detailsPageFreshRun = Runnable { it.fresh() } }
    }
}

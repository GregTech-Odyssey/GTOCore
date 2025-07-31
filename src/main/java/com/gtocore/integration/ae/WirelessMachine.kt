package com.gtocore.integration.ae

import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.common.saved.STATUS
import com.gtocore.common.saved.WirelessGrid
import com.gtocore.common.saved.WirelessSavedData

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component

import appeng.core.definitions.AEItems
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife
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
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

import java.util.UUID

class GridInfoInMachine(var machine: WirelessMachine) :
    ITagSerializable<CompoundTag>,
    IContentChangeAware {
    var onContentChanged: Runnable? = null

    // persisted
    var gridConnectedName: String = ""
        set(value) {
            field = value
            onContentChanged?.run()
            machine.self().requestSync()
        }
    var beSet = false // 是否已经设置过网格连接

    // cache
    var gridConnected: WirelessGrid? = null // ONLY SERVER
    var gridWillAdded: String = ""
    var freshRun: Runnable = Runnable {}
    var shouldFresh = false
    var freshTick = 0L
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
        val  createGrid = "gtocore.integration.ae.WirelessMachine.createGrid"
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
    var getGridInfoInMachine: GridInfoInMachine

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
        val init by lazy {
            if (!getGridInfoInMachine.beSet) {
                WirelessSavedData.getGirdList().firstOrNull { it.owner == getGridPermissionUUID() && it.isDefault }?.let {
                    joinGrid(it.name)
                }
            } else {
                if (getGridInfoInMachine.gridConnectedName.isNotEmpty()) {
                    linkGrid(getGridInfoInMachine.gridConnectedName)
                }
            }
            syncNetworkData()
        }
        self().subscribeServerTick {
            if (self().offsetTimer > nowTick + 20) {
                init
            }
        }
    }
    fun onWirelessMachineUnLoad() {
        if (self().isRemote) return
        unLinkGrid()
    }
    fun onWirelessMachineClientTick() {
        if (self().isRemote) {
            if (getGridInfoInMachine.shouldFresh && self().offsetTimer > getGridInfoInMachine.freshTick) {
                getGridInfoInMachine.shouldFresh = false
                getGridInfoInMachine.freshRun.run()
            }
        }
    }
    fun onWirelessMachinePlaced(player: LivingEntity?, stack: ItemStack?) {
        player?.let { self().ownerUUID = it.uuid }
    }
    // ////////////////////////////////
    // ****** 工具集 ******//
    // //////////////////////////////
    // 客户端请求同步网络数据
    fun syncNetworkData() {
        if (this.self().isRemote) WirelessSavedData.grid.askForSyncInClient()
    }

    // 刷新网络数据，刷新客户端和服务器UI
    fun triggerFresh() {
        if (this.self().isRemote) {
            WirelessSavedData.grid.askForSyncInClient()
            getGridInfoInMachine.shouldFresh = true
            getGridInfoInMachine.freshTick = self().offsetTimer + 20
        } else {
            getGridInfoInMachine.freshRun.run()
        }
    }
    fun createGridInfoInMachine() = GridInfoInMachine(this)
    fun linkGrid(gridName: String) { // 连接网格，例如机器加载
        val status = WirelessSavedData.joinToGrid(gridName, this, getGridPermissionUUID())
        when (status) {
            STATUS.SUCCESS -> {
                getGridInfoInMachine.gridConnected = WirelessSavedData.getGirdList().first { it.name == gridName }
            }
            STATUS.ALREADY_JOINT -> {
                getGridInfoInMachine.gridConnected = WirelessSavedData.getGirdList().first { it.name == gridName }
            }
            STATUS.NOT_FOUND_GRID -> {
                getGridInfoInMachine.gridConnected = null
                getGridInfoInMachine.gridConnectedName = ""
            }
            STATUS.NOT_PERMISSION -> {
                getGridInfoInMachine.gridConnected = null
            }
        }
    }
    fun joinGrid(gridName: String) {
        getGridInfoInMachine.gridConnectedName = gridName
        getGridInfoInMachine.beSet = true
        linkGrid(gridName)
    }
    fun unLinkGrid() { // 机器下线但不退出
        WirelessSavedData.leaveGrid(this)
    }
    fun leaveGrid() { // 退出网格
        unLinkGrid()
        getGridInfoInMachine.gridConnectedName = ""
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
                        textSupplier = { Component.translatable(currentlyConnectedTo,getGridInfoInMachine.gridConnectedName.ifEmpty { "无" }) },
                    )
                    hBox(height = 16, { spacing = 4 }) {
                        field(
                            width = 60,
                            getter = { getGridInfoInMachine.gridWillAdded },
                            setter = { getGridInfoInMachine.gridWillAdded = it },
                        )
                        button(transKet = createGrid, width = this@vBox.availableWidth - 60 - 8) {
                            if (!self().isRemote &&
                                getGridInfoInMachine.gridWillAdded.isNotEmpty() &&
                                WirelessSavedData.getGirdList()
                                    .none { it.name == getGridInfoInMachine.gridWillAdded }
                            ) {
                                WirelessSavedData.createNewGrid(
                                    getGridInfoInMachine.gridWillAdded,
                                    getUIRequesterUUID(),
                                )
                            }
                            triggerFresh()
                        }
                    }
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = {
                            Component.translatable(globalWirelessGrid,WirelessSavedData.getGirdList().count { it.owner == getUIRequesterUUID() },WirelessSavedData.getGirdList().count())
                        },
                    )
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = { Component.translatable(yourWirelessGrid) },
                    )
                    vScroll(width = availableWidth, height = 176 - 4 - 20 - 36 - 16, { spacing = 2 }) a@{
                        WirelessSavedData.getGirdList().filter { it.owner == getUIRequesterUUID() }
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
                                            triggerFresh()
                                        }
                                    } else {
                                        button(height = 14, text = { "⚝" }, width = 18) {
                                            if (!self().isRemote) {
                                                WirelessSavedData.cancelAsDefault(
                                                    grid.name,
                                                    getUIRequesterUUID(),
                                                )
                                            }
                                            triggerFresh()
                                        }
                                    }
                                    button(height = 14, transKet = removeGrid, width = 36) {
                                        if (!self().isRemote) {
                                            WirelessSavedData.removeGrid(grid.name, getUIRequesterUUID())
                                        }
                                        triggerFresh()
                                    }
                                }
                            }
                    }
                }
            }
        }.also { getGridInfoInMachine.freshRun = Runnable { it.fresh() } }
    }
    fun getDetailFancyUIProvider(): IFancyUIProvider {
        return object : IFancyUIProvider {
            override fun getTabIcon(): IGuiTexture? = ItemStackTexture(AEItems.WIRELESS_RECEIVER.stack())

            override fun getTitle(): Component? = Component.translatable(gridNodeList)

            override fun createMainPage(p0: FancyMachineUIWidget?): Widget? = rootFresh(256,166){
                hBox(height = availableHeight, { spacing = 4 }) {
                    blank()
                    vBox(width = this@rootFresh.availableWidth - 4, { spacing = 4 }) {
                        blank()
                        textBlock(
                            maxWidth = availableWidth - 4,
                            textSupplier = { Component.translatable(currentlyConnectedTo,getGridInfoInMachine.gridConnectedName.ifEmpty { "无" }) },
                        )
                        vScroll(width = availableWidth, height = 176 - 4 -10 - 16, { spacing = 2 }){
                            WirelessSavedData.getGirdList().firstOrNull { it.name ==getGridInfoInMachine.gridConnectedName }?.connectionPool?.forEach { machine1 ->
                                textBlock(maxWidth = availableWidth, textSupplier = { Component.literal("${machine1.self().playerOwner?.name} : ${machine1.self().pos}") })
                            }
                        }
                    }
                }
            }
        }
    }
}

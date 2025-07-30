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
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine
import com.gtolib.api.gui.ktflexible.blank
import com.gtolib.api.gui.ktflexible.button
import com.gtolib.api.gui.ktflexible.field
import com.gtolib.api.gui.ktflexible.rootFresh
import com.hepdd.gtmthings.utils.TeamUtil
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable

import java.util.UUID

class GridInfoInMachine(var machine: WirelessMachine) :
    ITagSerializable<CompoundTag>,
    IContentChangeAware {
    var onContentChanged: Runnable? = null

    // persisted
    var gridConnectedName: String = ""
        set(value) {
            field = value
            machine.self().requestSync()
        }
    var beSet = false // 是否已经设置过网格连接

    // cache
    var gridConnected: WirelessGrid? = null
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

interface WirelessMachine : IGridConnectedMachine {
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
            if (self().offsetTimer > nowTick + 10) {
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
            getGridInfoInMachine.freshTick = self().offsetTimer + 10
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
            STATUS.ALREADY_JOINT -> null
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
    fun getGridPermissionUUID(): UUID = TeamUtil.getTeamUUID(this.self().ownerUUID)
    fun getUIRequesterUUID(): UUID = TeamUtil.getTeamUUID(this.self().ownerUUID)
    fun getFancyUIProvider(): IFancyUIProvider = object : IFancyUIProvider {
        override fun getTabIcon(): IGuiTexture? = ItemStackTexture(AEItems.WIRELESS_RECEIVER.stack())

        override fun getTitle(): Component? = Component.literal("连接网络")

        override fun createMainPage(p0: FancyMachineUIWidget?) = rootFresh(176, 166) {
            hBox(height = availableHeight, { spacing = 4 }) {
                blank()
                vBox(width = this@rootFresh.availableWidth - 4, { spacing = 4 }) {
                    blank()
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = { Component.literal("当前连接的无线网络: ${getGridInfoInMachine.gridConnectedName.ifEmpty { "无" }}") },
                    )
                    hBox(height = 16, { spacing = 4 }) {
                        field(
                            width = 60,
                            getter = { getGridInfoInMachine.gridWillAdded },
                            setter = { getGridInfoInMachine.gridWillAdded = it },
                        )
                        button(text = { "创建新的无线网络" }, width = this@vBox.availableWidth - 60 - 8) {
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
                            Component.literal(
                                "全球可用无线网络 : ${
                                    WirelessSavedData.getGirdList().count { it.owner == getUIRequesterUUID() }
                                } / ${WirelessSavedData.getGirdList().count()}",
                            )
                        },
                    )
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = { Component.literal("你的无线网络 : ") },
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
                                    button(height = 14, text = { "删除" }, width = 36) {
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
}

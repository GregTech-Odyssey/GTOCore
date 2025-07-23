package com.gtocore.integration.ae

import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.common.saved.MEWirelessSavedData

import net.minecraft.core.Direction
import net.minecraft.network.chat.Component

import appeng.me.ManagedGridNode
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder
import com.gtolib.api.gui.ktflexible.blank
import com.gtolib.api.gui.ktflexible.button
import com.gtolib.api.gui.ktflexible.field
import com.gtolib.api.gui.ktflexible.root
import com.gtolib.api.gui.ktflexible.rootFresh
import com.lowdragmc.lowdraglib.gui.modular.ModularUI
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted
import net.minecraft.world.entity.player.Player

import java.util.*

class MEWirelessConnectionMachine(holder: IMachineBlockEntity) :
    MetaMachine(holder),
    IGridConnectedMachine,
    IFancyUIMachine {
    // ////////////////////////////////
    // ****** Grid Initialization ******//
    // //////////////////////////////
    var gridIsOnline: Boolean = false
    var gridMainNodeHolder: GridNodeHolder = GridNodeHolder(this)
    override fun isOnline() = gridIsOnline
    override fun setOnline(p0: Boolean) = run { gridIsOnline = p0 }
    override fun getMainNode(): ManagedGridNode = gridMainNodeHolder.mainNode.setExposedOnSides(EnumSet.allOf(Direction::class.java))
    override fun onLoad() {
        super.onLoad()
        val serverInitializer by lazy {
            println("MEWirelessConnectionMachine initialized: $pos at ${level?.dimension()?.location()} in Remote $isRemote")
            MEWirelessSavedData.findGridById(savedGird ?: UUID.randomUUID())?.let {
                joinGrid(it)
            }
        }
        subscribeServerTick {
            if (offsetTimer > 10 && offsetTimer % 20 == 0L) {
                if (!isRemote) serverInitializer
            }
        }
    }

    override fun isRemote() = super<MetaMachine>.isRemote

    override fun onUnload() {
        val serverDestroyer by lazy {
            println("MEWirelessConnectionMachine uninitialized: $pos at ${level?.dimension()?.location()} in Remote $isRemote")
            selectedGrid?.let {
                leaveGrid(it)
            }
        }
        if (!isRemote) serverDestroyer
        super.onUnload()
    }

    // ////////////////////////////////
    // ****** LOGIC ******//
    // //////////////////////////////
    @Persisted
    var savedGird: UUID? = null
    var selectedGrid: MEWirelessSavedData.MEWirelessConnectionGrid? = null
    fun joinGrid(grid: MEWirelessSavedData.MEWirelessConnectionGrid) {
        selectedGrid = grid
        grid.addToConnectionPool(this)
    }
    fun leaveGrid(grid: MEWirelessSavedData.MEWirelessConnectionGrid) {
        selectedGrid = null
        grid.removeFromConnectionPool(this)
    }
    fun changeToGrid(grid: MEWirelessSavedData.MEWirelessConnectionGrid) {
        selectedGrid?.let { leaveGrid(it) }
        joinGrid(grid)
    }

    // ////////////////////////////////
    // ****** UI ******//
    // //////////////////////////////
    var nameWillAdd: String = ""
    lateinit var playerUUID: UUID
    override fun createUI(entityPlayer: Player): ModularUI {
        playerUUID = entityPlayer.uuid
        return super.createUI(entityPlayer)
    }

    override fun createUIWidget() = rootFresh(176, 166) {
        hBox(height = availableHeight, { spacing = 4 }) {
            blank()
            vBox(width = this@rootFresh.availableWidth - 4, { spacing = 4 }) {
                blank()
                textBlock(maxWidth = availableWidth - 4, textSupplier = { Component.literal("当前连接的无线网络: ${selectedGrid?.gridName ?: "无"}") })
                hBox(height = 16, { spacing = 4 }) {
                    field(width = 60, getter = { nameWillAdd }, setter = { nameWillAdd = it })
                    button(text = { "创建新的无线网络" }, width = this@vBox.availableWidth - 60 - 8) {
                        if (isRemote) return@button
                        MEWirelessSavedData.createNewGrid(nameWillAdd, playerUUID)?.let {
                            MEWirelessSavedData.MEWirelessGrids.add(it)
                            changeToGrid(it)
                        }

                    }
                }
                vBox(width = availableWidth, { spacing = 2 })a@{
                    MEWirelessSavedData.findGridByPlayerId(ownerUUID ?: UUID.randomUUID()).forEach { grid ->
                        hBox(height = 16, { spacing = 4 }) {
                            button(text = { "切换到 ${grid.gridName}" }, width = this@a.availableWidth - 48 - 8) {
                                if (isRemote) return@button
                                changeToGrid(grid)
                            }
                            button(text = { "删除" }, width = 48) {
                                if (isRemote) return@button
                                leaveGrid(grid)
                                MEWirelessSavedData.removeGrid(grid)
                            }
                        }
                    }
                }
            }
        }
    }
}
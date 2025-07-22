package com.gtocore.integration.ae

import appeng.api.networking.GridHelper
import appeng.api.networking.IGridConnection
import appeng.api.orientation.BlockOrientation
import appeng.me.ManagedGridNode
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder
import com.gtocore.common.saved.MEWirelessSavedData
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraftforge.common.util.INBTSerializable
import java.util.*

class MEWirelessConnectionMachine(holder: IMachineBlockEntity) : MetaMachine(holder),IGridConnectedMachine,
    IFancyUIMachine {
    //////////////////////////////////
    // ****** Grid Initialization ******//
    ////////////////////////////////
    var gridIsOnline: Boolean = false
    var gridMainNodeHolder: GridNodeHolder = GridNodeHolder(this)
    override fun isOnline()=gridIsOnline
    override fun setOnline(p0: Boolean)= run { gridIsOnline = p0 }
    override fun getMainNode(): ManagedGridNode =gridMainNodeHolder.mainNode.setExposedOnSides(EnumSet.allOf(Direction::class.java))
    override fun onLoad() {
        super.onLoad()
        val serverInitializer by lazy {
            println("MEWirelessConnectionMachine initialized: ${pos} at ${level?.dimension()?.location()} in Remote ${isRemote}")
            MEWirelessSavedData.test.addToConnectionPool( this)
        }
        subscribeServerTick {
            if (offsetTimer > 10 && offsetTimer % 20 == 0L){
                if (!isRemote) serverInitializer
            }
        }
    }


    override fun onUnload() {
        val serverDestroyer by lazy {
            println("MEWirelessConnectionMachine uninitialized: ${pos} at ${level?.dimension()?.location()} in Remote ${isRemote}")
            MEWirelessSavedData.test.removeFromConnectionPool( this)
        }
        if(!isRemote) serverDestroyer
        super.onUnload()
    }
    //////////////////////////////////
    // ****** UI ******//
    ////////////////////////////////


    override fun isRemote()=super<MetaMachine>.isRemote
}
//////////////////////////////////
// ****** 触发链 ******//
////////////////////////////////
// 1. 玩家进行网络连接 / 断开
// 2. 机器被加载 / 卸载






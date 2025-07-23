package com.gtocore.common.saved

import appeng.api.networking.GridHelper
import appeng.api.networking.IGridConnection
import com.gtocore.integration.ae.MEWirelessConnectionMachine
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.world.level.saveddata.SavedData
import net.minecraftforge.common.util.INBTSerializable
import org.openjdk.nashorn.internal.ir.annotations.Ignore
import java.util.UUID
import kotlin.collections.forEach

private const val NBT_GRID = "MEWirelessConnectionGrids"

object MEWirelessSavedData : SavedData() {
    val MEWirelessGrids = mutableListOf<MEWirelessConnectionGrid>()
    override fun save(tag: CompoundTag): CompoundTag {
        tag.put(NBT_GRID, ListTag().apply { MEWirelessGrids.forEach { add(it.serializeNBT()) } })
        return tag
    }
    fun load(tag: CompoundTag): MEWirelessSavedData {
        tag.getList(NBT_GRID, 10).forEach { MEWirelessGrids.add(MEWirelessConnectionGrid().apply { deserializeNBT(it as CompoundTag) }) }
        return this
    }
    // 每个无线机器网络，处理无线连机的加入，退出，连接，刷新
    class MEWirelessConnectionGrid: INBTSerializable<CompoundTag>{
        //////////////////////////////////
        // ****** 网络元数据(保存在存档) ******//
        ////////////////////////////////
        var gridID : UUID = UUID.nameUUIDFromBytes("DEFAULT".toByteArray())
        var gridName : String = "DEFAULT"
        var gridOwnerUUid : UUID = UUID.nameUUIDFromBytes("DEFAULT-PLAYER".toByteArray()) // 拥有修改网络权限的权限
        var gridPermissionHolder : MutableList<UUID> = mutableListOf() // 可以发现此网络的UUID或者团队
            get() =  field.apply { if (!field.contains(gridOwnerUUid)) field.add(gridOwnerUUid) }.distinct().toMutableList()


        override fun serializeNBT(): CompoundTag {
            return CompoundTag().apply {
                putUUID("gridID", gridID)
                putString("gridName", gridName)
                putUUID("gridOwnerUUid", gridOwnerUUid)
                put("gridPermissionHolder", ListTag().apply {
                    gridPermissionHolder.forEach { add((StringTag.valueOf(it.toString()))) }
                })
            }
        }

        override fun deserializeNBT(p0: CompoundTag) {
            p0.apply {
                gridID = getUUID("gridID")
                gridName = getString("gridName")
                gridOwnerUUid = getUUID("gridOwnerUUid")
                gridPermissionHolder = mutableListOf()
                getList("gridPermissionHolder", 8).forEach {
                    gridPermissionHolder.add(UUID.fromString(it.asString))
                }
                gridPermissionHolder.distinct()
            }
        }

        //////////////////////////////////
        // ****** 运行时数据(不保存，由机器保存连接数据，并在加载的时候注册) ******//
        ////////////////////////////////
        val connectionPool = mutableListOf<MEWirelessConnectionMachine>()
        val mainMachine : MEWirelessConnectionMachine? = null // 如果有，则生成放射状网络，否则生成环形网络
        val connectionHolderPool = mutableListOf<IGridConnection>()
        fun getConnectionPair() : List<Pair<MEWirelessConnectionMachine, MEWirelessConnectionMachine>> {
            val machines = mutableListOf<MEWirelessConnectionMachine>()
            machines.addAll(connectionPool.filter{ it.mainNode.node!=null })

            mainMachine?.let { mainMachine ->
                machines.remove(mainMachine)
                return machines.map { mainMachine to it }
            }
            return machines.windowed(2) { it[0] to it[1] }
        }
        fun addToConnectionPool(machine: MEWirelessConnectionMachine){
            connectionPool.add(machine)
            refreshConnectionPool()
        }
        fun removeFromConnectionPool(machine: MEWirelessConnectionMachine){
            connectionPool.remove(machine)
            refreshConnectionPool()
        }
        fun refreshConnectionPool(){
            destroyAllConnection()
            createAllConnection()
        }

        fun destroyAllConnection(){
            connectionHolderPool.forEach { it.destroy() }
            connectionHolderPool.clear()
        }
        fun createAllConnection() {
            getConnectionPair().forEach {
                try {
                    val gridConnection = GridHelper.createConnection(it.first.mainNode.node, it.second.mainNode.node)
                    println("create connection between ${it.first.pos} and ${it.second.pos}")
                    connectionHolderPool.add(gridConnection)
                } catch (ignore: Exception) {
                }
            }
        }
    }
    var test = MEWirelessConnectionGrid()

}
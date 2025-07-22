package com.gtocore.common.network

import appeng.core.sync.network.NetworkHandler
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.server.ServerLifecycleHooks

object SyncFieldManager {
    val syncFieldMap = mutableMapOf<Pair<String, LogicalSide>, SyncField<*>>()
    fun clear() {
        syncFieldMap.clear()
    }
    fun <T> registerSyncField(syncField: SyncField<T>) {
        require(syncFieldMap.filter { it.value.uniqueName == syncField.uniqueName&& it.value.side == syncField.side }.isEmpty()){ "SyncField name is already registered" }
        syncFieldMap[syncField.uniqueName to syncField.side] = syncField
    }
    //////////////////////////////////
    // ****** 服务器修改，客户端更新 ******//
    ////////////////////////////////
    fun syncToAllClients(uniqueName: String){
        val syncField = syncFieldMap[uniqueName to LogicalSide.SERVER]
        require(syncField != null) { "SyncField with name $uniqueName is not registered" }
        require(syncField.side == LogicalSide.SERVER) { "${syncField.errorPrefix} This method can only be called in server side" }
        val server = ServerLifecycleHooks.getCurrentServer()
        server?.playerList?.players?.forEach{
            ServerMessage.send(server,it,"sync_field", { buf ->
                buf.writeUtf(uniqueName)
                syncField.writeToBuffer(buf)
            })
        }
    }
    fun handleFromServer(buffer: FriendlyByteBuf) {
        val uniqueName = buffer.readUtf()
        val syncField = syncFieldMap[uniqueName to LogicalSide.CLIENT]
        require(syncField != null) { "SyncField with name $uniqueName is not registered" }
        require(syncField.side == LogicalSide.CLIENT) { "${syncField.errorPrefix} This method can only be called in client side" }
        syncField.handleFromServer(buffer)
    }
    //////////////////////////////////
    // ****** 客户端修改，服务器更新 ******//
    //////////////////////////////////
    fun syncToAllServer(uniqueName: String) {
        val syncField = syncFieldMap[uniqueName to LogicalSide.CLIENT]
        require(syncField != null) { "SyncField with name $uniqueName is not registered" }
        require(syncField.side == LogicalSide.CLIENT) { "${syncField.errorPrefix} This method can only be called in client side" }
        ClientMessage.send("sync_field",{buf ->
            buf.writeUtf(uniqueName)
            syncField.writeToBuffer(buf)
        })
    }
    fun handleFromClient(buffer: FriendlyByteBuf) {
        val uniqueName = buffer.readUtf()
        val syncField = syncFieldMap[uniqueName to LogicalSide.SERVER]
        require(syncField != null) { "SyncField with name $uniqueName is not registered" }
        require(syncField.side == LogicalSide.SERVER) { "${syncField.errorPrefix} This method can only be called in server side" }
        syncField.handleFromClient(buffer)
//        syncToAllClients(uniqueName)
    }
}
abstract class SyncField<T> (
    val side: LogicalSide,
    val uniqueName : String,
    var value: T,
    var onInitCallBack : (SyncField<T>,new:T)-> Unit = { _, _ -> },
    var onSyncCallBack : (SyncField<T>,old:T,new:T)-> Unit = { _, _, _ -> },
): ITagSerializable<CompoundTag>, IContentChangeAware
{
    var onContentChanged : Runnable? = null
    override fun setOnContentsChanged(onContentChanged: Runnable?) {
        this.onContentChanged = onContentChanged
    }

    override fun getOnContentsChanged(): Runnable? {
        return onContentChanged
    }

    val errorPrefix = "[SyncField ${uniqueName} in side ${side}] :"
    init {
        //init
        onInitCallBack(this,value)
        //register
        SyncFieldManager.registerSyncField(this)
    }
    fun unregister() {
        SyncFieldManager.syncFieldMap.remove(uniqueName to side)
    }
    //////////////////////////////////
    // ****** 服务器修改，客户端更新 ******//
    ////////////////////////////////
    fun updateInServer(newValue: T) {
        require(side==LogicalSide.SERVER){ "$errorPrefix This method can only be called in server side" }
        if (value != newValue) {
            val oldValue = value
            value = newValue
            onSyncCallBack(this, oldValue, newValue)
            SyncFieldManager.syncToAllClients(uniqueName)
        }
    }
    fun handleFromServer(buffer: FriendlyByteBuf) {
        require(side==LogicalSide.CLIENT){ "$errorPrefix This method can only be called in client side" }
        val oldValue = value
        value = readFromBuffer(buffer)
        onSyncCallBack(this,oldValue , value)
    }
    //////////////////////////////////
    // ****** 客户端修改，服务器更新 ******//
    ////////////////////////////////
    fun updateInClient(newValue: T) {
        require(side==LogicalSide.CLIENT){ "$errorPrefix This method can only be called in client side" }
        if (value != newValue) {
            val oldValue = value
            value = newValue
            onSyncCallBack(this,oldValue , value)
            SyncFieldManager.syncToAllServer(uniqueName)
        }
    }
    fun handleFromClient(buffer: FriendlyByteBuf) {
        require(side==LogicalSide.SERVER){ "$errorPrefix This method can only be called in server side" }
        val oldValue = value
        value = readFromBuffer(buffer)
        onSyncCallBack(this,oldValue , value)
    }
    abstract fun readFromBuffer(buffer: FriendlyByteBuf) : T
    abstract fun writeToBuffer(buffer: FriendlyByteBuf): FriendlyByteBuf
}
fun createLogicalSide(isRemote : Boolean):LogicalSide = if(isRemote) LogicalSide.CLIENT else LogicalSide.SERVER
class IntSyncField(
    side: LogicalSide,
    uniqueName: String,
    value: Int,
    onInitCallBack: (SyncField<Int>, Int) -> Unit = { _, _ -> },
    onSyncCallBack: (SyncField<Int>, Int, Int) -> Unit = { _, _, _ -> },
) : SyncField<Int>(side, uniqueName, value, onInitCallBack, onSyncCallBack) {
    override fun readFromBuffer(buffer: FriendlyByteBuf): Int =buffer.readInt()
    override fun writeToBuffer(buffer: FriendlyByteBuf): FriendlyByteBuf = let { buffer.writeInt(value);buffer }
    override fun serializeNBT(): CompoundTag {
        return CompoundTag().apply { putInt("value",value) }
    }

    override fun deserializeNBT(nbt: CompoundTag) {
        updateInServer(nbt.getInt("value"))
    }
}

class BooleanSyncField(
    side: LogicalSide,
    uniqueName: String,
    value: Boolean,
    onInitCallBack: (SyncField<Boolean>, Boolean) -> Unit = { _, _ -> },
    onSyncCallBack: (SyncField<Boolean>, Boolean, Boolean) -> Unit = { _, _, _ -> },
) : SyncField<Boolean>(side, uniqueName, value, onInitCallBack, onSyncCallBack) {
    override fun readFromBuffer(buffer: FriendlyByteBuf): Boolean =buffer.readBoolean()
    override fun writeToBuffer(buffer: FriendlyByteBuf): FriendlyByteBuf = let { buffer.writeBoolean(value);buffer }
    override fun serializeNBT(): CompoundTag {
        return CompoundTag().apply { putBoolean("value",value) }
    }
    override fun deserializeNBT(nbt: CompoundTag) {
        updateInServer(nbt.getBoolean("value"))
    }
}
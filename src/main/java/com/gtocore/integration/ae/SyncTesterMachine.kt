package com.gtocore.integration.ae

import net.minecraftforge.fml.LogicalSide

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine
import com.gtolib.GTOCore
import com.gtolib.api.annotation.Synced
import com.gtolib.api.annotation.SyncedManager
import com.gtolib.api.capability.ISync
import com.gtolib.api.gui.ktflexible.button
import com.gtolib.api.gui.ktflexible.root
import com.gtolib.syncdata.SyncManagedFieldHolder
import com.lowdragmc.lowdraglib.gui.widget.Widget

import java.util.*

/**
 * 默认情况下，机器只能从服务器同步到客户端。使用方法：
 * - 重写 `getFieldHolder()` 管理器
 * - 给字段添加 `@DescSynced` 注解
 * - 默认会每隔一段时间自动同步
 *
 * 手动同步方法：
 * - `requestSync()`：将机器标记为需要同步，异步线程会在下次执行时进行同步，性能友好
 * - `syncNow()`：立即向客户端发送同步数据包，会阻塞服务器线程
 *
 * 若要实现从客户端同步到服务端，需实现 `ISync` 接口。使用方法：
 * - 一个方块实体只能有一个类实现ISync，如果有其他关联的类，使用@SyncedManager
 * - 重写 `getSyncHolder()` 管理器
 * - 给字段添加 `@Synced` 注解，可添加监听器，参数：LogicalSide（接收端），旧值，新值
 * - 或者使用ISyncedField相关类，无需加注解
 * - 无自动同步功能，需手动同步
 *
 * 手动同步方法：
 *  注解字段相关方法：
 * `syncToServer()`：立即向服务器发送同步数据包
 * `syncToClient()`：立即向客户端发送同步数据包
 * 加入可加入变量名参数，指定同步的变量
 * ISyncedField相关方法：
 * 同样可以使用注解字段的方法，下面是额外方法：
 * `setAndAutoSync()`：设置值并根据方块实体所在端位自动同步此字段
 * `setAndSyncToClient()`：设置值并立即向客户端同步此字段
 * `setAndSyncToServer()`：设置值并立即向服务器同步此字段
 *
 * 需要额外的注册方法
 * - `registerSync()`：注册到同步管理器，在机器 `onLoad()` 时调用或在方块实体 `clearRemoved()` 时调用
 * - `unregisterSync()`：从同步管理器中移除，在机器 `onUnload()` 时调用或在方块实体 `setRemoved()` 时调用
 *
 * 该机器演示ISync方法
 */
class SyncTesterMachine(holder: MetaMachineBlockEntity) :
    MetaMachine(holder),
    IFancyUIMachine,
    ISync {
    companion object {
        var SYNC_MANAGED_FIELD_HOLDER = SyncManagedFieldHolder(SyncTesterMachine::class.java)
    }

    override fun getSyncHolder() = SYNC_MANAGED_FIELD_HOLDER

    override fun isRemote() = super<MetaMachine>.isRemote

    // 作为子管理器,支持套娃
    @SyncedManager
    val a = a(this)

    // 使用同步字段
    val testInt = ISync.createIntField(this).setReceiverListener(this::intListener)

    // 使用注解 也可以不加监听器
    @Synced(listener = "booleanListener")
    var testBoolean = false

    fun intListener(side: LogicalSide, o: Int, n: Int) {
        GTOCore.LOGGER.info("intListener: $o -> $n to $side")
    }

    fun booleanListener(side: LogicalSide, o: Boolean, n: Boolean) {
        GTOCore.LOGGER.info("intListener: $o -> $n to $side")
    }

    override fun onLoad() {
        super.onLoad()
        registerSync()
    }

    override fun onUnload() {
        unregisterSync()
        super.onUnload()
    }

    override fun createUIWidget(): Widget? = root(176, 166) {
        vBox(width = availableWidth) {
            hBox(height = 20, { spacing = 4 }) {
                button(text = { "客户端Int+=1" }, onClick = { ck ->
                    if (isRemote) {
                        // 按是否为客户端自动指定方向
                        testInt.setAndAutoSync(testInt.get() + 1)
                        a.tesString.setAndAutoSync("int: " + testInt.get().toString())
                    }
                })
                button(text = { "服务端Int-=1" }, onClick = { ck ->
                    if (!isRemote) {
                        // 指定方向
                        testInt.setAndSyncToClient(testInt.get() - 1)
                        a.tesString.setAndSyncToClient("int: " + testInt.get().toString())
                    }
                })
            }
            hBox(height = 20, { spacing = 4 }) {
                button(text = { "客户端Boolean取反" }, onClick = { ck ->
                    if (isRemote) {
                        testBoolean = !testBoolean
                        // 全部字段同步
                        syncToServer()
                    }
                })
                button(text = { "服务端Boolean取反" }, onClick = { ck ->
                    if (!isRemote) {
                        testBoolean = !testBoolean
                        // 指定字段同步
                        syncToClient("testBoolean")
                    }
                })
            }
            button(text = { "获取数据" }) {
                if (isRemote) {
                    GTOCore.LOGGER.info("客户端Int: ${testInt.get()}")
                    GTOCore.LOGGER.info("客户端Boolean: $testBoolean")
                } else {
                    GTOCore.LOGGER.info("服务端Int: ${testInt.get()}")
                    GTOCore.LOGGER.info("服务端Boolean: $testBoolean")
                }
            }
        }
    }
}

class a(sync: ISync) {

    @SyncedManager
    var b = b(sync)

    val tesString = ISync.createStringField(sync).set("666").setSenderListener(this::stringListener)

    fun stringListener(side: LogicalSide, o: String, n: String) {
        GTOCore.LOGGER.info("stringListener: $o -> $n to $side")
        b.tesUUID.setAndAutoSync(UUID.randomUUID())
    }
}

class b(sync: ISync) {

    val tesUUID = ISync.createUUIDField(sync).set(UUID.randomUUID()).setReceiverListener(this::UUIDListener)

    fun UUIDListener(side: LogicalSide, o: UUID, n: UUID) {
        GTOCore.LOGGER.info("UUIDListener: $o -> $n to $side")
    }
}

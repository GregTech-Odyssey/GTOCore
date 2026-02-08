package com.gtocore.utils

import com.gtocore.common.machine.multiblock.part.ae.MEPatternPartMachineKt

import appeng.api.networking.IGrid
import appeng.blockentity.crafting.PatternProviderBlockEntity
import com.gtolib.api.ae2.IExpandedGrid
import kotlinx.coroutines.*

import java.lang.Runnable

object AEPatternRefresher {

    private const val TASK_CHUNK_SIZE = 10
    private const val DELAY_BETWEEN_CHUNKS_MS = 100L

    /**
     * 触发异步刷新。此方法会自动处理协程的启动和线程调度。
     * 无需返回值，调用即运行。
     */
    @JvmStatic
    fun refresh(grid: IGrid) {
        // 1. 安全检查：确保我们在服务端且 Grid 有效
        val level = grid.pivot?.level ?: return
        val server = level.server

        // 2. [主线程] 收集所有任务
        // 必须在主线程收集，因为 getActiveMachines 不是线程安全的
        val refreshTasks = mutableListOf<Runnable>()

        // 收集标准样板提供者
        grid.getActiveMachines(PatternProviderBlockEntity::class.java).forEach { machine ->
            refreshTasks.add(
                Runnable {
                    if (!machine.isRemoved) { // 执行前检查机器是否还存在
                        machine.logic.updatePatterns()
                    }
                },
            )
        }

        // 收集 GTOCore/GTCEu 扩展机器
        if (grid is IExpandedGrid) {
            grid.machines.values()
                .filter { it.isActive }
                .mapNotNull { it.owner as? MEPatternPartMachineKt<*> }
                .forEach { machine ->
                    refreshTasks.add(
                        Runnable {
                            (0 until machine.maxPatternCount).forEach { slotIndex ->
                                if (!machine.internalPatternInventory.getStackInSlot(slotIndex).isEmpty) {
                                    machine.onPatternChange(slotIndex)
                                }
                            }
                        },
                    )
                }
        }

        if (refreshTasks.isEmpty()) return

        // 3. [协程] 启动异步处理流程
        // 使用 GlobalScope 启动一个即使当前方法返回也会继续执行的任务
        // Dispatchers.Default 用于处理 delay 等待，不占用主线程
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(Dispatchers.Default) {
            refreshTasks.chunked(TASK_CHUNK_SIZE).forEach { chunk ->

                // 4. [主线程] 调度执行实际的更新逻辑
                server.execute {
                    chunk.forEach { task ->
                        try {
                            task.run()
                        } catch (e: Exception) {
                            e.printStackTrace() // 防止单个报错中断整个流程
                        }
                    }
                }

                // 5. [后台线程] 等待，避免卡顿
                delay(DELAY_BETWEEN_CHUNKS_MS)
            }
        }
    }
}

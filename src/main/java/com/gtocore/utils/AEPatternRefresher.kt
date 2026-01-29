package com.gtocore.utils

import com.gtocore.common.machine.multiblock.part.ae.MEPatternPartMachineKt

import appeng.api.networking.IGrid
import appeng.blockentity.crafting.PatternProviderBlockEntity
import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gtolib.api.ae2.IExpandedGrid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

data class RefreshProgress(val completed: Int, val total: Int)

/**
 * 一个用于异步刷新AE2网络中所有样板的工具类。
 * 这可以防止因一次性更新大量样板而导致的服务器卡顿。
 */
object AEPatternRefresher {

    private const val TASK_CHUNK_SIZE = 10 // 每次处理的任务数量
    private const val DELAY_BETWEEN_CHUNKS_MS = 100L // 每批任务之间的延迟（毫秒）

    /**
     * 异步刷新指定AE2网络中的所有样板提供者。
     *
     * @param grid 目标AE2网络。
     * @return 一个Kotlin Flow，它会随刷新进度发出[RefreshProgress]状态。
     *         调用者可以收集(collect)这个Flow来更新UI，例如进度条。
     */
    fun refreshAllPatternsAsync(grid: IGrid): Flow<RefreshProgress> = flow {
        val refreshTasks = mutableListOf<Runnable>()

        grid.getActiveMachines(PatternProviderBlockEntity::class.java).forEach {
            refreshTasks.add(Runnable { it.logic.updatePatterns() })
        }

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

        val totalTasks = refreshTasks.size
        if (totalTasks == 0) {
            emit(RefreshProgress(0, 0))
            return@flow
        }

        emit(RefreshProgress(0, totalTasks))

        refreshTasks.chunked(TASK_CHUNK_SIZE).forEachIndexed { index, chunk ->
            chunk.forEach { it.run() }
            val completed = ((index + 1) * TASK_CHUNK_SIZE).coerceAtMost(totalTasks)
            emit(RefreshProgress(completed, totalTasks))
            delay(DELAY_BETWEEN_CHUNKS_MS)
        }
    }.flowOn(Dispatchers.Default)
}

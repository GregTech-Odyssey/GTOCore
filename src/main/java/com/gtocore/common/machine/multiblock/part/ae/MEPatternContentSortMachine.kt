package com.gtocore.common.machine.multiblock.part.ae

import com.gtocore.api.gui.graphic.helper.ProgressBarColorStyle
import com.gtocore.api.gui.ktflexible.progressBar
import com.gtocore.api.gui.ktflexible.textBlock

import net.minecraft.network.chat.Component
import net.minecraft.server.TickTask
import net.minecraft.world.entity.player.Player

import appeng.api.networking.IManagedGridNode
import appeng.api.stacks.AEItemKey
import appeng.blockentity.crafting.PatternProviderBlockEntity
import appeng.me.Grid
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder
import com.gtolib.api.annotation.Scanned
import com.gtolib.api.annotation.language.RegisterLanguage
import com.gtolib.api.gui.ktflexible.button
import com.gtolib.api.gui.ktflexible.root
import com.gtolib.api.machine.feature.IMetaMachine
import com.gtolib.mixin.ae2.GridAccessor
import com.lowdragmc.lowdraglib.gui.modular.ModularUI
import com.lowdragmc.lowdraglib.gui.widget.PhantomSlotWidget
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer
import com.lowdragmc.lowdraglib.misc.ItemTransferList
import com.lowdragmc.lowdraglib.side.item.IItemTransfer
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

import java.util.concurrent.ConcurrentHashMap

@Scanned
class MEPatternContentSortMachine(holder: IMachineBlockEntity) :
    MetaMachine(holder),
    IFancyUIMachine,
    IGridConnectedMachine,
    IMetaMachine {
    @Scanned
    companion object {
        val manager = ManagedFieldHolder(MEPatternContentSortMachine::class.java, MANAGED_FIELD_HOLDER)
        const val PAGE_WIDTH = 276
        const val PAGE_HEIGHT = 166

        @RegisterLanguage(cn = "应用", en = "Apply")
        var TOOLTIPS_APPLY: String = "gtocore.tooltip.pattern_content_sort_machine.apply"

        @RegisterLanguage(cn = "    每行为一组，样板内所有此行物品输入会被替换为此行优先级最高的物品，每行物品优先级依据物品优先级从高到低排序", en = "    For each line, all items in the pattern input will be replaced with the highest priority item of that line, Prioritization of items in each row is based on item priority in descending order.")
        var TOOLTIPS_MEANS_FOR_LINE_0: String = "gtocore.tooltip.pattern_content_sort_machine.means_for_line_0"
    }

    override fun getFieldHolder() = manager

    @DescSynced
    var isGridOnline: Boolean = false

    @Persisted
    var gridNodeHolder = GridNodeHolder(this)
    var isInitialize = false
    override fun isOnline() = isGridOnline
    override fun setOnline(p0: Boolean) {
        isGridOnline = p0
    }
    override fun getMainNode(): IManagedGridNode? = gridNodeHolder.mainNode
    override fun isRemote() = super<IFancyUIMachine>.isRemote

    // //////////////////////////////
    // ****** LOGIC ******//
    // //////////////////////////////
    private val internalLogic by lazy { InternalLogic() }
    abstract class InternalLogicTemplate {
        class FlowData(val finished: Int, val total: Int)
        abstract fun applyRefresh()
        abstract fun receiveFlowData(flowData: FlowData)
        abstract fun collectFlowRunner(): List<Runnable>
        abstract fun buildCoroutine(run: List<Runnable>): CoroutineScope
    }
    private inner class InternalLogic : InternalLogicTemplate() {
        var lastFlowData: FlowData? = null
        var uniqueScope: CoroutineScope? = null
        var isRefreshing: Boolean = false
        override fun applyRefresh() {
            val runnables = collectFlowRunner()
            uniqueScope?.cancel()
            uniqueScope = buildCoroutine(runnables)
        }

        override fun receiveFlowData(flowData: FlowData) {
            lastFlowData = flowData
        }

        override fun collectFlowRunner(): List<Runnable> {
            val runnable = mutableListOf<Runnable>()
            if (gridNodeHolder.mainNode?.isActive == false) return runnable
            val grid = gridNodeHolder.mainNode?.grid ?: return runnable
            grid.getActiveMachines(PatternProviderBlockEntity::class.java).forEach {
                runnable.add { it.logic.updatePatterns() }
            }
            if (grid is Grid && grid is GridAccessor) {
                val machinesPart = mutableListOf<MEPatternPartMachineKt<*>>()
                grid.machines.forEach { _, node ->
                    if (node.isActive) {
                        val logicHost = node.owner
                        if (logicHost is MEPatternPartMachineKt<*>) {
                            machinesPart.plusAssign(logicHost)
                        }
                    }
                }
                machinesPart.forEach { s ->
                    runnable.add {
                        (0 until s.maxPatternCount).forEach {
                            if (!s.internalPatternInventory.getStackInSlot(it).isEmpty) {
                                s.onPatternChange(it)
                            }
                        }
                    }
                }
            }
            return runnable
        }

        override fun buildCoroutine(run: List<Runnable>): CoroutineScope {
            val scope = CoroutineScope(Dispatchers.Default)
            flow {
                val total = run.size
                val block = 5
                run.chunked(block).forEachIndexed { blockIndex, runnables ->
                    runnables.forEach { it.run() }
                    val finished = ((blockIndex + 1) * block).coerceAtMost(total)
                    emit(FlowData(finished, total))
                    delay(100)
                }
            }.onEach {
                receiveFlowData(it)
            }.onStart {
                isRefreshing = true
            }.onCompletion {
                isRefreshing = false
            }.launchIn(scope)
            return scope
        }
    }
    val externalLogic by lazy { ExternalLogic() }
    abstract class ExternalLogicTemplate {
        abstract fun getItemStackReplaced(stack: AEItemKey): AEItemKey
    }
    inner class ExternalLogic : ExternalLogicTemplate() {
        private val lastAccessTime = Object2LongOpenHashMap<AEItemKey>()

        // 物品->首选物品映射表
        val itemToPreferred: ConcurrentHashMap<AEItemKey, AEItemKey> = ConcurrentHashMap()

        // 物品->物品排序从高到低映射表
        val itemToSort: Object2ObjectMap<AEItemKey, ObjectArrayList<AEItemKey>> = Object2ObjectOpenHashMap()

        override fun getItemStackReplaced(stack: AEItemKey): AEItemKey {
            itemToPreferred[stack]?.let {
                lastAccessTime[stack] = System.currentTimeMillis()
                return it
            }
            val sortStacks = itemToSort[stack]
            if (sortStacks != null && sortStacks.isNotEmpty()) {
                val preferred = sortStacks.first()
                itemToPreferred[stack] = preferred
                lastAccessTime[stack] = System.currentTimeMillis()
                return preferred
            }

            itemToPreferred[stack] = stack
            lastAccessTime[stack] = System.currentTimeMillis()
            return stack
        }

        fun fullyRefresh() {
            itemToPreferred.clear()
            itemToSort.clear()
            itemTransferList.transfers.forEach { transfer ->
                val sortStacks = ObjectArrayList<AEItemKey>()
                val validStacks = (0 until transfer.slots)
                    .mapNotNull { transfer.getStackInSlot(it) }
                    .filterNot { it.isEmpty }
                    .sortedByDescending { it.count }
                    .mapNotNull { AEItemKey.of(it) }
                sortStacks.addAll(validStacks)

                validStacks.forEach { aek ->
                    itemToSort[aek] = sortStacks
                }
            }
        }
    }

    override fun onLoad() {
        super.onLoad()
        subscribeServerTick {
            if (!isRemote && !isInitialize && offsetTimer % 20 == 0L && gridNodeHolder.mainNode.isActive) {
                level?.server?.tell(
                    TickTask(40) {
                        externalLogic.fullyRefresh()
                        internalLogic.applyRefresh()
                    },
                )
                isInitialize = true
            }
        }
    }

    override fun onUnload() {
        super.onUnload()
        internalLogic.uniqueScope?.cancel()
    }
    // //////////////////////////////
    // ****** DATA ******//
    // //////////////////////////////

    @Persisted
    @DescSynced
    var itemTransferList: ItemTransferList = MyItemTransferList(*Array(10) { ItemStackTransfer(10) })
    class MyItemTransferList(vararg transfers: IItemTransfer) :
        ItemTransferList(*transfers),
        IContentChangeAware {
        var onContentChanged: Runnable? = null
        override fun setOnContentsChanged(a: Runnable?) {
            onContentChanged = a
        }
        override fun getOnContentsChanged(): Runnable? = Runnable {
            onContentsChanged()
            onContentChanged?.run()
        }
    }

    // //////////////////////////////
    // ****** UI ******//
    // //////////////////////////////

    class MyFancyMachineUIWidget(mainPage: IFancyUIProvider, width: Int, height: Int) : FancyMachineUIWidget(mainPage, width, height) {
        fun freshCurrentPage() {
            setupFancyUI(currentPage)
        }
    }
    var myFancyMachineUIWidget: MyFancyMachineUIWidget? = null

    override fun createUI(entityPlayer: Player?): ModularUI? = (ModularUI(PAGE_WIDTH, 166, this, entityPlayer)).widget(MyFancyMachineUIWidget(this, PAGE_WIDTH, 166).also { myFancyMachineUIWidget = it })

    override fun createUIWidget(): Widget = root(PAGE_WIDTH, PAGE_HEIGHT) {
        vScroll(width = availableWidth, height = availableHeight, { spacing = 2 }) {
            hBox(height = 14, { spacing = 2 }) {
                progressBar(
                    currentSupplier = { internalLogic.lastFlowData?.finished ?: 0 },
                    totalSupplier = { internalLogic.lastFlowData?.total ?: 1 },
                    width = this@vScroll.availableWidth - 50 - 2,
                    height = 14,
                    textColor = 0xFFFFFFFF.toInt(),
                    progressColorStyle = ProgressBarColorStyle.Gradient(0xFF33CC33.toInt(), 0xFF55CC55.toInt()),
                )
                button(width = 50, height = 14, transKet = TOOLTIPS_APPLY, onClick = { ck -> internalLogic.applyRefresh() })
            }
            textBlock(maxWidth = availableWidth, textSupplier = { Component.translatable(TOOLTIPS_MEANS_FOR_LINE_0) })
            itemTransferList.transfers.forEach { transfer ->
                vBox(width = availableWidth, alwaysHorizonCenter = true) {
                    hBox(height = 18) {
                        (0 until transfer.slots).forEach { slotIndexInHandler ->
                            widget(
                                PhantomSlotWidget(transfer, slotIndexInHandler, 0, 0).apply {
                                    setChangeListener {
                                        externalLogic.fullyRefresh()
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

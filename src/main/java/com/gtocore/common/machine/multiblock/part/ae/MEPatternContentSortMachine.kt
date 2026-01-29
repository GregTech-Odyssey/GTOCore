package com.gtocore.common.machine.multiblock.part.ae

import com.gtocore.api.gui.helper.ProgressBarColorStyle
import com.gtocore.api.gui.ktflexible.progressBar
import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.common.machine.multiblock.part.ae.MEPatternContentSortMachine.MODE.FLUID
import com.gtocore.common.machine.multiblock.part.ae.MEPatternContentSortMachine.MODE.ITEM
import com.gtocore.utils.AEKeySubstitutionMap
import com.gtocore.utils.AEPatternRefresher

import net.minecraft.network.chat.Component
import net.minecraft.server.TickTask
import net.minecraft.world.entity.player.Player
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.fluids.capability.IFluidHandler

import appeng.api.networking.IManagedGridNode
import appeng.api.stacks.AEFluidKey
import appeng.api.stacks.AEItemKey
import appeng.api.stacks.AEKey
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.TickableSubscription
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder
import com.gtolib.api.annotation.DataGeneratorScanned
import com.gtolib.api.annotation.language.RegisterLanguage
import com.gtolib.api.gui.ktflexible.button
import com.gtolib.api.gui.ktflexible.rootFresh
import com.lowdragmc.lowdraglib.gui.modular.ModularUI
import com.lowdragmc.lowdraglib.gui.widget.PhantomSlotWidget
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer
import com.lowdragmc.lowdraglib.misc.ItemTransferList
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@DataGeneratorScanned
class MEPatternContentSortMachine(holder: MetaMachineBlockEntity) :
    MetaMachine(holder),
    IFancyUIMachine,
    IGridConnectedMachine {
    @DataGeneratorScanned
    companion object {
        const val PAGE_WIDTH = 276
        const val PAGE_HEIGHT = 166

        @RegisterLanguage(cn = "应用", en = "Apply")
        var TOOLTIPS_APPLY: String = "gtocore.tooltip.pattern_content_sort_machine.apply"

        @RegisterLanguage(cn = "    每行为一组，样板内所有此行物品输入会被替换为此行优先级最高的物品，每行物品优先级依据物品优先级从左开始高到低排序，如果一行内有流体容器，则对内部流体做相应替换，左侧优先级最高", en = "    For each line, all items in the pattern input will be replaced with the highest priority item of that line, Prioritization of items in each row is based on item priority in descending order.")
        var TOOLTIPS_MEANS_FOR_LINE_0: String = "gtocore.tooltip.pattern_content_sort_machine.means_for_line_0"

        @RegisterLanguage(cn = "模式 : 流体替换", en = "Mode : Fluid Replacement")
        const val mode_fluid = "gtocore.tooltip.pattern_content_sort_machine.mode.fluid"

        @RegisterLanguage(cn = "模式 : 物品替换", en = "Mode : Item Replacement")
        const val mode_item = "gtocore.tooltip.pattern_content_sort_machine.mode.item"
    }

    @DescSynced
    var isGridOnline: Boolean = false

    private var tickableSubscription: TickableSubscription? = null

    @Persisted
    var gridNodeHolder = GridNodeHolder(this)
    var isInitialize = false
    override fun isOnline() = isGridOnline
    override fun setOnline(p0: Boolean) {
        isGridOnline = p0
    }
    var freshUI: Runnable = Runnable {}
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
    }
    private inner class InternalLogic : InternalLogicTemplate() {
        var lastFlowData: FlowData? = null
        var uniqueScope: CoroutineScope? = null
        var isRefreshing: Boolean = false
        override fun applyRefresh() {
            uniqueScope?.cancel()
            val grid = gridNodeHolder.mainNode?.grid ?: return
            uniqueScope = CoroutineScope(Dispatchers.Default)

            AEPatternRefresher.refreshAllPatternsAsync(grid)
                .onStart {
                    isRefreshing = true
                    // 初始化进度条
                    receiveFlowData(FlowData(0, 1))
                }
                .onEach { progress ->
                    receiveFlowData(FlowData(progress.completed, progress.total))
                }
                .onCompletion {
                    isRefreshing = false
                    lastFlowData?.let {
                        receiveFlowData(FlowData(it.total, it.total))
                    }
                }
                .launchIn(uniqueScope!!)
        }

        override fun receiveFlowData(flowData: FlowData) {
            lastFlowData = flowData
        }
    }

    val externalLogic by lazy { ExternalLogic() }
    abstract class ExternalLogicTemplate {
        abstract fun getAEKeyReplaced(stack: AEKey): AEKey
    }
    inner class ExternalLogic : ExternalLogicTemplate() {

        @Volatile
        private var substitutionMap = AEKeySubstitutionMap.EMPTY

        // TODO 使用PatternContentAccessTerminalPart获取(grid.getMachines(PatternContentAccessTerminalPart.class))
        override fun getAEKeyReplaced(stack: AEKey): AEKey = substitutionMap.getSubstitution(stack)

        fun fullyRefresh() {
            val priorityGroups = mutableListOf<List<AEKey>>()

            itemTransferList.transfers.forEach { transfer ->
                transfer as MyItemStackTransfer
                val priorityGroup = ObjectArrayList<AEKey>()
                val itemStacks = (0 until transfer.slots)
                    .mapNotNull { transfer.getStackInSlot(it) }
                    .filterNot { it.isEmpty }

                var currentMode = ITEM
                // 只要行内有一个流体容器，整行都按流体处理
                for (stack in itemStacks) {
                    stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent {
                        if (!it.drain(Int.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE).isEmpty) {
                            currentMode = FLUID
                        }
                    }
                    if (currentMode == FLUID) break
                }
                transfer.mode = currentMode

                when (transfer.mode) {
                    FLUID -> {
                        val fluids = itemStacks
                            .mapNotNull { stack ->
                                val cap = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().orElse(null)
                                cap?.drain(Int.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE)
                            }
                            .mapNotNull { AEFluidKey.of(it) }
                        priorityGroup.addAll(fluids)
                    }
                    ITEM -> {
                        val items = itemStacks.mapNotNull { AEItemKey.of(it) }
                        priorityGroup.addAll(items)
                    }
                }

                if (priorityGroup.isNotEmpty()) {
                    priorityGroups.add(priorityGroup)
                }
            }
            // 原子性替换
            this.substitutionMap = AEKeySubstitutionMap(priorityGroups)
        }
    }

    override fun onLoad() {
        super.onLoad()
        // Wire content-changed hooks so any ghost insert/remove persists immediately.
        if (itemTransferList is MyItemTransferList) {
            (itemTransferList as MyItemTransferList).onContentsChanged = Runnable {
                externalLogic.fullyRefresh()
                if (!isRemote) requestSync() else freshUI.run()
            }
        }
        itemTransferList.transfers.forEach { t ->
            if (t is MyItemStackTransfer) {
                t.onContentsChanged = Runnable {
                    externalLogic.fullyRefresh()
                    if (!isRemote) requestSync() else freshUI.run()
                }
            }
        }
        tickableSubscription = subscribeServerTick(tickableSubscription, {
            if (!isRemote && !isInitialize && gridNodeHolder.mainNode.isActive) {
                level?.server?.tell(
                    TickTask(40) {
                        externalLogic.fullyRefresh()
                        internalLogic.applyRefresh()
                    },
                )
                isInitialize = true
            }
        }, 20)
    }

    override fun onUnload() {
        super.onUnload()
        internalLogic.uniqueScope?.cancel()
        if (tickableSubscription != null) {
            tickableSubscription?.unsubscribe()
            tickableSubscription = null
        }
    }

    // //////////////////////////////
    // ****** DATA ******//
    // //////////////////////////////
    enum class MODE {
        FLUID,
        ITEM,
    }

    @Persisted
    @DescSynced
    var itemTransferList: ItemTransferList = MyItemTransferList(*Array(10) { MyItemStackTransfer(10) })
    private inner class MyItemStackTransfer(size: Int, var mode: MODE = ITEM) :
        ItemStackTransfer(size),
        IContentChangeAware {
        private var change: Runnable? = null
        override fun setOnContentsChanged(a: Runnable?) {
            change = a
        }
        override fun getOnContentsChanged(): Runnable? = change
        override fun onContentsChanged(slot: Int) {
            super.onContentsChanged(slot)
            externalLogic.fullyRefresh()
            if (!isRemote) requestSync() else freshUI.run()
            change?.run()
        }
    }
    private class MyItemTransferList(vararg transfers: MyItemStackTransfer) :
        ItemTransferList(*transfers),
        IContentChangeAware {
        var onContentChanged: Runnable? = null
        override fun setOnContentsChanged(a: Runnable?) {
            onContentChanged = a
        }
        override fun getOnContentsChanged(): Runnable = Runnable {
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

    override fun createUIWidget(): Widget = rootFresh(PAGE_WIDTH, PAGE_HEIGHT) {
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
                transfer as MyItemStackTransfer
                vBox(width = availableWidth, style = { spacing = 4 }) {
                    vBox(width = availableWidth, alwaysHorizonCenter = true) {
                        textBlock(maxWidth = availableWidth, textSupplier = {
                            when (transfer.mode) {
                                FLUID -> Component.translatable(mode_fluid)
                                ITEM -> Component.translatable(mode_item)
                            }
                        })
                        hBox(height = 18) {
                            (0 until transfer.slots).forEach { slotIndexInHandler ->
                                widget(
                                    PhantomSlotWidget(transfer, slotIndexInHandler, 0, 0).apply {
                                        setChangeListener {
                                            externalLogic.fullyRefresh()
                                            if (!isRemote) requestSync()
                                            if (isRemote) freshUI.run()
                                        }
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }.also { freshUI = Runnable { it.requireFresh() } }
}

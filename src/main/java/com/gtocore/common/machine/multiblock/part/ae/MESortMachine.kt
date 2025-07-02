package com.gtocore.common.machine.multiblock.part.ae

import com.gtocore.common.machine.multiblock.part.ae.MESortMachine.SortType.DEFAULT
import com.gtocore.common.machine.multiblock.part.ae.MESortMachine.SortType.TAG

import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.network.chat.Component
import net.minecraft.server.TickTask
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraftforge.items.IItemHandlerModifiable
import net.minecraftforge.items.ItemStackHandler

import appeng.api.networking.IManagedGridNode
import appeng.api.networking.security.IActionSource
import appeng.api.stacks.AEItemKey
import appeng.blockentity.crafting.PatternProviderBlockEntity
import appeng.me.Grid
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget
import com.gregtechceu.gtceu.api.gui.widget.PhantomSlotWidget
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder
import com.gtolib.api.annotation.Scanned
import com.gtolib.api.annotation.language.RegisterLanguage
import com.gtolib.api.gui.ktflexible.button
import com.gtolib.api.gui.ktflexible.field
import com.gtolib.api.gui.ktflexible.root
import com.gtolib.api.gui.ktflexible.text
import com.gtolib.api.gui.ktflexible.vBox
import com.gtolib.api.gui.ktflexible.vBoxThreeColumn
import com.gtolib.mixin.ae2.GridAccessor
import com.lowdragmc.lowdraglib.gui.modular.ModularUI
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import kotlinx.coroutines.Runnable

import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Predicate
import java.util.function.Supplier

@Scanned
class MESortMachine :
    MetaMachine,
    IGridConnectedMachine,
    IFancyUIMachine {
    constructor(holder: IMachineBlockEntity) : super(holder) {
    }

    @Scanned
    companion object {
        @RegisterLanguage(cn = "ME样板内容动态修改机", en = "ME Pattern Content Dynamic Editor Machine")
        val usingTooltips: String = "gtocore.gui.me_sort.using_tooltips"

        @RegisterLanguage(cn = "根据配置的同标签物品的个数确定优先级", en = "Determine priority based on the number of items with the same tag")
        val usingTooltips1: String = "gtocore.gui.me_sort.using_tooltips_1"

        @RegisterLanguage(cn = "上移", en = "Move Up")
        val moveUp: String = "gtocore.gui.me_sort.move_up"

        @RegisterLanguage(cn = "下移", en = "Move Down")
        val moveDown: String = "gtocore.gui.me_sort.move_down"

        @RegisterLanguage(cn = "删除", en = "Delete")
        val delete: String = "gtocore.gui.me_sort.delete"

        @RegisterLanguage(cn = "应用", en = "Apply")
        val apply: String = "gtocore.gui.me_sort.apply"

        @RegisterLanguage(cn = "添加", en = "Add")
        val add: String = "gtocore.gui.me_sort.add"
        const val PAGE_WIDTH = 276
        const val PAGE_HEIGHT = 166
        val MANAGED_FIELD_HOLDER: ManagedFieldHolder =
            ManagedFieldHolder(MESortMachine::class.java, MetaMachine.MANAGED_FIELD_HOLDER)
    }

    override fun getFieldHolder() = MANAGED_FIELD_HOLDER

    // //////////////////////////////
    // ****** AE ******//
    // //////////////////////////////
    @Persisted
    var gridNodeHolder: GridNodeHolder = GridNodeHolder(this).also {
        it.mainNode.setExposedOnSides(EnumSet.allOf(Direction::class.java))
    }

    val actionSource: IActionSource = IActionSource.ofMachine(gridNodeHolder.mainNode::getNode)

    @DescSynced
    var isinOnline: Boolean = false
    override fun isOnline(): Boolean = isinOnline
    override fun setOnline(online: Boolean): Unit = run { isinOnline = online }
    override fun getMainNode(): IManagedGridNode? = gridNodeHolder.mainNode

    // 内部存储
    enum class SortType {
        TAG,
        DEFAULT,
    }

    class MyPhantomSlotWidget : PhantomSlotWidget {
        constructor(
            itemHandler: IItemHandlerModifiable?,
            slotIndex: Int,
            xPosition: Int,
            yPosition: Int,
            validator: Predicate<ItemStack?>?,
        ) : super(itemHandler, slotIndex, xPosition, yPosition, validator)
    }

    abstract class Line :
        ITagSerializable<CompoundTag>,
        IContentChangeAware {
        var widgets: MutableList<MyPhantomSlotWidget> = mutableListOf()
    }
    // //////////////////////////////
    // ****** 刷新其他机器缓存 初始化 ******//
    // //////////////////////////////

    fun freshOtherMachineCache() {
        gridNodeHolder.mainNode.grid?.let { grid ->
            val machinesPattern = grid.getActiveMachines(PatternProviderBlockEntity::class.java)
            machinesPattern.forEach { it.logic.updatePatterns() }
            if (grid is Grid && grid is GridAccessor) {
                val machinesPart = mutableListOf<MEPatternPartMachine<*>>()
                grid.machines.forEach { _, node ->
                    if (node.isActive) {
                        val logicHost = node.owner
                        if (logicHost is MEPatternPartMachine<*>) {
                            machinesPart.plusAssign(logicHost)
                        }
                    }
                }
                machinesPart.forEach { s ->
                    (0 until s.maxPatternCount).forEach {
                        if (!s.internalPatternInventory.getStackInSlot(it).isEmpty) {
                            s.onPatternChange(it)
                        }
                    }
                }
            }
        }
    }
    var isInitialize = false

    init {
        subscribeServerTick {
            if (!isRemote && !isInitialize && offsetTimer % 20 == 0L && gridNodeHolder.mainNode.isActive) {
                level?.server?.tell(
                    TickTask(initializationGap) {
                        meSortMachineLogic.fullyRefresh()
                        freshOtherMachineCache()
                    },
                )
                isInitialize = true
            }
        }
    }

    // //////////////////////////////
    // ****** 数据结构 ******//
    // //////////////////////////////
    @Persisted
    @DescSynced
    var initializationGap: Int = 40

    @Persisted
    @DescSynced
    var tagLineList = LineContainer<TagLine> { TagLine(0) }
    inner class LineContainer<T : Line>(val factory: () -> T) :
        ITagSerializable<CompoundTag>,
        IContentChangeAware {
        var lists: MutableList<T> = mutableListOf()
        override fun serializeNBT(): CompoundTag? {
            val tag = CompoundTag()
            val listTag = ListTag()
            for (line in lists) {
                listTag.add(line.serializeNBT())
            }
            tag.put("lists", listTag)
            return tag
        }
        override fun deserializeNBT(nbt: CompoundTag?) {
            if (nbt != null) {
                val listTag = nbt.getList("lists", 10)
                for (i in 0 until listTag.size) {
                    lists.add(factory().apply { deserializeNBT(listTag.getCompound(i)) })
                }
            }
        }
        override fun setOnContentsChanged(onContentChanged: Runnable) {}
        override fun getOnContentsChanged() = Runnable {}
    }

    inner class TagLine :
        Line,
        ITagSerializable<CompoundTag>,
        IContentChangeAware {
        constructor(slotsNum: Int) {
            slots = slotsNum
        }

        fun getSortStacks(): List<AEItemKey> = getItemStacks()
            .distinctBy { it.item }
            .filterNot { it.isEmpty }
            .sortedByDescending { it.count }
            .mapNotNull { AEItemKey.of(it) }
        fun getItemStacks(): List<ItemStack> = widgets.mapNotNull { it.item }
        fun getItemStackTagList(stack: ItemStack): Set<TagKey<Item>> = stack.tags.toList().toSet()
        fun refreshAllowTags() {
            if (getItemStacks().none { !it.isEmpty }) {
                allowTags = emptySet()
                return
            }

            allowTags = getItemStacks().asSequence()
                .filterNot { it.isEmpty }
                .map { getItemStackTagList(it) }
                .reduceOrNull { acc, tags -> acc intersect tags } ?: emptySet()
        }

        var slots: Int = 0
            set(value) {
                widgets.clear()
                (0 until value).forEach { _ ->
                    widgets.add(
                        MyPhantomSlotWidget(ItemStackHandler(1), 0, 0, 0) { stack ->
                            stack?.run {
                                if (stack.tags.toList()?.isEmpty() ?: true) return@run false
                                if (allowTags.isEmpty()) return@run true
                                return@run stack.tags.toList().any { tag -> allowTags.contains(tag) }
                            } ?: false
                        }.also {
                            it.setChangeListener {
                                refreshAllowTags()
                                meSortMachineLogic.fullyRefresh()
                            }
                        },
                    )
                }
                field = value
            }
        var allowTags: Set<TagKey<Item>> = setOf()
        override fun serializeNBT(): CompoundTag? {
            val root = CompoundTag()
            root.putInt("slots", slots)
            val stackList = ListTag()
            for (stack in getItemStacks()) {
                stackList.add(stack.serializeNBT())
            }
            root.put("stacks", stackList)
            return root
        }

        override fun deserializeNBT(nbt: CompoundTag?) {
            if (nbt != null) {
                slots = nbt.getInt("slots")
                val stackList = nbt.getList("stacks", 10)
                for (i in 0 until stackList.size) {
                    widgets[i].item = ItemStack.of(stackList.getCompound(i))
                }
                refreshAllowTags()
            }
        }

        override fun setOnContentsChanged(onContentChanged: Runnable?) {}

        override fun getOnContentsChanged() = Runnable { }
    }

    // //////////////////////////////
    // ****** 逻辑和缓存 ******//
    // //////////////////////////////

    val meSortMachineLogic: MESortMachineLogic by lazy { MESortMachineLogic() }
    abstract class MESortMachineLogicTemplate {
        // 物品->首选物品映射表
        val itemToPreferred: ConcurrentHashMap<AEItemKey, AEItemKey> = ConcurrentHashMap()

        // 物品->物品排序从高到低映射表
        val itemToSort: Object2ObjectMap<AEItemKey, ObjectArrayList<AEItemKey>> = Object2ObjectOpenHashMap()

        abstract fun getItemStackReplaced(stack: AEItemKey): AEItemKey
        abstract fun fullyRefresh()
    }

    inner class MESortMachineLogic : MESortMachineLogicTemplate() {
        private val lastAccessTime = Object2LongOpenHashMap<AEItemKey>()

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

        override fun fullyRefresh() {
            itemToPreferred.clear()
            itemToSort.clear()
            tagLineList.lists.forEach { line ->
                val sortStacks = ObjectArrayList<AEItemKey>()
                val validStacks = line.getSortStacks()
                sortStacks.addAll(validStacks)

                validStacks.forEach { aek ->
                    itemToSort[aek] = sortStacks
                }
            }
        }
    }

    // //////////////////////////////
    // ****** UI ******//
    // //////////////////////////////
    val fancyMachineUIWidget = MyFancyMachineUIWidget(this, PAGE_WIDTH, PAGE_HEIGHT)

    override fun attachSideTabs(sideTabs: TabsWidget?) {
        sideTabs?.apply {
            mainTab = this@MESortMachine
            attachSubTab(SubPage(TAG))
        }
    }

    class MyFancyMachineUIWidget(mainPage: IFancyUIProvider, width: Int, height: Int) :
        FancyMachineUIWidget(
            mainPage,
            width,
            height,
        ) {
        fun openSetupUI(fancyUI: IFancyUIProvider?) {
            setupFancyUI(fancyUI)
        }
    }

    override fun createUIWidget(): WidgetGroup = vBox(width = PAGE_WIDTH) {}
    override fun createUI(entityPlayer: Player?): ModularUI? = ModularUI(PAGE_WIDTH, PAGE_HEIGHT, this, entityPlayer).widget(fancyMachineUIWidget)
    override fun isRemote(): Boolean = super<IFancyUIMachine>.isRemote
    inner class SubPage(val sortType: SortType) : IFancyUIProvider {
        override fun getTabIcon(): IGuiTexture? = ItemStackTexture(Items.IRON_INGOT)
        override fun getTitle(): Component? = Component.literal(sortType.name)
        override fun createMainPage(widget: FancyMachineUIWidget?) = root(PAGE_WIDTH, PAGE_HEIGHT) {
            vScroll(PAGE_WIDTH, PAGE_HEIGHT, { spacing = 4 }) {
                when (sortType) {
                    TAG -> {
                        hBox(height = 50, { spacing = 2 }) {
                            button(width = this@vScroll.availableWidth - 2 - 50 - 2 - 50, transKet = add, onClick = {
                                tagLineList.lists.add(TagLine(13))
                                fancyMachineUIWidget.openSetupUI(this@SubPage)
                            })
                            field(getter = { initializationGap.toString() }, setter = { initializationGap = it.toInt() })
                            button(width = 50, transKet = apply, onClick = {
                                meSortMachineLogic.fullyRefresh()
                                freshOtherMachineCache()
                            })
                        }
                        text(width = availableWidth, text = { Component.translatable(usingTooltips) }) { }
                        text(width = availableWidth, text = { Component.translatable(usingTooltips1) })
                        tagLineList.lists.forEach { line ->
                            vBoxThreeColumn(
                                availableWidth,
                                spacing = 2,
                                between = 7,
                                drawInBackgroundInit =
                                { g, _, _, _, vBox -> DrawerHelper.drawSolidRect(g, vBox.positionX + 2, vBox.positionY, 2, vBox.sizeHeight, 0xFF66CCFF.toInt()) },
                            ) {
                                hBox(16, { spacing = 2 }) {
                                    button(width = (this@vBoxThreeColumn.availableWidth - 4) / 3, transKet = moveUp, onClick = {
                                        moveItem(tagLineList.lists, line, -1)
                                        fancyMachineUIWidget.openSetupUI(this@SubPage)
                                    })
                                    button(width = (this@vBoxThreeColumn.availableWidth - 4) / 3, transKet = delete, onClick = {
                                        tagLineList.lists.remove(line)
                                        fancyMachineUIWidget.openSetupUI(this@SubPage)
                                    })
                                    button(width = (this@vBoxThreeColumn.availableWidth - 4) / 3, transKet = moveDown, onClick = {
                                        moveItem(tagLineList.lists, line, 1)
                                        fancyMachineUIWidget.openSetupUI(this@SubPage)
                                    })
                                }
                                vBox(availableWidth, { spacing = 2 }) {
                                    line.allowTags.forEach { tag ->
                                        text(width = availableWidth, text = Supplier { Component.literal(tag.location.path) }, init = {})
                                    }
                                }
                                vBox(width = availableWidth, alwaysHorizonCenter = true) {
                                    hBox(18) {
                                        line.widgets.forEach { widget ->
                                            widget(widget)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    DEFAULT -> {
                    }
                }
            }
        }
    }
    private fun <T> moveItem(list: MutableList<T>, item: T, direction: Int) {
        val index = list.indexOf(item)
        val targetIndex = index + direction
        if (targetIndex in 0 until list.size) {
            val temp = list.removeAt(index)
            list.add(targetIndex, temp)
        }
    }
}

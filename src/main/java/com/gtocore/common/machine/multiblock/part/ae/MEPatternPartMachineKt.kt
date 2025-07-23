package com.gtocore.common.machine.multiblock.part.ae

import com.gtocore.api.gui.ktflexible.multiPageAdvanced
import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.common.data.machines.GTAEMachines
import com.gtocore.common.machine.multiblock.part.ae.widget.slot.AEPatternViewSlotWidgetKt
import com.gtocore.common.network.IntSyncField
import com.gtocore.common.network.createLogicalSide

import net.minecraft.MethodsReturnNonnullByDefault
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.TickTask
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

import appeng.api.crafting.IPatternDetails
import appeng.api.implementations.blockentities.PatternContainerGroup
import appeng.api.inventories.InternalInventory
import appeng.api.networking.IGrid
import appeng.api.networking.IGridNodeListener
import appeng.api.networking.crafting.ICraftingProvider
import appeng.api.stacks.AEItemKey
import appeng.api.stacks.KeyCounter
import appeng.crafting.pattern.EncodedPatternItem
import appeng.helpers.patternprovider.PatternContainer
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.gregtechceu.gtceu.api.capability.recipe.IO
import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.TickableSubscription
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler
import com.gtolib.ae2.MyPatternDetailsHelper
import com.gtolib.ae2.pattern.IParallelPatternDetails
import com.gtolib.api.annotation.Scanned
import com.gtolib.api.annotation.language.RegisterLanguage
import com.gtolib.api.gui.ktflexible.*
import com.gtolib.api.gui.ktflexible.FreshWidgetGroupAbstract
import com.lowdragmc.lowdraglib.gui.modular.ModularUI
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder
import kotlinx.coroutines.Runnable

import java.util.function.IntSupplier
import javax.annotation.ParametersAreNonnullByDefault

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Scanned
internal abstract class MEPatternPartMachineKt<T : MEPatternPartMachineKt.AbstractInternalSlot>(holder: IMachineBlockEntity, val maxPatternCount: Int) :
    MEPartMachine(holder, IO.IN),
    ICraftingProvider,
    PatternContainer {

    // ==================== 常量和静态成员 ====================
    @Scanned
    companion object {
        @JvmStatic
        var MANAGED_FIELD_HOLDER = ManagedFieldHolder(
            MEPatternPartMachineKt::class.java,
            MEPartMachine.MANAGED_FIELD_HOLDER,
        )

        @RegisterLanguage(cn = "AE显示名称:", en = "AE Name:")
        val AE_NAME: String = "gtceu.ae.pattern_part_machine.ae_name"
    }

    // ==================== 持久化属性 ====================
    @Persisted
    @DescSynced
    private var patternInventory: CustomItemStackHandler = CustomItemStackHandler(maxPatternCount)

    @Persisted
    private var internalInventory: Array<AbstractInternalSlot> = createInternalSlotArray()

    @DescSynced
    @Persisted
    var customName: String = ""

    @DescSynced
    @Persisted
    private var currentSlotPage = 0

    var freshRootWidgetGroup: Runnable? = null

    // ==================== 运行时属性 ====================
    val detailsSlotMap: BiMap<IPatternDetails, T> = HashBiMap.create(maxPatternCount)

    private var patterns: List<IPatternDetails> = emptyList()
    private var needPatternSync: Boolean = false
    private var updateSubs: TickableSubscription? = null

    // ==================== 委托属性 ====================
    val internalPatternInventory by lazy {
        object : InternalInventory {
            override fun size(): Int = maxPatternCount
            override fun getStackInSlot(slotIndex: Int): ItemStack = patternInventory.getStackInSlot(slotIndex)
            override fun setItemDirect(slotIndex: Int, stack: ItemStack) {
                patternInventory.run {
                    setStackInSlot(slotIndex, stack)
                    onContentsChanged(slotIndex)
                }
                onPatternChange(slotIndex)
            }
        }
    }

    // ==================== 初始化 ====================
    init {
        patternInventory.setFilter(::patternFilter)
        internalInventory.indices.forEach { i ->
            internalInventory[i] = createInternalSlot(i)
        }
        getMainNode().addService(ICraftingProvider::class.java, this)
    }

    // ==================== 抽象方法 ====================
    abstract fun createInternalSlotArray(): Array<AbstractInternalSlot>
    abstract fun patternFilter(stack: ItemStack): Boolean
    abstract fun createInternalSlot(i: Int): T

    // ==================== 公开方法 ====================
    @Suppress("UNCHECKED_CAST")
    fun getInternalInventory(): Array<T> = internalInventory as Array<T>

    fun onPatternChange(index: Int) {
        if (isRemote()) return

        val internalInv = getInternalInventory()[index]
        val newPattern = patternInventory.getStackInSlot(index)
        val newPatternDetails = decodePattern(newPattern)
        val oldPatternDetails = detailsSlotMap.inverse()[internalInv]

        detailsSlotMap.forcePut(newPatternDetails, internalInv)

        oldPatternDetails?.takeIf { it != newPatternDetails }?.let {
            internalInv.onPatternChange()
        }

        updatePatterns()
    }

    // ==================== 扩展钩子方法 ====================
    open fun appendHoverTooltips(index: Int): Component? = null
    open fun onMouseClicked(index: Int) {}
    open fun getApplyIndex(): IntSupplier = IntSupplier { -1 }
    open fun onPageNext() {}
    open fun onPagePrev() {}
    open fun runOnUpdate() {}
    open fun addWidget(group: WidgetGroup) {}

    // ==================== 生命周期方法 ====================
    var pageField = IntSyncField(createLogicalSide(isRemote), { "$pos-page" }, 0) // 前面必须用var 刷新UI必须在客户端刷新，服务端依同步刷新
    override fun onLoad() {
        super.onLoad()
        when (val level = getLevel()) {
            is ServerLevel -> {
                level.server.tell(
                    TickTask(1) {
                        (0 until patternInventory.slots).forEach { i ->
                            val pattern = patternInventory.getStackInSlot(i)
                            decodePattern(pattern)?.let { patternDetails ->
                                detailsSlotMap[patternDetails] = getInternalInventory()[i]
                            }
                        }
                        updatePatterns()
                    },
                )
            }
        }
    }

    override fun onUnload() {
        pageField.unregister()
        super.onUnload()
    }

    override fun onMachineRemoved() {
        clearInventory(patternInventory)
    }

    override fun onMainNodeStateChanged(reason: IGridNodeListener.State) {
        super.onMainNodeStateChanged(reason)
        updateSubscription()
    }

    override fun getFieldHolder(): ManagedFieldHolder = MANAGED_FIELD_HOLDER

    // ==================== ICraftingProvider 接口实现 ====================
    override fun getAvailablePatterns(): List<IPatternDetails> = patterns

    override fun pushPattern(patternDetails: IPatternDetails, inputHolder: Array<KeyCounter>): Boolean = getMainNode().takeIf { it.isActive }?.let {
        detailsSlotMap[patternDetails]?.pushPattern(patternDetails, inputHolder)
    } ?: false

    override fun isBusy(): Boolean = false

    // ==================== PatternContainer 接口实现 ====================
    override fun getTerminalPatternInventory(): InternalInventory = internalPatternInventory

    override fun getTerminalGroup(): PatternContainerGroup {
        val (itemKey, description) = when {
            isFormed() -> {
                val controller = getControllers().first()
                val controllerDefinition = controller.self().definition
                AEItemKey.of(controllerDefinition.asStack()) to
                    if (customName.isNotEmpty()) {
                        Component.literal(customName)
                    } else {
                        Component.translatable(controllerDefinition.descriptionId)
                    }
            }
            else -> {
                AEItemKey.of(GTAEMachines.ME_PATTERN_BUFFER.item) to
                    if (customName.isNotEmpty()) {
                        Component.literal(customName)
                    } else {
                        GTAEMachines.ME_PATTERN_BUFFER.get().definition.item.description
                    }
            }
        }

        return PatternContainerGroup(itemKey, description, emptyList())
    }

    // ==================== 其他接口实现 ====================
    override fun getGrid(): IGrid? = mainNode.grid

    override fun getRecipeHandlers(): List<RecipeHandlerList> = emptyList()
    override fun getHandlerList(): RecipeHandlerList = RecipeHandlerList.NO_DATA
    override fun isWorkingEnabled(): Boolean = true
    override fun setWorkingEnabled(ignored: Boolean) {}
    override fun isDistinct(): Boolean = true
    override fun setDistinct(ignored: Boolean) {}
    override fun attachConfigurators(configuratorPanel: ConfiguratorPanel) {}

    // ==================== UI 相关方法 ====================
    lateinit var freshWidgetGroup: FreshWidgetGroupAbstract
    override fun createUIWidget(): Widget {
        freshWidgetGroup = rootFresh(176, 148) {
            val chunked: List<List<List<Int>>> = (0 until maxPatternCount).chunked(9).chunked(6)
            vBox(width = availableWidth, style = { spacing = 3 }) {
                hBox(height = 12, alwaysVerticalCenter = true) {
                    blank(width = 4)
                    textBlock(maxWidth = this@vBox.availableWidth, textSupplier = {
                        when (isOnline) {
                            true -> Component.translatable("gtceu.gui.me_network.online")
                            false -> Component.translatable("gtceu.gui.me_network.offline")
                        }
                    })
                    blank(width = 4)
                    textBlock(maxWidth = this@vBox.availableWidth, textSupplier = {
                        Component.translatable(AE_NAME)
                    })
                    field(height = 12, getter = { customName }, setter = { customName = it })
                }
                val height1 = this@rootFresh.availableHeight - 24 - 16
                val pageWidget =
                    multiPageAdvanced(width = this@vBox.availableWidth, runOnUpdate = ::runOnUpdate, height = height1, pageSelector = pageField) {
                        chunked.forEach { pageIndices ->
                            page {
                                vScroll(width = this@vBox.availableWidth, height = height1) {
                                    vBox(width = this@vBox.availableWidth, alwaysHorizonCenter = true) {
                                        buildToolBoxContent()
                                        pageIndices.forEach { lineIndices ->
                                            hBox(height = 18) {
                                                lineIndices.forEach { index ->
                                                    widget(createPatternSlot(index))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                hBox(height = 13, style = { spacing = 2 }) {
                    val wid = this@vBox.availableWidth - 2 * 2
                    button(
                        width = 30,
                        height = 13,
                        onClick = { ck ->
                            onPagePrev()
                            if (isRemote)pageField.updateInClient((pageField.value - 1).coerceAtLeast(0))
                        },
                        text = { "<<" },
                    )
                    text(height = 13, width = wid - 60, text = { Component.literal("${pageField.value + 1} / ${pageWidget.getMaxPage() + 1}") })
                    button(
                        height = 13,
                        width = 30,
                        onClick = { ck ->
                            onPageNext()
                            if (isRemote)pageField.updateInClient((pageField.value + 1).coerceAtLeast(pageWidget.getMaxPage()))
                        },
                        text = { ">>" },
                    )
                }
                pageWidget.refresh()
            }
        }
        return freshWidgetGroup
    }

    // ==================== 私有辅助方法 ====================
    private fun updatePatterns() {
        patterns = detailsSlotMap.keys.filterNotNull()
        needPatternSync = true
    }

    private fun decodePattern(stack: ItemStack): IPatternDetails? = IParallelPatternDetails.of(
        MyPatternDetailsHelper.decodePattern(stack, holder.self, getGrid()),
        getLevel(),
        1,
    )

    private fun updateSubscription() {
        when {
            getMainNode().isOnline -> {
                updateSubs = subscribeServerTick(updateSubs, ::update)
            }
            updateSubs != null -> {
                updateSubs?.unsubscribe()
                updateSubs = null
            }
        }
    }

    private fun update() {
        if (needPatternSync) {
            ICraftingProvider.requestUpdate(getMainNode())
            needPatternSync = false
        }
    }

    private fun createPatternSlot(index: Int): AEPatternViewSlotWidgetKt {
        val slot = AEPatternViewSlotWidgetKt(
            0,
            0,
            index,
            getApplyIndex(),
            patternInventory,
        ) { onMouseClicked(index) }

        slot.inner.setOccupiedTexture(GuiTextures.SLOT)
        slot.inner.setItemHook { stack ->
            when (val item = stack.item) {
                is EncodedPatternItem -> {
                    val output = item.getOutput(stack)
                    if (!output.isEmpty) output else stack
                }
                else -> stack
            }
        }
        slot.inner.setChangeListener { onPatternChange(index) }
        slot.inner.setOnAddedTooltips { _, tooltips ->
            appendHoverTooltips(index)?.let { tooltips.add(it) }
        }
        slot.inner.setBackground(GuiTextures.SLOT, GuiTextures.PATTERN_OVERLAY)

        return slot
    }
    open fun VBoxBuilder.buildToolBoxContent() {}
    class MyFancyMachineUIWidget(mainPage: IFancyUIProvider, width: Int, height: Int) : FancyMachineUIWidget(mainPage, width, height) {
        fun setup(provider: IFancyUIProvider) {
            setupFancyUI(provider)
        }
    }
    var modularUI: ModularUI? = null
    var fancyMachineUIWidget: FancyMachineUIWidget? = null
    override fun createUI(entityPlayer: Player?): ModularUI? {
        fancyMachineUIWidget = FancyMachineUIWidget(this, 176, 166)
        modularUI = (ModularUI(176, 166, this, entityPlayer)).widget(fancyMachineUIWidget)
        return modularUI
    }

    override fun createMainPage(widget: FancyMachineUIWidget?): Widget? = super.createMainPage(widget)

    // ==================== 内部类 ====================
    abstract class AbstractInternalSlot :
        ITagSerializable<CompoundTag>,
        IContentChangeAware {
        abstract fun pushPattern(patternDetails: IPatternDetails, inputHolder: Array<KeyCounter>): Boolean
        abstract fun onPatternChange()
        override fun serializeNBT(): CompoundTag = CompoundTag()
    }
}

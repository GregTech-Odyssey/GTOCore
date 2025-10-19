package com.gtocore.common.machine.multiblock.part.ae

import com.gtocore.api.gui.ktflexible.multiPageAdvanced
import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.common.data.machines.GTAEMachines
import com.gtocore.common.machine.multiblock.part.ae.widget.slot.AEPatternViewSlotWidgetKt
import com.gtocore.integration.ae.WirelessMachine
import com.gtocore.integration.eio.ITravelHandlerHook

import net.minecraft.MethodsReturnNonnullByDefault
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

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
import com.enderio.base.common.travel.TravelSavedData
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity
import com.gregtechceu.gtceu.api.capability.recipe.IO
import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton
import com.gregtechceu.gtceu.api.machine.TickableSubscription
import com.gregtechceu.gtceu.api.machine.feature.IDropSaveMachine
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList
import com.gregtechceu.gtceu.api.recipe.GTRecipeType
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler
import com.gregtechceu.gtceu.utils.TaskHandler
import com.gtolib.api.ae2.MyPatternDetailsHelper
import com.gtolib.api.ae2.pattern.IParallelPatternDetails
import com.gtolib.api.annotation.DataGeneratorScanned
import com.gtolib.api.annotation.language.RegisterLanguage
import com.gtolib.api.capability.ISync
import com.gtolib.api.gui.ktflexible.*
import com.gtolib.api.machine.feature.IEnhancedRecipeLogicMachine
import com.gtolib.api.network.SyncManagedFieldHolder
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup
import com.lowdragmc.lowdraglib.gui.util.ClickData
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted

import java.util.function.BiConsumer
import java.util.function.IntSupplier
import java.util.stream.Stream
import javax.annotation.ParametersAreNonnullByDefault

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@DataGeneratorScanned
internal abstract class MEPatternPartMachineKt<T : MEPatternPartMachineKt.AbstractInternalSlot>(holder: MetaMachineBlockEntity, val maxPatternCount: Int) :
    MEPartMachine(holder, IO.IN),
    ICraftingProvider,
    WirelessMachine,
    IInteractedMachine,
    ISync,
    PatternContainer,
    IDropSaveMachine {
    override fun onUse(state: BlockState?, world: Level?, pos: BlockPos?, player: Player?, hand: InteractionHand?, hit: BlockHitResult?): InteractionResult? {
        if (!isRemote) {
            newPageField.setAndSyncToClient(newPageField.get())
        }
        return super.onUse(state, world, pos, player, hand, hit)
    }

    // ==================== 常量和静态成员 ====================
    @DataGeneratorScanned
    companion object {

        @JvmStatic
        val SYNC_MANAGED_FIELD_HOLDER = SyncManagedFieldHolder(MEPatternPartMachineKt::class.java, syncFieldHolder)

        @RegisterLanguage(cn = "AE显示名称:", en = "AE Name:")
        const val AE_NAME: String = "gtceu.ae.pattern_part_machine.ae_name"

        @RegisterLanguage(cn = "仅在简单游戏难度下启用", en = "Enable only in Easy Game Mode")
        const val NOT_simple: String = "gtceu.ae.pattern_part_machine.not_simple"

        @RegisterLanguage(cn = "不在旅行网络中显示", en = "Do not show in Travel Network")
        const val NOT_SHOW_IN_TRAVEL: String = "gtceu.ae.pattern_part_machine.not_show_in_travel"

        @RegisterLanguage(cn = "在旅行网络中显示", en = "Show in Travel Network")
        const val SHOW_IN_TRAVEL: String = "gtceu.ae.pattern_part_machine.show_in_travel"
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

    @Persisted
    var showInTravelNetwork: Boolean = defaultShowInTravel()

    // ==================== 运行时属性 ====================
    val detailsSlotMap: BiMap<IPatternDetails, T> = HashBiMap.create(maxPatternCount)
    var detailsInit = false

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
        mainNode.addService(ICraftingProvider::class.java, this)
    }

    // ==================== 抽象方法 ====================
    abstract fun createInternalSlotArray(): Array<AbstractInternalSlot>
    abstract fun patternFilter(stack: ItemStack): Boolean
    abstract fun createInternalSlot(i: Int): T

    // ==================== 公开方法 ====================
    @Suppress("UNCHECKED_CAST")
    fun getInternalInventory(): Array<T> = internalInventory as Array<T>

    open fun onPatternChange(index: Int) {
        if (isRemote) return

        val internalInv = getInternalInventory()[index]
        val newPattern = patternInventory.getStackInSlot(index)
        val newPatternDetails = decodePattern(newPattern, index)
        val oldPatternDetails = detailsSlotMap.inverse()[internalInv]

        detailsSlotMap.forcePut(newPatternDetails, internalInv)

        oldPatternDetails?.takeIf { it != newPatternDetails }?.let {
            internalInv.onPatternChange()
        }

        updatePatterns()
    }

    open fun defaultShowInTravel(): Boolean = true

    // ==================== 扩展钩子方法 ====================
    open fun appendHoverTooltips(index: Int): Component? = null
    open fun onMouseClicked(index: Int) {}
    open fun getApplyIndex(): IntSupplier = IntSupplier { -1 }
    open fun onPageNext() {}
    open fun onPagePrev() {}
    open fun runOnUpdate() {}
    open fun addWidget(group: WidgetGroup) {}

    // ==================== 生命周期方法 ====================
    val newPageField = ISync.createIntField(this).set(0)

    override fun onMachinePlaced(player: LivingEntity?, stack: ItemStack?) {
        super<MEPartMachine>.onMachinePlaced(player, stack)
    }

    override fun onLoad() {
        super.onLoad()
        detailsInit = false
        level?.let { ITravelHandlerHook.removeAndReadd(it, this) }
    }

    override fun onUnload() {
        super.onUnload()
        detailsInit = false
        level?.let { TravelSavedData.getTravelData(it).removeTravelTargetAt(it, holder.blockPos) }
    }

    override fun clientTick() {
        super.clientTick()
    }

    override fun addedToController(controller: IMultiController) {
        super.addedToController(controller)
        ITravelHandlerHook.requireResync(level!!)
    }

    override fun onMainNodeStateChanged(reason: IGridNodeListener.State) {
        super<MEPartMachine>.onMainNodeStateChanged(reason)
        if (isOnline) {
            if (!detailsInit) {
                when (val level = getLevel()) {
                    is ServerLevel -> {
                        TaskHandler.enqueueServerTask(level, {
                            (0 until patternInventory.slots).forEach { i ->
                                val pattern = patternInventory.getStackInSlot(i)
                                decodePattern(pattern, i)?.let { patternDetails ->
                                    detailsSlotMap.forcePut(patternDetails, getInternalInventory()[i])
                                }
                            }
                            updatePatterns()
                            detailsInit = true
                        }, 10)
                    }
                }
            }
        } else {
            detailsInit = false
        }
    }

    override fun getSyncHolder(): SyncManagedFieldHolder = SYNC_MANAGED_FIELD_HOLDER

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
            isFormed -> {
                val controller = getControllers().first()
                val controllerDefinition = controller.self().definition
                AEItemKey.of(controllerDefinition.asStack()) to
                    if (customName.isNotEmpty()) {
                        Component.literal(customName)
                    } else {
                        Component.translatable(controllerDefinition.descriptionId)
                            .append("-")
                            .append(
                                (
                                    if (controller is IEnhancedRecipeLogicMachine) {
                                        Stream.of(
                                            *controller.recipeTypes,
                                        )
                                            .map { r: GTRecipeType? -> Component.translatable("gtceu." + r!!.registryName.path) }
                                            .collect(
                                                { Component.empty() },
                                                BiConsumer { c: MutableComponent?, t: MutableComponent? ->
                                                    c!!.append(
                                                        if (c.getString().isEmpty()) t else Component.literal("/").append(t),
                                                    )
                                                },
                                                { c1: MutableComponent?, c2: MutableComponent? ->
                                                    c1!!.append(
                                                        if (c2!!.string.isEmpty()) {
                                                            c2
                                                        } else {
                                                            Component.literal("/")
                                                                .append(c2)
                                                        },
                                                    )
                                                },
                                            )
                                    } else {
                                        Component.empty()
                                    }
                                    ),
                            )
                    }
            }
            else -> {
                AEItemKey.of(GTAEMachines.ME_PATTERN_BUFFER.asItem()) to
                    if (customName.isNotEmpty()) {
                        Component.literal(customName)
                    } else {
                        GTAEMachines.ME_PATTERN_BUFFER.get().definition.asItem().description
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
    override fun setDistinct(isDistinct: Boolean) {}
    override fun attachConfigurators(configuratorPanel: ConfiguratorPanel) {
        super.attachConfigurators(configuratorPanel)
        val configuratorToggle = IFancyConfiguratorButton.Toggle(
            GuiTextureGroup(
                GuiTextures.BUTTON,
                GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(true).copy()
                    .getSubTexture(0.0, 0.0, 1.0, 0.5),
            ),

            GuiTextureGroup(
                GuiTextures.BUTTON,
                GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(true).copy()
                    .getSubTexture(0.0, 0.5, 1.0, 0.5),
            ),
            { showInTravelNetwork },
            { _: ClickData, b: Boolean ->
                run {
                    showInTravelNetwork = b
                    ITravelHandlerHook.requireResync(level!!)
                }
            },
        ).setTooltipsSupplier { b ->
            listOf(SHOW_IN_TRAVEL.takeIf { b } ?: NOT_SHOW_IN_TRAVEL).map { Component.translatable(it) }
        }
        configuratorPanel.attachConfigurators(configuratorToggle)
    }

    // ==================== UI 相关方法 ====================
    lateinit var freshWidgetGroup: FreshWidgetGroupAbstract
    override fun createUIWidget(): Widget {
        freshWidgetGroup = rootFresh(176, 148) {
            val chunked: List<List<List<Int>>> = (0 until maxPatternCount).chunked(9).chunked(6)
            vBox(width = availableWidth, style = { spacing = 3 }) {
                hBox(height = 12, alwaysVerticalCenter = true) {
                    blank(width = 7)
                    textBlock(maxWidth = this@vBox.availableWidth, textSupplier = {
                        when (onlineField) {
                            true -> Component.translatable("gtceu.gui.me_network.online")
                            false -> Component.translatable("gtceu.gui.me_network.offline")
                        }
                    })
                    blank(width = 9)
                    textBlock(maxWidth = this@vBox.availableWidth, textSupplier = {
                        Component.translatable(AE_NAME)
                    })
                    field(height = 12, getter = { customName }, setter = {
                        customName = it
                        ITravelHandlerHook.requireResync(level!!)
                    })
                }
                val height1 = this@rootFresh.availableHeight - 24 - 16
                val pageWidget =
                    multiPageAdvanced(width = this@vBox.availableWidth, runOnUpdate = ::runOnUpdate, height = height1, pageSelector = newPageField) {
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
                        if (chunked.isEmpty()) {
                            page {
                                textBlock(maxWidth = this.availableWidth, textSupplier = {
                                    Component.translatable(NOT_simple)
                                })
                            }
                        }
                    }
                if (pageWidget.getMaxPageSize() > 1) {
                    hBox(height = 13, style = { spacing = 2 }) {
                        val wid = this@vBox.availableWidth - 2 * 2
                        button(
                            width = 30,
                            height = 13,
                            onClick = { ck ->
                                onPagePrev()
                                if (!isRemote)newPageField.setAndSyncToClient((newPageField.get() - 1).coerceAtLeast(0))
                            },
                            text = { "<<" },
                        )
                        text(height = 13, width = wid - 60, text = { Component.literal("${newPageField.get() + 1} / ${pageWidget.getMaxPageSize()}") })
                        button(
                            height = 13,
                            width = 30,
                            onClick = { ck ->
                                onPageNext()
                                if (!isRemote)newPageField.setAndSyncToClient((newPageField.get() + 1).coerceAtMost(pageWidget.getMaxPageSize() - 1))
                            },
                            text = { ">>" },
                        )
                    }
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

    open fun convertPattern(pattern: IPatternDetails, index: Int): IPatternDetails = pattern

    open fun decodePattern(stack: ItemStack, index: Int): IPatternDetails? {
        val pattern = MyPatternDetailsHelper.decodePattern(stack, holder.self, getGrid())
        if (pattern == null) return null
        return IParallelPatternDetails.of(convertPattern(pattern, index), level, 1)
    }

    private fun update() {
        if (needPatternSync) {
            if (isOnline) {
                ICraftingProvider.requestUpdate(getMainNode())
                needPatternSync = false
            }
        } else if (updateSubs != null) {
            updateSubs?.unsubscribe()
            updateSubs = null
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
    override fun createMainPage(widget: FancyMachineUIWidget?): Widget? = super.createMainPage(widget)

    override fun savePickClone(): Boolean = false

    override fun saveToItem(tag: CompoundTag) {
        tag.put("p", patternInventory.serializeNBT())
        tag.putString("n", customName)
        val list = ListTag()
        for (i in 0 until internalInventory.size) {
            list.add(internalInventory[i].serializeNBT())
        }
        tag.put("i", list)
    }

    override fun loadFromItem(tag: CompoundTag) {
        patternInventory.deserializeNBT(tag.getCompound("p"))
        customName = tag.getString("n")
        val list = tag.getList("i", Tag.TAG_COMPOUND.toInt())
        for (i in 0 until internalInventory.size) {
            internalInventory[i].deserializeNBT(list.getCompound(i))
        }
    }

    // ==================== 内部类 ====================
    abstract class AbstractInternalSlot :
        ITagSerializable<CompoundTag>,
        IContentChangeAware {
        abstract fun pushPattern(patternDetails: IPatternDetails, inputHolder: Array<KeyCounter>): Boolean
        abstract fun onPatternChange()
        override fun serializeNBT(): CompoundTag = CompoundTag()
    }
}

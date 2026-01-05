package com.gtocore.common.item

import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.common.data.GTOOrganItems.ORGAN_MODIFIER
import com.gtocore.common.data.translation.OrganTranslation.organModifierName
import com.gtocore.common.item.misc.OrganItemBase
import com.gtocore.common.item.misc.OrganType
import com.gtocore.utils.isNotEmpty
import com.gtocore.utils.ktFreshOrganState
import com.gtocore.utils.ktGetOrganStack

import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

import com.google.common.base.Predicate
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler
import com.gtolib.api.gui.ktflexible.HBoxBuilder
import com.gtolib.api.gui.ktflexible.blank
import com.gtolib.api.gui.ktflexible.root
import com.gtolib.api.player.IEnhancedPlayer
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory
import com.lowdragmc.lowdraglib.gui.modular.ModularUI
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture

class OrganModifierBehaviour :
    IItemUIFactory,
    IFancyUIProvider {
    
    private lateinit var player: Player
    
    @Override
    override fun use(item: Item?, level: Level?, player: Player, usedHand: InteractionHand?): InteractionResultHolder<ItemStack?>? {
        this.player = player
        return super.use(item, level, player, usedHand)
    }
    
    @Override
    override fun createUI(holder: HeldItemUIFactory.HeldItemHolder?, player: Player): ModularUI? = ModularUI(176, 166, holder, player)
        .widget(FancyMachineUIWidget(this, 176, 166))
    
    class HandlerContainer(val player: Player) {
        val organItemStacks = IEnhancedPlayer.of(player).playerData.organItemStacks
        var handlers = mutableMapOf<OrganType, CustomItemStackHandler>()
        
        init {
            OrganType.entries.forEach { organType ->
                handlers[organType] = CustomItemStackHandler(organType.slotCount).apply {
                    filter = Predicate { itemStack -> 
                        (itemStack.item as? OrganItemBase)?.organType == organType ?: false
                    }
                }
            }
            read()
        }
        
        fun read() {
            val organStacks = IEnhancedPlayer.of(player).playerData.ktGetOrganStack()
            OrganType.entries.forEach { organType ->
                handlers[organType]?.clear()
                organStacks[organType]?.forEachIndexed { index, itemStack ->
                    if (index < organType.slotCount) {
                        handlers[organType]?.setStackInSlot(index, itemStack)
                    }
                }
            }
        }
        
        fun save() {
            // 资源清理：清空旧数据
            organItemStacks.clear()
            
            // 显式副作用：保存数据到全局状态
            handlers.forEach { (_, handler) ->
                for (i in 0 until handler.slots) {
                    val itemStack = handler.getStackInSlot(i)
                    if (itemStack.isNotEmpty()) {
                        organItemStacks.add(itemStack)
                    }
                }
            }
        }
        
        /**
         * 资源清理：卸载时清理资源
         */
        fun cleanup() {
            handlers.forEach { (_, handler) ->
                handler.clear()
            }
            handlers.clear()
        }
    }
    @Override
    override fun createMainPage(widget: FancyMachineUIWidget?) = root(176, 166) {
        val handlerContainer = HandlerContainer(player)
        vScroll(width = availableWidth, height = availableHeight, { spacing = 8 }) {
            blank()
            handlerContainer.handlers.forEach { (organType, handler) ->
                hBox(height = 50) {
                    blank(width = 8)
                    vBox(width = availableWidth) {
                        textBlock(textSupplier = { Component.translatable(organType.getTranslationKey()) })
                        hBox(height = 18) {
                            (0 until handler.slots).forEach { index ->
                                createSlot(handlerContainer, handler, index)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun HBoxBuilder.createSlot(handlerContainer: HandlerContainer, handler: CustomItemStackHandler, index: Int) {
        widget(
            SlotWidget(handler, index, 0, 0, true, true).apply {
                setChangeListener {
                    handlerContainer.save()
                    handlerContainer.read()
                    // 数据持久化感知：标记数据为脏
                    IEnhancedPlayer.of(player).playerData.ktFreshOrganState()
                }
            },
        )
    }

    @Override
    override fun attachSideTabs(configuratorPanel: TabsWidget?) {
        configuratorPanel?.mainTab = this@OrganModifierBehaviour
    }
    
    @Override
    override fun getTabIcon(): IGuiTexture = ItemStackTexture(ORGAN_MODIFIER.get())

    @Override
    override fun getTitle(): Component = organModifierName.get()
}

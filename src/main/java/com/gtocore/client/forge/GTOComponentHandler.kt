package com.gtocore.client.forge

import com.gtocore.api.gui.graphic.GTOToolTipComponent
import com.gtocore.api.gui.graphic.GTOTooltipComponentItem
import com.gtocore.api.gui.graphic.impl.GTOComponentTooltipComponent
import com.gtocore.api.gui.graphic.impl.GTOProgressToolTipComponent
import com.gtocore.api.gui.graphic.impl.toPercentageWith
import com.gtocore.config.GTOConfig
import com.gtocore.utils.toLiteralSupplier

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.language.ClientLanguage
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.RenderTooltipEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent

import appeng.api.storage.StorageCells
import appeng.api.storage.cells.IBasicCellItem
import appeng.me.cells.BasicCellHandler
import com.google.common.collect.Lists
import com.gtolib.GTOCore
import com.mojang.datafixers.util.Either

@OnlyIn(Dist.CLIENT)
object GTOComponentHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @JvmStatic
    fun onGatherTooltipComponents(event: RenderTooltipEvent.GatherComponents) {
        val itemStack = event.itemStack
        val components = mutableListOf<GTOToolTipComponent>()
        val item = itemStack.item
        // 附着在Item上的处理器
        run {
            if (item is GTOTooltipComponentItem) {
                item.attachGTOTooltip(itemStack, components)
            }
        }
        // 多步合成的物品
        run {
            if (itemStack.hasTag()) {
                val step = itemStack.tag?.getInt("current_craft_step") ?: return@run
                val maxStep = itemStack.tag?.getInt("craft_step") ?: return@run
                if (maxStep == 0) return@run
                val text = Component.translatable(
                    "gtocore.tooltip.item.craft_step",
                    "$step/$maxStep (${((step.toFloat() / maxStep.toFloat()) * 100).toInt()}%)",
                ).string
                val component = GTOProgressToolTipComponent(
                    percentage = step toPercentageWith maxStep,
                    text = text,
                )
                components.add((component))
            }
        }
        // AE使用量
        run {
            if (item is IBasicCellItem) {
                val cellHandler = StorageCells.getHandler(itemStack) ?: return@run
                if (cellHandler !is BasicCellHandler) return@run
                val cellInventory = cellHandler.getCellInventory(itemStack, null) ?: return@run
                val usedBytes = cellInventory.usedBytes
                val totalBytes = cellInventory.totalBytes
                if (totalBytes <= 0) return@run
                val progress: Float = (usedBytes.toFloat() / totalBytes.toFloat())
                components.add(
                    (
                        GTOProgressToolTipComponent(
                            percentage = usedBytes toPercentageWith totalBytes,
                            text = "${(progress * 100).toInt()}%",
                        )
                        ),
                )
            }
        }
        // 英文翻译,优先级为0,英文环境不启用
        run {
            if (!GTOConfig.INSTANCE.showEnglishName) return@run
            val englishName = englishLanguage?.getOrDefault(itemStack.descriptionId) ?: return@run
            if (I18n.get(itemStack.descriptionId) == englishName) return@run
            val componentSupplier = englishName.toLiteralSupplier().gray()
            components.add(
                GTOComponentTooltipComponent(componentSupplier.get()),
            )
        }
        components.sortedBy { -it.priority }.forEach {
            event.tooltipElements.add(Either.right(it))
        }
    }
    var englishLanguage: ClientLanguage? = null
    init {
        // 初始化英语语言
        if (GTOConfig.INSTANCE.showEnglishName) {
            run {
                val manager = Minecraft.getInstance().languageManager
                val list: MutableList<String?> = Lists.newArrayList("en_us")
                val englishInfo = manager.getLanguage("en_us")
                if (englishInfo != null) {
                    englishLanguage = ClientLanguage.loadFrom(
                        Minecraft.getInstance().resourceManager,
                        list,
                        englishInfo.bidirectional(),
                    )
                } else {
                    GTOCore.LOGGER.warn("Failed to load English language for GTOCore.")
                }
            }
        }
    }
}

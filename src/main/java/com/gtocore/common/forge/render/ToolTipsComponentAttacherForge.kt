package com.gtocore.common.forge.render

import com.gtocore.api.gui.graphic.TooltipComponentManager
import com.gtocore.api.gui.graphic.component.BorderContainerComponent
import com.gtocore.api.gui.graphic.component.ProgressBarComponent
import com.gtocore.api.gui.graphic.helper.ProgressBarColorStyle

import net.minecraft.network.chat.Component
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.RenderTooltipEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

import appeng.api.storage.StorageCells
import appeng.api.storage.cells.IBasicCellItem
import appeng.items.tools.powered.PortableCellItem
import appeng.me.cells.BasicCellHandler

import kotlin.math.max

@OnlyIn(Dist.CLIENT)
object ToolTipsComponentAttacherForge {

    fun onGatherTooltipComponents(event: RenderTooltipEvent.GatherComponents) {
        event.tooltipElements
    }

    @SubscribeEvent
    @JvmStatic
    fun onItemTooltips(event: RenderTooltipEvent.Pre) {
        val components = event.components
        var tooltipWidth = 0
        var tooltipHeight = 0
        for (component in components) {
            val componentWidth = component.getWidth(event.font)
            if (componentWidth > tooltipWidth) {
                tooltipWidth = componentWidth
            }
            tooltipHeight += component.height
        }
        tooltipWidth += 8
        tooltipHeight += 8
        val componentManager = TooltipComponentManager()
        val itemStack = event.itemStack
        // //////////////////////////////
        // ****** AE硬盘 ******//
        // //////////////////////////////
        run {
            if (itemStack.item is IBasicCellItem || itemStack.item is PortableCellItem) {
                val cellHandler = StorageCells.getHandler(itemStack) ?: return@run
                if (cellHandler !is BasicCellHandler) return@run
                val cellInventory = cellHandler.getCellInventory(itemStack, null) ?: return@run
                val usedBytes = cellInventory.usedBytes
                val totalBytes = cellInventory.totalBytes
                if (totalBytes <= 0) return@run
                val progress = (usedBytes.toFloat() / totalBytes.toFloat())
                componentManager.components.add(
                    ProgressBarComponent(
                        progress,
                        tooltipWidth,
                        event.font,
                        textColor = 0xFFFFFFFF.toInt(),
                        colorStyle = ProgressBarColorStyle.Gradient(0xFF33CC33.toInt(), 0xFF55CC55.toInt()),
                        showTextInBar = "${(progress * 100).toInt()}%",
                    ),
                )
            }
        }
        // //////////////////////////////
        // ****** 多步合成的物品 ******//
        // //////////////////////////////
        run {
            if (itemStack.hasTag()) {
                val step = itemStack.tag?.getInt("current_craft_step") ?: return@run
                val maxStep = itemStack.tag?.getInt("craft_step") ?: return@run
                if (maxStep == 0) return@run
                val text = Component.translatable(
                    "gtocore.tooltip.item.craft_step",
                    "Step : $step/$maxStep (${((step.toFloat() / maxStep.toFloat()) * 100).toInt()}%)",
                ).string
                componentManager.components.add(
                    BorderContainerComponent(
                        ProgressBarComponent(
                            step.toFloat() / maxStep.toFloat(),
                            max(tooltipWidth, 100) - 8,
                            event.font,
                            flexibleForceWidth = true,
                            height = 10,
                            textColor = 0xFFFFFFFF.toInt(),
                            colorStyle = ProgressBarColorStyle.Gradient(0xFF33CC33.toInt(), 0xFF55CC55.toInt()),
                            showTextInBar = text,
                        ),
                        padding = 3,
                    ),
                )
            }
        }

        componentManager.render(
            event.graphics,
            event.x + 8,
            event.y - 16,
            tooltipWidth,
            tooltipHeight,
            event.font,
        )
    }
}

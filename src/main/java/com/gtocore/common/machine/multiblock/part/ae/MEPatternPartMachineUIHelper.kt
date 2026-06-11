package com.gtocore.common.machine.multiblock.part.ae

import com.gtocore.api.gui.ktflexible.multiPageAdvanced
import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.CIRCUIT_SPECIAL
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.FLUID_SPECIAL
import com.gtocore.eio_travel.logic.TravelUtils

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget
import com.gregtechceu.gtceu.api.gui.widget.TankWidget
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler
import com.gtolib.api.gui.ktflexible.VBoxBuilder
import com.gtolib.api.gui.ktflexible.blank
import com.gtolib.api.gui.ktflexible.field
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.jei.IngredientIO

fun buildHeader(container: VBoxBuilder, machine: MEPatternPartMachineKt<*>) {
    val width = container.availableWidth
    with(container) {
        with(machine) {
            hBox(height = 12, alwaysVerticalCenter = true) {
                blank(width = 7)
                textBlock(maxWidth = width, textSupplier = {
                    when (onlineField) {
                        true -> Component.translatable("gtceu.gui.me_network.online")
                        false -> Component.translatable("gtceu.gui.me_network.offline")
                    }
                })
                blank(width = 9)
                textBlock(maxWidth = width, textSupplier = {
                    Component.translatable(MEPatternPartMachineKt.AE_NAME)
                })
                field(height = 12, getter = { customName }, setter = {
                    customName = it
                    TravelUtils.requireResync(level!!)
                })
            }
        }
    }
}

fun createPatternPageWidget(container: VBoxBuilder, machine: MEPatternPartMachineKt<*>, pageHeight: Int, buildToolBoxContent: VBoxBuilder.() -> Unit, emptyPageTextSupplier: (() -> Component)? = null, prioritizeToolbox: Boolean = true) = with(container) {
    with(machine) {
        val width = container.availableWidth
        val chunked: List<List<List<Int>>> = (0 until maxPatternCount).chunked(9).chunked(6)
        multiPageAdvanced(width = width, runOnUpdate = ::runOnUpdate, height = pageHeight, pageSelector = newPageField) {
            chunked.forEach { pageIndices ->
                page {
                    vScroll(width = width, height = pageHeight) {
                        vBox(width = width, alwaysHorizonCenter = true) {
                            if (prioritizeToolbox) {
                                buildToolBoxContent()
                            }
                            pageIndices.forEach { lineIndices ->
                                hBox(height = 18) {
                                    lineIndices.forEach { index ->
                                        widget(createPatternSlot(index))
                                    }
                                }
                            }
                            if (!prioritizeToolbox) {
                                buildToolBoxContent()
                            }
                        }
                    }
                }
            }
            if (chunked.isEmpty()) {
                emptyPageTextSupplier?.let { supplier ->
                    page {
                        textBlock(maxWidth = this.availableWidth, textSupplier = supplier)
                    }
                }
            }
        }
    }
}

fun buildFluidSection(container: VBoxBuilder, width: Int, fluidHandler: Array<CustomFluidTank>) {
    with(container) {
        textBlock(maxWidth = width, textSupplier = { Component.translatable(FLUID_SPECIAL) })
        fluidHandler.indices.chunked(9).forEach { indices ->
            hBox(height = 18) {
                indices.forEach { index ->
                    widget(
                        TankWidget(
                            fluidHandler[index],
                            0,
                            0,
                            18,
                            18,
                            true,
                            true,
                        ).setBackground(GuiTextures.FLUID_SLOT),
                    )
                }
            }
        }
    }
}

fun buildCircuitSection(container: VBoxBuilder, width: Int, circuitSlot: Widget, getter: () -> String, setter: (String) -> Unit) {
    with(container) {
        textBlock(maxWidth = width, textSupplier = { Component.translatable(CIRCUIT_SPECIAL) })
        hBox(height = 18, style = { spacing = 4 }) {
            widget(circuitSlot)
            field(
                height = 18,
                getter = getter,
                setter = setter,
            )
        }
    }
}

fun createReadOnlyCircuitSlot(circuitHandler: CustomItemStackHandler): SlotWidget = SlotWidget(circuitHandler, 0, 0, 0, false, false).apply {
    setBackgroundTexture(GuiTextures.SLOT)
    setIngredientIO(IngredientIO.RENDER_ONLY)
}

fun buildSectionDivider(container: VBoxBuilder) {
    with(container) {
        blank(height = 4)
        widget(object : Widget(0, 0, availableWidth, 3) {
            override fun drawInBackground(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTicks: Float) {
                super.drawInBackground(graphics, mouseX, mouseY, partialTicks)
                DrawerHelper.drawSolidRect(graphics, positionX, positionY, sizeWidth, sizeHeight, 0xFFFFFFFF.toInt())
            }
        })
        blank(height = 4)
    }
}

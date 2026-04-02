package com.gtocore.common.machine.multiblock.part.ae.widget

import com.gtocore.api.gui.ktflexible.multiPageAdvanced
import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.common.machine.multiblock.part.ae.MEInputBufferPartMachine
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.circuit_special
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.emitting_crafting_mode
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.emitting_crafting_mode_tooltip
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.fluid_special
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.item_special
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.low_stock_triggering_mode
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.low_stock_triggering_mode_tooltip
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.low_stock_triggering_threshold
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.pattern_configuration
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.request_crafting_when_insufficient
import com.gtocore.common.machine.multiblock.part.ae.MEPatternPartMachineKt.Companion.AE_NAME
import com.gtocore.eio_travel.logic.TravelUtils

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.widget.LongInputWidget
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget
import com.gregtechceu.gtceu.api.gui.widget.TankWidget
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour
import com.gtolib.api.gui.ktflexible.VBoxBuilder
import com.gtolib.api.gui.ktflexible.blank
import com.gtolib.api.gui.ktflexible.field
import com.gtolib.api.gui.ktflexible.rootFresh
import com.lowdragmc.lowdraglib.gui.util.ClickData
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper
import com.lowdragmc.lowdraglib.gui.widget.SwitchWidget
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.jei.IngredientIO
import it.unimi.dsi.fastutil.booleans.BooleanConsumer

import java.util.concurrent.atomic.AtomicReference

fun createUIWidgetFor(machine: MEInputBufferPartMachine): Widget = with(machine) {
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
                    TravelUtils.requireResync(level!!)
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
                }
            pageWidget.refresh()
        }
    }
    return freshWidgetGroup
}

fun VBoxBuilder.buildToolBoxContentFor(machine: MEInputBufferPartMachine): Unit = with(machine) {
    when {
        configuratorField.get() < 0 -> {
        }

        configuratorField.get() in 0..<maxPatternCount -> {
            vBox(width = availableWidth, alwaysHorizonCenter = true, style = { spacing = 2 }) {
                val width = this@vBox.availableWidth
                val inv = getInternalInventory()[configuratorField.get()]
                val itemHandler = inv.lockableInventory

                textBlock(maxWidth = width, textSupplier = { Component.translatable(pattern_configuration) })
                hBox(height = 36, style = { spacing = 4 }) {
                    widget(AEItemConfigWidget(0, 0, inv.exportOnlyItemList))
                }
                hBox(height = 36, style = { spacing = 4 }) {
                    widget(AEFluidConfigWidget(3, 51, inv.exportOnlyFluidList))
                }
                textBlock(maxWidth = width, textSupplier = { Component.translatable(item_special) })
                (0 until itemHandler.slots).chunked(9).forEach { indices ->
                    hBox(height = 18) {
                        indices.forEach { index ->
                            widget(
                                SlotWidget(itemHandler, index, 0, 0, true, true).apply {
                                    setBackgroundTexture(GuiTextures.SLOT)
                                },
                            )
                        }
                    }
                }
                val fluidHandler: Array<CustomFluidTank> = inv.notConsumableFluid.storages
                textBlock(maxWidth = width, textSupplier = { Component.translatable(fluid_special) })
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
                val circuitHandler = inv.circuitInventory.storage
                textBlock(maxWidth = width, textSupplier = { Component.translatable(circuit_special) })
                hBox(height = 18, style = { spacing = 4 }) {
                    widget(
                        SlotWidget(circuitHandler, 0, 0, 0, false, false).apply {
                            setBackgroundTexture(GuiTextures.SLOT)
                            setIngredientIO(IngredientIO.RENDER_ONLY)
                        },
                    )
                    field(
                        height = 18,
                        getter = { IntCircuitBehaviour.getCircuitConfiguration(inv.circuitInventory.storage.getStackInSlot(0)).toString() },
                        setter = {
                            val circuit = when {
                                it.toIntOrNull() == null -> 0
                                else -> it.toInt().coerceAtMost(32).coerceAtLeast(0)
                            }
                            inv.circuitInventory.storage.setStackInSlot(0, if (circuit == 0) ItemStack.EMPTY else IntCircuitBehaviour.stack(circuit))
                        },
                    )
                }
                blank(height = 4)
                textBlock(maxWidth = width, textSupplier = { Component.translatable(low_stock_triggering_mode) })
                    .setHoverTooltips(low_stock_triggering_mode_tooltip)
                hBox(height = 24, style = { spacing = 4 }) {
                    val switch = AtomicReference<SwitchWidget>(null)
                    val longInput = LongInputWidget({ if (inv.minThreshold >= 0) inv.minThreshold else 0 }, {
                        if (switch.get() != null && switch.get()!!.isPressed) {
                            inv.minThreshold = it
                        }
                    })
                    switch.set(
                        widget(
                            switchWidget({ inv.minThreshold >= 0 }, {
                                if (it) {
                                    inv.minThreshold = 0
                                    longInput.isActive = true
                                    longInput.isVisible = true
                                } else {
                                    inv.minThreshold = -1
                                    longInput.isActive = false
                                    longInput.isVisible = false
                                }
                            }),
                        ).setHoverTooltips(low_stock_triggering_threshold) as SwitchWidget,
                    )
                    widget(longInput)
                }
                blank(height = 4)
                hBox(height = 24, style = { spacing = 4 }) {
                    textBlock(maxWidth = width, textSupplier = { Component.translatable(emitting_crafting_mode) })
                        .setHoverTooltips(emitting_crafting_mode_tooltip)
                    widget(switchWidget({ inv.isEmitterMode }, { inv.isEmitterMode = it }))
                }
                blank(height = 4)
                hBox(height = 24, style = { spacing = 4 }) {
                    textBlock(maxWidth = width, textSupplier = { Component.translatable(request_crafting_when_insufficient) })
                    widget(switchWidget({ inv.useRequest }, { inv.useRequest = it }))
                }
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
    }
}

private fun switchWidget(getter: () -> Boolean, setter: (Boolean) -> Unit): SwitchWidget {
    val switchWidget = SwitchWidget(0, 0, 16, 16, null)
    val updateTooltip =
        BooleanConsumer { isPressed: Boolean ->
            switchWidget.setHoverTooltips(
                if (isPressed) {
                    "attributeslib.value.boolean.enabled"
                } else {
                    "attributeslib.value.boolean.disabled"
                },
            )
        }
    switchWidget.setOnPressCallback { cd: ClickData?, result: Boolean? ->
        if (!cd!!.isRemote) {
            setter.invoke(result!!)
        } else {
            updateTooltip.accept(result!!)
        }
    }
    switchWidget.setPressed(getter.invoke())
        .setBaseTexture(
            GuiTextures.BUTTON,
            GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(true).copy()
                .getSubTexture(0.0, 0.0, 1.0, 0.5).scale(0.8f),
        )
        .setPressedTexture(
            GuiTextures.BUTTON,
            GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(true).copy()
                .getSubTexture(0.0, 0.5, 1.0, 0.5).scale(0.8f),
        )
    updateTooltip.accept(getter.invoke())
    return switchWidget
}

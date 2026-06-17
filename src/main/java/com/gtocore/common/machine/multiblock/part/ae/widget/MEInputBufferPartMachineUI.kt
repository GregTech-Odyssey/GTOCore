package com.gtocore.common.machine.multiblock.part.ae.widget

import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.common.machine.multiblock.part.ae.*
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.EMITTING_CRAFTING_MODE
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.EMITTING_CRAFTING_MODE_TOOLTIP
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.ITEM_SPECIAL
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.LOW_STOCK_TRIGGERING_MODE
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.LOW_STOCK_TRIGGERING_MODE_TOOLTIP
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.LOW_STOCK_TRIGGERING_THRESHOLD
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.PASSIVE_INPUT_MULTIPLIER
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.PASSIVE_INPUT_MULTIPLIER_TOOLTIP
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.PATTERN_CONFIGURATION
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt.Companion.REQUEST_CRAFTING_WHEN_INSUFFICIENT

import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.widget.LongInputWidget
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour
import com.gtolib.api.gui.ktflexible.VBoxBuilder
import com.gtolib.api.gui.ktflexible.blank
import com.gtolib.api.gui.ktflexible.rootFresh
import com.lowdragmc.lowdraglib.gui.util.ClickData
import com.lowdragmc.lowdraglib.gui.widget.SwitchWidget
import com.lowdragmc.lowdraglib.gui.widget.Widget
import it.unimi.dsi.fastutil.booleans.BooleanConsumer

import java.util.concurrent.atomic.AtomicReference

fun createUIWidgetFor(machine: MEInputBufferPartMachine): Widget = with(machine) {
    freshWidgetGroup = rootFresh(176, 148) {
        vBox(width = availableWidth, style = { spacing = 3 }) {
            buildHeader(this, machine)
            val height1 = this@rootFresh.availableHeight - 24 - 16
            val pageWidget =
                createPatternPageWidget(
                    container = this,
                    machine = machine,
                    pageHeight = height1,
                    buildToolBoxContent = { buildToolBoxContent() },
                    prioritizeToolbox = false,
                )
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

                buildSectionDivider(this)

                textBlock(maxWidth = width, textSupplier = { Component.translatable(ITEM_SPECIAL) })
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
                buildFluidSection(this, width, fluidHandler)
                val circuitHandler = inv.circuitInventory.storage
                buildCircuitSection(
                    container = this,
                    width = width,
                    circuitSlot = createReadOnlyCircuitSlot(circuitHandler),
                    getter = { IntCircuitBehaviour.getCircuitConfiguration(inv.circuitInventory.storage.getStackInSlot(0)).toString() },
                    setter = {
                        val circuit = when {
                            it.toIntOrNull() == null -> 0
                            else -> it.toInt().coerceAtMost(32).coerceAtLeast(0)
                        }
                        inv.circuitInventory.storage.setStackInSlot(0, if (circuit == 0) ItemStack.EMPTY else IntCircuitBehaviour.stack(circuit))
                    },
                )
                blank(height = 4)
                textBlock(maxWidth = width, textSupplier = { Component.translatable(LOW_STOCK_TRIGGERING_MODE) })
                    .setHoverTooltips(LOW_STOCK_TRIGGERING_MODE_TOOLTIP)
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
                        ).setHoverTooltips(LOW_STOCK_TRIGGERING_THRESHOLD) as SwitchWidget,
                    )
                    widget(longInput)
                }
                blank(height = 4)
                hBox(height = 24, style = { spacing = 4 }) {
                    textBlock(maxWidth = width, textSupplier = { Component.translatable(EMITTING_CRAFTING_MODE) })
                        .setHoverTooltips(EMITTING_CRAFTING_MODE_TOOLTIP)
                    widget(switchWidget({ inv.isEmitterMode }, { inv.isEmitterMode = it }))
                }
                blank(height = 4)
                hBox(height = 24, style = { spacing = 4 }) {
                    textBlock(maxWidth = width, textSupplier = { Component.translatable(REQUEST_CRAFTING_WHEN_INSUFFICIENT) })
                    widget(switchWidget({ inv.useRequest }, { inv.useRequest = it }))
                }
                blank(height = 4)
                textBlock(maxWidth = width, textSupplier = { Component.translatable(PASSIVE_INPUT_MULTIPLIER) })
                    .setHoverTooltips(Component.translatable(PASSIVE_INPUT_MULTIPLIER_TOOLTIP))
                hBox(height = 24, style = { spacing = 4 }) {
                    widget(
                        LongInputWidget({ if (inv.multiplier >= 1) inv.multiplier else 1 }, {
                            if (inv.multiplier == it) return@LongInputWidget
                            inv.multiplier = it
                            inv.reloadConfig()
                        }),
                    )
                }

                blank(height = 4)
                textBlock(maxWidth = width, textSupplier = { Component.translatable(PATTERN_CONFIGURATION) })
                hBox(height = 36, style = { spacing = 4 }) {
                    val c = AEItemConfigWidget(0, 0, inv.exportOnlyItemList)
                    c.setShowAmount(true)
                    widget(c)
                }
                hBox(height = 36, style = { spacing = 4 }) {
                    val c = AEFluidConfigWidget(3, 51, inv.exportOnlyFluidList)
                    c.setShowAmount(true)
                    widget(c)
                }
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

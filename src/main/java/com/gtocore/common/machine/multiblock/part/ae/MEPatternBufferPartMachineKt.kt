package com.gtocore.common.machine.multiblock.part.ae

import com.gtocore.api.gui.ktflexible.textBlock

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget
import com.gregtechceu.gtceu.api.gui.widget.TankWidget
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour
import com.gtolib.api.gui.ktflexible.VBoxBuilder
import com.gtolib.api.gui.ktflexible.blank
import com.gtolib.api.gui.ktflexible.field
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.jei.IngredientIO
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder

import java.util.function.IntSupplier

class MEPatternBufferPartMachineKt(holder: IMachineBlockEntity, maxPatternCount: Int) : MEPatternBufferPartMachine(holder, maxPatternCount) {
    companion object {
        @JvmStatic
        val MANAGED_FIELD_HOLDER = ManagedFieldHolder(MEPatternBufferPartMachineKt::class.java, MEPatternBufferPartMachine.MANAGED_FIELD_HOLDER)
    }

    override fun getFieldHolder(): ManagedFieldHolder = MANAGED_FIELD_HOLDER

    override fun getApplyIndex() = IntSupplier { configurator }
    override fun onPageNext() {
        super.onPageNext()
    }

    override fun onPagePrev() {
        super.onPagePrev()
    }

    override fun VBoxBuilder.buildToolBoxContent() {
        when {
            configurator < 0 -> {
            }
            configurator >= 0 && configurator < maxPatternCount -> {
                vBox(width = availableWidth, alwaysHorizonCenter = true, style = { spacing = 2 }) {
                    val width = this@vBox.availableWidth
                    val itemHandler = shareInventorys[configurator].storage
                    textBlock(maxWidth = width, textSupplier = { Component.literal("样板专用物品输入槽") })
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
                    val fluidHandler: Array<CustomFluidTank> = shareTanks[configurator].storages
                    textBlock(maxWidth = width, textSupplier = { Component.literal("样板专用流体输入槽") })
                    (0 until fluidHandler.size).chunked(9).forEach { indices ->
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
                    val circuitHandler = circuitInventorys[configurator].storage
                    textBlock(maxWidth = width, textSupplier = { Component.literal("样板专用电路输入槽") })
                    hBox(height = 18, style = { spacing = 4 }) {
                        run { if (circuitInventorys[configurator].storage.getStackInSlot(0).isEmpty) circuitInventorys[configurator].storage.setStackInSlot(0, IntCircuitBehaviour.stack(0)) }
                        widget(
                            SlotWidget(circuitHandler, 0, 0, 0, false, false).apply {
                                setBackgroundTexture(GuiTextures.SLOT)
                                setIngredientIO(IngredientIO.RENDER_ONLY)
                            },
                        )
                        field(
                            height = 18,
                            getter = { circuitConfigurations[configurator].toString() },
                            setter = {
                                val circuit = when {
                                    it.toIntOrNull() == null -> 0
                                    else -> it.toInt().coerceAtMost(32).coerceAtLeast(0)
                                }
                                circuitConfigurations[configurator] = circuit
                                circuitInventorys[configurator].storage.setStackInSlot(0, IntCircuitBehaviour.stack(circuit))
                            },
                        )
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
}

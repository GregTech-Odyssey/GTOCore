package com.gtocore.common.machine.multiblock.part.ae.widget.slot

import com.gtocore.api.gui.ktflexible.SyncWidget

import net.minecraft.client.gui.GuiGraphics
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.items.IItemHandlerModifiable

import com.lowdragmc.lowdraglib.gui.util.DrawerHelper

import java.util.function.IntSupplier

class AEPatternViewSlotWidgetInnerKt(itemHandler: IItemHandlerModifiable, slotIndex: Int, xPosition: Int, yPosition: Int, private val clicked: Runnable) :
    com.gregtechceu.gtceu.integration.ae2.gui.widget.slot.AEPatternViewSlotWidget(
        itemHandler,
        slotIndex,
        xPosition,
        yPosition,
    ) {

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (slotReference != null && gui != null && button == 2 && isMouseOverElement(mouseX, mouseY)) {
            clicked.run()
            return true
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }
}

class AEPatternViewSlotWidgetKt(x: Int, y: Int, val slotIndex: Int, val applyIndexSupplier: IntSupplier, val itemHandler: IItemHandlerModifiable, val clicked: Runnable) : SyncWidget(x, y, 18, 18) {
    var applyIndex = syncInt({ applyIndexSupplier.asInt }, -1, -1)
    var inner: AEPatternViewSlotWidgetInnerKt = AEPatternViewSlotWidgetInnerKt(itemHandler, slotIndex, 0, 0, clicked)
    init {
        addWidget(inner)
    }

    @OnlyIn(Dist.CLIENT)
    override fun drawInBackground(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks)
        if (applyIndex.lastValue == slotIndex) {
            graphics.pose().pushPose()
            graphics.pose().translate(positionX.toFloat(), positionY.toFloat(), 500f)
            DrawerHelper.drawBorder(graphics, 2, 2, 14, 14, 0xFFFFA500.toInt(), 2)
            graphics.pose().popPose()
        }
    }
}

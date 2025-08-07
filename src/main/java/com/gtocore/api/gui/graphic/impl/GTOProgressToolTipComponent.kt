package com.gtocore.api.gui.graphic.impl

import com.gtocore.api.gui.graphic.GTOClientTooltipComponent
import com.gtocore.api.gui.graphic.GTOToolTipComponent
import com.gtocore.api.gui.helper.ProgressBarColorStyle
import com.gtocore.api.gui.helper.ProgressBarHelper

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

class GTOProgressToolTipComponent(val percentage: Float, val text: String = "", val progressColorStyle: ProgressBarColorStyle = ProgressBarColorStyle.DEFAULT_GREEN) : GTOToolTipComponent()
class GTOProgressClientComponent(data: GTOProgressToolTipComponent) : GTOClientTooltipComponent<GTOProgressToolTipComponent>(data) {
    override fun renderImage(font: Font, x: Int, y: Int, guiGraphics: GuiGraphics) {
        guiGraphics.pose().pushPose()
        guiGraphics.pose().translate(x.toDouble() - 1, y.toDouble(), 400.0)
        ProgressBarHelper.drawProgressBarWithText(
            graphics = guiGraphics,
            progress = (data.percentage * 100).toInt(),
            totalWidth = data.width,
            totalHeight = data.height,
            text = data.text,
            borderWidth = 1,
            backgroundColor = 0xFF404040.toInt(),
            borderColor = 0xFF000000.toInt(),
            progressColorStyle = data.progressColorStyle,
        )
        guiGraphics.pose().popPose()
    }
}

class GTOComponentTooltipComponent(val component: Component) : GTOToolTipComponent(priority = 0)
infix fun Int.toPercentageWith(other: Int): Float = (this.toDouble() / other.toDouble()).toFloat()
infix fun Long.toPercentageWith(other: Long): Float = (this.toDouble() / other.toDouble()).toFloat()

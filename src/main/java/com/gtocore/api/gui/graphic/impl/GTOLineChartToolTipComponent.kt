package com.gtocore.api.gui.graphic.impl

import com.gtocore.api.gui.graphic.GTOClientTooltipComponent
import com.gtocore.api.gui.graphic.GTOToolTipComponent
import com.gtocore.api.gui.helper.LineChartHelper

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics

import java.math.BigInteger

class GTOLineChartToolTipComponent(
    var data: List<BigInteger> = emptyList(), // 存储从服务器同步来的数据
    var lineColor: Int = 0xFF2ECC71.toInt(),
    var drawAreaFill: Boolean = true,
) : GTOToolTipComponent(height = 60, width = 150) // 默认宽高

// 2. 客户端渲染器 (类似 GTOProgressClientComponent)
// 它负责调用 LineChartHelper 来执行实际的绘制
class GTOLineChartClientComponent(data: GTOLineChartToolTipComponent) : GTOClientTooltipComponent<GTOLineChartToolTipComponent>(data) {
    override fun renderImage(font: Font, x: Int, y: Int, guiGraphics: GuiGraphics) {
        guiGraphics.pose().pushPose()
        guiGraphics.pose().translate(x.toDouble(), y.toDouble(), 0.0)

        // 调用你的 LineChartHelper 来绘制图表
        LineChartHelper.drawLineChart(
            graphics = guiGraphics,
            data = data.data, // 将 BigInteger 列表传递给绘制函数
            totalWidth = data.width,
            totalHeight = data.height,
            lineColor = data.lineColor,
            drawAreaFill = data.drawAreaFill,
            // 这里可以添加更多从 data 对象中读取的自定义参数
        )

        guiGraphics.pose().popPose()
    }
}

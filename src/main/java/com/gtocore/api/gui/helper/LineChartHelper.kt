package com.gtocore.api.gui.helper

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.GameRenderer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

import com.lowdragmc.lowdraglib.gui.util.DrawerHelper
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import org.joml.Matrix4f

@OnlyIn(Dist.CLIENT)
object LineChartHelper {

    /**
     * 绘制线形图
     * @param graphics GuiGraphics 绘制上下文
     * @param data 要绘制的数据点列表，可以是任何 Number 类型
     * @param totalWidth 图表总宽度
     * @param totalHeight 图表总高度
     * @param borderWidth 边框宽度
     * @param backgroundColor 背景颜色
     * @param borderColor 边框颜色
     * @param lineColor 线条颜色
     * @param lineWidth 线条粗细
     * @param drawAreaFill 是否填充线下方的区域
     * @param areaFillColor 线下区域的填充颜色（建议使用带透明度的颜色）
     * @param drawDataPoints 是否在每个数据点上绘制标记
     * @param dataPointColor 数据点标记的颜色
     * @param dataPointRadius 数据点标记的半径
     * @return 实际绘制尺寸 (width, height)
     */
    fun drawLineChart(graphics: GuiGraphics, data: List<Number>?, totalWidth: Int, totalHeight: Int, borderWidth: Int = 1, backgroundColor: Int = 0xFF404040.toInt(), borderColor: Int = 0xFF000000.toInt(), lineColor: Int = 0xFF2ECC71.toInt(), lineWidth: Float = 1.5f, drawAreaFill: Boolean = false, areaFillColor: Int = 0x402ECC71, drawDataPoints: Boolean = true, dataPointColor: Int = 0xFFFFFFFF.toInt(), dataPointRadius: Float = 2f, autoReboundY: Boolean = true, minYBound: Double = Double.NaN, maxYBound: Double = Double.NaN): Pair<Int, Int> {
        val innerWidth = totalWidth - borderWidth * 2
        val innerHeight = totalHeight - borderWidth * 2

        // 1. 绘制边框和背景 (一次性)
        // 绘制顺序决定了层级，无需Z-Order
        if (borderWidth > 0) {
            DrawerHelper.drawBorder(graphics, 0, 0, totalWidth, totalHeight, borderColor, borderWidth)
        }
        if (innerWidth > 0 && innerHeight > 0) {
            DrawerHelper.drawSolidRect(graphics, borderWidth, borderWidth, innerWidth, innerHeight, backgroundColor)
        }

        if (data.isNullOrEmpty() || innerWidth <= 0 || innerHeight <= 0) {
            return totalWidth to totalHeight
        }

        // 2. 数据处理与坐标映射 (优化为单次遍历)
        val doubleData: List<Double>
        var minValue = Double.MAX_VALUE
        var maxValue = Double.MIN_VALUE
        if (autoReboundY) {
            doubleData = data.map {
                val d = it.toDouble()
                if (d < minValue) minValue = d
                if (d > maxValue) maxValue = d
                d
            }
        } else {
            doubleData = data.map { it.toDouble() }
            minValue = if (!minYBound.isNaN()) {
                minYBound
            } else {
                doubleData.minOrNull() ?: 0.0
            }
            maxValue = if (!maxYBound.isNaN()) {
                maxYBound
            } else {
                doubleData.maxOrNull() ?: 0.0
            }
        }

        if (data.size == 1) { // 特殊处理只有一个点的情况
            if (drawDataPoints) {
                val pointX = borderWidth + innerWidth / 2f
                val pointY = borderWidth + innerHeight / 2f
                DrawerHelper.drawSolidRect(graphics, (pointX - dataPointRadius).toInt(), (pointY - dataPointRadius).toInt(), (dataPointRadius * 2).toInt(), (dataPointRadius * 2).toInt(), dataPointColor)
            }
            return totalWidth to totalHeight
        }

        val dataRange = if (maxValue == minValue) 0.0 else maxValue - minValue

        val mapX = { index: Int ->
            borderWidth + (index.toFloat() / (data.size - 1)) * innerWidth
        }
        val mapY = { value: Double ->
            if (dataRange == 0.0) {
                borderWidth + innerHeight / 2f
            } else {
                (borderWidth + innerHeight - ((value - minValue) / dataRange) * innerHeight).toFloat()
            }
        }

        val points = doubleData.indices.map { i -> Pair(mapX(i), mapY(doubleData[i])) }
        val matrix = graphics.pose().last().pose()

        // 3. 绘制线下填充区域 (单次Draw Call)
        if (drawAreaFill) {
            drawArea(matrix, points, borderWidth.toFloat() + innerHeight, areaFillColor)
        }

        // 4. 批量绘制所有线段 (单次Draw Call)
        drawThickLines(matrix, points, lineWidth, lineColor)

        // 5. 批量绘制所有数据点 (单次Draw Call)
        if (drawDataPoints) {
            drawDataPoints(matrix, points, dataPointRadius, dataPointColor)
        }

        return totalWidth to totalHeight
    }

    /**
     * 使用 BufferBuilder 绘制线下填充区域
     */
    private fun drawArea(matrix: Matrix4f, points: List<Pair<Float, Float>>, bottomY: Float, color: Int) {
        val r = (color shr 16 and 0xFF) / 255.0f
        val g = (color shr 8 and 0xFF) / 255.0f
        val b = (color and 0xFF) / 255.0f
        val a = (color shr 24 and 0xFF) / 255.0f

        val tessellate = Tesselator.getInstance()
        val buffer = tessellate.builder
        RenderSystem.setShader(GameRenderer::getPositionColorShader)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()

        buffer.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR)
        for (point in points) {
            buffer.vertex(matrix, point.first, point.second, 0f).color(r, g, b, a).endVertex()
            buffer.vertex(matrix, point.first, bottomY, 0f).color(r, g, b, a).endVertex()
        }
        tessellate.end()
        RenderSystem.disableBlend()
    }

    private fun drawThickLines(matrix: Matrix4f, points: List<Pair<Float, Float>>, width: Float, color: Int) {
        if (points.size < 2) return

        val r = (color shr 16 and 0xFF) / 255.0f
        val g = (color shr 8 and 0xFF) / 255.0f
        val b = (color and 0xFF) / 255.0f
        val a = (color shr 24 and 0xFF) / 255.0f

        val tesselator = Tesselator.getInstance()
        val buffer = tesselator.builder
        RenderSystem.setShader(GameRenderer::getPositionColorShader)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR)
        for (i in 0 until points.size - 1) {
            val (x1, y1) = points[i]
            val (x2, y2) = points[i + 1]

            val dx = x2 - x1
            val dy = y2 - y1
            // 避免除以0
            val length = kotlin.math.sqrt(dx * dx + dy * dy)
            if (length == 0f) continue

            val halfWidth = width / 2f
            // 直接使用标准化的向量，比atan2/sin/cos更高效
            val offsetX = -dy / length * halfWidth
            val offsetY = dx / length * halfWidth

            buffer.vertex(matrix, x1 - offsetX, y1 - offsetY, 0f).color(r, g, b, a).endVertex()
            buffer.vertex(matrix, x1 + offsetX, y1 + offsetY, 0f).color(r, g, b, a).endVertex()
            buffer.vertex(matrix, x2 + offsetX, y2 + offsetY, 0f).color(r, g, b, a).endVertex()
            buffer.vertex(matrix, x2 - offsetX, y2 - offsetY, 0f).color(r, g, b, a).endVertex()
        }
        tesselator.end() // 所有线段绘制完毕后，一次性提交

        RenderSystem.disableBlend()
    }

    /**
     *  批量绘制所有数据点
     */
    private fun drawDataPoints(matrix: Matrix4f, points: List<Pair<Float, Float>>, radius: Float, color: Int) {
        val r = (color shr 16 and 0xFF) / 255.0f
        val g = (color shr 8 and 0xFF) / 255.0f
        val b = (color and 0xFF) / 255.0f
        val a = (color shr 24 and 0xFF) / 255.0f

        val halfRadius = radius / 2f

        val tesselator = Tesselator.getInstance()
        val buffer = tesselator.builder
        RenderSystem.setShader(GameRenderer::getPositionColorShader)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR)
        points.forEach { (x, y) ->
            buffer.vertex(matrix, x - halfRadius, y - halfRadius, 0f).color(r, g, b, a).endVertex()
            buffer.vertex(matrix, x + halfRadius, y - halfRadius, 0f).color(r, g, b, a).endVertex()
            buffer.vertex(matrix, x + halfRadius, y + halfRadius, 0f).color(r, g, b, a).endVertex()
            buffer.vertex(matrix, x - halfRadius, y + halfRadius, 0f).color(r, g, b, a).endVertex()
        }
        tesselator.end()

        RenderSystem.disableBlend()
    }

    /**
     * 创建一个 LineChartBuilder 用于链式调用构建线形图
     * @param graphics GuiGraphics 绘制上下文
     * @param data 要绘制的数据点列表
     * @return LineChartBuilder 实例，支持链式调用
     *
     * 使用示例:
     * ```
     * LineChartHelper.builder(graphics, data)
     *     .width(300)
     *     .height(200)
     *     .lineColor(0xFF2ECC71)
     *     .drawAreaFill(true)
     *     .draw()
     * ```
     */
    fun builder(graphics: GuiGraphics, data: List<Number>?): LineChartBuilder = LineChartBuilder(graphics, data)
}

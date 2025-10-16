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

import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

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
    fun drawLineChart(
        graphics: GuiGraphics,
        data: List<Number>,
        totalWidth: Int,
        totalHeight: Int,
        borderWidth: Int = 1,
        backgroundColor: Int = 0xFF404040.toInt(),
        borderColor: Int = 0xFF000000.toInt(),
        lineColor: Int = 0xFF2ECC71.toInt(),
        lineWidth: Float = 1.5f,
        drawAreaFill: Boolean = false,
        areaFillColor: Int = 0x402ECC71, // 带透明度的绿色
        drawDataPoints: Boolean = true,
        dataPointColor: Int = 0xFFFFFFFF.toInt(),
        dataPointRadius: Float = 2f,
    ): Pair<Int, Int> {
        val atomicZOrder = AtomicInteger(0)
        val innerX = borderWidth
        val innerY = borderWidth
        val innerWidth = totalWidth - borderWidth * 2
        val innerHeight = totalHeight - borderWidth * 2

        // 1. 绘制边框和背景 (底层)
        if (borderWidth > 0) {
            renderAndAddZOrder(graphics, {
                DrawerHelper.drawBorder(it, 0, 0, totalWidth, totalHeight, borderColor, borderWidth)
            }, atomicZOrder)
        }
        if (innerWidth > 0 && innerHeight > 0) {
            renderAndAddZOrder(graphics, {
                DrawerHelper.drawSolidRect(it, innerX, innerY, innerWidth, innerHeight, backgroundColor)
            }, atomicZOrder)
        }

        // 数据点少于2个，无法形成线段，提前返回
        if (data.size < 2 || innerWidth <= 0 || innerHeight <= 0) {
            // 如果只有一个点，可以考虑把它画出来
            if (data.size == 1 && drawDataPoints) {
                renderAndAddZOrder(graphics, {
                    val pointX = innerX + innerWidth / 2f
                    val pointY = innerY + innerHeight / 2f
                    DrawerHelper.drawSolidRect(it, (pointX - dataPointRadius).toInt(), (pointY - dataPointRadius).toInt(), (dataPointRadius * 2).toInt(), (dataPointRadius * 2).toInt(), dataPointColor)
                }, atomicZOrder)
            }
            return totalWidth to totalHeight
        }

        // 2. 数据处理与坐标映射
        val doubleData = data.map { it.toDouble() }
        val minValue = doubleData.minOrNull() ?: 0.0
        val maxValue = doubleData.maxOrNull() ?: 0.0
        val dataRange = maxValue - minValue

        // 将数据点的索引和值映射到图表内部的像素坐标
        val mapX = { index: Int ->
            innerX + (index.toFloat() / (data.size - 1)) * innerWidth
        }
        val mapY = { value: Double ->
            if (dataRange == 0.0) {
                innerY + innerHeight / 2f // 所有值都相等，画在中间
            } else {
                (innerY + innerHeight - ((value - minValue) / dataRange) * innerHeight).toFloat()
            }
        }

        val points = doubleData.indices.map { i -> Pair(mapX(i), mapY(doubleData[i])) }

        // 3. 绘制线下填充区域 (中层)
        if (drawAreaFill) {
            renderAndAddZOrder(graphics, {
                drawArea(it.pose().last().pose(), points, innerY.toFloat() + innerHeight, areaFillColor)
            }, atomicZOrder)
        }

        // 4. 绘制线段 (中上层)
        for (i in 0 until points.size - 1) {
            val p1 = points[i]
            val p2 = points[i + 1]
            renderAndAddZOrder(graphics, {
                drawThickLine(it.pose().last().pose(), p1.first, p1.second, p2.first, p2.second, lineWidth, lineColor)
            }, atomicZOrder)
        }

        // 5. 绘制数据点 (顶层)
        if (drawDataPoints) {
            points.forEach { (x, y) ->
                renderAndAddZOrder(graphics, {
                    // 使用画矩形的方式来模拟画圆点，简单高效
                    DrawerHelper.drawSolidRect(
                        it,
                        (x - dataPointRadius / 2).toInt(),
                        (y - dataPointRadius / 2).toInt(),
                        dataPointRadius.toInt(),
                        dataPointRadius.toInt(),
                        dataPointColor,
                    )
                }, atomicZOrder)
            }
        }

        return totalWidth to totalHeight
    }

    /**
     * Z-Order 包装渲染，与 ProgressBarHelper 保持一致
     */
    private fun renderAndAddZOrder(graphics: GuiGraphics, renderFunc: (GuiGraphics) -> Unit, zOrder: AtomicInteger) {
        graphics.pose().pushPose()
        graphics.pose().translate(0.0, 0.0, zOrder.getAndIncrement().toDouble())
        renderFunc(graphics)
        graphics.pose().popPose()
    }

    /**
     * 使用 BufferBuilder 绘制带粗细的线段
     */
    private fun drawThickLine(matrix: Matrix4f, x1: Float, y1: Float, x2: Float, y2: Float, width: Float, color: Int) {
        val dx = x2 - x1
        val dy = y2 - y1
        val angle = atan2(dy, dx)
        val halfWidth = width / 2f
        val offsetX = sin(angle) * halfWidth
        val offsetY = -cos(angle) * halfWidth

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
        buffer.vertex(matrix, x1 - offsetX, y1 - offsetY, 0f).color(r, g, b, a).endVertex()
        buffer.vertex(matrix, x1 + offsetX, y1 + offsetY, 0f).color(r, g, b, a).endVertex()
        buffer.vertex(matrix, x2 + offsetX, y2 + offsetY, 0f).color(r, g, b, a).endVertex()
        buffer.vertex(matrix, x2 - offsetX, y2 - offsetY, 0f).color(r, g, b, a).endVertex()
        tesselator.end()

        RenderSystem.disableBlend()
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
}

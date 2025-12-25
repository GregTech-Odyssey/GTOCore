package com.gtocore.api.gui.helper

import net.minecraft.client.gui.GuiGraphics
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * LineChartBuilder - 用于构建和绘制线形图的Builder类
 * 提供链式调用接口，使参数传递更加整洁直观
 *
 * 使用示例:
 * ```
 * LineChartBuilder(graphics, data)
 *     .width(300)
 *     .height(200)
 *     .lineColor(0xFF2ECC71)
 *     .lineWidth(2f)
 *     .drawAreaFill(true)
 *     .areaFillColor(0x402ECC71)
 *     .drawDataPoints(true)
 *     .dataPointRadius(3f)
 *     .draw()
 * ```
 */
@OnlyIn(Dist.CLIENT)
class LineChartBuilder(private val graphics: GuiGraphics, private val data: List<Number>?) {
    // 必需参数
    private var totalWidth: Int = 300
    private var totalHeight: Int = 200

    // 边框相关
    private var borderWidth: Int = 1
    private var borderColor: Int = 0xFF000000.toInt()

    // 背景颜色
    private var backgroundColor: Int = 0xFF404040.toInt()

    // 线条相关
    private var lineColor: Int = 0xFF2ECC71.toInt()
    private var lineWidth: Float = 1.5f

    // 区域填充相关
    private var drawAreaFill: Boolean = false
    private var areaFillColor: Int = 0x402ECC71

    // 数据点相关
    private var drawDataPoints: Boolean = true
    private var dataPointColor: Int = 0xFFFFFFFF.toInt()
    private var dataPointRadius: Float = 2f

    private var autoReboundY: Boolean = true
    private var minYBound: Double = Double.NaN
    private var maxYBound: Double = Double.NaN

    /**
     * 设置图表宽度
     */
    fun width(width: Int) = apply { this.totalWidth = width }

    /**
     * 设置图表高度
     */
    fun height(height: Int) = apply { this.totalHeight = height }

    /**
     * 设置图表尺寸
     */
    fun size(width: Int, height: Int) = apply {
        this.totalWidth = width
        this.totalHeight = height
    }

    /**
     * 设置边框宽度
     */
    fun borderWidth(borderWidth: Int) = apply { this.borderWidth = borderWidth }

    /**
     * 设置边框颜色（ARGB 格式）
     */
    fun borderColor(color: Int) = apply { this.borderColor = color }

    /**
     * 设置背景颜色（ARGB 格式）
     */
    fun backgroundColor(color: Int) = apply { this.backgroundColor = color }

    /**
     * 设置线条颜色（ARGB 格式）
     */
    fun lineColor(color: Int) = apply { this.lineColor = color }

    /**
     * 设置线条粗细
     */
    fun lineWidth(width: Float) = apply { this.lineWidth = width }

    /**
     * 设置是否填充线下方的区域
     */
    fun drawAreaFill(draw: Boolean) = apply { this.drawAreaFill = draw }

    /**
     * 设置线下区域的填充颜色（建议使用带透明度的颜色）
     */
    fun areaFillColor(color: Int) = apply { this.areaFillColor = color }

    /**
     * 设置是否在每个数据点上绘制标记
     */
    fun drawDataPoints(draw: Boolean) = apply { this.drawDataPoints = draw }

    /**
     * 设置数据点标记的颜色（ARGB 格式）
     */
    fun dataPointColor(color: Int) = apply { this.dataPointColor = color }

    /**
     * 设置数据点标记的半径
     */
    fun dataPointRadius(radius: Float) = apply { this.dataPointRadius = radius }

    /**
     * 一次性设置所有配色选项
     * @param borderColor 边框颜色
     * @param backgroundColor 背景颜色
     * @param lineColor 线条颜色
     * @param areaFillColor 填充颜色
     * @param dataPointColor 数据点颜色
     */
    fun colors(borderColor: Int, backgroundColor: Int, lineColor: Int, areaFillColor: Int = this.areaFillColor, dataPointColor: Int = this.dataPointColor) = apply {
        this.borderColor = borderColor
        this.backgroundColor = backgroundColor
        this.lineColor = lineColor
        this.areaFillColor = areaFillColor
        this.dataPointColor = dataPointColor
    }

    /**
     * 一次性设置线条相关参数
     */
    fun line(color: Int, width: Float = this.lineWidth) = apply {
        this.lineColor = color
        this.lineWidth = width
    }

    /**
     * 一次性设置数据点相关参数
     */
    fun dataPoint(color: Int, radius: Float = this.dataPointRadius) = apply {
        this.dataPointColor = color
        this.dataPointRadius = radius
    }

    /**
     * 一次性设置区域填充相关参数
     */
    fun areaFill(color: Int, draw: Boolean = true) = apply {
        this.areaFillColor = color
        this.drawAreaFill = draw
    }

    fun autoReboundY(auto: Boolean) = apply { this.autoReboundY = auto }
    fun yBound(min: Double, max: Double) = apply {
        this.minYBound = min
        this.maxYBound = max
    }

    /**
     * 执行绘制操作并返回实际绘制尺寸
     */
    fun draw(): Pair<Int, Int> = LineChartHelper.drawLineChart(
        graphics = graphics,
        data = data,
        totalWidth = totalWidth,
        totalHeight = totalHeight,
        borderWidth = borderWidth,
        backgroundColor = backgroundColor,
        borderColor = borderColor,
        lineColor = lineColor,
        lineWidth = lineWidth,
        drawAreaFill = drawAreaFill,
        areaFillColor = areaFillColor,
        drawDataPoints = drawDataPoints,
        dataPointColor = dataPointColor,
        dataPointRadius = dataPointRadius,
        autoReboundY = autoReboundY,
        minYBound = minYBound,
        maxYBound = maxYBound,
    )

    /**
     * 快速构建：使用默认主题色的线图
     * 常用于快速绘制单色线图
     */
    fun drawSimple(themeColor: Int): Pair<Int, Int> = LineChartBuilder(graphics, data)
        .borderColor(themeColor)
        .lineColor(themeColor)
        .dataPointColor(0xFFFFFFFF.toInt())
        .draw()
}

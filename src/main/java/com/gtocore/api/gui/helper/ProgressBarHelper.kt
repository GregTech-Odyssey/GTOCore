package com.gtocore.api.gui.helper

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

import com.lowdragmc.lowdraglib.gui.util.DrawerHelper

import kotlin.math.roundToInt

/**
 * 进度条绘制辅助工具
 */
object ProgressBarHelper {
    // region 主流程
    /**
     * 绘制带文本的进度条
     * @return 实际绘制尺寸 (width, height)
     */
    @OnlyIn(Dist.CLIENT)
    fun drawProgressBarWithText(graphics: GuiGraphics, progress: Int, totalWidth: Int, totalHeight: Int = 7, text: String, borderWidth: Int = 1, progressColorStyle: ProgressBarColorStyle = ProgressBarColorStyle.DEFAULT_GREEN, backgroundColor: Int = 0xFF404040.toInt(), borderColor: Int = 0xFF000000.toInt(), textColor: Int = 0xFFFFFFFF.toInt()): Pair<Int, Int> {
        val safeProgress = progress.coerceIn(0, 100)
        val progressFloat = safeProgress / 100f
        val innerX = borderWidth
        val innerY = borderWidth
        val innerWidth = totalWidth - borderWidth * 2
        val innerHeight = totalHeight - borderWidth * 2
        if (borderWidth > 0) {
            DrawerHelper.drawBorder(graphics, innerX, innerY, innerWidth, innerHeight, borderColor, borderWidth)
        }
        if (innerWidth > 0 && innerHeight > 0) {
            DrawerHelper.drawSolidRect(graphics, innerX, innerY, innerWidth, innerHeight, backgroundColor)
        }
        val progressWidth = (innerWidth * progressFloat).roundToInt()
        if (progressWidth > 0 && innerHeight > 0) {
            val color = when (progressColorStyle) {
                is ProgressBarColorStyle.Solid -> progressColorStyle.color
                is ProgressBarColorStyle.Segmented -> getSegmentedColor(progressFloat, progressColorStyle.segments)
                is ProgressBarColorStyle.Gradient -> interpolateColor(progressColorStyle.startColor, progressColorStyle.endColor, progressFloat)
                is ProgressBarColorStyle.MultiGradient -> getMultiGradientColor(progressFloat, progressColorStyle.colors)
            }
            DrawerHelper.drawSolidRect(graphics, innerX, innerY, progressWidth, innerHeight, color)
        }
        if (text.isNotEmpty()) {
            val font = Minecraft.getInstance().font
            val textWidth = font.width(text)
            val textHeight = font.lineHeight
            val textX = (totalWidth - textWidth) / 2f
            val textY = (totalHeight - textHeight) / 2f + 1f
            DrawerHelper.drawText(graphics, text, textX, textY, 1f, textColor, true)
        }
        return totalWidth to totalHeight
    }
    // endregion

    // region 私有色彩辅助
    private fun getSegmentedColor(progress: Float, segments: List<Pair<Float, Int>>): Int {
        for (i in segments.indices.reversed()) {
            if (progress >= segments[i].first) {
                return segments[i].second
            }
        }
        return segments.first().second
    }

    private fun getMultiGradientColor(progress: Float, colors: List<Pair<Float, Int>>): Int {
        if (progress <= colors.first().first) return colors.first().second
        if (progress >= colors.last().first) return colors.last().second
        for (i in 0 until colors.size - 1) {
            val current = colors[i]
            val next = colors[i + 1]
            if (progress >= current.first && progress <= next.first) {
                val t = (progress - current.first) / (next.first - current.first)
                return interpolateColor(current.second, next.second, t)
            }
        }
        return colors.last().second
    }

    private fun interpolateColor(startColor: Int, endColor: Int, factor: Float): Int {
        val safeFactor = factor.coerceIn(0f, 1f)
        val startA = (startColor shr 24) and 0xFF
        val startR = (startColor shr 16) and 0xFF
        val startG = (startColor shr 8) and 0xFF
        val startB = startColor and 0xFF
        val endA = (endColor shr 24) and 0xFF
        val endR = (endColor shr 16) and 0xFF
        val endG = (endColor shr 8) and 0xFF
        val endB = endColor and 0xFF
        val a = (startA + (endA - startA) * safeFactor).roundToInt()
        val r = (startR + (endR - startR) * safeFactor).roundToInt()
        val g = (startG + (endG - startG) * safeFactor).roundToInt()
        val b = (startB + (endB - startB) * safeFactor).roundToInt()
        return (a shl 24) or (r shl 16) or (g shl 8) or b
    }
    // endregion
}

/**
 * 进度条颜色样式
 */
sealed class ProgressBarColorStyle {
    data class Solid(val color: Int) : ProgressBarColorStyle()
    data class Segmented(val segments: List<Pair<Float, Int>>) : ProgressBarColorStyle() {
        init {
            require(segments.isNotEmpty()) { "Segments cannot be empty" }
            require(segments.all { it.first in 0f..1f }) { "All thresholds must be in range [0, 1]" }
            require(segments.sortedBy { it.first } == segments) { "Segments must be sorted by threshold" }
        }
    }
    data class Gradient(val startColor: Int, val endColor: Int) : ProgressBarColorStyle()
    data class MultiGradient(val colors: List<Pair<Float, Int>>) : ProgressBarColorStyle() {
        init {
            require(colors.size >= 2) { "At least 2 colors required for gradient" }
            require(colors.all { it.first in 0f..1f }) { "All positions must be in range [0, 1]" }
            require(colors.sortedBy { it.first } == colors) { "Colors must be sorted by position" }
        }
    }
    companion object {
        val HEALTH_GRADIENT = MultiGradient(
            listOf(
                0f to 0xFF33AA33.toInt(),
                0.5f to 0xFFFFAA33.toInt(),
                1f to 0xFF55CC55.toInt(),
            ),
        )
        val MANA_GRADIENT = MultiGradient(
            listOf(
                0f to 0xFF0066CC.toInt(),
                0.5f to 0xFF00CCCC.toInt(),
                1f to 0xFFCCFFFF.toInt(),
            ),
        )
        val EXPERIENCE_GRADIENT = Gradient(0xFF7FFF00.toInt(), 0xFFFFD700.toInt())
        val DEFAULT_GREEN = Solid(0xFF2ecc71.toInt())
        val DEFAULT_RED = Solid(0xFFe74c3c.toInt())
    }
}

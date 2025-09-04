package com.gtocore.api.gui.helper

import com.gtocore.api.misc.codec.CodecAbleTyped
import com.gtocore.api.misc.codec.CodecAbleTypedCompanion

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

import com.lowdragmc.lowdraglib.gui.util.DrawerHelper
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

import java.util.concurrent.atomic.AtomicInteger
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
        val atomicZOrder = AtomicInteger(0)
        if (borderWidth > 0) {
//            DrawerHelper.drawBorder(graphics, innerX, innerY, innerWidth, innerHeight, borderColor, borderWidth)
            renderAndAddZOrder(
                graphics,
                {
                    DrawerHelper.drawBorder(graphics, innerX, innerY, innerWidth, innerHeight, borderColor, borderWidth)
                },
                zOrder = atomicZOrder,
            )
        }
        if (innerWidth > 0 && innerHeight > 0) {
//            DrawerHelper.drawSolidRect(graphics, innerX, innerY, innerWidth, innerHeight, backgroundColor)
            renderAndAddZOrder(
                graphics,
                {
                    DrawerHelper.drawSolidRect(graphics, innerX, innerY, innerWidth, innerHeight, backgroundColor)
                },
                zOrder = atomicZOrder,
            )
        }
        val progressWidth = (innerWidth * progressFloat).roundToInt()
        if (progressWidth > 0 && innerHeight > 0) {
            val color = when (progressColorStyle) {
                is ProgressBarColorStyle.Solid -> progressColorStyle.color
                is ProgressBarColorStyle.Segmented -> getSegmentedColor(progressFloat, progressColorStyle.segments)
                is ProgressBarColorStyle.Gradient -> interpolateColor(progressColorStyle.startColor, progressColorStyle.endColor, progressFloat)
                is ProgressBarColorStyle.MultiGradient -> getMultiGradientColor(progressFloat, progressColorStyle.colors)
            }
            renderAndAddZOrder(
                graphics,
                {
                    DrawerHelper.drawSolidRect(graphics, innerX, innerY, progressWidth, innerHeight, color)
                },
                zOrder = atomicZOrder,
            )
        }
        if (text.isNotEmpty()) {
            val font = Minecraft.getInstance().font
            val textWidth = font.width(text)
            val textHeight = font.lineHeight
            val textX = (totalWidth - textWidth) / 2f
            val textY = (totalHeight - textHeight) / 2f + 1f
//            DrawerHelper.drawText(graphics, text, textX, textY, 1f, textColor, true)
            renderAndAddZOrder(
                graphics,
                {
                    DrawerHelper.drawText(graphics, text, textX, textY, 1f, textColor, false)
                },
                zOrder = atomicZOrder,
            )
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
    private fun renderAndAddZOrder(graphics: GuiGraphics, renderFunc: (GuiGraphics) -> Unit, zOrder: AtomicInteger) {
        graphics.pose().pushPose()
        graphics.pose().translate(0.0, 0.0, zOrder.andIncrement.toDouble())
        renderFunc(graphics)
        graphics.pose().popPose()
    }
    // endregion
}

/**
 * 进度条颜色样式
 */
sealed class ProgressBarColorStyle : CodecAbleTyped<ProgressBarColorStyle, ProgressBarColorStyle.Companion> {
    companion object : CodecAbleTypedCompanion<ProgressBarColorStyle> {
        override fun getCodec(): Codec<ProgressBarColorStyle> = throw NotImplementedError("请在对应子类实现")
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
        val DEFAULT_YELLOW = Solid(0xFFfdda0d.toInt())
        val DEFAULT_RED = Solid(0xFFe74c3c.toInt())
        val DURATION = MultiGradient(
            listOf(
                0f to DEFAULT_RED.color,
                0.1f to DEFAULT_RED.color,
                0.2f to DEFAULT_YELLOW.color,
                0.3f to DEFAULT_GREEN.color,
                1f to DEFAULT_GREEN.color,
            ),
        )
    }
    data class Solid(val color: Int) : ProgressBarColorStyle() {
        companion object : CodecAbleTypedCompanion<Solid> {
            override fun getCodec(): Codec<Solid> = Codec.INT.xmap(::Solid, Solid::color)
        }
    }
    data class Segmented(val segments: List<Pair<Float, Int>>) : ProgressBarColorStyle() {
        companion object : CodecAbleTypedCompanion<Segmented> {
            override fun getCodec(): Codec<Segmented> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.list(
                        RecordCodecBuilder.create<Pair<Float, Int>> { pairInstance ->
                            pairInstance.group(
                                Codec.FLOAT.fieldOf("threshold").forGetter(Pair<Float, Int>::first),
                                Codec.INT.fieldOf("color").forGetter(Pair<Float, Int>::second),
                            ).apply(pairInstance, ::Pair)
                        },
                    ).fieldOf("segments").forGetter(Segmented::segments),
                ).apply(instance, ::Segmented)
            }
        }
        init {
            require(segments.isNotEmpty()) { "Segments cannot be empty" }
            require(segments.all { it.first in 0f..1f }) { "All thresholds must be in range [0, 1]" }
            require(segments.sortedBy { it.first } == segments) { "Segments must be sorted by threshold" }
        }
    }
    data class Gradient(val startColor: Int, val endColor: Int) : ProgressBarColorStyle() {
        companion object : CodecAbleTypedCompanion<Gradient> {
            override fun getCodec(): Codec<Gradient> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.INT.fieldOf("startColor").forGetter(Gradient::startColor),
                    Codec.INT.fieldOf("endColor").forGetter(Gradient::endColor),
                ).apply(instance, ::Gradient)
            }
        }
    }
    data class MultiGradient(val colors: List<Pair<Float, Int>>) : ProgressBarColorStyle() {
        companion object : CodecAbleTypedCompanion<MultiGradient> {
            override fun getCodec(): Codec<MultiGradient> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.list(
                        RecordCodecBuilder.create<Pair<Float, Int>> { pairInstance ->
                            pairInstance.group(
                                Codec.FLOAT.fieldOf("position").forGetter(Pair<Float, Int>::first),
                                Codec.INT.fieldOf("color").forGetter(Pair<Float, Int>::second),
                            ).apply(pairInstance, ::Pair)
                        },
                    ).fieldOf("colors").forGetter(MultiGradient::colors),
                ).apply(instance, ::MultiGradient)
            }
        }
        init {
            require(colors.size >= 2) { "At least 2 colors required for gradient" }
            require(colors.all { it.first in 0f..1f }) { "All positions must be in range [0, 1]" }
            require(colors.sortedBy { it.first } == colors) { "Colors must be sorted by position" }
        }
    }
}

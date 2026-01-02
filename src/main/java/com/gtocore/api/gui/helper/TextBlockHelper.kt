package com.gtocore.api.gui.helper

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

import com.lowdragmc.lowdraglib.gui.util.DrawerHelper

/**
 * 文本块绘制辅助工具
 */
object TextBlockHelper {
    // region 主流程
    /**
     * 绘制文本块
     * @return 实际绘制尺寸 (width, height)
     */
    @OnlyIn(Dist.CLIENT)
    fun drawTextBlock(graphics: GuiGraphics, text: String, lineGap: Int = 1, scale: Float = 1f, color: Int = 0xFFFFFFFF.toInt(), maxWidth: Int = 40): Pair<Int, Int> {
        if (text.isEmpty()) return 0 to 0
        val textInfo = prepareTextInfo(text, maxWidth, lineGap)
        renderTextLines(graphics, textInfo.lines, lineGap, scale, color)
        return textInfo.size
    }

    /**
     * 计算文本块尺寸（不绘制）
     */
    @OnlyIn(Dist.CLIENT)
    fun calculateTextBlockSize(text: String, lineGap: Int = 1, maxWidth: Int = 40): Pair<Int, Int> {
        if (text.isEmpty()) return 0 to 0
        return prepareTextInfo(text, maxWidth, lineGap).size
    }
    // endregion

    // region 私有辅助
    private data class TextInfo(val lines: List<String>, val size: Pair<Int, Int>)

    /**
     * 预处理文本信息（换行和尺寸）
     */
    @OnlyIn(Dist.CLIENT)
    private fun prepareTextInfo(text: String, maxWidth: Int, lineGap: Int): TextInfo {
        val font = Minecraft.getInstance().font
        val wrappedLines = wrapText(text, font, maxWidth)
        val textHeight = font.lineHeight
        val actualWidth = wrappedLines.maxOfOrNull { font.width(it) } ?: 0
        val totalHeight = calculateTotalHeight(wrappedLines.size, textHeight, lineGap)
        return TextInfo(wrappedLines, actualWidth to totalHeight)
    }

    /**
     * 计算总高度
     */
    private fun calculateTotalHeight(lineCount: Int, textHeight: Int, lineGap: Int): Int {
        val adjustedTextHeight = textHeight - 2
        return if (lineCount <= 0) adjustedTextHeight else lineCount * adjustedTextHeight + (lineCount - 1) * lineGap
    }

    /**
     * 渲染文本行
     */
    @OnlyIn(Dist.CLIENT)
    private fun renderTextLines(graphics: GuiGraphics, lines: List<String>, lineGap: Int, scale: Float, color: Int) {
        val font = Minecraft.getInstance().font
        val adjustedTextHeight = font.lineHeight - 2
        lines.forEachIndexed { index, line ->
            val textY = index * (adjustedTextHeight + lineGap) - 1
            DrawerHelper.drawText(
                graphics,
                line,
                0f,
                textY.toFloat(),
                scale,
                color,
                false,
            )
        }
    }

    /**
     * 文本换行处理
     */
    private fun wrapText(text: String, font: Font, maxLineWidth: Int): List<String> {
        if (maxLineWidth <= 0) return listOf(text)
        return text.split(" ")
            .fold(mutableListOf<String>() to "") { (lines, currentLine), word ->
                processWord(lines, currentLine, word, font, maxLineWidth)
            }.let { (lines, lastLine) ->
                finalizeLinesWithLastLine(lines, lastLine)
            }
    }

    private fun processWord(lines: MutableList<String>, currentLine: String, word: String, font: Font, maxLineWidth: Int): Pair<MutableList<String>, String> {
        val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
        return when {
            font.width(testLine) <= maxLineWidth -> lines to testLine
            currentLine.isNotEmpty() -> {
                lines.add(currentLine)
                lines to word
            }
            else -> {
                val forceSplitLines = word.splitByWidth(font, maxLineWidth)
                lines.addAll(forceSplitLines)
                lines to ""
            }
        }
    }

    private fun finalizeLinesWithLastLine(lines: MutableList<String>, lastLine: String): List<String> = when {
        lastLine.isNotEmpty() -> lines.apply { add(lastLine) }
        lines.isEmpty() -> listOf("")
        else -> lines
    }

    private fun String.splitByWidth(font: Font, maxWidth: Int): List<String> = generateSequence(this) { remaining ->
        when {
            remaining.isEmpty() -> null
            else -> {
                val cutIndex = findCutIndex(remaining, font, maxWidth)
                remaining.drop(cutIndex).takeIf { it.isNotEmpty() }
            }
        }
    }.map { remaining ->
        val cutIndex = findCutIndex(remaining, font, maxWidth)
        remaining.take(cutIndex)
    }.toList()

    private fun findCutIndex(text: String, font: Font, maxWidth: Int): Int = (1..text.length)
        .takeWhile { font.width(text.take(it)) <= maxWidth }
        .lastOrNull() ?: 1
    // endregion
}

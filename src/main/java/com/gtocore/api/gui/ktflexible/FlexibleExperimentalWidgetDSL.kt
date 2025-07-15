package com.gtocore.api.gui.ktflexible

import com.gtocore.api.gui.graphic.helper.ProgressBarColorStyle
import com.gtocore.api.gui.graphic.helper.ProgressBarHelper
import com.gtocore.api.gui.graphic.helper.TextBlockHelper

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

import com.gtolib.api.gui.ktflexible.LayoutBuilder
import com.gtolib.api.gui.ktflexible.Style
import com.gtolib.api.gui.ktflexible.VBoxBuilder

import java.util.function.IntSupplier
import java.util.function.Supplier

fun LayoutBuilder<*>.progressBar(currentSupplier: IntSupplier, totalSupplier: IntSupplier, width: Int = 100, height: Int = 20, border: Int = 2, padding: Int = 1, progressColorStyle: ProgressBarColorStyle = ProgressBarColorStyle.DEFAULT_GREEN, backgroundColor: Int = 0xFF404040.toInt(), borderColor: Int = 0xFF000000.toInt(), textColor: Int = 0xFFFFFFFF.toInt(), showPercentage: Boolean = false) {
    val widget = object : SyncWidget(0, 0, width, height) {
        private val currentField = syncInt({ currentSupplier.asInt }, -1, 1)
        private val totalField = syncInt({ totalSupplier.asInt }, -2, 1)

        @OnlyIn(Dist.CLIENT)
        override fun drawInBackground(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTicks: Float) {
            super.drawInBackground(graphics, mouseX, mouseY, partialTicks)

            val safeTotal = maxOf(1, totalField.lastValue)
            val safeCurrent = currentField.lastValue.coerceIn(0, safeTotal)
            val percentage = ((safeCurrent.toFloat() / safeTotal.toFloat()) * 100).toInt()

            graphics.pose().pushPose()
            graphics.pose().translate(positionX.toFloat(), positionY.toFloat(), 0f)

            val (actualWidth, actualHeight) = ProgressBarHelper.drawProgressBarWithText(
                graphics = graphics,
                progress = percentage,
                totalWidth = width,
                totalHeight = height,
                text = if (showPercentage) "$percentage%" else "$safeCurrent / $safeTotal",
                borderWidth = border,
                progressColorStyle = progressColorStyle,
                backgroundColor = backgroundColor,
                borderColor = borderColor,
                textColor = textColor,
            )
            graphics.pose().popPose()
        }
    }
    widget(widget)
}

fun LayoutBuilder<*>.textBlock(textSupplier: Supplier<Component>, tab: Int = 0, maxWidth: Int = 40, textColor: Int? = null) {
    val widget = object : SyncWidget(0, 0, 100, 12) {
        private val textField = syncComponent({ textSupplier.get() }, -1, textSupplier.get())
        private val yPadding: Int = 1
        private val lineSpacing: Int = 1
        private var lastText: Component? = null

        override fun initWidget() {
            super.initWidget()
            updateSize()
        }

        private fun updateSize() {
            val text = textField.lastValue.string
            val availableWidth = maxWidth - tab
            val (actualWidth, totalHeight) = TextBlockHelper.calculateTextBlockSize(
                text = text,
                lineGap = lineSpacing,
                maxWidth = availableWidth,
            )

            setSize(actualWidth + tab, totalHeight + 2 * yPadding)
        }

        @OnlyIn(Dist.CLIENT)
        override fun drawInBackground(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTicks: Float) {
            super.drawInBackground(graphics, mouseX, mouseY, partialTicks)

            val currentText = textField.lastValue
            if (currentText != lastText) {
                lastText = currentText
                updateSize()
            }

            val text = currentText.string
            val availableWidth = maxWidth - tab

            // 使用TextBlockHelper绘制文本
            graphics.pose().pushPose()
            graphics.pose().translate((positionX + tab).toFloat(), (positionY + yPadding).toFloat(), 0f)

            val (actualWidth, actualHeight) = TextBlockHelper.drawTextBlock(
                graphics = graphics,
                text = text,
                lineGap = lineSpacing,
                scale = 1f,
                color = textColor ?: 0xFF000000.toInt(),
                maxWidth = availableWidth,
            )

            graphics.pose().popPose()
        }
    }
    widget(widget)
}
class MultiPageDSLBuilder {
    private val pageSuppliers: MutableList<Supplier<VBoxBuilder.() -> Unit>> = mutableListOf()
    fun page(box: VBoxBuilder.() -> Unit) {
        pageSuppliers.add({ box })
    }
    fun build(): List<Supplier<VBoxBuilder.() -> Unit>> = pageSuppliers
}
interface MultiPageVScroll {
    fun refresh()
    fun getMaxPage(): Int
}
fun LayoutBuilder<*>.multiPage(width: Int, height: Int, style: (Style.() -> Unit)? = null, pageSelector: IntSupplier, builder: MultiPageDSLBuilder.() -> Unit): MultiPageVScroll {
    val widget = object : SyncWidget(0, 0, width, height), MultiPageVScroll {
        val currentPage = syncInt({ pageSelector.asInt }, -1, pageSelector.asInt).apply {
            init = { if (!isRemote)refresh() }
            update = { old, new ->
                if (!isRemote)refresh()
            }
        }
        val pageSuppliers: MutableList<Supplier<VBoxBuilder.() -> Unit>> = mutableListOf()
        init {
            with(MultiPageDSLBuilder()) {
                builder()
                pageSuppliers.addAll(build())
            }
        }
        override fun refresh() {
            clearAllWidgets()
            val receiver = pageSuppliers[currentPage.lastValue].get()
            val vBoxBuilder = VBoxBuilder(width = width, style = style?.run { Style().apply { style() } } ?: Style { spacing = 0 })
            vBoxBuilder.buildAndInit(receiver)
            addWidget(vBoxBuilder.getBuiltWidget())
            this.initWidget()
        }

        override fun getMaxPage(): Int = pageSuppliers.size - 1
    }
    widget(widget)
    return widget as MultiPageVScroll
}

package com.gtocore.client.screen

import com.gtocore.common.forge.ClientForge

import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSequence

import com.gtolib.api.annotation.DataGeneratorScanned
import com.gtolib.api.annotation.language.RegisterLanguage

import com.gtocore.client.screen.MessageListScreen as MessageListScreen1

@DataGeneratorScanned
class MessageScreen(private val message: ClientForge.MessageDefinition, private val currentPage: Int, private val totalPages: Int, private val onConfirm: () -> Unit, private val onExpand: (() -> Unit)? = null, private val onMarkAll: (() -> Unit)? = null) : Screen(Component.translatable(TITLE_KEY)) {

    // Scrolling support
    private var scrollOffset = 0.0
    private var totalContentHeight = 0
    private var maxScrollOffset = 0.0

    // Cache wrapped lines for click detection
    private val wrappedLinesCache = mutableListOf<Pair<Component, List<FormattedCharSequence>>>()

    @DataGeneratorScanned
    companion object {
        @RegisterLanguage(cn = "GTO 消息系统", en = "GTO Message System")
        const val TITLE_KEY = "gto.message.title"

        @RegisterLanguage(cn = "✅ 知道了", en = "✅ Got it")
        const val GOT_IT_KEY = "gto.message.gotit"

        @RegisterLanguage(cn = "📜 展示历史", en = "📜 Show Historical")
        const val SHOW_HISTORICAL_KEY = "gto.message.show_historical"

        @RegisterLanguage(cn = "✅ 标记全部为已读", en = "✅ Mark All Read")
        const val MARK_ALL_KEY = "gto.message.mark_all"

        @RegisterLanguage(cn = "🔗 打开链接", en = "🔗 Open Link")
        const val OPEN_LINK_KEY = "gto.message.open_link"

        @RegisterLanguage(cn = "版本：%s | 日期：%s | 页：%d/%d", en = "Version: %s | Date: %s | Page %d/%d")
        const val VERSION_DATE_PAGE_KEY = "gto.message.version_date_page"

        @RegisterLanguage(cn = "📋", en = "📋")
        const val LIST_ICON_KEY = "gto.message.list_icon"

        @RegisterLanguage(cn = "✅ 所有消息已确认！", en = "✅ All messages confirmed!")
        const val ALL_CONFIRMED_KEY = "gto.message.all_confirmed"

        @RegisterLanguage(cn = "✅ 所有最近消息已确认！", en = "✅ All recent messages confirmed!")
        const val ALL_RECENT_CONFIRMED_KEY = "gto.message.all_recent_confirmed"

        @RegisterLanguage(cn = "您有更早的消息（>30天）", en = "You have older messages (>30 days)")
        const val OLDER_MESSAGES_KEY = "gto.message.older_messages"

        @RegisterLanguage(cn = "您想查看它们吗？", en = "Would you like to view them?")
        const val VIEW_THEM_KEY = "gto.message.view_them"

        @RegisterLanguage(cn = "✅ 已标记 %d 条消息为已读！", en = "✅ Marked %d message(s) as read!")
        const val MARKED_READ_KEY = "gto.message.marked_read"

        @RegisterLanguage(cn = "📜 正在显示历史消息...", en = "📜 Showing historical messages...")
        const val SHOWING_HISTORICAL_KEY = "gto.message.showing_historical"

        @RegisterLanguage(cn = "提示：你随时可以通过 /gtocorec message 访问这些信息", en = "Tip: You can access these messages anytime via /gtocorec message")
        const val COMMAND_TIP_KEY = "gto.message.command_tip"
    }

    override fun init() {
        super.init()

        // Pre-calculate wrapped lines and total content height
        val contentWidth = (this.width * 0.6).toInt()
        val lineHeight = 12

        wrappedLinesCache.clear()
        totalContentHeight = 0

        message.messages.forEach { msg ->
            val wrappedLines = this.font.split(msg, contentWidth)
            wrappedLinesCache.add(Pair(msg, wrappedLines))
            totalContentHeight += wrappedLines.size * lineHeight + 5 // 5 for spacing between messages
        }

        // Calculate max scroll offset based on visible area
        val contentStartY = 70
        val boxBottom = this.height - 50
        val visibleHeight = boxBottom - contentStartY - 15 // Leave some margin
        maxScrollOffset = (totalContentHeight - visibleHeight).coerceAtLeast(0).toDouble()
        scrollOffset = 0.0 // Reset scroll on init

        val buttonWidth = 120
        val buttonHeight = 20
        val buttonSpacing = 10
        val startY = this.height - 40

        // 右上角消息列表按钮
        val listButton = Button.builder(
            Component.translatable(LIST_ICON_KEY),
        ) { _ ->
            this.minecraft?.setScreen(MessageListScreen1())
        }.bounds(
            this.width - 30,
            5,
            25,
            20,
        ).build()
        this.addRenderableWidget(listButton)

        // 布局：将可见按钮居中排列，避免重叠
        val buttonEntries = mutableListOf<Pair<Component, (Button) -> Unit>>()

        // 左侧：展开历史（如果存在）
        onExpand?.let { expandCallback ->
            val comp = Component.translatable(SHOW_HISTORICAL_KEY).withStyle(Style.EMPTY.withColor(ChatFormatting.AQUA))
            buttonEntries.add(Pair(comp) { _: Button -> expandCallback() })
        }

        // 中间：确认
        val confirmComp = Component.translatable(GOT_IT_KEY).withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN))
        buttonEntries.add(
            Pair(confirmComp) { _: Button ->
                onConfirm()
                // 不要关闭screen，让onConfirm回调处理下一条消息的显示
            },
        )

        // 右侧：标记全部已读（如果存在）
        onMarkAll?.let { markAllCallback ->
            val comp = Component.translatable(MARK_ALL_KEY).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
            buttonEntries.add(
                Pair(comp) { _: Button ->
                    markAllCallback()
                    this.onClose()
                },
            )
        }

        // 计算总宽度并居中放置按钮组
        val visibleCount = buttonEntries.size
        if (visibleCount > 0) {
            val totalGroupWidth = visibleCount * buttonWidth + (visibleCount - 1) * buttonSpacing
            val centerX = this.width / 2 - totalGroupWidth / 2
            val minX = 10
            // 保留右侧空间，避免与右上角的列表按钮重叠（列表按钮大约占据 width-30..width-5）
            val reservedRight = 40
            val maxX = (this.width - reservedRight - totalGroupWidth).coerceAtLeast(minX)
            var x = centerX.coerceIn(minX, maxX)

            buttonEntries.forEach { (comp, action) ->
                val btn = Button.builder(comp) { b -> action(b) }
                    .bounds(x, startY, buttonWidth, buttonHeight)
                    .build()
                this.addRenderableWidget(btn)
                x += buttonWidth + buttonSpacing
            }
        }
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        // 渲染半透明背景
        renderBackground(guiGraphics)

        super.render(guiGraphics, mouseX, mouseY, partialTick)

        val titleY = 30
        val contentStartY = 70
        val lineHeight = 12
        var currentY = contentStartY - scrollOffset.toInt()

        // 计算文本区域 - 左边20%空白，中间60%正文，右边20%空白
        val leftMargin = (this.width * 0.2).toInt()
        val contentWidth = (this.width * 0.6).toInt()
        val rightMargin = leftMargin + contentWidth

        // 渲染装饰性背景框
        val boxTop = 20
        val boxBottom = this.height - 50
        guiGraphics.fill(leftMargin - 10, boxTop, rightMargin + 10, boxBottom, 0x90000000.toInt())

        // 渲染边框（高亮效果）
        guiGraphics.hLine(leftMargin - 10, rightMargin + 10, boxTop, 0xFF4A90E2.toInt())
        guiGraphics.hLine(leftMargin - 10, rightMargin + 10, boxBottom, 0xFF4A90E2.toInt())
        guiGraphics.vLine(leftMargin - 10, boxTop, boxBottom, 0xFF4A90E2.toInt())
        guiGraphics.vLine(rightMargin + 10, boxTop, boxBottom, 0xFF4A90E2.toInt())

        // 渲染标题（居中，带背景）
        val titleBgLeft = this.width / 2 - 100
        val titleBgRight = this.width / 2 + 100
        guiGraphics.fill(titleBgLeft, titleY - 8, titleBgRight, titleY + 15, 0xCC1E3A5F.toInt())
        guiGraphics.drawCenteredString(
            this.font,
            Component.translatable(TITLE_KEY).withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withBold(true)),
            this.width / 2,
            titleY,
            0xFFFFFF,
        )

        // 渲染版本和日期信息（居中，带图标）
        guiGraphics.drawCenteredString(
            this.font,
            Component.translatable(VERSION_DATE_PAGE_KEY, message.gameVersion, message.formatDate(), currentPage, totalPages),
            this.width / 2,
            titleY + 20,
            ChatFormatting.AQUA.color ?: 0x55AAFF,
        )

        // 渲染装饰性分隔线（渐变效果）
        for (i in 0..2) {
            val alpha = (0x60 - i * 0x20).coerceIn(0x20, 0xFF)
            guiGraphics.hLine(
                leftMargin,
                rightMargin,
                contentStartY - 5 + i,
                (alpha shl 24) or 0x4A90E2,
            )
        }

        // 开启剪裁测试，以裁剪超出可见区域的内容
        val scissorTop = contentStartY
        val scissorBottom = boxBottom - 10
        guiGraphics.enableScissor(leftMargin - 10, scissorTop, rightMargin + 10, scissorBottom)

        // 渲染消息内容（左对齐）并支持滚动
        wrappedLinesCache.forEach { (_, wrappedLines) ->
            wrappedLines.forEach { line ->
                // 仅在剪裁区域内可见时渲染
                if (currentY + lineHeight >= scissorTop && currentY <= scissorBottom) {
                    guiGraphics.drawString(
                        this.font,
                        line,
                        leftMargin,
                        currentY,
                        0xFFFFFF,
                    )
                }
                currentY += lineHeight
            }
            currentY += 5 // 消息之间的额外间距
        }

        // 禁用剪裁测试
        guiGraphics.disableScissor()

        // 渲染底部装饰性分隔线
        for (i in 0..2) {
            val alpha = (0x60 - i * 0x20).coerceIn(0x20, 0xFF)
            guiGraphics.hLine(
                leftMargin,
                rightMargin,
                this.height - 60 + i,
                (alpha shl 24) or 0x4A90E2,
            )
        }

        // 渲染命令提示信息（在底部按钮上方）
        val tipText = Component.translatable(COMMAND_TIP_KEY).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true))
        guiGraphics.drawCenteredString(
            this.font,
            tipText,
            this.width / 2,
            this.height - 55,
            ChatFormatting.GRAY.color ?: 0xAAAAAA,
        )

        // 如果内容可滚动，则渲染滚动指示器
        if (maxScrollOffset > 0) {
            val scrollBarX = rightMargin + 5
            val scrollBarTop = scissorTop + 5
            val scrollBarBottom = scissorBottom - 5
            val scrollBarHeight = scrollBarBottom - scrollBarTop

            // 背景轨道
            guiGraphics.fill(scrollBarX, scrollBarTop, scrollBarX + 3, scrollBarBottom, 0x80FFFFFF.toInt())

            // 滚动条滑块
            val thumbHeight = ((scrollBarHeight * scrollBarHeight) / (scrollBarHeight + maxScrollOffset)).toInt().coerceAtLeast(20)
            val thumbY = scrollBarTop + ((scrollOffset / maxScrollOffset) * (scrollBarHeight - thumbHeight)).toInt()
            guiGraphics.fill(scrollBarX, thumbY, scrollBarX + 3, thumbY + thumbHeight, 0xFF4A90E2.toInt())
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true
        }

        // 处理消息文本中的点击事件（特别是链接）
        val contentStartY = 70
        val lineHeight = 12
        var currentY = contentStartY - scrollOffset.toInt()

        val leftMargin = (this.width * 0.2).toInt()
        val contentWidth = (this.width * 0.6).toInt()
        val boxBottom = this.height - 50

        // 检查点击是否在可滚动内容区域内
        val scissorTop = contentStartY
        val scissorBottom = boxBottom - 10

        // 仅处理在可见剪裁区域内的点击
        if (mouseY < scissorTop || mouseY > scissorBottom) {
            return false
        }

        // 检查应用滚动偏移后，点击是否在消息文本区域内
        wrappedLinesCache.forEach { (msg, wrappedLines) ->
            wrappedLines.forEach { line ->
                // 检查鼠标是否在这一行文本范围内（包括滚动偏移）
                if (mouseY.toInt() in currentY..(currentY + lineHeight)) {
                    // 检查 X 坐标是否在文本区域内
                    if (mouseX.toInt() >= leftMargin && mouseX.toInt() <= leftMargin + contentWidth) {
                        // 使用原始消息的 style 处理点击
                        if (this.handleComponentClicked(msg.style)) {
                            return true
                        }
                    }
                }
                currentY += lineHeight
            }
            currentY += 5
        }

        return false
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, delta: Double): Boolean {
        if (maxScrollOffset > 0) {
            // 滚动速度：每个滚动刻度3行
            val scrollAmount = delta * 36.0
            scrollOffset = (scrollOffset - scrollAmount).coerceIn(0.0, maxScrollOffset)
            return true
        }
        return super.mouseScrolled(mouseX, mouseY, delta)
    }

    override fun renderBackground(guiGraphics: GuiGraphics) {
        // 自定义背景渲染（如果需要）
        // guiGraphics.fill(0, 0, this.width, this.height, 0xFF000000.toInt()) // 示例：全黑背景
    }

    override fun isPauseScreen(): Boolean {
        return false // 不暂停游戏
    }

    override fun shouldCloseOnEsc(): Boolean {
        return true // 允许通过ESC关闭
    }
}

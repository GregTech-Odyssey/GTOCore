package com.gtocore.client.screen

import com.gtocore.common.forge.ClientForge

import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ObjectSelectionList
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style

import com.gtolib.api.annotation.DataGeneratorScanned
import com.gtolib.api.annotation.language.RegisterLanguage

@DataGeneratorScanned
class MessageListScreen : Screen(Component.translatable(TITLE_KEY)) {
    @DataGeneratorScanned
    companion object {
        @RegisterLanguage(cn = "消息列表", en = "Message List")
        const val TITLE_KEY = "gto.message.list_title"

        @RegisterLanguage(cn = "返回", en = "Back")
        const val BACK_KEY = "gto.message.back"

        @RegisterLanguage(cn = "关闭", en = "Close")
        const val CLOSE_KEY = "gto.message.close"

        @RegisterLanguage(cn = "查看详情", en = "View Details")
        const val VIEW_DETAILS_KEY = "gto.message.view_details"

        @RegisterLanguage(cn = "✅ 已读", en = "✅ Read")
        const val READ_KEY = "gto.message.read"

        @RegisterLanguage(cn = "🔔 未读", en = "🔔 Unread")
        const val UNREAD_KEY = "gto.message.unread"

        @RegisterLanguage(cn = "全部标记为已读", en = "Mark All as Read")
        const val MARK_ALL_READ_KEY = "gto.message.mark_all_as_read"

        @RegisterLanguage(cn = "消息确认，显示下一条...", en = "Message confirmed. Showing next message...")
        const val CONFIRM_NEXT_KEY = "gto.message.confirm_next"

        @RegisterLanguage(cn = "没有待处理的消息。", en = "No pending messages.")
        const val NO_PENDING_KEY = "gto.message.no_pending"
    }

    private lateinit var messageList: MessageListWidget
    private val config = ClientForge.loadConfig()

    override fun init() {
        super.init()

        val langCode = Minecraft.getInstance().languageManager.selected

        // 获取所有应该显示的消息，按时间排序（新到旧）
        val messages = ClientForge.MESSAGE_DEFINITIONS
            .filter { it.shouldShow(langCode) }
            .sortedWith(
                compareByDescending<ClientForge.MessageDefinition> { it.priority }
                    .thenByDescending { it.dateString },
            )

        // 创建消息列表
        messageList = MessageListWidget(
            this.minecraft!!,
            this.width,
            this.height - 64,
            32,
            this.height - 32,
            50,
            messages,
            config,
        )
        this.addWidget(messageList)

        // 检查是否有未读消息
        val unreadMessages = messages.filter { !config.isConfirmed(it.contentHash) }
        val hasUnread = unreadMessages.isNotEmpty()

        // 返回/关闭按钮 - 根据是否有未读消息决定行为
        val backButton = Button.builder(
            Component.translatable(if (hasUnread) BACK_KEY else CLOSE_KEY),
        ) { _ ->
            if (hasUnread) {
                // 有未读消息，跳转到最新未读消息
                val latestUnread = unreadMessages.first()
                val page = messages.indexOf(latestUnread) + 1
                val total = messages.size

                val onConfirm: () -> Unit = {
                    config.confirmMessage(latestUnread.contentHash, latestUnread.id)
                    ClientForge.saveConfig(config)

                    // 查找下一条未读消息
                    val remainingUnread = messages.filter { !config.isConfirmed(it.contentHash) }
                    if (remainingUnread.isNotEmpty()) {
                        // 返回消息列表
                        this.minecraft?.setScreen(MessageListScreen())
                    } else {
                        // 没有更多未读消息，关闭
                        this.minecraft?.setScreen(null)
                    }
                }

                this.minecraft?.setScreen(MessageScreen(latestUnread, page, total, onConfirm))
            } else {
                // 没有未读消息，直接关闭
                this.onClose()
            }
        }.bounds(
            this.width / 2 - 155,
            this.height - 28,
            150,
            20,
        ).build()
        this.addRenderableWidget(backButton)

        // 全部标记为已读按钮
        val markAllButton = Button.builder(
            Component.translatable(MARK_ALL_READ_KEY),
        ) { _ ->
            messages.forEach { msg ->
                config.confirmMessage(msg.contentHash, msg.id)
            }
            ClientForge.saveConfig(config)
            this.minecraft?.setScreen(null)
        }.bounds(
            this.width / 2 + 5,
            this.height - 28,
            150,
            20,
        ).build()
        this.addRenderableWidget(markAllButton)
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        // 渲染背景
        renderBackground(guiGraphics)

        // 渲染列表
        messageList.render(guiGraphics, mouseX, mouseY, partialTick)

        // 渲染标题
        guiGraphics.drawCenteredString(
            this.font,
            this.title,
            this.width / 2,
            16,
            0xFFFFFF,
        )

        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (messageList.mouseClicked(mouseX, mouseY, button)) {
            return true
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean = messageList.mouseReleased(mouseX, mouseY, button) || super.mouseReleased(mouseX, mouseY, button)

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean = messageList.mouseDragged(mouseX, mouseY, button, dragX, dragY) || super.mouseDragged(mouseX, mouseY, button, dragX, dragY)

    override fun mouseScrolled(mouseX: Double, mouseY: Double, delta: Double): Boolean = messageList.mouseScrolled(mouseX, mouseY, delta) || super.mouseScrolled(mouseX, mouseY, delta)

    // 消息列表组件
    class MessageListWidget(minecraft: Minecraft, width: Int, height: Int, top: Int, bottom: Int, itemHeight: Int, messages: List<ClientForge.MessageDefinition>, config: ClientForge.MessageConfig) : ObjectSelectionList<MessageListWidget.MessageEntry>(minecraft, width, height, top, bottom, itemHeight) {

        init {
            messages.forEach { msg ->
                this.addEntry(MessageEntry(msg, config))
            }
        }

        override fun getRowWidth(): Int {
            // 根据屏幕宽度自适应：使用屏幕宽度的70%，但不小于400，不大于800
            return (this.width * 0.7).toInt().coerceIn(400, 800)
        }

        override fun getScrollbarPosition(): Int {
            // 滚动条位置：内容宽度的右侧 + 10像素边距
            return this.width / 2 + rowWidth / 2 + 10
        }

        class MessageEntry(private val message: ClientForge.MessageDefinition, private val config: ClientForge.MessageConfig) : Entry<MessageEntry>() {

            override fun render(guiGraphics: GuiGraphics, index: Int, top: Int, left: Int, width: Int, height: Int, mouseX: Int, mouseY: Int, hovering: Boolean, partialTick: Float) {
                val minecraft = Minecraft.getInstance()
                val font = minecraft.font
                val isRead = config.isConfirmed(message.contentHash)

                // 背景
                val bgColor = if (hovering) 0x80FFFFFF.toInt() else 0x40000000
                guiGraphics.fill(left, top, left + width, top + height, bgColor)

                // 状态指示器
                val statusText = if (isRead) {
                    Component.translatable(READ_KEY).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
                } else {
                    Component.translatable(UNREAD_KEY).withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN))
                }
                guiGraphics.drawString(font, statusText, left + 5, top + 5, 0xFFFFFF)

                // 版本和日期
                val versionDate = "${message.gameVersion} | ${message.formatDate()}"
                guiGraphics.drawString(
                    font,
                    versionDate,
                    left + width - font.width(versionDate) - 5,
                    top + 5,
                    ChatFormatting.AQUA.color ?: 0xFFFFFF,
                )

                // 消息预览（第一行）- 根据可用宽度自适应
                val preview = message.messages.firstOrNull()?.string ?: ""
                // 计算可用宽度：总宽度 - 左边距 - 右边距 - 优先级图标空间
                val availableWidth = width - 30
                val maxChars = (availableWidth / font.width("W")).coerceAtLeast(20)
                val trimmedPreview = if (preview.length > maxChars) "${preview.take(maxChars)}..." else preview
                guiGraphics.drawString(
                    font,
                    trimmedPreview,
                    left + 5,
                    top + 20,
                    if (isRead) ChatFormatting.GRAY.color ?: 0xAAAAAA else 0xFFFFFF,
                )

                // 优先级标签
                if (message.priority > 0) {
                    val priorityText = "⭐"
                    guiGraphics.drawString(
                        font,
                        priorityText,
                        left + width - 20,
                        top + 20,
                        ChatFormatting.GOLD.color ?: 0xFFAA00,
                    )
                }

                // 边框
                guiGraphics.hLine(left, left + width, top + height - 1, 0xFF444444.toInt())
            }

            override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
                if (button == 0) {
                    // 点击打开详情
                    val minecraft = Minecraft.getInstance()
                    val langCode = minecraft.languageManager.selected

                    // 获取所有可见消息用于计算页码
                    val allMessages = ClientForge.MESSAGE_DEFINITIONS
                        .filter { it.shouldShow(langCode) }
                        .sortedWith(
                            compareByDescending<ClientForge.MessageDefinition> { it.priority }
                                .thenByDescending { it.dateString },
                        )

                    val currentIndex = allMessages.indexOf(message)
                    val page = currentIndex + 1
                    val total = allMessages.size

                    // 创建回调
                    val onConfirm = {
                        config.confirmMessage(message.contentHash, message.id)
                        ClientForge.saveConfig(config)
                        // 返回列表
                        minecraft.setScreen(MessageListScreen())
                    }

                    minecraft.setScreen(MessageScreen(message, page, total, onConfirm))
                    return true
                }
                return false
            }

            override fun getNarration(): Component = Component.literal("${message.gameVersion} - ${message.formatDate()}")
        }
    }
}

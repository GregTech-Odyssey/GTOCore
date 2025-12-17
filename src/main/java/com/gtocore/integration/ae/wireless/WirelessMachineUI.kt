package com.gtocore.integration.ae.wireless

import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.common.saved.WirelessSavedData
import com.gtocore.config.GTOConfig
import com.gtocore.integration.ae.wireless.WirelessMachine.*

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

import appeng.core.definitions.AEItems
import appeng.core.localization.InGameTooltip
import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.gtolib.api.gui.ktflexible.*
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture
import com.lowdragmc.lowdraglib.gui.texture.TextTexture
import com.lowdragmc.lowdraglib.gui.util.ClickData
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget
import com.lowdragmc.lowdraglib.gui.widget.Widget

import java.util.function.Supplier

fun getSetupFancyUIProvider(self: WirelessMachine): IFancyUIProvider = object : IFancyUIProvider {
    override fun getTabIcon(): IGuiTexture = ItemStackTexture(AEItems.WIRELESS_RECEIVER.stack())

    override fun getTitle(): Component? = Component.translatable(gridNodeSelector)

    override fun createMainPage(p0: FancyMachineUIWidget?): Widget {
        return rootFresh(176, 166) {
            if (GTOConfig.INSTANCE.aeLog) println(1)
            // 移除页面打开即同步，避免触发刷新循环；改为由机器加载与按钮操作驱动同步
            hBox(height = availableHeight, { spacing = 4 }) {
                blank()
                vBox(width = this@rootFresh.availableWidth - 4, style = { spacing = 4 }) {
                    blank()
                    if (!self.allowThisMachineConnectToWirelessGrid()) {
                        textBlock(
                            maxWidth = availableWidth - 4,
                            textSupplier = { Component.translatable(banned) },
                        )
                        return@vBox
                    }
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = { Component.translatable(player, self.self().playerOwner?.name ?: "无") },
                    )
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = {
                            val id = self.wirelessMachinePersisted0.gridConnectedName
                            val nick =
                                self.wirelessMachineRunTime0.gridCache.get().firstOrNull { it.name == id }?.nickname
                            Component.translatable(currentlyConnectedTo, (nick ?: id).ifEmpty { "无" })
                        },
                    )
                    // 重新加入“创建网络”输入与按钮
                    hBox(height = 16, { spacing = 4 }) {
                        field(
                            width = 60,
                            getter = { self.wirelessMachineRunTime0.gridWillAdded },
                            setter = { self.wirelessMachineRunTime0.gridWillAdded = it },
                        )
                        button(transKet = createGrid, width = this@vBox.availableWidth - 60 - 8) { ck ->
                            if (!ck.isRemote) {
                                val input = self.wirelessMachineRunTime0.gridWillAdded.trim()
                                if (input.isNotEmpty() &&
                                    self.wirelessMachineRunTime0.gridCache.get().none { it.nickname == input }
                                ) {
                                    WirelessSavedData.createNewGrid(
                                        input,
                                        self.requesterUUID,
                                    )
                                    self.wirelessMachineRunTime0.gridWillAdded = ""
                                    self.refreshCachesOnServer()
                                }
                            }
                        }
                    }
                    button(width = availableWidth - 4, transKet = leave) { ck ->
                        if (!ck.isRemote) {
                            self.leaveGrid()
                        }
                    }
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = {
                            Component.translatable(
                                globalWirelessGrid,
                                self.wirelessMachineRunTime0.gridAccessibleCache.get().count(),
                                self.wirelessMachineRunTime0.gridCache.get().count(),
                            )
                        },
                    )
                    textBlock(
                        maxWidth = availableWidth - 4,
                        textSupplier = { Component.translatable(yourWirelessGrid) },
                    )
                    val availableHeight = 166 - ((4 * 10) + (1 * 16) + 16 + (4 * 7))
                    val finalListHeight = maxOf(0, (((availableHeight / 16) + 1) * 16) - 2)
                    vScroll(width = availableWidth, height = finalListHeight, { spacing = 2 }) a@{
                        self.wirelessMachineRunTime0.gridAccessibleCache.get()
                            .forEach { grid ->
                                hBox(height = 14, { spacing = 4 }) {
                                    button(
                                        height = 14,
                                        text = {
                                            "${
                                                if (grid.isDefault) "⭐" else ""
                                            }${
                                                grid.nickname
                                            }(${
                                                InGameTooltip.Channels.text(
                                                    WirelessSavedData.INSTANCE.gridPool.find { it.name == grid.name }?.getTotalUsedChannel(),
                                                ).string
                                            })"
                                        },
                                        width = this@a.availableWidth - 48 - 8 + 12 - 4 - 18,
                                        onClick = {
                                            if (!it.isRemote) {
                                                self.leaveGrid()
                                                self.joinGrid(grid.name)
                                            }
                                        },
                                    )
                                    if (!grid.isDefault) {
                                        button(height = 14, text = { "⭐" }, width = 18, onClick = {
                                            if (!it.isRemote) {
                                                WirelessSavedData.setAsDefault(grid.name, self.requesterUUID)
                                                self.refreshCachesOnServer()
                                            }
                                        })
                                    } else {
                                        button(height = 14, text = { "⚝" }, width = 18, onClick = {
                                            if (!it.isRemote) {
                                                WirelessSavedData.cancelAsDefault(
                                                    grid.name,
                                                    self.requesterUUID,
                                                )
                                                self.refreshCachesOnServer()
                                            }
                                        })
                                    }
                                    deleteButton(height = 14, transKey = removeGrid, width = 36, onConfirm = {
                                        if (!it.isRemote) {
                                            WirelessSavedData.removeGrid(grid.name, self.requesterUUID)
                                            self.refreshCachesOnServer()
                                        }
                                    })
                                }
                            }
                    }
                }
            }
        }.also { self.wirelessMachineRunTime0.connectPageFreshRun = Runnable { it.requireFresh() } }
    }
}

const val confirmTime = 1000L

private fun LayoutBuilder<*>.deleteButton(width: Int = 40, height: Int = 16, text: Supplier<String>? = null, transKey: String? = null, onConfirm: ((ClickData) -> Unit)? = null, onSimpleClick: (() -> Unit)? = null) {
    val textTexture = when {
        text != null -> TextTexture(text)
        transKey != null -> TextTexture { Component.translatable(transKey).string }
        else -> TextTexture { "Button" }
    }

    val button = object : ButtonWidget(
        0,
        0,
        width,
        height,
        GuiTextureGroup(GuiTextures.BUTTON, textTexture),
        { clickData ->
            onConfirm?.invoke(clickData) ?: onSimpleClick?.invoke()
        },
    ) {
        var holdStart: Long = 0
        var holding: Boolean = false

        override fun mouseClicked(mouseX: Double, mouseY: Double, button0: Int): Boolean {
            // 若按下在按钮范围内，开始计时
            if (this.isMouseOverElement(mouseX, mouseY) && !holding) {
                holding = true
                holdStart = System.currentTimeMillis()
                return true
            } else {
                return super.mouseClicked(mouseX, mouseY, button0)
            }
        }

        override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {
            if (holding) {
                if (!this.isMouseOverElement(mouseX, mouseY)) {
                    holding = false
                }
            }
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY)
        }

        override fun mouseMoved(mouseX: Double, mouseY: Double): Boolean {
            if (holding) {
                if (!this.isMouseOverElement(mouseX, mouseY)) {
                    holding = false
                }
            }
            return super.mouseMoved(mouseX, mouseY)
        }

        override fun mouseReleased(mouseX: Double, mouseY: Double, button0: Int): Boolean {
            if (holding) {
                val elapsed = System.currentTimeMillis() - holdStart
                if (elapsed >= confirmTime) {
                    this.mouseClicked(mouseX, mouseY, button0)
                }
                holding = false
            }
            return super.mouseReleased(mouseX, mouseY, button0)
        }

        override fun drawInForeground(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTicks: Float) {
            super.drawInForeground(graphics, mouseX, mouseY, partialTicks)

            // 绘制红色半透明进度条
            val now = System.currentTimeMillis()
            val elapsed = when {
                holding -> (now - holdStart).coerceAtLeast(0L)
                else -> 0L
            }
            val progress = (elapsed.toDouble() / confirmTime.toDouble()).coerceIn(0.0, 1.0)
            if (progress > 0.0) {
                val px = (this.positionX + 2)
                val py = (this.positionY + this.sizeHeight - 4)
                val pw = ((this.sizeWidth - 4) * progress).toInt()
                val color = 0x88FF0000.toInt() // 半透明红色 ARGB
                graphics.fill(px, py, px + pw, py + 2, color)
            }
        }
    }

    widget(button)
}

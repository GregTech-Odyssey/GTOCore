package com.gtocore.integration.ae.wireless

import com.gtocore.common.saved.TopologyNodeEntry
import com.gtocore.common.saved.TopologySummary
import com.gtocore.common.saved.WirelessNetworkSavedData

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items

import appeng.core.definitions.AEItems
import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture
import com.lowdragmc.lowdraglib.gui.texture.TextTexture
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget
import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup
import com.lowdragmc.lowdraglib.utils.Position
import com.lowdragmc.lowdraglib.utils.Size

// ============================================================================================
//  主页面 — 网络选择器
// ============================================================================================

/**
 * 无线网络管理 GUI — 主页面。
 *
 * 所有 UI 可变状态（连接标记、星标、节点类型显示等）完全来自 ISync 字段，
 * 不依赖 @DescSynced（走块同步路径，与 GUI 容器同步时机不一致）。
 *
 * ButtonWidget 贴图/颜色在创建时固定，任何状态变动都需完整重建 content WidgetGroup。
 * 哨兵组件在 updateScreen（客户端）和 detectAndSendChanges（服务端）中跟踪
 * 复合哈希（networkListCache + nodeTypeSync + unassignedOutputCount），
 * 保持双端 widget 树结构同步以确保按钮点击准确。
 */
fun createWirelessUIProvider(machine: WirelessMachine): IFancyUIProvider = object : IFancyUIProvider {

    override fun getTabIcon(): IGuiTexture = ItemStackTexture(AEItems.WIRELESS_RECEIVER.stack())

    override fun getTitle(): Component = Component.translatable(WirelessMachine.KEY_NODE_SELECTOR)

    override fun createMainPage(parent: FancyMachineUIWidget?): Widget {
        val W = 170
        val H = 160
        val root = WidgetGroup(0, 0, W, H)
        root.setBackground(GuiTextures.BACKGROUND)

        // --- Ban check (static) ---
        if (!machine.allowWirelessConnection()) {
            root.addWidget(LabelWidget(6, 6, Component.translatable(WirelessMachine.KEY_BANNED).string))
            return root
        }

        // Dynamic content group — cleared & rebuilt on any state change
        val content = WidgetGroup(0, 0, W, H)
        root.addWidget(content)

        // Mutable state that survives rebuilds (captured by reference)
        var createInput = ""
        val pendingDelete = mutableSetOf<String>()
        var localRebuildNeeded = false

        // ── Build function — populates content from scratch ──────────────
        fun buildContent() {
            content.clearAllWidgets()
            var y = 3

            // Node type — read from ISync field (not @DescSynced)
            val nodeTypeOrd = machine.nodeTypeSync.get()
            val currentNodeType = WirelessMachine.NodeType.entries.getOrElse(nodeTypeOrd) {
                WirelessMachine.NodeType.SOURCE
            }

            // Player + node type (single row, compact)
            val playerName = machine.requesterUUID.toString().take(8)
            val typeName = if (currentNodeType == WirelessMachine.NodeType.SOURCE) {
                Component.translatable(WirelessMachine.KEY_SOURCE_NODE).string
            } else {
                Component.translatable(WirelessMachine.KEY_CHILD_NODE).string
            }
            content.addWidget(
                LabelWidget(5, y, Component.translatable(WirelessMachine.KEY_PLAYER, playerName).string),
            )
            content.addWidget(
                LabelWidget(90, y, Component.translatable(WirelessMachine.KEY_NODE_TYPE, typeName).string),
            )
            y += 11

            // Toggle button (only for switchable machines)
            if (machine.supportsNodeTypeSwitching()) {
                val targetName = if (currentNodeType == WirelessMachine.NodeType.SOURCE) {
                    Component.translatable(WirelessMachine.KEY_CHILD_NODE).string
                } else {
                    Component.translatable(WirelessMachine.KEY_SOURCE_NODE).string
                }
                val toggleText = Component.translatable(WirelessMachine.KEY_TOGGLE_TYPE).string + " → " + targetName
                content.addWidget(
                    ButtonWidget(
                        5,
                        y,
                        W - 10,
                        12,
                        GuiTextureGroup(GuiTextures.BUTTON, TextTexture(toggleText)),
                    ) { clickData ->
                        if (!clickData.isRemote) {
                            val newType = if (machine.nodeType == WirelessMachine.NodeType.SOURCE) {
                                WirelessMachine.NodeType.CHILD
                            } else {
                                WirelessMachine.NodeType.SOURCE
                            }
                            machine.switchNodeType(newType)
                        }
                    },
                )
                y += 14
            }

            // Connection status — derived from ISync list (isConnected field)
            val connSummary = machine.networkListCache.get()?.firstOrNull { it.isConnected }
            val connDisplay = connSummary?.nickname?.takeIf { it.isNotEmpty() }
                ?: "§c" + Component.translatable(WirelessMachine.KEY_NONE).string
            content.addWidget(
                LabelWidget(5, y, Component.translatable(WirelessMachine.KEY_CONNECTED, connDisplay).string),
            )
            y += 11

            // Unassigned warning — only add spacing if warning is shown (#5)
            val unassigned = machine.unassignedOutputCount.get()
            if (unassigned > 0) {
                content.addWidget(
                    LabelWidget(
                        5,
                        y,
                        "§c" + Component.translatable(WirelessMachine.KEY_UNASSIGNED_WARNING, unassigned).string,
                    ),
                )
                y += 11
            }

            // Create input + Create button + Disconnect button (same row)
            content.addWidget(TextFieldWidget(5, y, 78, 12, { createInput }, { createInput = it }))
            content.addWidget(
                ButtonWidget(
                    85,
                    y,
                    36,
                    12,
                    GuiTextureGroup(
                        GuiTextures.BUTTON,
                        TextTexture(Component.translatable(WirelessMachine.KEY_CREATE).string),
                    ),
                ) { clickData ->
                    if (!clickData.isRemote) {
                        val input = createInput.trim()
                        if (input.isNotEmpty()) {
                            WirelessNetworkSavedData.createNetwork(input, machine.requesterUUID)
                            createInput = ""
                            machine.refreshNetworkListOnServer()
                        }
                    }
                },
            )
            content.addWidget(
                ButtonWidget(
                    123,
                    y,
                    42,
                    12,
                    GuiTextureGroup(
                        GuiTextures.BUTTON,
                        TextTexture(Component.translatable(WirelessMachine.KEY_LEAVE).string),
                    ),
                ) { clickData ->
                    if (!clickData.isRemote) {
                        machine.leaveNetwork()
                    }
                },
            )
            y += 14

            // Available networks header
            val netCount = machine.networkListCache.get()?.size ?: 0
            content.addWidget(
                LabelWidget(5, y, Component.translatable(WirelessMachine.KEY_AVAILABLE, netCount).string),
            )
            y += 11

            // Scrollable network list
            val listHeight = H - y - 3
            val scrollGroup = DraggableScrollableWidgetGroup(3, y, W - 6, maxOf(listHeight, 30))
                .setBackground(GuiTextures.DISPLAY)
                .setYBarStyle(null, ColorPattern.T_WHITE.rectTexture().setRadius(1f))
                .setYScrollBarWidth(3)
            content.addWidget(scrollGroup)

            // Build list entries
            val list = machine.networkListCache.get() ?: emptyList()
            val entryW = W - 12
            var ly = 1
            for (summary in list) {
                val entryGroup = WidgetGroup(0, ly, entryW, 15)
                // Use isConnected from ISync data (not @DescSynced connectedNetworkId)
                val isConnected = summary.isConnected
                val isPending = pendingDelete.contains(summary.id)

                // Join button (green highlight if connected)
                val joinText = buildString {
                    if (summary.isDefault) append("§6⭐ §r")
                    append(summary.nickname)
                    append(" §7(S:${summary.inputCount}/C:${summary.outputCount}")
                    if (summary.unassignedCount > 0) append(" §c⚠${summary.unassignedCount}§7")
                    append(")")
                    if (isConnected) append(" §a✔")
                }
                entryGroup.addWidget(
                    ButtonWidget(
                        0,
                        0,
                        entryW - 38,
                        14,
                        GuiTextureGroup(
                            ResourceBorderTexture.BUTTON_COMMON.copy().setColor(
                                if (isConnected) ColorPattern.GREEN.color else -1,
                            ),
                            TextTexture(joinText).setWidth(entryW - 42),
                        ),
                    ) { clickData ->
                        if (!clickData.isRemote && !isConnected) {
                            machine.leaveNetwork()
                            machine.joinNetwork(summary.id)
                        }
                    },
                )

                // Star button
                val starText = if (summary.isDefault) "§6⚝" else "⭐"
                entryGroup.addWidget(
                    ButtonWidget(
                        entryW - 37,
                        0,
                        16,
                        14,
                        GuiTextureGroup(GuiTextures.BUTTON, TextTexture(starText)),
                    ) { clickData ->
                        if (!clickData.isRemote) {
                            if (summary.isDefault) {
                                WirelessNetworkSavedData.cancelDefault(summary.id, machine.requesterUUID)
                            } else {
                                WirelessNetworkSavedData.setDefault(summary.id, machine.requesterUUID)
                            }
                            machine.refreshNetworkListOnServer()
                        }
                    },
                )

                // Delete button (two-phase confirmation)
                val delText = if (isPending) {
                    "§c" + Component.translatable(WirelessMachine.KEY_CONFIRM_DELETE).string
                } else {
                    Component.translatable(WirelessMachine.KEY_REMOVE).string
                }
                entryGroup.addWidget(
                    ButtonWidget(
                        entryW - 20,
                        0,
                        20,
                        14,
                        GuiTextureGroup(
                            ResourceBorderTexture.BUTTON_COMMON.copy().setColor(
                                if (isPending) ColorPattern.RED.color else -1,
                            ),
                            TextTexture(delText),
                        ),
                    ) { clickData ->
                        if (pendingDelete.contains(summary.id)) {
                            // Confirmed — server deletes, both sides clear pending
                            pendingDelete.remove(summary.id)
                            if (!clickData.isRemote) {
                                WirelessNetworkSavedData.removeNetwork(summary.id, machine.requesterUUID)
                                machine.refreshNetworkListOnServer()
                            }
                        } else {
                            // First click — both sides mark pending, trigger local rebuild
                            pendingDelete.add(summary.id)
                            localRebuildNeeded = true
                        }
                    },
                )

                scrollGroup.addWidget(entryGroup)
                ly += 16
            }
        }

        // Initial build
        buildContent()

        // Sentinel — watches composite state hash, triggers full content rebuild.
        // Sits on root (not content), so it survives content rebuilds.
        // Rebuilds on BOTH sides so widget tree stays in sync for click handling.
        root.addWidget(object : Widget(Position(0, 0), Size(0, 0)) {
            private var lastHash = 0

            override fun detectAndSendChanges() {
                super.detectAndSendChanges()
                check()
            }

            override fun updateScreen() {
                super.updateScreen()
                check()
            }

            private fun check() {
                val h = computeHash()
                if (h != lastHash) {
                    // Server data changed — rebuild, reset local pending state
                    lastHash = h
                    pendingDelete.clear()
                    localRebuildNeeded = false
                    buildContent()
                } else if (localRebuildNeeded) {
                    // Local interaction (e.g. pendingDelete click) — rebuild, keep pendingDelete
                    localRebuildNeeded = false
                    buildContent()
                }
            }

            // Hash uses ONLY ISync-synced fields — never @DescSynced fields.
            // This ensures client and server compute the same hash from the same data path,
            // preventing stale-data rebuilds (#2, #6).
            private fun computeHash(): Int {
                var h = machine.networkListCache.get()?.hashCode() ?: 0
                h = 31 * h + machine.nodeTypeSync.get()
                h = 31 * h + machine.unassignedOutputCount.get()
                return h
            }
        })

        // Server-side initial push (safety net — also called from InitFancyMachineUIWidget callback)
        if (!machine.self().isRemote) {
            machine.refreshNetworkListOnServer()
        }

        return root
    }
}

// ============================================================================================
//  拓扑页面 — 仅显示当前连接的网络 (#4)
// ============================================================================================

/**
 * 拓扑 TAB — 树形展示当前连接网络中源节点与子节点的连接关系。
 * 服务端已过滤为仅连接的网络，客户端直接展示缓存内容。
 */
fun createTopologyUIProvider(machine: WirelessMachine): IFancyUIProvider = object : IFancyUIProvider {

    override fun getTabIcon(): IGuiTexture = ItemStackTexture(Items.FILLED_MAP)

    override fun getTitle(): Component = Component.translatable(WirelessMachine.KEY_TOPOLOGY)

    override fun createMainPage(parent: FancyMachineUIWidget?): Widget {
        val W = 170
        val H = 160
        val root = WidgetGroup(0, 0, W, H)
        root.setBackground(GuiTextures.BACKGROUND)

        // Content group — rebuilt on data change
        val content = WidgetGroup(0, 0, W, H)
        root.addWidget(content)

        // Mutable state for rename & max connections inputs
        var renameInput = ""
        var maxConnInput = ""

        fun buildContent() {
            content.clearAllWidgets()

            val data = machine.topologyCache.get()

            // Header
            content.addWidget(
                LabelWidget(5, 3, Component.translatable(WirelessMachine.KEY_TOPOLOGY).string),
            )

            var y = 14

            // Controls — only shown when connected to a network
            if (!data.isNullOrEmpty()) {
                val topo = data[0]

                // Row 1: Rename — [ TextField ] [ 重命名 ]
                if (renameInput.isEmpty() || renameInput == topo.networkNickname) {
                    renameInput = topo.networkNickname
                }
                content.addWidget(TextFieldWidget(5, y, W - 50, 12, { renameInput }, { renameInput = it }))
                content.addWidget(
                    ButtonWidget(
                        W - 43,
                        y,
                        38,
                        12,
                        GuiTextureGroup(
                            GuiTextures.BUTTON,
                            TextTexture(Component.translatable(WirelessMachine.KEY_RENAME).string),
                        ),
                    ) { clickData ->
                        if (!clickData.isRemote) {
                            val name = renameInput.trim()
                            if (name.isNotEmpty()) {
                                WirelessNetworkSavedData.renameNetwork(
                                    topo.networkId,
                                    machine.requesterUUID,
                                    name,
                                )
                                machine.refreshNetworkListOnServer()
                            }
                        }
                    },
                )
                y += 14

                // Row 2: Max connections — "最大连接数: N" [ TextField ] [ 设置 ]
                val maxLabel = Component.translatable(
                    WirelessMachine.KEY_MAX_CONNECTIONS,
                    topo.maxOutputsPerInput,
                ).string
                content.addWidget(LabelWidget(5, y + 2, maxLabel))
                y += 13

                if (maxConnInput.isEmpty()) {
                    maxConnInput = topo.maxOutputsPerInput.toString()
                }
                content.addWidget(TextFieldWidget(5, y, W - 43, 12, { maxConnInput }, { maxConnInput = it }))
                content.addWidget(
                    ButtonWidget(
                        W - 36,
                        y,
                        31,
                        12,
                        GuiTextureGroup(
                            GuiTextures.BUTTON,
                            TextTexture(Component.translatable(WirelessMachine.KEY_SET).string),
                        ),
                    ) { clickData ->
                        if (!clickData.isRemote) {
                            val v = maxConnInput.trim().toIntOrNull()
                            if (v != null && v > 0) {
                                WirelessNetworkSavedData.setMaxOutputsPerInput(
                                    topo.networkId,
                                    machine.requesterUUID,
                                    v,
                                )
                                machine.refreshNetworkListOnServer()
                            }
                        }
                    },
                )
                y += 14
            }

            // Scrollable topology tree
            val scrollGroup = DraggableScrollableWidgetGroup(3, y, W - 6, H - y - 3)
                .setBackground(GuiTextures.DISPLAY)
                .setYBarStyle(null, ColorPattern.T_WHITE.rectTexture().setRadius(1f))
                .setYScrollBarWidth(3)
            content.addWidget(scrollGroup)

            if (data.isNullOrEmpty()) {
                scrollGroup.addWidget(
                    LabelWidget(4, 2, Component.translatable(WirelessMachine.KEY_TOPOLOGY_HINT).string),
                )
                return
            }

            var ly = 2
            for (topo in data) {
                // Network header
                val totalChildren = topo.sources.sumOf { it.children.size } + topo.unassigned.size
                scrollGroup.addWidget(
                    LabelWidget(2, ly, "§e▸ ${topo.networkNickname} §7(S:${topo.sources.size}/C:$totalChildren)"),
                )
                ly += 11

                // Source nodes
                for (src in topo.sources) {
                    scrollGroup.addWidget(
                        LabelWidget(2, ly, "  §b◈ ${formatNodeShort(src.source)}"),
                    )
                    ly += 10

                    if (src.children.isEmpty()) {
                        scrollGroup.addWidget(LabelWidget(2, ly, "      §8(无子节点)"))
                        ly += 10
                    } else {
                        for ((ci, child) in src.children.withIndex()) {
                            val branch = if (ci == src.children.size - 1) "└" else "├"
                            scrollGroup.addWidget(
                                LabelWidget(2, ly, "      §7$branch §f${formatNodeShort(child)}"),
                            )
                            ly += 10
                        }
                    }
                }

                // Unassigned
                if (topo.unassigned.isNotEmpty()) {
                    scrollGroup.addWidget(
                        LabelWidget(
                            2,
                            ly,
                            "  §c⚠ " + Component.translatable(WirelessMachine.KEY_UNASSIGNED).string + ":",
                        ),
                    )
                    ly += 10
                    for ((ui, u) in topo.unassigned.withIndex()) {
                        val branch = if (ui == topo.unassigned.size - 1) "└" else "├"
                        scrollGroup.addWidget(
                            LabelWidget(2, ly, "      §7$branch §c${formatNodeShort(u)}"),
                        )
                        ly += 10
                    }
                }

                ly += 4
            }
        }

        buildContent()

        // Sentinel — rebuild on data change
        root.addWidget(object : Widget(Position(0, 0), Size(0, 0)) {
            private var lastHash = 0

            override fun detectAndSendChanges() {
                super.detectAndSendChanges()
                check()
            }

            override fun updateScreen() {
                super.updateScreen()
                check()
            }

            private fun check() {
                val hash = machine.topologyCache.get()?.hashCode() ?: 0
                if (hash != lastHash) {
                    lastHash = hash
                    buildContent()
                }
            }
        })

        return root
    }
}

/** Format: "x,y,z [dim_short]" */
private fun formatNodeShort(node: TopologyNodeEntry): String {
    val dimShort = node.dim.substringAfterLast(':').take(8)
    return "${node.x},${node.y},${node.z} §7[$dimShort]"
}

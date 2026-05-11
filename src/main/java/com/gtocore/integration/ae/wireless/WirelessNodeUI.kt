package com.gtocore.integration.ae.wireless

import com.gtocore.common.item.MEWirelessMachineConfigurator
import com.gtocore.common.saved.TopologyNodeEntry
import com.gtocore.common.saved.WirelessNetworkSavedData
import com.gtocore.common.saved.WirelessNetworkSavedData.Companion.write

import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items

import appeng.core.definitions.AEItems
import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.lowdragmc.lowdraglib.gui.texture.*
import com.lowdragmc.lowdraglib.gui.widget.*
import com.lowdragmc.lowdraglib.utils.Position
import com.lowdragmc.lowdraglib.utils.Size

// ============================================================================================
//  UI Design Constants & Helpers
// ============================================================================================

object WirelessUIDesign {
    val COLOR_BG = 0xFF121212.toInt()
    val COLOR_SURFACE = 0xFF1E1E1E.toInt()
    val COLOR_ACCENT_SOURCE = 0xFF00E5FF.toInt() // Cyan
    val COLOR_ACCENT_CHILD = 0xFFFFAB40.toInt() // Orange
    val COLOR_SUCCESS = 0xFF00E676.toInt() // Green
    val COLOR_ERROR = 0xFFFF1744.toInt() // Red
    val COLOR_TEXT_DIM = 0xFF9E9E9E.toInt()
    val COLOR_TEXT_BRIGHT = 0xFFF5F5F5.toInt()

    fun cardTexture(borderColor: Int = 0x44FFFFFF.toInt()): IGuiTexture = GuiTextureGroup(
        ColorRectTexture(0xFF000000.toInt()),
        ResourceBorderTexture.BUTTON_COMMON.copy().setColor(borderColor),
    )

    fun headerTexture(color: Int): IGuiTexture = GuiTextureGroup(
        ColorRectTexture(0xFF000000.toInt()),
        ResourceBorderTexture.BUTTON_COMMON.copy().setColor(color),
    )
}

// ============================================================================================
//  主页面 — 网络选择器
// ============================================================================================

/**
 * 无线网络管理 GUI — 主页面。
 */
fun createWirelessUIProvider(machine: WirelessMachine): IFancyUIProvider = object : IFancyUIProvider {

    override fun getTabIcon(): IGuiTexture = ItemStackTexture(AEItems.WIRELESS_RECEIVER.stack())

    override fun getTitle(): Component = Component.translatable(WirelessMachine.KEY_NODE_SELECTOR)

    override fun createMainPage(parent: FancyMachineUIWidget?): Widget {
        val W = 176
        val H = 166
        val root = WidgetGroup(0, 0, W, H)
        root.setBackground(GuiTextures.BACKGROUND)

        if (!machine.allowWirelessConnection()) {
            root.addWidget(LabelWidget(W / 2 - 40, H / 2, Component.translatable(WirelessMachine.KEY_BANNED).string).setTextColor(WirelessUIDesign.COLOR_ERROR).setDropShadow(true))
            return root
        }

        val content = WidgetGroup(4, 4, W - 8, H - 8)
        root.addWidget(content)

        var createInput = ""
        val pendingDelete = mutableSetOf<String>()
        var localRebuildNeeded = false

        fun buildContent() {
            content.clearAllWidgets()
            val innerW = W - 8
            var y = 2

            // --- Header: Node Identity ---
            val nodeTypeOrd = machine.nodeTypeSync.get()
            val currentNodeType = WirelessMachine.NodeType.entries.getOrElse(nodeTypeOrd) { WirelessMachine.NodeType.SOURCE }
            val typeTitle = if (currentNodeType == WirelessMachine.NodeType.SOURCE) Component.translatable(WirelessMachine.KEY_SYSTEM_SOURCE) else Component.translatable(WirelessMachine.KEY_TERMINAL_NODE)
            val typeColor = if (currentNodeType == WirelessMachine.NodeType.SOURCE) WirelessUIDesign.COLOR_ACCENT_SOURCE else WirelessUIDesign.COLOR_ACCENT_CHILD

            val header = WidgetGroup(2, y, innerW - 4, 18)
            header.setBackground(WirelessUIDesign.headerTexture(typeColor))
            content.addWidget(header)

            header.addWidget(LabelWidget(6, 4, typeTitle.string).setTextColor(typeColor).setDropShadow(true))
            header.addWidget(LabelWidget(innerW - 54, 4, "#" + machine.requesterUUID.toString().take(6)).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
            y += 22

            // --- Status Panel ---
            val connSummary = machine.networkListCache.get()?.firstOrNull { it.isConnected }
            val connDisplay = connSummary?.nickname?.takeIf { it.isNotEmpty() } ?: Component.translatable(WirelessMachine.KEY_STANDALONE).string
            val connColor = if (connSummary != null) WirelessUIDesign.COLOR_SUCCESS else WirelessUIDesign.COLOR_ERROR

            val statusCard = WidgetGroup(2, y, innerW - 4, 28)
            statusCard.setBackground(WirelessUIDesign.cardTexture(if (connSummary != null) 0x6600FF00.toInt() else 0x33FFFFFF.toInt()))
            content.addWidget(statusCard)

            statusCard.addWidget(LabelWidget(6, 4, Component.translatable(WirelessMachine.KEY_STATUS).string).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
            statusCard.addWidget(LabelWidget(6, 12, connDisplay).setTextColor(connColor).setDropShadow(true))

            // Node switch button integrated into status card if supported
            if (machine.supportsNodeTypeSwitching()) {
                statusCard.addWidget(
                    ButtonWidget(innerW - 68, 6, 60, 16, GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON, TextTexture(Component.translatable(WirelessMachine.KEY_TOGGLE_TYPE).string))) { clickData ->
                        if (!clickData.isRemote) {
                            machine.switchNodeType(if (machine.nodeType == WirelessMachine.NodeType.SOURCE) WirelessMachine.NodeType.CHILD else WirelessMachine.NodeType.SOURCE)
                        }
                    },
                )
            }
            y += 32

            // Warnings
            val unassigned = machine.unassignedOutputCount.get()
            if (unassigned > 0) {
                val warnBox = WidgetGroup(2, y, innerW - 4, 14)
                warnBox.setBackground(ColorRectTexture(0x28FFFF00.toInt()))
                content.addWidget(warnBox)
                warnBox.addWidget(LabelWidget(6, 2, Component.translatable(WirelessMachine.KEY_UNASSIGNED_WARNING, unassigned).string).setTextColor(0xFFFBC02D.toInt()).setDropShadow(true))
                y += 18
            }

            // --- Action Row: Creation ---
            val actionRow = WidgetGroup(2, y, innerW - 4, 20)
            content.addWidget(actionRow)

            actionRow.addWidget(TextFieldWidget(0, 2, 70, 14, { createInput }, { createInput = it }).setBackground(GuiTextures.BACKGROUND_INVERSE))
            actionRow.addWidget(
                ButtonWidget(74, 2, 44, 14, GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON.copy().setColor(WirelessUIDesign.COLOR_ACCENT_SOURCE), TextTexture(Component.translatable(WirelessMachine.KEY_CREATE).string))) { clickData ->
                    if (!clickData.isRemote && createInput.trim().isNotEmpty()) {
                        WirelessNetworkSavedData.createNetwork(createInput.trim(), machine.requesterUUID)
                        createInput = ""
                        machine.refreshNetworkListOnServer()
                    }
                },
            )
            actionRow.addWidget(
                ButtonWidget(122, 2, 44, 14, GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON.copy().setColor(WirelessUIDesign.COLOR_ERROR), TextTexture(Component.translatable(WirelessMachine.KEY_LEAVE).string))) { clickData ->
                    if (!clickData.isRemote) machine.leaveNetwork()
                },
            )
            y += 22

            // --- Network List ---
            val listData = machine.networkListCache.get() ?: emptyList()
            val netCount = listData.size
            content.addWidget(LabelWidget(4, y, Component.translatable(WirelessMachine.KEY_CHANNELS, netCount).string).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
            y += 12

            val listHeight = H - 8 - y - 4
            val scrollGroup = DraggableScrollableWidgetGroup(2, y, innerW - 4, listHeight)
                .setBackground(ColorRectTexture(0x64000000.toInt()))
                .setYBarStyle(null, ColorRectTexture(0x44FFFFFF.toInt()))
                .setYScrollBarWidth(2)
            content.addWidget(scrollGroup)

            var ly = 2
            for (summary in listData) {
                val isConnected = summary.isConnected
                val isPending = pendingDelete.contains(summary.id)
                val isDefault = summary.isDefault

                val entryCard = WidgetGroup(2, ly, innerW - 12, 26)
                entryCard.setBackground(WirelessUIDesign.cardTexture(if (isConnected) WirelessUIDesign.COLOR_SUCCESS else 0x22FFFFFF.toInt()))

                // Main Button Area
                val mainBtn = ButtonWidget(0, 0, innerW - 48, 26, ColorRectTexture(0)) { clickData ->
                    if (!clickData.isRemote && !isConnected) {
                        machine.leaveNetwork()
                        machine.joinNetwork(summary.id)
                    }
                }
                entryCard.addWidget(mainBtn)

                val nameStr = (if (isDefault) "★ " else "") + summary.nickname
                entryCard.addWidget(LabelWidget(6, 4, nameStr).setTextColor(if (isConnected) WirelessUIDesign.COLOR_SUCCESS else WirelessUIDesign.COLOR_TEXT_BRIGHT).setDropShadow(true))
                val statsText = "${Component.translatable(WirelessMachine.KEY_INPUTS_COUNT, summary.inputCount).string}  ${Component.translatable(WirelessMachine.KEY_OUTPUTS_COUNT, summary.outputCount).string}"
                entryCard.addWidget(LabelWidget(6, 15, statsText).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))

                // Action Icons
                val starColor = if (isDefault) 0xFFFFD600.toInt() else 0x44FFFFFF.toInt()
                entryCard.addWidget(
                    ButtonWidget(innerW - 46, 6, 14, 14, GuiTextureGroup(ColorRectTexture(0), TextTexture("★").setColor(starColor))) { clickData ->
                        if (!clickData.isRemote) {
                            if (isDefault) {
                                WirelessNetworkSavedData.cancelDefault(summary.id, machine.requesterUUID)
                            } else {
                                WirelessNetworkSavedData.setDefault(summary.id, machine.requesterUUID)
                            }
                            machine.refreshNetworkListOnServer()
                        }
                    },
                )

                val delColor = if (isPending) WirelessUIDesign.COLOR_ERROR else 0x44FFFFFF.toInt()
                entryCard.addWidget(
                    ButtonWidget(innerW - 28, 6, 14, 14, GuiTextureGroup(ColorRectTexture(0), TextTexture("✖").setColor(delColor))) { clickData ->
                        if (pendingDelete.contains(summary.id)) {
                            pendingDelete.remove(summary.id)
                            if (!clickData.isRemote) {
                                WirelessNetworkSavedData.removeNetwork(summary.id, machine.requesterUUID)
                                machine.refreshNetworkListOnServer()
                            }
                        } else {
                            pendingDelete.add(summary.id)
                            localRebuildNeeded = true
                        }
                    },
                )

                scrollGroup.addWidget(entryCard)
                ly += 29
            }
        }

        buildContent()

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
                if (h != lastHash || localRebuildNeeded) {
                    lastHash = h
                    if (!localRebuildNeeded) pendingDelete.clear()
                    localRebuildNeeded = false
                    buildContent()
                }
            }
            private fun computeHash(): Int {
                var h = machine.networkListCache.get()?.hashCode() ?: 0
                h = 31 * h + machine.nodeTypeSync.get()
                h = 31 * h + machine.unassignedOutputCount.get()
                return h
            }
        })

        if (!machine.self().isRemote) machine.refreshNetworkListOnServer()
        return root
    }
}

fun createWirelessUIProvider(player: Player): IFancyUIProvider = object : IFancyUIProvider {

    override fun getTabIcon(): IGuiTexture = ItemStackTexture(AEItems.WIRELESS_RECEIVER.stack())

    override fun getTitle(): Component = Component.translatable(WirelessMachine.KEY_NODE_SELECTOR)

    override fun createMainPage(parent: FancyMachineUIWidget?): Widget {
        val W = 176
        val H = 166
        val root = WidgetGroup(0, 0, W, H)
        root.setBackground(GuiTextures.BACKGROUND)

        val content = WidgetGroup(4, 4, W - 8, H - 8)
        root.addWidget(content)

        var createInput = ""
        val pendingDelete = mutableSetOf<String>()
        var localRebuildNeeded = false

        fun buildContent() {
            content.clearAllWidgets()
            val innerW = W - 8
            var y = 2

            // --- Header: Configurator Identity ---
            val header = WidgetGroup(2, y, innerW - 4, 18)
            header.setBackground(WirelessUIDesign.headerTexture(WirelessUIDesign.COLOR_ACCENT_SOURCE))
            content.addWidget(header)

            header.addWidget(LabelWidget(6, 4, Component.translatable(WirelessMachine.KEY_CONFIGURATOR).string).setTextColor(WirelessUIDesign.COLOR_ACCENT_SOURCE).setDropShadow(true))
            header.addWidget(LabelWidget(innerW - 64, 4, "ID:" + player.uuid.toString().take(8)).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
            y += 22

            // --- Status Panel ---
            val currentSelectedNetwork = MEWirelessMachineConfigurator.getConfiguringNetworkId(player)
            val connSummaries = WirelessNetworkSavedData.getNetworkSummaries(player.uuid, currentSelectedNetwork, true)
            val connSummary = connSummaries.firstOrNull { it.isConnected }
            val connDisplay = connSummary?.nickname?.takeIf { it.isNotEmpty() } ?: Component.translatable(WirelessMachine.KEY_NO_TARGET).string
            val connColor = if (connSummary != null) WirelessUIDesign.COLOR_SUCCESS else WirelessUIDesign.COLOR_TEXT_DIM

            val statusCard = WidgetGroup(2, y, innerW - 4, 28)
            statusCard.setBackground(WirelessUIDesign.cardTexture(if (connSummary != null) 0x6600FF00.toInt() else 0x22FFFFFF.toInt()))
            content.addWidget(statusCard)

            statusCard.addWidget(LabelWidget(6, 4, Component.translatable(WirelessMachine.KEY_TARGET_FREQ).string).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
            statusCard.addWidget(LabelWidget(6, 12, connDisplay).setTextColor(connColor).setDropShadow(true))
            y += 32

            // --- Action Row ---
            val actionRow = WidgetGroup(2, y, innerW - 4, 20)
            content.addWidget(actionRow)

            actionRow.addWidget(TextFieldWidget(0, 2, 110, 14, { createInput }, { createInput = it }).setBackground(GuiTextures.BACKGROUND_INVERSE))
            actionRow.addWidget(
                ButtonWidget(114, 2, 52, 14, GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON.copy().setColor(WirelessUIDesign.COLOR_ACCENT_SOURCE), TextTexture(Component.translatable(WirelessMachine.KEY_CREATE).string))) { clickData ->
                    if (!clickData.isRemote && createInput.trim().isNotEmpty()) {
                        WirelessNetworkSavedData.createNetwork(createInput.trim(), player.uuid)
                        createInput = ""
                        write(player)
                    }
                },
            )
            y += 22

            // --- Network List ---
            val netCount = connSummaries.size
            content.addWidget(LabelWidget(4, y, Component.translatable(WirelessMachine.KEY_ACCESSIBLE_NETS, netCount).string).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
            y += 12

            val listHeight = H - 8 - y - 4
            val scrollGroup = DraggableScrollableWidgetGroup(2, y, innerW - 4, listHeight)
                .setBackground(ColorRectTexture(0x64000000.toInt()))
                .setYBarStyle(null, ColorRectTexture(0x44FFFFFF.toInt()))
                .setYScrollBarWidth(2)
            content.addWidget(scrollGroup)

            var ly = 2
            for (summary in connSummaries) {
                val isConnected = MEWirelessMachineConfigurator.getConfiguringNetworkId(player) == summary.id
                val isPending = pendingDelete.contains(summary.id)
                val isDefault = summary.isDefault

                val entryCard = WidgetGroup(2, ly, innerW - 12, 26)
                entryCard.setBackground(WirelessUIDesign.cardTexture(if (isConnected) WirelessUIDesign.COLOR_SUCCESS else 0x22FFFFFF.toInt()))

                val mainBtn = ButtonWidget(0, 0, innerW - 48, 26, ColorRectTexture(0)) { clickData ->
                    if (!clickData.isRemote && !isConnected) {
                        MEWirelessMachineConfigurator.setConfiguringNetworkId(player, summary.id)
                    }
                }
                entryCard.addWidget(mainBtn)

                val nameStr = (if (isDefault) "★ " else "") + summary.nickname
                entryCard.addWidget(LabelWidget(6, 4, nameStr).setTextColor(if (isConnected) WirelessUIDesign.COLOR_SUCCESS else WirelessUIDesign.COLOR_TEXT_BRIGHT).setDropShadow(true))
                entryCard.addWidget(LabelWidget(6, 15, "CH: ${summary.id.take(8)}").setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))

                // Action Icons
                val starColor = if (isDefault) 0xFFFFD600.toInt() else 0x44FFFFFF.toInt()
                entryCard.addWidget(
                    ButtonWidget(innerW - 46, 6, 14, 14, GuiTextureGroup(ColorRectTexture(0), TextTexture("★").setColor(starColor))) { clickData ->
                        if (!clickData.isRemote) {
                            if (isDefault) {
                                WirelessNetworkSavedData.cancelDefault(summary.id, player.uuid)
                            } else {
                                WirelessNetworkSavedData.setDefault(summary.id, player.uuid)
                            }
                            write(player)
                        }
                    },
                )

                val delColor = if (isPending) WirelessUIDesign.COLOR_ERROR else 0x44FFFFFF.toInt()
                entryCard.addWidget(
                    ButtonWidget(innerW - 28, 6, 14, 14, GuiTextureGroup(ColorRectTexture(0), TextTexture("✖").setColor(delColor))) { clickData ->
                        if (pendingDelete.contains(summary.id)) {
                            pendingDelete.remove(summary.id)
                            if (!clickData.isRemote) {
                                WirelessNetworkSavedData.removeNetwork(summary.id, player.uuid)
                                write(player)
                            }
                        } else {
                            pendingDelete.add(summary.id)
                            localRebuildNeeded = true
                        }
                    },
                )

                scrollGroup.addWidget(entryCard)
                ly += 29
            }
        }

        buildContent()

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
                if (h != lastHash || localRebuildNeeded) {
                    lastHash = h
                    if (!localRebuildNeeded) pendingDelete.clear()
                    localRebuildNeeded = false
                    buildContent()
                }
            }
            private fun computeHash(): Int {
                var h = WirelessNetworkSavedData.getNetworkSummaries(player.uuid, filter = true).hashCode()
                h = 31 * h + MEWirelessMachineConfigurator.getConfiguringNetworkId(player).hashCode()
                return h
            }
        })

        write(player)
        return root
    }
}

// ============================================================================================
//  拓扑页面 — 树形可视化
// ============================================================================================

fun createTopologyUIProvider(machine: WirelessMachine): IFancyUIProvider = object : IFancyUIProvider {

    override fun getTabIcon(): IGuiTexture = ItemStackTexture(Items.FILLED_MAP)

    override fun getTitle(): Component = Component.translatable(WirelessMachine.KEY_TOPOLOGY)

    override fun createMainPage(parent: FancyMachineUIWidget?): Widget {
        val W = 176
        val H = 166
        val root = WidgetGroup(0, 0, W, H)
        root.setBackground(GuiTextures.BACKGROUND)

        val content = WidgetGroup(4, 4, W - 8, H - 8)
        root.addWidget(content)

        var renameInput = ""
        var maxConnInput = ""

        fun buildContent() {
            content.clearAllWidgets()
            val innerW = W - 8
            var y = 2

            val data = machine.topologyCache.get()

            // Header
            val header = WidgetGroup(2, y, innerW - 4, 18)
            header.setBackground(WirelessUIDesign.headerTexture(WirelessUIDesign.COLOR_ACCENT_SOURCE))
            content.addWidget(header)
            header.addWidget(LabelWidget(6, 4, Component.translatable(WirelessMachine.KEY_TOPOLOGY).string).setTextColor(WirelessUIDesign.COLOR_ACCENT_SOURCE).setDropShadow(true))
            y += 22

            if (!data.isNullOrEmpty()) {
                val topo = data[0]

                // Manage Card: Height increased to 64 to avoid overlap
                val manageCard = WidgetGroup(2, y, innerW - 4, 64)
                manageCard.setBackground(WirelessUIDesign.cardTexture(0x33FFFFFF.toInt()))
                content.addWidget(manageCard)

                // Rename Row
                if (renameInput.isEmpty() || renameInput == topo.networkNickname) renameInput = topo.networkNickname
                manageCard.addWidget(LabelWidget(6, 4, Component.translatable(WirelessMachine.KEY_RENAME).string).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
                manageCard.addWidget(TextFieldWidget(6, 14, 90, 14, { renameInput }, { renameInput = it }).setBackground(GuiTextures.BACKGROUND_INVERSE))
                manageCard.addWidget(
                    ButtonWidget(100, 14, 58, 14, GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON.copy().setColor(WirelessUIDesign.COLOR_ACCENT_SOURCE), TextTexture(Component.translatable(WirelessMachine.KEY_RENAME).string))) { clickData ->
                        if (!clickData.isRemote && renameInput.trim().isNotEmpty()) {
                            WirelessNetworkSavedData.renameNetwork(topo.networkId, machine.requesterUUID, renameInput.trim())
                            machine.refreshNetworkListOnServer()
                        }
                    },
                )

                // Max Connections Row
                if (maxConnInput.isEmpty()) maxConnInput = topo.maxOutputsPerInput.toString()
                manageCard.addWidget(LabelWidget(6, 32, Component.translatable(WirelessMachine.KEY_MAX_CONNECTIONS, topo.maxOutputsPerInput).string).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
                manageCard.addWidget(TextFieldWidget(6, 42, 90, 14, { maxConnInput }, { maxConnInput = it }).setBackground(GuiTextures.BACKGROUND_INVERSE))
                manageCard.addWidget(
                    ButtonWidget(100, 42, 58, 14, GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON.copy().setColor(WirelessUIDesign.COLOR_ACCENT_CHILD), TextTexture(Component.translatable(WirelessMachine.KEY_SET).string))) { clickData ->
                        if (!clickData.isRemote) {
                            val v = maxConnInput.trim().toIntOrNull()
                            if (v != null && v > 0) {
                                WirelessNetworkSavedData.setMaxOutputsPerInput(topo.networkId, machine.requesterUUID, v)
                                machine.refreshNetworkListOnServer()
                            }
                        }
                    },
                )
                y += 70
            }

            // Scrollable List
            val scrollGroup = DraggableScrollableWidgetGroup(2, y, innerW - 4, H - 8 - y - 2)
                .setBackground(ColorRectTexture(0x64000000))
                .setYBarStyle(null, ColorRectTexture(0x44FFFFFF))
                .setYScrollBarWidth(2)
            content.addWidget(scrollGroup)

            if (data.isNullOrEmpty()) {
                scrollGroup.addWidget(LabelWidget(innerW / 2 - 40, 20, Component.translatable(WirelessMachine.KEY_NO_NETWORK_ACTIVE).string).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM).setDropShadow(true))
                return
            }

            var ly = 4
            for (topo in data) {
                val totalChildren = topo.sources.sumOf { it.children.size } + topo.unassigned.size

                scrollGroup.addWidget(LabelWidget(4, ly, "▣ ${topo.networkNickname}").setTextColor(0xFFFFD600.toInt()).setDropShadow(true))
                scrollGroup.addWidget(LabelWidget(innerW - 60, ly, "S:${topo.sources.size} C:$totalChildren").setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
                ly += 16

                for (src in topo.sources) {
                    scrollGroup.addWidget(LabelWidget(6, ly, "  ▼ ${Component.translatable(WirelessMachine.KEY_SOURCE_TITLE).string} [${formatNodeShort(src.source)}]").setTextColor(WirelessUIDesign.COLOR_ACCENT_SOURCE))
                    ly += 12

                    if (src.children.isEmpty()) {
                        scrollGroup.addWidget(LabelWidget(18, ly, "   (NO CLIENTS CONNECTED)").setTextColor(0x66FFFFFF.toInt()))
                        ly += 10
                    } else {
                        for ((ci, child) in src.children.withIndex()) {
                            val branch = if (ci == src.children.size - 1) "└" else "├"
                            scrollGroup.addWidget(LabelWidget(18, ly, "$branch ${Component.translatable(WirelessMachine.KEY_CLIENT_TITLE).string} [${formatNodeShort(child)}]").setTextColor(0xFFE0E0E0.toInt()))
                            ly += 11
                        }
                    }
                    ly += 4
                }

                if (topo.unassigned.isNotEmpty()) {
                    scrollGroup.addWidget(LabelWidget(6, ly, "  ⚠ ${Component.translatable(WirelessMachine.KEY_UNASSIGNED).string}").setTextColor(WirelessUIDesign.COLOR_ERROR))
                    ly += 12
                    for ((ui, u) in topo.unassigned.withIndex()) {
                        val branch = if (ui == topo.unassigned.size - 1) "└" else "├"
                        scrollGroup.addWidget(LabelWidget(18, ly, "$branch NODE [${formatNodeShort(u)}]").setTextColor(WirelessUIDesign.COLOR_ACCENT_CHILD))
                        ly += 11
                    }
                }
                ly += 8
            }
        }

        buildContent()

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

/** Format: "x, y, z [dim]" */
private fun formatNodeShort(node: TopologyNodeEntry): String {
    val dimShort = node.dim.substringAfterLast(':').uppercase().take(6)
    return "${node.x},${node.y},${node.z} §7$dimShort"
}

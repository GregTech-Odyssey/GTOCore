package com.gtocore.integration.ae.wireless

import com.gtocore.common.item.MEWirelessMachineConfigurator
import com.gtocore.common.saved.*
import com.gtocore.common.saved.WirelessNetworkSavedData.Companion.write

import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

import appeng.core.definitions.AEItems
import appeng.core.localization.PlayerMessages
import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider
import com.lowdragmc.lowdraglib.gui.texture.*
import com.lowdragmc.lowdraglib.gui.widget.*
import com.lowdragmc.lowdraglib.utils.Position
import com.lowdragmc.lowdraglib.utils.Size
import io.github.lounode.extrabotany.common.proxy.Proxy

import java.util.*

private const val W = 176
private const val H = 166
private const val INNER_W = W - 8

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
        val root = rootWidget()
        if (!machine.allowWirelessConnection()) {
            root.addWidget(LabelWidget(W / 2 - 40, H / 2, Component.translatable(WirelessMachine.KEY_BANNED).string).setTextColor(WirelessUIDesign.COLOR_ERROR).setDropShadow(true))
            return root
        }

        val content = contentWidget(root)
        val state = NetworkPageState()

        fun buildContent() {
            content.clearAllWidgets()
            var y = 2

            content.addWidget(machineHeaderWidget(y, machine))
            y += 22
            content.addWidget(machineStatusWidget(y, machine))
            y += 32

            val unassigned = machine.unassignedOutputCount.get()
            if (unassigned > 0) {
                content.addWidget(unassignedWarningWidget(y, unassigned))
                y += 18
            }

            content.addWidget(
                createNetworkRowWidget(y, state, 70, 74, 44, 122, 44, {
                    WirelessNetworkSavedData.createNetwork(it, machine.requesterUUID)
                    machine.refreshNetworkListOnServer()
                }) { machine.leaveNetwork() },
            )
            y += 22

            val networks = machine.networkListCache.get() ?: emptyList()
            content.addWidget(
                networkListWidget(
                    y, Component.translatable(WirelessMachine.KEY_CHANNELS, networks.size).string, networks, state,
                    isConnected = { it.isConnected },
                    subtitle = { "${Component.translatable(WirelessMachine.KEY_INPUTS_COUNT, it.inputCount).string}  ${Component.translatable(WirelessMachine.KEY_OUTPUTS_COUNT, it.outputCount).string}" },
                    onSelect = {
                        machine.leaveNetwork()
                        machine.joinNetwork(it.id)
                    },
                    onDefault = {
                        toggleDefaultNetwork(it, machine.requesterUUID)
                        machine.refreshNetworkListOnServer()
                    },
                    onDelete = {
                        WirelessNetworkSavedData.removeNetwork(it.id, machine.requesterUUID)
                        machine.refreshNetworkListOnServer()
                    },
                ),
            )
        }

        buildContent()
        root.addWidget(
            rebuildWatcher(
                hash = { machineNetworkHash(machine) },
                local = { state.localRebuildNeeded },
                beforeRebuild = {
                    if (!it) state.pendingDelete.clear()
                    state.localRebuildNeeded = false
                },
                rebuild = ::buildContent,
            ),
        )

        if (!machine.self().isRemote) machine.refreshNetworkListOnServer()
        return root
    }
}

fun createWirelessUIProvider(player: Player): IFancyUIProvider = object : IFancyUIProvider {

    override fun getTabIcon(): IGuiTexture = ItemStackTexture(AEItems.WIRELESS_RECEIVER.stack())

    override fun getTitle(): Component = Component.translatable(WirelessMachine.KEY_NODE_SELECTOR)

    override fun createMainPage(parent: FancyMachineUIWidget?): Widget {
        val root = rootWidget()
        val content = contentWidget(root)
        val state = NetworkPageState()

        fun buildContent() {
            content.clearAllWidgets()
            var y = 2

            content.addWidget(configuratorHeaderWidget(y, player))
            y += 22

            val selected = MEWirelessMachineConfigurator.getConfiguringNetworkId(player)
            val networks = WirelessNetworkSavedData.getNetworkSummaries(player.uuid, selected, true)
            content.addWidget(configuratorStatusWidget(y, networks.firstOrNull { it.isConnected }))
            y += 32

            content.addWidget(
                createNetworkRowWidget(y, state, 110, 114, 52, onCreate = {
                    WirelessNetworkSavedData.createNetwork(it, player.uuid)
                    write(player)
                }),
            )
            y += 22

            content.addWidget(
                networkListWidget(
                    y, Component.translatable(WirelessMachine.KEY_ACCESSIBLE_NETS, networks.size).string, networks, state,
                    isConnected = { MEWirelessMachineConfigurator.getConfiguringNetworkId(player) == it.id },
                    subtitle = { "CH: ${it.id.take(8)}" },
                    onSelect = { MEWirelessMachineConfigurator.setConfiguringNetworkId(player, it.id) },
                    onDefault = {
                        toggleDefaultNetwork(it, player.uuid)
                        write(player)
                    },
                    onDelete = {
                        WirelessNetworkSavedData.removeNetwork(it.id, player.uuid)
                        write(player)
                    },
                ),
            )
        }

        buildContent()
        root.addWidget(
            rebuildWatcher(
                hash = { configuratorNetworkHash(player) },
                local = { state.localRebuildNeeded },
                beforeRebuild = {
                    if (!it) state.pendingDelete.clear()
                    state.localRebuildNeeded = false
                },
                rebuild = ::buildContent,
            ),
        )

        write(player)
        return root
    }
}

private class NetworkPageState {
    var createInput = ""
    val pendingDelete = mutableSetOf<String>()
    var localRebuildNeeded = false
}

private fun machineHeaderWidget(y: Int, machine: WirelessMachine): WidgetGroup {
    val nodeType = WirelessMachine.NodeType.entries.getOrElse(machine.nodeTypeSync.get()) { WirelessMachine.NodeType.SOURCE }
    val title = if (nodeType == WirelessMachine.NodeType.SOURCE) Component.translatable(WirelessMachine.KEY_SYSTEM_SOURCE) else Component.translatable(WirelessMachine.KEY_TERMINAL_NODE)
    val color = if (nodeType == WirelessMachine.NodeType.SOURCE) WirelessUIDesign.COLOR_ACCENT_SOURCE else WirelessUIDesign.COLOR_ACCENT_CHILD
    return headerWidget(y, title.string, color, "#" + machine.requesterUUID.toString().take(6), INNER_W - 54)
}

private fun configuratorHeaderWidget(y: Int, player: Player): WidgetGroup = headerWidget(y, Component.translatable(WirelessMachine.KEY_CONFIGURATOR).string, WirelessUIDesign.COLOR_ACCENT_SOURCE, "ID:" + player.uuid.toString().take(8), INNER_W - 64)

private fun headerWidget(y: Int, title: String, color: Int, rightText: String? = null, rightX: Int = 0): WidgetGroup {
    val header = WidgetGroup(2, y, INNER_W - 4, 18)
    header.setBackground(WirelessUIDesign.headerTexture(color))
    header.addWidget(LabelWidget(6, 4, title).setTextColor(color).setDropShadow(true))
    if (rightText != null) header.addWidget(LabelWidget(rightX, 4, rightText).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
    return header
}

private fun machineStatusWidget(y: Int, machine: WirelessMachine): WidgetGroup {
    val connected = machine.networkListCache.get()?.firstOrNull { it.isConnected }
    val text = connected?.nickname?.takeIf { it.isNotEmpty() } ?: Component.translatable(WirelessMachine.KEY_STANDALONE).string
    val status = statusCardWidget(y, Component.translatable(WirelessMachine.KEY_STATUS).string, text, if (connected != null) WirelessUIDesign.COLOR_SUCCESS else WirelessUIDesign.COLOR_ERROR, if (connected != null) 0x6600FF00.toInt() else 0x33FFFFFF.toInt())
    if (machine.supportsNodeTypeSwitching()) {
        status.addWidget(
            ButtonWidget(INNER_W - 68, 6, 60, 16, GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON, TextTexture(Component.translatable(WirelessMachine.KEY_TOGGLE_TYPE).string))) { clickData ->
                if (!clickData.isRemote) machine.switchNodeType(if (machine.nodeType == WirelessMachine.NodeType.SOURCE) WirelessMachine.NodeType.CHILD else WirelessMachine.NodeType.SOURCE)
            },
        )
    }
    return status
}

private fun configuratorStatusWidget(y: Int, connected: NetworkSummary?): WidgetGroup {
    val text = connected?.nickname?.takeIf { it.isNotEmpty() } ?: Component.translatable(WirelessMachine.KEY_NO_TARGET).string
    return statusCardWidget(y, Component.translatable(WirelessMachine.KEY_TARGET_FREQ).string, text, if (connected != null) WirelessUIDesign.COLOR_SUCCESS else WirelessUIDesign.COLOR_TEXT_DIM, if (connected != null) 0x6600FF00.toInt() else 0x22FFFFFF.toInt())
}

private fun statusCardWidget(y: Int, label: String, value: String, valueColor: Int, borderColor: Int): WidgetGroup {
    val card = WidgetGroup(2, y, INNER_W - 4, 28)
    card.setBackground(WirelessUIDesign.cardTexture(borderColor))
    card.addWidget(LabelWidget(6, 4, label).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
    card.addWidget(LabelWidget(6, 12, value).setTextColor(valueColor).setDropShadow(true))
    return card
}

private fun unassignedWarningWidget(y: Int, unassigned: Int): WidgetGroup {
    val warnBox = WidgetGroup(2, y, INNER_W - 4, 14)
    warnBox.setBackground(ColorRectTexture(0x28FFFF00.toInt()))
    warnBox.addWidget(LabelWidget(6, 2, Component.translatable(WirelessMachine.KEY_UNASSIGNED_WARNING, unassigned).string).setTextColor(0xFFFBC02D.toInt()).setDropShadow(true))
    return warnBox
}

private fun createNetworkRowWidget(y: Int, state: NetworkPageState, inputW: Int, createX: Int, createW: Int, leaveX: Int? = null, leaveW: Int = 0, onCreate: (String) -> Unit, onLeave: (() -> Unit)? = null): WidgetGroup {
    val row = WidgetGroup(2, y, INNER_W - 4, 20)
    row.addWidget(TextFieldWidget(0, 2, inputW, 14, { state.createInput }, { state.createInput = it }).setBackground(GuiTextures.BACKGROUND_INVERSE))
    row.addWidget(
        ButtonWidget(createX, 2, createW, 14, buttonTexture(WirelessUIDesign.COLOR_ACCENT_SOURCE, Component.translatable(WirelessMachine.KEY_CREATE).string)) { clickData ->
            val name = state.createInput.trim()
            if (!clickData.isRemote && name.isNotEmpty()) {
                onCreate(name)
                state.createInput = ""
            }
        },
    )
    if (leaveX != null && onLeave != null) {
        row.addWidget(
            ButtonWidget(leaveX, 2, leaveW, 14, buttonTexture(WirelessUIDesign.COLOR_ERROR, Component.translatable(WirelessMachine.KEY_LEAVE).string)) { clickData ->
                if (!clickData.isRemote) onLeave()
            },
        )
    }
    return row
}

private fun networkListWidget(y: Int, title: String, networks: List<NetworkSummary>, state: NetworkPageState, isConnected: (NetworkSummary) -> Boolean, subtitle: (NetworkSummary) -> String, onSelect: (NetworkSummary) -> Unit, onDefault: (NetworkSummary) -> Unit, onDelete: (NetworkSummary) -> Unit): WidgetGroup {
    val group = WidgetGroup(0, y, INNER_W, H - 8 - y)
    group.addWidget(LabelWidget(4, 0, title).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
    val scroll = scrollWidget(2, 12, INNER_W - 4, H - 8 - y - 16)
    group.addWidget(scroll)

    var ly = 2
    for (summary in networks) {
        scroll.addWidget(networkEntryWidget(summary, ly, isConnected(summary), subtitle(summary), state, onSelect, onDefault, onDelete))
        ly += 29
    }
    return group
}

private fun networkEntryWidget(summary: NetworkSummary, y: Int, connected: Boolean, subtitle: String, state: NetworkPageState, onSelect: (NetworkSummary) -> Unit, onDefault: (NetworkSummary) -> Unit, onDelete: (NetworkSummary) -> Unit): WidgetGroup {
    val pending = state.pendingDelete.contains(summary.id)
    val card = WidgetGroup(2, y, INNER_W - 12, 26)
    card.setBackground(WirelessUIDesign.cardTexture(if (connected) WirelessUIDesign.COLOR_SUCCESS else 0x22FFFFFF.toInt()))
    card.addWidget(
        ButtonWidget(0, 0, INNER_W - 48, 26, ColorRectTexture(0)) { clickData ->
            if (!clickData.isRemote && !connected) onSelect(summary)
        },
    )
    card.addWidget(LabelWidget(6, 4, (if (summary.isDefault) "★ " else "") + summary.nickname).setTextColor(if (connected) WirelessUIDesign.COLOR_SUCCESS else WirelessUIDesign.COLOR_TEXT_BRIGHT).setDropShadow(true))
    card.addWidget(LabelWidget(6, 15, subtitle).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
    card.addWidget(
        ButtonWidget(INNER_W - 46, 6, 14, 14, GuiTextureGroup(ColorRectTexture(0), TextTexture("★").setColor(if (summary.isDefault) 0xFFFFD600.toInt() else 0x44FFFFFF.toInt()))) { clickData ->
            if (!clickData.isRemote) onDefault(summary)
        },
    )
    card.addWidget(
        ButtonWidget(INNER_W - 28, 6, 14, 14, GuiTextureGroup(ColorRectTexture(0), TextTexture("✖").setColor(if (pending) WirelessUIDesign.COLOR_ERROR else 0x44FFFFFF.toInt()))) { clickData ->
            if (state.pendingDelete.contains(summary.id)) {
                state.pendingDelete.remove(summary.id)
                if (!clickData.isRemote) onDelete(summary)
            } else {
                state.pendingDelete.add(summary.id)
                state.localRebuildNeeded = true
            }
        },
    )
    return card
}

private fun toggleDefaultNetwork(summary: NetworkSummary, requester: UUID) {
    if (summary.isDefault) WirelessNetworkSavedData.cancelDefault(summary.id, requester) else WirelessNetworkSavedData.setDefault(summary.id, requester)
}

// ============================================================================================
//  拓扑页面 — 树形可视化
// ============================================================================================

fun createTopologyUIProvider(machine: WirelessMachine): IFancyUIProvider = object : IFancyUIProvider {

    override fun getTabIcon(): IGuiTexture = ItemStackTexture(Items.FILLED_MAP)

    override fun getTitle(): Component = Component.translatable(WirelessMachine.KEY_TOPOLOGY)

    override fun createMainPage(parent: FancyMachineUIWidget?): Widget {
        val root = rootWidget()
        val content = contentWidget(root)
        val state = TopologyPageState()

        fun buildContent() {
            content.clearAllWidgets()
            var y = 2
            val data = machine.topologyCache.get()

            content.addWidget(headerWidget(y, Component.translatable(WirelessMachine.KEY_TOPOLOGY).string, WirelessUIDesign.COLOR_ACCENT_SOURCE))
            y += 22
            if (!data.isNullOrEmpty()) {
                content.addWidget(topologyManageCardWidget(y, machine, data[0], state))
                y += 70
            }
            content.addWidget(topologyTreeWidget(y, data))
        }

        buildContent()
        root.addWidget(rebuildWatcher(hash = { machine.topologyCache.get()?.hashCode() ?: 0 }, rebuild = ::buildContent))
        return root
    }
}

private class TopologyPageState {
    var renameInput = ""
    var maxConnInput = ""
}

private fun topologyManageCardWidget(y: Int, machine: WirelessMachine, topo: TopologySummary, state: TopologyPageState): WidgetGroup {
    val card = WidgetGroup(2, y, INNER_W - 4, 64)
    card.setBackground(WirelessUIDesign.cardTexture(0x33FFFFFF.toInt()))
    if (state.renameInput.isEmpty() || state.renameInput == topo.networkNickname) state.renameInput = topo.networkNickname
    if (state.maxConnInput.isEmpty()) state.maxConnInput = topo.maxOutputsPerInput.toString()

    card.addWidget(LabelWidget(6, 4, Component.translatable(WirelessMachine.KEY_RENAME).string).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
    card.addWidget(TextFieldWidget(6, 14, 90, 14, { state.renameInput }, { state.renameInput = it }).setBackground(GuiTextures.BACKGROUND_INVERSE))
    card.addWidget(
        ButtonWidget(100, 14, 58, 14, buttonTexture(WirelessUIDesign.COLOR_ACCENT_SOURCE, Component.translatable(WirelessMachine.KEY_RENAME).string)) { clickData ->
            if (!clickData.isRemote && state.renameInput.trim().isNotEmpty()) {
                WirelessNetworkSavedData.renameNetwork(topo.networkId, machine.requesterUUID, state.renameInput.trim())
                machine.refreshNetworkListOnServer()
            }
        },
    )

    card.addWidget(LabelWidget(6, 32, Component.translatable(WirelessMachine.KEY_MAX_CONNECTIONS, topo.maxOutputsPerInput).string).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
    card.addWidget(TextFieldWidget(6, 42, 90, 14, { state.maxConnInput }, { state.maxConnInput = it }).setBackground(GuiTextures.BACKGROUND_INVERSE))
    card.addWidget(
        ButtonWidget(100, 42, 58, 14, buttonTexture(WirelessUIDesign.COLOR_ACCENT_CHILD, Component.translatable(WirelessMachine.KEY_SET).string)) { clickData ->
            val v = state.maxConnInput.trim().toIntOrNull()
            if (!clickData.isRemote && v != null && v > 0) {
                WirelessNetworkSavedData.setMaxOutputsPerInput(topo.networkId, machine.requesterUUID, v)
                machine.refreshNetworkListOnServer()
            }
        },
    )
    return card
}

private fun topologyTreeWidget(y: Int, data: List<TopologySummary>?): WidgetGroup {
    val scroll = scrollWidget(2, y, INNER_W - 4, H - 8 - y - 2)
    if (data.isNullOrEmpty()) {
        scroll.addWidget(LabelWidget(INNER_W / 2 - 40, 20, Component.translatable(WirelessMachine.KEY_NO_NETWORK_ACTIVE).string).setTextColor(WirelessUIDesign.COLOR_TEXT_DIM).setDropShadow(true))
        return scroll
    }

    var ly = 4
    for (topo in data) {
        ly = topologyNetworkWidget(scroll, ly, topo)
    }
    return scroll
}

private fun topologyNetworkWidget(parent: WidgetGroup, y: Int, topo: TopologySummary): Int {
    var ly = y
    val totalChildren = topo.sources.sumOf { it.children.size } + topo.unassigned.size
    parent.addWidget(LabelWidget(4, ly, "▣ ${topo.networkNickname}").setTextColor(0xFFFFD600.toInt()).setDropShadow(true))
    parent.addWidget(LabelWidget(INNER_W - 60, ly, "S:${topo.sources.size} C:$totalChildren").setTextColor(WirelessUIDesign.COLOR_TEXT_DIM))
    ly += 16
    for (source in topo.sources) ly = topologySourceWidget(parent, ly, source)
    if (topo.unassigned.isNotEmpty()) ly = topologyUnassignedWidget(parent, ly, topo.unassigned)
    return ly + 8
}

private fun topologySourceWidget(parent: WidgetGroup, y: Int, source: TopologySourceEntry): Int {
    var ly = y
    parent.addWidget(TeleportSupportLabelWidget(6, ly, "  ▼ ${Component.translatable(WirelessMachine.KEY_SOURCE_TITLE).string}", source.source).setTextColor(WirelessUIDesign.COLOR_ACCENT_SOURCE))
    ly += 12
    if (source.children.isEmpty()) {
        parent.addWidget(LabelWidget(18, ly, WirelessMachine.NO_CLIENT).setTextColor(0x66FFFFFF.toInt()))
        ly += 10
    } else {
        for ((i, child) in source.children.withIndex()) {
            val branch = if (i == source.children.size - 1) "└" else "├"
            parent.addWidget(TeleportSupportLabelWidget(18, ly, "$branch ${Component.translatable(WirelessMachine.KEY_CLIENT_TITLE).string}", child).setTextColor(0xFFE0E0E0.toInt()))
            ly += 11
        }
    }
    return ly + 4
}

private fun topologyUnassignedWidget(parent: WidgetGroup, y: Int, nodes: List<TopologyNodeEntry>): Int {
    var ly = y
    parent.addWidget(LabelWidget(6, ly, "  ⚠ ${Component.translatable(WirelessMachine.KEY_UNASSIGNED).string}").setTextColor(WirelessUIDesign.COLOR_ERROR))
    ly += 12
    for ((i, node) in nodes.withIndex()) {
        val branch = if (i == nodes.size - 1) "└" else "├"
        parent.addWidget(TeleportSupportLabelWidget(18, ly, "$branch NODE", node).setTextColor(WirelessUIDesign.COLOR_ACCENT_CHILD))
        ly += 11
    }
    return ly
}

private fun rootWidget(): WidgetGroup = WidgetGroup(0, 0, W, H).apply { setBackground(GuiTextures.BACKGROUND) }

private fun contentWidget(root: WidgetGroup): WidgetGroup = WidgetGroup(4, 4, INNER_W, H - 8).also(root::addWidget)

private fun scrollWidget(x: Int, y: Int, width: Int, height: Int): DraggableScrollableWidgetGroup = DraggableScrollableWidgetGroup(x, y, width, height)
    .setBackground(ColorRectTexture(0x64000000))
    .setYBarStyle(null, ColorRectTexture(0x44FFFFFF))
    .setYScrollBarWidth(2)

private fun buttonTexture(color: Int, text: String): IGuiTexture = GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON.copy().setColor(color), TextTexture(text))

private fun rebuildWatcher(hash: () -> Int, local: () -> Boolean = { false }, beforeRebuild: (Boolean) -> Unit = {}, rebuild: () -> Unit): Widget = object : Widget(Position(0, 0), Size(0, 0)) {
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
        val h = hash()
        val needsLocalRebuild = local()
        if (h != lastHash || needsLocalRebuild) {
            lastHash = h
            beforeRebuild(needsLocalRebuild)
            rebuild()
        }
    }
}

private fun machineNetworkHash(machine: WirelessMachine): Int {
    var h = machine.networkListCache.get()?.hashCode() ?: 0
    h = 31 * h + machine.nodeTypeSync.get()
    h = 31 * h + machine.unassignedOutputCount.get()
    return h
}

private fun configuratorNetworkHash(player: Player): Int {
    var h = WirelessNetworkSavedData.getNetworkSummaries(player.uuid, filter = true).hashCode()
    h = 31 * h + MEWirelessMachineConfigurator.getConfiguringNetworkId(player).hashCode()
    return h
}

/** Format: "x, y, z [dim]" */
private fun formatNodeShort(node: TopologyNodeEntry): String {
    val dimShort = node.dim.substringAfterLast(':').uppercase().take(6)
    return "${node.x},${node.y},${node.z} §7$dimShort"
}

class TeleportSupportLabelWidget(x: Int, y: Int, text: String, val node: TopologyNodeEntry) : LabelWidget(x, y, text + " [${formatNodeShort(node)}]") {

    @OnlyIn(Dist.CLIENT)
    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val tpCommand = "/execute in ${node.dim} run tp @s ${node.x} ${node.y} ${node.z}"
        Proxy.INSTANCE.clientPlayer?.displayClientMessage(
            PlayerMessages.ClickToTeleport.text().setStyle(
                Style.EMPTY.withColor(0xFF00E5FF.toInt()).withUnderlined(true).withClickEvent(
                    ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, tpCommand),
                ),
            ),
            false,
        )
        Proxy.INSTANCE.clientPlayer?.playNotifySound(SoundEvents.CHICKEN_EGG, SoundSource.PLAYERS, 0.5f, 1f)
        return true
    }
}

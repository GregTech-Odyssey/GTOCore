package com.gtocore.integration.ae

import com.gtocore.api.gui.ktflexible.InitFancyMachineUIWidget

import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

import appeng.api.networking.IManagedGridNode
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder
import com.hepdd.gtmthings.utils.TeamUtil
import com.lowdragmc.lowdraglib.gui.modular.ModularUI
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder

import java.util.*

class MeWirelessConnectMachine(holder: IMachineBlockEntity) :
    MetaMachine(holder),
    WirelessMachine,
    IMachineLife,
    IFancyUIMachine {
    companion object {
        @JvmStatic
        val manager = ManagedFieldHolder(MeWirelessConnectMachine::class.java, MANAGED_FIELD_HOLDER)
    }
    val gridHolder = GridNodeHolder(this)
    var isGridOnline: Boolean = false
    var playerUUID: UUID = UUID.randomUUID()

    override fun getFieldHolder() = manager
    override fun isOnline(): Boolean = isGridOnline

    override fun setOnline(p0: Boolean) {
        isGridOnline = p0
    }
    override fun getMainNode(): IManagedGridNode? = gridHolder.mainNode.setExposedOnSides(EnumSet.allOf(Direction::class.java))

    @DescSynced
    @Persisted
    override var wirelessMachinePersisted = createWirelessMachinePersisted()
    override var wirelessMachineRunTime = createWirelessMachineRunTime()

    override fun isRemote() = super<MetaMachine>.isRemote
    override fun attachSideTabs(sideTabs: TabsWidget) {
        sideTabs.mainTab = getSetupFancyUIProvider()
        sideTabs.attachSubTab(getDetailFancyUIProvider())
    }

    override fun onLoad() {
        super.onLoad()
        onWirelessMachineLoad()
    }

    override fun onMachinePlaced(player: LivingEntity?, stack: ItemStack?) {
        super.onMachinePlaced(player, stack)
        onWirelessMachinePlaced(player, stack)
    }

    override fun onUnload() {
        onWirelessMachineUnLoad()
        super.onUnload()
    }

    override fun clientTick() {
        onWirelessMachineClientTick()
        super.clientTick()
    }

    override fun getUIRequesterUUID(): UUID = TeamUtil.getTeamUUID(playerUUID)

    override fun createUI(entityPlayer: Player): ModularUI {
        playerUUID = entityPlayer.uuid
        return (ModularUI(176, 166, this, entityPlayer)).widget(
            InitFancyMachineUIWidget(
                this,
                176,
                166,
            ) { if (entityPlayer is ServerPlayer)syncDataToClientInServer() },
        )
    }
}

package com.gtocore.integration.ae

import com.gtocore.api.gui.ktflexible.InitFancyMachineUIWidget

import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

import appeng.api.networking.GridFlags
import appeng.api.networking.IManagedGridNode
import appeng.api.util.AECableType
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget
import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder
import com.gtolib.api.annotation.SyncedManager
import com.gtolib.api.capability.ISync
import com.gtolib.api.network.SyncManagedFieldHolder
import com.lowdragmc.lowdraglib.gui.modular.ModularUI
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted

class MeWirelessConnectMachine(holder: MetaMachineBlockEntity) :
    MetaMachine(holder),
    WirelessMachine,
    IMachineLife,
    ISync,
    IFancyUIMachine {
    companion object {

        @JvmStatic
        val syncManager = SyncManagedFieldHolder(MeWirelessConnectMachine::class.java)
    }
    val gridHolder = GridNodeHolder(this)

    @DescSynced
    var isGridOnline: Boolean = false

    override fun isOnline(): Boolean = isGridOnline
    override fun getCableConnectionType(dir: Direction): AECableType = AECableType.DENSE_SMART

    override fun setOnline(p0: Boolean) {
        isGridOnline = p0
    }
    override fun getMainNode(): IManagedGridNode? = gridHolder.mainNode

    init {
        this.getMainNode()?.setFlags(GridFlags.DENSE_CAPACITY)
    }

    @DescSynced
    @Persisted
    override var wirelessMachinePersisted = createWirelessMachinePersisted()

    @SyncedManager
    override var wirelessMachineRunTime = createWirelessMachineRunTime()

    override fun isRemote() = super<MetaMachine>.isRemote
    override fun attachSideTabs(sideTabs: TabsWidget) {
        sideTabs.mainTab = getSetupFancyUIProvider()
    }

    override fun onLoad() {
        super.onLoad()
        registerSync()
        onWirelessMachineLoad()
    }

    override fun onMachinePlaced(player: LivingEntity?, stack: ItemStack?) {
        super.onMachinePlaced(player, stack)
        onWirelessMachinePlaced(player, stack)
    }

    override fun onUnload() {
        onWirelessMachineUnLoad()
        unregisterSync()
        super.onUnload()
    }

    override fun clientTick() {
        onWirelessMachineClientTick()
        super.clientTick()
    }

    override fun createUI(entityPlayer: Player): ModularUI = (ModularUI(176, 166, this, entityPlayer)).widget(
        InitFancyMachineUIWidget(
            this,
            176,
            166,
        ) { if (!isRemote)refreshCachesOnServer() },
    )

    override fun getSyncHolder(): SyncManagedFieldHolder = syncManager
}

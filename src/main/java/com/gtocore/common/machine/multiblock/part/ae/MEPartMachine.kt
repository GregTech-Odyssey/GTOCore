package com.gtocore.common.machine.multiblock.part.ae

import com.gtocore.api.gui.ktflexible.InitFancyMachineUIWidget
import com.gtocore.integration.ae.WirelessMachine
import com.gtocore.integration.ae.WirelessMachinePersisted
import com.gtocore.integration.ae.WirelessMachineRunTime

import net.minecraft.MethodsReturnNonnullByDefault
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.items.IItemHandlerModifiable

import appeng.api.networking.IManagedGridNode
import appeng.api.networking.security.IActionSource
import com.gregtechceu.gtceu.api.GTValues
import com.gregtechceu.gtceu.api.capability.recipe.IO
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget
import com.gregtechceu.gtceu.api.item.tool.GTToolType
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDistinctPart
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder
import com.gtolib.api.machine.feature.IMEPartMachine
import com.gtolib.api.machine.feature.multiblock.IExtendedRecipeCapabilityHolder
import com.hepdd.gtmthings.utils.TeamUtil
import com.lowdragmc.lowdraglib.gui.modular.ModularUI
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder
import com.mojang.datafixers.util.Pair

import java.util.*
import javax.annotation.ParametersAreNonnullByDefault

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
internal abstract class MEPartMachine(holder: IMachineBlockEntity, io: IO) :
    TieredIOPartMachine(holder, GTValues.LuV, io),
    WirelessMachine,
    IMEPartMachine,
    IDistinctPart,
    IMachineLife {
    @Persisted
    private val nodeHolder: GridNodeHolder = GridNodeHolder(this)

    @DescSynced
    var onlineField: Boolean = false

    val actionSourceField: IActionSource = IActionSource.ofMachine { nodeHolder.getMainNode().node }

    @Persisted
    protected var distinctField: Boolean = false

    @Persisted
    var isAllFacing: Boolean = false

    override fun getItemHandlerCap(side: Direction?, useCoverCapability: Boolean): IItemHandlerModifiable? = null

    override fun getFluidHandlerCap(side: Direction?, useCoverCapability: Boolean): IFluidHandlerModifiable? = null

    override fun onToolClick(toolType: MutableSet<GTToolType>, itemStack: ItemStack, context: UseOnContext): Pair<GTToolType?, InteractionResult?> {
        val result = super.onToolClick(toolType, itemStack, context)
        if (result.second == InteractionResult.PASS && toolType.contains(GTToolType.WIRE_CUTTER)) {
            val player = context.player
            if (player == null) return result
            return Pair.of<GTToolType?, InteractionResult?>(GTToolType.WIRE_CUTTER, onWireCutterClick(player, context.hand))
        }
        return result
    }

    override fun shouldRenderGrid(player: Player, pos: BlockPos, state: BlockState, held: ItemStack, toolTypes: MutableSet<GTToolType?>): Boolean = super.shouldRenderGrid(player, pos, state, held, toolTypes) || toolTypes.contains(GTToolType.WIRE_CUTTER)

    private fun onWireCutterClick(playerIn: Player, hand: InteractionHand): InteractionResult {
        playerIn.swing(hand)
        if (isAllFacing) {
            getMainNode().setExposedOnSides(EnumSet.of(this.getFrontFacing()))
            if (isRemote) {
                playerIn.displayClientMessage(Component.translatable("gtocore.me_front"), true)
            }
            isAllFacing = false
        } else {
            getMainNode().setExposedOnSides(EnumSet.allOf(Direction::class.java))
            if (isRemote) {
                playerIn.displayClientMessage(Component.translatable("gtocore.me_any"), true)
            }
            isAllFacing = true
        }
        return InteractionResult.CONSUME
    }

    override fun onLoad() {
        super.onLoad()
        if (isAllFacing) {
            mainNode.setExposedOnSides(EnumSet.allOf(Direction::class.java))
        }
        if (isRemote) return
        onWirelessMachineLoad()
        handlerList.isDistinct = distinctField
        handlerList.setColor(paintingColor)
    }

    override fun getMainNode(): IManagedGridNode = nodeHolder.getMainNode()

    override fun onRotated(oldFacing: Direction, newFacing: Direction) {
        super.onRotated(oldFacing, newFacing)
        mainNode.setExposedOnSides(EnumSet.of(newFacing))
    }

    override fun getFieldHolder(): ManagedFieldHolder = MANAGED_FIELD_HOLDER

    override fun onPaintingColorChanged(color: Int) {
        for (c in getControllers()) {
            if (c is IExtendedRecipeCapabilityHolder) {
                c.arrangeDistinct()
            }
        }
    }

    override fun isDistinct(): Boolean = distinctField

    override fun setDistinct(isDistinct: Boolean) {
        this.distinctField = isDistinct
        handlerList.isDistinct = isDistinct
        for (controller in getControllers()) {
            if (controller is IExtendedRecipeCapabilityHolder) {
                controller.arrangeDistinct()
            }
        }
    }

    override fun setOnline(isOnline: Boolean) {
        this.onlineField = isOnline
    }

    override fun isOnline(): Boolean = this.onlineField

    override fun getActionSource(): IActionSource = this.actionSourceField

    override fun onMachinePlaced(player: LivingEntity?, stack: ItemStack?) {
        super.onMachinePlaced(player, stack)
        onWirelessMachinePlaced(player, stack)
    }

    override fun clientTick() {
        onWirelessMachineClientTick()
        super.clientTick()
    }

    override fun onUnload() {
        onWirelessMachineUnLoad()
        super.onUnload()
    }

    companion object {
        val MANAGED_FIELD_HOLDER: ManagedFieldHolder = ManagedFieldHolder(
            MEPartMachine::class.java,
            TieredIOPartMachine.MANAGED_FIELD_HOLDER,
        )

        const val CONFIG_SIZE: Int = 16
    }

    // ////////////////////////////////
    // ****** 无线连接设置 ******//
    // //////////////////////////////
    var playerUUID: UUID = UUID.randomUUID()
    override fun createUI(entityPlayer: Player?): ModularUI? {
        playerUUID = entityPlayer?.uuid ?: UUID.randomUUID()
        return (ModularUI(176, 166, this, entityPlayer)).widget(InitFancyMachineUIWidget(this, 176, 166) { if (entityPlayer is ServerPlayer)syncDataToClientInServer() })
    }

    @Persisted
    @DescSynced
    override var wirelessMachinePersisted: WirelessMachinePersisted = createWirelessMachinePersisted()
    override var wirelessMachineRunTime: WirelessMachineRunTime = createWirelessMachineRunTime()
    override fun attachSideTabs(sideTabs: TabsWidget) {
        super<TieredIOPartMachine>.attachSideTabs(sideTabs)
        sideTabs.attachSubTab(getSetupFancyUIProvider())
        sideTabs.attachSubTab(getDetailFancyUIProvider())
    }
    override fun getUIRequesterUUID(): UUID = TeamUtil.getTeamUUID(playerUUID)
}

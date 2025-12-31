package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.machine.trait.WirelessComputationContainerTrait;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IWailaDisplayProvider;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.WorkableMultiblockPartMachine;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.hepdd.gtmthings.api.capability.IBindable;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;

import java.util.UUID;

import static com.gtocore.integration.jade.GTOJadePlugin.getProgress;
import static com.hepdd.gtmthings.utils.TeamUtil.GetName;

public final class WirelessNetworkComputationHatchMachine extends WorkableMultiblockPartMachine implements IInteractedMachine, IBindable, IWailaDisplayProvider {

    private final WirelessComputationContainerTrait trait;

    public WirelessNetworkComputationHatchMachine(MetaMachineBlockEntity holder, boolean transmitter) {
        super(holder);
        trait = new WirelessComputationContainerTrait(this, transmitter);
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    @Override
    public boolean canShared() {
        return false;
    }

    @Override
    public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.getItemInHand(hand).is(GTItems.TOOL_DATA_STICK.asItem())) {
            setOwnerUUID(player.getUUID());
            if (isRemote()) {
                player.sendSystemMessage(Component.translatable("gtmthings.machine.wireless_energy_hatch.tooltip.bind", GetName(player)));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean onLeftClick(Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction) {
        if (player.getItemInHand(hand).is(GTItems.TOOL_DATA_STICK.asItem())) {
            setOwnerUUID(null);
            if (isRemote()) {
                player.sendSystemMessage(Component.translatable("gtmthings.machine.wireless_energy_hatch.tooltip.unbind"));
            }
            return true;
        }
        return false;
    }

    @Override
    @Nullable
    public UUID getUUID() {
        return trait.getUUID();
    }

    @Override
    public void appendWailaTooltip(CompoundTag data, ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        long capacity = data.getLong("capacity");
        if (capacity == 0) return;
        long storage = data.getLong("storage");
        IElementHelper helper = iTooltip.getElementHelper();
        iTooltip.add(helper.progress(getProgress(storage, capacity), Component.literal(storage + " / " + capacity + " CWU"), iTooltip.getElementHelper().progressStyle().color(0xFF006D6A).textColor(-1), Util.make(BoxStyle.DEFAULT, style -> style.borderColor = 0xFF555555), true));
    }

    @Override
    public void appendWailaData(CompoundTag data, BlockAccessor blockAccessor) {
        var c = trait.getWirelessComputationContainer();
        if (c == null) return;
        data.putLong("capacity", c.getCapacity());
        data.putLong("storage", c.getStorage());
    }
}

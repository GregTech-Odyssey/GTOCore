package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.machine.trait.WirelessComputationContainerTrait;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.hepdd.gtmthings.api.capability.IBindable;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static com.hepdd.gtmthings.utils.TeamUtil.GetName;

public final class WirelessNetworkComputationHatchMachine extends MultiblockPartMachine implements IMachineLife, IInteractedMachine, IBindable {

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
    public void onMachinePlaced(@Nullable LivingEntity player, ItemStack stack) {
        if (player != null) {
            setOwnerUUID(player.getUUID());
        }
    }

    @Override
    @Nullable
    public UUID getUUID() {
        return trait.getUUID();
    }

    public WirelessComputationContainerTrait getTrait() {
        return this.trait;
    }
}

package com.gtocore.common.machine.mana.part;

import com.gtolib.api.GTOValues;
import com.gtolib.api.machine.mana.trait.NotifiableManaContainer;
import com.gtolib.api.machine.mana.trait.NotifiableWirelessManaContainer;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.hepdd.gtmthings.api.capability.IBindable;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static com.hepdd.gtmthings.utils.TeamUtil.GetName;

public final class WirelessManaHatchPartMachine extends ManaHatchPartMachine implements IInteractedMachine, IBindable {

    public WirelessManaHatchPartMachine(MetaMachineBlockEntity holder, int tier, IO io, int rate) {
        super(holder, tier, io, rate);
    }

    @Override
    NotifiableManaContainer createManaContainer(int rate) {
        int tierMana = GTOValues.MANA[tier] * rate;
        if (io == IO.OUT) {
            return new NotifiableWirelessManaContainer(this, IO.OUT, 256L * tierMana, 4L * tierMana);
        } else return new NotifiableWirelessManaContainer(this, IO.IN, 256L * tierMana, 4L * tierMana);
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
    public @Nullable UUID getUUID() {
        return ((NotifiableWirelessManaContainer) getManaContainer()).getUUID();
    }
}

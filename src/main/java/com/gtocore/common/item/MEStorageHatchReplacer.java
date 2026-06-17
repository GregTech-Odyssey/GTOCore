package com.gtocore.common.item;

import com.gtocore.common.data.machines.GTAEMachines;
import com.gtocore.common.machine.multiblock.part.ae.StorageAccessPartMachine;

import com.gtolib.api.item.IMachineUpgraderBehavior;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Supplier;

public enum MEStorageHatchReplacer implements IMachineUpgraderBehavior {

    Long(() -> GTAEMachines.ME_STORAGE_ACCESS_HATCH),
    BigInt(() -> GTAEMachines.ME_BIG_STORAGE_ACCESS_HATCH),
    LongIO(() -> GTAEMachines.ME_IO_STORAGE_ACCESS_HATCH),;

    private final Supplier<MachineDefinition> upgradeTo;

    MEStorageHatchReplacer(Supplier<MachineDefinition> upgradeTo) {
        this.upgradeTo = upgradeTo;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var pos = context.getClickedPos();
        var world = context.getLevel();
        var tile = world.getBlockEntity(pos);
        if (tile instanceof MetaMachineBlockEntity mbe &&
                mbe.getMetaMachine() instanceof StorageAccessPartMachine machine) {

            var originState = world.getBlockState(pos);
            var state = copyBlockStateProperties(originState, upgradeTo.get().get().defaultBlockState());

            BlockEntity upgradedTile = upgradeTo.get().get().newBlockEntity(pos, state);
            if (upgradedTile instanceof MetaMachineBlockEntity upgradedMbe &&
                    upgradedMbe.getMetaMachine() instanceof StorageAccessPartMachine upgradedMachine &&
                    machine.getDefinition() != upgradedMachine.getDefinition()) {

                replaceBlockEntityWithNBTHook(world, pos, tile, upgradedTile, state, null);
                state.getBlock().setPlacedBy(context.getLevel(), pos, state, context.getPlayer(), context.getItemInHand());

                ItemStack replaced = machine.getDefinition().asStack();
                if (context.getPlayer() instanceof ServerPlayer player) {
                    player.playNotifySound(upgradedMachine.getBlockState().getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 2.0F);
                    context.getItemInHand().shrink(1);
                    if (!context.getPlayer().getInventory().add(replaced)) context.getPlayer().drop(replaced, false);
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }
}

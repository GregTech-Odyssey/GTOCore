package com.gtocore.common.item;

import com.gtocore.common.data.machines.GTAEMachines;
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.function.Supplier;

public enum PatternBufferUpgraderBehavior implements IInteractionItem {

    PatternBuffer(() -> GTAEMachines.ME_PATTERN_BUFFER),
    ExPatternBuffer(() -> GTAEMachines.ME_EXTEND_PATTERN_BUFFER),
    UltraPatternBuffer(() -> GTAEMachines.ME_EXTEND_PATTERN_BUFFER_ULTRA),;

    private final Supplier<MachineDefinition> upgradeTo;

    PatternBufferUpgraderBehavior(Supplier<MachineDefinition> upgradeTo) {
        this.upgradeTo = upgradeTo;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var pos = context.getClickedPos();
        var world = context.getLevel();
        var tile = world.getBlockEntity(pos);
        if (tile instanceof MetaMachineBlockEntity mbe &&
                mbe.getMetaMachine() instanceof MEPatternBufferPartMachineKt machine) {
            var originState = world.getBlockState(pos);
            var state = upgradeTo.get().getBlock().defaultBlockState();
            for (var sp : originState.getValues().entrySet()) {
                var pt = sp.getKey();
                var va = sp.getValue();
                try {
                    if (state.hasProperty(pt)) {
                        state = state.<Comparable, Comparable>setValue((Property) pt, va);
                    }
                } catch (Exception ignore) {
                    // NO-OP
                }
            }
            BlockEntity upgradedTile = upgradeTo.get().get().newBlockEntity(pos, state);
            if (upgradedTile instanceof MetaMachineBlockEntity upgradedMbe &&
                    upgradedMbe.getMetaMachine() instanceof MEPatternBufferPartMachineKt upgradedMachine &&
                    machine.getMaxPatternCount() < upgradedMachine.getMaxPatternCount()) {
                machine.unregisterSync();
                replaceBufferPart(world, pos, tile, upgradedTile, state, machine.getMaxPatternCount(), upgradedMachine.getMaxPatternCount());
                upgradedMachine.registerSync();
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

    private static void replaceBufferPart(Level world, BlockPos pos, BlockEntity oldTile, BlockEntity newTile, BlockState newBlock, int oldSize, int newSize) {
        var contents = oldTile.serializeNBT();
        world.removeBlockEntity(pos);
        world.removeBlock(pos, false);
        world.setBlock(pos, newBlock, 3);
        world.setBlockEntity(newTile);
        operateContentsNBT(contents, oldSize, newSize);
        newTile.deserializeNBT(contents);
    }

    private static void operateContentsNBT(CompoundTag contents, int oldSize, int newSize) {
        contents.getCompound("patternInventory").putInt("size", newSize);
        for (int i = oldSize; i < newSize; i++) {
            CompoundTag innerTag = new CompoundTag();
            innerTag.put("t", ByteTag.valueOf((byte) 0));
            CompoundTag tag = new CompoundTag();
            tag.put("t", ByteTag.valueOf((byte) 11));
            tag.put("p", innerTag);
            contents.getList("internalInventory", Tag.TAG_COMPOUND).add(tag);
        }
    }
}

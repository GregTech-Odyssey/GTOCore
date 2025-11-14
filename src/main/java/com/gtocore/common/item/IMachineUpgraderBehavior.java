package com.gtocore.common.item;

import com.gregtechceu.gtceu.api.item.component.IInteractionItem;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Shared helpers for machine upgrade interactions.
 * Provides utilities for copying block state properties and replacing block entities while
 * allowing a caller-provided hook to operate on the serialized NBT prior to deserialization.
 */
public interface IMachineUpgraderBehavior extends IInteractionItem {

    default BlockState copyBlockStateProperties(BlockState originState, BlockState targetDefaultState) {
        var state = targetDefaultState;
        for (var sp : originState.getValues().entrySet()) {
            var pt = sp.getKey();
            var va = sp.getValue();
            try {
                if (state.hasProperty(pt)) {
                    state = state.<Comparable, Comparable>setValue((net.minecraft.world.level.block.state.properties.Property) pt, va);
                }
            } catch (Exception ignore) {
                // NO-OP: preserve best-effort copying of compatible properties
            }
        }
        return state;
    }

    default void replaceBlockEntityWithNBTHook(Level world, BlockPos pos, BlockEntity oldTile, BlockEntity newTile, BlockState newBlock, @Nullable Consumer<CompoundTag> nbtOperator) {
        var contents = oldTile.serializeNBT();
        world.removeBlockEntity(pos);
        world.removeBlock(pos, false);
        world.setBlock(pos, newBlock, 3);
        world.setBlockEntity(newTile);
        if (nbtOperator != null) nbtOperator.accept(contents);
        newTile.deserializeNBT(contents);
    }
}

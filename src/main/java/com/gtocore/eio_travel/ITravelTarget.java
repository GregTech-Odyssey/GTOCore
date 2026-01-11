package com.gtocore.eio_travel;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public interface ITravelTarget {

    ResourceLocation getSerializationName();

    BlockPos getPos();

    CompoundTag save();

    int getItem2BlockRange();

    int getBlock2BlockRange();

    /**
     * @return Whether the target can be teleported to.
     */
    default boolean canTeleportTo() {
        return true;
    }

    /**
     * @return Whether the target can be jumped to like an elevator.
     */
    default boolean canJumpTo() {
        return true;
    }
}

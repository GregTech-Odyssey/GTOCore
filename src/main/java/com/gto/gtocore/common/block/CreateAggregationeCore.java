package com.gto.gtocore.common.block;

import com.gto.gtocore.common.data.GTOItems;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.NotNull;

public final class CreateAggregationeCore extends Block {

    public CreateAggregationeCore(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        BlockPos[] poss = new BlockPos[] { pos.offset(1, 0, 0),
                pos.offset(0, 1, 0),
                pos.offset(0, 0, 1),
                pos.offset(-1, 0, 0),
                pos.offset(0, -1, 0),
                pos.offset(0, 0, -1) };
        for (BlockPos p : poss) {
            Block block = level.getBlockState(p).getBlock();
            if (block == Blocks.COMMAND_BLOCK || block == Blocks.CHAIN_COMMAND_BLOCK || block == Blocks.REPEATING_COMMAND_BLOCK) {
                level.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    @NotNull
    public InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (player.getMainHandItem().is(GTOItems.COMMAND_WAND.get())) {
            BlockPos offset = pos.offset(0, 1, 0);
            if (level.getBlockState(offset).getBlock() == Blocks.AIR) {
                ItemStack offItem = player.getOffhandItem();
                if (offItem.getItem() == Blocks.COMMAND_BLOCK.asItem()) {
                    player.setItemInHand(InteractionHand.OFF_HAND, offItem.copyWithCount(offItem.getCount() - 1));
                    level.setBlockAndUpdate(offset, Blocks.COMMAND_BLOCK.defaultBlockState());
                    return InteractionResult.CONSUME;
                }
                if (offItem.getItem() == Blocks.CHAIN_COMMAND_BLOCK.asItem()) {
                    player.setItemInHand(InteractionHand.OFF_HAND, offItem.copyWithCount(offItem.getCount() - 1));
                    level.setBlockAndUpdate(offset, Blocks.CHAIN_COMMAND_BLOCK.defaultBlockState());
                    return InteractionResult.CONSUME;
                }
                if (offItem.getItem() == Blocks.REPEATING_COMMAND_BLOCK.asItem()) {
                    player.setItemInHand(InteractionHand.OFF_HAND, offItem.copyWithCount(offItem.getCount() - 1));
                    level.setBlockAndUpdate(offset, Blocks.REPEATING_COMMAND_BLOCK.defaultBlockState());
                }
            }
        }
        return InteractionResult.PASS;
    }
}

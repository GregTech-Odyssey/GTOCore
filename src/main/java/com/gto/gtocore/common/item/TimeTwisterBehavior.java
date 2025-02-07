package com.gto.gtocore.common.item;

import com.gto.gtocore.common.entity.TaskEntity;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;

import java.util.Objects;

public final class TimeTwisterBehavior implements IInteractionItem {

    public static final TimeTwisterBehavior INSTANCE = new TimeTwisterBehavior();

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = Objects.requireNonNull(context.getPlayer());
        WirelessEnergyContainer container = WirelessEnergyContainer.getOrCreateContainer(context.getPlayer().getUUID());
        RecipeLogic recipeLogic = GTCapabilityHelper.getRecipeLogic(context.getLevel(), context.getClickedPos(), null);
        if (player.isShiftKeyDown()) {
            if (container.removeEnergy(819200, null) == 819200) {
                context.getLevel().addFreshEntity(new TaskEntity(context.getLevel(), context.getClickedPos(), e -> tick(e, context.getLevel(), context.getClickedPos())));
            }
        } else {
            if (recipeLogic != null && recipeLogic.isWorking()) {
                MetaMachine machine = recipeLogic.getMachine();
                if (machine instanceof IOverclockMachine overclockMachine) {
                    int reducedDuration = (int) ((recipeLogic.getDuration() - recipeLogic.getProgress()) * 0.5);
                    long eu = 8 * reducedDuration * overclockMachine.getOverclockVoltage();
                    if (eu > 0 && container.removeEnergy(eu, null) == eu) {
                        recipeLogic.setProgress(recipeLogic.getProgress() + reducedDuration);
                        player.displayClientMessage(Component.literal("消耗了 " + FormattingUtil.formatNumbers(eu) + " EU，使机器运行时间减少了 " + reducedDuration + " tick"), true);
                        return InteractionResult.CONSUME;
                    }
                }
            } else if (isBlockEntity(context)) {
                if (container.removeEnergy(8192, null) == 8192) {
                    tickBlock(context.getLevel(), context.getClickedPos(), 0);
                    player.displayClientMessage(Component.literal("消耗了 8192 EU，使方块实体额外执行了 200 Tick"), true);
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static boolean isBlockEntity(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block instanceof MetaMachineBlock) return false;
        return block instanceof EntityBlock && level.getBlockEntity(pos) != null;
    }

    private static void tick(TaskEntity entity, Level level, BlockPos pos) {
        if (entity.tickCount > 100) {
            entity.discard();
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) blockEntity.getPersistentData().remove("accelerate_tick");
            return;
        }
        tickBlock(level, pos, entity.tickCount);
    }

    private static void tickBlock(Level level, BlockPos pos, int tick) {
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();
        if (level instanceof ServerLevel && block.isRandomlyTicking(blockState)) blockState.randomTick((ServerLevel) level, pos, level.getRandom());
        if (block instanceof EntityBlock entityBlock) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity == null) return;
            // noinspection unchecked
            BlockEntityTicker<BlockEntity> ticker = (BlockEntityTicker<BlockEntity>) entityBlock.getTicker(level, blockState, blockEntity.getType());
            if (ticker == null) return;
            for (int i = 0; i < 200; i++) {
                if (blockEntity.isRemoved()) break;
                ticker.tick(level, pos, blockState, blockEntity);
            }
            if (tick > 0) blockEntity.getPersistentData().putInt("accelerate_tick", tick);
        }
    }
}

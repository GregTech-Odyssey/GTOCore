package com.gtocore.common.item;

import com.gtocore.common.entity.TaskEntity;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.wireless.ExtendWirelessEnergyContainer;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
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

import java.math.BigInteger;

@DataGeneratorScanned
public final class TimeTwisterBehavior implements IInteractionItem {

    @RegisterLanguage(cn = "消耗了 %s EU，使方块实体额外执行了 %s Tick", en = "Consumed %s EU, making the block entity execute an additional %s ticks")
    private static final String CONSUMED = "gtocore.item.time_twister.consumed";

    public static final TimeTwisterBehavior INSTANCE = new TimeTwisterBehavior();

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getLevel().isClientSide()) return InteractionResult.PASS;
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;
        ExtendWirelessEnergyContainer container = (ExtendWirelessEnergyContainer) WirelessEnergyContainer.getOrCreateContainer(context.getPlayer().getUUID());
        if (player.isShiftKeyDown() && container.removeEnergy(819200, null) == 819200) {
            if (isBlockEntity(context)) {
                context.getLevel().addFreshEntity(new TaskEntity(context.getLevel(), context.getClickedPos(), e -> tick(container, e, context, false)));
            } else {
                context.getLevel().addFreshEntity(new TaskEntity(context.getLevel(), context.getClickedPos(), e -> tick(container, e, context, true)));
            }
        } else if (container.removeEnergy(8192, null) == 8192) {
            if (isBlockEntity(context)) {
                tickBlock(context.getLevel(), context.getClickedPos(), 0);
                player.displayClientMessage(Component.translatable(CONSUMED, 8192, 200), true);
                return InteractionResult.CONSUME;
            } else if (tickGT(container, context)) {
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    private static boolean tickGT(ExtendWirelessEnergyContainer container, UseOnContext context) {
        RecipeLogic recipeLogic = GTCapabilityHelper.getRecipeLogic(context.getLevel().getBlockEntity(context.getClickedPos()));
        if (recipeLogic != null && recipeLogic.isWorking()) {
            MetaMachine machine = recipeLogic.getMachine();
            if (machine instanceof IOverclockMachine overclockMachine) {

                GTRecipe recipe = recipeLogic.getLastRecipe();
                if (recipe == null || recipe.getOutputEUt() > 0) {
                    return false;
                }

                int maxReducedDuration = Math.max((int) ((recipeLogic.getDuration() - recipeLogic.getProgress()) * 0.5), 10);
                int tickEUMultiplier = 2 << GTOCore.difficulty;
                BigInteger eu = BigInteger.valueOf(overclockMachine.getOverclockVoltage()).multiply(BigInteger.valueOf(tickEUMultiplier));
                BigInteger canUsedEU = BigInteger.valueOf(container.getRate()).min(container.getStorage()).min(eu.multiply(BigInteger.valueOf(maxReducedDuration)));
                if (eu.compareTo(BigInteger.ZERO) > 0 && canUsedEU.compareTo(eu) > 0) {
                    var tick = canUsedEU.divide(eu).intValue();
                    var usedEU = eu.multiply(BigInteger.valueOf(tick));
                    container.setStorage(container.getStorage().subtract(usedEU));
                    recipeLogic.setProgress(recipeLogic.getProgress() + tick);
                    if (context.getPlayer() == null) return false;
                    context.getPlayer().displayClientMessage(Component.translatable(CONSUMED, FormattingUtil.formatNumbers(usedEU), tick), true);
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isBlockEntity(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block instanceof MetaMachineBlock) return false;
        return block instanceof EntityBlock && level.getBlockEntity(pos) != null;
    }

    private static void tick(ExtendWirelessEnergyContainer container, TaskEntity entity, UseOnContext context, boolean gt) {
        if (entity.tickCount > 100) {
            entity.discard();
            if (!gt) {
                BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
                if (blockEntity != null) blockEntity.getPersistentData().remove("accelerate_tick");
            }
            return;
        }
        if (gt) tickGT(container, context);
        else tickBlock(context.getLevel(), context.getClickedPos(), entity.tickCount);
    }

    private static void tickBlock(Level level, BlockPos pos, int tick) {
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();
        if (level instanceof ServerLevel && block.isRandomlyTicking(blockState))
            blockState.randomTick((ServerLevel) level, pos, level.getRandom());
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

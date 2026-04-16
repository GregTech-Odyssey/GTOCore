package com.gtocore.common.item;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.entity.TaskEntity;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.mana.feature.IManaContainerMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.wireless.ExtendWirelessEnergyContainer;
import com.gtolib.api.wireless.WirelessManaContainer;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
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
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import javax.annotation.Nullable;

import static com.gtocore.integration.jade.GTOJadePlugin.getProgress;

@DataGeneratorScanned
public final class TimeTwisterBehavior implements IInteractionItem {

    @RegisterLanguage(cn = "消耗了 %s EU，使方块实体额外执行了 %s Tick", en = "Consumed %s EU, making the block entity execute an additional %s ticks")
    private static final String CONSUMED_EU = "gtocore.item.time_twister.consumed_eu";
    @RegisterLanguage(cn = "消耗了 %s Mana，使方块实体额外执行了 %s Tick", en = "Consumed %s Mana, making the block entity execute an additional %s ticks")
    private static final String CONSUMED_MANA = "gtocore.item.time_twister.consumed_mana";
    @RegisterLanguage(cn = "预计消耗 %s EU（当前无线能源的 %s%%）", en = "Expected to consume %s EU (%s%% of current energy storage)")
    private static final String EXPECTED_EU = "gtocore.item.time_twister.expected_eu";
    @RegisterLanguage(cn = "预计消耗 %s Mana（当前无线魔力的 %s%%）", en = "Expected to consume %s Mana (%s%% of current mana storage)")
    private static final String EXPECTED_MANA = "gtocore.item.time_twister.expected_mana";

    public static final TimeTwisterBehavior INSTANCE = new TimeTwisterBehavior();
    public static final int TASK_DURATION = 100;

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getLevel().isClientSide()) return InteractionResult.PASS;
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;
        ExtendWirelessEnergyContainer euContainer = (ExtendWirelessEnergyContainer) WirelessEnergyContainer.getOrCreateContainer(context.getPlayer().getUUID());
        WirelessManaContainer manaContainer = WirelessManaContainer.getOrCreateContainer(context.getPlayer().getUUID());
        if (player.isShiftKeyDown() && euContainer.removeEnergy(819200, null) == 819200) {
            if (isBlockEntity(context)) {
                context.getLevel().addFreshEntity(new TaskEntity(context.getLevel(), context.getClickedPos(), e -> tick(euContainer, manaContainer, e, context, false)));
            } else {
                context.getLevel().addFreshEntity(new TaskEntity(context.getLevel(), context.getClickedPos(), e -> tick(euContainer, manaContainer, e, context, true)));
            }
        } else if (euContainer.removeEnergy(8192, null) == 8192) {
            if (isBlockEntity(context)) {
                tickBlock(context.getLevel(), context.getClickedPos(), 0);
                player.displayClientMessage(Component.translatable(CONSUMED_EU, 8192, 200), true);
                return InteractionResult.CONSUME;
            } else if (tickGT(euContainer, manaContainer, context, false) != null) {
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    public static void appendWailaData(CompoundTag data, BlockAccessor blockAccessor) {
        var player = blockAccessor.getPlayer();
        var item = player.getMainHandItem();
        if (item.getItem() != GTOItems.TIME_TWISTER.asItem()) return;
        ExtendWirelessEnergyContainer euContainer = (ExtendWirelessEnergyContainer) WirelessEnergyContainer.getOrCreateContainer(player.getUUID());
        WirelessManaContainer manaContainer = WirelessManaContainer.getOrCreateContainer(player.getUUID());

        var machine = blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity ? ((MetaMachineBlockEntity) blockAccessor.getBlockEntity()).metaMachine : null;
        BigInteger euOrMana = tickGT(euContainer, manaContainer, new UseOnContext(
                player, InteractionHand.MAIN_HAND, blockAccessor.getHitResult()), true);
        boolean isMana = machine instanceof IManaContainerMachine;
        var storage = isMana ? manaContainer.getStorage() : euContainer.getStorage();
        if (euOrMana == null && storage.compareTo(BigInteger.ZERO) <= 0) {
            return;
        }
        if (euOrMana == null && machine == null) {
            euOrMana = BigInteger.valueOf(8192L);
        }
        if (player.isShiftKeyDown() && euOrMana != null) {
            euOrMana = euOrMana.multiply(BigInteger.valueOf(100L));
        }

        if (euOrMana != null) {
            data.putString("time_twister_cost", euOrMana.toString());
            data.putBoolean("time_twister_is_mana", isMana);
            data.putFloat("time_twister_consumption", new BigDecimal(euOrMana).divide(new BigDecimal(storage), 2, RoundingMode.HALF_UP).floatValue());
        }
    }

    public static void appendWailaTooltip(CompoundTag data, ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        var cost = data.getString("time_twister_cost");
        if (cost.isEmpty()) return;
        var isMana = data.getBoolean("time_twister_is_mana");
        var consumption = data.getFloat("time_twister_consumption");
        var formattedBig = FormattingUtil.formatNumbers(new BigInteger(cost));
        var formattedPercent = FormattingUtil.formatNumber2Places(consumption * 100);
        if (isMana) {
            iTooltip.add(Component.translatable(EXPECTED_MANA, formattedBig, formattedPercent));
        } else {
            iTooltip.add(Component.translatable(EXPECTED_EU, formattedBig, formattedPercent));
        }
    }

    private static @Nullable BigInteger tickGT(ExtendWirelessEnergyContainer euContainer, WirelessManaContainer manaContainer, UseOnContext context, boolean simulate) {
        RecipeLogic recipeLogic = GTCapabilityHelper.getRecipeLogic(context.getLevel().getBlockEntity(context.getClickedPos()));
        if (recipeLogic == null || !recipeLogic.isWorking()) {
            return null;
        }
        MetaMachine machine = recipeLogic.getMachine();
        GTRecipe recipe = recipeLogic.getLastRecipe();
        if (recipe == null) {
            return null;
        }

        int maxReducedDuration = Math.max((int) ((recipeLogic.getDuration() - recipeLogic.getProgress()) * 0.5), 10);
        int energyMultiplier = 2 << GTOCore.difficulty;

        if (machine instanceof IOverclockMachine) {
            var eut = BigInteger.valueOf(recipe.getInputEUt());
            if (eut.compareTo(BigInteger.ZERO) <= 0) return null;
            eut = eut.multiply(BigInteger.valueOf(energyMultiplier));

            var limit = euContainer.getStorage().min(BigInteger.valueOf(euContainer.getRate()));
            if (limit.compareTo(eut) < 0) return null;
            var tick = limit.divide(eut).min(BigInteger.valueOf(maxReducedDuration));
            var usedEU = eut.multiply(tick);
            if (!simulate) {
                euContainer.setStorage(euContainer.getStorage().subtract(usedEU));
                recipeLogic.setProgress(recipeLogic.getProgress() + tick.intValue());
                if (context.getPlayer() == null) return null;
                context.getPlayer().displayClientMessage(Component.translatable(CONSUMED_EU, FormattingUtil.formatNumbers(usedEU), tick), true);
            }
            return usedEU;
        } else if (machine instanceof IManaContainerMachine) {
            var manat = BigInteger.valueOf(Recipe.of(recipe).getInputMANAt());
            if (manat.compareTo(BigInteger.ZERO) <= 0) return null;
            manat = manat.multiply(BigInteger.valueOf(energyMultiplier));

            var storage = manaContainer.getStorage();
            if (storage.compareTo(manat) < 0) return null;
            var tick = storage.divide(manat).min(BigInteger.valueOf(maxReducedDuration));
            var usedMana = manat.multiply(tick);

            if (!simulate) {
                manaContainer.setStorage(manaContainer.getStorage().subtract(usedMana));
                recipeLogic.setProgress(recipeLogic.getProgress() + tick.intValue());
                if (context.getPlayer() == null) return null;
                context.getPlayer().displayClientMessage(Component.translatable(CONSUMED_MANA, FormattingUtil.formatNumbers(usedMana), tick), true);
            }
            return usedMana;
        }

        return null;
    }

    private static boolean isBlockEntity(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block instanceof MetaMachineBlock) return false;
        return block instanceof EntityBlock && level.getBlockEntity(pos) != null;
    }

    private static void tick(ExtendWirelessEnergyContainer euContainer, WirelessManaContainer manaContainer, TaskEntity entity, UseOnContext context, boolean gt) {
        if (entity.tickCount > TASK_DURATION) {
            entity.discard();
            if (!gt) {
                BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
                if (blockEntity != null) blockEntity.getPersistentData().remove("accelerate_tick");
            }
            return;
        }
        if (gt) tickGT(euContainer, manaContainer, context, false);
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

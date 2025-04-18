package com.gto.gtocore.common.item.tools;

import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class GrassHarvesterBehaviour extends TooltipBehavior implements IInteractionItem {

    private static final Map<Block, Map<Item, Float>> HARVESTABLE_BLOCKS = Map.of(
            Blocks.GRASS, Map.of(
                    Items.WHEAT_SEEDS, 0.9F,
                    Items.BEETROOT_SEEDS, 0.1F,
                    Items.MELON_SEEDS, 0.005F,
                    Items.PUMPKIN_SEEDS, 0.005F,
                    Items.POTATO, 0.005F,
                    Items.CARROT, 0.005F));

    public GrassHarvesterBehaviour() {
        super(lines -> {
            lines.add(Component.translatable("gtocore.behaviour.grass_harvest.description"));
            lines.add(Component.translatable("gtocore.behaviour.grass_harvest.description2"));
        });
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        ItemStack itemInHand = context.getItemInHand();
        List<ItemStack> bonus = new ArrayList<>(List.of());
        if (level instanceof ServerLevel serverLevel && !level.isClientSide() && player != null) {
            RandomSource random = level.getRandom();
            BlockPos clickedPos = context.getClickedPos();
            Block block = serverLevel.getBlockState(clickedPos).getBlock();
            AtomicBoolean isMatch = new AtomicBoolean(false);
            // 生成bonus
            HARVESTABLE_BLOCKS.forEach((matchBlock, items) -> {
                if (block.equals(matchBlock)) {
                    isMatch.set(true);
                    items.forEach((item, chance) -> {
                        if (random.nextFloat() < chance) {
                            bonus.add(new ItemStack(item, (int) Math.max(1, chance * (random.nextInt(1, 2)))));
                        }
                    });
                }
            });
            if (isMatch.get()) {
                // 掉落物
                bonus.forEach(itemStack -> Block.popResource(level, clickedPos, itemStack));
                // 消耗耐久
                itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
                // 破坏方块
                level.destroyBlock(clickedPos, false);
                level.playSound(null, clickedPos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F + random.nextFloat() * 0.4F);
            } else {
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.SUCCESS;
    }
}

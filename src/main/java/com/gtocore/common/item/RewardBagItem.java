package com.gtocore.common.item;

import com.gtolib.GTOCore;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RewardBagItem extends Item {

    @Getter
    private final ResourceLocation defaultLootTable;

    public RewardBagItem(Properties properties, ResourceLocation defaultLootTable) {
        super(properties
                .stacksTo(16)
                .rarity(Rarity.UNCOMMON));
        this.defaultLootTable = defaultLootTable;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack bagStack = player.getItemInHand(usedHand);
        if (level instanceof ServerLevel serverLevel) {
            // 获取关联的战利品表
            LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(defaultLootTable);
            if (lootTable == LootTable.EMPTY) {
                GTOCore.LOGGER.error("Loot table not registered: {}", defaultLootTable);
                return InteractionResultHolder.fail(bagStack);
            }
            // 1. 计算消耗数量
            int consumeCount = player.isShiftKeyDown() ? bagStack.getCount() : 1;
            if (!player.isCreative()) bagStack.shrink(consumeCount);
            // 2. 构建战利品上下文
            LootParams params = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.TOOL, bagStack)
                    .withParameter(LootContextParams.ORIGIN, player.position())
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .create(LootContextParamSets.FISHING);
            // 3. 执行总次数的抽奖
            for (int i = 0; i < consumeCount; i++) {
                lootTable.getRandomItems(params, serverLevel.getRandom().nextLong(), item -> {
                    if (item.isEmpty()) return;
                    ItemEntity itemEntity = player.spawnAtLocation(item);
                    if (itemEntity != null) itemEntity.setNoPickUpDelay();
                });
            }
            // 播放打开音效
            level.playSound(null, player.blockPosition(), SoundEvents.CHEST_OPEN, SoundSource.PLAYERS, 0.8F, 1.0F);
            return InteractionResultHolder.success(bagStack);
        }
        return InteractionResultHolder.consume(bagStack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.item.reward_bag.increases").withStyle(ChatFormatting.BLUE));
    }

    @Override
    public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack, @NotNull Enchantment enchantment) {
        return enchantment == Enchantments.BLOCK_FORTUNE || enchantment == Enchantments.BLOCK_EFFICIENCY;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return true;
    }
}

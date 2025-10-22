package com.gtocore.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * 1.20.1 Forge 兼容版奖励袋物品，支持时运附魔（运行时根据时运等级增加抽奖次数）
 */
public class RewardBagItem extends Item {

    public static final String TAG_LOOT_TABLE = "LootTable";
    private final ResourceLocation defaultLootTable; // 关联的战利品表ID

    // 构造函数：初始化物品属性并绑定默认战利品表
    public RewardBagItem(Properties properties, ResourceLocation defaultLootTable) {
        super(properties
                .stacksTo(64)
                .rarity(Rarity.UNCOMMON));
        this.defaultLootTable = defaultLootTable;
    }

    /**
     * 核心：允许物品被时运附魔
     */
    @Override
    public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack, @NotNull Enchantment enchantment) {
        return enchantment == Enchantments.BLOCK_FORTUNE;
    }

    /**
     * 确保物品可被附魔
     */
    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return true;
    }

    /**
     * 打开奖励袋逻辑：根据时运等级增加额外抽奖次数
     */
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack bagStack = player.getItemInHand(usedHand);
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            // 获取关联的战利品表
            ResourceLocation lootTableId = getLootTableId(bagStack);
            if (lootTableId == null) {
                return InteractionResultHolder.fail(bagStack);
            }
            LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(lootTableId);
            if (lootTable == LootTable.EMPTY) {
                return InteractionResultHolder.fail(bagStack);
            }

            // 1. 计算消耗数量（Shift+右键消耗全部）
            int consumeCount = player.isShiftKeyDown() ? bagStack.getCount() : 1;
            if (!player.isCreative()) {
                bagStack.shrink(consumeCount);
            }

            // 2. 获取时运等级（从奖励袋自身附魔中读取）
            int fortuneLevel = getFortuneLevel(bagStack);

            // 3. 计算总抽奖次数：基础次数（消耗数量） + 额外次数（消耗数量 × 时运等级）
            int bonusRolls = consumeCount * fortuneLevel;
            int totalRolls = consumeCount + bonusRolls;

            // 4. 构建战利品上下文
            LootParams params = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .withParameter(LootContextParams.ORIGIN, player.position())
                    .withParameter(LootContextParams.TOOL, bagStack) // 传入自身（冗余但兼容之前逻辑）
                    .create(LootContextParamSets.CHEST);

            // 5. 执行总次数的抽奖
            for (int i = 0; i < totalRolls; i++) {
                lootTable.getRandomItems(params, player.getLootTableSeed(),
                        item -> Objects.requireNonNull(player.spawnAtLocation(item)).setNoPickUpDelay());
            }

            // 播放打开音效
            level.playSound(null, player.blockPosition(), SoundEvents.ALLAY_ITEM_GIVEN,
                    SoundSource.PLAYERS, 0.8F, 1.0F);
            return InteractionResultHolder.success(bagStack);
        }
        return InteractionResultHolder.consume(bagStack);
    }

    /**
     * 物品 tooltip：显示时运提示和额外次数规则
     */
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        ResourceLocation lootTableId = getLootTableId(stack);
        if (lootTableId != null) {
            tooltip.add(Component.literal("战利品表: " + lootTableId).withStyle(ChatFormatting.GRAY));
        }
        // 明确提示时运与额外次数的关系
        tooltip.add(Component.literal("时运每级增加等量额外奖励").withStyle(ChatFormatting.BLUE));
        int fortuneLevel = getFortuneLevel(stack);
        if (fortuneLevel > 0) {
            tooltip.add(Component.translatable("enchantment.minecraft.block_fortune")
                    .append(" " + fortuneLevel)
                    .withStyle(ChatFormatting.GREEN));
        }
    }

    /**
     * 获取物品的时运等级
     */
    private int getFortuneLevel(ItemStack stack) {
        return stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
    }

    /**
     * 获取关联的战利品表ID
     */
    @Nullable
    public ResourceLocation getLootTableId(ItemStack stack) {
        String tableKey = stack.getOrCreateTag().getString(TAG_LOOT_TABLE);
        if (!tableKey.isEmpty()) {
            return ResourceLocation.tryParse(tableKey);
        }
        return defaultLootTable;
    }
}

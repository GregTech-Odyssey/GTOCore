package com.gtocore.data.lootTables.GTOLootItemFunction;

import com.gtolib.utils.RegistriesUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import org.jetbrains.annotations.NotNull;

public class lootRegistrationTool {

    /**
     * 为任意 LootPoolSingletonContainer 子类的 Builder 追加自定义逻辑
     * 泛型约束 T extends LootPoolSingletonContainer.Builder<T>：匹配递归泛型（T 是自身 Builder 的子类型）
     */
    public static <T extends LootItem.Builder<T>> @NotNull T appendCustomLogic(
                                                                               @NotNull T baseBuilder,
                                                                               CustomLogicFunction.LootLogic logic) {
        return baseBuilder.apply(new CustomLogicFunction.Builder(logic));
    }

    /**
     * 创建固定数量的物品战利品条目
     */
    public static @NotNull LootItem.Builder getLootItem(Item item, int weight, int count) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)));
    }

    /**
     * 创建随机数量范围的物品战利品条目
     */
    public static @NotNull LootItem.Builder getLootItemWithRange(Item item, int weight, int minCount, int maxCount) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)));
    }

    /**
     * 创建带随机附魔的物品战利品条目
     */
    public static @NotNull LootItem.Builder getEnchantedLootItem(Item item, int weight, int count) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)))
                .apply(EnchantRandomlyFunction.randomEnchantment());
    }

    /**
     * 创建引用其他战利品表的条目
     */
    public static @NotNull LootTableReference.Builder getLootTableReference(ResourceLocation tableId, int weight) {
        return LootTableReference.lootTableReference(tableId)
                .setWeight(weight);
    }

    // -------------------------- 自定义逻辑示例（泛型类型匹配） --------------------------
    // 示例1：给玩家添加10个钻石
    public static final CustomLogicFunction.LootLogic GIVE_DIAMONDS_LOGIC = (player, level, entity, pos, tool, stack) -> {
        if (player != null) {
            ItemStack diamonds = new ItemStack(RegistriesUtils.getItem("minecraft:diamond"), 10);
            if (!player.getInventory().add(diamonds)) {
                player.drop(diamonds, false); // 背包满则掉落
            }
            player.sendSystemMessage(Component.literal("你获得了10个钻石！"));
        }
    };

    // 示例2：在战利品位置生成一个宝箱
    public static final CustomLogicFunction.LootLogic SPAWN_CHEST_LOGIC = (player, level, entity, pos, tool, stack) -> {
        BlockPos chestPos = pos.above();
        if (level.isEmptyBlock(chestPos)) { // 检查位置是否可放置
            level.setBlockAndUpdate(chestPos, RegistriesUtils.getBlock("minecraft:chest").defaultBlockState());
            if (player != null) {
                player.sendSystemMessage(Component.literal("附近生成了宝箱：" + chestPos));
            }
        }
    };

    // 示例3：删除相关实体
    public static final CustomLogicFunction.LootLogic REMOVE_ENTITY_LOGIC = (player, level, entity, pos, tool, stack) -> {
        if (entity != null && !entity.isRemoved()) {
            entity.remove(Entity.RemovalReason.DISCARDED);
            if (player != null) {
                player.sendSystemMessage(Component.literal("已清除实体：" + entity.getType().getDescriptionId()));
            }
        }
    };

    // 示例4：基于 getLootItemWithRange 添加自定义逻辑（泛型自动匹配）
    public static final LootItem.Builder diamondEntry = appendCustomLogic(
            getLootItemWithRange(Items.DIAMOND, 80, 2, 4),
            (player, level, entity, pos, tool, stack) -> {
                if (player != null) {
                    player.sendSystemMessage(Component.literal("发现了钻石！"));
                }
            });
}

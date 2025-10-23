package com.gtocore.data.lootTables;

import com.gtolib.utils.RegistriesUtils;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import static com.gtocore.data.lootTables.GTOLootItemFunction.lootRegistrationTool.*;
import static com.gtocore.data.lootTables.GTOLootItemFunction.lootRegistrationTool.diamondEntry;
import static com.gtocore.data.lootTables.RewardBagLoot.*;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;
import static net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition.invert;
import static net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition.randomChance;

public class DemoLoot {

    /**
     * 战利品表创建方法集合，包含多种场景的战利品配置
     * 基于Minecraft 1.20.1 Forge API，通过LootPool（战利品池）和LootItem（战利品项）定义掉落规则
     */

    /**
     * 创建史诗级箱子的战利品表（适用于高价值箱子，如遗迹宝箱）
     * 核心逻辑：分三个互斥/补充池，通过条件控制不同场景的掉落
     */
    private static LootTable createEpicChestTable() {
        // 池1：基础奖励池（必掉落，受玩家幸运值影响）
        // 设计思路：确保每次打开都有基础收益，幸运值提升额外奖励
        LootPool.Builder basePool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(2)) // 基础抽取次数：固定2次（每次从池中随机选一个物品）
                .setBonusRolls(UniformGenerator.between(0, 2)) // 幸运值加成：每点幸运值额外增加0-2次抽取
                // 物品1：钻石（权重100，必掉项，数量1-3个）
                // 权重越高，在池中被抽中的概率越大（100为高权重，几乎必出）
                .add(getLootItemWithRange(RegistriesUtils.getItem("minecraft:diamond"), 100, 1, 3))
                // 物品2：附魔铁胸甲（权重80，较高概率出现）
                .add(getEnchantedLootItem(RegistriesUtils.getItem("minecraft:iron_chestplate"), 80, 1));

        ResourceLocation VANILLA_DUNGEON_REFERENCE = fromNamespaceAndPath("minecraft", "chests/simple_dungeon");

        // 池2：稀有奖励池（仅在特定条件下触发）
        // 设计思路：鼓励玩家使用特定工具（钻石镐）开采，提升玩法策略性
        LootPool.Builder rarePool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1)) // 固定抽取1次
                // 触发条件：玩家使用的工具必须是钻石镐
                // MatchTool.toolMatches()用于匹配工具类型，ItemPredicate定义工具条件
                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(RegistriesUtils.getItem("minecraft:diamond_pickaxe"))))
                // 物品1：下界之星（权重30，低概率稀有物品）
                .add(getLootItem(RegistriesUtils.getItem("minecraft:nether_star"), 30, 1))
                // 物品2：引用地牢战利品表（权重70，较高概率获取地牢稀有物品）
                // LootTableReference用于嵌套其他战利品表，复用已有掉落规则
                .add(getLootTableReference(VANILLA_DUNGEON_REFERENCE, 70));

        // 池3：诅咒奖励池（低概率触发，与稀有池互斥）
        // 设计思路：增加随机性惩罚，未使用指定工具时有概率触发
        LootPool.Builder cursePool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1)) // 固定抽取1次
                // 触发条件1：10%的随机概率
                .when(randomChance(0.1f))
                // 触发条件2：未使用钻石镐（对稀有池的条件取反）
                .when(invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(RegistriesUtils.getItem("minecraft:diamond_pickaxe")))))
                // 惩罚物品：10个腐肉（权重100，必出）
                .add(getLootItem(RegistriesUtils.getItem("minecraft:rotten_flesh"), 100, 10));

        // 构建最终战利品表
        return LootTable.lootTable()
                .withPool(basePool) // 添加基础池
                .withPool(rarePool) // 添加稀有池
                .withPool(cursePool) // 添加诅咒池
                // 关联场景：方块破坏（BLOCK）
                // 该场景支持工具判断（如上述钻石镐条件），适用于开采方块时掉落战利品
                .setParamSet(LootContextParamSets.BLOCK)
                .build();
    }

    /**
     * 创建季节性礼物袋的战利品表（适用于节日活动物品，如圣诞礼物）
     * 核心逻辑：低概率稀有物品+高概率基础奖励，突出节日专属物品
     */
    private static LootTable createSeasonalGiftTable() {
        // 池1：节日专属物品池（低概率稀有掉落）
        LootPool.Builder seasonalPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1)) // 固定抽取1次
                .when(randomChance(0.1f)) // 触发概率：10%（仅10%的概率会从该池掉落物品）
                .add(LootItem.lootTableItem(Items.GOLDEN_APPLE) // 节日专属物品：黄金苹果
                        .setWeight(100) // 权重100（池中唯一物品，触发则必出）
                        .apply(SetNameFunction.setName(Component.literal("节日金苹果")))); // 自定义名称，突出节日特性

        // 池2：额外惊喜池（高概率基础奖励）
        LootPool.Builder surprisePool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1)) // 固定抽取1次
                .when(randomChance(0.5f)) // 触发概率：50%
                .add(LootItem.lootTableItem(Items.EMERALD) // 绿宝石（基础货币奖励）
                        .setWeight(100) // 高权重
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))) // 数量：2-5个
                .add(LootTableReference.lootTableReference(ADVENTURER_BAG_LOOT[0]) // 引用冒险者背包战利品表
                        .setWeight(50)); // 权重50（与绿宝石的抽取概率比为1:2）

        // 构建战利品表
        return LootTable.lootTable()
                .withPool(seasonalPool)
                .withPool(surprisePool)
                // 关联场景：实体（ENTITY）
                // 适用于实体掉落、物品右键使用等场景（如玩家使用礼物袋时触发）
                .setParamSet(LootContextParamSets.ENTITY)
                .build();
    }

    /**
     * 创建冒险奖励箱的战利品表（适用于地牢、遗迹中的普通奖励箱）
     * 核心逻辑：基础奖励保底+难度关联奖励+惩罚机制，适应不同游戏难度
     */
    private static LootTable createAdventureRewardTable() {
        // 池1：基础奖励池（必掉落，保底收益）
        LootPool.Builder basePool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1)) // 固定抽取1次
                .add(LootItem.lootTableItem(Items.IRON_INGOT) // 铁锭（基础资源）
                        .setWeight(100)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6)))); // 数量：3-6个

        // 池2：困难模式奖励池（高难度专属奖励）
        LootPool.Builder difficultyPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1)) // 固定抽取1次
                .add(LootItem.lootTableItem(Items.DIAMOND) // 钻石（中高价值资源）
                        .setWeight(60) // 权重60
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))) // 数量：1-2个
                .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE) // 附魔金苹果（稀有消耗品）
                        .setWeight(20) // 权重20（与钻石的抽取概率比为1:3）
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))); // 固定1个

        // 池3：惩罚奖励池（低价值物品，平衡整体收益）
        LootPool.Builder penaltyPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1)) // 固定抽取1次
                .add(LootItem.lootTableItem(Items.ROTTEN_FLESH) // 腐肉（低价值物品）
                        .setWeight(100)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))); // 数量：2-4个

        // 构建战利品表
        return LootTable.lootTable()
                .withPool(basePool)
                .withPool(difficultyPool)
                .withPool(penaltyPool)
                // 关联场景：箱子（CHEST）
                // 适用于容器类方块（如箱子、 barrels）的战利品掉落
                .setParamSet(LootContextParamSets.CHEST)
                .build();
    }

    /**
     * 创建下界探险袋的战利品表（适用于下界主题的物品包）
     * 核心逻辑：下界特色物品+元素奖励+稀有工具，突出下界地域特性
     */
    private static LootTable createNetherExplorerBagTable() {
        // 池1：下界基础物品池（必掉，体现下界地域特色）
        LootPool.Builder netherBasePool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(2)) // 抽取2次（每次随机选一个物品）
                .add(LootItem.lootTableItem(Items.NETHERRACK) // 下界岩（下界基础方块）
                        .setWeight(100)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 8)))) // 数量：4-8个
                .add(LootItem.lootTableItem(Items.GOLD_NUGGET) // 金粒（下界常见资源）
                        .setWeight(80) // 权重80（与下界岩的抽取概率比为4:5）
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6)))); // 数量：3-6个

        // 池2：火焰奖励池（下界元素特色奖励）
        LootPool.Builder firePool = LootPool.lootPool()
                .setRolls(UniformGenerator.between(1, 2)) // 随机抽取1-2次
                .add(LootItem.lootTableItem(Items.BLAZE_ROD) // 烈焰棒（下界火焰生物掉落物）
                        .setWeight(70)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))) // 数量：1-3个
                .add(LootItem.lootTableItem(Items.FIRE_CHARGE) // 火焰弹（下界特色消耗品）
                        .setWeight(80) // 权重80（比烈焰棒略高）
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))) // 数量：2-5个
                // 自定义名称强化主题
                .apply(SetNameFunction.setName(Component.literal("下界火焰棒")));

        // 池3：稀有下界合金工具池（高价值稀有奖励）
        LootPool.Builder rareToolPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1)) // 固定抽取1次
                .add(LootItem.lootTableItem(Items.NETHERITE_PICKAXE) // 下界合金镐（顶级工具）
                        .setWeight(10) // 低权重（稀有掉落）
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))) // 固定1个
                        .apply(EnchantRandomlyFunction.randomEnchantment())); // 随机附魔（增加随机性）

        // 构建战利品表
        return LootTable.lootTable()
                .withPool(netherBasePool)
                .withPool(firePool)
                .withPool(rareToolPool)
                // 关联场景：实体（ENTITY）
                // 适用于物品使用（如下界探险袋右键打开）或实体掉落场景
                .setParamSet(LootContextParamSets.ENTITY)
                .build();
    }

    /**
     * 创建一个复杂的示范性奖池
     * 核心逻辑：下界特色物品+元素奖励+稀有工具，突出下界地域特性
     */
    public static LootPool createMultiConditionPool() {
        // -------------------------- 1. 时间条件：仅白天（0-12000刻） --------------------------
        IntRange dayTimeRange = IntRange.range(0, 12000);
        LootItemCondition.Builder timeCondition = TimeCheck.time(dayTimeRange)
                .setPeriod(24000); // 按全天周期（24000刻）循环

        // -------------------------- 3. 天气条件：仅下雨但不雷暴 --------------------------
        LootItemCondition.Builder weatherCondition = WeatherCheck.weather()
                .setRaining(true)    // 必须下雨
                .setThundering(false); // 不能是雷暴

        // -------------------------- 4. 数值检查条件：玩家幸运值≥2 --------------------------
        // ValueCheckCondition：检查NumberProvider的值是否在IntRange范围内
        // 这里检查"幸运值"（LootContextParams.LUCK）是否≥2
        // LootItemCondition luckCondition = ValueCheckCondition.hasValue(
        // LootContextParams.BLOCK_ENTITY, // 数值来源：玩家幸运值
        // IntRange.lowerBound(2) // 范围：≥2
        // );

        // -------------------------- 5. 位置条件：Y坐标≥60且在主世界 --------------------------
        // LocationCheck：基于位置谓词（LocationPredicate）检查位置
        LocationPredicate.Builder locationPredicate = LocationPredicate.Builder.location()
                // 条件1：Y坐标≥60
                .setY(MinMaxBounds.Doubles.between(60, 100))
                // 条件2：维度为主世界（overworld）
                .setDimension(Level.OVERWORLD);

        LootItemCondition.Builder locationCondition = LocationCheck.checkLocation(
                locationPredicate, // 位置谓词
                BlockPos.ZERO // 无坐标偏移（基于战利品生成原点）
        );

        // -------------------------- 7. 池内条目配置 --------------------------
        // 仅当所有条件满足时，才会抽取以下条目
        LootItem.Builder enchantedGoldApple = LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE)
                .setWeight(10) // 稀有物品，权重低
                .setQuality(3) // 幸运值越高，权重加成越多
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

        LootItem.Builder ironIngotEntry = LootItem.lootTableItem(Items.IRON_INGOT)
                .setWeight(100)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8)));

        // 条目1：苹果（权重100，固定1个，仅持有工具时生效）
        LootItem.Builder appleEntry = LootItem.lootTableItem(Items.APPLE)
                .setWeight(100)
                .setQuality(2)
                // .when(hasTool()) // 1.20.1中通过LootItemConditions工具类获取条件
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

        // 条目2：钻石（权重50，1-3个，50%概率生效）
        LootItem.Builder diamondEntry = LootItem.lootTableItem(Items.DIAMOND)
                .setWeight(50)
                .setQuality(1)
                .when(randomChance(0.5f)) // 50%概率条件
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)));

        // 条目3：引用子表（权重30）
        LootTableReference.Builder subTableEntry = LootTableReference.lootTableReference(
                new ResourceLocation("mymod:loots/rare_loot")).setWeight(30)
                .setQuality(0);

        // -------------------------- 8. 组装战利品池 --------------------------
        return LootPool.lootPool()
                .name("multi_condition_rare_pool")
                .when(timeCondition) // 应用所有条件
                .when(weatherCondition)
                .when(locationCondition)
                .setRolls(ConstantValue.exactly(1)) // 固定抽取1次
                .setBonusRolls(UniformGenerator.between(0, 1)) // 额外0-1次（受幸运值影响）
                .add(enchantedGoldApple)
                .add(ironIngotEntry)
                .add(appleEntry)
                .add(diamondEntry)
                .add(subTableEntry)
                .build();
    }
}

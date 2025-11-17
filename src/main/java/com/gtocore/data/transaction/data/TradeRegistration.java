package com.gtocore.data.transaction.data;

import com.gtocore.data.transaction.common.TradingStationMachine;
import com.gtocore.data.transaction.data.trade.TradeGroup0;
import com.gtocore.data.transaction.data.trade.UnlockTrade;
import com.gtocore.data.transaction.manager.TradeEntry;
import com.gtocore.data.transaction.manager.TradingManager;

import com.gtolib.GTOCore;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;

import java.util.List;

import static com.gtocore.data.transaction.data.TradeLang.TECH_OPERATOR_COIN;

/**
 * 交易实例注册示例：展示如何使用TradeEntry构建具体交易
 */
public class TradeRegistration {

    public static void init() {
        UnlockTrade.init();
        TradeGroup0.init();

        // 测试注册
        registerTestData();
    }

    /**
     * 这里是交易注册的核心
     * <p>
     * 交易存储在两个位置
     * 一个在 UnlockManager 中以 O2OOpenCacheHashMap<String, List<TradeEntry>> 的形式存储
     * 这里存储这一些交易接受，或者是阶段认证，等等不作为交易但是以交易的形式表达的操作
     * <p>
     * 一个存储在 TradingManager 中以 List<TradingShopGroup> - List<TradingShop> - List<TradeEntry> 多层间存储
     * 最顶层为商店组列表，商店组最大 32个。
     * 中层为商店组，每个商店组中最大包含 5个商店。
     * (but. 第 0个组 最多包含两个 (有主页等内容)，第 1个组最多包含 0个 (此页为玩家交易页设置))
     * 底层为商店，每个商店可以存储大量的交易，
     * 从顶层到底层以各种方式分类放入
     */

    /**
     * 已经注册的商店组
     * <p>
     * 0 - 欢迎来到格雷科技
     */

    // 创建交易条目实例
    public static TradeEntry createJungleBeaconTrade() {
        return new TradeEntry.Builder()
                // 设置UI显示：信标图标+描述
                .texture(new ItemStackTexture(Blocks.BEACON.asItem()))
                .description(List.of(
                        Component.literal("丛林祭坛仪式"),
                        Component.literal("消耗：10个绿宝石"),
                        Component.literal("效果：在祭坛位置生成信标")))
                // 解锁条件文本
                .unlockCondition("null")

                // 输入资源（交易消耗）
                .inputItem(new ItemStack(Items.EMERALD, 10)) // 10个绿宝石（假设"emerald"是绿宝石货币ID）

                // 前置检查：3x3区域有草方块 + 丛林群系
                .preCheck(TradeRegistration::checkJungleAndGrass)

                // 执行逻辑：在坐标生成信标
                .onExecute(TradeRegistration::spawnBeaconAtPos)
                .build();
    }

    // 前置检查逻辑：3x3区域有草方块且在丛林群系
    private static int checkJungleAndGrass(TradingStationMachine machine, TradeEntry entry) {
        Level level = machine.getLevel();

        // 关键：仅服务端执行，客户端直接返回false（避免客户端调用）
        if (level == null || level.isClientSide()) {
            return 0;
        }

        GTOCore.LOGGER.info("run checkJungleAndGrass");
        BlockPos centerPos = machine.getPos();

        // 2. 检查3x3区域内是否至少有1个草方块（中心坐标向xyz各扩展1格）
        boolean hasGrassIn3x3 = false;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos checkPos = centerPos.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(checkPos);
                    if (state.is(Blocks.GRASS_BLOCK)) {
                        hasGrassIn3x3 = true;
                        break; // 找到草方块就退出循环
                    }
                }
                if (hasGrassIn3x3) break;
            }
            if (hasGrassIn3x3) break;
        }

        return Integer.MAX_VALUE; // 同时满足群系和草方块条件才返回true
    }

    // 执行逻辑：在交易坐标生成信标
    private static void spawnBeaconAtPos(TradingStationMachine machine, int multiplier, TradeEntry entry) {
        Level world = machine.getLevel();
        BlockPos pos = machine.getPos();

        GTOCore.LOGGER.info("run spawnBeaconAtPos");

        // 在目标位置放置信标（替换原有方块）
        world.setBlock(pos, Blocks.BEACON.defaultBlockState(), 3); // 3=更新标志（同步客户端+触发方块更新）

        // 可选：添加粒子效果或音效增强体验
        world.levelEvent(2001, pos, Block.getId(Blocks.BEACON.defaultBlockState())); // 方块放置粒子
    }

    // --- 可配置的测试参数 ---
    private static final int NUMBER_OF_GROUPS = 10;    // 创建3个商店组
    private static final int SHOPS_PER_GROUP = 1;     // 每个组创建2个商店

    /**
     * 执行批量注册。
     * 建议在Mod的初始化阶段调用此方法。
     */
    public static void registerTestData() {
        TradingManager manager = TradingManager.getInstance();

        // 1. 定义测试用的交易条目模板
        List<TradeEntry> testTrades = createTestTradeTemplates();

        // 2. 循环创建商店组
        for (int groupIndex = 0; groupIndex < NUMBER_OF_GROUPS; groupIndex++) {
            String groupName = "测试组 " + (groupIndex + 1);

            // 添加商店组
            int registeredGroupIndex = manager.addShopGroup(
                    groupName,
                    "test.unlock.group." + (groupIndex + 1), // 解锁条件也动态化
                    new ItemStackTexture(Items.BOOK),
                    new ItemStackTexture(Items.EMERALD));

            // 3. 在当前组内循环创建商店
            for (int shopIndex = 0; shopIndex < SHOPS_PER_GROUP + groupIndex / 2; shopIndex++) {
                String shopName = "商店 " + (groupIndex + 1) + "-" + (shopIndex + 1);

                // 为商店添加一个简单的纹理（例如，使用箱子图标）
                ItemStackTexture shopTexture = new ItemStackTexture(Items.CHEST);

                // 添加商店
                int registeredShopIndex = manager.addShopByGroupIndex(
                        registeredGroupIndex,
                        shopName,
                        "test.unlock.shop." + (groupIndex + 1) + "." + (shopIndex + 1),
                        shopTexture);

                // 4. 向当前商店添加所有测试交易条目
                for (int i = 3; i < shopIndex * 2 + 5; i++) {
                    for (TradeEntry templateEntry : testTrades) {
                        manager.addTradeEntryByIndices(registeredGroupIndex, registeredShopIndex, templateEntry);
                    }
                }
            }
        }
    }

    /**
     * 创建并返回一个包含多个测试交易条目的列表。
     * 这些条目将作为模板被添加到所有测试商店中。
     * 
     * @return 测试交易条目列表
     */
    private static List<TradeEntry> createTestTradeTemplates() {
        // 示例1: 木头换面包 (来自 TradeRegistration)
        TradeEntry woodForBread = new TradeEntry.Builder()
                .texture(new ItemStackTexture(Items.BREAD))
                .description(List.of(Component.literal("10个木头 → 1个面包")))
                .unlockCondition("无解锁条件")
                .inputItem(new ItemStack(Items.OAK_WOOD, 10))
                .outputItem(new ItemStack(Items.BREAD, 1))
                .build();

        // 示例2: 绿宝石换信标 (来自 TradeRegistration)
        TradeEntry emeraldForBeacon = TradeRegistration.createJungleBeaconTrade();

        // 示例3: 新的测试交易 - 石头换 cobblestone
        TradeEntry stoneForCobblestone = new TradeEntry.Builder()
                .texture(new ItemStackTexture(Items.COBBLESTONE))
                .description(List.of(Component.literal("1个石头 → 2个圆石")))
                .unlockCondition("null")
                .inputItem(new ItemStack(Items.STONE, 1))
                .outputItem(new ItemStack(Items.COBBLESTONE, 2))
                .build();

        // 示例4: 新的测试交易 - 水和岩浆换黑曜石
        TradeEntry fluidsForObsidian = new TradeEntry.Builder()
                .texture(new ItemStackTexture(Items.OBSIDIAN))
                .description(List.of(Component.literal("1桶水 + 1桶岩浆 → 1个黑曜石")))
                .unlockCondition("null")
                .inputFluid(new FluidStack(Fluids.WATER, 1000))
                .inputFluid(new FluidStack(Fluids.LAVA, 1000))
                .outputItem(new ItemStack(Items.OBSIDIAN, 1))
                .build();

        // 示例5: 新的测试交易 - 使用货币
        TradeEntry currencyForDiamond = new TradeEntry.Builder()
                .texture(new ItemStackTexture(Items.DIAMOND))
                .description(List.of(Component.literal("1000单位货币 → 1个钻石")))
                .unlockCondition("需要解锁货币系统")
                .inputCurrency(TECH_OPERATOR_COIN, 1000)
                .outputItem(new ItemStack(Items.DIAMOND, 1))
                .build();

        // 返回所有模板
        return List.of(woodForBread, emeraldForBeacon, stoneForCobblestone, fluidsForObsidian, currencyForDiamond);
    }
}

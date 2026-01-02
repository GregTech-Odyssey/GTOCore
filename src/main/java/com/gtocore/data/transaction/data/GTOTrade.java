package com.gtocore.data.transaction.data;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.api.gui.StackTexture;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.data.transaction.data.trade.*;
import com.gtocore.data.transaction.manager.TradeData;
import com.gtocore.data.transaction.manager.TradeEntry;

import com.gtolib.GTOCore;
import com.gtolib.utils.WalletUtils;
import com.gtolib.utils.holder.IntObjectHolder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;

import java.util.ArrayList;
import java.util.List;

import static com.gtocore.data.transaction.TradingStationTool.addMultipliedItems;
import static com.gtocore.data.transaction.data.TradeLang.TECH_OPERATOR_COIN;
import static com.gtocore.data.transaction.data.trade.UnlockTrade.SINGLE_TRANSACTION;
import static com.gtocore.data.transaction.data.trade.UnlockTrade.UNLOCK_BASE;

/**
 * 交易实例注册示例：展示如何使用TradeEntry构建具体交易
 */
public final class GTOTrade {

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
    public static void init() {
        recipe();

        /* 解锁交易组 */
        UnlockTrade.init();

        /* 欢迎来到格雷科技 */
        WelcomeGroup.init();

        /* 员工交易中心 */
        PlayersGroup.init();

        /* 员工福利兑换中心 */
        WelfareGroup.init();

        /* 能源部 */
        EnergyGroup.init();
    }

    private static void recipe() {
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("palm_sized_bank"), GTOItems.PALM_SIZED_BANK.asStack(),
                " A ",
                "ABA",
                " A ",
                'A', new MaterialEntry(GTOTagPrefix.COIN, GTMaterials.Copper), 'B', GTItems.TERMINAL.asStack());

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("lv_trading_station"), GTOMachines.TRADING_STATION[GTValues.LV].asStack(),
                " A ",
                "ABA",
                " A ",
                'A', new MaterialEntry(GTOTagPrefix.COIN, GTMaterials.Copper), 'B', GTOItems.GREG_MEMBERSHIP_CARD.asStack());
    }

    /**
     * 简单的交易项目构建
     *
     * @param BuyingOrSelling 买入或售出，true消耗货币购买物品，false售出物品得到货币
     * @param unlockCondition 解锁标签
     * @param stack           物品堆
     * @param currency        货币种类
     * @param amount          货币数量
     * @return 构建好的简单交易项目
     */
    public static TradeEntry simpleItemTrading(boolean BuyingOrSelling, String unlockCondition, ItemStack stack, String currency, int amount) {
        TradeEntry.Builder builder = new TradeEntry.Builder().texture(new StackTexture(stack)).unlockCondition(unlockCondition);
        if (BuyingOrSelling) builder.inputCurrency(currency, amount).outputItem(stack);
        else builder.outputCurrency(currency, amount).inputItem(stack);
        return builder.build();
    }

    /**
     * 免费物品领取的交易项目构建
     *
     * @param unlockCondition 解锁标签
     * @param stack           物品堆
     * @return 构建好的免费物品领取交易项目
     */
    public static TradeEntry freeItemTrading(String unlockCondition, ItemStack stack) {
        return new TradeEntry.Builder().texture(new StackTexture(stack)).unlockCondition(unlockCondition).outputItem(stack).build();
    }

    /**
     * 简单的抽奖交易项目构建
     *
     * @param unlockCondition 解锁标签
     * @param currency        货币种类
     * @param amount          货币数量
     * @param components      抽奖描述
     * @param reward          抽奖内容
     * @return 构建好的简易抽奖交易项目
     */
    public static TradeEntry SimpleLotteryTrading(String unlockCondition, String currency, int amount, List<Component> components, List<IntObjectHolder<ItemStack>> reward) {
        ItemStack[] itemStacks = reward.stream().map(i -> i.obj).toArray(ItemStack[]::new);
        List<Component> description = new ArrayList<>(components);
        for (IntObjectHolder<ItemStack> entry : reward) {
            ItemStack itemStack = entry.obj;
            description.add(Component.translatable("gtocore.trade_lottery.weight", itemStack.getDisplayName(), itemStack.getCount(), entry.number));
        }
        List<IntObjectHolder<ItemStack>> finalReward = ImmutableList.copyOf(reward);
        TradeEntry.Builder builder = new TradeEntry.Builder()
                .texture(new StackTexture(itemStacks))
                .unlockCondition(unlockCondition)
                .description(description)
                .inputCurrency(currency, amount)
                .onExecute((a, b, c) -> performLottery(a, b, c, finalReward));
        return builder.build();
    }

    public static IntObjectHolder<ItemStack> lotteryItem(int weight, ItemStack itemStack) {
        return new IntObjectHolder<>(weight, itemStack);
    }

    // 执行逻辑：抽奖
    private static void performLottery(TradeData data, TradeEntry entry, int multiplier, List<IntObjectHolder<ItemStack>> rewards) {
        if (!(data.level() instanceof ServerLevel serverLevel)) return;
        int totalWeight = rewards.stream().mapToInt(i -> i.number).sum();
        List<ItemStack> stackList = new ArrayList<>();
        RandomSource random = serverLevel.getRandom();
        for (int i = 0; i < multiplier; i++) {
            int randomRoll = random.nextInt(totalWeight);
            int currentWeight = 0;
            for (IntObjectHolder<ItemStack> rewardEntry : rewards) {
                currentWeight += rewardEntry.number;
                if (randomRoll < currentWeight) {
                    ItemStack winningStack = rewardEntry.obj.copy();
                    stackList.add(winningStack);
                    break;
                }
            }
        }
        addMultipliedItems(data.outputItem(), stackList, 1, data.level(), data.pos());
    }

    /**
     * 简单的限次交易项目构建
     *
     * @param record        交易历史标记
     * @param time          交易历史保留时间
     * @param maxMultiplier 时间内最大交易次数
     * @return 构建好的简单交易项目
     */
    public static TradeEntry simpleLimitedTimesItemTrading(boolean BuyingOrSelling, String unlockCondition, ItemStack stack, String currency, int amount, String record, long time, int maxMultiplier) {
        TradeEntry.Builder builder = new TradeEntry.Builder()
                .texture(new StackTexture(stack))
                .unlockCondition(unlockCondition)
                .preCheck((a, b) -> checkMultiplier(a, b, record, maxMultiplier))
                .onExecute((a, b, c) -> performAddMultiplier(a, b, c, record, time));
        if (BuyingOrSelling) builder.inputCurrency(currency, amount).outputItem(stack);
        else builder.outputCurrency(currency, amount).inputItem(stack);
        return builder.build();
    }

    // 前置检查逻辑：检查交易历史次数
    public static int checkMultiplier(TradeData data, TradeEntry entry, String record, int maxMultiplier) {
        Level level = data.level();
        ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
        long amount = WalletUtils.getTransactionTotalAmount(data.uuid(), serverLevel, record);
        if (maxMultiplier > amount) return Math.toIntExact(maxMultiplier - amount);
        return 0;
    }

    // 执行逻辑：添加交易历史标记
    public static void performAddMultiplier(TradeData data, TradeEntry entry, int multiplier, String record, long time) {
        Level level = data.level();
        ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
        WalletUtils.addScheduledDeletion(data.uuid(), serverLevel, record, time, multiplier);
    }

    /**
     * 简单的单次交易项目构建
     *
     * @param tag 交易历史标记
     * @return 构建好的简单交易项目
     */
    public static TradeEntry simpleSingleTimesItemTrading(boolean BuyingOrSelling, String unlockCondition, ItemStack stack, String currency, int amount, String tag) {
        TradeEntry.Builder builder = new TradeEntry.Builder()
                .texture(new StackTexture(stack))
                .unlockCondition(unlockCondition)
                .preCheck((a, b) -> checkTag(a, b, tag))
                .onExecute((a, b, c) -> performTag(a, b, c, tag));
        if (BuyingOrSelling) builder.inputCurrency(currency, amount).outputItem(stack);
        else builder.outputCurrency(currency, amount).inputItem(stack);
        return builder.build();
    }

    // 前置检查逻辑：检查交易是否交易过
    public static int checkTag(TradeData data, TradeEntry entry, String tag) {
        Level level = data.level();
        ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
        if (!WalletUtils.containsTagValueInWallet(data.uuid(), serverLevel, SINGLE_TRANSACTION, tag)) return 1;
        return 0;
    }

    // 执行逻辑：添加交易历史标记
    public static void performTag(TradeData data, TradeEntry entry, int multiplier, String tag) {
        Level level = data.level();
        ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
        WalletUtils.addTagToWallet(data.uuid(), serverLevel, SINGLE_TRANSACTION, tag);
    }

    // 测试用交易
    public static List<TradeEntry> createTestTradeTemplates() {
        // 示例1: 木头换面包 (来自 TradeRegistration)
        TradeEntry woodForBread = new TradeEntry.Builder()
                .texture(new ItemStackTexture(Items.BREAD))
                .description(List.of(Component.literal("10个木头 → 1个面包")))
                .unlockCondition(UNLOCK_BASE)
                .inputItem(new ItemStack(Items.OAK_WOOD, 10))
                .outputItem(new ItemStack(Items.BREAD, 1))
                .build();

        // 示例3: 新的测试交易 - 石头换 cobblestone
        TradeEntry stoneForCobblestone = new TradeEntry.Builder()
                .texture(new StackTexture(Items.COBBLESTONE))
                .description(List.of(Component.literal("1个石头 → 2个圆石")))
                .unlockCondition(UNLOCK_BASE)
                .inputItem(new ItemStack(Items.STONE, 1))
                .outputItem(new ItemStack(Items.COBBLESTONE, 2))
                .build();

        // 示例4: 新的测试交易 - 水和岩浆换黑曜石
        TradeEntry fluidsForObsidian = new TradeEntry.Builder()
                .texture(new StackTexture(GTOMaterials.TranscendingMatter.getFluid(60000000)))
                .description(List.of(Component.literal("1桶水 + 1桶岩浆 → 1个黑曜石")))
                .unlockCondition(UNLOCK_BASE)
                .inputFluid(new FluidStack(Fluids.WATER, 1000))
                .inputFluid(new FluidStack(Fluids.LAVA, 1000))
                .outputItem(new ItemStack(Items.OBSIDIAN, 1))
                .build();

        // 示例5: 新的测试交易 - 使用货币
        TradeEntry currencyForDiamond = new TradeEntry.Builder()
                .texture(new StackTexture(Items.DIAMOND))
                .description(List.of(Component.literal("1000单位货币 → 1个钻石")))
                .unlockCondition(UNLOCK_BASE)
                .inputCurrency(TECH_OPERATOR_COIN, 1000)
                .outputItem(new ItemStack(Items.DIAMOND, 1))
                .build();

        // 返回所有模板
        return List.of(woodForBread, stoneForCobblestone, fluidsForObsidian, currencyForDiamond);
    }
}

package com.gtocore.data.transaction.data;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.api.gui.StackTexture;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.data.transaction.data.trade.*;
import com.gtocore.data.transaction.manager.TradeEntry;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;

import java.util.List;

import static com.gtocore.data.transaction.data.TradeLang.TECH_OPERATOR_COIN;
import static com.gtocore.data.transaction.data.trade.UnlockTrade.UNLOCK_BASE;

/**
 * 交易实例注册示例：展示如何使用TradeEntry构建具体交易
 */
public class GTOTrade {

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

        /** 解锁交易组 */
        UnlockTrade.init();

        /** 欢迎来到格雷科技 */
        WelcomeGroup.init();

        /** 员工交易中心 */
        PlayersGroup.init();

        /** 员工福利兑换中心 */
        WelfareGroup.init();

        /** 能源部 */
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
                'A', new MaterialEntry(GTOTagPrefix.COIN, GTMaterials.Copper), 'B', GTOItems.GRAY_MEMBERSHIP_CARD.asStack());
    }

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

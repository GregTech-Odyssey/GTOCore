package com.gtocore.data.transaction.data.trade;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.api.gui.StackTexture;
import com.gtocore.data.transaction.manager.TradeData;
import com.gtocore.data.transaction.manager.TradeEntry;
import com.gtocore.data.transaction.manager.TradingManager;

import com.gtolib.utils.WalletUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.gui.GuiTextures;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

import java.util.List;
import java.util.Set;

import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMachines.TRADING_STATION;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.data.transaction.data.TradeLang.TECH_OPERATOR_COIN;
import static com.gtocore.data.transaction.data.TradeLang.addTradeLang;
import static com.gtocore.data.transaction.data.trade.UnlockTrade.UNLOCK_BASE;
import static com.lowdragmc.lowdraglib.LDLib.random;

public final class WelcomeGroup {

    /**
     * 欢迎来到格雷科技
     * <p>
     * - 币兑区
     * - 会员区
     */
    public static void init() {
        int GroupIndex = TradingManager.INSTANCE.addShopGroup(
                addTradeLang("欢迎来到格雷科技", "Welcome to Gray Technology"),
                GuiTextures.GREGTECH_LOGO,
                GuiTextures.GREGTECH_LOGO);

        int ShopIndex1 = TradingManager.INSTANCE.addShopByGroupIndex(
                GroupIndex,
                addTradeLang("格雷科技销售部兑币区", "Currency Exchange Area of Gray Technology Sales Department"),
                UNLOCK_BASE,
                Set.of(TECH_OPERATOR_COIN),
                new ItemStackTexture(ChemicalHelper.get(GTOTagPrefix.COIN, Neutronium)));

        Material[] materials = { Copper, Cupronickel, Silver, Gold, Osmium, Naquadah, Neutronium, Adamantine, Infinity, Neutron };

        for (int i = 0; i < materials.length; i++) {
            TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1, createCoinExchangeTrade(materials[i], i));
            TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1, createCoinWithdrawTrade(materials[i], i));
        }

        int ShopIndex2 = TradingManager.INSTANCE.addShopByGroupIndex(
                GroupIndex,
                addTradeLang("格雷科技销售部会员区", "Membership Area of Gray Technology Sales Department"),
                UNLOCK_BASE,
                Set.of(TECH_OPERATOR_COIN),
                new ResourceTexture("minecraft:textures/mob_effect/luck.png"));

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex2, new TradeEntry.Builder()
                .texture(new ResourceTexture("minecraft:textures/mob_effect/luck.png"))
                .description(List.of(
                        Component.translatable(addTradeLang("每周签到", "Weekly check-in")),
                        Component.translatable(addTradeLang("领取幸运物资", "Claim lucky supplies"))))
                .unlockCondition(UNLOCK_BASE)
                .preCheck(WelcomeGroup::checkThisWeek)
                .onExecute(WelcomeGroup::performCheckIn)
                .build());

        for (int i = 1; i < 8; i++) {
            TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex2, new TradeEntry.Builder()
                    .texture(new StackTexture(TRADING_STATION[1].asStack()))
                    .addDescription(Component.translatable(addTradeLang("升级贸易站", "Upgrade Trading Station")))
                    .addDescription(Component.translatable("gtocore.trade_group.exchanged", "Tier" + (i + 1), "Tier" + i))
                    .unlockCondition(UNLOCK_BASE)
                    .inputItem(TRADING_STATION[i].asStack())
                    .inputCurrency(TECH_OPERATOR_COIN, 4 << (i * 3))
                    .outputItem(TRADING_STATION[i + 1].asStack())
                    .build());
        }
    }

    public static TradeEntry createCoinExchangeTrade(Material material, int tier) {
        ItemStack Coin = ChemicalHelper.get(GTOTagPrefix.COIN, material);
        return new TradeEntry.Builder()
                .texture(new ItemStackTexture(Coin))
                .description(List.of(Component.translatable("gtocore.trade_group.exchanged", Component.translatable("gtocore.currency." + TECH_OPERATOR_COIN), Coin.getDisplayName())))
                .unlockCondition(UNLOCK_BASE)
                .inputItem(Coin)
                .outputCurrency(TECH_OPERATOR_COIN, 1L << (tier * 3))
                .build();
    }

    public static TradeEntry createCoinWithdrawTrade(Material material, int tier) {
        ItemStack Coin = ChemicalHelper.get(GTOTagPrefix.COIN, material);
        return new TradeEntry.Builder()
                .texture(new ItemStackTexture(Coin))
                .description(List.of(Component.translatable("gtocore.trade_group.exchanged", Coin.getDisplayName(), Component.translatable("gtocore.currency." + TECH_OPERATOR_COIN))))
                .unlockCondition(UNLOCK_BASE)
                .inputCurrency(TECH_OPERATOR_COIN, 1L << (tier * 3))
                .outputItem(Coin)
                .build();
    }

    private static final String Weekly_check_in = "Weekly check-in";
    private static final long Weekly_time = 140L;

    // 前置检查逻辑：检查本周是否签过到
    private static int checkThisWeek(TradeData data, TradeEntry entry) {
        Level level = data.level();
        ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
        long time = WalletUtils.getGameMinuteKey(level) / Weekly_time * Weekly_time;
        if (WalletUtils.getTransactionMinuteAmount(data.uuid(), serverLevel, Weekly_check_in, time) == 0) return 1;
        return 0;
    }

    // 执行逻辑：添加标记，随机给予0-100技术员币
    private static void performCheckIn(TradeData data, TradeEntry entry, int multiplier) {
        Level level = data.level();
        ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
        WalletUtils.addTimingCompressionStrategyTransaction(data.uuid(), serverLevel, Weekly_check_in, Weekly_time, 1);
        WalletUtils.addCurrency(data.uuid(), serverLevel, TECH_OPERATOR_COIN, random.nextInt() & 100);
    }
}

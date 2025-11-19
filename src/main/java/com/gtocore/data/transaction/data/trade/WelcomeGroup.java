package com.gtocore.data.transaction.data.trade;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.data.transaction.common.TradingStationMachine;
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
import static com.gregtechceu.gtceu.common.data.GTMaterials.Gold;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Naquadah;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Neutronium;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Osmium;
import static com.gtocore.common.data.GTOMaterials.Adamantine;
import static com.gtocore.common.data.GTOMaterials.Infinity;
import static com.gtocore.data.transaction.data.TradeLang.TECH_OPERATOR_COIN;
import static com.gtocore.data.transaction.data.TradeLang.addTradeLang;
import static com.gtocore.data.transaction.data.trade.UnlockTrade.UNLOCK_BASE;
import static com.lowdragmc.lowdraglib.LDLib.random;

public class WelcomeGroup {

    /**
     * 欢迎来到格雷科技
     * <p>
     * - 币兑区
     * - 会员区
     */
    public static void init() {
        TradingManager manager = TradingManager.getInstance();
        int GroupIndex = manager.addShopGroup(
                addTradeLang("欢迎来到格雷科技", "Welcome to Gray Technology"),
                GuiTextures.GREGTECH_LOGO,
                GuiTextures.GREGTECH_LOGO);

        int ShopIndex1 = manager.addShopByGroupIndex(
                GroupIndex,
                addTradeLang("欢迎来到格雷科技销售部币兑区", "Welcome to the Currency Exchange Area of Gray Technology Sales Department"),
                UNLOCK_BASE,
                Set.of(TECH_OPERATOR_COIN),
                GuiTextures.GREGTECH_LOGO);

        Material[] materials = { Copper, Cupronickel, Silver, Gold, Osmium, Naquadah, Neutronium, Adamantine, Infinity };

        for (int i = 0; i < materials.length; i++) {
            manager.addTradeEntryByIndices(GroupIndex, ShopIndex1, createCoinExchangeTrade(materials[i], i));
            manager.addTradeEntryByIndices(GroupIndex, ShopIndex1, createCoinWithdrawTrade(materials[i], i));
        }

        int ShopIndex2 = manager.addShopByGroupIndex(
                GroupIndex,
                addTradeLang("欢迎来到格雷科技销售部会员区", "Welcome to the Membership Area of Gray Technology Sales Department"),
                UNLOCK_BASE,
                Set.of(TECH_OPERATOR_COIN),
                GuiTextures.GREGTECH_LOGO);

        manager.addTradeEntryByIndices(GroupIndex, ShopIndex2, createWeeklyCheckIn());
    }

    public static TradeEntry createCoinExchangeTrade(Material material, int tier) {
        ItemStack Coin = ChemicalHelper.get(GTOTagPrefix.COIN, material);
        return new TradeEntry.Builder()
                .texture(new ItemStackTexture(Coin))
                .description(List.of(Component.translatable(addTradeLang("将硬币兑换为技术员币", "Exchange coins for technician coins"))))
                .unlockCondition(UNLOCK_BASE)
                .inputItem(Coin)
                .outputCurrency(TECH_OPERATOR_COIN, 1L << (tier * 3))
                .build();
    }

    public static TradeEntry createCoinWithdrawTrade(Material material, int tier) {
        ItemStack Coin = ChemicalHelper.get(GTOTagPrefix.COIN, material);
        return new TradeEntry.Builder()
                .texture(new ItemStackTexture(Coin))
                .description(List.of(Component.translatable(addTradeLang("将技术员币提款为硬币", "Technician coins can be withdrawn as coins"))))
                .unlockCondition(UNLOCK_BASE)
                .inputCurrency(TECH_OPERATOR_COIN, 1L << (tier * 3))
                .outputItem(Coin)
                .build();
    }

    private static final String Weekly_check_in = "Weekly check-in";
    private static final long Weekly_time = 140L;

    private static TradeEntry createWeeklyCheckIn() {
        return new TradeEntry.Builder()
                .texture(new ResourceTexture("minecraft:textures/mob_effect/luck.png"))
                .description(List.of(
                        Component.literal(addTradeLang("每周签到", "Weekly check-in")),
                        Component.literal(addTradeLang("领取幸运物资", "Claim lucky supplies"))))
                .unlockCondition(UNLOCK_BASE)
                .preCheck(WelcomeGroup::checkThisWeek)
                .onExecute(WelcomeGroup::performCheckIn)
                .build();
    }

    // 前置检查逻辑：检查本周是否签过到
    private static int checkThisWeek(TradingStationMachine machine, TradeEntry entry) {
        Level level = machine.getLevel();
        ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
        long time = WalletUtils.getGameMinuteKey(level) / Weekly_time * Weekly_time;
        if (WalletUtils.getTransactionMinuteAmount(machine.getUuid(), serverLevel, Weekly_check_in, time) == 0) return 1;
        return 0;
    }

    // 执行逻辑：添加标记，随机给予0-100技术员币
    private static void performCheckIn(TradingStationMachine machine, int multiplier, TradeEntry entry) {
        Level level = machine.getLevel();
        ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
        WalletUtils.addDailyCompressionStrategyTransaction(machine.getUuid(), serverLevel, Weekly_check_in, Weekly_time, 1);
        WalletUtils.addCurrency(machine.getUuid(), serverLevel, TECH_OPERATOR_COIN, random.nextInt() & 100);
    }
}

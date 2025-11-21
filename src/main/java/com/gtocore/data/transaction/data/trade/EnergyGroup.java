package com.gtocore.data.transaction.data.trade;

import com.gtocore.api.gui.GTOGuiTextures;
import com.gtocore.data.transaction.manager.TradeData;
import com.gtocore.data.transaction.manager.TradeEntry;
import com.gtocore.data.transaction.manager.TradingManager;

import com.gtolib.api.GTOValues;
import com.gtolib.utils.WalletUtils;

import com.gregtechceu.gtceu.api.GTValues;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import static com.gtocore.data.transaction.data.TradeLang.*;
import static com.gtocore.data.transaction.data.trade.UnlockTrade.GT_Values;
import static com.gtocore.data.transaction.data.trade.UnlockTrade.UNLOCK_BASE;

public class EnergyGroup {

    /**
     * 能源部
     * <p>
     * - 能源购入
     * - 能源出售
     */
    public static void init() {
        int GroupIndex = TradingManager.INSTANCE.addShopGroup(
                addTradeLang("能源部", "Energy Agency"),
                GTOGuiTextures.ENERGY,
                GTOGuiTextures.ENERGY);

        int ShopIndex1 = TradingManager.INSTANCE.addShopByGroupIndex(
                GroupIndex,
                addTradeLang("能源出售", "Power selling"),
                UNLOCK_BASE,
                Set.of(ENERGY_COIN),
                GTOGuiTextures.ENERGY);

        int ShopIndex2 = TradingManager.INSTANCE.addShopByGroupIndex(
                GroupIndex,
                addTradeLang("能源购入", "power procurement"),
                UNLOCK_BASE,
                Set.of(ENERGY_COIN),
                GTOGuiTextures.ENERGY);

        for (int i = 1; i < 9; i++) {
            addEnergyTrade(GroupIndex, ShopIndex1, ShopIndex2, i, proportion, ENERGY_COIN);
        }
        for (int i = 9; i < 14; i++) {
            addEnergyTrade(GroupIndex, ShopIndex1, ShopIndex2, i, proportion * 131072, COMPACT_ENERGY_COIN);
        }
    }

    private static final long proportion = 1200 * 10 * 8;

    private static void addEnergyTrade(int groupIndex, int shopIndex1, int shopIndex2, int values, long proportion, String currency) {
        BigInteger energy = BigInteger.valueOf(GTValues.VEX[values]).multiply(BigInteger.valueOf(1200L * 10));
        BigInteger energy2 = energy.multiply(BigInteger.valueOf(6));
        long amount = energy.divide(BigInteger.valueOf(proportion)).longValue();
        long amount2 = amount * 6;

        TradingManager.INSTANCE.addTradeEntryByIndices(groupIndex, shopIndex1,
                new TradeEntry.Builder()
                        .texture(new ResourceTexture("gtocore:textures/item/circuit/" + GTValues.VN[values].toLowerCase() + ".png"))
                        .description(List.of(
                                Component.translatable(addTradeLang("出售能源", "energy sales")),
                                Component.translatable(addTradeLang("将出售 %s 1A %s 分钟的能量", "Will sell %s 1A %s minutes of energy"), GTValues.VN[values], 10)))
                        .unlockCondition(GTOValues.VNFR[values])
                        .inputEnergy(energy)
                        .outputCurrency(currency, amount)
                        .preCheck((a, b) -> checkMultiplier(a, b, 10 * values))
                        .onExecute(EnergyGroup::performAddMultiplier)
                        .build());

        TradingManager.INSTANCE.addTradeEntryByIndices(groupIndex, shopIndex1,
                new TradeEntry.Builder()
                        .texture(new ResourceTexture("gtocore:textures/item/circuit/magneto_" + GTValues.VN[values].toLowerCase() + ".png"))
                        .description(List.of(
                                Component.translatable(addTradeLang("出售能源", "energy sales")),
                                Component.translatable(addTradeLang("将出售 %s 1A %s 分钟的能量", "Will sell %s 1A %s minutes of energy"), GTValues.VN[values], 10 * 6)))
                        .unlockCondition(GTOValues.VNFR[values])
                        .inputEnergy(energy2)
                        .outputCurrency(currency, amount)
                        .build());

        TradingManager.INSTANCE.addTradeEntryByIndices(groupIndex, shopIndex2,
                new TradeEntry.Builder()
                        .texture(new ResourceTexture("gtocore:textures/item/circuit/" + GTValues.VN[values].toLowerCase() + ".png"))
                        .description(List.of(
                                Component.translatable(addTradeLang("购入能源", "procure energy")),
                                Component.translatable(addTradeLang("将购入 %s 1A %s 分钟的能量", "Will purchase %s 1A %s minutes of energy"), GTValues.VN[values], 10)))
                        .unlockCondition(GTOValues.VNFR[values])
                        .inputCurrency(currency, amount)
                        .outputEnergy(energy)
                        .build());

        TradingManager.INSTANCE.addTradeEntryByIndices(groupIndex, shopIndex2,
                new TradeEntry.Builder()
                        .texture(new ResourceTexture("gtocore:textures/item/circuit/magneto_" + GTValues.VN[values].toLowerCase() + ".png"))
                        .description(List.of(
                                Component.translatable(addTradeLang("购入能源", "procure energy")),
                                Component.translatable(addTradeLang("将购入 %s 1A %s 分钟的能量", "Will purchase %s 1A %s minutes of energy"), GTValues.VN[values], 10 * 6)))
                        .unlockCondition(GTOValues.VNFR[values])
                        .inputCurrency(currency, amount2)
                        .outputEnergy(energy2)
                        .build());
    }

    // 前置检查逻辑：检查交易历史次数
    private static int checkMultiplier(TradeData machine, TradeEntry entry, int maxMultiplier) {
        Level level = machine.getLevel();
        ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
        long amount = WalletUtils.getTransactionTotalAmount(machine.getUuid(), serverLevel, GT_Values);
        if (maxMultiplier > amount) return Math.toIntExact(maxMultiplier - amount);
        return 0;
    }

    // 执行逻辑：添加交易历史标记
    private static void performAddMultiplier(TradeData machine, TradeEntry entry, int multiplier) {
        Level level = machine.getLevel();
        ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
        WalletUtils.addScheduledDeletion(machine.getUuid(), serverLevel, GT_Values, 360L, multiplier);
    }
}

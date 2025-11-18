package com.gtocore.data.transaction.data.trade;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.data.transaction.manager.TradeEntry;
import com.gtocore.data.transaction.manager.TradingManager;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.gui.GuiTextures;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;

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
                UNLOCK_BASE,
                GuiTextures.GREGTECH_LOGO,
                GuiTextures.GREGTECH_LOGO);

        int ShopIndex1 = manager.addShopByGroupIndex(
                GroupIndex,
                addTradeLang("欢迎来到格雷科技销售部币兑区", "Welcome to the Currency Exchange Area of Gray Technology Sales Department"),
                UNLOCK_BASE,
                Set.of(TECH_OPERATOR_COIN),
                GuiTextures.GREGTECH_LOGO);

        int ShopIndex2 = manager.addShopByGroupIndex(
                GroupIndex,
                addTradeLang("欢迎来到格雷科技销售部会员区", "Welcome to the Membership Area of Gray Technology Sales Department"),
                UNLOCK_BASE,
                Set.of(TECH_OPERATOR_COIN),
                GuiTextures.GREGTECH_LOGO);

        Material[] materials = { Copper, Cupronickel, Silver, Gold, Osmium, Naquadah, Neutronium, Adamantine, Infinity };

        for (int i = 0; i < materials.length; i++) {
            manager.addTradeEntryByIndices(GroupIndex, ShopIndex1, createCoinExchangeTrade(materials[i], i));
            manager.addTradeEntryByIndices(GroupIndex, ShopIndex1, createCoinWithdrawTrade(materials[i], i));
        }
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
}

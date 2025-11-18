package com.gtocore.data.transaction.data.trade;

import com.gtocore.data.transaction.manager.TradingManager;

import com.gregtechceu.gtceu.api.gui.GuiTextures;

import static com.gtocore.data.transaction.data.TradeLang.UNLOCK_BASE;
import static com.gtocore.data.transaction.data.TradeLang.addTradeLang;

public class WelfareGroup {

    /**
     * 员工福利兑换中心
     * <p>
     * - 币兑区
     * - 会员区
     */
    public static void init() {
        TradingManager manager = TradingManager.getInstance();

        int GroupIndex = manager.addShopGroup(
                addTradeLang("员工福利兑换中心", "Employee Benefits Redemption Center"),
                UNLOCK_BASE,
                GuiTextures.GREGTECH_LOGO,
                GuiTextures.GREGTECH_LOGO);

        int ShopIndex1 = manager.addShopByGroupIndex(
                GroupIndex,
                addTradeLang("福利兑换 壹", "Welfare Redemption 1"),
                UNLOCK_BASE,
                GuiTextures.GREGTECH_LOGO);
    }
}

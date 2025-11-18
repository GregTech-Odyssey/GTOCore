package com.gtocore.data.transaction.data.trade;

import com.gtocore.data.transaction.manager.TradingManager;

import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;

import static com.gtocore.data.transaction.data.TradeLang.UNLOCK_BASE;
import static com.gtocore.data.transaction.data.TradeLang.addTradeLang;
import static com.gtocore.utils.PlayerHeadUtils.get_GregoriusT_Head;
import static com.gtocore.utils.PlayerHeadUtils.get_maple197_Head;

public class PlayersGroup {

    /**
     * 员工交易中心
     * <p>
     * - 币兑区
     * - 会员区
     */
    public static void init() {
        TradingManager manager = TradingManager.getInstance();

        int GroupIndex = manager.addShopGroup(
                addTradeLang("员工交易中心", "Employee Trading Center"),
                UNLOCK_BASE,
                new ItemStackTexture(get_GregoriusT_Head()),
                new ItemStackTexture(get_maple197_Head()));
    }
}

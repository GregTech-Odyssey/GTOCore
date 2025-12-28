package com.gtocore.data.transaction.data.trade;

import com.gtocore.data.transaction.manager.TradingManager;

import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;

import static com.gtocore.data.transaction.data.TradeLang.addTradeLang;
import static com.gtocore.utils.PlayerHeadUtils.get_GregoriusT_Head;
import static com.gtocore.utils.PlayerHeadUtils.get_maple197_Head;

public final class PlayersGroup {

    /**
     * 员工交易中心
     * <p>
     * - 凭证兑换
     */
    public static void init() {
        int GroupIndex = TradingManager.INSTANCE.addShopGroup(
                addTradeLang("员工交易中心", "Employee Trading Center"),
                new ItemStackTexture(get_GregoriusT_Head()),
                new ItemStackTexture(get_maple197_Head()));
    }
}

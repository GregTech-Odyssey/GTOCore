package com.gtocore.data.transaction.data.trade;

import com.gtocore.data.transaction.manager.UnlockManager;

public class UnlockTrade {

    public static final String UNLOCK_SHOP = "unlock_shop";
    public static final String UNLOCK_TRADE = "unlock_trade";
    public static final String UNLOCK_BASE = "base";

    public static void init() {
        UnlockManager UnlockManager = com.gtocore.data.transaction.manager.UnlockManager.getInstance();
    }
}

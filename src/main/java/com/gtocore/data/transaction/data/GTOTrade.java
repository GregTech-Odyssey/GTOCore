package com.gtocore.data.transaction.data;

import com.gtocore.data.transaction.data.trade.PlayersGroup;
import com.gtocore.data.transaction.data.trade.UnlockTrade;
import com.gtocore.data.transaction.data.trade.WelcomeGroup;
import com.gtocore.data.transaction.data.trade.WelfareGroup;

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
        /** -1 - 解锁交易组 */
        UnlockTrade.init();

        /** 0 - 欢迎来到格雷科技 */
        WelcomeGroup.init();

        /** 1 - 员工交易中心 */
        PlayersGroup.init();

        /** 2 - 员工福利兑换中心 */
        WelfareGroup.init();
    }
}

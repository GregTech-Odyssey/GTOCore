package com.gtocore.data.transaction.data;

import com.gtolib.api.lang.CNEN;

import com.gregtechceu.gtceu.GTCEu;

import com.fast.fastcollection.O2OOpenCacheHashMap;

import java.util.Map;

public final class TradeLang {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    private static void addTradeLang(String key, String cn, String en) {
        if (LANG != null) LANG.put(key, new CNEN(cn, en));
    }

    public static String addTradeLang(String cn, String en) {
        int hash = cn.hashCode();
        String key = "gtocore.trade." + hash;
        if (TradeLang.LANG != null) TradeLang.LANG.put(key, new CNEN(cn, en));
        return key;
    }

    /// - 技术员币：Tech Operator Coin
    public static final String TECH_OPERATOR_COIN = "tech_operator_coin";
    /// - 研究员币：Research Pioneer Coin
    public static final String RESEARCH_PIONEER_COIN = "research_pioneer_coin";
    /// - 工程师币：Tech Transformer Coin
    public static final String TECH_TRANSFORMER_COIN = "tech_transformer_coin";
    /// - 引航员币：Voyager Navigator Coin
    public static final String VOYAGER_NAVIGATOR_COIN = "voyager_navigator_coin";
    /// - 科技执政官币：Tech Administrator Coin
    public static final String TECH_ADMINISTRATOR_COIN = "tech_administrator_coin";
    /// - 首席科学家币：Chief Science Steward Coin
    public static final String CHIEF_SCIENCE_STEWARD_COIN = "chief_science_steward_coin";

    /// - 能量币：Energy Coin
    /// - 致密能量币：Compact Energy Coin
    /// 比例 524288：1
    public static final String ENERGY_COIN = "energy_coin";
    public static final String COMPACT_ENERGY_COIN = "compact_energy_coin";

    static {
        addTradeLang("gtocore.currency." + TECH_OPERATOR_COIN, "技术员币", "Tech Operator Coin");
        addTradeLang("gtocore.currency." + RESEARCH_PIONEER_COIN, "研究员币", "Research Pioneer Coin");
        addTradeLang("gtocore.currency." + TECH_TRANSFORMER_COIN, "工程师币", "Tech Transformer Coin");
        addTradeLang("gtocore.currency." + VOYAGER_NAVIGATOR_COIN, "引航员币", "Voyager Navigator Coin");
        addTradeLang("gtocore.currency." + TECH_ADMINISTRATOR_COIN, "科技执政官币", "Tech Administrator Coin");
        addTradeLang("gtocore.currency." + CHIEF_SCIENCE_STEWARD_COIN, "首席科学家币", "Chief Science Steward Coin");

        addTradeLang("gtocore.currency." + ENERGY_COIN, "能量币", "Energy Coin");
        addTradeLang("gtocore.currency." + COMPACT_ENERGY_COIN, "致密能量币", "Compact Energy Coin");

        addTradeLang("gtocore.palm_sized_bank.textList.1", "欢迎使用掌上银行！", "Welcome to Mobile Banking!");
        addTradeLang("gtocore.palm_sized_bank.textList.2", "在这里, 您可以方便地管理您的虚拟资产", "Here, you can conveniently manage your virtual assets");
        addTradeLang("gtocore.palm_sized_bank.textList.3", "请注意保护您的账户信息, 避免泄露给他人", "Please be careful to protect your account information and avoid disclosing it to others");
        addTradeLang("gtocore.palm_sized_bank.textList.4", "祝您使用愉快！", "Wish you a pleasant experience!");
        addTradeLang("gtocore.palm_sized_bank.textList.5", "无法获取玩家信息", "Failed to obtain player information");
        addTradeLang("gtocore.palm_sized_bank.textList.6", "当前用户: %s", "Current user: %s");
        addTradeLang("gtocore.palm_sized_bank.textList.7", "用户 UUID: %s", "User UUID: %s");
        addTradeLang("gtocore.palm_sized_bank.textList.8", "创建钱包", "Create a wallet");
        addTradeLang("gtocore.palm_sized_bank.textList.9", "钱包不存在", "Wallet does not exist");

        addTradeLang("gtocore.palm_sized_bank.textList.10", "资产总览", "Asset Overview");
        addTradeLang("gtocore.palm_sized_bank.textList.11", "货币种类", "Currency Type");
        addTradeLang("gtocore.palm_sized_bank.textList.12", "持有数量", "Amount");

        addTradeLang("gtocore.palm_sized_bank.textList.20", "交易记录", "Transaction records");
        addTradeLang("gtocore.palm_sized_bank.textList.21", "交易主键", "Transaction Key");
        addTradeLang("gtocore.palm_sized_bank.textList.22", "交易总量", "Total transaction volume");
        addTradeLang("gtocore.palm_sized_bank.textList.23", "策略类型标识", "Strategy type identifier");
        addTradeLang("gtocore.palm_sized_bank.textList.24", "本分钟交易量", "Trading volume this minute");
        addTradeLang("gtocore.palm_sized_bank.textList.25", "上一分钟交易量", "Previous minute's trading volume");
        addTradeLang("gtocore.palm_sized_bank.textList.26", "交易明细", "Transaction details");

        addTradeLang("gtocore.palm_sized_bank.textList.30", "标签组", "Tag Group");
        addTradeLang("gtocore.palm_sized_bank.textList.31", "标签列表", "Tag list");

        addTradeLang("gtocore.palm_sized_bank.textList.40", "需要%s%s", "need%s%s");
        addTradeLang("gtocore.palm_sized_bank.textList.41", "[获取会员卡]", "[Get membership card]");
        addTradeLang("gtocore.palm_sized_bank.textList.42", "添加到共享名单", "Add to shared list");

        addTradeLang("gtocore.palm_sized_bank.textList.50", "转账给此账户", "Transfer to this account");
        addTradeLang("gtocore.palm_sized_bank.textList.51", "转账此货币", "Transfer this currency");
        addTradeLang("gtocore.palm_sized_bank.textList.52", "转账金额", "Transfer amount");
        addTradeLang("gtocore.palm_sized_bank.textList.53", "[转账]", "[Transfer]");
        addTradeLang("gtocore.palm_sized_bank.textList.54", "[确认转账]", "[Transfer Confirmed]");

        addTradeLang("gtocore.gray_membership_card.hover_text.1", "未知", "unknown");
        addTradeLang("gtocore.gray_membership_card.hover_text.2", "离线玩家", "Offline players");
        addTradeLang("gtocore.gray_membership_card.hover_text.3", "主人: ", "Owner: ");
        addTradeLang("gtocore.gray_membership_card.hover_text.4", "共享者: ", "Shared by: ");

        addTradeLang("gtocore.trade_group.true", "价格", "Price");
        addTradeLang("gtocore.trade_group.false", "商品", "Commodity");
        addTradeLang("gtocore.trade_group.unlock", "未解锁, 需要解锁 %s", "Not unlocked, needs to be unlocked %s");
        addTradeLang("gtocore.trade_group.unsatisfied", "不满足额外条件", "Additional conditions not met");
        addTradeLang("gtocore.trade_group.amount", "可交易次数: %s", "Number of tradables: %s");
        addTradeLang("gtocore.trade_group.repeatedly1", "按下Ctrl尝试交易10次", "Press Ctrl to attempt 10 trades");
        addTradeLang("gtocore.trade_group.repeatedly2", "同时按下Ctrl Shift尝试交易100次", "Simultaneously press Ctrl Shift to attempt 100 trades");
        addTradeLang("gtocore.trade_group.exchanged", "将%2$s兑换为%1$s", "Exchange %1$s for %2$s");

        addTradeLang("gtocore.trading_station.unlock_shop", "解锁商店", "Unlock Store");
        addTradeLang("gtocore.trading_station.item_storage", "物品存储", "Item Storage");
        addTradeLang("gtocore.trading_station.fluid_storage", "流体存储", "Fluid Storage");

        addTradeLang("gtocore.trading_station.textList.2", "⇦ 请放入会员卡", "⇦ Please insert your membership card.");
        addTradeLang("gtocore.trading_station.textList.3", "欢迎「 %s 」", "Welcome「 %s 」");
        addTradeLang("gtocore.trading_station.textList.4", "共享给: ", "Share with: ");
        addTradeLang("gtocore.trading_station.textList.8", "[刷新]", "[Refresh]");

        addTradeLang("gtocore.trading_station.textList.11", "请从掌上银行获取会员卡以使用交易站", "Please obtain a membership card from your mobile banking app to use the trading platform.");

        addTradeLang("gtocore.trading_station.textList.20", "商店未解锁, 需要解锁 %s", "Store locked, needs to be unlocked %s");
        addTradeLang("gtocore.trading_station.textList.21", "交易解锁", "Transaction unlock");
        addTradeLang("gtocore.trading_station.textList.22", "解锁 %s", "Unlock %s");

        addTradeLang("gtocore.trade_lottery.weight", "- %s x%s [权重: %s]", "- %s x%s [Weight: %s]");
    }
}

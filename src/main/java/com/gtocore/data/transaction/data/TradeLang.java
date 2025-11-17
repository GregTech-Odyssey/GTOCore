package com.gtocore.data.transaction.data;

import com.gtolib.api.lang.CNEN;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import java.util.Map;

public class TradeLang {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    private static void add(String key, String cn, String en) {
        if (LANG != null) LANG.put(key, new CNEN(cn, en));
    }

    private static void add(String cn, String en) {
        String key = en.replace(' ', '_').toLowerCase();
        add(key, cn, en);
    }

    public static String addTradeLang(String cn, String en) {
        int hash = cn.hashCode();
        String key = "gtocore.trade." + hash;
        if (TradeLang.LANG != null) TradeLang.LANG.put(key, new CNEN(cn, en));
        return key;
    }

    /// - 技术员币：Tech Operator Coin
    /// - 研究员币：Research Pioneer Coin
    /// - 工程师币：Tech Transformer Coin
    /// - 引航员币：Voyager Navigator Coin
    /// - 科技执政官币：Tech Administrator Coin
    /// - 首席科学家币：Chief Science Steward Coin
    public static final String TECH_OPERATOR_COIN = "tech_operator_coin";
    public static final String RESEARCH_PIONEER_COIN = "research_pioneer_coin";
    public static final String TECH_TRANSFORMER_COIN = "tech_transformer_coin";
    public static final String VOYAGER_NAVIGATOR_COIN = "voyager_navigator_coin";
    public static final String TECH_ADMINISTRATOR_COIN = "tech_administrator_coin";
    public static final String CHIEF_SCIENCE_STEWARD_COIN = "chief_science_steward_coin";

    public static final String UNLOCK_SHOP_GROUP = "unlock_shop_group";
    public static final String UNLOCK_SHOP = "unlock_shop";
    public static final String UNLOCK_TRADE = "unlock_trade";
    public static final String UNLOCK_BASE = "base";

    static {
        add("gtocore.currency." + TECH_OPERATOR_COIN, "技术员币", "Tech Operator Coin");
        add("gtocore.currency." + RESEARCH_PIONEER_COIN, "研究员币", "Research Pioneer Coin");
        add("gtocore.currency." + TECH_TRANSFORMER_COIN, "工程师币", "Tech Transformer Coin");
        add("gtocore.currency." + VOYAGER_NAVIGATOR_COIN, "引航员币", "Voyager Navigator Coin");
        add("gtocore.currency." + TECH_ADMINISTRATOR_COIN, "科技执政官币", "Tech Administrator Coin");
        add("gtocore.currency." + CHIEF_SCIENCE_STEWARD_COIN, "首席科学家币", "Chief Science Steward Coin");

        add("gtocore.palm_sized_bank.textList.1", "欢迎使用掌上银行！", "Welcome to Mobile Banking!");
        add("gtocore.palm_sized_bank.textList.2", "在这里，您可以方便地管理您的虚拟资产", "Here, you can conveniently manage your virtual assets");
        add("gtocore.palm_sized_bank.textList.3", "请注意保护您的账户信息，避免泄露给他人", "Please be careful to protect your account information and avoid disclosing it to others");
        add("gtocore.palm_sized_bank.textList.4", "祝您使用愉快！", "Wish you a pleasant experience!");
        add("gtocore.palm_sized_bank.textList.5", "无法获取玩家信息", "Failed to obtain player information");
        add("gtocore.palm_sized_bank.textList.6", "当前用户: %s", "Current user: %s");
        add("gtocore.palm_sized_bank.textList.7", "用户 UUID: %s", "User UUID: %s");
        add("gtocore.palm_sized_bank.textList.8", "创建钱包", "Create a wallet");
        add("gtocore.palm_sized_bank.textList.9", "钱包不存在", "Wallet does not exist");

        add("gtocore.palm_sized_bank.textList.10", "资产总览", "Asset Overview");
        add("gtocore.palm_sized_bank.textList.11", "货币种类", "Currency Type");
        add("gtocore.palm_sized_bank.textList.12", "持有数量", "Amount");

        add("gtocore.palm_sized_bank.textList.20", "交易记录", "Transaction records");
        add("gtocore.palm_sized_bank.textList.21", "交易主键", "Transaction Key");
        add("gtocore.palm_sized_bank.textList.22", "交易总量", "Total transaction volume");
        add("gtocore.palm_sized_bank.textList.23", "策略类型标识", "Strategy type identifier");
        add("gtocore.palm_sized_bank.textList.24", "本分钟交易量", "Trading volume this minute");
        add("gtocore.palm_sized_bank.textList.25", "上一分钟交易量", "Previous minute's trading volume");
        add("gtocore.palm_sized_bank.textList.26", "交易明细", "Transaction details");

        add("gtocore.palm_sized_bank.textList.30", "标签组", "Tag Group");
        add("gtocore.palm_sized_bank.textList.31", "标签列表", "Tag list");

        add("gtocore.palm_sized_bank.textList.40", "需要%s%s", "need%s%s");
        add("gtocore.palm_sized_bank.textList.41", "[获取会员卡]", "[Get membership card]");
        add("gtocore.palm_sized_bank.textList.42", "添加到共享名单", "Add to shared list");

        add("gtocore.palm_sized_bank.textList.50", "转账给此账户", "Transfer to this account");
        add("gtocore.palm_sized_bank.textList.51", "转账此货币", "Transfer this currency");
        add("gtocore.palm_sized_bank.textList.52", "转账金额", "Transfer amount");
        add("gtocore.palm_sized_bank.textList.53", "[转账]", "[Transfer]");
        add("gtocore.palm_sized_bank.textList.54", "[确认转账]", "[Transfer Confirmed]");

        add("gtocore.gray_membership_card.hover_text.1", "未知", "unknown");
        add("gtocore.gray_membership_card.hover_text.2", "离线玩家", "Offline players");
        add("gtocore.gray_membership_card.hover_text.3", "主人: ", "Owner: ");
        add("gtocore.gray_membership_card.hover_text.4", "共享者: ", "Shared by: ");

        add("gtocore.trade_group.true", "价格", "Price");
        add("gtocore.trade_group.false", "商品", "Commodity");
        add("gtocore.trade_group.unlock", "未解锁, 需要解锁 %s", "Not unlocked, needs to be unlocked %s");
        add("gtocore.trade_group.unsatisfied", "不满足额外条件", "Additional conditions not met");

        add("gtocore.trading_station.textList.1", "这台机器不属于你，你无法操作这台机器", "This machine does not belong to you, you cannot operate it.");
        add("gtocore.trading_station.textList.2", "⇦ 请放入会员卡", "⇦ Please insert your membership card.");
        add("gtocore.trading_station.textList.3", "欢迎「 %s 」", "Welcome「 %s 」");
        add("gtocore.trading_station.textList.4", "共享给: ", "Share with: ");
        add("gtocore.trading_station.textList.5", "[展开]", "[Expand]");
        add("gtocore.trading_station.textList.6", "[收起]", "[Collapse]");
        add("gtocore.trading_station.textList.7", "--详细介绍--", "--Detailed Introduction--");
        add("gtocore.trading_station.textList.8", "[刷新]", "[Refresh]");
        add("gtocore.trading_station.textList.9", "物品存储", "Item Storage");
        add("gtocore.trading_station.textList.10", "流体存储", "Fluid Storage");

        add("gtocore.trading_station.textList.21", "交易解锁", "Transaction unlock");
        add("gtocore.trading_station.textList.22", "解锁 %s", "Unlock %s");
    }
}

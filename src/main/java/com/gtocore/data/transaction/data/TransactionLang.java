package com.gtocore.data.transaction.data;

import com.gtolib.api.lang.CNEN;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import java.util.Map;

public class TransactionLang {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    private static void add(String key, String cn, String en) {
        if (LANG != null) LANG.put(key, new CNEN(cn, en));
    }

    private static void add(String cn, String en) {
        String key = en.replace(' ', '_').toLowerCase();
        add(key, cn, en);
    }

    public static final String UNLOCK_SHOP_GROUP = "unlock_shop_group";
    public static final String UNLOCK_SHOP = "unlock_shop";
    public static final String UNLOCK_TRANSACTION = "unlock_transaction";

    static {
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

        add("gtocore.trading_station.textList.21", "机器等级: %s", "Machine Level: %s");
        add("gtocore.trading_station.textList.22", "升级", "Upgrade");
        add("gtocore.trading_station.textList.23", "当前等级：%s/%s", "Current Level: %s/%s");

        add("gtocore.currency.coins", "金币", "coins");
        add("gtocore.currency.gems", "宝石", "gems");
        add("gtocore.currency.tokens", "代币", "tokens");
        add("gtocore.currency.technician_coin", "技术员币", "Technician Coin");

        add("gtocore.transaction_group.true", "价格", "Price");
        add("gtocore.transaction_group.false", "商品", "Commodity");
        add("gtocore.transaction_group.unlock", "未解锁, 需要解锁 %s", "Not unlocked, needs to be unlocked %s");
        add("gtocore.transaction_group.unsatisfied", "不满足额外条件", "Additional conditions not met");

        // ==================== 机器等级与升级面板核心文本 ====================
        // 面板标题
        add("gtocore.trading_station.upgrade.panel_title", "交易站升级", "Trading Station Upgrades");
        // 当前等级显示（带进度条提示）
        add("gtocore.trading_station.upgrade.current_level", "当前等级：%s/%s", "Current Level: %s/%s");
        // 等级进度描述（如“已满足等级3条件”）
        add("gtocore.trading_station.upgrade.level_progress", "已满足等级%s升级条件", "Conditions met for Level %s");
        // 未满足条件提示
        add("gtocore.trading_station.upgrade.level_not_ready", "未满足等级%s升级条件", "Conditions not met for Level %s");

        // ==================== 升级项目名称（补充更具体的命名） ====================
        add("gtocore.trading_station.upgrade.currentLevel", "机器升级", "Machine Upgrade");
        add("gtocore.trading_station.upgrade.fluid_tank", "储罐升级", "Fluid Tank Upgrade");
        add("gtocore.trading_station.upgrade.auto_trade", "自动交易升级", "Auto-Trade Upgrade");
        add("gtocore.trading_station.upgrade.lucky_merchant", "幸运商人升级", "Lucky Merchant Upgrade");
        add("gtocore.trading_station.upgrade.me_interaction", "ME交互升级", "ME Interaction Upgrade");
        add("gtocore.trading_station.upgrade.player_trade", "玩家交易升级", "Player Trade Upgrade");
        add("gtocore.trading_station.upgrade.capacity", "机器容量升级", "Machine Capacity Upgrade");

        // ==================== 升级项目详细描述（分等级说明效果） ====================
        // 储罐升级（仅1级，启用流体存储）
        add("gtocore.trading_station.upgrade.fluid_tank.desc",
                "解锁机器的流体存储能力\n- 初始提供4个流体槽\n- 后续可通过容量升级扩展",
                "Unlocks fluid storage capability\n- Initial 4 fluid tanks\n- Expandable via Capacity Upgrade");

        // 自动交易升级（分等级说明频率）
        add("gtocore.trading_station.upgrade.auto_trade.desc.lv1",
                "自动交易Lv.1：每40刻（2秒）执行1次\n- 需等级3解锁",
                "Auto-Trade Lv.1: Executes every 40 ticks (2s)\n- Unlocked at Level 3");
        add("gtocore.trading_station.upgrade.auto_trade.desc.lv2",
                "自动交易Lv.2：每30刻（1.5秒）执行1次\n- 需等级3，容量升级≥5",
                "Auto-Trade Lv.2: Executes every 30 ticks (1.5s)\n- Requires Level 3, Capacity Lv.≥5");
        add("gtocore.trading_station.upgrade.auto_trade.desc.lv8",
                "自动交易Lv.8：每5刻（0.25秒）执行1次\n- 需等级5，容量升级≥16",
                "Auto-Trade Lv.8: Executes every 5 ticks (0.25s)\n- Requires Level 5, Capacity Lv.≥16");

        // 幸运商人升级（仅1级，说明概率）
        add("gtocore.trading_station.upgrade.lucky_merchant.desc",
                "交易时有5%概率掉落幸运商店开启卡\n- 幸运卡可解锁特殊限定交易\n- 需等级4解锁",
                "5% chance to drop Lucky Shop Card on trade\n- Lucky Card unlocks special limited trades\n- Unlocked at Level 4");

        // ME交互升级（仅1级，说明功能）
        add("gtocore.trading_station.upgrade.me_interaction.desc",
                "允许机器直接与ME网络交互\n- 自动导入ME网络中的交易材料\n- 自动导出交易产物到ME网络\n- 需等级5解锁",
                "Allows direct interaction with ME Network\n- Auto-imports trade materials from ME\n- Auto-exports trade products to ME\n- Unlocked at Level 5");

        // 玩家交易升级（分等级说明权限）
        add("gtocore.trading_station.upgrade.player_trade.desc.lv1",
                "玩家交易Lv.1：允许与同服务器玩家交易\n- 需等级1解锁",
                "Player Trade Lv.1: Trade with players on the same server\n- Unlocked at Level 1");
        add("gtocore.trading_station.upgrade.player_trade.desc.lv4",
                "玩家交易Lv.4：允许跨团队玩家交易\n- 需等级4，容量升级≥10",
                "Player Trade Lv.4: Trade with players across teams\n- Requires Level 4, Capacity Lv.≥10");

        // 机器容量升级（分等级说明增量）
        add("gtocore.trading_station.upgrade.capacity.desc.lv1",
                "容量升级Lv.1：物品槽+10，流体槽+1（若已启用）\n- 需等级1解锁",
                "Capacity Lv.1: +10 item slots, +1 fluid slot (if enabled)\n- Unlocked at Level 1");
        add("gtocore.trading_station.upgrade.capacity.desc.lv16",
                "容量升级Lv.16：物品槽+30，流体槽+4（若已启用）\n- 需等级5，ME交互升级已安装",
                "Capacity Lv.16: +30 item slots, +4 fluid slots (if enabled)\n- Requires Level 5, ME Interaction installed");

        // ==================== UI交互按钮文本 ====================
        // 升级安装按钮（通用）
        add("gtocore.trading_station.upgrade.button.install", "安装升级", "Install Upgrade");
        // 升级卸载按钮（通用）
        add("gtocore.trading_station.upgrade.button.uninstall", "卸载升级", "Uninstall Upgrade");
        // 查看升级详情按钮
        add("gtocore.trading_station.upgrade.button.view_details", "查看详情", "View Details");
        // 确认升级按钮
        add("gtocore.trading_station.upgrade.button.confirm_upgrade", "确认升级", "Confirm Upgrade");
        // 取消按钮
        add("gtocore.trading_station.upgrade.button.cancel", "取消", "Cancel");

        // ==================== 错误与状态提示文本 ====================
        // 升级等级已达上限
        add("gtocore.trading_station.upgrade.error.max_level", "该升级已达最高等级！", "This upgrade is at maximum level!");
        // 缺少升级物品
        add("gtocore.trading_station.upgrade.error.missing_item", "缺少升级物品：%s", "Missing upgrade item: %s");
        // 未满足前置升级
        add("gtocore.trading_station.upgrade.error.missing_prerequisite", "需先安装：%s", "Requires prior installation of: %s");
        // 机器等级不足
        add("gtocore.trading_station.upgrade.error.level_too_low", "机器等级不足（当前%s级，需%s级）", "Machine level too low (Current Lv.%s, Required Lv.%s)");
        // 升级安装成功
        add("gtocore.trading_station.upgrade.success.install", "升级【%s】安装成功！", "Upgrade [%s] installed successfully!");
        // 升级卸载成功
        add("gtocore.trading_station.upgrade.success.uninstall", "升级【%s】卸载成功！", "Upgrade [%s] uninstalled successfully!");
        // 等级提升成功（带奖励提示）
        add("gtocore.trading_station.upgrade.success.level_up", "恭喜！机器已升级至%s级\n- 解锁新功能：%s", "Congratulations! Machine upgraded to Lv.%s\n- Unlocked new feature: %s");

        // ==================== 其他补充描述 ====================
        // 升级物品提示（如“将升级卡放入此处”）
        add("gtocore.trading_station.upgrade.slot_hint", "放入升级卡", "Insert Upgrade Card");
        // 升级占用槽位提示
        add("gtocore.trading_station.upgrade.slot_occupied", "升级槽已占用", "Upgrade slot occupied");
        // 自动交易状态提示（运行中）
        add("gtocore.trading_station.auto_trade.status.running", "自动交易运行中（Lv.%s）", "Auto-Trade running (Lv.%s)");
        // 自动交易状态提示（暂停）
        add("gtocore.trading_station.auto_trade.status.paused", "自动交易已暂停", "Auto-Trade paused");
        // 幸运商人触发提示
        add("gtocore.trading_station.lucky_merchant.notify", "幸运商人！获得：%s", "Lucky Merchant! Received: %s");
    }
}

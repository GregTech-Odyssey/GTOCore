package com.gtocore.data.bank;

import com.gtolib.api.lang.CNEN;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import java.util.Map;

public class BankLang {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    private static void add(String key, String cn, String en) {
        if (LANG != null) LANG.put(key, new CNEN(cn, en));
    }

    private static void add(String cn, String en) {
        String key = en.replace(' ', '_').toLowerCase();
        add(key, cn, en);
    }

    static {
        add("gtocore.bank.title", "银行", "Bank");
        add("gtocore.bank.account.info", "账户信息", "Account Info");
        add("gtocore.bank.account.number", "账户号码: %s", "Account Number: %s");
        add("gtocore.bank.account.owner", "账户持有者: %s", "Account Owner: %s");
        add("gtocore.bank.account.balance", "账户余额: %s", "Account Balance: %s");
        add("gtocore.bank.action.deposit", "存款", "Deposit");
        add("gtocore.bank.action.withdraw", "取款", "Withdraw");
        add("gtocore.bank.action.transfer", "转账", "Transfer");
        add("gtocore.bank.input.amount", "输入金额", "Input Amount");
        add("gtocore.bank.input.recipient", "输入收款人账户号码", "Input Recipient Account Number");
        add("gtocore.bank.message.insufficient_funds", "余额不足！", "Insufficient Funds!");
        add("gtocore.bank.message.invalid_account", "无效的账户号码！", "Invalid Account Number!");
        add("gtocore.bank.message.transfer_successful", "转账成功！", "Transfer Successful!");

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

        add("gtocore.bank.currency.coins", "金币", "coins");
        add("gtocore.bank.currency.gems", "宝石", "gems");
        add("gtocore.bank.currency.tokens", "代币", "tokens");
    }
}

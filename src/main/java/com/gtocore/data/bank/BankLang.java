package com.gtocore.data.bank;

import com.gtolib.api.lang.CNEN;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import java.util.Map;

public class BankLang {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    private static void add(String key, String cn, String en) {
        if (LANG != null) LANG.put("gtocore." + key, new CNEN(cn, en));
    }

    private static void add(String cn, String en) {
        String key = en.replace(' ', '_').toLowerCase();
        add(key, cn, en);
    }

    static {
        add("bank.title", "银行", "Bank");
        add("bank.account.info", "账户信息", "Account Info");
        add("bank.account.number", "账户号码: %s", "Account Number: %s");
        add("bank.account.owner", "账户持有者: %s", "Account Owner: %s");
        add("bank.account.balance", "账户余额: %s", "Account Balance: %s");
        add("bank.action.deposit", "存款", "Deposit");
        add("bank.action.withdraw", "取款", "Withdraw");
        add("bank.action.transfer", "转账", "Transfer");
        add("bank.input.amount", "输入金额", "Input Amount");
        add("bank.input.recipient", "输入收款人账户号码", "Input Recipient Account Number");
        add("bank.message.insufficient_funds", "余额不足！", "Insufficient Funds!");
        add("bank.message.invalid_account", "无效的账户号码！", "Invalid Account Number!");
        add("bank.message.transfer_successful", "转账成功！", "Transfer Successful!");
    }
}

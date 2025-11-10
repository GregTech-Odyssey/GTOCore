package com.gtocore.data.transaction;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class TransactionEntryGroup {
    // 组名
    private String groupName;
    // 解锁条件
    private String unlockCondition;
    // 交易条目的列表
    private List<TransactionEntry> transactionEntries;

    public void addTransactionEntry(TransactionEntry entry) {
        if (transactionEntries == null) {
            transactionEntries = new ArrayList<>();
        }
        transactionEntries.add(entry);
    }
}
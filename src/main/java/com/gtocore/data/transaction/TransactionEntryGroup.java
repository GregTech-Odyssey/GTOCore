package com.gtocore.data.transaction;

import net.minecraft.network.chat.Component;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 交易条目分组，用于归类管理相关交易条目
 */
@Getter
public class TransactionEntryGroup {

    // 组名（支持样式和多语言，不可修改）
    private final Component groupName;

    // 组解锁条件（简单字符串描述）
    @Setter
    private String unlockCondition;
    // 组内交易条目（初始化避免null，内部维护）
    private final List<TransactionEntry> transactionEntries = new ArrayList<>();

    /**
     * 构造器：直接初始化组名（核心参数必传）
     * 
     * @param groupName 组名（非空，支持样式）
     */
    public TransactionEntryGroup(Component groupName) {
        this.groupName = groupName; // 允许空Component（如Component.empty()），简化校验
    }

    /**
     * 向组内添加交易条目
     * 
     * @param entry 待添加的交易条目（非空则添加）
     */
    public void addTransactionEntry(TransactionEntry entry) {
        if (entry != null) { // 简单校验，避免添加null
            transactionEntries.add(entry);
        }
    }

    /**
     * 获取不可修改的交易条目列表（防止外部篡改）
     */
    public List<TransactionEntry> getTransactionEntries() {
        return Collections.unmodifiableList(transactionEntries);
    }
}

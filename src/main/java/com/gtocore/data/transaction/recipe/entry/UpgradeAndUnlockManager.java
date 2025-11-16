package com.gtocore.data.transaction.recipe.entry;

import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UpgradeAndUnlockManager {

    private static class SingletonHolder {

        private static final UpgradeAndUnlockManager INSTANCE = new UpgradeAndUnlockManager();
    }

    public static UpgradeAndUnlockManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private UpgradeAndUnlockManager() {}

    private final O2OOpenCacheHashMap<String, List<TransactionEntry>> UpgradeAndUnlockGroups = new O2OOpenCacheHashMap<>();

    /** 获取键数量 */
    public int getEntryCount() {
        return UpgradeAndUnlockGroups.size();
    }

    /** 获取指定键的交易数量 */
    public int getEntryTransactionCount(String key) {
        return UpgradeAndUnlockGroups.getOrDefault(key, new ArrayList<>()).size();
    }

    /** 向指定键添加交易条目 */
    public void addTransactionToEntry(String key, @Nullable TransactionEntry entry) {
        if (!UpgradeAndUnlockGroups.containsKey(key)) {
            UpgradeAndUnlockGroups.put(key, new ArrayList<>());
        }
        UpgradeAndUnlockGroups.get(key).add(entry);
    }

    /** 通过键获取交易列表 */
    @Nullable
    public List<TransactionEntry> getTransactionEntryList(String key) {
        return UpgradeAndUnlockGroups.getOrDefault(key, new ArrayList<>());
    }

    /** 通过键获取交易 */
    @Nullable
    public TransactionEntry getTransactionEntry(String key, int index) {
        List<TransactionEntry> TransactionList = UpgradeAndUnlockGroups.getOrDefault(key, new ArrayList<>());
        return index < TransactionList.size() ? TransactionList.get(index) : null;
    }
}

package com.gtocore.data.transaction.manager;

import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpgradeOrUnlockManager {

    private static class SingletonHolder {

        private static final UpgradeOrUnlockManager INSTANCE = new UpgradeOrUnlockManager();
    }

    public static UpgradeOrUnlockManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private UpgradeOrUnlockManager() {}

    private final O2OOpenCacheHashMap<String, List<TradeEntry>> upgradeOrUnlockGroups = new O2OOpenCacheHashMap<>();

    /** 获取组的数量 */
    public int getGroupCount() {
        return upgradeOrUnlockGroups.size();
    }

    /** 获取指定组（key）下的交易条目数量 */
    public int getEntryTradeCount(String key) {
        return upgradeOrUnlockGroups.getOrDefault(key, new ArrayList<>()).size();
    }

    /** 向指定组（key）添加一个交易条目 */
    public void addTradeToEntry(String key, @Nullable TradeEntry entry) {
        if (entry == null) return;
        upgradeOrUnlockGroups.computeIfAbsent(key, k -> new ArrayList<>()).add(entry);
    }

    /** 获取指定组（key）下的所有交易条目 */
    public List<TradeEntry> getTradeEntryList(String key) {
        List<TradeEntry> entries = upgradeOrUnlockGroups.getOrDefault(key, new ArrayList<>());
        return Collections.unmodifiableList(entries);
    }

    /** 通过组的键和索引获取一个交易条目 */
    @Nullable
    public TradeEntry getTradeEntry(String key, int index) {
        List<TradeEntry> tradeList = upgradeOrUnlockGroups.get(key);
        if (tradeList == null || index < 0 || index >= tradeList.size()) {
            return null;
        }
        return tradeList.get(index);
    }
}

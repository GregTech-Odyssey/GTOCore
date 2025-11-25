package com.gtocore.data.transaction.manager;

import com.fast.fastcollection.O2OOpenCacheHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class UnlockManager {

    public static final UnlockManager INSTANCE = new UnlockManager();

    private UnlockManager() {}

    private final O2OOpenCacheHashMap<String, List<TradeEntry>> unlockGroups = new O2OOpenCacheHashMap<>();

    /** 获取组的数量 */
    public int getGroupCount() {
        return unlockGroups.size();
    }

    /** 获取指定组（key）下的交易条目数量 */
    public int getEntryTradeCount(String key) {
        return unlockGroups.getOrDefault(key, Collections.emptyList()).size();
    }

    /** 通过组的键的集合 */
    @Nullable
    public Set<String> getKeySet() {
        return unlockGroups.keySet();
    }

    /** 向指定组（key）添加一个交易条目 */
    public void addTradeToEntry(String key, @Nullable TradeEntry entry) {
        if (entry == null) return;
        unlockGroups.computeIfAbsent(key, k -> new ArrayList<>()).add(entry);
    }

    /** 获取指定组（key）下的所有交易条目 */
    public List<TradeEntry> getTradeEntryList(String key) {
        List<TradeEntry> entries = unlockGroups.getOrDefault(key, Collections.emptyList());
        return Collections.unmodifiableList(entries);
    }

    /** 通过组的键和索引获取一个交易条目 */
    @Nullable
    public TradeEntry getTradeEntry(String key, int index) {
        List<TradeEntry> tradeList = unlockGroups.get(key);
        if (tradeList == null || index < 0 || index >= tradeList.size()) {
            return null;
        }
        return tradeList.get(index);
    }
}

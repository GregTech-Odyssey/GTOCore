package com.gtocore.data.transaction.recipe.entry;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 交易管理器（线程安全单例），仅保留核心功能：添加、数量查询、索引获取。
 * 索引规则：所有索引均从 0 开始。
 */
public class TradingManager {

    // 线程安全单例（静态内部类模式，核心必备）
    private static class SingletonHolder {

        private static final TradingManager INSTANCE = new TradingManager();
    }

    /** 获取全局唯一实例 */
    public static TradingManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    // 线程安全容器（核心必备，保证并发安全）
    private final List<TradingShopGroup> shopGroups = Collections.synchronizedList(new ArrayList<>());

    // 私有构造函数（禁止外部实例化）
    private TradingManager() {}

    // ------------------------------ 核心工具方法（仅保留必要校验）------------------------------
    /** 校验索引是否有效（0 <= index < size） */
    private boolean isIndexValid(int index, int size) {
        return index >= 0 && index < size;
    }

    /** 校验对象非空，为空则抛出索引越界异常 */
    private void checkNotNull(@Nullable Object obj, String targetName, int index, int maxValidIndex) {
        if (obj == null) {
            throw new IndexOutOfBoundsException(
                    String.format("%s index out of bounds: %d. Max valid index: %d",
                            targetName, index, maxValidIndex));
        }
    }

    // ------------------------------ 数量查询（仅保留3个核心层级）------------------------------
    /** 获取商店组总数量 */
    public int getGroupCount() {
        return shopGroups.size();
    }

    /** 获取指定组内的商店数量 */
    public int getShopCount(int groupIndex) {
        TradingShopGroup group = getShopGroup(groupIndex);
        checkNotNull(group, "Shop Group", groupIndex, getGroupCount() - 1);
        return group.getShopCount();
    }

    /** 获取指定商店内的交易条目数量 */
    public int getTransactionCount(int groupIndex, int shopIndex) {
        TradingShop shop = getShopByIndices(groupIndex, shopIndex);
        return shop.getTransactionCount();
    }

    /**
     * 校验交易条目索引是否有效（组索引+商店索引+条目索引均合法）
     * 
     * @param groupIndex 商店组索引（0开始）
     * @param shopIndex  商店在组内的索引（0开始）
     * @param entryIndex 交易条目在商店内的索引（0开始）
     * @return 索引全部有效返回true，否则返回false
     */
    public boolean isTransactionIndexValid(int groupIndex, int shopIndex, int entryIndex) {
        // 1. 校验组索引
        if (groupIndex < 0 || groupIndex >= getGroupCount()) {
            return false;
        }
        // 2. 校验商店索引
        TradingShopGroup group = getShopGroup(groupIndex);
        if (group == null || shopIndex < 0 || shopIndex >= group.getShopCount()) {
            return false;
        }
        // 3. 校验交易条目索引
        TradingShop shop = getShopByIndices(groupIndex, shopIndex);
        return entryIndex >= 0 && entryIndex < shop.getTransactionCount();
    }

    // ------------------------------ 元素添加（仅保留3个核心便捷方法）------------------------------
    /** 添加新的商店组，返回组索引 */
    public int addShopGroup(@NotNull String groupName, @Nullable String unlockCondition,
                            @Nullable IGuiTexture texture1, @Nullable IGuiTexture texture2) {
        Objects.requireNonNull(groupName, "Group name cannot be null");
        TradingShopGroup newGroup = new TradingShopGroup(groupName, unlockCondition, texture1, texture2);
        shopGroups.add(newGroup);
        return shopGroups.size() - 1;
    }

    /** 向指定组添加商店，返回商店在组内的索引 */
    public int addShopByGroupIndex(int groupIndex, @NotNull String shopName,
                                   @Nullable String unlockCondition, @Nullable IGuiTexture texture) {
        Objects.requireNonNull(shopName, "Shop name cannot be null");
        TradingShopGroup group = getShopGroup(groupIndex);
        checkNotNull(group, "Shop Group", groupIndex, getGroupCount() - 1);
        return group.addShop(shopName, unlockCondition, texture);
    }

    /** 向指定商店添加交易条目 */
    public void addTransactionEntryByIndices(int groupIndex, int shopIndex, @Nullable TransactionEntry entry) {
        if (entry == null) return;
        TradingShop shop = getShopByIndices(groupIndex, shopIndex);
        shop.addTransactionEntry(entry);
    }

    // ------------------------------ 元素获取（仅保留3个核心索引获取方法）------------------------------
    /** 通过索引获取商店组（索引无效返回null） */
    @Nullable
    public TradingShopGroup getShopGroup(int index) {
        return isIndexValid(index, getGroupCount()) ? shopGroups.get(index) : null;
    }

    /** 通过「组索引+商店索引」获取商店（索引无效抛异常） */
    @NotNull
    public TradingShop getShopByIndices(int groupIndex, int shopIndex) {
        TradingShopGroup group = getShopGroup(groupIndex);
        checkNotNull(group, "Shop Group", groupIndex, getGroupCount() - 1);

        TradingShop shop = group.getShop(shopIndex);
        checkNotNull(shop, "Shop", shopIndex, group.getShopCount() - 1);
        return shop;
    }

    /** 通过「组索引+商店索引+条目索引」获取交易条目（索引无效抛异常） */
    @NotNull
    public TransactionEntry getTransactionEntryByIndices(int groupIndex, int shopIndex, int entryIndex) {
        TradingShop shop = getShopByIndices(groupIndex, shopIndex);

        TransactionEntry entry = shop.getTransactionEntry(entryIndex);
        checkNotNull(entry, "Transaction Entry", entryIndex, shop.getTransactionCount() - 1);
        return entry;
    }

    // ------------------------------ 内部类：交易商店组（仅保留核心方法）------------------------------
    @Getter
    public class TradingShopGroup {

        private final String name;
        private final String unlockCondition;
        private final IGuiTexture texture1;
        private final IGuiTexture texture2;
        private final List<TradingShop> shops = Collections.synchronizedList(new ArrayList<>());

        private TradingShopGroup(@NotNull String name, @Nullable String unlockCondition,
                                 @Nullable IGuiTexture texture1, @Nullable IGuiTexture texture2) {
            this.name = name;
            this.unlockCondition = unlockCondition;
            this.texture1 = texture1;
            this.texture2 = texture2;
        }

        /** 向本组添加商店，返回商店索引 */
        public int addShop(@NotNull String shopName, @Nullable String unlockCondition, @Nullable IGuiTexture texture) {
            Objects.requireNonNull(shopName, "Shop name cannot be null");
            TradingShop newShop = new TradingShop(shopName, unlockCondition, texture);
            shops.add(newShop);
            return shops.size() - 1;
        }

        /** 获取本组内商店数量 */
        public int getShopCount() {
            return shops.size();
        }

        /** 通过索引获取本组内商店（索引无效返回null） */
        @Nullable
        public TradingShop getShop(int index) {
            return isIndexValid(index, getShopCount()) ? shops.get(index) : null;
        }
    }

    // ------------------------------ 内部类：交易商店（仅保留核心方法）------------------------------
    @Getter
    public class TradingShop {

        private final String name;
        private final String unlockCondition;
        private final IGuiTexture texture;
        private final List<TransactionEntry> transactionEntries = Collections.synchronizedList(new ArrayList<>());

        private TradingShop(@NotNull String name, @Nullable String unlockCondition, @Nullable IGuiTexture texture) {
            this.name = name;
            this.unlockCondition = unlockCondition;
            this.texture = texture;
        }

        /** 向本商店添加交易条目 */
        public void addTransactionEntry(@Nullable TransactionEntry entry) {
            if (entry != null) transactionEntries.add(entry);
        }

        /** 获取本商店内交易条目数量 */
        public int getTransactionCount() {
            return transactionEntries.size();
        }

        /** 通过索引获取本商店内交易条目（索引无效返回null） */
        @Nullable
        public TransactionEntry getTransactionEntry(int index) {
            return isIndexValid(index, getTransactionCount()) ? transactionEntries.get(index) : null;
        }
    }
}

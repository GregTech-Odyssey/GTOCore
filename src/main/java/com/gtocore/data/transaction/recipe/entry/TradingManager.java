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
 * 交易管理器（线程安全单例），用于层级化管理交易商店组、商店及交易条目。
 */
public class TradingManager {

    // 线程安全单例（静态内部类模式，兼顾懒加载和线程安全）
    private static class SingletonHolder {

        private static final TradingManager INSTANCE = new TradingManager();
    }

    /**
     * 获取全局唯一实例
     * 
     * @return 交易管理器单例
     */
    public static TradingManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    // 容器使用线程安全列表（兼顾性能和并发安全性）
    private final List<TradingShopGroup> shopGroups = Collections.synchronizedList(new ArrayList<>());

    // 私有构造函数，禁止外部实例化
    private TradingManager() {}

    // ------------------------------ 数量查询方法（统一归类）------------------------------

    /**
     * 获取商店组的总数量（0个int参数）
     * 
     * @return 商店组总数
     */
    public int getGroupCount() {
        return shopGroups.size();
    }

    /**
     * 获取指定组内的商店数量（1个int参数）
     * 
     * @param groupIndex 商店组索引（0开始）
     * @return 该组内商店总数
     * @throws IndexOutOfBoundsException 索引越界时抛出（含最大有效索引提示）
     */
    public int getShopCount(int groupIndex) {
        TradingShopGroup group = getShopGroup(groupIndex);
        checkNotNull(group, "Shop Group", groupIndex, getGroupCount() - 1);
        return group.getShopCount();
    }

    /**
     * 获取指定商店内的交易条目数量（2个int参数）
     * 
     * @param groupIndex 商店组索引（0开始）
     * @param shopIndex  商店在组内的索引（0开始）
     * @return 该商店内交易条目总数
     * @throws IndexOutOfBoundsException 索引越界时抛出（含最大有效索引提示）
     */
    public int getTransactionCount(int groupIndex, int shopIndex) {
        TradingShop shop = getShopByIndices(groupIndex, shopIndex);
        return shop.getTransactionCount();
    }

    // ------------------------------ 元素添加方法（统一归类）------------------------------

    /**
     * 添加新的商店组
     * 
     * @param groupName       组名称（非空）
     * @param unlockCondition 解锁条件（可为null）
     * @param texture1        纹理1（可为null）
     * @param texture2        纹理2（可为null）
     * @return 新组的索引（0开始）
     */
    public int addShopGroup(@NotNull String groupName, @Nullable String unlockCondition,
                            @Nullable IGuiTexture texture1, @Nullable IGuiTexture texture2) {
        Objects.requireNonNull(groupName, "Group name cannot be null");
        TradingShopGroup newGroup = new TradingShopGroup(groupName, unlockCondition, texture1, texture2);
        shopGroups.add(newGroup);
        return shopGroups.size() - 1;
    }

    /**
     * 直接向指定组添加商店（便捷方法，无需手动获取组实例）
     * 
     * @param groupIndex      商店组索引（0开始）
     * @param shopName        商店名称（非空）
     * @param unlockCondition 解锁条件（可为null）
     * @param texture         商店纹理（可为null）
     * @return 新商店在该组的索引（0开始）
     * @throws IndexOutOfBoundsException 组索引越界时抛出
     */
    public int addShopByGroupIndex(int groupIndex, @NotNull String shopName,
                                   @Nullable String unlockCondition, @Nullable IGuiTexture texture) {
        Objects.requireNonNull(shopName, "Shop name cannot be null");
        TradingShopGroup group = getShopGroup(groupIndex);
        checkNotNull(group, "Shop Group", groupIndex, getGroupCount() - 1);
        return group.addShop(shopName, unlockCondition, texture);
    }

    /**
     * 直接向指定商店添加交易条目
     * 
     * @param groupIndex 商店组索引（0开始）
     * @param shopIndex  商店在组内的索引（0开始）
     * @param entry      交易条目（null会被忽略）
     * @throws IndexOutOfBoundsException 索引越界时抛出
     */
    public void addTransactionEntryByIndices(int groupIndex, int shopIndex, @Nullable TransactionEntry entry) {
        if (entry == null) return;
        TradingShop shop = getShopByIndices(groupIndex, shopIndex);
        shop.addTransactionEntry(entry);
    }

    // ------------------------------ 元素获取方法（统一归类）------------------------------

    /**
     * 获取所有商店组的不可修改列表（防止外部篡改）
     * 
     * @return 不可修改的商店组列表
     */
    public List<TradingShopGroup> getShopGroups() {
        return Collections.unmodifiableList(shopGroups);
    }

    /**
     * 根据索引获取商店组（索引越界返回null）
     * 
     * @param index 商店组索引（0开始）
     * @return 对应的商店组，索引越界返回null
     */
    @Nullable
    public TradingShopGroup getShopGroup(int index) {
        if (isIndexValid(index, getGroupCount())) {
            return shopGroups.get(index);
        }
        return null;
    }

    /**
     * 通过组索引+商店索引直接获取商店（索引越界抛出异常）
     * 
     * @param groupIndex 商店组索引（0开始）
     * @param shopIndex  商店在组内的索引（0开始）
     * @return 对应的商店（非null）
     * @throws IndexOutOfBoundsException 索引越界时抛出
     */
    @NotNull
    public TradingShop getShopByIndices(int groupIndex, int shopIndex) {
        TradingShopGroup group = getShopGroup(groupIndex);
        checkNotNull(group, "Shop Group", groupIndex, getGroupCount() - 1);

        TradingShop shop = group.getShop(shopIndex);
        checkNotNull(shop, "Shop", shopIndex, group.getShopCount() - 1, groupIndex);
        return shop;
    }

    /**
     * 通过组索引+商店索引+条目索引直接获取交易条目（索引越界抛出异常）
     * 
     * @param groupIndex 商店组索引（0开始）
     * @param shopIndex  商店在组内的索引（0开始）
     * @param entryIndex 交易条目在商店内的索引（0开始）
     * @return 对应的交易条目（非null）
     * @throws IndexOutOfBoundsException 索引越界时抛出
     */
    @NotNull
    public TransactionEntry getTransactionEntryByIndices(int groupIndex, int shopIndex, int entryIndex) {
        TradingShop shop = getShopByIndices(groupIndex, shopIndex);
        TransactionEntry entry = shop.getTransactionEntry(entryIndex);
        checkNotNull(entry, "Transaction Entry", entryIndex, shop.getTransactionCount() - 1, groupIndex, shopIndex);
        return entry;
    }

    // ------------------------------ 内部工具方法（封装重复逻辑）------------------------------

    /**
     * 校验索引是否有效（0 <= index < size）
     * 
     * @param index 要校验的索引
     * @param size  容器大小
     * @return 有效返回true，否则false
     */
    private boolean isIndexValid(int index, int size) {
        return index >= 0 && index < size;
    }

    /**
     * 校验对象非空，为空则抛出带详细信息的索引越界异常
     * 
     * @param obj           要校验的对象
     * @param targetName    目标对象名称（如"Shop Group"）
     * @param index         无效索引
     * @param maxValidIndex 最大有效索引
     * @param parentIndices 父层级索引（可选，用于多级提示）
     */
    private void checkNotNull(@Nullable Object obj, String targetName, int index, int maxValidIndex, int... parentIndices) {
        if (obj == null) {
            StringBuilder parentMsg = new StringBuilder();
            if (parentIndices.length > 0) {
                parentMsg.append(" (in group ");
                for (int i = 0; i < parentIndices.length; i++) {
                    parentMsg.append(parentIndices[i]);
                    if (i < parentIndices.length - 1) parentMsg.append(", shop ");
                }
                parentMsg.append(")");
            }
            throw new IndexOutOfBoundsException(
                    String.format("%s index out of bounds: %d%s. Max valid index: %d",
                            targetName, index, parentMsg, maxValidIndex));
        }
    }

    // ------------------------------ 内部类：交易商店组 ------------------------------

    /**
     * 交易商店组，管理同组内的多个交易商店
     */
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

        /**
         * 添加新的交易商店到本组
         * 
         * @param shopName        商店名称（非空）
         * @param unlockCondition 解锁条件（可为null）
         * @param texture         商店纹理（可为null）
         * @return 新商店在本组的索引（0开始）
         */
        public int addShop(@NotNull String shopName, @Nullable String unlockCondition, @Nullable IGuiTexture texture) {
            Objects.requireNonNull(shopName, "Shop name cannot be null");
            TradingShop newShop = new TradingShop(shopName, unlockCondition, texture);
            shops.add(newShop);
            return shops.size() - 1;
        }

        /**
         * 获取本组内商店数量（替代原getSize，命名更清晰）
         * 
         * @return 商店数量
         */
        public int getShopCount() {
            return shops.size();
        }

        /**
         * 根据索引获取本组内的商店（索引越界返回null）
         * 
         * @param index 商店索引（0开始）
         * @return 对应的商店，索引越界返回null
         */
        @Nullable
        public TradingShop getShop(int index) {
            if (isIndexValid(index, getShopCount())) {
                return shops.get(index);
            }
            return null;
        }
    }

    // ------------------------------ 内部类：交易商店 ------------------------------

    /**
     * 交易商店，管理具体的交易条目
     */
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

        /**
         * 添加交易条目到本商店
         * 
         * @param entry 交易条目（null会被忽略）
         */
        public void addTransactionEntry(@Nullable TransactionEntry entry) {
            if (entry != null) {
                transactionEntries.add(entry);
            }
        }

        /**
         * 获取本商店内交易条目数量（替代原getSize，命名更清晰）
         * 
         * @return 交易条目数量
         */
        public int getTransactionCount() {
            return transactionEntries.size();
        }

        /**
         * 根据索引获取本商店内的交易条目（索引越界返回null）
         * 
         * @param index 交易条目索引（0开始）
         * @return 对应的交易条目，索引越界返回null
         */
        @Nullable
        public TransactionEntry getTransactionEntry(int index) {
            if (isIndexValid(index, getTransactionCount())) {
                return transactionEntries.get(index);
            }
            return null;
        }
    }
}

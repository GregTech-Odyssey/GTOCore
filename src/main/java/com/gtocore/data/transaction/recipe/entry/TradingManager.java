package com.gtocore.data.transaction.recipe.entry;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 交易管理器，用于层级化管理交易商店组和商店。
 */
public class TradingManager {

    private final List<TradingShopGroup> shopGroups = new ArrayList<>();

    /**
     * 添加一个新的商店组。
     *
     * @param groupName       组名称
     * @param unlockCondition 解锁条件
     * @param texture1        纹理1
     * @param texture2        纹理2
     * @return 新添加商店组在列表中的索引
     */
    public int addShopGroup(@NotNull String groupName, @Nullable String unlockCondition,
                            @Nullable IGuiTexture texture1, @Nullable IGuiTexture texture2) {
        TradingShopGroup newGroup = new TradingShopGroup(groupName, unlockCondition, texture1, texture2);
        shopGroups.add(newGroup);
        return shopGroups.size() - 1;
    }

    /**
     * 获取所有商店组的不可修改列表。
     *
     * @return 不可修改的商店组列表
     */
    public List<TradingShopGroup> getShopGroups() {
        return Collections.unmodifiableList(shopGroups);
    }

    /**
     * 根据索引获取商店组。
     *
     * @param index 组的索引
     * @return 对应的商店组，如果索引越界则返回 null
     */
    @Nullable
    public TradingShopGroup getShopGroup(int index) {
        if (index >= 0 && index < shopGroups.size()) {
            return shopGroups.get(index);
        }
        return null;
    }

    /**
     * 通过「商店组索引」和「组内商店索引」直接添加交易条目
     */
    public void addTransactionEntryByIndices(int groupIndex, int shopIndex, @Nullable TransactionEntry entry) {
        if (entry == null) return;
        TradingShopGroup targetGroup = getShopGroup(groupIndex);
        if (targetGroup == null) {
            throw new IndexOutOfBoundsException("Shop Group index out of bounds: " + groupIndex);
        }
        targetGroup.addTransactionEntry(shopIndex, entry);
    }

    /**
     * 交易商店组，用于管理同组的多个交易商店。
     */
    @Getter
    public class TradingShopGroup {

        private final String name;
        private final String unlockCondition;
        private final IGuiTexture texture1;
        private final IGuiTexture texture2;
        private final List<TradingShop> shops = new ArrayList<>();

        private TradingShopGroup(@NotNull String name, @Nullable String unlockCondition,
                                 @Nullable IGuiTexture texture1, @Nullable IGuiTexture texture2) {
            this.name = name;
            this.unlockCondition = unlockCondition;
            this.texture1 = texture1;
            this.texture2 = texture2;
        }

        /**
         * 向本组添加一个新的交易商店。
         *
         * @param shopName        商店名称
         * @param unlockCondition 解锁条件
         * @param texture         纹理
         * @return 新添加商店在本组列表中的索引
         */
        public int addShop(@NotNull String shopName, @Nullable String unlockCondition, @Nullable IGuiTexture texture) {
            TradingShop newShop = new TradingShop(shopName, unlockCondition, texture);
            shops.add(newShop);
            return shops.size() - 1;
        }

        /**
         * 获取本组内所有商店的不可修改列表。
         *
         * @return 不可修改的商店列表
         */
        public List<TradingShop> getShops() {
            return Collections.unmodifiableList(shops);
        }

        /**
         * 根据索引获取本组内的商店。
         *
         * @param index 商店的索引
         * @return 对应的商店，如果索引越界则返回 null
         */
        @Nullable
        public TradingShop getShop(int index) {
            if (index >= 0 && index < shops.size()) {
                return shops.get(index);
            }
            return null;
        }

        /**
         * 向指定索引的商店添加交易条目。
         *
         * @param shopIndex 商店在组内的索引
         * @param entry     要添加的交易条目
         * @throws IndexOutOfBoundsException 如果索引越界
         */
        public void addTransactionEntry(int shopIndex, @Nullable TransactionEntry entry) {
            if (entry == null) return;
            TradingShop shop = getShop(shopIndex);
            if (shop == null) {
                throw new IndexOutOfBoundsException("Shop index out of bounds: " + shopIndex);
            }
            shop.addTransactionEntry(entry);
        }
    }

    /**
     * 交易商店，用于管理具体的交易条目。
     */
    @Getter
    public class TradingShop {

        private final String name;
        private final String unlockCondition;
        private final IGuiTexture texture;
        private final List<TransactionEntry> transactionEntries = new ArrayList<>();

        private TradingShop(@NotNull String name, @Nullable String unlockCondition, @Nullable IGuiTexture texture) {
            this.name = name;
            this.unlockCondition = unlockCondition;
            this.texture = texture;
        }

        /**
         * 向商店添加一个交易条目。
         *
         * @param entry 要添加的交易条目，null 会被忽略
         */
        public void addTransactionEntry(@Nullable TransactionEntry entry) {
            if (entry == null) return;
            transactionEntries.add(entry);
        }

        /**
         * 获取商店内所有交易条目的不可修改列表。
         *
         * @return 不可修改的交易条目列表
         */
        public List<TransactionEntry> getTransactionEntries() {
            return Collections.unmodifiableList(transactionEntries);
        }
    }
}

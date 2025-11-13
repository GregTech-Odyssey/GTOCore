package com.gtocore.data.transaction.recipe.entry;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import lombok.Getter;

import java.util.*;

/**
 * 交易条目的容器
 */
public class TransactionGroup {

    public List<TradingShopGroup> AllTradingShopGroup = new ArrayList<>();

    @Getter
    public static class TradingShopGroup {

        private final String shopName;
        private final String unlockCondition;
        private final IGuiTexture texture1;
        private final IGuiTexture texture2;
        private final List<TradingShop> tradingShops;

        public TradingShopGroup(String groupName, String unlockCondition, IGuiTexture texture1, IGuiTexture texture2) {
            this.shopName = groupName;
            this.unlockCondition = unlockCondition;
            this.texture1 = texture1;
            this.texture2 = texture2;
            this.tradingShops = new ArrayList<>();
        }

        // 向组内商店添加交易条目
        public void addTradingShop(TradingShop shop) {
            tradingShops.add(shop);
        }

        // 向组内商店添加交易条目
        public void addTransactionEntry(int shop, TransactionEntry entry) {
            if (entry != null) tradingShops.get(shop).addTransactionEntry(entry);
        }

        // 获取不可修改的交易条目列表
        public List<TransactionEntry> getTransactionEntries(int shop) {
            return tradingShops.get(shop).getTransactionEntries();
        }
    }

    @Getter
    public static class TradingShop {

        private final String shopName;
        private final String unlockCondition;
        private final IGuiTexture texture;
        private final List<TransactionEntry> transactionEntries;

        public TradingShop(String groupName, String unlockCondition, IGuiTexture texture) {
            this.shopName = groupName;
            this.unlockCondition = unlockCondition;
            this.texture = texture;
            this.transactionEntries = new ArrayList<>();
        }

        // 向组内添加交易条目
        public void addTransactionEntry(TransactionEntry entry) {
            if (entry != null) transactionEntries.add(entry);
        }

        // 获取不可修改的交易条目列表
        public List<TransactionEntry> getTransactionEntries() {
            return Collections.unmodifiableList(transactionEntries);
        }
    }
}

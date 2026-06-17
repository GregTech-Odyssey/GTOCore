package com.gtocore.data.transaction.data.trade;

import com.gtocore.data.transaction.manager.TradeData;
import com.gtocore.data.transaction.manager.TradeEntry;
import com.gtocore.data.transaction.manager.UnlockManager;

import com.gtolib.api.GTOValues;
import com.gtolib.utils.WalletUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.common.data.GTMachines;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

import static com.gtocore.data.transaction.data.TradeLang.addTradeLang;

public final class UnlockTrade {

    public static final String SINGLE_TRANSACTION = "Single Transaction";

    public static final String UNLOCK_SHOP = "Unlock Shop";
    public static final String UNLOCK_TRADE = "Unlock Trade";
    public static final String UNLOCK_BASE = "Base";
    /// 包含 GTOValues.VNFR[] 中的电压

    public static final String GT_Values = "GT Values";

    public static void init() {
        for (int i = 0; i < 14; i++) {
            TradeEntry.TradeGroup.Builder tradeGroup = new TradeEntry.TradeGroup.Builder();
            tradeGroup.addItem(GTMachines.HULL[i].asStack());
            UnlockManager.INSTANCE.addTradeToEntry(addTradeLang("格雷电压", "GT Values"),
                    tradeEntry(new ResourceTexture("gtocore:textures/item/circuit/" + GTValues.VN[i].toLowerCase() + ".png"),
                            UNLOCK_TRADE, GTOValues.VNFR[i], tradeGroup));
        }
    }

    private static TradeEntry tradeEntry(String tagKey, String tagValue, TradeEntry.TradeGroup.Builder tradeGroup) {
        return tradeEntry(GuiTextures.LOCK, tagKey, tagValue, tradeGroup);
    }

    private static TradeEntry tradeEntry(IGuiTexture texture, String tagKey, String tagValue, TradeEntry.TradeGroup.Builder tradeGroup) {
        return new TradeEntry.Builder()
                .texture(texture)
                .addDescription(Component.translatable(addTradeLang("解锁交易条件 %s - %s", "Unlock transaction conditions %s - %s"), tagKey, tagValue))
                .input(tradeGroup)
                .preCheck((a, b) -> checkUnlock(a, b, tagKey, tagValue))
                .onExecute((a, b, c) -> performUnlock(a, b, c, tagKey, tagValue))
                .build();
    }

    // 前置检查逻辑：检查标签是否添加
    private static int checkUnlock(TradeData data, TradeEntry entry, String tagKey, String tagValue) {
        Level level = data.level();
        ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
        if (!WalletUtils.containsTagValueInWallet(data.uuid(), serverLevel, tagKey, tagValue)) return 1;
        return 0;
    }

    // 执行逻辑：添加标签
    private static void performUnlock(TradeData data, TradeEntry entry, int multiplier, String tagKey, String tagValue) {
        Level level = data.level();
        ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
        WalletUtils.addTagToWallet(data.uuid(), serverLevel, tagKey, tagValue);
    }
}

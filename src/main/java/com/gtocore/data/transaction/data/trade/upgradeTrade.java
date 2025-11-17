package com.gtocore.data.transaction.data.trade;

import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.machines.GTAEMachines;
import com.gtocore.data.transaction.common.TradingStationMachine;
import com.gtocore.data.transaction.manager.TradeEntry;
import com.gtocore.data.transaction.manager.UpgradeOrUnlockManager;

import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;

import java.util.List;
import java.util.Map;

import static com.gtocore.data.transaction.data.TradeLang.UNLOCK_BASE;

public class upgradeTrade {

    public static void init() {
        UpgradeOrUnlockManager upgradeOrUnlockManager = UpgradeOrUnlockManager.getInstance();

        // 机器升级
        for (int i = 2; i <= TradingStationMachine.getMaxLevel("currentLevel"); i++) {
            upgradeOrUnlockManager.addTradeToEntry("currentLevel", createUpgradeCurrentLevelTrade(i));
        }

        // 功能升级
        for (String key : TradingStationMachine.UpgradeKeys.ALL_KEYS) {
            for (int i = 1; i <= TradingStationMachine.getMaxLevel(key); i++) {
                upgradeOrUnlockManager.addTradeToEntry(key, createUpgradeToTierByStepTrade(key, i));
            }
        }
    }

    public static TradeEntry createUpgradeToTierByStepTrade(String upgrade, int tier) {
        return new TradeEntry.Builder()
                .texture(new ItemStackTexture(UPGRADE_TEXTURE_MAP.get(upgrade)))
                .description(List.of(Component.translatable("gtocore.trading_station.upgrade." + upgrade)
                        .append(Component.literal(" " + (tier - 1) + "→" + tier))))
                .unlockCondition(UNLOCK_BASE)
                .inputItem(new ItemStack(Items.EMERALD, 10))
                .preCheck((machine, entry) -> {
                    if (machine.getUpgradeLevel(upgrade) == tier - 1 && machine.canSetUpgradeLevel(upgrade, tier)) return 1;
                    return 0;
                })
                .onExecute((machine, multiplier, entry) -> {
                    machine.doUpgradeLevel(upgrade, tier);
                    Level level = machine.getLevel();
                    if (level != null) {
                        level.playSound(null, machine.getPos(), SoundEvents.CHEST_OPEN, SoundSource.PLAYERS, 0.8F, 1.0F);
                    }
                })
                .build();
    }

    public static TradeEntry createUpgradeCurrentLevelTrade(int tier) {
        return new TradeEntry.Builder()
                .texture(new ItemStackTexture(UPGRADE_TEXTURE_MAP.get("currentLevel")))
                .description(List.of(Component.translatable("gtocore.trading_station.upgrade.currentLevel")
                        .append(Component.literal(" " + (tier - 1) + "→" + tier))))
                .unlockCondition(UNLOCK_BASE)
                .inputItem(new ItemStack(Items.EMERALD, 10))
                .preCheck((machine, entry) -> {
                    if (machine.getUpgradeLevel("currentLevel") == tier - 1 && machine.canUpgradeToNextLevel()) return 1;
                    return 0;
                })
                .onExecute((machine, multiplier, entry) -> {
                    machine.doUpgradeMachineLevel();
                    Level level = machine.getLevel();
                    if (level != null) {
                        level.playSound(null, machine.getPos(), SoundEvents.CHEST_OPEN, SoundSource.PLAYERS, 0.8F, 1.0F);
                    }
                })
                .build();
    }

    private static final Map<String, Item> UPGRADE_TEXTURE_MAP = Map.of(
            "currentLevel", GTOMachines.TRADING_STATION.asItem(),
            TradingStationMachine.UpgradeKeys.FLUID_TANK, GTItems.FLUID_CELL.asItem(),
            TradingStationMachine.UpgradeKeys.AUTO_TRADE, GTItems.ROBOT_ARM_MV.asItem(),
            TradingStationMachine.UpgradeKeys.LUCKY_MERCHANT, RegistriesUtils.getItem("apotheosis:lucky_foot"),
            TradingStationMachine.UpgradeKeys.ME_INTERACTION, GTAEMachines.ME_STORAGE_ACCESS_HATCH.asItem(),
            TradingStationMachine.UpgradeKeys.PLAYER_TRADE, Items.PLAYER_HEAD,
            TradingStationMachine.UpgradeKeys.CAPACITY, Items.CHEST);
}

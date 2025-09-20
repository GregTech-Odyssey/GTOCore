package com.gtocore.integration.ae;

import net.minecraft.server.level.ServerPlayer;

import appeng.api.networking.crafting.CalculationStrategy;
import appeng.api.stacks.AEKey;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocator;
import appeng.menu.me.common.IClientRepo;
import appeng.menu.me.crafting.CraftAmountMenu;

public interface IConfirmLongMenu {

    void gtocore$confirmLong(long amount, boolean craftMissingAmount, boolean autoStart);

    interface IConfirmLongStartMenu {

        boolean gtocore$planLongJob(AEKey what, long amount, CalculationStrategy strategy);

        IClientRepo gtocore$getClientRepo();
    }

    static void open(ServerPlayer player, MenuLocator locator, AEKey whatToCraft, long initialAmount) {
        MenuOpener.open(CraftAmountMenu.TYPE, player, locator);

        if (player.containerMenu instanceof IConfirmLongMenu cca) {
            cca.gtocore$setWhatToCraftLong(whatToCraft, initialAmount);
            player.containerMenu.broadcastChanges();
        }
    }

    void gtocore$setWhatToCraftLong(AEKey whatToCraft, long initialAmount);
}

package com.gtocore.integration.ae.hooks;

import net.minecraft.server.level.ServerPlayer;

import appeng.api.stacks.KeyCounter;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocator;
import appeng.menu.me.crafting.CraftAmountMenu;

public interface ICraftAmountMenu {

    /**
     * Opens the craft amount screen for the given player.
     * This method allows multiple items to be crafted as a composite request.
     */
    static void open(ServerPlayer player, MenuLocator locator, KeyCounter whatToCraft, long initialAmount) {
        if (whatToCraft.isEmpty()) {
            return;
        }
        MenuOpener.open(CraftAmountMenu.TYPE, player, locator);

        if (player.containerMenu instanceof ICraftAmountMenu cca) {
            cca.gto$setWhatToCraft(whatToCraft, initialAmount);
            cca.broadcastChanges();
        }
    }

    void broadcastChanges();

    void gto$setWhatToCraft(KeyCounter whatToCraft, long initialAmount);
}

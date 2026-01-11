package com.gtocore.integration.construction_wand;

import com.gtolib.api.player.IEnhancedPlayer;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import de.mari_023.ae2wtlib.wct.CraftingTerminalHandler;
import thetadev.constructionwand.api.IContainerHandler;

public class AEHandler implements IContainerHandler {

    @Override
    public boolean matches(Player player, ItemStack itemStack, ItemStack itemStack1) {
        return CraftingTerminalHandler.getCraftingTerminalHandler(player).getCraftingTerminal().is(itemStack1.getItem());
    }

    @Override
    public int countItems(Player player, ItemStack itemStack, ItemStack itemStack1) {
        if (player instanceof ServerPlayer sp) {
            var ae = IEnhancedPlayer.getMEStorageService(sp);
            if (ae != null) {
                return Math.toIntExact(ae.getInventory().extract(AEItemKey.of(itemStack1), Integer.MAX_VALUE, Actionable.SIMULATE, IActionSource.ofPlayer(sp)));
            }
        }
        return 0;
    }

    @Override
    public int useItems(Player player, ItemStack itemStack, ItemStack itemStack1, int i) {
        if (player instanceof ServerPlayer sp) {
            var ae = IEnhancedPlayer.getMEStorageService(sp);
            if (ae != null) {
                return i - Math.toIntExact(ae.getInventory().extract(AEItemKey.of(itemStack1), i, Actionable.MODULATE, IActionSource.ofPlayer(sp)));
            }
        }
        return i;
    }
}

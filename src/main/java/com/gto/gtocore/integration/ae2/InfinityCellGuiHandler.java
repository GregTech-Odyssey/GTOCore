package com.gto.gtocore.integration.ae2;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import appeng.api.implementations.blockentities.IChestOrDrive;
import appeng.api.storage.cells.ICellGuiHandler;
import appeng.api.storage.cells.ICellHandler;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.menu.me.common.MEStorageMenu;

public class InfinityCellGuiHandler implements ICellGuiHandler {

    @Override
    public boolean isSpecializedFor(ItemStack cell) {
        return cell.getItem() instanceof InfinityCell;
    }

    @Override
    public void openChestGui(Player player, IChestOrDrive chest, ICellHandler cellHandler, ItemStack cell) {
        MenuOpener.open(MEStorageMenu.TYPE, player,
                MenuLocators.forBlockEntity(((BlockEntity) chest)));
    }
}

package com.gtocore.integration.ae.wtlib;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import appeng.api.implementations.menuobjects.ItemMenuHost;
import appeng.api.storage.ITerminalHost;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.client.gui.style.ScreenStyle;
import appeng.core.AEConfig;
import appeng.items.tools.powered.WirelessTerminalItem;
import appeng.menu.ISubMenu;
import appeng.menu.SlotSemantics;
import appeng.menu.ToolboxMenu;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.slot.RestrictedInputSlot;

import com.almostreliable.merequester.client.RequesterTerminalScreen;
import com.almostreliable.merequester.terminal.RequesterTerminalMenu;
import de.mari_023.ae2wtlib.AE2wtlib;
import de.mari_023.ae2wtlib.AE2wtlibSlotSemantics;
import de.mari_023.ae2wtlib.terminal.IUniversalWirelessTerminalItem;
import de.mari_023.ae2wtlib.terminal.WTMenuHost;
import de.mari_023.ae2wtlib.wct.WCTMenuHost;
import de.mari_023.ae2wtlib.wut.CycleTerminalButton;
import de.mari_023.ae2wtlib.wut.IUniversalTerminalCapable;
import de.mari_023.ae2wtlib.wut.ItemWUT;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class WRTMenu extends RequesterTerminalMenu {

    public static final String ID = "requester_wt_menu";
    public static final MenuType<WRTMenu> TYPE = MenuTypeBuilder.create(WRTMenu::new, WRTHost.class).build(ID);

    private final WRTHost watMenuHost;
    private final ToolboxMenu toolboxMenu;

    WRTMenu(MenuType<?> menuType, int id, Inventory playerInventory, WRTHost host) {
        super(menuType, id, playerInventory, host);
        watMenuHost = host;
        toolboxMenu = new ToolboxMenu(this);

        IUpgradeInventory upgrades = watMenuHost.getUpgrades();
        for (int i = 0; i < upgrades.size(); i++) {
            var slot = new RestrictedInputSlot(RestrictedInputSlot.PlacableItemType.UPGRADES, upgrades, i);
            slot.setNotDraggable();
            addSlot(slot, SlotSemantics.UPGRADE);
        }
        addSlot(new RestrictedInputSlot(RestrictedInputSlot.PlacableItemType.QE_SINGULARITY,
                watMenuHost.getSubInventory(WCTMenuHost.INV_SINGULARITY), 0), AE2wtlibSlotSemantics.SINGULARITY);
    }

    @Override
    public void broadcastChanges() {
        toolboxMenu.tick();
        super.broadcastChanges();
    }

    public boolean isWUT() {
        return watMenuHost.getItemStack().getItem() instanceof ItemWUT;
    }

    public ITerminalHost getHost() {
        return watMenuHost;
    }

    public ToolboxMenu getToolbox() {
        return toolboxMenu;
    }

    public static class WRTHost extends WTMenuHost {

        public WRTHost(final Player ep, @Nullable Integer inventorySlot, final ItemStack is,
                       BiConsumer<Player, ISubMenu> returnToMainMenu) {
            super(ep, inventorySlot, is, returnToMainMenu);
            readFromNbt();
        }

        @Override
        public ItemStack getMainMenuIcon() {
            return new ItemStack(AE2wtlib.PATTERN_ACCESS_TERMINAL);
        }
    }

    public static class WRTScreen extends RequesterTerminalScreen<WRTMenu> implements IUniversalTerminalCapable {

        public WRTScreen(WRTMenu menu, Inventory playerInventory, Component name, ScreenStyle style) {
            super(menu, playerInventory, name, style);
            if (getMenu().isWUT())
                addToLeftToolbar(new CycleTerminalButton(btn -> cycleTerminal()));

            setSlotsHidden(SlotSemantics.UPGRADE, true);
            setSlotsHidden(AE2wtlibSlotSemantics.SINGULARITY, true);
        }

        @Override
        public void storeState() {}
    }

    public static class WRTItem extends WirelessTerminalItem implements IUniversalWirelessTerminalItem {

        public WRTItem(Properties p) {
            super(AEConfig.instance().getWirelessTerminalBattery(), p.stacksTo(1));
        }

        @Override
        public @NotNull MenuType<?> getMenuType(@NotNull ItemStack stack) {
            return TYPE;
        }

        @Override
        public MenuType<?> getMenuType() {
            return TYPE;
        }

        @Override
        public @Nullable ItemMenuHost getMenuHost(Player player, int inventorySlot, ItemStack stack, @Nullable BlockPos pos) {
            return new WRTHost(player, inventorySlot, stack, (p, sm) -> this.openFromInventory(p, inventorySlot, true));
        }
    }
}

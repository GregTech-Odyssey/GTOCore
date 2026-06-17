package com.gtocore.integration.ae.wtlib;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import appeng.api.config.YesNo;
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
import appeng.util.ConfigManager;

import gto_ae.api.config.ExtendedSettings;
import gto_ae.client.gui.me.facility_management.FacilityManagementScreen;
import gto_ae.helpers.facility_management.FacilityManagement;
import gto_ae.helpers.facility_management.IFacilityManagementHost;
import gto_ae.helpers.facility_management.IO;
import gto_ae.helpers.facility_management.WorkingStatus;
import gto_ae.menu.implementations.FacilityManagementMenu;

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

public class WFTMenu extends FacilityManagementMenu {

    public static final String ID = "wft_menu";
    public static final MenuType<WFTMenu> TYPE = MenuTypeBuilder.create(WFTMenu::new, WFTHost.class).build(ID);

    private final WFTHost watMenuHost;
    private final ToolboxMenu toolboxMenu;

    WFTMenu(MenuType<?> menuType, int id, Inventory playerInventory, WFTHost host) {
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

    public static class WFTHost extends WTMenuHost implements IFacilityManagementHost {

        private final ConfigManager configManager = new ConfigManager(this::saveChanges);

        private final FacilityManagement logic = new FacilityManagement(this);

        final Player player;

        public WFTHost(final Player ep, @Nullable Integer inventorySlot, final ItemStack is,
                       BiConsumer<Player, ISubMenu> returnToMainMenu) {
            super(ep, inventorySlot, is, returnToMainMenu);
            player = ep;
            this.configManager.registerSetting(ExtendedSettings.WORKING_STATUS_SETTING, WorkingStatus.NONE);
            this.configManager.registerSetting(ExtendedSettings.FILTER_MODE, IO.NONE);
            this.configManager.registerSetting(ExtendedSettings.HAS_CPU_TASK, YesNo.UNDECIDED);
            readFromNbt();
        }

        @Override
        public ItemStack getMainMenuIcon() {
            return new ItemStack(AE2wtlib.PATTERN_ACCESS_TERMINAL);
        }

        @Override
        public void saveChanges() {
            super.saveChanges();
            var tag = new CompoundTag();
            configManager.writeToNBT(tag);
            getItemStack().getOrCreateTag().put("wft_cm", tag);
            var tag1 = new CompoundTag();
            logic.serializeNBT(tag1);
            getItemStack().getOrCreateTag().put("wft_logic", tag1);
        }

        @Override
        public void readFromNbt() {
            super.readFromNbt();
            var tag = getItemStack().getOrCreateTag().getCompound("wft_cm");
            if (!tag.isEmpty()) configManager.readFromNBT(tag);
            var tag1 = getItemStack().getOrCreateTag().getCompound("wft_logic");
            if (!tag1.isEmpty()) logic.deserializeNBT(tag1);
        }

        @Override
        public FacilityManagement getLogic() {
            return logic;
        }

        @Override
        public Level getLevel() {
            return player.level();
        }

        @Override
        public void markForSave() {
            saveChanges();
        }

        @Override
        public IO getIOModeFilter() {
            return configManager.getSetting(ExtendedSettings.FILTER_MODE);
        }

        @Override
        public void setFilterMode(IO newMode) {
            configManager.putSetting(ExtendedSettings.FILTER_MODE, newMode);
        }

        @Override
        public WorkingStatus getWorkingFilter() {
            return configManager.getSetting(ExtendedSettings.WORKING_STATUS_SETTING);
        }

        @Override
        public void setWorkingFilter(WorkingStatus newStatus) {
            configManager.putSetting(ExtendedSettings.WORKING_STATUS_SETTING, newStatus);
        }

        @Override
        public YesNo getCraftingJobsFilter() {
            return configManager.getSetting(ExtendedSettings.HAS_CPU_TASK);
        }

        @Override
        public void setFilterCraftingJobs(YesNo newValue) {
            configManager.putSetting(ExtendedSettings.HAS_CPU_TASK, newValue);
        }
    }

    public static class WFTScreen extends FacilityManagementScreen<WFTMenu> implements IUniversalTerminalCapable {

        public WFTScreen(WFTMenu menu, Inventory playerInventory, Component name, ScreenStyle style) {
            super(menu, playerInventory, name, style);
            if (getMenu().isWUT()) {
                addToLeftToolbar(new CycleTerminalButton(btn -> cycleTerminal()));
            }

            setSlotsHidden(SlotSemantics.UPGRADE, true);
            setSlotsHidden(AE2wtlibSlotSemantics.SINGULARITY, true);
        }

        @Override
        public void storeState() {}
    }

    public static class WFTItem extends WirelessTerminalItem implements IUniversalWirelessTerminalItem {

        public WFTItem(Properties p) {
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
            return new WFTHost(player, inventorySlot, stack, (p, sm) -> this.openFromInventory(p, inventorySlot, true));
        }
    }
}

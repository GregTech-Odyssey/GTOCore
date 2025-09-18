package com.gtocore.mixin.ae2.menu;

import com.gtocore.common.network.ClientMessage;
import com.gtocore.integration.ae.IConfirmLongMenu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

import appeng.api.networking.crafting.CalculationStrategy;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.ISubMenuHost;
import appeng.menu.AEBaseMenu;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.me.crafting.CraftAmountMenu;
import appeng.menu.me.crafting.CraftConfirmMenu;
import appeng.menu.slot.AppEngSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(CraftAmountMenu.class)
public abstract class CraftAmountMenuMixin extends AEBaseMenu implements ISubMenu, IConfirmLongMenu {

    public CraftAmountMenuMixin(MenuType<?> menuType, int id, Inventory playerInventory, Object host) {
        super(menuType, id, playerInventory, host);
    }

    @Shadow(remap = false)
    public abstract Level getLevel();

    @Shadow(remap = false)
    private AEKey whatToCraft;

    @Shadow(remap = false)
    @Final
    private ISubMenuHost host;

    @Shadow(remap = false)
    @Final
    private AppEngSlot craftingItem;

    @Override
    public void gtocore$confirmLong(long amount, boolean craftMissingAmount, boolean autoStart) {
        if (this.getLevel().isClientSide()) {
            long finalAmount = amount;
            ClientMessage.send("craftLongTask", buf -> {
                buf.writeVarLong(finalAmount);
                buf.writeBoolean(craftMissingAmount);
                buf.writeBoolean(autoStart);
            });
            return;
        }

        if (this.whatToCraft == null) {
            return;
        }

        if (craftMissingAmount) {
            var host = getActionHost();
            if (host != null) {
                var node = host.getActionableNode();
                if (node != null) {
                    var storage = node.getGrid().getStorageService();
                    var existingAmount = (int) Math.min(storage.getCachedInventory().get(whatToCraft),
                            Integer.MAX_VALUE);
                    if (existingAmount > amount) {
                        amount = 0;
                    } else {
                        amount -= existingAmount;
                    }
                }
            }
        }

        var locator = getLocator();
        if (locator != null) {
            var player = getPlayer();
            if (amount > 0) {
                MenuOpener.open(CraftConfirmMenu.TYPE, player, locator);

                if (player.containerMenu instanceof CraftConfirmMenu ccc) {
                    ccc.setAutoStart(autoStart);
                    ((IConfirmLongStartMenu) ccc).gtocore$planLongJob(
                            whatToCraft,
                            amount,
                            CalculationStrategy.REPORT_MISSING_ITEMS);
                    broadcastChanges();
                }
            } else {
                // When the amount to craft is 0, return to the previous menu without crafting
                this.host.returnToMainMenu(player, this);
            }
        }
    }

    @Override
    public void gtocore$setWhatToCraftLong(AEKey whatToCraft, long initialAmount) {
        this.whatToCraft = Objects.requireNonNull(whatToCraft, "whatToCraft");
        this.craftingItem.set(GenericStack.wrapInItemStack(whatToCraft, initialAmount));
    }
}

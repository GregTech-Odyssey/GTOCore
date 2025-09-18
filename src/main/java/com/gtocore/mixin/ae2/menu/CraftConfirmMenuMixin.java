package com.gtocore.mixin.ae2.menu;

import com.gtocore.integration.ae.IConfirmLongMenu;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.CalculationStrategy;
import appeng.api.networking.crafting.ICraftingPlan;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.menu.AEBaseMenu;
import appeng.menu.locator.MenuLocator;
import appeng.menu.me.crafting.CraftConfirmMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.Future;

@Mixin(CraftConfirmMenu.class)
public abstract class CraftConfirmMenuMixin extends AEBaseMenu implements IConfirmLongMenu.IConfirmLongStartMenu {

    @Shadow(remap = false)
    private Future<ICraftingPlan> job;

    @Shadow(remap = false)
    private ICraftingPlan result;
    @Unique
    private long gto$amount;

    public CraftConfirmMenuMixin(MenuType<?> menuType, int id, Inventory playerInventory, Object host) {
        super(menuType, id, playerInventory, host);
    }

    @Shadow(remap = false)
    public abstract void clearError();

    @Shadow(remap = false)
    private AEKey whatToCraft;

    @Shadow(remap = false)
    protected abstract IGrid getGrid();

    @Shadow(remap = false)
    protected abstract IActionSource getActionSrc();

    @Override
    public boolean gtocore$planLongJob(AEKey what, long amount, CalculationStrategy strategy) {
        if (this.job != null) {
            this.job.cancel(true);
        }
        this.result = null;
        this.clearError();

        this.whatToCraft = what;
        this.gto$amount = amount;

        var player = getPlayer();

        var grid = getGrid();
        if (grid == null) {
            return false;
        }

        var cg = grid.getCraftingService();

        this.job = cg.beginCraftingCalculation(
                player.level(),
                this::getActionSrc,
                what,
                amount,
                strategy);
        return true;
    }

    @Redirect(method = "goBack", at = @At(value = "INVOKE", target = "Lappeng/menu/me/crafting/CraftAmountMenu;open(Lnet/minecraft/server/level/ServerPlayer;Lappeng/menu/locator/MenuLocator;Lappeng/api/stacks/AEKey;I)V"), remap = false)
    private void redirectOpenCraftAmountMenuAE2(ServerPlayer player, MenuLocator locator, AEKey whatToCraft, int ignored) {
        IConfirmLongMenu.open(player, locator, whatToCraft, gto$amount);
    }
}

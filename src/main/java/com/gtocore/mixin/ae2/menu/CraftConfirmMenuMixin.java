package com.gtocore.mixin.ae2.menu;

import com.gtocore.integration.ae.hooks.IConfirmLongMenu;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.TypeFilter;
import appeng.api.config.ViewItems;
import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.CalculationStrategy;
import appeng.api.networking.crafting.ICraftingPlan;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.ISubMenuHost;
import appeng.client.gui.me.common.Repo;
import appeng.client.gui.widgets.ISortSource;
import appeng.core.sync.packets.MEInventoryUpdatePacket;
import appeng.menu.AEBaseMenu;
import appeng.menu.locator.MenuLocator;
import appeng.menu.me.common.IClientRepo;
import appeng.menu.me.common.IncrementalUpdateHelper;
import appeng.menu.me.crafting.CraftConfirmMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
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

    @Unique
    private IClientRepo gto$repo;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void onConstructed(int id, Inventory ip, ISubMenuHost host, CallbackInfo ci) {
        this.gto$repo = new Repo(() -> 0, new ISortSource() {

            @Override
            public SortOrder getSortBy() {
                return SortOrder.AMOUNT;
            }

            @Override
            public SortDir getSortDir() {
                return SortDir.ASCENDING;
            }

            @Override
            public ViewItems getSortDisplay() {
                return ViewItems.ALL;
            }

            @Override
            public TypeFilter getTypeFilter() {
                return TypeFilter.ALL;
            }
        });
    }

    @Override
    public IClientRepo gtocore$getClientRepo() {
        return gto$repo;
    }

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

    @Redirect(method = "replan", at = @At(value = "INVOKE", target = "Lappeng/menu/me/crafting/CraftConfirmMenu;planJob(Lappeng/api/stacks/AEKey;ILappeng/api/networking/crafting/CalculationStrategy;)Z"), remap = false)
    private boolean redirectPlanJobAE2(CraftConfirmMenu instance, AEKey whatToCraft, int ignored, CalculationStrategy strategy) {
        return gtocore$planLongJob(whatToCraft, gto$amount, strategy);
    }

    @Redirect(method = "startJob", at = @At(value = "INVOKE", target = "Lappeng/api/networking/crafting/ICraftingPlan;simulation()Z", remap = false), remap = false)
    private boolean ignoreCantCraft(ICraftingPlan instance) {
        return false;
    }

    @Unique
    private boolean gto$sent = false;

    @Inject(method = "broadcastChanges", at = @At("RETURN"))
    private void onBroadcastChanges(CallbackInfo ci) {
        if (!gto$sent) {
            var builder = MEInventoryUpdatePacket.builder(containerId, true);
            builder.addFull(new IncrementalUpdateHelper(), getGrid().getStorageService().getInventory().getAvailableStacks(), Set.of(), new KeyCounter());
            builder.buildAndSend(this::sendPacketToClient);
            gto$sent = true;
        }
    }
}

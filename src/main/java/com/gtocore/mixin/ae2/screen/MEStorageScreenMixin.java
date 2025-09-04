package com.gtocore.mixin.ae2.screen;

import com.gtolib.api.ae2.gui.FixedRepoSlot;
import com.gtolib.api.ae2.gui.hooks.IFixedRepoSlotHandlingScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.me.common.Repo;
import appeng.client.gui.style.ScreenStyle;
import appeng.menu.me.common.GridInventoryEntry;
import appeng.menu.me.common.MEStorageMenu;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MEStorageScreen.class)
public abstract class MEStorageScreenMixin<C extends MEStorageMenu> extends AEBaseScreen<C> implements IFixedRepoSlotHandlingScreen {

    protected MEStorageScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Shadow(remap = false)
    protected abstract void handleGridInventoryEntryMouseClick(@Nullable GridInventoryEntry entry, int mouseButton, ClickType clickType);

    @Shadow(remap = false)
    protected abstract void renderGridInventoryEntryTooltip(GuiGraphics guiGraphics, GridInventoryEntry entry, int x, int y);

    @Final
    @Shadow(remap = false)
    protected Repo repo;

    @Shadow(remap = false)
    protected abstract boolean isViewOnlyCraftable();

    @Override
    public boolean gtolib$isViewOnlyCraftable() {
        return isViewOnlyCraftable();
    }

    @Unique
    @Override
    public Repo gtolib$getRepo() {
        return repo;
    }

    @Override
    public MEStorageMenu gtolib$getMenu() {
        return ((MEStorageScreen<?>) (Object) this).getMenu();
    }

    @Override
    public void gtolib$handleGridInventoryEntryMouseClick(@Nullable GridInventoryEntry entry, int mouseButton, ClickType clickType) {
        this.handleGridInventoryEntryMouseClick(entry, mouseButton, clickType);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void modifyMouseClicked(double xCoord, double yCoord, int btn, CallbackInfoReturnable<Boolean> cir) {
        if (gtolib$mouseClicked(xCoord, yCoord, btn)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    public void modifyMouseScrolled(double x, double y, double wheelDelta, CallbackInfoReturnable<Boolean> cir) {
        if (IFixedRepoSlotHandlingScreen.gtolib$mouseScrolled(this, x, y, 0, wheelDelta)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "slotClicked", at = @At("HEAD"))
    public void modifySlotClicked(Slot slot, int slotIdx, int mouseButton, ClickType clickType, CallbackInfo ci) {
        gtolib$slotClicked(slot, slotIdx, mouseButton, clickType);
    }

    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    public void modifyRenderTooltip(GuiGraphics guiGraphics, int x, int y, CallbackInfo ci) {
        if (IFixedRepoSlotHandlingScreen.gtolib$renderTooltip(this, guiGraphics, x, y)) ci.cancel();
    }

    @Inject(method = "renderSlot", at = @At("HEAD"), cancellable = true)
    public void modifyRenderSlot(GuiGraphics guiGraphics, Slot s, CallbackInfo ci) {
        if (IFixedRepoSlotHandlingScreen.gtolib$renderSlot(this, guiGraphics, s)) ci.cancel();
    }

    @Unique
    public boolean gtolib$mouseClicked(double xCoord, double yCoord, int btn) {
        if (Minecraft.getInstance().options.keyPickItem.matchesMouse(btn)) {
            Slot slot = findSlot(xCoord, yCoord);
            if (slot instanceof FixedRepoSlot repoSlot && repoSlot.isCraftable()) {
                gtolib$handleGridInventoryEntryMouseClick(repoSlot.getEntry(), btn, ClickType.CLONE);
                return true;
            }
        }

        return false;
    }

    @Unique
    public void gtolib$slotClicked(Slot slot, int slotIdx, int mouseButton, ClickType clickType) {
        if (slot instanceof FixedRepoSlot repoSlot) {
            gtolib$handleGridInventoryEntryMouseClick(repoSlot.getEntry(), mouseButton, clickType);
        }
    }

    @Override
    public Slot gtolib$hoveredSlot() {
        return hoveredSlot;
    }

    @Override
    public void gtolib$renderGridInventoryEntryTooltip(GuiGraphics guiGraphics, GridInventoryEntry entry, int x, int y) {
        renderGridInventoryEntryTooltip(guiGraphics, entry, x, y);
    }
}

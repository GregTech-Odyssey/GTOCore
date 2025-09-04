package com.gtocore.mixin.ae2.menu;

import com.gtolib.api.ae2.gui.hooks.ITagSelectableMenu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.menu.implementations.UpgradeableMenu;
import appeng.menu.slot.FakeSlot;
import appeng.util.ConfigInventory;
import com.glodblock.github.extendedae.common.parts.PartTagStorageBus;
import com.glodblock.github.extendedae.container.ContainerTagStorageBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ContainerTagStorageBus.class)
public abstract class ContainerTagStorageBusMixin extends UpgradeableMenu<PartTagStorageBus> implements ITagSelectableMenu {

    @Unique
    private FakeSlot gtolib$whitelistSlot;

    protected ContainerTagStorageBusMixin(MenuType<?> menuType, int id, Inventory ip, PartTagStorageBus host) {
        super(menuType, id, ip, host);
    }

    @Unique
    @Override
    public FakeSlot gtolib$getBlacklistSlot() {
        return gtolib$blacklistSlot;
    }

    @Unique
    @Override
    public FakeSlot gtolib$getWhitelistSlot() {
        return gtolib$whitelistSlot;
    }

    @Unique
    private FakeSlot gtolib$blacklistSlot;
    @Unique
    private Consumer<AEKey> gtolib$whitelistChanged;
    @Unique
    private Consumer<AEKey> gtolib$blacklistChanged;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void onInit(int id, Inventory ip, PartTagStorageBus te, CallbackInfo ci) {
        ConfigInventory gtolib$whitelistInventory = ConfigInventory.configTypes(1, this::gtolib$onWhitelistChanged);
        ConfigInventory gtolib$blacklistInventory = ConfigInventory.configTypes(1, this::gtolib$onBlacklistChanged);
        gtolib$whitelistSlot = new FakeSlot(gtolib$whitelistInventory.createMenuWrapper(), 0);
        gtolib$blacklistSlot = new FakeSlot(gtolib$blacklistInventory.createMenuWrapper(), 0);
        var menu = (ContainerTagStorageBus) (Object) this;
        addSlot(gtolib$whitelistSlot);
        addSlot(gtolib$blacklistSlot);
    }

    @Unique
    private void gtolib$onBlacklistChanged() {
        if (gtolib$blacklistChanged != null) {
            var gStack = GenericStack.fromItemStack(gtolib$blacklistSlot.getItem());
            if (gStack == null) {
                gtolib$blacklistChanged.accept(null);
            } else gtolib$blacklistChanged.accept(gStack.what());
        }
    }

    @Unique
    private void gtolib$onWhitelistChanged() {
        if (gtolib$whitelistChanged != null) {
            var gStack = GenericStack.fromItemStack(gtolib$whitelistSlot.getItem());
            if (gStack == null) {
                gtolib$whitelistChanged.accept(null);
            } else gtolib$whitelistChanged.accept(gStack.what());
        }
    }

    @Unique
    @Override
    public void gtolib$setWhitelistChanged(Consumer<AEKey> consumer) {
        this.gtolib$whitelistChanged = consumer;
    }

    @Unique
    @Override
    public void gtolib$setBlacklistChanged(Consumer<AEKey> consumer) {
        this.gtolib$blacklistChanged = consumer;
    }
}

package com.gtocore.mixin.ae2.screen;

import com.gtolib.api.ae2.gui.hooks.ITagSelectableMenu;
import com.gtolib.api.ae2.gui.hooks.ITagSelectableScreen;
import com.gtolib.api.ae2.gui.widgets.AEListBox;
import com.gtolib.api.ae2.gui.widgets.AESlotWidget;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.api.stacks.AEKey;
import appeng.client.gui.implementations.UpgradeableScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.menu.slot.FakeSlot;
import com.glodblock.github.extendedae.client.gui.GuiTagStorageBus;
import com.glodblock.github.extendedae.client.gui.widget.MultilineTextFieldWidget;
import com.glodblock.github.extendedae.container.ContainerTagStorageBus;
import com.lowdragmc.lowdraglib.core.mixins.accessor.SlotAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiTagStorageBus.class)
public abstract class GuiTagStorageBusMixin extends UpgradeableScreen<ContainerTagStorageBus> implements ITagSelectableScreen {

    @Unique
    private AEListBox gtolib$listBox;
    @Final
    @Shadow(remap = false)
    private MultilineTextFieldWidget filterInputs;
    @Final
    @Shadow(remap = false)
    private MultilineTextFieldWidget filterInputs2;
    @Unique
    private FakeSlot gtolib$whitelistSlot;
    @Unique
    private FakeSlot gtolib$blacklistSlot;

    protected GuiTagStorageBusMixin(ContainerTagStorageBus menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void onInit(ContainerTagStorageBus menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) {
        var screen = (GuiTagStorageBus) (Object) this;
        ITagSelectableMenu hookedMenu = (ITagSelectableMenu) menu;
        gtolib$listBox = new AEListBox(screen);
        gtolib$listBox.setVisible(false);
        gtolib$listBox.setCatchScrollbar(false);
        gtolib$whitelistSlot = hookedMenu.gtolib$getWhitelistSlot();
        gtolib$blacklistSlot = hookedMenu.gtolib$getBlacklistSlot();
        ((SlotAccessor) gtolib$whitelistSlot).setX(140);
        ((SlotAccessor) gtolib$whitelistSlot).setY(40);
        ((SlotAccessor) gtolib$blacklistSlot).setX(140);
        ((SlotAccessor) gtolib$blacklistSlot).setY(60);
        hookedMenu.gtolib$setBlacklistChanged(key -> gtolib$onChangedCallback(false, key));
        hookedMenu.gtolib$setWhitelistChanged(key -> gtolib$onChangedCallback(true, key));
        widgets.add("gtolib$listBox", gtolib$listBox);
        var whiteSlot = new AESlotWidget(gtolib$whitelistSlot, screen);
        whiteSlot.setTooltip(Component.translatable("gtocore.part.extendae.tag_filter.whitelist.tooltip"));
        widgets.add("gtolib$whitelistSlot", whiteSlot);
        var blackSlot = new AESlotWidget(gtolib$blacklistSlot, screen);
        blackSlot.setTooltip(Component.translatable("gtocore.part.extendae.tag_filter.blacklist.tooltip"));
        widgets.add("gtolib$blacklistSlot", blackSlot);
    }

    @Unique
    @Override
    public void gtolib$onChangedCallback(boolean isWhitelist, @Nullable AEKey key) {
        ITagSelectableScreen.gtolib$onChangedCallback(
                isWhitelist, key, gtolib$listBox, filterInputs, filterInputs2, gtolib$whitelistSlot, gtolib$blacklistSlot);
    }
}

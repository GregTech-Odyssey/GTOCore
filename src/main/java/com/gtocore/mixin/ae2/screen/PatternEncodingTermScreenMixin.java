package com.gtocore.mixin.ae2.screen;

import com.gtocore.integration.ae.client.AESearchPatternProviderListBox;
import com.gtocore.integration.ae.hooks.IExtendedPatternEncodingTerm;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ActionButton;
import appeng.menu.me.items.PatternEncodingTermMenu;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatternEncodingTermScreen.class)
public class PatternEncodingTermScreenMixin<C extends PatternEncodingTermMenu> extends MEStorageScreen<C> implements IExtendedPatternEncodingTerm {

    @Shadow(remap = false)
    @Final
    private ActionButton encodeBtn;
    @Unique
    private AESearchPatternProviderListBox gto$listBox;

    public PatternEncodingTermScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtolib$onInit(PatternEncodingTermMenu menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) {
        gto$listBox = new AESearchPatternProviderListBox(this);
        gto$listBox.setVisible(false);
        gto$listBox.setCatchScrollbar(false);
        widgets.add("gto$listBox", gto$listBox);
    }

    @Override
    public ActionButton gto$getEncodeButton() {
        return encodeBtn;
    }

    @Override
    public AESearchPatternProviderListBox gto$getPatternDestDisplay() {
        return gto$listBox;
    }

    @Override
    public IExtendedPatternEncodingTerm.Menu gto$getMenu() {
        return (Menu) menu;
    }
}

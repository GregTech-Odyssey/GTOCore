package com.gto.gtocore.mixin.ae2.gui;

import com.gto.gtocore.integration.ae2.BlockingType;
import com.gto.gtocore.integration.ae2.GTOSettings;
import com.gto.gtocore.integration.ae2.IPatternProviderMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ServerSettingToggleButton;
import appeng.client.gui.widgets.SettingToggleButton;
import appeng.menu.implementations.PatternProviderMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatternProviderScreen.class)
public abstract class PatternProviderScreenMixin<C extends PatternProviderMenu> extends AEBaseScreen<C> {

    @Unique
    private SettingToggleButton<BlockingType> gtocore$enhancedblockingmodebutton;

    protected PatternProviderScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(PatternProviderMenu menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) {
        this.gtocore$enhancedblockingmodebutton = new ServerSettingToggleButton<>(GTOSettings.BLOCKING_TYPE, BlockingType.ALL);
        this.addToLeftToolbar(this.gtocore$enhancedblockingmodebutton);
    }

    @Inject(method = "updateBeforeRender", at = @At("TAIL"), remap = false)
    private void updateBeforeRender(CallbackInfo ci) {
        this.gtocore$enhancedblockingmodebutton.set(((IPatternProviderMenu) menu).gtocore$getBlocking());
    }
}

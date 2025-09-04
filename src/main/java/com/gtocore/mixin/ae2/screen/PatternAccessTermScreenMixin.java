package com.gtocore.mixin.ae2.screen;

import com.gtolib.api.ae2.GTOSettings;
import com.gtolib.api.ae2.IPatternAccessTermMenu;
import com.gtolib.api.ae2.ShowMolecularAssembler;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.patternaccess.PatternAccessTermScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ServerSettingToggleButton;
import appeng.menu.implementations.PatternAccessTermMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatternAccessTermScreen.class)
public abstract class PatternAccessTermScreenMixin<C extends PatternAccessTermMenu> extends AEBaseScreen<C> {

    @Unique
    private ServerSettingToggleButton<ShowMolecularAssembler> gtolib$showMolecularAssembler;

    protected PatternAccessTermScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void onInit(PatternAccessTermMenu menu, Inventory playerInventory,
                        Component title, ScreenStyle style, CallbackInfo ci) {
        gtolib$showMolecularAssembler = new ServerSettingToggleButton<>(GTOSettings.TERMINAL_SHOW_MOLECULAR_ASSEMBLERS,
                ShowMolecularAssembler.ALL);
        this.addToLeftToolbar(gtolib$showMolecularAssembler);
    }

    @Inject(method = "updateBeforeRender", at = @At("TAIL"), remap = false)
    private void updateBeforeRender(CallbackInfo ci) {
        this.gtolib$showMolecularAssembler.set(((IPatternAccessTermMenu) this.getMenu()).gtolib$getShownMolecularAssemblers());
    }
}

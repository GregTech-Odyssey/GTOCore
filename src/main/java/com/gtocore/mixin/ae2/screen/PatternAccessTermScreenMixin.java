package com.gtocore.mixin.ae2.screen;

import com.gtocore.integration.ae.PatternEncoderStats;
import com.gtocore.integration.ae.client.PatternEncoderStatsButton;
import com.gtocore.integration.ae.client.PatternEncoderStatsScreen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.patternaccess.PatternAccessTermScreen;
import appeng.client.gui.me.patternaccess.PatternContainerRecord;
import appeng.client.gui.style.ScreenStyle;
import appeng.menu.implementations.PatternAccessTermMenu;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(PatternAccessTermScreen.class)
public abstract class PatternAccessTermScreenMixin<C extends PatternAccessTermMenu> extends AEBaseScreen<C> {

    @Shadow(remap = false)
    @Final
    private HashMap<Long, PatternContainerRecord> byId;

    protected PatternAccessTermScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtocore$addPatternStatsButton(PatternAccessTermMenu menu, Inventory playerInventory,
                                               Component title, ScreenStyle style, CallbackInfo ci) {
        addToLeftToolbar(new PatternEncoderStatsButton(btn -> switchToScreen(
                new PatternEncoderStatsScreen<>(this, PatternEncoderStats.collect(byId.values())))));
    }
}

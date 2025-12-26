package com.gtocore.mixin.ae2.gui;

import com.gtolib.api.ae2.gui.hooks.SlotsPositionMap;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.style.SlotPosition;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mixin(ScreenStyle.class)
public class ScreenStyleMixin {

    @Mutable
    @Final
    @Shadow(remap = false)
    private Map<String, SlotPosition> slots;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(CallbackInfo ci) {
        slots = new SlotsPositionMap(slots);
    }
}

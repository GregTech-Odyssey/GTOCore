package com.gtocore.mixin.ae2.gui;

import com.gtolib.api.ae2.gui.hooks.SlotsPositionMap;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.style.SlotPosition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mixin(ScreenStyle.class)
public class ScreenStyleMixin {

    @Final
    @Shadow(remap = false)
    private Map<String, SlotPosition> slots;

    @Inject(method = "getSlots", at = @At("HEAD"), remap = false, cancellable = true)
    private void gtolib$getSlots(CallbackInfoReturnable<Map<String, SlotPosition>> cir) {
        cir.setReturnValue(new SlotsPositionMap(slots));
    }
}

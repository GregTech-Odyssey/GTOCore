package com.gto.gtocore.mixin.mc;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.WaterFluid;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WaterFluid.class, priority = 0)
public class WaterFluidMixin {

    @Inject(method = "canConvertToSource", at = @At("HEAD"), cancellable = true)
    private void canConvertToSource(Level level, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}

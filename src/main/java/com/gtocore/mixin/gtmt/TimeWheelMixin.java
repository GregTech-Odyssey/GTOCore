package com.gtocore.mixin.gtmt;

import com.hepdd.gtmthings.api.misc.Slot;
import com.hepdd.gtmthings.api.misc.TimeWheel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayDeque;

@Mixin(TimeWheel.class)
public class TimeWheelMixin {

    @Shadow(remap = false)
    private int firstUpdateTick;
    @Shadow(remap = false)
    private int lastUpdateTick;
    @Shadow(remap = false)
    int slotResolution;
    @Final
    @Shadow(remap = false)
    private int slotNum;
    @Shadow(remap = false)
    ArrayDeque<Slot> slots;
    @Shadow(remap = false)
    private BigInteger sum;

    @Inject(method = "getAvgByTick", cancellable = true, remap = false, at = @At("HEAD"))
    private void getAvgByTick(CallbackInfoReturnable<BigDecimal> cir) {
        if (this.lastUpdateTick - this.firstUpdateTick < this.slotResolution * this.slotNum) {
            cir.setReturnValue((new BigDecimal(this.sum)).divide(BigDecimal.valueOf(this.lastUpdateTick - this.firstUpdateTick + 1), RoundingMode.HALF_UP));
        } else {
            try {
                cir.setReturnValue(this.slots.isEmpty() ? BigDecimal.ZERO : (new BigDecimal(this.sum)).divide(BigDecimal.valueOf((long) this.slots.size() * (long) this.slotResolution + (long) (this.lastUpdateTick % this.slotResolution) - (long) this.slotResolution), RoundingMode.HALF_UP));
            } catch (ArithmeticException e) {
                cir.setReturnValue(BigDecimal.ZERO);
            }
        }
    }
}

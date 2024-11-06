package com.gto.gtocore.mixin.gtm.cover;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.common.cover.PumpCover;

import net.minecraft.core.Direction;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PumpCover.class)
public class PumpCoverMixin extends CoverBehavior {

    @Mutable
    @Shadow(remap = false)
    @Final
    public long maxMilliBucketsPerTick;

    public PumpCoverMixin(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    public void PumpCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide, int tier, CallbackInfo ci) {
        this.maxMilliBucketsPerTick = tier == 0 ? Integer.MAX_VALUE :
                64 * (long) Math.pow(4, Math.min(tier - 1, GTValues.IV));
    }
}

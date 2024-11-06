package com.gto.gtocore.mixin.gtm.cover;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.common.cover.ConveyorCover;

import net.minecraft.core.Direction;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConveyorCover.class)
public class ConveyorCoverMixin extends CoverBehavior {

    @Shadow(remap = false)
    @Final
    public int tier;

    @Mutable
    @Shadow(remap = false)
    @Final
    public int maxItemTransferRate;

    public ConveyorCoverMixin(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    public void ConveyorCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide, int tier, CallbackInfo ci) {
        this.maxItemTransferRate = tier == 0 ? Integer.MAX_VALUE : 2 * (int) Math.pow(4, Math.min(tier, GTValues.LuV));
    }
}

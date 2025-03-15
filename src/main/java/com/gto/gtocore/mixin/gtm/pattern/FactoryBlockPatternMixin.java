package com.gto.gtocore.mixin.gtm.pattern;

import com.gto.gtocore.api.pattern.OptimizedBlockPattern;

import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FactoryBlockPattern.class)
public class FactoryBlockPatternMixin {

    @Shadow(remap = false)
    @Final
    private RelativeDirection[] structureDir;

    @Inject(method = "build", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/pattern/BlockPattern;<init>([[[Lcom/gregtechceu/gtceu/api/pattern/TraceabilityPredicate;[Lcom/gregtechceu/gtceu/api/pattern/util/RelativeDirection;[[I[I)V"), remap = false, cancellable = true)
    public void build(CallbackInfoReturnable<BlockPattern> cir, @Local TraceabilityPredicate[][][] predicate, @Local int[][] aisleRepetitions, @Local int[] centerOffset) {
        cir.setReturnValue(new OptimizedBlockPattern(predicate, structureDir, aisleRepetitions, centerOffset));
    }
}

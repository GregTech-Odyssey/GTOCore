package com.gto.gtocore.mixin.ae2.logic;

import com.gregtechceu.gtceu.common.data.GTItems;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.helpers.patternprovider.PatternProviderLogic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

/**
 * @author EasterFG on 2024/10/13
 */
@Mixin(PatternProviderLogic.class)
public class PatternProviderLogicMixin {

    @Shadow(remap = false)
    @Final
    private Set<AEKey> patternInputs;

    @Inject(method = "updatePatterns", at = @At("TAIL"), remap = false)
    public void updatePatternsHook(CallbackInfo ci) {
        patternInputs.remove(AEItemKey.of(GTItems.INTEGRATED_CIRCUIT.get()));
    }
}

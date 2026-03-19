package com.gtocore.mixin.ae2.eae;

import com.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.GTCEu;

import appeng.helpers.patternprovider.PatternProviderLogic;

import com.glodblock.github.extendedae.common.tileentities.TileExPatternProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileExPatternProvider.class)
public abstract class TileExPatternProviderMixin {

    @Unique
    private TileExPatternProvider exae$getSelf() {
        return (TileExPatternProvider) (Object) this;
    }

    @Inject(
            method = "createLogic",
            at = @At("HEAD"),
            cancellable = true,
            remap = false)
    private void modifyCreateLogic(CallbackInfoReturnable<PatternProviderLogic> cir) {
        if (GTCEu.isDev() && GTOConfig.INSTANCE.devMode.exPatternSize > 36) {
            cir.setReturnValue(new PatternProviderLogic(exae$getSelf().getMainNode(), exae$getSelf(), GTOConfig.INSTANCE.devMode.exPatternSize));
        }
    }
}

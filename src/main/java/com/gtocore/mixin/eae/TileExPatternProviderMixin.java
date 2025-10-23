package com.gtocore.mixin.eae;

import com.gtocore.config.GTOConfig;

import com.gtolib.GTOCore;

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
        // This method is intentionally left empty to prevent the original logic from executing.
        // The logic is handled in the XModUtils class.
        if (!GTOCore.isExpert() && GTOConfig.INSTANCE.exPatternSize > 36) {
            cir.setReturnValue(new PatternProviderLogic(exae$getSelf().getMainNode(), exae$getSelf(), GTOConfig.INSTANCE.exPatternSize));
        }
    }
}

package com.gtocore.mixin.apoth;

import com.gtocore.integration.apotheosis.BedrockMineRestoreAffix;
import com.gtocore.integration.apotheosis.FTBUltimineAffix;

import com.gtolib.GTOCore;

import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.placebo.reload.DynamicRegistry;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@org.spongepowered.asm.mixin.Mixin(dev.shadowsoffire.apotheosis.adventure.affix.AffixRegistry.class)
public abstract class AffixRegistry extends DynamicRegistry<Affix> {

    public AffixRegistry(Logger logger, String path, boolean synced, boolean subtypes) {
        super(logger, path, synced, subtypes);
    }

    @Inject(method = "registerBuiltinCodecs", at = @At("TAIL"), remap = false)
    private void hookRegisterBuiltinCodecs(CallbackInfo ci) {
        this.registerCodec(GTOCore.id("ftbu"), FTBUltimineAffix.CODEC);
        this.registerCodec(GTOCore.id("bedrockore"), BedrockMineRestoreAffix.CODEC);
    }
}

package com.gto.gtocore.mixin.gtm.registry;

import com.gto.gtocore.api.registries.GTORegistration;
import com.gto.gtocore.common.data.GTOCreativeModeTabs;

import com.gregtechceu.gtceu.common.data.GTFluids;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GTFluids.class)
public class GTFluidsMixin {

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/registry/registrate/GTRegistrate;creativeModeTab(Ljava/util/function/Supplier;)V"), remap = false)
    private static void setFluidCreativeModeTab(CallbackInfo ci) {
        GTORegistration.REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_MATERIAL_FLUID);
    }
}

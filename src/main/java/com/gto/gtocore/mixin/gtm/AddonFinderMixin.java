package com.gto.gtocore.mixin.gtm;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.api.addon.AddonFinder;
import com.gregtechceu.gtceu.api.addon.IGTAddon;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AddonFinder.class)
public class AddonFinderMixin {

    @Shadow(remap = false)
    protected static List<IGTAddon> cache;

    @Inject(method = "getAddons", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/addon/AddonFinder;getInstances(Ljava/lang/Class;Ljava/lang/Class;)Ljava/util/List;"), remap = false, cancellable = true)
    private static void getAddons(CallbackInfoReturnable<List<IGTAddon>> cir) {
        cache = List.of(GTOCore.GTADDON);
        cir.setReturnValue(cache);
    }

    @Inject(method = "getAddon", at = @At("RETURN"), remap = false, cancellable = true)
    private static void getAddon(String modId, CallbackInfoReturnable<IGTAddon> cir) {
        cir.setReturnValue(modId.equals(GTOCore.MOD_ID) ? GTOCore.GTADDON : null);
    }
}

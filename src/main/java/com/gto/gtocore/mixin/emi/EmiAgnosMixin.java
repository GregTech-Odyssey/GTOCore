package com.gto.gtocore.mixin.emi;

import com.gto.gtocore.integration.emi.GTEMIPlugin;

import com.google.common.collect.Lists;
import dev.emi.emi.platform.EmiAgnos;
import dev.emi.emi.registry.EmiPluginContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EmiAgnos.class)
public class EmiAgnosMixin {

    @Inject(method = "getPlugins", at = @At("HEAD"), remap = false, cancellable = true)
    private static void getPlugins(CallbackInfoReturnable<List<EmiPluginContainer>> cir) {
        List<EmiPluginContainer> containers = Lists.newArrayList();
        GTEMIPlugin.addEMIPlugin(containers);
        cir.setReturnValue(containers);
    }
}

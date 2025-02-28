package com.gto.gtocore.mixin.emi;

import com.gto.gtocore.integration.emi.GTEMIPlugin;

import com.gregtechceu.gtceu.GTCEu;

import com.google.common.collect.Lists;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.emi.EMIPlugin;
import dev.emi.emi.VanillaPlugin;
import dev.emi.emi.platform.EmiAgnos;
import dev.emi.emi.registry.EmiPluginContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.integration.emi.BotaniaEmiPlugin;

import java.util.List;

@Mixin(EmiAgnos.class)
public class EmiAgnosMixin {

    @Inject(method = "getPlugins", at = @At("HEAD"), remap = false, cancellable = true)
    private static void getPlugins(CallbackInfoReturnable<List<EmiPluginContainer>> cir) {
        List<EmiPluginContainer> containers = Lists.newArrayList();
        containers.add(new EmiPluginContainer(new VanillaPlugin(), "emi"));
        containers.add(new EmiPluginContainer(new EMIPlugin(), LDLib.MOD_ID));
        containers.add(new EmiPluginContainer(new GTEMIPlugin(), GTCEu.MOD_ID));
        containers.add(new EmiPluginContainer(new BotaniaEmiPlugin(), BotaniaAPI.MODID));
        cir.setReturnValue(containers);
    }
}

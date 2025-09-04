package com.gtocore.mixin.gtm.registry;

import com.gtolib.api.registries.GTORegistration;

import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.registry.GTRegistration;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GTRegistration.class)
public final class GTRegistrationMixin {

    @Mutable
    @Shadow(remap = false)
    @Final
    public static GTRegistrate REGISTRATE;

    static {
        REGISTRATE = GTORegistration.GTM;
    }
}

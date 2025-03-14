package com.gto.gtocore.mixin.apotheosis;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;

import com.llamalad7.mixinextras.sugar.Local;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.spawn.enchantment.CapturingEnchant;
import dev.shadowsoffire.placebo.registry.RegistryEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Apotheosis.class)
public class ApotheosisMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void clinit(CallbackInfo ci) {
        Apotheosis.enableSpawner = false;
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(CallbackInfo ci, @Local IEventBus bus) {
        bus.addGenericListener(Enchantment.class, ApotheosisMixin::gtocore$enchants);
    }

    @Unique
    private static void gtocore$enchants(RegistryEvent.Register<Enchantment> e) {
        e.getRegistry().register(new CapturingEnchant(), "capturing");
    }
}

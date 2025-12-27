package com.gtocore.mixin.gtm;

import com.gregtechceu.gtceu.forge.ForgeCommonEventListener;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(ForgeCommonEventListener.class)
public final class ForgeCommonEventListenerMixin {

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/eventbus/api/IEventBus;addListener(Lnet/minecraftforge/eventbus/api/EventPriority;Ljava/util/function/Consumer;)V"), remap = false)
    private static void init(IEventBus instance, EventPriority eventPriority, Consumer tConsumer) {}
}

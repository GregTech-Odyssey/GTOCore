package com.gtocore.mixin.guideme;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import guideme.internal.GuideME;
import guideme.internal.network.OpenGuideRequest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(GuideME.class)
public class GuideMEMixin {

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/eventbus/api/IEventBus;addListener(Ljava/util/function/Consumer;)V"), remap = false)
    private void redirectEventBus(IEventBus instance, Consumer<?> tConsumer) {}

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/api/distmarker/Dist;isClient()Z"), remap = false)
    private boolean redirectIsClient(Dist instance) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private void handlePacket(OpenGuideRequest packet, Supplier<NetworkEvent.Context> contextSource) {}

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void sendPacket(PacketDistributor.PacketTarget target, OpenGuideRequest request) {}
}

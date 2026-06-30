package com.gtocore.mixin.mc.client;

import com.gtocore.utils.StxckUtil;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @Shadow
    public abstract ClientLevel getLevel();

    @Redirect(method = "handleTakeItemEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;removeEntity(ILnet/minecraft/world/entity/Entity$RemovalReason;)V", ordinal = 0))
    private void handleRemoveItemEntity(ClientLevel instance, int entityId, Entity.RemovalReason reason) {
        var entity = getLevel().getEntity(entityId);
        if (entity == null || StxckUtil.tryRefillItemStackOnEntityRemove(entity, reason)) return;
        getLevel().removeEntity(entityId, reason);
    }
}

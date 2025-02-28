package com.gto.gtocore.mixin.kubejs;

import com.gto.gtocore.common.network.ClientMessage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import dev.architectury.networking.NetworkManager;
import dev.latvian.mods.kubejs.net.SendDataFromClientMessage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SendDataFromClientMessage.class)
public class SendDataFromClientMessageMixin {

    @Shadow(remap = false)
    @Final
    private String channel;

    @Shadow(remap = false)
    @Final
    private CompoundTag data;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void handle(NetworkManager.PacketContext context) {
        if (!channel.isEmpty() && context.getPlayer() instanceof ServerPlayer serverPlayer) {
            ClientMessage.handle(channel, serverPlayer, data);
        }
    }
}
